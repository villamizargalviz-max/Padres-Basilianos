package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.CostumbresArticle
import com.example.data.model.CostumbresSection
import com.example.data.repository.CostumbresBasilianasData
import com.example.ui.components.BasilianCrestView
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

enum class CostumbresTab {
    CAPITULOS,
    PREFACIO,
    LEMA
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CostumbresBasilianasScreen(
    viewModel: BasilianViewModel,
    appLanguage: AppLanguage = AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val strings = remember(appLanguage) { AppStrings.get(appLanguage) }
    var selectedTab by remember { mutableStateOf(CostumbresTab.CAPITULOS) }
    val searchQuery by viewModel.costumbresSearchQuery.collectAsState()
    val isTtsSpeaking by viewModel.isTtsSpeaking.collectAsState()
    var selectedSectionFilter by remember { mutableStateOf<String?>(null) }
    var activeArticleDetail by remember { mutableStateOf<CostumbresArticle?>(null) }

    val tabTitles = remember(appLanguage) {
        when (appLanguage) {
            AppLanguage.EN -> mapOf(
                CostumbresTab.CAPITULOS to "Chapters & Norms",
                CostumbresTab.PREFACIO to "Preface 2022",
                CostumbresTab.LEMA to "Motto & Charism"
            )
            AppLanguage.FR -> mapOf(
                CostumbresTab.CAPITULOS to "Chapitres et Normes",
                CostumbresTab.PREFACIO to "Préface 2022",
                CostumbresTab.LEMA to "Devise et Charisme"
            )
            AppLanguage.ES -> mapOf(
                CostumbresTab.CAPITULOS to "Capítulos y Normas",
                CostumbresTab.PREFACIO to "Prefacio 2022",
                CostumbresTab.LEMA to "Lema y Carisma"
            )
        }
    }

    val searchPlaceholder = when (appLanguage) {
        AppLanguage.EN -> "Search norms e.g. habit, council, chapter..."
        AppLanguage.FR -> "Rechercher des normes ex. habit, conseil, chapitre..."
        AppLanguage.ES -> "Buscar en normas ej. hábito, consejo, capítulo..."
    }
    val allChaptersLabel = when (appLanguage) {
        AppLanguage.EN -> "All Chapters"
        AppLanguage.FR -> "Tous les Chapitres"
        AppLanguage.ES -> "Todos los Capítulos"
    }

    val doc = remember(appLanguage) {
        com.example.data.repository.BasilianDocumentsLocalizer.localizeCostumbresDocument(CostumbresBasilianasData.document, appLanguage)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ParchmentWhite)
    ) {
        // Segmented Tab Selector compacto arriba
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            containerColor = Color.White,
            contentColor = ShieldGreen,
            divider = { HorizontalDivider(color = ParchmentBorder) }
        ) {
            CostumbresTab.values().forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = {
                        Text(
                            text = tabTitles[tab] ?: "",
                            fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == tab) ShieldGreen else TextSecondaryDark,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                )
            }
        }

        // Tab Content
        when (selectedTab) {
            CostumbresTab.CAPITULOS -> {
                CapitulosContent(
                    docTitle = doc.title,
                    docNote = doc.bicentennialNote,
                    docCityYear = doc.cityAndYear,
                    sections = doc.sections,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.setCostumbresSearchQuery(it) },
                    selectedSectionFilter = selectedSectionFilter,
                    onSelectSectionFilter = { selectedSectionFilter = it },
                    onArticleClick = { activeArticleDetail = it },
                    onTtsClick = { text -> viewModel.speakText(text) },
                    isTtsSpeaking = isTtsSpeaking,
                    searchPlaceholder = searchPlaceholder,
                    allChaptersLabel = allChaptersLabel,
                    appLanguage = appLanguage
                )
            }
            CostumbresTab.PREFACIO -> {
                PrefacioContent(
                    preface = doc.preface,
                    onTtsClick = { text -> viewModel.speakText(text) },
                    isTtsSpeaking = isTtsSpeaking,
                    appLanguage = appLanguage
                )
            }
            CostumbresTab.LEMA -> {
                LemaContent(appLanguage = appLanguage)
            }
        }
    }

    // Modal Bottom Sheet for Article Reader
    activeArticleDetail?.let { article ->
        ModalBottomSheet(
            onDismissRequest = { activeArticleDetail = null },
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            ArticleDetailBottomSheet(
                article = article,
                onClose = { activeArticleDetail = null },
                onSpeak = { viewModel.speakText("${article.code}. ${article.title}. ${article.content}") },
                isSpeaking = isTtsSpeaking,
                appLanguage = appLanguage
            )
        }
    }
}

@Composable
private fun CapitulosContent(
    docTitle: String,
    docNote: String,
    docCityYear: String,
    sections: List<CostumbresSection>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedSectionFilter: String?,
    onSelectSectionFilter: (String?) -> Unit,
    onArticleClick: (CostumbresArticle) -> Unit,
    onTtsClick: (String) -> Unit,
    isTtsSpeaking: Boolean,
    searchPlaceholder: String = "Buscar en normas...",
    allChaptersLabel: String = "Todos",
    appLanguage: AppLanguage = AppLanguage.ES
) {
    val filteredArticles = remember(sections, searchQuery, selectedSectionFilter) {
        val all = sections.flatMap { it.articles }
        all.filter { art ->
            val matchesFilter = selectedSectionFilter == null || art.categoryId == selectedSectionFilter
            val matchesQuery = searchQuery.isBlank() ||
                    art.code.contains(searchQuery, ignoreCase = true) ||
                    art.title.contains(searchQuery, ignoreCase = true) ||
                    art.content.contains(searchQuery, ignoreCase = true) ||
                    art.categoryTitle.contains(searchQuery, ignoreCase = true)
            matchesFilter && matchesQuery
        }
    }

    // Todo el contenido dentro de un único LazyColumn para adaptarse a cualquier tamaño de celular
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, ShieldGreen.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasilianCrestView(
                        size = 52.dp,
                        showMottoText = false
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ShieldRed.copy(alpha = 0.12f),
                            contentColor = ShieldRedDark
                        ) {
                            Text(
                                text = docNote,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 9.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = docTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = ShieldGreenDark,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "$docCityYear • P. Kevin Storey CSB",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(searchPlaceholder, fontSize = 12.5.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = ShieldGreen) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = TextSecondaryDark)
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ShieldGreen,
                        unfocusedBorderColor = ParchmentBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedSectionFilter == null,
                            onClick = { onSelectSectionFilter(null) },
                            label = { Text(allChaptersLabel, fontSize = 11.5.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ShieldGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    items(sections) { sec ->
                        FilterChip(
                            selected = selectedSectionFilter == sec.id,
                            onClick = {
                                onSelectSectionFilter(if (selectedSectionFilter == sec.id) null else sec.id)
                            },
                            label = { Text(sec.title, fontSize = 11.5.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ShieldGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        if (filteredArticles.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "No articles found matching '$searchQuery'"
                            AppLanguage.FR -> "Aucun article trouvé avec '$searchQuery'"
                            AppLanguage.ES -> "No se encontraron artículos con '$searchQuery'"
                        },
                        color = TextSecondaryDark,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            item {
                Text(
                    text = when (appLanguage) {
                        AppLanguage.EN -> "Showing ${filteredArticles.size} articles of Basilian Customs"
                        AppLanguage.FR -> "Affichage de ${filteredArticles.size} articles des Coutumes Basiliennes"
                        AppLanguage.ES -> "Mostrando ${filteredArticles.size} artículos de las Costumbres Basilianas"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondaryDark,
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(filteredArticles, key = { it.code }) { article ->
                ArticleCard(
                    article = article,
                    onClick = { onArticleClick(article) },
                    onTts = { onTtsClick("${article.code}. ${article.title}. ${article.content}") },
                    isSpeaking = isTtsSpeaking,
                    appLanguage = appLanguage
                )
            }
        }
    }
}

@Composable
private fun ArticleCard(
    article: CostumbresArticle,
    onClick: () -> Unit,
    onTts: () -> Unit,
    isSpeaking: Boolean,
    appLanguage: AppLanguage = AppLanguage.ES
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ParchmentBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Article Code Badge in Shield Colors
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ShieldGreenContainer,
                    contentColor = ShieldGreenDark
                ) {
                    Text(
                        text = article.code,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = article.categoryTitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = ShieldRedDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (article.fvbReference.isNotEmpty()) {
                    Text(
                        text = article.fvbReference,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondaryDark,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ShieldGreenDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = article.content,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimaryDark,
                lineHeight = 20.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onClick,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "Read full article"
                            AppLanguage.FR -> "Lire en entier"
                            AppLanguage.ES -> "Leer completo"
                        },
                        color = ShieldGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = ShieldGreen,
                        modifier = Modifier.size(14.dp)
                    )
                }

                IconButton(
                    onClick = onTts,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Outlined.VolumeUp,
                        contentDescription = "Escuchar",
                        tint = ShieldRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticleDetailBottomSheet(
    article: CostumbresArticle,
    onClose: () -> Unit,
    onSpeak: () -> Unit,
    isSpeaking: Boolean,
    appLanguage: AppLanguage = AppLanguage.ES
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = ShieldGreenContainer,
                contentColor = ShieldGreenDark
            ) {
                Text(
                    text = article.code,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Row {
                IconButton(onClick = onSpeak) {
                    Icon(Icons.Default.VolumeUp, contentDescription = "Escuchar", tint = ShieldRed)
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = article.categoryTitle,
            style = MaterialTheme.typography.labelMedium,
            color = ShieldRedDark,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = article.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = ShieldGreenDark,
            fontFamily = FontFamily.Serif
        )

        if (article.fvbReference.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            val constRefLabel = when (appLanguage) {
                AppLanguage.EN -> "Constitutional reference:"
                AppLanguage.FR -> "Référence constitutionnelle :"
                AppLanguage.ES -> "Referencia constitucional:"
            }
            Text(
                text = "$constRefLabel ${article.fvbReference}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondaryDark,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = ParchmentBorder)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = article.content,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimaryDark,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun PrefacioContent(
    preface: com.example.data.model.CostumbresPreface,
    onTtsClick: (String) -> Unit,
    isTtsSpeaking: Boolean,
    appLanguage: AppLanguage = AppLanguage.ES
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, ParchmentBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = preface.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = ShieldGreenDark,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${preface.author} • ${preface.authorTitle}",
                        style = MaterialTheme.typography.labelMedium,
                        color = ShieldRedDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = preface.approvedBy,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = ParchmentBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    preface.content.forEach { paragraph ->
                        Text(
                            text = paragraph,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimaryDark,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Button(
                        onClick = {
                            onTtsClick(preface.content.joinToString("\n"))
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ShieldGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            when (appLanguage) {
                                AppLanguage.EN -> "Listen to Preface"
                                AppLanguage.FR -> "Écouter la Préface"
                                AppLanguage.ES -> "Escuchar Prefacio"
                            }
                        )
                    }
                }
            }
        }

        // 6 Enmiendas de 2022
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ShieldGreenContainer.copy(alpha = 0.5f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, ShieldGreen.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ShieldGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (appLanguage) {
                                AppLanguage.EN -> "Six Amendments Authorized in 2022"
                                AppLanguage.FR -> "Six Amendements Autorisés en 2022"
                                AppLanguage.ES -> "Seis Enmiendas Autorizadas en 2022"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ShieldGreenDark
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    preface.keyAmendments2022.forEachIndexed { idx, enmienda ->
                        Row(modifier = Modifier.padding(vertical = 3.dp)) {
                            Text(
                                text = "${idx + 1}. ",
                                fontWeight = FontWeight.Bold,
                                color = ShieldRedDark,
                                fontSize = 13.sp
                            )
                            Text(
                                text = enmienda,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimaryDark,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Comité de revisión y traductores
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, ParchmentBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "Review Committee and Translators"
                            AppLanguage.FR -> "Comité de Révision et Traducteurs"
                            AppLanguage.ES -> "Comité de Revisión y Traductores"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = ShieldGreenDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "Review Committee:"
                            AppLanguage.FR -> "Comité de Révision :"
                            AppLanguage.ES -> "Comité de Revisión:"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = ShieldRedDark
                    )
                    preface.reviewCommittee.forEach { member ->
                        Text("• $member", style = MaterialTheme.typography.bodySmall, color = TextPrimaryDark)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "Spanish Translators:"
                            AppLanguage.FR -> "Traducteurs en espagnol :"
                            AppLanguage.ES -> "Traductores al español:"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = ShieldRedDark
                    )
                    preface.translators.forEach { translator ->
                        Text("• $translator", style = MaterialTheme.typography.bodySmall, color = TextPrimaryDark)
                    }
                }
            }
        }
    }
}

@Composable
private fun LemaContent(appLanguage: AppLanguage = AppLanguage.ES) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            BasilianCrestView(size = 140.dp, showMottoText = true)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, ShieldGreen.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "Trilingual Motto of the Congregation"
                            AppLanguage.FR -> "Devise Trilingue de la Congrégation"
                            AppLanguage.ES -> "Lema Trilingüe de la Congregación"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = ShieldRedDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = CostumbresBasilianasData.closingMottoFrench,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = ShieldGreenDark,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = CostumbresBasilianasData.closingMottoSpanish,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = ShieldRed,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = CostumbresBasilianasData.closingMottoEnglish,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = ShieldGreenDark,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = ParchmentBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "«Teach me goodness, discipline, and knowledge»"
                            AppLanguage.FR -> "« Enseigne-moi la bonté, la discipline et la science »"
                            AppLanguage.ES -> "«Enséñame bondad, disciplina y conocimiento»"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ShieldGreenDark,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "Psalm 118:66 • Bonitatem et disciplinam et scientiam doce me"
                            AppLanguage.FR -> "Psaume 118:66 • Bonitatem et disciplinam et scientiam doce me"
                            AppLanguage.ES -> "Salmo 118:66 • Bonitatem et disciplinam et scientiam doce me"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
