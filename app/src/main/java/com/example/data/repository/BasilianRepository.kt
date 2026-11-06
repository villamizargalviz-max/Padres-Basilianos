package com.example.data.repository

import com.example.data.db.BasilianDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class BasilianRepository(private val dao: BasilianDao) {

    // Content queries
    fun getPatrons(): List<PatronSaint> = BasilianStaticData.patrons
    fun getFounders(): List<Founder> = BasilianStaticData.founders
    fun getTimeline(): List<HistoricalEvent> = BasilianStaticData.historicalTimeline
    fun getDailyPrayers(): List<PrayerItem> = BasilianStaticData.dailyPrayers
    fun getCalendarDates(): List<BasilianCalendarDate> = BasilianStaticData.calendarDates

    fun getTodayInspiration(): DailyInspiration {
        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val inspirations = BasilianStaticData.dailyInspirations
        return inspirations[dayOfYear % inspirations.size]
    }

    // Room persistence: Reflections
    val allReflections: Flow<List<UserReflection>> = dao.getAllReflections()

    suspend fun addReflection(title: String, content: String, category: String, virtueTag: String) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val dateString = dateFormat.format(Date())
        dao.insertReflection(
            UserReflection(
                dateText = dateString,
                title = title,
                content = content,
                category = category,
                virtueTag = virtueTag
            )
        )
    }

    suspend fun deleteReflection(reflection: UserReflection) {
        dao.deleteReflection(reflection)
    }

    // Room persistence: Daily prayer checklist
    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getTodayPrayerStatus(): Flow<DailyPrayerStatus?> {
        return dao.getDailyPrayerStatus(getTodayDateString())
    }

    suspend fun togglePrayerDone(prayerId: String) {
        val today = getTodayDateString()
        val current = dao.getDailyPrayerStatusSync(today) ?: DailyPrayerStatus(dateString = today)
        val updated = when (prayerId) {
            "laudes" -> current.copy(laudesDone = !current.laudesDone)
            "angelus" -> current.copy(angelusDone = !current.angelusDone)
            "visperas" -> current.copy(visperasDone = !current.visperasDone)
            "completas" -> current.copy(completasDone = !current.completasDone)
            "examen" -> current.copy(examenDone = !current.examenDone)
            else -> current
        }
        dao.saveDailyPrayerStatus(updated)
    }

    // Room persistence: Favorites
    val allFavorites: Flow<List<FavoriteItem>> = dao.getAllFavorites()

    fun isFavorite(id: String): Flow<Boolean> = dao.isFavorite(id)

    suspend fun toggleFavorite(itemId: String, itemType: String, title: String, isFavNow: Boolean) {
        if (isFavNow) {
            dao.removeFavorite(itemId)
        } else {
            dao.addFavorite(FavoriteItem(itemId = itemId, itemType = itemType, title = title))
        }
    }

    // Room persistence: Date Reminders
    val allReminders: Flow<List<DateReminder>> = dao.getAllReminders()

    fun isReminderSet(eventId: String): Flow<Boolean> = dao.isReminderSet(eventId)

    suspend fun toggleReminder(event: BasilianCalendarDate, isSet: Boolean) {
        if (isSet) {
            dao.deleteReminder(event.id)
        } else {
            dao.insertReminder(
                DateReminder(
                    eventId = event.id,
                    title = event.title,
                    dateText = event.dateFormatted,
                    description = event.briefDescription,
                    isEnabled = true
                )
            )
        }
    }
}
