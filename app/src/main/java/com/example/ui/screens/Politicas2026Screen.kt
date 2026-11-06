package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.repository.PoliticaArticle
import com.example.data.repository.PoliticaSection
import com.example.data.repository.Politicas2026Data
import com.example.ui.components.BasilianCrestView
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

@Composable
fun Politicas2026Screen(
    viewModel: BasilianViewModel,
    appLanguage: AppLanguage = AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val strings = remember(appLanguage) { AppStrings.get(appLanguage) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedSectionIndex by remember { mutableIntStateOf(0) }
    val sections = remember(appLanguage) {
        com.example.data.repository.BasilianDocumentsLocalizer.localizePoliticasSections(Politicas2026Data.sections, appLanguage)
    }
    val currentSection = sections.getOrElse(selectedSectionIndex) { sections.first() }

    val docBadge = when (appLanguage) {
        AppLanguage.EN -> "OFFICIAL POLICY 2026 • PRAESIDIUM"
        AppLanguage.FR -> "POLITIQUE OFFICIELLE 2026 • PRAESIDIUM"
        AppLanguage.ES -> "NORMATIVA OFICIAL 2026 • PRAESIDIUM"
    }
    val docTitle = when (appLanguage) {
        AppLanguage.EN -> "Policies and Procedures"
        AppLanguage.FR -> "Politiques et Procédures"
        AppLanguage.ES -> "Políticas y Procedimientos"
    }
    val docSubtitle = when (appLanguage) {
        AppLanguage.EN -> "On Misconduct and Protection of Minors"
        AppLanguage.FR -> "Sur les Inconduites et la Protection des Mineurs"
        AppLanguage.ES -> "Sobre la Conducta Inapropiada y Protección de Menores"
    }
    val searchPlaceholder = when (appLanguage) {
        AppLanguage.EN -> "Search in policies (e.g. grooming, boundaries, victim)..."
        AppLanguage.FR -> "Rechercher dans les politiques (ex. sollicitation, limites, victime)..."
        AppLanguage.ES -> "Buscar en políticas (ej. grooming, límites, víctima)..."
    }

    val filteredArticles = remember(searchQuery, currentSection) {
        if (searchQuery.isBlank()) {
            currentSection.articles
        } else {
            val q = searchQuery.trim().lowercase()
            sections.flatMap { it.articles }.filter {
                it.title.lowercase().contains(q) ||
                it.content.lowercase().contains(q) ||
                it.number.lowercase().contains(q)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BasilianIvory)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Cabecera Oficial del Documento de Políticas 2026
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.5.dp, ShieldRed.copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    BasilianCrestView(size = 64.dp, showMottoText = false)
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ShieldRed.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = docBadge,
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldRed,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                letterSpacing = 0.8.sp
                            )
                        }
                        Text(
                            text = docTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ShieldGreenDark,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = docSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )
                        Text(
                            text = Politicas2026Data.curiaAddress,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }

        // Buscador de Artículos de la Política
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(searchPlaceholder) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = ShieldRed)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_politicas_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ShieldRed,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )
        }

        // Selector de Secciones
        if (searchQuery.isBlank()) {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(sections.indices.toList()) { index ->
                        val sec = sections[index]
                        val isSelected = index == selectedSectionIndex
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedSectionIndex = index },
                            label = {
                                Text(
                                    text = sec.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ShieldRed,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = ShieldGreenDark
                            ),
                            border = BorderStroke(1.dp, if (isSelected) ShieldRed else Color.LightGray.copy(alpha = 0.5f))
                        )
                    }
                }
            }
        }

        // Artículos de la Sección
        items(filteredArticles) { article ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ShieldGreenDark.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "Art. ${article.number}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = ShieldGreenDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = ShieldGreenDark,
                        fontFamily = FontFamily.Serif
                    )

                    Text(
                        text = article.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF1E293B),
                        lineHeight = 22.sp
                    )

                    if (article.highlights.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ShieldGold.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            article.highlights.forEach { h ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = ShieldRed,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = h,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ShieldGreenDark,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
