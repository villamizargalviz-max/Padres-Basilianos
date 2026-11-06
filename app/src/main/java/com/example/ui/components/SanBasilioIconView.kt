package com.example.ui.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.*
import java.io.File
import java.io.FileOutputStream
import kotlin.math.cos
import kotlin.math.sin

private const val PREF_SAN_BASILIO_PHOTO_KEY = "custom_san_basilio_photo_uri"

/**
 * Ícono Sagrado y Retrato Venerable de San Basilio Magno (329-379 d.C.)
 * Arzobispo de Cesarea, Padre de la Iglesia, Doctor de la Gracia y Santo Patrono
 * de la Congregación de San Basilio (Padres Basilianos).
 * Incluye opción directa para subir o personalizar la foto de San Basilio Magno debajo del lema y año.
 */
@Composable
fun SanBasilioIconView(
    modifier: Modifier = Modifier,
    canvasSize: Dp = 190.dp
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("basilian_prefs", Context.MODE_PRIVATE) }
    var customPhotoUri by remember {
        mutableStateOf(prefs.getString(PREF_SAN_BASILIO_PHOTO_KEY, null))
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    val destFile = File(context.filesDir, "san_basilio_custom_${System.currentTimeMillis()}.jpg")
                    FileOutputStream(destFile).use { output ->
                        inputStream.copyTo(output)
                    }
                    inputStream.close()
                    val savedUri = Uri.fromFile(destFile).toString()
                    prefs.edit().putString(PREF_SAN_BASILIO_PHOTO_KEY, savedUri).apply()
                    customPhotoUri = savedUri
                }
            } catch (_: Exception) {
                val fallback = uri.toString()
                prefs.edit().putString(PREF_SAN_BASILIO_PHOTO_KEY, fallback).apply()
                customPhotoUri = fallback
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, ShieldGold.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Marco dorado del Ícono o Foto subida por el usuario
            Box(
                modifier = Modifier
                    .size(canvasSize)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFFF9E6),
                                Color(0xFFF3E5AB),
                                Color(0xFFD4AF37).copy(alpha = 0.4f),
                                ShieldGreenDark
                            )
                        )
                    )
                    .border(2.5.dp, ShieldGold, RoundedCornerShape(20.dp))
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (!customPhotoUri.isNullOrBlank()) {
                    AsyncImage(
                        model = customPhotoUri,
                        contentDescription = "Foto de San Basilio Magno",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                    )
                } else {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val cx = w / 2f
                        val cy = h / 2f

                        // 1. Aureola dorada divina con resplandores
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFE082),
                                    ShieldGold,
                                    Color(0xFFB8860B)
                                ),
                                center = Offset(cx, cy * 0.65f),
                                radius = w * 0.38f
                            ),
                            radius = w * 0.36f,
                            center = Offset(cx, cy * 0.65f)
                        )
                        drawCircle(
                            color = Color(0xFF6B4E1B),
                            radius = w * 0.36f,
                            center = Offset(cx, cy * 0.65f),
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // Rayos de la aureola
                        val numRays = 24
                        for (i in 0 until numRays) {
                            val angle = (i * 360f / numRays) * (Math.PI / 180f)
                            val r1 = w * 0.29f
                            val r2 = w * 0.35f
                            val start = Offset(
                                (cx + r1 * cos(angle)).toFloat(),
                                (cy * 0.65f + r1 * sin(angle)).toFloat()
                            )
                            val end = Offset(
                                (cx + r2 * cos(angle)).toFloat(),
                                (cy * 0.65f + r2 * sin(angle)).toFloat()
                            )
                            drawLine(
                                color = Color(0xFFD4AF37).copy(alpha = 0.6f),
                                start = start,
                                end = end,
                                strokeWidth = 1.2.dp.toPx()
                            )
                        }

                        // 2. Túnica episcopal verde oscura (Cesarea)
                        val tunicPath = Path().apply {
                            moveTo(cx - w * 0.42f, h)
                            lineTo(cx - w * 0.28f, cy * 0.95f)
                            lineTo(cx + w * 0.28f, cy * 0.95f)
                            lineTo(cx + w * 0.42f, h)
                            close()
                        }
                        drawPath(tunicPath, color = ShieldGreenDark)

                        // 3. Omophorion blanco sagrado con cruces negras (Palio arzobispal)
                        val omoPath = Path().apply {
                            moveTo(cx - w * 0.22f, cy * 0.98f)
                            lineTo(cx - w * 0.08f, h)
                            lineTo(cx + w * 0.08f, h)
                            lineTo(cx + w * 0.22f, cy * 0.98f)
                            lineTo(cx + w * 0.12f, cy * 0.92f)
                            lineTo(cx, cy * 1.05f)
                            lineTo(cx - w * 0.12f, cy * 0.92f)
                            close()
                        }
                        drawPath(omoPath, color = Color(0xFFFCFAF2))
                        drawPath(omoPath, color = Color(0xFF1E293B), style = Stroke(1.5.dp.toPx()))

                        // Cruz paté bordada en el omophorion
                        drawCrossPatee(
                            center = Offset(cx, cy * 1.25f),
                            armLength = w * 0.07f,
                            color = Color(0xFF1A1A1A)
                        )

                        // 4. Cabeza y rostro venerable de San Basilio
                        val faceCenter = Offset(cx, cy * 0.68f)
                        drawOval(
                            color = Color(0xFFE8C8A2), // Tono de piel canela mediterránea/capadocio
                            topLeft = Offset(faceCenter.x - w * 0.15f, faceCenter.y - h * 0.18f),
                            size = Size(w * 0.30f, h * 0.32f)
                        )

                        // Cabello oscuro venerable
                        val hairPath = Path().apply {
                            moveTo(cx - w * 0.16f, cy * 0.62f)
                            cubicTo(
                                cx - w * 0.18f, cy * 0.45f,
                                cx + w * 0.18f, cy * 0.45f,
                                cx + w * 0.16f, cy * 0.62f
                            )
                            lineTo(cx + w * 0.14f, cy * 0.60f)
                            cubicTo(
                                cx + w * 0.10f, cy * 0.50f,
                                cx - w * 0.10f, cy * 0.50f,
                                cx - w * 0.14f, cy * 0.60f
                            )
                            close()
                        }
                        drawPath(hairPath, color = Color(0xFF2C241E))

                        // Barba patriarcal oscura alargada (iconografía clásica de San Basilio)
                        val beardPath = Path().apply {
                            moveTo(cx - w * 0.14f, cy * 0.72f)
                            lineTo(cx - w * 0.10f, cy * 0.98f)
                            lineTo(cx, cy * 1.04f)
                            lineTo(cx + w * 0.10f, cy * 0.98f)
                            lineTo(cx + w * 0.14f, cy * 0.72f)
                            cubicTo(
                                cx + w * 0.08f, cy * 0.80f,
                                cx - w * 0.08f, cy * 0.80f,
                                cx - w * 0.14f, cy * 0.72f
                            )
                            close()
                        }
                        drawPath(beardPath, color = Color(0xFF231B15))

                        // Cejas
                        drawLine(
                            color = Color(0xFF1E1712),
                            start = Offset(cx - w * 0.09f, cy * 0.62f),
                            end = Offset(cx - w * 0.03f, cy * 0.63f),
                            strokeWidth = 2.dp.toPx()
                        )
                        drawLine(
                            color = Color(0xFF1E1712),
                            start = Offset(cx + w * 0.03f, cy * 0.63f),
                            end = Offset(cx + w * 0.09f, cy * 0.62f),
                            strokeWidth = 2.dp.toPx()
                        )
                        // Ojos contemplativos
                        drawCircle(Color(0xFF2B1D14), radius = 2.dp.toPx(), center = Offset(cx - w * 0.06f, cy * 0.66f))
                        drawCircle(Color(0xFF2B1D14), radius = 2.dp.toPx(), center = Offset(cx + w * 0.06f, cy * 0.66f))

                        // Nariz ascética
                        drawLine(
                            color = Color(0xFFB58B61),
                            start = Offset(cx, cy * 0.64f),
                            end = Offset(cx, cy * 0.72f),
                            strokeWidth = 1.5.dp.toPx()
                        )

                        // 5. Libro del Evangelio en su mano izquierda
                        val bookX = cx + w * 0.14f
                        val bookY = cy * 0.88f
                        drawRoundRect(
                            color = ShieldRedDark,
                            topLeft = Offset(bookX, bookY),
                            size = Size(w * 0.22f, h * 0.20f),
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )
                        drawRoundRect(
                            color = ShieldGold,
                            topLeft = Offset(bookX, bookY),
                            size = Size(w * 0.22f, h * 0.20f),
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                            style = Stroke(width = 1.8.dp.toPx())
                        )
                        // Cruz dorada sobre la tapa del Evangelio
                        drawLine(
                            color = ShieldGold,
                            start = Offset(bookX + w * 0.11f, bookY + h * 0.04f),
                            end = Offset(bookX + w * 0.11f, bookY + h * 0.16f),
                            strokeWidth = 2.dp.toPx()
                        )
                        drawLine(
                            color = ShieldGold,
                            start = Offset(bookX + w * 0.05f, bookY + h * 0.09f),
                            end = Offset(bookX + w * 0.17f, bookY + h * 0.09f),
                            strokeWidth = 2.dp.toPx()
                        )

                        // 6. Mano derecha en gesto de bendición (ícono bizantino)
                        val handX = cx - w * 0.18f
                        val handY = cy * 0.92f
                        drawCircle(
                            color = Color(0xFFE8C8A2),
                            radius = w * 0.055f,
                            center = Offset(handX, handY)
                        )
                    }
                }
            }

            // Título, Año (329 – 379 d.C.), Lema y Botón para Subir Foto de San Basilio
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "SAN BASILIO MAGNO",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ShieldGreenDark,
                    letterSpacing = 1.2.sp,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "329 – 379 d.C. • Obispo de Cesarea de Capadocia",
                    style = MaterialTheme.typography.labelSmall,
                    color = ShieldRed,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Doctor de la Iglesia • Padre del Monaquismo Fraterno",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ShieldGold.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "«El que ama a Dios ama necesariamente a su hermano; porque si dices que amas a Dios y aborreces a tu prójimo, eres mentiroso. En la caridad activa reside toda la Ley y los Profetas»",
                        style = MaterialTheme.typography.bodySmall,
                        color = ShieldGreenDark,
                        fontFamily = FontFamily.Serif,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Controles para Subir o Cambiar la Foto de San Basilio Magno
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ShieldGreenDark,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("upload_san_basilio_photo_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Subir foto de San Basilio",
                            tint = ShieldGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (customPhotoUri.isNullOrBlank()) "Subir Foto de San Basilio" else "Cambiar Foto de San Basilio",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (!customPhotoUri.isNullOrBlank()) {
                        OutlinedButton(
                            onClick = {
                                prefs.edit().remove(PREF_SAN_BASILIO_PHOTO_KEY).apply()
                                customPhotoUri = null
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, ShieldRed.copy(alpha = 0.6f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldRed),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("restore_san_basilio_icon_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restore,
                                contentDescription = "Restaurar ícono original",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Restaurar", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawCrossPatee(center: Offset, armLength: Float, color: Color) {
    val path = Path().apply {
        // Brazo Superior
        moveTo(center.x, center.y)
        lineTo(center.x - armLength * 0.6f, center.y - armLength)
        lineTo(center.x + armLength * 0.6f, center.y - armLength)
        close()
        // Brazo Inferior
        moveTo(center.x, center.y)
        lineTo(center.x - armLength * 0.6f, center.y + armLength)
        lineTo(center.x + armLength * 0.6f, center.y + armLength)
        close()
        // Brazo Izquierdo
        moveTo(center.x, center.y)
        lineTo(center.x - armLength, center.y - armLength * 0.6f)
        lineTo(center.x - armLength, center.y + armLength * 0.6f)
        close()
        // Brazo Derecho
        moveTo(center.x, center.y)
        lineTo(center.x + armLength, center.y - armLength * 0.6f)
        lineTo(center.x + armLength, center.y + armLength * 0.6f)
        close()
    }
    drawPath(path, color = color)
}
