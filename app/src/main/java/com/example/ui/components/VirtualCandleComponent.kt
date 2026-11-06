package com.example.ui.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.theme.BasilianAppTheme

/**
 * Interactive 'Virtual Candle' component designed for the 'En Memoria' / Memorial section.
 * Features realistic Compose Canvas animations simulating a flickering flame:
 * - Fluid vertical stretching & horizontal flutter
 * - Warm ambient pulsing radiant aura / light halo
 * - Multilayer teardrop flame (inner white-hot core, vivid amber/yellow body, soft blue base)
 * - Shaded cylindrical wax pillar with melted basin & wick
 * - Interactive tap: smooth lighting / extinguishing transition
 */
@Composable
fun VirtualCandleComponent(
    isLit: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    candleHeight: Dp = 130.dp,
    candleWidth: Dp = 100.dp,
    appTheme: BasilianAppTheme = BasilianAppTheme.ESCUDO_BASILIANO,
    appLanguage: AppLanguage = AppLanguage.ES,
    showInteractiveLabel: Boolean = true
) {
    // Smooth transition between lit (1f) and unlit (0f)
    val burnIntensity by animateFloatAsState(
        targetValue = if (isLit) 1f else 0f,
        animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing),
        label = "burn_intensity"
    )

    // Staggered infinite animations simulating organic draft & flame flicker
    val flickerTransition = rememberInfiniteTransition(label = "candle_flicker_transition")

    val flameScaleY by flickerTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.14f,
        animationSpec = infiniteRepeatable(
            animation = tween(420, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_scale_y"
    )

    val flameScaleX by flickerTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(310, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_scale_x"
    )

    val flameSway by flickerTransition.animateFloat(
        initialValue = -3.2f,
        targetValue = 3.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(670, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_sway"
    )

    val auraAlpha by flickerTransition.animateFloat(
        initialValue = 0.28f,
        targetValue = 0.52f,
        animationSpec = infiniteRepeatable(
            animation = tween(530, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_alpha"
    )

    val auraRadiusScale by flickerTransition.animateFloat(
        initialValue = 0.90f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(790, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_radius_scale"
    )

    val statusText = when {
        isLit -> when (appLanguage) {
            AppLanguage.EN -> "🕯️ Candle Lit • In Memoriam"
            AppLanguage.FR -> "🕯️ Cierge Allumé • En Mémoire"
            else -> "🕯️ Cirio Encendido • En Memoria"
        }
        else -> when (appLanguage) {
            AppLanguage.EN -> "🤍 Tap to light candle"
            AppLanguage.FR -> "🤍 Toucher pour allumer"
            else -> "🤍 Toca para encender el cirio"
        }
    }

    val a11yDesc = when {
        isLit -> when (appLanguage) {
            AppLanguage.EN -> "Lit memorial candle. Tap to extinguish."
            AppLanguage.FR -> "Cierge mémorial allumé. Touchez pour éteindre."
            else -> "Cirio memorial encendido. Toca para apagar."
        }
        else -> when (appLanguage) {
            AppLanguage.EN -> "Unlit memorial candle. Tap to light."
            AppLanguage.FR -> "Cierge mémorial éteint. Touchez pour allumer."
            else -> "Cirio memorial apagado. Toca para encender."
        }
    }

    Column(
        modifier = modifier
            .testTag("virtual_candle_component")
            .semantics {
                role = Role.Button
                contentDescription = a11yDesc
            }
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onToggle)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Canvas rendering the candle body, wick, halo, and animated flame
        Canvas(
            modifier = Modifier
                .size(width = candleWidth, height = candleHeight)
                .testTag("candle_canvas")
        ) {
            val width = size.width
            val height = size.height

            // Dimensions of candle wax body
            val pillarWidth = width * 0.44f
            val pillarHeight = height * 0.46f
            val pillarLeft = (width - pillarWidth) / 2f
            val pillarTop = height * 0.48f

            // Wick position
            val wickBaseX = width / 2f
            val wickBaseY = pillarTop + 4f
            val wickTopX = wickBaseX - 1.5f
            val wickTopY = pillarTop - 14f

            // Flame center & metrics
            val flameCenterX = wickTopX + (flameSway * burnIntensity)
            val flameBaseY = wickTopY + 2f
            val flameMaxHeight = height * 0.38f * flameScaleY * burnIntensity
            val flameMaxWidth = width * 0.22f * flameScaleX * burnIntensity

            // 1. Warm ambient halo / aura when lit
            if (burnIntensity > 0.05f) {
                val auraRadius = (width * 0.48f) * auraRadiusScale * burnIntensity
                val haloBrush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFB300).copy(alpha = auraAlpha * burnIntensity),
                        Color(0xFFFF8F00).copy(alpha = (auraAlpha * 0.6f) * burnIntensity),
                        Color(0xFFFF6F00).copy(alpha = (auraAlpha * 0.2f) * burnIntensity),
                        Color.Transparent
                    ),
                    center = Offset(flameCenterX, flameBaseY - flameMaxHeight * 0.5f),
                    radius = auraRadius.coerceAtLeast(10f)
                )
                drawCircle(
                    brush = haloBrush,
                    radius = auraRadius,
                    center = Offset(flameCenterX, flameBaseY - flameMaxHeight * 0.5f)
                )
            }

            // 2. Pillar Candle Body (Ivory Wax with 3D gradient)
            val waxBrush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFE8DDC6), // subtle shadow on left edge
                    Color(0xFFFFFDF8), // bright highlight
                    Color(0xFFF9F3E3), // creamy center
                    Color(0xFFE8DFC8), // soft warm body
                    Color(0xFFD4C5A5)  // shadow on right edge
                ),
                startX = pillarLeft,
                endX = pillarLeft + pillarWidth
            )
            drawRoundRect(
                brush = waxBrush,
                topLeft = Offset(pillarLeft, pillarTop),
                size = Size(pillarWidth, pillarHeight),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // Subtle wax drip on the left side
            val dripPath = Path().apply {
                moveTo(pillarLeft + 6f, pillarTop)
                cubicTo(
                    pillarLeft + 6f, pillarTop + 14f,
                    pillarLeft + 10f, pillarTop + 20f,
                    pillarLeft + 7f, pillarTop + 26f
                )
                cubicTo(
                    pillarLeft + 5f, pillarTop + 28f,
                    pillarLeft + 3f, pillarTop + 24f,
                    pillarLeft + 4f, pillarTop
                )
                close()
            }
            drawPath(
                path = dripPath,
                color = Color(0xFFFFFDF8).copy(alpha = 0.9f)
            )

            // Top melted wax basin (perspective oval)
            val basinBrush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF8D7B68).copy(alpha = 0.5f), // dark center around wick
                    Color(0xFFF4ECD8),
                    Color(0xFFFFFDF8)
                ),
                center = Offset(width / 2f, pillarTop + 3f),
                radius = pillarWidth * 0.5f
            )
            drawOval(
                brush = basinBrush,
                topLeft = Offset(pillarLeft, pillarTop - 4f),
                size = Size(pillarWidth, 10f)
            )

            // 3. Candle Wick
            val wickPath = Path().apply {
                moveTo(wickBaseX, wickBaseY)
                quadraticBezierTo(
                    wickBaseX + 2f, (wickBaseY + wickTopY) / 2f,
                    wickTopX, wickTopY
                )
            }
            drawPath(
                path = wickPath,
                color = Color(0xFF212121),
                style = Stroke(width = 3.5f, cap = StrokeCap.Round)
            )

            // Glowing ember on the wick tip
            val emberColor = if (burnIntensity > 0.2f) {
                Color(0xFFFF3D00).copy(alpha = burnIntensity)
            } else {
                Color(0xFF757575)
            }
            drawCircle(
                color = emberColor,
                radius = 2.2f,
                center = Offset(wickTopX, wickTopY)
            )

            // 4. Multilayer Flickering Flame
            if (burnIntensity > 0.05f && flameMaxHeight > 4f) {
                drawMultilayerFlame(
                    flameCenterX = flameCenterX,
                    flameBaseY = flameBaseY,
                    flameHeight = flameMaxHeight,
                    flameWidth = flameMaxWidth,
                    intensity = burnIntensity
                )
            }
        }

        if (showInteractiveLabel) {
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isLit) appTheme.goldColor.copy(alpha = 0.16f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isLit) appTheme.goldColor.copy(alpha = 0.5f) else Color.Transparent
                )
            ) {
                Text(
                    text = statusText,
                    fontSize = 11.sp,
                    fontWeight = if (isLit) FontWeight.Bold else FontWeight.Medium,
                    color = if (isLit) appTheme.primaryColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

/**
 * Draws the realistic multilayer flame using Compose Path and Brush gradients:
 * 1. Base subtle blue hue (real candle reaction zone)
 * 2. Outer flame: warm amber/orange teardrop
 * 3. Mid flame: rich luminous yellow teardrop
 * 4. Inner core: white-hot brilliant yellow/white nucleus
 */
private fun DrawScope.drawMultilayerFlame(
    flameCenterX: Float,
    flameBaseY: Float,
    flameHeight: Float,
    flameWidth: Float,
    intensity: Float
) {
    val flameTipY = flameBaseY - flameHeight

    // 1. Base blue flame cone
    val blueBaseWidth = flameWidth * 0.65f
    val blueBaseHeight = flameHeight * 0.22f
    val bluePath = Path().apply {
        moveTo(flameCenterX - blueBaseWidth / 2f, flameBaseY)
        cubicTo(
            flameCenterX - blueBaseWidth * 0.4f, flameBaseY - blueBaseHeight * 0.5f,
            flameCenterX + blueBaseWidth * 0.4f, flameBaseY - blueBaseHeight * 0.5f,
            flameCenterX + blueBaseWidth / 2f, flameBaseY
        )
        close()
    }
    drawPath(
        path = bluePath,
        color = Color(0x6642A5F5).copy(alpha = 0.55f * intensity),
        style = Fill
    )

    // 2. Outer teardrop flame (Amber / Vivid Orange)
    val outerFlamePath = createTeardropPath(
        centerX = flameCenterX,
        baseY = flameBaseY,
        tipY = flameTipY,
        halfWidth = flameWidth * 0.5f
    )
    val outerFlameBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFF3D00).copy(alpha = 0.95f * intensity), // reddish tip
            Color(0xFFFF9100).copy(alpha = 0.98f * intensity), // vibrant orange
            Color(0xFFFFC107).copy(alpha = 1.0f * intensity)   // golden base
        ),
        startY = flameTipY,
        endY = flameBaseY
    )
    drawPath(
        path = outerFlamePath,
        brush = outerFlameBrush,
        style = Fill
    )

    // 3. Mid flame (Brilliant Yellow)
    val midFlameHeight = flameHeight * 0.72f
    val midFlameWidth = flameWidth * 0.65f
    val midFlameTipY = flameBaseY - midFlameHeight
    val midFlamePath = createTeardropPath(
        centerX = flameCenterX,
        baseY = flameBaseY - 1f,
        tipY = midFlameTipY,
        halfWidth = midFlameWidth * 0.5f
    )
    val midFlameBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFD54F).copy(alpha = 0.95f * intensity),
            Color(0xFFFFEE58).copy(alpha = 1.0f * intensity)
        ),
        startY = midFlameTipY,
        endY = flameBaseY
    )
    drawPath(
        path = midFlamePath,
        brush = midFlameBrush,
        style = Fill
    )

    // 4. Inner core (White-hot heart)
    val coreHeight = flameHeight * 0.44f
    val coreWidth = flameWidth * 0.35f
    val coreTipY = flameBaseY - coreHeight
    val corePath = createTeardropPath(
        centerX = flameCenterX,
        baseY = flameBaseY - 2f,
        tipY = coreTipY,
        halfWidth = coreWidth * 0.5f
    )
    drawPath(
        path = corePath,
        color = Color(0xFFFFFDE7).copy(alpha = 0.95f * intensity),
        style = Fill
    )
}

/**
 * Creates a fluid organic teardrop curve for candle flames.
 */
private fun createTeardropPath(
    centerX: Float,
    baseY: Float,
    tipY: Float,
    halfWidth: Float
): Path {
    val totalHeight = baseY - tipY
    val bellyY = baseY - totalHeight * 0.35f

    return Path().apply {
        moveTo(centerX, tipY)
        // Right side curve from tip down to belly and base
        cubicTo(
            centerX + halfWidth * 0.35f, tipY + totalHeight * 0.35f,
            centerX + halfWidth, bellyY,
            centerX + halfWidth * 0.4f, baseY
        )
        // Bottom subtle curve across the base
        quadraticBezierTo(
            centerX, baseY + 1f,
            centerX - halfWidth * 0.4f, baseY
        )
        // Left side curve from base back up through belly to tip
        cubicTo(
            centerX - halfWidth, bellyY,
            centerX - halfWidth * 0.35f, tipY + totalHeight * 0.35f,
            centerX, tipY
        )
        close()
    }
}

/**
 * Compact / Miniature Virtual Candle designed for list cards (e.g. DeceasedBrotherCard).
 * Renders an animated miniature flame with flickers when lit, and an unlit candle wick when extinguished.
 */
@Composable
fun VirtualCandleMini(
    isLit: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    appTheme: BasilianAppTheme = BasilianAppTheme.ESCUDO_BASILIANO,
    size: Dp = 38.dp
) {
    val burnIntensity by animateFloatAsState(
        targetValue = if (isLit) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "mini_burn"
    )

    val flickerTransition = rememberInfiniteTransition(label = "mini_candle_flicker")
    val flameScaleY by flickerTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.14f,
        animationSpec = infiniteRepeatable(
            animation = tween(380, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mini_scale_y"
    )
    val flameSway by flickerTransition.animateFloat(
        initialValue = -1.8f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(520, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mini_sway"
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                if (isLit) appTheme.goldColor.copy(alpha = 0.18f)
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
            )
            .clickable(onClick = onToggle),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.85f)) {
            val w = this.size.width
            val h = this.size.height

            // Mini candle wax body
            val pWidth = w * 0.38f
            val pHeight = h * 0.38f
            val pLeft = (w - pWidth) / 2f
            val pTop = h * 0.52f

            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFE0D5BA), Color(0xFFFFFDF8), Color(0xFFD4C5A5)),
                    startX = pLeft,
                    endX = pLeft + pWidth
                ),
                topLeft = Offset(pLeft, pTop),
                size = Size(pWidth, pHeight),
                cornerRadius = CornerRadius(4f, 4f)
            )

            // Mini wick
            val wickX = w / 2f
            val wickTopY = pTop - 5f
            drawLine(
                color = Color(0xFF263238),
                start = Offset(wickX, pTop),
                end = Offset(wickX, wickTopY),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )

            // Mini flame when lit
            if (burnIntensity > 0.05f) {
                val fHeight = (h * 0.36f) * flameScaleY * burnIntensity
                val fWidth = (w * 0.28f) * burnIntensity
                val fCenterX = wickX + (flameSway * burnIntensity)
                val fBaseY = wickTopY + 1f

                // Outer flame
                val path = createTeardropPath(
                    centerX = fCenterX,
                    baseY = fBaseY,
                    tipY = fBaseY - fHeight,
                    halfWidth = fWidth * 0.5f
                )
                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFF3D00), Color(0xFFFFB300), Color(0xFFFFEE58)),
                        startY = fBaseY - fHeight,
                        endY = fBaseY
                    )
                )

                // White core
                val coreP = createTeardropPath(
                    centerX = fCenterX,
                    baseY = fBaseY,
                    tipY = fBaseY - fHeight * 0.45f,
                    halfWidth = fWidth * 0.22f
                )
                drawPath(path = coreP, color = Color.White.copy(alpha = 0.9f))
            } else {
                // Dim resting dot
                drawCircle(
                    color = Color.Gray.copy(alpha = 0.5f),
                    radius = 1.8f,
                    center = Offset(wickX, wickTopY)
                )
            }
        }
    }
}
