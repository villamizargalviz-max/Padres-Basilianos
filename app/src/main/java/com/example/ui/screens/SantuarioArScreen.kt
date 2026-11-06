package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.data.model.ArIconographyData
import com.example.data.model.IconHotspot
import com.example.ui.components.SanBasilioIconView
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel
import kotlin.math.roundToInt

enum class ArScreenMode(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    SANTUARIO_CAMARA("Santuario Portátil AR", Icons.Default.ViewInAr),
    EXEGESIS_INTERACTIVA("Iconos Interactivos", Icons.Default.TouchApp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SantuarioArScreen(
    viewModel: BasilianViewModel,
    modifier: Modifier = Modifier
) {
    var selectedMode by remember { mutableStateOf(ArScreenMode.SANTUARIO_CAMARA) }
    val selectedHotspot by viewModel.selectedHotspot.collectAsState()
    val isSpeaking by viewModel.isTtsSpeaking.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF121814),
        topBar = {
            Surface(
                color = Color(0xFF0F1A13),
                tonalElevation = 6.dp
            ) {
                TabRow(
                    selectedTabIndex = selectedMode.ordinal,
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedMode.ordinal]),
                            color = ShieldGold,
                            height = 3.dp
                        )
                    }
                ) {
                    ArScreenMode.values().forEach { mode ->
                        Tab(
                            selected = selectedMode == mode,
                            onClick = { selectedMode = mode },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = mode.icon,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = if (selectedMode == mode) ShieldGold else Color.White.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = mode.label,
                                        fontWeight = if (selectedMode == mode) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedMode == mode) ShieldGold else Color.White.copy(alpha = 0.8f),
                                        fontSize = 13.sp
                                    )
                                }
                            },
                            modifier = Modifier.testTag("ar_tab_${mode.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedMode) {
                ArScreenMode.SANTUARIO_CAMARA -> {
                    SantuarioCamaraArView(
                        viewModel = viewModel,
                        isSpeaking = isSpeaking
                    )
                }
                ArScreenMode.EXEGESIS_INTERACTIVA -> {
                    IconoInteractivoExegesisView(
                        viewModel = viewModel,
                        selectedHotspot = selectedHotspot,
                        isSpeaking = isSpeaking
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------------------------
// MODO 1: SANTUARIO PORTÁTIL EN TIEMPO REAL (CÁMARA AR & ALTAR 3D)
// ----------------------------------------------------------------------
@Composable
fun SantuarioCamaraArView(
    viewModel: BasilianViewModel,
    isSpeaking: Boolean
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    // Controles de posicionamiento del altar
    var altarOffsetX by remember { mutableStateOf(0f) }
    var altarOffsetY by remember { mutableStateOf(0f) }

    val arScale by viewModel.arScale.collectAsState()
    val arTilt by viewModel.arTilt.collectAsState()
    val candlesEnabled by viewModel.arCandlesEnabled.collectAsState()
    val auraEnabled by viewModel.arAuraEnabled.collectAsState()

    var isZenMode by remember { mutableStateOf(false) }

    // Animación de llama de cirios parpadeante
    val infiniteTransition = rememberInfiniteTransition(label = "CandleFlicker")
    val flameFlicker by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(180, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FlameScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("santuario_ar_container")
    ) {
        // 1. FONDO: VISTA DE CÁMARA EN VIVO O FONDO SAGRADO DE CAPILA
        if (hasCameraPermission) {
            CameraPreviewFeed(modifier = Modifier.fillMaxSize())
        } else {
            // Fondo de Capilla y Altar de Madera Noble
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1E2822),
                                Color(0xFF2C1810),
                                Color(0xFF120C08)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = ShieldGold,
                        modifier = Modifier.size(54.dp)
                    )
                    Text(
                        text = "Santuario Portátil en tu Entorno",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Permite el acceso a la cámara para proyectar el ícono sagrado de San Basilio sobre tu mesa de noche, escritorio o altar personal.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                        colors = ButtonDefaults.buttonColors(containerColor = ShieldGold, contentColor = Color(0xFF1B3B2B)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("request_camera_button")
                    ) {
                        Icon(imageVector = Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Activar Cámara AR", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Overlay sutil de viñeta para dar solemnidad
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.45f))
                    )
                )
        )

        // 2. EL ALTAR PORTÁTIL FLOTANTE EN PERSPECTIVA 3D
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(altarOffsetX.roundToInt(), altarOffsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        altarOffsetX += dragAmount.x
                        altarOffsetY += dragAmount.y
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = arScale
                        scaleY = arScale
                        rotationX = arTilt
                        cameraDistance = 12f * density
                    }
            ) {
                // Fila con Cirio Izquierdo + Ícono Sagrado + Cirio Derecho
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Cirio Izquierdo
                    if (candlesEnabled) {
                        AltarCandleView(flameScale = flameFlicker)
                    }

                    // Ícono Sagrado con Marco Dorado y Resplandor de Gracia
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.testTag("ar_san_basilio_altar")
                    ) {
                        if (auraEnabled) {
                            // Aura Taboriana radiante
                            Box(
                                modifier = Modifier
                                    .size(240.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                ShieldGoldLight.copy(alpha = 0.45f),
                                                ShieldGold.copy(alpha = 0.20f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )
                        }

                        // Lienzo del Icono
                        SanBasilioIconView(
                            modifier = Modifier
                                .width(200.dp)
                                .shadow(14.dp, RoundedCornerShape(22.dp)),
                            canvasSize = 160.dp
                        )
                    }

                    // Cirio Derecho
                    if (candlesEnabled) {
                        AltarCandleView(flameScale = flameFlicker * 0.96f)
                    }
                }

                // Base o Peana de Madera Sagrada del Altar
                Box(
                    modifier = Modifier
                        .width(260.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF5D4037), Color(0xFF3E2723), Color(0xFF1B0000))
                            )
                        )
                        .border(1.dp, ShieldGold.copy(alpha = 0.6f), RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                )
            }
        }

        // 3. BARRA SUPERIOR DE INSTRUCCIONES AR
        if (!isZenMode) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color.Black.copy(alpha = 0.70f),
                border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TouchApp,
                        contentDescription = null,
                        tint = ShieldGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Arrastra para mover el altar sobre la mesa",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontSize = 11.5.sp
                    )
                }
            }
        }

        // 4. PANEL DE CONTROLES INFERIOR FLOTANTE
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Controles de ajuste (ocultables en modo zen)
            AnimatedVisibility(
                visible = !isZenMode,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF152219).copy(alpha = 0.90f),
                    border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.4f)),
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Slider de Tamaño
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Tamaño",
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldGoldLight,
                                modifier = Modifier.width(60.dp)
                            )
                            Slider(
                                value = arScale,
                                onValueChange = { viewModel.setArScale(it) },
                                valueRange = 0.6f..1.5f,
                                modifier = Modifier.weight(1f),
                                colors = SliderDefaults.colors(
                                    thumbColor = ShieldGold,
                                    activeTrackColor = ShieldGoldLight,
                                    inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                                )
                            )
                        }

                        // Slider de Inclinación 3D
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Inclinación",
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldGoldLight,
                                modifier = Modifier.width(60.dp)
                            )
                            Slider(
                                value = arTilt,
                                onValueChange = { viewModel.setArTilt(it) },
                                valueRange = -30f..15f,
                                modifier = Modifier.weight(1f),
                                colors = SliderDefaults.colors(
                                    thumbColor = ShieldGold,
                                    activeTrackColor = ShieldGoldLight,
                                    inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                                )
                            )
                        }

                        // Botones de Cirios y Aura
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                selected = candlesEnabled,
                                onClick = { viewModel.toggleArCandles() },
                                label = { Text("Cirios Pascuales", fontSize = 11.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Whatshot,
                                        contentDescription = null,
                                        tint = if (candlesEnabled) Color(0xFFFF9800) else Color.Gray,
                                        modifier = Modifier.size(14.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ShieldGold.copy(alpha = 0.25f),
                                    selectedLabelColor = Color.White
                                )
                            )

                            FilterChip(
                                selected = auraEnabled,
                                onClick = { viewModel.toggleArAura() },
                                label = { Text("Luz Increada", fontSize = 11.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = if (auraEnabled) ShieldGoldLight else Color.Gray,
                                        modifier = Modifier.size(14.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ShieldGold.copy(alpha = 0.25f),
                                    selectedLabelColor = Color.White
                                )
                            )

                            // Botón de recentrar
                            IconButton(
                                onClick = {
                                    altarOffsetX = 0f
                                    altarOffsetY = 0f
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CenterFocusStrong,
                                    contentDescription = "Recentrar",
                                    tint = ShieldGoldLight,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Barra de Acción Rápida (Modo Oración y Bendición)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { isZenMode = !isZenMode },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isZenMode) ShieldGold else Color(0xFF2C3E30)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = if (isZenMode) Icons.Default.Tune else Icons.Default.Spa,
                        contentDescription = null,
                        tint = if (isZenMode) Color(0xFF1B3B2B) else ShieldGoldLight,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isZenMode) "Mostrar Ajustes" else "Modo Contemplación",
                        color = if (isZenMode) Color(0xFF1B3B2B) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp
                    )
                }

                Button(
                    onClick = {
                        val blessing = "Que la bendición de Dios Todopoderoso: Padre, Hijo y Espíritu Santo, y la intercesión venerable de San Basilio Magno, custodie tu vida, tu hogar y tu camino hoy y siempre. Amén."
                        viewModel.speakText(blessing)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Default.VolumeOff else Icons.Default.RecordVoiceOver,
                        contentDescription = null,
                        tint = ShieldGoldLight,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSpeaking) "Detener" else "Plegaria de Bendición",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp
                    )
                }
            }
        }
    }
}

// Visualizador de Cirio Pascual con llama animada
@Composable
fun AltarCandleView(flameScale: Float) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Llama parpadeante
        Box(
            modifier = Modifier
                .size((14 * flameScale).dp, (24 * flameScale).dp)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 6.dp, bottomEnd = 6.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFD54F), Color(0xFFFF6D00))
                    )
                )
        )
        // Mecha negra
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(4.dp)
                .background(Color.Black)
        )
        // Cera del cirio blanco
        Box(
            modifier = Modifier
                .width(16.dp)
                .height(65.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFFEEEEEE), Color(0xFFFFFFFF), Color(0xFFD7CCC8))
                    )
                )
                .border(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
        )
        // Candelabro dorado
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFFB8860B), ShieldGold, Color(0xFF8B6508))
                    )
                )
        )
    }
}

// Vista de Cámara CameraX PreviewView
@Composable
fun CameraPreviewFeed(modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = modifier
    )
}

// ----------------------------------------------------------------------
// MODO 2: ICONOS INTERACTIVOS & EXÉGESIS TEOLÓGICA PROFUNDA
// ----------------------------------------------------------------------
@Composable
fun IconoInteractivoExegesisView(
    viewModel: BasilianViewModel,
    selectedHotspot: IconHotspot?,
    isSpeaking: Boolean
) {
    val hotspots = ArIconographyData.hotspots

    LazyRow(
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = false
    ) {
        // En una pantalla regular usamos Column con scroll
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2A20)),
            border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(ShieldGold.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, tint = ShieldGold, modifier = Modifier.size(22.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "VENTANAS AL CIELO",
                        style = MaterialTheme.typography.labelSmall,
                        color = ShieldGoldLight,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Exégesis Teológica del Ícono Sagrado",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Toca los puntos dorados para descubrir el simbolismo patrístico de cada elemento.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // LIENZO INTERACTIVO CON EL ÍCONO Y LOS PUNTOS PULSANTES
        Box(
            modifier = Modifier
                .size(310.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(Color(0xFF0F1A13))
                .border(2.dp, ShieldGold, RoundedCornerShape(26.dp))
                .testTag("interactive_icon_canvas"),
            contentAlignment = Alignment.Center
        ) {
            // El ícono de San Basilio
            SanBasilioIconView(
                modifier = Modifier.fillMaxSize(),
                canvasSize = 270.dp
            )

            // PUNTOS INTERACTIVOS (HOTSPOTS)
            hotspots.forEach { hotspot ->
                val isSelected = selectedHotspot?.id == hotspot.id

                // Animación de pulso
                val infiniteTransition = rememberInfiniteTransition(label = "HotspotPulse")
                val pulseScale by infiniteTransition.animateFloat(
                    initialValue = 1.0f,
                    targetValue = if (isSelected) 1.4f else 1.15f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(900, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "PulseScale"
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Posicionamiento absoluto según coordenadas normalizadas
                    val xOffset = (hotspot.normalizedX * 310).dp - 18.dp
                    val yOffset = (hotspot.normalizedY * 310).dp - 18.dp

                    Box(
                        modifier = Modifier
                            .offset(x = xOffset, y = yOffset)
                            .size(36.dp)
                            .clickable { viewModel.selectIconHotspot(hotspot) }
                            .testTag("hotspot_marker_${hotspot.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        // Halo exterior pulsante
                        Box(
                            modifier = Modifier
                                .size((30 * pulseScale).dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) ShieldRed.copy(alpha = 0.55f) else ShieldGold.copy(alpha = 0.45f)
                                )
                        )
                        // Centro del botón
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) ShieldRed else ShieldGold)
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✦",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // SELECTOR DE PUNTOS EN CHIPS HORIZONTALES
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(hotspots) { hotspot ->
                val isSelected = selectedHotspot?.id == hotspot.id
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.selectIconHotspot(hotspot) },
                    label = {
                        Text(
                            text = hotspot.shortBadge,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ShieldGold,
                        selectedLabelColor = Color(0xFF1B3B2B),
                        containerColor = Color.White.copy(alpha = 0.10f),
                        labelColor = Color.White
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) ShieldGold else Color.White.copy(alpha = 0.25f)
                    ),
                    modifier = Modifier.testTag("hotspot_chip_${hotspot.id}")
                )
            }
        }

        // DETALLE TEOLÓGICO DEL PUNTO SELECCIONADO
        selectedHotspot?.let { spot ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("selected_hotspot_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.5.dp, ShieldGold),
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
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ShieldRed.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = spot.theologicalCategory,
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldRed,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                fontSize = 10.5.sp
                            )
                        }

                        IconButton(
                            onClick = { viewModel.playHotspotExegesis(spot) },
                            modifier = Modifier
                                .size(40.dp)
                                .background(ShieldGreenDark, CircleShape)
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                contentDescription = "Escuchar exégesis",
                                tint = ShieldGoldLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = spot.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ShieldGreenDark,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = spot.greekTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = ShieldGold,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = spot.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )

                    // EXÉGESIS TEOLÓGICA
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F6EE)),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.35f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Exégesis Teológica:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ShieldGreenDark
                            )
                            Text(
                                text = spot.theologicalExegesis,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF2E241E),
                                lineHeight = 19.sp
                            )
                        }
                    }

                    // CITA PATRÍSTICA
                    Card(
                        colors = CardDefaults.cardColors(containerColor = ShieldGreenDark.copy(alpha = 0.08f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = spot.patristicQuote,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF1B3B2B),
                                fontFamily = FontFamily.Serif,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                            Text(
                                text = "— ${spot.patristicSource}",
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
