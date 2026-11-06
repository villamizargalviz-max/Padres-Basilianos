package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlin.math.cos
import kotlin.math.sin

/**
 * Paleta de Colores Futuristas de Alto Contraste para Tarjetas, Citas y Tipografía Luminosa.
 */
object FuturisticDesignPalette {
    val DeepObsidian = Color(0xFF040D1E)
    val SapphireGlass = Color(0xFF081A36)
    val IndigoGlass = Color(0xFF13163B)
    val CyberCyan = Color(0xFF00F0FF)
    val SacredGold = Color(0xFFF59E0B)
    val GoldLuminous = Color(0xFFFDE68A)
    val EmeraldNeon = Color(0xFF10B981)
    val EmeraldLight = Color(0xFF6EE7B7)
    val RoyalViolet = Color(0xFFA855F7)
    val VioletLight = Color(0xFFDDD6FE)
    val CrimsonRose = Color(0xFFF43F5E)
    val RoseLight = Color(0xFFFECDD3)
    val ElectricBlue = Color(0xFF38BDF8)
    val NeonGold = Color(0xFFFFD700)
    val NeonCyan = Color(0xFF00F0FF)
    val NeonEmerald = Color(0xFF10B981)
    val NeonMagenta = Color(0xFFEC4899)
    val NeonViolet = Color(0xFFA855F7)

    private val primaryPalette = listOf(
        NeonCyan,
        NeonGold,
        NeonEmerald,
        NeonMagenta,
        NeonViolet,
        ElectricBlue
    )
    private val secondaryPalette = listOf(
        NeonGold,
        NeonCyan,
        NeonViolet,
        NeonCyan,
        NeonEmerald,
        NeonGold
    )

    fun accentForIndex(index: Int): Color =
        primaryPalette[kotlin.math.abs(index) % primaryPalette.size]

    fun secondaryForIndex(index: Int): Color =
        secondaryPalette[kotlin.math.abs(index) % secondaryPalette.size]
}

/**
 * Intensidad global del motor 3D Futurista para toda la aplicación.
 */
enum class Futuristic3DIntensity(val label: String, val tiltFactor: Float, val glowAlpha: Float) {
    HOLO_3D("🌐 3D Holográfico", 1.0f, 0.90f),
    HIPER_3D("🚀 3D Profundo", 1.45f, 1.0f),
    CRISTAL_3D("💎 Cristal Sacro", 0.65f, 0.72f)
}

val LocalFuturistic3DIntensity = compositionLocalOf { Futuristic3DIntensity.HOLO_3D }

/**
 * Modificador que transforma cualquier tarjeta o contenedor en un Panel HUD Holográfico Futurista.
 * Alinea las tarjetas de forma limpia y recta (sin bordes torcidos ni sombras desfasadas),
 * añadiendo esquinas cibernéticas luminosas (HUD Corner Brackets), nodos de energía neón,
 * barrido de luz de cristal y haces láser superiores/inferiores.
 */
fun Modifier.futuristic3DCard(
    accentColor: Color = FuturisticDesignPalette.CyberCyan,
    secondaryColor: Color = FuturisticDesignPalette.SacredGold,
    cornerRadius: Dp = 20.dp,
    tiltDegrees: Float = 0f,
    tiltXDegrees: Float = tiltDegrees,
    tiltYDegrees: Float = 0f,
    animateBreathing: Boolean = true
): Modifier = composed {
    val intensity = LocalFuturistic3DIntensity.current
    val infiniteTransition = rememberInfiniteTransition(label = "card_hud_pulse")
    val animPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hud_pulse_phase"
    )

    val glowFactor = intensity.glowAlpha * (0.82f + 0.18f * animPhase)

    this
        .graphicsLayer {
            shadowElevation = 14f * intensity.tiltFactor
        }
        .drawWithContent {
            val radiusPx = cornerRadius.toPx()
            val w = size.width
            val h = size.height

            // 1. Contenido original de la tarjeta
            drawContent()

            // 2. Resplandor diagonal interno de cristal holográfico sutil
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.10f * glowFactor),
                        Color.Transparent,
                        secondaryColor.copy(alpha = 0.08f * glowFactor)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(w, h)
                ),
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(radiusPx, radiusPx)
            )

            // 3. Borde perimetral de cristal neón multicolor perfectamente alineado
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.88f * glowFactor),
                        secondaryColor.copy(alpha = 0.72f * glowFactor),
                        accentColor.copy(alpha = 0.55f * glowFactor),
                        secondaryColor.copy(alpha = 0.85f * glowFactor)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(w, h)
                ),
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(radiusPx, radiusPx),
                style = Stroke(width = 1.6.dp.toPx())
            )

            // 4. Haz de luz láser superior (Top Specular Energy Bar)
            val beamStart = w * (0.14f + 0.06f * animPhase)
            val beamEnd = w * (0.86f - 0.06f * animPhase)
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        accentColor.copy(alpha = 0.95f * glowFactor),
                        Color.White.copy(alpha = 0.90f * glowFactor),
                        secondaryColor.copy(alpha = 0.95f * glowFactor),
                        Color.Transparent
                    ),
                    startX = beamStart,
                    endX = beamEnd
                ),
                start = Offset(beamStart, 1.5.dp.toPx()),
                end = Offset(beamEnd, 1.5.dp.toPx()),
                strokeWidth = 2.6.dp.toPx(),
                cap = StrokeCap.Round
            )

            // 5. Haz de luz láser inferior sutil
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        secondaryColor.copy(alpha = 0.75f * glowFactor),
                        accentColor.copy(alpha = 0.75f * glowFactor),
                        Color.Transparent
                    ),
                    startX = w * 0.22f,
                    endX = w * 0.78f
                ),
                start = Offset(w * 0.22f, h - 1.5.dp.toPx()),
                end = Offset(w * 0.78f, h - 1.5.dp.toPx()),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )

            // 6. Esquinas HUD Futuristas (4 Brackets L-Shaped + Nodos Luminosos)
            val inset = 4.dp.toPx()
            val bracketLen = minOf(22.dp.toPx(), w * 0.14f, h * 0.22f)
            val bracketStroke = 2.4.dp.toPx()
            val nodeRadius = 2.6.dp.toPx()

            // Esquina Superior-Izquierda (Accent)
            drawLine(
                color = accentColor.copy(alpha = 0.95f * glowFactor),
                start = Offset(inset, inset + bracketLen),
                end = Offset(inset, inset + 4.dp.toPx()),
                strokeWidth = bracketStroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = accentColor.copy(alpha = 0.95f * glowFactor),
                start = Offset(inset + 4.dp.toPx(), inset),
                end = Offset(inset + bracketLen, inset),
                strokeWidth = bracketStroke,
                cap = StrokeCap.Round
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.9f * glowFactor),
                radius = nodeRadius,
                center = Offset(inset + 3.dp.toPx(), inset + 3.dp.toPx())
            )

            // Esquina Superior-Derecha (Secondary)
            drawLine(
                color = secondaryColor.copy(alpha = 0.95f * glowFactor),
                start = Offset(w - inset - bracketLen, inset),
                end = Offset(w - inset - 4.dp.toPx(), inset),
                strokeWidth = bracketStroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = secondaryColor.copy(alpha = 0.95f * glowFactor),
                start = Offset(w - inset, inset + 4.dp.toPx()),
                end = Offset(w - inset, inset + bracketLen),
                strokeWidth = bracketStroke,
                cap = StrokeCap.Round
            )
            drawCircle(
                color = secondaryColor.copy(alpha = 0.95f * glowFactor),
                radius = nodeRadius,
                center = Offset(w - inset - 3.dp.toPx(), inset + 3.dp.toPx())
            )

            // Esquina Inferior-Izquierda (Secondary)
            drawLine(
                color = secondaryColor.copy(alpha = 0.90f * glowFactor),
                start = Offset(inset, h - inset - bracketLen),
                end = Offset(inset, h - inset - 4.dp.toPx()),
                strokeWidth = bracketStroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = secondaryColor.copy(alpha = 0.90f * glowFactor),
                start = Offset(inset + 4.dp.toPx(), h - inset),
                end = Offset(inset + bracketLen, h - inset),
                strokeWidth = bracketStroke,
                cap = StrokeCap.Round
            )
            drawCircle(
                color = secondaryColor.copy(alpha = 0.9f * glowFactor),
                radius = nodeRadius,
                center = Offset(inset + 3.dp.toPx(), h - inset - 3.dp.toPx())
            )

            // Esquina Inferior-Derecha (Accent)
            drawLine(
                color = accentColor.copy(alpha = 0.95f * glowFactor),
                start = Offset(w - inset - bracketLen, h - inset),
                end = Offset(w - inset - 4.dp.toPx(), h - inset),
                strokeWidth = bracketStroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = accentColor.copy(alpha = 0.95f * glowFactor),
                start = Offset(w - inset, h - inset - bracketLen),
                end = Offset(w - inset, h - inset - 4.dp.toPx()),
                strokeWidth = bracketStroke,
                cap = StrokeCap.Round
            )
            drawCircle(
                color = accentColor.copy(alpha = 0.95f * glowFactor),
                radius = nodeRadius,
                center = Offset(w - inset - 3.dp.toPx(), h - inset - 3.dp.toPx())
            )
        }
}

/**
 * Tarjeta Futurista de Cita Importante Rescatada ("Quote Callout Card").
 * Resalta las citas patrísticas, históricas o bíblicas con colores vibrantes,
 * esquinas HUD holográficas, tipografía de alto contraste y barra luminosa lateral.
 */
@Composable
fun FuturisticQuoteCalloutCard(
    quoteText: String,
    authorOrSource: String,
    badgeLabel: String = "CITA DESTACADA",
    categoryTag: String = badgeLabel,
    accentColor: Color = FuturisticDesignPalette.SacredGold,
    secondaryColor: Color = FuturisticDesignPalette.CyberCyan,
    onSpeakQuote: (() -> Unit)? = null,
    onSpeak: (() -> Unit)? = onSpeakQuote,
    modifier: Modifier = Modifier
) {
    val resolvedBadge = if (categoryTag.isNotBlank()) categoryTag else badgeLabel
    val resolvedOnSpeak = onSpeak ?: onSpeakQuote
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = FuturisticDesignPalette.DeepObsidian.copy(alpha = 0.94f),
        shadowElevation = 10.dp,
        modifier = modifier
            .fillMaxWidth()
            .futuristic3DCard(
                accentColor = accentColor,
                secondaryColor = secondaryColor,
                cornerRadius = 20.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.20f),
                            FuturisticDesignPalette.SapphireGlass.copy(alpha = 0.96f),
                            secondaryColor.copy(alpha = 0.16f)
                        )
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Barra vertical de plasma neón/oro
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .heightIn(min = 56.dp, max = 115.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(accentColor, Color.White, secondaryColor)
                        )
                    )
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = accentColor.copy(alpha = 0.24f),
                        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.85f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = resolvedBadge.uppercase(),
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = FuturisticDesignPalette.GoldLuminous,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }

                    if (resolvedOnSpeak != null) {
                        Surface(
                            onClick = resolvedOnSpeak,
                            shape = CircleShape,
                            color = secondaryColor.copy(alpha = 0.20f),
                            border = BorderStroke(1.dp, secondaryColor.copy(alpha = 0.75f)),
                            modifier = Modifier.size(30.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Escuchar cita",
                                    tint = secondaryColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                val cleanQuote = quoteText.trim().removeSurrounding("«", "»").removeSurrounding("\"", "\"")
                Text(
                    text = "«$cleanQuote»",
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    fontSize = 14.5.sp,
                    lineHeight = 20.5.sp
                )

                if (authorOrSource.isNotBlank()) {
                    Text(
                        text = "— $authorOrSource",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = secondaryColor,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

/**
 * Formateador Inteligente de Texto en un solo cuadro unificado (estilo Gemini).
 * Presenta toda la información de forma limpia y fluida sin dividir la respuesta
 * en múltiples sub-cuadros, resaltando en línea palabras clave en negrita (**...**),
 * citas («...») y fechas históricas.
 */
@Composable
fun FuturisticFormattedTextWithQuotes(
    rawText: String,
    primaryAccent: Color = FuturisticDesignPalette.CyberCyan,
    quoteAccent: Color = FuturisticDesignPalette.SacredGold,
    accentColor: Color = primaryAccent,
    secondaryColor: Color = quoteAccent,
    baseTextColor: Color = Color(0xFFF8FAFC),
    onSpeakQuote: (() -> Unit)? = null,
    fontScale: Float = 1f,
    modifier: Modifier = Modifier
) {
    val paragraphs = remember(rawText) {
        rawText.split("\n").map { it.trim() }.filter { it.isNotBlank() && it != "---" }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        paragraphs.forEachIndexed { idx, line ->
            val isBullet = line.startsWith("•") || line.startsWith("-") ||
                (line.startsWith("*") && !line.startsWith("**")) ||
                line.matches(Regex("^\\d+\\..*"))

            if (isBullet) {
                val bulletText = line
                    .removePrefix("•")
                    .removePrefix("-")
                    .let { if (it.startsWith("*") && !it.startsWith("**")) it.removePrefix("*") else it }
                    .trim()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "•",
                        color = if (idx % 2 == 0) accentColor else secondaryColor,
                        fontWeight = FontWeight.Black,
                        fontSize = (15f * fontScale).sp,
                        lineHeight = (22f * fontScale).sp
                    )
                    Text(
                        text = buildHighlightedAnnotatedString(
                            text = bulletText,
                            highlightColor = FuturisticDesignPalette.GoldLuminous,
                            accentColor = accentColor
                        ),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = (14.5f * fontScale).sp,
                            lineHeight = (21.5f * fontScale).sp
                        ),
                        color = baseTextColor
                    )
                }
            } else {
                Text(
                    text = buildHighlightedAnnotatedString(
                        text = line,
                        highlightColor = FuturisticDesignPalette.GoldLuminous,
                        accentColor = accentColor
                    ),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (15f * fontScale).sp,
                        lineHeight = (22.5f * fontScale).sp
                    ),
                    color = baseTextColor,
                    fontWeight = if (idx == 0) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

private fun buildHighlightedAnnotatedString(
    text: String,
    highlightColor: Color,
    accentColor: Color
) = buildAnnotatedString {
    val tokenRegex = Regex("(\\*\\*[^*]+\\*\\*|«[^»]+»|\"[^\"]{4,120}\"|\\b(17\\d\\d|18\\d\\d|19\\d\\d|20\\d\\d|329|370|379)\\b)")
    var lastIndex = 0
    for (match in tokenRegex.findAll(text)) {
        if (match.range.first > lastIndex) {
            append(text.substring(lastIndex, match.range.first).replace("*", ""))
        }
        val token = match.value
        when {
            token.startsWith("**") && token.endsWith("**") && token.length > 4 -> {
                withStyle(
                    SpanStyle(
                        color = accentColor,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(token.removeSurrounding("**"))
                }
            }
            token.startsWith("«") || token.startsWith("\"") -> {
                withStyle(
                    SpanStyle(
                        color = highlightColor,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                ) {
                    append(token)
                }
            }
            else -> {
                withStyle(
                    SpanStyle(
                        color = accentColor,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(token)
                }
            }
        }
        lastIndex = match.range.last + 1
    }
    if (lastIndex < text.length) {
        append(text.substring(lastIndex).replace("*", ""))
    }
}

/**
 * Fondo Festivo de Cumpleaños en Canvas 3D con Globos/Bombas de Colores, Confeti Luminoso
 * y una Torta de Cumpleaños de 2 Pisos con Velas Encendidas.
 */
@Composable
fun BirthdayCelebrationFestiveCanvas(
    extraBurstTrigger: Int = 0,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "birthday_festive_canvas")
    val floatPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "balloons_float"
    )
    val swayPhase by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "balloons_sway"
    )
    val flameFlicker by infiniteTransition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 420, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "candle_flame"
    )

    val balloonColors = remember {
        listOf(
            Color(0xFFFFD700), // Oro Festivo
            Color(0xFF00F0FF), // Cian Holográfico
            Color(0xFFEC4899), // Rosa Fiesta
            Color(0xFF10B981), // Esmeralda Neón
            Color(0xFFA855F7), // Violeta Real
            Color(0xFFF43F5E), // Coral Rubí
            Color(0xFF38BDF8)  // Azul Eléctrico
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. Halo festivo multicolor de fondo
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFD700).copy(alpha = 0.20f),
                    Color(0xFFEC4899).copy(alpha = 0.12f),
                    Color.Transparent
                ),
                center = Offset(w * 0.82f, h * 0.45f),
                radius = w * 0.55f
            ),
            center = Offset(w * 0.82f, h * 0.45f),
            radius = w * 0.55f
        )

        // 2. Globos / Bombas de Fiesta flotando con brillo 3D e hilo ondulado
        val balloonCount = 8 + (extraBurstTrigger % 4)
        for (i in 0 until balloonCount) {
            val baseXRatio = (0.07f + (i * 0.125f)) % 0.94f
            val speedOffset = (i * 0.17f) % 1f
            val cycle = (floatPhase + speedOffset) % 1f
            val centerX = w * baseXRatio + sin((cycle * 6.28f) + i) * 10.dp.toPx() * swayPhase
            val centerY = h * (1.05f - cycle * 1.15f)
            val bColor = balloonColors[i % balloonColors.size]
            val rx = (13f + (i % 3) * 3.5f).dp.toPx()
            val ry = rx * 1.22f

            // Hilo del globo
            val stringPath = Path().apply {
                moveTo(centerX, centerY + ry)
                quadraticTo(
                    centerX + 5.dp.toPx() * swayPhase,
                    centerY + ry + 14.dp.toPx(),
                    centerX - 3.dp.toPx() * swayPhase,
                    centerY + ry + 28.dp.toPx()
                )
            }
            drawPath(
                path = stringPath,
                color = Color.White.copy(alpha = 0.35f),
                style = Stroke(width = 1.1.dp.toPx())
            )

            // Cuerpo 3D del globo (bomba)
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.65f),
                        bColor.copy(alpha = 0.50f),
                        bColor.copy(alpha = 0.28f)
                    ),
                    center = Offset(centerX - rx * 0.28f, centerY - ry * 0.28f),
                    radius = ry * 1.1f
                ),
                topLeft = Offset(centerX - rx, centerY - ry),
                size = Size(rx * 2f, ry * 2f)
            )
            // Contorno luminoso del globo
            drawOval(
                color = bColor.copy(alpha = 0.75f),
                topLeft = Offset(centerX - rx, centerY - ry),
                size = Size(rx * 2f, ry * 2f),
                style = Stroke(width = 1.3.dp.toPx())
            )
            // Brillo especular del globo
            drawOval(
                color = Color.White.copy(alpha = 0.55f),
                topLeft = Offset(centerX - rx * 0.55f, centerY - ry * 0.60f),
                size = Size(rx * 0.42f, ry * 0.32f)
            )
        }

        // 3. Confeti festivo y destellos dorados
        for (c in 0 until 22) {
            val cx = w * ((c * 0.047f + floatPhase * 0.15f) % 1f)
            val cy = h * ((c * 0.083f + floatPhase * 0.6f) % 1f)
            val confColor = balloonColors[(c * 2) % balloonColors.size]
            drawRoundRect(
                color = confColor.copy(alpha = 0.55f),
                topLeft = Offset(cx, cy),
                size = Size(5.dp.toPx(), 2.6.dp.toPx()),
                cornerRadius = CornerRadius(2f, 2f)
            )
        }

        // 4. Ilustración Holográfica de Torta de Cumpleaños de 2 Pisos con Velas Encendidas (Esquina derecha)
        val cakeCenterX = w * 0.84f
        val cakeBaseY = h * 0.84f
        val tier1W = 68.dp.toPx()
        val tier1H = 22.dp.toPx()
        val tier2W = 48.dp.toPx()
        val tier2H = 18.dp.toPx()

        // Base / Plato dorado de la torta
        drawRoundRect(
            brush = Brush.horizontalGradient(
                listOf(Color(0xFFF59E0B), Color(0xFFFDE68A), Color(0xFFF59E0B))
            ),
            topLeft = Offset(cakeCenterX - tier1W * 0.62f, cakeBaseY),
            size = Size(tier1W * 1.24f, 4.5.dp.toPx()),
            cornerRadius = CornerRadius(6f, 6f)
        )

        // Primer piso de la torta (inferior)
        drawRoundRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFFEC4899).copy(alpha = 0.55f), Color(0xFF831843).copy(alpha = 0.65f))
            ),
            topLeft = Offset(cakeCenterX - tier1W / 2f, cakeBaseY - tier1H),
            size = Size(tier1W, tier1H),
            cornerRadius = CornerRadius(10f, 10f)
        )
        drawRoundRect(
            color = Color(0xFFFFD700).copy(alpha = 0.75f),
            topLeft = Offset(cakeCenterX - tier1W / 2f, cakeBaseY - tier1H),
            size = Size(tier1W, tier1H),
            cornerRadius = CornerRadius(10f, 10f),
            style = Stroke(width = 1.4.dp.toPx())
        )

        // Segundo piso de la torta (superior)
        val tier2TopY = cakeBaseY - tier1H - tier2H
        drawRoundRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF00F0FF).copy(alpha = 0.50f), Color(0xFF1E3A8A).copy(alpha = 0.65f))
            ),
            topLeft = Offset(cakeCenterX - tier2W / 2f, tier2TopY),
            size = Size(tier2W, tier2H),
            cornerRadius = CornerRadius(8f, 8f)
        )
        drawRoundRect(
            color = Color(0xFF00F0FF).copy(alpha = 0.80f),
            topLeft = Offset(cakeCenterX - tier2W / 2f, tier2TopY),
            size = Size(tier2W, tier2H),
            cornerRadius = CornerRadius(8f, 8f),
            style = Stroke(width = 1.3.dp.toPx())
        )

        // 3 Velitas encendidas en la parte superior de la torta
        val candleOffsets = listOf(-13.dp.toPx(), 0f, 13.dp.toPx())
        val candleH = 12.dp.toPx()
        val candleW = 3.6.dp.toPx()
        candleOffsets.forEachIndexed { idx, dx ->
            val cx = cakeCenterX + dx
            val candleTop = tier2TopY - candleH
            // Cuerpo de la vela
            drawRoundRect(
                color = Color.White.copy(alpha = 0.90f),
                topLeft = Offset(cx - candleW / 2f, candleTop),
                size = Size(candleW, candleH),
                cornerRadius = CornerRadius(3f, 3f)
            )
            // Halo de la llama
            val flameY = candleTop - 5.dp.toPx() * flameFlicker
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFD700).copy(alpha = 0.85f),
                        Color(0xFFF97316).copy(alpha = 0.35f),
                        Color.Transparent
                    ),
                    center = Offset(cx, flameY),
                    radius = 9.dp.toPx() * flameFlicker
                ),
                center = Offset(cx, flameY),
                radius = 9.dp.toPx() * flameFlicker
            )
            // Núcleo de la llama
            drawOval(
                color = if (idx % 2 == 0) Color(0xFFFDE047) else Color(0xFFFB923C),
                topLeft = Offset(cx - 2.4.dp.toPx(), flameY - 4.dp.toPx()),
                size = Size(4.8.dp.toPx(), 8.dp.toPx() * flameFlicker)
            )
        }
    }
}

/**
 * Especificación de casilla fotográfica personalizable para el usuario.
 */
data class FuturisticPhotoSlotSpec(
    val slotKey: String,
    val title: String,
    val subtitle: String,
    val accentColor: Color = FuturisticDesignPalette.CyberCyan
)

/**
 * Galería Fotográfica Holográfica Multi-Casilla donde el usuario puede subir, cambiar,
 * ampliar o eliminar sus propias imágenes desde la galería del teléfono en cualquier pantalla.
 */
@Composable
fun FuturisticCustomPhotoGallerySection(
    sectionTitle: String,
    sectionSubtitle: String,
    slots: List<FuturisticPhotoSlotSpec>,
    uploadedMap: Map<String, String>,
    onUploadPhoto: (slotKey: String, uri: Uri) -> Unit,
    onRemovePhoto: (slotKey: String) -> Unit,
    accentColor: Color = FuturisticDesignPalette.CyberCyan,
    secondaryColor: Color = FuturisticDesignPalette.SacredGold,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeSlotKeyForPicker by remember { mutableStateOf<String?>(null) }
    var extraUserSlotsCount by remember { mutableIntStateOf(0) }
    var fullscreenPreviewUri by remember { mutableStateOf<String?>(null) }
    var fullscreenPreviewTitle by remember { mutableStateOf("") }

    val mediaPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        val targetKey = activeSlotKeyForPicker
        if (uri != null && targetKey != null) {
            onUploadPhoto(targetKey, uri)
        }
    }

    val allSlots = remember(slots, extraUserSlotsCount, uploadedMap.keys) {
        val baseKeys = slots.map { it.slotKey }.toSet()
        val prefix = slots.firstOrNull()?.slotKey?.substringBefore("_") ?: "custom"
        val savedExtraKeys = uploadedMap.keys
            .filter { it.startsWith("${prefix}_extra_") && it !in baseKeys }
            .sorted()
        val dynamicCount = maxOf(extraUserSlotsCount, savedExtraKeys.size)
        val extraSpecs = (1..dynamicCount).map { idx ->
            val key = "${prefix}_extra_$idx"
            FuturisticPhotoSlotSpec(
                slotKey = key,
                title = "Espacio Visual Extra #$idx",
                subtitle = "Toca para subir tu imagen personalizada",
                accentColor = FuturisticDesignPalette.accentForIndex(idx)
            )
        }
        slots + extraSpecs
    }

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = FuturisticDesignPalette.DeepObsidian.copy(alpha = 0.92f),
        modifier = modifier
            .fillMaxWidth()
            .futuristic3DCard(
                accentColor = accentColor,
                secondaryColor = secondaryColor,
                cornerRadius = 22.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF06152D),
                            Color(0xFF0B2144),
                            Color(0xFF14183E)
                        )
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = accentColor.copy(alpha = 0.20f),
                        border = BorderStroke(1.2.dp, accentColor),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Collections,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = sectionTitle,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 14.5.sp
                        )
                        Text(
                            text = sectionSubtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = FuturisticDesignPalette.GoldLuminous,
                            fontSize = 11.sp
                        )
                    }
                }

                // Botón para agregar más espacios de fotos dinámicamente
                Surface(
                    onClick = { extraUserSlotsCount += 1 },
                    shape = RoundedCornerShape(50),
                    color = secondaryColor.copy(alpha = 0.22f),
                    border = BorderStroke(1.dp, secondaryColor)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Añadir espacio",
                            tint = FuturisticDesignPalette.GoldLuminous,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "+ Espacio",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = FuturisticDesignPalette.GoldLuminous
                        )
                    }
                }
            }

            // Carrusel horizontal de tarjetas de imagen subibles
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                allSlots.forEach { slot ->
                    val uriStr = uploadedMap[slot.slotKey]
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF040D1E).copy(alpha = 0.90f),
                        border = BorderStroke(
                            1.4.dp,
                            Brush.linearGradient(
                                listOf(slot.accentColor, secondaryColor.copy(alpha = 0.7f))
                            )
                        ),
                        modifier = Modifier
                            .width(195.dp)
                            .height(172.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (!uriStr.isNullOrBlank()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(Uri.parse(uriStr))
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = slot.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable {
                                            fullscreenPreviewUri = uriStr
                                            fullscreenPreviewTitle = slot.title
                                        }
                                )
                                // Degradado inferior para título y controles
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Black.copy(alpha = 0.25f),
                                                    Color.Transparent,
                                                    Color(0xFF020617).copy(alpha = 0.92f)
                                                )
                                            )
                                        )
                                )
                                // Botones superiores (Cambiar / Quitar)
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        onClick = {
                                            activeSlotKeyForPicker = slot.slotKey
                                            mediaPicker.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        },
                                        shape = CircleShape,
                                        color = Color(0xFF040D1E).copy(alpha = 0.82f),
                                        border = BorderStroke(1.dp, slot.accentColor),
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Cambiar imagen",
                                                tint = slot.accentColor,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                    Surface(
                                        onClick = { onRemovePhoto(slot.slotKey) },
                                        shape = CircleShape,
                                        color = Color(0xFF7F1D1D).copy(alpha = 0.85f),
                                        border = BorderStroke(1.dp, Color(0xFFF87171)),
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.DeleteOutline,
                                                contentDescription = "Quitar imagen",
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                                // Pie de foto
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = slot.title,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Toca para ampliar • Subida desde tu galería",
                                        color = slot.accentColor,
                                        fontSize = 9.5.sp,
                                        maxLines = 1
                                    )
                                }
                            } else {
                                // Estado vacío listo para subir imagen
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable {
                                            activeSlotKeyForPicker = slot.slotKey
                                            mediaPicker.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        }
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = slot.accentColor.copy(alpha = 0.16f),
                                        border = BorderStroke(1.2.dp, slot.accentColor),
                                        modifier = Modifier.size(44.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.AddPhotoAlternate,
                                                contentDescription = null,
                                                tint = slot.accentColor,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = slot.title,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = slot.subtitle,
                                        color = Color(0xFF94A3B8),
                                        fontSize = 10.sp,
                                        textAlign = TextAlign.Center,
                                        maxLines = 2,
                                        lineHeight = 13.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = slot.accentColor.copy(alpha = 0.22f),
                                        border = BorderStroke(1.dp, slot.accentColor)
                                    ) {
                                        Text(
                                            text = "📤 Subir Imagen",
                                            color = slot.accentColor,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (fullscreenPreviewUri != null) {
        Dialog(onDismissRequest = { fullscreenPreviewUri = null }) {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = FuturisticDesignPalette.DeepObsidian),
                border = BorderStroke(1.5.dp, accentColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = fullscreenPreviewTitle,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                        IconButton(onClick = { fullscreenPreviewUri = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                        }
                    }
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(Uri.parse(fullscreenPreviewUri))
                            .crossfade(true)
                            .build(),
                        contentDescription = fullscreenPreviewTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }
        }
    }
}

/**
 * Vista Pública / Protección de Privacidad cuando una persona externa (no basiliano)
 * accede con un correo general. Oculta la información interna de los religiosos (cumpleaños,
 * teléfonos, casas, constituciones privadas) y muestra un espacio espiritual abierto para laicos,
 * permitiendo verificar el correo o código C.S.B. si el usuario es un religioso basiliano.
 */
@Composable
fun BasilianPrivateAccessRestrictedView(
    areaTitle: String,
    publicWelcomeTitle: String,
    publicWelcomeDescription: String,
    currentEmail: String?,
    onUnlockWithBasilianCode: (String) -> Boolean,
    onSwitchToBasilianPreview: () -> Unit,
    modifier: Modifier = Modifier
) {
    var codeInput by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var showVerifyBox by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = FuturisticDesignPalette.DeepObsidian.copy(alpha = 0.94f),
            modifier = Modifier
                .fillMaxWidth()
                .futuristic3DCard(
                    accentColor = FuturisticDesignPalette.CyberCyan,
                    secondaryColor = FuturisticDesignPalette.SacredGold,
                    cornerRadius = 22.dp
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF06162E),
                                Color(0xFF0F2547),
                                Color(0xFF19153E)
                            )
                        )
                    )
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = FuturisticDesignPalette.SacredGold.copy(alpha = 0.20f),
                        border = BorderStroke(1.2.dp, FuturisticDesignPalette.SacredGold),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                tint = FuturisticDesignPalette.GoldLuminous,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = FuturisticDesignPalette.CyberCyan.copy(alpha = 0.18f),
                            border = BorderStroke(1.dp, FuturisticDesignPalette.CyberCyan)
                        ) {
                            Text(
                                text = "🌐 VISTA PÚBLICA PARA AMIGOS Y LAICOS",
                                color = FuturisticDesignPalette.CyberCyan,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = publicWelcomeTitle,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp
                        )
                    }
                }

                Text(
                    text = publicWelcomeDescription,
                    color = Color(0xFFE2E8F0),
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
                )

                // Tarjeta de espiritualidad abierta para amigos de San Basilio
                FuturisticQuoteCalloutCard(
                    quoteText = "La bondad, la disciplina y la ciencia son semillas abiertas a todo corazón que busca a Dios en fraternidad.",
                    authorOrSource = "Espiritualidad de San Basilio Magno para Laicos y Amigos",
                    badgeLabel = "✨ COMUNIDAD ABIERTA"
                )

                // Explicación de privacidad basiliana + desbloqueo para religiosos C.S.B.
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.05f),
                    border = BorderStroke(1.dp, FuturisticDesignPalette.SacredGold.copy(alpha = 0.45f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = FuturisticDesignPalette.GoldLuminous,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Protección de Datos Internos Basilianos ($areaTitle)",
                                color = FuturisticDesignPalette.GoldLuminous,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "Tu correo actual (${currentEmail ?: "visitante"}) está configurado como visitante externo. La información propiamente privada de los religiosos basilianos (directorio personal, cumpleaños internos, teléfonos y documentos de gobierno C.S.B.) sólo aparece cuando accedes con un correo basiliano reconocido.",
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.5.sp,
                            lineHeight = 16.5.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showVerifyBox = !showVerifyBox },
                                border = BorderStroke(1.dp, FuturisticDesignPalette.CyberCyan),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = FuturisticDesignPalette.CyberCyan),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text("Soy Basiliano (Código)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = onSwitchToBasilianPreview,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = FuturisticDesignPalette.SacredGold,
                                    contentColor = Color(0xFF040D1E)
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text("Activar Vista Basiliana", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }

                        AnimatedVisibility(visible = showVerifyBox) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = codeInput,
                                    onValueChange = {
                                        codeInput = it
                                        feedbackMessage = null
                                    },
                                    label = { Text("Código Comunitario C.S.B.", color = FuturisticDesignPalette.CyberCyan) },
                                    placeholder = { Text("••••••••", color = Color.White.copy(alpha = 0.4f)) },
                                    visualTransformation = PasswordVisualTransformation(),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = FuturisticDesignPalette.CyberCyan,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                if (feedbackMessage != null) {
                                    Text(
                                        text = feedbackMessage!!,
                                        color = Color(0xFFF87171),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Button(
                                    onClick = {
                                        val ok = onUnlockWithBasilianCode(codeInput)
                                        if (!ok) {
                                            codeInput = ""
                                            feedbackMessage = "🚨 Código inválido. Intento registrado y notificado al sistema de seguridad."
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = FuturisticDesignPalette.CyberCyan,
                                        contentColor = Color(0xFF020617)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Verificar y Desbloquear Acceso Privado C.S.B.", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Superposición Espacial 3D que proyecta una retícula de perspectiva tridimensional,
 * poliedros sagrados girando en 3 ejes (X, Y, Z) y partículas de profundidad Z
 * sobre todas las pantallas sin ocultar el arte sacro existente.
 */
@Composable
fun Futuristic3DSpatialOverlay(
    isDark: Boolean,
    accentColor: Color = FuturisticDesignPalette.CyberCyan,
    goldColor: Color = FuturisticDesignPalette.SacredGold,
    modifier: Modifier = Modifier
) {
    val intensity = LocalFuturistic3DIntensity.current
    val infiniteTransition = rememberInfiniteTransition(label = "spatial_3d_overlay")

    val rotAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rot_3d_angle"
    )

    val gridFlow by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "grid_3d_flow"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val horizonY = h * 0.72f
        val vanishX = w * 0.5f
        val baseAlpha = (if (isDark) 0.26f else 0.18f) * intensity.glowAlpha

        // 1. Piso de Perspectiva 3D (Líneas de fuga hacia el punto de horizonte)
        for (i in -6..6) {
            val bottomX = vanishX + (i / 6f) * (w * 1.15f)
            drawLine(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        accentColor.copy(alpha = baseAlpha * 0.55f),
                        goldColor.copy(alpha = baseAlpha * 0.9f)
                    ),
                    startY = horizonY,
                    endY = h
                ),
                start = Offset(vanishX, horizonY),
                end = Offset(bottomX, h),
                strokeWidth = 1.2.dp.toPx()
            )
        }

        // Líneas horizontales de profundidad Z que avanzan hacia el espectador en 3D
        val depthSteps = 8
        for (step in 0 until depthSteps) {
            val progress = ((step + gridFlow) / depthSteps).coerceIn(0.02f, 1f)
            val zFactor = progress * progress
            val yPos = horizonY + (h - horizonY) * zFactor
            drawLine(
                color = accentColor.copy(alpha = (baseAlpha * zFactor).coerceIn(0f, 0.45f)),
                start = Offset(0f, yPos),
                end = Offset(w, yPos),
                strokeWidth = (0.8f + 1.8f * zFactor).dp.toPx()
            )
        }

        // 2. Techo de Perspectiva 3D sutil en la parte superior
        val topHorizonY = h * 0.18f
        for (i in -4..4) {
            val topX = vanishX + (i / 4f) * (w * 0.95f)
            drawLine(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        accentColor.copy(alpha = baseAlpha * 0.65f),
                        Color.Transparent
                    ),
                    startY = 0f,
                    endY = topHorizonY
                ),
                start = Offset(topX, 0f),
                end = Offset(vanishX, topHorizonY),
                strokeWidth = 1.dp.toPx()
            )
        }

        // 3. Poliedro Octaedro 3D girando en tiempo real (Esquina superior derecha)
        drawWireframeOctahedron3D(
            center = Offset(w * 0.87f, h * 0.16f),
            radius = w * 0.095f,
            angleDeg = rotAngle,
            primaryColor = accentColor.copy(alpha = (baseAlpha * 2.1f).coerceIn(0.1f, 0.65f)),
            vertexColor = goldColor.copy(alpha = (baseAlpha * 2.5f).coerceIn(0.15f, 0.85f))
        )

        // 4. Cubo Hiper-Holográfico 3D girando en sentido inverso (Esquina inferior izquierda)
        drawWireframeCube3D(
            center = Offset(w * 0.13f, h * 0.82f),
            halfSize = w * 0.065f,
            angleDeg = -rotAngle * 1.2f,
            lineColor = goldColor.copy(alpha = (baseAlpha * 1.9f).coerceIn(0.1f, 0.55f)),
            vertexColor = accentColor.copy(alpha = (baseAlpha * 2.3f).coerceIn(0.15f, 0.75f))
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawWireframeOctahedron3D(
    center: Offset,
    radius: Float,
    angleDeg: Float,
    primaryColor: Color,
    vertexColor: Color
) {
    val radY = Math.toRadians(angleDeg.toDouble())
    val radX = Math.toRadians((angleDeg * 0.65f).toDouble())

    val rawVertices = arrayOf(
        floatArrayOf(0f, -1.25f, 0f),
        floatArrayOf(0f, 1.25f, 0f),
        floatArrayOf(-1f, 0f, 0f),
        floatArrayOf(1f, 0f, 0f),
        floatArrayOf(0f, 0f, -1f),
        floatArrayOf(0f, 0f, 1f)
    )

    val projected = rawVertices.map { v ->
        project3DPoint(v[0], v[1], v[2], radX, radY, center, radius)
    }

    val edges = arrayOf(
        0 to 2, 0 to 3, 0 to 4, 0 to 5,
        1 to 2, 1 to 3, 1 to 4, 1 to 5,
        2 to 4, 4 to 3, 3 to 5, 5 to 2
    )

    edges.forEach { (a, b) ->
        drawLine(
            color = primaryColor,
            start = projected[a],
            end = projected[b],
            strokeWidth = 1.6.dp.toPx()
        )
    }

    projected.forEach { pt ->
        drawCircle(
            color = vertexColor,
            radius = 3.2.dp.toPx(),
            center = pt
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawWireframeCube3D(
    center: Offset,
    halfSize: Float,
    angleDeg: Float,
    lineColor: Color,
    vertexColor: Color
) {
    val radY = Math.toRadians(angleDeg.toDouble())
    val radX = Math.toRadians((angleDeg * 0.7f + 25f).toDouble())

    val rawVertices = arrayOf(
        floatArrayOf(-1f, -1f, -1f),
        floatArrayOf(1f, -1f, -1f),
        floatArrayOf(1f, 1f, -1f),
        floatArrayOf(-1f, 1f, -1f),
        floatArrayOf(-1f, -1f, 1f),
        floatArrayOf(1f, -1f, 1f),
        floatArrayOf(1f, 1f, 1f),
        floatArrayOf(-1f, 1f, 1f)
    )

    val projected = rawVertices.map { v ->
        project3DPoint(v[0], v[1], v[2], radX, radY, center, halfSize)
    }

    val edges = arrayOf(
        0 to 1, 1 to 2, 2 to 3, 3 to 0,
        4 to 5, 5 to 6, 6 to 7, 7 to 4,
        0 to 4, 1 to 5, 2 to 6, 3 to 7
    )

    edges.forEach { (a, b) ->
        drawLine(
            color = lineColor,
            start = projected[a],
            end = projected[b],
            strokeWidth = 1.5.dp.toPx()
        )
    }

    projected.forEach { pt ->
        drawCircle(
            color = vertexColor,
            radius = 2.8.dp.toPx(),
            center = pt
        )
    }
}

private fun project3DPoint(
    x: Float,
    y: Float,
    z: Float,
    radX: Double,
    radY: Double,
    center: Offset,
    scale: Float
): Offset {
    val cosY = cos(radY).toFloat()
    val sinY = sin(radY).toFloat()
    val x1 = x * cosY - z * sinY
    val z1 = x * sinY + z * cosY

    val cosX = cos(radX).toFloat()
    val sinX = sin(radX).toFloat()
    val y2 = y * cosX - z1 * sinX
    val z2 = y * sinX + z1 * cosX

    val fov = 3.6f
    val perspective = fov / (fov + z2 + 2.2f)
    return Offset(
        x = center.x + x1 * scale * perspective * 1.6f,
        y = center.y + y2 * scale * perspective * 1.6f
    )
}

/**
 * Insignia 3D interactiva con un poliedro giratorio en vivo para encabezados futuristas.
 */
@Composable
fun Rotating3DPolyhedronBadge(
    size: Dp = 44.dp,
    accentColor: Color = FuturisticDesignPalette.CyberCyan,
    goldColor: Color = FuturisticDesignPalette.SacredGold
) {
    val infiniteTransition = rememberInfiniteTransition(label = "badge_3d_poly")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "badge_angle"
    )

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.35f),
                        Color(0xFF040D1A).copy(alpha = 0.94f)
                    )
                )
            )
            .border(1.5.dp, Brush.sweepGradient(listOf(accentColor, goldColor, accentColor)), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawWireframeOctahedron3D(
                center = Offset(this.size.width / 2f, this.size.height / 2f),
                radius = this.size.width * 0.34f,
                angleDeg = angle,
                primaryColor = accentColor,
                vertexColor = goldColor
            )
        }
    }
}

/**
 * Banner Superior 3D Holográfico y Futurista con Diseño Editorial de Alto Contraste.
 * Exclusivo para la primera pantalla (Oración Diaria).
 */
@Composable
fun Futuristic3DHoloHeaderBanner(
    moduleTitle: String,
    moduleSubtitle: String,
    userName: String?,
    currentIntensity: Futuristic3DIntensity,
    onSelectIntensity: (Futuristic3DIntensity) -> Unit,
    onActivateJarvisBg: () -> Unit = {},
    featuredQuote: String? = null,
    featuredQuoteAuthor: String? = null,
    accentColor: Color = FuturisticDesignPalette.CyberCyan,
    secondaryColor: Color = FuturisticDesignPalette.SacredGold,
    modifier: Modifier = Modifier
) {
    val displayName = userName?.trim()?.takeIf { it.isNotBlank() } ?: "Hermano(a) en Cristo"

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = FuturisticDesignPalette.DeepObsidian.copy(alpha = 0.94f),
        modifier = modifier
            .fillMaxWidth()
            .futuristic3DCard(
                accentColor = accentColor,
                secondaryColor = secondaryColor,
                cornerRadius = 22.dp
            )
            .testTag("futuristic_3d_holo_banner")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF041022),
                            Color(0xFF0D2346),
                            Color(0xFF13163B)
                        )
                    )
                )
                .padding(horizontal = 15.dp, vertical = 13.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Rotating3DPolyhedronBadge(size = 46.dp, accentColor = accentColor, goldColor = secondaryColor)

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = secondaryColor.copy(alpha = 0.22f),
                            border = BorderStroke(1.dp, secondaryColor.copy(alpha = 0.85f))
                        ) {
                            Text(
                                text = "✨ HOLA, ${displayName.uppercase()}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = FuturisticDesignPalette.GoldLuminous,
                                fontSize = 9.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = accentColor.copy(alpha = 0.16f),
                            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.65f))
                        ) {
                            Text(
                                text = "C.S.B. 3D",
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.ExtraBold,
                                color = accentColor,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = moduleTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = moduleSubtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF7DD3FC),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (!featuredQuote.isNullOrBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.07f),
                    border = BorderStroke(1.dp, secondaryColor.copy(alpha = 0.55f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(3.5.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Brush.verticalGradient(listOf(secondaryColor, accentColor)))
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "«${featuredQuote.trim().removeSurrounding("«", "»")}»",
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Serif,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = FuturisticDesignPalette.GoldLuminous,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (!featuredQuoteAuthor.isNullOrBlank()) {
                                Text(
                                    text = "— $featuredQuoteAuthor",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = accentColor,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Futuristic3DIntensity.entries.forEach { mode ->
                    val isSelected = currentIntensity == mode
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (isSelected) accentColor.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.07f),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) accentColor else Color.White.copy(alpha = 0.22f)
                        ),
                        modifier = Modifier.clickable { onSelectIntensity(mode) }
                    ) {
                        Text(
                            text = mode.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                            color = if (isSelected) accentColor else Color(0xFFE2E8F0),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = secondaryColor.copy(alpha = 0.22f),
                    border = BorderStroke(1.dp, secondaryColor.copy(alpha = 0.80f)),
                    modifier = Modifier.clickable { onActivateJarvisBg() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = FuturisticDesignPalette.GoldLuminous,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Holograma Sacro 3D",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = FuturisticDesignPalette.GoldLuminous,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}
