package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.AiChatMessageEntity
import com.example.data.model.AiChatSessionEntity
import com.example.data.model.AiLearnedFactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AiChatDao {

    @Query("SELECT * FROM ai_chat_sessions WHERE userEmail = :userEmail ORDER BY updatedAt DESC")
    fun observeSessionsForUser(userEmail: String): Flow<List<AiChatSessionEntity>>

    @Query("SELECT * FROM ai_chat_sessions WHERE userEmail = :userEmail ORDER BY updatedAt DESC")
    suspend fun getSessionsForUserOnce(userEmail: String): List<AiChatSessionEntity>

    @Query("SELECT * FROM ai_chat_sessions WHERE sessionId = :sessionId LIMIT 1")
    suspend fun getSessionByIdSync(sessionId: String): AiChatSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(session: AiChatSessionEntity)

    @Query("UPDATE ai_chat_sessions SET isActive = 0 WHERE userEmail = :userEmail")
    suspend fun clearActiveSessionForUser(userEmail: String)

    @Query("UPDATE ai_chat_sessions SET isActive = 1, updatedAt = :updatedAt WHERE userEmail = :userEmail AND sessionId = :sessionId")
    suspend fun markSessionActive(userEmail: String, sessionId: String, updatedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM ai_chat_sessions WHERE sessionId = :sessionId")
    suspend fun deleteSession(sessionId: String)

    @Query("SELECT * FROM ai_chat_messages WHERE userEmail = :userEmail ORDER BY timestamp ASC")
    fun observeAllMessagesForUser(userEmail: String): Flow<List<AiChatMessageEntity>>

    @Query("SELECT * FROM ai_chat_messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun observeMessagesForSession(sessionId: String): Flow<List<AiChatMessageEntity>>

    @Query("SELECT * FROM ai_chat_messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    suspend fun getMessagesForSessionOnce(sessionId: String): List<AiChatMessageEntity>

    @Query("SELECT * FROM ai_chat_messages WHERE userEmail = :userEmail ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentMessagesForUserSync(userEmail: String, limit: Int = 40): List<AiChatMessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: AiChatMessageEntity)

    @Query("DELETE FROM ai_chat_messages WHERE sessionId = :sessionId")
    suspend fun deleteMessagesForSession(sessionId: String)

    @Query("SELECT * FROM ai_learned_facts ORDER BY timestamp DESC")
    fun observeAllLearnedFacts(): Flow<List<AiLearnedFactEntity>>

    @Query("SELECT * FROM ai_learned_facts ORDER BY timestamp DESC")
    suspend fun getAllLearnedFactsOnce(): List<AiLearnedFactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLearnedFact(fact: AiLearnedFactEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLearnedFacts(facts: List<AiLearnedFactEntity>)

    @Query("DELETE FROM ai_learned_facts WHERE factId = :factId")
    suspend fun deleteLearnedFact(factId: String)
}
