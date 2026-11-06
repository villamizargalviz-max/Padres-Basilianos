package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HistoricalEvent
import com.example.data.model.HistoryCategory
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

@Composable
fun HistoryScreen(
    viewModel: BasilianViewModel,
    modifier: Modifier = Modifier
) {
    val timeline = viewModel.timelineEvents
    val selectedCategory by viewModel.selectedHistoryCategory.collectAsState()
    val isSpeaking by viewModel.isTtsSpeaking.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val filteredTimeline = remember(timeline, selectedCategory, searchQuery) {
        timeline.filter { event ->
            val matchesCategory = selectedCategory == null || event.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    event.year.contains(searchQuery, ignoreCase = true) ||
                    event.title.contains(searchQuery, ignoreCase = true) ||
                    event.location.contains(searchQuery, ignoreCase = true) ||
                    event.summary.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, BasilianGoldLight.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = BasilianCrimson,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Fechas Clave y Cronología Histórica",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = BasilianNavy
                        )
                        Text(
                            text = "Un recorrido vivo desde San Basilio Magno (379) y Annonay (1822) hasta el Bicentenario (2022) y el presente (2026).",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar año, lugar o acontecimiento...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = BasilianBlue)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_history_input")
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { viewModel.setHistoryCategory(null) },
                    label = { Text("Todas", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedCategory == HistoryCategory.FOUNDATION,
                    onClick = { viewModel.setHistoryCategory(HistoryCategory.FOUNDATION) },
                    label = { Text("1822", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedCategory == HistoryCategory.COLOMBIA_MISSION,
                    onClick = { viewModel.setHistoryCategory(HistoryCategory.COLOMBIA_MISSION) },
                    label = { Text("Colombia 1987", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedCategory == HistoryCategory.RECENT_2026,
                    onClick = { viewModel.setHistoryCategory(HistoryCategory.RECENT_2026) },
                    label = { Text("2022–2026", fontSize = 11.sp) }
                )
            }
        }

        items(filteredTimeline, key = { "${it.year}_${it.title}" }) { event ->
            TimelineCard(
                event = event,
                onListen = {
                    val text = "Año ${event.year}. ${event.title}. ${event.dateOrPeriod}, en ${event.location}. ${event.summary}. Significado: ${event.significance}"
                    viewModel.speakText(text)
                }
            )
        }
    }
}

@Composable
private fun TimelineCard(
    event: HistoricalEvent,
    onListen: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("timeline_card_${event.year}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, ParchmentBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = when (event.category) {
                            HistoryCategory.FOUNDATION -> BasilianNavy
                            HistoryCategory.PONTIFICAL_APPROVAL -> BasilianBlue
                            HistoryCategory.EXPANSION_AMERICA -> Color(0xFF2E7D32)
                            HistoryCategory.COLOMBIA_MISSION -> BasilianCrimson
                            HistoryCategory.RENEWAL_BICENTENARY -> BasilianGoldDark
                            HistoryCategory.RECENT_2026 -> Color(0xFF00695C)
                        },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text(
                            text = event.year,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Column {
                        Text(
                            text = event.dateOrPeriod,
                            style = MaterialTheme.typography.labelSmall,
                            color = BasilianGoldDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = event.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = onListen,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Escuchar evento",
                        tint = BasilianBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = event.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = event.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Significado: ${event.significance}",
                    style = MaterialTheme.typography.labelSmall,
                    color = BasilianNavy,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
