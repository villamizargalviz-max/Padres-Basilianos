package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import androidx.compose.animation.*
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.BasilianCalendarDate
import com.example.data.model.CalendarCategory
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.BasilianViewModel
import java.util.Calendar

@Composable
fun CalendarScreen(
    viewModel: BasilianViewModel,
    appLanguage: AppLanguage = AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val strings = remember(appLanguage) { AppStrings.get(appLanguage) }
    val currentMonth by viewModel.calendarMonth.collectAsState()
    val currentYear by viewModel.calendarYear.collectAsState()
    val selectedDay by viewModel.selectedCalendarDay.collectAsState()
    val selectedCategory by viewModel.selectedCalendarCategory.collectAsState()
    val selectedEvent by viewModel.selectedCalendarEvent.collectAsState()
    val activeReminders by viewModel.allReminders.collectAsState()
    val isSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val patronGreetings by viewModel.patronFeastGreetings.collectAsState()
    val userProfile by viewModel.currentUserProfile.collectAsState()

    var patronDetailEvent by remember { mutableStateOf<BasilianCalendarDate?>(null) }

    val allDates = viewModel.calendarDates

    // Events for current month, filtered by category
    val monthEvents = remember(currentMonth, selectedCategory) {
        allDates.filter { date ->
            date.month == currentMonth &&
                    (selectedCategory == CalendarCategory.ALL || date.category == selectedCategory)
        }
    }

    // Days in current month that have at least one Basilian event
    val eventDaysThisMonth = remember(currentMonth) {
        allDates.filter { it.month == currentMonth }.map { it.day }.toSet()
    }

    // Days that are explicitly important for Basilians (Patron feasts & foundation milestones)
    val basilianHighlightDaysThisMonth = remember(currentMonth) {
        allDates.filter { it.month == currentMonth && (it.isBasilianHighlight || it.category == CalendarCategory.PATRON_FEAST || it.category == CalendarCategory.FOUNDATION_ANNIVERSARY) }.map { it.day }.toSet()
    }

    val monthNames = when (appLanguage) {
        AppLanguage.EN -> listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        AppLanguage.FR -> listOf(
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        )
        AppLanguage.ES -> listOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )
    }

    val displayedEvents = remember(monthEvents, selectedDay) {
        if (selectedDay != null && selectedDay!! > 0) {
            monthEvents.filter { it.day == selectedDay }
        } else {
            monthEvents
        }
    }

    val activeRemindersLabel = when (appLanguage) {
        AppLanguage.EN -> "${activeReminders.size} active reminders"
        AppLanguage.FR -> "${activeReminders.size} rappels actifs"
        AppLanguage.ES -> "${activeReminders.size} recordatorios activos"
    }
    val calendarHeaderTitle = strings.recursos.calendarHeaderTitle
    val calendarHeaderSubtitle = when (appLanguage) {
        AppLanguage.EN -> "Important dates, patron feasts, anniversaries of the Congregation of St. Basil, and interactive reminders."
        AppLanguage.FR -> "Dates importantes, saints patrons, anniversaires de la Congrégation de Saint-Basile et rappels interactifs."
        AppLanguage.ES -> "Fechas importantes, patronos, aniversarios de la Congregación de San Basilio y recordatorios interactivos."
    }
    val monthFeastsCountText = when (appLanguage) {
        AppLanguage.EN -> "${monthEvents.size} celebrations this month"
        AppLanguage.FR -> "${monthEvents.size} célébrations ce mois-ci"
        AppLanguage.ES -> "${monthEvents.size} festividades en este mes"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("calendar_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp)
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
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                color = BasilianGold.copy(alpha = 0.2f),
                                shape = CircleShape,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = BasilianGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            // Active reminders count pill
                            Surface(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = BasilianGold,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = activeRemindersLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = calendarHeaderTitle,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = calendarHeaderSubtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        // Interactive Month Navigation Card & Calendar Grid
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("calendar_month_picker"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Month & Year Header Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.prevCalendarMonth() },
                            modifier = Modifier.testTag("calendar_prev_month_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Mes anterior",
                                tint = BasilianBlue
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${monthNames[currentMonth - 1]} $currentYear",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = monthFeastsCountText,
                                style = MaterialTheme.typography.labelSmall,
                                color = BasilianGoldDark
                            )
                        }

                        IconButton(
                            onClick = { viewModel.nextCalendarMonth() },
                            modifier = Modifier.testTag("calendar_next_month_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Mes siguiente",
                                tint = BasilianBlue
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Month Selector Carousel
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(monthNames.indices.toList()) { index ->
                            val monthNum = index + 1
                            val isSelected = monthNum == currentMonth
                            val hasCelebration = allDates.any { it.month == monthNum }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) BasilianNavy else if (hasCelebration) BasilianNavy.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable { viewModel.selectCalendarMonth(monthNum) }
                            ) {
                                Text(
                                    text = monthNames[index].take(3),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Weekdays Header
                    val daysOfWeek = when (appLanguage) {
                        AppLanguage.EN -> listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        AppLanguage.FR -> listOf("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim")
                        AppLanguage.ES -> listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        daysOfWeek.forEach { dayName ->
                            Text(
                                text = dayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(36.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Calendar Days Grid
                    InteractiveCalendarGrid(
                        year = currentYear,
                        month = currentMonth,
                        selectedDay = selectedDay,
                        eventDays = eventDaysThisMonth,
                        basilianHighlightDays = basilianHighlightDaysThisMonth,
                        onDaySelected = { day ->
                            viewModel.selectCalendarDay(day)
                            val highlightEvent = allDates.firstOrNull { it.month == currentMonth && it.day == day && (it.isBasilianHighlight || it.basilianRelationship != null) }
                            if (highlightEvent != null) {
                                patronDetailEvent = highlightEvent
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Indicador de fechas basilianas
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BasilianGold.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = BasilianGoldDark,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = when (appLanguage) {
                                AppLanguage.EN -> "Gold dates: Basilian solemnities and patrons. Tap to view relationship."
                                AppLanguage.FR -> "Dates dorées : Solennités et patrons basiliens. Cliquez pour voir le lien."
                                AppLanguage.ES -> "Fechas en oro: Solemnidades y patronos basilianos. Pulsa para ver su relación."
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = BasilianNavy,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // Red Mundial de Mensajes de «¡Feliz Día!» en Fechas Especiales de Nuestros Patronos
        // (Lo que se escribe en Colombia llega en vivo a Estados Unidos, Canadá, México y Francia)
        item {
            GlobalPatronFeastGreetingsCard(
                allPatronDates = allDates.filter {
                    it.isBasilianHighlight ||
                        it.category == CalendarCategory.PATRON_FEAST ||
                        it.category == CalendarCategory.FOUNDATION_ANNIVERSARY
                },
                currentMonth = currentMonth,
                greetings = patronGreetings,
                defaultUserContext = userProfile?.cityAndCountry?.takeIf { it.isNotBlank() } ?: "🇨🇴 Colombia",
                onPublishGreeting = { event, msg, contextCountry ->
                    viewModel.publishPatronFeastGreeting(
                        eventId = event.id,
                        eventTitle = event.title,
                        patronName = event.patronName ?: event.title,
                        customMessage = msg,
                        customContextOrCountry = contextCountry
                    ) { _, statusMsg ->
                        Toast.makeText(context, statusMsg, Toast.LENGTH_LONG).show()
                    }
                },
                onSpeakGreeting = { textToSpeak ->
                    viewModel.speakText(textToSpeak)
                }
            )
        }

        // Category Filter Chips
        item {
            Column {
                Text(
                    text = when (appLanguage) {
                        AppLanguage.EN -> "Filter by commemoration type"
                        AppLanguage.FR -> "Filtrer par type de commémoration"
                        AppLanguage.ES -> "Filtrar por tipo de conmemoración"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(CalendarCategory.entries.toList()) { category ->
                        val isSelected = selectedCategory == category
                        val categoryLabel = when (appLanguage) {
                            AppLanguage.EN -> when (category) {
                                CalendarCategory.ALL -> "All dates"
                                CalendarCategory.PATRON_FEAST -> "Saints & Patrons"
                                CalendarCategory.FOUNDATION_ANNIVERSARY -> "CSB Anniversaries"
                                CalendarCategory.MISSION_COLOMBIA -> "Colombia Mission"
                                CalendarCategory.SPIRITUAL_MEMORIAL -> "Memorials & Jubilees"
                            }
                            AppLanguage.FR -> when (category) {
                                CalendarCategory.ALL -> "Toutes les dates"
                                CalendarCategory.PATRON_FEAST -> "Saints et Patrons"
                                CalendarCategory.FOUNDATION_ANNIVERSARY -> "Anniversaires CSB"
                                CalendarCategory.MISSION_COLOMBIA -> "Mission Colombie"
                                CalendarCategory.SPIRITUAL_MEMORIAL -> "Mémoires et Jubilés"
                            }
                            AppLanguage.ES -> category.displayName
                        }
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectCalendarCategory(category) },
                            label = { Text(categoryLabel, fontSize = 12.sp) },
                            leadingIcon = if (isSelected) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BasilianBlue,
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Selected Day Details Header or Current Month Events
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val eventsTitle = if (selectedDay != null) {
                    when (appLanguage) {
                        AppLanguage.EN -> "Events of ${monthNames[currentMonth - 1]} $selectedDay"
                        AppLanguage.FR -> "Événements du $selectedDay ${monthNames[currentMonth - 1]}"
                        AppLanguage.ES -> "Acontecimientos del $selectedDay de ${monthNames[currentMonth - 1]}"
                    }
                } else {
                    when (appLanguage) {
                        AppLanguage.EN -> "Feasts in ${monthNames[currentMonth - 1]}"
                        AppLanguage.FR -> "Fêtes en ${monthNames[currentMonth - 1]}"
                        AppLanguage.ES -> "Festividades de ${monthNames[currentMonth - 1]}"
                    }
                }
                Text(
                    text = eventsTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (selectedDay != null) {
                    TextButton(onClick = { viewModel.selectCalendarDay(0) }) {
                        val viewEntireMonth = when (appLanguage) {
                            AppLanguage.EN -> "View entire month"
                            AppLanguage.FR -> "Voir tout le mois"
                            AppLanguage.ES -> "Ver todo el mes"
                        }
                        Text(viewEntireMonth, color = BasilianBlue, fontSize = 12.sp)
                    }
                }
            }
        }

        if (displayedEvents.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.EventBusy,
                            contentDescription = null,
                            tint = BasilianBlue.copy(alpha = 0.6f),
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        val noEventsMsg = when (appLanguage) {
                            AppLanguage.EN -> "No special dates recorded for this specific day."
                            AppLanguage.FR -> "Aucune date spéciale enregistrée pour ce jour particulier."
                            AppLanguage.ES -> "No hay fechas especiales registradas para este día específico."
                        }
                        Text(
                            text = noEventsMsg,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { viewModel.selectCalendarDay(0) },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            val viewAllFeasts = when (appLanguage) {
                                AppLanguage.EN -> "View all feasts of ${monthNames[currentMonth - 1]}"
                                AppLanguage.FR -> "Voir toutes les fêtes de ${monthNames[currentMonth - 1]}"
                                AppLanguage.ES -> "Ver todas las festividades de ${monthNames[currentMonth - 1]}"
                            }
                            Text(viewAllFeasts)
                        }
                    }
                }
            }
        } else {
            items(displayedEvents, key = { it.id }) { rawEvent ->
                val event = com.example.data.repository.BasilianContentLocalization.getLocalizedCalendarDate(rawEvent, appLanguage)
                val isReminderSet = activeReminders.any { it.eventId == event.id }

                CalendarEventCard(
                    event = event,
                    isReminderSet = isReminderSet,
                    isSpeaking = isSpeaking,
                    onToggleReminder = {
                        viewModel.toggleEventReminder(event, context)
                        val msg = if (!isReminderSet) {
                            when (appLanguage) {
                                AppLanguage.EN -> "Reminder scheduled for: ${event.title}"
                                AppLanguage.FR -> "Rappel programmé pour : ${event.title}"
                                AppLanguage.ES -> "Recordatorio programado para: ${event.title}"
                            }
                        } else {
                            when (appLanguage) {
                                AppLanguage.EN -> "Reminder cancelled"
                                AppLanguage.FR -> "Rappel annulé"
                                AppLanguage.ES -> "Recordatorio cancelado"
                            }
                        }
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    },
                    onSyncToDeviceCalendar = {
                        addEventToDeviceCalendar(context, event)
                    },
                    onSpeak = {
                        viewModel.speakText("${event.title}. ${event.briefDescription}. ${event.detailedHistory}")
                    },
                    onOpenPrayer = {
                        event.suggestedPrayerId?.let { prayerId ->
                            val prayer = viewModel.prayersList.firstOrNull { it.id == prayerId }
                            if (prayer != null) {
                                viewModel.selectPrayer(prayer)
                                viewModel.selectTab(AppTab.ORACION)
                            }
                        }
                    },
                    onOpenPatron = {
                        event.relatedPatronOrFounderId?.let { patronId ->
                            val patron = viewModel.patronsList.firstOrNull { it.id == patronId }
                            if (patron != null) {
                                viewModel.selectPatron(patron)
                                viewModel.selectTab(AppTab.PATRONOS)
                            } else {
                                val founder = viewModel.foundersList.firstOrNull { it.id == patronId }
                                if (founder != null) {
                                    viewModel.selectFounder(founder)
                                    viewModel.selectTab(AppTab.FUNDADORES)
                                }
                            }
                        }
                    },
                    eventGreetings = patronGreetings.filter { it.eventId == rawEvent.id || it.patronName.equals(rawEvent.patronName ?: rawEvent.title, ignoreCase = true) },
                    defaultUserContext = userProfile?.cityAndCountry?.takeIf { it.isNotBlank() } ?: "🇨🇴 Colombia",
                    onSendHappyDayMessage = { msgText, countryContext ->
                        viewModel.publishPatronFeastGreeting(
                            eventId = rawEvent.id,
                            eventTitle = event.title,
                            patronName = event.patronName ?: event.title,
                            customMessage = msgText,
                            customContextOrCountry = countryContext
                        ) { _, statusMsg ->
                            Toast.makeText(context, statusMsg, Toast.LENGTH_LONG).show()
                        }
                    },
                    appLanguage = appLanguage
                )
            }
        }
    }

    // Modal de Diálogo: Patrono y su relación con los Padres Basilianos
    patronDetailEvent?.let { rawPatronEvent ->
        val patronEvent = com.example.data.repository.BasilianContentLocalization.getLocalizedCalendarDate(rawPatronEvent, appLanguage)
        AlertDialog(
            onDismissRequest = { patronDetailEvent = null },
            icon = {
                Surface(
                    shape = CircleShape,
                    color = BasilianGold.copy(alpha = 0.2f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = BasilianGoldDark,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "BASILIAN PATRON & FEAST"
                            AppLanguage.FR -> "PATRON ET DATE BASILIENNE"
                            AppLanguage.ES -> "PATRONO Y FECHA BASILIANA"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = BasilianGoldDark,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = patronEvent.patronName ?: patronEvent.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BasilianNavy,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BasilianNavy.copy(alpha = 0.08f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val datePrefix = when (appLanguage) {
                            AppLanguage.EN -> "Liturgical Date:"
                            AppLanguage.FR -> "Date liturgique :"
                            AppLanguage.ES -> "Fecha Litúrgica:"
                        }
                        Text(
                            text = "$datePrefix ${patronEvent.dateFormatted} • ${patronEvent.liturgicalRank}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BasilianNavy,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    Text(
                        text = patronEvent.briefDescription,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BasilianGold.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, BasilianGoldDark.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Church,
                                    contentDescription = null,
                                    tint = BasilianGoldDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = when (appLanguage) {
                                        AppLanguage.EN -> "Bond with the Congregation of Saint Basil:"
                                        AppLanguage.FR -> "Lien avec la Congrégation de Saint-Basile :"
                                        AppLanguage.ES -> "Vínculo con la Congregación de San Basilio:"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BasilianGoldDark
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = patronEvent.basilianRelationship ?: patronEvent.detailedHistory,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF2C241E),
                                lineHeight = 20.sp
                            )
                        }
                    }

                    // Enviar mensaje mundial de "¡Feliz Día!" desde el diálogo del Patrono
                    var dialogGreetingText by remember(rawPatronEvent.id) { mutableStateOf("") }
                    var dialogSentBanner by remember(rawPatronEvent.id) { mutableStateOf<String?>(null) }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BasilianBlue.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, BasilianBlue.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "🌍 Enviar «¡Feliz Día!» para toda la Congregación (Colombia, EE. UU., Canadá, México, Francia):",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = BasilianNavy
                            )
                            OutlinedTextField(
                                value = dialogGreetingText,
                                onValueChange = { dialogGreetingText = it },
                                placeholder = {
                                    Text(
                                        "Ej: ¡Feliz día de ${patronEvent.patronName ?: patronEvent.title} desde Colombia para todos los hermanos!",
                                        fontSize = 11.sp
                                    )
                                },
                                singleLine = false,
                                maxLines = 2,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    val msgToSend = dialogGreetingText.trim().ifBlank {
                                        "¡Feliz día de ${patronEvent.patronName ?: patronEvent.title}! Un abrazo fraterno para toda la Congregación en Colombia, Estados Unidos, Canadá, México y Francia."
                                    }
                                    viewModel.publishPatronFeastGreeting(
                                        eventId = rawPatronEvent.id,
                                        eventTitle = patronEvent.title,
                                        patronName = patronEvent.patronName ?: patronEvent.title,
                                        customMessage = msgToSend,
                                        customContextOrCountry = userProfile?.cityAndCountry?.takeIf { it.isNotBlank() } ?: "🇨🇴 Colombia"
                                    ) { _, statusMsg ->
                                        dialogSentBanner = statusMsg
                                        dialogGreetingText = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BasilianGoldDark),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Replicar ¡Feliz Día! en Todas las Apps", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            if (dialogSentBanner != null) {
                                Text(
                                    text = dialogSentBanner!!,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { patronDetailEvent = null },
                    colors = ButtonDefaults.buttonColors(containerColor = BasilianNavy)
                ) {
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "Understood"
                            AppLanguage.FR -> "Compris"
                            AppLanguage.ES -> "Entendido"
                        },
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        val text = "${patronEvent.patronName ?: patronEvent.title}. ${patronEvent.briefDescription}. Relación con los Basilianos: ${patronEvent.basilianRelationship ?: patronEvent.detailedHistory}"
                        viewModel.speakText(text)
                    }
                ) {
                    Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "Listen"
                            AppLanguage.FR -> "Écouter"
                            AppLanguage.ES -> "Escuchar"
                        }
                    )
                }
            }
        )
    }
}

@Composable
private fun InteractiveCalendarGrid(
    year: Int,
    month: Int,
    selectedDay: Int?,
    eventDays: Set<Int>,
    basilianHighlightDays: Set<Int> = emptySet(),
    onDaySelected: (Int) -> Unit
) {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, 1)
    }

    // Days in month
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    // Day of week for 1st day (Sunday = 1, Monday = 2, ...)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    // Convert to Monday = 0, Sunday = 6
    val offset = (firstDayOfWeek + 5) % 7

    val todayCal = Calendar.getInstance()
    val isCurrentRealMonth = todayCal.get(Calendar.YEAR) == year && (todayCal.get(Calendar.MONTH) + 1) == month
    val realToday = if (isCurrentRealMonth) todayCal.get(Calendar.DAY_OF_MONTH) else -1

    val totalCells = offset + daysInMonth
    val rows = (totalCells + 6) / 7

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (r in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (c in 0 until 7) {
                    val cellIndex = r * 7 + c
                    val dayNumber = cellIndex - offset + 1

                    if (dayNumber in 1..daysInMonth) {
                        val isSelected = selectedDay == dayNumber
                        val hasEvent = eventDays.contains(dayNumber)
                        val isBasilianHighlight = basilianHighlightDays.contains(dayNumber)
                        val isToday = dayNumber == realToday

                        DayCell(
                            day = dayNumber,
                            isSelected = isSelected,
                            hasEvent = hasEvent,
                            isBasilianHighlight = isBasilianHighlight,
                            isToday = isToday,
                            onClick = { onDaySelected(dayNumber) }
                        )
                    } else {
                        // Empty spacer cell
                        Spacer(modifier = Modifier.size(38.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    day: Int,
    isSelected: Boolean,
    hasEvent: Boolean,
    isBasilianHighlight: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> BasilianNavy
                    isBasilianHighlight -> BasilianGold.copy(alpha = 0.35f)
                    isToday -> BasilianGold.copy(alpha = 0.25f)
                    hasEvent -> BasilianBlue.copy(alpha = 0.1f)
                    else -> Color.Transparent
                }
            )
            .then(
                when {
                    isSelected -> Modifier
                    isBasilianHighlight -> Modifier.border(1.5.dp, BasilianGoldDark, CircleShape)
                    isToday -> Modifier.border(1.5.dp, BasilianGoldDark, CircleShape)
                    else -> Modifier
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected || isToday || hasEvent || isBasilianHighlight) FontWeight.Bold else FontWeight.Normal,
                color = when {
                    isSelected -> Color.White
                    isBasilianHighlight -> Color(0xFF5D4037)
                    isToday -> BasilianGoldDark
                    hasEvent -> BasilianBlue
                    else -> MaterialTheme.colorScheme.onSurface
                },
                fontSize = 13.sp
            )

            if (hasEvent || isBasilianHighlight) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .size(if (isBasilianHighlight) 6.dp else 5.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isSelected -> BasilianGold
                                isBasilianHighlight -> BasilianGoldDark
                                else -> BasilianCrimson
                            }
                        )
                )
            }
        }
    }
}

@Composable
private fun CalendarEventCard(
    event: BasilianCalendarDate,
    isReminderSet: Boolean,
    isSpeaking: Boolean,
    onToggleReminder: () -> Unit,
    onSyncToDeviceCalendar: () -> Unit,
    onSpeak: () -> Unit,
    onOpenPrayer: () -> Unit,
    onOpenPatron: () -> Unit,
    eventGreetings: List<com.example.data.firebase.PatronFeastGreetingMessage> = emptyList(),
    defaultUserContext: String = "🇨🇴 Colombia",
    onSendHappyDayMessage: (String, String) -> Unit = { _, _ -> },
    appLanguage: AppLanguage = AppLanguage.ES
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("calendar_event_card_${event.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(
            1.dp,
            if (isReminderSet) BasilianGold else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Date and Liturgical Rank Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = BasilianNavy.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = BasilianBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = event.dateFormatted,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BasilianNavy
                        )
                    }
                }

                Surface(
                    color = when (event.category) {
                        CalendarCategory.PATRON_FEAST -> BasilianGold.copy(alpha = 0.2f)
                        CalendarCategory.FOUNDATION_ANNIVERSARY -> BasilianCrimson.copy(alpha = 0.12f)
                        CalendarCategory.MISSION_COLOMBIA -> BasilianNavy.copy(alpha = 0.12f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = event.liturgicalRank,
                        style = MaterialTheme.typography.labelSmall,
                        color = when (event.category) {
                            CalendarCategory.PATRON_FEAST -> BasilianGoldDark
                            CalendarCategory.FOUNDATION_ANNIVERSARY -> BasilianCrimson
                            CalendarCategory.MISSION_COLOMBIA -> BasilianBlue
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Brief Description
            Text(
                text = event.briefDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Detailed Historical Significance
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = BasilianBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (appLanguage) {
                                AppLanguage.EN -> "Spiritual and historical significance:"
                                AppLanguage.FR -> "Signification spirituelle et historique :"
                                AppLanguage.ES -> "Significado espiritual e histórico:"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BasilianBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = event.detailedHistory,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )
                }
            }

            // Bloque Destacado: Patrono y su relación con los Padres Basilianos
            if (event.isBasilianHighlight || event.basilianRelationship != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = BasilianGold.copy(alpha = 0.15f),
                    border = BorderStroke(1.2.dp, BasilianGoldDark.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = BasilianGoldDark,
                                modifier = Modifier.size(17.dp)
                            )
                            Text(
                                text = when (appLanguage) {
                                    AppLanguage.EN -> "PATRON & RELATIONSHIP WITH BASILIANS"
                                    AppLanguage.FR -> "PATRON ET LIEN AVEC LES BASILIENS"
                                    AppLanguage.ES -> "PATRONO Y SU RELACIÓN CON LOS BASILIANOS"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = BasilianGoldDark,
                                letterSpacing = 0.6.sp
                            )
                        }

                        if (event.patronName != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = event.patronName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = BasilianNavy
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = event.basilianRelationship ?: event.detailedHistory,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF2E241E),
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Parte interactiva para colocar un mensajito de ¡Feliz Día! que se replica a todas las apps
                        var showInlineGreetingBox by remember { mutableStateOf(false) }
                        var inlineGreetingText by remember { mutableStateOf("") }
                        var inlineCountryContext by remember { mutableStateOf(defaultUserContext) }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BasilianNavy.copy(alpha = 0.08f),
                            border = BorderStroke(1.dp, BasilianBlue.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Celebration,
                                            contentDescription = null,
                                            tint = BasilianGoldDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Column {
                                            Text(
                                                text = "🎉 Mensaje Mundial de «¡Feliz Día!»",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = BasilianNavy
                                            )
                                            Text(
                                                text = "Se replica en vivo a Colombia, EE. UU., Canadá, México y Francia",
                                                fontSize = 10.sp,
                                                color = BasilianBlue
                                            )
                                        }
                                    }
                                    Button(
                                        onClick = { showInlineGreetingBox = !showInlineGreetingBox },
                                        colors = ButtonDefaults.buttonColors(containerColor = BasilianGoldDark),
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
                                        modifier = Modifier.testTag("btn_toggle_patron_greeting_${event.id}")
                                    ) {
                                        Text(
                                            text = if (showInlineGreetingBox) "Cerrar" else "Escribir ¡Feliz Día!",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                AnimatedVisibility(visible = showInlineGreetingBox) {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        val contexts = listOf(
                                            "🇨🇴 Colombia",
                                            "🇺🇸 Estados Unidos",
                                            "🇨🇦 Canadá",
                                            "🇲🇽 México",
                                            "🇫🇷 Francia"
                                        )
                                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            items(contexts) { ctx ->
                                                val selected = inlineCountryContext.contains(ctx.substringAfter(" "))
                                                FilterChip(
                                                    selected = selected,
                                                    onClick = { inlineCountryContext = ctx },
                                                    label = { Text(ctx, fontSize = 10.5.sp) }
                                                )
                                            }
                                        }
                                        OutlinedTextField(
                                            value = inlineGreetingText,
                                            onValueChange = { inlineGreetingText = it },
                                            label = { Text("Tu mensaje de ¡Feliz Día! para toda la Congregación", fontSize = 11.sp) },
                                            placeholder = {
                                                Text(
                                                    "Ej: ¡Feliz día de ${event.patronName ?: event.title} desde $inlineCountryContext para todos nuestros hermanos!",
                                                    fontSize = 11.sp
                                                )
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("input_patron_greeting_${event.id}")
                                        )
                                        Button(
                                            onClick = {
                                                val finalMsg = inlineGreetingText.trim().ifBlank {
                                                    "¡Feliz día de ${event.patronName ?: event.title} desde $inlineCountryContext! Un abrazo fraterno para toda la Congregación."
                                                }
                                                onSendHappyDayMessage(finalMsg, inlineCountryContext)
                                                inlineGreetingText = ""
                                                showInlineGreetingBox = false
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = BasilianNavy),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("btn_send_patron_greeting_${event.id}")
                                        ) {
                                            Icon(Icons.Default.Public, contentDescription = null, modifier = Modifier.size(16.dp), tint = BasilianGold)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Enviar y Replicar en Todas las Apps",
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }

                                if (eventGreetings.isNotEmpty()) {
                                    eventGreetings.take(3).forEach { g ->
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color.White.copy(alpha = 0.85f),
                                            border = BorderStroke(0.8.dp, BasilianGoldDark.copy(alpha = 0.45f)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Text(
                                                    text = "«${g.message}»",
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = BasilianNavy
                                                )
                                                Text(
                                                    text = "— ${g.authorName} • ${g.authorCountryOrContext}",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = BasilianGoldDark
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

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row: Reminder, Sync Calendar, Listen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Reminder Button
                Button(
                    onClick = onToggleReminder,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isReminderSet) BasilianGoldDark else BasilianBlue
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("reminder_button_${event.id}"),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = if (isReminderSet) Icons.Default.NotificationsActive else Icons.Default.NotificationAdd,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isReminderSet) {
                            when (appLanguage) {
                                AppLanguage.EN -> "Reminder set"
                                AppLanguage.FR -> "Rappel actif"
                                AppLanguage.ES -> "Recordatorio activo"
                            }
                        } else {
                            when (appLanguage) {
                                AppLanguage.EN -> "Add reminder"
                                AppLanguage.FR -> "Ajouter rappel"
                                AppLanguage.ES -> "Añadir recordatorio"
                            }
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Device Calendar Sync
                OutlinedButton(
                    onClick = onSyncToDeviceCalendar,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("calendar_sync_button_${event.id}"),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = when (appLanguage) {
                            AppLanguage.EN -> "Save to device calendar"
                            AppLanguage.FR -> "Enregistrer dans le calendrier"
                            AppLanguage.ES -> "Guardar en calendario del teléfono"
                        },
                        tint = BasilianBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Audio Listen
                OutlinedButton(
                    onClick = onSpeak,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                        contentDescription = when (appLanguage) {
                            AppLanguage.EN -> "Listen to event"
                            AppLanguage.FR -> "Écouter l'événement"
                            AppLanguage.ES -> "Escuchar fecha"
                        },
                        tint = BasilianBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Optional Quick Navigation Row (Prayer or Patron)
            if (event.suggestedPrayerId != null || event.relatedPatronOrFounderId != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (event.suggestedPrayerId != null) {
                        OutlinedButton(
                            onClick = onOpenPrayer,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = BasilianCrimson
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when (appLanguage) {
                                    AppLanguage.EN -> "Pray"
                                    AppLanguage.FR -> "Prier"
                                    AppLanguage.ES -> "Rezar oración"
                                },
                                fontSize = 11.sp,
                                color = BasilianCrimson
                            )
                        }
                    }

                    if (event.relatedPatronOrFounderId != null) {
                        OutlinedButton(
                            onClick = onOpenPatron,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = BasilianBlue
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when (appLanguage) {
                                    AppLanguage.EN -> "View profile"
                                    AppLanguage.FR -> "Voir profil"
                                    AppLanguage.ES -> "Ver perfil"
                                },
                                fontSize = 11.sp,
                                color = BasilianBlue
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun addEventToDeviceCalendar(context: Context, event: BasilianCalendarDate) {
    try {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, event.month - 1)
            set(Calendar.DAY_OF_MONTH, event.day)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.YEAR, 1)
            }
        }

        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.timeInMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
            .putExtra(CalendarContract.Events.TITLE, event.title)
            .putExtra(CalendarContract.Events.DESCRIPTION, "${event.briefDescription}\n\n${event.detailedHistory}")
            .putExtra(CalendarContract.Events.EVENT_LOCATION, "Congregación de San Basilio")

        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "No se encontró aplicación de calendario compatible", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Tarjeta Global en el Calendario para escribir y ver Mensajes de «¡Feliz Día!»
 * en las fechas especiales de nuestros Santos Patronos y Aniversarios Basilianos,
 * replicándose en tiempo real entre Colombia, Estados Unidos, Canadá, México y Francia.
 */
@Composable
private fun GlobalPatronFeastGreetingsCard(
    allPatronDates: List<BasilianCalendarDate>,
    currentMonth: Int,
    greetings: List<com.example.data.firebase.PatronFeastGreetingMessage>,
    defaultUserContext: String,
    onPublishGreeting: (BasilianCalendarDate, String, String) -> Unit,
    onSpeakGreeting: (String) -> Unit
) {
    val patronDatesForPicker = remember(allPatronDates, currentMonth) {
        val inMonth = allPatronDates.filter { it.month == currentMonth }
        if (inMonth.isNotEmpty()) inMonth + allPatronDates.filter { it.month != currentMonth }
        else allPatronDates
    }
    var selectedPatronEvent by remember(patronDatesForPicker) {
        mutableStateOf(patronDatesForPicker.firstOrNull())
    }
    var customGreetingInput by remember { mutableStateOf("") }
    var selectedCountryContext by remember(defaultUserContext) {
        mutableStateOf(
            if (defaultUserContext.isNotBlank()) defaultUserContext else "🇨🇴 Colombia • Casa Annonay"
        )
    }

    val countryContexts = listOf(
        "🇨🇴 Colombia • Misión C.S.B.",
        "🇺🇸 Estados Unidos • Basilian Fathers",
        "🇨🇦 Canadá • Toronto / Curia C.S.B.",
        "🇲🇽 México • Comunidad C.S.B.",
        "🇫🇷 Francia • Annonay (Cuna 1822)"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("global_patron_greetings_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0A192F)),
        border = BorderStroke(1.5.dp, BasilianGold),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = BasilianGold.copy(alpha = 0.22f),
                    border = BorderStroke(1.dp, BasilianGold),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            tint = BasilianGold,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🌍 Red Mundial de «¡Feliz Día!» • Nuestros Patronos",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = BasilianGold
                    )
                    Text(
                        text = "Escribe un mensaje de Feliz Día desde tu país (ej. Colombia) y les llegará en vivo a todas las aplicaciones en EE. UU., Canadá, México y Francia.",
                        fontSize = 11.sp,
                        color = Color(0xFFE0F2FE),
                        lineHeight = 14.sp
                    )
                }
            }

            // 1. Seleccionar Fecha Especial / Patrono
            Text(
                text = "1. Fiesta Patronal o Fecha Especial Basiliana:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFDE68A)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(patronDatesForPicker, key = { it.id }) { pDate ->
                    val isSelected = selectedPatronEvent?.id == pDate.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedPatronEvent = pDate },
                        label = {
                            Text(
                                text = "${pDate.patronName ?: pDate.title} (${pDate.dateFormatted})",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BasilianGold,
                            selectedLabelColor = Color(0xFF020617),
                            containerColor = Color.White.copy(alpha = 0.08f),
                            labelColor = Color.White
                        )
                    )
                }
            }

            // 2. Seleccionar País / Contexto desde donde saludas
            Text(
                text = "2. Desde qué país o comunidad envías el saludo:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFBAE6FD)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(countryContexts) { ctx ->
                    val isSelected = selectedCountryContext == ctx
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCountryContext = ctx },
                        label = {
                            Text(
                                text = ctx,
                                fontSize = 10.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF0284C7),
                            selectedLabelColor = Color.White,
                            containerColor = Color.White.copy(alpha = 0.06f),
                            labelColor = Color(0xFFBAE6FD)
                        )
                    )
                }
            }

            // 3. Campo para escribir el mensaje de ¡Feliz Día! + frases rápidas
            val activePatronLabel = selectedPatronEvent?.let { it.patronName ?: it.title } ?: "Nuestro Santo Patrono"
            val quickTemplates = remember(activePatronLabel, selectedCountryContext) {
                listOf(
                    "¡Feliz día de $activePatronLabel! Un abrazo fraterno desde $selectedCountryContext para todos los hermanos en Estados Unidos, Canadá, México, Francia y Colombia.",
                    "¡Bendecida solemnidad de $activePatronLabel! Unidos en oración en Bondad, Disciplina y Ciencia.",
                    "¡Feliz fiesta patronal de $activePatronLabel para toda la familia de los Padres Basilianos C.S.B.!"
                )
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(quickTemplates) { tpl ->
                    AssistChip(
                        onClick = { customGreetingInput = tpl },
                        label = {
                            Text(
                                text = "✨ ${tpl.take(48)}...",
                                fontSize = 10.sp,
                                color = Color(0xFFFDE68A)
                            )
                        },
                        border = BorderStroke(0.8.dp, BasilianGold.copy(alpha = 0.5f))
                    )
                }
            }

            OutlinedTextField(
                value = customGreetingInput,
                onValueChange = { customGreetingInput = it },
                label = {
                    Text(
                        "Escribe tu mensaje de «¡Feliz Día!» para toda la Congregación",
                        fontSize = 11.sp,
                        color = BasilianGold
                    )
                },
                placeholder = {
                    Text(
                        "Ej: ¡Feliz día de $activePatronLabel desde Colombia para todos los hermanos en EE. UU., Canadá, México y Francia!",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.45f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = BasilianGold,
                    unfocusedBorderColor = BasilianGold.copy(alpha = 0.45f),
                    cursorColor = BasilianGold
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("global_patron_greeting_input")
            )

            Button(
                onClick = {
                    val targetEvent = selectedPatronEvent ?: allPatronDates.firstOrNull() ?: return@Button
                    val messageToSend = customGreetingInput.trim().ifBlank {
                        "¡Feliz día de $activePatronLabel! Un saludo fraterno desde $selectedCountryContext para todos nuestros hermanos basilianos en el mundo."
                    }
                    onPublishGreeting(targetEvent, messageToSend, selectedCountryContext)
                    customGreetingInput = ""
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BasilianGold,
                    contentColor = Color(0xFF020617)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_publish_global_patron_greeting")
            ) {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(17.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Enviar «¡Feliz Día!» a Todas las Aplicaciones C.S.B.",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.5.sp
                )
            }

            // 4. Muro en Vivo de Mensajes recibidos desde los diferentes contextos y países
            if (greetings.isNotEmpty()) {
                HorizontalDivider(color = Color.White.copy(alpha = 0.14f))
                Text(
                    text = "📬 Mensajes de «¡Feliz Día!» Replicados en la Congregación (${greetings.size}):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF38BDF8)
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    greetings.take(5).forEach { g ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.07f),
                            border = BorderStroke(1.dp, BasilianGold.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = BasilianGold.copy(alpha = 0.2f)
                                        ) {
                                            Text(
                                                text = g.patronName.ifBlank { g.eventTitle },
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = BasilianGold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text(
                                            text = g.authorCountryOrContext,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF38BDF8)
                                        )
                                    }
                                    Text(
                                        text = "«${g.message}»",
                                        fontSize = 11.5.sp,
                                        color = Color.White,
                                        lineHeight = 15.sp
                                    )
                                    Text(
                                        text = "Enviado por: ${g.authorName}",
                                        fontSize = 10.sp,
                                        color = Color(0xFFFDE68A),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        onSpeakGreeting("Mensaje de feliz día por ${g.patronName}, enviado por ${g.authorName} desde ${g.authorCountryOrContext}: ${g.message}")
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Escuchar saludo",
                                        tint = BasilianGold,
                                        modifier = Modifier.size(18.dp)
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
