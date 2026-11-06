package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppTab

@Composable
fun BasilianNavBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier,
    appTheme: BasilianAppTheme = BasilianAppTheme.ESCUDO_BASILIANO,
    appLanguage: AppLanguage = AppLanguage.ES,
    showOracion: Boolean = true,
    showLiturgia: Boolean = true,
    showCumpleanos: Boolean = true,
    showHistoria: Boolean = true,
    showRecursos: Boolean = true,
    isBasilianAccess: Boolean = true
) {
    val strings = AppStrings.get(appLanguage)
    val intensity3D = LocalFuturistic3DIntensity.current
    val holoCyan = Color(0xFF00F0FF)
    val holoGold = Color(0xFFF59E0B)

    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = holoCyan,
        selectedTextColor = holoCyan,
        indicatorColor = holoCyan.copy(alpha = 0.22f),
        unselectedIconColor = Color(0xFF94A3B8),
        unselectedTextColor = Color(0xFFCBD5E1)
    )

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                cameraDistance = 16f * density
                rotationX = 3.2f * intensity3D.tiltFactor
                shadowElevation = 24f
            }
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF07162C).copy(alpha = 0.96f),
                        Color(0xFF020813).copy(alpha = 0.99f)
                    )
                )
            )
            .drawWithContent {
                drawContent()
                // Arista superior 3D con brillo holográfico cian y oro
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            holoCyan.copy(alpha = 0.85f),
                            holoGold.copy(alpha = 0.95f),
                            holoCyan.copy(alpha = 0.85f)
                        )
                    ),
                    start = Offset(0f, 1.dp.toPx()),
                    end = Offset(size.width, 1.dp.toPx()),
                    strokeWidth = 2.2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
            .testTag("basilian_bottom_nav"),
        containerColor = Color.Transparent,
        tonalElevation = 12.dp
    ) {
        // 1. Oración Diaria y Meditación
        if (showOracion) {
            val isOracion = currentTab == AppTab.ORACION_DIARIA || currentTab == AppTab.REZANDO_HOY || currentTab == AppTab.INICIO
            val scale3d by animateFloatAsState(if (isOracion) 1.14f else 1f, tween(250), label = "oracion_3d")
            NavigationBarItem(
                selected = isOracion,
                onClick = { onTabSelected(AppTab.ORACION_DIARIA) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Church,
                        contentDescription = strings.nav.oracion,
                        modifier = Modifier
                            .size(22.dp)
                            .graphicsLayer {
                                scaleX = scale3d
                                scaleY = scale3d
                                rotationY = if (isOracion) -8f else 0f
                            }
                    )
                },
                label = {
                    Text(
                        text = strings.nav.oracion,
                        fontSize = 11.sp,
                        fontWeight = if (isOracion) FontWeight.ExtraBold else FontWeight.Medium
                    )
                },
                colors = itemColors,
                modifier = Modifier.testTag("nav_tab_oracion")
            )
        }

        // 2. Red Basiliana para la Educación y Mapa de Obras
        if (showLiturgia) {
            val isLiturgia = currentTab == AppTab.LITURGIA_HORAS || currentTab == AppTab.COMUNIDAD
            val scale3d by animateFloatAsState(if (isLiturgia) 1.14f else 1f, tween(250), label = "liturgia_3d")
            NavigationBarItem(
                selected = isLiturgia,
                onClick = { onTabSelected(AppTab.LITURGIA_HORAS) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = strings.nav.liturgia,
                        modifier = Modifier
                            .size(22.dp)
                            .graphicsLayer {
                                scaleX = scale3d
                                scaleY = scale3d
                                rotationY = if (isLiturgia) -8f else 0f
                            }
                    )
                },
                label = {
                    Text(
                        text = strings.nav.liturgia,
                        fontSize = 11.sp,
                        fontWeight = if (isLiturgia) FontWeight.ExtraBold else FontWeight.Medium
                    )
                },
                colors = itemColors,
                modifier = Modifier.testTag("nav_tab_liturgia")
            )
        }

        // 3. Cumpleaños & Directorio (Basilianos) / Liturgia & Ordo Colombiano (No Basilianos)
        if (showCumpleanos) {
            val isCumpleanos = currentTab == AppTab.DIRECTORIO
            val scale3d by animateFloatAsState(if (isCumpleanos) 1.14f else 1f, tween(250), label = "cumple_3d")
            val tabLabel = if (isBasilianAccess) {
                strings.nav.cumpleanos
            } else {
                when (appLanguage) {
                    AppLanguage.EN -> "Liturgy / Ordo"
                    AppLanguage.FR -> "Liturgie / Ordo"
                    AppLanguage.ES -> "Liturgia / Ordo"
                }
            }
            NavigationBarItem(
                selected = isCumpleanos,
                onClick = { onTabSelected(AppTab.DIRECTORIO) },
                icon = {
                    Icon(
                        imageVector = if (isBasilianAccess) Icons.Default.Cake else Icons.Default.AutoStories,
                        contentDescription = tabLabel,
                        modifier = Modifier
                            .size(22.dp)
                            .graphicsLayer {
                                scaleX = scale3d
                                scaleY = scale3d
                                rotationY = if (isCumpleanos) -8f else 0f
                            }
                    )
                },
                label = {
                    Text(
                        text = tabLabel,
                        fontSize = 11.sp,
                        fontWeight = if (isCumpleanos) FontWeight.ExtraBold else FontWeight.Medium
                    )
                },
                colors = itemColors,
                modifier = Modifier.testTag("nav_tab_cumpleanos")
            )
        }

        // 4. Historia y Taller Creativo J.A.R.V.I.S. 3D
        if (showHistoria) {
            val isHistoriaIa = currentTab == AppTab.HISTORIA_IA
            val scale3d by animateFloatAsState(if (isHistoriaIa) 1.18f else 1f, tween(250), label = "historia_3d")
            NavigationBarItem(
                selected = isHistoriaIa,
                onClick = { onTabSelected(AppTab.HISTORIA_IA) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = strings.nav.historiaIa,
                        modifier = Modifier
                            .size(22.dp)
                            .graphicsLayer {
                                scaleX = scale3d
                                scaleY = scale3d
                                rotationY = if (isHistoriaIa) 10f else 0f
                            }
                    )
                },
                label = {
                    Text(
                        text = strings.nav.historiaIa,
                        fontSize = 11.sp,
                        fontWeight = if (isHistoriaIa) FontWeight.ExtraBold else FontWeight.Medium
                    )
                },
                colors = itemColors,
                modifier = Modifier.testTag("nav_tab_historia_ia")
            )
        }

        // 5. Recursos y Biblioteca Sagrada
        if (showRecursos) {
            val isRecursos = currentTab == AppTab.RECURSOS ||
                    currentTab == AppTab.HISTORIA_Y_COMUNIDAD ||
                    currentTab == AppTab.HISTORIA ||
                    currentTab == AppTab.CALENDARIO ||
                    currentTab == AppTab.FUNDADORES ||
                    currentTab == AppTab.ORACIONES ||
                    currentTab == AppTab.ORACION ||
                    currentTab == AppTab.PATRONOS ||
                    currentTab == AppTab.LIBRO_1922 ||
                    currentTab == AppTab.FORMA_DE_VIDA ||
                    currentTab == AppTab.COSTUMBRES ||
                    currentTab == AppTab.POLITICAS_2026 ||
                    currentTab == AppTab.CUADERNO ||
                    currentTab == AppTab.ACTUALIDAD ||
                    currentTab == AppTab.RUTAS_APRENDIZAJE

            val scale3d by animateFloatAsState(if (isRecursos) 1.14f else 1f, tween(250), label = "recursos_3d")
            NavigationBarItem(
                selected = isRecursos,
                onClick = { onTabSelected(AppTab.RECURSOS) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = strings.nav.recursos,
                        modifier = Modifier
                            .size(22.dp)
                            .graphicsLayer {
                                scaleX = scale3d
                                scaleY = scale3d
                                rotationY = if (isRecursos) -8f else 0f
                            }
                    )
                },
                label = {
                    Text(
                        text = strings.nav.recursos,
                        fontSize = 11.sp,
                        fontWeight = if (isRecursos) FontWeight.ExtraBold else FontWeight.Medium
                    )
                },
                colors = itemColors,
                modifier = Modifier.testTag("nav_tab_recursos")
            )
        }
    }
}
