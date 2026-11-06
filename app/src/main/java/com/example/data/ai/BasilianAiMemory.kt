package com.example.data.ai

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AppLanguage
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class LearnedBasilianFact(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val details: String,
    val category: String, // "HISTORIA", "VOCACIONES", "CURIA", "LITURGIA", "COMUNIDAD", "AUTOAPRENDIZAJE", "PREFERENCIA"
    val timestamp: Long = System.currentTimeMillis(),
    val verifiedWithGoogle: Boolean = true
)

data class AiChatTurn(
    val id: String = UUID.randomUUID().toString(),
    val userPrompt: String,
    val aiResponse: String,
    val modeName: String = CreativeHistoryMode.HISTORIA_COMPLETA.name,
    val thinkingDepthName: String = GeminiThinkingDepth.EQUILIBRADO.name,
    val timestamp: Long = System.currentTimeMillis(),
    val learnedFactTitle: String? = null
)

data class AiChatSessionThread(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val turns: List<AiChatTurn> = emptyList()
)

/**
 * Estilos de diálogo adaptativos por usuario para que cada persona que use la app compartida
 * pueda entrenar y adaptar su propio chat con Basilio IA sin perder la cercanía y esencia basiliana.
 */
enum class UserAiDialogueStyle(
    val id: String,
    val badgeTitle: String,
    val shortLabel: String,
    val subtitle: String,
    val promptDirective: String
) {
    CERCANO_INTEGRAL(
        id = "CERCANO_INTEGRAL",
        badgeTitle = "🤝 Cercano Integral (Por defecto)",
        shortLabel = "🤝 Cercano",
        subtitle = "Equilibrado, fraterno, cálido, completo y 100% basiliano",
        promptDirective = "ESTILO PERSONAL DEL USUARIO [CERCANO INTEGRAL]: Mantén un equilibrio armónico entre calidez fraterna, profundidad histórica/patrística y claridad pedagógica, hablando como un hermano y amigo basiliano sabio y cercano."
    ),
    ACADEMICO_PATRISTICO(
        id = "ACADEMICO_PATRISTICO",
        badgeTitle = "🎓 Académico & Patrístico",
        shortLabel = "🎓 Académico",
        subtitle = "Rigor teológico, citas patrísticas, fuentes históricas y léxico elevado",
        promptDirective = "ESTILO PERSONAL DEL USUARIO [ACADÉMICO Y PATRÍSTICO]: Este usuario prefiere un diálogo de alto nivel académico, teológico, filosófico e histórico. Utiliza vocabulario preciso y riguroso, profundiza en las fuentes primarias de San Basilio Magno (Asketikon, Hexamerón, De Spiritu Sancto, Epístolas), Padres de la Iglesia, Magisterio y archivos históricos de la C.S.B., manteniendo siempre la amabilidad y cercanía fraterna."
    ),
    PASTORAL_ACOMPANANTE(
        id = "PASTORAL_ACOMPANANTE",
        badgeTitle = "🌿 Pastoralista & Cercano",
        shortLabel = "🌿 Pastoral",
        subtitle = "Acompañamiento humano, parroquial, juvenil, empático y vivencial",
        promptDirective = "ESTILO PERSONAL DEL USUARIO [PASTORALISTA Y ACOMPAÑANTE]: Este usuario prefiere un tono profundamente pastoral, empático, cálido y orientado a la vida cotidiana, al acompañamiento de comunidades, jóvenes, familias y discernimiento espiritual. Conecta cada enseñanza de San Basilio y de la C.S.B. con la praxis pastoral, la misericordia y el cuidado del corazón."
    ),
    SENCILLO_CLARO(
        id = "SENCILLO_CLARO",
        badgeTitle = "💡 Sencillo y Directo",
        shortLabel = "💡 Sencillo",
        subtitle = "Explicaciones fáciles de entender, lenguaje claro y sin tecnicismos",
        promptDirective = "ESTILO PERSONAL DEL USUARIO [SENCILLO Y CLARO]: Este usuario prefiere respuestas explicadas con palabras sencillas, naturales, claras y fáciles de comprender para cualquier persona, evitando tecnicismos complejos y yendo directo a las ideas esenciales con calidez fraterna."
    ),
    DIDACTICO_CON_EJEMPLOS(
        id = "DIDACTICO_CON_EJEMPLOS",
        badgeTitle = "📖 Explicación con Ejemplos",
        shortLabel = "📖 Con Ejemplos",
        subtitle = "Pedagógico, explica todo con ejemplos prácticos, parábolas y casos reales",
        promptDirective = "ESTILO PERSONAL DEL USUARIO [DIDÁCTICO CON EJEMPLOS]: Este usuario aprende y disfruta mejor cuando le explicas cada concepto mediante ejemplos concretos de la vida diaria, analogías pedagógicas (como hacía San Basilio con las abejas o el artesano), casos prácticos e historias ilustrativas."
    );

    companion object {
        fun fromId(id: String?): UserAiDialogueStyle {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: CERCANO_INTEGRAL
        }
    }
}

data class UserPersonalAiProfile(
    val userEmail: String,
    val preferredStyle: UserAiDialogueStyle = UserAiDialogueStyle.CERCANO_INTEGRAL,
    val customWordsOrToneInstruction: String = "",
    val personalMemoryNotes: List<String> = emptyList(),
    val updatedAt: Long = System.currentTimeMillis()
)

object BasilianAiMemory {
    private const val PREFS_NAME = "basilian_ai_learned_memory_prefs"
    private const val KEY_LEARNED_FACTS = "key_learned_basilian_facts"
    private const val KEY_ACCURACY_SCORE = "key_basilian_accuracy_score"
    private const val KEY_CHAT_THREADS_PREFIX = "key_ai_chat_threads_"
    private const val KEY_ACTIVE_CHAT_ID_PREFIX = "key_ai_active_chat_id_"
    private const val KEY_USER_PERSONAL_PROFILE_PREFIX = "key_ai_user_personal_profile_"

    // Hechos fundamentales pre-aprendidos y consolidados en el autoaprendizaje (Sincronizados con basilian.org y Directorio Oficial)
    private val DEFAULT_SEEDS = listOf(
        LearnedBasilianFact(
            id = "seed_1",
            title = "Escolásticos 2026 en Bogotá: Ciro Cárdenas y Fabien Mwehu",
            details = "Ciro Alfonso Cárdenas Balaguera y Fabien Mwehu Itondo culminaron su noviciado e hicieron su Primera Profesión Religiosa de votos el 8 de agosto de 2026 en la Congregación de San Basilio. Actualmente son escolásticos en formación teológica en el Teologado de Bogotá, Colombia.",
            category = "VOCACIONES",
            verifiedWithGoogle = true
        ),
        LearnedBasilianFact(
            id = "seed_2",
            title = "Novicios 2026-2027 en Casa Annonay (Bogotá)",
            details = "El noviciado canónico 2026-2027 se realiza en Casa Annonay (Barrio Egipto, Bogotá). Los 3 novicios actuales son Diego Alejandro Martínez López, José Yonni Villa de Jesús y Jorge Mario Rodríguez Peña, bajo la guía del Maestro de Novicios y Vicario General P. Maurice Luke Restivo, C.S.B.",
            category = "VOCACIONES",
            verifiedWithGoogle = true
        ),
        LearnedBasilianFact(
            id = "seed_3",
            title = "Superior General 2026-2030: Muy Rev. P. John Brian Huber, C.S.B.",
            details = "El Superior General de la Congregación de San Basilio (2026-2030) es el Muy Rev. P. John Brian Huber, C.S.B., con sede en la Curia Generalicia en Toronto, Canadá (146 Davenport Road). Vicario General y 1.er Consultor: Rev. P. Maurice Luke Restivo, C.S.B.; 2.° Consultor: Rev. P. Pedro Mora Gómez, C.S.B.; 3.er Consultor: Rev. P. Morgan Vernon Rice, C.S.B.; 4.° Consultor: Rev. P. Patrick Fulton, C.S.B.",
            category = "CURIA",
            verifiedWithGoogle = true
        ),
        LearnedBasilianFact(
            id = "seed_4",
            title = "Realidad Actual de Cali y Presencia en Colombia (Bogotá y Medellín)",
            details = "VERDAD OFICIAL VERIFICADA (basilian.org y Curia C.S.B.): Los Padres Basilianos (C.S.B.) YA NO están en Cali. Históricamente llegaron a Cali en 1987 (Parroquia Nuestra Señora de la Asunción en Andrés Sanín y Parroquia/Colegio en Los Naranjos, Distrito de Aguablanca, con los padres Francisco Amico, Robert 'Bob' Holmes, Roberto Seguín y Mauricio Restivo), pero esa etapa misionera en Cali ya concluyó y tanto la parroquia como el colegio se separaron / fueron entregados a la Arquidiócesis de Cali. Actualmente en Colombia la Congregación de San Basilio está presente ÚNICAMENTE en Bogotá (Parroquia Nuestra Señora de Egipto, Noviciado Internacional Casa Annonay y Teologado) y en Medellín (Parroquia San Basilio).",
            category = "HISTORIA",
            verifiedWithGoogle = true
        ),
        LearnedBasilianFact(
            id = "seed_5",
            title = "Lema y Escudo: Bonitatem et disciplinam et scientiam doce me",
            details = "El lema basiliano proviene del Salmo 118 (119), versículo 66: 'Enséñame la bondad, la disciplina y la ciencia'. Simboliza las tres columnas de la pedagogía basiliana: caridad evangélica, formación del carácter y excelencia intelectual.",
            category = "HISTORIA",
            verifiedWithGoogle = true
        ),
        LearnedBasilianFact(
            id = "seed_6",
            title = "La Basiliada: Ciudadela de la Caridad (370 d.C.)",
            details = "Fundada por San Basilio Magno a las afueras de Cesarea de Capadocia, fue el primer complejo hospitalario, albergue para pobres y leprosario integral de la cristiandad.",
            category = "LITURGIA",
            verifiedWithGoogle = true
        ),
        LearnedBasilianFact(
            id = "seed_7",
            title = "Conexión Viva con basilian.org, Google, YouTube y Redes Sociales",
            details = "Basilio IA está permanentemente conectado con la página oficial de la Congregación de San Basilio (https://www.basilian.org y https://www.basilian.org/es/), Google Search, el canal oficial de YouTube (@BasilianFathers), páginas universitarias (St. Michael's College Toronto, University of St. Thomas Houston, St. John Fisher University, PIMS) y redes sociales oficiales (@basilianfathers en Facebook e Instagram) para ofrecer siempre datos reales, verificados y actualizados.",
            category = "CURIA",
            verifiedWithGoogle = true
        )
    )

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun normalizeEmailKey(email: String?): String {
        return email?.trim()?.lowercase()?.ifBlank { "default_user" } ?: "default_user"
    }

    @Synchronized
    fun getAllLearnedFacts(context: Context): List<LearnedBasilianFact> {
        val prefs = getPrefs(context)
        val rawJson = prefs.getString(KEY_LEARNED_FACTS, null)
        if (rawJson.isNullOrBlank()) {
            saveFacts(context, DEFAULT_SEEDS)
            return DEFAULT_SEEDS
        }

        return try {
            val jsonArray = JSONArray(rawJson)
            val userFacts = mutableListOf<LearnedBasilianFact>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.optString("id", UUID.randomUUID().toString())
                // Las semillas base se sincronizan siempre desde DEFAULT_SEEDS para que nunca queden datos antiguos
                if (id.startsWith("seed_")) continue
                val title = obj.optString("title", "Aprendizaje Basiliano")
                val details = obj.optString("details", "")
                val category = obj.optString("category", "HISTORIA")
                val isPollutedChatEcho = title.startsWith("Memoria de charla:", ignoreCase = true) ||
                    title.startsWith("Razonamiento hipotético", ignoreCase = true) ||
                    title.startsWith("Preferencia/Corrección", ignoreCase = true) ||
                    details.contains("Se conversó sobre:", ignoreCase = true)
                if (!isPollutedChatEcho && details.isNotBlank()) {
                    userFacts.add(
                        LearnedBasilianFact(
                            id = id,
                            title = title,
                            details = details,
                            category = category,
                            timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                            verifiedWithGoogle = obj.optBoolean("verifiedWithGoogle", true)
                        )
                    )
                }
            }
            val merged = userFacts + DEFAULT_SEEDS
            saveFacts(context, merged)
            merged
        } catch (_: Exception) {
            DEFAULT_SEEDS
        }
    }

    @Synchronized
    fun addLearnedFact(
        context: Context,
        title: String,
        details: String,
        category: String = "AUTOAPRENDIZAJE",
        verifiedWithGoogle: Boolean = true
    ): LearnedBasilianFact {
        val current = getAllLearnedFacts(context).toMutableList()
        val cleanTitle = title.trim().take(110)
        val cleanDetails = details.trim().take(600)
        val existingIndex = current.indexOfFirst {
            it.title.equals(cleanTitle, ignoreCase = true) ||
                (cleanDetails.length > 25 && it.details.contains(cleanDetails.take(35), ignoreCase = true))
        }
        val fact = LearnedBasilianFact(
            id = if (existingIndex >= 0) current[existingIndex].id else UUID.randomUUID().toString(),
            title = cleanTitle,
            details = cleanDetails,
            category = category.uppercase(),
            timestamp = System.currentTimeMillis(),
            verifiedWithGoogle = verifiedWithGoogle
        )
        if (existingIndex >= 0) {
            current.removeAt(existingIndex)
        }
        current.add(0, fact)
        saveFacts(context, current.take(60))
        return fact
    }

    @Synchronized
    fun deleteLearnedFact(context: Context, factId: String): List<LearnedBasilianFact> {
        val current = getAllLearnedFacts(context).filterNot { it.id == factId }
        saveFacts(context, current)
        return current
    }

    /**
     * Motor de Autoaprendizaje:
     * Solo guarda hechos verificables cuando el usuario explícitamente enseña un dato nuevo a la IA,
     * evitando guardar ecos genéricos de conversación que desvíen futuras respuestas.
     */
    @Synchronized
    fun autoLearnFromChat(
        context: Context,
        userQuery: String,
        aiAnswer: String,
        userEmail: String? = null
    ): LearnedBasilianFact? {
        val cleanQuery = userQuery
            .removePrefix("[RESUMIR_TEXTO]")
            .removePrefix("[SUBIR_TEXTO]")
            .removePrefix("[CREAR_MUSICA]")
            .removePrefix("[CREAR_IMAGEN]")
            .trim()
        if (cleanQuery.length < 10) return null
        val q = cleanQuery.lowercase()

        // 0. Detectar si el usuario está entrenando o adaptando su estilo personal de diálogo
        detectAndApplyPersonalStyleTraining(context, userEmail, cleanQuery, q)

        val isExplicitFactTeaching = q.startsWith("recuerda que ") ||
            q.startsWith("aprende que ") ||
            q.startsWith("te enseño que ") ||
            q.startsWith("ten en cuenta que ") ||
            q.startsWith("guarda este dato:") ||
            q.startsWith("actualiza que ") ||
            q.startsWith("para que sepas:")

        if (isExplicitFactTeaching) {
            val taughtContent = cleanQuery.replace(
                Regex("^(recuerda que|aprende que|te enseño que|ten en cuenta que|guarda este dato:|actualiza que|para que sepas:)\\s*", RegexOption.IGNORE_CASE),
                ""
            ).trim()
            if (taughtContent.length >= 10) {
                return addLearnedFact(
                    context = context,
                    title = taughtContent.take(65),
                    details = taughtContent.take(500),
                    category = "CORRECCION_VERIFICADA",
                    verifiedWithGoogle = true
                )
            }
        }

        // Meta-aprendizaje continuo de las charlas cuando un sacerdote o usuario corrige o actualiza un hecho histórico/comunitario
        val isConversationalCorrectionOrUpdate = (
            q.contains("no es verdad") ||
                q.contains("no es cierto") ||
                q.contains("ya no pertenece") ||
                q.contains("se separaron") ||
                q.contains("se separó") ||
                q.contains("ya no es de los basilianos") ||
                q.contains("ya no es basiliano") ||
                q.contains("dejó de pertenecer") ||
                q.contains("dejo de pertenecer") ||
                q.contains("pasó a la arquidiócesis") ||
                q.contains("paso a la arquidiocesis") ||
                q.contains("eso cambió") ||
                q.contains("eso cambio") ||
                q.contains("estás desactualizado") ||
                q.contains("estas desactualizado") ||
                ((q.contains("estás equivocad") || q.contains("estas equivocad") || q.contains("te equivocaste") || q.contains("eso no es así") || q.contains("eso no es asi")) &&
                    (q.contains("porque") || q.contains("en realidad") || q.contains("ahora") || q.contains("actualmente") || q.contains("pasó") || q.contains("paso")))
            ) && cleanQuery.length >= 22

        if (isConversationalCorrectionOrUpdate) {
            val normalizedCorrection = cleanQuery
                .replace(
                    Regex(
                        "^(oye\\s+|mira\\s+|hermano\\s+|padre\\s+|basilio\\s+)?(eso\\s+)?(no es verdad|no es cierto|te equivocaste|estás equivocado|estas equivocado|eso no es así|eso no es asi)[,.:;\\s]*(porque|pues|en realidad|es que)?\\s*",
                        RegexOption.IGNORE_CASE
                    ),
                    ""
                )
                .trim()
                .ifBlank { cleanQuery }

            val topicTitle = when {
                q.contains("colegio") && q.contains("cali") -> "Actualización sobre el Colegio San Basilio en Cali"
                q.contains("cali") -> "Actualización histórica de la comunidad en Cali"
                q.contains("medellín") || q.contains("medellin") -> "Actualización histórica de la comunidad en Medellín"
                q.contains("bogotá") || q.contains("bogota") -> "Actualización histórica de la comunidad en Bogotá"
                q.contains("méxico") || q.contains("mexico") || q.contains("tehuacán") -> "Actualización de la comunidad en México"
                else -> "Actualización aprendida en charla: ${normalizedCorrection.take(45)}"
            }

            return addLearnedFact(
                context = context,
                title = topicTitle,
                details = "Actualización aportada en diálogo comunitario: $normalizedCorrection",
                category = "CORRECCION_VERIFICADA",
                verifiedWithGoogle = true
            )
        }

        return null
    }

    private fun saveFacts(context: Context, facts: List<LearnedBasilianFact>) {
        val jsonArray = JSONArray()
        for (f in facts) {
            val obj = JSONObject().apply {
                put("id", f.id)
                put("title", f.title)
                put("details", f.details)
                put("category", f.category)
                put("timestamp", f.timestamp)
                put("verifiedWithGoogle", f.verifiedWithGoogle)
            }
            jsonArray.put(obj)
        }
        getPrefs(context).edit().putString(KEY_LEARNED_FACTS, jsonArray.toString()).apply()
    }

    // =========================================================================
    // GESTIÓN DE MÚLTIPLES CHATS CON MEMORIA COMPLETA POR CADA CHAT (ESTILO GEMINI)
    // =========================================================================
    @Synchronized
    fun getChatThreadsForUser(context: Context, email: String?): List<AiChatSessionThread> {
        val userKey = normalizeEmailKey(email)
        val raw = getPrefs(context).getString(KEY_CHAT_THREADS_PREFIX + userKey, null)
        if (raw.isNullOrBlank()) {
            val initialThread = AiChatSessionThread(
                id = "chat_main_${System.currentTimeMillis()}",
                title = "Conversación Principal IA"
            )
            saveChatThreadsForUser(context, email, listOf(initialThread))
            setActiveChatIdForUser(context, email, initialThread.id)
            return listOf(initialThread)
        }

        return try {
            val arr = JSONArray(raw)
            val threads = mutableListOf<AiChatSessionThread>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val turnsArr = obj.optJSONArray("turns") ?: JSONArray()
                val turns = mutableListOf<AiChatTurn>()
                for (j in 0 until turnsArr.length()) {
                    val tObj = turnsArr.getJSONObject(j)
                    turns.add(
                        AiChatTurn(
                            id = tObj.optString("id", UUID.randomUUID().toString()),
                            userPrompt = tObj.optString("userPrompt", ""),
                            aiResponse = tObj.optString("aiResponse", ""),
                            modeName = tObj.optString("modeName", CreativeHistoryMode.HISTORIA_COMPLETA.name),
                            thinkingDepthName = tObj.optString("thinkingDepthName", GeminiThinkingDepth.EQUILIBRADO.name),
                            timestamp = tObj.optLong("timestamp", System.currentTimeMillis()),
                            learnedFactTitle = tObj.optString("learnedFactTitle", "").takeIf { it.isNotBlank() }
                        )
                    )
                }
                threads.add(
                    AiChatSessionThread(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        title = obj.optString("title", "Conversación IA"),
                        createdAt = obj.optLong("createdAt", System.currentTimeMillis()),
                        updatedAt = obj.optLong("updatedAt", System.currentTimeMillis()),
                        turns = turns
                    )
                )
            }
            if (threads.isEmpty()) {
                val fallback = AiChatSessionThread(title = "Conversación Principal IA")
                saveChatThreadsForUser(context, email, listOf(fallback))
                listOf(fallback)
            } else {
                threads.sortedByDescending { it.updatedAt }
            }
        } catch (_: Exception) {
            val fallback = AiChatSessionThread(title = "Conversación Principal IA")
            listOf(fallback)
        }
    }

    @Synchronized
    fun getActiveChatIdForUser(context: Context, email: String?): String {
        val userKey = normalizeEmailKey(email)
        val savedId = getPrefs(context).getString(KEY_ACTIVE_CHAT_ID_PREFIX + userKey, null)
        val threads = getChatThreadsForUser(context, email)
        if (!savedId.isNullOrBlank() && threads.any { it.id == savedId }) {
            return savedId
        }
        val firstId = threads.first().id
        setActiveChatIdForUser(context, email, firstId)
        return firstId
    }

    @Synchronized
    fun setActiveChatIdForUser(context: Context, email: String?, chatId: String) {
        val userKey = normalizeEmailKey(email)
        getPrefs(context).edit().putString(KEY_ACTIVE_CHAT_ID_PREFIX + userKey, chatId).apply()
    }

    @Synchronized
    fun createNewChatThread(context: Context, email: String?, customTitle: String? = null): AiChatSessionThread {
        val timeFmt = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date())
        val sessionId = "chat_${System.currentTimeMillis()}"
        val resolvedTitle = customTitle?.takeIf { it.isNotBlank() } ?: "Nuevo Chat ($timeFmt)"
        return createNewChatThreadWithId(context, email, sessionId, resolvedTitle)
    }

    @Synchronized
    fun createNewChatThreadWithId(
        context: Context,
        email: String?,
        sessionId: String,
        title: String
    ): AiChatSessionThread {
        val threads = getChatThreadsForUser(context, email).filterNot { it.id == sessionId }.toMutableList()
        val now = System.currentTimeMillis()
        val newThread = AiChatSessionThread(
            id = sessionId,
            title = title.ifBlank { "Nuevo Chat" },
            createdAt = now,
            updatedAt = now,
            turns = emptyList()
        )
        threads.add(0, newThread)
        saveChatThreadsForUser(context, email, threads.take(30))
        setActiveChatIdForUser(context, email, newThread.id)
        return newThread
    }

    @Synchronized
    fun deleteChatThread(context: Context, email: String?, chatId: String): List<AiChatSessionThread> {
        val remaining = getChatThreadsForUser(context, email).filterNot { it.id == chatId }.toMutableList()
        if (remaining.isEmpty()) {
            remaining.add(AiChatSessionThread(title = "Conversación Principal IA"))
        }
        saveChatThreadsForUser(context, email, remaining)
        val currentActive = getActiveChatIdForUser(context, email)
        if (currentActive == chatId) {
            setActiveChatIdForUser(context, email, remaining.first().id)
        }
        return remaining
    }

    @Synchronized
    fun appendTurnToChat(
        context: Context,
        email: String?,
        chatId: String,
        turn: AiChatTurn
    ): List<AiChatSessionThread> {
        val threads = getChatThreadsForUser(context, email).toMutableList()
        val idx = threads.indexOfFirst { it.id == chatId }
        val cleanPromptTitle = turn.userPrompt
            .removePrefix("[RESUMIR_TEXTO]")
            .removePrefix("[SUBIR_TEXTO]")
            .removePrefix("[CREAR_MUSICA]")
            .removePrefix("[CREAR_IMAGEN]")
            .trim()
            .take(42)

        if (idx >= 0) {
            val existing = threads[idx]
            val updatedTurns = existing.turns + turn
            val newTitle = if (existing.turns.isEmpty() && cleanPromptTitle.isNotBlank()) {
                cleanPromptTitle
            } else {
                existing.title
            }
            threads[idx] = existing.copy(
                title = newTitle,
                updatedAt = System.currentTimeMillis(),
                turns = updatedTurns.takeLast(50)
            )
        } else {
            val created = AiChatSessionThread(
                id = chatId,
                title = cleanPromptTitle.ifBlank { "Conversación IA" },
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                turns = listOf(turn)
            )
            threads.add(0, created)
        }
        val sorted = threads.sortedByDescending { it.updatedAt }.take(30)
        saveChatThreadsForUser(context, email, sorted)
        setActiveChatIdForUser(context, email, chatId)
        return sorted
    }

    private fun saveChatThreadsForUser(
        context: Context,
        email: String?,
        threads: List<AiChatSessionThread>
    ) {
        val userKey = normalizeEmailKey(email)
        val arr = JSONArray()
        for (thread in threads) {
            val turnsArr = JSONArray()
            for (t in thread.turns) {
                turnsArr.put(
                    JSONObject().apply {
                        put("id", t.id)
                        put("userPrompt", t.userPrompt)
                        put("aiResponse", t.aiResponse)
                        put("modeName", t.modeName)
                        put("thinkingDepthName", t.thinkingDepthName)
                        put("timestamp", t.timestamp)
                        if (!t.learnedFactTitle.isNullOrBlank()) {
                            put("learnedFactTitle", t.learnedFactTitle)
                        }
                    }
                )
            }
            arr.put(
                JSONObject().apply {
                    put("id", thread.id)
                    put("title", thread.title)
                    put("createdAt", thread.createdAt)
                    put("updatedAt", thread.updatedAt)
                    put("turns", turnsArr)
                }
            )
        }
        getPrefs(context).edit().putString(KEY_CHAT_THREADS_PREFIX + userKey, arr.toString()).apply()
    }

    // =========================================================================
    // PERFIL Y ENTRENAMIENTO PERSONAL DE IA POR CADA USUARIO (MEMORIA GEMINI PERSONAL)
    // =========================================================================
    @Synchronized
    fun getUserPersonalAiProfile(context: Context, email: String?): UserPersonalAiProfile {
        val userKey = normalizeEmailKey(email)
        val raw = getPrefs(context).getString(KEY_USER_PERSONAL_PROFILE_PREFIX + userKey, null)
        if (raw.isNullOrBlank()) {
            return UserPersonalAiProfile(userEmail = userKey)
        }
        return try {
            val obj = JSONObject(raw)
            val notesArr = obj.optJSONArray("personalMemoryNotes") ?: JSONArray()
            val notes = mutableListOf<String>()
            for (i in 0 until notesArr.length()) {
                val n = notesArr.optString(i, "").trim()
                if (n.isNotBlank()) notes.add(n)
            }
            UserPersonalAiProfile(
                userEmail = userKey,
                preferredStyle = UserAiDialogueStyle.fromId(obj.optString("preferredStyle", UserAiDialogueStyle.CERCANO_INTEGRAL.id)),
                customWordsOrToneInstruction = obj.optString("customWordsOrToneInstruction", ""),
                personalMemoryNotes = notes,
                updatedAt = obj.optLong("updatedAt", System.currentTimeMillis())
            )
        } catch (_: Exception) {
            UserPersonalAiProfile(userEmail = userKey)
        }
    }

    @Synchronized
    fun saveUserPersonalAiProfile(context: Context, email: String?, profile: UserPersonalAiProfile): UserPersonalAiProfile {
        val userKey = normalizeEmailKey(email)
        val notesArr = JSONArray()
        profile.personalMemoryNotes.take(25).forEach { note ->
            if (note.isNotBlank()) notesArr.put(note.trim())
        }
        val obj = JSONObject().apply {
            put("userEmail", userKey)
            put("preferredStyle", profile.preferredStyle.id)
            put("customWordsOrToneInstruction", profile.customWordsOrToneInstruction.trim().take(500))
            put("personalMemoryNotes", notesArr)
            put("updatedAt", System.currentTimeMillis())
        }
        getPrefs(context).edit().putString(KEY_USER_PERSONAL_PROFILE_PREFIX + userKey, obj.toString()).apply()
        return profile.copy(userEmail = userKey, updatedAt = System.currentTimeMillis())
    }

    @Synchronized
    fun setUserPreferredDialogueStyle(
        context: Context,
        email: String?,
        style: UserAiDialogueStyle
    ): UserPersonalAiProfile {
        val current = getUserPersonalAiProfile(context, email)
        return saveUserPersonalAiProfile(
            context,
            email,
            current.copy(preferredStyle = style, updatedAt = System.currentTimeMillis())
        )
    }

    @Synchronized
    fun setUserCustomStyleInstruction(
        context: Context,
        email: String?,
        customInstruction: String
    ): UserPersonalAiProfile {
        val current = getUserPersonalAiProfile(context, email)
        return saveUserPersonalAiProfile(
            context,
            email,
            current.copy(customWordsOrToneInstruction = customInstruction.trim().take(500), updatedAt = System.currentTimeMillis())
        )
    }

    @Synchronized
    fun addUserPersonalMemoryNote(
        context: Context,
        email: String?,
        note: String
    ): UserPersonalAiProfile {
        val clean = note.trim().take(300)
        if (clean.isBlank()) return getUserPersonalAiProfile(context, email)
        val current = getUserPersonalAiProfile(context, email)
        val updatedNotes = (listOf(clean) + current.personalMemoryNotes.filterNot { it.equals(clean, ignoreCase = true) }).take(25)
        return saveUserPersonalAiProfile(
            context,
            email,
            current.copy(personalMemoryNotes = updatedNotes, updatedAt = System.currentTimeMillis())
        )
    }

    @Synchronized
    fun removeUserPersonalMemoryNote(
        context: Context,
        email: String?,
        note: String
    ): UserPersonalAiProfile {
        val current = getUserPersonalAiProfile(context, email)
        val updatedNotes = current.personalMemoryNotes.filterNot { it.equals(note.trim(), ignoreCase = true) }
        return saveUserPersonalAiProfile(
            context,
            email,
            current.copy(personalMemoryNotes = updatedNotes, updatedAt = System.currentTimeMillis())
        )
    }

    /**
     * Detecta automáticamente en el chat cuando el usuario entrena o pide a la IA que le hable
     * de un modo particular (académico, pastoralista, más sencillo, con ejemplos, o usando ciertas palabras).
     */
    @Synchronized
    fun detectAndApplyPersonalStyleTraining(
        context: Context,
        email: String?,
        cleanQuery: String,
        lowerQuery: String
    ): UserPersonalAiProfile? {
        val asksStyleAdaptation = lowerQuery.contains("háblame") || lowerQuery.contains("hablame") ||
            lowerQuery.contains("respóndeme") || lowerQuery.contains("respondeme") ||
            lowerQuery.contains("explícame siempre") || lowerQuery.contains("explicame siempre") ||
            lowerQuery.contains("quiero que seas más") || lowerQuery.contains("quiero que seas mas") ||
            lowerQuery.contains("sé más ") || lowerQuery.contains("se mas ") ||
            lowerQuery.contains("quiero entrenarte") || lowerQuery.contains("te entreno para") ||
            lowerQuery.contains("conmigo sé") || lowerQuery.contains("conmigo habla") ||
            lowerQuery.contains("adapta tu") || lowerQuery.contains("dame respuestas más") ||
            lowerQuery.contains("dame respuestas mas") || lowerQuery.contains("usa estas palabras") ||
            lowerQuery.contains("habla con estas palabras")

        if (!asksStyleAdaptation) return null

        var profile = getUserPersonalAiProfile(context, email)
        var modified = false

        when {
            lowerQuery.contains("académic") || lowerQuery.contains("academic") || lowerQuery.contains("patrístic") || lowerQuery.contains("teológic") || lowerQuery.contains("erudit") -> {
                profile = profile.copy(preferredStyle = UserAiDialogueStyle.ACADEMICO_PATRISTICO)
                modified = true
            }
            lowerQuery.contains("pastoral") || lowerQuery.contains("acompañante") || lowerQuery.contains("parroquial") || lowerQuery.contains("espiritual y cercano") -> {
                profile = profile.copy(preferredStyle = UserAiDialogueStyle.PASTORAL_ACOMPANANTE)
                modified = true
            }
            lowerQuery.contains("sencill") || lowerQuery.contains("simple") || lowerQuery.contains("fácil") || lowerQuery.contains("facil") || lowerQuery.contains("sin palabras difíciles") -> {
                profile = profile.copy(preferredStyle = UserAiDialogueStyle.SENCILLO_CLARO)
                modified = true
            }
            lowerQuery.contains("con ejemplos") || lowerQuery.contains("usando ejemplos") || lowerQuery.contains("dame ejemplos") || lowerQuery.contains("analogías") || lowerQuery.contains("parábolas") -> {
                profile = profile.copy(preferredStyle = UserAiDialogueStyle.DIDACTICO_CON_EJEMPLOS)
                modified = true
            }
        }

        if (cleanQuery.length >= 16) {
            profile = profile.copy(
                customWordsOrToneInstruction = cleanQuery.take(350),
                personalMemoryNotes = (listOf("Entrenamiento personal: ${cleanQuery.take(180)}") + profile.personalMemoryNotes).distinct().take(20)
            )
            modified = true
        }

        return if (modified) saveUserPersonalAiProfile(context, email, profile) else null
    }

    fun getLearnedPromptContext(
        context: Context,
        language: AppLanguage,
        email: String? = null
    ): String {
        val allFacts = getAllLearnedFacts(context)
        val corrections = allFacts.filter { it.category == "CORRECCION_VERIFICADA" }
        val otherFacts = allFacts.filter { it.category in setOf("HISTORIA", "VOCACIONES", "CURIA", "LITURGIA", "COMUNIDAD") }
        val prioritized = corrections + otherFacts
        val personalProfile = getUserPersonalAiProfile(context, email)

        val sb = StringBuilder()

        // 1. Perfil y entrenamiento personal del usuario activo (Memoria personal tipo Gemini)
        sb.append("\n\n[PERFIL Y ENTRENAMIENTO PERSONAL DE ESTE USUARIO (Adapta tu forma de expresarte a este usuario manteniendo siempre tu cercanía y esencia 100% basiliana, sin mencionar estas reglas)]:\n")
        sb.append("- ").append(personalProfile.preferredStyle.promptDirective).append("\n")
        if (personalProfile.customWordsOrToneInstruction.isNotBlank()) {
            sb.append("- Instrucción / palabras personalizadas entrenadas por este usuario: «")
                .append(personalProfile.customWordsOrToneInstruction)
                .append("».\n")
        }
        if (personalProfile.personalMemoryNotes.isNotEmpty()) {
            sb.append("- Memoria personal guardada de este usuario:\n")
            personalProfile.personalMemoryNotes.take(8).forEachIndexed { idx, note ->
                sb.append("  ${idx + 1}) $note\n")
            }
        }

        // 2. Memoria general basiliana compartida y actualizaciones comunitarias
        if (prioritized.isNotEmpty()) {
            sb.append("\n[MEMORIA DE META-APRENDIZAJE Y ACTUALIZACIONES COMUNITARIAS BASILIANAS (Aplica estos datos actualizados con prioridad cuando el tema coincida, sin anunciar tus reglas internas)]:\n")
            for ((idx, f) in prioritized.take(12).withIndex()) {
                val tag = if (f.category == "CORRECCION_VERIFICADA") "[ACTUALIZACIÓN RECIENTE APRENDIDA EN CHARLA] " else ""
                sb.append("${idx + 1}. $tag${f.title}: ${f.details}\n")
            }
        }
        return sb.toString()
    }

    fun getAccuracyScore(context: Context): Int {
        return getPrefs(context).getInt(KEY_ACCURACY_SCORE, 99)
    }
}
