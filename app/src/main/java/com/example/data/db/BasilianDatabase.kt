package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AiChatMessageEntity
import com.example.data.model.AiChatSessionEntity
import com.example.data.model.AiLearnedFactEntity
import com.example.data.model.CachedLiturgiaDayEntity
import com.example.data.model.CachedSaintEntity
import com.example.data.model.DailyPrayerStatus
import com.example.data.model.DateReminder
import com.example.data.model.FavoriteItem
import com.example.data.model.UserReflection

@Database(
    entities = [
        UserReflection::class,
        DailyPrayerStatus::class,
        FavoriteItem::class,
        DateReminder::class,
        CachedLiturgiaDayEntity::class,
        CachedSaintEntity::class,
        AiChatSessionEntity::class,
        AiChatMessageEntity::class,
        AiLearnedFactEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class BasilianDatabase : RoomDatabase() {
    abstract fun basilianDao(): BasilianDao
    abstract fun liturgiaCacheDao(): LiturgiaCacheDao
    abstract fun aiChatDao(): AiChatDao

    companion object {
        @Volatile
        private var INSTANCE: BasilianDatabase? = null

        fun getDatabase(context: Context): BasilianDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BasilianDatabase::class.java,
                    "basilian_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
