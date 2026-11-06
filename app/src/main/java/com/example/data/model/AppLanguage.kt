package com.example.data.model

import java.util.Locale

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val flag: String,
    val locale: Locale,
    val motto: String,
    val historicalContext: String
) {
    ES(
        code = "es",
        displayName = "Español",
        nativeName = "Español",
        flag = "🇪🇸",
        locale = Locale("es", "CO"),
        motto = "«Bonitatem et disciplinam et scientiam doce me» • Enséñame bondad, disciplina y ciencia",
        historicalContext = "Presencia misionera en Colombia (Bogotá, Medellín, Cali) y México"
    ),
    EN(
        code = "en",
        displayName = "Inglés / English",
        nativeName = "English",
        flag = "🇺🇸",
        locale = Locale.US,
        motto = "«Bonitatem et disciplinam et scientiam doce me» • Teach me goodness, discipline, and knowledge",
        historicalContext = "Instituciones universitarias y colegios en Canadá (Toronto) y EE. UU. (Rochester, Houston)"
    ),
    FR(
        code = "fr",
        displayName = "Francés / Français",
        nativeName = "Français",
        flag = "🇫🇷",
        locale = Locale.FRANCE,
        motto = "«Bonitatem et disciplinam et scientiam doce me» • Enseigne-moi la bonté, la discipline et la science",
        historicalContext = "Cuna de fundación de la Congregación de San Basilio en Annonay, Francia (1822)"
    );

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return values().firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ES
        }
    }
}
