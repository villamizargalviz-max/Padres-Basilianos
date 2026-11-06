package com.example.data.model

data class FvbArticle(
    val number: Int,
    val chapter: String,
    val text: String,
    val biblicalFootnotes: String = ""
)

data class FvbPreface(
    val year: String,
    val title: String,
    val author: String,
    val dateOrContext: String,
    val content: List<String>
)

data class FvbIndexItem(
    val topic: String,
    val articles: String
)

data class FormaDeVidaDocument(
    val title: String,
    val subtitle: String,
    val institution: String,
    val cityAndYears: String,
    val prefaces: List<FvbPreface>,
    val decree: List<String>,
    val abbreviations: List<Pair<String, String>>,
    val articles: List<FvbArticle>,
    val index: List<FvbIndexItem>
)
