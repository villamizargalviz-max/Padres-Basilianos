package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasilianTopBar(
    currentTab: AppTab,
    isSpeaking: Boolean,
    onStopAudio: () -> Unit,
    onReturnHome: () -> Unit = {},
    onOpenCuriaPortal: () -> Unit = {},
    onOpenThemeSelector: () -> Unit = {},
    onOpenLanguageSelector: () -> Unit = {},
    onOpenUserAccount: () -> Unit = {},
    onCycle3DIntensity: () -> Unit = {},
    onLogoutUser: () -> Unit = {},
    userName: String = "",
    userEmail: String = "",
    userPhotoUri: String? = null,
    appTheme: BasilianAppTheme = BasilianAppTheme.ESCUDO_BASILIANO,
    appLanguage: AppLanguage = AppLanguage.ES,
    isBasilianAccess: Boolean = true,
    modifier: Modifier = Modifier
) {
    val strings = AppStrings.get(appLanguage)
    val intensity3D = LocalFuturistic3DIntensity.current

    val infiniteTransition = rememberInfiniteTransition(label = "top_bar_crest")
    val crestTilt by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "crest_tilt"
    )

    val title = when (currentTab) {
        AppTab.ORACION_DIARIA, AppTab.REZANDO_HOY, AppTab.INICIO -> strings.topBar.titleOracion
        AppTab.DIRECTORIO -> strings.topBar.titleDirectorio
        AppTab.LITURGIA_HORAS, AppTab.COMUNIDAD -> strings.topBar.titleLiturgia
        AppTab.HISTORIA_IA -> strings.topBar.titleHistoriaIa
        AppTab.PORTAL_CURIA -> strings.topBar.titleCuria
        else -> strings.topBar.titleRecursos
    }

    // Subtítulo limpio dividido según la sección actual y el idioma activo
    val sectionLabel = when (currentTab) {
        AppTab.ORACION_DIARIA, AppTab.REZANDO_HOY, AppTab.INICIO -> when (appLanguage) {
            AppLanguage.EN -> "Prayer"
            AppLanguage.FR -> "Prière"
            AppLanguage.ES -> "Oración"
        }
        AppTab.DIRECTORIO -> when (appLanguage) {
            AppLanguage.EN -> "Birthdays & Community"
            AppLanguage.FR -> "Anniversaires & Communauté"
            AppLanguage.ES -> "Cumpleaños y Comunidad"
        }
        AppTab.LITURGIA_HORAS, AppTab.COMUNIDAD -> strings.topBar.titleLiturgia
        AppTab.HISTORIA_IA -> when (appLanguage) {
            AppLanguage.EN -> "AI Conversation & History"
            AppLanguage.FR -> "Conversation & Histoire IA"
            AppLanguage.ES -> "Conversación e Historia IA"
        }
        AppTab.PORTAL_CURIA -> when (appLanguage) {
            AppLanguage.EN -> "General Curia"
            AppLanguage.FR -> "Curie Générale"
            AppLanguage.ES -> "Curia General"
        }
        else -> when (appLanguage) {
            AppLanguage.EN -> "Resources & Patron Saints"
            AppLanguage.FR -> "Ressources & Saints Patrons"
            AppLanguage.ES -> "Recursos y Nuestros Patronos"
        }
    }

    val worldWorksSuffix = when (appLanguage) {
        AppLanguage.EN -> "Global Works & Schools"
        AppLanguage.FR -> "Œuvres & Écoles dans le Monde"
        AppLanguage.ES -> "Obras en el Mundo"
    }

    val subtitle = when (currentTab) {
        AppTab.ORACION_DIARIA, AppTab.REZANDO_HOY, AppTab.INICIO ->
            if (userName.isNotBlank()) "$sectionLabel • $userName • ${strings.topBar.subtitleOracion}" else "$sectionLabel • ${strings.topBar.subtitleOracion}"
        AppTab.DIRECTORIO -> "$sectionLabel • ${strings.topBar.subtitleDirectorio}"
        AppTab.LITURGIA_HORAS, AppTab.COMUNIDAD -> "$sectionLabel • $worldWorksSuffix"
        AppTab.HISTORIA_IA -> if (userName.isNotBlank()) "$sectionLabel • $userName" else strings.topBar.subtitleHistoriaIa
        AppTab.PORTAL_CURIA -> strings.topBar.subtitleCuria
        else -> "$sectionLabel • ${strings.topBar.subtitleRecursos}"
    }

    val holoCyan = Color(0xFF00F0FF)
    val holoGold = Color(0xFFF59E0B)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF030B1A),
                        if (currentTab == AppTab.HISTORIA_IA) Color(0xFF072B47) else appTheme.primaryColor.copy(alpha = 0.92f),
                        Color(0xFF06162E)
                    )
                )
            )
            .drawWithContent {
                drawContent()
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            holoCyan.copy(alpha = 0.85f),
                            holoGold.copy(alpha = 0.95f),
                            holoCyan.copy(alpha = 0.85f)
                        )
                    ),
                    start = Offset(0f, size.height - 1.5.dp.toPx()),
                    end = Offset(size.width, size.height - 1.5.dp.toPx()),
                    strokeWidth = 2.2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 9.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        onClickLabel = "Volver a Oración e Inicio",
                        onClick = onReturnHome
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                cameraDistance = 12f * density
                                rotationY = crestTilt * intensity3D.tiltFactor
                            }
                    ) {
                        BasilianCrestView(
                            size = 38.dp,
                            showMottoText = false,
                            onClick = onReturnHome
                        )
                    }
                    // Etiqueta debajo del escudo según la sección donde estamos e idioma
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = holoGold.copy(alpha = 0.20f),
                        border = BorderStroke(0.8.dp, holoGold.copy(alpha = 0.7f))
                    ) {
                        Text(
                            text = when (currentTab) {
                                AppTab.ORACION_DIARIA, AppTab.REZANDO_HOY, AppTab.INICIO -> when (appLanguage) {
                                    AppLanguage.EN -> "PRAYER"
                                    AppLanguage.FR -> "PRIÈRE"
                                    AppLanguage.ES -> "ORACIÓN"
                                }
                                AppTab.LITURGIA_HORAS, AppTab.COMUNIDAD -> when (appLanguage) {
                                    AppLanguage.EN -> "EDUCATION"
                                    AppLanguage.FR -> "ÉDUCATION"
                                    AppLanguage.ES -> "EDUCACIÓN"
                                }
                                AppTab.DIRECTORIO -> when (appLanguage) {
                                    AppLanguage.EN -> "COMMUNITY"
                                    AppLanguage.FR -> "COMMUNAUTÉ"
                                    AppLanguage.ES -> "COMUNIDAD"
                                }
                                AppTab.HISTORIA_IA -> when (appLanguage) {
                                    AppLanguage.EN -> "AI HISTORY"
                                    AppLanguage.FR -> "HISTOIRE IA"
                                    AppLanguage.ES -> "HISTORIA IA"
                                }
                                AppTab.PORTAL_CURIA -> "CURIA"
                                else -> when (appLanguage) {
                                    AppLanguage.EN -> "RESOURCES"
                                    AppLanguage.FR -> "RESSOURCES"
                                    AppLanguage.ES -> "RECURSOS"
                                }
                            },
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = holoGold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = holoCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (isSpeaking) {
                    IconButton(
                        onClick = onStopAudio,
                        modifier = Modifier
                            .background(BasilianCrimson, RoundedCornerShape(12.dp))
                            .size(36.dp)
                            .testTag("stop_audio_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeOff,
                            contentDescription = "Detener lectura",
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                } else {
                    // Botón de Cuenta y Correo Vinculado (muestra la foto subida por el usuario o ícono de perfil)
                    IconButton(
                        onClick = onOpenUserAccount,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("top_bar_user_account_button")
                    ) {
                        if (!userPhotoUri.isNullOrBlank()) {
                            AsyncImage(
                                model = userPhotoUri,
                                contentDescription = "Mi Foto de Perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, holoGold, CircleShape)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Mi Perfil y Cuenta",
                                tint = holoCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Botón de Selección de Idioma
                    IconButton(
                        onClick = onOpenLanguageSelector,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("top_bar_language_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = strings.topBar.changeLanguageTooltip,
                            tint = holoGold,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    // Botón de Selección de Tema Visual
                    IconButton(
                        onClick = onOpenThemeSelector,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("top_bar_theme_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = strings.topBar.changeThemeTooltip,
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    if (isBasilianAccess &&
                        com.example.data.security.BasilianSecurityManager.isLeaderEmailAddress(userEmail) &&
                        currentTab != AppTab.PORTAL_CURIA
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = holoGold.copy(alpha = 0.22f),
                            border = BorderStroke(1.2.dp, holoGold),
                            modifier = Modifier.padding(start = 1.dp)
                        ) {
                            IconButton(
                                onClick = onOpenCuriaPortal,
                                modifier = Modifier
                                    .size(32.dp)
                                    .testTag("top_bar_portal_key")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VpnKey,
                                    contentDescription = "Portal de Curia Privada",
                                    tint = holoGold,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
