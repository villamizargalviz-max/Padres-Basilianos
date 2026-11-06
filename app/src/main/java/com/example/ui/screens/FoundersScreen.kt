package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Founder
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

@Composable
fun FoundersScreen(
    viewModel: BasilianViewModel,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val strings = remember(appLanguage) { com.example.ui.i18n.AppStrings.get(appLanguage) }
    val founders = viewModel.foundersList
    val selectedFounder by viewModel.selectedFounder.collectAsState()
    val isSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val customPhotos by viewModel.customPhotos.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var photoTargetFounderKey by remember { mutableStateOf<String?>(null) }

    val founderPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        val targetKey = photoTargetFounderKey
        if (uri != null && targetKey != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            viewModel.setCustomPhoto(targetKey, uri)
        }
    }

    val filteredFounders = remember(searchQuery, founders) {
        founders.filter { founder ->
            founder.name.contains(searchQuery, ignoreCase = true) ||
                    founder.role.contains(searchQuery, ignoreCase = true) ||
                    founder.title.contains(searchQuery, ignoreCase = true) ||
                    founder.biography.contains(searchQuery, ignoreCase = true)
        }
    }

    val foundersBadgeLabel = when (appLanguage) {
        com.example.data.model.AppLanguage.EN -> "The 10 Founding Priests (Annonay, 1822) • ${founders.size} Founders"
        com.example.data.model.AppLanguage.FR -> "Les 10 Prêtres Fondateurs (Annonay, 1822) • ${founders.size} Fondateurs"
        else -> "Los 10 Sacerdotes Fundadores (Annonay, 1822) • ${founders.size} Fundadores"
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF07152B)),
                border = BorderStroke(1.5.dp, Color(0xFF00F0FF).copy(alpha = 0.7f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF00F0FF).copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = strings.recursos.foundersHeaderTitle,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Historia, descripción espiritual y espacio para subir la fotografía de cada uno de los 10 Sacerdotes Fundadores (Annonay, 1822).",
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
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(strings.recursos.searchChaptersPlaceholder, fontSize = 13.sp) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = BasilianBlue)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = strings.recursos.closeBtn)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_founders_input")
            )
        }

        item {
            FilterChip(
                selected = true,
                onClick = { },
                label = { Text(foundersBadgeLabel, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
            )
        }

        items(filteredFounders, key = { it.id }) { rawFounder ->
            val founder = com.example.data.repository.BasilianContentLocalization.getLocalizedFounder(rawFounder, appLanguage)
            val photoKey = "founder_${rawFounder.name.hashCode()}"
            val customPhotoUri = customPhotos[photoKey]
                ?: customPhotos["founder_${founder.name.hashCode()}"]
                ?: customPhotos["pioneer_${rawFounder.name.hashCode()}"]
            FounderCard(
                founder = founder,
                appLanguage = appLanguage,
                customPhotoUri = customPhotoUri,
                onSelect = { viewModel.selectFounder(rawFounder) },
                onUploadPhoto = {
                    photoTargetFounderKey = photoKey
                    founderPhotoLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onListen = {
                    val text = "${founder.name}. ${founder.role}. ${founder.title}. ${founder.biography}. ${founder.keyContribution}. ${founder.quoteOrEpitaph}"
                    viewModel.speakText(text)
                }
            )
        }
    }

    // Modal de Detalle con Foto y Botón para Subir/Cambiar Foto
    selectedFounder?.let { rawFounder ->
        val founder = com.example.data.repository.BasilianContentLocalization.getLocalizedFounder(rawFounder, appLanguage)
        val photoKey = "founder_${rawFounder.name.hashCode()}"
        val customPhotoUri = customPhotos[photoKey]
            ?: customPhotos["founder_${founder.name.hashCode()}"]
            ?: customPhotos["pioneer_${rawFounder.name.hashCode()}"]

        val closeLabel = strings.recursos.closeBtn
        val listenLabel = when (appLanguage) {
            com.example.data.model.AppLanguage.EN -> if (isSpeaking) "Stop" else "Listen"
            com.example.data.model.AppLanguage.FR -> if (isSpeaking) "Arrêter" else "Écouter"
            else -> if (isSpeaking) "Detener" else "Escuchar"
        }
        val bioLabel = when (appLanguage) {
            com.example.data.model.AppLanguage.EN -> "Biography & History:"
            com.example.data.model.AppLanguage.FR -> "Biographie et Histoire:"
            else -> "Descripción e Historia:"
        }
        val contribLabel = when (appLanguage) {
            com.example.data.model.AppLanguage.EN -> "Key Contribution to Congregation:"
            com.example.data.model.AppLanguage.FR -> "Contribution Clé à la Congrégation:"
            else -> "Legado y Aporte a la Congregación:"
        }
        val virtueLabel = when (appLanguage) {
            com.example.data.model.AppLanguage.EN -> "Distinctive Spiritual Virtue:"
            com.example.data.model.AppLanguage.FR -> "Vertu Spirituelle Distinctive:"
            else -> "Virtud Espiritual Distintiva:"
        }
        val wordsLabel = when (appLanguage) {
            com.example.data.model.AppLanguage.EN -> "Memorable Words:"
            com.example.data.model.AppLanguage.FR -> "Mots mémorables:"
            else -> "Palabras memorables:"
        }

        AlertDialog(
            onDismissRequest = { viewModel.selectFounder(null) },
            confirmButton = {
                Button(
                    onClick = { viewModel.selectFounder(null) },
                    colors = ButtonDefaults.buttonColors(containerColor = BasilianBlue)
                ) {
                    Text(closeLabel)
                }
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            photoTargetFounderKey = photoKey
                            founderPhotoLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(if (customPhotoUri.isNullOrBlank()) "Subir Foto" else "Cambiar Foto")
                    }

                    OutlinedButton(
                        onClick = {
                            val text = "${founder.name}. ${founder.role}. ${founder.title}. Biografía: ${founder.biography}. Aporte: ${founder.keyContribution}. Frase: ${founder.quoteOrEpitaph}"
                            viewModel.speakText(text)
                        }
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(listenLabel)
                    }
                }
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(BasilianBlue.copy(alpha = 0.15f))
                            .border(1.5.dp, BasilianGoldDark, RoundedCornerShape(14.dp))
                            .clickable {
                                photoTargetFounderKey = photoKey
                                founderPhotoLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (!customPhotoUri.isNullOrBlank()) {
                            AsyncImage(
                                model = customPhotoUri,
                                contentDescription = founder.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Subir foto",
                                tint = BasilianBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = founder.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BasilianNavy
                        )
                        Text(
                            text = "${founder.years} • ${founder.role}",
                            style = MaterialTheme.typography.bodySmall,
                            color = BasilianCrimson,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Surface(
                        color = BasilianBlue.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = founder.title,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = BasilianBlue,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = bioLabel,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = BasilianNavy
                    )
                    Text(
                        text = founder.biography,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = contribLabel,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = BasilianGoldDark
                    )
                    Text(
                        text = founder.keyContribution,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = virtueLabel,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = BasilianCrimson
                    )
                    Text(
                        text = founder.spiritualVirtue,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = wordsLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BasilianNavy
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "«${founder.quoteOrEpitaph}»",
                                style = MaterialTheme.typography.bodySmall,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun FounderCard(
    founder: Founder,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES,
    customPhotoUri: String? = null,
    onSelect: () -> Unit,
    onUploadPhoto: () -> Unit,
    onListen: () -> Unit
) {
    val virtueLabel = when (appLanguage) {
        com.example.data.model.AppLanguage.EN -> "Virtue: ${founder.spiritualVirtue}"
        com.example.data.model.AppLanguage.FR -> "Vertu : ${founder.spiritualVirtue}"
        else -> "Virtud: ${founder.spiritualVirtue}"
    }
    val listenTooltip = when (appLanguage) {
        com.example.data.model.AppLanguage.EN -> "Listen to biography"
        com.example.data.model.AppLanguage.FR -> "Écouter la biographie"
        else -> "Escuchar semblanza"
    }

    val accentColor = if (founder.isFrenchFounder) Color(0xFF00F0FF) else Color(0xFFF59E0B)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .testTag("founder_card_${founder.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF07152B)),
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(1.2.dp, accentColor.copy(alpha = 0.65f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Espacio de Fotografía del Fundador (tocar para subir o cambiar foto)
                Box(
                    modifier = Modifier
                        .size(78.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.radialGradient(
                                listOf(accentColor.copy(alpha = 0.28f), Color(0xFF0F172A))
                            )
                        )
                        .border(1.5.dp, accentColor, RoundedCornerShape(16.dp))
                        .clickable { onUploadPhoto() }
                        .testTag("btn_upload_founder_photo_${founder.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    if (!customPhotoUri.isNullOrBlank()) {
                        AsyncImage(
                            model = customPhotoUri,
                            contentDescription = founder.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Subir foto",
                                tint = accentColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Subir foto",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = accentColor.copy(alpha = 0.16f),
                        border = BorderStroke(0.8.dp, accentColor.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = "FUNDADOR C.S.B. • ANNONAY 1822",
                            color = accentColor,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = founder.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "${founder.years} • ${founder.role}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFBBF24),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                IconButton(
                    onClick = onListen,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = listenTooltip,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Título espiritual/histórico
            Text(
                text = founder.title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFBAE6FD)
            )

            // Descripción biográfica e histórica completa (igual que en Nuestros Patronos)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.05f),
                border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.12f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(11.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = founder.biography,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.93f),
                        lineHeight = 18.sp
                    )
                    Text(
                        text = "✨ Legado: ${founder.keyContribution}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFDE68A),
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp
                    )
                    Text(
                        text = "«${founder.quoteOrEpitaph}»",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF93C5FD),
                        fontSize = 11.sp
                    )
                }
            }

            // Fila inferior: Virtud + Botón directo para subir/cambiar foto
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFFF59E0B).copy(alpha = 0.16f),
                    border = BorderStroke(0.8.dp, Color(0xFFF59E0B).copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = virtueLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFFDE68A),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onUploadPhoto,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, accentColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (customPhotoUri.isNullOrBlank()) "Subir foto" else "Cambiar foto",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

