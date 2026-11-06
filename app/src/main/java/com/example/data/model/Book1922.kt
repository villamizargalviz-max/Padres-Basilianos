package com.example.data.model

data class Book1922Chapter(
    val id: String,
    val partNumber: String,
    val partTitle: String,
    val chapterTitle: String,
    val subtitle: String,
    val pageRange: String,
    val summary: String,
    val sections: List<BookSectionContent>,
    val keyTakeaways: List<String>,
    val quotes: List<String>
)

data class BookSectionContent(
    val title: String,
    val paragraphs: List<String>,
    val historicalNotes: List<String> = emptyList()
)

data class SuperiorGeneralHistoricalRecord(
    val name: String,
    val years: String,
    val period: String,
    val jurisdiction: String,
    val keyMilestones: String
)

data class GovernmentEvolutionEra(
    val era: String,
    val title: String,
    val structureDescription: String,
    val details: List<String>
)
