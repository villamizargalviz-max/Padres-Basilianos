package com.example.audio

import android.media.AudioAttributes
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import java.util.Locale

/**
 * Motor de Voz Humana Profunda, Tranquila y Latinoamericana (Basilio Estándar).
 * Diseñado para ofrecer una lectura cercana a la voz humana real en Español (Latinoamérica),
 * Inglés y Francés con las 5 voces de la app (3 de mujeres y 2 de hombres):
 * 1. Detecta automáticamente el idioma del texto (Español LatAm, Inglés o Francés).
 * 2. Selecciona voces neuronales cálidas acordes al género (3 femeninas, 2 masculinas) y timbre profundo.
 * 3. Elimina emojis y símbolos gráficos para evitar lecturas robóticas.
 * 4. Divide el texto por frases e inserta pausas de respiración humana serena.
 */
object HumanVoiceEngine {

    private const val TAG = "HumanVoiceEngine"

    enum class VoicePace(val label: String, val speechRate: Float, val pitch: Float) {
        ORANTE_SERENA("Profunda, Tranquila y Humana", 0.88f, 0.94f),
        CALIDA_PASTORAL("Cercana y Latinoamericana", 0.90f, 0.96f),
        CONTEMPLATIVA_MONASTICA("Pausada y Contemplativa", 0.85f, 0.91f)
    }

    var currentPace: VoicePace = VoicePace.ORANTE_SERENA
    var selectedVoiceOption: GoogleCloudTtsService.CloudVoiceOption =
        GoogleCloudTtsService.CloudVoiceOption.STUDIO_FEMENINA

    /**
     * Detecta si el texto está en Español ("es"), Inglés ("en") o Francés ("fr")
     * para que las 5 voces hablen con pronunciación nativa natural en los tres idiomas.
     */
    fun detectLanguageCode(rawText: String): String {
        if (rawText.isBlank()) return "es"
        val lower = " " + rawText.lowercase()
            .replace(Regex("[^a-záéíóúüñàâäçéèêëîïôöùûüÿœæ'\\s]"), " ") + " "

        val frenchTokens = listOf(
            " le ", " la ", " les ", " des ", " une ", " est ", " dans ", " pour ", " avec ",
            " sur ", " pas ", " plus ", " vous ", " nous ", " c'est ", " qu'", " d'", " l'",
            " bonjour ", " merci ", " frère ", " frères ", " prière ", " église ", " seigneur ",
            " évangile ", " saint ", " mais ", " sont ", " cette ", " tout ", " comme "
        )
        val englishTokens = listOf(
            " the ", " and ", " that ", " have ", " for ", " not ", " with ", " you ", " this ",
            " but ", " his ", " from ", " they ", " say ", " her ", " she ", " will ", " one ",
            " all ", " would ", " there ", " their ", " what ", " about ", " which ", " when ",
            " hello ", " father ", " brother ", " prayer ", " gospel ", " lord ", " church ",
            " how ", " can ", " your ", " are ", " was ", " were "
        )
        val spanishTokens = listOf(
            " el ", " la ", " los ", " las ", " de ", " que ", " en ", " un ", " una ", " por ",
            " con ", " para ", " es ", " del ", " como ", " más ", " pero ", " sus ", " ya ",
            " este ", " porque ", " esta ", " entre ", " cuando ", " muy ", " sin ", " sobre ",
            " también ", " hasta ", " hay ", " donde ", " quien ", " desde ", " todo ", " nos ",
            " hola ", " padre ", " hermano ", " oración ", " evangelio ", " señor ", " iglesia ",
            " qué ", " cómo ", " está ", " están ", " Dios "
        )

        var frScore = 0
        var enScore = 0
        var esScore = 0

        frenchTokens.forEach { if (lower.contains(it)) frScore += 2 }
        englishTokens.forEach { if (lower.contains(it)) enScore += 2 }
        spanishTokens.forEach { if (lower.contains(it)) esScore += 2 }

        if (lower.contains("ñ") || lower.contains("¿") || lower.contains("¡")) esScore += 5
        if (lower.contains("ç") || lower.contains("œ") || lower.contains("d'") || lower.contains("l'") || lower.contains("qu'")) frScore += 4

        return when {
            frScore > esScore && frScore >= enScore && frScore >= 3 -> "fr"
            enScore > esScore && enScore > frScore && enScore >= 3 -> "en"
            else -> "es"
        }
    }

    /**
     * Configura la instancia de TTS con la voz más humana, profunda, tranquila y latinoamericana
     * disponible en el dispositivo, respetando las 5 voces (3 mujeres y 2 hombres) y los 3 idiomas (ES, EN, FR).
     */
    fun configureHumanVoice(
        tts: TextToSpeech,
        preferredLocale: Locale = Locale("es", "CO"),
        pace: VoicePace = currentPace,
        voiceOption: GoogleCloudTtsService.CloudVoiceOption = selectedVoiceOption,
        detectedLanguage: String = preferredLocale.language.lowercase(),
        onReady: (() -> Unit)? = null
    ) {
        try {
            val targetLang = when (detectedLanguage) {
                "en" -> "en"
                "fr" -> "fr"
                else -> "es"
            }

            // Configurar el idioma con preferencia por acento latinoamericano cálido en español
            when (targetLang) {
                "en" -> {
                    var res = tts.setLanguage(Locale.US)
                    if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts.setLanguage(Locale.UK)
                    }
                }
                "fr" -> {
                    var res = tts.setLanguage(Locale.FRANCE)
                    if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts.setLanguage(Locale.CANADA_FRENCH)
                    }
                }
                else -> {
                    var res = tts.setLanguage(Locale("es", "CO"))
                    if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                        res = tts.setLanguage(Locale("es", "MX"))
                    }
                    if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                        res = tts.setLanguage(Locale("es", "US"))
                    }
                    if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts.setLanguage(Locale("es"))
                    }
                }
            }

            val availableVoices = tts.voices
            if (!availableVoices.isNullOrEmpty()) {
                val langVoices = availableVoices.filter {
                    it.locale.language.equals(targetLang, ignoreCase = true)
                }

                if (langVoices.isNotEmpty()) {
                    val wantsFemale = voiceOption.isFemale
                    val rankedVoices = langVoices.sortedWith(
                        compareByDescending<Voice> { voice ->
                            val name = voice.name.lowercase()
                            val country = voice.locale.country.uppercase()
                            var score = 0

                            // Priorizar voces neurales/naturales de alta fidelidad humana
                            if (name.contains("natural") || name.contains("neural") || name.contains("studio")) score += 140
                            if (name.contains("wavenet") || name.contains("journey")) score += 110
                            if (!name.contains("legacy")) score += 20

                            // Identificación de género femenino vs masculino en motores Android / Google TTS
                            val isLikelyFemale = name.contains("female") ||
                                name.contains("sfb") || name.contains("sfc") || name.contains("sfd") ||
                                name.contains("-a-") || name.endsWith("-a") || name.endsWith("-c") || name.endsWith("-f") ||
                                name.contains("iob") || name.contains("ioc")
                            val isLikelyMale = name.contains("male") ||
                                name.contains("sfa") || name.contains("sfe") || name.contains("sfg") ||
                                name.contains("-b-") || name.endsWith("-b") || name.endsWith("-d") ||
                                name.contains("iod") || name.contains("iom")

                            if (wantsFemale && isLikelyFemale) score += 85
                            if (!wantsFemale && isLikelyMale) score += 85

                            // Priorizar hablado latinoamericano cálido cuando es español
                            if (targetLang == "es") {
                                when (country) {
                                    "CO", "MX", "US", "419", "AR", "CL", "PE", "VE" -> score += 65
                                    else -> score += 10
                                }
                            } else if (targetLang == "en" && country == "US") {
                                score += 40
                            } else if (targetLang == "fr" && (country == "FR" || country == "CA")) {
                                score += 40
                            }

                            if (name.contains("local")) score += 15
                            score
                        }.thenByDescending { it.quality }
                            .thenBy { if (it.features?.contains(TextToSpeech.Engine.KEY_FEATURE_NOT_INSTALLED) == true) 1 else 0 }
                    )

                    val chosenVoice = if (rankedVoices.size > voiceOption.voiceVariantIndex) {
                        val candidate = rankedVoices[voiceOption.voiceVariantIndex]
                        val top = rankedVoices.first()
                        // Usar la variante si mantiene alta calidad; si no, usar la mejor con ajuste de tono profundo
                        if (candidate.quality >= top.quality) candidate else top
                    } else {
                        rankedVoices.firstOrNull()
                    }

                    if (chosenVoice != null) {
                        tts.voice = chosenVoice
                        Log.i(TAG, "Voz humana seleccionada (${voiceOption.displayName}): ${chosenVoice.name} (${chosenVoice.locale})")
                    }
                }
            }

            tts.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )

            // Ajuste acústico profundo, tranquilo y humano para cada una de las 5 voces (3 mujeres y 2 hombres)
            val targetPitch = when (voiceOption) {
                GoogleCloudTtsService.CloudVoiceOption.STUDIO_FEMENINA -> 0.94f     // Mujer 1: Profunda y tranquila
                GoogleCloudTtsService.CloudVoiceOption.NEURAL2_DEVOCIONAL -> 0.97f // Mujer 2: Serena y maternal
                GoogleCloudTtsService.CloudVoiceOption.WAVENET_CONSOLADORA -> 0.91f // Mujer 3: Contemplativa y suave
                GoogleCloudTtsService.CloudVoiceOption.STUDIO_MASCULINA -> 0.85f    // Hombre 1: Profundo y tranquilo
                GoogleCloudTtsService.CloudVoiceOption.NEURAL2_PASTORAL -> 0.82f    // Hombre 2: Sabio y pastoral
            }

            val targetRate = when (voiceOption) {
                GoogleCloudTtsService.CloudVoiceOption.STUDIO_FEMENINA -> 0.88f
                GoogleCloudTtsService.CloudVoiceOption.NEURAL2_DEVOCIONAL -> 0.87f
                GoogleCloudTtsService.CloudVoiceOption.WAVENET_CONSOLADORA -> 0.86f
                GoogleCloudTtsService.CloudVoiceOption.STUDIO_MASCULINA -> 0.88f
                GoogleCloudTtsService.CloudVoiceOption.NEURAL2_PASTORAL -> 0.86f
            }.coerceIn(0.82f, 0.93f)

            tts.setPitch(targetPitch)
            tts.setSpeechRate(targetRate)
            onReady?.invoke()
        } catch (e: Exception) {
            Log.w(TAG, "Error configurando voz humana: ${e.message}")
        }
    }

    /**
     * Limpia emojis, etiquetas y abreviaturas en Español, Inglés y Francés
     * para que la voz suene 100% fluida, profunda, tranquila y humana.
     */
    fun humanizeTextForSpeech(rawText: String, detectedLang: String = detectLanguageCode(rawText)): String {
        if (rawText.isBlank()) return ""

        var cleaned = rawText
            // 1. Eliminar todos los emojis y pictogramas que hacen sonar robótico al lector
            .replace(Regex("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+"), " ")
            .replace(Regex("[\\u2600-\\u27BF]+"), " ")
            .replace(Regex("[\\uFE00-\\uFE0F]+"), "")

        if (detectedLang == "es") {
            cleaned = cleaned
                .replace(Regex("\\bMt\\b", RegexOption.IGNORE_CASE), "San Mateo")
                .replace(Regex("\\bMc\\b", RegexOption.IGNORE_CASE), "San Marcos")
                .replace(Regex("\\bLc\\b", RegexOption.IGNORE_CASE), "San Lucas")
                .replace(Regex("\\bJn\\b", RegexOption.IGNORE_CASE), "San Juan")
                .replace(Regex("\\bSal\\b", RegexOption.IGNORE_CASE), "Salmo")
                .replace(Regex("\\bC\\.S\\.B\\.", RegexOption.IGNORE_CASE), "de los Padres Basilianos")
                .replace(Regex("\\bCSB\\b", RegexOption.IGNORE_CASE), "de los Padres Basilianos")
                .replace(Regex("\\bBNENE\\b"), "Red Basiliana para la Educación")
                .replace(Regex("\\bI\\.E\\.\\s*"), "Institución Educativa ")
                .replace(Regex("\\bNtra\\.\\s*Sra\\.\\b", RegexOption.IGNORE_CASE), "Nuestra Señora")
                .replace(Regex("\\bP\\.\\s+([A-ZÁÉÍÓÚ])"), "Padre $1")
                .replace(Regex("\\bMons\\.\\s+([A-ZÁÉÍÓÚ])"), "Monseñor $1")
                .replace(Regex("\\bRev\\.\\s+([A-ZÁÉÍÓÚ])"), "Reverendo Padre $1")
                .replace(Regex("\\bvv?\\.\\s*(\\d+)"), "versículos $1")
                .replace(Regex("\\bv\\.\\s*(\\d+)"), "versículo $1")
                .replace("d.C.", "después de Cristo")
                .replace("a.C.", "antes de Cristo")
        } else if (detectedLang == "en") {
            cleaned = cleaned
                .replace(Regex("\\bC\\.S\\.B\\.", RegexOption.IGNORE_CASE), "of the Basilian Fathers")
                .replace(Regex("\\bCSB\\b", RegexOption.IGNORE_CASE), "of the Basilian Fathers")
                .replace(Regex("\\bFr\\.\\s+([A-Z])"), "Father $1")
                .replace(Regex("\\bSt\\.\\s+([A-Z])"), "Saint $1")
        } else if (detectedLang == "fr") {
            cleaned = cleaned
                .replace(Regex("\\bC\\.S\\.B\\.", RegexOption.IGNORE_CASE), "des Pères Basiliens")
                .replace(Regex("\\bCSB\\b", RegexOption.IGNORE_CASE), "des Pères Basiliens")
                .replace(Regex("\\bP\\.\\s+([A-ZÉÈÊÀ])"), "Père $1")
                .replace(Regex("\\bSt\\.\\s+([A-ZÉÈÊÀ])"), "Saint $1")
        }

        return cleaned
            // 3. Limpieza de etiquetas y símbolos gráficos
            .replace("[Antífona]", "Antífona. ")
            .replace("[Estrofa I]", "Primera estrofa. ")
            .replace("[Estrofa II]", "Segunda estrofa. ")
            .replace("[Estrofa III]", "Tercera estrofa. ")
            .replace("[Doxología]", "Gloria. ")
            .replace("•", ", ")
            .replace("|", ", ")
            .replace("«", "")
            .replace("»", "")
            .replace("“", "")
            .replace("”", "")
            .replace("\"", "")
            .replace("*", "")
            .replace("#", "")
            .replace("_", "")
            .replace("—", ", ")
            .replace("–", ", ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    /**
     * Reproduce un texto dividiéndolo en frases naturales con pausas de respiración suaves
     * para lograr una cadencia profunda, tranquila, latinoamericana y humana en ES, EN y FR.
     */
    fun speakHumanized(
        tts: TextToSpeech,
        text: String,
        utteranceId: String = "BasilianHumanVoice",
        onDone: (() -> Unit)? = null
    ) {
        val detectedLang = detectLanguageCode(text)
        val humanized = humanizeTextForSpeech(text, detectedLang)
        if (humanized.isBlank()) {
            onDone?.invoke()
            return
        }

        configureHumanVoice(
            tts = tts,
            pace = currentPace,
            voiceOption = selectedVoiceOption,
            detectedLanguage = detectedLang
        )

        val safeText = if (humanized.length > 3200) humanized.take(3200) else humanized
        val sentences = safeText
            .split(Regex("(?<=[.!?\\n;])\\s+"))
            .flatMap { rawSentence ->
                val clean = rawSentence.trim()
                if (clean.length <= 280) listOf(clean)
                else clean.chunked(260)
            }
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val finalUtteranceId = if (sentences.size <= 1) utteranceId else "${utteranceId}_part_${sentences.lastIndex}"

        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(id: String?) {}

            override fun onDone(id: String?) {
                if (id == finalUtteranceId || id == utteranceId) {
                    onDone?.invoke()
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(id: String?) {
                if (id == finalUtteranceId || id == utteranceId) {
                    onDone?.invoke()
                }
            }
        })

        val params = Bundle().apply {
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 0.98f)
        }

        if (sentences.size <= 1) {
            tts.speak(humanized, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        } else {
            sentences.forEachIndexed { index, sentence ->
                val queueMode = if (index == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
                val partId = "${utteranceId}_part_$index"
                tts.speak(sentence, queueMode, params, partId)
                // Pausa de respiración humana tranquila entre frases (310 ms)
                if (index < sentences.lastIndex) {
                    tts.playSilentUtterance(310L, TextToSpeech.QUEUE_ADD, "${utteranceId}_breath_$index")
                }
            }
        }
    }
}
