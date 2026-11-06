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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserReflection
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

@Composable
fun CuadernoScreen(
    viewModel: BasilianViewModel,
    modifier: Modifier = Modifier
) {
    val reflections by viewModel.userReflections.collectAsState()
    val isSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val todayInspiration = viewModel.todayInspiration

    var showNewNoteDialog by remember { mutableStateOf(false) }
    var selectedVirtueFilter by remember { mutableStateOf("TODAS") }

    var titleInput by remember { mutableStateOf("") }
    var contentInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("Reflexión") }
    var virtueTagInput by remember { mutableStateOf("Bondad") }

    val filteredReflections = remember(reflections, selectedVirtueFilter) {
        if (selectedVirtueFilter == "TODAS") reflections
        else reflections.filter { it.virtueTag.equals(selectedVirtueFilter, ignoreCase = true) }
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("cuaderno_screen"),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    titleInput = ""
                    contentInput = ""
                    categoryInput = "Reflexión"
                    virtueTagInput = "Bondad"
                    showNewNoteDialog = true
                },
                containerColor = BasilianBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("add_reflection_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Escribir nota")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(BasilianNavy, BasilianBlue)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Surface(
                                    color = BasilianGold.copy(alpha = 0.2f),
                                    shape = CircleShape,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.AutoStories,
                                            contentDescription = null,
                                            tint = BasilianGold,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Surface(
                                    color = Color.White.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "${reflections.size} notas guardadas",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Cuaderno Espiritual",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "Tu diario personal para oraciones, resoluciones morales y meditaciones en el espíritu basiliano.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            // Quick Import Today's Inspiration Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, BasilianGold.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "¿Inspirado por la reflexión de hoy?",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = BasilianNavy
                            )
                            Text(
                                text = todayInspiration.stBasilWisdom.take(80) + "...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                titleInput = "Reflexión: ${todayInspiration.basilianMottoFocus}"
                                contentInput = "Enseñanza: \"${todayInspiration.stBasilWisdom}\"\n\nMi propósito: "
                                categoryInput = "Reflexión"
                                virtueTagInput = if (todayInspiration.basilianMottoFocus.contains("Bondad")) "Bondad"
                                else if (todayInspiration.basilianMottoFocus.contains("Disciplina")) "Disciplina"
                                else "Ciencia"
                                showNewNoteDialog = true
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BasilianGoldDark),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("Escribir", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }

            // Virtue Filter Chips
            item {
                Column {
                    Text(
                        text = "Filtrar por pilar basiliano",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(listOf("TODAS", "Bondad", "Disciplina", "Ciencia")) { virtue ->
                            val isSelected = selectedVirtueFilter == virtue
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedVirtueFilter = virtue },
                                label = { Text(virtue, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BasilianNavy,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Reflections List
            if (filteredReflections.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = BasilianBlue.copy(alpha = 0.5f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Aún no tienes notas con este filtro.",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Toca el botón '+' para escribir tu primera reflexión.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            } else {
                items(filteredReflections, key = { it.id }) { note ->
                    ReflectionCard(
                        note = note,
                        isSpeaking = isSpeaking,
                        onSpeak = {
                            viewModel.speakText("${note.title}. ${note.content}")
                        },
                        onDelete = {
                            viewModel.deleteReflection(note)
                        }
                    )
                }
            }
        }
    }

    // New Note Dialog
    if (showNewNoteDialog) {
        AlertDialog(
            onDismissRequest = { showNewNoteDialog = false },
            title = {
                Text(
                    text = "Nueva Entrada Espiritual",
                    fontWeight = FontWeight.Bold,
                    color = BasilianNavy
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Título o Asunto") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Virtue Tag Selector
                    Text("Pilar:", style = MaterialTheme.typography.labelMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Bondad", "Disciplina", "Ciencia").forEach { virtue ->
                            val selected = virtueTagInput == virtue
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selected) BasilianBlue else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { virtueTagInput = virtue }
                            ) {
                                Text(
                                    text = virtue,
                                    fontSize = 11.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = contentInput,
                        onValueChange = { contentInput = it },
                        label = { Text("Escribe tu reflexión o propósito...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 6
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (titleInput.isNotBlank() || contentInput.isNotBlank()) {
                            viewModel.saveReflection(
                                title = if (titleInput.isBlank()) "Reflexión" else titleInput,
                                content = contentInput,
                                category = categoryInput,
                                virtueTag = virtueTagInput
                            )
                            showNewNoteDialog = false
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BasilianBlue)
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewNoteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ReflectionCard(
    note: UserReflection,
    isSpeaking: Boolean,
    onSpeak: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = when (note.virtueTag) {
                        "Bondad" -> BasilianGold.copy(alpha = 0.2f)
                        "Disciplina" -> BasilianCrimson.copy(alpha = 0.12f)
                        else -> BasilianBlue.copy(alpha = 0.12f)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = note.virtueTag,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when (note.virtueTag) {
                            "Bondad" -> BasilianGoldDark
                            "Disciplina" -> BasilianCrimson
                            else -> BasilianBlue
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = note.dateText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onSpeak) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                        contentDescription = "Escuchar",
                        tint = BasilianBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
