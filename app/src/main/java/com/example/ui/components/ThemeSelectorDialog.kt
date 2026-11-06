package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BasilianAppTheme
import com.example.ui.theme.ThemeCategory

@Composable
fun ThemeSelectorDialog(
    currentTheme: BasilianAppTheme,
    onThemeSelected: (BasilianAppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(ThemeCategory.TODOS) }

    val filteredThemes = remember(selectedCategory) {
        if (selectedCategory == ThemeCategory.TODOS) {
            BasilianAppTheme.values().toList()
        } else {
            BasilianAppTheme.values().filter { it.category == selectedCategory }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = null,
                tint = currentTheme.primaryColor,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = "Temas Visuales Épicos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Elige tu ambientación: Escudo Oficial C.S.B., Harry Potter, Marvel, Disney o Anime. Todos los colores cuentan con máxima legibilidad y armonía:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                // Chips de Categorías
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(ThemeCategory.values()) { category ->
                        val isCatSelected = category == selectedCategory
                        FilterChip(
                            selected = isCatSelected,
                            onClick = { selectedCategory = category },
                            label = {
                                Text(
                                    text = category.title,
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = currentTheme.primaryColor.copy(alpha = 0.20f),
                                selectedLabelColor = currentTheme.primaryColor,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredThemes) { theme ->
                        val isSelected = theme == currentTheme

                        // El contenedor de la tarjeta del tema usa colores con contraste garantizado
                        val cardBg = if (theme.isDark) {
                            Color(0xFF182232)
                        } else {
                            Color(0xFFFFFFFF)
                        }

                        val cardTitleColor = if (theme.isDark) {
                            Color(0xFFF8FAFC)
                        } else {
                            Color(0xFF0F172A)
                        }

                        val cardSubtitleColor = if (theme.isDark) {
                            Color(0xFFCBD5E1)
                        } else {
                            Color(0xFF475569)
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    onThemeSelected(theme)
                                    onDismiss()
                                }
                                .testTag("theme_card_${theme.id}"),
                            color = cardBg,
                            border = if (isSelected) {
                                BorderStroke(2.5.dp, theme.primaryColor)
                            } else {
                                BorderStroke(1.dp, if (theme.isDark) Color(0xFF334155) else Color(0xFFE2E8F0))
                            },
                            shadowElevation = if (isSelected) 4.dp else 1.dp,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Fila Superior: Muestras de color, Nombre y Badge de Categoría
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        // Muestra triple de color con aro sutil de contraste
                                        Row(
                                            modifier = Modifier.padding(end = 10.dp),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .clip(CircleShape)
                                                    .background(theme.primaryColor)
                                                    .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .clip(CircleShape)
                                                    .background(theme.secondaryColor)
                                                    .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .clip(CircleShape)
                                                    .background(theme.goldColor)
                                                    .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                                            )
                                        }

                                        Text(
                                            text = theme.displayName,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                            color = cardTitleColor
                                        )
                                    }

                                    if (isSelected) {
                                        Surface(
                                            shape = CircleShape,
                                            color = theme.primaryColor,
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Seleccionado",
                                                    tint = theme.onPrimaryColor,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                // Subtítulo descriptivo de los tonos
                                Text(
                                    text = theme.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.5.sp,
                                    color = cardSubtitleColor,
                                    lineHeight = 15.sp
                                )

                                // Píldoras informativas: Modo, Tipografía y Tag
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // Modo Claro / Oscuro
                                    Surface(
                                        color = if (theme.isDark) Color(0xFF0F172A) else Color(0xFFF1F5F9),
                                        shape = RoundedCornerShape(6.dp),
                                        border = BorderStroke(0.5.dp, if (theme.isDark) Color(0xFF475569) else Color(0xFFCBD5E1))
                                    ) {
                                        Text(
                                            text = if (theme.isDark) "🌙 Modo Oscuro" else "☀️ Modo Claro",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (theme.isDark) Color(0xFFF8FAFC) else Color(0xFF1E293B),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    // Tipografía legible
                                    Surface(
                                        color = theme.primaryColor.copy(alpha = 0.16f),
                                        shape = RoundedCornerShape(6.dp),
                                        border = BorderStroke(0.5.dp, theme.primaryColor.copy(alpha = 0.35f))
                                    ) {
                                        Text(
                                            text = "✍️ ${theme.fontName}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = theme.primaryColor,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    // Lema o Categoría
                                    Surface(
                                        color = theme.goldColor.copy(alpha = 0.16f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = theme.themeTag,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (theme.isDark) theme.goldColor else Color(0xFF92400E),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                // Lema temático
                                Text(
                                    text = "«${theme.themeMotto}»",
                                    fontSize = 10.5.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    color = if (theme.isDark) theme.primaryColor else Color(0xFF166534),
                                    lineHeight = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", fontWeight = FontWeight.Bold)
            }
        }
    )
}
