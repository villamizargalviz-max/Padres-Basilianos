package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DailyPrayerStatus
import com.example.data.model.DateReminder
import com.example.data.model.FavoriteItem
import com.example.data.model.UserReflection
import kotlinx.coroutines.flow.Flow

@Dao
interface BasilianDao {

    // User Reflections
    @Query("SELECT * FROM user_reflections ORDER BY timestamp DESC")
    fun getAllReflections(): Flow<List<UserReflection>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReflection(reflection: UserReflection): Long

    @Delete
    suspend fun deleteReflection(reflection: UserReflection)

    // Daily Prayer Status
    @Query("SELECT * FROM daily_prayer_status WHERE dateString = :date LIMIT 1")
    fun getDailyPrayerStatus(date: String): Flow<DailyPrayerStatus?>

    @Query("SELECT * FROM daily_prayer_status WHERE dateString = :date LIMIT 1")
    suspend fun getDailyPrayerStatusSync(date: String): DailyPrayerStatus?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDailyPrayerStatus(status: DailyPrayerStatus)

    // Favorites
    @Query("SELECT * FROM favorite_items ORDER BY savedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteItem>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_items WHERE itemId = :id)")
    fun isFavorite(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteItem)

    @Query("DELETE FROM favorite_items WHERE itemId = :id")
    suspend fun removeFavorite(id: String)

    // Date Reminders for Calendar
    @Query("SELECT * FROM date_reminders ORDER BY createdAt DESC")
    fun getAllReminders(): Flow<List<DateReminder>>

    @Query("SELECT EXISTS(SELECT 1 FROM date_reminders WHERE eventId = :id)")
    fun isReminderSet(id: String): Flow<Boolean>

    @Query("SELECT * FROM date_reminders WHERE eventId = :id LIMIT 1")
    suspend fun getReminderSync(id: String): DateReminder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: DateReminder)

    @Query("DELETE FROM date_reminders WHERE eventId = :id")
    suspend fun deleteReminder(id: String)
}
