package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.ui.components.BasilianCrestView
import com.example.ui.components.JarvisFuturistaCanvas
import com.example.ui.components.LocalCustomCrestUri
import com.example.ui.components.futuristic3DCard
import com.example.ui.viewmodel.BasilianViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.sin

private val emailVerificationHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
        .connectTimeout(4, TimeUnit.SECONDS)
        .readTimeout(4, TimeUnit.SECONDS)
        .build()
}

/**
 * Verificación real de seguridad y existencia de correo electrónico:
 * 1. Bloquea caracteres de inyección (SQL/NoSQL/XSS/Prompt Injection).
 * 2. Bloquea correos inventados, temporales, secuencias de teclado o de prueba.
 * 3. Verifica en tiempo real mediante Google DNS-over-HTTPS (registros MX y A)
 *    que el dominio del correo exista verdaderamente en Internet y tenga servidores de correo activos.
 */
private suspend fun verifyRealEmailAndSecurity(
    rawEmail: String,
    rawName: String,
    knownCommunityEmails: Set<String>
): Pair<Boolean, String?> = withContext(Dispatchers.IO) {
    val email = rawEmail.trim().lowercase()

    // 1. Escudo anti-inyección / anti-hackeo
    val forbiddenPattern = Regex("""[<>"';\\`${'$'}\{\}\[\]\(\)\|\^~]|\b(select|insert|drop|delete|update|script|onerror|onload|ignore previous)\b""", RegexOption.IGNORE_CASE)
    if (forbiddenPattern.containsMatchIn(email) || forbiddenPattern.containsMatchIn(rawName)) {
        return@withContext false to "🛡️ Escudo de Seguridad C.S.B.: Se detectaron caracteres o comandos no permitidos."
    }

    // 2. Formato estricto RFC
    val emailRegex = Regex("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,12}$")
    if (!emailRegex.matches(email)) {
        return@withContext false to "Por favor ingresa un correo electrónico con formato real y válido (ejemplo: nombre.apellido@gmail.com)."
    }

    // Si pertenece al directorio oficial pre-verificado de la Congregación, acceso directo
    if (knownCommunityEmails.contains(email)) {
        return@withContext true to null
    }

    val localPart = email.substringBefore("@")
    val domain = email.substringAfter("@")

    if (localPart.length < 3 || localPart.length > 64) {
        return@withContext false to "El usuario del correo («$localPart») es demasiado corto o inválido. Ingresa un correo que verdaderamente exista."
    }
    if (localPart.startsWith(".") || localPart.endsWith(".") || localPart.contains("..")) {
        return@withContext false to "El correo contiene puntos consecutivos o mal ubicados. Ingresa un correo real."
    }

    // 3. Bloqueo de dominios falsos, inventados o desechables
    val blockedDomains = setOf(
        "test.com", "testing.com", "ejemplo.com", "example.com", "falso.com", "fake.com",
        "correo.com", "mail.com", "email.com", "inventado.com", "noexiste.com", "nada.com",
        "hola.com", "asdf.com", "qwerty.com", "123.com", "1234.com", "abc.com", "xyz.com",
        "mailinator.com", "tempmail.com", "10minutemail.com", "guerrillamail.com", "yopmail.com",
        "trashmail.com", "sharklasers.com", "getnada.com", "dispostable.com", "maildrop.cc",
        "temp-mail.org", "fakeinbox.com", "throwawaymail.com", "basura.com", "temporal.com"
    )
    if (domain in blockedDomains) {
        return@withContext false to "⚠️ El dominio @$domain es de prueba o temporal y no existe como correo personal válido. Usa tu correo real."
    }

    // 4. Bloqueo de nombres de usuario inventados / pulsaciones aleatorias
    val cleanLocalAlpha = localPart.replace(Regex("[^a-z]"), "")
    val fakeLocalWords = setOf(
        "test", "testing", "prueba", "pruebas", "falso", "fake", "inventado", "noexiste",
        "correo", "email", "usuario", "user", "admin", "administrador", "root", "anonimo",
        "nadie", "ninguno", "cualquiera", "hola", "holahola", "asdf", "asdfg", "asdfgh",
        "asdfghjkl", "qwerty", "qwertyuiop", "zxcv", "zxcvb", "zxcvbnm", "abcd", "abcde",
        "abcdef", "xyz", "xxx", "xxxx", "aaaa", "bbbb", "cccc", "dddd", "eeee", "ffff",
        "123", "1234", "12345", "123456", "1234567", "12345678", "123456789", "0000"
    )
    if (localPart in fakeLocalWords || cleanLocalAlpha in fakeLocalWords) {
        return@withContext false to "⚠️ El correo «$email» parece ser de prueba o inventado. Por seguridad, ingresa únicamente un correo electrónico real que exista."
    }

    // Bloquear si solo tiene números (ej. 987654@gmail.com) o menos de 2 letras
    if (cleanLocalAlpha.length < 2) {
        return@withContext false to "⚠️ El correo debe contener un nombre real con letras antes del @ (no solo números)."
    }

    // Bloquear repetición de 4 caracteres iguales seguidos (ej. aaaa@gmail.com, jjjj12@gmail.com)
    if (Regex("(.)\\1{3,}").containsMatchIn(localPart)) {
        return@withContext false to "⚠️ El correo «$email» contiene secuencias repetitivas no válidas. Ingresa tu correo electrónico real."
    }

    // Bloquear secuencias sin vocales (ej. bcdfgh@gmail.com) de 5 o más letras
    val hasVowel = cleanLocalAlpha.any { it in "aeiouy" }
    if (!hasVowel && cleanLocalAlpha.length >= 4) {
        return@withContext false to "⚠️ El usuario del correo «$localPart» no parece un nombre o cuenta real. Verifica que esté bien escrito."
    }

    // Reglas reales de proveedores principales (Gmail exige mínimo 6 caracteres en cuentas estándar)
    if ((domain == "gmail.com" || domain == "googlemail.com") && localPart.replace(".", "").length < 6) {
        return@withContext false to "⚠️ Las cuentas reales de @gmail.com requieren al menos 6 caracteres antes del @. Ingresa tu cuenta de Gmail verdadera."
    }

    // 5. Verificación en vivo de servidores de correo reales (DNS MX / A) mediante Google Public DNS
    try {
        val mxUrl = "https://dns.google/resolve?name=${java.net.URLEncoder.encode(domain, "UTF-8")}&type=MX"
        val mxReq = Request.Builder().url(mxUrl).addHeader("Accept", "application/json").build()
        emailVerificationHttpClient.newCall(mxReq).execute().use { resp ->
            if (resp.isSuccessful) {
                val bodyStr = resp.body?.string().orEmpty()
                val json = JSONObject(bodyStr)
                val status = json.optInt("Status", -1)
                // Status == 3 significa NXDOMAIN (el dominio NO existe en Internet)
                if (status == 3) {
                    return@withContext false to "❌ El dominio @$domain NO existe en Internet. Por favor ingresa un correo electrónico verdadero."
                }
                val answers = json.optJSONArray("Answer")
                val hasValidMx = answers != null && answers.length() > 0 && (0 until answers.length()).any { idx ->
                    val data = answers.optJSONObject(idx)?.optString("data").orEmpty().trim()
                    data.isNotBlank() && data != "0 ." && data != "."
                }
                if (hasValidMx) {
                    return@withContext true to null
                }

                // Si no reportó MX directo, verificar registro A del dominio
                val aUrl = "https://dns.google/resolve?name=${java.net.URLEncoder.encode(domain, "UTF-8")}&type=A"
                val aReq = Request.Builder().url(aUrl).addHeader("Accept", "application/json").build()
                emailVerificationHttpClient.newCall(aReq).execute().use { aResp ->
                    if (aResp.isSuccessful) {
                        val aJson = JSONObject(aResp.body?.string().orEmpty())
                        val aStatus = aJson.optInt("Status", -1)
                        val aAnswers = aJson.optJSONArray("Answer")
                        if (aStatus != 0 || aAnswers == null || aAnswers.length() == 0) {
                            return@withContext false to "❌ El servidor @$domain no tiene buzón de correo activo en Internet. Ingresa un correo verdadero."
                        }
                    }
                }
            }
        }
    } catch (_: Exception) {
        // Si no hay conexión momentánea a DNS, solo permitir dominios oficiales conocidos
        val trustedOfflineDomains = setOf(
            "gmail.com", "hotmail.com", "outlook.com", "yahoo.com", "icloud.com",
            "live.com", "msn.com", "basilian.org", "csb.org", "stthom.edu", "utoronto.ca"
        )
        val isTrustedInstitutional = domain in trustedOfflineDomains ||
            domain.endsWith(".edu.co") || domain.endsWith(".edu") || domain.endsWith(".org.co")
        if (!isTrustedInstitutional) {
            return@withContext false to "⚠️ No se pudo verificar la existencia del dominio @$domain. Usa un proveedor verificado (@gmail.com, @outlook.com, @basilian.org, etc.)."
        }
    }

    return@withContext true to null
}

/**
 * Pantalla de Acceso y Vinculación por Correo Electrónico.
 * Garantiza que la IA de Historia y toda la aplicación reconozcan
 * a la persona por su nombre y vinculen todas sus consultas, oraciones y perfil
 * a su correo electrónico verificado.
 */
@Composable
fun EmailAccessGateScreen(
    viewModel: BasilianViewModel,
    onAccessGranted: () -> Unit = {}
) {
    val communityMembers by viewModel.communityMembers.collectAsState()
    val customCrestUri = LocalCustomCrestUri.current
    val coroutineScope = rememberCoroutineScope()

    var emailInput by remember { mutableStateOf("") }
    var fullNameInput by remember { mutableStateOf("") }
    var personalPasswordInput by remember { mutableStateOf("") }
    var showPersonalPassword by remember { mutableStateOf(false) }
    var basilianIdentityInput by remember { mutableStateOf("") }
    var nameManuallyEdited by remember { mutableStateOf(false) }
    var isBasilianSelection by remember { mutableStateOf<Boolean?>(null) } // null = auto por correo, true = Sí soy Basiliano, false = No (Visitante/Laico)
    var selectedRole by remember { mutableStateOf("Novicio") }
    var cityInput by remember { mutableStateOf("Colombia • Comunidad Basiliana") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var infoMessage by remember { mutableStateOf<String?>(null) }
    var isVerifyingEmail by remember { mutableStateOf(false) }

    // Estado del flujo de Confirmación de Correo mediante Firebase Auth
    var awaitingEmailConfirmation by remember { mutableStateOf(false) }
    var generatedConfirmationCode by remember { mutableStateOf("") }
    var confirmationCodeInput by remember { mutableStateOf("") }

    val registeredBasilianEmails by viewModel.registeredBasilianEmails.collectAsState()

    // Detectar automáticamente si el correo escrito pertenece al Directorio Oficial C.S.B. o a los nuevos correos agregados en "Solo para Mí"
    val matchedDirectoryMember = remember(emailInput) {
        val clean = emailInput.trim().lowercase()
        if (clean.contains("@")) {
            com.example.data.repository.BasilianDirectoryRepository.members.firstOrNull { m ->
                m.email.isNotBlank() &&
                    m.email.lowercase().split(",", ";", " ").any { it.trim() == clean }
            }
        } else null
    }
    val matchedRegisteredEntry = remember(emailInput, registeredBasilianEmails) {
        val clean = emailInput.trim().lowercase()
        if (clean.contains("@")) {
            registeredBasilianEmails.firstOrNull { it.email.equals(clean, ignoreCase = true) }
        } else null
    }
    val isRecognizedInDirectory = remember(emailInput, matchedDirectoryMember, matchedRegisteredEntry) {
        matchedDirectoryMember != null || matchedRegisteredEntry != null || viewModel.isEmailInOfficialBasilianDirectory(emailInput)
    }

    val roles = listOf(
        "Novicio",
        "Asociado",
        "Escolástico / Religioso Basiliano",
        "Sacerdote Basiliano (C.S.B.)",
        "Laico / Espiritualidad Basiliana",
        "Docente / Estudiante",
        "Joven en Discernimiento"
    )

    val HoloCyan = Color(0xFF00F0FF)
    val HoloBlue = Color(0xFF0284C7)
    val HoloGold = Color(0xFFF59E0B)
    val HoloGlass = Color(0xFF07152B).copy(alpha = 0.88f)

    Box(modifier = Modifier.fillMaxSize()) {
        JarvisFuturistaCanvas(isActivePulse = true)
        com.example.ui.components.Futuristic3DSpatialOverlay(
            isDark = true,
            accentColor = HoloCyan,
            goldColor = HoloGold
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Insignia Superior: Congregación de los Padres Basilianos C.S.B.
            Surface(
                shape = RoundedCornerShape(50),
                color = HoloCyan.copy(alpha = 0.14f),
                border = BorderStroke(1.dp, HoloCyan.copy(alpha = 0.65f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = HoloGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Congregación de los Padres Basilianos C.S.B.",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = HoloCyan,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Escudo con Halo
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(106.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                HoloCyan.copy(alpha = 0.30f),
                                HoloGold.copy(alpha = 0.18f),
                                Color.Transparent
                            )
                        )
                    )
                    .border(1.5.dp, HoloCyan.copy(alpha = 0.6f), CircleShape)
            ) {
                BasilianCrestView(
                    size = 78.dp,
                    showMottoText = false,
                    customCrestUri = customCrestUri
                )
            }

            Text(
                text = "Bienvenido a Padres Basilianos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            // Frase de Bienvenida Cercana + Cita de los Padres Basilianos
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = HoloGold.copy(alpha = 0.10f),
                border = BorderStroke(1.dp, HoloGold.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "«Enséñame la bondad, la disciplina y la ciencia, porque lo que nos une en una sola familia es el amor de Cristo y el cuidado mutuo entre hermanos.»",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFDE68A),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                    Text(
                        text = "Qué alegría recibirte en esta casa espiritual. Aquí encontrarás un espacio fraterno de oración, sabiduría y encuentro humano.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFBAE6FD),
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp
                    )
                }
            }

            // Tarjeta Principal de Acceso
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .futuristic3DCard(
                        accentColor = HoloCyan,
                        secondaryColor = HoloGold,
                        cornerRadius = 22.dp
                    )
                    .testTag("card_email_access_gate"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = HoloGlass),
                border = BorderStroke(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(listOf(HoloCyan, HoloGold, HoloBlue))
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = HoloGold,
                            modifier = Modifier.size(22.dp)
                        )
                        Column {
                            Text(
                                text = "Ingreso con Contraseña Personal",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Ingresa tu correo, nombre y contraseña personal para acceder",
                                style = MaterialTheme.typography.labelSmall,
                                color = HoloGold
                            )
                        }
                    }

                    // Selector inicial: ¿Eres religioso Basiliano o no? (O detección directa por correo)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White.copy(alpha = 0.05f),
                        border = BorderStroke(1.dp, HoloCyan.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "¿Perteneces a la Comunidad Religiosa Basiliana (C.S.B.)?",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = HoloCyan
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val isYes = isBasilianSelection == true || (isBasilianSelection == null && isRecognizedInDirectory)
                                val isNo = isBasilianSelection == false

                                OutlinedButton(
                                    onClick = {
                                        isBasilianSelection = true
                                        if (selectedRole.contains("Laico") || selectedRole.contains("Docente")) {
                                            selectedRole = "Sacerdote Basiliano (C.S.B.)"
                                        }
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isYes) HoloGold.copy(alpha = 0.24f) else Color.Transparent,
                                        contentColor = if (isYes) HoloGold else Color.White
                                    ),
                                    border = BorderStroke(1.2.dp, if (isYes) HoloGold else Color.White.copy(alpha = 0.3f)),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("btn_select_is_basilian_yes")
                                ) {
                                    Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Sí, soy Basiliano", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = {
                                        isBasilianSelection = false
                                        selectedRole = "Laico / Espiritualidad Basiliana"
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isNo) HoloCyan.copy(alpha = 0.24f) else Color.Transparent,
                                        contentColor = if (isNo) HoloCyan else Color.White
                                    ),
                                    border = BorderStroke(1.2.dp, if (isNo) HoloCyan else Color.White.copy(alpha = 0.3f)),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("btn_select_is_basilian_no")
                                ) {
                                    Icon(Icons.Default.Public, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("No (Visitante / Laico)", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(
                                text = "💡 Si eres Basiliano y tu correo está en el Directorio Oficial, entrarás directamente con acceso completo apenas escribas tu correo.",
                                fontSize = 10.5.sp,
                                color = Color(0xFFBAE6FD),
                                lineHeight = 14.sp
                            )
                        }
                    }

                    // Campo 1: Correo Electrónico
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { newEmail ->
                            emailInput = newEmail
                            errorMessage = null
                            infoMessage = null
                            awaitingEmailConfirmation = false
                            val dirMatch = com.example.data.repository.BasilianDirectoryRepository.members.firstOrNull { m ->
                                m.email.isNotBlank() &&
                                    m.email.lowercase().split(",", ";", " ").any { it.trim() == newEmail.trim().lowercase() }
                            }
                            val regMatch = registeredBasilianEmails.firstOrNull {
                                it.email.equals(newEmail.trim().lowercase(), ignoreCase = true)
                            }
                            if (dirMatch != null) {
                                fullNameInput = dirMatch.name
                                selectedRole = when (dirMatch.category) {
                                    com.example.data.model.MemberCategory.SCHOLASTIC -> "Escolástico / Religioso Basiliano"
                                    com.example.data.model.MemberCategory.NOVICE -> "Novicio"
                                    else -> "Sacerdote Basiliano (C.S.B.)"
                                }
                                cityInput = "${dirMatch.currentHouse} • ${dirMatch.country}"
                                isBasilianSelection = true
                            } else if (regMatch != null) {
                                if (regMatch.fatherName.isNotBlank()) {
                                    fullNameInput = regMatch.fatherName
                                }
                                selectedRole = regMatch.role.ifBlank { "Sacerdote Basiliano (C.S.B.)" }
                                isBasilianSelection = true
                            } else if (!nameManuallyEdited) {
                                val rawName = newEmail.substringBefore("@")
                                    .replace(".", " ")
                                    .replace("_", " ")
                                    .replace("-", " ")
                                    .trim()
                                if (rawName.isNotBlank()) {
                                    fullNameInput = rawName.split(" ")
                                        .filter { it.isNotBlank() }
                                        .joinToString(" ") { word ->
                                            word.replaceFirstChar { c -> c.uppercase() }
                                        }
                                }
                            }
                        },
                        label = { Text("Tu Correo Electrónico Real *", color = HoloCyan) },
                        placeholder = { Text("ejemplo@basilian.org o tu@gmail.com", color = Color.White.copy(alpha = 0.45f)) },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = HoloCyan)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = HoloCyan,
                            unfocusedBorderColor = HoloCyan.copy(alpha = 0.4f),
                            cursorColor = HoloCyan
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("email_access_input")
                    )

                    // Insignia en tiempo real si el correo está en el directorio oficial
                    AnimatedVisibility(visible = isRecognizedInDirectory && isBasilianSelection != false) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF064E3B).copy(alpha = 0.7f),
                            border = BorderStroke(1.dp, Color(0xFF34D399)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF34D399), modifier = Modifier.size(20.dp))
                                Text(
                                    text = "✅ Correo verificado en el Directorio Oficial de los Padres Basilianos C.S.B. (${matchedDirectoryMember?.name ?: matchedRegisteredEntry?.fatherName ?: "Miembro C.S.B."}). Protegido con Clave Personal.",
                                    color = Color(0xFFD1FAE5),
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }

                    // Campo 2: Nombre
                    OutlinedTextField(
                        value = fullNameInput,
                        onValueChange = {
                            fullNameInput = it
                            nameManuallyEdited = true
                            errorMessage = null
                        },
                        label = { Text("Tu Nombre *", color = HoloGold) },
                        placeholder = { Text("Ej: P. Juan, Hno. Mateo, Andrés...", color = Color.White.copy(alpha = 0.45f)) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = HoloGold)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = HoloGold,
                            unfocusedBorderColor = HoloGold.copy(alpha = 0.45f),
                            cursorColor = HoloGold
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("name_access_input")
                    )

                    // Campo 3: Contraseña / Clave Personal por Usuario (Seguridad Anti-Suplantación)
                    val accountAlreadyHasPassword = remember(emailInput) {
                        viewModel.hasPersonalAccountPassword(emailInput)
                    }
                    val isLeaderEmailTyped = remember(emailInput) {
                        com.example.data.security.BasilianSecurityManager.isLeaderEmailAddress(emailInput)
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF0A1D37),
                        border = BorderStroke(1.2.dp, HoloGold.copy(alpha = 0.7f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = HoloGold,
                                    modifier = Modifier.size(17.dp)
                                )
                                Text(
                                    text = if (accountAlreadyHasPassword) {
                                        "🔒 Cuenta Protegida: Ingresa tu Clave Personal"
                                    } else {
                                        "🛡️ Crea tu Clave Personal de Acceso"
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = HoloGold
                                )
                            }
                            Text(
                                text = if (accountAlreadyHasPassword) {
                                    "Esta cuenta ya tiene su contraseña personal registrada. Nadie más puede entrar a tu perfil sin esta clave."
                                } else {
                                    "Elige una contraseña personal (mín. 4 caracteres) para blindar tu correo. Así ningún desconocido ni otro usuario podrá entrar con tu correo."
                                },
                                fontSize = 10.5.sp,
                                color = Color(0xFFBAE6FD),
                                lineHeight = 14.sp
                            )

                            OutlinedTextField(
                                value = personalPasswordInput,
                                onValueChange = {
                                    personalPasswordInput = it
                                    errorMessage = null
                                },
                                label = {
                                    Text(
                                        text = if (accountAlreadyHasPassword) "Tu Clave Personal *" else "Crea tu Clave Personal (mín. 4 caracteres) *",
                                        color = HoloGold
                                    )
                                },
                                placeholder = {
                                    Text("Escribe tu contraseña personal...", color = Color.White.copy(alpha = 0.45f))
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Key, contentDescription = null, tint = HoloGold)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { showPersonalPassword = !showPersonalPassword }) {
                                        Icon(
                                            imageVector = if (showPersonalPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = "Mostrar u ocultar clave",
                                            tint = HoloCyan
                                        )
                                    }
                                },
                                visualTransformation = if (showPersonalPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = HoloGold,
                                    unfocusedBorderColor = HoloGold.copy(alpha = 0.45f),
                                    cursorColor = HoloGold
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("personal_password_access_input")
                            )
                        }
                    }

                    // Selector de Rol / Vocación
                    Text(
                        text = "Tu Vocación o Rol:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFBAE6FD)
                    )

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        roles.forEach { role ->
                            val selected = selectedRole == role
                            FilterChip(
                                selected = selected,
                                onClick = { selectedRole = role },
                                label = {
                                    Text(
                                        text = role,
                                        fontSize = 11.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = HoloCyan.copy(alpha = 0.25f),
                                    selectedLabelColor = HoloCyan,
                                    containerColor = Color.White.copy(alpha = 0.06f),
                                    labelColor = Color.White.copy(alpha = 0.8f)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selected,
                                    borderColor = Color.White.copy(alpha = 0.2f),
                                    selectedBorderColor = HoloCyan
                                )
                            )
                        }
                    }

                    // Campo 3: Ciudad / Comunidad
                    OutlinedTextField(
                        value = cityInput,
                        onValueChange = { cityInput = it },
                        label = { Text("Ciudad o Casa Basiliana", color = Color(0xFFBAE6FD)) },
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = HoloCyan)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = HoloCyan,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                            cursorColor = HoloCyan
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedVisibility(visible = errorMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF7F1D1D).copy(alpha = 0.7f),
                            border = BorderStroke(1.dp, Color(0xFFF87171)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = errorMessage ?: "",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    // Botón Principal de Acceso directo solo con Contraseña (sin códigos ni verificaciones adicionales)
                    Button(
                        onClick = {
                            val cleanEmail = emailInput.trim().lowercase()
                            if (cleanEmail.isBlank() || !cleanEmail.contains("@")) {
                                errorMessage = "Por favor escribe un correo electrónico válido."
                                return@Button
                            }
                            val finalName = fullNameInput.trim().ifBlank {
                                cleanEmail.substringBefore("@")
                                    .replace(".", " ")
                                    .replace("_", " ")
                                    .replaceFirstChar { it.uppercase() }
                            }
                            if (finalName.length < 2) {
                                errorMessage = "Por favor escribe tu nombre para continuar."
                                return@Button
                            }
                            // Verificar o registrar únicamente la Contraseña Personal de la cuenta
                            val (passOk, passError) = viewModel.verifyOrRegisterPersonalAccountPassword(
                                email = cleanEmail,
                                fullName = finalName,
                                passwordInput = personalPasswordInput,
                                isRecognizedBasilian = isRecognizedInDirectory
                            )
                            if (!passOk) {
                                errorMessage = passError ?: "Debes ingresar tu Contraseña Personal (mín. 4 caracteres) para acceder."
                                return@Button
                            }

                            errorMessage = null
                            viewModel.authenticateAndLinkUserByEmail(
                                email = cleanEmail,
                                fullName = finalName,
                                role = selectedRole,
                                city = cityInput.trim().ifBlank { "Colombia" },
                                explicitlySelectedBasilian = isBasilianSelection
                            )
                            onAccessGranted()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HoloCyan,
                            contentColor = Color(0xFF020617)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("btn_submit_email_access")
                    ) {
                        Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ingresar con mi Contraseña",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.5.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Diálogo de perfil personal para subir foto propia, editar nombre y correo, o salir de la cuenta.
 */
@Composable
fun UserLinkedAccountDialog(
    viewModel: BasilianViewModel,
    onDismiss: () -> Unit,
    onReplayIntroVideo: () -> Unit = {}
) {
    val context = LocalContext.current
    val profile by viewModel.currentUserProfile.collectAsState()
    val isBasilianVerified by viewModel.isBasilianPrivateAccess.collectAsState()

    var emailField by remember(profile) { mutableStateOf(profile?.email ?: "") }
    var nameField by remember(profile) { mutableStateOf(profile?.fullName ?: "") }
    var roleField by remember(profile) { mutableStateOf(profile?.role ?: "Novicio") }
    var cityField by remember(profile) { mutableStateOf(profile?.cityAndCountry ?: "Colombia") }
    var photoUri by remember(profile) { mutableStateOf(profile?.profilePhotoUri) }
    var savedBanner by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            val uriString = uri.toString()
            photoUri = uriString
            profile?.let { current ->
                viewModel.saveUserProfile(current.copy(profilePhotoUri = uriString))
            }
            savedBanner = "✅ ¡Tu foto personal ha sido guardada en tu perfil!"
        }
    }

    val HoloCyan = Color(0xFF00F0FF)
    val HoloGold = Color(0xFFF59E0B)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF07152B)),
            border = BorderStroke(1.5.dp, HoloCyan),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = HoloCyan)
                        Column {
                            Text(
                                text = "Mi Perfil Personal C.S.B.",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "Congregación de los Padres Basilianos",
                                style = MaterialTheme.typography.labelSmall,
                                color = HoloGold
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                    }
                }

                // Tarjeta para subir foto personal del usuario
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.07f),
                    border = BorderStroke(1.dp, HoloGold.copy(alpha = 0.55f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(HoloCyan.copy(alpha = 0.35f), Color(0xFF0F172A))
                                    )
                                )
                                .border(2.dp, HoloGold, CircleShape)
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (!photoUri.isNullOrBlank()) {
                                AsyncImage(
                                    model = photoUri,
                                    contentDescription = "Mi foto personal",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text(
                                    text = (nameField.ifBlank { "B" }).take(2).uppercase(),
                                    color = HoloGold,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 24.sp
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = nameField.ifBlank { profile?.fullName ?: "Hermano(a) Basiliano(a)" },
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "«Bonitatem et disciplinam et scientiam doce me»",
                                color = Color(0xFFBAE6FD),
                                fontStyle = FontStyle.Italic,
                                fontSize = 11.sp
                            )
                            OutlinedButton(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, HoloCyan),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = HoloCyan),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("btn_upload_user_profile_photo")
                            ) {
                                Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (photoUri.isNullOrBlank()) "Subir mi foto" else "Cambiar mi foto",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = emailField,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Correo Electrónico Vinculado (Protegido con Clave)", color = HoloCyan) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = HoloGold) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White.copy(alpha = 0.85f),
                        unfocusedTextColor = Color.White.copy(alpha = 0.85f),
                        focusedBorderColor = HoloCyan.copy(alpha = 0.5f),
                        unfocusedBorderColor = HoloCyan.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                var newPersonalPassInput by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = newPersonalPassInput,
                    onValueChange = { newPersonalPassInput = it },
                    label = { Text("Cambiar mi Clave Personal (opcional)", color = HoloGold) },
                    placeholder = { Text("Escribe nueva clave (mín. 4 caracteres)", color = Color.White.copy(alpha = 0.45f)) },
                    leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, tint = HoloGold) },
                    trailingIcon = {
                        if (newPersonalPassInput.trim().length >= 4) {
                            TextButton(
                                onClick = {
                                    val changed = viewModel.changePersonalAccountPassword(
                                        email = emailField,
                                        newPassword = newPersonalPassInput,
                                        fullName = nameField
                                    )
                                    if (changed) {
                                        newPersonalPassInput = ""
                                        savedBanner = "🔐 ¡Tu Clave Personal de acceso ha sido actualizada!"
                                    }
                                }
                            ) {
                                Text("Actualizar", color = HoloGold, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = HoloGold,
                        unfocusedBorderColor = HoloGold.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nameField,
                    onValueChange = { nameField = it },
                    label = { Text("Tu Nombre", color = HoloGold) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = HoloGold,
                        unfocusedBorderColor = HoloGold.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = roleField,
                    onValueChange = { roleField = it },
                    label = { Text("Rol / Vocación", color = Color(0xFFBAE6FD)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = HoloCyan,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Insignia de Nivel de Seguridad y Acceso actual
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isBasilianVerified) Color(0xFF064E3B).copy(alpha = 0.45f) else HoloCyan.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, if (isBasilianVerified) Color(0xFF34D399) else HoloCyan.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isBasilianVerified) Icons.Default.VerifiedUser else Icons.Default.Public,
                            contentDescription = null,
                            tint = if (isBasilianVerified) Color(0xFF34D399) else HoloCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isBasilianVerified) "Acceso Comunidad Religiosa Basiliana C.S.B." else "Acceso Espiritualidad Basiliana (Público)",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp
                            )
                            Text(
                                text = if (isBasilianVerified) {
                                    "Tienes acceso completo a los documentos internos, Regla y contexto comunitario."
                                } else {
                                    "Vista espiritual abierta. Los datos privados de la Congregación están protegidos."
                                },
                                color = Color(0xFFBAE6FD),
                                fontSize = 10.5.sp
                            )
                        }
                    }
                }

                // Botón para volver a ver el Video Futurista de Presentación de los Basilianos
                OutlinedButton(
                    onClick = onReplayIntroVideo,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HoloGold),
                    border = BorderStroke(1.dp, HoloGold.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("btn_replay_intro_video")
                ) {
                    Icon(Icons.Default.PlayCircleFilled, contentDescription = null, modifier = Modifier.size(17.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver Video de Presentación: ¿Quiénes son los Basilianos?", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                if (savedBanner != null) {
                    Text(
                        text = savedBanner!!,
                        color = Color(0xFF4ADE80),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onDismiss()
                            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                viewModel.stopSpeaking()
                                viewModel.logoutUserProfile()
                            }, 80L)
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF87171)),
                        border = BorderStroke(1.dp, Color(0xFFF87171)),
                        modifier = Modifier.weight(1f).testTag("dialog_logout_button")
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Salir de la Cuenta", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (emailField.isNotBlank()) {
                                viewModel.authenticateAndLinkUserByEmail(
                                    email = emailField,
                                    fullName = nameField.ifBlank { "Hermano Basiliano" },
                                    role = roleField,
                                    city = cityField
                                )
                                if (!photoUri.isNullOrBlank()) {
                                    viewModel.currentUserProfile.value?.let { updated ->
                                        viewModel.saveUserProfile(updated.copy(profilePhotoUri = photoUri))
                                    }
                                }
                                savedBanner = "✅ ¡Tu perfil personal ha sido guardado!"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HoloCyan,
                            contentColor = Color(0xFF020617)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Guardar Perfil", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Presentación tipo Video Futurista (de 8.5 segundos, entre 7 y 10 segundos)
 * que se reproduce inmediatamente después de ingresar con el correo,
 * relatando quiénes son los Padres Basilianos (C.S.B.) antes de abrir la página principal.
 */
@Composable
fun BasilianFuturisticIntroVideoScreen(
    userName: String,
    viewModel: BasilianViewModel,
    onVideoFinished: () -> Unit
) {
    val customCrestUri = LocalCustomCrestUri.current
    val totalDurationMs = 8500L
    var elapsedMs by remember { mutableStateOf(0L) }

    val scenes = remember(userName) {
        listOf(
            Triple(
                "1822 • ANNONAY, FRANCIA",
                "¿Quiénes son los Padres Basilianos?",
                "Nacidos en las montañas de Francia en 1822, diez sacerdotes fundaron la Congregación de San Basilio bajo la luz de San Basilio Magno."
            ),
            Triple(
                "CARISMA Y MISIÓN UNIVERSAL",
                "Bondad, Disciplina y Ciencia",
                "«Bonitatem et disciplinam et scientiam doce me». Educando juventudes, universidades y parroquias en Francia, Canadá, EE.UU., México y Colombia."
            ),
            Triple(
                "COMUNIDAD DE AMOR Y ENSEÑANZA",
                "Bienvenido(a), ${userName.ifBlank { "Hermano(a)" }}",
                "Lo que nos une es el amor de Dios, el servicio al ser humano y la búsqueda de la verdad."
            )
        )
    }

    LaunchedEffect(Unit) {
        val narration = "Bienvenido, ${userName.ifBlank { "hermano" }}. Somos la Congregación de los Padres Basilianos, fundada en mil ochocientos veintidós bajo la luz de San Basilio Magno. Enseñamos bondad, disciplina y ciencia con amor y servicio a la humanidad."
        viewModel.speakTextImmediate(narration)

        val step = 100L
        while (elapsedMs < totalDurationMs) {
            delay(step)
            elapsedMs += step
        }
        viewModel.stopSpeaking()
        onVideoFinished()
    }

    val progress = (elapsedMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f)
    val currentSceneIndex = when {
        progress < 0.36f -> 0
        progress < 0.74f -> 1
        else -> 2
    }

    val infiniteTransition = rememberInfiniteTransition(label = "intro_video_fx")
    val ringRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing)),
        label = "ringRotation"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulseScale"
    )

    val HoloCyan = Color(0xFF00F0FF)
    val HoloGold = Color(0xFFF59E0B)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF010613),
                        Color(0xFF04142E),
                        Color(0xFF08224A),
                        Color(0xFF020617)
                    )
                )
            )
            .testTag("basilian_intro_video_screen")
    ) {
        // Fondo holográfico futurista animado tipo cine 3D
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height * 0.38f
            val maxR = size.minDimension * 0.42f

            // Anillos orbitales futuristas
            for (i in 1..4) {
                val r = maxR * (i / 4f) * pulseScale
                drawCircle(
                    color = if (i % 2 == 0) HoloCyan.copy(alpha = 0.22f) else HoloGold.copy(alpha = 0.20f),
                    radius = r,
                    center = Offset(cx, cy),
                    style = Stroke(width = (3f - i * 0.4f).coerceAtLeast(1f))
                )
            }

            // Nodos estelares girando alrededor del escudo
            for (node in 0 until 12) {
                val angleRad = Math.toRadians((ringRotation + node * 30.0))
                val orbitR = maxR * (0.65f + (node % 3) * 0.14f)
                val nx = cx + (orbitR * cos(angleRad)).toFloat()
                val ny = cy + (orbitR * sin(angleRad)).toFloat()
                drawCircle(
                    color = if (node % 2 == 0) HoloCyan else HoloGold,
                    radius = if (node % 3 == 0) 5.5f else 3.5f,
                    center = Offset(nx, ny)
                )
                drawLine(
                    color = HoloCyan.copy(alpha = 0.12f),
                    start = Offset(cx, cy),
                    end = Offset(nx, ny),
                    strokeWidth = 1.2f
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Barra superior: Indicador de Video Futurista + Botón Saltar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = HoloCyan.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, HoloCyan.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Default.PlayCircleFilled,
                            contentDescription = null,
                            tint = HoloCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "HISTORIA VIVA C.S.B. • ${((totalDurationMs - elapsedMs) / 1000L + 1).coerceAtLeast(1)}s",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                TextButton(
                    onClick = {
                        viewModel.stopSpeaking()
                        onVideoFinished()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = HoloGold),
                    modifier = Modifier.testTag("btn_skip_intro_video")
                ) {
                    Text("Saltar →", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                }
            }

            // Centro: Escudo Holográfico + Escena Animada
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size((136 * pulseScale).dp)
                ) {
                    BasilianCrestView(
                        size = 115.dp,
                        customCrestUri = customCrestUri
                    )
                }

                Crossfade(targetState = currentSceneIndex, label = "scene_crossfade") { idx ->
                    val scene = scenes[idx]
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF06152D).copy(alpha = 0.88f)
                        ),
                        border = BorderStroke(
                            1.5.dp,
                            Brush.horizontalGradient(listOf(HoloCyan, HoloGold, HoloCyan))
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(22.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = HoloGold.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, HoloGold)
                            ) {
                                Text(
                                    text = scene.first,
                                    color = HoloGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }

                            Text(
                                text = scene.second,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = scene.third,
                                color = Color(0xFFE0F2FE),
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Parte inferior: Barra de progreso cinematográfica de 8.5 segundos
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    color = HoloCyan,
                    trackColor = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(50))
                )
                Text(
                    text = "Congregación de los Padres Basilianos C.S.B. • Entrando a la plataforma...",
                    color = Color(0xFFBAE6FD).copy(alpha = 0.85f),
                    fontSize = 11.sp
                )
            }
        }
    }
}

