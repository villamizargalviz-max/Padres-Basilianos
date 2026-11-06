package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import com.example.data.security.BasilianSecurityManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

/**
 * Motor Unificado de Voz Humana Profunda, Tranquila y Latinoamericana (Basilio Estándar):
 * - Exactamente 5 voces humanas (3 de mujeres y 2 de hombres).
 * - Hablado natural latinoamericano, profundo, calmado y sin robótica.
 * - Soporte trilingüe fluido en Español (Latinoamérica), Inglés y Francés.
 */
object GoogleCloudTtsService {

    private const val TAG = "BasilioHumanVoice"
    private const val ELEVENLABS_BASE_URL = "https://api.elevenlabs.io/v1/text-to-speech"
    private const val TTS_API_URL = "https://texttospeech.googleapis.com/v1/text:synthesize"
    private const val GEMINI_TTS_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-tts:generateContent"

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(14, TimeUnit.SECONDS)
            .writeTimeout(8, TimeUnit.SECONDS)
            .build()
    }

    private var mediaPlayer: MediaPlayer? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentVoice = MutableStateFlow(CloudVoiceOption.STUDIO_FEMENINA)
    val currentVoice: StateFlow<CloudVoiceOption> = _currentVoice.asStateFlow()

    private val _lastError = MutableStateFlow<String?>(null)
    val lastError: StateFlow<String?> = _lastError.asStateFlow()

    /**
     * Catálogo exclusivo de 5 voces humanas (3 femeninas y 2 masculinas)
     * con acento latinoamericano cálido, timbre profundo y lectura tranquila
     * en Español, Inglés y Francés.
     */
    enum class CloudVoiceOption(
        val voiceName: String,
        val englishVoiceName: String,
        val frenchVoiceName: String,
        val elevenLabsVoiceId: String,
        val geminiVoiceName: String,
        val languageCode: String,
        val displayName: String,
        val styleDescription: String,
        val voiceType: String,
        val isFemale: Boolean,
        val voiceVariantIndex: Int,
        val speakingRate: Double,
        val pitch: Double,
        val supportsSsmlPitch: Boolean
    ) {
        // 1. MUJER 1: Voz femenina profunda, tranquila y cálida (Latinoamérica)
        STUDIO_FEMENINA(
            voiceName = "es-US-Neural2-A",
            englishVoiceName = "en-US-Neural2-F",
            frenchVoiceName = "fr-FR-Neural2-A",
            elevenLabsVoiceId = "21m00Tcm4TlvDq8ikWAM",
            geminiVoiceName = "Aoede",
            languageCode = "es-US",
            displayName = "Camila (Mujer 1) • Profunda y Tranquila",
            styleDescription = "Voz femenina latinoamericana, profunda, serena y natural • Habla Español, Inglés y Francés",
            voiceType = "Humana LatAm • ES / EN / FR",
            isFemale = true,
            voiceVariantIndex = 0,
            speakingRate = 0.88,
            pitch = -1.2,
            supportsSsmlPitch = true
        ),

        // 2. MUJER 2: Voz femenina serena, maternal y acogedora (Latinoamérica)
        NEURAL2_DEVOCIONAL(
            voiceName = "es-US-Wavenet-A",
            englishVoiceName = "en-US-Neural2-C",
            frenchVoiceName = "fr-FR-Neural2-C",
            elevenLabsVoiceId = "EXAVITQu4vr4xnSDxMaL",
            geminiVoiceName = "Kore",
            languageCode = "es-US",
            displayName = "Valeria (Mujer 2) • Serena y Maternal",
            styleDescription = "Voz femenina latinoamericana, cálida, pausada y consoladora • Habla Español, Inglés y Francés",
            voiceType = "Humana LatAm • ES / EN / FR",
            isFemale = true,
            voiceVariantIndex = 1,
            speakingRate = 0.87,
            pitch = -0.8,
            supportsSsmlPitch = true
        ),

        // 3. MUJER 3: Voz femenina contemplativa, suave y reflexiva (Latinoamérica)
        WAVENET_CONSOLADORA(
            voiceName = "es-US-Neural2-A",
            englishVoiceName = "en-US-Neural2-E",
            frenchVoiceName = "fr-FR-Neural2-E",
            elevenLabsVoiceId = "XrExE9yKIg1WjnnlVkGX",
            geminiVoiceName = "Leda",
            languageCode = "es-US",
            displayName = "Elena (Mujer 3) • Contemplativa y Suave",
            styleDescription = "Voz femenina latinoamericana, tranquila, empática y profunda • Habla Español, Inglés y Francés",
            voiceType = "Humana LatAm • ES / EN / FR",
            isFemale = true,
            voiceVariantIndex = 2,
            speakingRate = 0.86,
            pitch = -1.6,
            supportsSsmlPitch = true
        ),

        // 4. HOMBRE 1: Voz masculina profunda, tranquila y fraterna (Latinoamérica)
        STUDIO_MASCULINA(
            voiceName = "es-US-Studio-B",
            englishVoiceName = "en-US-Studio-Q",
            frenchVoiceName = "fr-FR-Studio-D",
            elevenLabsVoiceId = "nPczCjzI2devNBz1zQrb",
            geminiVoiceName = "Charon",
            languageCode = "es-US",
            displayName = "Mateo (Hombre 1) • Profundo y Tranquilo",
            styleDescription = "Voz masculina latinoamericana, grave, calmada y muy humana • Habla Español, Inglés y Francés",
            voiceType = "Humana LatAm • ES / EN / FR",
            isFemale = false,
            voiceVariantIndex = 0,
            speakingRate = 0.88,
            pitch = -1.4,
            supportsSsmlPitch = false
        ),

        // 5. HOMBRE 2: Voz masculina sabia, pastoral y reposada (Latinoamérica)
        NEURAL2_PASTORAL(
            voiceName = "es-US-Neural2-B",
            englishVoiceName = "en-US-Neural2-D",
            frenchVoiceName = "fr-FR-Neural2-B",
            elevenLabsVoiceId = "ErXwobaYiN019PkySvjV",
            geminiVoiceName = "Orus",
            languageCode = "es-US",
            displayName = "Santiago (Hombre 2) • Sabio y Pastoral",
            styleDescription = "Voz masculina latinoamericana, serena, cercana y reflexiva • Habla Español, Inglés y Francés",
            voiceType = "Humana LatAm • ES / EN / FR",
            isFemale = false,
            voiceVariantIndex = 1,
            speakingRate = 0.87,
            pitch = -1.8,
            supportsSsmlPitch = true
        );

        companion object {
            fun findByName(name: String): CloudVoiceOption {
                values().find { it.name == name || it.voiceName == name }?.let { return it }
                return when (name) {
                    "JOURNEY_REFLEXIVA" -> WAVENET_CONSOLADORA
                    "STUDIO_ESPANA_MASCULINA", "JOURNEY_SOLEMNE", "WAVENET_BASILIANA" -> NEURAL2_PASTORAL
                    else -> STUDIO_FEMENINA
                }
            }
        }
    }

    private fun isPlaceholderKey(key: String): Boolean {
        return key.isBlank() ||
            key.startsWith("MY_") ||
            key.startsWith("YOUR_") ||
            key.equals("PLACEHOLDER", ignoreCase = true)
    }

    private fun isSkTtsKey(key: String): Boolean {
        return key.startsWith("sk-tts-", ignoreCase = true) ||
            key.startsWith("sk_", ignoreCase = true) ||
            key.startsWith("sk-", ignoreCase = true)
    }

    fun getEffectiveElevenLabsKey(context: Context): String {
        val encrypted = BasilianSecurityManager.getDecryptedSecret(context, "encrypted_elevenlabs_api_key").trim()
        if (!isPlaceholderKey(encrypted) && !encrypted.startsWith("AIza")) {
            return encrypted
        }

        val cloudPrefs = context.getSharedPreferences("basilian_cloud_tts_prefs", Context.MODE_PRIVATE)
        val customElevenKey1 = cloudPrefs.getString("custom_elevenlabs_api_key", null)?.trim().orEmpty()
        if (!isPlaceholderKey(customElevenKey1) && !customElevenKey1.startsWith("AIza")) {
            return customElevenKey1
        }

        val appPrefs = context.getSharedPreferences("basilian_prefs", Context.MODE_PRIVATE)
        val customElevenKey2 = appPrefs.getString("custom_elevenlabs_api_key", null)?.trim().orEmpty()
        if (!isPlaceholderKey(customElevenKey2) && !customElevenKey2.startsWith("AIza")) {
            return customElevenKey2
        }

        val customTtsKey = cloudPrefs.getString("custom_tts_api_key", null)?.trim().orEmpty()
        if (!isPlaceholderKey(customTtsKey) && isSkTtsKey(customTtsKey)) {
            return customTtsKey
        }

        try {
            val buildElevenKey = BuildConfig.ELEVENLABS_API_KEY.trim()
            if (!isPlaceholderKey(buildElevenKey) && !buildElevenKey.startsWith("AIza")) {
                return buildElevenKey
            }
        } catch (_: Exception) {}

        try {
            val buildTtsKey = BuildConfig.GOOGLE_CLOUD_TTS_API_KEY.trim()
            if (!isPlaceholderKey(buildTtsKey) && isSkTtsKey(buildTtsKey)) {
                return buildTtsKey
            }
        } catch (_: Exception) {}

        return ""
    }

    /**
     * Obtiene exclusivamente la clave de Google Cloud (AIza...) sin mezclarla con claves TTS (sk-tts-... / sk_...),
     * evitando conflictos cuando ambas APIs están configuradas.
     */
    fun getEffectiveGoogleApiKey(context: Context): String {
        val encryptedGoogle = BasilianSecurityManager.getDecryptedSecret(context, "encrypted_google_tts_api_key").trim()
        if (!isPlaceholderKey(encryptedGoogle) && !isSkTtsKey(encryptedGoogle)) {
            return encryptedGoogle
        }

        val prefs = context.getSharedPreferences("basilian_cloud_tts_prefs", Context.MODE_PRIVATE)
        val customKey = prefs.getString("custom_tts_api_key", null)?.trim().orEmpty()
        if (!isPlaceholderKey(customKey) && !isSkTtsKey(customKey)) {
            return customKey
        }

        try {
            val buildTtsKey = BuildConfig.GOOGLE_CLOUD_TTS_API_KEY.trim()
            if (!isPlaceholderKey(buildTtsKey) && !isSkTtsKey(buildTtsKey)) {
                return buildTtsKey
            }
        } catch (_: Exception) {}

        try {
            val geminiKey = BuildConfig.GEMINI_API_KEY.trim()
            if (!isPlaceholderKey(geminiKey) && !isSkTtsKey(geminiKey)) {
                return geminiKey
            }
        } catch (_: Exception) {}

        return ""
    }

    fun getEffectiveApiKey(context: Context): String {
        val elevenKey = getEffectiveElevenLabsKey(context)
        if (elevenKey.isNotBlank()) return elevenKey
        return getEffectiveGoogleApiKey(context)
    }

    fun setCustomApiKey(context: Context, key: String) {
        val trimmed = key.trim().removeSurrounding("\"").removeSurrounding("'")
        val prefs = context.getSharedPreferences("basilian_cloud_tts_prefs", Context.MODE_PRIVATE).edit()
        val appPrefs = context.getSharedPreferences("basilian_prefs", Context.MODE_PRIVATE).edit()
        if (trimmed.isBlank()) {
            BasilianSecurityManager.saveEncryptedSecret(context, "encrypted_elevenlabs_api_key", "")
            BasilianSecurityManager.saveEncryptedSecret(context, "encrypted_google_tts_api_key", "")
            prefs.remove("custom_elevenlabs_api_key").remove("custom_tts_api_key").apply()
            appPrefs.remove("custom_elevenlabs_api_key").apply()
            _lastError.value = null
            return
        }
        if (trimmed.startsWith("AIza")) {
            BasilianSecurityManager.saveEncryptedSecret(context, "encrypted_google_tts_api_key", trimmed)
            prefs.putString("custom_tts_api_key", trimmed).remove("custom_elevenlabs_api_key").apply()
            appPrefs.remove("custom_elevenlabs_api_key").apply()
        } else {
            BasilianSecurityManager.saveEncryptedSecret(context, "encrypted_elevenlabs_api_key", trimmed)
            prefs.putString("custom_elevenlabs_api_key", trimmed).remove("custom_tts_api_key").apply()
            appPrefs.putString("custom_elevenlabs_api_key", trimmed).apply()
        }
        try {
            File(context.cacheDir, "cloud_tts_cache").deleteRecursively()
        } catch (_: Exception) {}
        _lastError.value = null
    }

    fun clearCustomApiKeysAndCache(context: Context) {
        setCustomApiKey(context, "")
        try {
            File(context.cacheDir, "cloud_tts_cache").deleteRecursively()
        } catch (_: Exception) {}
    }

    fun selectVoice(voice: CloudVoiceOption, context: Context? = null) {
        _currentVoice.value = voice
        HumanVoiceEngine.selectedVoiceOption = voice
        context?.getSharedPreferences("basilian_cloud_tts_prefs", Context.MODE_PRIVATE)
            ?.edit()
            ?.putString("selected_cloud_voice", voice.name)
            ?.apply()
    }

    fun loadSavedVoice(context: Context) {
        val prefs = context.getSharedPreferences("basilian_cloud_tts_prefs", Context.MODE_PRIVATE)
        val savedName = prefs.getString("selected_cloud_voice", CloudVoiceOption.STUDIO_FEMENINA.name)
        val resolved = CloudVoiceOption.findByName(savedName ?: "")
        _currentVoice.value = resolved
        HumanVoiceEngine.selectedVoiceOption = resolved
    }

    fun buildNaturalSsml(rawText: String, voice: CloudVoiceOption): String {
        val detectedLang = HumanVoiceEngine.detectLanguageCode(rawText)
        val cleanText = HumanVoiceEngine.humanizeTextForSpeech(rawText, detectedLang)
            .replace("&", when (detectedLang) {
                "en" -> " and "
                "fr" -> " et "
                else -> " y "
            })
            .replace("<", "")
            .replace(">", "")
            .replace("\"", "")
            .trim()

        if (cleanText.isBlank()) return "<speak></speak>"

        val ratePercent = (voice.speakingRate * 100).toInt().coerceIn(82, 94)
        val prosodyOpen = if (voice.supportsSsmlPitch && voice.pitch != 0.0) {
            val sign = if (voice.pitch > 0) "+" else ""
            """<prosody rate="${ratePercent}%" pitch="${sign}${voice.pitch}st" volume="medium">"""
        } else {
            """<prosody rate="${ratePercent}%" volume="medium">"""
        }

        val paragraphs = cleanText.split(Regex("\\n+")).map { it.trim() }.filter { it.isNotEmpty() }
        val bodyBuilder = StringBuilder()
        paragraphs.forEachIndexed { pIndex, paragraph ->
            bodyBuilder.append("<p>")
            val sentences = paragraph.split(Regex("(?<=[.!?…])\\s+")).map { it.trim() }.filter { it.isNotEmpty() }
            sentences.forEachIndexed { sIndex, sentence ->
                val phrasedSentence = sentence
                    .replace(Regex(",\\s+"), """, <break time="230ms"/> """)
                    .replace(Regex(";\\s+"), """; <break time="310ms"/> """)
                    .replace(Regex(":\\s+"), """: <break time="280ms"/> """)

                bodyBuilder.append("<s>").append(phrasedSentence).append("</s>")
                if (sIndex < sentences.lastIndex) {
                    bodyBuilder.append("""<break time="430ms"/>""")
                }
            }
            bodyBuilder.append("</p>")
            if (pIndex < paragraphs.lastIndex) {
                bodyBuilder.append("""<break time="580ms"/>""")
            }
        }

        return """<speak>$prosodyOpen$bodyBuilder</prosody></speak>"""
    }

    suspend fun speakOrFallback(
        context: Context,
        textToRead: String,
        voice: CloudVoiceOption = _currentVoice.value,
        onLocalFallback: () -> Unit
    ) {
        stop()
        _lastError.value = null
        HumanVoiceEngine.selectedVoiceOption = voice

        val detectedLang = HumanVoiceEngine.detectLanguageCode(textToRead)
        val humanizedText = HumanVoiceEngine.humanizeTextForSpeech(textToRead, detectedLang)
        if (humanizedText.isBlank()) {
            onLocalFallback()
            return
        }

        val elevenKey = getEffectiveElevenLabsKey(context)
        val googleKey = getEffectiveGoogleApiKey(context)

        if (elevenKey.isBlank() && googleKey.isBlank()) {
            withContext(Dispatchers.Main) {
                onLocalFallback()
            }
            return
        }

        _isLoading.value = true

        try {
            val audioFile = getOrSynthesizeHumanAudio(
                context = context,
                text = humanizedText,
                detectedLang = detectedLang,
                voice = voice,
                elevenLabsKey = elevenKey,
                googleApiKey = googleKey
            )
            if (audioFile != null && audioFile.exists() && audioFile.length() > 250) {
                val startedOk = withContext(Dispatchers.Main) {
                    playAudioFile(context, audioFile)
                }
                if (!startedOk) {
                    try { audioFile.delete() } catch (_: Exception) {}
                    withContext(Dispatchers.Main) {
                        onLocalFallback()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    onLocalFallback()
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Usando motor local humanizado tras aviso de red: ${e.message}")
            _lastError.value = e.message
            withContext(Dispatchers.Main) {
                onLocalFallback()
            }
        } finally {
            _isLoading.value = false
        }
    }

    private suspend fun getOrSynthesizeHumanAudio(
        context: Context,
        text: String,
        detectedLang: String,
        voice: CloudVoiceOption,
        elevenLabsKey: String,
        googleApiKey: String
    ): File? = withContext(Dispatchers.IO) {
        val cacheDir = File(context.cacheDir, "cloud_tts_cache").apply { mkdirs() }
        val hash = sha256("basilio_human_v5_${voice.name}_${detectedLang}_${text.hashCode()}_${text.length}")
        val mp3CacheFile = File(cacheDir, "$hash.mp3")
        val wavCacheFile = File(cacheDir, "$hash.wav")

        if (mp3CacheFile.exists() && mp3CacheFile.length() > 500) {
            return@withContext mp3CacheFile
        }
        if (wavCacheFile.exists() && wavCacheFile.length() > 800) {
            return@withContext wavCacheFile
        }

        // 1. PRIORIDAD MÁXIMA: Síntesis Multilingüe Hiperrealista (ES LatAm / EN / FR)
        if (elevenLabsKey.isNotBlank()) {
            try {
                val elevenMp3 = synthesizeWithMultilingualVoiceApi(text, voice, elevenLabsKey, mp3CacheFile)
                if (elevenMp3 != null && elevenMp3.exists() && elevenMp3.length() > 500) {
                    return@withContext elevenMp3
                }
            } catch (e: Exception) {
                Log.d(TAG, "API de voz primaria aviso (${e.message}), pasando a síntesis neural profunda...")
            }
        }

        // 2. Síntesis Neural Basilio Estándar con voz humana profunda, tranquila y trilingüe (ES LatAm / EN / FR)
        if (googleApiKey.isNotBlank()) {
            try {
                val geminiWav = synthesizeWithDeepCalmHumanTts(text, detectedLang, voice, googleApiKey, wavCacheFile)
                if (geminiWav != null && geminiWav.exists() && geminiWav.length() > 800) {
                    return@withContext geminiWav
                }
            } catch (e: Exception) {
                Log.d(TAG, "Síntesis neural profunda aviso (${e.message}), pasando a Cloud Studio SSML...")
            }

            // 3. Google Cloud Text-to-Speech API (Voces Studio / Neural2 + SSML profundo y tranquilo)
            try {
                val cloudMp3 = synthesizeWithGoogleCloudStudioSsml(text, detectedLang, voice, googleApiKey, mp3CacheFile)
                if (cloudMp3 != null && cloudMp3.exists() && cloudMp3.length() > 500) {
                    return@withContext cloudMp3
                }
            } catch (e: Exception) {
                Log.d(TAG, "Cloud TTS Studio SSML aviso: ${e.message}")
            }
        }

        return@withContext null
    }

    /**
     * Síntesis de voz multilingüe (`eleven_multilingual_v2`) afinada con alta estabilidad y calidez
     * para sonar profunda, tranquila, latinoamericana y nada robótica en Español, Inglés y Francés.
     */
    private fun synthesizeWithMultilingualVoiceApi(
        text: String,
        voice: CloudVoiceOption,
        apiKey: String,
        outputMp3File: File
    ): File? {
        val cleanKey = apiKey.trim().removeSurrounding("\"").removeSurrounding("'")
        if (cleanKey.isBlank()) return null

        val clippedText = if (text.length > 1100) text.take(1100) else text
        val candidateModels = listOf(
            "eleven_multilingual_v2",
            "eleven_flash_v2_5"
        )
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val url = BasilianSecurityManager.enforceHttpsUrl("$ELEVENLABS_BASE_URL/${voice.elevenLabsVoiceId}")

        for (modelId in candidateModels) {
            try {
                val payload = JSONObject().apply {
                    put("text", clippedText)
                    put("model_id", modelId)
                    put("voice_settings", JSONObject().apply {
                        // Alta estabilidad y similitud para lograr una voz humana profunda, serena y sin saltos robóticos
                        put("stability", 0.64)
                        put("similarity_boost", 0.86)
                        if (modelId == "eleven_multilingual_v2") {
                            put("style", 0.18)
                            put("use_speaker_boost", true)
                        }
                    })
                }

                val request = Request.Builder()
                    .url(url)
                    .post(payload.toString().toRequestBody(mediaType))
                    .addHeader("xi-api-key", cleanKey)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "audio/mpeg")
                    .build()

                httpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val bytes = response.body?.bytes()
                        if (bytes != null && bytes.size >= 500) {
                            FileOutputStream(outputMp3File).use { fos ->
                                fos.write(bytes)
                                fos.flush()
                            }
                            return outputMp3File
                        }
                    } else {
                        Log.w(TAG, "Multilingual Voice model $modelId HTTP ${response.code}")
                        if (response.code == 401 || response.code == 403) {
                            _lastError.value = "Usando motor de voz humana Basilio Estándar (clave externa sin autorización)"
                            return null
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Multilingual Voice model $modelId exception: ${e.message}")
            }
        }
        return null
    }

    private fun synthesizeWithGoogleCloudStudioSsml(
        text: String,
        detectedLang: String,
        voice: CloudVoiceOption,
        apiKey: String,
        outputMp3File: File
    ): File? {
        val ssmlPayload = buildNaturalSsml(text, voice)

        val (targetLangCode, targetVoiceName) = when (detectedLang) {
            "en" -> "en-US" to voice.englishVoiceName
            "fr" -> "fr-FR" to voice.frenchVoiceName
            else -> voice.languageCode to voice.voiceName
        }

        val supportsPitchForTarget = !targetVoiceName.contains("Studio", ignoreCase = true)

        val jsonPayload = JSONObject().apply {
            put("input", JSONObject().apply {
                put("ssml", ssmlPayload)
            })
            put("voice", JSONObject().apply {
                put("languageCode", targetLangCode)
                put("name", targetVoiceName)
            })
            put("audioConfig", JSONObject().apply {
                put("audioEncoding", "MP3")
                put("speakingRate", voice.speakingRate)
                if (supportsPitchForTarget && voice.pitch != 0.0) {
                    put("pitch", voice.pitch)
                }
                put("volumeGainDb", 0.5)
                // Perfil acústico con cuerpo grave y cálido para evitar sonido metálico o robótico
                put("effectsProfileId", JSONArray().put("large-home-entertainment-class-device"))
            })
        }

        val requestUrl = "$TTS_API_URL?key=$apiKey"
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url(requestUrl)
            .post(jsonPayload.toString().toRequestBody(mediaType))
            .addHeader("Content-Type", "application/json")
            .build()

        val response = httpClient.newCall(request).execute()
        if (response.isSuccessful) {
            val responseJson = JSONObject(response.body?.string() ?: "")
            val base64Audio = responseJson.optString("audioContent")
            if (!base64Audio.isNullOrBlank()) {
                val audioBytes = Base64.decode(base64Audio, Base64.DEFAULT)
                FileOutputStream(outputMp3File).use { fos ->
                    fos.write(audioBytes)
                    fos.flush()
                }
                return outputMp3File
            }
        }

        val fallbackVoiceName = when (detectedLang) {
            "en" -> if (voice.isFemale) "en-US-Neural2-F" else "en-US-Neural2-D"
            "fr" -> if (voice.isFemale) "fr-FR-Neural2-A" else "fr-FR-Neural2-B"
            else -> if (voice.isFemale) "es-US-Neural2-A" else "es-US-Neural2-B"
        }

        val minimalSsml = "<speak>" +
            text.replace("&", " ").replace("<", "").replace(">", "")
                .replace(Regex("(?<=[.!?…])\\s+"), """ <break time="420ms"/> """)
                .replace(Regex(",\\s+"), """, <break time="220ms"/> """) +
            "</speak>"

        val fallbackPayload = JSONObject().apply {
            put("input", JSONObject().apply {
                put("ssml", minimalSsml)
            })
            put("voice", JSONObject().apply {
                put("languageCode", targetLangCode)
                put("name", fallbackVoiceName)
            })
            put("audioConfig", JSONObject().apply {
                put("audioEncoding", "MP3")
                put("speakingRate", 0.87)
                put("pitch", if (voice.isFemale) -1.0 else -1.6)
            })
        }

        val retryRequest = Request.Builder()
            .url(requestUrl)
            .post(fallbackPayload.toString().toRequestBody(mediaType))
            .addHeader("Content-Type", "application/json")
            .build()

        val retryResponse = httpClient.newCall(retryRequest).execute()
        if (!retryResponse.isSuccessful) {
            return null
        }

        val retryJson = JSONObject(retryResponse.body?.string() ?: "")
        val retryBase64 = retryJson.optString("audioContent")
        if (retryBase64.isNullOrBlank()) return null

        val retryBytes = Base64.decode(retryBase64, Base64.DEFAULT)
        FileOutputStream(outputMp3File).use { fos ->
            fos.write(retryBytes)
            fos.flush()
        }
        return outputMp3File
    }

    private fun synthesizeWithDeepCalmHumanTts(
        text: String,
        detectedLang: String,
        voice: CloudVoiceOption,
        apiKey: String,
        outputWavFile: File
    ): File? {
        val clippedText = if (text.length > 1600) text.take(1600) else text
        val genderPersona = if (voice.isFemale) {
            "una mujer latinoamericana de voz profunda, muy humana, serena, tranquila, cálida y maternal"
        } else {
            "un hombre latinoamericano de voz grave, profunda, muy humana, serena, tranquila y fraterna"
        }
        val languageInstruction = when (detectedLang) {
            "en" -> "en inglés fluido con calidez latinoamericana suave"
            "fr" -> "en francés fluido, cálido y natural"
            else -> "en español latinoamericano neutro, natural y cercano"
        }
        val promptText = "Lee el siguiente texto $languageInstruction como $genderPersona. " +
            "Evita por completo cualquier entonación robótica o acelerada; habla con calma, profundidad, " +
            "respiración humana natural y pausas serenas:\n\n$clippedText"

        val payload = JSONObject().apply {
            put("contents", JSONArray().put(
                JSONObject().put("parts", JSONArray().put(
                    JSONObject().put("text", promptText)
                ))
            ))
            put("generationConfig", JSONObject().apply {
                put("responseModalities", JSONArray().put("AUDIO"))
                put("speechConfig", JSONObject().apply {
                    put("voiceConfig", JSONObject().apply {
                        put("prebuiltVoiceConfig", JSONObject().apply {
                            put("voiceName", voice.geminiVoiceName)
                        })
                    })
                })
            })
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url("$GEMINI_TTS_URL?key=$apiKey")
            .post(payload.toString().toRequestBody(mediaType))
            .addHeader("Content-Type", "application/json")
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) {
            return null
        }

        val bodyStr = response.body?.string().orEmpty()
        val root = JSONObject(bodyStr)
        val candidates = root.optJSONArray("candidates") ?: return null
        if (candidates.length() == 0) return null
        val parts = candidates.getJSONObject(0)
            .optJSONObject("content")
            ?.optJSONArray("parts") ?: return null
        if (parts.length() == 0) return null

        val inlineData = parts.getJSONObject(0).optJSONObject("inlineData") ?: return null
        val base64Pcm = inlineData.optString("data")
        if (base64Pcm.isNullOrBlank()) return null

        val pcmBytes = Base64.decode(base64Pcm, Base64.DEFAULT)
        val wavBytes = pcm24kMonoToWav(pcmBytes, sampleRate = 24000)

        FileOutputStream(outputWavFile).use { fos ->
            fos.write(wavBytes)
            fos.flush()
        }
        return outputWavFile
    }

    private fun pcm24kMonoToWav(pcmData: ByteArray, sampleRate: Int = 24000): ByteArray {
        val channels = 1
        val bitsPerSample = 16
        val byteRate = sampleRate * channels * bitsPerSample / 8
        val totalDataLen = pcmData.size + 36

        val header = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN).apply {
            put("RIFF".toByteArray(Charsets.US_ASCII))
            putInt(totalDataLen)
            put("WAVE".toByteArray(Charsets.US_ASCII))
            put("fmt ".toByteArray(Charsets.US_ASCII))
            putInt(16)
            putShort(1)
            putShort(channels.toShort())
            putInt(sampleRate)
            putInt(byteRate)
            putShort((channels * bitsPerSample / 8).toShort())
            putShort(bitsPerSample.toShort())
            put("data".toByteArray(Charsets.US_ASCII))
            putInt(pcmData.size)
        }.array()

        val out = ByteArrayOutputStream(44 + pcmData.size)
        out.write(header)
        out.write(pcmData)
        return out.toByteArray()
    }

    private fun requestSpeechAudioFocus(context: Context) {
        try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val focusReq = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build()
                    )
                    .build()
                audioManager.requestAudioFocus(focusReq)
            } else {
                @Suppress("DEPRECATION")
                audioManager.requestAudioFocus(
                    null,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
                )
            }
        } catch (_: Exception) {}
    }

    private fun playAudioFile(context: Context, file: File): Boolean {
        return try {
            requestSpeechAudioFocus(context)
            mediaPlayer?.release()
            val mp = MediaPlayer()
            mp.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            mp.setDataSource(file.absolutePath)
            mp.setOnCompletionListener {
                _isPlaying.value = false
            }
            mp.setOnErrorListener { _, what, extra ->
                Log.e(TAG, "Error en MediaPlayer: what=$what, extra=$extra")
                _isPlaying.value = false
                false
            }
            mp.prepare()
            mp.setVolume(1.0f, 1.0f)
            mp.start()
            mediaPlayer = mp
            _isPlaying.value = true
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error preparando/iniciando MediaPlayer: ${e.message}", e)
            _isPlaying.value = false
            false
        }
    }

    fun stop() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            Log.w(TAG, "Error al detener MediaPlayer: ${e.message}")
        } finally {
            _isPlaying.value = false
            _isLoading.value = false
        }
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
