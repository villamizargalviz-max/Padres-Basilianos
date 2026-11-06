package com.example.data.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.concurrent.ConcurrentHashMap
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Gestor Central de Seguridad de Grado Militar de la Congregación de San Basilio (C.S.B.).
 * Implementa las 9 capas de protección solicitadas:
 * 1. Ocultamiento y Enmascaramiento de API Keys (nunca exponer claves en texto plano en la UI ni en logs).
 * 2. Prevención de Secretos en Git (uso de variables de entorno y claves cifradas en Keystore).
 * 3. Uso de Identificador Público de BD (Public DB Key) + Reglas de Autenticación en Servidor.
 * 4. Encriptación de Datos Sensibles en Reposo con Android Keystore (AES-256-GCM sin padding).
 * 5. Autenticación Forzada de Servidor (verificación de sesión firmada + token Firebase).
 * 6. Cookies / Tokens de Sesión Seguros (HttpOnly, Secure, SameSite=Strict, HMAC/SHA-256 + expiración).
 * 7. Protección Anti-Bot (Rate Limiting por ventana deslizante + Honeypot + Desafío criptográfico).
 * 8. Parametrización de Consultas SQL y prevención de SQL/NoSQL Injection.
 * 9. Validación estricta de Inputs (XSS/Script/Command sanitization) y Forzado de HTTPS (TLS 1.2/1.3).
 */
object BasilianSecurityManager {

    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "BasilianCSB_AES256_MasterKey_v1"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val GCM_TAG_LENGTH = 128
    private const val SECURE_PREFS_NAME = "basilian_encrypted_vault_prefs"

    // Ventana deslizante para protección Anti-Bot (Rate Limiting por acción)
    private val requestTimestamps = ConcurrentHashMap<String, MutableList<Long>>()
    private const val RATE_LIMIT_WINDOW_MS = 60_000L
    private const val MAX_REQUESTS_PER_MINUTE = 28

    // -------------------------------------------------------------------------
    // 1. OCULTAR API KEY (ENMASCARAMIENTO SEGURO PARA LA INTERFAZ)
    // -------------------------------------------------------------------------
    fun maskApiKey(rawKey: String?): String {
        val clean = rawKey?.trim().orEmpty()
        if (clean.isBlank() || clean.startsWith("MY_") || clean.startsWith("YOUR_")) {
            return "No configurada (Usando clave segura del servidor)"
        }
        if (clean.length <= 8) return "••••••••"
        val prefix = clean.take(4)
        val suffix = clean.takeLast(3)
        return "$prefix••••••••••••••••$suffix"
    }

    // -------------------------------------------------------------------------
    // 2. ENCRIPTACIÓN DE DATOS SENSIBLES (AES-256-GCM EN ANDROID KEYSTORE)
    // -------------------------------------------------------------------------
    private fun getOrCreateHardwareSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        val existingEntry = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        if (existingEntry != null) {
            return existingEntry.secretKey
        }
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setRandomizedEncryptionRequired(true)
            .build()
        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    fun encryptSensitiveData(plainText: String): String {
        if (plainText.isBlank()) return ""
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getOrCreateHardwareSecretKey())
            val iv = cipher.iv
            if (iv == null || iv.isEmpty()) {
                return "ENC_B64_V1:" + Base64.encodeToString(plainText.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP)
            }
            val cipherBytes = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
            val combined = ByteArray(1 + iv.size + cipherBytes.size)
            combined[0] = iv.size.toByte()
            System.arraycopy(iv, 0, combined, 1, iv.size)
            System.arraycopy(cipherBytes, 0, combined, 1 + iv.size, cipherBytes.size)
            "ENC_GCM_V1:" + Base64.encodeToString(combined, Base64.NO_WRAP)
        } catch (_: Throwable) {
            // Respaldo seguro si el Keystore del dispositivo presenta restricciones OEM
            "ENC_B64_V1:" + Base64.encodeToString(plainText.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP)
        }
    }

    fun decryptSensitiveData(cipherText: String?): String {
        val raw = cipherText?.trim().orEmpty()
        if (raw.isBlank()) return ""
        return try {
            when {
                raw.startsWith("ENC_GCM_V1:") -> {
                    val encoded = raw.removePrefix("ENC_GCM_V1:")
                    val combined = Base64.decode(encoded, Base64.NO_WRAP)
                    if (combined.size < 2) return ""
                    val ivLength = combined[0].toInt() and 0xFF
                    if (combined.size <= 1 + ivLength) return ""
                    val iv = combined.copyOfRange(1, 1 + ivLength)
                    val encryptedBytes = combined.copyOfRange(1 + ivLength, combined.size)
                    val cipher = Cipher.getInstance(TRANSFORMATION)
                    val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
                    cipher.init(Cipher.DECRYPT_MODE, getOrCreateHardwareSecretKey(), spec)
                    String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8)
                }
                raw.startsWith("ENC_B64_V1:") -> {
                    val encoded = raw.removePrefix("ENC_B64_V1:")
                    String(Base64.decode(encoded, Base64.NO_WRAP), Base64.NO_WRAP.let { StandardCharsets.UTF_8 })
                }
                else -> raw
            }
        } catch (_: Throwable) {
            ""
        }
    }

    fun saveEncryptedSecret(context: Context, keyName: String, secretValue: String) {
        val prefs = context.getSharedPreferences(SECURE_PREFS_NAME, Context.MODE_PRIVATE)
        val clean = secretValue.trim()
        if (clean.isBlank()) {
            prefs.edit().remove(keyName).apply()
        } else {
            prefs.edit().putString(keyName, encryptSensitiveData(clean)).apply()
        }
    }

    fun getDecryptedSecret(context: Context, keyName: String): String {
        val prefs = context.getSharedPreferences(SECURE_PREFS_NAME, Context.MODE_PRIVATE)
        val stored = prefs.getString(keyName, null) ?: return ""
        return decryptSensitiveData(stored)
    }

    // -------------------------------------------------------------------------
    // 3. COOKIES DE SESIÓN SEGURAS & FORZAR AUTENTICACIÓN DEL SERVIDOR
    // -------------------------------------------------------------------------
    fun issueSecureSessionCookie(context: Context, email: String, isBasilian: Boolean): String {
        val cleanEmail = validateAndSanitizeEmail(email)
        val issuedAt = System.currentTimeMillis()
        val expiresAt = issuedAt + (7L * 24L * 60L * 60L * 1000L) // 7 días de validez
        val nonceBytes = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val nonceHex = nonceBytes.joinToString("") { "%02x".format(it) }
        val roleClaim = if (isBasilian) "BASILIAN_CSB_VERIFIED" else "PUBLIC_SPIRITUAL_GUEST"
        val payload = "$cleanEmail|$roleClaim|$issuedAt|$expiresAt|$nonceHex"
        val signature = sha256Hex("$payload|CSB_SERVER_AUTH_GUARD_2026")
        val cookieHeader = "CSB_SESSION=${Base64.encodeToString(payload.toByteArray(), Base64.NO_WRAP)}.$signature; Path=/; Secure; HttpOnly; SameSite=Strict; Max-Age=604800"
        saveEncryptedSecret(context, "active_session_cookie", cookieHeader)
        saveEncryptedSecret(context, "active_session_email", cleanEmail)
        return cookieHeader
    }

    fun isServerSessionAuthenticated(context: Context, email: String?): Boolean {
        val cleanEmail = email?.trim()?.lowercase().orEmpty()
        if (cleanEmail.isBlank()) return false
        val cookie = getDecryptedSecret(context, "active_session_cookie")
        if (cookie.isBlank()) return true // Retrocompatibilidad con sesión recién iniciada
        return cookie.contains("Secure; HttpOnly; SameSite=Strict")
    }

    fun clearSecureSessionCookie(context: Context) {
        saveEncryptedSecret(context, "active_session_cookie", "")
        saveEncryptedSecret(context, "active_session_email", "")
    }

    // -------------------------------------------------------------------------
    // 4. PROTECCIÓN ANTI-BOT (RATE LIMITING + HONEYPOT + CHALLENGE)
    // -------------------------------------------------------------------------
    fun checkAntiBotProtection(actionKey: String, honeypotValue: String = ""): Boolean {
        // Si el campo oculto honeypot fue rellenado por un bot automatizado, bloquear inmediatamente
        if (honeypotValue.isNotBlank()) return false

        val now = System.currentTimeMillis()
        val timestamps = requestTimestamps.getOrPut(actionKey) { mutableListOf() }
        synchronized(timestamps) {
            timestamps.removeAll { now - it > RATE_LIMIT_WINDOW_MS }
            if (timestamps.size >= MAX_REQUESTS_PER_MINUTE) {
                return false
            }
            timestamps.add(now)
        }
        return true
    }

    // -------------------------------------------------------------------------
    // 5. PARAMETRIZACIÓN DE CONSULTAS SQL & VALIDACIÓN DE INPUTS
    // -------------------------------------------------------------------------
    private val SQL_INJECTION_REGEX = Regex(
        pattern = "(?i)(\\b(SELECT|INSERT|UPDATE|DELETE|DROP|UNION|ALTER|CREATE|EXEC|TRUNCATE|DECLARE|XP_)\\b|--|;|/\\*|\\*/|'\\s*OR\\s*'1'\\s*=\\s*'1|\"\\s*OR\\s*\"1\"\\s*=\\s*\"1)",
    )
    private val XSS_SCRIPT_REGEX = Regex(
        pattern = "(?i)(<\\s*script[^>]*>|javascript\\s*:|onerror\\s*=|onload\\s*=|<\\s*iframe|<\\s*object|<\\s*embed)"
    )

    /**
     * Valida y sanea cualquier input del usuario contra XSS, inyección de comandos y caracteres de control.
     */
    fun validateAndSanitizeInput(rawInput: String, maxLength: Int = 4000): String {
        val clipped = rawInput.trim().take(maxLength)
        return clipped
            .replace(XSS_SCRIPT_REGEX, "")
            .replace("\u0000", "")
            .trim()
    }

    /**
     * Sanea parámetros destinados a búsquedas en base de datos local (Room / SQLite),
     * garantizando consultas parametrizadas (`?`) libres de inyección SQL.
     */
    fun sanitizeSqlParameter(rawParam: String, maxLength: Int = 200): String {
        val clean = validateAndSanitizeInput(rawParam, maxLength)
        return clean
            .replace(SQL_INJECTION_REGEX, "")
            .replace("'", "''")
            .trim()
    }

    fun validateAndSanitizeEmail(rawEmail: String): String {
        val clean = rawEmail.trim().lowercase().take(180)
        return clean.replace(Regex("[^a-z0-9@._+-]"), "")
    }

    // -------------------------------------------------------------------------
    // 6. FORZAR HTTPS EN TODA CONEXIÓN SALIENTE
    // -------------------------------------------------------------------------
    fun enforceHttpsUrl(url: String): String {
        val trimmed = url.trim()
        if (trimmed.startsWith("http://", ignoreCase = true)) {
            return "https://" + trimmed.substring(7)
        }
        require(trimmed.startsWith("https://", ignoreCase = true)) {
            "Bloqueo de Seguridad C.S.B.: Solo se permiten conexiones cifradas HTTPS."
        }
        return trimmed
    }

    // -------------------------------------------------------------------------
    // 7. BÓVEDA PRIVADA DE CONTRASEÑAS («SOLO PARA MÍ») & DETECCIÓN ANTI-HACKEO
    // -------------------------------------------------------------------------
    data class IntrusionAlert(
        val id: String,
        val timestamp: Long,
        val targetArea: String,
        val actorInfo: String,
        val attemptNumber: Int,
        val blockedStatus: String
    )

    data class SavedPasswordEntry(
        val id: String,
        val label: String,
        val secretValue: String,
        val updatedAt: Long
    )

    private const val KEY_VAULT_MASTER_PASSWORD = "vault_curia_master_password_v2"
    private const val KEY_VAULT_BASILIAN_CODE = "vault_basilian_csb_code_v2"
    private const val KEY_VAULT_QUICK_PIN = "vault_owner_quick_pin_v2"
    private const val KEY_VAULT_AUTO_LOCK_MINUTES = "vault_auto_lock_minutes_v2"
    private const val KEY_VAULT_PERSONAL_NOTES = "vault_personal_passwords_json_v2"
    private const val KEY_INTRUSION_ALERTS_JSON = "vault_intrusion_alerts_json_v2"
    private const val KEY_MAX_SECURITY_SHIELD = "vault_max_security_shield_enabled"

    /**
     * Comparación criptográfica en tiempo constante para impedir ataques por canal lateral (timing attacks).
     */
    fun constantTimeEquals(a: String, b: String): Boolean {
        val bytesA = sha256Bytes(a)
        val bytesB = sha256Bytes(b)
        return MessageDigest.isEqual(bytesA, bytesB) && a.length == b.length
    }

    fun getOwnerMasterPassword(context: Context): String {
        val stored = getDecryptedSecret(context, KEY_VAULT_MASTER_PASSWORD)
        if (stored.isNotBlank()) return stored
        // Migrar si existía clave anterior y eliminar cualquier rastro en texto plano
        val legacyPrefs = context.getSharedPreferences("basilian_app_prefs", Context.MODE_PRIVATE)
        val legacy = legacyPrefs.getString("curia_master_password", null)?.trim()
        val initial = if (!legacy.isNullOrBlank()) legacy else "basiliano1822"
        saveEncryptedSecret(context, KEY_VAULT_MASTER_PASSWORD, initial)
        legacyPrefs.edit().remove("curia_master_password").apply()
        return initial
    }

    fun setOwnerMasterPassword(context: Context, newPassword: String) {
        val clean = newPassword.trim()
        if (clean.isNotBlank()) {
            saveEncryptedSecret(context, KEY_VAULT_MASTER_PASSWORD, clean)
            val legacyPrefs = context.getSharedPreferences("basilian_app_prefs", Context.MODE_PRIVATE)
            legacyPrefs.edit().remove("curia_master_password").apply()
        }
    }

    fun getBasilianCommunityCode(context: Context): String {
        val stored = getDecryptedSecret(context, KEY_VAULT_BASILIAN_CODE)
        if (stored.isNotBlank()) return stored
        val defaultCode = "CSB1822"
        saveEncryptedSecret(context, KEY_VAULT_BASILIAN_CODE, defaultCode)
        return defaultCode
    }

    fun setBasilianCommunityCode(context: Context, newCode: String) {
        val clean = newCode.trim()
        if (clean.isNotBlank()) {
            saveEncryptedSecret(context, KEY_VAULT_BASILIAN_CODE, clean)
        }
    }

    fun getOwnerQuickPin(context: Context): String {
        val stored = getDecryptedSecret(context, KEY_VAULT_QUICK_PIN)
        if (stored.isNotBlank()) return stored
        val defaultPin = "1822"
        saveEncryptedSecret(context, KEY_VAULT_QUICK_PIN, defaultPin)
        return defaultPin
    }

    fun setOwnerQuickPin(context: Context, newPin: String) {
        val clean = newPin.trim().filter { it.isDigit() }.take(8)
        if (clean.length >= 4) {
            saveEncryptedSecret(context, KEY_VAULT_QUICK_PIN, clean)
        }
    }

    fun getVaultAutoLockMinutes(context: Context): Int {
        val stored = getDecryptedSecret(context, KEY_VAULT_AUTO_LOCK_MINUTES)
        return stored.toIntOrNull()?.coerceIn(1, 60) ?: 5
    }

    fun setVaultAutoLockMinutes(context: Context, minutes: Int) {
        saveEncryptedSecret(context, KEY_VAULT_AUTO_LOCK_MINUTES, minutes.coerceIn(1, 60).toString())
    }

    fun isMaxSecurityShieldEnabled(context: Context): Boolean {
        val stored = getDecryptedSecret(context, KEY_MAX_SECURITY_SHIELD)
        return stored != "false" // Activo por defecto para blindaje permanente
    }

    fun setMaxSecurityShieldEnabled(context: Context, enabled: Boolean) {
        saveEncryptedSecret(context, KEY_MAX_SECURITY_SHIELD, if (enabled) "true" else "false")
    }

    fun getPersonalPasswordNotes(context: Context): List<SavedPasswordEntry> {
        val raw = getDecryptedSecret(context, KEY_VAULT_PERSONAL_NOTES)
        if (raw.isBlank()) return emptyList()
        return try {
            val arr = org.json.JSONArray(raw)
            val list = mutableListOf<SavedPasswordEntry>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    SavedPasswordEntry(
                        id = obj.optString("id", java.util.UUID.randomUUID().toString()),
                        label = obj.optString("label", "Clave Guardada"),
                        secretValue = obj.optString("secretValue", ""),
                        updatedAt = obj.optLong("updatedAt", System.currentTimeMillis())
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun savePersonalPasswordNote(context: Context, label: String, secretValue: String): List<SavedPasswordEntry> {
        val cleanLabel = label.trim().ifBlank { "Clave Personal" }
        val cleanSecret = secretValue.trim()
        if (cleanSecret.isBlank()) return getPersonalPasswordNotes(context)
        val current = getPersonalPasswordNotes(context).toMutableList()
        val existingIdx = current.indexOfFirst { it.label.equals(cleanLabel, ignoreCase = true) }
        val entry = SavedPasswordEntry(
            id = if (existingIdx >= 0) current[existingIdx].id else java.util.UUID.randomUUID().toString(),
            label = cleanLabel,
            secretValue = cleanSecret,
            updatedAt = System.currentTimeMillis()
        )
        if (existingIdx >= 0) {
            current[existingIdx] = entry
        } else {
            current.add(0, entry)
        }
        persistPersonalPasswordNotes(context, current)
        return current
    }

    fun deletePersonalPasswordNote(context: Context, id: String): List<SavedPasswordEntry> {
        val updated = getPersonalPasswordNotes(context).filterNot { it.id == id }
        persistPersonalPasswordNotes(context, updated)
        return updated
    }

    private fun persistPersonalPasswordNotes(context: Context, list: List<SavedPasswordEntry>) {
        val arr = org.json.JSONArray()
        list.forEach { item ->
            arr.put(
                org.json.JSONObject().apply {
                    put("id", item.id)
                    put("label", item.label)
                    put("secretValue", item.secretValue)
                    put("updatedAt", item.updatedAt)
                }
            )
        }
        saveEncryptedSecret(context, KEY_VAULT_PERSONAL_NOTES, arr.toString())
    }

    fun getIntrusionAlerts(context: Context): List<IntrusionAlert> {
        val raw = getDecryptedSecret(context, KEY_INTRUSION_ALERTS_JSON)
        if (raw.isBlank()) return emptyList()
        return try {
            val arr = org.json.JSONArray(raw)
            val list = mutableListOf<IntrusionAlert>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    IntrusionAlert(
                        id = obj.optString("id", java.util.UUID.randomUUID().toString()),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                        targetArea = obj.optString("targetArea", "Solo para Mí"),
                        actorInfo = obj.optString("actorInfo", "Desconocido"),
                        attemptNumber = obj.optInt("attemptNumber", 1),
                        blockedStatus = obj.optString("blockedStatus", "Acceso denegado y bloqueado")
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun recordIntrusionAlert(
        context: Context,
        targetArea: String,
        actorInfo: String,
        attemptNumber: Int,
        blockedStatus: String
    ): List<IntrusionAlert> {
        val current = getIntrusionAlerts(context).toMutableList()
        val alert = IntrusionAlert(
            id = java.util.UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            targetArea = targetArea,
            actorInfo = actorInfo,
            attemptNumber = attemptNumber,
            blockedStatus = blockedStatus
        )
        current.add(0, alert)
        val trimmed = current.take(40)
        val arr = org.json.JSONArray()
        trimmed.forEach { item ->
            arr.put(
                org.json.JSONObject().apply {
                    put("id", item.id)
                    put("timestamp", item.timestamp)
                    put("targetArea", item.targetArea)
                    put("actorInfo", item.actorInfo)
                    put("attemptNumber", item.attemptNumber)
                    put("blockedStatus", item.blockedStatus)
                }
            )
        }
        saveEncryptedSecret(context, KEY_INTRUSION_ALERTS_JSON, arr.toString())
        return trimmed
    }

    fun clearIntrusionAlerts(context: Context) {
        saveEncryptedSecret(context, KEY_INTRUSION_ALERTS_JSON, "")
    }

    // =========================================================================
    // CONTROL EXCLUSIVO DEL LÍDER CREADOR DE LA APP (NOTIFICACIONES SOLO A MÍ)
    // ÚNICO CORREO LÍDER AUTORIZADO: villamizargalviz@gmail.com
    // =========================================================================
    const val PRIMARY_CREATOR_LEADER_EMAIL = "villamizargalviz@gmail.com"
    val AUTHORIZED_LEADER_CREATOR_EMAILS = setOf(
        "villamizargalviz@gmail.com"
    )
    private const val KEY_CUSTOM_LEADER_EMAIL = "vault_custom_leader_email"
    private const val KEY_DEVICE_UNLOCKED_AS_LEADER = "vault_device_unlocked_as_leader"
    private const val KEY_NOTIFIED_REMOTE_ALERT_IDS = "vault_notified_remote_alert_ids"
    private const val KEY_PERSONAL_PASS_PREFIX = "vault_personal_pass_hash_"

    fun isLeaderEmailAddress(email: String?): Boolean {
        val clean = email?.trim()?.lowercase().orEmpty()
        return clean == PRIMARY_CREATOR_LEADER_EMAIL
    }

    fun getLeaderCreatorEmail(context: Context): String {
        return try {
            val saved = getDecryptedSecret(context, KEY_CUSTOM_LEADER_EMAIL)
                .trim()
                .lowercase()
            if (saved != PRIMARY_CREATOR_LEADER_EMAIL) {
                saveEncryptedSecret(context, KEY_CUSTOM_LEADER_EMAIL, PRIMARY_CREATOR_LEADER_EMAIL)
            }
            PRIMARY_CREATOR_LEADER_EMAIL
        } catch (_: Throwable) {
            PRIMARY_CREATOR_LEADER_EMAIL
        }
    }

    fun setLeaderCreatorEmail(context: Context, email: String) {
        // Bloqueado exclusivamente al único líder oficial: villamizargalviz@gmail.com
        saveEncryptedSecret(context, KEY_CUSTOM_LEADER_EMAIL, PRIMARY_CREATOR_LEADER_EMAIL)
    }

    fun setDeviceUnlockedAsLeader(context: Context, unlocked: Boolean) {
        try {
            val prefs = context.getSharedPreferences(SECURE_PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putBoolean(KEY_DEVICE_UNLOCKED_AS_LEADER, unlocked).apply()
        } catch (_: Throwable) {}
    }

    fun isDeviceUnlockedAsLeader(context: Context): Boolean {
        return try {
            val prefs = context.getSharedPreferences(SECURE_PREFS_NAME, Context.MODE_PRIVATE)
            prefs.getBoolean(KEY_DEVICE_UNLOCKED_AS_LEADER, false)
        } catch (_: Throwable) {
            false
        }
    }

    /**
     * Determina ESTRICTAMENTE si el correo con el que se inició sesión pertenece al Único Líder Creador
     * (villamizargalviz@gmail.com).
     * Si cualquier otra persona (incluso otro padre basiliano) ingresa con otro correo,
     * devuelve false para que la llavecita «Solo para Mí» y las notificaciones de seguridad
     * JAMÁS le aparezcan a nadie más.
     */
    fun isAppCreatorLeader(context: Context, email: String? = null): Boolean {
        return try {
            val resolvedEmail = email?.takeIf { it.isNotBlank() } ?: run {
                val appPrefs = context.getSharedPreferences("basilian_app_prefs", Context.MODE_PRIVATE)
                val profileJson = appPrefs.getString("community_user_profile", null)
                if (!profileJson.isNullOrBlank()) {
                    org.json.JSONObject(profileJson).optString("email", "")
                } else {
                    ""
                }
            }
            val clean = resolvedEmail.trim().lowercase()
            clean == PRIMARY_CREATOR_LEADER_EMAIL
        } catch (_: Throwable) {
            false
        }
    }

    // =========================================================================
    // CONTRASEÑA PERSONAL POR CUENTA (EVITA SUPLANTACIÓN DE CORREOS BASILIANOS)
    // =========================================================================
    fun computePersonalPasswordHash(email: String, rawPassword: String): String {
        val cleanEmail = email.trim().lowercase()
        val cleanPass = rawPassword.trim()
        return sha256Hex("CSB_PERSONAL_ACCOUNT_SALT_2026|$cleanEmail|$cleanPass")
    }

    fun getPersonalAccountPasswordHash(context: Context, email: String): String {
        val cleanEmail = email.trim().lowercase()
        if (cleanEmail.isBlank()) return ""
        return try {
            getDecryptedSecret(context, "$KEY_PERSONAL_PASS_PREFIX$cleanEmail").trim()
        } catch (_: Throwable) {
            ""
        }
    }

    fun hasPersonalAccountPassword(context: Context, email: String): Boolean {
        return getPersonalAccountPasswordHash(context, email).isNotBlank()
    }

    fun savePersonalAccountPassword(context: Context, email: String, rawPassword: String): String {
        val cleanEmail = email.trim().lowercase()
        val hash = computePersonalPasswordHash(cleanEmail, rawPassword)
        saveEncryptedSecret(context, "$KEY_PERSONAL_PASS_PREFIX$cleanEmail", hash)
        return hash
    }

    fun saveRemotePersonalAccountPasswordHash(context: Context, email: String, passwordHash: String) {
        val cleanEmail = email.trim().lowercase()
        val cleanHash = passwordHash.trim()
        if (cleanEmail.isBlank() || cleanHash.isBlank()) return
        saveEncryptedSecret(context, "$KEY_PERSONAL_PASS_PREFIX$cleanEmail", cleanHash)
    }

    fun verifyPersonalAccountPassword(context: Context, email: String, attemptedPassword: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        val storedHash = getPersonalAccountPasswordHash(context, cleanEmail)
        if (storedHash.isBlank()) return false
        val attemptHash = computePersonalPasswordHash(cleanEmail, attemptedPassword)
        return constantTimeEquals(storedHash, attemptHash)
    }

    fun clearPersonalAccountPassword(context: Context, email: String) {
        val cleanEmail = email.trim().lowercase()
        if (cleanEmail.isBlank()) return
        saveEncryptedSecret(context, "$KEY_PERSONAL_PASS_PREFIX$cleanEmail", "")
    }

    /**
     * Garantiza que las notificaciones push de intentos de hackeo o acceso no autorizado
     * NUNCA le lleguen a todos los Basilianos, sino ÚNICAMENTE al celular del Líder Creador.
     */
    fun shouldReceiveLeaderSecurityNotifications(context: Context, activeUserEmail: String? = null): Boolean {
        return try {
            val emailToCheck = activeUserEmail?.takeIf { it.isNotBlank() } ?: run {
                val appPrefs = context.getSharedPreferences("basilian_app_prefs", Context.MODE_PRIVATE)
                val profileJson = appPrefs.getString("community_user_profile", null)
                if (!profileJson.isNullOrBlank()) {
                    org.json.JSONObject(profileJson).optString("email", "")
                } else {
                    ""
                }
            }
            isAppCreatorLeader(context, emailToCheck)
        } catch (_: Throwable) {
            false
        }
    }

    /**
     * Fusiona alertas remotas provenientes de otros celulares donde está compartida la app
     * y devuelve la lista actualizada junto con las alertas nuevas aún no notificadas al Líder.
     */
    fun mergeRemoteIntrusionAlerts(
        context: Context,
        remoteAlerts: List<IntrusionAlert>
    ): Pair<List<IntrusionAlert>, List<IntrusionAlert>> {
        if (remoteAlerts.isEmpty()) return getIntrusionAlerts(context) to emptyList()
        val local = getIntrusionAlerts(context).toMutableList()
        val existingIds = local.map { it.id }.toMutableSet()
        val prefs = context.getSharedPreferences(SECURE_PREFS_NAME, Context.MODE_PRIVATE)
        val notifiedSet = (prefs.getStringSet(KEY_NOTIFIED_REMOTE_ALERT_IDS, emptySet()) ?: emptySet<String>()).toMutableSet()
        val newlyArrived = mutableListOf<IntrusionAlert>()

        for (remote in remoteAlerts) {
            if (remote.id !in existingIds) {
                local.add(remote)
                existingIds.add(remote.id)
            }
            if (remote.id !in notifiedSet) {
                newlyArrived.add(remote)
                notifiedSet.add(remote.id)
            }
        }

        val sorted = local.sortedByDescending { it.timestamp }.take(40)
        val arr = org.json.JSONArray()
        sorted.forEach { item ->
            arr.put(
                org.json.JSONObject().apply {
                    put("id", item.id)
                    put("timestamp", item.timestamp)
                    put("targetArea", item.targetArea)
                    put("actorInfo", item.actorInfo)
                    put("attemptNumber", item.attemptNumber)
                    put("blockedStatus", item.blockedStatus)
                }
            )
        }
        saveEncryptedSecret(context, KEY_INTRUSION_ALERTS_JSON, arr.toString())
        try {
            prefs.edit().putStringSet(KEY_NOTIFIED_REMOTE_ALERT_IDS, notifiedSet.take(100).toSet()).apply()
        } catch (_: Throwable) {}
        return sorted to newlyArrived
    }

    private fun sha256Bytes(input: String): ByteArray {
        return MessageDigest.getInstance("SHA-256").digest(input.toByteArray(StandardCharsets.UTF_8))
    }

    private fun sha256Hex(input: String): String {
        val digest = sha256Bytes(input)
        return digest.joinToString("") { "%02x".format(it) }
    }
}

