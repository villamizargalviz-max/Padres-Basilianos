package com.example.data.db

import androidx.room.*
import com.example.data.model.CachedLiturgiaDayEntity
import com.example.data.model.CachedSaintEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LiturgiaCacheDao {

    @Query("SELECT * FROM cached_ordo_liturgia WHERE dateKey = :dateKey LIMIT 1")
    fun getCachedDay(dateKey: String): Flow<CachedLiturgiaDayEntity?>

    @Query("SELECT * FROM cached_ordo_liturgia WHERE dateKey = :dateKey LIMIT 1")
    suspend fun getCachedDaySync(dateKey: String): CachedLiturgiaDayEntity?

    @Query("SELECT * FROM cached_ordo_liturgia ORDER BY cachedAtTimestamp DESC")
    fun getAllCachedDays(): Flow<List<CachedLiturgiaDayEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedDay(entity: CachedLiturgiaDayEntity)

    @Query("DELETE FROM cached_ordo_liturgia WHERE dateKey = :dateKey")
    suspend fun deleteCachedDay(dateKey: String)

    @Query("SELECT COUNT(*) FROM cached_ordo_liturgia")
    fun getCachedDaysCount(): Flow<Int>

    // Daily Saints (Santoral Católico y Ordo Colombiano)
    @Query("SELECT * FROM cached_daily_saints WHERE month = :month AND day = :day")
    fun getSaintsForDay(month: Int, day: Int): Flow<List<CachedSaintEntity>>

    @Query("SELECT * FROM cached_daily_saints WHERE month = :month AND day = :day")
    suspend fun getSaintsForDaySync(month: Int, day: Int): List<CachedSaintEntity>

    @Query("SELECT * FROM cached_daily_saints ORDER BY month ASC, day ASC")
    fun getAllSaints(): Flow<List<CachedSaintEntity>>

    @Query("SELECT * FROM cached_daily_saints WHERE isColombianPatron = 1")
    fun getColombianSaints(): Flow<List<CachedSaintEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaints(saints: List<CachedSaintEntity>)

    @Query("SELECT COUNT(*) FROM cached_daily_saints")
    suspend fun getSaintsCount(): Int
}
