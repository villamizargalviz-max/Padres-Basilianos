package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

enum class NomadSubTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    EN_MOVIMIENTO("En Movimiento", Icons.Default.DirectionsBike),
    PEREGRINACION("Peregrinación Virtual", Icons.Default.Explore)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModoNomadaScreen(
    viewModel: BasilianViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableStateOf(NomadSubTab.EN_MOVIMIENTO) }
    val activeTrip by viewModel.activeTrip.collectAsState()
    val totalKm by viewModel.totalPilgrimageKm.collectAsState()
    val isSpeaking by viewModel.isTtsSpeaking.collectAsState()

    var showAddKmDialog by remember { mutableStateOf(false) }
    var selectedMilestoneForDetail by remember { mutableStateOf<PilgrimageMilestone?>(null) }
    var selectedCapsuleForDetail by remember { mutableStateOf<WisdomCapsule?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF9F9F6),
        topBar = {
            Surface(
                color = ShieldGreenDark,
                tonalElevation = 4.dp
            ) {
                TabRow(
                    selectedTabIndex = selectedSubTab.ordinal,
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab.ordinal]),
                            color = ShieldGoldLight,
                            height = 3.dp
                        )
                    }
                ) {
                    NomadSubTab.values().forEach { tab ->
                        Tab(
                            selected = selectedSubTab == tab,
                            onClick = { selectedSubTab = tab },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = if (selectedSubTab == tab) ShieldGoldLight else Color.White.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = tab.label,
                                        fontWeight = if (selectedSubTab == tab) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedSubTab == tab) ShieldGoldLight else Color.White.copy(alpha = 0.8f),
                                        fontSize = 13.sp
                                    )
                                }
                            },
                            modifier = Modifier.testTag("nomad_subtab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        when (selectedSubTab) {
            NomadSubTab.EN_MOVIMIENTO -> {
                EnMovimientoTabContent(
                    viewModel = viewModel,
                    activeTrip = activeTrip,
                    isSpeaking = isSpeaking,
                    onOpenAddKm = { showAddKmDialog = true },
                    onSelectCapsule = { selectedCapsuleForDetail = it },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            NomadSubTab.PEREGRINACION -> {
                PeregrinacionTabContent(
                    viewModel = viewModel,
                    totalKm = totalKm,
                    onOpenAddKm = { showAddKmDialog = true },
                    onSelectMilestone = { selectedMilestoneForDetail = it },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }

    // Modal para registrar kilómetros manualmente (ej. trayectos previos en bici o a pie)
    if (showAddKmDialog) {
        AddKilometersDialog(
            onDismiss = { showAddKmDialog = false },
            onConfirm = { km ->
                viewModel.addPilgrimageKm(km)
                showAddKmDialog = false
            }
        )
    }

    // Modal de Detalle de Hito Histórico Desbloqueado
    selectedMilestoneForDetail?.let { milestone ->
        val isUnlocked = totalKm >= milestone.targetKm
        MilestoneDetailDialog(
            milestone = milestone,
            isUnlocked = isUnlocked,
            totalKm = totalKm,
            onPlayAudio = { viewModel.playMilestoneMeditation(milestone) },
            onDismiss = { selectedMilestoneForDetail = null }
        )
    }

    // Modal de Detalle y Lectura de Cápsula de Sabiduría
    selectedCapsuleForDetail?.let { capsule ->
        CapsuleDetailDialog(
            capsule = capsule,
            isSpeaking = isSpeaking,
            onPlayAudio = { viewModel.playWisdomCapsule(capsule) },
            onStopAudio = { viewModel.stopSpeaking() },
            onDismiss = { selectedCapsuleForDetail = null }
        )
    }
}

// ----------------------------------------------------------------------
// CONTENIDO TAB 1: EN MOVIMIENTO (TRAYECTO ACTIVO & CÁPSULAS)
// ----------------------------------------------------------------------
@Composable
fun EnMovimientoTabContent(
    viewModel: BasilianViewModel,
    activeTrip: ActiveTripState,
    isSpeaking: Boolean,
    onOpenAddKm: () -> Unit,
    onSelectCapsule: (WisdomCapsule) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMode by remember { mutableStateOf(activeTrip.mode) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado Carismático
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(ShieldGreen.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsWalk,
                            contentDescription = null,
                            tint = ShieldGreenDark,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ESPIRITUALIDAD NÓMADA",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ShieldRed,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Orar en el Camino y la Ciudad",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ShieldGreenDark,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Transforma tu trayecto diario en un claustro contemplativo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray,
                            fontSize = 11.5.sp
                        )
                    }
                }
            }
        }

        // Selector de Modo de Desplazamiento
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Selecciona tu Modo de Desplazamiento:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = ShieldGreenDark
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TransportMode.values().forEach { mode ->
                        val isSelected = selectedMode == mode
                        Surface(
                            onClick = {
                                selectedMode = mode
                                if (!activeTrip.isRunning) {
                                    // Actualiza modo si no está corriendo
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) ShieldGreenDark else Color.White,
                            border = BorderStroke(
                                1.2.dp,
                                if (isSelected) ShieldGold else Color.LightGray.copy(alpha = 0.6f)
                            ),
                            shadowElevation = if (isSelected) 3.dp else 1.dp,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("transport_mode_${mode.name.lowercase()}")
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = mode.iconEmoji,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = mode.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else ShieldGreenDark,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // PANEL TACÓMETRO DE TRAYECTO ACTIVO
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("active_trip_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (activeTrip.isRunning) Color(0xFF0D2818) else ShieldGreenDark
                ),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Barra superior del tacómetro
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (activeTrip.isRunning) Color(0xFF4CAF50) else ShieldGoldLight)
                            )
                            Text(
                                text = if (activeTrip.isRunning) "TRAYECTO EN CURSO" else "TRAYECTO LISTO",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (activeTrip.isRunning) Color(0xFF81C784) else ShieldGoldLight,
                                letterSpacing = 1.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${selectedMode.iconEmoji} ${selectedMode.label}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Métricas Principales (Distancia y Tiempo)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Distancia
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "DISTANCIA",
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldGoldLight.copy(alpha = 0.8f),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = String.format(java.util.Locale.US, "%.2f", activeTrip.distanceKm),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "kilómetros",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }

                        // Separador vertical
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(60.dp)
                                .background(Color.White.copy(alpha = 0.2f))
                        )

                        // Tiempo
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val minutes = activeTrip.elapsedSeconds / 60
                            val seconds = activeTrip.elapsedSeconds % 60
                            Text(
                                text = "TIEMPO",
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldGoldLight.copy(alpha = 0.8f),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = String.format(java.util.Locale.US, "%02d:%02d", minutes, seconds),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "min : seg",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }

                    // Métricas Secundarias: Velocidad y Calorías
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.25f))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = ShieldGoldLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Velocidad: ${String.format(java.util.Locale.US, "%.1f", activeTrip.currentSpeedKmh)} km/h",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = ShieldRedLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${activeTrip.estimatedCalories} kcal",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Botones de Acción de Trayecto
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (!activeTrip.isRunning) {
                            Button(
                                onClick = { viewModel.startTrip(selectedMode) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("start_trip_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ShieldGold,
                                    contentColor = Color(0xFF1B3B2B)
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (activeTrip.distanceKm > 0f) "Reanudar" else "Iniciar Trayecto",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Button(
                                onClick = { viewModel.pauseTrip() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("pause_trip_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFA000),
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Pause,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Pausar", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { viewModel.finishTrip() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("finish_trip_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ShieldRed,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Stop,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Finalizar", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Botón secundario para registrar kilómetros manuales
                    OutlinedButton(
                        onClick = onOpenAddKm,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("add_manual_km_button"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldGoldLight),
                        border = BorderStroke(1.dp, ShieldGoldLight.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddLocationAlt,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+ Registrar trayecto manual (bici/caminata)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Sugerencia de Cápsula si el trayecto la activa
        activeTrip.activeCapsule?.let { capsule ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_suggested_capsule"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6)),
                    border = BorderStroke(1.5.dp, ShieldGold),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = null,
                                tint = ShieldRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "CÁPSULA SUGERIDA PARA ESTE TRAYECTO",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ShieldRed
                            )
                        }
                        Text(
                            text = capsule.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ShieldGreenDark
                        )
                        Text(
                            text = capsule.quote,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4A3700),
                            fontFamily = FontFamily.Serif,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { viewModel.playWisdomCapsule(capsule) },
                                colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = if (isSpeaking) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = ShieldGoldLight
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isSpeaking) "Detener" else "Escuchar Cápsula",
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // TÍTULO SECCIÓN CÁPSULAS DE SABIDURÍA
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cápsulas de Sabiduría Basilianas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ShieldGreenDark,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "${viewModel.wisdomCapsules.size} disponibles",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }

        // LISTA DE CÁPSULAS DE SABIDURÍA EN AUDIO
        items(viewModel.wisdomCapsules) { capsule ->
            CapsuleItemCard(
                capsule = capsule,
                isSpeaking = isSpeaking,
                onPlay = { viewModel.playWisdomCapsule(capsule) },
                onDetails = { onSelectCapsule(capsule) }
            )
        }
    }
}

@Composable
fun CapsuleItemCard(
    capsule: WisdomCapsule,
    isSpeaking: Boolean,
    onPlay: () -> Unit,
    onDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDetails() }
            .testTag("capsule_${capsule.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(1.5.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ShieldGreen.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = capsule.recommendedMode.iconEmoji, fontSize = 12.sp)
                        Text(
                            text = capsule.recommendedMode.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = ShieldGreenDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${capsule.durationMinutes} min",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }

            Text(
                text = capsule.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = ShieldGreenDark
            )

            Text(
                text = capsule.quote,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray,
                fontFamily = FontFamily.Serif,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = capsule.author,
                    style = MaterialTheme.typography.labelSmall,
                    color = ShieldRed,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp
                )

                IconButton(
                    onClick = onPlay,
                    modifier = Modifier
                        .size(36.dp)
                        .background(ShieldGreenDark, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Reproducir audio",
                        tint = ShieldGoldLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------------------------
// CONTENIDO TAB 2: PEREGRINACIÓN VIRTUAL (MAPA DE VIAJES DE SAN BASILIO)
// ----------------------------------------------------------------------
@Composable
fun PeregrinacionTabContent(
    viewModel: BasilianViewModel,
    totalKm: Float,
    onOpenAddKm: () -> Unit,
    onSelectMilestone: (PilgrimageMilestone) -> Unit,
    modifier: Modifier = Modifier
) {
    val maxKm = 300.0f
    val progressFraction = (totalKm / maxKm).coerceIn(0f, 1f)
    val percentage = (progressFraction * 100).toInt()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner de Progreso de la Peregrinación
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("pilgrimage_hero_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = ShieldGreenDark),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "PEREGRINACIÓN HISTÓRICA",
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldGoldLight,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Los Viajes de San Basilio",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = FontFamily.Serif
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = ShieldGold
                        ) {
                            Text(
                                text = "$percentage%",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B3B2B),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Barra de progreso dorada
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        LinearProgressIndicator(
                            progress = { progressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = ShieldGold,
                            trackColor = Color.White.copy(alpha = 0.2f)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${String.format(java.util.Locale.US, "%.1f", totalKm)} km recorridos",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Text(
                                text = "Meta: 300.0 km",
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldGoldLight
                            )
                        }
                    }

                    Button(
                        onClick = onOpenAddKm,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ShieldGold,
                            contentColor = Color(0xFF1B3B2B)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddRoad,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "+ Registrar Kilómetros de Hoy", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // TÍTULO DEL MAPA
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Mapa Virtual de Hitos Históricos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ShieldGreenDark,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "Avanza físicamente para desbloquear textos exclusivos sobre la vida monástica y la contemplación activa.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }

        // HITOS HISTÓRICOS INTERACTIVOS
        items(viewModel.pilgrimageMilestones) { milestone ->
            val isUnlocked = totalKm >= milestone.targetKm
            MilestoneCardItem(
                milestone = milestone,
                isUnlocked = isUnlocked,
                totalKm = totalKm,
                onClick = { onSelectMilestone(milestone) }
            )
        }
    }
}

@Composable
fun MilestoneCardItem(
    milestone: PilgrimageMilestone,
    isUnlocked: Boolean,
    totalKm: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("milestone_${milestone.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) Color.White else Color(0xFFF3F4F6)
        ),
        border = BorderStroke(
            1.5.dp,
            if (isUnlocked) ShieldGold else Color.LightGray.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(if (isUnlocked) 3.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Insignia / Estado
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) Brush.radialGradient(
                            colors = listOf(ShieldGoldLight, ShieldGold)
                        ) else Brush.linearGradient(
                            colors = listOf(Color.LightGray, Color.Gray)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isUnlocked) milestone.iconEmoji else "🔒",
                    fontSize = 22.sp
                )
            }

            // Datos del hito
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "HITO ${milestone.stepNumber} • ${milestone.targetKm.toInt()} KM",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) ShieldRed else Color.Gray,
                        letterSpacing = 0.8.sp
                    )
                    if (isUnlocked) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = ShieldGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "DESBLOQUEADO",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ShieldGreenDark,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    } else {
                        val kmLeft = (milestone.targetKm - totalKm).coerceAtLeast(0f)
                        Text(
                            text = "Faltan ${String.format(java.util.Locale.US, "%.1f", kmLeft)} km",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                    }
                }

                Text(
                    text = milestone.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) ShieldGreenDark else Color.DarkGray
                )

                Text(
                    text = "${milestone.geographicRegion} (${milestone.period})",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    fontSize = 11.5.sp
                )

                if (isUnlocked) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Toca para leer el texto exclusivo de meditación",
                        style = MaterialTheme.typography.labelSmall,
                        color = ShieldGold,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = if (isUnlocked) ShieldGold else Color.Gray,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ----------------------------------------------------------------------
// DIÁLOGOS Y MODALES
// ----------------------------------------------------------------------
@Composable
fun AddKilometersDialog(
    onDismiss: () -> Unit,
    onConfirm: (Float) -> Unit
) {
    var kmInput by remember { mutableStateOf("5.0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Default.AddLocationAlt, contentDescription = null, tint = ShieldGreenDark)
                Text(
                    text = "Registrar Trayecto Manual",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ShieldGreenDark
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Ingresa los kilómetros recorridos en tu bicicleta o caminata para sumarlos a tu Peregrinación Virtual.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
                OutlinedTextField(
                    value = kmInput,
                    onValueChange = { kmInput = it },
                    label = { Text("Kilómetros recorridos") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("km_input_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ShieldGreenDark,
                        focusedLabelColor = ShieldGreenDark
                    )
                )

                // Botones rápidos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("2.0", "5.0", "10.0", "15.0").forEach { quickKm ->
                        Surface(
                            onClick = { kmInput = quickKm },
                            shape = RoundedCornerShape(8.dp),
                            color = ShieldGreen.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, ShieldGreen.copy(alpha = 0.3f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "+$quickKm",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ShieldGreenDark,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsed = kmInput.toFloatOrNull() ?: 0f
                    if (parsed > 0f) {
                        onConfirm(parsed)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark)
            ) {
                Text("Sumar al Mapa", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}

@Composable
fun MilestoneDetailDialog(
    milestone: PilgrimageMilestone,
    isUnlocked: Boolean,
    totalKm: Float,
    onPlayAudio: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "HITO ${milestone.stepNumber} • ${milestone.geographicRegion}",
                    style = MaterialTheme.typography.labelSmall,
                    color = ShieldRed,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = milestone.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ShieldGreenDark,
                    fontFamily = FontFamily.Serif
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isUnlocked) {
                    // Cita Patrística
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8F0)),
                        border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = milestone.patristicQuote,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF3E2723),
                                fontFamily = FontFamily.Serif,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }

                    // Texto Exclusivo Desbloqueado
                    Text(
                        text = milestone.exclusiveText,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray,
                        lineHeight = 18.sp
                    )

                    // Ejercicio Contemplativo
                    Card(
                        colors = CardDefaults.cardColors(containerColor = ShieldGreenDark.copy(alpha = 0.08f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Ejercicio de Contemplación Activa:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ShieldGreenDark
                            )
                            Text(
                                text = milestone.contemplativeExercise,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF1B3B2B)
                            )
                        }
                    }
                } else {
                    val kmNeeded = (milestone.targetKm - totalKm).coerceAtLeast(0f)
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "🔒", fontSize = 36.sp)
                        Text(
                            text = "Hito Aún Bloqueado",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "Necesitas acumular ${String.format(java.util.Locale.US, "%.1f", kmNeeded)} km más en tus trayectos diarios para desbloquear este texto de contemplación activa y sabiduría monástica.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (isUnlocked) {
                Button(
                    onClick = onPlayAudio,
                    colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark)
                ) {
                    Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null, tint = ShieldGoldLight, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Escuchar Meditación")
                }
            } else {
                Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark)) {
                    Text("Entendido")
                }
            }
        },
        dismissButton = {
            if (isUnlocked) {
                TextButton(onClick = onDismiss) {
                    Text("Cerrar", color = Color.Gray)
                }
            }
        }
    )
}

@Composable
fun CapsuleDetailDialog(
    capsule: WisdomCapsule,
    isSpeaking: Boolean,
    onPlayAudio: () -> Unit,
    onStopAudio: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "${capsule.recommendedMode.iconEmoji} ${capsule.recommendedMode.label} • ${capsule.durationMinutes} MIN",
                    style = MaterialTheme.typography.labelSmall,
                    color = ShieldRed,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = capsule.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ShieldGreenDark,
                    fontFamily = FontFamily.Serif
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8F0)),
                    border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = capsule.quote,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFF3E2723),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                        Text(
                            text = "— ${capsule.author} (${capsule.patristicSource})",
                            style = MaterialTheme.typography.labelSmall,
                            color = ShieldRed,
                            fontSize = 10.sp
                        )
                    }
                }

                Text(
                    text = capsule.audioScript,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    lineHeight = 18.sp
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = ShieldGreen.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Pregunta para meditar en tu camino:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ShieldGreenDark
                        )
                        Text(
                            text = capsule.reflectionPrompt,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF1B3B2B),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (isSpeaking) onStopAudio() else onPlayAudio() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSpeaking) ShieldRed else ShieldGreenDark
                )
            ) {
                Icon(
                    imageVector = if (isSpeaking) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isSpeaking) "Detener Audio" else "Reproducir Audio")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = Color.Gray)
            }
        }
    )
}
