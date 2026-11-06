package com.example.data.model

enum class MemberCategory(val label: String) {
    ALL("Todos"),
    PRIEST("Sacerdote"),
    BISHOP("Obispo"),
    DEACON("Diácono"),
    SCHOLASTIC("Escolástico"),
    NOVICE("Novicio"),
    ASSOCIATE("Asociado")
}

data class BasilianDirectoryMember(
    val id: String,
    val name: String,
    val birthDateRaw: String, // e.g. "01 October 2000"
    val birthDay: Int,
    val birthMonth: Int, // 1 = Enero, 12 = Diciembre
    val birthYear: Int,
    val professionDate: String, // e.g. "07 January 2023"
    val ordinationDate: String = "", // e.g. "02 January 2004"
    val category: MemberCategory,
    val country: String, // "Colombia", "México", "Canadá", "USA", "Francia"
    val currentHouse: String,
    val address: String = "",
    val cellPhone: String = "",
    val email: String = "",
    val officePhone: String = "",
    val rolesOrMinistries: String = "",
    val photoUrl: String? = null
) {
    fun getFormattedBirthday(lang: AppLanguage = AppLanguage.ES): String {
        return when (lang) {
            AppLanguage.EN -> {
                val monthName = when (birthMonth) {
                    1 -> "January"; 2 -> "February"; 3 -> "March"; 4 -> "April"
                    5 -> "May"; 6 -> "June"; 7 -> "July"; 8 -> "August"
                    9 -> "September"; 10 -> "October"; 11 -> "November"; 12 -> "December"
                    else -> ""
                }
                "$monthName $birthDay"
            }
            AppLanguage.FR -> {
                val monthName = when (birthMonth) {
                    1 -> "Janvier"; 2 -> "Février"; 3 -> "Mars"; 4 -> "Avril"
                    5 -> "Mai"; 6 -> "Juin"; 7 -> "Juillet"; 8 -> "Août"
                    9 -> "Septembre"; 10 -> "Octobre"; 11 -> "Novembre"; 12 -> "Décembre"
                    else -> ""
                }
                "$birthDay $monthName"
            }
            AppLanguage.ES -> {
                val monthName = when (birthMonth) {
                    1 -> "Enero"; 2 -> "Febrero"; 3 -> "Marzo"; 4 -> "Abril"
                    5 -> "Mayo"; 6 -> "Junio"; 7 -> "Julio"; 8 -> "Agosto"
                    9 -> "Septiembre"; 10 -> "Octubre"; 11 -> "Noviembre"; 12 -> "Diciembre"
                    else -> ""
                }
                "$birthDay de $monthName"
            }
        }
    }

    /**
     * Calcula los días que faltan para el próximo cumpleaños respecto a una fecha (mes, día)
     */
    fun daysUntilBirthday(currentMonth: Int, currentDay: Int): Int {
        val daysInMonths = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        fun dayOfYear(m: Int, d: Int): Int {
            var sum = 0
            for (i in 0 until (m - 1)) {
                sum += daysInMonths[i]
            }
            return sum + d
        }

        val todayDoy = dayOfYear(currentMonth, currentDay)
        val bdayDoy = dayOfYear(birthMonth, birthDay)

        return if (bdayDoy >= todayDoy) {
            bdayDoy - todayDoy
        } else {
            (365 - todayDoy) + bdayDoy
        }
    }

    fun isBirthdayToday(currentMonth: Int, currentDay: Int): Boolean {
        return birthMonth == currentMonth && birthDay == currentDay
    }

    fun calculateAge(currentYear: Int = 2026): Int {
        return if (birthYear > 1900) currentYear - birthYear else 0
    }
}

enum class DeceasedCategory(val label: String) {
    TODOS("Todos"),
    RECENTES_2025_2026("Pascua Reciente (2022-2026)"),
    COLOMBIA_MEMORIA("Misión Colombia & LatAm"),
    FUNDADORES_1822("10 Padres Fundadores (1822)"),
    SUPERIORES_GENERALES("Superiores & Obispos"),
    NECROLOGIA_GENERAL("Necrología Histórica (1822-2026)")
}

data class DeceasedBrother(
    val id: String,
    val name: String,
    val age: Int? = null,
    val birthAndDeathYears: String, // e.g. "1935 - 2025" or "1757 - 1838"
    val datePassed: String, // e.g. "8 de agosto de 2025"
    val category: DeceasedCategory,
    val titleOrRole: String,
    val memorialQuote: String,
    val prayerIntention: String = "Concédeles Señor el descanso eterno y brille para ellos la luz perpetua.",
    val monthPassed: Int = 0, // 1 = Enero .. 12 = Diciembre
    val dayPassed: Int = 0    // 1..31
)

data class BasilianCouncilMember(
    val id: String,
    val roleTitle: String,
    val memberName: String,
    val location: String,
    val email: String,
    val phone: String = "",
    val term: String = "2026 - 2030",
    val description: String = ""
)

data class BasilianHouseInfo(
    val id: String,
    val name: String,
    val country: String,
    val city: String,
    val address: String,
    val phone: String = "",
    val email: String = "",
    val superiorOrRector: String = "",
    val category: String = "Comunidad"
)
