package com.example.data.model

data class Founder(
    val id: String,
    val name: String,
    val years: String,
    val role: String,
    val title: String,
    val biography: String,
    val keyContribution: String,
    val spiritualVirtue: String,
    val quoteOrEpitaph: String,
    val isFrenchFounder: Boolean = true,
    val photoUri: String? = null
)

data class PatronSaint(
    val id: String,
    val name: String,
    val feastDay: String,
    val title: String,
    val iconName: String,
    val biography: String,
    val connectionToBasilians: String,
    val virtueForDailyLife: String,
    val patronPrayer: String
)

data class HistoricalEvent(
    val year: String,
    val dateOrPeriod: String,
    val title: String,
    val location: String,
    val summary: String,
    val significance: String,
    val category: HistoryCategory
)

enum class HistoryCategory {
    FOUNDATION,
    PONTIFICAL_APPROVAL,
    EXPANSION_AMERICA,
    COLOMBIA_MISSION,
    RENEWAL_BICENTENARY,
    RECENT_2026
}

data class PrayerItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val timeOfDay: String, // "Mañana", "Mediodía", "Tarde", "Noche", "Cualquier momento"
    val sourceDocument: String, // e.g. "Costumbres Basilianas E.18", "Forma de Vida Basiliana 51"
    val latinTitle: String? = null,
    val prayerText: String,
    val reflection: String
)

data class DailyInspiration(
    val dayOfYear: Int,
    val scriptureRef: String,
    val scriptureText: String,
    val basilianMottoFocus: String, // "Bondad" (Bonitatem), "Disciplina" (Disciplinam), or "Ciencia" (Scientiam)
    val stBasilWisdom: String,
    val practicalActionForToday: String
)

data class BasilianCalendarDate(
    val id: String,
    val month: Int, // 1 to 12
    val day: Int,   // 1 to 31
    val dateFormatted: String, // e.g., "2 de Enero", "21 de Noviembre"
    val title: String,
    val category: CalendarCategory,
    val briefDescription: String,
    val detailedHistory: String,
    val liturgicalRank: String, // "Solemnidad Patronal", "Fiesta Titular", "Memoria Obligatoria", "Aniversario Histórico"
    val suggestedPrayerId: String? = null,
    val relatedPatronOrFounderId: String? = null,
    val isBasilianHighlight: Boolean = true,
    val patronName: String? = null,
    val basilianRelationship: String? = null
)

enum class CalendarCategory(val displayName: String) {
    ALL("Todas las fechas"),
    PATRON_FEAST("Santos y Patronos"),
    FOUNDATION_ANNIVERSARY("Aniversarios CSB"),
    MISSION_COLOMBIA("Misión Colombia"),
    SPIRITUAL_MEMORIAL("Memorias y Jubileos")
}
