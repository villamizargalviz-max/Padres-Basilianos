package com.example.data.repository

import android.content.Context
import com.example.data.ai.AiChatSessionThread
import com.example.data.ai.AiChatTurn
import com.example.data.ai.BasilianAiMemory
import com.example.data.ai.LearnedBasilianFact
import com.example.data.db.AiChatDao
import com.example.data.model.AiChatMessageEntity
import com.example.data.model.AiChatSessionEntity
import com.example.data.model.AiLearnedFactEntity
import com.example.data.model.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repositorio de Persistencia de Historial de Chat para la IA utilizando Room Database.
 * Asegura que cada sesión y cada turno de conversación del usuario se almacene y recupere en SQLite,
 * permitiendo a la IA mantener un contexto histórico completo basado en interacciones previas.
 */
class AiChatRoomRepository(
    private val aiChatDao: AiChatDao,
    private val appContext: Context
) {
    fun normalizeEmailKey(userEmail: String?): String {
        return userEmail?.trim()?.lowercase()?.ifBlank { "default_user" } ?: "default_user"
    }

    /**
     * Observa de forma reactiva todas las sesiones de chat de un usuario junto con sus mensajes en Room.
     */
    fun observeSessionsWithTurns(userEmail: String?): Flow<List<AiChatSessionThread>> {
        val userKey = normalizeEmailKey(userEmail)
        return combine(
            aiChatDao.observeSessionsForUser(userKey),
            aiChatDao.observeAllMessagesForUser(userKey)
        ) { sessions: List<AiChatSessionEntity>, allMessages: List<AiChatMessageEntity> ->
            val messagesBySession = allMessages.groupBy { it.sessionId }
            sessions.map { s ->
                val sessionTurns = messagesBySession[s.sessionId].orEmpty()
                    .sortedBy { it.timestamp }
                    .map { m ->
                        AiChatTurn(
                            id = m.messageId,
                            userPrompt = m.userPrompt,
                            aiResponse = m.aiResponse,
                            modeName = m.modeName,
                            thinkingDepthName = m.thinkingDepthName,
                            timestamp = m.timestamp,
                            learnedFactTitle = m.learnedFactTitle
                        )
                    }
                AiChatSessionThread(
                    id = s.sessionId,
                    title = s.title,
                    createdAt = s.createdAt,
                    updatedAt = s.updatedAt,
                    turns = sessionTurns
                )
            }
        }
    }

    /**
     * Observa de forma reactiva los hechos aprendidos por la IA en Room Database.
     */
    fun observeLearnedFacts(): Flow<List<LearnedBasilianFact>> {
        return aiChatDao.observeAllLearnedFacts().map { entities ->
            entities.map { e ->
                LearnedBasilianFact(
                    id = e.factId,
                    title = e.title,
                    details = e.details,
                    category = e.category,
                    timestamp = e.timestamp,
                    verifiedWithGoogle = e.verifiedWithGoogle
                )
            }
        }
    }

    /**
     * Inicializa y migra automáticamente las sesiones y conocimientos previos hacia Room Database
     * para garantizar que ninguna conversación previa se pierda.
     */
    suspend fun ensureInitializedAndMigrated(userEmail: String?): Pair<List<AiChatSessionThread>, String> {
        val userKey = normalizeEmailKey(userEmail)

        // 1. Sembrar o migrar hechos aprendidos en Room
        val existingFacts = aiChatDao.getAllLearnedFactsOnce()
        if (existingFacts.isEmpty()) {
            val prefsFacts = BasilianAiMemory.getAllLearnedFacts(appContext)
            val entities = prefsFacts.map { f ->
                AiLearnedFactEntity(
                    factId = f.id,
                    title = f.title,
                    details = f.details,
                    category = f.category,
                    timestamp = f.timestamp,
                    verifiedWithGoogle = f.verifiedWithGoogle
                )
            }
            if (entities.isNotEmpty()) {
                aiChatDao.upsertLearnedFacts(entities)
            }
        }

        // 2. Sembrar o migrar sesiones de chat del usuario en Room
        var roomSessions = aiChatDao.getSessionsForUserOnce(userKey)
        if (roomSessions.isEmpty()) {
            val legacyThreads = BasilianAiMemory.getChatThreadsForUser(appContext, userEmail)
            val activeLegacyId = BasilianAiMemory.getActiveChatIdForUser(appContext, userEmail)
            if (legacyThreads.isNotEmpty()) {
                for (thread in legacyThreads) {
                    aiChatDao.upsertSession(
                        AiChatSessionEntity(
                            sessionId = thread.id,
                            userEmail = userKey,
                            title = thread.title,
                            createdAt = thread.createdAt,
                            updatedAt = thread.updatedAt,
                            isActive = thread.id == activeLegacyId
                        )
                    )
                    for (turn in thread.turns) {
                        aiChatDao.insertMessage(
                            AiChatMessageEntity(
                                messageId = turn.id,
                                sessionId = thread.id,
                                userEmail = userKey,
                                userPrompt = turn.userPrompt,
                                aiResponse = turn.aiResponse,
                                modeName = turn.modeName,
                                thinkingDepthName = turn.thinkingDepthName,
                                timestamp = turn.timestamp,
                                learnedFactTitle = turn.learnedFactTitle
                            )
                        )
                    }
                }
            } else {
                val defaultSessionId = "chat_main_${System.currentTimeMillis()}"
                aiChatDao.upsertSession(
                    AiChatSessionEntity(
                        sessionId = defaultSessionId,
                        userEmail = userKey,
                        title = "Conversación Principal IA",
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis(),
                        isActive = true
                    )
                )
            }
            roomSessions = aiChatDao.getSessionsForUserOnce(userKey)
        }

        val activeSession = roomSessions.firstOrNull { it.isActive } ?: roomSessions.first()
        if (!activeSession.isActive) {
            aiChatDao.clearActiveSessionForUser(userKey)
            aiChatDao.markSessionActive(userKey, activeSession.sessionId)
        }

        val threads = loadFullThreadsOnce(userKey)
        return threads to activeSession.sessionId
    }

    suspend fun loadFullThreadsOnce(userEmail: String?): List<AiChatSessionThread> {
        val userKey = normalizeEmailKey(userEmail)
        val sessions = aiChatDao.getSessionsForUserOnce(userKey)
        return sessions.map { s ->
            val messages = aiChatDao.getMessagesForSessionOnce(s.sessionId)
            AiChatSessionThread(
                id = s.sessionId,
                title = s.title,
                createdAt = s.createdAt,
                updatedAt = s.updatedAt,
                turns = messages.map { m ->
                    AiChatTurn(
                        id = m.messageId,
                        userPrompt = m.userPrompt,
                        aiResponse = m.aiResponse,
                        modeName = m.modeName,
                        thinkingDepthName = m.thinkingDepthName,
                        timestamp = m.timestamp,
                        learnedFactTitle = m.learnedFactTitle
                    )
                }
            )
        }
    }

    suspend fun createNewSession(userEmail: String?, customTitle: String? = null): AiChatSessionThread {
        val userKey = normalizeEmailKey(userEmail)
        val timeFmt = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date())
        val now = System.currentTimeMillis()
        val sessionId = "chat_$now"
        val title = customTitle?.takeIf { it.isNotBlank() } ?: "Nuevo Chat ($timeFmt)"

        aiChatDao.clearActiveSessionForUser(userKey)
        val entity = AiChatSessionEntity(
            sessionId = sessionId,
            userEmail = userKey,
            title = title,
            createdAt = now,
            updatedAt = now,
            isActive = true
        )
        aiChatDao.upsertSession(entity)
        BasilianAiMemory.createNewChatThreadWithId(appContext, userEmail, sessionId, title)

        return AiChatSessionThread(
            id = sessionId,
            title = title,
            createdAt = now,
            updatedAt = now,
            turns = emptyList()
        )
    }

    suspend fun selectSession(userEmail: String?, sessionId: String): List<AiChatSessionThread> {
        val userKey = normalizeEmailKey(userEmail)
        aiChatDao.clearActiveSessionForUser(userKey)
        aiChatDao.markSessionActive(userKey, sessionId)
        BasilianAiMemory.setActiveChatIdForUser(appContext, userEmail, sessionId)
        return loadFullThreadsOnce(userKey)
    }

    suspend fun deleteSession(userEmail: String?, sessionId: String): Pair<List<AiChatSessionThread>, String> {
        val userKey = normalizeEmailKey(userEmail)
        aiChatDao.deleteMessagesForSession(sessionId)
        aiChatDao.deleteSession(sessionId)
        BasilianAiMemory.deleteChatThread(appContext, userEmail, sessionId)

        val remaining = aiChatDao.getSessionsForUserOnce(userKey)
        if (remaining.isEmpty()) {
            val fallback = createNewSession(userEmail, "Conversación Principal IA")
            return listOf(fallback) to fallback.id
        }
        val nextActive = remaining.firstOrNull { it.isActive } ?: remaining.first()
        aiChatDao.clearActiveSessionForUser(userKey)
        aiChatDao.markSessionActive(userKey, nextActive.sessionId)
        BasilianAiMemory.setActiveChatIdForUser(appContext, userEmail, nextActive.sessionId)
        return loadFullThreadsOnce(userKey) to nextActive.sessionId
    }

    suspend fun appendTurnToSession(
        userEmail: String?,
        sessionId: String,
        turn: AiChatTurn
    ): List<AiChatSessionThread> {
        val userKey = normalizeEmailKey(userEmail)
        val now = System.currentTimeMillis()
        val cleanPromptTitle = turn.userPrompt
            .removePrefix("[RESUMIR_TEXTO]")
            .removePrefix("[SUBIR_TEXTO]")
            .removePrefix("[CREAR_MUSICA]")
            .removePrefix("[CREAR_IMAGEN]")
            .trim()
            .take(44)

        val sessions = aiChatDao.getSessionsForUserOnce(userKey)
        val existingSession = sessions.firstOrNull { it.sessionId == sessionId }
        val existingMessages = aiChatDao.getMessagesForSessionOnce(sessionId)

        if (existingSession != null) {
            val newTitle = if (existingMessages.isEmpty() && cleanPromptTitle.isNotBlank()) {
                cleanPromptTitle
            } else {
                existingSession.title
            }
            aiChatDao.upsertSession(
                existingSession.copy(
                    title = newTitle,
                    updatedAt = now,
                    isActive = true
                )
            )
        } else {
            aiChatDao.clearActiveSessionForUser(userKey)
            aiChatDao.upsertSession(
                AiChatSessionEntity(
                    sessionId = sessionId,
                    userEmail = userKey,
                    title = cleanPromptTitle.ifBlank { "Conversación IA" },
                    createdAt = now,
                    updatedAt = now,
                    isActive = true
                )
            )
        }

        aiChatDao.insertMessage(
            AiChatMessageEntity(
                messageId = turn.id,
                sessionId = sessionId,
                userEmail = userKey,
                userPrompt = turn.userPrompt,
                aiResponse = turn.aiResponse,
                modeName = turn.modeName,
                thinkingDepthName = turn.thinkingDepthName,
                timestamp = turn.timestamp,
                learnedFactTitle = turn.learnedFactTitle
            )
        )

        // Mantener sincronizado BasilianAiMemory para compatibilidad
        BasilianAiMemory.appendTurnToChat(
            context = appContext,
            email = userEmail,
            chatId = sessionId,
            turn = turn
        )

        return loadFullThreadsOnce(userKey)
    }

    suspend fun addLearnedFact(
        title: String,
        details: String,
        category: String = "AUTOAPRENDIZAJE",
        verifiedWithGoogle: Boolean = true
    ): List<LearnedBasilianFact> {
        val saved = BasilianAiMemory.addLearnedFact(
            context = appContext,
            title = title,
            details = details,
            category = category,
            verifiedWithGoogle = verifiedWithGoogle
        )
        aiChatDao.upsertLearnedFact(
            AiLearnedFactEntity(
                factId = saved.id,
                title = saved.title,
                details = saved.details,
                category = saved.category,
                timestamp = saved.timestamp,
                verifiedWithGoogle = saved.verifiedWithGoogle
            )
        )
        return getAllLearnedFactsOnce()
    }

    suspend fun deleteLearnedFact(factId: String): List<LearnedBasilianFact> {
        aiChatDao.deleteLearnedFact(factId)
        BasilianAiMemory.deleteLearnedFact(appContext, factId)
        return getAllLearnedFactsOnce()
    }

    suspend fun getAllLearnedFactsOnce(): List<LearnedBasilianFact> {
        val entities = aiChatDao.getAllLearnedFactsOnce()
            .filterNot { e ->
                e.title.startsWith("Memoria de charla:", ignoreCase = true) ||
                    e.title.startsWith("Razonamiento hipotético", ignoreCase = true) ||
                    e.title.startsWith("Preferencia/Corrección", ignoreCase = true) ||
                    e.details.contains("Se conversó sobre:", ignoreCase = true)
            }
        if (entities.isEmpty()) {
            return BasilianAiMemory.getAllLearnedFacts(appContext)
        }
        return entities.map { e ->
            LearnedBasilianFact(
                id = e.factId,
                title = e.title,
                details = e.details,
                category = e.category,
                timestamp = e.timestamp,
                verifiedWithGoogle = e.verifiedWithGoogle
            )
        }
    }

    /**
     * Construye el contexto histórico desde Room Database de forma silenciosa:
     * provee hechos verificados sin inducir a la IA a mencionar conversaciones pasadas ni hacer meta-análisis.
     */
    suspend fun buildHistoricalContextFromRoom(
        userEmail: String?,
        activeSessionId: String,
        language: AppLanguage = AppLanguage.ES
    ): String {
        val facts = getAllLearnedFactsOnce()
            .filter { it.category in setOf("HISTORIA", "VOCACIONES", "CURIA", "LITURGIA", "COMUNIDAD") }
        if (facts.isEmpty()) return ""

        val sb = StringBuilder()
        sb.append("\n[DATOS VERIFICADOS DE LA CONGREGACIÓN DE SAN BASILIO EN ROOM DATABASE (ÚSALOS SOLO SI EL USUARIO PREGUNTA POR ELLOS; NUNCA LOS MENCIONES SI NO TIENEN RELACIÓN CON LA PREGUNTA ACTUAL)]\n")
        for (f in facts.take(8)) {
            sb.append("- ${f.title}: ${f.details}\n")
        }
        return sb.toString()
    }
}
