package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.receiver.NotificationHelper
import com.example.ui.components.BasilianNavBar
import com.example.ui.components.BasilianTopBar
import com.example.ui.components.HolySpiritGlobalBackground
import com.example.ui.components.LocalCustomCrestUri
import com.example.ui.components.ThemeSelectorDialog
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.BasilianViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create notification channel on app start
        try {
            NotificationHelper.createNotificationChannel(this)
        } catch (_: Throwable) {}

        // Pre-create Chromium WebView code cache directories
        try {
            val jsCacheDir = java.io.File(cacheDir, "WebView/Default/HTTP Cache/Code Cache/js")
            if (!jsCacheDir.exists()) {
                jsCacheDir.mkdirs()
            }
        } catch (_: Exception) {}

        setContent {
            val viewModel: BasilianViewModel = viewModel()
            val currentTab by viewModel.currentTab.collectAsState()
            val isSpeaking by viewModel.isTtsSpeaking.collectAsState()
            val appTheme by viewModel.currentAppTheme.collectAsState()
            val customCrestUri by viewModel.customCrestUri.collectAsState()
            val globalBgMode by viewModel.globalBackgroundMode.collectAsState()
            val globalCustomUri by viewModel.globalBackgroundCustomUri.collectAsState()
            val globalOpacity by viewModel.globalBackgroundOpacity.collectAsState()

            val showTabOracion by viewModel.showTabOracion.collectAsState()
            val showTabLiturgia by viewModel.showTabLiturgia.collectAsState()
            val showTabCumpleanos by viewModel.showTabCumpleanos.collectAsState()
            val showTabHistoria by viewModel.showTabHistoria.collectAsState()
            val showTabRecursos by viewModel.showTabRecursos.collectAsState()
            val appLanguage by viewModel.appLanguage.collectAsState()
            val userProfile by viewModel.currentUserProfile.collectAsState()
            val futuristic3DIntensity by viewModel.futuristic3DIntensity.collectAsState()
            val isBasilianPrivateAccess by viewModel.isBasilianPrivateAccess.collectAsState()
            val isCuriaAdmin by viewModel.curiaIsAuthenticated.collectAsState()
            val isAppCreatorLeader by viewModel.isAppCreatorLeader.collectAsState()
            val hasFullBasilianAccess = isBasilianPrivateAccess || isCuriaAdmin
            val hasLeaderAccess = isAppCreatorLeader &&
                com.example.data.security.BasilianSecurityManager.isAppCreatorLeader(this@MainActivity, userProfile?.email)
            val isAuthenticated = userProfile != null && !userProfile?.email.isNullOrBlank()

            val introPrefs = remember { getSharedPreferences("basilian_prefs", MODE_PRIVATE) }
            var showThemeSelector by remember { mutableStateOf(false) }
            var showLanguageSelector by remember { mutableStateOf(false) }
            var showUserAccountDialog by remember { mutableStateOf(false) }
            var showIntroVideoAfterLogin by remember {
                mutableStateOf(
                    isAuthenticated && !introPrefs.getBoolean("basilian_intro_cinema_v4_shown", false)
                )
            }

            // Auto-redirección si la pestaña activa es ocultada por orden de la IA
            LaunchedEffect(currentTab, showTabOracion, showTabLiturgia, showTabCumpleanos, showTabHistoria, showTabRecursos, hasLeaderAccess) {
                val isCurrentTabHidden = when (currentTab) {
                    AppTab.ORACION_DIARIA, AppTab.REZANDO_HOY, AppTab.INICIO, AppTab.ESPIRITUALIDAD_NOMADA -> !showTabOracion
                    AppTab.LITURGIA_HORAS, AppTab.COMUNIDAD, AppTab.GUIA_ESPIRITUAL -> !showTabLiturgia
                    AppTab.DIRECTORIO -> !showTabCumpleanos
                    AppTab.HISTORIA_IA -> !showTabHistoria
                    AppTab.PORTAL_CURIA -> !hasLeaderAccess
                    AppTab.SANTUARIO_AR -> false
                    else -> !showTabRecursos
                }
                if (isCurrentTabHidden) {
                    when {
                        showTabOracion -> viewModel.selectTab(AppTab.ORACION_DIARIA)
                        showTabLiturgia -> viewModel.selectTab(AppTab.LITURGIA_HORAS)
                        showTabCumpleanos -> viewModel.selectTab(AppTab.DIRECTORIO)
                        showTabHistoria -> viewModel.selectTab(AppTab.HISTORIA_IA)
                        showTabRecursos -> viewModel.selectTab(AppTab.RECURSOS)
                        hasLeaderAccess -> viewModel.selectTab(AppTab.PORTAL_CURIA)
                        else -> viewModel.selectTab(AppTab.ORACION_DIARIA)
                    }
                }
            }

            MyApplicationTheme(appTheme = appTheme) {
                CompositionLocalProvider(
                    LocalCustomCrestUri provides customCrestUri,
                    com.example.ui.components.LocalFuturistic3DIntensity provides futuristic3DIntensity
                ) {
                    // Request notification permission on Android 13+ (API 33+)
                    val permissionLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestPermission()
                    ) { _ -> }

                    LaunchedEffect(Unit) {
                        try {
                            viewModel.syncAllNotificationAlarms(this@MainActivity)
                        } catch (_: Throwable) {}

                        val navigateTo = try {
                            intent?.getStringExtra("NAVIGATE_TO_TAB")
                        } catch (_: Throwable) {
                            null
                        }
                        if (navigateTo == "DIRECTORIO") {
                            viewModel.selectTab(AppTab.DIRECTORIO)
                        } else if (navigateTo == "ORACION_DIARIA") {
                            viewModel.selectTab(AppTab.ORACION_DIARIA)
                        } else if (navigateTo == "RECURSOS") {
                            viewModel.selectTab(AppTab.RECURSOS)
                        } else if (navigateTo == "PORTAL_CURIA" && hasLeaderAccess) {
                            viewModel.selectTab(AppTab.PORTAL_CURIA)
                        }
                    }

                    LaunchedEffect(isAuthenticated) {
                        if (!isAuthenticated) {
                            showThemeSelector = false
                            showLanguageSelector = false
                            showUserAccountDialog = false
                            showIntroVideoAfterLogin = false
                            introPrefs.edit().putBoolean("basilian_intro_cinema_v4_shown", false).apply()
                        } else {
                            if (!introPrefs.getBoolean("basilian_intro_cinema_v4_shown", false)) {
                                showIntroVideoAfterLogin = true
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                kotlinx.coroutines.delay(500)
                                if (ContextCompat.checkSelfPermission(
                                        this@MainActivity,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                        }
                    }

                    if (isAuthenticated && showThemeSelector) {
                        ThemeSelectorDialog(
                            currentTheme = appTheme,
                            onThemeSelected = { newTheme ->
                                viewModel.setAppTheme(newTheme)
                                showThemeSelector = false
                            },
                            onDismiss = { showThemeSelector = false }
                        )
                    }

                    if (isAuthenticated && showLanguageSelector) {
                        com.example.ui.components.LanguageSelectorDialog(
                            currentLanguage = appLanguage,
                            onLanguageSelected = { newLang ->
                                viewModel.setAppLanguage(newLang)
                                showLanguageSelector = false
                            },
                            onDismiss = { showLanguageSelector = false }
                        )
                    }

                    if (isAuthenticated && showUserAccountDialog) {
                        UserLinkedAccountDialog(
                            viewModel = viewModel,
                            onDismiss = { showUserAccountDialog = false },
                            onReplayIntroVideo = {
                                showUserAccountDialog = false
                                showIntroVideoAfterLogin = true
                            }
                        )
                    }

                    if (!isAuthenticated) {
                        EmailAccessGateScreen(
                            viewModel = viewModel,
                            onAccessGranted = {
                                introPrefs.edit().putBoolean("basilian_intro_cinema_v4_shown", false).apply()
                                showIntroVideoAfterLogin = true
                                viewModel.selectTab(AppTab.ORACION_DIARIA)
                            }
                        )
                    } else if (showIntroVideoAfterLogin) {
                        BasilianFuturisticIntroVideoScreen(
                            userName = userProfile?.fullName.orEmpty(),
                            viewModel = viewModel,
                            onVideoFinished = {
                                introPrefs.edit().putBoolean("basilian_intro_cinema_v4_shown", true).apply()
                                showIntroVideoAfterLogin = false
                            }
                        )
                    } else {
                        HolySpiritGlobalBackground(
                            mode = globalBgMode,
                            customImageUri = globalCustomUri,
                            opacity = globalOpacity,
                            appTheme = appTheme
                        ) {
                            Scaffold(
                                containerColor = Color.Transparent,
                                contentWindowInsets = WindowInsets.safeDrawing,
                                topBar = {
                                    BasilianTopBar(
                                        currentTab = currentTab,
                                        isSpeaking = isSpeaking,
                                        onStopAudio = { viewModel.stopSpeaking() },
                                        onReturnHome = {
                                            viewModel.selectTab(AppTab.ORACION_DIARIA)
                                            viewModel.refreshBasilianCorpusData()
                                            android.widget.Toast.makeText(
                                                this@MainActivity,
                                                "🛡️ Inicio: Información de los Basilianos actualizada desde los PDF oficiales",
                                                android.widget.Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        onOpenCuriaPortal = {
                                            if (hasLeaderAccess) {
                                                viewModel.selectTab(AppTab.PORTAL_CURIA)
                                            }
                                        },
                                        onOpenThemeSelector = { showThemeSelector = true },
                                        onOpenLanguageSelector = { showLanguageSelector = true },
                                        onOpenUserAccount = { showUserAccountDialog = true },
                                        onLogoutUser = {
                                            showThemeSelector = false
                                            showLanguageSelector = false
                                            showUserAccountDialog = false
                                            viewModel.stopSpeaking()
                                            viewModel.logoutUserProfile()
                                            android.widget.Toast.makeText(
                                                this@MainActivity,
                                                "🚪 Has salido de tu cuenta. Ingresa tu correo para volver a entrar.",
                                                android.widget.Toast.LENGTH_LONG
                                            ).show()
                                        },
                                        onCycle3DIntensity = {
                                            viewModel.cycleFuturistic3DIntensity()
                                            val modeLabel = viewModel.futuristic3DIntensity.value.label
                                            android.widget.Toast.makeText(
                                                this@MainActivity,
                                                "🌐 Motor Visual: $modeLabel activado en toda la app",
                                                android.widget.Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        userName = userProfile?.fullName.orEmpty(),
                                        userEmail = userProfile?.email.orEmpty(),
                                        userPhotoUri = userProfile?.profilePhotoUri,
                                        appTheme = appTheme,
                                        appLanguage = appLanguage,
                                        isBasilianAccess = hasLeaderAccess
                                    )
                                },
                                bottomBar = {
                                    BasilianNavBar(
                                        currentTab = currentTab,
                                        onTabSelected = { tab -> viewModel.selectTab(tab) },
                                        appTheme = appTheme,
                                        appLanguage = appLanguage,
                                        showOracion = showTabOracion,
                                        showLiturgia = showTabLiturgia,
                                        showCumpleanos = showTabCumpleanos,
                                        showHistoria = showTabHistoria,
                                        showRecursos = showTabRecursos,
                                        isBasilianAccess = hasFullBasilianAccess
                                    )
                                }
                            ) { innerPadding ->
                                val incomingNotice by viewModel.incomingLiveUpdateNotice.collectAsState()
                                val verifiedUpdateUrl by viewModel.otaDownloadUrl.collectAsState()

                                androidx.compose.foundation.layout.Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                        .consumeWindowInsets(innerPadding)
                                        .imePadding()
                                ) {
                                    AnimatedVisibility(visible = !incomingNotice.isNullOrBlank()) {
                                        Surface(
                                            color = Color(0xFF042F2E),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            androidx.compose.foundation.layout.Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                                            ) {
                                                androidx.compose.material3.Text(
                                                    text = incomingNotice.orEmpty(),
                                                    color = Color(0xFFECFDF5),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                if (verifiedUpdateUrl.isNotBlank()) {
                                                    androidx.compose.material3.TextButton(
                                                        onClick = {
                                                            try {
                                                                val intent = android.content.Intent(
                                                                    android.content.Intent.ACTION_VIEW,
                                                                    android.net.Uri.parse(verifiedUpdateUrl)
                                                                )
                                                                startActivity(intent)
                                                            } catch (_: Exception) {}
                                                        }
                                                    ) {
                                                        androidx.compose.material3.Text(
                                                            text = "Abrir Link",
                                                            color = Color(0xFFFDE047)
                                                        )
                                                    }
                                                }
                                                androidx.compose.material3.TextButton(
                                                    onClick = { viewModel.dismissIncomingLiveUpdateNotice() }
                                                ) {
                                                    androidx.compose.material3.Text(
                                                        text = "Entendido",
                                                        color = Color(0xFF34D399)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .weight(1f)
                                    ) {
                                        AnimatedContent(
                                            targetState = currentTab,
                                            modifier = Modifier.fillMaxSize(),
                                            transitionSpec = {
                                                fadeIn() togetherWith fadeOut()
                                            },
                                            label = "tab_transition"
                                        ) { tab ->
                                            when (tab) {
                                                AppTab.ORACION_DIARIA, AppTab.REZANDO_HOY, AppTab.INICIO, AppTab.ESPIRITUALIDAD_NOMADA -> RezandoHoyScreen(viewModel = viewModel, appLanguage = appLanguage)
                                                AppTab.LITURGIA_HORAS, AppTab.COMUNIDAD -> LiturgiaHorasScreen(viewModel = viewModel, appLanguage = appLanguage)
                                                AppTab.DIRECTORIO -> DirectorioCumpleanosScreen(viewModel = viewModel, appLanguage = appLanguage)
                                                AppTab.HISTORIA_IA, AppTab.GUIA_ESPIRITUAL -> HistoriaCreativaScreen(
                                                    viewModel = viewModel,
                                                    appLanguage = appLanguage,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                                AppTab.SANTUARIO_AR -> SantuarioArScreen(viewModel = viewModel)
                                                AppTab.PORTAL_CURIA -> {
                                                    if (hasLeaderAccess) {
                                                        PortalCuriaScreen(viewModel = viewModel)
                                                    } else {
                                                        LaunchedEffect(Unit) {
                                                            viewModel.selectTab(AppTab.ORACION_DIARIA)
                                                        }
                                                        RezandoHoyScreen(viewModel = viewModel, appLanguage = appLanguage)
                                                    }
                                                }
                                                else -> RecursosUnifiedScreen(
                                                    viewModel = viewModel,
                                                    activeAppTab = tab,
                                                    appLanguage = appLanguage
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
    }
}
}
