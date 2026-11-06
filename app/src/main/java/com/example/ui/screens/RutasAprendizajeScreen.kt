package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.*
import com.example.ui.components.BasilianCrestView
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

@Composable
fun RutasAprendizajeScreen(
    viewModel: BasilianViewModel,
    modifier: Modifier = Modifier
) {
    val currentProfile by viewModel.adaptiveProfile.collectAsState()
    val currentRhythm by viewModel.adaptiveRhythm.collectAsState()
    val completedIds by viewModel.completedModuleIds.collectAsState()

    var selectedEje by remember { mutableStateOf("Todos") }
    val allModules = remember { RutasAprendizajeData.modulos }

    val filteredModules = remember(selectedEje) {
        if (selectedEje == "Todos") allModules else allModules.filter { it.eje == selectedEje }
    }

    val completionPercentage = remember(completedIds.size, allModules.size) {
        if (allModules.isEmpty()) 0 else (completedIds.size * 100) / allModules.size
    }

    var selectedModuleForReflection by remember { mutableStateOf<ModuloAprendizaje?>(null) }
    var metaReflectionInput by remember { mutableStateOf("") }
    var showSavedSnackbar by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BasilianIvory)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cabecera: Pedagogía Nómada y Meta-Aprendizaje
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.5.dp, ShieldGreen.copy(alpha = 0.35f)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BasilianCrestView(size = 56.dp, showMottoText = false)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "PEDAGOGÍA NÓMADA & FORMACIÓN",
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldRed,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.1.sp
                            )
                            Text(
                                text = "Rutas de Aprendizaje Adaptativo",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ShieldGreenDark,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = "Aprender en el camino, discernir en el desierto y crecer en fraternidad",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }

                    // Barra de Progreso del Itinerario
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Progreso del Itinerario:",
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldGreenDark,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "$completionPercentage% completado (${completedIds.size}/${allModules.size})",
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        LinearProgressIndicator(
                            progress = { completionPercentage / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = ShieldGreen,
                            trackColor = ShieldGreen.copy(alpha = 0.15f)
                        )
                    }
                }
            }
        }

        // 1. Selector de Perfil de Formación
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "1. Tu Etapa de Formación:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = ShieldGreenDark
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(PerfilFormacion.values()) { perfil ->
                            val isSelected = perfil == currentProfile
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setAdaptiveProfile(perfil) },
                                label = {
                                    Text(
                                        text = perfil.label,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ShieldGreenDark,
                                    selectedLabelColor = Color.White,
                                    containerColor = BasilianIvory,
                                    labelColor = ShieldGreenDark
                                )
                            )
                        }
                    }
                    Text(
                        text = currentProfile.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = ShieldRed,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }

        // 2. Selector de Ritmo Nómada (Meta-Aprendizaje en tiempo)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "2. Ritmo Nómada Diario:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = ShieldGreenDark
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RitmoNomada.values().forEach { ritmo ->
                            val isSelected = ritmo == currentRhythm
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) ShieldGold.copy(alpha = 0.2f) else BasilianIvory,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) ShieldGold else Color.LightGray.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setAdaptiveRhythm(ritmo) }
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${ritmo.durationMinutes} min",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) ShieldGreenDark else Color.DarkGray
                                    )
                                    Text(
                                        text = ritmo.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = currentRhythm.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }
            }
        }

        // Filtro por Eje Temático
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "EJES TEMÁTICOS BASILIANOS:",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                val ejes = listOf("Todos", "Psicología Espiritual", "Virtudes Basilianas", "Ascética y Oración Nómada")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(ejes) { eje ->
                        val isSelected = eje == selectedEje
                        SuggestionChip(
                            onClick = { selectedEje = eje },
                            label = {
                                Text(
                                    text = eje,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = if (isSelected) ShieldRed else Color.White,
                                labelColor = if (isSelected) Color.White else ShieldGreenDark
                            ),
                            border = BorderStroke(1.dp, if (isSelected) ShieldRed else Color.LightGray.copy(alpha = 0.5f))
                        )
                    }
                }
            }
        }

        // Lista de Módulos Adaptativos
        items(filteredModules) { modulo ->
            val isCompleted = completedIds.contains(modulo.id)
            val isSelectedForMeta = selectedModuleForReflection?.id == modulo.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("module_card_${modulo.id}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCompleted) Color(0xFFF0FDF4) else Color.White
                ),
                border = BorderStroke(
                    1.2.dp,
                    if (isCompleted) ShieldGreen else Color.LightGray.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Tag de Eje y Estado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (modulo.eje) {
                                "Psicología Espiritual" -> ShieldGreenDark.copy(alpha = 0.12f)
                                "Virtudes Basilianas" -> ShieldRed.copy(alpha = 0.12f)
                                else -> ShieldGold.copy(alpha = 0.18f)
                            }
                        ) {
                            Text(
                                text = modulo.eje.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = when (modulo.eje) {
                                    "Psicología Espiritual" -> ShieldGreenDark
                                    "Virtudes Basilianas" -> ShieldRed
                                    else -> Color(0xFF8B6508)
                                },
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                letterSpacing = 0.8.sp
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                viewModel.toggleModuleCompletion(modulo.id)
                            }
                        ) {
                            Checkbox(
                                checked = isCompleted,
                                onCheckedChange = { viewModel.toggleModuleCompletion(modulo.id) },
                                colors = CheckboxDefaults.colors(checkedColor = ShieldGreen)
                            )
                            Text(
                                text = if (isCompleted) "Completado" else "Pendiente",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isCompleted) ShieldGreen else Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Título y Cita Patrística
                    Text(
                        text = modulo.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ShieldGreenDark,
                        fontFamily = FontFamily.Serif
                    )

                    Text(
                        text = modulo.subtitulo,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BasilianIvory,
                        border = BorderStroke(0.8.dp, ShieldGold.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = modulo.citasFuente,
                            style = MaterialTheme.typography.labelSmall,
                            color = ShieldRed,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    // Contenido Teórico
                    Text(
                        text = modulo.contenidoTeorico,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF1E293B),
                        lineHeight = 22.sp
                    )

                    // Aplicación Práctica
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ShieldGreen.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, ShieldGreen.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsWalk,
                                contentDescription = null,
                                tint = ShieldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = "PRÁCTICA DEL CAMINO NÓMADA:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ShieldGreenDark,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = modulo.aplicacionPractica,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF1E293B)
                                )
                            }
                        }
                    }

                    // Sección de Meta-Aprendizaje (Preguntas Reflexivas)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ShieldGold.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = ShieldGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "PREGUNTAS DE META-APRENDIZAJE:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ShieldGreenDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            modulo.preguntasMetaAprendizaje.forEach { preg ->
                                Text(
                                    text = "• $preg",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF2C241E),
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedButton(
                                onClick = {
                                    if (isSelectedForMeta) {
                                        selectedModuleForReflection = null
                                    } else {
                                        selectedModuleForReflection = modulo
                                        metaReflectionInput = ""
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = ShieldGreenDark
                                ),
                                border = BorderStroke(1.dp, ShieldGreen)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isSelectedForMeta) "Cerrar Diario de Meta-Aprendizaje" else "Escribir en mi Diario Espiritual",
                                    fontSize = 12.sp
                                )
                            }

                            // Formulario desplegable para guardar la reflexión
                            AnimatedVisibility(visible = isSelectedForMeta) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = metaReflectionInput,
                                        onValueChange = { metaReflectionInput = it },
                                        placeholder = {
                                            Text(
                                                "Escribe aquí tu respuesta reflexiva sobre cómo aplicar esta virtud hoy...",
                                                fontSize = 12.sp
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        minLines = 3,
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    Button(
                                        onClick = {
                                            if (metaReflectionInput.isNotBlank()) {
                                                viewModel.saveReflection(
                                                    title = "Ruta Nómada: ${modulo.titulo}",
                                                    content = metaReflectionInput,
                                                    category = modulo.eje,
                                                    virtueTag = modulo.subtitulo
                                                )
                                                if (!isCompleted) {
                                                    viewModel.toggleModuleCompletion(modulo.id)
                                                }
                                                metaReflectionInput = ""
                                                selectedModuleForReflection = null
                                                showSavedSnackbar = true
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Guardar en mi Cuaderno", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
