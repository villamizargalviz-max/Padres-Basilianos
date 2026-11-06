package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppLanguage
import com.example.data.model.Book1922Chapter
import com.example.data.model.GovernmentEvolutionEra
import com.example.data.model.SuperiorGeneralHistoricalRecord
import com.example.data.repository.Book1922Data
import com.example.ui.components.BasilianCrestView
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

private enum class BookSubTab {
    CAPITULOS,
    SUPERIORES,
    ORGANIGRAMA
}

@Composable
fun Book1922Screen(
    viewModel: BasilianViewModel,
    appLanguage: AppLanguage = AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val strings = remember(appLanguage) { AppStrings.get(appLanguage) }
    var currentSubTab by remember { mutableStateOf(BookSubTab.CAPITULOS) }
    val isSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val selectedChapter by viewModel.selectedBookChapter.collectAsState()
    val searchQuery by viewModel.bookSearchQuery.collectAsState()

    val localizedChapters = remember(appLanguage) {
        com.example.data.repository.BasilianDocumentsLocalizer.localizeBook1922Chapters(Book1922Data.chapters, appLanguage)
    }
    val localizedSuperiors = remember(appLanguage) {
        com.example.data.repository.BasilianDocumentsLocalizer.localizeBook1922Superiors(Book1922Data.superiorsGeneralSince1822, appLanguage)
    }
    val localizedGovEras = remember(appLanguage) {
        com.example.data.repository.BasilianDocumentsLocalizer.localizeBook1922GovernmentEras(Book1922Data.governmentEvolution, appLanguage)
    }

    val filteredChapters = remember(searchQuery, localizedChapters) {
        if (searchQuery.isBlank()) localizedChapters
        else {
            localizedChapters.filter { ch ->
                ch.chapterTitle.contains(searchQuery, ignoreCase = true) ||
                ch.partTitle.contains(searchQuery, ignoreCase = true) ||
                ch.summary.contains(searchQuery, ignoreCase = true) ||
                ch.sections.any { s -> s.paragraphs.any { p -> p.contains(searchQuery, ignoreCase = true) } }
            }
        }
    }

    val filteredSuperiors = remember(searchQuery, localizedSuperiors) {
        if (searchQuery.isBlank()) localizedSuperiors
        else {
            localizedSuperiors.filter { sup ->
                sup.name.contains(searchQuery, ignoreCase = true) ||
                sup.period.contains(searchQuery, ignoreCase = true) ||
                sup.jurisdiction.contains(searchQuery, ignoreCase = true) ||
                sup.keyMilestones.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. HERO HEADER: Documento Histórico de Medellín
            item {
                BookHeroHeader(appLanguage = appLanguage)
            }

            // 2. Buscador en el Libro
            item {
                val placeholderText = when (appLanguage) {
                    AppLanguage.EN -> "Search in the 1922 centenary work (e.g., Forster, Annonay, Vow)..."
                    AppLanguage.FR -> "Rechercher dans l'œuvre de 1922 (ex. Forster, Annonay, Vœu)..."
                    AppLanguage.ES -> "Buscar en la obra de 1922 (ej. Forster, Annonay, Voto, Flahiff)..."
                }
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setBookSearchQuery(it) },
                    placeholder = { Text(placeholderText, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar", tint = BasilianBlue)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setBookSearchQuery("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpiar")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BasilianGold,
                        unfocusedBorderColor = ParchmentBorder,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("book_search_field")
                )
            }

            // 3. Subtabs: Capítulos | Superiores Generales | Organigrama
            item {
                val chapLabel = when (appLanguage) {
                    AppLanguage.EN -> "Chapters"
                    AppLanguage.FR -> "Chapitres"
                    AppLanguage.ES -> "Capítulos"
                }
                val supLabel = when (appLanguage) {
                    AppLanguage.EN -> "Superiors"
                    AppLanguage.FR -> "Supérieurs"
                    AppLanguage.ES -> "Superiores"
                }
                val govLabel = when (appLanguage) {
                    AppLanguage.EN -> "Government"
                    AppLanguage.FR -> "Gouvernement"
                    AppLanguage.ES -> "Gobierno"
                }
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        TabButton(
                            title = chapLabel,
                            icon = Icons.Default.MenuBook,
                            selected = currentSubTab == BookSubTab.CAPITULOS,
                            onClick = { currentSubTab = BookSubTab.CAPITULOS },
                            modifier = Modifier.weight(1f)
                        )
                        TabButton(
                            title = supLabel,
                            icon = Icons.Default.Badge,
                            selected = currentSubTab == BookSubTab.SUPERIORES,
                            onClick = { currentSubTab = BookSubTab.SUPERIORES },
                            modifier = Modifier.weight(1f)
                        )
                        TabButton(
                            title = govLabel,
                            icon = Icons.Default.AccountTree,
                            selected = currentSubTab == BookSubTab.ORGANIGRAMA,
                            onClick = { currentSubTab = BookSubTab.ORGANIGRAMA },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // 4. Content based on active subtab
            when (currentSubTab) {
                BookSubTab.CAPITULOS -> {
                    items(filteredChapters, key = { it.id }) { chapter ->
                        BookChapterCard(
                            chapter = chapter,
                            appLanguage = appLanguage,
                            onClick = { viewModel.selectBookChapter(chapter) }
                        )
                    }
                }

                BookSubTab.SUPERIORES -> {
                    item {
                        val supTitle = when (appLanguage) {
                            AppLanguage.EN -> "Superiors General and Provincials"
                            AppLanguage.FR -> "Supérieurs Généraux et Provinciaux"
                            AppLanguage.ES -> "Superiores Generales y Provinciales"
                        }
                        val supDesc = when (appLanguage) {
                            AppLanguage.EN -> "Historical record from the 1822 foundation to the present (2026), including Viviers (France) and Toronto (Americas) branches during the separation (1922-1955)."
                            AppLanguage.FR -> "Registre historique depuis la fondation en 1822 jusqu'à aujourd'hui (2026), incluant les branches de Viviers (France) et Toronto (Amériques) pendant la séparation (1922-1955)."
                            AppLanguage.ES -> "Registro histórico desde la fundación en 1822 hasta la actualidad (2026), incluyendo las ramas de Viviers (Francia) y Toronto (América) durante la separación (1922-1955)."
                        }
                        Surface(
                            color = BasilianGoldLight.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, BasilianGold.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = supTitle,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BasilianNavy
                                )
                                Text(
                                    text = supDesc,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    items(filteredSuperiors, key = { "${it.name}_${it.period}" }) { superior ->
                        SuperiorGeneralCard(superior)
                    }
                }

                BookSubTab.ORGANIGRAMA -> {
                    items(localizedGovEras, key = { it.era }) { era ->
                        GovernmentEraCard(era)
                    }
                }
            }
        }
    }

    // Detail Dialog for Full Chapter Reading
    selectedChapter?.let { rawChapter ->
        val chapter = remember(rawChapter, appLanguage) {
            localizedChapters.find { it.id == rawChapter.id } ?: rawChapter
        }
        ChapterReaderDialog(
            chapter = chapter,
            appLanguage = appLanguage,
            isSpeaking = isSpeaking,
            onListen = {
                val fullNarrative = buildString {
                    append(chapter.partTitle).append(". ")
                    append(chapter.chapterTitle).append(". ")
                    chapter.sections.forEach { sec ->
                        append(sec.title).append(". ")
                        sec.paragraphs.forEach { p -> append(p).append(" ") }
                    }
                }
                viewModel.speakText(fullNarrative)
            },
            onClose = { viewModel.selectBookChapter(null) }
        )
    }
}

@Composable
private fun BookHeroHeader(appLanguage: AppLanguage = AppLanguage.ES) {
    val badgeText = when (appLanguage) {
        AppLanguage.EN -> "CONGREGATIONAL HISTORICAL DOCUMENT"
        AppLanguage.FR -> "DOCUMENT HISTORIQUE CONGRÉGATIONNEL"
        AppLanguage.ES -> "DOCUMENTO HISTÓRICO CONGREGACIONAL"
    }
    val archiveLocation = when (appLanguage) {
        AppLanguage.EN -> "Custody & Archive: Saint Basil Parish • Medellín, Antioquia, Colombia"
        AppLanguage.FR -> "Garde et Archives : Paroisse Saint-Basile • Medellín, Antioquia, Colombie"
        AppLanguage.ES -> "Custodia y Archivo: Parroquia San Basilio • Medellín, Antioquia, Colombia"
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("book_hero_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BasilianNavy),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(BasilianNavy, Color(0xFF142746), Color(0xFF0D1B30))
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Crest in Center
                BasilianCrestView(
                    size = 110.dp,
                    showMottoText = false
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BasilianGold.copy(alpha = 0.20f),
                    border = BorderStroke(1.dp, BasilianGold)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = BasilianGoldLight,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = when (appLanguage) {
                        AppLanguage.EN -> "1922: Before and After"
                        AppLanguage.FR -> "1922 : Avant et Après"
                        AppLanguage.ES -> "1922: Antes y Después"
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = when (appLanguage) {
                        AppLanguage.EN -> "By Kevin J. Kirley, CSB (November 21, 1992)"
                        AppLanguage.FR -> "Par Kevin J. Kirley, CSB (21 novembre 1992)"
                        AppLanguage.ES -> "Por Kevin J. Kirley, CSB (21 de Noviembre de 1992)"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = BasilianGoldLight,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = BasilianGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = archiveLocation,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) BasilianBlue else Color.Transparent,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun BookChapterCard(
    chapter: Book1922Chapter,
    appLanguage: AppLanguage = AppLanguage.ES,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val localizedChapterTitle = when (appLanguage) {
        AppLanguage.EN -> when (chapter.id) {
            "intro" -> "A Look at the Turning Point of Our History"
            "parte1" -> "From the French Revolution to Missionary Expansion"
            "parte2" -> "Separation, Simple Vows and Renewal"
            "parte3" -> "Expansion, Vatican II and Contemporary Mission"
            else -> chapter.chapterTitle
        }
        AppLanguage.FR -> when (chapter.id) {
            "intro" -> "Regard sur le Point Critique de Notre Histoire"
            "parte1" -> "De la Révolution Française à l'Expansion Missionnaire"
            "parte2" -> "Séparation, Vœux Simples et Renouveau"
            "parte3" -> "Expansion, Vatican II et Mission Contemporaine"
            else -> chapter.chapterTitle
        }
        AppLanguage.ES -> chapter.chapterTitle
    }

    val localizedSubtitle = when (appLanguage) {
        AppLanguage.EN -> when (chapter.id) {
            "intro" -> "For the 1993 General Chapter"
            "parte1" -> "Saint-Symphorien, Annonay, Toronto and the Trial of Exile"
            "parte2" -> "The 1922 Assembly, Cardinal Merry del Val and Separation"
            "parte3" -> "The Era of Cardinal Flahiff and Worldwide Growth"
            else -> chapter.subtitle
        }
        AppLanguage.FR -> when (chapter.id) {
            "intro" -> "Pour le Chapitre Général de 1993"
            "parte1" -> "Saint-Symphorien, Annonay, Toronto et l'Épreuve de l'Exil"
            "parte2" -> "L'Assemblée de 1922, le Cardinal Merry del Val et la Séparation"
            "parte3" -> "L'Époque du Cardinal Flahiff et la Croissance Mondiale"
            else -> chapter.subtitle
        }
        AppLanguage.ES -> chapter.subtitle
    }

    val sectionsText = when (appLanguage) {
        AppLanguage.EN -> "${chapter.sections.size} reading sections"
        AppLanguage.FR -> "${chapter.sections.size} sections de lecture"
        AppLanguage.ES -> "${chapter.sections.size} secciones de lectura"
    }

    val readChapterText = when (appLanguage) {
        AppLanguage.EN -> "Read chapter"
        AppLanguage.FR -> "Lire le chapitre"
        AppLanguage.ES -> "Leer capítulo"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag("book_chapter_card_${chapter.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, ParchmentBorder),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BasilianNavy.copy(alpha = 0.08f)
                ) {
                    Text(
                        text = chapter.partNumber.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = BasilianNavy,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = chapter.pageRange,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = localizedChapterTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = localizedSubtitle,
                style = MaterialTheme.typography.bodySmall,
                color = BasilianCrimson,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = chapter.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3
            )

            HorizontalDivider(color = ParchmentBorder.copy(alpha = 0.5f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sectionsText,
                    style = MaterialTheme.typography.labelSmall,
                    color = BasilianBlue
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = readChapterText,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = BasilianBlue
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = BasilianBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SuperiorGeneralCard(
    superior: SuperiorGeneralHistoricalRecord,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, ParchmentBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = BasilianNavy,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = BasilianGold,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = superior.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = BasilianGoldLight.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = superior.period,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BasilianGoldDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = "${superior.years} • ${superior.jurisdiction}",
                    style = MaterialTheme.typography.bodySmall,
                    color = BasilianCrimson,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = superior.keyMilestones,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun GovernmentEraCard(
    era: GovernmentEvolutionEra,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, ParchmentBorder)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = BasilianBlue.copy(alpha = 0.12f)
            ) {
                Text(
                    text = era.era,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = BasilianBlue,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Text(
                text = era.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = era.structureDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = BasilianNavy,
                fontWeight = FontWeight.SemiBold
            )

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    era.details.forEach { detail ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "•", color = BasilianGoldDark, fontWeight = FontWeight.Bold)
                            Text(
                                text = detail,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChapterReaderDialog(
    chapter: Book1922Chapter,
    appLanguage: AppLanguage = AppLanguage.ES,
    isSpeaking: Boolean,
    onListen: () -> Unit,
    onClose: () -> Unit
) {
    val localizedTitle = when (appLanguage) {
        AppLanguage.EN -> when (chapter.id) {
            "intro" -> "A Look at the Turning Point of Our History"
            "parte1" -> "From the French Revolution to Missionary Expansion"
            "parte2" -> "Separation, Simple Vows and Renewal"
            "parte3" -> "Expansion, Vatican II and Contemporary Mission"
            else -> chapter.chapterTitle
        }
        AppLanguage.FR -> when (chapter.id) {
            "intro" -> "Regard sur le Point Critique de Notre Histoire"
            "parte1" -> "De la Révolution Française à l'Expansion Missionnaire"
            "parte2" -> "Séparation, Vœux Simples et Renouveau"
            "parte3" -> "Expansion, Vatican II et Mission Contemporaine"
            else -> chapter.chapterTitle
        }
        AppLanguage.ES -> chapter.chapterTitle
    }

    val backLabel = when (appLanguage) {
        AppLanguage.EN -> "Back"
        AppLanguage.FR -> "Retour"
        AppLanguage.ES -> "Regresar"
    }

    val listenLabel = when (appLanguage) {
        AppLanguage.EN -> "Listen to chapter"
        AppLanguage.FR -> "Écouter le chapitre"
        AppLanguage.ES -> "Escuchar capítulo"
    }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top App Bar for Reader
                Surface(
                    color = BasilianNavy,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = backLabel,
                                tint = Color.White
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = chapter.partNumber,
                                style = MaterialTheme.typography.labelSmall,
                                color = BasilianGoldLight
                            )
                            Text(
                                text = localizedTitle,
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }

                        IconButton(onClick = onListen) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                                contentDescription = listenLabel,
                                tint = BasilianGoldLight
                            )
                        }
                    }
                }

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Subtitle
                    Text(
                        text = chapter.subtitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BasilianCrimson
                    )

                    // Quote Callout if available
                    if (chapter.quotes.isNotEmpty()) {
                        Surface(
                            color = BasilianGoldLight.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, BasilianGold.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = chapter.quotes.first(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontStyle = FontStyle.Italic,
                                    color = BasilianNavy,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }

                    // Key Takeaways
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Ideas Centrales del Capítulo:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BasilianBlue
                            )
                            chapter.keyTakeaways.forEach { takeaway ->
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = BasilianGoldDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = takeaway,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    // All Sections with Full Paragraphs
                    chapter.sections.forEach { section ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, ParchmentBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = section.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BasilianNavy
                                )

                                section.paragraphs.forEach { paragraph ->
                                    Text(
                                        text = paragraph,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 24.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}
