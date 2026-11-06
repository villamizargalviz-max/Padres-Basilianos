package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BasilianGoldDark
import com.example.ui.theme.BasilianGoldLight
import com.example.ui.theme.BasilianRed
import com.example.ui.theme.DeepNavyBlue

@Composable
fun GospelLiturgicalArt(
    theme: String,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF2C1654),
                        Color(0xFF140826),
                        Color(0xFF090412)
                    )
                )
            )
            .border(2.dp, Brush.linearGradient(listOf(BasilianGoldLight, BasilianGoldDark)), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            // Fondo con aureola celestial dorada
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        BasilianGoldLight.copy(alpha = 0.35f),
                        Color(0xFF8B6B23).copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.5f, h * 0.45f),
                    radius = w * 0.45f
                )
            )

            when (theme) {
                "PAN_DE_VIDA" -> drawEucharistArt(w, h)
                "BUEN_PASTOR" -> drawGoodShepherdArt(w, h)
                "BUEN_SAMARITANO" -> drawGoodSamaritanArt(w, h)
                "BIENAVENTURANZAS" -> drawBeatitudesArt(w, h)
                "AMOR_FRATERNO" -> drawBrotherlyLoveArt(w, h)
                "CRISTO_EN_EL_POBRE" -> drawChristInThePoorArt(w, h)
                "MANSO_Y_HUMILDE" -> drawSacredHeartArt(w, h)
                "LUZ_DEL_MUNDO" -> drawLightOfTheWorldArt(w, h)
                "GRANO_DE_MOSTAZA" -> drawMustardSeedArt(w, h)
                "CAMINO_VERDAD_VIDA" -> drawWayTruthLifeArt(w, h)
                "MAGNIFICAT" -> drawMagnificatArt(w, h)
                "VOCACION_REDES" -> drawFishersOfMenArt(w, h)
                "LAVATORIO_PIES" -> drawWashingOfFeetArt(w, h)
                "LIRIOS_Y_AVES" -> drawLiliesAndBirdsArt(w, h)
                "CRISTO_SERVIDOR" -> drawServantChristArt(w, h)
                "ORACION_CONFIADA" -> drawPrayingHandsArt(w, h)
                "TESORO_CELESTIAL" -> drawHeavenlyTreasureArt(w, h)
                else -> drawGeneralGospelArt(w, h)
            }
        }
    }
}

// 1. Pan de Vida y Cáliz de la Salvación
private fun DrawScope.drawEucharistArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    val deepGold = BasilianGoldDark
    // Cáliz
    val chalicePath = Path().apply {
        moveTo(w * 0.35f, h * 0.55f)
        cubicTo(w * 0.35f, h * 0.72f, w * 0.65f, h * 0.72f, w * 0.65f, h * 0.55f)
        close()
    }
    drawPath(chalicePath, Brush.verticalGradient(listOf(gold, deepGold)))
    // Base y tallo
    drawLine(gold, Offset(w * 0.5f, h * 0.68f), Offset(w * 0.5f, h * 0.85f), strokeWidth = 5f, cap = StrokeCap.Round)
    drawLine(gold, Offset(w * 0.38f, h * 0.85f), Offset(w * 0.62f, h * 0.85f), strokeWidth = 5f, cap = StrokeCap.Round)
    // Hostia consagrada resplandeciente
    drawCircle(Color.White, radius = w * 0.18f, center = Offset(w * 0.5f, h * 0.42f))
    drawCircle(deepGold, radius = w * 0.18f, center = Offset(w * 0.5f, h * 0.42f), style = Stroke(width = 2.5f))
    // Cruz grabada en la Hostia
    drawLine(deepGold, Offset(w * 0.5f, h * 0.32f), Offset(w * 0.5f, h * 0.52f), strokeWidth = 2.5f)
    drawLine(deepGold, Offset(w * 0.42f, h * 0.40f), Offset(w * 0.58f, h * 0.40f), strokeWidth = 2.5f)
    // Rayos
    for (i in 0 until 8) {
        val angle = (i * 45f) * (Math.PI / 180f).toFloat()
        val startX = w * 0.5f + (w * 0.22f) * kotlin.math.cos(angle)
        val startY = h * 0.42f + (w * 0.22f) * kotlin.math.sin(angle)
        val endX = w * 0.5f + (w * 0.30f) * kotlin.math.cos(angle)
        val endY = h * 0.42f + (w * 0.30f) * kotlin.math.sin(angle)
        drawLine(gold.copy(alpha = 0.7f), Offset(startX, startY), Offset(endX, endY), strokeWidth = 2f)
    }
}

// 2. Buen Pastor con Cayado y Oveja
private fun DrawScope.drawGoodShepherdArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    // Cayado de Pastor
    val staffPath = Path().apply {
        moveTo(w * 0.35f, h * 0.85f)
        lineTo(w * 0.35f, h * 0.35f)
        cubicTo(w * 0.35f, h * 0.18f, w * 0.55f, h * 0.18f, w * 0.55f, h * 0.30f)
        cubicTo(w * 0.55f, h * 0.38f, w * 0.47f, h * 0.42f, w * 0.42f, h * 0.35f)
    }
    drawPath(staffPath, gold, style = Stroke(width = 4.5f, cap = StrokeCap.Round))
    // Resplandor celestial
    drawCircle(gold.copy(alpha = 0.3f), radius = w * 0.22f, center = Offset(w * 0.65f, h * 0.45f))
    // Silueta blanca de la oveja / cordero
    drawOval(Color.White, Offset(w * 0.52f, h * 0.52f), Size(w * 0.32f, h * 0.22f))
    drawCircle(Color.White, radius = w * 0.10f, center = Offset(w * 0.78f, h * 0.55f))
    // Ojo y orejitas
    drawCircle(DeepNavyBlue, radius = 2f, center = Offset(w * 0.82f, h * 0.54f))
    // Patitas
    drawLine(Color.White, Offset(w * 0.58f, h * 0.74f), Offset(w * 0.58f, h * 0.86f), strokeWidth = 3f, cap = StrokeCap.Round)
    drawLine(Color.White, Offset(w * 0.72f, h * 0.74f), Offset(w * 0.72f, h * 0.86f), strokeWidth = 3f, cap = StrokeCap.Round)
}

// 3. Buen Samaritano: Óleo, Bálsamo y Compasión
private fun DrawScope.drawGoodSamaritanArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    // Ánfora con óleo de consolación
    val amphora = Path().apply {
        moveTo(w * 0.42f, h * 0.35f)
        lineTo(w * 0.58f, h * 0.35f)
        lineTo(w * 0.62f, h * 0.45f)
        cubicTo(w * 0.72f, h * 0.58f, w * 0.68f, h * 0.78f, w * 0.50f, h * 0.80f)
        cubicTo(w * 0.32f, h * 0.78f, w * 0.28f, h * 0.58f, w * 0.38f, h * 0.45f)
        close()
    }
    drawPath(amphora, Brush.verticalGradient(listOf(Color(0xFFE2A747), Color(0xFF9E6516))))
    // Gota de óleo sagrado cayendo
    val dropPath = Path().apply {
        moveTo(w * 0.50f, h * 0.20f)
        cubicTo(w * 0.55f, h * 0.28f, w * 0.55f, h * 0.32f, w * 0.50f, h * 0.33f)
        cubicTo(w * 0.45f, h * 0.32f, w * 0.45f, h * 0.28f, w * 0.50f, h * 0.20f)
        close()
    }
    drawPath(dropPath, BasilianGoldLight)
    // Cruz roja de alivio médico / caridad
    drawRect(BasilianRed, Offset(w * 0.47f, h * 0.52f), Size(w * 0.06f, h * 0.16f))
    drawRect(BasilianRed, Offset(w * 0.42f, h * 0.57f), Size(w * 0.16f, h * 0.06f))
}

// 4. Bienaventuranzas: El Monte Santo y el Sol de Justicia
private fun DrawScope.drawBeatitudesArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    // Sol radiante en el monte
    drawCircle(gold, radius = w * 0.18f, center = Offset(w * 0.5f, h * 0.35f))
    // Montaña estilizada
    val mountain = Path().apply {
        moveTo(w * 0.1f, h * 0.85f)
        lineTo(w * 0.5f, h * 0.48f)
        lineTo(w * 0.9f, h * 0.85f)
        close()
    }
    drawPath(mountain, Brush.verticalGradient(listOf(Color(0xFF4A3475), Color(0xFF1E1035))))
    // Cruz en la cima del monte
    drawLine(gold, Offset(w * 0.5f, h * 0.38f), Offset(w * 0.5f, h * 0.52f), strokeWidth = 3.5f)
    drawLine(gold, Offset(w * 0.44f, h * 0.43f), Offset(w * 0.56f, h * 0.43f), strokeWidth = 3.5f)
}

// 5. Amor Fraterno: Hermanos y Fuego del Espíritu
private fun DrawScope.drawBrotherlyLoveArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    val red = BasilianRed
    // Corazón ardiente
    val heart = Path().apply {
        moveTo(w * 0.5f, h * 0.75f)
        cubicTo(w * 0.25f, h * 0.55f, w * 0.20f, h * 0.35f, w * 0.35f, h * 0.30f)
        cubicTo(w * 0.45f, h * 0.28f, w * 0.50f, h * 0.42f, w * 0.50f, h * 0.45f)
        cubicTo(w * 0.50f, h * 0.42f, w * 0.55f, h * 0.28f, w * 0.65f, h * 0.30f)
        cubicTo(w * 0.80f, h * 0.35f, w * 0.75f, h * 0.55f, w * 0.5f, h * 0.75f)
        close()
    }
    drawPath(heart, Brush.verticalGradient(listOf(red, Color(0xFF7A1212))))
    // Llama del Espíritu Santo sobre el corazón
    val flame = Path().apply {
        moveTo(w * 0.5f, h * 0.15f)
        cubicTo(w * 0.56f, h * 0.22f, w * 0.55f, h * 0.29f, w * 0.50f, h * 0.33f)
        cubicTo(w * 0.45f, h * 0.29f, w * 0.44f, h * 0.22f, w * 0.50f, h * 0.15f)
        close()
    }
    drawPath(flame, gold)
    // Cruz interior blanca
    drawLine(Color.White, Offset(w * 0.5f, h * 0.46f), Offset(w * 0.5f, h * 0.64f), strokeWidth = 3f)
    drawLine(Color.White, Offset(w * 0.43f, h * 0.52f), Offset(w * 0.57f, h * 0.52f), strokeWidth = 3f)
}

// 6. Cristo en el Pobre: El manto y el pan compartido
private fun DrawScope.drawChristInThePoorArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    // Hogaza de pan partida
    drawArc(
        color = gold,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(w * 0.25f, h * 0.45f),
        size = Size(w * 0.50f, h * 0.35f)
    )
    // Corte del pan partido
    drawLine(DeepNavyBlue, Offset(w * 0.5f, h * 0.45f), Offset(w * 0.5f, h * 0.62f), strokeWidth = 3f)
    // Cruz de luz sobre el pan
    drawLine(Color.White, Offset(w * 0.5f, h * 0.22f), Offset(w * 0.5f, h * 0.40f), strokeWidth = 3f)
    drawLine(Color.White, Offset(w * 0.42f, h * 0.30f), Offset(w * 0.58f, h * 0.30f), strokeWidth = 3f)
}

// 7. Manso y Humilde de Corazón
private fun DrawScope.drawSacredHeartArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    // Corona de espinas estilizada alrededor
    drawCircle(gold.copy(alpha = 0.4f), radius = w * 0.26f, center = Offset(w * 0.5f, h * 0.5f), style = Stroke(width = 2.5f))
    // Cruz redentora
    drawLine(gold, Offset(w * 0.5f, h * 0.26f), Offset(w * 0.5f, h * 0.74f), strokeWidth = 4f, cap = StrokeCap.Round)
    drawLine(gold, Offset(w * 0.34f, h * 0.42f), Offset(w * 0.66f, h * 0.42f), strokeWidth = 4f, cap = StrokeCap.Round)
    // Yugo suave / Manto
    drawArc(
        color = Color.White.copy(alpha = 0.8f),
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(w * 0.28f, h * 0.48f),
        size = Size(w * 0.44f, h * 0.25f),
        style = Stroke(width = 3f, cap = StrokeCap.Round)
    )
}

// 8. Luz del Mundo: Faro Sagrado y Lámpara
private fun DrawScope.drawLightOfTheWorldArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    // Lámpara de aceite encendida
    val lamp = Path().apply {
        moveTo(w * 0.25f, h * 0.68f)
        cubicTo(w * 0.30f, h * 0.80f, w * 0.70f, h * 0.80f, w * 0.75f, h * 0.68f)
        lineTo(w * 0.65f, h * 0.60f)
        lineTo(w * 0.35f, h * 0.60f)
        close()
    }
    drawPath(lamp, Brush.verticalGradient(listOf(gold, BasilianGoldDark)))
    // Llama brillante
    val flame = Path().apply {
        moveTo(w * 0.5f, h * 0.28f)
        cubicTo(w * 0.58f, h * 0.42f, w * 0.58f, h * 0.55f, w * 0.5f, h * 0.60f)
        cubicTo(w * 0.42f, h * 0.55f, w * 0.42f, h * 0.42f, w * 0.5f, h * 0.28f)
        close()
    }
    drawPath(flame, Brush.verticalGradient(listOf(Color.White, gold, BasilianRed)))
    // Rayos que iluminan
    for (i in 0 until 6) {
        val y = h * (0.30f + i * 0.08f)
        drawLine(gold.copy(alpha = 0.6f), Offset(w * 0.20f, y), Offset(w * 0.10f, y), strokeWidth = 2f)
        drawLine(gold.copy(alpha = 0.6f), Offset(w * 0.80f, y), Offset(w * 0.90f, y), strokeWidth = 2f)
    }
}

// 9. Grano de Mostaza: El brote fértil que crece
private fun DrawScope.drawMustardSeedArt(w: Float, h: Float) {
    val green = Color(0xFF4CAF50)
    val lightGreen = Color(0xFF81C784)
    val gold = BasilianGoldLight
    // Tallo
    val stem = Path().apply {
        moveTo(w * 0.5f, h * 0.85f)
        cubicTo(w * 0.45f, h * 0.60f, w * 0.55f, h * 0.45f, w * 0.5f, h * 0.28f)
    }
    drawPath(stem, green, style = Stroke(width = 4f, cap = StrokeCap.Round))
    // Hojas
    drawOval(lightGreen, Offset(w * 0.50f, h * 0.40f), Size(w * 0.26f, h * 0.12f))
    drawOval(lightGreen, Offset(w * 0.24f, h * 0.52f), Size(w * 0.26f, h * 0.12f))
    // Grano dorado en la base
    drawCircle(gold, radius = w * 0.08f, center = Offset(w * 0.5f, h * 0.85f))
    // Cruz en la cima
    drawLine(gold, Offset(w * 0.5f, h * 0.18f), Offset(w * 0.5f, h * 0.28f), strokeWidth = 2.5f)
    drawLine(gold, Offset(w * 0.45f, h * 0.22f), Offset(w * 0.55f, h * 0.22f), strokeWidth = 2.5f)
}

// 10. Camino, Verdad y Vida: Sendero y Alfa/Omega
private fun DrawScope.drawWayTruthLifeArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    // Camino hacia la luz
    val path = Path().apply {
        moveTo(w * 0.25f, h * 0.85f)
        lineTo(w * 0.45f, h * 0.40f)
        lineTo(w * 0.55f, h * 0.40f)
        lineTo(w * 0.75f, h * 0.85f)
        close()
    }
    drawPath(path, Brush.verticalGradient(listOf(gold.copy(alpha = 0.4f), Color(0xFF381F66))))
    // Cruz al final del camino
    drawLine(gold, Offset(w * 0.5f, h * 0.22f), Offset(w * 0.5f, h * 0.42f), strokeWidth = 3.5f)
    drawLine(gold, Offset(w * 0.43f, h * 0.28f), Offset(w * 0.57f, h * 0.28f), strokeWidth = 3.5f)
}

// 11. Magníficat: Corona de 12 estrellas y Lirio de María
private fun DrawScope.drawMagnificatArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    val white = Color.White
    // Media luna virginal
    drawArc(
        color = white,
        startAngle = 30f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(w * 0.30f, h * 0.65f),
        size = Size(w * 0.40f, h * 0.18f),
        style = Stroke(width = 3.5f, cap = StrokeCap.Round)
    )
    // Monograma mariano o Lirio
    drawLine(gold, Offset(w * 0.5f, h * 0.30f), Offset(w * 0.5f, h * 0.65f), strokeWidth = 3f)
    drawCircle(white, radius = w * 0.08f, center = Offset(w * 0.5f, h * 0.35f))
    // 6 estrellas alrededor
    for (i in 0 until 6) {
        val angle = (i * 60f) * (Math.PI / 180f).toFloat()
        val sx = w * 0.5f + (w * 0.30f) * kotlin.math.cos(angle)
        val sy = h * 0.40f + (w * 0.30f) * kotlin.math.sin(angle)
        drawCircle(gold, radius = 2.5f, center = Offset(sx, sy))
    }
}

// 12. Pescadores de Hombres: Barca y Redes
private fun DrawScope.drawFishersOfMenArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    // Barquilla de Pedro
    val boat = Path().apply {
        moveTo(w * 0.20f, h * 0.60f)
        cubicTo(w * 0.25f, h * 0.78f, w * 0.75f, h * 0.78f, w * 0.80f, h * 0.60f)
        close()
    }
    drawPath(boat, Brush.verticalGradient(listOf(Color(0xFF8D5B28), Color(0xFF4A2800))))
    // Mástil en forma de Cruz
    drawLine(gold, Offset(w * 0.5f, h * 0.25f), Offset(w * 0.5f, h * 0.62f), strokeWidth = 3.5f)
    drawLine(gold, Offset(w * 0.40f, h * 0.35f), Offset(w * 0.60f, h * 0.35f), strokeWidth = 3.5f)
    // Olas del mar
    val waves = Path().apply {
        moveTo(w * 0.1f, h * 0.78f)
        cubicTo(w * 0.3f, h * 0.74f, w * 0.4f, h * 0.82f, w * 0.6f, h * 0.78f)
        cubicTo(w * 0.75f, h * 0.74f, w * 0.85f, h * 0.82f, w * 0.95f, h * 0.78f)
    }
    drawPath(waves, Color(0xFF42A5F5), style = Stroke(width = 2.5f, cap = StrokeCap.Round))
}

// 13. Lavatorio de los Pies: Jofaina y Toalla
private fun DrawScope.drawWashingOfFeetArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    // Jofaina / Cuenco de agua
    val basin = Path().apply {
        moveTo(w * 0.25f, h * 0.65f)
        cubicTo(w * 0.30f, h * 0.82f, w * 0.70f, h * 0.82f, w * 0.75f, h * 0.65f)
        close()
    }
    drawPath(basin, Brush.verticalGradient(listOf(gold, BasilianGoldDark)))
    // Cántaro que derrama agua cristalina
    val pitcher = Path().apply {
        moveTo(w * 0.55f, h * 0.30f)
        lineTo(w * 0.70f, h * 0.35f)
        lineTo(w * 0.65f, h * 0.52f)
        lineTo(w * 0.50f, h * 0.50f)
        close()
    }
    drawPath(pitcher, Color(0xFFD4AF37))
    // Chorro de agua hacia la jofaina
    drawLine(Color(0xFF64B5F6), Offset(w * 0.52f, h * 0.50f), Offset(w * 0.48f, h * 0.68f), strokeWidth = 3f, cap = StrokeCap.Round)
}

// 14. Lirios del Campo y Aves
private fun DrawScope.drawLiliesAndBirdsArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    val white = Color.White
    // Ave en vuelo libre
    val bird = Path().apply {
        moveTo(w * 0.30f, h * 0.32f)
        cubicTo(w * 0.40f, h * 0.22f, w * 0.48f, h * 0.32f, w * 0.50f, h * 0.32f)
        cubicTo(w * 0.52f, h * 0.32f, w * 0.60f, h * 0.22f, w * 0.70f, h * 0.32f)
    }
    drawPath(bird, white, style = Stroke(width = 3f, cap = StrokeCap.Round))
    // Lirio blanco florecido
    val stem = Path().apply {
        moveTo(w * 0.5f, h * 0.85f)
        lineTo(w * 0.5f, h * 0.55f)
    }
    drawPath(stem, Color(0xFF81C784), style = Stroke(width = 3f, cap = StrokeCap.Round))
    drawCircle(white, radius = w * 0.10f, center = Offset(w * 0.5f, h * 0.52f))
    drawCircle(gold, radius = w * 0.04f, center = Offset(w * 0.5f, h * 0.52f))
}

// 15. Cristo Servidor: Estola y Toalla
private fun DrawScope.drawServantChristArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    // Estola sacerdotal cruzada
    drawLine(gold, Offset(w * 0.35f, h * 0.25f), Offset(w * 0.65f, h * 0.75f), strokeWidth = 6f, cap = StrokeCap.Round)
    drawLine(gold, Offset(w * 0.65f, h * 0.25f), Offset(w * 0.35f, h * 0.75f), strokeWidth = 6f, cap = StrokeCap.Round)
    // Toalla blanca de servicio
    drawRoundRect(Color.White, Offset(w * 0.38f, h * 0.45f), Size(w * 0.24f, h * 0.38f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f))
}

// 16. Manos Orantes y Alabanza
private fun DrawScope.drawPrayingHandsArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    // Manos alzadas en oración
    val leftHand = Path().apply {
        moveTo(w * 0.42f, h * 0.80f)
        lineTo(w * 0.48f, h * 0.35f)
        lineTo(w * 0.50f, h * 0.35f)
        lineTo(w * 0.48f, h * 0.80f)
        close()
    }
    val rightHand = Path().apply {
        moveTo(w * 0.58f, h * 0.80f)
        lineTo(w * 0.52f, h * 0.35f)
        lineTo(w * 0.50f, h * 0.35f)
        lineTo(w * 0.52f, h * 0.80f)
        close()
    }
    drawPath(leftHand, gold)
    drawPath(rightHand, BasilianGoldDark)
    // Rayos de bendición que bajan del cielo
    for (i in -2..2) {
        val x = w * (0.50f + i * 0.12f)
        drawLine(Color.White.copy(alpha = 0.7f), Offset(x, h * 0.15f), Offset(w * 0.5f, h * 0.32f), strokeWidth = 2f)
    }
}

// 17. Tesoro Celestial: Gracia inagotable
private fun DrawScope.drawHeavenlyTreasureArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    // Cofre abierto de salvación
    drawRoundRect(BasilianGoldDark, Offset(w * 0.28f, h * 0.55f), Size(w * 0.44f, h * 0.28f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f))
    // Luz que emana del cofre
    val lightBeam = Path().apply {
        moveTo(w * 0.35f, h * 0.55f)
        lineTo(w * 0.15f, h * 0.20f)
        lineTo(w * 0.85f, h * 0.20f)
        lineTo(w * 0.65f, h * 0.55f)
        close()
    }
    drawPath(lightBeam, Brush.verticalGradient(listOf(gold.copy(alpha = 0.7f), Color.Transparent)))
    // Cruz radiante en el centro
    drawLine(Color.White, Offset(w * 0.5f, h * 0.22f), Offset(w * 0.5f, h * 0.45f), strokeWidth = 3f)
    drawLine(Color.White, Offset(w * 0.42f, h * 0.30f), Offset(w * 0.58f, h * 0.30f), strokeWidth = 3f)
}

// 18. General / Predeterminado: Libro de los Santos Evangelios
private fun DrawScope.drawGeneralGospelArt(w: Float, h: Float) {
    val gold = BasilianGoldLight
    // Libro sagrado abierto
    drawRoundRect(Color(0xFF8D5B28), Offset(w * 0.20f, h * 0.35f), Size(w * 0.60f, h * 0.48f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f))
    drawRoundRect(Color.White, Offset(w * 0.24f, h * 0.38f), Size(w * 0.52f, h * 0.42f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f, 2f))
    // Cruz grabada en el centro del libro
    drawLine(gold, Offset(w * 0.5f, h * 0.44f), Offset(w * 0.5f, h * 0.72f), strokeWidth = 3f)
    drawLine(gold, Offset(w * 0.38f, h * 0.54f), Offset(w * 0.62f, h * 0.54f), strokeWidth = 3f)
}
