package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BasilianDirectoryMember
import com.example.data.model.ColombianLiturgicalDay
import com.example.data.model.DeceasedBrother
import com.example.data.model.DeceasedCategory
import com.example.data.model.LiturgicalHourType
import com.example.data.model.MemberCategory
import com.example.data.repository.BasilianDirectoryRepository
import com.example.data.repository.BasilianDirectoryLocalization
import com.example.data.repository.LiturgiaOrdoColombianoRepository
import com.example.data.repository.MisaDelDiaDevocionarioRepository
import com.example.ui.components.FuturisticDesignPalette
import com.example.ui.components.FuturisticQuoteCalloutCard
import com.example.ui.components.ScheduledNotificationsSection
import com.example.ui.components.CEC_ORDO_INICIO_URL
import com.example.ui.components.OrdoCecWebView
import com.example.ui.components.VirtualCandleComponent
import com.example.ui.components.VirtualCandleMini
import com.example.ui.components.futuristic3DCard
import com.example.ui.theme.BasilianAppTheme
import com.example.ui.viewmodel.BasilianViewModel
import java.util.Calendar
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectorioCumpleanosScreen(
    viewModel: BasilianViewModel,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appTheme by viewModel.currentAppTheme.collectAsState()
    val strings = com.example.ui.i18n.AppStrings.get(appLanguage)

    var selectedMainTab by remember { mutableIntStateOf(0) } // 0 = Cumpleaños & Hermanos Vivos, 1 = Hermanos Difuntos
    val searchQuery by viewModel.directorySearchQuery.collectAsState()
    val countryFilter by viewModel.selectedCountryFilter.collectAsState()
    val memberCatFilter by viewModel.selectedMemberCategory.collectAsState()
    val monthFilter by viewModel.selectedMonthFilter.collectAsState()

    val deceasedCatFilter by viewModel.selectedDeceasedCategory.collectAsState()
    val deceasedSearchQuery by viewModel.deceasedSearchQuery.collectAsState()
    val litCandleIds by viewModel.litCandleBrotherIds.collectAsState()
    val customCumpleanosCover by viewModel.customCumpleanosCoverUri.collectAsState()
    val userProfile by viewModel.currentUserProfile.collectAsState()
    val intensity3D by viewModel.futuristic3DIntensity.collectAsState()
    val isBasilianPrivateAccess by viewModel.isBasilianPrivateAccess.collectAsState()
    val isCuriaAdmin by viewModel.curiaIsAuthenticated.collectAsState()
    val hasFullBasilianAccess = isBasilianPrivateAccess || isCuriaAdmin
    var permissionRequestedBanner by remember { mutableStateOf(false) }
    var unlockCodeInput by remember { mutableStateOf("") }

    val cumpleanosImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.setCustomCumpleanosCover(uri)
        }
    }

    // Fecha actual para calcular cumpleaños de hoy y días restantes
    val now = remember { Calendar.getInstance() }
    val currentMonth = now.get(Calendar.MONTH) + 1 // 1..12
    val currentDay = now.get(Calendar.DAY_OF_MONTH)

    val todayBirthdays = remember(currentMonth, currentDay) {
        BasilianDirectoryRepository.getTodayBirthdays(currentMonth, currentDay)
    }
    val upcomingBirthdays = remember(currentMonth, currentDay) {
        BasilianDirectoryRepository.getUpcomingBirthdays(currentMonth, currentDay, limit = 8)
    }

    val filteredMembers = remember(searchQuery, countryFilter, memberCatFilter, monthFilter) {
        BasilianDirectoryRepository.members.filter { member ->
            val matchesSearch = searchQuery.isBlank() ||
                    member.name.contains(searchQuery, ignoreCase = true) ||
                    member.currentHouse.contains(searchQuery, ignoreCase = true) ||
                    member.country.contains(searchQuery, ignoreCase = true) ||
                    member.rolesOrMinistries.contains(searchQuery, ignoreCase = true) ||
                    member.email.contains(searchQuery, ignoreCase = true)

            val matchesCountry = countryFilter == "Todos" || member.country.equals(countryFilter, ignoreCase = true)
            val matchesCategory = memberCatFilter == MemberCategory.ALL || member.category == memberCatFilter
            val matchesMonth = monthFilter == null || member.birthMonth == monthFilter

            matchesSearch && matchesCountry && matchesCategory && matchesMonth
        }
    }

    val filteredDeceased = remember(deceasedCatFilter, deceasedSearchQuery, appLanguage) {
        BasilianDirectoryRepository.deceasedBrothers.filter { dec ->
            val localizedTitle = BasilianDirectoryLocalization.getLocalizedTitle(dec, appLanguage)
            val localizedQuote = BasilianDirectoryLocalization.getLocalizedQuote(dec, appLanguage)
            val localizedName = BasilianDirectoryLocalization.getLocalizedName(dec, appLanguage)
            val localizedDate = BasilianDirectoryLocalization.getLocalizedDatePassed(dec, appLanguage)
            val matchesSearch = deceasedSearchQuery.isBlank() ||
                    dec.name.contains(deceasedSearchQuery, ignoreCase = true) ||
                    localizedName.contains(deceasedSearchQuery, ignoreCase = true) ||
                    dec.titleOrRole.contains(deceasedSearchQuery, ignoreCase = true) ||
                    localizedTitle.contains(deceasedSearchQuery, ignoreCase = true) ||
                    dec.memorialQuote.contains(deceasedSearchQuery, ignoreCase = true) ||
                    localizedQuote.contains(deceasedSearchQuery, ignoreCase = true) ||
                    dec.datePassed.contains(deceasedSearchQuery, ignoreCase = true) ||
                    localizedDate.contains(deceasedSearchQuery, ignoreCase = true) ||
                    dec.birthAndDeathYears.contains(deceasedSearchQuery, ignoreCase = true)

            val matchesCat = deceasedCatFilter == DeceasedCategory.TODOS || dec.category == deceasedCatFilter

            matchesSearch && matchesCat
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .testTag("directorio_cumpleanos_screen")
    ) {
        if (!hasFullBasilianAccess) {
            // Para usuarios NO basilianos: Únicamente el Ordo Colombiano Oficial (https://web-ordo-colombiano.cec.org.co/inicio)
            // integrado directamente como propio de la app, práctico y sin pedir contraseñas ni mostrar contenido interno.
            OrdoCecWebView(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("non_basilian_ordo_cec_direct_view"),
                initialUrl = CEC_ORDO_INICIO_URL,
                displayTitle = "Liturgia & Ordo Colombiano • CEC",
                appLanguage = appLanguage,
                onSwitchToNativeMode = null
            )
        } else {
            // Selector de Pestaña Principal para Religiosos Basilianos (Cumpleaños / Memoria Pascual)
            PrimaryTabRow(
                selectedTabIndex = selectedMainTab,
                containerColor = if (appTheme.isDark) Color(0xFF0A192F).copy(alpha = 0.92f) else Color.White.copy(alpha = 0.92f),
                contentColor = appTheme.primaryColor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedMainTab == 0,
                    onClick = { selectedMainTab = 0 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cake,
                                contentDescription = null,
                                tint = if (selectedMainTab == 0) appTheme.primaryColor else Color.Gray,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${strings.directorio.tabBirthdays} (${BasilianDirectoryRepository.members.size})",
                                fontWeight = if (selectedMainTab == 0) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    modifier = Modifier.testTag("tab_directorio_vivos")
                )
                Tab(
                    selected = selectedMainTab == 1,
                    onClick = { selectedMainTab = 1 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = if (selectedMainTab == 1) appTheme.secondaryColor else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${strings.directorio.tabEaster} (${BasilianDirectoryRepository.deceasedBrothers.size})",
                                fontWeight = if (selectedMainTab == 1) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    modifier = Modifier.testTag("tab_directorio_difuntos")
                )
            }

            when (selectedMainTab) {
                0 -> {
                    HermanosCumpleanosContent(
                        members = filteredMembers,
                        todayBirthdays = todayBirthdays,
                        upcomingBirthdays = upcomingBirthdays,
                        currentMonth = currentMonth,
                        currentDay = currentDay,
                        searchQuery = searchQuery,
                        countryFilter = countryFilter,
                        memberCatFilter = memberCatFilter,
                        monthFilter = monthFilter,
                        appTheme = appTheme,
                        appLanguage = appLanguage,
                        customCoverUri = customCumpleanosCover,
                        onPickCover = {
                            cumpleanosImagePicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        onResetCover = { viewModel.resetCustomCumpleanosCover() },
                        onSearchChange = { viewModel.setDirectorySearchQuery(it) },
                        onCountryChange = { viewModel.setSelectedCountryFilter(it) },
                        onCategoryChange = { viewModel.setSelectedMemberCategory(it) },
                        onMonthChange = { viewModel.setSelectedMonthFilter(it) },
                        onSendBirthdayGreeting = { member ->
                            sendBirthdayGreeting(context, member, appLanguage)
                        }
                    )
                }
                1 -> {
                    HermanosDifuntosContent(
                        deceasedList = filteredDeceased,
                        selectedCat = deceasedCatFilter,
                        searchQuery = deceasedSearchQuery,
                        litCandleIds = litCandleIds,
                        currentMonth = currentMonth,
                        currentDay = currentDay,
                        appTheme = appTheme,
                        appLanguage = appLanguage,
                        onCatChange = { viewModel.setDeceasedCategory(it) },
                        onSearchChange = { viewModel.setDeceasedSearchQuery(it) },
                        onToggleCandle = { brotherId ->
                            viewModel.toggleCandleForBrother(brotherId)
                            Toast.makeText(context, strings.directorio.candleLitToast, Toast.LENGTH_SHORT).show()
                        },
                        onPrayResponsorio = {
                            viewModel.speakText(strings.directorio.responsorialPrayerText)
                        },
                        onSpeakCustomPrayer = { prayerText ->
                            viewModel.speakText(prayerText)
                        }
                    )
                }
                else -> {
                    HermanosCumpleanosContent(
                        members = filteredMembers,
                        todayBirthdays = todayBirthdays,
                        upcomingBirthdays = upcomingBirthdays,
                        currentMonth = currentMonth,
                        currentDay = currentDay,
                        searchQuery = searchQuery,
                        countryFilter = countryFilter,
                        memberCatFilter = memberCatFilter,
                        monthFilter = monthFilter,
                        appTheme = appTheme,
                        appLanguage = appLanguage,
                        customCoverUri = customCumpleanosCover,
                        onPickCover = {
                            cumpleanosImagePicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        onResetCover = { viewModel.resetCustomCumpleanosCover() },
                        onSearchChange = { viewModel.setDirectorySearchQuery(it) },
                        onCountryChange = { viewModel.setSelectedCountryFilter(it) },
                        onCategoryChange = { viewModel.setSelectedMemberCategory(it) },
                        onMonthChange = { viewModel.setSelectedMonthFilter(it) },
                        onSendBirthdayGreeting = { member ->
                            sendBirthdayGreeting(context, member, appLanguage)
                        }
                    )
                }
            }
        }
    }
}

// =============================================================================
// SUB-COMPONENTE: PESTAÑA 1 - CUMPLEAÑOS Y HERMANOS VIVOS
// =============================================================================
@Composable
fun HermanosCumpleanosContent(
    members: List<BasilianDirectoryMember>,
    todayBirthdays: List<BasilianDirectoryMember>,
    upcomingBirthdays: List<BasilianDirectoryMember>,
    currentMonth: Int,
    currentDay: Int,
    searchQuery: String,
    countryFilter: String,
    memberCatFilter: MemberCategory,
    monthFilter: Int?,
    appTheme: BasilianAppTheme,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES,
    customCoverUri: String? = null,
    customGallerySlots: Map<String, String> = emptyMap(),
    onUploadGallerySlot: (String, Uri) -> Unit = { _, _ -> },
    onRemoveGallerySlot: (String) -> Unit = {},
    onPickCover: () -> Unit = {},
    onResetCover: () -> Unit = {},
    onSearchChange: (String) -> Unit,
    onCountryChange: (String) -> Unit,
    onCategoryChange: (MemberCategory) -> Unit,
    onMonthChange: (Int?) -> Unit,
    onSendBirthdayGreeting: (BasilianDirectoryMember) -> Unit
) {
    val context = LocalContext.current
    val strings = remember(appLanguage) { com.example.ui.i18n.AppStrings.get(appLanguage) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. HERO BANNER DE CUMPLEAÑOS LLAMATIVO CON GLOBOS Y TORTA
        item {
            BirthdayHeroBanner(
                todayBirthdays = todayBirthdays,
                upcomingBirthdays = upcomingBirthdays,
                currentMonth = currentMonth,
                currentDay = currentDay,
                appTheme = appTheme,
                appLanguage = appLanguage,
                onSendGreeting = onSendBirthdayGreeting
            )
        }

        // 2. PRÓXIMOS CUMPLEAÑOS - CARRUSEL DESLIZABLE
        if (upcomingBirthdays.isNotEmpty()) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🎂 ${strings.directorio.upcomingBirthdaysHeader}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${upcomingBirthdays.size} ${strings.directorio.nearBirthdaysCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = appTheme.primaryColor
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(upcomingBirthdays, key = { it.id }) { member ->
                            val daysLeft = member.daysUntilBirthday(currentMonth, currentDay)
                            UpcomingBirthdayChipCard(
                                member = member,
                                daysLeft = daysLeft,
                                appTheme = appTheme,
                                appLanguage = appLanguage,
                                onSendGreeting = { onSendBirthdayGreeting(member) }
                            )
                        }
                    }
                }
            }
        }

        // 3. BARRA DE BÚSQUEDA Y FILTRO POR PAÍS
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text(strings.directorio.searchPlaceholder) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = appTheme.primaryColor
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("directory_search_input"),
                shape = appTheme.cardShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = appTheme.primaryColor,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                singleLine = true
            )
        }

        // 4. FILTROS RÁPIDOS POR PAÍS
        item {
            val countries = listOf("Todos", "Colombia", "México", "Canadá", "USA", "Francia")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                countries.forEach { c ->
                    val isSelected = countryFilter.equals(c, ignoreCase = true)
                    val localizedCountry = strings.directorio.countryNames[c] ?: c
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCountryChange(c) },
                        label = {
                            val flag = when (c) {
                                "Colombia" -> "🇨🇴 "
                                "México" -> "🇲🇽 "
                                "Canadá" -> "🇨🇦 "
                                "USA" -> "🇺🇸 "
                                "Francia" -> "🇫🇷 "
                                else -> "🌍 "
                            }
                            Text(text = "$flag$localizedCountry", fontSize = 12.sp)
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = appTheme.primaryColor,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // 4.5. FILTRO POR CATEGORÍA VOCACIONAL (SACERDOTES, ESCOLÁSTICOS, NOVICIOS, ETC.)
        item {
            val categories = listOf(
                MemberCategory.ALL,
                MemberCategory.PRIEST,
                MemberCategory.SCHOLASTIC,
                MemberCategory.NOVICE,
                MemberCategory.BISHOP,
                MemberCategory.ASSOCIATE
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = memberCatFilter == cat
                    val localizedCat = when (cat) {
                        MemberCategory.ALL -> strings.directorio.filterAllCategories
                        MemberCategory.PRIEST -> strings.directorio.categoryPriest
                        MemberCategory.SCHOLASTIC -> strings.directorio.categoryScholastic
                        MemberCategory.NOVICE -> strings.directorio.categoryNovice
                        MemberCategory.BISHOP -> strings.directorio.categoryBishop
                        MemberCategory.ASSOCIATE -> strings.directorio.categoryBrother
                        else -> strings.directorio.categoryBrother
                    }
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategoryChange(cat) },
                        label = { Text(localizedCat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = appTheme.goldColor,
                            selectedLabelColor = Color.Black
                        )
                    )
                }
            }
        }

        // 5. FILTRO POR MES DE CUMPLEAÑOS
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.directorio.birthdaysByMonthTitle,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (monthFilter != null) {
                        TextButton(
                            onClick = { onMonthChange(null) },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(strings.directorio.viewAllMonthsBtn, fontSize = 11.sp, color = appTheme.primaryColor)
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    (1..12).forEach { mNum ->
                        val isSelected = monthFilter == mNum
                        val mName = strings.directorio.monthNames.getOrNull(mNum - 1)?.take(3) ?: "$mNum"
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                onMonthChange(if (isSelected) null else mNum)
                            },
                            label = { Text(mName, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = appTheme.goldColor,
                                selectedLabelColor = Color.Black
                            )
                        )
                    }
                }
            }
        }

        // 6. ENCABEZADO DE RESULTADOS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val registeredBrothersLabel = when (appLanguage) {
                    com.example.data.model.AppLanguage.EN -> "Registered Confreres (${members.size})"
                    com.example.data.model.AppLanguage.FR -> "Confrères Enregistrés (${members.size})"
                    else -> "Hermanos Registrados (${members.size})"
                }
                Text(
                    text = registeredBrothersLabel,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "[${appTheme.themeTag}]",
                    style = MaterialTheme.typography.bodySmall,
                    color = appTheme.primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 7. LISTA DE HERMANOS
        if (members.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = appTheme.cardShape,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val noResultsMsg = when (appLanguage) {
                            com.example.data.model.AppLanguage.EN -> "No brothers found matching this criteria."
                            com.example.data.model.AppLanguage.FR -> "Aucun confrère trouvé correspondant à ce critère."
                            else -> "No se encontraron hermanos con ese criterio."
                        }
                        Text(
                            text = noResultsMsg,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        } else {
            items(members, key = { it.id }) { member ->
                MemberCard(
                    member = member,
                    currentMonth = currentMonth,
                    currentDay = currentDay,
                    appTheme = appTheme,
                    appLanguage = appLanguage,
                    onSendGreeting = { onSendBirthdayGreeting(member) },
                    onCall = { phone ->
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.replace(" ", "")}"))
                        context.startActivity(intent)
                    },
                    onEmail = { email ->
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:$email")
                            val subj = when (appLanguage) {
                                com.example.data.model.AppLanguage.EN -> "Basilian Fraternal Greeting"
                                com.example.data.model.AppLanguage.FR -> "Salutation Fraternelle Basilienne"
                                else -> "Saludo Fraterno Basiliano"
                            }
                            putExtra(Intent.EXTRA_SUBJECT, subj)
                        }
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

// =============================================================================
// HERO BANNER DE CUMPLEAÑOS FUTURISTA 3D MULTICOLOR
// =============================================================================
@Composable
private fun BirthdayHeroBanner(
    todayBirthdays: List<BasilianDirectoryMember>,
    upcomingBirthdays: List<BasilianDirectoryMember>,
    currentMonth: Int,
    currentDay: Int,
    appTheme: BasilianAppTheme,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES,
    onSendGreeting: (BasilianDirectoryMember) -> Unit
) {
    val strings = remember(appLanguage) { com.example.ui.i18n.AppStrings.get(appLanguage) }
    val infiniteTransition = rememberInfiniteTransition(label = "banner_anim")
    val glowAnim by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    var partyBurstTrigger by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .futuristic3DCard(
                accentColor = Color(0xFFFFD700),
                secondaryColor = Color(0xFFEC4899),
                cornerRadius = 22.dp
            )
            .testTag("birthday_hero_banner"),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(
            2.dp,
            Brush.linearGradient(
                listOf(
                    Color(0xFFFFD700).copy(alpha = glowAnim),
                    Color(0xFFEC4899).copy(alpha = glowAnim),
                    Color(0xFF00F0FF).copy(alpha = glowAnim)
                )
            )
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF07142B),
                            Color(0xFF151E46),
                            Color(0xFF28103E)
                        )
                    )
                )
        ) {
            // Fondo animado de Globos / Bombas de colores, Confeti y Torta de Cumpleaños con Velas
            com.example.ui.components.BirthdayCelebrationFestiveCanvas(
                extraBurstTrigger = partyBurstTrigger,
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Etiqueta superior festiva de ¡FELIZ CUMPLEAÑOS! con Globos y Torta
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = { partyBurstTrigger += 1 },
                        color = Color(0xFFFFD700).copy(alpha = 0.24f),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.2.dp, Color(0xFFFFD700))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = when (appLanguage) {
                                    com.example.data.model.AppLanguage.EN -> "🎈 HAPPY BIRTHDAY! • BASILIAN CELEBRATION 🎂"
                                    com.example.data.model.AppLanguage.FR -> "🎈 JOYEUX ANNIVERSAIRE ! • FÊTE C.S.B. 🎂"
                                    else -> "🎈 ¡FELIZ CUMPLEAÑOS! • FIESTA FRATERNA C.S.B. 🎂"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFFDE68A),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.5.sp,
                                letterSpacing = 0.6.sp
                            )
                        }
                    }
                    val todayDateStr = when (appLanguage) {
                        com.example.data.model.AppLanguage.EN -> "Today: $currentMonth/$currentDay"
                        com.example.data.model.AppLanguage.FR -> "Aujourd'hui : $currentDay/$currentMonth"
                        else -> "Hoy: $currentDay/$currentMonth"
                    }
                    Surface(
                        onClick = { partyBurstTrigger += 1 },
                        color = Color(0xFFEC4899).copy(alpha = 0.22f),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color(0xFFEC4899))
                    ) {
                        Text(
                            text = "🎊 $todayDateStr",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                        )
                    }
                }

                if (todayBirthdays.isNotEmpty()) {
                    // ¡HAY CUMPLEAÑOS HOY!
                    Text(
                        text = "🎂 ${strings.directorio.todayBirthdaysHeader}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = strings.directorio.blessingsInMinistry,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE2E8F0)
                    )

                    todayBirthdays.forEach { celebrant ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.2.dp, Color(0xFFFFD700)),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1D36))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = celebrant.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    val ageStr = when (appLanguage) {
                                        com.example.data.model.AppLanguage.EN -> "Turns ${celebrant.calculateAge()} years old"
                                        com.example.data.model.AppLanguage.FR -> "Fête ses ${celebrant.calculateAge()} ans"
                                        else -> "Cumple ${celebrant.calculateAge()} años"
                                    }
                                    val celebrantHouse = BasilianDirectoryLocalization.getLocalizedHouseName(celebrant.currentHouse, appLanguage)
                                    Text(
                                        text = "$ageStr • $celebrantHouse",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFFFD700),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Button(
                                    onClick = { onSendGreeting(celebrant) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFFD700),
                                        contentColor = Color(0xFF040D1A)
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(strings.directorio.sendGreetingBtn, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                } else {
                    // NO HAY CUMPLEAÑOS HOY, MOSTRAR EL MÁS PRÓXIMO CON DISEÑO HOLOGRÁFICO
                    val nextOne = upcomingBirthdays.firstOrNull()
                    if (nextOne != null) {
                        val days = nextOne.daysUntilBirthday(currentMonth, currentDay)
                        Text(
                            text = "✨ ${strings.directorio.upcomingBirthdaysHeader.uppercase()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF00F0FF),
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Surface(
                            color = Color(0xFF081B33).copy(alpha = 0.92f),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(
                                1.4.dp,
                                Brush.horizontalGradient(listOf(Color(0xFF00F0FF), Color(0xFFFFD700)))
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = nextOne.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    val daysLeftStr = if (days == 1) strings.directorio.tomorrowBirthday else "$days ${strings.directorio.daysUntilBirthday}"
                                    val localizedCountry = strings.directorio.countryNames[nextOne.country] ?: nextOne.country
                                    Text(
                                        text = "🎂 ${nextOne.getFormattedBirthday(appLanguage)} • $daysLeftStr (${nextOne.calculateAge()} ${strings.directorio.ageLabel})",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFFFFD700),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp
                                    )
                                    val nextOneHouse = BasilianDirectoryLocalization.getLocalizedHouseName(nextOne.currentHouse, appLanguage)
                                    Text(
                                        text = "📍 $localizedCountry • $nextOneHouse",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF00F0FF),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Button(
                                    onClick = { onSendGreeting(nextOne) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFFD700),
                                        contentColor = Color(0xFF040D1A)
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(strings.directorio.sendGreetingBtn, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                                    }
                                }
                            }
                        }
                    }
                }

                // Cita Fraterna Rescatada con Colores en Cumpleaños
                val bdayQuote = when (appLanguage) {
                    com.example.data.model.AppLanguage.EN -> "«In our common life, the joy and birthday of one brother multiplies the thanksgiving of the entire Basilian family.»"
                    com.example.data.model.AppLanguage.FR -> "«Dans notre vie commune, la joie et l'anniversaire d'un frère multiplient l'action de grâce de toute la famille basilienne.»"
                    else -> "«En nuestra vida común, la alegría por la vida de un hermano multiplica la bendición y gratitud de toda la familia basiliana.»"
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF10B981).copy(alpha = 0.14f),
                    border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.7f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("❝", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF10B981))
                        Text(
                            text = bdayQuote,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFFF8FAFC),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// CHIP CARD 3D MULTICOLOR PARA PRÓXIMO CUMPLEAÑOS
// =============================================================================
@Composable
private fun UpcomingBirthdayChipCard(
    member: BasilianDirectoryMember,
    daysLeft: Int,
    appTheme: BasilianAppTheme,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES,
    onSendGreeting: () -> Unit
) {
    val strings = remember(appLanguage) { com.example.ui.i18n.AppStrings.get(appLanguage) }
    val chipAccent = remember(member.id) {
        FuturisticDesignPalette.accentForIndex(kotlin.math.abs(member.id.hashCode()))
    }
    val chipSecondary = remember(member.id) {
        FuturisticDesignPalette.secondaryForIndex(kotlin.math.abs(member.id.hashCode()))
    }

    Card(
        modifier = Modifier
            .width(205.dp)
            .futuristic3DCard(
                accentColor = chipAccent,
                secondaryColor = chipSecondary,
                cornerRadius = 18.dp,
                tiltDegrees = 1.6f
            )
            .clickable { onSendGreeting() },
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            1.4.dp,
            Brush.linearGradient(listOf(chipAccent, chipSecondary))
        ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF071529).copy(alpha = 0.96f),
                            Color(0xFF0F2542).copy(alpha = 0.94f),
                            chipAccent.copy(alpha = 0.14f)
                        )
                    )
                )
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (daysLeft <= 7) Color(0xFFEC4899) else chipAccent.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(0.8.dp, if (daysLeft <= 7) Color(0xFFFFD700) else chipAccent)
                ) {
                    val daysChipStr = when {
                        daysLeft == 0 -> when (appLanguage) {
                            com.example.data.model.AppLanguage.EN -> "🎉 TODAY!"
                            com.example.data.model.AppLanguage.FR -> "🎉 AUJOURD'HUI!"
                            else -> "🎉 ¡HOY!"
                        }
                        daysLeft == 1 -> "⚡ ${strings.directorio.tomorrowBirthday}"
                        appLanguage == com.example.data.model.AppLanguage.EN -> "⏳ In $daysLeft days"
                        appLanguage == com.example.data.model.AppLanguage.FR -> "⏳ Dans $daysLeft jours"
                        else -> "⏳ En $daysLeft días"
                    }
                    Text(
                        text = daysChipStr,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
                Surface(
                    color = Color(0xFFFFD700).copy(alpha = 0.18f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "🎂 ${member.birthDay}/${member.birthMonth}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFD700),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = member.name.substringBefore(","),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = member.name.substringAfter(", ").trim(),
                style = MaterialTheme.typography.bodySmall,
                color = chipAccent,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            val localizedCountry = strings.directorio.countryNames[member.country] ?: member.country
            Text(
                text = "$localizedCountry • ${member.calculateAge()} ${strings.directorio.ageLabel}",
                fontSize = 11.sp,
                color = Color(0xFFE2E8F0),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// =============================================================================
// TARJETA DETALLADA 3D FUTURISTA DE UN HERMANO BASILIANO
// =============================================================================
@Composable
private fun MemberCard(
    member: BasilianDirectoryMember,
    currentMonth: Int,
    currentDay: Int,
    appTheme: BasilianAppTheme,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES,
    onSendGreeting: () -> Unit,
    onCall: (String) -> Unit,
    onEmail: (String) -> Unit
) {
    val strings = remember(appLanguage) { com.example.ui.i18n.AppStrings.get(appLanguage) }
    val daysUntilBday = member.daysUntilBirthday(currentMonth, currentDay)
    val isToday = member.isBirthdayToday(currentMonth, currentDay)

    val (cardAccent, cardSecondary) = remember(member.category, isToday) {
        if (isToday) {
            Color(0xFFFFD700) to Color(0xFFEC4899)
        } else {
            when (member.category) {
                MemberCategory.BISHOP -> Color(0xFFFFD700) to Color(0xFFA855F7)
                MemberCategory.PRIEST -> Color(0xFF00F0FF) to Color(0xFF10B981)
                MemberCategory.SCHOLASTIC -> Color(0xFFA855F7) to Color(0xFF00F0FF)
                MemberCategory.NOVICE -> Color(0xFF10B981) to Color(0xFFFFD700)
                MemberCategory.ASSOCIATE -> Color(0xFFFF6B8B) to Color(0xFFF59E0B)
                else -> Color(0xFF00F0FF) to Color(0xFFFFD700)
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .futuristic3DCard(
                accentColor = cardAccent,
                secondaryColor = cardSecondary,
                cornerRadius = 20.dp,
                tiltDegrees = if (isToday) 2.2f else 1.3f
            )
            .testTag("member_card_${member.id}"),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            if (isToday) 2.2.dp else 1.4.dp,
            Brush.linearGradient(listOf(cardAccent, cardSecondary))
        ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(if (isToday) 8.dp else 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF061325).copy(alpha = 0.96f),
                            Color(0xFF0D213D).copy(alpha = 0.94f),
                            cardAccent.copy(alpha = if (isToday) 0.22f else 0.10f)
                        )
                    )
                )
        ) {
            // Barra lateral luminosa 3D según vocación/categoría
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .heightIn(min = 145.dp)
                    .background(Brush.verticalGradient(listOf(cardAccent, cardSecondary, cardAccent)))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(15.dp)
            ) {
                // Fila de encabezado: Nombre en blanco brillante + Badge de País/Categoría en color
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                        Text(
                            text = member.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 16.5.sp
                        )
                        if (member.rolesOrMinistries.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "✦ ${BasilianDirectoryLocalization.getLocalizedMemberRole(member.rolesOrMinistries, appLanguage)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    val localizedCountry = strings.directorio.countryNames[member.country] ?: member.country
                    val localizedCatLabel = when (member.category) {
                        MemberCategory.PRIEST -> strings.directorio.categoryPriest
                        MemberCategory.BISHOP -> strings.directorio.categoryBishop
                        MemberCategory.SCHOLASTIC -> strings.directorio.categoryScholastic
                        MemberCategory.NOVICE -> strings.directorio.categoryNovice
                        MemberCategory.ASSOCIATE -> strings.directorio.categoryBrother
                        else -> member.category.label
                    }

                    Surface(
                        color = cardAccent.copy(alpha = 0.20f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, cardAccent.copy(alpha = 0.8f))
                    ) {
                        Text(
                            text = "$localizedCountry • $localizedCatLabel",
                            color = cardAccent,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Cuadro interno de Cumpleaños y Cuenta Regresiva con alto contraste
                Surface(
                    color = Color(0xFF091B34),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(0.8.dp, cardAccent.copy(alpha = 0.45f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Cake,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFFFFD700)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${member.getFormattedBirthday(appLanguage)} (${member.calculateAge()} ${strings.directorio.ageLabel})",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }

                        val daysText = when {
                            isToday -> if (appLanguage == com.example.data.model.AppLanguage.EN) "🎉 TODAY!" else if (appLanguage == com.example.data.model.AppLanguage.FR) "🎉 AUJOURD'HUI!" else "🎉 ¡HOY!"
                            daysUntilBday == 1 -> strings.directorio.tomorrowBirthday
                            appLanguage == com.example.data.model.AppLanguage.EN -> "$daysUntilBday days left"
                            appLanguage == com.example.data.model.AppLanguage.FR -> "Dans $daysUntilBday jours"
                            else -> "Faltan $daysUntilBday días"
                        }
                        Surface(
                            color = if (isToday) Color(0xFFFFD700) else cardSecondary.copy(alpha = 0.22f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = daysText,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isToday) Color(0xFF040D1A) else cardSecondary,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Fechas de Profesión y Ordenación con etiquetas de colores
                val vowsPrefix = when (appLanguage) {
                    com.example.data.model.AppLanguage.EN -> "✝️ Vows: "
                    com.example.data.model.AppLanguage.FR -> "✝️ Vœux: "
                    else -> "✝️ Votos: "
                }
                val ordinationPrefix = when (appLanguage) {
                    com.example.data.model.AppLanguage.EN -> "🕊️ Ordination: "
                    com.example.data.model.AppLanguage.FR -> "🕊️ Ordination: "
                    else -> "🕊️ Ordenación: "
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (member.professionDate.isNotBlank()) {
                        Text(
                            text = "$vowsPrefix${BasilianDirectoryLocalization.getLocalizedProfessionDate(member.professionDate, appLanguage)}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF10B981)
                        )
                    }
                    if (member.ordinationDate.isNotBlank()) {
                        Text(
                            text = "$ordinationPrefix${BasilianDirectoryLocalization.getLocalizedOrdinationDate(member.ordinationDate, appLanguage)}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF38BDF8)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Casa actual y dirección
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    val localizedHouse = BasilianDirectoryLocalization.getLocalizedHouseName(member.currentHouse, appLanguage)
                    Text(
                        text = "$localizedHouse • ${member.address}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE2E8F0),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botones de Contacto y Felicitación Rápida con estilo Neón 3D
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val sendGreetingLabel = if (isToday) {
                        when (appLanguage) {
                            com.example.data.model.AppLanguage.EN -> "Greet Today"
                            com.example.data.model.AppLanguage.FR -> "Fêter Aujourd'hui"
                            else -> "Felicitar Hoy"
                        }
                    } else {
                        strings.directorio.sendGreetingBtn
                    }
                    Button(
                        onClick = onSendGreeting,
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isToday) Color(0xFFFFD700) else cardAccent,
                            contentColor = Color(0xFF040D1A)
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 7.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Celebration,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = sendGreetingLabel,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    val phone = member.cellPhone.ifBlank { member.officePhone }
                    if (phone.isNotBlank()) {
                        OutlinedButton(
                            onClick = { onCall(phone) },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.2.dp, Color(0xFF10B981)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF10B981)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 7.dp)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = strings.directorio.callBtn, modifier = Modifier.size(15.dp))
                        }
                    }

                    if (member.email.isNotBlank()) {
                        OutlinedButton(
                            onClick = { onEmail(member.email) },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.2.dp, Color(0xFFFFD700)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFFD700)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 7.dp)
                        ) {
                            Icon(Icons.Default.Email, contentDescription = strings.directorio.emailBtn, modifier = Modifier.size(15.dp))
                        }
                    }

                    val localContext = LocalContext.current
                    OutlinedButton(
                        onClick = {
                            val cardText = MisaDelDiaDevocionarioRepository.buildBasilianBirthdayCardText(member)
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Felicitación Fraterna C.S.B. - ${member.name}")
                                putExtra(Intent.EXTRA_TEXT, cardText)
                            }
                            localContext.startActivity(Intent.createChooser(shareIntent, "Compartir Tarjeta C.S.B."))
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.2.dp, Color(0xFF00F0FF)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00F0FF)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 7.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir Tarjeta C.S.B.", modifier = Modifier.size(15.dp))
                    }
                }
            }
        }
    }
}

// =============================================================================
// SUB-COMPONENTE: PESTAÑA 2 - HERMANOS DIFUNTOS EN PASCUA
// =============================================================================
@Composable
fun HermanosDifuntosContent(
    deceasedList: List<DeceasedBrother>,
    selectedCat: DeceasedCategory,
    searchQuery: String,
    litCandleIds: Set<String>,
    currentMonth: Int = 11,
    currentDay: Int = 1,
    appTheme: BasilianAppTheme,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES,
    onCatChange: (DeceasedCategory) -> Unit,
    onSearchChange: (String) -> Unit,
    onToggleCandle: (String) -> Unit,
    onPrayResponsorio: () -> Unit,
    onSpeakCustomPrayer: (String) -> Unit = {}
) {
    val strings = remember(appLanguage) { com.example.ui.i18n.AppStrings.get(appLanguage) }
    var selectedNecroMonth by remember { mutableStateOf(currentMonth) }
    var selectedNecroDay by remember { mutableStateOf(currentDay) }
    var filterListBySelectedMonth by remember { mutableStateOf<Int?>(null) }

    val brothersOfSelectedDay = remember(selectedNecroMonth, selectedNecroDay) {
        BasilianDirectoryRepository.getDeceasedByDate(selectedNecroMonth, selectedNecroDay)
    }

    val daysWithBrothersInMonth = remember(selectedNecroMonth) {
        BasilianDirectoryRepository.getDeceasedByMonth(selectedNecroMonth)
            .map { it.dayPassed }
            .filter { it > 0 }
            .distinct()
            .sorted()
    }

    val displayedDeceasedList = remember(deceasedList, filterListBySelectedMonth) {
        if (filterListBySelectedMonth == null) {
            deceasedList
        } else {
            deceasedList.filter { it.monthPassed == filterListBySelectedMonth }
                .sortedWith(compareBy({ it.dayPassed }, { it.name }))
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. HERO MEMORIAL - VELADORA VIRTUAL Y ORACIÓN
        item {
            MemorialHeroCard(
                appTheme = appTheme,
                appLanguage = appLanguage,
                onPrayResponsorio = onPrayResponsorio
            )
        }

        // 2. NECROLOGÍA OFICIAL DEL DÍA (CON TODOS LOS HERMANOS DEL DÍA Y ENCENDIDO DE VELITAS)
        item {
            NecrologiaDelDiaInteractiveCard(
                selectedMonth = selectedNecroMonth,
                selectedDay = selectedNecroDay,
                daysWithBrothersInMonth = daysWithBrothersInMonth,
                brothersOfDay = brothersOfSelectedDay,
                litCandleIds = litCandleIds,
                appTheme = appTheme,
                appLanguage = appLanguage,
                onSelectMonth = { newMonth ->
                    selectedNecroMonth = newMonth
                    val availableDays = BasilianDirectoryRepository.getDeceasedByMonth(newMonth)
                        .map { it.dayPassed }
                        .filter { it > 0 }
                        .distinct()
                        .sorted()
                    if (selectedNecroDay !in availableDays && availableDays.isNotEmpty()) {
                        selectedNecroDay = availableDays.first()
                    }
                },
                onSelectDay = { newDay ->
                    selectedNecroDay = newDay
                },
                onResetToToday = {
                    selectedNecroMonth = currentMonth
                    selectedNecroDay = currentDay
                },
                onToggleCandle = onToggleCandle,
                onLightAllCandlesForDay = {
                    brothersOfSelectedDay.forEach { bro ->
                        if (!litCandleIds.contains(bro.id)) {
                            onToggleCandle(bro.id)
                        }
                    }
                },
                onPrayForDayBrothers = {
                    if (brothersOfSelectedDay.isNotEmpty()) {
                        val namesJoined = brothersOfSelectedDay.joinToString(", ") { it.name.removeSuffix(", CSB") }
                        val monthName = com.example.data.repository.BasilianNecrologyCatalog.monthNameDisplay(selectedNecroMonth, appLanguage)
                        val prayer = "Laudemus viros gloriosos. Hagamos el elogio de los hombres ilustres. " +
                            "En este día $selectedNecroDay de $monthName, encendemos nuestra luz y elevamos nuestra oración por nuestros hermanos basilianos: " +
                            "$namesJoined. Concédeles, Señor, el descanso eterno, y brille para ellos la luz perpetua. Amén."
                        onSpeakCustomPrayer(prayer)
                    } else {
                        onPrayResponsorio()
                    }
                }
            )
        }

        // 3. BUSCADOR DE HERMANOS DIFUNTOS (NOMBRE, FECHA, AÑO O PALABRA CLAVE)
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text(strings.directorio.searchDeceasedPlaceholder) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = appTheme.primaryColor
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("deceased_search_input"),
                shape = appTheme.cardShape,
                singleLine = true
            )
        }

        // 4. CHIPS DE CATEGORÍAS Y FILTRO POR MES DE LA NECROLOGÍA (ENERO - DICIEMBRE)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DeceasedCategory.values().forEach { cat ->
                        val isSelected = selectedCat == cat
                        val localizedCat = BasilianDirectoryLocalization.getLocalizedCategoryLabel(cat, appLanguage)
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCatChange(cat) },
                            label = { Text(localizedCat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = appTheme.primaryColor,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Selector horizontal para filtrar el listado completo por Mes de la Necrología
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = filterListBySelectedMonth == null,
                        onClick = { filterListBySelectedMonth = null },
                        label = {
                            Text(
                                text = "📅 Todos los Meses (${deceasedList.size})",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFD97706),
                            selectedLabelColor = Color.White
                        )
                    )
                    (1..12).forEach { m ->
                        val mLabel = com.example.data.repository.BasilianNecrologyCatalog.monthNameDisplay(m, appLanguage)
                        val isSelectedMonth = filterListBySelectedMonth == m
                        FilterChip(
                            selected = isSelectedMonth,
                            onClick = {
                                filterListBySelectedMonth = if (isSelectedMonth) null else m
                                selectedNecroMonth = m
                            },
                            label = { Text(mLabel, fontSize = 11.5.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFD97706),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // 5. LISTA COMPLETA DE HERMANOS DIFUNTOS (CON VELADORA VIRTUAL Y PEDAGOGÍA)
        items(displayedDeceasedList, key = { it.id }) { brother ->
            val isCandleLit = litCandleIds.contains(brother.id)

            DeceasedBrotherCard(
                brother = brother,
                isCandleLit = isCandleLit,
                appTheme = appTheme,
                appLanguage = appLanguage,
                onToggleCandle = { onToggleCandle(brother.id) }
            )
        }
    }
}

// =============================================================================
// TARJETA INTERACTIVA DE LA NECROLOGÍA DEL DÍA (AGRUPA VARIOS HERMANOS EN EL DÍA)
// =============================================================================
@Composable
private fun NecrologiaDelDiaInteractiveCard(
    selectedMonth: Int,
    selectedDay: Int,
    daysWithBrothersInMonth: List<Int>,
    brothersOfDay: List<DeceasedBrother>,
    litCandleIds: Set<String>,
    appTheme: BasilianAppTheme,
    appLanguage: com.example.data.model.AppLanguage,
    onSelectMonth: (Int) -> Unit,
    onSelectDay: (Int) -> Unit,
    onResetToToday: () -> Unit,
    onToggleCandle: (String) -> Unit,
    onLightAllCandlesForDay: () -> Unit,
    onPrayForDayBrothers: () -> Unit
) {
    val monthDisplay = com.example.data.repository.BasilianNecrologyCatalog.monthNameDisplay(selectedMonth, appLanguage)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("necrologia_del_dia_card"),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.8.dp,
            Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFF00F0FF)))
        ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF09162B).copy(alpha = 0.96f)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🕯️ NECROLOGÍA BASILIANA DEL DÍA",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFD700)
                    )
                    Text(
                        text = "«Laudemus viros gloriosos — Hagamos el elogio de los hombres ilustres»",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF00F0FF)
                    )
                }
                TextButton(onClick = onResetToToday) {
                    Text(
                        text = "Hoy",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFD700)
                    )
                }
            }

            // Selector rápido de Mes (1..12)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                (1..12).forEach { m ->
                    val label = com.example.data.repository.BasilianNecrologyCatalog.monthNameDisplay(m, appLanguage).take(3)
                    val isSelected = m == selectedMonth
                    Surface(
                        onClick = { onSelectMonth(m) },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Color(0xFFFFD700) else Color(0xFF15294B),
                        border = BorderStroke(1.dp, if (isSelected) Color.White else Color(0xFF334155))
                    ) {
                        Text(
                            text = label,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF071326) else Color.White
                        )
                    }
                }
            }

            // Selector rápido de Día con difuntos en ese mes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Día de $monthDisplay:",
                    fontSize = 11.5.sp,
                    color = Color(0xFFCBD5E1),
                    fontWeight = FontWeight.SemiBold
                )
                daysWithBrothersInMonth.forEach { d ->
                    val countOnDay = BasilianDirectoryRepository.getDeceasedByDate(selectedMonth, d).size
                    val isSelected = d == selectedDay
                    Surface(
                        onClick = { onSelectDay(d) },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Color(0xFF00F0FF) else Color(0xFF15294B),
                        border = BorderStroke(1.dp, if (isSelected) Color.White else Color(0xFF334155))
                    ) {
                        Text(
                            text = if (countOnDay > 1) "$d ($countOnDay)" else "$d",
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isSelected) Color(0xFF071326) else Color.White
                        )
                    }
                }
            }

            // Cabecera de los hermanos recordados en esta fecha
            Text(
                text = if (brothersOfDay.size > 1) {
                    "📅 $selectedDay de $monthDisplay — Recordamos en comunión a ${brothersOfDay.size} hermanos basilianos:"
                } else if (brothersOfDay.size == 1) {
                    "📅 $selectedDay de $monthDisplay — Recordamos en comunión a nuestro hermano basiliano:"
                } else {
                    "📅 $selectedDay de $monthDisplay — En oración por todos los difuntos de la Congregación de San Basilio"
                },
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            if (brothersOfDay.isEmpty()) {
                Text(
                    text = "En esta fecha particular no hay fallecimientos registrados en el listado diario; selecciona otro día resaltado arriba o enciende tu velita por todos los hermanos en Pascua.",
                    fontSize = 11.5.sp,
                    color = Color(0xFFCBD5E1)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    brothersOfDay.forEach { bro ->
                        val isLit = litCandleIds.contains(bro.id)
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isLit) Color(0xFF1E293B).copy(alpha = 0.95f) else Color(0xFF0F1E36),
                            border = BorderStroke(
                                1.dp,
                                if (isLit) Color(0xFFFFD700) else Color(0xFF334155)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                    Text(
                                        text = bro.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isLit) Color(0xFFFFD700) else Color.White
                                    )
                                    Text(
                                        text = "✝️ Años: ${bro.birthAndDeathYears}${bro.age?.let { " ($it años)" } ?: ""} • ${bro.titleOrRole}",
                                        fontSize = 11.sp,
                                        color = Color(0xFFCBD5E1)
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    VirtualCandleMini(
                                        isLit = isLit,
                                        onToggle = { onToggleCandle(bro.id) },
                                        appTheme = appTheme,
                                        size = 36.dp
                                    )
                                    TextButton(
                                        onClick = { onToggleCandle(bro.id) },
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (isLit) "🕯️ Encendida" else "Encender",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isLit) Color(0xFFFFD700) else Color(0xFF00F0FF)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onLightAllCandlesForDay,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD97706),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (brothersOfDay.size > 1) "🕯️ Encender las ${brothersOfDay.size} Velitas" else "🕯️ Encender Velita",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    OutlinedButton(
                        onClick = onPrayForDayBrothers,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.2.dp, Color(0xFF00F0FF)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00F0FF)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Orar por Ellos",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// HERO CARD MEMORIAL Y VELADORA VIRTUAL INTERACTIVA
// =============================================================================
@Composable
private fun MemorialHeroCard(
    appTheme: BasilianAppTheme,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES,
    onPrayResponsorio: () -> Unit
) {
    val strings = remember(appLanguage) { com.example.ui.i18n.AppStrings.get(appLanguage) }
    var isHeroCandleLit by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("memorial_hero_card"),
        shape = appTheme.cardShape,
        border = BorderStroke(appTheme.cardBorderWidth, appTheme.goldColor.copy(alpha = 0.6f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Componente de Cirio Virtual Interactivo con simulación de llama parpadeante animada
            VirtualCandleComponent(
                isLit = isHeroCandleLit,
                onToggle = {
                    isHeroCandleLit = !isHeroCandleLit
                    val toastMsg = if (isHeroCandleLit) {
                        strings.directorio.candleLitToast
                    } else {
                        when (appLanguage) {
                            com.example.data.model.AppLanguage.EN -> "Candle extinguished"
                            com.example.data.model.AppLanguage.FR -> "Cierge éteint"
                            else -> "Cirio apagado"
                        }
                    }
                    Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                },
                candleHeight = 120.dp,
                candleWidth = 92.dp,
                appTheme = appTheme,
                appLanguage = appLanguage,
                showInteractiveLabel = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = strings.directorio.deceasedHeader,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = appTheme.primaryColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = strings.directorio.deceasedSubtitle,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onPrayResponsorio,
                colors = ButtonDefaults.buttonColors(
                    containerColor = appTheme.primaryColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(strings.directorio.prayResponsorialBtn, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// =============================================================================
// TARJETA DE UN HERMANO DIFUNTO (COMPLETAMENTE TRADUCIDA CON VELADORA MINI)
// =============================================================================
@Composable
private fun DeceasedBrotherCard(
    brother: DeceasedBrother,
    isCandleLit: Boolean,
    appTheme: BasilianAppTheme,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES,
    onToggleCandle: () -> Unit
) {
    val strings = remember(appLanguage) { com.example.ui.i18n.AppStrings.get(appLanguage) }

    val localizedName = remember(brother.name, appLanguage) {
        BasilianDirectoryLocalization.getLocalizedName(brother, appLanguage)
    }
    val localizedTitle = remember(brother.titleOrRole, appLanguage) {
        BasilianDirectoryLocalization.getLocalizedTitle(brother, appLanguage)
    }
    val localizedYears = remember(brother.birthAndDeathYears, appLanguage) {
        BasilianDirectoryLocalization.getLocalizedYears(brother, appLanguage)
    }
    val localizedDate = remember(brother.datePassed, appLanguage) {
        BasilianDirectoryLocalization.getLocalizedDatePassed(brother, appLanguage)
    }
    val localizedQuote = remember(brother.memorialQuote, appLanguage) {
        BasilianDirectoryLocalization.getLocalizedQuote(brother, appLanguage)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .futuristic3DCard(
                accentColor = if (isCandleLit) Color(0xFFFFD700) else Color(0xFFA855F7),
                secondaryColor = Color(0xFF00F0FF),
                cornerRadius = 20.dp,
                tiltDegrees = 1.4f
            )
            .testTag("deceased_card_${brother.id}"),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            if (isCandleLit) 2.dp else 1.3.dp,
            Brush.linearGradient(
                listOf(
                    if (isCandleLit) Color(0xFFFFD700) else Color(0xFFA855F7),
                    Color(0xFF00F0FF)
                )
            )
        ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF071326).copy(alpha = 0.96f),
                            Color(0xFF102240).copy(alpha = 0.94f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text(
                        text = localizedName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = localizedTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Bold
                    )
                }

                // Cirio Virtual Mini interactivo con animación de llama
                VirtualCandleMini(
                    isLit = isCandleLit,
                    onToggle = onToggleCandle,
                    appTheme = appTheme,
                    size = 42.dp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            val yearsLabel = when (appLanguage) {
                com.example.data.model.AppLanguage.EN -> "Years: "
                com.example.data.model.AppLanguage.FR -> "Années: "
                else -> "Años: "
            }
            val easterDateLabel = when (appLanguage) {
                com.example.data.model.AppLanguage.EN -> "Easter: "
                com.example.data.model.AppLanguage.FR -> "Pâques: "
                else -> "Pascua: "
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$yearsLabel$localizedYears${brother.age?.let { " ($it años)" } ?: ""}",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFCBD5E1)
                )
                Text(
                    text = "✝️ $easterDateLabel$localizedDate",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF00F0FF)
                )
            }

            if (brother.monthPassed > 0 && brother.dayPassed > 0) {
                val companionsOfSameDay = remember(brother.id, brother.monthPassed, brother.dayPassed) {
                    BasilianDirectoryRepository.getDeceasedByDate(brother.monthPassed, brother.dayPassed)
                        .filter { it.id != brother.id }
                }
                if (companionsOfSameDay.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    val mName = com.example.data.repository.BasilianNecrologyCatalog.monthNameDisplay(brother.monthPassed, appLanguage)
                    Text(
                        text = "🤝 En este día (${brother.dayPassed} de $mName) también recordamos a: ${companionsOfSameDay.joinToString(", ") { it.name.removeSuffix(", CSB") }}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFDE68A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            FuturisticQuoteCalloutCard(
                quoteText = localizedQuote,
                authorOrSource = localizedName,
                categoryTag = "MEMORIA PASCUAL C.S.B.",
                accentColor = if (isCandleLit) Color(0xFFFFD700) else Color(0xFFA855F7),
                secondaryColor = Color(0xFF00F0FF)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val candleStatusText = if (isCandleLit) strings.directorio.candleLit else strings.directorio.lightCandle
                Text(
                    text = if (isCandleLit) "🕯️ $candleStatusText" else candleStatusText,
                    fontSize = 11.5.sp,
                    color = if (isCandleLit) Color(0xFFFFD700) else Color(0xFF94A3B8),
                    fontWeight = FontWeight.ExtraBold
                )
                val toggleButtonLabel = if (isCandleLit) {
                    when (appLanguage) {
                        com.example.data.model.AppLanguage.EN -> "Extinguish"
                        com.example.data.model.AppLanguage.FR -> "Éteindre"
                        else -> "Apagar"
                    }
                } else {
                    strings.directorio.lightCandle
                }
                TextButton(
                    onClick = onToggleCandle,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = toggleButtonLabel,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF00F0FF)
                    )
                }
            }
        }
    }
}

// =============================================================================
// HELPER PARA ENVIAR FELICITACIÓN DE CUMPLEAÑOS
// =============================================================================
fun sendBirthdayGreeting(
    context: Context,
    member: BasilianDirectoryMember,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES
) {
    val greetingMessage = when (appLanguage) {
        com.example.data.model.AppLanguage.EN -> {
            "Dear brother ${member.name}!\n\n" +
            "On this blessed day of your birthday (${member.getFormattedBirthday(appLanguage)}), your Basilian brothers join together in deep thanksgiving to God for the gift of your life, your vocation, and your generous witness in the Congregation of St. Basil.\n\n" +
            "We ask the Lord Jesus, through the intercession of the Virgin Mary and St. Basil the Great, to pour out abundant blessings upon your ministry and community.\n\n" +
            "A warm and fraternal embrace in Christ!\n«Bonitatem et disciplinam et scientiam doce me»"
        }
        com.example.data.model.AppLanguage.FR -> {
            "Cher frère ${member.name} !\n\n" +
            "En ce jour béni de ton anniversaire (${member.getFormattedBirthday(appLanguage)}), tes confrères Basiliens s'unissent dans une profonde action de grâce à Dieu pour le don de ta vie, ta vocation et ton témoignage généreux dans la Congrégation de Saint-Basile.\n\n" +
            "Nous demandons au Seigneur Jésus, par l'intercession de la Vierge Marie et de Saint Basile le Grand, de répandre d'abondantes bénédictions sur ton ministère et ta communauté.\n\n" +
            "Une fraternelle et chaleureuse accolade dans le Christ !\n«Bonitatem et disciplinam et scientiam doce me»"
        }
        else -> {
            "¡Querido hermano ${member.name}!\n\n" +
            "En este día tan bendecido de tu cumpleaños (${member.getFormattedBirthday(appLanguage)}), tus hermanos Basilianos nos unimos en profunda acción de gracias a Dios por el don de tu vida, tu vocación y tu testimonio generoso en la Congregación de San Basilio.\n\n" +
            "Pedimos al Señor Jesús, por intercesión de la Virgen María y de San Basilio Magno, que derrame abundantes bendiciones sobre tu ministerio y comunidad.\n\n" +
            "¡Un fuerte y fraterno abrazo en Cristo!\n«Bonitatem et disciplinam et scientiam doce me»"
        }
    }

    val emailSubject = when (appLanguage) {
        com.example.data.model.AppLanguage.EN -> "Happy Birthday, dear brother ${member.name}! - Basilian Community"
        com.example.data.model.AppLanguage.FR -> "Joyeux Anniversaire, cher frère ${member.name} ! - Communauté Basilienne"
        else -> "¡Feliz Cumpleaños, querido hermano ${member.name}! - Comunidad Basiliana"
    }

    val chooserTitle = when (appLanguage) {
        com.example.data.model.AppLanguage.EN -> "Send birthday greeting to ${member.name}"
        com.example.data.model.AppLanguage.FR -> "Envoyer les vœux à ${member.name}"
        else -> "Enviar felicitación a ${member.name}"
    }

    // Si tiene WhatsApp o celular
    val phone = member.cellPhone.ifBlank { member.officePhone }
    val cleanPhone = phone.replace(Regex("[^0-9]"), "")

    if (cleanPhone.isNotBlank()) {
        try {
            val waIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(greetingMessage)}")
            }
            context.startActivity(waIntent)
            return
        } catch (_: Exception) {
            // Continuar con correo o selector
        }
    }

    // Si tiene correo
    if (member.email.isNotBlank()) {
        try {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${member.email}")
                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                putExtra(Intent.EXTRA_TEXT, greetingMessage)
            }
            context.startActivity(emailIntent)
            return
        } catch (_: Exception) {
            // Continuar con selector genérico
        }
    }

    // Selector genérico de compartir
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, emailSubject)
        putExtra(Intent.EXTRA_TEXT, greetingMessage)
    }
    context.startActivity(Intent.createChooser(shareIntent, chooserTitle))
}

// =============================================================================
// SECCIÓN: DIRECTORIO OFICIAL 2026-2027 (CURIA, CONSEJO GENERAL & CASAS)
// =============================================================================
@Composable
fun DirectorioCuriaYCasasContent(
    appTheme: BasilianAppTheme,
    appLanguage: com.example.data.model.AppLanguage = com.example.data.model.AppLanguage.ES,
    onContactClick: (email: String, phone: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = remember(appLanguage) { com.example.ui.i18n.AppStrings.get(appLanguage) }
    var sectionFilter by remember { mutableIntStateOf(0) } // 0: Consejo General, 1: Casas & Misiones

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Cabecera Oficial del Directorio Septiembre 2026
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, appTheme.goldColor.copy(alpha = 0.7f)),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = appTheme.primaryColor.copy(alpha = 0.15f),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.AccountBalance,
                                    contentDescription = null,
                                    tint = appTheme.primaryColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = strings.directorio.officialHeaderTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = strings.directorio.officialHeaderSubtitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = appTheme.goldColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Text(
                        text = strings.directorio.curiaCouncilSubtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray,
                        lineHeight = 18.sp
                    )

                    // Filtro de subsección
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = sectionFilter == 0,
                            onClick = { sectionFilter = 0 },
                            leadingIcon = { Icon(Icons.Default.Groups, null, modifier = Modifier.size(16.dp)) },
                            label = { Text("${strings.directorio.generalCouncilTitle} (${BasilianDirectoryRepository.generalCouncil2026.size})", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = sectionFilter == 1,
                            onClick = { sectionFilter = 1 },
                            leadingIcon = { Icon(Icons.Default.Home, null, modifier = Modifier.size(16.dp)) },
                            label = { Text("${strings.directorio.housesTitle} (${BasilianDirectoryRepository.basilianHouses2026.size})", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        if (sectionFilter == 0) {
            item {
                Text(
                    text = strings.directorio.generalCouncilTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = appTheme.primaryColor
                )
            }

            items(BasilianDirectoryRepository.generalCouncil2026) { member ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = appTheme.primaryColor.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = BasilianDirectoryLocalization.getLocalizedCouncilRole(member, appLanguage),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = appTheme.primaryColor,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = member.memberName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = BasilianDirectoryLocalization.getLocalizedCouncilLocation(member, appLanguage),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = appTheme.goldColor.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = BasilianDirectoryLocalization.getLocalizedCouncilTerm(member, appLanguage),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = appTheme.goldColor,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        if (member.description.isNotBlank()) {
                            Text(
                                text = BasilianDirectoryLocalization.getLocalizedCouncilDescription(member, appLanguage),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.DarkGray
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (member.email.isNotBlank()) {
                                OutlinedButton(
                                    onClick = { onContactClick(member.email, "") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.Email, null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(strings.directorio.emailBtn, fontSize = 11.sp)
                                }
                            }
                            if (member.phone.isNotBlank()) {
                                Button(
                                    onClick = { onContactClick("", member.phone) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(vertical = 4.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = appTheme.primaryColor)
                                ) {
                                    Icon(Icons.Default.Phone, null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(strings.directorio.callBtn, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            item {
                Text(
                    text = strings.directorio.housesTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = appTheme.primaryColor
                )
            }

            items(BasilianDirectoryRepository.basilianHouses2026) { house ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = BasilianDirectoryLocalization.getLocalizedHouseName(house.name, appLanguage),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                val localizedCountry = strings.directorio.countryNames[house.country] ?: house.country
                                Text(
                                    text = "📍 ${house.city}, $localizedCountry",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = appTheme.primaryColor,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = appTheme.goldColor.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = BasilianDirectoryLocalization.getLocalizedHouseCategory(house.category, appLanguage),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = appTheme.goldColor,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Text(
                            text = house.address,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )

                        if (house.superiorOrRector.isNotBlank()) {
                            val localizedSuperior = BasilianDirectoryLocalization.getLocalizedSuperior(house.superiorOrRector, appLanguage)
                            Text(
                                text = "${strings.directorio.localSuperior}: $localizedSuperior",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                fontStyle = FontStyle.Italic
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (house.email.isNotBlank()) {
                                OutlinedButton(
                                    onClick = { onContactClick(house.email, "") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.Email, null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(strings.directorio.emailBtn, fontSize = 11.sp)
                                }
                            }
                            if (house.phone.isNotBlank()) {
                                Button(
                                    onClick = { onContactClick("", house.phone) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(vertical = 4.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = appTheme.primaryColor)
                                ) {
                                    Icon(Icons.Default.Phone, null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(strings.directorio.callBtn, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LiturgiaOrdoColombianoNonBasilianPortal(
    selectedSubTab: Int,
    viewModel: BasilianViewModel,
    appLanguage: com.example.data.model.AppLanguage,
    userEmail: String,
    permissionRequestedBanner: Boolean,
    onRequestPermission: () -> Unit,
    unlockCodeInput: String,
    onUnlockCodeChange: (String) -> Unit,
    onValidateUnlockCode: () -> Unit
) {
    val context = LocalContext.current
    val isEnglish = appLanguage == com.example.data.model.AppLanguage.EN
    val isFrench = appLanguage == com.example.data.model.AppLanguage.FR
    val allCelebrations = remember { LiturgiaOrdoColombianoRepository.getAllFeaturedColombianCelebrations() }
    var selectedCelebrationIndex by remember { mutableIntStateOf(0) }
    var selectedHourType by remember { mutableStateOf(LiturgicalHourType.LAUDES) }
    var showBasilianVerifyPanel by remember { mutableStateOf(false) }

    val currentDay: ColombianLiturgicalDay = allCelebrations.getOrElse(selectedCelebrationIndex) {
        LiturgiaOrdoColombianoRepository.getTodayColombianDay()
    }
    val bilingualHour = currentDay.hours[selectedHourType] ?: currentDay.hours[LiturgicalHourType.LAUDES]
    val hourContent = if (isEnglish) bilingualHour?.en else bilingualHour?.es

    val openUrl: (String) -> Unit = { url ->
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No se pudo abrir el enlace: $url", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ordo_colombiano_non_basilian_portal"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (selectedSubTab == 0) {
            // Cabecera del Ordo Litúrgico de Colombia (CEC)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0A192F).copy(alpha = 0.95f)),
                    border = BorderStroke(1.5.dp, Color(currentDay.color.hexCode).copy(alpha = 0.85f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color(0xFFF59E0B).copy(alpha = 0.18f),
                                border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.6f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "🇨🇴 ORDO COLOMBIANO (CEC)",
                                        color = Color(0xFFFDE68A),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color(currentDay.color.hexCode).copy(alpha = 0.22f),
                                border = BorderStroke(1.dp, Color(currentDay.color.hexCode))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(currentDay.color.hexCode))
                                    )
                                    Text(
                                        text = if (isEnglish) currentDay.color.nameEn else currentDay.color.nameEs,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }

                        Text(
                            text = if (isEnglish) currentDay.dateDisplayEn else currentDay.dateDisplayEs,
                            color = Color(0xFF93C5FD),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = if (isEnglish) currentDay.titleEn else currentDay.titleEs,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 19.sp,
                            lineHeight = 24.sp
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF1E293B)
                            ) {
                                Text(
                                    text = if (isEnglish) currentDay.rankEn else currentDay.rankEs,
                                    color = Color(0xFF38BDF8),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF1E293B)
                            ) {
                                Text(
                                    text = if (isEnglish) currentDay.psalterWeekEn else currentDay.psalterWeekEs,
                                    color = Color(0xFFA7F3D0),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = Color.White.copy(alpha = 0.12f))

                        Text(
                            text = if (isEnglish) "📋 Colombian Ordo Rubric (CEC):" else "📋 Nota del Ordo Colombiano (CEC):",
                            color = Color(0xFFFBBF24),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = if (isEnglish) currentDay.ordoNotesEn else currentDay.ordoNotesEs,
                            color = Color(0xFFE2E8F0),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF0F2942),
                            border = BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (isEnglish) "⛪ Basilian & Colombian Church Connection" else "⛪ Comunión con la Iglesia en Colombia",
                                    color = Color(0xFF00F0FF),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isEnglish) currentDay.basilianConnectionEn else currentDay.basilianConnectionEs,
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        // Botón directo al Ordo oficial de la Conferencia Episcopal de Colombia + Escuchar + Compartir + Recordatorio
                        val massReadings = remember(currentDay.id) {
                            MisaDelDiaDevocionarioRepository.getMassReadingsForDay(currentDay)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { openUrl("https://www.cec.org.co") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_open_cec_ordo"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF59E0B),
                                    contentColor = Color(0xFF0A192F)
                                )
                            ) {
                                Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isEnglish) "Official CEC Colombia" else if (isFrench) "Ordo Officiel CEC" else "Ordo Oficial CEC Colombia",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    val summarySpeech = buildString {
                                        append(if (isEnglish) currentDay.titleEn else currentDay.titleEs)
                                        append(". ")
                                        append(if (isEnglish) currentDay.ordoNotesEn else currentDay.ordoNotesEs)
                                        if (hourContent != null) {
                                            append(". ")
                                            append(hourContent.hourName)
                                            append(". ")
                                            append(hourContent.introductoryVerse)
                                            append(". ")
                                            append(hourContent.briefReading.text)
                                            append(". ")
                                            append(hourContent.concludingPrayer)
                                        }
                                    }
                                    viewModel.speakText(summarySpeech)
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFF00F0FF)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00F0FF))
                            ) {
                                Icon(Icons.Default.RecordVoiceOver, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isEnglish) "Listen" else if (isFrench) "Écouter" else "Escuchar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.activateColombianOrdoDailyReminders()
                                    Toast.makeText(
                                        context,
                                        "🔔 Recordatorios diarios activados: Laudes (6:30 a. m.) y Vísperas (6:00 p. m.)",
                                        Toast.LENGTH_LONG
                                    ).show()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_activate_ordo_reminders"),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFF34D399)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF34D399))
                            ) {
                                Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = if (isEnglish) "Daily Lauds/Vespers Alert" else "Aviso Laudes y Vísperas",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    val shareText = MisaDelDiaDevocionarioRepository.buildLiturgicalShareCardText(
                                        day = currentDay,
                                        massReadings = massReadings,
                                        isEnglish = isEnglish
                                    )
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, currentDay.titleEs)
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Compartir Ordo Colombiano y Evangelio"))
                                },
                                modifier = Modifier.testTag("btn_share_ordo_card"),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFDE68A))
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = if (isEnglish) "Share Card" else "Compartir",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Tarjeta del Evangelio y Lecturas de la Misa Diaria en Colombia (CEC)
            item {
                val massReadings = remember(currentDay.id) {
                    MisaDelDiaDevocionarioRepository.getMassReadingsForDay(currentDay)
                }
                var expandedMassReadings by remember { mutableStateOf(true) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_daily_mass_readings_cec"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF08172E).copy(alpha = 0.96f)),
                    border = BorderStroke(1.3.dp, Color(0xFFF59E0B).copy(alpha = 0.65f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedMassReadings = !expandedMassReadings },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isEnglish) "📖 Daily Mass Readings & Gospel (CEC Colombia)" else "📖 Evangelio y Lecturas de la Misa Diaria (CEC)",
                                    color = Color(0xFFFDE68A),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = if (isEnglish) massReadings.lectionaryCycleNoteEn else massReadings.lectionaryCycleNoteEs,
                                    color = Color(0xFF93C5FD),
                                    fontSize = 11.sp
                                )
                            }
                            Icon(
                                imageVector = if (expandedMassReadings) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = Color(0xFFFDE68A)
                            )
                        }

                        AnimatedVisibility(visible = expandedMassReadings) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                HorizontalDivider(color = Color.White.copy(alpha = 0.12f))

                                // Primera Lectura
                                Text(
                                    text = "📜 Primera Lectura (${massReadings.firstReadingCitation})",
                                    color = Color(0xFF38BDF8),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.5.sp
                                )
                                Text(
                                    text = if (isEnglish) massReadings.firstReadingTextEn else massReadings.firstReadingTextEs,
                                    color = Color.White,
                                    fontSize = 12.5.sp,
                                    lineHeight = 18.sp
                                )

                                // Salmo Responsorial
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF0F2942),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = "🎵 Salmo Responsorial (${massReadings.responsorialPsalmCitation})",
                                            color = Color(0xFFFDE68A),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = if (isEnglish) massReadings.responsorialPsalmResponseEn else massReadings.responsorialPsalmResponseEs,
                                            color = Color(0xFFFBBF24),
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = if (isEnglish) massReadings.responsorialPsalmVersesEn else massReadings.responsorialPsalmVersesEs,
                                            color = Color(0xFFE2E8F0),
                                            fontSize = 12.sp,
                                            lineHeight = 17.sp
                                        )
                                    }
                                }

                                // Santo Evangelio
                                Text(
                                    text = "${massReadings.gospelTitleEs} (${massReadings.gospelCitation})",
                                    color = Color(0xFF34D399),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.5.sp
                                )
                                Text(
                                    text = if (isEnglish) massReadings.gospelAcclamationEn else massReadings.gospelAcclamationEs,
                                    color = Color(0xFFFDE68A),
                                    fontSize = 11.5.sp,
                                    fontStyle = FontStyle.Italic
                                )
                                Text(
                                    text = if (isEnglish) massReadings.gospelTextEn else massReadings.gospelTextEs,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    lineHeight = 19.sp
                                )

                                // Reflexión Basiliana
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF064E3B).copy(alpha = 0.4f),
                                    border = BorderStroke(1.dp, Color(0xFF34D399).copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = if (isEnglish) "💡 Basilian Reflection on Today's Word" else "💡 Reflexión Basiliana sobre la Palabra de Hoy",
                                            color = Color(0xFF6EE7B7),
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = if (isEnglish) massReadings.basilianHomilyReflectionEn else massReadings.basilianHomilyReflectionEs,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            lineHeight = 17.sp
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        val gospelAudio = buildString {
                                            append(massReadings.gospelTitleEs)
                                            append(". ")
                                            append(if (isEnglish) massReadings.gospelTextEn else massReadings.gospelTextEs)
                                            append(". Reflexión Basiliana: ")
                                            append(if (isEnglish) massReadings.basilianHomilyReflectionEn else massReadings.basilianHomilyReflectionEs)
                                        }
                                        viewModel.speakText(gospelAudio)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF00F0FF),
                                        contentColor = Color(0xFF0A192F)
                                    )
                                ) {
                                    Icon(Icons.Default.RecordVoiceOver, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isEnglish) "Listen to Today's Gospel & Reflection" else "Escuchar Evangelio y Reflexión con Voz Humana",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Selector de Celebraciones del Calendario Propio de Colombia (Ordo CEC)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isEnglish)
                            "📅 Celebrations of the Colombian Liturgical Ordo (CEC)"
                        else
                            "📅 Celebraciones del Ordo Litúrgico de Colombia (CEC)",
                        color = Color(0xFFFDE68A),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(allCelebrations.size) { index ->
                            val cel = allCelebrations[index]
                            val isSelected = selectedCelebrationIndex == index
                            val chipLabel = when (cel.id) {
                                "chiquinquira_jul_9" -> "🇨🇴 Ntra. Sra. de Chiquinquirá (9 Jul)"
                                "pedro_claver_sep_9" -> "⛓️ San Pedro Claver (9 Sep)"
                                "luis_beltran_oct_9" -> "✝️ San Luis Beltrán (9 Oct)"
                                "laura_montoya_oct_21" -> "🌿 Santa Laura Montoya (21 Oct)"
                                "basilio_magno_ene_2" -> "📘 San Basilio Magno (2 Ene)"
                                else -> if (isEnglish) "☀️ Today's Ordo CEC" else "☀️ Ordo de Hoy en Colombia"
                            }
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCelebrationIndex = index },
                                label = {
                                    Text(
                                        text = chipLabel,
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFF59E0B).copy(alpha = 0.25f),
                                    selectedLabelColor = Color(0xFFFDE68A),
                                    containerColor = Color(0xFF0A192F).copy(alpha = 0.85f),
                                    labelColor = Color(0xFFCBD5E1)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = Color.White.copy(alpha = 0.2f),
                                    selectedBorderColor = Color(0xFFF59E0B)
                                )
                            )
                        }
                    }
                }
            }

            // Selector de Horas Litúrgicas
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isEnglish)
                            "🕰️ Liturgy of the Hours (Colombian CEC Edition)"
                        else
                            "🕰️ Liturgia de las Horas (Según el Ordo de Colombia)",
                        color = Color(0xFF00F0FF),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val hoursList = LiturgicalHourType.values().toList()
                        items(hoursList) { hourType ->
                            val isSelected = selectedHourType == hourType
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedHourType = hourType },
                                label = {
                                    Text(
                                        text = "${if (isEnglish) hourType.titleEn else hourType.titleEs} (${hourType.suggestedTime})",
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF00F0FF).copy(alpha = 0.22f),
                                    selectedLabelColor = Color(0xFF00F0FF),
                                    containerColor = Color(0xFF0A192F).copy(alpha = 0.85f),
                                    labelColor = Color(0xFFCBD5E1)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = Color.White.copy(alpha = 0.2f),
                                    selectedBorderColor = Color(0xFF00F0FF)
                                )
                            )
                        }
                    }
                }
            }

            // Contenido completo de la Hora Litúrgica seleccionada
            if (hourContent != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0A192F).copy(alpha = 0.94f)),
                        border = BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.35f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = hourContent.hourName,
                                color = Color(0xFFFDE68A),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 17.sp
                            )

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF1E293B)
                            ) {
                                Text(
                                    text = "📌 Rúbrica CEC: ${hourContent.ordoRubricNote}",
                                    color = Color(0xFFFBBF24),
                                    fontSize = 11.sp,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }

                            // Invocación Inicial
                            Text(
                                text = if (isEnglish) "✠ Introductory Verse" else "✠ Invocación Inicial",
                                color = Color(0xFF38BDF8),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = hourContent.introductoryVerse,
                                color = Color.White,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )

                            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                            // Himno
                            Text(
                                text = "🎵 ${hourContent.hymnTitle}",
                                color = Color(0xFFFDE68A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = hourContent.hymnText,
                                color = Color(0xFFE2E8F0),
                                fontSize = 13.sp,
                                fontStyle = FontStyle.Italic,
                                lineHeight = 19.sp
                            )

                            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                            // Salmodia
                            Text(
                                text = if (isEnglish) "📖 Psalmody" else "📖 Salmodia",
                                color = Color(0xFF00F0FF),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp
                            )

                            hourContent.psalms.forEach { psalm ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFF0F2942),
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = psalm.title,
                                            color = Color(0xFFFDE68A),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        if (psalm.subtitle.isNotBlank()) {
                                            Text(
                                                text = psalm.subtitle,
                                                color = Color(0xFF93C5FD),
                                                fontSize = 11.sp,
                                                fontStyle = FontStyle.Italic
                                            )
                                        }
                                        Text(
                                            text = psalm.antiphon,
                                            color = Color(0xFFFBBF24),
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = psalm.psalmText,
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            lineHeight = 19.sp
                                        )
                                        if (psalm.gloriaPatri.isNotBlank()) {
                                            Text(
                                                text = psalm.gloriaPatri,
                                                color = Color(0xFFA7F3D0),
                                                fontSize = 12.sp,
                                                fontStyle = FontStyle.Italic
                                            )
                                        }
                                        Text(
                                            text = psalm.antiphon,
                                            color = Color(0xFFFBBF24),
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }

                            // Lectura Breve
                            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                            Text(
                                text = "📜 ${hourContent.briefReading.title} (${hourContent.briefReading.citation})",
                                color = Color(0xFF38BDF8),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = hourContent.briefReading.text,
                                color = Color.White,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                            Text(
                                text = "V/. ${hourContent.briefReading.responsoryVerse}\nR/. ${hourContent.briefReading.responsoryAnswer}",
                                color = Color(0xFFA7F3D0),
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )

                            // Segunda Lectura Patrística (Oficio de Lectura)
                            val patristic = hourContent.secondReading
                            if (patristic != null) {
                                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                                Text(
                                    text = "🏛️ ${patristic.title}",
                                    color = Color(0xFFFDE68A),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = patristic.citation,
                                    color = Color(0xFF93C5FD),
                                    fontSize = 11.sp,
                                    fontStyle = FontStyle.Italic
                                )
                                Text(
                                    text = patristic.text,
                                    color = Color(0xFFE2E8F0),
                                    fontSize = 13.sp,
                                    lineHeight = 19.sp
                                )
                                Text(
                                    text = "V/. ${patristic.responsoryVerse}\nR/. ${patristic.responsoryAnswer}",
                                    color = Color(0xFFA7F3D0),
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }

                            // Cántico Evangélico (Benedictus / Magnificat / Nunc Dimittis)
                            if (!hourContent.canticleTitle.isNullOrBlank() && !hourContent.canticleText.isNullOrBlank()) {
                                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                                Text(
                                    text = "✨ ${hourContent.canticleTitle}",
                                    color = Color(0xFFFDE68A),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp
                                )
                                hourContent.canticleAntiphon?.let { ant ->
                                    Text(
                                        text = ant,
                                        color = Color(0xFFFBBF24),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                }
                                Text(
                                    text = hourContent.canticleText,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    lineHeight = 19.sp
                                )
                                hourContent.canticleAntiphon?.let { ant ->
                                    Text(
                                        text = ant,
                                        color = Color(0xFFFBBF24),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            // Preces
                            val preces = hourContent.preces
                            if (preces != null) {
                                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                                Text(
                                    text = if (isEnglish) "🙏 Intercessions (Preces)" else "🙏 Preces",
                                    color = Color(0xFF38BDF8),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = preces.intro,
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 12.sp
                                )
                                preces.petitions.forEach { (invocation, answer) ->
                                    Text(
                                        text = "• $invocation\n  R/. $answer",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                                if (preces.conclusion.isNotBlank()) {
                                    Text(
                                        text = preces.conclusion,
                                        color = Color(0xFFFBBF24),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            // Padre Nuestro y Oración Conclusiva
                            if (hourContent.ourFather.isNotBlank()) {
                                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                                Text(
                                    text = if (isEnglish) "Our Father" else "Padre Nuestro",
                                    color = Color(0xFFFDE68A),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = hourContent.ourFather,
                                    color = Color(0xFFE2E8F0),
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }

                            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                            Text(
                                text = if (isEnglish) "Concluding Prayer" else "Oración Conclusiva",
                                color = Color(0xFF00F0FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = hourContent.concludingPrayer,
                                color = Color.White,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )

                            // Antífona Mariana Final (Completas)
                            hourContent.marianAntiphon?.let { marian ->
                                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                                Text(
                                    text = marian,
                                    color = Color(0xFFFDE68A),
                                    fontSize = 12.sp,
                                    fontStyle = FontStyle.Italic,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Sub-pestaña 1: Portal CEC & Enlaces directos al Ordo Colombiano
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0A192F).copy(alpha = 0.95f)),
                    border = BorderStroke(1.5.dp, Color(0xFF00F0FF).copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "🇨🇴 Vínculo Oficial con el Ordo Colombiano (CEC)",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "Accede directamente a los portales litúrgicos de la Conferencia Episcopal de Colombia (CEC), el Calendario Litúrgico Propio de Colombia y las lecturas diarias aprobadas para las jurisdicciones eclesiásticas colombianas.",
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        // Enlace 1: Conferencia Episcopal de Colombia
                        OutlinedCard(
                            onClick = { openUrl("https://www.cec.org.co") },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFF0F2942)),
                            border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "🏛️ Conferencia Episcopal de Colombia (CEC)",
                                        color = Color(0xFFFDE68A),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "Portal oficial www.cec.org.co · Departamento de Liturgia y Ordo Anual",
                                        color = Color(0xFFCBD5E1),
                                        fontSize = 11.sp
                                    )
                                }
                                Icon(Icons.Default.OpenInNew, contentDescription = null, tint = Color(0xFFF59E0B))
                            }
                        }

                        // Enlace 2: Lecturas y Evangelio del Día
                        OutlinedCard(
                            onClick = { openUrl("https://www.vaticannews.va/es/palabra-del-dia.html") },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFF0F2942)),
                            border = BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "📖 Lecturas de la Misa y Evangelio del Día",
                                        color = Color(0xFF00F0FF),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "Leccionario Litúrgico · Palabra del Día en comunión con la Iglesia",
                                        color = Color(0xFFCBD5E1),
                                        fontSize = 11.sp
                                    )
                                }
                                Icon(Icons.Default.OpenInNew, contentDescription = null, tint = Color(0xFF00F0FF))
                            }
                        }

                        // Enlace 3: Comunidad Basiliana en Colombia y el Mundo
                        OutlinedCard(
                            onClick = { openUrl("https://www.basilian.org") },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFF0F2942)),
                            border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "✝️ Congregación de San Basilio (C.S.B.)",
                                        color = Color(0xFF93C5FD),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "Presencia activa en Bogotá y Medellín • Misión educativa, formativa y parroquial",
                                        color = Color(0xFFCBD5E1),
                                        fontSize = 11.sp
                                    )
                                }
                                Icon(Icons.Default.OpenInNew, contentDescription = null, tint = Color(0xFF38BDF8))
                            }
                        }
                    }
                }
            }

            // Devocionario Basiliano y Colombiano (Audio + Texto)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_basilian_devotional_prayers"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0A192F).copy(alpha = 0.95f)),
                    border = BorderStroke(1.5.dp, Color(0xFFF59E0B).copy(alpha = 0.65f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (isEnglish)
                                "📿 Basilian & Colombian Devotional Prayers (Audio + Text)"
                            else
                                "📿 Devocionario Basiliano y Colombiano (Audio + Texto)",
                            color = Color(0xFFFDE68A),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = if (isEnglish)
                                "Pray and listen with natural human voice to traditional prayers of St. Basil the Great, Our Lady of Chiquinquirá, and Guided Lectio Divina."
                            else
                                "Reza y escucha con voz humana natural las oraciones de San Basilio Magno, Nuestra Señora de Chiquinquirá, las Vocaciones C.S.B. y la Lectio Divina guiada.",
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        MisaDelDiaDevocionarioRepository.devotionalPrayers.forEach { prayer ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF0F2942),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.14f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(50),
                                            color = Color(0xFFF59E0B).copy(alpha = 0.2f)
                                        ) {
                                            Text(
                                                text = if (isEnglish) prayer.categoryEn else prayer.categoryEs,
                                                color = Color(0xFFFDE68A),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = if (isEnglish) prayer.titleEn else prayer.titleEs,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.5.sp
                                    )
                                    Text(
                                        text = if (isEnglish) prayer.subtitleEn else prayer.subtitleEs,
                                        color = Color(0xFF93C5FD),
                                        fontSize = 11.sp,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Text(
                                        text = if (isEnglish) prayer.bodyEn else prayer.bodyEs,
                                        color = Color(0xFFE2E8F0),
                                        fontSize = 12.5.sp,
                                        lineHeight = 18.sp
                                    )
                                    Text(
                                        text = "✠ ${prayer.latinMottoOrClosing}",
                                        color = Color(0xFFFBBF24),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                val speech = "${if (isEnglish) prayer.titleEn else prayer.titleEs}. ${if (isEnglish) prayer.bodyEn else prayer.bodyEs}"
                                                viewModel.speakText(speech)
                                            },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF00F0FF),
                                                contentColor = Color(0xFF0A192F)
                                            )
                                        ) {
                                            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, modifier = Modifier.size(15.dp))
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Text(
                                                text = if (isEnglish) "Listen Prayer" else "Escuchar Oración",
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 11.5.sp
                                            )
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                val shareBody = "🙏 *${if (isEnglish) prayer.titleEn else prayer.titleEs}*\n\n${if (isEnglish) prayer.bodyEn else prayer.bodyEs}\n\n✠ _${prayer.latinMottoOrClosing}_"
                                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                    type = "text/plain"
                                                    putExtra(Intent.EXTRA_TEXT, shareBody)
                                                }
                                                context.startActivity(Intent.createChooser(shareIntent, prayer.titleEs))
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFDE68A))
                                        ) {
                                            Icon(Icons.Default.Share, contentDescription = "Compartir oración", modifier = Modifier.size(15.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Acceso discreto para hermanos basilianos que ingresaron con correo externo
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0A192F).copy(alpha = 0.88f)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.14f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showBasilianVerifyPanel = !showBasilianVerifyPanel },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "¿Eres religioso de la Congregación de San Basilio (C.S.B.)?",
                                color = Color(0xFFFDE68A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Icon(
                            imageVector = if (showBasilianVerifyPanel) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }

                    AnimatedVisibility(visible = showBasilianVerifyPanel) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Los registros internos de la fraternidad están reservados para miembros verificados C.S.B. (${userEmail.ifBlank { "Sin sesión" }}).",
                                color = Color(0xFFCBD5E1),
                                fontSize = 11.sp
                            )

                            if (permissionRequestedBanner) {
                                Text(
                                    text = "✅ Solicitud enviada al administrador de la Curia.",
                                    color = Color(0xFF10B981),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            } else {
                                OutlinedButton(
                                    onClick = onRequestPermission,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("btn_request_basilian_permission"),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, Color(0xFFF59E0B))
                                ) {
                                    Text(
                                        text = "Solicitar Verificación de Correo Basiliano",
                                        color = Color(0xFFFDE68A),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = unlockCodeInput,
                                    onValueChange = onUnlockCodeChange,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_basilian_unlock_code"),
                                    placeholder = { Text("Código interno C.S.B.", fontSize = 11.sp) },
                                    singleLine = true,
                                    visualTransformation = PasswordVisualTransformation(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = Color(0xFF00F0FF),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.25f)
                                    )
                                )
                                Button(
                                    onClick = onValidateUnlockCode,
                                    modifier = Modifier.testTag("btn_validate_basilian_code"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF00F0FF),
                                        contentColor = Color(0xFF0A192F)
                                    )
                                ) {
                                    Text("Validar", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

