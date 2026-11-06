package com.example.ui.components

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.ui.theme.BasilianAppTheme
import kotlin.math.cos
import kotlin.math.sin

import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp

enum class GlobalBgMode(val title: String, val emoji: String) {
    JARVIS_FUTURISTA("Holograma Futurista 3D", "✨"),
    ESPIRITU_SANTO("Espíritu Santo Celestial", "🕊️"),
    VITRAL_SACRO("Vitral Sacro Resplandeciente", "✨"),
    PERGAMINO_MONASTICO("Pergamino Cálido Monástico", "📜"),
    CALIZ_EUCARISTICO("Cáliz Dorado & Eucaristía", "🍷"),
    NOCHE_MISTICA("Vigilia & Noche Monástica", "🌌"),
    CUSTOM_IMAGE("Imagen Personalizada", "🖼️"),
    TEMATICO_PURO("Tema Dinámico", "🎨")
}

@Composable
fun HolySpiritGlobalBackground(
    mode: GlobalBgMode = GlobalBgMode.VITRAL_SACRO,
    customImageUri: String? = null,
    opacity: Float = 0.90f,
    appTheme: BasilianAppTheme = BasilianAppTheme.ESCUDO_BASILIANO,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        // 1. Capa de Fondo Dinámica y Rica (Nunca pantalla blanca fría)
        when (mode) {
            GlobalBgMode.CUSTOM_IMAGE -> {
                if (!customImageUri.isNullOrBlank()) {
                    AsyncImage(
                        model = Uri.parse(customImageUri),
                        contentDescription = "Fondo de la aplicación",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Velo cálido y suave para legibilidad de los textos
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                if (appTheme.isDark) Color(0xFF0F172A).copy(alpha = 1f - opacity.coerceIn(0.2f, 0.9f))
                                else Color(0xFFFFFDF8).copy(alpha = 1f - opacity.coerceIn(0.2f, 0.9f))
                            )
                    )
                } else {
                    VitralSacroCanvas(isDark = appTheme.isDark)
                }
            }
            GlobalBgMode.ESPIRITU_SANTO -> {
                HolySpiritCanvas(isDark = appTheme.isDark, primaryColor = appTheme.primaryColor)
            }
            GlobalBgMode.VITRAL_SACRO -> {
                VitralSacroCanvas(isDark = appTheme.isDark)
            }
            GlobalBgMode.PERGAMINO_MONASTICO -> {
                PergaminoMonasticoCanvas(isDark = appTheme.isDark)
            }
            GlobalBgMode.CALIZ_EUCARISTICO -> {
                CalizEucaristicoCanvas(isDark = appTheme.isDark)
            }
            GlobalBgMode.JARVIS_FUTURISTA -> {
                JarvisFuturistaCanvas(isActivePulse = true)
            }
            GlobalBgMode.NOCHE_MISTICA -> {
                NocheMisticaCanvas()
            }
            GlobalBgMode.TEMATICO_PURO -> {
                val bgGradient = if (appTheme.isDark) {
                    listOf(
                        appTheme.backgroundColor,
                        appTheme.primaryColor.copy(alpha = 0.35f),
                        Color(0xFFB45309).copy(alpha = 0.15f),
                        appTheme.surfaceColor
                    )
                } else {
                    listOf(
                        Color(0xFFFFFBEB),
                        appTheme.primaryColor.copy(alpha = 0.12f),
                        Color(0xFFFEF3C7),
                        Color(0xFFFDFBF7)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(bgGradient))
                )
            }
        }

        // 2. Capa Visual Decorativa Universal: Degradados Suaves & Patrón de Iconos de Espiritualidad Basiliana
        BasilianSpiritualDecorativePattern(
            isDark = appTheme.isDark,
            appTheme = appTheme
        )

        // 2.5. Capa Espacial 3D Futurista Universal: Retícula de perspectiva 3D y poliedros holográficos en rotación
        Futuristic3DSpatialOverlay(
            isDark = appTheme.isDark || mode == GlobalBgMode.JARVIS_FUTURISTA || mode == GlobalBgMode.NOCHE_MISTICA,
            accentColor = if (mode == GlobalBgMode.JARVIS_FUTURISTA) Color(0xFF00F0FF) else appTheme.primaryColor,
            goldColor = appTheme.goldColor
        )

        // 3. Contenido de la Pantalla encima del fondo
        content()
    }
}

/**
 * Fondo A: Espíritu Santo con rayos celestiales y paloma sagrada
 */
@Composable
fun HolySpiritCanvas(
    isDark: Boolean,
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    val topSky = if (isDark) Color(0xFF0F172A) else Color(0xFFFFFDF5)
    val goldenCenter = if (isDark) Color(0xFFD97706).copy(alpha = 0.45f) else Color(0xFFFDE68A).copy(alpha = 0.65f)
    val bottomEarth = if (isDark) Color(0xFF020617) else Color(0xFFFAF5EB)

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(topSky, goldenCenter, bottomEarth)
                )
            )
    ) {
        val width = size.width
        val height = size.height
        val centerX = width * 0.5f
        val centerY = height * 0.28f

        // A. Resplandor Celestial Central
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFDF7A).copy(alpha = if (isDark) 0.50f else 0.60f),
                    Color(0xFFF59E0B).copy(alpha = if (isDark) 0.25f else 0.35f),
                    Color.Transparent
                ),
                center = Offset(centerX, centerY),
                radius = width * 0.75f
            ),
            radius = width * 0.75f,
            center = Offset(centerX, centerY)
        )

        // B. Rayos Divinos Radiantes
        val rayCount = 20
        for (i in 0 until rayCount) {
            val angleDeg = (i * (360.0 / rayCount))
            val angleRad = Math.toRadians(angleDeg)
            val rayLength = width * 0.7f
            val endX = centerX + (rayLength * cos(angleRad)).toFloat()
            val endY = centerY + (rayLength * sin(angleRad)).toFloat()

            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFEF08A).copy(alpha = if (isDark) 0.45f else 0.40f),
                        Color(0xFFF59E0B).copy(alpha = if (isDark) 0.20f else 0.20f),
                        Color.Transparent
                    ),
                    start = Offset(centerX, centerY),
                    end = Offset(endX, endY)
                ),
                start = Offset(centerX, centerY),
                end = Offset(endX, endY),
                strokeWidth = if (i % 2 == 0) 4f else 2f,
                cap = StrokeCap.Round
            )
        }

        // C. Aureola Sagrada
        drawCircle(
            color = Color(0xFFD4AF37).copy(alpha = if (isDark) 0.6f else 0.55f),
            radius = width * 0.14f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 3f)
        )
        drawCircle(
            color = Color(0xFFFEF3C7).copy(alpha = if (isDark) 0.4f else 0.35f),
            radius = width * 0.17f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 1.5f)
        )

        // D. Silueta Artística de la Paloma
        drawHolyDove(
            centerX = centerX,
            centerY = centerY,
            scale = width * 0.0017f,
            isDark = isDark
        )
    }
}

/**
 * Fondo B: Vitral Sacro & Catedral Gótica (Luminoso, cálido y no blanco)
 */
@Composable
fun VitralSacroCanvas(
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val topBg = if (isDark) Color(0xFF0F172A) else Color(0xFFFFFBEB)
    val midBg = if (isDark) Color(0xFF1E1B4B) else Color(0xFFFEF3C7)
    val botBg = if (isDark) Color(0xFF020617) else Color(0xFFFDF6E2)

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(topBg, midBg, botBg)))
    ) {
        val width = size.width
        val height = size.height
        val centerX = width * 0.5f
        val topY = height * 0.18f

        // 1. Resplandor de Rosa de Vitral
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFDE68A).copy(alpha = if (isDark) 0.45f else 0.55f),
                    Color(0xFFF59E0B).copy(alpha = if (isDark) 0.25f else 0.30f),
                    Color(0xFFD97706).copy(alpha = if (isDark) 0.12f else 0.15f),
                    Color.Transparent
                ),
                center = Offset(centerX, topY),
                radius = width * 0.65f
            ),
            radius = width * 0.65f,
            center = Offset(centerX, topY)
        )

        // 2. Facetas del Vitral (Gemas de color rubí, esmeralda, zafiro, oro cálido)
        val facets = 12
        val radius = width * 0.38f
        for (i in 0 until facets) {
            val angleStart = Math.toRadians((i * (360.0 / facets)))
            val angleMid = Math.toRadians(((i + 0.5) * (360.0 / facets)))
            val angleEnd = Math.toRadians(((i + 1) * (360.0 / facets)))

            val x1 = centerX + (radius * cos(angleStart)).toFloat()
            val y1 = topY + (radius * sin(angleStart)).toFloat()
            val x2 = centerX + (radius * 1.15f * cos(angleMid)).toFloat()
            val y2 = topY + (radius * 1.15f * sin(angleMid)).toFloat()
            val x3 = centerX + (radius * cos(angleEnd)).toFloat()
            val y3 = topY + (radius * sin(angleEnd)).toFloat()

            val facetColor = when (i % 4) {
                0 -> Color(0xFF1E824C).copy(alpha = if (isDark) 0.22f else 0.26f) // Verde Basiliano
                1 -> Color(0xFFD97706).copy(alpha = if (isDark) 0.28f else 0.32f) // Oro Cálido
                2 -> Color(0xFFB91C1C).copy(alpha = if (isDark) 0.20f else 0.24f) // Rubí Litúrgico
                else -> Color(0xFF1D4ED8).copy(alpha = if (isDark) 0.20f else 0.24f) // Azul Místico
            }

            val path = Path().apply {
                moveTo(centerX, topY)
                lineTo(x1, y1)
                lineTo(x2, y2)
                lineTo(x3, y3)
                close()
            }
            drawPath(path, color = facetColor)
            // Líneas de plomo del vitral
            drawPath(path, color = Color(0xFFB45309).copy(alpha = 0.35f), style = Stroke(width = 1.5f))
        }

        // 3. Arcos Góticos del Vitral
        val archPath = Path().apply {
            moveTo(centerX - width * 0.42f, topY + height * 0.5f)
            cubicTo(
                centerX - width * 0.42f, topY - width * 0.2f,
                centerX, topY - width * 0.45f,
                centerX, topY - width * 0.45f
            )
            cubicTo(
                centerX, topY - width * 0.45f,
                centerX + width * 0.42f, topY - width * 0.2f,
                centerX + width * 0.42f, topY + height * 0.5f
            )
        }
        drawPath(archPath, color = Color(0xFFD4AF37).copy(alpha = 0.45f), style = Stroke(width = 2.5f))

        // 4. Motas de polvo dorado celestial
        val goldenSparkles = listOf(
            Offset(centerX - width * 0.25f, topY + height * 0.15f),
            Offset(centerX + width * 0.28f, topY + height * 0.22f),
            Offset(centerX - width * 0.15f, topY + height * 0.35f),
            Offset(centerX + width * 0.18f, topY + height * 0.42f),
            Offset(centerX, topY + height * 0.55f)
        )
        goldenSparkles.forEach { spot ->
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFEF08A).copy(alpha = 0.6f), Color.Transparent),
                    center = spot,
                    radius = 28f
                ),
                radius = 28f,
                center = spot
            )
        }
    }
}

/**
 * Fondo C: Pergamino Cálido Monástico con sello de abadía
 */
@Composable
fun PergaminoMonasticoCanvas(
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val pTop = if (isDark) Color(0xFF1E140A) else Color(0xFFFFF9EE)
    val pMid = if (isDark) Color(0xFF2D1E10) else Color(0xFFFDF0D5)
    val pBot = if (isDark) Color(0xFF150D05) else Color(0xFFF7E6C4)

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(pTop, pMid, pBot)))
    ) {
        val w = size.width
        val h = size.height

        // Textura suave de pergamino / viñeta en las esquinas
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.Transparent,
                    Color(0xFFD97706).copy(alpha = if (isDark) 0.18f else 0.12f),
                    Color(0xFF92400E).copy(alpha = if (isDark) 0.35f else 0.20f)
                ),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.85f
            )
        )

        // Filigrana de Cruz Sacra estilizada en el fondo
        val cx = w * 0.5f
        val cy = h * 0.32f
        val crossHeight = w * 0.55f
        val crossWidth = w * 0.35f
        val armY = cy - crossHeight * 0.15f

        // Palo vertical
        drawLine(
            color = Color(0xFFD4AF37).copy(alpha = if (isDark) 0.25f else 0.20f),
            start = Offset(cx, cy - crossHeight * 0.5f),
            end = Offset(cx, cy + crossHeight * 0.5f),
            strokeWidth = 3f
        )
        // Palo horizontal
        drawLine(
            color = Color(0xFFD4AF37).copy(alpha = if (isDark) 0.25f else 0.20f),
            start = Offset(cx - crossWidth * 0.5f, armY),
            end = Offset(cx + crossWidth * 0.5f, armY),
            strokeWidth = 3f
        )
        // Círculo de gloria detrás de la cruz
        drawCircle(
            color = Color(0xFFFDE68A).copy(alpha = if (isDark) 0.15f else 0.18f),
            radius = crossWidth * 0.35f,
            center = Offset(cx, armY),
            style = Stroke(width = 2f)
        )
    }
}

/**
 * Fondo D: Cáliz Dorado & Eucaristía Resplandeciente
 */
@Composable
fun CalizEucaristicoCanvas(
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val bgTop = if (isDark) Color(0xFF140F2D) else Color(0xFFFFF8E7)
    val bgMid = if (isDark) Color(0xFF261230) else Color(0xFFFEF3C7)
    val bgBot = if (isDark) Color(0xFF0F0B1E) else Color(0xFFFDF0D5)

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(bgTop, bgMid, bgBot)))
    ) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val cy = h * 0.26f

        // Hostia Sagrada Radiante
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.95f),
                    Color(0xFFFEF3C7).copy(alpha = 0.85f),
                    Color(0xFFF59E0B).copy(alpha = 0.40f),
                    Color.Transparent
                ),
                center = Offset(cx, cy - 65f),
                radius = 120f
            ),
            radius = 120f,
            center = Offset(cx, cy - 65f)
        )

        // Cruz en la Hostia
        drawLine(
            color = Color(0xFFD97706).copy(alpha = 0.6f),
            start = Offset(cx, cy - 90f),
            end = Offset(cx, cy - 40f),
            strokeWidth = 2.5f
        )
        drawLine(
            color = Color(0xFFD97706).copy(alpha = 0.6f),
            start = Offset(cx - 20f, cy - 65f),
            end = Offset(cx + 20f, cy - 65f),
            strokeWidth = 2.5f
        )

        // Silueta Dorada del Cáliz
        val chalicePath = Path().apply {
            // Copa
            moveTo(cx - 70f, cy - 10f)
            cubicTo(cx - 70f, cy + 60f, cx + 70f, cy + 60f, cx + 70f, cy - 10f)
            lineTo(cx - 70f, cy - 10f)
            close()
            // Tallo
            moveTo(cx - 14f, cy + 50f)
            lineTo(cx - 10f, cy + 120f)
            // Base
            cubicTo(cx - 60f, cy + 130f, cx - 60f, cy + 150f, cx - 55f, cy + 150f)
            lineTo(cx + 55f, cy + 150f)
            cubicTo(cx + 60f, cy + 150f, cx + 60f, cy + 130f, cx + 10f, cy + 120f)
            lineTo(cx + 14f, cy + 50f)
            close()
        }

        drawPath(
            path = chalicePath,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFDE68A), Color(0xFFD97706), Color(0xFFB45309)),
                startY = cy - 10f,
                endY = cy + 150f
            )
        )
        drawPath(
            path = chalicePath,
            color = Color(0xFFFEF3C7).copy(alpha = 0.8f),
            style = Stroke(width = 2f)
        )
    }
}

/**
 * Fondo E: Vigilia & Noche Monástica (Estrellada con calidez de vela)
 */
@Composable
fun NocheMisticaCanvas(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF030712),
                        Color(0xFF0F172A),
                        Color(0xFF1E1B4B),
                        Color(0xFF31102A),
                        Color(0xFF1C0A00)
                    )
                )
            )
    ) {
        val w = size.width
        val h = size.height

        // Estrellas en el cielo
        val stars = listOf(
            Offset(w * 0.15f, h * 0.08f),
            Offset(w * 0.35f, h * 0.05f),
            Offset(w * 0.72f, h * 0.09f),
            Offset(w * 0.88f, h * 0.14f),
            Offset(w * 0.22f, h * 0.18f),
            Offset(w * 0.55f, h * 0.15f),
            Offset(w * 0.82f, h * 0.25f),
            Offset(w * 0.40f, h * 0.28f),
            Offset(w * 0.12f, h * 0.32f),
            Offset(w * 0.65f, h * 0.35f)
        )
        stars.forEachIndexed { idx, pt ->
            val rad = if (idx % 3 == 0) 3.5f else 2f
            drawCircle(color = Color(0xFFFEF3C7).copy(alpha = 0.85f), radius = rad, center = pt)
        }

        // Calidez de lámpara o cirio pascual abajo
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFF59E0B).copy(alpha = 0.45f),
                    Color(0xFFB45309).copy(alpha = 0.20f),
                    Color.Transparent
                ),
                center = Offset(w * 0.5f, h * 0.92f),
                radius = w * 0.6f
            ),
            radius = w * 0.6f,
            center = Offset(w * 0.5f, h * 0.92f)
        )
    }
}

private fun DrawScope.drawHolyDove(
    centerX: Float,
    centerY: Float,
    scale: Float,
    isDark: Boolean
) {
    val doveWhite = if (isDark) Color(0xFFF8FAFC).copy(alpha = 0.90f) else Color(0xFFFFFFFF).copy(alpha = 0.95f)
    val doveGold = Color(0xFFD4AF37).copy(alpha = 0.85f)

    val dovePath = Path().apply {
        moveTo(centerX, centerY - 14f * scale)
        lineTo(centerX + 2f * scale, centerY - 18f * scale)
        lineTo(centerX - 2f * scale, centerY - 18f * scale)
        close()

        moveTo(centerX - 2f * scale, centerY - 8f * scale)
        cubicTo(
            centerX - 25f * scale, centerY - 32f * scale,
            centerX - 50f * scale, centerY - 20f * scale,
            centerX - 60f * scale, centerY - 4f * scale
        )
        cubicTo(
            centerX - 42f * scale, centerY + 2f * scale,
            centerX - 20f * scale, centerY + 2f * scale,
            centerX - 4f * scale, centerY + 6f * scale
        )

        cubicTo(
            centerX - 10f * scale, centerY + 30f * scale,
            centerX + 10f * scale, centerY + 30f * scale,
            centerX + 4f * scale, centerY + 6f * scale
        )

        cubicTo(
            centerX + 20f * scale, centerY + 2f * scale,
            centerX + 42f * scale, centerY + 2f * scale,
            centerX + 60f * scale, centerY - 4f * scale
        )
        cubicTo(
            centerX + 50f * scale, centerY - 20f * scale,
            centerX + 25f * scale, centerY - 32f * scale,
            centerX + 2f * scale, centerY - 8f * scale
        )
        close()
    }

    drawPath(
        path = dovePath,
        color = doveGold,
        style = Stroke(width = 3.5f * scale)
    )

    drawPath(
        path = dovePath,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color.White,
                doveWhite,
                Color(0xFFFEF3C7).copy(alpha = 0.85f)
            ),
            startY = centerY - 30f * scale,
            endY = centerY + 30f * scale
        )
    )

    val graceRayCount = 7
    for (r in 0 until graceRayCount) {
        val spread = (r - (graceRayCount / 2)) * 14f * scale
        drawLine(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFEF08A).copy(alpha = 0.6f),
                    Color(0xFFF59E0B).copy(alpha = 0.25f),
                    Color.Transparent
                ),
                startY = centerY + 25f * scale,
                endY = centerY + 90f * scale
            ),
            start = Offset(centerX + spread * 0.4f, centerY + 25f * scale),
            end = Offset(centerX + spread * 1.8f, centerY + 90f * scale),
            strokeWidth = 1.8f * scale,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Capa Visual Decorativa Basiliana:
 * Incorpora degradados suaves y un sutil patrón de iconos de espiritualidad basiliana
 * (Paloma, Cruz, Escudo, Libro de Ciencia/Virtud, Cáliz Eucarístico, Espiga de Trigo, Lirio y Estrella)
 * para eliminar cualquier fondo blanco plano en todas las pantallas.
 */
@Composable
fun BasilianSpiritualDecorativePattern(
    isDark: Boolean,
    appTheme: BasilianAppTheme = BasilianAppTheme.ESCUDO_BASILIANO,
    modifier: Modifier = Modifier
) {
    val watermarkColor = if (isDark) {
        appTheme.goldColor.copy(alpha = 0.048f)
    } else {
        appTheme.primaryColor.copy(alpha = 0.040f)
    }

    val ambientTopGradient = if (isDark) {
        listOf(
            appTheme.primaryColor.copy(alpha = 0.12f),
            appTheme.backgroundColor.copy(alpha = 0.05f),
            Color.Transparent
        )
    } else {
        listOf(
            appTheme.primaryColor.copy(alpha = 0.08f),
            Color(0xFFFEF3C7).copy(alpha = 0.15f),
            Color.Transparent
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. Degradado ambiental suave superior e inferior (Warm Sacred Glow)
        drawRect(
            brush = Brush.verticalGradient(
                colors = ambientTopGradient,
                startY = 0f,
                endY = h * 0.45f
            )
        )

        // Resplandor cálido sutil en esquina inferior para evitar cualquier frialdad
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    if (isDark) Color(0xFF064E3B).copy(alpha = 0.15f) else Color(0xFF10B981).copy(alpha = 0.05f),
                    Color.Transparent
                ),
                center = Offset(w * 0.9f, h * 0.85f),
                radius = w * 0.6f
            ),
            radius = w * 0.6f,
            center = Offset(w * 0.9f, h * 0.85f)
        )

        // 2. Patrón de Iconos Sutiles de Espiritualidad Basiliana en Malla Armónica
        val colStep = 95f * density
        val rowStep = 115f * density
        val symbolSize = 26f * density

        val cols = (w / colStep).toInt() + 2
        val rows = (h / rowStep).toInt() + 2

        for (r in 0 until rows) {
            val y = r * rowStep
            // Desplazamiento alterno para un patrón de rombo/damasco sagrado
            val xOffset = if (r % 2 == 1) colStep * 0.5f else 0f

            for (c in 0 until cols) {
                val x = c * colStep + xOffset
                val symbolIndex = (c + r * 3) % 8

                when (symbolIndex) {
                    0 -> drawSubtleDove(Offset(x, y), symbolSize, watermarkColor)
                    1 -> drawSubtleCross(Offset(x, y), symbolSize * 0.9f, watermarkColor)
                    2 -> drawSubtleShield(Offset(x, y), symbolSize * 0.95f, watermarkColor)
                    3 -> drawSubtleBook(Offset(x, y), symbolSize, watermarkColor)
                    4 -> drawSubtleChalice(Offset(x, y), symbolSize * 0.95f, watermarkColor)
                    5 -> drawSubtleWheat(Offset(x, y), symbolSize * 0.9f, watermarkColor)
                    6 -> drawSubtleLily(Offset(x, y), symbolSize * 0.9f, watermarkColor)
                    7 -> drawSubtleStar(Offset(x, y), symbolSize * 0.85f, watermarkColor)
                }
            }
        }
    }
}

private fun DrawScope.drawSubtleCross(center: Offset, size: Float, color: Color) {
    val half = size / 2f
    val strokeWidth = size * 0.10f
    drawLine(
        color = color,
        start = Offset(center.x, center.y - half),
        end = Offset(center.x, center.y + half),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(center.x - half * 0.65f, center.y - half * 0.3f),
        end = Offset(center.x + half * 0.65f, center.y - half * 0.3f),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawSubtleDove(center: Offset, size: Float, color: Color) {
    val s = size / 2f
    val path = Path().apply {
        moveTo(center.x, center.y - s * 0.55f)
        cubicTo(center.x - s * 0.4f, center.y - s * 0.85f, center.x - s, center.y - s * 0.35f, center.x - s * 0.75f, center.y - s * 0.05f)
        cubicTo(center.x - s * 0.45f, center.y + s * 0.05f, center.x - s * 0.2f, center.y + s * 0.2f, center.x, center.y + s * 0.1f)
        lineTo(center.x - s * 0.2f, center.y + s * 0.75f)
        lineTo(center.x, center.y + s * 0.50f)
        lineTo(center.x + s * 0.2f, center.y + s * 0.75f)
        lineTo(center.x, center.y + s * 0.1f)
        cubicTo(center.x + s * 0.2f, center.y + s * 0.2f, center.x + s * 0.45f, center.y + s * 0.05f, center.x + s * 0.75f, center.y - s * 0.05f)
        cubicTo(center.x + s, center.y - s * 0.35f, center.x + s * 0.4f, center.y - s * 0.85f, center.x, center.y - s * 0.55f)
        close()
    }
    drawPath(path = path, color = color, style = Stroke(width = size * 0.08f, cap = StrokeCap.Round))
}

private fun DrawScope.drawSubtleShield(center: Offset, size: Float, color: Color) {
    val s = size / 2f
    val path = Path().apply {
        moveTo(center.x - s * 0.65f, center.y - s * 0.65f)
        lineTo(center.x + s * 0.65f, center.y - s * 0.65f)
        lineTo(center.x + s * 0.65f, center.y)
        cubicTo(center.x + s * 0.65f, center.y + s * 0.45f, center.x + s * 0.3f, center.y + s * 0.75f, center.x, center.y + s * 0.90f)
        cubicTo(center.x - s * 0.3f, center.y + s * 0.75f, center.x - s * 0.65f, center.y + s * 0.45f, center.x - s * 0.65f, center.y)
        close()
    }
    drawPath(path = path, color = color, style = Stroke(width = size * 0.08f, cap = StrokeCap.Round))
    drawLine(color = color, start = Offset(center.x, center.y - s * 0.45f), end = Offset(center.x, center.y + s * 0.45f), strokeWidth = size * 0.06f)
    drawLine(color = color, start = Offset(center.x - s * 0.35f, center.y - s * 0.1f), end = Offset(center.x + s * 0.35f, center.y - s * 0.1f), strokeWidth = size * 0.06f)
}

private fun DrawScope.drawSubtleBook(center: Offset, size: Float, color: Color) {
    val s = size / 2f
    val path = Path().apply {
        moveTo(center.x, center.y - s * 0.35f)
        cubicTo(center.x - s * 0.25f, center.y - s * 0.55f, center.x - s * 0.55f, center.y - s * 0.45f, center.x - s * 0.8f, center.y - s * 0.3f)
        lineTo(center.x - s * 0.8f, center.y + s * 0.4f)
        cubicTo(center.x - s * 0.55f, center.y + s * 0.25f, center.x - s * 0.25f, center.y + s * 0.35f, center.x, center.y + s * 0.55f)
        cubicTo(center.x + s * 0.25f, center.y + s * 0.35f, center.x + s * 0.55f, center.y + s * 0.25f, center.x + s * 0.8f, center.y + s * 0.4f)
        lineTo(center.x + s * 0.8f, center.y - s * 0.3f)
        cubicTo(center.x + s * 0.55f, center.y - s * 0.45f, center.x + s * 0.25f, center.y - s * 0.55f, center.x, center.y - s * 0.35f)
        close()
    }
    drawPath(path = path, color = color, style = Stroke(width = size * 0.08f, cap = StrokeCap.Round))
    drawLine(color = color, start = Offset(center.x, center.y - s * 0.35f), end = Offset(center.x, center.y + s * 0.55f), strokeWidth = size * 0.07f)
}

private fun DrawScope.drawSubtleChalice(center: Offset, size: Float, color: Color) {
    val s = size / 2f
    drawCircle(color = color, radius = s * 0.22f, center = Offset(center.x, center.y - s * 0.6f), style = Stroke(width = size * 0.06f))
    val cupPath = Path().apply {
        moveTo(center.x - s * 0.55f, center.y - s * 0.2f)
        cubicTo(center.x - s * 0.55f, center.y + s * 0.25f, center.x + s * 0.55f, center.y + s * 0.25f, center.x + s * 0.55f, center.y - s * 0.2f)
        close()
    }
    drawPath(path = cupPath, color = color, style = Stroke(width = size * 0.08f, cap = StrokeCap.Round))
    drawLine(color = color, start = Offset(center.x, center.y + s * 0.2f), end = Offset(center.x, center.y + s * 0.6f), strokeWidth = size * 0.08f)
    drawLine(color = color, start = Offset(center.x - s * 0.45f, center.y + s * 0.75f), end = Offset(center.x + s * 0.45f, center.y + s * 0.75f), strokeWidth = size * 0.08f, cap = StrokeCap.Round)
}

private fun DrawScope.drawSubtleWheat(center: Offset, size: Float, color: Color) {
    val s = size / 2f
    drawLine(color = color, start = Offset(center.x, center.y + s * 0.8f), end = Offset(center.x, center.y - s * 0.8f), strokeWidth = size * 0.06f, cap = StrokeCap.Round)
    for (i in 0..3) {
        val y = center.y - s * 0.55f + (i * s * 0.32f)
        drawOval(color = color, topLeft = Offset(center.x - s * 0.32f, y - s * 0.1f), size = Size(s * 0.32f, s * 0.18f), style = Stroke(width = size * 0.06f))
        drawOval(color = color, topLeft = Offset(center.x, y - s * 0.1f), size = Size(s * 0.32f, s * 0.18f), style = Stroke(width = size * 0.06f))
    }
}

private fun DrawScope.drawSubtleLily(center: Offset, size: Float, color: Color) {
    val s = size / 2f
    val centerPetal = Path().apply {
        moveTo(center.x, center.y - s * 0.8f)
        cubicTo(center.x - s * 0.2f, center.y - s * 0.2f, center.x - s * 0.2f, center.y + s * 0.2f, center.x, center.y + s * 0.25f)
        cubicTo(center.x + s * 0.2f, center.y + s * 0.2f, center.x + s * 0.2f, center.y - s * 0.2f, center.x, center.y - s * 0.8f)
        close()
    }
    drawPath(path = centerPetal, color = color, style = Stroke(width = size * 0.07f))
    val leftPetal = Path().apply {
        moveTo(center.x - s * 0.65f, center.y - s * 0.35f)
        cubicTo(center.x - s * 0.25f, center.y - s * 0.45f, center.x - s * 0.1f, center.y, center.x, center.y + s * 0.2f)
        cubicTo(center.x - s * 0.35f, center.y + s * 0.2f, center.x - s * 0.6f, center.y, center.x - s * 0.65f, center.y - s * 0.35f)
        close()
    }
    drawPath(path = leftPetal, color = color, style = Stroke(width = size * 0.07f))
    val rightPetal = Path().apply {
        moveTo(center.x + s * 0.65f, center.y - s * 0.35f)
        cubicTo(center.x + s * 0.25f, center.y - s * 0.45f, center.x + s * 0.1f, center.y, center.x, center.y + s * 0.2f)
        cubicTo(center.x + s * 0.35f, center.y + s * 0.2f, center.x + s * 0.6f, center.y, center.x + s * 0.65f, center.y - s * 0.35f)
        close()
    }
    drawPath(path = rightPetal, color = color, style = Stroke(width = size * 0.07f))
}

private fun DrawScope.drawSubtleStar(center: Offset, size: Float, color: Color) {
    val s = size / 2f
    drawLine(color = color, start = Offset(center.x, center.y - s), end = Offset(center.x, center.y + s), strokeWidth = size * 0.07f, cap = StrokeCap.Round)
    drawLine(color = color, start = Offset(center.x - s, center.y), end = Offset(center.x + s, center.y), strokeWidth = size * 0.07f, cap = StrokeCap.Round)
    val d = s * 0.5f
    drawLine(color = color, start = Offset(center.x - d, center.y - d), end = Offset(center.x + d, center.y + d), strokeWidth = size * 0.05f, cap = StrokeCap.Round)
    drawLine(color = color, start = Offset(center.x - d, center.y + d), end = Offset(center.x + d, center.y - d), strokeWidth = size * 0.05f, cap = StrokeCap.Round)
}

/**
 * Fondo Futurista Holográfico Estilo J.A.R.V.I.S. (Núcleo C.S.B. Quantum HUD)
 * Combina anillos de reactor de arco giratorios, barrido de radar táctico,
 * malla cibernética en perspectiva y geometría sagrada dorada/cian.
 */
@Composable
fun JarvisFuturistaCanvas(
    modifier: Modifier = Modifier,
    isActivePulse: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "jarvis_hud_transition")

    val outerRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isActivePulse) 14000 else 28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "jarvis_outer_ring"
    )

    val innerRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isActivePulse) 9000 else 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "jarvis_inner_ring"
    )

    val corePulse by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.14f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isActivePulse) 900 else 2100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "jarvis_core_pulse"
    )

    val scanLineProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "jarvis_scan_line"
    )

    val cyanNeon = Color(0xFF00F0FF)
    val electricBlue = Color(0xFF0284C7)
    val jarvisGold = Color(0xFFF59E0B)
    val deepSpaceTop = Color(0xFF020617)
    val deepSpaceMid = Color(0xFF04142C)
    val deepSpaceBot = Color(0xFF01040D)

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(deepSpaceTop, deepSpaceMid, deepSpaceBot)))
    ) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val cy = h * 0.34f

        // 1. Malla Holográfica Cibernética (Grid Táctico J.A.R.V.I.S.)
        val gridStep = 44.dp.toPx().coerceAtLeast(28f)
        var x = 0f
        while (x <= w) {
            drawLine(
                color = cyanNeon.copy(alpha = 0.055f),
                start = Offset(x, 0f),
                end = Offset(x, h),
                strokeWidth = 1f
            )
            x += gridStep
        }
        var y = 0f
        while (y <= h) {
            drawLine(
                color = cyanNeon.copy(alpha = 0.055f),
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = 1f
            )
            y += gridStep
        }

        // Líneas de perspectiva desde la base
        for (i in -4..4) {
            val bottomX = cx + i * (w * 0.22f)
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Transparent, cyanNeon.copy(alpha = 0.14f), electricBlue.copy(alpha = 0.22f)),
                    start = Offset(cx, cy),
                    end = Offset(bottomX, h)
                ),
                start = Offset(cx, cy),
                end = Offset(bottomX, h),
                strokeWidth = 1.2f
            )
        }

        // 2. Resplandor Central del Reactor de Arco Holográfico
        val baseRadius = w * 0.44f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    cyanNeon.copy(alpha = 0.28f * corePulse),
                    electricBlue.copy(alpha = 0.16f),
                    jarvisGold.copy(alpha = 0.07f),
                    Color.Transparent
                ),
                center = Offset(cx, cy),
                radius = baseRadius * 1.45f
            ),
            radius = baseRadius * 1.45f,
            center = Offset(cx, cy)
        )

        // 3. Anillo Exterior de Grados y Telemetría (Giratorio)
        val rOuter = baseRadius * 0.96f
        drawCircle(
            color = cyanNeon.copy(alpha = 0.32f),
            radius = rOuter,
            center = Offset(cx, cy),
            style = Stroke(width = 2f)
        )

        val tickCount = 72
        for (i in 0 until tickCount) {
            val angleDeg = i * (360f / tickCount) + outerRotation
            val rad = Math.toRadians(angleDeg.toDouble())
            val isMajor = i % 6 == 0
            val innerTickR = if (isMajor) rOuter * 0.91f else rOuter * 0.95f
            val sx = cx + (innerTickR * cos(rad)).toFloat()
            val sy = cy + (innerTickR * sin(rad)).toFloat()
            val ex = cx + (rOuter * cos(rad)).toFloat()
            val ey = cy + (rOuter * sin(rad)).toFloat()
            drawLine(
                color = if (isMajor) jarvisGold.copy(alpha = 0.65f) else cyanNeon.copy(alpha = 0.35f),
                start = Offset(sx, sy),
                end = Offset(ex, ey),
                strokeWidth = if (isMajor) 2.6f else 1.2f,
                cap = StrokeCap.Round
            )
        }

        // 4. Anillos Segmentados Tácticos Intermedios (Contra-rotación)
        val rMid = baseRadius * 0.74f
        for (seg in 0 until 4) {
            drawArc(
                color = cyanNeon.copy(alpha = 0.48f),
                startAngle = innerRotation + seg * 90f,
                sweepAngle = 54f,
                useCenter = false,
                topLeft = Offset(cx - rMid, cy - rMid),
                size = Size(rMid * 2f, rMid * 2f),
                style = Stroke(width = 4.5f, cap = StrokeCap.Round)
            )
            drawArc(
                color = jarvisGold.copy(alpha = 0.50f),
                startAngle = outerRotation * 1.3f + seg * 90f + 62f,
                sweepAngle = 20f,
                useCenter = false,
                topLeft = Offset(cx - rMid * 0.88f, cy - rMid * 0.88f),
                size = Size(rMid * 1.76f, rMid * 1.76f),
                style = Stroke(width = 3f, cap = StrokeCap.Round)
            )
        }

        // 5. Anillo Interior del Reactor de Arco & Nodos de Datos
        val rInner = baseRadius * 0.46f * corePulse
        drawCircle(
            color = cyanNeon.copy(alpha = 0.55f),
            radius = rInner,
            center = Offset(cx, cy),
            style = Stroke(width = 2.5f)
        )
        drawCircle(
            color = jarvisGold.copy(alpha = 0.42f),
            radius = rInner * 0.72f,
            center = Offset(cx, cy),
            style = Stroke(width = 1.8f)
        )

        val nodeCount = 8
        for (n in 0 until nodeCount) {
            val a = Math.toRadians((outerRotation * 1.5f + n * (360f / nodeCount)).toDouble())
            val nx = cx + (rInner * cos(a)).toFloat()
            val ny = cy + (rInner * sin(a)).toFloat()
            drawCircle(
                color = if (n % 2 == 0) cyanNeon else jarvisGold,
                radius = 4.2f,
                center = Offset(nx, ny)
            )
        }

        // 6. Cruz / Cristograma Holográfico Central en el Núcleo J.A.R.V.I.S.
        val crossSize = rInner * 0.42f
        drawLine(
            color = jarvisGold.copy(alpha = 0.65f),
            start = Offset(cx, cy - crossSize),
            end = Offset(cx, cy + crossSize),
            strokeWidth = 2.5f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = jarvisGold.copy(alpha = 0.65f),
            start = Offset(cx - crossSize * 0.75f, cy - crossSize * 0.15f),
            end = Offset(cx + crossSize * 0.75f, cy - crossSize * 0.15f),
            strokeWidth = 2.5f,
            cap = StrokeCap.Round
        )

        // 7. Línea de Escaneo Láser Horizontal (HUD Scanner)
        val scanY = h * scanLineProgress
        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    cyanNeon.copy(alpha = 0.45f),
                    jarvisGold.copy(alpha = 0.60f),
                    cyanNeon.copy(alpha = 0.45f),
                    Color.Transparent
                )
            ),
            start = Offset(0f, scanY),
            end = Offset(w, scanY),
            strokeWidth = 2f
        )

        // 8. Escuadras Tácticas HUD en las 4 esquinas
        val bracketLen = 28.dp.toPx()
        val pad = 12.dp.toPx()
        val bracketColor = cyanNeon.copy(alpha = 0.55f)
        // Top-Left
        drawLine(bracketColor, Offset(pad, pad), Offset(pad + bracketLen, pad), strokeWidth = 3f)
        drawLine(bracketColor, Offset(pad, pad), Offset(pad, pad + bracketLen), strokeWidth = 3f)
        // Top-Right
        drawLine(bracketColor, Offset(w - pad, pad), Offset(w - pad - bracketLen, pad), strokeWidth = 3f)
        drawLine(bracketColor, Offset(w - pad, pad), Offset(w - pad, pad + bracketLen), strokeWidth = 3f)
        // Bottom-Left
        drawLine(bracketColor, Offset(pad, h - pad), Offset(pad + bracketLen, h - pad), strokeWidth = 3f)
        drawLine(bracketColor, Offset(pad, h - pad), Offset(pad, h - pad - bracketLen), strokeWidth = 3f)
        // Bottom-Right
        drawLine(bracketColor, Offset(w - pad, h - pad), Offset(w - pad - bracketLen, h - pad), strokeWidth = 3f)
        drawLine(bracketColor, Offset(w - pad, h - pad), Offset(w - pad, h - pad - bracketLen), strokeWidth = 3f)
    }
}

