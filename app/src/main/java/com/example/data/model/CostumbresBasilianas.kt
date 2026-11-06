package com.example.data.model

data class CostumbresArticle(
    val code: String, // e.g., "A.01", "B.01", "C.02"
    val title: String,
    val content: String,
    val categoryId: String,
    val categoryTitle: String,
    val fvbReference: String = "" // e.g. "ver FVB 1-8"
)

data class CostumbresSection(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val pageNumber: Int,
    val fvbReference: String = "",
    val articles: List<CostumbresArticle>
)

data class CostumbresDocument(
    val title: String,
    val subtitle: String,
    val institution: String,
    val cityAndYear: String,
    val bicentennialNote: String,
    val preface: CostumbresPreface,
    val sections: List<CostumbresSection>
)

data class CostumbresPreface(
    val title: String,
    val approvedBy: String,
    val reviewCommittee: List<String>,
    val translators: List<String>,
    val keyAmendments2022: List<String>,
    val author: String,
    val authorTitle: String,
    val content: List<String>
)
