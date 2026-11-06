package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * CompositionLocal que almacena la URI del escudo oficial personalizado subido por el usuario.
 */
val LocalCustomCrestUri = compositionLocalOf<String?> { null }

/**
 * Escudo Oficial Auténtico de la Congregación de San Basilio (Padres Basilianos - C.S.B.)
 * Si el usuario ha subido su imagen oficial del escudo, la renderiza con alta fidelidad.
 * En caso contrario, dibuja el escudo heráldico vectorial fiel al de 1822.
 */
@Composable
fun BasilianCrestView(
    modifier: Modifier = Modifier,
    size: Dp = 130.dp,
    showMottoText: Boolean = true,
    customCrestUri: String? = LocalCustomCrestUri.current,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.then(
            if (onClick != null) {
                Modifier.clickable(
                    onClickLabel = "Volver al Inicio",
                    onClick = onClick
                )
            } else Modifier
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .shadow(8.dp, CircleShape)
                .background(Color.White, shape = CircleShape)
                .clip(CircleShape)
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            if (!customCrestUri.isNullOrBlank()) {
                AsyncImage(
                    model = customCrestUri,
                    contentDescription = "Escudo Oficial de la Congregación de San Basilio",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = this.size.width
                val canvasHeight = this.size.height
                val center = Offset(canvasWidth / 2f, canvasHeight / 2f)
                val outerRadius = canvasWidth / 2f - 2.dp.toPx()

                // Fondo blanco del sello circular
                drawCircle(
                    color = Color.White,
                    radius = outerRadius,
                    center = center
                )

                // 1. Doble anillo negro exterior
                drawCircle(
                    color = Color(0xFF111827),
                    radius = outerRadius,
                    center = center,
                    style = Stroke(width = 2.5f.dp.toPx())
                )
                drawCircle(
                    color = Color(0xFF111827),
                    radius = outerRadius - 3.5f.dp.toPx(),
                    center = center,
                    style = Stroke(width = 1.dp.toPx())
                )

                // 2. Anillo interior que delimita la orla del lema
                val innerRimRadius = outerRadius * 0.72f
                drawCircle(
                    color = Color(0xFF111827),
                    radius = innerRimRadius,
                    center = center,
                    style = Stroke(width = 1.6f.dp.toPx())
                )

                // Puntos decorativos en la orla que evocan las palabras del lema
                val ringTextRadius = (outerRadius + innerRimRadius) / 2f
                val dotColor = Color(0xFF111827)
                val dotCount = 14
                for (i in 0 until dotCount) {
                    val angleDeg = 150f + i * (240f / (dotCount - 1))
                    val rad = Math.toRadians(angleDeg.toDouble())
                    val dx = center.x + ringTextRadius * cos(rad).toFloat()
                    val dy = center.y + ringTextRadius * sin(rad).toFloat()
                    drawCircle(color = dotColor, radius = 1.2f.dp.toPx(), center = Offset(dx, dy))
                }

                // 3. Escudo Francés apuntado central (Heater Shield)
                val shieldWidth = innerRimRadius * 1.52f
                val shieldHeight = innerRimRadius * 1.76f
                val shieldLeft = center.x - shieldWidth / 2f
                val shieldTop = center.y - shieldHeight / 2f - 2.dp.toPx()
                val midX = center.x
                val midY = shieldTop + shieldHeight * 0.50f

                // Silueta completa del escudo
                val shieldPath = Path().apply {
                    moveTo(shieldLeft, shieldTop)
                    lineTo(shieldLeft + shieldWidth, shieldTop)
                    lineTo(shieldLeft + shieldWidth, shieldTop + shieldHeight * 0.55f)
                    cubicTo(
                        shieldLeft + shieldWidth, shieldTop + shieldHeight * 0.85f,
                        center.x, shieldTop + shieldHeight,
                        center.x, shieldTop + shieldHeight
                    )
                    cubicTo(
                        center.x, shieldTop + shieldHeight,
                        shieldLeft, shieldTop + shieldHeight * 0.85f,
                        shieldLeft, shieldTop + shieldHeight * 0.55f
                    )
                    close()
                }

                // Cuartel 1 (Top-Left): VERDE
                val q1Path = Path().apply {
                    moveTo(shieldLeft, shieldTop)
                    lineTo(midX, shieldTop)
                    lineTo(midX, midY)
                    lineTo(shieldLeft, midY)
                    close()
                }
                drawPath(path = q1Path, color = ShieldGreen)

                // Cuartel 2 (Top-Right): ROJO
                val q2Path = Path().apply {
                    moveTo(midX, shieldTop)
                    lineTo(shieldLeft + shieldWidth, shieldTop)
                    lineTo(shieldLeft + shieldWidth, midY)
                    lineTo(midX, midY)
                    close()
                }
                drawPath(path = q2Path, color = ShieldRed)

                // Cuartel 3 (Bottom-Left): ROJO
                val q3Path = Path().apply {
                    moveTo(shieldLeft, midY)
                    lineTo(midX, midY)
                    lineTo(midX, shieldTop + shieldHeight)
                    cubicTo(
                        center.x, shieldTop + shieldHeight,
                        shieldLeft, shieldTop + shieldHeight * 0.85f,
                        shieldLeft, shieldTop + shieldHeight * 0.55f
                    )
                    lineTo(shieldLeft, midY)
                    close()
                }
                drawPath(path = q3Path, color = ShieldRed)

                // Cuartel 4 (Bottom-Right): VERDE
                val q4Path = Path().apply {
                    moveTo(midX, midY)
                    lineTo(shieldLeft + shieldWidth, midY)
                    lineTo(shieldLeft + shieldWidth, shieldTop + shieldHeight * 0.55f)
                    cubicTo(
                        shieldLeft + shieldWidth, shieldTop + shieldHeight * 0.85f,
                        center.x, shieldTop + shieldHeight,
                        center.x, shieldTop + shieldHeight
                    )
                    close()
                }
                drawPath(path = q4Path, color = ShieldGreen)

                // ==========================================
                // FIGURAS HERÁLDICAS EN BLANCO Y NEGRO
                // ==========================================

                // CUARTEL 1: CÁLIZ BLANCO (Top-Left, sobre fondo verde)
                val c1CenterX = shieldLeft + shieldWidth * 0.25f
                val c1CenterY = shieldTop + shieldHeight * 0.24f
                drawOfficialWhiteChalice(c1CenterX, c1CenterY, shieldWidth * 0.38f)

                // CUARTEL 2: LIBRO ABIERTO (Top-Right, sobre fondo rojo)
                val c2CenterX = midX + shieldWidth * 0.25f
                val c2CenterY = shieldTop + shieldHeight * 0.24f
                drawOfficialOpenBook(c2CenterX, c2CenterY, shieldWidth * 0.40f)

                // CUARTEL 3: RESPLANDOR Y FLOR DE LIS (Bottom-Left, sobre fondo rojo)
                val c3CenterX = shieldLeft + shieldWidth * 0.26f
                val c3CenterY = midY + shieldHeight * 0.21f
                drawOfficialSunburstFleurDeLis(c3CenterX, c3CenterY, shieldWidth * 0.36f)

                // CUARTEL 4: CRUZ PATÉ BLANCA (Bottom-Right, sobre fondo verde)
                val c4CenterX = midX + shieldWidth * 0.24f
                val c4CenterY = midY + shieldHeight * 0.21f
                drawOfficialWhitePateeCross(c4CenterX, c4CenterY, shieldWidth * 0.32f)

                // Líneas divisorias en cruz del escudo (Negro nítido)
                drawLine(
                    color = Color(0xFF111827),
                    start = Offset(shieldLeft, midY),
                    end = Offset(shieldLeft + shieldWidth, midY),
                    strokeWidth = 1.8f.dp.toPx()
                )
                drawLine(
                    color = Color(0xFF111827),
                    start = Offset(midX, shieldTop),
                    end = Offset(midX, shieldTop + shieldHeight),
                    strokeWidth = 1.8f.dp.toPx()
                )

                // Contorno negro del escudo
                drawPath(
                    path = shieldPath,
                    color = Color(0xFF111827),
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // 4. Cinta inferior con "1822"
                val ribbonBottom = canvasHeight - 2.dp.toPx()
                val ribbonWidth = shieldWidth * 0.95f
                val ribbonHeight = 16.dp.toPx()
                val ribbonLeft = center.x - ribbonWidth / 2f
                val ribbonTop = ribbonBottom - ribbonHeight

                val ribbonPath = Path().apply {
                    moveTo(ribbonLeft, ribbonTop + 4.dp.toPx())
                    cubicTo(ribbonLeft + ribbonWidth * 0.25f, ribbonTop, ribbonLeft + ribbonWidth * 0.75f, ribbonTop, ribbonLeft + ribbonWidth, ribbonTop + 4.dp.toPx())
                    lineTo(ribbonLeft + ribbonWidth - 3.dp.toPx(), ribbonBottom)
                    cubicTo(ribbonLeft + ribbonWidth * 0.75f, ribbonBottom - 4.dp.toPx(), ribbonLeft + ribbonWidth * 0.25f, ribbonBottom - 4.dp.toPx(), ribbonLeft + 3.dp.toPx(), ribbonBottom)
                    close()
                }

                // Fondo blanco de la cinta
                drawPath(path = ribbonPath, color = Color.White)
                drawPath(path = ribbonPath, color = Color(0xFF111827), style = Stroke(width = 1.4f.dp.toPx()))

                // Cruces diamantadas y puntos rojos junto al 1822
                drawCircle(color = ShieldRed, radius = 1.5f.dp.toPx(), center = Offset(center.x - 16.dp.toPx(), ribbonTop + ribbonHeight * 0.5f))
                drawCircle(color = ShieldRed, radius = 1.5f.dp.toPx(), center = Offset(center.x + 16.dp.toPx(), ribbonTop + ribbonHeight * 0.5f))
            }

            // Año 1822 superpuesto en la cinta en tipografía serif roja idéntica al escudo
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 4.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = "܀ 1822 ܀",
                    color = ShieldRed,
                    fontWeight = FontWeight.Black,
                    fontSize = (size.value * 0.095f).sp,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 0.5.sp
                )
            }
            }
        }

        if (showMottoText) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "• ENSEÑAME • BONDAD • DISCIPLINA • Y • CONOCIMIENTO •",
                style = MaterialTheme.typography.labelSmall,
                color = ShieldGreenDark,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.8.sp,
                textAlign = TextAlign.Center,
                fontSize = 9.sp
            )
            Text(
                text = "Congregación de San Basilio (Padres Basilianos • C.S.B.)",
                style = MaterialTheme.typography.bodySmall,
                color = ShieldRedDark,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// -----------------------------------------------------------------
// Figuras heráldicas idénticas a la imagen oficial
// -----------------------------------------------------------------

/**
 * Cáliz sacerdotal blanco con copa acampanada, nudo y base patena.
 */
private fun DrawScope.drawOfficialWhiteChalice(cx: Float, cy: Float, size: Float) {
    val w = size * 0.72f
    val h = size * 0.82f
    val top = cy - h / 2f

    val chalicePath = Path().apply {
        // Labio superior
        moveTo(cx - w / 2f, top)
        lineTo(cx + w / 2f, top)
        // Copa acampanada
        cubicTo(
            cx + w / 2f, top + h * 0.38f,
            cx + w * 0.16f, top + h * 0.48f,
            cx + w * 0.12f, top + h * 0.55f
        )
        // Tallo
        lineTo(cx + w * 0.10f, top + h * 0.75f)
        // Base abocardada
        lineTo(cx + w * 0.45f, top + h)
        lineTo(cx - w * 0.45f, top + h)
        lineTo(cx - w * 0.10f, top + h * 0.75f)
        // Regreso del tallo
        lineTo(cx - w * 0.12f, top + h * 0.55f)
        cubicTo(
            cx - w * 0.16f, top + h * 0.48f,
            cx - w / 2f, top + h * 0.38f,
            cx - w / 2f, top
        )
        close()
    }

    // Relleno Blanco
    drawPath(path = chalicePath, color = Color.White)
    // Contorno Negro
    drawPath(path = chalicePath, color = Color(0xFF111827), style = Stroke(width = 1.3f.dp.toPx()))

    // Nudo del cáliz
    drawLine(
        color = Color(0xFF111827),
        start = Offset(cx - w * 0.18f, top + h * 0.64f),
        end = Offset(cx + w * 0.18f, top + h * 0.64f),
        strokeWidth = 1.5f.dp.toPx()
    )
}

/**
 * Libro abierto blanco con lomo, páginas y broches en el lomo izquierdo.
 */
private fun DrawScope.drawOfficialOpenBook(cx: Float, cy: Float, size: Float) {
    val w = size * 0.86f
    val h = size * 0.66f
    val top = cy - h / 2f
    val spineX = cx

    // Página izquierda
    val leftPage = Path().apply {
        moveTo(spineX, top + 2.dp.toPx())
        cubicTo(spineX - w * 0.25f, top - 2.dp.toPx(), spineX - w * 0.45f, top, spineX - w * 0.50f, top + 4.dp.toPx())
        lineTo(spineX - w * 0.50f, top + h)
        cubicTo(spineX - w * 0.45f, top + h - 4.dp.toPx(), spineX - w * 0.25f, top + h - 2.dp.toPx(), spineX, top + h + 2.dp.toPx())
        close()
    }

    // Página derecha
    val rightPage = Path().apply {
        moveTo(spineX, top + 2.dp.toPx())
        cubicTo(spineX + w * 0.25f, top - 2.dp.toPx(), spineX + w * 0.45f, top, spineX + w * 0.50f, top + 4.dp.toPx())
        lineTo(spineX + w * 0.50f, top + h)
        cubicTo(spineX + w * 0.45f, top + h - 4.dp.toPx(), spineX + w * 0.25f, top + h - 2.dp.toPx(), spineX, top + h + 2.dp.toPx())
        close()
    }

    drawPath(leftPage, color = Color.White)
    drawPath(rightPage, color = Color.White)

    drawPath(leftPage, color = Color(0xFF111827), style = Stroke(width = 1.3f.dp.toPx()))
    drawPath(rightPage, color = Color(0xFF111827), style = Stroke(width = 1.3f.dp.toPx()))

    // Lomo central
    drawLine(
        color = Color(0xFF111827),
        start = Offset(spineX, top + 2.dp.toPx()),
        end = Offset(spineX, top + h + 2.dp.toPx()),
        strokeWidth = 1.6f.dp.toPx()
    )

    // Broches o cintas en el borde izquierdo (característicos del libro del escudo)
    val claspLeft = spineX - w * 0.50f - 2.dp.toPx()
    drawLine(
        color = Color(0xFF111827),
        start = Offset(claspLeft, top + h * 0.30f),
        end = Offset(spineX - w * 0.50f, top + h * 0.30f),
        strokeWidth = 1.8f.dp.toPx()
    )
    drawLine(
        color = Color(0xFF111827),
        start = Offset(claspLeft, top + h * 0.70f),
        end = Offset(spineX - w * 0.50f, top + h * 0.70f),
        strokeWidth = 1.8f.dp.toPx()
    )
}

/**
 * Resplandor solar circular blanco con rayos y Flor de Lis blanca interior.
 */
private fun DrawScope.drawOfficialSunburstFleurDeLis(cx: Float, cy: Float, size: Float) {
    val haloRadius = size * 0.38f

    // 1. Rayos del resplandor exterior
    val rayCount = 20
    val innerRayR = haloRadius + 1.dp.toPx()
    val outerRayR = haloRadius + 3.5f.dp.toPx()
    for (i in 0 until rayCount) {
        val angle = Math.toRadians((i * (360.0 / rayCount)))
        val startX = cx + innerRayR * cos(angle).toFloat()
        val startY = cy + innerRayR * sin(angle).toFloat()
        val endX = cx + outerRayR * cos(angle).toFloat()
        val endY = cy + outerRayR * sin(angle).toFloat()
        drawLine(
            color = Color(0xFF111827),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 1f.dp.toPx()
        )
    }

    // 2. Círculo blanco del halo
    drawCircle(color = Color.White, radius = haloRadius, center = Offset(cx, cy))
    drawCircle(color = Color(0xFF111827), radius = haloRadius, center = Offset(cx, cy), style = Stroke(width = 1.2f.dp.toPx()))

    // 3. Flor de Lis blanca en el interior
    val lisH = haloRadius * 1.55f
    val lisW = haloRadius * 1.35f
    val top = cy - lisH / 2f

    val lisPath = Path().apply {
        // Pétalo central
        moveTo(cx, top)
        cubicTo(cx + lisW * 0.20f, top + lisH * 0.25f, cx + lisW * 0.16f, top + lisH * 0.50f, cx + lisW * 0.08f, top + lisH * 0.60f)
        // Pétalo derecho curvado
        cubicTo(cx + lisW * 0.45f, top + lisH * 0.30f, cx + lisW * 0.52f, top + lisH * 0.52f, cx + lisW * 0.20f, top + lisH * 0.68f)
        lineTo(cx + lisW * 0.10f, top + lisH * 0.70f)
        // Base
        lineTo(cx + lisW * 0.16f, top + lisH)
        lineTo(cx - lisW * 0.16f, top + lisH)
        lineTo(cx - lisW * 0.10f, top + lisH * 0.70f)
        // Pétalo izquierdo curvado
        lineTo(cx - lisW * 0.20f, top + lisH * 0.68f)
        cubicTo(cx - lisW * 0.52f, top + lisH * 0.52f, cx - lisW * 0.45f, top + lisH * 0.30f, cx - lisW * 0.08f, top + lisH * 0.60f)
        cubicTo(cx - lisW * 0.16f, top + lisH * 0.50f, cx - lisW * 0.20f, top + lisH * 0.25f, cx, top)
        close()
    }

    drawPath(path = lisPath, color = Color.White)
    drawPath(path = lisPath, color = Color(0xFF111827), style = Stroke(width = 1.1f.dp.toPx()))

    // Cinta horizontal de amarre
    val tieY = top + lisH * 0.64f
    drawLine(
        color = Color(0xFF111827),
        start = Offset(cx - lisW * 0.20f, tieY),
        end = Offset(cx + lisW * 0.20f, tieY),
        strokeWidth = 1.4f.dp.toPx()
    )
}

/**
 * Cruz Paté blanca con brazos ensanchados triangulares y contorno negro.
 */
private fun DrawScope.drawOfficialWhitePateeCross(cx: Float, cy: Float, size: Float) {
    val r = size * 0.46f
    val neck = r * 0.24f

    val crossPath = Path().apply {
        // Brazo superior
        moveTo(cx - neck, cy - neck)
        lineTo(cx - r, cy - r)
        lineTo(cx + r, cy - r)
        lineTo(cx + neck, cy - neck)

        // Brazo derecho
        lineTo(cx + r, cy - neck * 1.5f)
        lineTo(cx + r, cy + neck * 1.5f)
        lineTo(cx + neck, cy + neck)

        // Brazo inferior
        lineTo(cx + r, cy + r)
        lineTo(cx - r, cy + r)
        lineTo(cx - neck, cy + neck)

        // Brazo izquierdo
        lineTo(cx - r, cy + neck * 1.5f)
        lineTo(cx - r, cy - neck * 1.5f)
        close()
    }

    drawPath(crossPath, color = Color.White)
    drawPath(
        crossPath,
        color = Color(0xFF111827),
        style = Stroke(width = 1.4f.dp.toPx(), join = StrokeJoin.Miter)
    )
}
