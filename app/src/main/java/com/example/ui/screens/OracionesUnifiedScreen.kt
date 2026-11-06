package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

enum class OracionesSubTab(val label: String) {
    ORACIONES_TRADICION("Oraciones Comunes"),
    SANTOS_PATRONOS("Santos Patronos"),
    CALENDARIO("Calendario Basiliano")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OracionesUnifiedScreen(
    viewModel: BasilianViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableStateOf(OracionesSubTab.ORACIONES_TRADICION) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BasilianIvory)
    ) {
        Surface(
            color = ShieldGreenDark,
            tonalElevation = 4.dp
        ) {
            TabRow(
                selectedTabIndex = selectedSubTab.ordinal,
                containerColor = Color.Transparent,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab.ordinal]),
                        color = ShieldGoldLight,
                        height = 3.dp
                    )
                },
                divider = {}
            ) {
                OracionesSubTab.values().forEach { tab ->
                    Tab(
                        selected = selectedSubTab == tab,
                        onClick = { selectedSubTab = tab },
                        text = {
                            Text(
                                text = tab.label,
                                fontSize = 12.sp,
                                fontWeight = if (selectedSubTab == tab) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedSubTab == tab) ShieldGoldLight else Color.White.copy(alpha = 0.85f)
                            )
                        }
                    )
                }
            }
        }

        when (selectedSubTab) {
            OracionesSubTab.ORACIONES_TRADICION -> {
                PrayersScreen(viewModel = viewModel)
            }
            OracionesSubTab.SANTOS_PATRONOS -> {
                PatronsScreen(viewModel = viewModel)
            }
            OracionesSubTab.CALENDARIO -> {
                CalendarScreen(viewModel = viewModel)
            }
        }
    }
}
