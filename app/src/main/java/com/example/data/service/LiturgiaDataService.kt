package com.example.data.service

import android.util.Log
import com.example.data.model.*
import com.example.data.repository.LiturgiaOrdoColombianoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

object LiturgiaDataService {
    private const val TAG = "LiturgiaDataService"
    private const val CALAPI_URL = "http://calapi.inadiutorium.cz/api/v0/en/calendars/default/today"

    data class OnlineCalendarInfo(
        val date: String,
        val season: String,
        val seasonWeek: Int,
        val weekday: String,
        val celebrationTitle: String,
        val celebrationColour: String,
        val rank: String,
        val isLive: Boolean = true
    )

    suspend fun fetchTodayUniversalCalendar(): OnlineCalendarInfo = withContext(Dispatchers.IO) {
        try {
            val url = URL(CALAPI_URL)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 4000
                readTimeout = 4000
                setRequestProperty("User-Agent", "PadresBasilianosApp/1.0")
                setRequestProperty("Accept", "application/json")
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
                val root = JSONObject(jsonString)
                val date = root.optString("date", "")
                val season = root.optString("season", "ordinary")
                val seasonWeek = root.optInt("season_week", 1)
                val weekday = root.optString("weekday", "")

                var celebrationTitle = ""
                var celebrationColour = "green"
                var rank = "weekday"

                val celebrations = root.optJSONArray("celebrations")
                if (celebrations != null && celebrations.length() > 0) {
                    val first = celebrations.getJSONObject(0)
                    celebrationTitle = first.optString("title", "")
                    celebrationColour = first.optString("colour", "green")
                    rank = first.optString("rank", "weekday")
                }

                return@withContext OnlineCalendarInfo(
                    date = date,
                    season = season,
                    seasonWeek = seasonWeek,
                    weekday = weekday,
                    celebrationTitle = celebrationTitle,
                    celebrationColour = celebrationColour,
                    rank = rank,
                    isLive = true
                )
            }
        } catch (e: Exception) {
            Log.w(TAG, "Offline fallback for Liturgia: ${e.message}")
        }

        // Offline canonical fallback
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        OnlineCalendarInfo(
            date = sdf.format(cal.time),
            season = "ordinary",
            seasonWeek = (cal.get(Calendar.WEEK_OF_YEAR) % 34) + 1,
            weekday = SimpleDateFormat("EEEE", Locale.US).format(cal.time).lowercase(),
            celebrationTitle = "Feria Litúrgica del Tiempo Ordinario",
            celebrationColour = "green",
            rank = "weekday",
            isLive = false
        )
    }

    suspend fun getSynchronizedLiturgicalDay(): ColombianLiturgicalDay {
        val onlineInfo = fetchTodayUniversalCalendar()
        return LiturgiaOrdoColombianoRepository.buildSynchronizedDay(onlineInfo)
    }
}
