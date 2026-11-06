package com.example.data.model

data class ApuntesChapter(
    val id: String,
    val romanNumeral: String,
    val title: String,
    val subtitle: String = "",
    val years: String = "",
    val summary: String,
    val sections: List<ApuntesSection>
)

data class ApuntesSection(
    val title: String,
    val content: String,
    val quote: String = "",
    val quoteAuthor: String = ""
)

data class ApuntesColombiaBook(
    val title: String,
    val subtitle: String,
    val years: String,
    val author: String,
    val prologueAuthor: String,
    val prologueDate: String,
    val prologueText: List<String>,
    val introductionText: List<String>,
    val chapters: List<ApuntesChapter>
)
