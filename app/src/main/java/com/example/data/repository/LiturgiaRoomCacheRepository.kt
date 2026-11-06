package com.example.data.repository

import com.example.data.db.LiturgiaCacheDao
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class LiturgiaRoomCacheRepository(
    private val liturgiaCacheDao: LiturgiaCacheDao
) {

    fun getCachedDay(dateKey: String): Flow<CachedLiturgiaDayEntity?> {
        return liturgiaCacheDao.getCachedDay(dateKey)
    }

    suspend fun getCachedDaySync(dateKey: String): CachedLiturgiaDayEntity? = withContext(Dispatchers.IO) {
        liturgiaCacheDao.getCachedDaySync(dateKey)
    }

    fun getAllCachedDays(): Flow<List<CachedLiturgiaDayEntity>> {
        return liturgiaCacheDao.getAllCachedDays()
    }

    fun getCachedDaysCount(): Flow<Int> {
        return liturgiaCacheDao.getCachedDaysCount()
    }

    /**
     * Guarda en Room la Liturgia de las Horas de un día específico del Ordo Colombiano.
     * Incluye todas las horas canónicas en Español e Inglés y el resumen de santos.
     */
    suspend fun saveToRoomCache(day: ColombianLiturgicalDay) = withContext(Dispatchers.IO) {
        val calendar = Calendar.getInstance()
        val saints = DailySaintsRepository.getSaintsForDate(calendar)
        val saintsSummary = saints.joinToString(" • ") { "${it.name} (${it.liturgicalRank})" }

        val laudes = day.hours[LiturgicalHourType.LAUDES]
        val visperas = day.hours[LiturgicalHourType.VISPERAS]
        val completas = day.hours[LiturgicalHourType.COMPLETAS]
        val oficio = day.hours[LiturgicalHourType.OFICIO_DE_LECTURA]
        val intermedia = day.hours[LiturgicalHourType.SEXTA] ?: day.hours[LiturgicalHourType.TERCIA]
        val invitatorio = day.hours[LiturgicalHourType.INVITATORIO]

        val entity = CachedLiturgiaDayEntity(
            dateKey = day.id,
            celebrationNameEs = day.titleEs,
            celebrationNameEn = day.titleEn,
            rankEs = day.rankEs,
            rankEn = day.rankEn,
            seasonName = day.season.nameEs,
            colorName = day.color.nameEs,
            colorHex = day.color.hexCode,
            psalterWeekEs = day.psalterWeekEs,
            psalterWeekEn = day.psalterWeekEn,
            ordoNotesEs = day.ordoNotesEs,
            ordoNotesEn = day.ordoNotesEn,
            basilianConnectionEs = day.basilianConnectionEs,
            basilianConnectionEn = day.basilianConnectionEn,
            saintsSummary = saintsSummary,
            laudesEsText = formatHourToText(laudes?.es),
            laudesEnText = formatHourToText(laudes?.en),
            visperasEsText = formatHourToText(visperas?.es),
            visperasEnText = formatHourToText(visperas?.en),
            completasEsText = formatHourToText(completas?.es),
            completasEnText = formatHourToText(completas?.en),
            oficioEsText = formatHourToText(oficio?.es),
            oficioEnText = formatHourToText(oficio?.en),
            intermediaEsText = formatHourToText(intermedia?.es),
            intermediaEnText = formatHourToText(intermedia?.en),
            invitatorioEsText = formatHourToText(invitatorio?.es),
            invitatorioEnText = formatHourToText(invitatorio?.en),
            readingsCitation = laudes?.es?.briefReading?.citation ?: "Lecturas del día según el Ordo Colombiano",
            cachedAtTimestamp = System.currentTimeMillis(),
            sourceUrl = "https://web-ordo-colombiano.cec.org.co/liturgia-horas"
        )

        liturgiaCacheDao.insertCachedDay(entity)
    }

    /**
     * Asegura que el Santoral y las Fiestas de los Santos estén cacheados en Room.
     */
    suspend fun populateSaintsDatabaseIfNeeded() = withContext(Dispatchers.IO) {
        val count = liturgiaCacheDao.getSaintsCount()
        if (count == 0) {
            val allSaints = DailySaintsRepository.getAllKnownSaints().map { it.toEntity() }
            liturgiaCacheDao.insertSaints(allSaints)
        }
    }

    fun getSaintsForDay(month: Int, day: Int): Flow<List<DailySaint>> {
        return liturgiaCacheDao.getSaintsForDay(month, day).map { entities ->
            if (entities.isNotEmpty()) {
                entities.map { DailySaint.fromEntity(it) }
            } else {
                DailySaintsRepository.getSaintsForMonthAndDay(month, day)
            }
        }
    }

    suspend fun deleteCachedDay(dateKey: String) = withContext(Dispatchers.IO) {
        liturgiaCacheDao.deleteCachedDay(dateKey)
    }

    private fun formatHourToText(content: HourContent?): String {
        if (content == null) return ""
        val sb = StringBuilder()
        sb.appendLine("## ${content.hourName}")
        sb.appendLine("* ${content.ordoRubricNote}")
        sb.appendLine()
        sb.appendLine(content.introductoryVerse)
        sb.appendLine()
        sb.appendLine("### HIMNO: ${content.hymnTitle}")
        sb.appendLine(content.hymnText)
        sb.appendLine()
        sb.appendLine("### SALMODIA")
        content.psalms.forEach { psalm ->
            sb.appendLine("Antífona: ${psalm.antiphon}")
            sb.appendLine("${psalm.title} - ${psalm.subtitle}")
            sb.appendLine(psalm.psalmText)
            sb.appendLine(psalm.gloriaPatri)
            sb.appendLine()
        }
        sb.appendLine("### LECTURA BREVE: ${content.briefReading.title} (${content.briefReading.citation})")
        sb.appendLine(content.briefReading.text)
        sb.appendLine("V/. ${content.briefReading.responsoryVerse}")
        sb.appendLine("R/. ${content.briefReading.responsoryAnswer}")
        sb.appendLine()
        if (content.canticleTitle != null && content.canticleText != null) {
            sb.appendLine("### CÁNTICO EVANGÉLICO: ${content.canticleTitle}")
            if (content.canticleAntiphon != null) {
                sb.appendLine("Antífona: ${content.canticleAntiphon}")
            }
            sb.appendLine(content.canticleText)
            sb.appendLine()
        }
        if (content.preces != null) {
            sb.appendLine("### PRECES")
            sb.appendLine(content.preces.intro)
            content.preces.petitions.forEach { (inv, resp) ->
                sb.appendLine("• $inv  R/. $resp")
            }
            sb.appendLine(content.preces.conclusion)
            sb.appendLine()
        }
        sb.appendLine("Padre Nuestro: ${content.ourFather}")
        sb.appendLine()
        sb.appendLine("Oración: ${content.concludingPrayer}")
        sb.appendLine()
        sb.appendLine("Bendición: ${content.blessing}")
        if (content.marianAntiphon != null) {
            sb.appendLine()
            sb.appendLine("Antífona Mariana: ${content.marianAntiphon}")
        }
        return sb.toString()
    }
}
