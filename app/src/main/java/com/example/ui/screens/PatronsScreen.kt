package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.PatronSaint
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel
import kotlin.math.cos
import kotlin.math.sin

// Paleta Tecnológica HUD estilo Reactor Arc (Iron Man Tech)
private val HudObsidianBg = Color(0xFF030914)
private val HudDeepNavy = Color(0xFF08172E)
private val HudArcCyan = Color(0xFF00F0FF)
private val HudArcBlue = Color(0xFF38BDF8)
private val HudTitaniumGold = Color(0xFFF59E0B)
private val HudWarmGold = Color(0xFFFDE047)
private val HudCrimsonRed = Color(0xFFEF4444)

@Composable
fun PatronsScreen(
    viewModel: BasilianViewModel,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val strings = remember(appLanguage) { com.example.ui.i18n.AppStrings.get(appLanguage) }
    val basePatrons = viewModel.patronsList
    val selectedPatron by viewModel.selectedPatron.collectAsState()
    val isSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val customPhotos by viewModel.customPhotos.collectAsState()

    // Permitir organizar el orden de los patronos en cualquier celular
    var orderedIds by remember(basePatrons) { mutableStateOf(basePatrons.map { it.id }) }
    val patrons = remember(orderedIds, basePatrons) {
        val byId = basePatrons.associateBy { it.id }
        orderedIds.mapNotNull { byId[it] } + basePatrons.filter { it.id !in orderedIds }
    }

    var filterMode by remember { mutableStateOf("TODOS") } // "TODOS", "OFICIALES", "LATAM"
    var pendingPhotoPatronKey by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        val key = pendingPhotoPatronKey
        if (uri != null && key != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            viewModel.setCustomPhoto(key, uri)
        }
        pendingPhotoPatronKey = null
    }

    val filteredPatrons = remember(filterMode, patrons) {
        when (filterMode) {
            "OFICIALES" -> patrons.filter { it.id != "oscar_romero" }
            "LATAM" -> patrons.filter { it.id == "oscar_romero" }
            else -> patrons
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "hud_arc_reactor")
    val reactorAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "reactor_rotation"
    )
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "reactor_pulse"
    )

    val allLabel = when (appLanguage) {
        com.example.data.model.AppLanguage.EN -> "All Patrons (${patrons.size})"
        com.example.data.model.AppLanguage.FR -> "Tous (${patrons.size})"
        else -> "Todos (${patrons.size})"
    }
    val officialLabel = when (appLanguage) {
        com.example.data.model.AppLanguage.EN -> "6 Official Patrons"
        com.example.data.model.AppLanguage.FR -> "6 Patrons Officiels"
        else -> "6 Patronos Oficiales"
    }
    val latamLabel = when (appLanguage) {
        com.example.data.model.AppLanguage.EN -> "LatAm Patron"
        com.example.data.model.AppLanguage.FR -> "Patron LatAm"
        else -> "Patrono LatAm"
    }

    // Todo el contenido dentro de un único LazyColumn para que se vea y desplace completo en CUALQUIER celular
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        HudObsidianBg,
                        HudDeepNavy,
                        Color(0xFF050E1D)
                    )
                )
            ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = HudArcCyan),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF071428)),
                border = BorderStroke(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(
                        listOf(HudArcCyan, HudTitaniumGold, HudCrimsonRed, HudArcCyan)
                    )
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(44.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val maxR = size.minDimension / 2f
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        HudArcCyan.copy(alpha = 0.55f * pulseGlow),
                                        HudArcBlue.copy(alpha = 0.2f),
                                        Color.Transparent
                                    )
                                ),
                                radius = maxR
                            )
                            drawCircle(
                                color = HudArcCyan,
                                radius = maxR * 0.88f,
                                style = Stroke(width = 2.dp.toPx())
                            )
                            drawCircle(
                                color = HudTitaniumGold,
                                radius = maxR * 0.62f,
                                style = Stroke(width = 1.5.dp.toPx())
                            )
                            drawCircle(
                                color = Color.White,
                                radius = maxR * 0.26f * pulseGlow
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NUESTROS PATRONOS C.S.B. (${patrons.size})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Lee la historia y descripción completa de cada patrono, sube su fotografía y organízalos (↑/↓) en cualquier celular.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFBAE6FD),
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    "TODOS" to allLabel,
                    "OFICIALES" to officialLabel,
                    "LATAM" to latamLabel
                ).forEach { (key, label) ->
                    val selected = filterMode == key
                    Surface(
                        onClick = { filterMode = key },
                        shape = RoundedCornerShape(10.dp),
                        color = if (selected) HudArcCyan.copy(alpha = 0.22f) else Color(0xFF0A192F),
                        border = BorderStroke(
                            width = if (selected) 1.5.dp else 1.dp,
                            color = if (selected) HudArcCyan else HudTitaniumGold.copy(alpha = 0.35f)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(vertical = 7.dp, horizontal = 4.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.5.sp,
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
                                color = if (selected) HudWarmGold else Color.White.copy(alpha = 0.85f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        items(filteredPatrons.size, key = { idx -> filteredPatrons[idx].id }) { index ->
            val rawPatron = filteredPatrons[index]
            val patron = com.example.data.repository.BasilianContentLocalization.getLocalizedPatron(rawPatron, appLanguage)
            val photoKey = "patron_${rawPatron.id}"
            val legacyKey = "patron_${patron.name.hashCode()}"
            val customPhotoUri = customPhotos[photoKey] ?: customPhotos[legacyKey]

            PatronHudCard(
                patron = patron,
                orderPosition = index + 1,
                totalCount = filteredPatrons.size,
                customPhotoUri = customPhotoUri,
                reactorAngle = reactorAngle,
                pulseGlow = pulseGlow,
                appLanguage = appLanguage,
                onSelect = { viewModel.selectPatron(rawPatron) },
                onMoveUp = {
                    val currentIdx = orderedIds.indexOf(rawPatron.id)
                    if (currentIdx > 0) {
                        val mutable = orderedIds.toMutableList()
                        val item = mutable.removeAt(currentIdx)
                        mutable.add(currentIdx - 1, item)
                        orderedIds = mutable
                    }
                },
                onMoveDown = {
                    val currentIdx = orderedIds.indexOf(rawPatron.id)
                    if (currentIdx >= 0 && currentIdx < orderedIds.lastIndex) {
                        val mutable = orderedIds.toMutableList()
                        val item = mutable.removeAt(currentIdx)
                        mutable.add(currentIdx + 1, item)
                        orderedIds = mutable
                    }
                },
                onUploadPhoto = {
                    pendingPhotoPatronKey = photoKey
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onRemovePhoto = {
                    viewModel.removeCustomPhoto(photoKey)
                    viewModel.removeCustomPhoto(legacyKey)
                },
                onListen = {
                    val text = "${patron.name}. ${patron.title}. Fiesta: ${patron.feastDay}. ${patron.biography}. Vínculo con los Basilianos: ${patron.connectionToBasilians}. Oración: ${patron.patronPrayer}"
                    viewModel.speakText(text)
                }
            )
        }
    }

    // Modal Holográfico Detallado del Patrono
    selectedPatron?.let { rawPatron ->
        val patron = com.example.data.repository.BasilianContentLocalization.getLocalizedPatron(rawPatron, appLanguage)
        val photoKey = "patron_${rawPatron.id}"
        val legacyKey = "patron_${patron.name.hashCode()}"
        val customPhotoUri = customPhotos[photoKey] ?: customPhotos[legacyKey]
        val closeLabel = strings.recursos.closeBtn
        val listenLabel = when (appLanguage) {
            com.example.data.model.AppLanguage.EN -> if (isSpeaking) "Stop Audio" else "Listen Voice"
            com.example.data.model.AppLanguage.FR -> if (isSpeaking) "Arrêter" else "Écouter"
            else -> if (isSpeaking) "Detener Voz" else "Escuchar con Voz Humana"
        }

        AlertDialog(
            onDismissRequest = { viewModel.selectPatron(null) },
            containerColor = Color(0xFF061326),
            tonalElevation = 12.dp,
            confirmButton = {
                Button(
                    onClick = { viewModel.selectPatron(null) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HudTitaniumGold,
                        contentColor = Color(0xFF030914)
                    )
                ) {
                    Text(closeLabel, fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        if (isSpeaking) {
                            viewModel.stopSpeaking()
                        } else {
                            val text = "${patron.name}. ${patron.title}. Fiesta: ${patron.feastDay}. ${patron.biography}. Vínculo con los Basilianos: ${patron.connectionToBasilians}. Oración: ${patron.patronPrayer}"
                            viewModel.speakText(text)
                        }
                    },
                    border = BorderStroke(1.dp, HudArcCyan),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HudArcCyan)
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(listenLabel, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .border(2.dp, HudArcCyan, CircleShape)
                            .background(Color(0xFF0D2240))
                            .clickable {
                                pendingPhotoPatronKey = photoKey
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (!customPhotoUri.isNullOrBlank()) {
                            AsyncImage(
                                model = customPhotoUri,
                                contentDescription = patron.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().clip(CircleShape)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = HudWarmGold,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = patron.name.uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = patron.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = HudWarmGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (!customPhotoUri.isNullOrBlank()) {
                        AsyncImage(
                            model = customPhotoUri,
                            contentDescription = patron.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.5.dp, HudArcCyan, RoundedCornerShape(14.dp))
                        )
                    }

                    Button(
                        onClick = {
                            pendingPhotoPatronKey = photoKey
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HudArcCyan.copy(alpha = 0.2f),
                            contentColor = HudArcCyan
                        ),
                        border = BorderStroke(1.dp, HudArcCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (customPhotoUri.isNullOrBlank()) "Subir Foto de ${patron.name}" else "Cambiar Foto de ${patron.name}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Surface(
                        color = HudTitaniumGold.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, HudTitaniumGold.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = null,
                                tint = HudWarmGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Fiesta Litúrgica: ${patron.feastDay}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = HudWarmGold
                            )
                        }
                    }

                    Text(
                        text = "⚡ Biografía & Descripción:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = HudArcCyan
                    )
                    Text(
                        text = patron.biography,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.92f),
                        lineHeight = 18.sp
                    )

                    Text(
                        text = "🛡️ Vínculo con los Padres Basilianos:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = HudWarmGold
                    )
                    Text(
                        text = patron.connectionToBasilians,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFBAE6FD),
                        lineHeight = 18.sp
                    )

                    Text(
                        text = "🔥 Virtud para tu día a día:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFF87171)
                    )
                    Text(
                        text = patron.virtueForDailyLife,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 18.sp
                    )

                    Surface(
                        color = Color(0xFF0A1E3C),
                        border = BorderStroke(1.dp, HudArcCyan.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "🙏 Oración Patronal:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = HudArcCyan
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "«${patron.patronPrayer}»",
                                style = MaterialTheme.typography.bodySmall,
                                fontStyle = FontStyle.Italic,
                                lineHeight = 18.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun PatronHudCard(
    patron: PatronSaint,
    orderPosition: Int = 1,
    totalCount: Int = 7,
    customPhotoUri: String? = null,
    reactorAngle: Float,
    pulseGlow: Float,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES,
    onSelect: () -> Unit,
    onMoveUp: () -> Unit = {},
    onMoveDown: () -> Unit = {},
    onUploadPhoto: () -> Unit,
    onRemovePhoto: () -> Unit,
    onListen: () -> Unit
) {
    val accentColor = when (patron.id) {
        "san_basilio" -> HudWarmGold
        "virgen_maria" -> HudArcCyan
        "san_jose" -> Color(0xFFFBBF24)
        "san_francisco" -> Color(0xFF34D399)
        "santo_tomas" -> Color(0xFF60A5FA)
        "san_juan_bosco" -> Color(0xFFFB923C)
        else -> Color(0xFFC084FC)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(22.dp), spotColor = accentColor)
            .clickable(onClick = onSelect)
            .testTag("patron_card_${patron.id}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF071326)),
        border = BorderStroke(
            width = 1.5.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    accentColor,
                    HudArcCyan.copy(alpha = 0.65f),
                    HudTitaniumGold.copy(alpha = 0.8f)
                )
            )
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Si el usuario subió una fotografía para este patrono, mostrarla en formato panorámico amplio y nítido
                if (!customPhotoUri.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.5.dp, accentColor, RoundedCornerShape(16.dp))
                            .clickable(onClick = onUploadPhoto)
                    ) {
                        AsyncImage(
                            model = customPhotoUri,
                            contentDescription = patron.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Surface(
                            color = Color(0xFF030914).copy(alpha = 0.78f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, accentColor),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "📸 Toca para cambiar foto",
                                color = HudWarmGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Fila superior: Retrato Circular + Datos Principales + Controles de Orden y Audio
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clickable(onClick = onUploadPhoto),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val r = size.minDimension / 2f
                            drawCircle(
                                color = accentColor.copy(alpha = 0.85f),
                                radius = r * 0.96f,
                                style = Stroke(width = 2.dp.toPx())
                            )
                            drawCircle(
                                color = HudArcCyan.copy(alpha = 0.55f),
                                radius = r * 0.84f,
                                style = Stroke(width = 1.2.dp.toPx())
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF0C2142),
                            border = BorderStroke(1.5.dp, accentColor),
                            modifier = Modifier.size(54.dp)
                        ) {
                            if (!customPhotoUri.isNullOrBlank()) {
                                AsyncImage(
                                    model = customPhotoUri,
                                    contentDescription = patron.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = when (patron.iconName) {
                                            "menu_book" -> Icons.Default.MenuBook
                                            "auto_awesome" -> Icons.Default.AutoAwesome
                                            "handyman" -> Icons.Default.Handyman
                                            "park" -> Icons.Default.Eco
                                            "school" -> Icons.Default.School
                                            "groups" -> Icons.Default.Groups
                                            "local_florist" -> Icons.Default.LocalFlorist
                                            else -> Icons.Default.RecordVoiceOver
                                        },
                                        contentDescription = null,
                                        tint = accentColor,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = HudTitaniumGold,
                            border = BorderStroke(1.dp, Color(0xFF030914)),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(22.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AddAPhoto,
                                    contentDescription = "Subir foto",
                                    tint = Color(0xFF030914),
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = accentColor.copy(alpha = 0.18f),
                                border = BorderStroke(0.8.dp, accentColor)
                            ) {
                                Text(
                                    text = "#$orderPosition PATRONO",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = accentColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "📅 ${patron.feastDay}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = HudArcCyan,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = patron.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 15.5.sp
                        )

                        Text(
                            text = patron.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = HudWarmGold,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.2.sp,
                            lineHeight = 14.sp
                        )
                    }

                    // Controles para organizar orden (↑ / ↓) y escuchar audio
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (orderPosition > 1) {
                                Surface(
                                    onClick = onMoveUp,
                                    shape = CircleShape,
                                    color = Color(0xFF0F294A),
                                    border = BorderStroke(1.dp, HudWarmGold.copy(alpha = 0.7f)),
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowUp,
                                            contentDescription = "Subir posición",
                                            tint = HudWarmGold,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                            if (orderPosition < totalCount) {
                                Surface(
                                    onClick = onMoveDown,
                                    shape = CircleShape,
                                    color = Color(0xFF0F294A),
                                    border = BorderStroke(1.dp, HudWarmGold.copy(alpha = 0.7f)),
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Bajar posición",
                                            tint = HudWarmGold,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        IconButton(
                            onClick = onListen,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(HudArcCyan.copy(alpha = 0.14f))
                                .border(1.dp, HudArcCyan.copy(alpha = 0.6f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Escuchar descripción del patrono",
                                tint = HudArcCyan,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Panel Holográfico con la Descripción / Biografía Completa de cada Patrono
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF0A1D37),
                    border = BorderStroke(1.dp, HudArcCyan.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "⚡ HISTORIA Y DESCRIPCIÓN DEL PATRONO:",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = HudArcCyan,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = patron.biography,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.95f),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                        HorizontalDivider(color = HudArcCyan.copy(alpha = 0.2f), thickness = 0.8.dp)
                        Text(
                            text = "🛡️ Vínculo con los Padres Basilianos: ${patron.connectionToBasilians}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFBAE6FD),
                            fontSize = 11.2.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Barra de acciones adaptable: Subir/Cambiar Foto + Quitar Foto + Ver Oración e Historia
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onUploadPhoto,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HudTitaniumGold,
                            contentColor = Color(0xFF030914)
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 7.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("upload_patron_photo_${patron.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Subir foto del patrono",
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (customPhotoUri.isNullOrBlank()) "Subir Foto" else "Cambiar Foto",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1
                        )
                    }

                    if (!customPhotoUri.isNullOrBlank()) {
                        OutlinedButton(
                            onClick = onRemovePhoto,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, HudCrimsonRed),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF87171)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 7.dp),
                            modifier = Modifier.testTag("remove_patron_photo_${patron.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Quitar foto",
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onSelect,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, HudArcCyan),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = HudArcCyan),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 7.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Ver Todo",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
