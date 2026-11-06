package com.example.data.model

data class DailyPrayerReminderConfig(
    val type: String, // "LAUDES", "ANGELUS", "VESPERAS"
    val title: String,
    val subtitle: String,
    val defaultHour: Int,
    val defaultMinute: Int,
    val isEnabled: Boolean = true,
    val iconEmoji: String = "🔔"
) {
    val formattedTime: String
        get() {
            val h = if (defaultHour == 0) 12 else if (defaultHour > 12) defaultHour - 12 else defaultHour
            val amPm = if (defaultHour >= 12) "PM" else "AM"
            val m = if (defaultMinute < 10) "0$defaultMinute" else "$defaultMinute"
            return "$h:$m $amPm"
        }
}

data class CustomBirthday(
    val id: String,
    val name: String,
    val day: Int,
    val month: Int,
    val relationOrRole: String = "Amigo / Cohermano",
    val personalBlessing: String = "Que el Señor derrame abundantes bendiciones de paz, salud y fidelidad en tu vida.",
    val isReminderEnabled: Boolean = true
) {
    val formattedDate: String
        get() {
            val months = listOf(
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
            )
            val monthName = months.getOrElse(month - 1) { "Mes $month" }
            return "$day de $monthName"
        }
}

data class LiturgicalSolemnityReminder(
    val id: String,
    val title: String,
    val day: Int,
    val month: Int,
    val rank: String,
    val briefDescription: String,
    val isEnabled: Boolean = true
) {
    val formattedDate: String
        get() {
            val months = listOf(
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
            )
            val monthName = months.getOrElse(month - 1) { "Mes $month" }
            return "$day de $monthName"
        }
}
