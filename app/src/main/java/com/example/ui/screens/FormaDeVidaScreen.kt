package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.FvbArticle
import com.example.data.repository.FormaDeVidaData
import com.example.ui.components.BasilianCrestView
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

enum class FormaDeVidaSubTab {
    CONSTITUCION_FVB,
    COSTUMBRES_2022,
    PREFACIOS_HISTORICOS,
    INDICE_TEMATICO
}

fun getLocalizedFvbChapterName(chapter: String, lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> when {
        chapter.contains("Capítulo 1", ignoreCase = true) || chapter.contains("Capítulo I", ignoreCase = true) -> "Chapter 1: The Community of Saint Basil"
        chapter.contains("Capítulo 2", ignoreCase = true) || chapter.contains("Capítulo II", ignoreCase = true) -> "Chapter 2: Consecration & Vows"
        chapter.contains("Capítulo 3", ignoreCase = true) || chapter.contains("Capítulo III", ignoreCase = true) -> "Chapter 3: Apostolic Life & Education"
        chapter.contains("Capítulo 4", ignoreCase = true) || chapter.contains("Capítulo IV", ignoreCase = true) -> "Chapter 4: Government & Administration"
        chapter.contains("Capítulo 5", ignoreCase = true) || chapter.contains("Capítulo V", ignoreCase = true) -> "Chapter 5: Formation & Incorporation"
        chapter.contains("Capítulo 6", ignoreCase = true) || chapter.contains("Capítulo VI", ignoreCase = true) -> "Chapter 6: Fraternal Communion & Goods"
        else -> chapter.replace("Capítulo", "Chapter")
    }
    AppLanguage.FR -> when {
        chapter.contains("Capítulo 1", ignoreCase = true) || chapter.contains("Capítulo I", ignoreCase = true) -> "Chapitre 1: La Communauté de Saint-Basile"
        chapter.contains("Capítulo 2", ignoreCase = true) || chapter.contains("Capítulo II", ignoreCase = true) -> "Chapitre 2: Consécration et Vœux"
        chapter.contains("Capítulo 3", ignoreCase = true) || chapter.contains("Capítulo III", ignoreCase = true) -> "Chapitre 3: Vie Apostolique et Éducation"
        chapter.contains("Capítulo 4", ignoreCase = true) || chapter.contains("Capítulo IV", ignoreCase = true) -> "Chapitre 4: Gouvernement et Administration"
        chapter.contains("Capítulo 5", ignoreCase = true) || chapter.contains("Capítulo V", ignoreCase = true) -> "Chapitre 5: Formation et Incorporation"
        chapter.contains("Capítulo 6", ignoreCase = true) || chapter.contains("Capítulo VI", ignoreCase = true) -> "Chapitre 6: Communion Fraternelle et Biens"
        else -> chapter.replace("Capítulo", "Chapitre")
    }
    AppLanguage.ES -> chapter
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormaDeVidaScreen(
    viewModel: BasilianViewModel,
    appLanguage: AppLanguage = AppLanguage.ES,
    initialSubTab: FormaDeVidaSubTab = FormaDeVidaSubTab.CONSTITUCION_FVB,
    modifier: Modifier = Modifier
) {
    val strings = remember(appLanguage) { AppStrings.get(appLanguage) }
    var selectedSubTab by remember(initialSubTab) { mutableStateOf(initialSubTab) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedChapterFilter by remember { mutableStateOf<String?>(null) }
    var selectedArticleDetail by remember { mutableStateOf<FvbArticle?>(null) }
    val isTtsSpeaking by viewModel.isTtsSpeaking.collectAsState()

    val doc = remember(appLanguage) {
        com.example.data.repository.BasilianDocumentsLocalizer.localizeFormaDeVidaDocument(FormaDeVidaData.document, appLanguage)
    }
    val chapters = remember(doc) { doc.articles.map { it.chapter }.distinct() }
    val articlePrefix = when (appLanguage) {
        AppLanguage.EN -> "Article"
        AppLanguage.FR -> "Article"
        AppLanguage.ES -> "Artículo"
    }

    val tabLabels = remember(appLanguage) {
        when (appLanguage) {
            AppLanguage.EN -> mapOf(
                FormaDeVidaSubTab.CONSTITUCION_FVB to "Way of Life (153 Arts.)",
                FormaDeVidaSubTab.COSTUMBRES_2022 to "Customs 2022",
                FormaDeVidaSubTab.PREFACIOS_HISTORICOS to "Prefaces & Decree",
                FormaDeVidaSubTab.INDICE_TEMATICO to "Analytical Index"
            )
            AppLanguage.FR -> mapOf(
                FormaDeVidaSubTab.CONSTITUCION_FVB to "Forme de Vie (153 Arts.)",
                FormaDeVidaSubTab.COSTUMBRES_2022 to "Coutumes 2022",
                FormaDeVidaSubTab.PREFACIOS_HISTORICOS to "Préfaces et Décret",
                FormaDeVidaSubTab.INDICE_TEMATICO to "Index Analytique"
            )
            AppLanguage.ES -> mapOf(
                FormaDeVidaSubTab.CONSTITUCION_FVB to "Forma de Vida (153 Arts.)",
                FormaDeVidaSubTab.COSTUMBRES_2022 to "Costumbres 2022",
                FormaDeVidaSubTab.PREFACIOS_HISTORICOS to "Prefacios & Decreto",
                FormaDeVidaSubTab.INDICE_TEMATICO to "Índice Analítico"
            )
        }
    }

    val searchPlaceholder = when (appLanguage) {
        AppLanguage.EN -> "Search article e.g. 34, poverty, prayer..."
        AppLanguage.FR -> "Rechercher un article ex. 34, pauvreté, prière..."
        AppLanguage.ES -> "Buscar artículo ej. 34, pobreza, oración..."
    }
    val allArtsChipLabel = when (appLanguage) {
        AppLanguage.EN -> "All 153 Articles"
        AppLanguage.FR -> "Tous les 153 Articles"
        AppLanguage.ES -> "Todos los 153 Arts."
    }
    val constDocBadge = when (appLanguage) {
        AppLanguage.EN -> "BASIC CONSTITUTIONAL DOCUMENT"
        AppLanguage.FR -> "DOCUMENT CONSTITUTIONNEL FONDAMENTAL"
        AppLanguage.ES -> "DOCUMENTO BÁSICO CONSTITUCIONAL"
    }
    val holySeeApproval = when (appLanguage) {
        AppLanguage.EN -> "Approved by the Holy See • Rome, May 12, 1983"
        AppLanguage.FR -> "Approuvé par le Saint-Siège • Rome, 12 mai 1983"
        AppLanguage.ES -> "Aprobado por la Santa Sede • Roma, 12 de mayo de 1983"
    }
    val listenBtn = when (appLanguage) {
        AppLanguage.EN -> "Listen"
        AppLanguage.FR -> "Écouter"
        AppLanguage.ES -> "Escuchar"
    }
    val closeBtn = strings.common.close

    val filteredArticles = remember(searchQuery, selectedChapterFilter, doc) {
        doc.articles.filter { article ->
            val matchesChapter = selectedChapterFilter == null || article.chapter == selectedChapterFilter
            val matchesSearch = searchQuery.isBlank() ||
                    article.number.toString() == searchQuery.trim() ||
                    article.text.contains(searchQuery, ignoreCase = true) ||
                    article.chapter.contains(searchQuery, ignoreCase = true) ||
                    article.biblicalFootnotes.contains(searchQuery, ignoreCase = true)
            matchesChapter && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BasilianIvory)
    ) {
        // Cabecera compacta con selector de sub-pestañas
        Surface(
            color = ShieldGreenDark,
            tonalElevation = 3.dp
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedSubTab.ordinal,
                containerColor = Color.Transparent,
                contentColor = Color.White,
                edgePadding = 8.dp,
                indicator = { tabPositions ->
                    if (selectedSubTab.ordinal < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab.ordinal]),
                            color = ShieldGoldLight,
                            height = 3.dp
                        )
                    }
                },
                divider = {}
            ) {
                FormaDeVidaSubTab.values().forEach { tab ->
                    Tab(
                        selected = selectedSubTab == tab,
                        onClick = { selectedSubTab = tab },
                        text = {
                            Text(
                                text = tabLabels[tab] ?: "",
                                fontSize = 11.5.sp,
                                fontWeight = if (selectedSubTab == tab) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedSubTab == tab) ShieldGoldLight else Color.White.copy(alpha = 0.85f)
                            )
                        }
                    )
                }
            }
        }

        when (selectedSubTab) {
            FormaDeVidaSubTab.CONSTITUCION_FVB -> {
                // Todo dentro del LazyColumn para adaptarse a cualquier tamaño de celular sin quitar espacio vertical
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(14.dp),
                            tonalElevation = 2.dp,
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("fvb_search_input"),
                                    placeholder = { Text(searchPlaceholder, fontSize = 12.5.sp) },
                                    leadingIcon = {
                                        Icon(Icons.Default.Search, contentDescription = null, tint = ShieldGreen)
                                    },
                                    trailingIcon = {
                                        if (searchQuery.isNotEmpty()) {
                                            IconButton(onClick = { searchQuery = "" }) {
                                                Icon(Icons.Default.Close, contentDescription = "Limpiar")
                                            }
                                        }
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ShieldGreen,
                                        unfocusedBorderColor = Color.LightGray
                                    )
                                )

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    item {
                                        FilterChip(
                                            selected = selectedChapterFilter == null,
                                            onClick = { selectedChapterFilter = null },
                                            label = { Text(allArtsChipLabel, fontSize = 11.5.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = ShieldGreenDark,
                                                selectedLabelColor = Color.White
                                            )
                                        )
                                    }
                                    items(chapters) { chap ->
                                        FilterChip(
                                            selected = selectedChapterFilter == chap,
                                            onClick = {
                                                selectedChapterFilter = if (selectedChapterFilter == chap) null else chap
                                            },
                                            label = { Text(getLocalizedFvbChapterName(chap, appLanguage), fontSize = 11.5.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = ShieldGreenDark,
                                                selectedLabelColor = Color.White
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                        item {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ShieldGreen.copy(alpha = 0.08f),
                                border = BorderStroke(1.dp, ShieldGreen.copy(alpha = 0.3f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    BasilianCrestView(size = 36.dp, showMottoText = false)
                                    Column {
                                        Text(
                                            text = constDocBadge,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = ShieldRed,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = holySeeApproval,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = ShieldGreenDark,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }

                        items(filteredArticles) { article ->
                            FvbArticleCard(
                                article = article,
                                appLanguage = appLanguage,
                                onReadAloud = {
                                    viewModel.speakText("$articlePrefix ${article.number}. ${article.chapter}. ${article.text}")
                                },
                                onOpenDetail = { selectedArticleDetail = article }
                            )
                        }
                    }
            }

            FormaDeVidaSubTab.COSTUMBRES_2022 -> {
                // Pantalla de Costumbres Basilianas 2022
                CostumbresBasilianasScreen(viewModel = viewModel, appLanguage = appLanguage)
            }

            FormaDeVidaSubTab.PREFACIOS_HISTORICOS -> {
                // Prefacios y Decretos
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 14.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Decreto de Roma 1983
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, ShieldGold),
                            elevation = CardDefaults.cardElevation(3.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = ShieldGold.copy(alpha = 0.15f)
                                ) {
                                    val decreeTitle = when (appLanguage) {
                                        AppLanguage.EN -> "DECREE OF APPROVAL BY THE HOLY SEE (1983)"
                                        AppLanguage.FR -> "DÉCRET D'APPROBATION DU SAINT-SIÈGE (1983)"
                                        AppLanguage.ES -> "DECRETO DE APROBACIÓN DE LA SANTA SEDE (1983)"
                                    }
                                    Text(
                                        text = decreeTitle,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ShieldRed,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                doc.decree.forEach { paragraph ->
                                    Text(
                                        text = paragraph,
                                        style = MaterialTheme.typography.bodyMedium,
                                        lineHeight = 22.sp,
                                        color = Color(0xFF2C3E50)
                                    )
                                }
                            }
                        }
                    }

                    // Prefacios sucesivos (2013, 2002, 2000, 1983)
                    items(doc.prefaces) { pref ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp),
                            border = BorderStroke(1.dp, ShieldGreen.copy(alpha = 0.25f))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = pref.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ShieldGreenDark,
                                    fontFamily = FontFamily.Serif
                                )
                                Text(
                                    text = pref.author,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = ShieldRed,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = pref.dateOrContext,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                                pref.content.forEach { p ->
                                    Text(
                                        text = p,
                                        style = MaterialTheme.typography.bodyMedium,
                                        lineHeight = 22.sp,
                                        color = Color(0xFF334155)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                        }
                    }

                    // Abreviaturas
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val abbrTitle = when (appLanguage) {
                                    AppLanguage.EN -> "Ecclesial Document Abbreviations"
                                    AppLanguage.FR -> "Abréviations des Documents Ecclésiaux"
                                    AppLanguage.ES -> "Abreviaturas de Documentos Eclesiales"
                                }
                                Text(
                                    text = abbrTitle,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ShieldGreenDark
                                )
                                doc.abbreviations.forEach { (abbr, meaning) ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "$abbr:",
                                            fontWeight = FontWeight.Bold,
                                            color = ShieldRed,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = meaning,
                                            fontSize = 13.sp,
                                            color = Color(0xFF475569)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            FormaDeVidaSubTab.INDICE_TEMATICO -> {
                // Índice Analítico
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 14.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        val indexTitle = when (appLanguage) {
                            AppLanguage.EN -> "Thematic Index of the Basilian Way of Life"
                            AppLanguage.FR -> "Index Thématique de la Forme de Vie Basilienne"
                            AppLanguage.ES -> "Índice Temático de la Forma de Vida Basiliana"
                        }
                        Text(
                            text = indexTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ShieldGreenDark,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    items(doc.index) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(1.dp),
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.topic,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1E293B),
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = ShieldGreen.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "Arts: ${item.articles}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = ShieldGreenDark,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de Artículo Completo
    selectedArticleDetail?.let { article ->
        AlertDialog(
            onDismissRequest = { selectedArticleDetail = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ShieldGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${article.number}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = getLocalizedFvbChapterName(article.chapter, appLanguage),
                        style = MaterialTheme.typography.titleMedium,
                        color = ShieldGreenDark,
                        fontFamily = FontFamily.Serif
                    )
                }
            },
            text = {
                val notesPrefix = when (appLanguage) {
                    AppLanguage.EN -> "Notes and references:"
                    AppLanguage.FR -> "Notes et références :"
                    AppLanguage.ES -> "Notas y referencias:"
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = article.text,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp,
                        color = Color(0xFF1E293B)
                    )
                    if (article.biblicalFootnotes.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ShieldGold.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "$notesPrefix ${article.biblicalFootnotes}",
                                style = MaterialTheme.typography.bodySmall,
                                color = ShieldGreenDark,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.speakText("$articlePrefix ${article.number}. ${article.chapter}. ${article.text}")
                }) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(listenBtn)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedArticleDetail = null }) {
                    Text(closeBtn)
                }
            }
        )
    }
}

@Composable
fun FvbArticleCard(
    article: FvbArticle,
    appLanguage: AppLanguage = AppLanguage.ES,
    onReadAloud: () -> Unit,
    onOpenDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    val readDesc = when (appLanguage) {
        AppLanguage.EN -> "Listen to article"
        AppLanguage.FR -> "Écouter l'article"
        AppLanguage.ES -> "Leer artículo"
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenDetail)
            .testTag("fvb_article_${article.number}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ShieldGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${article.number}",
                            fontWeight = FontWeight.Bold,
                            color = ShieldGreenDark,
                            fontSize = 13.sp
                        )
                    }

                    Text(
                        text = getLocalizedFvbChapterName(article.chapter, appLanguage),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = ShieldRed
                    )
                }

                IconButton(
                    onClick = onReadAloud,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = readDesc,
                        tint = ShieldGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = article.text,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp,
                color = Color(0xFF2C3E50),
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )

            if (article.biblicalFootnotes.isNotBlank()) {
                Text(
                    text = "Ref: ${article.biblicalFootnotes}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}
