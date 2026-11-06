package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Trazo orgánico / pincel a mano alzada para subrayar títulos de forma artesanal.
 * Posee un suave desnivel y curvatura con un lavado base de marcador acuarela y
 * un trazo principal definido, evitando rigideces rectas.
 */
@Composable
fun OrganicBrushUnderline(
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
    height: Dp = 6.dp,
    strokeWidth: Float = 5f
) {
    Canvas(
        modifier = modifier
            .height(height)
    ) {
        val w = size.width
        val h = size.height
        val midY = h / 2f

        // 1. Lavado base de marcador suave translúcido
        val basePath = Path().apply {
            moveTo(2f, midY + 1.2f)
            cubicTo(
                w * 0.28f, midY - 2.5f,
                w * 0.68f, midY + 2.8f,
                w - 2f, midY - 0.4f
            )
        }
        drawPath(
            path = basePath,
            color = color.copy(alpha = 0.28f),
            style = Stroke(
                width = strokeWidth + 4.5f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // 2. Trazo artesanal a mano alzada con curvatura sutil
        val penPath = Path().apply {
            moveTo(4f, midY + 0.6f)
            cubicTo(
                w * 0.26f, midY - 1.8f,
                w * 0.72f, midY + 2.2f,
                w - 3f, midY + 0.3f
            )
        }
        drawPath(
            path = penPath,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

/**
 * Cabecera temática con icono/emoji incrustado en una pastilla/fondo circular difuminado
 * y un subrayado orgánico con efecto de trazo a mano alzada debajo del título.
 */
@Composable
fun CraftedSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    emoji: String? = null,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    pillBgColor: Color? = null,
    underlineWidth: Dp = 130.dp,
    titleSize: Int = 18
) {
    val resolvedPillBg = pillBgColor ?: accentColor.copy(alpha = 0.14f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (icon != null || emoji != null) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(resolvedPillBg),
                    contentAlignment = Alignment.Center
                ) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(18.dp)
                        )
                    } else if (emoji != null) {
                        Text(text = emoji, fontSize = 16.sp)
                    }
                }
            }

            Column(modifier = Modifier.weight(1f, fill = false)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = titleSize.sp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.5.sp
                    )
                }
            }
        }

        // Subrayado con trazo de pincel orgánico a mano alzada
        Spacer(modifier = Modifier.height(3.dp))
        OrganicBrushUnderline(
            color = accentColor,
            modifier = Modifier
                .padding(start = if (icon != null || emoji != null) 44.dp else 2.dp)
                .width(underlineWidth)
        )
    }
}
