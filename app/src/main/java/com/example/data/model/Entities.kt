package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_reflections")
data class UserReflection(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val title: String,
    val content: String,
    val category: String = "Reflexión", // "Reflexión", "Gratitud", "Petición de Oración"
    val virtueTag: String = "Bondad" // "Bondad", "Disciplina", "Ciencia"
)

@Entity(tableName = "daily_prayer_status")
data class DailyPrayerStatus(
    @PrimaryKey val dateString: String, // YYYY-MM-DD
    val laudesDone: Boolean = false,
    val angelusDone: Boolean = false,
    val visperasDone: Boolean = false,
    val completasDone: Boolean = false,
    val examenDone: Boolean = false
)

@Entity(tableName = "favorite_items")
data class FavoriteItem(
    @PrimaryKey val itemId: String, // ID of patron, founder, or prayer
    val itemType: String, // "PRAYER", "PATRON", "FOUNDER"
    val title: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "date_reminders")
data class DateReminder(
    @PrimaryKey val eventId: String,
    val title: String,
    val dateText: String,
    val description: String = "",
    val isEnabled: Boolean = true,
    val reminderHour: Int = 8,
    val reminderMinute: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "cached_ordo_liturgia")
data class CachedLiturgiaDayEntity(
    @PrimaryKey val dateKey: String, // e.g. "2026-09-28" or ID
    val celebrationNameEs: String,
    val celebrationNameEn: String,
    val rankEs: String,
    val rankEn: String,
    val seasonName: String,
    val colorName: String,
    val colorHex: Long,
    val psalterWeekEs: String,
    val psalterWeekEn: String,
    val ordoNotesEs: String,
    val ordoNotesEn: String,
    val basilianConnectionEs: String = "",
    val basilianConnectionEn: String = "",
    val saintsSummary: String = "",
    val laudesEsText: String = "",
    val laudesEnText: String = "",
    val visperasEsText: String = "",
    val visperasEnText: String = "",
    val completasEsText: String = "",
    val completasEnText: String = "",
    val oficioEsText: String = "",
    val oficioEnText: String = "",
    val intermediaEsText: String = "",
    val intermediaEnText: String = "",
    val invitatorioEsText: String = "",
    val invitatorioEnText: String = "",
    val readingsCitation: String = "",
    val cachedAtTimestamp: Long = System.currentTimeMillis(),
    val sourceUrl: String = "https://web-ordo-colombiano.cec.org.co/liturgia-horas"
)

@Entity(tableName = "cached_daily_saints")
data class CachedSaintEntity(
    @PrimaryKey val saintId: String,
    val month: Int,
    val day: Int,
    val saintName: String,
    val title: String,
    val liturgicalRank: String,
    val shortBiography: String,
    val patronage: String,
    val collectPrayer: String,
    val isColombianPatron: Boolean = false,
    val feastDateDisplay: String = ""
)

@Entity(tableName = "ai_chat_sessions")
data class AiChatSessionEntity(
    @PrimaryKey val sessionId: String,
    val userEmail: String,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = false
)

@Entity(tableName = "ai_chat_messages")
data class AiChatMessageEntity(
    @PrimaryKey val messageId: String,
    val sessionId: String,
    val userEmail: String,
    val userPrompt: String,
    val aiResponse: String,
    val modeName: String = "HISTORIA_COMPLETA",
    val thinkingDepthName: String = "EQUILIBRADO",
    val timestamp: Long = System.currentTimeMillis(),
    val learnedFactTitle: String? = null
)

@Entity(tableName = "ai_learned_facts")
data class AiLearnedFactEntity(
    @PrimaryKey val factId: String,
    val title: String,
    val details: String,
    val category: String,
    val timestamp: Long = System.currentTimeMillis(),
    val verifiedWithGoogle: Boolean = true
)



