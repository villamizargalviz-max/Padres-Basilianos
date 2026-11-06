package com.example.ui.screens

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.RezandoVoySession
import com.example.data.model.RezandoVoyStep
import com.example.data.repository.DailyGospelAndBasilianWisdomRepository
import com.example.data.repository.RezandoVoyData
import com.example.ui.components.BasilianCrestView
import com.example.ui.components.ChristInteriorStreakSimulatorCard
import com.example.ui.components.CraftedSectionHeader
import com.example.ui.components.GospelLiturgicalArt
import com.example.ui.components.OrganicBrushUnderline
import com.example.ui.components.SanBasilioIconView
import com.example.ui.components.futuristic3DCard
import com.example.ui.components.rememberVoiceDictation
import com.example.ui.components.VoiceDictationMicButton
import com.example.data.repository.LiturgiaOrdoColombianoRepository
import com.example.data.repository.DailySaintsRepository
import com.example.data.model.AppLanguage
import com.example.data.repository.DailySaint
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.BasilianViewModel

@Composable
fun RezandoHoyScreen(
    viewModel: BasilianViewModel,
    appLanguage: AppLanguage = AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val strings = AppStrings.get(appLanguage)
    val isTtsSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val currentTheme by viewModel.currentAppTheme.collectAsState()
    val customCrestUri by viewModel.customCrestUri.collectAsState()
    val customPrayerSideImageUri by viewModel.customPrayerSideImageUri.collectAsState()
    val todayWisdom by viewModel.todayWisdom.collectAsState()
    val dailyPrayers by viewModel.dailyPrayerConfigs.collectAsState()
    val primaryPrayer = dailyPrayers.firstOrNull { it.type == "LAUDES" } ?: dailyPrayers.firstOrNull()
    val dailySaints by viewModel.dailySaints.collectAsState()
    val cachedDaysCount by viewModel.cachedDaysCount.collectAsState()
    var selectedSaintDetail by remember { mutableStateOf<DailySaint?>(null) }
    val context = LocalContext.current

    val showHeroBanner by viewModel.showHeroBanner.collectAsState()
    val showDevotionalImageSlots by viewModel.showDevotionalImageSlots.collectAsState()
    val showOrdoQuickAccess by viewModel.showOrdoQuickAccess.collectAsState()
    val showCuriaAnnouncementInPrayer by viewModel.showCuriaAnnouncementInPrayer.collectAsState()
    val showDailyAudioPlayer by viewModel.showDailyAudioPlayer.collectAsState()
    val globalBgMode by viewModel.globalBackgroundMode.collectAsState()
    val userProfile by viewModel.currentUserProfile.collectAsState()
    val intensity3D by viewModel.futuristic3DIntensity.collectAsState()
    val hasFullBasilianAccess by viewModel.isBasilianPrivateAccess.collectAsState()
    val isAppCreatorLeader by viewModel.isAppCreatorLeader.collectAsState()

    val sessions = remember(todayWisdom, appLanguage) { RezandoVoyData.getAllSessions(todayWisdom, appLanguage) }
    var selectedSessionIndex by remember { mutableIntStateOf(0) }
    val currentSession = sessions.getOrElse(selectedSessionIndex) { sessions.first() }

    var currentStepIndex by remember { mutableIntStateOf(0) }
    var fontScaleMultiplier by remember { mutableFloatStateOf(1f) }

    var showImageOptionsDialog by remember { mutableStateOf(false) }
    val prayerImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.setCustomPrayerSideImage(uri)
        }
    }
    val crestPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.setCustomCrest(uri)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. GRAN ALTAR DE ORACIÓN: ESCUDO CENTRAL GRANDE + IMAGEN LATERAL PERSONALIZABLE (CON EFECTO 3D)
        if (showHeroBanner) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .futuristic3DCard(
                            accentColor = Color(0xFF00F0FF),
                            secondaryColor = currentTheme.goldColor,
                            cornerRadius = 20.dp
                        )
                        .testTag("basilian_prayer_grand_altar"),
                    shape = currentTheme.cardShape,
                    colors = CardDefaults.cardColors(
                        containerColor = currentTheme.surfaceColor
                    ),
                    border = BorderStroke(2.dp, currentTheme.goldColor.copy(alpha = 0.8f)),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 1. Insignia Principal Centrada a Ancho Completo: CONGREGACIÓN DE SAN BASILIO
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = currentTheme.goldColor.copy(alpha = 0.16f),
                        border = BorderStroke(1.2.dp, currentTheme.goldColor.copy(alpha = 0.75f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = strings.rezandoHoy.altarTitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = currentTheme.accentRed,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.4.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }

                    // 2. Fila equilibrada de Botones Oficiales junto al Escudo (Padres Basilianos + Pastoral Vocacional)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Página oficial de los Padres Basilianos junto al Escudo
                        Surface(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.basilian.org/"))
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            },
                            shape = RoundedCornerShape(50),
                            color = currentTheme.goldColor.copy(alpha = 0.16f),
                            border = BorderStroke(1.2.dp, currentTheme.goldColor),
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_basilian_org_inicio")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                BasilianCrestView(size = 20.dp, showMottoText = false)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Padres Basilianos",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = currentTheme.primaryColor,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Pastoral Vocacional integrada junto al Escudo y la página de los Basilianos
                        Surface(
                            onClick = {
                                try {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://www.facebook.com/share/1CNcP9dK3p/?mibextid=wwXIfr")
                                    )
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            },
                            shape = RoundedCornerShape(50),
                            color = Color(0xFF1877F2).copy(alpha = 0.14f),
                            border = BorderStroke(1.dp, Color(0xFF1877F2).copy(alpha = 0.7f)),
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_pastoral_vocacional_crest")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Pastoral Vocacional",
                                    tint = Color(0xFF1877F2),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Pastoral Vocacional",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF1877F2),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Text(
                        text = strings.rezandoHoy.altarSubtitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = currentTheme.primaryColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center
                    )

                    // FILA CON EL ESCUDO CENTRAL Y LA FOTO DE DEVOCIÓN / FOTO QUE CADA QUIEN COLOCA
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // IMAGEN CENTRAL: ESCUDO BASILIANO (120dp)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = currentTheme.goldColor.copy(alpha = 0.12f),
                                border = BorderStroke(2.dp, currentTheme.goldColor),
                                shadowElevation = 4.dp,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clickable {
                                        viewModel.selectTab(AppTab.ORACION_DIARIA)
                                        viewModel.refreshBasilianCorpusData()
                                    }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    if (customCrestUri != null) {
                                        AsyncImage(
                                            model = customCrestUri,
                                            contentDescription = when (appLanguage) {
                                                AppLanguage.EN -> "Official Crest"
                                                AppLanguage.FR -> "Blason Officiel"
                                                AppLanguage.ES -> "Escudo Oficial"
                                            },
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(RoundedCornerShape(18.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        BasilianCrestView(
                                            size = 108.dp,
                                            showMottoText = false,
                                            onClick = {
                                                viewModel.selectTab(AppTab.ORACION_DIARIA)
                                                viewModel.refreshBasilianCorpusData()
                                            }
                                        )
                                    }
                                }
                            }
                            Text(
                                text = strings.rezandoHoy.crestLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.primaryColor,
                                letterSpacing = 0.8.sp
                            )
                            Surface(
                                onClick = {
                                    crestPicker.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                color = currentTheme.goldColor.copy(alpha = 0.15f),
                                border = BorderStroke(0.8.dp, currentTheme.goldColor.copy(alpha = 0.6f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = currentTheme.primaryColor,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = if (customCrestUri != null)
                                            when (appLanguage) {
                                                AppLanguage.EN -> "Change crest"
                                                AppLanguage.FR -> "Changer blason"
                                                AppLanguage.ES -> "Cambiar escudo"
                                            }
                                        else
                                            when (appLanguage) {
                                                AppLanguage.EN -> "Custom crest"
                                                AppLanguage.FR -> "Personnaliser"
                                                AppLanguage.ES -> "Personalizar"
                                            },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = currentTheme.primaryColor
                                    )
                                }
                            }
                        }

                        // IMAGEN LATERAL: LA FOTO QUE CADA QUIEN COLOCA (SAN BASILIO O DEVOCIÓN PERSONAL)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = currentTheme.primaryColor.copy(alpha = 0.08f),
                                border = BorderStroke(2.dp, currentTheme.primaryColor.copy(alpha = 0.5f)),
                                shadowElevation = 4.dp,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clickable {
                                        prayerImagePicker.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                    .testTag("prayer_side_image_surface")
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    if (customPrayerSideImageUri != null) {
                                        AsyncImage(
                                            model = customPrayerSideImageUri,
                                            contentDescription = when (appLanguage) {
                                                AppLanguage.EN -> "Personal Devotion Image"
                                                AppLanguage.FR -> "Image de Dévotion Personnelle"
                                                AppLanguage.ES -> "Imagen de Devoción Personal"
                                            },
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(RoundedCornerShape(18.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        SanBasilioIconView(canvasSize = 100.dp)
                                    }

                                    // Badge de edición en la esquina inferior derecha
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(6.dp)
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.95f))
                                            .border(1.2.dp, currentTheme.primaryColor, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PhotoCamera,
                                            contentDescription = when (appLanguage) {
                                                AppLanguage.EN -> "Upload photo"
                                                AppLanguage.FR -> "Télécharger photo"
                                                AppLanguage.ES -> "Subir foto"
                                            },
                                            tint = currentTheme.primaryColor,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = if (customPrayerSideImageUri != null) strings.rezandoHoy.myDevotionLabel else strings.rezandoHoy.patronLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.primaryColor,
                                fontSize = 10.sp
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    onClick = {
                                        prayerImagePicker.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    color = currentTheme.primaryColor.copy(alpha = 0.15f),
                                    border = BorderStroke(0.8.dp, currentTheme.primaryColor.copy(alpha = 0.6f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.AddPhotoAlternate,
                                            contentDescription = null,
                                            tint = currentTheme.primaryColor,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = if (customPrayerSideImageUri != null)
                                                when (appLanguage) {
                                                    AppLanguage.EN -> "Change photo"
                                                    AppLanguage.FR -> "Changer photo"
                                                    AppLanguage.ES -> "Cambiar foto"
                                                }
                                            else
                                                when (appLanguage) {
                                                    AppLanguage.EN -> "Upload photo"
                                                    AppLanguage.FR -> "Télécharger"
                                                    AppLanguage.ES -> "Subir foto"
                                                },
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = currentTheme.primaryColor
                                        )
                                    }
                                }

                                if (customPrayerSideImageUri != null) {
                                    Surface(
                                        onClick = { viewModel.resetCustomPrayerSideImage() },
                                        shape = RoundedCornerShape(8.dp),
                                        color = ShieldRed.copy(alpha = 0.12f)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = when (appLanguage) {
                                                AppLanguage.EN -> "Remove photo"
                                                AppLanguage.FR -> "Supprimer photo"
                                                AppLanguage.ES -> "Quitar foto"
                                            },
                                            tint = ShieldRed,
                                            modifier = Modifier.padding(5.dp).size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Lema en cinta dorada
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = currentTheme.goldColor.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, currentTheme.goldColor.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = appLanguage.motto,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.primaryColor,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.Serif,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                        )
                    }
                }
            }
        }
        }

        // CITA SAGRADA DESTACADA DEL DÍA (CUADRO FUTURISTA 3D MULTICOLOR)
        item {
            val dailyQuotes = remember(appLanguage) {
                when (appLanguage) {
                    AppLanguage.EN -> listOf(
                        Triple(
                            "Just as bees gather nectar from flowers to make honey, so too the wise soul gathers truth and virtue from every sacred page.",
                            "Saint Basil the Great • Address to Young Men",
                            "PATRISTIC WISDOM"
                        ),
                        Triple(
                            "Teach me goodness, discipline, and knowledge, for I have believed in Your commandments.",
                            "Psalm 119:66 • Basilian Motto C.S.B.",
                            "SACRED SCRIPTURE"
                        )
                    )
                    AppLanguage.FR -> listOf(
                        Triple(
                            "Comme les abeilles butinent les fleurs pour produire le miel, ainsi l'âme sage recueille la vérité et la vertu dans les Saintes Écritures.",
                            "Saint Basile le Grand • Aux Jeunes Gens",
                            "SAGESSE PATRISTIQUE"
                        ),
                        Triple(
                            "Enseigne-moi la bonté, la discipline et la science, car j'ai foi en tes commandements.",
                            "Psaume 119, 66 • Devise Basilienne C.S.B.",
                            "SAINTE ÉCRITURE"
                        )
                    )
                    AppLanguage.ES -> listOf(
                        Triple(
                            "Como las abejas extraen de las flores la miel más pura, así el alma sabia rescata de la Palabra la luz, la bondad y la verdad que edifican la vida en común.",
                            "San Basilio Magno • A los Jóvenes (Regla C.S.B.)",
                            "CITA DESTACADA DEL DÍA"
                        ),
                        Triple(
                            "Enséñame la bondad, la disciplina y la ciencia, porque creo en tus mandamientos.",
                            "Salmo 118, 66 • Lema Oficial C.S.B.",
                            "PALABRA VIVA"
                        )
                    )
                }
            }
            val activeQuote = dailyQuotes[selectedSessionIndex % dailyQuotes.size]
            com.example.ui.components.FuturisticQuoteCalloutCard(
                quoteText = activeQuote.first,
                authorOrSource = activeQuote.second,
                categoryTag = activeQuote.third,
                accentColor = com.example.ui.components.FuturisticDesignPalette.NeonGold,
                secondaryColor = com.example.ui.components.FuturisticDesignPalette.NeonCyan,
                onSpeak = { viewModel.speakText("${activeQuote.first}. ${activeQuote.second}") }
            )
        }

        // ESTADO DE ORACIÓN Y ACOMPAÑAMIENTO DEL CORAZÓN (EXCLUSIVO DE LA PRIMERA PANTALLA)
        item {
            FraternalHeartAccompanimentCard(
                appLanguage = appLanguage,
                currentTheme = currentTheme,
                onSpeak = { text -> viewModel.speakText(text) },
                onVoiceIntention = { spoken ->
                    val prompt = when (appLanguage) {
                        AppLanguage.EN -> "Brother guide, I bring this heartfelt prayer intention before the Lord: \"$spoken\". Please offer me fraternal counsel, scripture wisdom from Saint Basil, and a blessing."
                        AppLanguage.FR -> "Cher frère guide, je dépose cette intention du cœur devant le Seigneur : «$spoken». Offre-moi un conseil fraternel, la sagesse de saint Basile et une bénédiction."
                        AppLanguage.ES -> "Hermano guía, pongo en el altar del Señor esta intención del corazón: «$spoken». Dame un consuelo fraternal, la sabiduría de San Basilio y una bendición."
                    }
                    viewModel.generateCreativeHistory(
                        prompt = prompt,
                        mode = com.example.data.ai.CreativeHistoryMode.HISTORIA_COMPLETA
                    )
                    viewModel.selectTab(AppTab.HISTORIA_IA)
                }
            )
        }

        // Acceso directo a la Red Basiliana para la Educación y Mapa Interactivo de Obras
        if (showOrdoQuickAccess) {
            item {
                Surface(
                    onClick = { viewModel.selectTab(AppTab.LITURGIA_HORAS) },
                    shape = RoundedCornerShape(14.dp),
                    color = currentTheme.surfaceColor,
                    border = BorderStroke(1.2.dp, currentTheme.goldColor.copy(alpha = 0.7f)),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open_official_ordo_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("🎓🗺️", fontSize = 20.sp)
                            Column {
                                Text(
                                    text = strings.rezandoHoy.ordoQuickTitle,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = currentTheme.primaryColor
                                )
                                Text(
                                    text = strings.rezandoHoy.ordoQuickSubtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = when (appLanguage) {
                                AppLanguage.EN -> "Go to Basilian Education Network"
                                AppLanguage.FR -> "Aller au Réseau d'Éducation"
                                AppLanguage.ES -> "Ir a Red Basiliana para la Educación"
                            },
                            tint = currentTheme.primaryColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // RETRATO E ÍCONO SAGRADO DE SAN BASILIO MAGNO
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SanBasilioIconView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("san_basilio_hero_icon"),
                    canvasSize = 175.dp
                )

                // Botones de Acceso Rápido a Realidad Aumentada y Modo Nómada
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        onClick = { viewModel.selectTab(AppTab.HISTORIA_IA) },
                        shape = RoundedCornerShape(14.dp),
                        color = currentTheme.surfaceColor,
                        border = BorderStroke(1.2.dp, currentTheme.goldColor),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("launch_historia_ia_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = currentTheme.goldColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = strings.nav.historiaIa,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = currentTheme.onSurfaceColor
                                )
                                Text(
                                    text = strings.historiaIa.cronistaSubtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = currentTheme.primaryColor,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Surface(
                        onClick = { viewModel.selectTab(AppTab.RECURSOS) },
                        shape = RoundedCornerShape(14.dp),
                        color = currentTheme.surfaceColor,
                        border = BorderStroke(1.2.dp, currentTheme.goldColor),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("launch_recursos_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderSpecial,
                                contentDescription = null,
                                tint = currentTheme.goldColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = strings.nav.recursos,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = currentTheme.onSurfaceColor
                                )
                                Text(
                                    text = strings.recursos.subTabFounders,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = currentTheme.primaryColor,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Selector de Días y Momentos de Oración Semanal
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CraftedSectionHeader(
                    title = when (appLanguage) {
                        AppLanguage.EN -> "Meditation & Guided Prayer"
                        AppLanguage.FR -> "Méditation & Prière Guidée"
                        AppLanguage.ES -> "Meditación & Oración Guiada"
                    },
                    subtitle = when (appLanguage) {
                        AppLanguage.EN -> "Select liturgical moment of the week"
                        AppLanguage.FR -> "Sélectionnez le moment liturgique de la semaine"
                        AppLanguage.ES -> "Selecciona el momento litúrgico de la semana"
                    },
                    emoji = "✨",
                    accentColor = ShieldGreenDark,
                    underlineWidth = 165.dp
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(sessions.indices.toList()) { index ->
                        val sess = sessions[index]
                        val isSelected = index == selectedSessionIndex
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) currentTheme.primaryColor else currentTheme.surfaceColor,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) currentTheme.goldColor else currentTheme.outlineColor
                            ),
                            modifier = Modifier
                                .clickable {
                                    selectedSessionIndex = index
                                    currentStepIndex = 0
                                }
                                .testTag("session_tab_$index"),
                            shadowElevation = if (isSelected) 3.dp else 1.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = if (index == 0) Icons.Default.Today else if (sess.dayOfWeek.contains("Domingo")) Icons.Default.Nightlight else Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = if (isSelected) currentTheme.onPrimaryColor else currentTheme.primaryColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = sess.dayOfWeek,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isSelected) currentTheme.onPrimaryColor else currentTheme.onSurfaceColor,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Control de Tamaño de Letra y Lectura en Voz Alta
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = currentTheme.surfaceColor,
                border = BorderStroke(1.dp, currentTheme.outlineColor.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = currentSession.dateDisplay,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.onSurfaceColor,
                            fontFamily = FontFamily.Serif
                        )
                        val readingPrefix = when (appLanguage) {
                            AppLanguage.EN -> "Sacred Reading"
                            AppLanguage.FR -> "Lecture Sainte"
                            AppLanguage.ES -> "Lectura Sagrada"
                        }
                        Text(
                            text = "$readingPrefix: ${currentSession.scriptureReference}",
                            style = MaterialTheme.typography.bodySmall,
                            color = currentTheme.secondaryColor,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = {
                                fontScaleMultiplier = if (fontScaleMultiplier >= 1.3f) 1.0f else fontScaleMultiplier + 0.15f
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatSize,
                                contentDescription = when (appLanguage) {
                                    AppLanguage.EN -> "Adjust text size"
                                    AppLanguage.FR -> "Ajuster la taille du texte"
                                    AppLanguage.ES -> "Cambiar tamaño de texto"
                                },
                                tint = currentTheme.primaryColor
                            )
                        }

                        if (showDailyAudioPlayer) {
                            IconButton(
                                onClick = {
                                    if (isTtsSpeaking) {
                                        viewModel.stopSpeaking()
                                    } else {
                                        val text = buildString {
                                            append(currentSession.dateDisplay)
                                            append(". ")
                                            append(currentSession.scriptureReference)
                                            append(". ")
                                            currentSession.steps.forEach { step ->
                                                append(step.title)
                                                append(": ")
                                                append(step.body)
                                                append(". ")
                                            }
                                        }
                                        viewModel.speakText(text)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (isTtsSpeaking) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                    contentDescription = when (appLanguage) {
                                        AppLanguage.EN -> "Listen to meditation"
                                        AppLanguage.FR -> "Écouter la méditation"
                                        AppLanguage.ES -> "Escuchar texto"
                                    },
                                    tint = if (isTtsSpeaking) currentTheme.secondaryColor else currentTheme.primaryColor
                                )
                            }
                        }
                    }
                }
            }
        }

        // Pasos de la Meditación Diaria Basiliana
        items(currentSession.steps.indices.toList()) { index ->
            val step = currentSession.steps[index]
            val isCurrent = index == currentStepIndex
            RezandoVoyStepCard(
                step = step,
                stepNumber = index + 1,
                isCurrent = isCurrent,
                fontScaleMultiplier = fontScaleMultiplier,
                appLanguage = appLanguage,
                onSpeakStep = {
                    viewModel.speakText("${step.title}. ${step.subtitle}. ${step.body}")
                },
                onSelectStep = {
                    currentStepIndex = index
                }
            )
        }

        // Accesos Directos a las Nuevas Áreas Solicitadas
        item {
            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val dimensionsHeader = when (appLanguage) {
                    AppLanguage.EN -> "BASILIAN DIMENSIONS & COMMUNITY"
                    AppLanguage.FR -> "DIMENSIONS BASILIENNES & COMMUNAUTÉ"
                    AppLanguage.ES -> "DIMENSIONES BASILIANAS & COMUNIDAD"
                }
                Text(
                    text = dimensionsHeader,
                    style = MaterialTheme.typography.labelSmall,
                    color = ShieldRed,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                // 1. Guía Espiritual IA
                QuickDocNavCard(
                    title = when (appLanguage) {
                        AppLanguage.EN -> "Spiritual Guidance & Fraternal AI"
                        AppLanguage.FR -> "Accompagnement Spirituel IA"
                        AppLanguage.ES -> "Guía Espiritual con IA"
                    },
                    subtitle = when (appLanguage) {
                        AppLanguage.EN -> "Theological companion based on the Rule and Letters of Saint Basil"
                        AppLanguage.FR -> "Compagnon théologique inspiré de la Règle et des Lettres de saint Basile"
                        AppLanguage.ES -> "Asistente teológico fundado en la Regla de San Basilio y sus Cartas"
                    },
                    badge = when (appLanguage) {
                        AppLanguage.EN -> "SPIRITUAL CARE"
                        AppLanguage.FR -> "SOIN SPIRITUEL"
                        AppLanguage.ES -> "INTELIGENCIA ESPIRITUAL"
                    },
                    icon = Icons.Default.Psychology,
                    accentColor = ShieldGreenDark,
                    onClick = { viewModel.selectTab(AppTab.HISTORIA_IA) }
                )

                // 2. Rutas de Aprendizaje Adaptativo
                QuickDocNavCard(
                    title = when (appLanguage) {
                        AppLanguage.EN -> "Basilian Formation Journeys"
                        AppLanguage.FR -> "Parcours de Formation Basilienne"
                        AppLanguage.ES -> "Rutas de Formación Basiliana"
                    },
                    subtitle = when (appLanguage) {
                        AppLanguage.EN -> "Pedagogy and discernment for postulants, novices, and lay companions"
                        AppLanguage.FR -> "Pédagogie et discernement pour novices, étudiants et laïcs"
                        AppLanguage.ES -> "Formación pedagógica y discernimiento para postulantes, novicios y laicos"
                    },
                    badge = when (appLanguage) {
                        AppLanguage.EN -> "FORMATION"
                        AppLanguage.FR -> "FORMATION"
                        AppLanguage.ES -> "FORMACIÓN"
                    },
                    icon = Icons.Default.School,
                    accentColor = Color(0xFFD4AF37),
                    onClick = { viewModel.selectTab(AppTab.RUTAS_APRENDIZAJE) }
                )

                if (hasFullBasilianAccess) {
                    // 3. Políticas y Procedimientos 2026 (Exclusivo Basilianos)
                    QuickDocNavCard(
                        title = when (appLanguage) {
                            AppLanguage.EN -> "Policies & Safeguarding 2026"
                            AppLanguage.FR -> "Protection & Bienveillance 2026"
                            AppLanguage.ES -> "Políticas & Procedimientos 2026"
                        },
                        subtitle = when (appLanguage) {
                            AppLanguage.EN -> "Praesidium Accreditation • Child Safeguarding & Pastoral Integrity"
                            AppLanguage.FR -> "Accréditation Praesidium • Protection des mineurs et intégrité pastorale"
                            AppLanguage.ES -> "Acreditación Praesidium • Prevención y Protección de Menores"
                        },
                        badge = when (appLanguage) {
                            AppLanguage.EN -> "GOVERNANCE"
                            AppLanguage.FR -> "GOUVERNANCE"
                            AppLanguage.ES -> "GOBERNANZA"
                        },
                        icon = Icons.Default.Shield,
                        accentColor = ShieldRed,
                        onClick = { viewModel.selectTab(AppTab.POLITICAS_2026) }
                    )

                    // 4. Portal Privado ("Solo para Mí" - Exclusivo para el Líder Creador de la App)
                    if (isAppCreatorLeader) {
                        QuickDocNavCard(
                            title = when (appLanguage) {
                                AppLanguage.EN -> "Solo para Mí • Leader Control"
                                AppLanguage.FR -> "Solo para Mí • Contrôle du Leader"
                                AppLanguage.ES -> "Solo para Mí • Control del Líder"
                            },
                            subtitle = when (appLanguage) {
                                AppLanguage.EN -> "Exclusive leader panel to improve the shared app & receive security alerts"
                                AppLanguage.FR -> "Panneau exclusif du créateur pour améliorer l'application et la sécurité"
                                AppLanguage.ES -> "Exclusivo para mí como líder: mejorar la app compartida y recibir alertas privadas"
                            },
                            badge = when (appLanguage) {
                                AppLanguage.EN -> "LEADER ONLY"
                                AppLanguage.FR -> "LEADER"
                                AppLanguage.ES -> "SOLO LÍDER"
                            },
                            icon = Icons.Default.Key,
                            accentColor = Color(0xFF4A148C),
                            onClick = { viewModel.selectTab(AppTab.PORTAL_CURIA) }
                        )
                    }
                }
            }
        }

        // 5. SIMULADOR DEL CRISTO INTERIOR Y RACHA DE ORACIÓN DIARIA (10,000 DÍAS DE CRECIMIENTO EN JESÚS)
        item {
            ChristInteriorStreakSimulatorCard(
                appLanguage = appLanguage,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )
        }
    }

    if (showImageOptionsDialog) {
        AlertDialog(
            onDismissRequest = { showImageOptionsDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = currentTheme.primaryColor)
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "Devotional Prayer Image"
                            AppLanguage.FR -> "Image Dévotionnelle de Prière"
                            AppLanguage.ES -> "Imagen Devocional de Oración"
                        },
                        fontWeight = FontWeight.Bold,
                        color = currentTheme.primaryColor,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Vista previa
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = currentTheme.primaryColor.copy(alpha = 0.08f),
                        border = BorderStroke(1.5.dp, currentTheme.primaryColor.copy(alpha = 0.4f)),
                        shadowElevation = 3.dp,
                        modifier = Modifier.size(120.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            if (customPrayerSideImageUri != null) {
                                AsyncImage(
                                    model = customPrayerSideImageUri,
                                    contentDescription = "Imagen de San Basilio o Devoción",
                                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                SanBasilioIconView(canvasSize = 100.dp)
                            }
                        }
                    }

                    Text(
                        text = if (customPrayerSideImageUri != null)
                            when (appLanguage) {
                                AppLanguage.EN -> "You have a custom image active in the morning prayer and Gospel header."
                                AppLanguage.FR -> "Vous avez une image personnalisée active dans l'en-tête de prière."
                                AppLanguage.ES -> "Tienes una imagen personalizada activa en el encabezado de oración matutina y Evangelio."
                            }
                        else
                            when (appLanguage) {
                                AppLanguage.EN -> "Currently showing the titular icon of Saint Basil the Great. You can pick an image from your gallery."
                                AppLanguage.FR -> "L'icône de saint Basile le Grand est affichée. Vous pouvez choisir une image depuis votre galerie."
                                AppLanguage.ES -> "Actualmente se muestra el icono titular de San Basilio Magno. Puedes elegir una imagen de devoción desde tu galería."
                            },
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = {
                            prayerImagePicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                            showImageOptionsDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = currentTheme.primaryColor),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (customPrayerSideImageUri != null)
                                when (appLanguage) {
                                    AppLanguage.EN -> "Change for another photo"
                                    AppLanguage.FR -> "Changer de photo"
                                    AppLanguage.ES -> "Cambiar por otra foto"
                                }
                            else
                                when (appLanguage) {
                                    AppLanguage.EN -> "Choose photo from gallery"
                                    AppLanguage.FR -> "Choisir une photo de la galerie"
                                    AppLanguage.ES -> "Elegir foto de mi galería"
                                },
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (customPrayerSideImageUri != null) {
                        OutlinedButton(
                            onClick = {
                                viewModel.resetCustomPrayerSideImage()
                                showImageOptionsDialog = false
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldRed),
                            border = BorderStroke(1.dp, ShieldRed.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when (appLanguage) {
                                    AppLanguage.EN -> "Restore Saint Basil the Great"
                                    AppLanguage.FR -> "Restaurer Saint Basile le Grand"
                                    AppLanguage.ES -> "Restaurar San Basilio Magno"
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showImageOptionsDialog = false }) {
                    Text(strings.common.close, color = currentTheme.primaryColor, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (selectedSaintDetail != null) {
        val saint = com.example.data.repository.UniversalContentLocalizer.localizeDailySaint(selectedSaintDetail!!, appLanguage)
        AlertDialog(
            onDismissRequest = { selectedSaintDetail = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🕊️ ", fontSize = 20.sp)
                    Column {
                        Text(saint.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1B5E20))
                        Text(saint.liturgicalRank, fontSize = 11.sp, color = Color(0xFFC62828), fontWeight = FontWeight.SemiBold)
                    }
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = saint.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = currentTheme.primaryColor
                    )
                    Text(
                        text = saint.shortBiography,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 16.sp
                    )
                    if (saint.patronage.isNotBlank()) {
                        val patronPrefix = when (appLanguage) {
                            AppLanguage.EN -> "🛡️ Patron of:"
                            AppLanguage.FR -> "🛡️ Saint patron de :"
                            AppLanguage.ES -> "🛡️ Patrono de:"
                        }
                        Text(
                            text = "$patronPrefix ${saint.patronage}",
                            fontSize = 11.5.sp,
                            fontStyle = FontStyle.Italic,
                            color = currentTheme.primaryColor
                        )
                    }
                    if (saint.collectPrayer.isNotBlank()) {
                        val collectPrefix = when (appLanguage) {
                            AppLanguage.EN -> "Opening Collect:"
                            AppLanguage.FR -> "Prière d'ouverture :"
                            AppLanguage.ES -> "Oración colecta:"
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "$collectPrefix\n«${saint.collectPrayer}»",
                                fontSize = 11.sp,
                                modifier = Modifier.padding(10.dp),
                                lineHeight = 15.sp,
                                color = Color(0xFF263238)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedSaintDetail = null }) {
                    Text(strings.common.close, color = currentTheme.primaryColor, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun RezandoVoyStepCard(
    step: RezandoVoyStep,
    stepNumber: Int,
    isCurrent: Boolean,
    fontScaleMultiplier: Float,
    onSpeakStep: () -> Unit,
    onSelectStep: () -> Unit,
    appLanguage: AppLanguage = AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val accentColor = remember(stepNumber) {
        com.example.ui.components.FuturisticDesignPalette.accentForIndex(stepNumber - 1)
    }
    val secondaryColor = remember(stepNumber) {
        com.example.ui.components.FuturisticDesignPalette.secondaryForIndex(stepNumber - 1)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .futuristic3DCard(
                accentColor = accentColor,
                secondaryColor = secondaryColor,
                cornerRadius = 20.dp,
                tiltDegrees = if (isCurrent) 2.2f else 1.2f
            )
            .clickable { onSelectStep() }
            .testTag("step_card_$stepNumber"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(
            if (isCurrent) 2.dp else 1.2.dp,
            Brush.linearGradient(
                listOf(
                    accentColor.copy(alpha = if (isCurrent) 0.95f else 0.65f),
                    secondaryColor.copy(alpha = if (isCurrent) 0.85f else 0.45f)
                )
            )
        ),
        elevation = CardDefaults.cardElevation(if (isCurrent) 8.dp else 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF061224).copy(alpha = 0.96f),
                            Color(0xFF0D213B).copy(alpha = 0.94f),
                            accentColor.copy(alpha = if (isCurrent) 0.16f else 0.08f)
                        )
                    )
                )
        ) {
            // Barra lateral luminosa 3D de color por paso
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .heightIn(min = 120.dp)
                    .background(
                        Brush.verticalGradient(listOf(accentColor, secondaryColor, accentColor))
                    )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(accentColor.copy(alpha = 0.30f), secondaryColor.copy(alpha = 0.20f))
                                    )
                                )
                                .border(1.5.dp, accentColor, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "0$stepNumber",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = accentColor,
                                fontSize = 12.sp
                            )
                        }
                        Column {
                            Surface(
                                color = accentColor.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(0.8.dp, accentColor.copy(alpha = 0.65f))
                            ) {
                                Text(
                                    text = step.title.uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = accentColor,
                                    letterSpacing = 1.sp,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = step.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Surface(
                        onClick = onSpeakStep,
                        shape = CircleShape,
                        color = accentColor.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.7f)),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = when (appLanguage) {
                                    AppLanguage.EN -> "Listen to step"
                                    AppLanguage.FR -> "Écouter l'étape"
                                    AppLanguage.ES -> "Escuchar paso"
                                },
                                tint = accentColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = accentColor.copy(alpha = 0.25f), thickness = 0.8.dp)

                // Cuerpo del paso con resaltado inteligente de citas y tipografía luminosa
                com.example.ui.components.FuturisticFormattedTextWithQuotes(
                    rawText = step.body,
                    accentColor = accentColor,
                    secondaryColor = secondaryColor,
                    baseTextColor = Color(0xFFF8FAFC),
                    onSpeakQuote = { onSpeakStep() }
                )
            }
        }
    }
}

@Composable
fun QuickDocNavCard(
    title: String,
    subtitle: String,
    badge: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = Color.Transparent,
        border = BorderStroke(
            1.4.dp,
            Brush.linearGradient(listOf(accentColor, Color(0xFFFFD700).copy(alpha = 0.65f)))
        ),
        shadowElevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
            .futuristic3DCard(
                accentColor = accentColor,
                secondaryColor = Color(0xFFFFD700),
                cornerRadius = 18.dp,
                tiltDegrees = 1.4f
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF071529).copy(alpha = 0.95f),
                            Color(0xFF0E2341).copy(alpha = 0.92f),
                            accentColor.copy(alpha = 0.14f)
                        )
                    )
                )
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(accentColor.copy(alpha = 0.22f))
                    .border(1.2.dp, accentColor, RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = accentColor.copy(alpha = 0.22f),
                    border = BorderStroke(0.8.dp, accentColor.copy(alpha = 0.7f))
                ) {
                    Text(
                        text = "✦ $badge",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 9.5.sp,
                        letterSpacing = 0.8.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 15.sp
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFE2E8F0),
                    fontSize = 11.5.sp
                )
            }

            Surface(
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.22f),
                border = BorderStroke(1.dp, accentColor.copy(alpha = 0.7f)),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FraternalHeartAccompanimentCard(
    appLanguage: AppLanguage,
    currentTheme: BasilianAppTheme,
    onSpeak: (String) -> Unit,
    onVoiceIntention: (String) -> Unit
) {
    val accompaniments = remember(appLanguage) {
        com.example.data.repository.BasilianContentLocalization.getHeartAccompaniments(appLanguage)
    }
    var selectedItem by remember { mutableStateOf<com.example.data.repository.BasilianContentLocalization.HeartAccompaniment?>(null) }

    // Voice Dictation for Heart Prayers & Pastoral Intentions
    val voiceController = rememberVoiceDictation(
        appLanguage = appLanguage,
        onSpeechResult = { spoken ->
            onVoiceIntention(spoken)
        }
    )

    val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    val (greetingHeader, greetingSub) = when (appLanguage) {
        AppLanguage.EN -> when {
            currentHour < 12 -> "Good morning in Christ Jesus" to "May the Lord's peace and goodness guide every step of your journey today."
            currentHour < 18 -> "Good afternoon, brother and sister" to "May the gentle grace of Christ renew your energy and calm your heart."
            else -> "Blessed evening in God's presence" to "Rest your soul and lay down your burdens in the Father's merciful embrace."
        }
        AppLanguage.FR -> when {
            currentHour < 12 -> "Bonjour dans le Christ Jésus" to "Que la paix et la bonté du Seigneur éclairent chacun de vos pas aujourd'hui."
            currentHour < 18 -> "Bon après-midi, cher frère, chère sœur" to "Que la douce grâce du Christ renouvelle vos forces et apaise votre esprit."
            else -> "Bénie soit votre soirée" to "Reposez votre âme et déposez vos fardeaux dans l'amour miséricordieux du Père."
        }
        AppLanguage.ES -> when {
            currentHour < 12 -> "Buenos días en Cristo Jesús" to "Que la bondad, la disciplina y la serenidad de Dios guíen cada uno de tus pasos hoy."
            currentHour < 18 -> "Buenas tardes, hermano y hermana" to "Que la gracia consoladora del Señor renueve tus fuerzas en medio de tus labores."
            else -> "Bendecida noche en Dios" to "Descansa tu corazón y entrega tus fatigas al amor providente y tierno del Padre."
        }
    }

    val heartQuestion = when (appLanguage) {
        AppLanguage.EN -> "How is your heart feeling right now?"
        AppLanguage.FR -> "Comment se porte votre cœur en ce moment ?"
        AppLanguage.ES -> "¿Cómo se encuentra tu corazón en este momento?"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .futuristic3DCard(
                accentColor = Color(0xFF10B981),
                secondaryColor = Color(0xFF00F0FF),
                cornerRadius = 22.dp,
                tiltDegrees = 1.6f
            )
            .testTag("fraternal_accompaniment_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(
            1.6.dp,
            Brush.linearGradient(listOf(Color(0xFF10B981), Color(0xFF00F0FF), Color(0xFFFFD700)))
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF06162A).copy(alpha = 0.96f),
                            Color(0xFF0C233E).copy(alpha = 0.94f)
                        )
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Warm pastoral greeting with 3D badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF10B981).copy(alpha = 0.2f),
                    border = BorderStroke(1.2.dp, Color(0xFF10B981)),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🕊️", fontSize = 20.sp)
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = Color(0xFF10B981).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "ESTADO DE ORACIÓN • ACOMPAÑAMIENTO FRATERNO",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF10B981),
                            letterSpacing = 0.8.sp,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = greetingHeader,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = greetingSub,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE2E8F0),
                        fontSize = 11.5.sp
                    )
                }
            }

            HorizontalDivider(color = Color(0xFF00F0FF).copy(alpha = 0.25f), thickness = 0.8.dp)

            // Voice Intention Banner (Direct Dictation from Home Screen)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF0A1D36),
                border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (appLanguage) {
                                AppLanguage.EN -> "Speak a prayer intention"
                                AppLanguage.FR -> "Dicter une intention de prière"
                                AppLanguage.ES -> "Habla tu intención o pide consuelo"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFFD700)
                        )
                        Text(
                            text = when (appLanguage) {
                                AppLanguage.EN -> "Tap microphone to speak to the spiritual guide"
                                AppLanguage.FR -> "Touchez le micro pour parler à l'aumônier IA"
                                AppLanguage.ES -> "Toca el micro para dictar y recibir consejo pastoral"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 10.5.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                    VoiceDictationMicButton(
                        controller = voiceController,
                        tooltip = when (appLanguage) {
                            AppLanguage.EN -> "Dictate prayer"
                            AppLanguage.FR -> "Dicter prière"
                            AppLanguage.ES -> "Dictar oración"
                        },
                        primaryColor = Color(0xFF00F0FF),
                        testTag = "btn_voice_heart_card"
                    )
                }
            }

            Text(
                text = heartQuestion,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF00F0FF)
            )

            // Chips of human emotions / states with vibrant colors
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(accompaniments.indices.toList()) { idx ->
                    val acc = accompaniments[idx]
                    val isSelected = selectedItem?.stateId == acc.stateId
                    val chipAccent = com.example.ui.components.FuturisticDesignPalette.accentForIndex(idx)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedItem = if (isSelected) null else acc
                        },
                        label = {
                            Text(
                                text = "${acc.icon} ${acc.emotionLabel}",
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                color = if (isSelected) Color.White else Color(0xFFE2E8F0)
                            )
                        },
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = chipAccent.copy(alpha = 0.5f),
                            selectedBorderColor = chipAccent
                        ),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFF091B32),
                            selectedContainerColor = chipAccent.copy(alpha = 0.30f),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Expanded accompaniment message with highlighted quote box
            selectedItem?.let { item ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF091D36),
                    border = BorderStroke(1.2.dp, Color(0xFF00F0FF).copy(alpha = 0.65f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = item.pastoralWord,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )

                        com.example.ui.components.FuturisticQuoteCalloutCard(
                            quoteText = item.biblicalPromise,
                            authorOrSource = item.shortSpokenPrayer,
                            categoryTag = "PROMESA BÍBLICA & BENDICIÓN",
                            accentColor = Color(0xFFFFD700),
                            secondaryColor = Color(0xFF10B981),
                            onSpeak = {
                                val text = "${item.pastoralWord}. ${item.biblicalPromise}. ${item.shortSpokenPrayer}"
                                onSpeak(text)
                            }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val text = "${item.pastoralWord}. ${item.biblicalPromise}. ${item.shortSpokenPrayer}"
                                    onSpeak(text)
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color(0xFF00F0FF)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF00F0FF))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = when (appLanguage) {
                                        AppLanguage.EN -> "Listen to comfort"
                                        AppLanguage.FR -> "Écouter la prière"
                                        AppLanguage.ES -> "Escuchar consuelo"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00F0FF)
                                )
                            }

                            TextButton(onClick = { selectedItem = null }) {
                                Text(
                                    text = when (appLanguage) {
                                        AppLanguage.EN -> "Close"
                                        AppLanguage.FR -> "Fermer"
                                        AppLanguage.ES -> "Cerrar"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF6B8B)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


