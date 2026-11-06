package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PrayerItem
import com.example.data.repository.BasilianStaticData
import com.example.ui.components.BasilianCrestView
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.BasilianViewModel

@Composable
fun HomeScreen(
    viewModel: BasilianViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val inspiration = viewModel.todayInspiration
    val recommendedPrayer = viewModel.getRecommendedPrayer()
    val isSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val appLanguage by viewModel.appLanguage.collectAsState()
    val strings = com.example.ui.i18n.AppStrings.get(appLanguage)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("home_hero_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = BasilianNavy),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(BasilianNavy, BasilianBlue)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BasilianCrestView(size = 96.dp, showMottoText = false)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = strings.home.congregationTitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = BasilianGoldLight,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = strings.home.fathersTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = BasilianGold.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(BasilianGold, BasilianGoldDark)))
                    ) {
                        Text(
                            text = appLanguage.motto,
                            style = MaterialTheme.typography.bodyMedium,
                            color = BasilianGoldLight,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "«${BasilianStaticData.saintBasilQuote}»",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = "— ${strings.home.stBasilDoctorChurch}",
                        style = MaterialTheme.typography.labelSmall,
                        color = BasilianGold,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Dedicated Section: La Congregación de los Padres Basilianos
        BasilianCongregationSection(
            onExploreFounders = { viewModel.selectTab(AppTab.FUNDADORES) },
            onExploreCommunity = { viewModel.selectTab(AppTab.ACTUALIDAD) },
            onExploreHistory = { viewModel.selectTab(AppTab.HISTORIA) },
            onOpenBook1922 = { viewModel.selectTab(AppTab.LIBRO_1922) },
            onPrayCongregationPrayer = {
                val congregationalPrayer = BasilianStaticData.dailyPrayers.find { it.id == "oracion_san_basilio" }
                if (congregationalPrayer != null) {
                    viewModel.selectPrayer(congregationalPrayer)
                    viewModel.selectTab(AppTab.ORACION)
                }
            },
            onListenPrayer = {
                val text = strings.home.patronalPrayerExcerpt
                viewModel.speakText(text)
            },
            isSpeaking = isSpeaking,
            homeStrings = strings.home
        )

        // Tarjeta Destacada: Costumbres Basilianas 2022
        CostumbresHighlightCard(
            onOpenCostumbres = { viewModel.selectTab(AppTab.COSTUMBRES) },
            homeStrings = strings.home
        )

        // Tarjeta Destacada: Libro Histórico 1922 (Antes y Después)
        Book1922HighlightCard(
            onOpenBook = { viewModel.selectTab(AppTab.LIBRO_1922) },
            homeStrings = strings.home
        )

        // Recommended Prayer of the Current Moment
        RecommendedPrayerCard(
            prayer = recommendedPrayer,
            isSpeaking = isSpeaking,
            homeStrings = strings.home,
            onListen = {
                val fullText = "${recommendedPrayer.title}. ${recommendedPrayer.subtitle}. ${recommendedPrayer.prayerText}"
                viewModel.speakText(fullText)
            },
            onOpenPrayers = {
                viewModel.selectPrayer(recommendedPrayer)
                viewModel.selectTab(AppTab.ORACION)
            }
        )

        // Píldora del Día: Bondad, Disciplina y Ciencia
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("home_daily_inspiration_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(ParchmentBorder, BasilianGoldLight)))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = BasilianGold,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = BasilianNavy,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = strings.home.dailyPillTag,
                                style = MaterialTheme.typography.labelSmall,
                                color = BasilianCrimson,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "${strings.home.dailyPillFocus}: ${inspiration.basilianMottoFocus}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            val textToRead = "Píldora del día sobre ${inspiration.basilianMottoFocus}. Palabra de Dios: ${inspiration.scriptureText}. Sabiduría de San Basilio: ${inspiration.stBasilWisdom}. Acción práctica para hoy: ${inspiration.practicalActionForToday}"
                            viewModel.speakText(textToRead)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = strings.home.listenDailyPillTooltip,
                            tint = BasilianBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scripture
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "«${inspiration.scriptureText}»",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "— ${inspiration.scriptureRef}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BasilianBlue,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // St. Basil Wisdom
                Text(
                    text = strings.home.stBasilWisdomHeader,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = BasilianGoldDark
                )
                Text(
                    text = inspiration.stBasilWisdom,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Practical Challenge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BasilianCrimson.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircleOutline,
                        contentDescription = null,
                        tint = BasilianCrimson,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = strings.home.practicalChallengeHeader,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BasilianCrimson
                        )
                        Text(
                            text = inspiration.practicalActionForToday,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Quick Navigation Grid to App Pillars
        Text(
            text = strings.home.exploreBasilianLifeTitle,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PillarCard(
                title = strings.home.calendarTile,
                subtitle = strings.home.calendarSubtitle,
                icon = Icons.Default.CalendarMonth,
                badgeColor = BasilianGoldDark,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.selectTab(AppTab.CALENDARIO) }
            )
            PillarCard(
                title = strings.home.directoryTile,
                subtitle = strings.home.directorySubtitle,
                icon = Icons.Default.Groups,
                badgeColor = BasilianBlue,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.selectTab(AppTab.FUNDADORES) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PillarCard(
                title = strings.home.patronsTile,
                subtitle = strings.home.patronsSubtitle,
                icon = Icons.Default.Star,
                badgeColor = BasilianCrimson,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.selectTab(AppTab.PATRONOS) }
            )
            PillarCard(
                title = strings.home.actualityTile,
                subtitle = strings.home.actualitySubtitle,
                icon = Icons.Default.Public,
                badgeColor = BasilianNavy,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.selectTab(AppTab.ACTUALIDAD) }
            )
        }

        // Bicentenary & Mission Highlight Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.selectTab(AppTab.HISTORIA) },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = BasilianGold.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = BasilianGoldDark,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = strings.home.bicentenaryCardTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = strings.home.bicentenaryCardDesc,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = BasilianBlue
                )
            }
        }
    }
}

@Composable
private fun RecommendedPrayerCard(
    prayer: PrayerItem,
    isSpeaking: Boolean,
    homeStrings: com.example.ui.i18n.HomeStrings,
    onListen: () -> Unit,
    onOpenPrayers: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("home_recommended_prayer_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = BasilianBlue.copy(alpha = 0.12f),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = BasilianBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = homeStrings.recommendedPrayerHeader,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = BasilianBlue,
                        letterSpacing = 0.8.sp
                    )
                }

                Text(
                    text = prayer.timeOfDay,
                    style = MaterialTheme.typography.labelSmall,
                    color = BasilianCrimson,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = prayer.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = prayer.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = prayer.prayerText.take(160) + "...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onListen,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (isSpeaking) homeStrings.stopPrayer else homeStrings.listenPrayer, fontSize = 12.sp)
                }

                Button(
                    onClick = onOpenPrayers,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BasilianBlue),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = homeStrings.prayNowBtn, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun PillarCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    badgeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = badgeColor.copy(alpha = 0.12f),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun BasilianCongregationSection(
    onExploreFounders: () -> Unit,
    onExploreCommunity: () -> Unit,
    onExploreHistory: () -> Unit,
    onOpenBook1922: () -> Unit,
    onPrayCongregationPrayer: () -> Unit,
    onListenPrayer: () -> Unit,
    isSpeaking: Boolean,
    homeStrings: com.example.ui.i18n.HomeStrings,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("basilian_congregation_section_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, BasilianGold.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Section Title Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = BasilianNavy,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Church,
                            contentDescription = null,
                            tint = BasilianGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = homeStrings.congregationSectionTag,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = BasilianCrimson,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = homeStrings.congregationSectionQuestion,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Description of Identity & Charism
            Text(
                text = homeStrings.congregationSectionDesc,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // 3 Key Pillars
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = homeStrings.threePillarsHeader,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = BasilianNavy
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BasilianGold,
                            modifier = Modifier.size(10.dp)
                        ) {}
                        Text(
                            text = homeStrings.pillarGoodnessDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BasilianBlue,
                            modifier = Modifier.size(10.dp)
                        ) {}
                        Text(
                            text = homeStrings.pillarDisciplineDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BasilianCrimson,
                            modifier = Modifier.size(10.dp)
                        ) {}
                        Text(
                            text = homeStrings.pillarKnowledgeDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Presencia en Colombia
            Surface(
                color = BasilianBlue.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onExploreCommunity)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = BasilianBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = homeStrings.colPresenceTitle,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = BasilianBlue
                        )
                        Text(
                            text = homeStrings.colPresenceDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = BasilianBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Official Congregation Prayer Card
            Surface(
                color = BasilianNavy.copy(alpha = 0.05f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BasilianNavy.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = homeStrings.patronalPrayerTitle,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = BasilianNavy
                        )
                        IconButton(
                            onClick = onListenPrayer,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                                contentDescription = homeStrings.listenPrayer,
                                tint = BasilianBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Text(
                        text = "«${homeStrings.patronalPrayerExcerpt}»",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Button(
                        onClick = onPrayCongregationPrayer,
                        colors = ButtonDefaults.buttonColors(containerColor = BasilianNavy),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = BasilianGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = homeStrings.prayFullPrayerBtn,
                            color = BasilianGoldLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Quick actions to founders and history
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onExploreFounders,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = BasilianNavy
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = homeStrings.tenFoundersBtn,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BasilianNavy
                    )
                }

                OutlinedButton(
                    onClick = onExploreHistory,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HistoryEdu,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = BasilianCrimson
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = homeStrings.keyDatesBtn,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BasilianCrimson
                    )
                }

                Button(
                    onClick = onOpenBook1922,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BasilianGoldDark),
                    modifier = Modifier.weight(1.1f),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = homeStrings.work1922Btn,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun CostumbresHighlightCard(
    onOpenCostumbres: () -> Unit,
    homeStrings: com.example.ui.i18n.HomeStrings,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenCostumbres)
            .testTag("costumbres_highlight_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ShieldGreenDark),
        elevation = CardDefaults.cardElevation(5.dp),
        border = BorderStroke(1.5.dp, ShieldRed)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(ShieldGreenDark, ShieldGreen, Color(0xFF0F3D24))
                    )
                )
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ShieldRed.copy(alpha = 0.25f),
                        border = BorderStroke(1.dp, ShieldRedLight)
                    ) {
                        Text(
                            text = homeStrings.docBicentenaryTag,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.1.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    BasilianCrestView(size = 36.dp, showMottoText = false)
                }

                Text(
                    text = homeStrings.customsBookTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = homeStrings.customsApprovedBy,
                    style = MaterialTheme.typography.bodySmall,
                    color = ShieldGoldLight,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = homeStrings.customsSummary,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 18.sp
                )

                Button(
                    onClick = onOpenCostumbres,
                    colors = ButtonDefaults.buttonColors(containerColor = ShieldRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = homeStrings.readCustomsBtn,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun Book1922HighlightCard(
    onOpenBook: () -> Unit,
    homeStrings: com.example.ui.i18n.HomeStrings,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenBook)
            .testTag("book_1922_highlight_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = BasilianNavy),
        elevation = CardDefaults.cardElevation(5.dp),
        border = BorderStroke(1.5.dp, BasilianGold)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(BasilianNavy, Color(0xFF1E3A5F), Color(0xFF132238))
                    )
                )
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BasilianGold.copy(alpha = 0.25f),
                        border = BorderStroke(1.dp, BasilianGold)
                    ) {
                        Text(
                            text = homeStrings.book1922HeaderTag,
                            style = MaterialTheme.typography.labelSmall,
                            color = BasilianGoldLight,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.1.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        tint = BasilianGold,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = homeStrings.book1922Title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = homeStrings.book1922Author,
                    style = MaterialTheme.typography.bodySmall,
                    color = BasilianGoldLight,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = homeStrings.book1922Summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f),
                    lineHeight = 18.sp
                )

                Button(
                    onClick = onOpenBook,
                    colors = ButtonDefaults.buttonColors(containerColor = BasilianGold),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = BasilianNavy,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = homeStrings.readBook1922Btn,
                        color = BasilianNavy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}


