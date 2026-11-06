package com.example.data.model

enum class LiturgicalLanguage(val code: String, val label: String, val flag: String) {
    ESPANOL("es", "Español", "🇨🇴"),
    ENGLISH("en", "English", "🇺🇸")
}

enum class LiturgicalHourType(
    val id: String,
    val titleEs: String,
    val titleEn: String,
    val suggestedTime: String,
    val iconName: String
) {
    INVITATORIO(
        id = "invitatorio",
        titleEs = "Invitatorio",
        titleEn = "Invitatory",
        suggestedTime = "Al comenzar el día",
        iconName = "WbSunny"
    ),
    OFICIO_DE_LECTURA(
        id = "oficio_lectura",
        titleEs = "Oficio de Lectura",
        titleEn = "Office of Readings",
        suggestedTime = "Madrugada o en cualquier momento",
        iconName = "AutoStories"
    ),
    LAUDES(
        id = "laudes",
        titleEs = "Laudes",
        titleEn = "Morning Prayer (Lauds)",
        suggestedTime = "6:00 AM - 9:00 AM",
        iconName = "WbTwilight"
    ),
    TERCIA(
        id = "tercia",
        titleEs = "Hora Tercia (Media Mañana)",
        titleEn = "Midmorning (Terce)",
        suggestedTime = "9:00 AM",
        iconName = "Schedule"
    ),
    SEXTA(
        id = "sexta",
        titleEs = "Hora Sexta (Mediodía)",
        titleEn = "Midday (Sext)",
        suggestedTime = "12:00 PM",
        iconName = "WbSunny"
    ),
    NONA(
        id = "nona",
        titleEs = "Hora Nona (Media Tarde)",
        titleEn = "Midafternoon (None)",
        suggestedTime = "3:00 PM",
        iconName = "AccessTime"
    ),
    VISPERAS(
        id = "visperas",
        titleEs = "Vísperas",
        titleEn = "Evening Prayer (Vespers)",
        suggestedTime = "6:00 PM - 8:00 PM",
        iconName = "NightsStay"
    ),
    COMPLETAS(
        id = "completas",
        titleEs = "Completas",
        titleEn = "Night Prayer (Compline)",
        suggestedTime = "Antes del descanso nocturno",
        iconName = "Bedtime"
    )
}

enum class LiturgicalSeason(val nameEs: String, val nameEn: String) {
    TIEMPO_ORDINARIO("Tiempo Ordinario", "Ordinary Time"),
    CUARESMA("Tiempo de Cuaresma", "Lent"),
    PASCUA("Tiempo Pascual", "Easter Season"),
    ADVIENTO("Tiempo de Adviento", "Advent"),
    NAVIDAD("Tiempo de Navidad", "Christmas Season")
}

enum class LiturgicalColor(val nameEs: String, val nameEn: String, val hexCode: Long) {
    VERDE("Verde (Esperanza y vida cotidiana)", "Green (Hope and Ordinary Time)", 0xFF2E7D32),
    BLANCO("Blanco (Pureza, Resurrección y Solemnidades)", "White (Purity, Resurrection and Solemnities)", 0xFFF9A825),
    MORADO("Morado (Penitencia, Conversión y Espera)", "Violet (Penance and Expectation)", 0xFF6A1B9A),
    ROJO("Rojo (Espíritu Santo y Mártires)", "Red (Holy Spirit and Martyrs)", 0xFFC62828),
    ROSA("Rosa (Alegría en Gaudete y Laetare)", "Rose (Joy in Gaudete and Laetare)", 0xFFAD1457)
}

data class PsalmItem(
    val title: String,
    val subtitle: String = "",
    val antiphon: String,
    val psalmText: String,
    val gloriaPatri: String
)

data class ReadingItem(
    val title: String,
    val citation: String,
    val text: String,
    val responsoryVerse: String,
    val responsoryAnswer: String
)

data class PrecesItem(
    val intro: String,
    val petitions: List<Pair<String, String>>, // invocation to response/intention
    val conclusion: String
)

data class HourContent(
    val hourName: String,
    val ordoRubricNote: String,
    val introductoryVerse: String,
    val hymnTitle: String,
    val hymnText: String,
    val psalms: List<PsalmItem>,
    val briefReading: ReadingItem,
    val secondReading: ReadingItem? = null,
    val canticleAntiphon: String? = null,
    val canticleTitle: String? = null,
    val canticleText: String? = null,
    val preces: PrecesItem? = null,
    val ourFather: String,
    val concludingPrayer: String,
    val blessing: String,
    val marianAntiphon: String? = null,
    val examinationOfConscience: String? = null
)

data class BilingualLiturgicalHour(
    val hourType: LiturgicalHourType,
    val es: HourContent,
    val en: HourContent
)

data class ColombianLiturgicalDay(
    val id: String,
    val dateDisplayEs: String,
    val dateDisplayEn: String,
    val titleEs: String,
    val titleEn: String,
    val rankEs: String, // e.g. "Solemnidad Nacional - Ordo CEC Colombia"
    val rankEn: String, // e.g. "National Solemnity - CEC Colombia Ordo"
    val color: LiturgicalColor,
    val season: LiturgicalSeason,
    val psalterWeekEs: String, // "Semana I del Salterio"
    val psalterWeekEn: String, // "Week I of the Psalter"
    val ordoNotesEs: String,
    val ordoNotesEn: String,
    val basilianConnectionEs: String,
    val basilianConnectionEn: String,
    val hours: Map<LiturgicalHourType, BilingualLiturgicalHour>
)
