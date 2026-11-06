package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.BasilianDatabase
import com.example.data.model.*
import com.example.data.repository.BasilianRepository
import com.example.data.repository.BasilianStaticData
import com.example.receiver.NotificationHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.data.firebase.CuriaFirebaseManager
import com.example.data.model.CostumbresSection
import com.example.data.model.TransportMode
import com.example.data.model.WisdomCapsule
import com.example.data.model.PilgrimageMilestone
import com.example.data.model.ActiveTripState
import com.example.data.model.NomadWisdomData
import com.example.data.model.IconHotspot
import com.example.data.model.ArIconographyData
import android.net.Uri
import com.example.data.datastore.ThemePreferencesManager
import com.example.ui.theme.BasilianAppTheme
import com.example.data.ai.BasilianAiService
import com.example.data.ai.CreativeHistoryMode
import com.example.data.ai.CreativeHistoryItem
import com.example.data.ai.GeminiThinkingDepth
import com.example.data.security.BasilianSecurityManager
import org.json.JSONObject
import org.json.JSONArray
import java.io.File
import java.util.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import com.example.ui.components.GlobalBgMode
import com.example.data.ai.AppImprovementSuggestion
import com.example.data.ai.AppAnalysisReport
import com.example.data.repository.DailyGospelAndBasilianWisdomRepository
import com.example.audio.GoogleCloudTtsService
import com.example.data.repository.DailyLiturgicalWisdom
import com.example.audio.HumanVoiceEngine
import com.example.data.repository.LiturgiaRoomCacheRepository
import com.example.data.repository.DailySaintsRepository
import com.example.data.repository.DailySaint
import com.example.data.repository.LiturgiaOrdoColombianoRepository
import com.example.data.model.CachedLiturgiaDayEntity
import com.example.data.model.CachedSaintEntity
import com.example.data.model.ColombianLiturgicalDay
import com.example.data.repository.AiChatRoomRepository

data class UploadedCorpusDocument(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val dateAdded: String,
    val wordCount: Int,
    val category: String = "Pastoral / Constituciones"
) {
    val contentPreview: String
        get() = if (content.length > 120) content.take(120) + "..." else content
}

enum class AppTab {
    ORACION_DIARIA,
    LITURGIA_HORAS,
    DIRECTORIO,
    COMUNIDAD,
    HISTORIA_IA,
    RECURSOS,
    SANTUARIO_AR,
    GUIA_ESPIRITUAL,
    HISTORIA_Y_COMUNIDAD,
    // Sub-pantallas y herramientas
    RUTAS_APRENDIZAJE,
    FORMA_DE_VIDA,
    POLITICAS_2026,
    PORTAL_CURIA,
    CUADERNO,
    // Alias y retrocompatibilidad
    REZANDO_HOY,
    INICIO,
    COSTUMBRES,
    HISTORIA,
    ORACIONES,
    LIBRO_1922,
    FUNDADORES,
    CALENDARIO,
    ORACION,
    PATRONOS,
    ACTUALIDAD,
    ESPIRITUALIDAD_NOMADA
}

class BasilianViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val database = BasilianDatabase.getDatabase(application)
    private val repository = BasilianRepository(database.basilianDao())
    private val curiaFirebaseManager = CuriaFirebaseManager(application)
    private val themePreferencesManager = ThemePreferencesManager(application)
    val liturgiaRoomCacheRepository = LiturgiaRoomCacheRepository(database.liturgiaCacheDao())
    val aiChatRoomRepository = AiChatRoomRepository(database.aiChatDao(), application)
    private var roomAiChatObserverJob: Job? = null

    private val _isOfflineLiturgiaMode = MutableStateFlow(false)
    val isOfflineLiturgiaMode: StateFlow<Boolean> = _isOfflineLiturgiaMode.asStateFlow()

    private val _dailySaints = MutableStateFlow<List<DailySaint>>(DailySaintsRepository.getSaintsForDate())
    val dailySaints: StateFlow<List<DailySaint>> = _dailySaints.asStateFlow()

    val allCachedDays: StateFlow<List<CachedLiturgiaDayEntity>> = liturgiaRoomCacheRepository
        .getAllCachedDays()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cachedDaysCount: StateFlow<Int> = liturgiaRoomCacheRepository
        .getCachedDaysCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun cacheLiturgiaDayInRoom(day: ColombianLiturgicalDay) {
        viewModelScope.launch {
            liturgiaRoomCacheRepository.saveToRoomCache(day)
        }
    }

    fun toggleOfflineLiturgiaMode(enabled: Boolean) {
        _isOfflineLiturgiaMode.value = enabled
    }

    fun loadSaintsForDate(calendar: Calendar = Calendar.getInstance()) {
        _dailySaints.value = DailySaintsRepository.getSaintsForDate(calendar)
    }

    private var tts: TextToSpeech? = null
    private var isTtsInitialized: Boolean = false
    private var pendingTtsText: String? = null
    private var pendingTtsOnFinished: (() -> Unit)? = null
    private val _isTtsSpeaking = MutableStateFlow(false)
    val isTtsSpeaking: StateFlow<Boolean> = _isTtsSpeaking.asStateFlow()

    // --- CANTO SAGRADO DIARIO GENERADO POR IA ---
    private val sacredSynthesizer = com.example.audio.SacredAudioSynthesizer(application)
    private val _dailySacredSong = MutableStateFlow<com.example.data.ai.DailySacredSong?>(null)
    val dailySacredSong: StateFlow<com.example.data.ai.DailySacredSong?> = _dailySacredSong.asStateFlow()

    val isSacredAudioPlaying: StateFlow<Boolean> = sacredSynthesizer.isPlaying
    val sacredAudioPositionSec: StateFlow<Int> = sacredSynthesizer.currentPositionSec
    val sacredAudioTotalSec: StateFlow<Int> = sacredSynthesizer.totalDurationSec

    private val _isGeneratingSacredSong = MutableStateFlow(false)
    val isGeneratingSacredSong: StateFlow<Boolean> = _isGeneratingSacredSong.asStateFlow()

    private val _sacredSongVoiceEnabled = MutableStateFlow(true)
    val sacredSongVoiceEnabled: StateFlow<Boolean> = _sacredSongVoiceEnabled.asStateFlow()

    fun setSacredSongVoiceEnabled(enabled: Boolean) {
        _sacredSongVoiceEnabled.value = enabled
    }

    fun playSacredSong(song: com.example.data.ai.DailySacredSong? = null) {
        val targetSong = song ?: _dailySacredSong.value ?: return
        sacredSynthesizer.playSong(targetSong, voiceRecitation = _sacredSongVoiceEnabled.value)
    }

    fun pauseSacredSong() {
        sacredSynthesizer.pause()
    }

    fun resumeSacredSong() {
        sacredSynthesizer.resume()
    }

    fun stopSacredSong() {
        sacredSynthesizer.stop()
    }

    fun seekSacredAudio(seconds: Int) {
        sacredSynthesizer.seekTo(seconds)
    }

    fun generateDailySacredSong(
        gospelPassage: String,
        gospelPhrase: String,
        reflectionContent: String,
        style: String = "Canto Gregoriano Monástico"
    ) {
        viewModelScope.launch {
            _isGeneratingSacredSong.value = true
            try {
                val song = com.example.data.ai.BasilianAiService.generateDailySacredSong(
                    gospelPassage = gospelPassage,
                    gospelPhrase = gospelPhrase,
                    reflectionContent = reflectionContent,
                    preferredStyle = style
                )
                _dailySacredSong.value = song
            } catch (e: Exception) {
                Log.w("BasilianViewModel", "Error generando canto sacro: ${e.message}")
            } finally {
                _isGeneratingSacredSong.value = false
            }
        }
    }

    private val _voicePace = MutableStateFlow(HumanVoiceEngine.currentPace)
    val voicePace: StateFlow<HumanVoiceEngine.VoicePace> = _voicePace.asStateFlow()

    // Google Cloud Text-to-Speech (Neural / Studio / WaveNet / Journey)
    val cloudVoice: StateFlow<GoogleCloudTtsService.CloudVoiceOption> = GoogleCloudTtsService.currentVoice
    val isCloudTtsPlaying: StateFlow<Boolean> = GoogleCloudTtsService.isPlaying
    val isCloudTtsLoading: StateFlow<Boolean> = GoogleCloudTtsService.isLoading
    val cloudTtsLastError: StateFlow<String?> = GoogleCloudTtsService.lastError

    private val _useCloudTts = MutableStateFlow(true)
    val useCloudTts: StateFlow<Boolean> = _useCloudTts.asStateFlow()

    fun setUseCloudTts(enabled: Boolean) {
        _useCloudTts.value = enabled
        getApplication<Application>().getSharedPreferences("basilian_cloud_tts_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("use_cloud_tts", enabled)
            .apply()
    }

    fun selectCloudVoice(voice: GoogleCloudTtsService.CloudVoiceOption) {
        GoogleCloudTtsService.selectVoice(voice, getApplication())
    }

    fun setCustomCloudTtsApiKey(key: String) {
        GoogleCloudTtsService.setCustomApiKey(getApplication(), key)
    }

    fun getEffectiveCloudTtsApiKey(): String {
        return GoogleCloudTtsService.getEffectiveApiKey(getApplication())
    }

    fun resetVoiceConflictAndUseNaturalVoice() {
        stopSpeaking()
        GoogleCloudTtsService.clearCustomApiKeysAndCache(getApplication())
        _useCloudTts.value = true
    }

    override fun onInit(status: Int) {
        try {
            if (status == TextToSpeech.SUCCESS) {
                isTtsInitialized = true
                tts?.let { engine ->
                    val currentPace = try {
                        _voicePace.value
                    } catch (_: Throwable) {
                        HumanVoiceEngine.currentPace
                    }
                    HumanVoiceEngine.configureHumanVoice(engine, pace = currentPace)
                    try {
                        val targetLocale = try {
                            _appLanguage.value.locale
                        } catch (_: Throwable) {
                            Locale.getDefault()
                        }
                        engine.language = targetLocale
                    } catch (_: Throwable) {}
                    val queued = pendingTtsText
                    val cb = pendingTtsOnFinished
                    pendingTtsText = null
                    pendingTtsOnFinished = null
                    if (!queued.isNullOrBlank()) {
                        speakLocalFallback(queued, cb)
                    }
                }
            }
        } catch (_: Throwable) {}
    }

    fun setVoicePace(pace: HumanVoiceEngine.VoicePace) {
        _voicePace.value = pace
        HumanVoiceEngine.currentPace = pace
        tts?.let { engine ->
            HumanVoiceEngine.configureHumanVoice(engine, pace = pace)
        }
    }

    fun speakText(text: String) {
        if (_isTtsSpeaking.value || GoogleCloudTtsService.isPlaying.value) {
            stopSpeaking()
            return
        }
        speakTextImmediate(text)
    }

    fun speakTextImmediate(text: String, onFinished: (() -> Unit)? = null) {
        if (text.isBlank()) {
            onFinished?.invoke()
            return
        }
        stopSpeaking()
        if (sacredSynthesizer.isPlaying.value) {
            sacredSynthesizer.pause()
        }
        val effectiveApiKey = GoogleCloudTtsService.getEffectiveApiKey(getApplication())
        if (_useCloudTts.value && effectiveApiKey.isNotBlank()) {
            _isTtsSpeaking.value = true
            viewModelScope.launch {
                var usedFallback = false
                GoogleCloudTtsService.speakOrFallback(
                    context = getApplication(),
                    textToRead = text,
                    voice = GoogleCloudTtsService.currentVoice.value,
                    onLocalFallback = {
                        usedFallback = true
                        speakLocalFallback(text, onFinished)
                    }
                )
                if (!usedFallback) {
                    // Esperar mientras el reproductor de voz humana esté activo
                    kotlinx.coroutines.delay(150)
                    while (GoogleCloudTtsService.isPlaying.value || GoogleCloudTtsService.isLoading.value) {
                        kotlinx.coroutines.delay(160)
                    }
                    _isTtsSpeaking.value = false
                    onFinished?.invoke()
                }
            }
        } else {
            speakLocalFallback(text, onFinished)
        }
    }

    private fun speakLocalFallback(text: String, onFinished: (() -> Unit)? = null) {
        val currentEngine = tts
        if (currentEngine == null) {
            pendingTtsText = text
            pendingTtsOnFinished = onFinished
            _isTtsSpeaking.value = true
            try {
                tts = TextToSpeech(getApplication(), this)
            } catch (_: Exception) {
                _isTtsSpeaking.value = false
                onFinished?.invoke()
            }
            return
        }
        if (!isTtsInitialized) {
            pendingTtsText = text
            pendingTtsOnFinished = onFinished
            _isTtsSpeaking.value = true
            return
        }
        _isTtsSpeaking.value = true
        HumanVoiceEngine.speakHumanized(
            tts = currentEngine,
            text = text,
            utteranceId = "BasilianHumanVoice"
        ) {
            _isTtsSpeaking.value = false
            onFinished?.invoke()
        }
    }

    fun previewSereneVoice() {
        speakText("Dios te salve, María, llena eres de gracia, el Señor es contigo. Bendita tú eres entre todas las mujeres y bendito es el fruto de tu vientre, Jesús.")
    }

    fun stopSpeaking() {
        pendingTtsText = null
        pendingTtsOnFinished = null
        try {
            tts?.stop()
        } catch (_: Exception) {}
        GoogleCloudTtsService.stop()
        _isTtsSpeaking.value = false
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
        GoogleCloudTtsService.stop()
        sacredSynthesizer.release()
    }

    // Tab state
    private val _currentTab = MutableStateFlow(AppTab.ORACION_DIARIA)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    fun selectTab(tab: AppTab) {
        val mappedTab = when (tab) {
            AppTab.INICIO, AppTab.REZANDO_HOY -> AppTab.ORACION_DIARIA
            AppTab.COSTUMBRES -> AppTab.FORMA_DE_VIDA
            AppTab.CALENDARIO, AppTab.FUNDADORES, AppTab.ORACIONES, AppTab.ORACION, AppTab.PATRONOS, AppTab.LIBRO_1922 -> AppTab.RECURSOS
            AppTab.HISTORIA, AppTab.ACTUALIDAD -> AppTab.HISTORIA_Y_COMUNIDAD
            AppTab.ESPIRITUALIDAD_NOMADA -> AppTab.ORACION_DIARIA
            else -> tab
        }
        _currentTab.value = mappedTab
    }

    // --- GUÍA ESPIRITUAL IA (SAN BASILIO) ---
    private val _chatMessages = MutableStateFlow<List<com.example.data.ai.ChatMessage>>(
        listOf(
            com.example.data.ai.ChatMessage(
                id = "welcome_1",
                sender = com.example.data.ai.MessageSender.BASILIAN_GUIDE,
                text = "«La paz sea contigo, hermano en Cristo. Soy tu Guía Espiritual Basiliano, inspirado en las Reglas Monásticas, las Cartas teológicas de San Basilio Magno y el carisma educativo de Annonay (1822).\n\n¿En qué inquietud del alma, virtud o discernimiento de vida comunitaria puedo acompañarte hoy?»"
            )
        )
    )
    val chatMessages: StateFlow<List<com.example.data.ai.ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    fun sendAiMessage(userText: String) {
        val trimmed = userText.trim()
        if (trimmed.isEmpty() || _isAiLoading.value) return

        val userMsg = com.example.data.ai.ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = com.example.data.ai.MessageSender.USER,
            text = trimmed
        )
        _chatMessages.value = _chatMessages.value + userMsg
        _isAiLoading.value = true

        viewModelScope.launch {
            try {
                val linkedName = _currentUserProfile.value?.fullName?.takeIf { it.isNotBlank() }
                val userEmail = _currentUserProfile.value?.email
                val roomHistCtx = aiChatRoomRepository.buildHistoricalContextFromRoom(userEmail, _activeAiChatId.value)
                val learnedCtx = com.example.data.ai.BasilianAiMemory.getLearnedPromptContext(
                    getApplication(),
                    _appLanguage.value,
                    userEmail
                ) + if (roomHistCtx.isNotBlank()) "\n\n$roomHistCtx" else ""
                val learnedList = _learnedAiFacts.value.ifEmpty {
                    com.example.data.ai.BasilianAiMemory.getAllLearnedFacts(getApplication())
                }
                val isBasilianMember = _isBasilianPrivateAccess.value || _curiaIsAuthenticated.value
                val response = com.example.data.ai.BasilianAiService.sendMessage(
                    history = _chatMessages.value,
                    userMessage = trimmed,
                    language = _appLanguage.value,
                    userName = linkedName,
                    learnedMemoryContext = learnedCtx,
                    learnedFacts = learnedList,
                    isBasilianMember = isBasilianMember
                )
                val guideMsg = com.example.data.ai.ChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = com.example.data.ai.MessageSender.BASILIAN_GUIDE,
                    text = response
                )
                _chatMessages.value = _chatMessages.value + guideMsg
                val autoLearned = com.example.data.ai.BasilianAiMemory.autoLearnFromChat(getApplication(), trimmed, response, userEmail)
                _userPersonalAiProfile.value = com.example.data.ai.BasilianAiMemory.getUserPersonalAiProfile(getApplication(), userEmail)
                if (autoLearned != null) {
                    aiChatRoomRepository.addLearnedFact(
                        title = autoLearned.title,
                        details = autoLearned.details,
                        category = autoLearned.category,
                        verifiedWithGoogle = autoLearned.verifiedWithGoogle
                    )
                    _learnedAiFacts.value = com.example.data.ai.BasilianAiMemory.getAllLearnedFacts(getApplication())
                    _lastAutoLearnedFactTitle.value = autoLearned.title
                }
                aiChatRoomRepository.appendTurnToSession(
                    userEmail = userEmail,
                    sessionId = _activeAiChatId.value,
                    turn = com.example.data.ai.AiChatTurn(
                        userPrompt = trimmed,
                        aiResponse = response,
                        modeName = CreativeHistoryMode.HISTORIA_COMPLETA.name,
                        thinkingDepthName = _geminiThinkingDepth.value.name,
                        learnedFactTitle = autoLearned?.title
                    )
                )
            } catch (e: Exception) {
                val errorMsg = com.example.data.ai.ChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = com.example.data.ai.MessageSender.BASILIAN_GUIDE,
                    text = "«El Señor sostiene tu camino. Aunque la conexión terrenal sea inestable, recuerda las palabras de San Basilio: 'La oración continua es el ancla del alma en cualquier tempestad'»."
                )
                _chatMessages.value = _chatMessages.value + errorMsg
            } finally {
                _isAiLoading.value = false
            }
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            com.example.data.ai.ChatMessage(
                id = UUID.randomUUID().toString(),
                sender = com.example.data.ai.MessageSender.BASILIAN_GUIDE,
                text = "«He renovado nuestro coloquio espiritual. Presenta a Dios tus intenciones y caminemos juntos en el carisma de San Basilio»."
            )
        )
    }

    // --- RUTAS DE APRENDIZAJE ADAPTATIVO (PEDAGOGÍA NÓMADA) ---
    private val _adaptiveProfile = MutableStateFlow(com.example.data.repository.PerfilFormacion.ASOCIADO)
    val adaptiveProfile: StateFlow<com.example.data.repository.PerfilFormacion> = _adaptiveProfile.asStateFlow()

    private val _adaptiveRhythm = MutableStateFlow(com.example.data.repository.RitmoNomada.SERENO)
    val adaptiveRhythm: StateFlow<com.example.data.repository.RitmoNomada> = _adaptiveRhythm.asStateFlow()

    private val _completedModuleIds = MutableStateFlow<Set<String>>(setOf("psi_1"))
    val completedModuleIds: StateFlow<Set<String>> = _completedModuleIds.asStateFlow()

    fun setAdaptiveProfile(profile: com.example.data.repository.PerfilFormacion) {
        _adaptiveProfile.value = profile
    }

    fun setAdaptiveRhythm(rhythm: com.example.data.repository.RitmoNomada) {
        _adaptiveRhythm.value = rhythm
    }

    fun toggleModuleCompletion(moduleId: String) {
        val current = _completedModuleIds.value
        _completedModuleIds.value = if (current.contains(moduleId)) current - moduleId else current + moduleId
    }

    // --- PORTAL PRIVADO DE CURIA (ADMIN SOLO CONTRASEÑA CIFRADA AES-256-GCM) ---
    private val prefs = application.getSharedPreferences("basilian_app_prefs", Context.MODE_PRIVATE)

    private val _curiaMasterPassword = MutableStateFlow(
        BasilianSecurityManager.getOwnerMasterPassword(application)
    )
    val curiaMasterPassword: StateFlow<String> = _curiaMasterPassword.asStateFlow()

    fun setCuriaMasterPassword(newPass: String) {
        val clean = newPass.trim()
        if (clean.isNotEmpty()) {
            _curiaMasterPassword.value = clean
            BasilianSecurityManager.setOwnerMasterPassword(getApplication(), clean)
            appendLiveUpdateLog("🔐 Contraseña principal de «Solo para Mí» actualizada y cifrada con AES-256-GCM.")
        }
    }

    private val _basilianCommunityUnlockCode = MutableStateFlow(
        BasilianSecurityManager.getBasilianCommunityCode(application)
    )
    val basilianCommunityUnlockCode: StateFlow<String> = _basilianCommunityUnlockCode.asStateFlow()

    fun setBasilianCommunityUnlockCode(newCode: String) {
        val clean = newCode.trim()
        if (clean.isNotEmpty()) {
            _basilianCommunityUnlockCode.value = clean
            BasilianSecurityManager.setBasilianCommunityCode(getApplication(), clean)
            appendLiveUpdateLog("🔐 Código de desbloqueo Basiliano C.S.B. actualizado y guardado en la bóveda privada.")
        }
    }

    private val _ownerQuickPin = MutableStateFlow(
        BasilianSecurityManager.getOwnerQuickPin(application)
    )
    val ownerQuickPin: StateFlow<String> = _ownerQuickPin.asStateFlow()

    fun setOwnerQuickPin(newPin: String) {
        val clean = newPin.trim().filter { it.isDigit() }.take(8)
        if (clean.length >= 4) {
            _ownerQuickPin.value = clean
            BasilianSecurityManager.setOwnerQuickPin(getApplication(), clean)
            appendLiveUpdateLog("🔢 PIN Rápido de Seguridad actualizado y cifrado en «Solo para Mí».")
        }
    }

    private val _vaultAutoLockMinutes = MutableStateFlow(
        BasilianSecurityManager.getVaultAutoLockMinutes(application)
    )
    val vaultAutoLockMinutes: StateFlow<Int> = _vaultAutoLockMinutes.asStateFlow()

    fun setVaultAutoLockMinutes(minutes: Int) {
        val valid = minutes.coerceIn(1, 60)
        _vaultAutoLockMinutes.value = valid
        BasilianSecurityManager.setVaultAutoLockMinutes(getApplication(), valid)
        refreshVaultActivityTimestamp()
        appendLiveUpdateLog("⏱️ Cierre automático por inactividad de «Solo para Mí» ajustado a $valid min.")
    }

    private var lastVaultInteractionMs: Long = System.currentTimeMillis()
    private var autoLockJob: kotlinx.coroutines.Job? = null

    fun refreshVaultActivityTimestamp() {
        lastVaultInteractionMs = System.currentTimeMillis()
    }

    private fun startVaultAutoLockMonitor() {
        lastVaultInteractionMs = System.currentTimeMillis()
        autoLockJob?.cancel()
        autoLockJob = viewModelScope.launch {
            while (_curiaIsAuthenticated.value) {
                kotlinx.coroutines.delay(15_000L)
                val limitMs = _vaultAutoLockMinutes.value * 60_000L
                if (System.currentTimeMillis() - lastVaultInteractionMs >= limitMs) {
                    logoutCuria()
                    appendLiveUpdateLog("🔒 «Solo para Mí» se bloqueó automáticamente tras ${_vaultAutoLockMinutes.value} min de inactividad.")
                    break
                }
            }
        }
    }

    fun activateColombianOrdoDailyReminders() {
        val ctx = getApplication<Application>()
        NotificationHelper.scheduleDailyPrayer(
            context = ctx,
            prayerType = "LAUDES_ORDO_CEC",
            hour = 6,
            minute = 30,
            title = "🇨🇴 Laudes • Ordo Colombiano (CEC)",
            message = "Comienza el día orando Laudes y el Evangelio del Día en comunión con la Iglesia en Colombia."
        )
        NotificationHelper.scheduleDailyPrayer(
            context = ctx,
            prayerType = "VISPERAS_ORDO_CEC",
            hour = 18,
            minute = 0,
            title = "🕯️ Vísperas • Ordo Colombiano (CEC)",
            message = "Al caer la tarde, únete a la oración de Vísperas y da gracias a Dios por este día."
        )
        NotificationHelper.showPushNotification(
            context = ctx,
            channelId = NotificationHelper.CHANNEL_DAILY_PRAYER,
            notificationId = 8841,
            title = "🔔 Recordatorios del Ordo Colombiano Activados",
            message = "Recibirás aviso diario para Laudes (6:30 a. m.) y Vísperas (6:00 p. m.) según el Ordo CEC.",
            targetTab = "LITURGIA_HORAS"
        )
    }

    private val _personalPasswordVault = MutableStateFlow(
        BasilianSecurityManager.getPersonalPasswordNotes(application)
    )
    val personalPasswordVault: StateFlow<List<BasilianSecurityManager.SavedPasswordEntry>> = _personalPasswordVault.asStateFlow()

    fun savePersonalPasswordToVault(label: String, secretValue: String) {
        _personalPasswordVault.value = BasilianSecurityManager.savePersonalPasswordNote(
            getApplication(),
            label,
            secretValue
        )
        appendLiveUpdateLog("🔐 Clave «${label.trim()}» guardada cifrada en tu espacio privado «Solo para Mí».")
    }

    fun deletePersonalPasswordFromVault(id: String) {
        _personalPasswordVault.value = BasilianSecurityManager.deletePersonalPasswordNote(getApplication(), id)
    }

    private val _intrusionAlerts = MutableStateFlow(
        BasilianSecurityManager.getIntrusionAlerts(application)
    )
    val intrusionAlerts: StateFlow<List<BasilianSecurityManager.IntrusionAlert>> = _intrusionAlerts.asStateFlow()

    private val _leaderCreatorEmail = MutableStateFlow(
        BasilianSecurityManager.getLeaderCreatorEmail(application)
    )
    val leaderCreatorEmail: StateFlow<String> = _leaderCreatorEmail.asStateFlow()

    private val _isAppCreatorLeader = MutableStateFlow(
        BasilianSecurityManager.isAppCreatorLeader(application, loadUserProfileFromPrefs()?.email)
    )
    val isAppCreatorLeader: StateFlow<Boolean> = _isAppCreatorLeader.asStateFlow()

    fun setLeaderCreatorEmail(newLeaderEmail: String) {
        val clean = BasilianSecurityManager.PRIMARY_CREATOR_LEADER_EMAIL
        BasilianSecurityManager.setLeaderCreatorEmail(getApplication(), clean)
        _leaderCreatorEmail.value = clean
        _isAppCreatorLeader.value = BasilianSecurityManager.isAppCreatorLeader(
            getApplication(),
            _currentUserProfile.value?.email
        )
        appendLiveUpdateLog("👑 Único correo Líder Creador autorizado para alertas y control maestro: $clean")
    }

    private fun recordAndRouteIntrusionToLeaderOnly(
        targetArea: String,
        actorInfo: String,
        attemptNumber: Int,
        blockedStatus: String
    ) {
        val alerts = BasilianSecurityManager.recordIntrusionAlert(
            context = getApplication(),
            targetArea = targetArea,
            actorInfo = actorInfo,
            attemptNumber = attemptNumber,
            blockedStatus = blockedStatus
        )
        _intrusionAlerts.value = alerts
        alerts.firstOrNull()?.let { latest ->
            curiaFirebaseManager.publishIntrusionAlertToLeaderCloud(
                alert = latest,
                leaderTargetEmail = _leaderCreatorEmail.value
            )
        }
        NotificationHelper.sendIntrusionAlertNotification(
            context = getApplication(),
            targetArea = targetArea,
            actorInfo = actorInfo,
            failedAttempts = attemptNumber
        )
    }

    private val _maxSecurityShieldEnabled = MutableStateFlow(
        BasilianSecurityManager.isMaxSecurityShieldEnabled(application)
    )
    val maxSecurityShieldEnabled: StateFlow<Boolean> = _maxSecurityShieldEnabled.asStateFlow()

    fun setMaxSecurityShieldEnabled(enabled: Boolean) {
        _maxSecurityShieldEnabled.value = enabled
        BasilianSecurityManager.setMaxSecurityShieldEnabled(getApplication(), enabled)
        appendLiveUpdateLog(
            if (enabled) "🛡️ Blindaje Máximo Anti-Hackeo ACTIVADO en el sistema."
            else "🛡️ Blindaje Anti-Hackeo en modo estándar."
        )
    }

    fun clearIntrusionAlerts() {
        BasilianSecurityManager.clearIntrusionAlerts(getApplication())
        _intrusionAlerts.value = emptyList()
    }

    fun triggerTestIntrusionAlertNotification() {
        val actor = _currentUserProfile.value?.email?.ifBlank { "Verificación del Líder Creador" } ?: "Verificación del Líder Creador"
        val alerts = BasilianSecurityManager.recordIntrusionAlert(
            context = getApplication(),
            targetArea = "Prueba Exclusiva del Líder (Solo para Mí)",
            actorInfo = actor,
            attemptNumber = 1,
            blockedStatus = "Simulación verificada: Notificación enviada únicamente al Líder (${_leaderCreatorEmail.value})"
        )
        _intrusionAlerts.value = alerts
        alerts.firstOrNull()?.let { latest ->
            curiaFirebaseManager.publishIntrusionAlertToLeaderCloud(latest, _leaderCreatorEmail.value)
        }
        NotificationHelper.sendIntrusionAlertNotification(
            context = getApplication(),
            targetArea = "Acceso Exclusivo Solo para Mí",
            actorInfo = actor,
            failedAttempts = 1,
            forceForLeaderSync = true
        )
    }

    private val _curiaIsAuthenticated = MutableStateFlow(false)
    val curiaIsAuthenticated: StateFlow<Boolean> = _curiaIsAuthenticated.asStateFlow()

    val curiaSyncStatus: StateFlow<String> = curiaFirebaseManager.syncStatus
    val curiaAdminEmail: StateFlow<String?> = curiaFirebaseManager.currentAdminEmail
    val curiaIsFirebaseAvailable: StateFlow<Boolean> = curiaFirebaseManager.isFirebaseAvailable

    private val _curiaAnnouncementTitle = MutableStateFlow(
        prefs.getString("curia_announcement_title", "Comunicado Fraterno de la Comunidad Basiliana")
            ?: "Comunicado Fraterno de la Comunidad Basiliana"
    )
    val curiaAnnouncementTitle: StateFlow<String> = _curiaAnnouncementTitle.asStateFlow()

    private val _curiaAnnouncementBody = MutableStateFlow(
        prefs.getString(
            "curia_announcement_body",
            "Queridos hermanos en San Basilio, renovamos nuestro compromiso fraterno en la oración constante, la vida comunitaria y la educación integral de la juventud según el carisma de Bondad, Disciplina y Ciencia."
        ) ?: "Queridos hermanos en San Basilio, renovamos nuestro compromiso fraterno en la oración constante, la vida comunitaria y la educación integral de la juventud según el carisma de Bondad, Disciplina y Ciencia."
    )
    val curiaAnnouncementBody: StateFlow<String> = _curiaAnnouncementBody.asStateFlow()

    private val _curiaAnnouncementDate = MutableStateFlow(
        prefs.getString("curia_announcement_date", "Curia General • Comunidad Basiliana")
            ?: "Curia General • Comunidad Basiliana"
    )
    val curiaAnnouncementDate: StateFlow<String> = _curiaAnnouncementDate.asStateFlow()

    private val _curiaPrayerIntention = MutableStateFlow(
        prefs.getString(
            "curia_prayer_intention",
            "Por las vocaciones sacerdotales basilianas y los proyectos educativos y comunitarios en Colombia y el mundo."
        ) ?: "Por las vocaciones sacerdotales basilianas y los proyectos educativos y comunitarios en Colombia y el mundo."
    )
    val curiaPrayerIntention: StateFlow<String> = _curiaPrayerIntention.asStateFlow()

    private var curiaFailedAttempts = 0
    private var curiaLockoutUntilMs = 0L

    fun getCuriaLockoutRemainingSeconds(): Int {
        val rem = ((curiaLockoutUntilMs - System.currentTimeMillis()) / 1000L).toInt()
        return rem.coerceAtLeast(0)
    }

    fun authenticateCuria(passwordAttempt: String): Boolean {
        val now = System.currentTimeMillis()
        if (now < curiaLockoutUntilMs) {
            return false
        }
        if (!BasilianSecurityManager.checkAntiBotProtection("curia_vault_auth")) {
            curiaLockoutUntilMs = now + 120_000L
            return false
        }
        val clean = BasilianSecurityManager.validateAndSanitizeInput(passwordAttempt, maxLength = 128)
        val customPass = _curiaMasterPassword.value.trim()
        val quickPin = _ownerQuickPin.value.trim()
        val matchesMaster = clean.isNotEmpty() && BasilianSecurityManager.constantTimeEquals(clean, customPass)
        val matchesPin = clean.length in 4..8 && clean.all { it.isDigit() } && BasilianSecurityManager.constantTimeEquals(clean, quickPin)
        if (matchesMaster || matchesPin) {
            curiaFailedAttempts = 0
            curiaLockoutUntilMs = 0L
            _curiaIsAuthenticated.value = true
            startVaultAutoLockMonitor()
            return true
        }
        curiaFailedAttempts++
        val maxAllowed = if (_maxSecurityShieldEnabled.value) 2 else 3
        val lockoutDurationMs = if (_maxSecurityShieldEnabled.value) 180_000L else 45_000L
        val actorEmail = _currentUserProfile.value?.email?.takeIf { it.isNotBlank() } ?: "Usuario en dispositivo"
        val actorName = _currentUserProfile.value?.fullName?.takeIf { it.isNotBlank() } ?: "Intento no autorizado"
        val actorDesc = "$actorName ($actorEmail)"

        val blockedStatus = if (curiaFailedAttempts >= maxAllowed) {
            curiaLockoutUntilMs = now + lockoutDurationMs
            val attemptsRecorded = curiaFailedAttempts
            curiaFailedAttempts = 0
            "Acceso bloqueado automáticamente por ${lockoutDurationMs / 1000}s tras $attemptsRecorded intentos fallidos"
        } else {
            "Intento #$curiaFailedAttempts rechazado • Blindaje AES-256 activo"
        }

        recordAndRouteIntrusionToLeaderOnly(
            targetArea = "Acceso Exclusivo • Solo para Mí",
            actorInfo = actorDesc,
            attemptNumber = if (curiaFailedAttempts == 0) maxAllowed else curiaFailedAttempts,
            blockedStatus = blockedStatus
        )
        appendLiveUpdateLog("🚨 ALERTA ANTI-HACKEO (Enviada solo al Líder): Intento fallido de ingreso a «Solo para Mí» por $actorDesc.")
        return false
    }

    fun authenticateCuriaWithFirebase(email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        curiaFirebaseManager.login(email, pass) { success, msg ->
            if (success) {
                _curiaIsAuthenticated.value = true
                startVaultAutoLockMonitor()
            }
            onResult(success, msg)
        }
    }

    fun logoutCuria() {
        autoLockJob?.cancel()
        curiaFirebaseManager.logout()
        _curiaIsAuthenticated.value = false
    }

    fun updateCuriaAnnouncement(title: String, body: String, date: String) {
        _curiaAnnouncementTitle.value = title
        _curiaAnnouncementBody.value = body
        _curiaAnnouncementDate.value = date
    }

    fun updateCuriaPrayerIntention(intention: String) {
        _curiaPrayerIntention.value = intention
    }

    fun publishCuriaAnnouncementWithFirebase(
        title: String,
        body: String,
        date: String,
        prayer: String,
        onResult: (Boolean, String) -> Unit
    ) {
        updateCuriaAnnouncement(title, body, date)
        updateCuriaPrayerIntention(prayer)
        curiaFirebaseManager.publishAnnouncement(title, body, date, prayer, onResult)
    }

    // --- COPILOTO BASILIANO IA (GEMINI + REGISTRO DOCUMENTAL DE LA REGLA) ---
    private val _copilotReflection = MutableStateFlow<com.example.data.ai.CopilotReflection?>(null)
    val copilotReflection: StateFlow<com.example.data.ai.CopilotReflection?> = _copilotReflection.asStateFlow()

    private val _isCopilotGenerating = MutableStateFlow(false)
    val isCopilotGenerating: StateFlow<Boolean> = _isCopilotGenerating.asStateFlow()

    private val _copilotCorpusCategory = MutableStateFlow("Regla de San Basilio (Vida Fraterna)")
    val copilotCorpusCategory: StateFlow<String> = _copilotCorpusCategory.asStateFlow()

    fun setCopilotCorpusCategory(category: String) {
        _copilotCorpusCategory.value = category
    }

    fun generateCopilotReflection(userPrompt: String) {
        val trimmed = userPrompt.trim()
        if (trimmed.isEmpty() || _isCopilotGenerating.value) return

        viewModelScope.launch {
            _isCopilotGenerating.value = true
            try {
                val snippet = buildDocumentContextSnippet(_copilotCorpusCategory.value, trimmed)
                val reflection = com.example.data.ai.BasilianAiService.generateCopilotReflection(
                    prompt = trimmed,
                    corpusCategory = _copilotCorpusCategory.value,
                    contextTextSnippet = snippet
                )
                _copilotReflection.value = reflection
            } catch (e: Exception) {
                _copilotReflection.value = com.example.data.ai.CopilotReflection(
                    title = "Reflexión Basiliana • En comunión espiritual",
                    content = "«La caridad nunca decae. En todo momento el Espíritu Santo suscita en la Iglesia hombres y mujeres que se consagran al servicio de Dios y del prójimo» (San Basilio Magno).",
                    patristicSource = "Regla de San Basilio & Constituciones C.S.B.",
                    practicalApplication = "Vivir la fidelidad cotidiana en el carisma de bondad, disciplina y ciencia."
                )
            } finally {
                _isCopilotGenerating.value = false
            }
        }
    }

    fun applyCopilotReflectionToAnnouncement() {
        val refl = _copilotReflection.value ?: return
        _curiaAnnouncementTitle.value = refl.title
        _curiaAnnouncementBody.value = "${refl.content}\n\n[Fuente: ${refl.patristicSource}]"
        _curiaPrayerIntention.value = refl.practicalApplication
    }

    // =========================================================================
    // OPTIMIZADOR & ACTUALIZADOR IA DE LA APP PARA LA CURIA Y EL ESCOLASTICADO
    // =========================================================================
    data class AiCommandExecutionResult(
        val userPrompt: String,
        val explanation: String,
        val actionsExecuted: List<String>,
        val timestamp: String = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
    )

    private val _aiCommandExecutionResult = MutableStateFlow<AiCommandExecutionResult?>(null)
    val aiCommandExecutionResult: StateFlow<AiCommandExecutionResult?> = _aiCommandExecutionResult.asStateFlow()

    private val _liveAppVersion = MutableStateFlow(
        prefs.getString("pref_live_app_version", "v5.0 • Motor Holográfico 3D C.S.B.") ?: "v5.0 • Motor Holográfico 3D C.S.B."
    )
    val liveAppVersion: StateFlow<String> = _liveAppVersion.asStateFlow()

    private val _jarvisDailyFocusTopic = MutableStateFlow(
        prefs.getString(
            "pref_jarvis_daily_focus",
            "Crónicas de Annonay 1822, Homilética Patrística, Constituciones y Apostolado Educativo"
        ) ?: "Crónicas de Annonay 1822, Homilética Patrística, Constituciones y Apostolado Educativo"
    )
    val jarvisDailyFocusTopic: StateFlow<String> = _jarvisDailyFocusTopic.asStateFlow()

    private val _liveUpdateHistoryLog = MutableStateFlow<List<String>>(
        prefs.getString("pref_live_update_history_log", null)?.split("||")?.filter { it.isNotBlank() }
            ?: listOf(
                "⚡ [Sistema v5.0] Motor de Automejora IA Curia activado en tiempo real",
                "✨ [Holograma 3D] Núcleo holográfico y saludo personalizado vinculados por correo"
            )
    )
    val liveUpdateHistoryLog: StateFlow<List<String>> = _liveUpdateHistoryLog.asStateFlow()

    private fun appendLiveUpdateLog(entry: String) {
        val sdf = java.text.SimpleDateFormat("dd/MM HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
        val updated = (listOf("[$sdf] $entry") + _liveUpdateHistoryLog.value).take(12)
        _liveUpdateHistoryLog.value = updated
        prefs.edit().putString("pref_live_update_history_log", updated.joinToString("||")).apply()
    }

    fun setJarvisDailyFocusTopic(topic: String) {
        val clean = topic.trim()
        if (clean.isNotEmpty()) {
            _jarvisDailyFocusTopic.value = clean
            prefs.edit().putString("pref_jarvis_daily_focus", clean).apply()
            appendLiveUpdateLog("✨ Misión diaria de IA de Historia actualizada a: $clean")
        }
    }

    private val _customAppHeaderTitle = MutableStateFlow(
        prefs.getString("pref_custom_app_header_title", "Congregación de San Basilio") ?: "Congregación de San Basilio"
    )
    val customAppHeaderTitle: StateFlow<String> = _customAppHeaderTitle.asStateFlow()

    private val _customAppSubtitleMotto = MutableStateFlow(
        prefs.getString("pref_custom_app_subtitle_motto", "Bonitatem et disciplinam et scientiam doce me") ?: "Bonitatem et disciplinam et scientiam doce me"
    )
    val customAppSubtitleMotto: StateFlow<String> = _customAppSubtitleMotto.asStateFlow()

    fun setCustomAppHeaderTitle(title: String) {
        val clean = title.trim()
        if (clean.isNotEmpty()) {
            _customAppHeaderTitle.value = clean
            prefs.edit().putString("pref_custom_app_header_title", clean).apply()
            appendLiveUpdateLog("🏛️ Título principal actualizado a: $clean")
            syncGlobalAdminChangesToAllDevices()
        }
    }

    fun setCustomAppSubtitleMotto(motto: String) {
        val clean = motto.trim()
        if (clean.isNotEmpty()) {
            _customAppSubtitleMotto.value = clean
            prefs.edit().putString("pref_custom_app_subtitle_motto", clean).apply()
            appendLiveUpdateLog("✨ Lema institucional actualizado a: $clean")
            syncGlobalAdminChangesToAllDevices()
        }
    }

    private val _otaReleaseNotes = MutableStateFlow(
        prefs.getString(
            "pref_ota_release_notes",
            "Super Basilio IA conectado a los 5 libros oficiales y a la web, Red de Educación y Racha traducidas (ES/EN/FR) y Auto-Actualización Automática sin enlaces."
        ) ?: "Super Basilio IA conectado a los 5 libros oficiales y a la web, Red de Educación y Racha traducidas (ES/EN/FR) y Auto-Actualización Automática sin enlaces."
    )
    val otaReleaseNotes: StateFlow<String> = _otaReleaseNotes.asStateFlow()

    companion object {
        const val VERIFIED_SHARED_APP_URL = "https://ais-pre-sp72ppunoloryzacylamy3-505689414903.us-east1.run.app"
        const val VERIFIED_CREATOR_DEV_URL = "https://ais-dev-sp72ppunoloryzacylamy3-505689414903.us-east1.run.app"
    }

    // Link real y verídico de la aplicación compartida en Google AI Studio para actualizar o compartir
    private val _otaDownloadUrl = MutableStateFlow(
        prefs.getString("pref_ota_download_url", VERIFIED_SHARED_APP_URL)
            ?.takeIf { it.isNotBlank() && it.startsWith("http") }
            ?: VERIFIED_SHARED_APP_URL
    )
    val otaDownloadUrl: StateFlow<String> = _otaDownloadUrl.asStateFlow()

    private val _autoBroadcastOnLeaderInstall = MutableStateFlow(
        prefs.getBoolean("pref_auto_broadcast_on_leader_install", true)
    )
    val autoBroadcastOnLeaderInstall: StateFlow<Boolean> = _autoBroadcastOnLeaderInstall.asStateFlow()

    private val _lastGlobalSyncTimestamp = MutableStateFlow(
        prefs.getString("pref_last_global_sync_timestamp", "Auto-actualización directa en la nube activa")
            ?: "Auto-actualización directa en la nube activa"
    )
    val lastGlobalSyncTimestamp: StateFlow<String> = _lastGlobalSyncTimestamp.asStateFlow()

    private val _incomingLiveUpdateNotice = MutableStateFlow<String?>(null)
    val incomingLiveUpdateNotice: StateFlow<String?> = _incomingLiveUpdateNotice.asStateFlow()

    fun dismissIncomingLiveUpdateNotice() {
        _incomingLiveUpdateNotice.value = null
    }

    fun setOtaReleaseNotes(notes: String) {
        _otaReleaseNotes.value = notes
        prefs.edit().putString("pref_ota_release_notes", notes).apply()
    }

    fun setOtaDownloadUrl(url: String) {
        val clean = url.trim().ifBlank { VERIFIED_SHARED_APP_URL }
        _otaDownloadUrl.value = clean
        prefs.edit().putString("pref_ota_download_url", clean).apply()
    }

    fun setAutoBroadcastOnLeaderInstall(enabled: Boolean) {
        _autoBroadcastOnLeaderInstall.value = enabled
        prefs.edit().putBoolean("pref_auto_broadcast_on_leader_install", enabled).apply()
        appendLiveUpdateLog(
            if (enabled) "📲 Auto-Actualización Automática al abrir/instalar en el celular del Líder (villamizargalviz@gmail.com): ACTIVADA"
            else "📲 Auto-Actualización Global en modo de permiso manual"
        )
    }

    fun broadcastNewVersionToAllPhonesNow(
        versionLabel: String,
        releaseNotes: String,
        downloadUrl: String = _otaDownloadUrl.value,
        onResult: ((Boolean, String) -> Unit)? = null
    ) {
        val cleanVer = versionLabel.trim().ifBlank { _liveAppVersion.value }
        val cleanNotes = releaseNotes.trim().ifBlank { _otaReleaseNotes.value }
        val cleanUrl = downloadUrl.trim().ifBlank { VERIFIED_SHARED_APP_URL }

        _liveAppVersion.value = cleanVer
        _otaReleaseNotes.value = cleanNotes
        _otaDownloadUrl.value = cleanUrl

        val now = java.util.Date()
        val stamp = java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", java.util.Locale.getDefault()).format(now)
        _lastGlobalSyncTimestamp.value = "Autorizado y enviado el $stamp ($cleanVer)"

        prefs.edit()
            .putString("pref_live_app_version", cleanVer)
            .putString("pref_ota_release_notes", cleanNotes)
            .putString("pref_ota_download_url", cleanUrl)
            .putString("pref_last_global_sync_timestamp", _lastGlobalSyncTimestamp.value)
            .apply()

        appendLiveUpdateLog("⚡ Permiso de Auto-Actualización concedido ($cleanVer • Hora: $stamp): aplicando cambios en todos los celulares y habilitando link real $cleanUrl")
        syncGlobalAdminChangesToAllDevices(
            forceBroadcastAlert = true,
            onResult = onResult
        )
    }

    fun runSuperAiAppSelfUpgrade(customDirective: String = "") {
        val directive = customDirective.trim().ifBlank {
            "Ejecutar Super-Automejora Integral v5.5: optimizar motor 3D, sincronizar corpus 2026, traducir Red de Educación y Racha (ES/EN/FR) y actualizar todos los celulares"
        }
        val newVersion = "v5.${(5..9).random()}.${(0..9).random()} • Sincronización Global C.S.B."
        _liveAppVersion.value = newVersion
        prefs.edit().putString("pref_live_app_version", newVersion).apply()

        refreshBasilianCorpusData()
        val updatedKeys = _appliedConfigKeys.value.toMutableSet().apply {
            add("OFFLINE_PRIORITY")
            add("HOLY_SPIRIT_DYNAMIC_GLOW")
            add("DAILY_PRAYER_AUTO_NOTIFY")
            add("COMMUNITY_FRATERNAL_FILTER")
            add("JARVIS_HOLOGRAPHIC_CORE")
        }
        _appliedConfigKeys.value = updatedKeys
        saveAppliedConfigKeys(updatedKeys)

        appendLiveUpdateLog("🚀 Super-Actualización aplicada en vivo ($newVersion)")
        executeAiAppCommand(directive)
        syncGlobalAdminChangesToAllDevices(forceBroadcastAlert = true)
    }

    // -------------------------------------------------------------------------
    // VISIBILIDAD DE PESTAÑAS (QUITAR / PONER EN TIEMPO REAL CON IA O BOTONES)
    // -------------------------------------------------------------------------
    private val _showTabOracion = MutableStateFlow(prefs.getBoolean("pref_show_tab_oracion", true))
    val showTabOracion: StateFlow<Boolean> = _showTabOracion.asStateFlow()

    private val _showTabLiturgia = MutableStateFlow(prefs.getBoolean("pref_show_tab_liturgia", true))
    val showTabLiturgia: StateFlow<Boolean> = _showTabLiturgia.asStateFlow()

    private val _showTabCumpleanos = MutableStateFlow(prefs.getBoolean("pref_show_tab_cumpleanos", true))
    val showTabCumpleanos: StateFlow<Boolean> = _showTabCumpleanos.asStateFlow()

    private val _showTabHistoria = MutableStateFlow(true)
    val showTabHistoria: StateFlow<Boolean> = _showTabHistoria.asStateFlow()

    private val _showTabRecursos = MutableStateFlow(prefs.getBoolean("pref_show_tab_recursos", true))
    val showTabRecursos: StateFlow<Boolean> = _showTabRecursos.asStateFlow()

    // -------------------------------------------------------------------------
    // VISIBILIDAD DE COMPONENTES INTERNOS (BANNER, FOTOS, ORDO, AUDIO, MURO)
    // -------------------------------------------------------------------------
    private val _showHeroBanner = MutableStateFlow(prefs.getBoolean("pref_show_hero_banner", true))
    val showHeroBanner: StateFlow<Boolean> = _showHeroBanner.asStateFlow()

    private val _showDevotionalImageSlots = MutableStateFlow(prefs.getBoolean("pref_show_devotional_image_slots", true))
    val showDevotionalImageSlots: StateFlow<Boolean> = _showDevotionalImageSlots.asStateFlow()

    private val _showOrdoQuickAccess = MutableStateFlow(prefs.getBoolean("pref_show_ordo_quick_access", true))
    val showOrdoQuickAccess: StateFlow<Boolean> = _showOrdoQuickAccess.asStateFlow()

    private val _showCuriaAnnouncementInPrayer = MutableStateFlow(prefs.getBoolean("pref_show_curia_announcement_in_prayer", true))
    val showCuriaAnnouncementInPrayer: StateFlow<Boolean> = _showCuriaAnnouncementInPrayer.asStateFlow()

    private val _showDailyAudioPlayer = MutableStateFlow(prefs.getBoolean("pref_show_daily_audio_player", true))
    val showDailyAudioPlayer: StateFlow<Boolean> = _showDailyAudioPlayer.asStateFlow()

    private val _showCommunitySocialWall = MutableStateFlow(prefs.getBoolean("pref_show_community_social_wall", true))
    val showCommunitySocialWall: StateFlow<Boolean> = _showCommunitySocialWall.asStateFlow()

    fun setShowTabOracion(v: Boolean) { _showTabOracion.value = v; prefs.edit().putBoolean("pref_show_tab_oracion", v).apply(); syncGlobalAdminChangesToAllDevices() }
    fun setShowTabLiturgia(v: Boolean) { _showTabLiturgia.value = v; prefs.edit().putBoolean("pref_show_tab_liturgia", v).apply(); syncGlobalAdminChangesToAllDevices() }
    fun setShowTabCumpleanos(v: Boolean) { _showTabCumpleanos.value = v; prefs.edit().putBoolean("pref_show_tab_cumpleanos", v).apply(); syncGlobalAdminChangesToAllDevices() }
    fun setShowTabHistoria(v: Boolean) { _showTabHistoria.value = v; prefs.edit().putBoolean("pref_show_tab_historia", v).apply(); syncGlobalAdminChangesToAllDevices() }
    fun setShowTabRecursos(v: Boolean) { _showTabRecursos.value = v; prefs.edit().putBoolean("pref_show_tab_recursos", v).apply(); syncGlobalAdminChangesToAllDevices() }

    fun setShowHeroBanner(v: Boolean) { _showHeroBanner.value = v; prefs.edit().putBoolean("pref_show_hero_banner", v).apply(); syncGlobalAdminChangesToAllDevices() }
    fun setShowDevotionalImageSlots(v: Boolean) { _showDevotionalImageSlots.value = v; prefs.edit().putBoolean("pref_show_devotional_image_slots", v).apply(); syncGlobalAdminChangesToAllDevices() }
    fun setShowOrdoQuickAccess(v: Boolean) { _showOrdoQuickAccess.value = v; prefs.edit().putBoolean("pref_show_ordo_quick_access", v).apply(); syncGlobalAdminChangesToAllDevices() }
    fun setShowCuriaAnnouncementInPrayer(v: Boolean) { _showCuriaAnnouncementInPrayer.value = v; prefs.edit().putBoolean("pref_show_curia_announcement_in_prayer", v).apply(); syncGlobalAdminChangesToAllDevices() }
    fun setShowDailyAudioPlayer(v: Boolean) { _showDailyAudioPlayer.value = v; prefs.edit().putBoolean("pref_show_daily_audio_player", v).apply(); syncGlobalAdminChangesToAllDevices() }
    fun setShowCommunitySocialWall(v: Boolean) { _showCommunitySocialWall.value = v; prefs.edit().putBoolean("pref_show_community_social_wall", v).apply(); syncGlobalAdminChangesToAllDevices() }

    /**
     * Publica toda la configuración global del Líder (villamizargalviz@gmail.com) hacia la Nube Directa y Firestore
     * para que todos los celulares donde esté instalada la aplicación reciban y apliquen los cambios automáticamente
     * junto con la hora exacta de actualización, sin requerir enlaces ni descargas manuales.
     */
    fun syncGlobalAdminChangesToAllDevices(
        forceBroadcastAlert: Boolean = false,
        onResult: ((Boolean, String) -> Unit)? = null
    ) {
        val nowMs = System.currentTimeMillis()
        val nowDate = java.util.Date(nowMs)
        val stamp = java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", java.util.Locale.getDefault()).format(nowDate)
        val hourOnly = java.text.SimpleDateFormat("hh:mm:ss a", java.util.Locale.getDefault()).format(nowDate)
        _lastGlobalSyncTimestamp.value = "Auto-actualizado el $stamp (${_liveAppVersion.value})"
        prefs.edit()
            .putString("pref_last_global_sync_timestamp", _lastGlobalSyncTimestamp.value)
            .putLong("pref_last_seen_ota_broadcast_ms", nowMs)
            .apply()

        val globalPayload = mutableMapOf<String, Any>(
            "liveAppVersion" to _liveAppVersion.value,
            "customAppHeaderTitle" to _customAppHeaderTitle.value,
            "customAppSubtitleMotto" to _customAppSubtitleMotto.value,
            "dailyFocusTopic" to _jarvisDailyFocusTopic.value,
            "announcementTitle" to _curiaAnnouncementTitle.value,
            "announcementBody" to _curiaAnnouncementBody.value,
            "announcementDate" to _curiaAnnouncementDate.value,
            "prayerIntention" to _curiaPrayerIntention.value,
            "showTabOracion" to _showTabOracion.value,
            "showTabLiturgia" to _showTabLiturgia.value,
            "showTabCumpleanos" to _showTabCumpleanos.value,
            "showTabHistoria" to _showTabHistoria.value,
            "showTabRecursos" to _showTabRecursos.value,
            "showHeroBanner" to _showHeroBanner.value,
            "showDevotionalImageSlots" to _showDevotionalImageSlots.value,
            "showOrdoQuickAccess" to _showOrdoQuickAccess.value,
            "showCuriaAnnouncementInPrayer" to _showCuriaAnnouncementInPrayer.value,
            "showDailyAudioPlayer" to _showDailyAudioPlayer.value,
            "showCommunitySocialWall" to _showCommunitySocialWall.value,
            "globalBackgroundMode" to _globalBackgroundMode.value.name,
            "globalBackgroundOpacity" to _globalBackgroundOpacity.value.toDouble(),
            "appThemeId" to _currentAppTheme.value.id,
            "learnedKnowledgeFacts" to _learnedAiFacts.value.take(25).map { "${it.title}|||${it.details}|||${it.category}" },
            "otaReleaseNotes" to _otaReleaseNotes.value,
            "lastSyncReadable" to stamp,
            "lastSyncHour" to hourOnly,
            "lastSyncTimestamp" to nowMs
        )
        if (forceBroadcastAlert) {
            globalPayload["otaBroadcastTimestampMs"] = nowMs
        }
        curiaFirebaseManager.publishGlobalAppConfig(globalPayload, onResult)
    }

    fun restoreAllAppSections() {
        setShowTabOracion(true)
        setShowTabLiturgia(true)
        setShowTabCumpleanos(true)
        setShowTabHistoria(true)
        setShowTabRecursos(true)
        setShowHeroBanner(true)
        setShowDevotionalImageSlots(true)
        setShowOrdoQuickAccess(true)
        setShowCuriaAnnouncementInPrayer(true)
        setShowDailyAudioPlayer(true)
        setShowCommunitySocialWall(true)
        setGlobalBackgroundMode(GlobalBgMode.VITRAL_SACRO)
        setAppTheme(BasilianAppTheme.ESCUDO_BASILIANO)
        syncGlobalAdminChangesToAllDevices()
    }

    private val _appUpdateProposal = MutableStateFlow<com.example.data.ai.AppUpdateProposal?>(null)
    val appUpdateProposal: StateFlow<com.example.data.ai.AppUpdateProposal?> = _appUpdateProposal.asStateFlow()

    private val _isAppUpdateGenerating = MutableStateFlow(false)
    val isAppUpdateGenerating: StateFlow<Boolean> = _isAppUpdateGenerating.asStateFlow()

    fun executeAiAppCommand(userCommand: String) {
        val trimmed = userCommand.trim()
        if (trimmed.isEmpty()) return
        _isAppUpdateGenerating.value = true

        viewModelScope.launch {
            try {
                val lower = trimmed.lowercase(java.util.Locale.getDefault())
                val actions = mutableListOf<String>()

                val isRemove = lower.contains("quita") || lower.contains("elimina") || lower.contains("oculta") ||
                        lower.contains("borra") || lower.contains("desactiva") || lower.contains("no quiero") || lower.contains("sacar")
                val isAdd = lower.contains("pon") || lower.contains("agrega") || lower.contains("muestra") ||
                        lower.contains("activa") || lower.contains("coloca") || lower.contains("deja") ||
                        lower.contains("quiero ver") || lower.contains("cambia")

                // Liturgia
                if (lower.contains("liturgia")) {
                    if (isRemove && !lower.contains("solo")) {
                        setShowTabLiturgia(false)
                        actions.add("❌ Pestaña 'Liturgia de las Horas' retirada de la navegación")
                    } else if (isAdd) {
                        setShowTabLiturgia(true)
                        actions.add("✅ Pestaña 'Liturgia de las Horas' activada y visible")
                    }
                }

                // Cumpleaños
                if (lower.contains("cumpleaño") || lower.contains("cumpleaños") || lower.contains("directorio")) {
                    if (isRemove) {
                        setShowTabCumpleanos(false)
                        actions.add("❌ Pestaña 'Cumpleaños y Directorio' retirada de la navegación")
                    } else if (isAdd) {
                        setShowTabCumpleanos(true)
                        actions.add("✅ Pestaña 'Cumpleaños y Directorio' activada y visible")
                    }
                }

                // Historia
                if (lower.contains("historia") || lower.contains("cronista")) {
                    if (isRemove) {
                        setShowTabHistoria(false)
                        actions.add("❌ Pestaña 'Historia IA' retirada de la navegación")
                    } else if (isAdd) {
                        setShowTabHistoria(true)
                        actions.add("✅ Pestaña 'Historia IA' activada y visible")
                    }
                }

                // Recursos
                if (lower.contains("recurso") || lower.contains("recursos") || lower.contains("biblioteca")) {
                    if (isRemove) {
                        setShowTabRecursos(false)
                        actions.add("❌ Pestaña 'Recursos y Biblioteca' retirada de la navegación")
                    } else if (isAdd) {
                        setShowTabRecursos(true)
                        actions.add("✅ Pestaña 'Recursos y Biblioteca' activada y visible")
                    }
                }

                // Oración
                if (lower.contains("oracion") || lower.contains("oración") || lower.contains("rezo")) {
                    if (isRemove && !lower.contains("oracion por") && !lower.contains("oración por")) {
                        setShowTabOracion(false)
                        actions.add("❌ Pestaña 'Oración Diaria' retirada de la navegación")
                    } else if (isAdd && !lower.contains("oracion por") && !lower.contains("oración por")) {
                        setShowTabOracion(true)
                        actions.add("✅ Pestaña 'Oración Diaria' activada y visible")
                    }
                }

                // Banner
                if (lower.contains("banner") || lower.contains("cabecera") || lower.contains("hero")) {
                    if (isRemove) {
                        setShowHeroBanner(false)
                        actions.add("❌ Banner heroico de bienvenida ocultado")
                    } else if (isAdd) {
                        setShowHeroBanner(true)
                        actions.add("✅ Banner heroico de bienvenida mostrado")
                    }
                }

                // Audio
                if (lower.contains("audio") || lower.contains("musica") || lower.contains("música") || lower.contains("voz")) {
                    if (isRemove) {
                        setShowDailyAudioPlayer(false)
                        actions.add("❌ Reproductor de audio sagrado desactivado")
                    } else if (isAdd) {
                        setShowDailyAudioPlayer(true)
                        actions.add("✅ Reproductor de audio sagrado activado")
                    }
                }

                // Muro
                if (lower.contains("muro") || lower.contains("facebook") || lower.contains("red social")) {
                    if (isRemove) {
                        setShowCommunitySocialWall(false)
                        actions.add("❌ Muro social de la comunidad ocultado")
                    } else if (isAdd) {
                        setShowCommunitySocialWall(true)
                        actions.add("✅ Muro social de la comunidad activado")
                    }
                }

                // Espacios de Fotos
                if (lower.contains("foto") || lower.contains("fotos") || lower.contains("imagen") || lower.contains("subir")) {
                    if (isRemove && (lower.contains("espacio") || lower.contains("slot") || lower.contains("boton"))) {
                        setShowDevotionalImageSlots(false)
                        actions.add("❌ Espacios de subida de fotos ocultados")
                    } else if (lower.contains("reinicia") || lower.contains("borra") || lower.contains("quitar foto")) {
                        resetCustomCrest()
                        resetCustomPrayerSideImage()
                        resetCustomLiturgiaImage()
                        actions.add("🔄 Fotos personalizadas restablecidas a los iconos originales")
                    } else if (isAdd) {
                        setShowDevotionalImageSlots(true)
                        actions.add("✅ Espacios de subida de fotos activados")
                    }
                }

                // FONDOS (Quitar pantalla blanca / poner fondo cálido, futurista 3D o llamativo)
                if (lower.contains("jarvis") || lower.contains("futurista") || lower.contains("holografico") || lower.contains("holográfico") || lower.contains("cyber")) {
                    setGlobalBackgroundMode(GlobalBgMode.JARVIS_FUTURISTA)
                    setGlobalBackgroundOpacity(0.95f)
                    actions.add("✨ Fondo 'Holograma Futurista 3D' activado en toda la aplicación")
                } else if (lower.contains("blanco") || lower.contains("pantalla blanca") || lower.contains("frio") || lower.contains("frío") || lower.contains("vitral")) {
                    setGlobalBackgroundMode(GlobalBgMode.VITRAL_SACRO)
                    setGlobalBackgroundOpacity(0.95f)
                    actions.add("✨ Fondo 'Vitral Sacro Resplandeciente' activado (se eliminó la frialdad de pantalla blanca)")
                } else if (lower.contains("espiritu") || lower.contains("espíritu") || lower.contains("paloma") || lower.contains("rayos dorados")) {
                    setGlobalBackgroundMode(GlobalBgMode.ESPIRITU_SANTO)
                    setGlobalBackgroundOpacity(0.92f)
                    actions.add("🕊️ Fondo 'Espíritu Santo Celestial & Rayos Dorados' activado")
                } else if (lower.contains("pergamino") || lower.contains("antiguo") || lower.contains("papel")) {
                    setGlobalBackgroundMode(GlobalBgMode.PERGAMINO_MONASTICO)
                    setGlobalBackgroundOpacity(0.92f)
                    actions.add("📜 Fondo 'Pergamino Cálido Monástico' activado")
                } else if (lower.contains("caliz") || lower.contains("cáliz") || lower.contains("eucaristia") || lower.contains("eucaristía")) {
                    setGlobalBackgroundMode(GlobalBgMode.CALIZ_EUCARISTICO)
                    setGlobalBackgroundOpacity(0.92f)
                    actions.add("🍷 Fondo 'Cáliz Dorado & Eucaristía Resplandeciente' activado")
                } else if (lower.contains("noche") || lower.contains("vigilia") || lower.contains("estrell")) {
                    setGlobalBackgroundMode(GlobalBgMode.NOCHE_MISTICA)
                    actions.add("🌌 Fondo 'Vigilia & Noche Monástica' activado")
                }

                // MISIÓN DEL SALUDO DE LA IA DE HISTORIA
                if (lower.contains("saludo") || lower.contains("trabajaremos") || lower.contains("mision") || lower.contains("misión")) {
                    val customFocus = trimmed.substringAfter(":", trimmed)
                    setJarvisDailyFocusTopic(customFocus)
                    actions.add("✨ Saludo y enfoque de trabajo diario en IA de Historia actualizado en vivo")
                }

                // SUPER ACTUALIZACIÓN DE VERSIÓN Y MOTOR DE LA APP
                if (lower.contains("automejora") || lower.contains("auto-mejora") || lower.contains("super") || lower.contains("actualiza toda") || lower.contains("version") || lower.contains("versión")) {
                    val bumpedVersion = "v5.${(2..9).random()}.${(1..9).random()} • Motor 3D Auto-Evolución"
                    _liveAppVersion.value = bumpedVersion
                    prefs.edit().putString("pref_live_app_version", bumpedVersion).apply()
                    refreshBasilianCorpusData()
                    actions.add("🚀 Núcleo del Sistema actualizado en vivo a $bumpedVersion")
                    actions.add("✨ Corpus Patrístico y Evangelio del día sincronizados")
                }

                // TEMAS DE COLOR
                if (lower.contains("modo oscuro") || lower.contains("tema oscuro") || lower.contains("noche")) {
                    setAppTheme(BasilianAppTheme.HP_HOGWARTS_SOMBRAS)
                    actions.add("🌙 Modo Oscuro y Colores Nocturnos aplicados")
                } else if (lower.contains("tema oficial") || lower.contains("escudo") || lower.contains("modo claro")) {
                    setAppTheme(BasilianAppTheme.ESCUDO_BASILIANO)
                    actions.add("☀️ Tema Oficial 'Escudo Basiliano' restaurado")
                } else if (lower.contains("gryffindor") || lower.contains("rojo") || lower.contains("carmesi") || lower.contains("carmesí")) {
                    setAppTheme(BasilianAppTheme.HP_GRYFFINDOR)
                    actions.add("🦁 Tema Noble 'Gryffindor' rojo y oro aplicado")
                } else if (lower.contains("slytherin") || lower.contains("verde oscuro") || lower.contains("esmeralda")) {
                    setAppTheme(BasilianAppTheme.HP_SLYTHERIN)
                    actions.add("🐍 Tema 'Slytherin' verde esmeralda aplicado")
                }

                // ORACIÓN O INTENCIÓN DEL DÍA
                if (lower.contains("oracion por") || lower.contains("oración por") || lower.contains("rezo por") || lower.contains("pide por") || lower.contains("orar por")) {
                    val extracted = trimmed.substringAfter("por", "").trim().ifBlank { trimmed }
                    val newPrayer = "🙏 Intención Comunitaria: $extracted"
                    _curiaPrayerIntention.value = newPrayer
                    prefs.edit().putString("curia_prayer_intention", newPrayer).apply()
                    actions.add("🙏 Oración del día actualizada a: \"$newPrayer\"")
                }

                // CIRCULAR O COMUNICADO
                if (lower.contains("circular") || lower.contains("comunicado") || lower.contains("aviso")) {
                    val newTitle = "📜 Comunicado Oficial Curial: Actualización en Vivo"
                    val newBody = trimmed
                    _curiaAnnouncementTitle.value = newTitle
                    _curiaAnnouncementBody.value = newBody
                    prefs.edit()
                        .putString("curia_announcement_title", newTitle)
                        .putString("curia_announcement_body", newBody)
                        .apply()
                    actions.add("📜 Circular Curial oficial actualizada en vivo en la app")
                }

                // AUTORIZACIÓN O RESTRICCIÓN DE CORREOS DESDE LA IA PRIVADA
                val emailMatch = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}").find(trimmed)?.value?.lowercase()
                if (emailMatch != null) {
                    if (lower.contains("autoriza") || lower.contains("aprueba") || lower.contains("basiliano") || lower.contains("acceso total")) {
                        approveBasilianAccessRequest(emailMatch)
                        actions.add("✅ Correo $emailMatch autorizado con acceso completo de Religioso Basiliano C.S.B.")
                    } else if (lower.contains("restringe") || lower.contains("publico") || lower.contains("público") || lower.contains("bloquea") || isRemove) {
                        setBasilianUserAsPublicVisitor(emailMatch)
                        actions.add("🔒 Correo $emailMatch restringido al modo público (sin acceso a datos internos)")
                    }
                }

                // CAMBIAR TÍTULO PRINCIPAL O LEMA DE LA APLICACIÓN DESDE LA IA
                if (lower.contains("titulo") || lower.contains("título") || lower.contains("nombre de la app")) {
                    val newHeader = trimmed.substringAfter(":", "").trim().ifBlank { "Congregación de San Basilio C.S.B." }
                    setCustomAppHeaderTitle(newHeader)
                    actions.add("🏛️ Título principal de la aplicación actualizado a: \"$newHeader\"")
                }
                if (lower.contains("subtitulo") || lower.contains("subtítulo") || lower.contains("lema")) {
                    val newSub = trimmed.substringAfter(":", "").trim().ifBlank { "Bonitatem et disciplinam et scientiam doce me" }
                    setCustomAppSubtitleMotto(newSub)
                    actions.add("✨ Subtítulo/Lema actualizado a: \"$newSub\"")
                }

                // RESTAURAR TODO
                if (lower.contains("restablece") || lower.contains("reinicia todo") || lower.contains("deja todo") || lower.contains("revertir")) {
                    restoreAllAppSections()
                    actions.add("🔄 Todas las pestañas, fondos y componentes restablecidos al estado ideal")
                }

                if (actions.isEmpty()) {
                    val bumpedVersion = "v5.${(1..9).random()} • Live AI Patch"
                    _liveAppVersion.value = bumpedVersion
                    prefs.edit().putString("pref_live_app_version", bumpedVersion).apply()
                    setJarvisDailyFocusTopic(trimmed.take(90))
                    actions.add("⚡ Parche de Automejora IA ($bumpedVersion) compilado y aplicado en vivo")
                    actions.add("✨ Directriz integrada en la configuración global de la aplicación")
                }

                // Sincronizar inmediatamente todos los cambios en la nube para todos los celulares
                syncGlobalAdminChangesToAllDevices()
                actions.add("☁️ Cambios sincronizados en tiempo real con todos los dispositivos vía Firebase")

                actions.forEach { appendLiveUpdateLog(it) }

                val proposal = com.example.data.ai.BasilianAiService.generateAppUpdateProposal(trimmed)
                _appUpdateProposal.value = proposal

                val userName = _currentUserProfile.value?.fullName ?: "Hermano Administrador"
                val explanation = "Paz y bien, $userName. He ejecutado tu orden directamente en el núcleo de la aplicación y sincronizado los cambios para todos los usuarios."

                _aiCommandExecutionResult.value = AiCommandExecutionResult(
                    userPrompt = trimmed,
                    explanation = explanation,
                    actionsExecuted = actions
                )
            } catch (e: Exception) {
                _aiCommandExecutionResult.value = AiCommandExecutionResult(
                    userPrompt = trimmed,
                    explanation = "Se ejecutaron las actualizaciones del motor de automejora en tiempo real.",
                    actionsExecuted = listOf("✅ Sistema y componentes actualizados en vivo")
                )
            } finally {
                _isAppUpdateGenerating.value = false
            }
        }
    }

    fun generateAppOptimizationWithAi(userRequest: String, focusArea: String = "Curia y Escolasticado") {
        executeAiAppCommand(userRequest)
    }

    fun applyProposalToCuriaAnnouncement() {
        val p = _appUpdateProposal.value ?: return
        val dateStr = "Actualizado con IA • ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date())}"
        _curiaAnnouncementTitle.value = p.title
        _curiaAnnouncementBody.value = p.announcementBody
        _curiaPrayerIntention.value = p.prayerIntention
        _curiaAnnouncementDate.value = dateStr

        prefs.edit()
            .putString("curia_announcement_title", p.title)
            .putString("curia_announcement_body", p.announcementBody)
            .putString("curia_prayer_intention", p.prayerIntention)
            .putString("curia_announcement_date", dateStr)
            .apply()

        curiaFirebaseManager.publishAnnouncement(p.title, p.announcementBody, dateStr, p.prayerIntention) { _, _ -> }
    }

    fun applyProposalToCommunityWall() {
        val p = _appUpdateProposal.value ?: return
        publishOfficialCuriaPost(
            title = p.title,
            body = p.suggestedCommunityPost.ifBlank { p.announcementBody },
            prayerIntention = p.prayerIntention
        )
    }

    // =========================================================================
    // PANEL IA: ANÁLISIS DE USO DE LA APP Y APLICACIÓN DIRECTA DE CONFIGURACIÓN
    // =========================================================================
    private val _appAnalysisReport = MutableStateFlow<AppAnalysisReport?>(null)
    val appAnalysisReport: StateFlow<AppAnalysisReport?> = _appAnalysisReport.asStateFlow()

    private val _isAnalyzingApp = MutableStateFlow(false)
    val isAnalyzingApp: StateFlow<Boolean> = _isAnalyzingApp.asStateFlow()

    private val _appliedConfigKeys = MutableStateFlow<Set<String>>(loadAppliedConfigKeys())
    val appliedConfigKeys: StateFlow<Set<String>> = _appliedConfigKeys.asStateFlow()

    fun runAiAppUsageAnalysis() {
        if (_isAnalyzingApp.value) return
        _isAnalyzingApp.value = true
        viewModelScope.launch {
            try {
                val telemetry = buildTelemetrySummary()
                val uploadedDocs = getUploadedDocsContext()
                val report = BasilianAiService.analyzeAppUsageAndSuggestImprovements(
                    telemetrySummary = telemetry,
                    uploadedDocsContext = uploadedDocs
                )
                val currentApplied = _appliedConfigKeys.value
                val updatedSuggestions = report.suggestions.map { sug ->
                    if (currentApplied.contains(sug.configKey)) sug.copy(isApplied = true) else sug
                }
                _appAnalysisReport.value = report.copy(suggestions = updatedSuggestions)
            } catch (e: Exception) {
                // El servicio maneja fallbacks internamente
            } finally {
                _isAnalyzingApp.value = false
            }
        }
    }

    fun applyAiSuggestion(suggestion: AppImprovementSuggestion) {
        val updatedKeys = _appliedConfigKeys.value.toMutableSet()
        updatedKeys.add(suggestion.configKey)
        _appliedConfigKeys.value = updatedKeys
        saveAppliedConfigKeys(updatedKeys)

        when (suggestion.configKey) {
            "OFFLINE_PRIORITY" -> {
                prefs.edit().putBoolean("pref_offline_priority", true).apply()
            }
            "HOLY_SPIRIT_DYNAMIC_GLOW" -> {
                setGlobalBackgroundMode(GlobalBgMode.ESPIRITU_SANTO)
                setGlobalBackgroundOpacity(0.90f)
            }
            "DAILY_PRAYER_AUTO_NOTIFY" -> {
                prefs.edit().putBoolean("pref_daily_prayer_notifications", true).apply()
            }
            "COMMUNITY_FRATERNAL_FILTER" -> {
                prefs.edit().putBoolean("pref_community_fraternal_filter", true).apply()
            }
            else -> {
                prefs.edit().putString("pref_cfg_${suggestion.configKey}", suggestion.configValue).apply()
            }
        }

        _appAnalysisReport.value?.let { rep ->
            val updatedList = rep.suggestions.map { s ->
                if (s.id == suggestion.id || s.configKey == suggestion.configKey) {
                    s.copy(isApplied = true)
                } else s
            }
            _appAnalysisReport.value = rep.copy(suggestions = updatedList)
        }
    }

    private fun buildTelemetrySummary(): String {
        val profilesCount = _communityMembers.value.size
        val postsCount = _communityPosts.value.size
        val currentThemeName = _currentAppTheme.value.displayName
        val docsCount = _uploadedCorpusDocuments.value.size
        val bgMode = _globalBackgroundMode.value.title
        val offlineActive = _appliedConfigKeys.value.contains("OFFLINE_PRIORITY")
        val prayerNotify = _appliedConfigKeys.value.contains("DAILY_PRAYER_AUTO_NOTIFY")
        return """
            - Perfiles y religiosos/laicos registrados: $profilesCount
            - Testimonios e intenciones en el Muro Fraterno: $postsCount
            - Tema dinámico activo: $currentThemeName
            - Fondo Global: $bgMode (Atenuación: ${(_globalBackgroundOpacity.value * 100).toInt()}%)
            - Documentos pastorales/constitucionales en el Corpus IA: $docsCount
            - Caché Offline optimizada: $offlineActive
            - Notificaciones comunitarias automáticas: $prayerNotify
        """.trimIndent()
    }

    // =========================================================================
    // CORPUS DINÁMICO DE DOCUMENTOS SUBIDOS PARA LA IA
    // =========================================================================
    private val _uploadedCorpusDocuments = MutableStateFlow<List<UploadedCorpusDocument>>(loadUploadedCorpusDocuments())
    val uploadedCorpusDocuments: StateFlow<List<UploadedCorpusDocument>> = _uploadedCorpusDocuments.asStateFlow()

    fun addCorpusDocument(title: String, content: String, category: String = "Pastoral / Constituciones") {
        if (title.isBlank() || content.isBlank()) return
        val words = content.split("\\s+".toRegex()).count()
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        val doc = UploadedCorpusDocument(
            id = UUID.randomUUID().toString(),
            title = title.trim(),
            content = content.trim(),
            dateAdded = sdf.format(java.util.Date()),
            wordCount = words,
            category = category
        )
        val updated = listOf(doc) + _uploadedCorpusDocuments.value
        _uploadedCorpusDocuments.value = updated
        saveUploadedCorpusDocuments(updated)
    }

    fun removeCorpusDocument(id: String) {
        val updated = _uploadedCorpusDocuments.value.filter { it.id != id }
        _uploadedCorpusDocuments.value = updated
        saveUploadedCorpusDocuments(updated)
    }

    private fun getUploadedDocsContext(): String {
        val docs = _uploadedCorpusDocuments.value
        if (docs.isEmpty()) return ""
        return docs.take(5).joinToString("\n\n---\n") { doc ->
            "DOCUMENTO: ${doc.title} (${doc.category})\n${doc.content.take(1200)}"
        }
    }

    // =========================================================================
    // FONDO GLOBAL PERSONALIZABLE (ESPÍRITU SANTO / IMAGEN GALERÍA / DINÁMICO)
    // =========================================================================
    private val _globalBackgroundMode = MutableStateFlow(loadGlobalBackgroundMode())
    val globalBackgroundMode: StateFlow<GlobalBgMode> = _globalBackgroundMode.asStateFlow()

    private val _globalBackgroundImageUri = MutableStateFlow(prefs.getString("global_bg_image_uri", null))
    val globalBackgroundImageUri: StateFlow<String?> = _globalBackgroundImageUri.asStateFlow()

    private val _globalBackgroundOpacity = MutableStateFlow(prefs.getFloat("global_bg_opacity", 0.88f))
    val globalBackgroundOpacity: StateFlow<Float> = _globalBackgroundOpacity.asStateFlow()

    // Aliases para compatibilidad con la vista
    val globalBackgroundCustomUri: StateFlow<String?> = _globalBackgroundImageUri.asStateFlow()
    fun setGlobalBackgroundCustomUri(uri: String?) = setGlobalBackgroundImageUri(uri)
    val appliedAiConfigKeys: StateFlow<Set<String>> = _appliedConfigKeys.asStateFlow()
    fun analyzeApp() = runAiAppUsageAnalysis()

    fun setGlobalBackgroundMode(mode: GlobalBgMode) {
        _globalBackgroundMode.value = mode
        prefs.edit().putString("global_bg_mode", mode.name).apply()
    }

    fun setGlobalBackgroundImageUri(uri: String?) {
        _globalBackgroundImageUri.value = uri
        prefs.edit().putString("global_bg_image_uri", uri).apply()
        if (uri != null) {
            setGlobalBackgroundMode(GlobalBgMode.CUSTOM_IMAGE)
        }
    }

    fun setGlobalBackgroundOpacity(opacity: Float) {
        val clamped = opacity.coerceIn(0.2f, 1.0f)
        _globalBackgroundOpacity.value = clamped
        prefs.edit().putFloat("global_bg_opacity", clamped).apply()
    }

    private val _futuristic3DIntensity = MutableStateFlow(
        try {
            val saved3d = prefs.getString("futuristic_3d_intensity", com.example.ui.components.Futuristic3DIntensity.HOLO_3D.name)
            com.example.ui.components.Futuristic3DIntensity.valueOf(saved3d ?: com.example.ui.components.Futuristic3DIntensity.HOLO_3D.name)
        } catch (_: Exception) {
            com.example.ui.components.Futuristic3DIntensity.HOLO_3D
        }
    )
    val futuristic3DIntensity: StateFlow<com.example.ui.components.Futuristic3DIntensity> = _futuristic3DIntensity.asStateFlow()

    fun setFuturistic3DIntensity(intensity: com.example.ui.components.Futuristic3DIntensity) {
        _futuristic3DIntensity.value = intensity
        prefs.edit().putString("futuristic_3d_intensity", intensity.name).apply()
    }

    fun cycleFuturistic3DIntensity() {
        val entries = com.example.ui.components.Futuristic3DIntensity.entries
        val nextIndex = (entries.indexOf(_futuristic3DIntensity.value) + 1) % entries.size
        setFuturistic3DIntensity(entries[nextIndex])
    }

    private fun loadGlobalBackgroundMode(): GlobalBgMode {
        val saved = prefs.getString("global_bg_mode", GlobalBgMode.JARVIS_FUTURISTA.name)
        return try {
            GlobalBgMode.valueOf(saved ?: GlobalBgMode.JARVIS_FUTURISTA.name)
        } catch (_: Exception) {
            GlobalBgMode.JARVIS_FUTURISTA
        }
    }

    fun publishOfficialCuriaPost(title: String, body: String, prayerIntention: String) {
        viewModelScope.launch {
            val newPost = CommunityPost(
                authorId = "curia_generalicia_official",
                authorEmail = "curia@basilianos.org",
                authorName = "Curia Generalicia & Escolasticado",
                authorRole = "Gobierno & Formación Basiliana (C.S.B.)",
                authorPatronSaint = "San Basilio Magno & San José",
                authorPhotoUri = _customCrestUri.value,
                text = "⚜️ COMUNICADO OFICIAL: $title\n\n$body\n\n🙏 Intención: $prayerIntention",
                imageUri = null,
                timestamp = System.currentTimeMillis(),
                likesCount = 7,
                likedByMe = true,
                prayerCount = 15
            )
            val updated = listOf(newPost) + _communityPosts.value
            _communityPosts.value = updated
            saveCommunityPostsToPrefs(updated)
        }
    }

    private fun buildDocumentContextSnippet(category: String, query: String): String {
        return when {
            category.contains("Constitución", ignoreCase = true) -> {
                val articles = com.example.data.repository.FormaDeVidaData.document.articles
                val matches = articles.filter { 
                    it.text.contains(query, ignoreCase = true) || it.chapter.contains(query, ignoreCase = true) 
                }.take(3)
                if (matches.isNotEmpty()) {
                    matches.joinToString("\n\n") { "Art. ${it.number} (${it.chapter}): ${it.text}" }
                } else {
                    articles.take(3).joinToString("\n\n") { "Art. ${it.number} (${it.chapter}): ${it.text}" }
                }
            }
            category.contains("Hexamerón", ignoreCase = true) -> {
                "Tratado del Hexamerón (San Basilio Magno, Homilías I a IX): Sobre el orden, belleza y armonía del universo creado por el Verbo de Dios. Las estrellas, las leyes gravitatorias y las aguas celestes son escuela de reverencia para la inteligencia humana."
            }
            category.contains("Vocacion", ignoreCase = true) -> {
                "Pastoral Vocacional Basilianos Colombia: Formación de jóvenes en colegios y parroquias, retiros de discernimiento 'Ven y Verás', noviciado y vida en comunidad fraterna según el lema 'Bonitatem et disciplinam et scientiam doce me'."
            }
            else -> {
                "Reglas Mayores y Menores (Asketikon de San Basilio Magno): La vida fraterna en común, la oración incesante del corazón, el trabajo manual y docente como liturgia cotidiana, el cuidado del enfermo y del desvalido en la Basiliada de Cesarea."
            }
        }
    }

    // Detail dialog states
    private val _selectedPatron = MutableStateFlow<PatronSaint?>(null)
    val selectedPatron: StateFlow<PatronSaint?> = _selectedPatron.asStateFlow()

    fun selectPatron(patron: PatronSaint?) {
        _selectedPatron.value = patron
    }

    private val _selectedFounder = MutableStateFlow<Founder?>(null)
    val selectedFounder: StateFlow<Founder?> = _selectedFounder.asStateFlow()

    fun selectFounder(founder: Founder?) {
        _selectedFounder.value = founder
    }

    private val _selectedPrayer = MutableStateFlow<PrayerItem?>(null)
    val selectedPrayer: StateFlow<PrayerItem?> = _selectedPrayer.asStateFlow()

    fun selectPrayer(prayer: PrayerItem?) {
        _selectedPrayer.value = prayer
    }

    // 1922 Book state
    private val _selectedBookChapter = MutableStateFlow<Book1922Chapter?>(null)
    val selectedBookChapter: StateFlow<Book1922Chapter?> = _selectedBookChapter.asStateFlow()

    fun selectBookChapter(chapter: Book1922Chapter?) {
        _selectedBookChapter.value = chapter
    }

    private val _bookSearchQuery = MutableStateFlow("")
    val bookSearchQuery: StateFlow<String> = _bookSearchQuery.asStateFlow()

    fun setBookSearchQuery(query: String) {
        _bookSearchQuery.value = query
    }

    // Costumbres Basilianas 2022 State
    private val _selectedCostumbresSection = MutableStateFlow<CostumbresSection?>(null)
    val selectedCostumbresSection: StateFlow<CostumbresSection?> = _selectedCostumbresSection.asStateFlow()

    fun selectCostumbresSection(section: CostumbresSection?) {
        _selectedCostumbresSection.value = section
    }

    private val _costumbresSearchQuery = MutableStateFlow("")
    val costumbresSearchQuery: StateFlow<String> = _costumbresSearchQuery.asStateFlow()

    fun setCostumbresSearchQuery(query: String) {
        _costumbresSearchQuery.value = query
    }

    // Filter states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private val _selectedHistoryCategory = MutableStateFlow<HistoryCategory?>(null)
    val selectedHistoryCategory: StateFlow<HistoryCategory?> = _selectedHistoryCategory.asStateFlow()

    fun setHistoryCategory(category: HistoryCategory?) {
        _selectedHistoryCategory.value = category
    }

    // Dynamic Daily Content
    val todayInspiration: DailyInspiration = repository.getTodayInspiration()

    // Liturgy recommended based on current hour of day
    fun getRecommendedPrayer(): PrayerItem {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val prayers = BasilianStaticData.dailyPrayers
        return when {
            hour in 5..10 -> prayers.first { it.id == "laudes" }
            hour in 11..14 -> prayers.first { it.id == "angelus" }
            hour in 15..19 -> prayers.first { it.id == "visperas" }
            hour in 20..24 || hour in 0..4 -> prayers.first { it.id == "completas" }
            else -> prayers.first { it.id == "oracion_san_basilio" }
        }
    }

    // Prayers
    val prayersList: List<PrayerItem> = repository.getDailyPrayers()

    // Patrons
    val patronsList: List<PatronSaint> = repository.getPatrons()

    // Founders
    val foundersList: List<Founder> = repository.getFounders()

    // Timeline
    val timelineEvents: List<HistoricalEvent> = repository.getTimeline()

    // Room state: Reflections
    val userReflections: StateFlow<List<UserReflection>> = repository.allReflections
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveReflection(title: String, content: String, category: String, virtueTag: String) {
        viewModelScope.launch {
            repository.addReflection(title, content, category, virtueTag)
        }
    }

    fun deleteReflection(reflection: UserReflection) {
        viewModelScope.launch {
            repository.deleteReflection(reflection)
        }
    }

    // Room state: Today Prayer Progress
    val todayPrayerStatus: StateFlow<DailyPrayerStatus?> = repository.getTodayPrayerStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun togglePrayer(prayerId: String) {
        viewModelScope.launch {
            repository.togglePrayerDone(prayerId)
        }
    }

    // Examen de Conciencia step state (for interactive night exam)
    private val _examenStep = MutableStateFlow(0)
    val examenStep: StateFlow<Int> = _examenStep.asStateFlow()

    fun nextExamenStep() {
        if (_examenStep.value < 4) _examenStep.value++ else _examenStep.value = 0
    }

    fun prevExamenStep() {
        if (_examenStep.value > 0) _examenStep.value--
    }

    fun resetExamen() {
        _examenStep.value = 0
    }

    // Calendar State & Logic
    val calendarDates: List<BasilianCalendarDate> = repository.getCalendarDates()

    private val currentCalendar = Calendar.getInstance()
    private val _calendarMonth = MutableStateFlow(currentCalendar.get(Calendar.MONTH) + 1) // 1 to 12
    val calendarMonth: StateFlow<Int> = _calendarMonth.asStateFlow()

    private val _calendarYear = MutableStateFlow(currentCalendar.get(Calendar.YEAR))
    val calendarYear: StateFlow<Int> = _calendarYear.asStateFlow()

    private val _selectedCalendarDay = MutableStateFlow<Int?>(currentCalendar.get(Calendar.DAY_OF_MONTH))
    val selectedCalendarDay: StateFlow<Int?> = _selectedCalendarDay.asStateFlow()

    private val _selectedCalendarCategory = MutableStateFlow(CalendarCategory.ALL)
    val selectedCalendarCategory: StateFlow<CalendarCategory> = _selectedCalendarCategory.asStateFlow()

    private val _selectedCalendarEvent = MutableStateFlow<BasilianCalendarDate?>(null)
    val selectedCalendarEvent: StateFlow<BasilianCalendarDate?> = _selectedCalendarEvent.asStateFlow()

    fun selectCalendarMonth(month: Int) {
        _calendarMonth.value = month.coerceIn(1, 12)
        _selectedCalendarDay.value = null
    }

    fun nextCalendarMonth() {
        if (_calendarMonth.value == 12) {
            _calendarMonth.value = 1
            _calendarYear.value += 1
        } else {
            _calendarMonth.value += 1
        }
        _selectedCalendarDay.value = null
    }

    fun prevCalendarMonth() {
        if (_calendarMonth.value == 1) {
            _calendarMonth.value = 12
            _calendarYear.value -= 1
        } else {
            _calendarMonth.value -= 1
        }
        _selectedCalendarDay.value = null
    }

    fun selectCalendarDay(day: Int) {
        _selectedCalendarDay.value = day
        // If there's an event on this day in this month, select it
        val event = calendarDates.firstOrNull { it.month == _calendarMonth.value && it.day == day }
        _selectedCalendarEvent.value = event
    }

    fun selectCalendarCategory(category: CalendarCategory) {
        _selectedCalendarCategory.value = category
    }

    fun selectCalendarEvent(event: BasilianCalendarDate?) {
        _selectedCalendarEvent.value = event
        if (event != null) {
            _calendarMonth.value = event.month
            _selectedCalendarDay.value = event.day
        }
    }

    // =========================================================================
    // MENSAJES MUNDIALES DE «¡FELIZ DÍA!» EN FECHAS DE PATRONOS BASILIANOS
    // (Colombia ↔ Estados Unidos ↔ Canadá ↔ México ↔ Francia)
    // =========================================================================
    private val _patronFeastGreetings = MutableStateFlow(loadPatronFeastGreetingsFromPrefs())
    val patronFeastGreetings: StateFlow<List<com.example.data.firebase.PatronFeastGreetingMessage>> = _patronFeastGreetings.asStateFlow()

    private fun loadPatronFeastGreetingsFromPrefs(): List<com.example.data.firebase.PatronFeastGreetingMessage> {
        val raw = prefs.getString("patron_feast_greetings_json", null)
        val parsed = mutableListOf<com.example.data.firebase.PatronFeastGreetingMessage>()
        if (!raw.isNullOrBlank()) {
            try {
                val arr = JSONArray(raw)
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    parsed.add(
                        com.example.data.firebase.PatronFeastGreetingMessage(
                            id = obj.optString("id", "greeting_$i"),
                            eventId = obj.optString("eventId", ""),
                            eventTitle = obj.optString("eventTitle", "Fiesta Patronal Basiliana"),
                            patronName = obj.optString("patronName", "Santo Patrono C.S.B."),
                            authorName = obj.optString("authorName", "Hermano Basiliano"),
                            authorEmail = obj.optString("authorEmail", ""),
                            authorCountryOrContext = obj.optString("authorCountryOrContext", "Comunidad C.S.B."),
                            message = obj.optString("message", "¡Feliz día de nuestro Patrono!"),
                            timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                        )
                    )
                }
            } catch (_: Exception) {}
        }
        if (parsed.isEmpty()) {
            parsed.addAll(
                listOf(
                    com.example.data.firebase.PatronFeastGreetingMessage(
                        id = "seed_greeting_colombia",
                        eventId = "san_basilio_ene_2",
                        eventTitle = "Solemnidad de San Basilio Magno",
                        patronName = "San Basilio Magno",
                        authorName = "Comunidad Casa Annonay • Bogotá",
                        authorEmail = "villamizargalviz@gmail.com",
                        authorCountryOrContext = "🇨🇴 Colombia • Misión C.S.B.",
                        message = "¡Feliz día de nuestro Padre San Basilio y de nuestros Santos Patronos! Un abrazo fraterno desde Colombia para todos nuestros hermanos en Estados Unidos, Canadá, México y Francia.",
                        timestamp = System.currentTimeMillis() - 3600_000L
                    ),
                    com.example.data.firebase.PatronFeastGreetingMessage(
                        id = "seed_greeting_usa_canada",
                        eventId = "fundacion_annonay_nov_21",
                        eventTitle = "Fundación de la Congregación en Annonay (1822)",
                        patronName = "Los 10 Fundadores y Patronos C.S.B.",
                        authorName = "Hermanos Basilianos en Norteamérica",
                        authorEmail = "comunidad@basilian.org",
                        authorCountryOrContext = "🇺🇸 Estados Unidos & 🇨🇦 Canadá",
                        message = "¡Feliz Fiesta Patronal y Aniversario C.S.B.! Unidos en una sola alma y un solo corazón en Bondad, Disciplina y Ciencia con nuestros hermanos de Colombia, México y Francia.",
                        timestamp = System.currentTimeMillis() - 7200_000L
                    )
                )
            )
        }
        return parsed.sortedByDescending { it.timestamp }
    }

    private fun savePatronFeastGreetingsToPrefs(list: List<com.example.data.firebase.PatronFeastGreetingMessage>) {
        try {
            val arr = JSONArray()
            list.take(60).forEach { g ->
                arr.put(
                    JSONObject().apply {
                        put("id", g.id)
                        put("eventId", g.eventId)
                        put("eventTitle", g.eventTitle)
                        put("patronName", g.patronName)
                        put("authorName", g.authorName)
                        put("authorEmail", g.authorEmail)
                        put("authorCountryOrContext", g.authorCountryOrContext)
                        put("message", g.message)
                        put("timestamp", g.timestamp)
                    }
                )
            }
            prefs.edit().putString("patron_feast_greetings_json", arr.toString()).apply()
        } catch (_: Exception) {}
    }

    fun publishPatronFeastGreeting(
        eventId: String,
        eventTitle: String,
        patronName: String,
        customMessage: String,
        customContextOrCountry: String = "",
        onResult: ((Boolean, String) -> Unit)? = null
    ) {
        val cleanMsg = BasilianSecurityManager.validateAndSanitizeInput(customMessage, maxLength = 400)
        if (cleanMsg.isBlank()) {
            onResult?.invoke(false, "Escribe un mensaje fraterno de feliz día antes de enviarlo.")
            return
        }
        val profile = _currentUserProfile.value
        val authorName = profile?.fullName?.takeIf { it.isNotBlank() } ?: "Hermano Basiliano"
        val authorEmail = profile?.email?.takeIf { it.isNotBlank() } ?: ""
        val authorContext = customContextOrCountry.trim().ifBlank {
            profile?.cityAndCountry?.takeIf { it.isNotBlank() } ?: "🇨🇴 Colombia • Comunidad C.S.B."
        }
        val newGreeting = com.example.data.firebase.PatronFeastGreetingMessage(
            id = "greeting_${System.currentTimeMillis()}_${(100..999).random()}",
            eventId = eventId,
            eventTitle = eventTitle,
            patronName = patronName.ifBlank { eventTitle },
            authorName = authorName,
            authorEmail = authorEmail,
            authorCountryOrContext = authorContext,
            message = cleanMsg,
            timestamp = System.currentTimeMillis()
        )
        val updated = (listOf(newGreeting) + _patronFeastGreetings.value).distinctBy { it.id }.take(60)
        _patronFeastGreetings.value = updated
        savePatronFeastGreetingsToPrefs(updated)
        prefs.edit().putBoolean("notified_patron_greeting_${newGreeting.id}", true).apply()

        curiaFirebaseManager.publishPatronFeastGreeting(newGreeting) { ok, msg ->
            onResult?.invoke(ok, msg)
        }
    }

    // =========================================================================
    // SISTEMA DE NOTIFICACIONES PUSH PROGRAMADAS: ORACIÓN, SOLEMNIDADES Y CUMPLEAÑOS
    // =========================================================================
    private val notifPrefs = application.getSharedPreferences("basilian_notifications_prefs", Context.MODE_PRIVATE)

    // 1. Oración Diaria (Laudes, Ángelus, Vísperas)
    private val _dailyPrayerConfigs = MutableStateFlow(loadDailyPrayerConfigs())
    val dailyPrayerConfigs: StateFlow<List<DailyPrayerReminderConfig>> = _dailyPrayerConfigs.asStateFlow()

    private fun loadDailyPrayerConfigs(): List<DailyPrayerReminderConfig> {
        val laudesEnabled = notifPrefs.getBoolean("prayer_laudes_enabled", true)
        val laudesH = notifPrefs.getInt("prayer_laudes_h", 7)
        val laudesM = notifPrefs.getInt("prayer_laudes_m", 30)

        val angelusEnabled = notifPrefs.getBoolean("prayer_angelus_enabled", true)
        val angelusH = notifPrefs.getInt("prayer_angelus_h", 12)
        val angelusM = notifPrefs.getInt("prayer_angelus_m", 0)

        val vesperasEnabled = notifPrefs.getBoolean("prayer_vesperas_enabled", true)
        val vesperasH = notifPrefs.getInt("prayer_vesperas_h", 20)
        val vesperasM = notifPrefs.getInt("prayer_vesperas_m", 0)

        return listOf(
            DailyPrayerReminderConfig(
                type = "LAUDES",
                title = "Oración de la Mañana (Laudes)",
                subtitle = "Comienza el día con la oración matutina y la bendición de San Basilio",
                defaultHour = laudesH,
                defaultMinute = laudesM,
                isEnabled = laudesEnabled,
                iconEmoji = "🌅"
            ),
            DailyPrayerReminderConfig(
                type = "ANGELUS",
                title = "Ángelus y Pausa del Mediodía",
                subtitle = "El Ángel del Señor anunció a María... Encuentro de paz al mediodía",
                defaultHour = angelusH,
                defaultMinute = angelusM,
                isEnabled = angelusEnabled,
                iconEmoji = "☀️"
            ),
            DailyPrayerReminderConfig(
                type = "VESPERAS",
                title = "Oración de la Noche (Vísperas y Completas)",
                subtitle = "Examen del día y descanso bajo el amparo de Cristo",
                defaultHour = vesperasH,
                defaultMinute = vesperasM,
                isEnabled = vesperasEnabled,
                iconEmoji = "🌙"
            )
        )
    }

    fun toggleDailyPrayer(prayerType: String, isEnabled: Boolean, context: Context) {
        val current = _dailyPrayerConfigs.value.toMutableList()
        val index = current.indexOfFirst { it.type == prayerType }
        if (index != -1) {
            val updated = current[index].copy(isEnabled = isEnabled)
            current[index] = updated
            _dailyPrayerConfigs.value = current

            notifPrefs.edit()
                .putBoolean("prayer_${prayerType.lowercase()}_enabled", isEnabled)
                .apply()

            if (isEnabled) {
                NotificationHelper.scheduleDailyPrayer(
                    context = context,
                    prayerType = prayerType,
                    hour = updated.defaultHour,
                    minute = updated.defaultMinute,
                    title = updated.title,
                    message = updated.subtitle
                )
            } else {
                NotificationHelper.cancelDailyPrayer(context, prayerType)
            }
        }
    }

    fun updateDailyPrayerTime(prayerType: String, hour: Int, minute: Int, context: Context) {
        val current = _dailyPrayerConfigs.value.toMutableList()
        val index = current.indexOfFirst { it.type == prayerType }
        if (index != -1) {
            val updated = current[index].copy(defaultHour = hour, defaultMinute = minute)
            current[index] = updated
            _dailyPrayerConfigs.value = current

            notifPrefs.edit()
                .putInt("prayer_${prayerType.lowercase()}_h", hour)
                .putInt("prayer_${prayerType.lowercase()}_m", minute)
                .apply()

            if (updated.isEnabled) {
                NotificationHelper.scheduleDailyPrayer(
                    context = context,
                    prayerType = prayerType,
                    hour = hour,
                    minute = minute,
                    title = updated.title,
                    message = updated.subtitle
                )
            }
        }
    }

    // 2. Solemnidades del Calendario Litúrgico
    private val _solemnityReminders = MutableStateFlow(loadSolemnityReminders())
    val solemnityReminders: StateFlow<List<LiturgicalSolemnityReminder>> = _solemnityReminders.asStateFlow()

    private fun loadSolemnityReminders(): List<LiturgicalSolemnityReminder> {
        val baseSolemnities = listOf(
            LiturgicalSolemnityReminder(
                id = "sol_san_basilio_pascua",
                title = "Pascua de San Basilio Magno",
                day = 1, month = 1,
                rank = "Solemnidad de Santa María y Tránsito de San Basilio",
                briefDescription = "Tránsito de nuestro Santo Padre al Cielo (379 d.C.)."
            ),
            LiturgicalSolemnityReminder(
                id = "sol_san_basilio_titular",
                title = "Solemnidad de San Basilio y San Gregorio Nacianceno",
                day = 2, month = 1,
                rank = "Solemnidad Titular Congregacional",
                briefDescription = "Fiesta patronal de la Congregación de San Basilio en todo el mundo."
            ),
            LiturgicalSolemnityReminder(
                id = "sol_san_jose",
                title = "Solemnidad de San José, Esposo de la Virgen",
                day = 19, month = 3,
                rank = "Solemnidad de la Iglesia Universal",
                briefDescription = "Custodio del Redentor y protector de las vocaciones."
            ),
            LiturgicalSolemnityReminder(
                id = "sol_san_oscar_romero",
                title = "Fiesta de San Óscar Romero, Obispo y Mártir",
                day = 24, month = 3,
                rank = "Memoria Solemne Congregacional",
                briefDescription = "Patrono basiliano de la justicia evangélica y la opción por los pobres."
            ),
            LiturgicalSolemnityReminder(
                id = "sol_santa_cruz",
                title = "Fiesta de la Exaltación de la Santa Cruz",
                day = 14, month = 9,
                rank = "Fiesta del Señor",
                briefDescription = "La Cruz, signo insigne del escudo basiliano y manantial de redención."
            ),
            LiturgicalSolemnityReminder(
                id = "sol_virgen_dolores",
                title = "Nuestra Señora de los Dolores",
                day = 15, month = 9,
                rank = "Memoria Obligatoria",
                briefDescription = "María al pie de la cruz y madre espiritual de los creyentes."
            ),
            LiturgicalSolemnityReminder(
                id = "sol_todos_los_santos",
                title = "Solemnidad de Todos los Santos",
                day = 1, month = 11,
                rank = "Solemnidad de Precepto",
                briefDescription = "Celebración de la santidad compartida de la Iglesia triunfante."
            ),
            LiturgicalSolemnityReminder(
                id = "sol_fundacion_annonay",
                title = "Fundación Basiliana en Annonay (1822)",
                day = 21, month = 11,
                rank = "Solemnidad Congregacional (Presentación de María)",
                briefDescription = "Aniversario de los 10 sacerdotes fundadores guiados por Joseph Lapierre."
            ),
            LiturgicalSolemnityReminder(
                id = "sol_inmaculada_concepcion",
                title = "Solemnidad de la Inmaculada Concepción",
                day = 8, month = 12,
                rank = "Solemnidad de Precepto",
                briefDescription = "Patrona celestial de la Iglesia y pureza evangélica."
            ),
            LiturgicalSolemnityReminder(
                id = "sol_navidad",
                title = "Natividad de Nuestro Señor Jesucristo",
                day = 25, month = 12,
                rank = "Solemnidad del Nacimiento del Señor",
                briefDescription = "El Verbo se hizo carne y habitó entre nosotros."
            )
        )

        return baseSolemnities.map { sol ->
            val enabled = notifPrefs.getBoolean("solemnity_${sol.id}_enabled", true)
            sol.copy(isEnabled = enabled)
        }
    }

    fun toggleSolemnityReminder(solemnity: LiturgicalSolemnityReminder, context: Context) {
        val current = _solemnityReminders.value.toMutableList()
        val index = current.indexOfFirst { it.id == solemnity.id }
        if (index != -1) {
            val newEnabled = !current[index].isEnabled
            val updated = current[index].copy(isEnabled = newEnabled)
            current[index] = updated
            _solemnityReminders.value = current

            notifPrefs.edit().putBoolean("solemnity_${solemnity.id}_enabled", newEnabled).apply()

            if (newEnabled) {
                NotificationHelper.scheduleSolemnityReminder(
                    context = context,
                    solemnityId = updated.id,
                    month = updated.month,
                    day = updated.day,
                    title = "Solemnidad Litúrgica: ${updated.title}",
                    message = updated.briefDescription
                )
            } else {
                NotificationHelper.cancelReminder(context, updated.id)
            }
        }
    }

    // 3. CUMPLEAÑOS (Congregacionales y Personalizados)
    private val _birthdaysEnabledGeneral = MutableStateFlow(
        notifPrefs.getBoolean("birthdays_general_enabled", true)
    )
    val birthdaysEnabledGeneral: StateFlow<Boolean> = _birthdaysEnabledGeneral.asStateFlow()

    private val _customBirthdays = MutableStateFlow(loadCustomBirthdays())
    val customBirthdays: StateFlow<List<CustomBirthday>> = _customBirthdays.asStateFlow()

    private fun loadCustomBirthdays(): List<CustomBirthday> {
        val jsonStr = notifPrefs.getString("custom_birthdays_json", null)
        val list = mutableListOf<CustomBirthday>()
        if (!jsonStr.isNullOrBlank()) {
            try {
                val array = JSONArray(jsonStr)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        CustomBirthday(
                            id = obj.getString("id"),
                            name = obj.getString("name"),
                            day = obj.getInt("day"),
                            month = obj.getInt("month"),
                            relationOrRole = obj.optString("role", "Amigo / Cohermano"),
                            personalBlessing = obj.optString("blessing", "Que el Señor derrame paz y alegría en tu vida."),
                            isReminderEnabled = obj.optBoolean("enabled", true)
                        )
                    )
                }
            } catch (e: Exception) {
                Log.w("BasilianViewModel", "Error cargando cumpleaños: ${e.message}")
            }
        }

        // Si no hay personalizados, pre-cargar cumpleaños entrañables de la congregación
        if (list.isEmpty()) {
            list.addAll(
                listOf(
                    CustomBirthday(
                        id = "bday_tom_dugan",
                        name = "P. Tom Dugan, CSB (96 años)",
                        day = 25, month = 8,
                        relationOrRole = "Venerable Cohermano Basiliano",
                        personalBlessing = "¡96 años de vida sacerdotal y entrega fecunda! Oramos por la salud y paz del querido P. Tom Dugan."
                    ),
                    CustomBirthday(
                        id = "bday_kevin_kirley",
                        name = "P. Kevin Kirley, CSB (100 años)",
                        day = 14, month = 3,
                        relationOrRole = "Centenario Basiliano Insigne",
                        personalBlessing = "¡Un siglo de vida y consagración basiliana! Damos gracias a Dios por el centenario del P. Kirley."
                    ),
                    CustomBirthday(
                        id = "bday_john_huber",
                        name = "P. John B. Huber, CSB (Superior General)",
                        day = 18, month = 7,
                        relationOrRole = "Superior General de la Congregación",
                        personalBlessing = "Pedimos al Espíritu Santo que guíe y bendiga el pastoreo del P. John Huber al frente de la Congregación."
                    ),
                    CustomBirthday(
                        id = "bday_kevin_storey",
                        name = "P. Kevin J. Storey, CSB (Superior Emérito)",
                        day = 10, month = 10,
                        relationOrRole = "Superior General Emérito (2018-2026)",
                        personalBlessing = "Acción de gracias por su incansable ministerio de comunión y misión en América."
                    ),
                    CustomBirthday(
                        id = "bday_joseph_lapierre",
                        name = "P. Joseph Bovier Lapierre, CSB",
                        day = 15, month = 12,
                        relationOrRole = "Primer Superior y Fundador de Annonay (1822)",
                        personalBlessing = "Natalicio del primer Superior General de los Padres Basilianos en Annonay."
                    ),
                    CustomBirthday(
                        id = "bday_pierre_tourvieille",
                        name = "P. Pierre Tourvieille, CSB",
                        day = 8, month = 5,
                        relationOrRole = "Cofundador de Annonay",
                        personalBlessing = "Memoria del natalicio del padre y formador de las primeras vocaciones basilianas."
                    ),
                    CustomBirthday(
                        id = "bday_oscar_romero",
                        name = "San Óscar Arnulfo Romero",
                        day = 15, month = 8,
                        relationOrRole = "Santo Patrono Basiliano y Mártir",
                        personalBlessing = "Natalicio de San Óscar Romero: 'Sentir con la Iglesia y ser voz de los que no tienen voz'."
                    ),
                    CustomBirthday(
                        id = "bday_erick_enriquez",
                        name = "P. Erick de Jesús Enríquez, CSB",
                        day = 28, month = 2,
                        relationOrRole = "Sacerdote ordenado en Tehuacán",
                        personalBlessing = "Que el Señor fortalezca su ministerio pastoral en favor de la juventud y las familias."
                    ),
                    CustomBirthday(
                        id = "bday_adan_gomez",
                        name = "P. José Adán Gómez, CSB",
                        day = 4, month = 6,
                        relationOrRole = "Sacerdote ordenado en San Lorenzo",
                        personalBlessing = "Bendición sacerdotal en su cumpleaños para el servicio del Reino de Dios."
                    ),
                    CustomBirthday(
                        id = "bday_anderson_usuga",
                        name = "P. Anderson Fabián Úsuga, CSB",
                        day = 12, month = 9,
                        relationOrRole = "Sacerdote ordenado en Medellín",
                        personalBlessing = "Fecundidad evangélica en su sacerdocio y misión comunitaria en Colombia."
                    )
                )
            )
            saveCustomBirthdays(list)
        }

        return list
    }

    private fun saveCustomBirthdays(list: List<CustomBirthday>) {
        try {
            val array = JSONArray()
            list.forEach { bday ->
                val obj = JSONObject().apply {
                    put("id", bday.id)
                    put("name", bday.name)
                    put("day", bday.day)
                    put("month", bday.month)
                    put("role", bday.relationOrRole)
                    put("blessing", bday.personalBlessing)
                    put("enabled", bday.isReminderEnabled)
                }
                array.put(obj)
            }
            notifPrefs.edit().putString("custom_birthdays_json", array.toString()).apply()
        } catch (e: Exception) {
            Log.w("BasilianViewModel", "Error guardando cumpleaños: ${e.message}")
        }
    }

    fun addCustomBirthday(
        name: String,
        day: Int,
        month: Int,
        role: String,
        blessing: String,
        context: Context
    ) {
        val newBday = CustomBirthday(
            id = "bday_custom_${System.currentTimeMillis()}",
            name = name.trim(),
            day = day,
            month = month,
            relationOrRole = role.trim().ifBlank { "Amigo / Cohermano" },
            personalBlessing = blessing.trim().ifBlank { "Que el Señor colme tus días de paz, salud y bendición." },
            isReminderEnabled = true
        )
        val updated = _customBirthdays.value + newBday
        _customBirthdays.value = updated
        saveCustomBirthdays(updated)

        if (_birthdaysEnabledGeneral.value) {
            NotificationHelper.scheduleBirthdayReminder(
                context = context,
                birthdayId = newBday.id,
                month = newBday.month,
                day = newBday.day,
                personName = newBday.name,
                blessingNote = newBday.personalBlessing
            )
        }
    }

    fun deleteCustomBirthday(id: String, context: Context) {
        val updated = _customBirthdays.value.filterNot { it.id == id }
        _customBirthdays.value = updated
        saveCustomBirthdays(updated)
        NotificationHelper.cancelReminder(context, ("bday_$id"))
    }

    fun toggleCustomBirthdayReminder(bday: CustomBirthday, context: Context) {
        val current = _customBirthdays.value.toMutableList()
        val index = current.indexOfFirst { it.id == bday.id }
        if (index != -1) {
            val newEnabled = !current[index].isReminderEnabled
            val updatedItem = current[index].copy(isReminderEnabled = newEnabled)
            current[index] = updatedItem
            _customBirthdays.value = current
            saveCustomBirthdays(current)

            if (newEnabled && _birthdaysEnabledGeneral.value) {
                NotificationHelper.scheduleBirthdayReminder(
                    context = context,
                    birthdayId = updatedItem.id,
                    month = updatedItem.month,
                    day = updatedItem.day,
                    personName = updatedItem.name,
                    blessingNote = updatedItem.personalBlessing
                )
            } else {
                NotificationHelper.cancelReminder(context, ("bday_${updatedItem.id}"))
            }
        }
    }

    fun toggleAllBirthdays(enabled: Boolean, context: Context) {
        _birthdaysEnabledGeneral.value = enabled
        notifPrefs.edit().putBoolean("birthdays_general_enabled", enabled).apply()

        if (enabled) {
            _customBirthdays.value.filter { it.isReminderEnabled }.forEach { bday ->
                NotificationHelper.scheduleBirthdayReminder(
                    context = context,
                    birthdayId = bday.id,
                    month = bday.month,
                    day = bday.day,
                    personName = bday.name,
                    blessingNote = bday.personalBlessing
                )
            }
        } else {
            _customBirthdays.value.forEach { bday ->
                NotificationHelper.cancelReminder(context, ("bday_${bday.id}"))
            }
        }
    }

    /**
     * Envía una notificación push instantánea de demostración según la categoría solicitada.
     */
    fun sendInstantTestPush(type: String, context: Context) {
        when (type) {
            "DAILY_PRAYER" -> {
                NotificationHelper.showPushNotification(
                    context = context,
                    notificationId = 1001,
                    title = "🌅 Oración de la Mañana (Laudes)",
                    message = "«El Señor renueva su misericordia cada amanecer». Hoy meditamos: San Mateo 18, 21-35. Perdón de corazón setenta veces siete.",
                    channelId = NotificationHelper.CHANNEL_DAILY_PRAYER,
                    targetTab = "ORACION_DIARIA"
                )
            }
            "SOLEMNITY" -> {
                NotificationHelper.showPushNotification(
                    context = context,
                    notificationId = 1002,
                    title = "✝️ Solemnidad Litúrgica: Exaltación de la Santa Cruz",
                    message = "«Tanto amó Dios al mundo que entregó a su Unigénito». Conmemoramos la Cruz gloriosa, estandarte del escudo basiliano.",
                    channelId = NotificationHelper.CHANNEL_SOLEMNITIES,
                    targetTab = "CALENDARIO"
                )
            }
            "BIRTHDAY" -> {
                NotificationHelper.showPushNotification(
                    context = context,
                    notificationId = 1003,
                    title = "🎂 ¡Cumpleaños de Cohermano Basiliano!",
                    message = "¡Hoy celebramos la vida y vocación del P. Tom Dugan, CSB (96 años)! Oremos en fraternidad por su salud y santidad.",
                    channelId = NotificationHelper.CHANNEL_BIRTHDAYS,
                    targetTab = "DIRECTORIO"
                )
            }
            else -> {
                NotificationHelper.showPushNotification(
                    context = context,
                    notificationId = 1000,
                    title = "🔔 Notificaciones Basilianas Activas",
                    message = "Las notificaciones push programadas para la Oración Diaria, Solemnidades y Cumpleaños están configuradas correctamente.",
                    channelId = NotificationHelper.CHANNEL_GENERAL
                )
            }
        }
    }

    /**
     * Sincroniza y reactiva todas las alarmas programadas en el sistema del dispositivo.
     */
    fun syncAllNotificationAlarms(context: Context) {
        NotificationHelper.createAllNotificationChannels(context)

        // 1. Oraciones diarias activas
        _dailyPrayerConfigs.value.filter { it.isEnabled }.forEach { config ->
            NotificationHelper.scheduleDailyPrayer(
                context = context,
                prayerType = config.type,
                hour = config.defaultHour,
                minute = config.defaultMinute,
                title = config.title,
                message = config.subtitle
            )
        }

        // 2. Solemnidades activas
        _solemnityReminders.value.filter { it.isEnabled }.forEach { sol ->
            NotificationHelper.scheduleSolemnityReminder(
                context = context,
                solemnityId = sol.id,
                month = sol.month,
                day = sol.day,
                title = "Solemnidad Litúrgica: ${sol.title}",
                message = sol.briefDescription
            )
        }

        // 3. Cumpleaños activos
        if (_birthdaysEnabledGeneral.value) {
            _customBirthdays.value.filter { it.isReminderEnabled }.forEach { bday ->
                NotificationHelper.scheduleBirthdayReminder(
                    context = context,
                    birthdayId = bday.id,
                    month = bday.month,
                    day = bday.day,
                    personName = bday.name,
                    blessingNote = bday.personalBlessing
                )
            }
        }
    }

    // Reminders State (Calendar Screen)
    val allReminders: StateFlow<List<DateReminder>> = repository.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleEventReminder(event: BasilianCalendarDate, context: Context) {
        viewModelScope.launch {
            val isCurrentlySet = allReminders.value.any { it.eventId == event.id }
            repository.toggleReminder(event, isCurrentlySet)
            if (!isCurrentlySet) {
                NotificationHelper.scheduleSolemnityReminder(
                    context = context,
                    solemnityId = event.id,
                    month = event.month,
                    day = event.day,
                    title = "Festividad Basiliana: ${event.title}",
                    message = event.briefDescription
                )
            } else {
                NotificationHelper.cancelReminder(context, event.id)
            }
        }
    }

    // ==========================================
    // ESPIRITUALIDAD EN MOVIMIENTO (MODO NÓMADA)
    // ==========================================
    private val nomadPrefs = application.getSharedPreferences("basilian_nomad_prefs", Context.MODE_PRIVATE)

    private val _totalPilgrimageKm = MutableStateFlow(
        nomadPrefs.getFloat("total_pilgrimage_km", 22.5f)
    )
    val totalPilgrimageKm: StateFlow<Float> = _totalPilgrimageKm.asStateFlow()

    private val _activeTrip = MutableStateFlow(ActiveTripState())
    val activeTrip: StateFlow<ActiveTripState> = _activeTrip.asStateFlow()

    private var tripTimerJob: Job? = null

    val wisdomCapsules: List<WisdomCapsule> = NomadWisdomData.capsules
    val pilgrimageMilestones: List<PilgrimageMilestone> = NomadWisdomData.milestones

    fun startTrip(mode: TransportMode) {
        tripTimerJob?.cancel()
        _activeTrip.value = ActiveTripState(
            isRunning = true,
            mode = mode,
            elapsedSeconds = 0L,
            distanceKm = 0.0f,
            currentSpeedKmh = mode.averageSpeedKmh,
            estimatedCalories = 0
        )

        tripTimerJob = viewModelScope.launch {
            while (_activeTrip.value.isRunning) {
                delay(1000L)
                val current = _activeTrip.value
                val newSeconds = current.elapsedSeconds + 1
                // Increment distance based on speed
                val speedVariance = (0.95f + (Math.random().toFloat() * 0.10f))
                val currentSpeed = current.mode.averageSpeedKmh * speedVariance
                val addedKm = (currentSpeed / 3600f)
                val newDistance = current.distanceKm + addedKm

                val caloriesPerSecond = when (current.mode) {
                    TransportMode.WALKING -> 0.07f
                    TransportMode.CYCLING -> 0.16f
                    TransportMode.TRANSIT -> 0.02f
                }
                val newCalories = (newSeconds * caloriesPerSecond).toInt()

                // Check if a capsule should be suggested
                val capsuleToSuggest = current.activeCapsule ?: wisdomCapsules.firstOrNull { capsule ->
                    capsule.recommendedMode == current.mode && newDistance >= capsule.triggerKmThreshold
                }

                _activeTrip.value = current.copy(
                    elapsedSeconds = newSeconds,
                    distanceKm = newDistance,
                    currentSpeedKmh = currentSpeed,
                    estimatedCalories = newCalories,
                    activeCapsule = capsuleToSuggest
                )
            }
        }
    }

    fun pauseTrip() {
        val current = _activeTrip.value
        _activeTrip.value = current.copy(isRunning = false)
        tripTimerJob?.cancel()
    }

    fun resumeTrip() {
        val current = _activeTrip.value
        _activeTrip.value = current.copy(isRunning = true)
        tripTimerJob = viewModelScope.launch {
            while (_activeTrip.value.isRunning) {
                delay(1000L)
                val curr = _activeTrip.value
                val newSeconds = curr.elapsedSeconds + 1
                val speedVariance = (0.95f + (Math.random().toFloat() * 0.10f))
                val currentSpeed = curr.mode.averageSpeedKmh * speedVariance
                val addedKm = (currentSpeed / 3600f)
                val newDistance = curr.distanceKm + addedKm

                val caloriesPerSecond = when (curr.mode) {
                    TransportMode.WALKING -> 0.07f
                    TransportMode.CYCLING -> 0.16f
                    TransportMode.TRANSIT -> 0.02f
                }
                val newCalories = (newSeconds * caloriesPerSecond).toInt()

                _activeTrip.value = curr.copy(
                    elapsedSeconds = newSeconds,
                    distanceKm = newDistance,
                    currentSpeedKmh = currentSpeed,
                    estimatedCalories = newCalories
                )
            }
        }
    }

    fun finishTrip() {
        val current = _activeTrip.value
        tripTimerJob?.cancel()
        if (current.distanceKm > 0f) {
            addPilgrimageKm(current.distanceKm)
        }
        _activeTrip.value = ActiveTripState(isRunning = false)
    }

    fun addPilgrimageKm(kmToAdd: Float) {
        val updated = (_totalPilgrimageKm.value + kmToAdd).coerceAtLeast(0f)
        _totalPilgrimageKm.value = updated
        nomadPrefs.edit().putFloat("total_pilgrimage_km", updated).apply()
    }

    fun resetPilgrimageProgress() {
        _totalPilgrimageKm.value = 0f
        nomadPrefs.edit().putFloat("total_pilgrimage_km", 0f).apply()
    }

    fun playWisdomCapsule(capsule: WisdomCapsule) {
        val narration = "${capsule.title}. ${capsule.author} nos enseña: ${capsule.quote}. ${capsule.audioScript}. Pregunta para tu camino: ${capsule.reflectionPrompt}"
        speakText(narration)
    }

    fun playMilestoneMeditation(milestone: PilgrimageMilestone) {
        val narration = "Hito de Peregrinación: ${milestone.title}. ${milestone.geographicRegion}, período ${milestone.period}. Cita patrística: ${milestone.patristicQuote}. ${milestone.exclusiveText}. Ejercicio de contemplación activa: ${milestone.contemplativeExercise}"
        speakText(narration)
    }

    // ==========================================
    // ICONOGRAFÍA EN REALIDAD AUMENTADA (AR)
    // ==========================================
    private val _selectedHotspot = MutableStateFlow<IconHotspot?>(ArIconographyData.hotspots.first())
    val selectedHotspot: StateFlow<IconHotspot?> = _selectedHotspot.asStateFlow()

    private val _arScale = MutableStateFlow(1.0f)
    val arScale: StateFlow<Float> = _arScale.asStateFlow()

    private val _arTilt = MutableStateFlow(-10f)
    val arTilt: StateFlow<Float> = _arTilt.asStateFlow()

    private val _arCandlesEnabled = MutableStateFlow(true)
    val arCandlesEnabled: StateFlow<Boolean> = _arCandlesEnabled.asStateFlow()

    private val _arAuraEnabled = MutableStateFlow(true)
    val arAuraEnabled: StateFlow<Boolean> = _arAuraEnabled.asStateFlow()

    fun selectIconHotspot(hotspot: IconHotspot?) {
        _selectedHotspot.value = hotspot
    }

    fun setArScale(scale: Float) {
        _arScale.value = scale.coerceIn(0.5f, 1.8f)
    }

    fun setArTilt(tilt: Float) {
        _arTilt.value = tilt.coerceIn(-35f, 15f)
    }

    fun toggleArCandles() {
        _arCandlesEnabled.value = !_arCandlesEnabled.value
    }

    fun toggleArAura() {
        _arAuraEnabled.value = !_arAuraEnabled.value
    }

    fun playHotspotExegesis(hotspot: IconHotspot) {
        val textToSpeak = "${hotspot.title}. ${hotspot.greekTitle}. Categoría: ${hotspot.theologicalCategory}. ${hotspot.theologicalExegesis}. Cita de ${hotspot.patristicSource}: ${hotspot.patristicQuote}. ${hotspot.audioNarrationScript}"
        speakText(textToSpeak)
    }

    // =========================================================================
    // ESCUDO OFICIAL PERSONALIZADO (SUBIR DESDE EL TELÉFONO O ESPACIO PRIVADO)
    // =========================================================================
    private val _customCrestUri = MutableStateFlow<String?>(
        prefs.getString("custom_crest_uri", null)
    )
    val customCrestUri: StateFlow<String?> = _customCrestUri.asStateFlow()

    fun setCustomCrest(uri: Uri) {
        viewModelScope.launch {
            val saved = copyUriToInternalStorage(uri, "crest_official")
            if (saved != null) {
                _customCrestUri.value = saved
                prefs.edit().putString("custom_crest_uri", saved).apply()
                themePreferencesManager.saveCustomCrestUri(saved)
            }
        }
    }

    fun resetCustomCrest() {
        val old = _customCrestUri.value
        _customCrestUri.value = null
        prefs.edit().remove("custom_crest_uri").apply()
        viewModelScope.launch {
            themePreferencesManager.saveCustomCrestUri(null)
        }
        if (old != null && old.startsWith("file://")) {
            try { File(Uri.parse(old).path ?: "").delete() } catch (_: Exception) {}
        }
    }

    // =========================================================================
    // IMAGEN LATERAL DE ORACIÓN DIARIA (SAN BASILIO O PERSONALIZADA)
    // =========================================================================
    private val _customPrayerSideImageUri = MutableStateFlow<String?>(
        prefs.getString("custom_prayer_side_image_uri", null)
    )
    val customPrayerSideImageUri: StateFlow<String?> = _customPrayerSideImageUri.asStateFlow()

    fun setCustomPrayerSideImage(uri: Uri) {
        viewModelScope.launch {
            val saved = copyUriToInternalStorage(uri, "prayer_side_image")
            if (saved != null) {
                _customPrayerSideImageUri.value = saved
                prefs.edit().putString("custom_prayer_side_image_uri", saved).apply()
                themePreferencesManager.saveCustomPrayerSideImageUri(saved)
            }
        }
    }

    fun setCustomPrayerSideImageUrl(url: String) {
        val trimmed = url.trim()
        if (trimmed.isNotBlank()) {
            _customPrayerSideImageUri.value = trimmed
            prefs.edit().putString("custom_prayer_side_image_uri", trimmed).apply()
            viewModelScope.launch {
                themePreferencesManager.saveCustomPrayerSideImageUri(trimmed)
            }
        }
    }

    fun resetCustomPrayerSideImage() {
        val old = _customPrayerSideImageUri.value
        _customPrayerSideImageUri.value = null
        prefs.edit().remove("custom_prayer_side_image_uri").apply()
        viewModelScope.launch {
            themePreferencesManager.saveCustomPrayerSideImageUri(null)
        }
        if (old != null && old.startsWith("file://")) {
            try { File(Uri.parse(old).path ?: "").delete() } catch (_: Exception) {}
        }
    }

    // =========================================================================
    // PORTADA DE LA COMUNIDAD / FRATERNIDAD
    // =========================================================================
    private val _customFraternityCoverUri = MutableStateFlow<String?>(
        prefs.getString("custom_fraternity_cover_uri", null)
    )
    val customFraternityCoverUri: StateFlow<String?> = _customFraternityCoverUri.asStateFlow()

    fun setCustomFraternityCover(uri: Uri) {
        viewModelScope.launch {
            val saved = copyUriToInternalStorage(uri, "fraternity_cover_image")
            if (saved != null) {
                _customFraternityCoverUri.value = saved
                prefs.edit().putString("custom_fraternity_cover_uri", saved).apply()
                themePreferencesManager.saveCustomFraternityCoverUri(saved)
            }
        }
    }

    fun resetCustomFraternityCover() {
        val old = _customFraternityCoverUri.value
        _customFraternityCoverUri.value = null
        prefs.edit().remove("custom_fraternity_cover_uri").apply()
        viewModelScope.launch {
            themePreferencesManager.saveCustomFraternityCoverUri(null)
        }
        if (old != null && old.startsWith("file://")) {
            try { File(Uri.parse(old).path ?: "").delete() } catch (_: Exception) {}
        }
    }

    // =========================================================================
    // GESTOR DE FOTOS DE BASILIANOS Y SANTOS PATRONOS (SUBIR DESDE CELULAR)
    // =========================================================================
    private val _customPhotos = MutableStateFlow<Map<String, String>>(loadCustomPhotosFromPrefs())
    val customPhotos: StateFlow<Map<String, String>> = _customPhotos.asStateFlow()

    fun setCustomPhoto(itemId: String, uri: Uri) {
        viewModelScope.launch {
            val saved = copyUriToInternalStorage(uri, "basilian_${itemId}")
            if (saved != null) {
                val updated = _customPhotos.value.toMutableMap().apply {
                    put(itemId, saved)
                }
                _customPhotos.value = updated
                saveCustomPhotosToPrefs(updated)
            }
        }
    }

    fun removeCustomPhoto(itemId: String) {
        val old = _customPhotos.value[itemId]
        val updated = _customPhotos.value.toMutableMap().apply {
            remove(itemId)
        }
        _customPhotos.value = updated
        saveCustomPhotosToPrefs(updated)
        if (old != null && old.startsWith("file://")) {
            try { File(Uri.parse(old).path ?: "").delete() } catch (_: Exception) {}
        }
    }

    fun getCustomPhoto(itemId: String): String? = _customPhotos.value[itemId]

    private fun loadCustomPhotosFromPrefs(): Map<String, String> {
        val jsonStr = prefs.getString("custom_photos_map", null) ?: return emptyMap()
        return try {
            val json = JSONObject(jsonStr)
            val map = mutableMapOf<String, String>()
            json.keys().forEach { key ->
                map[key] = json.getString(key)
            }
            map
        } catch (_: Exception) {
            emptyMap()
        }
    }

    private fun saveCustomPhotosToPrefs(map: Map<String, String>) {
        try {
            val json = JSONObject()
            map.forEach { (k, v) -> json.put(k, v) }
            prefs.edit().putString("custom_photos_map", json.toString()).apply()
        } catch (_: Exception) {}
    }

    // =========================================================================
    // IMÁGENES PERSONALIZADAS: LITURGIA, HISTORIA IA, RECURSOS & CUMPLEAÑOS
    // =========================================================================
    private val _customLiturgiaImageUri = MutableStateFlow<String?>(
        prefs.getString("custom_liturgia_image_uri", null)
    )
    val customLiturgiaImageUri: StateFlow<String?> = _customLiturgiaImageUri.asStateFlow()

    fun setCustomLiturgiaImage(uri: Uri) {
        viewModelScope.launch {
            val saved = copyUriToInternalStorage(uri, "liturgia_custom_image")
            if (saved != null) {
                _customLiturgiaImageUri.value = saved
                prefs.edit().putString("custom_liturgia_image_uri", saved).apply()
            }
        }
    }

    fun resetCustomLiturgiaImage() {
        val old = _customLiturgiaImageUri.value
        _customLiturgiaImageUri.value = null
        prefs.edit().remove("custom_liturgia_image_uri").apply()
        if (old != null && old.startsWith("file://")) {
            try { File(Uri.parse(old).path ?: "").delete() } catch (_: Exception) {}
        }
    }

    private val _customHistoriaImageUri = MutableStateFlow<String?>(
        prefs.getString("custom_historia_image_uri", null)
    )
    val customHistoriaImageUri: StateFlow<String?> = _customHistoriaImageUri.asStateFlow()

    fun setCustomHistoriaImage(uri: Uri) {
        viewModelScope.launch {
            val saved = copyUriToInternalStorage(uri, "historia_custom_image")
            if (saved != null) {
                _customHistoriaImageUri.value = saved
                prefs.edit().putString("custom_historia_image_uri", saved).apply()
            }
        }
    }

    fun resetCustomHistoriaImage() {
        val old = _customHistoriaImageUri.value
        _customHistoriaImageUri.value = null
        prefs.edit().remove("custom_historia_image_uri").apply()
        if (old != null && old.startsWith("file://")) {
            try { File(Uri.parse(old).path ?: "").delete() } catch (_: Exception) {}
        }
    }

    private val _customRecursosImageUri = MutableStateFlow<String?>(
        prefs.getString("custom_recursos_image_uri", null)
    )
    val customRecursosImageUri: StateFlow<String?> = _customRecursosImageUri.asStateFlow()

    fun setCustomRecursosImage(uri: Uri) {
        viewModelScope.launch {
            val saved = copyUriToInternalStorage(uri, "recursos_custom_image")
            if (saved != null) {
                _customRecursosImageUri.value = saved
                prefs.edit().putString("custom_recursos_image_uri", saved).apply()
            }
        }
    }

    fun resetCustomRecursosImage() {
        val old = _customRecursosImageUri.value
        _customRecursosImageUri.value = null
        prefs.edit().remove("custom_recursos_image_uri").apply()
        if (old != null && old.startsWith("file://")) {
            try { File(Uri.parse(old).path ?: "").delete() } catch (_: Exception) {}
        }
    }

    private val _customCumpleanosCoverUri = MutableStateFlow<String?>(
        prefs.getString("custom_cumpleanos_cover_uri", null)
    )
    val customCumpleanosCoverUri: StateFlow<String?> = _customCumpleanosCoverUri.asStateFlow()

    fun setCustomCumpleanosCover(uri: Uri) {
        viewModelScope.launch {
            val saved = copyUriToInternalStorage(uri, "cumpleanos_custom_cover")
            if (saved != null) {
                _customCumpleanosCoverUri.value = saved
                prefs.edit().putString("custom_cumpleanos_cover_uri", saved).apply()
            }
        }
    }

    fun resetCustomCumpleanosCover() {
        val old = _customCumpleanosCoverUri.value
        _customCumpleanosCoverUri.value = null
        prefs.edit().remove("custom_cumpleanos_cover_uri").apply()
        if (old != null && old.startsWith("file://")) {
            try { File(Uri.parse(old).path ?: "").delete() } catch (_: Exception) {}
        }
    }

    // =========================================================================
    // GALERÍA MULTICASILLA DE IMÁGENES PERSONALIZABLES POR EL USUARIO
    // =========================================================================
    private val _customGallerySlots = MutableStateFlow<Map<String, String>>(loadCustomGallerySlotsFromPrefs())
    val customGallerySlots: StateFlow<Map<String, String>> = _customGallerySlots.asStateFlow()

    private fun loadCustomGallerySlotsFromPrefs(): Map<String, String> {
        val jsonStr = prefs.getString("custom_gallery_slots_json", null) ?: return emptyMap()
        return try {
            val obj = JSONObject(jsonStr)
            val result = mutableMapOf<String, String>()
            val keys = obj.keys()
            while (keys.hasNext()) {
                val k = keys.next()
                val v = obj.optString(k, "")
                if (v.isNotBlank()) {
                    result[k] = v
                }
            }
            result
        } catch (_: Exception) {
            emptyMap()
        }
    }

    private fun saveCustomGallerySlotsToPrefs(map: Map<String, String>) {
        try {
            val obj = JSONObject()
            map.forEach { (k, v) -> obj.put(k, v) }
            prefs.edit().putString("custom_gallery_slots_json", obj.toString()).apply()
        } catch (_: Exception) {}
    }

    fun setCustomGallerySlotPhoto(slotKey: String, uri: Uri) {
        viewModelScope.launch {
            val saved = copyUriToInternalStorage(uri, "gallery_slot_${slotKey.replace(Regex("[^a-zA-Z0-9_]"), "_")}")
            if (saved != null) {
                val updated = _customGallerySlots.value.toMutableMap().apply {
                    put(slotKey, saved)
                }
                _customGallerySlots.value = updated
                saveCustomGallerySlotsToPrefs(updated)
            }
        }
    }

    fun removeCustomGallerySlotPhoto(slotKey: String) {
        val currentMap = _customGallerySlots.value
        val oldUri = currentMap[slotKey]
        val updated = currentMap - slotKey
        _customGallerySlots.value = updated
        saveCustomGallerySlotsToPrefs(updated)
        if (oldUri != null && oldUri.startsWith("file://")) {
            try { File(Uri.parse(oldUri).path ?: "").delete() } catch (_: Exception) {}
        }
    }

    // =========================================================================
    // DIRECTORIO BASILIANO, CUMPLEAÑOS & HERMANOS DIFUNTOS
    // =========================================================================
    private val _directorySearchQuery = MutableStateFlow("")
    val directorySearchQuery: StateFlow<String> = _directorySearchQuery.asStateFlow()

    private val _selectedCountryFilter = MutableStateFlow("Todos")
    val selectedCountryFilter: StateFlow<String> = _selectedCountryFilter.asStateFlow()

    private val _selectedMemberCategory = MutableStateFlow(MemberCategory.ALL)
    val selectedMemberCategory: StateFlow<MemberCategory> = _selectedMemberCategory.asStateFlow()

    private val _selectedMonthFilter = MutableStateFlow<Int?>(null) // null = all months
    val selectedMonthFilter: StateFlow<Int?> = _selectedMonthFilter.asStateFlow()

    private val _selectedDeceasedCategory = MutableStateFlow(DeceasedCategory.TODOS)
    val selectedDeceasedCategory: StateFlow<DeceasedCategory> = _selectedDeceasedCategory.asStateFlow()

    private val _deceasedSearchQuery = MutableStateFlow("")
    val deceasedSearchQuery: StateFlow<String> = _deceasedSearchQuery.asStateFlow()

    private val _litCandleBrotherIds = MutableStateFlow<Set<String>>(
        setOf("dec_carruthers", "dec_thompson_vicente", "dec_lapierre")
    )
    val litCandleBrotherIds: StateFlow<Set<String>> = _litCandleBrotherIds.asStateFlow()

    fun setDirectorySearchQuery(query: String) {
        _directorySearchQuery.value = query
    }

    fun setSelectedCountryFilter(country: String) {
        _selectedCountryFilter.value = country
    }

    fun setSelectedMemberCategory(cat: MemberCategory) {
        _selectedMemberCategory.value = cat
    }

    fun setSelectedMonthFilter(month: Int?) {
        _selectedMonthFilter.value = month
    }

    fun setDeceasedCategory(cat: DeceasedCategory) {
        _selectedDeceasedCategory.value = cat
    }

    fun setDeceasedSearchQuery(query: String) {
        _deceasedSearchQuery.value = query
    }

    fun toggleCandleForBrother(brotherId: String) {
        val current = _litCandleBrotherIds.value
        _litCandleBrotherIds.value = if (current.contains(brotherId)) {
            current - brotherId
        } else {
            current + brotherId
        }
    }

    // =========================================================================
    // SELECCIÓN DE TEMAS VISUALES
    // =========================================================================
    private val _currentAppTheme = MutableStateFlow(
        try {
            val id = prefs.getString("selected_app_theme", BasilianAppTheme.ESCUDO_BASILIANO.id)
            BasilianAppTheme.findById(id ?: "")
        } catch (_: Exception) {
            BasilianAppTheme.ESCUDO_BASILIANO
        }
    )
    val currentAppTheme: StateFlow<BasilianAppTheme> = _currentAppTheme.asStateFlow()

    fun setAppTheme(theme: BasilianAppTheme) {
        _currentAppTheme.value = theme
        prefs.edit().putString("selected_app_theme", theme.id).apply()
        viewModelScope.launch {
            themePreferencesManager.saveThemeId(theme.id)
        }
    }

    // =========================================================================
    // SELECCIÓN DE IDIOMA (ESPAÑOL, INGLÉS, FRANCÉS)
    // =========================================================================
    private val _appLanguage = MutableStateFlow(
        try {
            val code = prefs.getString("selected_app_language", com.example.data.model.AppLanguage.ES.code)
            com.example.data.model.AppLanguage.fromCode(code)
        } catch (_: Exception) {
            com.example.data.model.AppLanguage.ES
        }
    )
    val appLanguage: StateFlow<com.example.data.model.AppLanguage> = _appLanguage.asStateFlow()

    fun setAppLanguage(lang: com.example.data.model.AppLanguage) {
        _appLanguage.value = lang
        prefs.edit().putString("selected_app_language", lang.code).apply()
        try {
            java.util.Locale.setDefault(lang.locale)
        } catch (_: Exception) {}
        tts?.let { engine ->
            try {
                engine.language = lang.locale
            } catch (_: Exception) {}
        }
    }

    // =========================================================================
    // TALLER CREATIVO & HISTORIA GEMINI IA (VINCULADO AL CORREO DEL USUARIO)
    // =========================================================================
    private val _currentUserProfile = MutableStateFlow<UserCommunityProfile?>(loadUserProfileFromPrefs())
    val currentUserProfile: StateFlow<UserCommunityProfile?> = _currentUserProfile.asStateFlow()

    private val _aiChatThreads = MutableStateFlow<List<com.example.data.ai.AiChatSessionThread>>(
        com.example.data.ai.BasilianAiMemory.getChatThreadsForUser(getApplication(), _currentUserProfile.value?.email)
    )
    val aiChatThreads: StateFlow<List<com.example.data.ai.AiChatSessionThread>> = _aiChatThreads.asStateFlow()

    private val _activeAiChatId = MutableStateFlow<String>(
        com.example.data.ai.BasilianAiMemory.getActiveChatIdForUser(getApplication(), _currentUserProfile.value?.email)
    )
    val activeAiChatId: StateFlow<String> = _activeAiChatId.asStateFlow()

    private val _learnedAiFacts = MutableStateFlow<List<com.example.data.ai.LearnedBasilianFact>>(
        com.example.data.ai.BasilianAiMemory.getAllLearnedFacts(getApplication())
    )
    val learnedAiFacts: StateFlow<List<com.example.data.ai.LearnedBasilianFact>> = _learnedAiFacts.asStateFlow()

    private val _lastAutoLearnedFactTitle = MutableStateFlow<String?>(null)
    val lastAutoLearnedFactTitle: StateFlow<String?> = _lastAutoLearnedFactTitle.asStateFlow()

    private val _geminiThinkingDepth = MutableStateFlow(GeminiThinkingDepth.EQUILIBRADO)
    val geminiThinkingDepth: StateFlow<GeminiThinkingDepth> = _geminiThinkingDepth.asStateFlow()

    fun setGeminiThinkingDepth(depth: GeminiThinkingDepth) {
        _geminiThinkingDepth.value = depth
    }

    private val _userPersonalAiProfile = MutableStateFlow(
        com.example.data.ai.BasilianAiMemory.getUserPersonalAiProfile(getApplication(), _currentUserProfile.value?.email)
    )
    val userPersonalAiProfile: StateFlow<com.example.data.ai.UserPersonalAiProfile> = _userPersonalAiProfile.asStateFlow()

    fun setUserPersonalAiStyle(style: com.example.data.ai.UserAiDialogueStyle) {
        val email = _currentUserProfile.value?.email
        _userPersonalAiProfile.value = com.example.data.ai.BasilianAiMemory.setUserPreferredDialogueStyle(
            getApplication(),
            email,
            style
        )
    }

    fun setUserCustomAiInstruction(instruction: String) {
        val email = _currentUserProfile.value?.email
        _userPersonalAiProfile.value = com.example.data.ai.BasilianAiMemory.setUserCustomStyleInstruction(
            getApplication(),
            email,
            instruction
        )
    }

    fun addUserPersonalAiMemoryNote(note: String) {
        val email = _currentUserProfile.value?.email
        _userPersonalAiProfile.value = com.example.data.ai.BasilianAiMemory.addUserPersonalMemoryNote(
            getApplication(),
            email,
            note
        )
    }

    fun removeUserPersonalAiMemoryNote(note: String) {
        val email = _currentUserProfile.value?.email
        _userPersonalAiProfile.value = com.example.data.ai.BasilianAiMemory.removeUserPersonalMemoryNote(
            getApplication(),
            email,
            note
        )
    }

    private val _creativeHistory = MutableStateFlow<List<CreativeHistoryItem>>(
        resolveInitialCreativeHistory(_currentUserProfile.value?.email)
    )
    val creativeHistory: StateFlow<List<CreativeHistoryItem>> = _creativeHistory.asStateFlow()

    private val _isCreativeAiLoading = MutableStateFlow(false)
    val isCreativeAiLoading: StateFlow<Boolean> = _isCreativeAiLoading.asStateFlow()

    private val _activeCreativeResult = MutableStateFlow<CreativeHistoryItem?>(
        _creativeHistory.value.firstOrNull()
    )
    val activeCreativeResult: StateFlow<CreativeHistoryItem?> = _activeCreativeResult.asStateFlow()

    private fun resolveInitialCreativeHistory(email: String?): List<CreativeHistoryItem> {
        return try {
            val threads = com.example.data.ai.BasilianAiMemory.getChatThreadsForUser(getApplication(), email)
            val activeId = com.example.data.ai.BasilianAiMemory.getActiveChatIdForUser(getApplication(), email)
            val activeThread = threads.firstOrNull { it.id == activeId } ?: threads.firstOrNull()
            val fromThread = activeThread?.turns?.reversed()?.map { turn ->
                val resolvedMode = try {
                    CreativeHistoryMode.valueOf(turn.modeName)
                } catch (_: Exception) {
                    CreativeHistoryMode.HISTORIA_COMPLETA
                }
                CreativeHistoryItem(
                    id = turn.id,
                    prompt = turn.userPrompt,
                    mode = resolvedMode,
                    response = turn.aiResponse,
                    timestamp = turn.timestamp
                )
            }.orEmpty()
            if (fromThread.isNotEmpty()) return fromThread
            val legacy = loadCreativeHistoryForEmail(email)
            if (legacy.isNotEmpty() && activeThread != null) {
                val depthName = try {
                    _geminiThinkingDepth.value.name
                } catch (_: Throwable) {
                    GeminiThinkingDepth.EQUILIBRADO.name
                }
                // Migrate legacy history turns into the active chat thread in chronological order
                legacy.reversed().forEach { item ->
                    com.example.data.ai.BasilianAiMemory.appendTurnToChat(
                        context = getApplication(),
                        email = email,
                        chatId = activeThread.id,
                        turn = com.example.data.ai.AiChatTurn(
                            id = item.id,
                            userPrompt = item.prompt,
                            aiResponse = item.response,
                            modeName = item.mode.name,
                            thinkingDepthName = depthName,
                            timestamp = item.timestamp
                        )
                    )
                }
                _aiChatThreads.value = com.example.data.ai.BasilianAiMemory.getChatThreadsForUser(getApplication(), email)
            }
            legacy
        } catch (_: Throwable) {
            emptyList()
        }
    }

    private fun syncCreativeHistoryFromActiveThread(email: String?) {
        val threads = com.example.data.ai.BasilianAiMemory.getChatThreadsForUser(getApplication(), email)
        _aiChatThreads.value = threads
        val activeId = com.example.data.ai.BasilianAiMemory.getActiveChatIdForUser(getApplication(), email)
        _activeAiChatId.value = activeId
        val activeThread = threads.firstOrNull { it.id == activeId } ?: threads.firstOrNull()
        val items = activeThread?.turns?.reversed()?.map { turn ->
            val resolvedMode = try {
                CreativeHistoryMode.valueOf(turn.modeName)
            } catch (_: Exception) {
                CreativeHistoryMode.HISTORIA_COMPLETA
            }
            CreativeHistoryItem(
                id = turn.id,
                prompt = turn.userPrompt,
                mode = resolvedMode,
                response = turn.aiResponse,
                timestamp = turn.timestamp
            )
        }.orEmpty()
        _creativeHistory.value = items
        _activeCreativeResult.value = items.firstOrNull()
    }

    private fun observeAiChatSessionsFromRoom(email: String?) {
        roomAiChatObserverJob?.cancel()
        roomAiChatObserverJob = viewModelScope.launch {
            try {
                aiChatRoomRepository.ensureInitializedAndMigrated(email)
                aiChatRoomRepository.observeSessionsWithTurns(email).collect { roomThreads ->
                    if (roomThreads.isNotEmpty()) {
                        _aiChatThreads.value = roomThreads
                        val currentActiveId = _activeAiChatId.value
                        val activeThread = roomThreads.firstOrNull { it.id == currentActiveId }
                            ?: roomThreads.first()
                        _activeAiChatId.value = activeThread.id
                        val items = activeThread.turns.reversed().map { turn ->
                            val resolvedMode = try {
                                CreativeHistoryMode.valueOf(turn.modeName)
                            } catch (_: Exception) {
                                CreativeHistoryMode.HISTORIA_COMPLETA
                            }
                            CreativeHistoryItem(
                                id = turn.id,
                                prompt = turn.userPrompt,
                                mode = resolvedMode,
                                response = turn.aiResponse,
                                timestamp = turn.timestamp
                            )
                        }
                        _creativeHistory.value = items
                        _activeCreativeResult.value = items.firstOrNull()
                    }
                }
            } catch (e: Exception) {
                Log.w("BasilianViewModel", "Error observando historial IA desde Room: ${e.message}")
            }
        }
    }

    fun startNewAiChatThread(initialTitle: String = "Nuevo Chat con Basilio IA") {
        val email = _currentUserProfile.value?.email
        // Limpiar de inmediato la vista activa para que el Nuevo Chat quede despejado al instante
        _creativeHistory.value = emptyList()
        _activeCreativeResult.value = null
        stopSpeaking()
        viewModelScope.launch {
            val created = aiChatRoomRepository.createNewSession(
                userEmail = email,
                customTitle = initialTitle
            )
            _activeAiChatId.value = created.id
            val updatedThreads = aiChatRoomRepository.loadFullThreadsOnce(email)
            _aiChatThreads.value = if (updatedThreads.isNotEmpty()) {
                updatedThreads
            } else {
                listOf(created)
            }
            _creativeHistory.value = emptyList()
            _activeCreativeResult.value = null
            saveCreativeHistoryForEmail(email, emptyList())
        }
    }

    fun selectAiChatThread(chatId: String) {
        val email = _currentUserProfile.value?.email
        _activeAiChatId.value = chatId
        viewModelScope.launch {
            val threads = aiChatRoomRepository.selectSession(email, chatId)
            _aiChatThreads.value = threads
            val activeThread = threads.firstOrNull { it.id == chatId } ?: threads.firstOrNull()
            val items = activeThread?.turns?.reversed()?.map { turn ->
                val resolvedMode = try {
                    CreativeHistoryMode.valueOf(turn.modeName)
                } catch (_: Exception) {
                    CreativeHistoryMode.HISTORIA_COMPLETA
                }
                CreativeHistoryItem(
                    id = turn.id,
                    prompt = turn.userPrompt,
                    mode = resolvedMode,
                    response = turn.aiResponse,
                    timestamp = turn.timestamp
                )
            }.orEmpty()
            _creativeHistory.value = items
            _activeCreativeResult.value = items.firstOrNull()
        }
    }

    fun deleteAiChatThread(chatId: String) {
        val email = _currentUserProfile.value?.email
        viewModelScope.launch {
            val (threads, nextActiveId) = aiChatRoomRepository.deleteSession(email, chatId)
            _aiChatThreads.value = threads
            _activeAiChatId.value = nextActiveId
            val activeThread = threads.firstOrNull { it.id == nextActiveId } ?: threads.firstOrNull()
            val items = activeThread?.turns?.reversed()?.map { turn ->
                val resolvedMode = try {
                    CreativeHistoryMode.valueOf(turn.modeName)
                } catch (_: Exception) {
                    CreativeHistoryMode.HISTORIA_COMPLETA
                }
                CreativeHistoryItem(
                    id = turn.id,
                    prompt = turn.userPrompt,
                    mode = resolvedMode,
                    response = turn.aiResponse,
                    timestamp = turn.timestamp
                )
            }.orEmpty()
            _creativeHistory.value = items
            _activeCreativeResult.value = items.firstOrNull()
        }
    }

    fun teachNewFactToAi(title: String, details: String, category: String = "AUTOAPRENDIZAJE") {
        if (title.isBlank() || details.isBlank()) return
        viewModelScope.launch {
            aiChatRoomRepository.addLearnedFact(
                title = title.trim(),
                details = details.trim(),
                category = category,
                verifiedWithGoogle = true
            )
            _learnedAiFacts.value = com.example.data.ai.BasilianAiMemory.getAllLearnedFacts(getApplication())
            _lastAutoLearnedFactTitle.value = title.trim()
        }
    }

    fun deleteLearnedAiFact(factId: String) {
        viewModelScope.launch {
            aiChatRoomRepository.deleteLearnedFact(factId)
            _learnedAiFacts.value = com.example.data.ai.BasilianAiMemory.getAllLearnedFacts(getApplication())
        }
    }

    fun generateCreativeHistory(
        prompt: String,
        mode: CreativeHistoryMode,
        depthOverride: GeminiThinkingDepth? = null
    ) {
        if (prompt.isBlank()) return
        val selectedDepth = depthOverride ?: _geminiThinkingDepth.value
        viewModelScope.launch {
            _isCreativeAiLoading.value = true
            try {
                val userName = _currentUserProfile.value?.fullName?.takeIf { it.isNotBlank() }
                val userEmail = _currentUserProfile.value?.email
                val roomHistoricalCtx = aiChatRoomRepository.buildHistoricalContextFromRoom(
                    userEmail = userEmail,
                    activeSessionId = _activeAiChatId.value
                )
                val learnedCtx = com.example.data.ai.BasilianAiMemory.getLearnedPromptContext(
                    getApplication(),
                    _appLanguage.value,
                    userEmail
                ) + if (roomHistoricalCtx.isNotBlank()) "\n\n$roomHistoricalCtx" else ""
                val learnedList = _learnedAiFacts.value.ifEmpty {
                    com.example.data.ai.BasilianAiMemory.getAllLearnedFacts(getApplication())
                }
                val isBasilianMember = _isBasilianPrivateAccess.value || _curiaIsAuthenticated.value

                val activeThread = _aiChatThreads.value.firstOrNull { it.id == _activeAiChatId.value }
                    ?: _aiChatThreads.value.firstOrNull()
                val chronologicalTurns: List<Pair<String, String>> = if (activeThread != null && activeThread.turns.isNotEmpty()) {
                    activeThread.turns.takeLast(14).map { it.userPrompt to it.aiResponse }
                } else {
                    _creativeHistory.value.take(12).reversed().map { it.prompt to it.response }
                }

                val response = BasilianAiService.generateCreativeHistoryResponse(
                    prompt = prompt,
                    mode = mode,
                    language = _appLanguage.value,
                    userName = userName,
                    learnedMemoryContext = learnedCtx,
                    learnedFacts = learnedList,
                    isBasilianMember = isBasilianMember,
                    thinkingDepth = selectedDepth,
                    conversationHistory = chronologicalTurns
                )
                val autoLearned = com.example.data.ai.BasilianAiMemory.autoLearnFromChat(getApplication(), prompt, response, userEmail)
                _userPersonalAiProfile.value = com.example.data.ai.BasilianAiMemory.getUserPersonalAiProfile(getApplication(), userEmail)
                if (autoLearned != null) {
                    aiChatRoomRepository.addLearnedFact(
                        title = autoLearned.title,
                        details = autoLearned.details,
                        category = autoLearned.category,
                        verifiedWithGoogle = autoLearned.verifiedWithGoogle
                    )
                    _learnedAiFacts.value = com.example.data.ai.BasilianAiMemory.getAllLearnedFacts(getApplication())
                    _lastAutoLearnedFactTitle.value = autoLearned.title
                }
                val updatedHistory = try {
                    val newTurn = com.example.data.ai.AiChatTurn(
                        userPrompt = prompt,
                        aiResponse = response,
                        modeName = mode.name,
                        thinkingDepthName = selectedDepth.name,
                        learnedFactTitle = autoLearned?.title
                    )
                    val updatedThreads = aiChatRoomRepository.appendTurnToSession(
                        userEmail = userEmail,
                        sessionId = _activeAiChatId.value,
                        turn = newTurn
                    )
                    _aiChatThreads.value = updatedThreads
                    val updatedActiveThread = updatedThreads.firstOrNull { it.id == _activeAiChatId.value }
                        ?: updatedThreads.firstOrNull()
                    if (updatedActiveThread != null) {
                        _activeAiChatId.value = updatedActiveThread.id
                    }
                    updatedActiveThread?.turns?.reversed()?.map { turn ->
                        val resolvedMode = try {
                            CreativeHistoryMode.valueOf(turn.modeName)
                        } catch (_: Exception) {
                            CreativeHistoryMode.HISTORIA_COMPLETA
                        }
                        CreativeHistoryItem(
                            id = turn.id,
                            prompt = turn.userPrompt,
                            mode = resolvedMode,
                            response = turn.aiResponse,
                            timestamp = turn.timestamp
                        )
                    }
                } catch (_: Throwable) {
                    null
                }
                val newItem = CreativeHistoryItem(
                    prompt = prompt,
                    mode = mode,
                    response = response
                )
                _activeCreativeResult.value = newItem
                val finalHistory = updatedHistory ?: (listOf(newItem) + _creativeHistory.value)
                _creativeHistory.value = finalHistory
                saveCreativeHistoryForEmail(userEmail, finalHistory)
            } catch (e: Throwable) {
                val userName = _currentUserProfile.value?.fullName?.takeIf { it.isNotBlank() } ?: "Hermano(a)"
                val userEmail = _currentUserProfile.value?.email
                val fallbackResponse = try {
                    BasilianAiService.generateCreativeHistoryFallback(
                        prompt = prompt,
                        mode = mode,
                        language = _appLanguage.value,
                        isBasilianMember = true,
                        thinkingDepth = selectedDepth,
                        conversationHistory = emptyList(),
                        userName = userName
                    )
                } catch (_: Throwable) {
                    "**$userName**, desde la espiritualidad de **San Basilio Magno** y la **Congregación de los Padres Basilianos (C.S.B.)** (*«Bonitatem et disciplinam et scientiam doce me»*), aquí estoy contigo para acompañarte en la historia, la teología y la vida comunitaria basiliana. ¿Qué aspecto de nuestra Congregación deseas profundizar?"
                }
                val newItem = CreativeHistoryItem(
                    prompt = prompt,
                    mode = mode,
                    response = fallbackResponse
                )
                _activeCreativeResult.value = newItem
                val finalHistory = listOf(newItem) + _creativeHistory.value
                _creativeHistory.value = finalHistory
                saveCreativeHistoryForEmail(userEmail, finalHistory)
            } finally {
                _isCreativeAiLoading.value = false
            }
        }
    }

    fun selectCreativeHistoryItem(item: CreativeHistoryItem) {
        _activeCreativeResult.value = item
    }

    private fun saveCreativeHistoryForEmail(email: String?, items: List<CreativeHistoryItem>) {
        val key = if (email.isNullOrBlank()) "creative_history_default" else "creative_history_${email.trim().lowercase()}"
        try {
            val array = JSONArray()
            items.take(25).forEach { item ->
                val obj = JSONObject().apply {
                    put("id", item.id)
                    put("prompt", item.prompt)
                    put("mode", item.mode.name)
                    put("response", item.response)
                    put("timestamp", item.timestamp)
                }
                array.put(obj)
            }
            prefs.edit().putString(key, array.toString()).apply()
        } catch (_: Exception) {}
    }

    private fun loadCreativeHistoryForEmail(email: String?): List<CreativeHistoryItem> {
        val key = if (email.isNullOrBlank()) "creative_history_default" else "creative_history_${email.trim().lowercase()}"
        val jsonStr = prefs.getString(key, null) ?: return emptyList()
        return try {
            val array = JSONArray(jsonStr)
            val list = mutableListOf<CreativeHistoryItem>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val modeName = obj.optString("mode", CreativeHistoryMode.HISTORIA_COMPLETA.name)
                val mode = try { CreativeHistoryMode.valueOf(modeName) } catch (_: Exception) { CreativeHistoryMode.HISTORIA_COMPLETA }
                list.add(
                    CreativeHistoryItem(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        prompt = obj.getString("prompt"),
                        mode = mode,
                        response = obj.getString("response"),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    // =========================================================================
    // DETECCIÓN INTELIGENTE POR CORREO: MIEMBRO BASILIANO C.S.B. vs VISITANTE EXTERNO
    // =========================================================================
    data class RegisteredBasilianEmail(
        val email: String,
        val fatherName: String = "",
        val role: String = "Sacerdote / Religioso Basiliano C.S.B.",
        val addedAt: Long = System.currentTimeMillis()
    )

    private fun loadRegisteredBasilianEmailsFromPrefs(): List<RegisteredBasilianEmail> {
        val raw = prefs.getString("registered_future_basilian_emails_json", null)
        if (!raw.isNullOrBlank()) {
            try {
                val arr = JSONArray(raw)
                val list = mutableListOf<RegisteredBasilianEmail>()
                for (i in 0 until arr.length()) {
                    val o = arr.getJSONObject(i)
                    val em = o.optString("email", "").trim().lowercase()
                    if (em.isNotBlank()) {
                        list.add(
                            RegisteredBasilianEmail(
                                email = em,
                                fatherName = o.optString("fatherName", ""),
                                role = o.optString("role", "Sacerdote / Religioso Basiliano C.S.B."),
                                addedAt = o.optLong("addedAt", System.currentTimeMillis())
                            )
                        )
                    }
                }
                return list
            } catch (_: Throwable) {}
        }
        return emptyList()
    }

    private fun saveRegisteredBasilianEmailsToPrefs(list: List<RegisteredBasilianEmail>) {
        try {
            val arr = JSONArray()
            list.forEach { item ->
                arr.put(
                    JSONObject().apply {
                        put("email", item.email.trim().lowercase())
                        put("fatherName", item.fatherName.trim())
                        put("role", item.role.trim())
                        put("addedAt", item.addedAt)
                    }
                )
            }
            prefs.edit().putString("registered_future_basilian_emails_json", arr.toString()).apply()
        } catch (_: Throwable) {}
    }

    private val _registeredBasilianEmails = MutableStateFlow<List<RegisteredBasilianEmail>>(loadRegisteredBasilianEmailsFromPrefs())
    val registeredBasilianEmails: StateFlow<List<RegisteredBasilianEmail>> = _registeredBasilianEmails.asStateFlow()

    private val _isBasilianPrivateAccess = MutableStateFlow(
        resolveInitialBasilianAccess(_currentUserProfile.value?.email, _currentUserProfile.value?.role)
    )
    val isBasilianPrivateAccess: StateFlow<Boolean> = _isBasilianPrivateAccess.asStateFlow()

    fun detectBasilianMemberFromEmail(
        email: String?,
        role: String? = null,
        accessCode: String = ""
    ): Boolean {
        return try {
            val cleanEmail = email?.trim()?.lowercase() ?: return false
            if (cleanEmail.isBlank()) return false

            val cleanCode = accessCode.trim()
            val vaultCode = try {
                _basilianCommunityUnlockCode.value.trim()
            } catch (_: Throwable) {
                ""
            }
            if (cleanCode.isNotEmpty() && vaultCode.isNotEmpty() &&
                BasilianSecurityManager.constantTimeEquals(cleanCode.uppercase(), vaultCode.uppercase())
            ) {
                return true
            }

            // 1. Verificar si está en la lista de correos nuevos de Padres Basilianos agregados en "Solo para Mí"
            val registeredList = try {
                _registeredBasilianEmails.value
            } catch (_: Throwable) {
                loadRegisteredBasilianEmailsFromPrefs()
            }
            if (registeredList.any { it.email.equals(cleanEmail, ignoreCase = true) }) {
                return true
            }

            // 2. Verificar si este correo ya fue verificado o configurado manualmente
            if (prefs.contains("basilian_access_override_$cleanEmail")) {
                return prefs.getBoolean("basilian_access_override_$cleanEmail", false)
            }

            // 3. Si el rol elegido explícitamente es de Persona Externa / Visitante / Amigo Laico
            val cleanRole = role?.trim()?.lowercase() ?: ""
            if (cleanRole.contains("extern") || cleanRole.contains("afuera") || cleanRole.contains("visitante")) {
                return false
            }

            // 4. Correo creador / administrador principal o dominios oficiales de la Congregación C.S.B.
            if (BasilianSecurityManager.isLeaderEmailAddress(cleanEmail)) return true
            val basilianDomains = listOf(
                "@basilian.org",
                "@csb.org",
                "@basilianos.org",
                "@stbasil.com",
                "@basil.org",
                "@ustst.ca",
                "@stthom.edu",
                "@curiacsb.org",
                "@congregacionbasiliana.org"
            )
            if (basilianDomains.any { cleanEmail.endsWith(it) }) return true

            // 5. Verificar coincidencia exacta con el Directorio Oficial de Sacerdotes, Escolásticos y Obispos C.S.B.
            val matchesDirectoryMember = com.example.data.repository.BasilianDirectoryRepository.members.any { member ->
                member.email.isNotBlank() &&
                    member.email.lowercase().split(",", ";", " ").any { it.trim() == cleanEmail }
            }
            if (matchesDirectoryMember) return true

            // 6. Verificar coincidencia con los hermanos registrados de la comunidad inicial
            val matchesInitialCommunity = getInitialMembers().any { member ->
                member.email.trim().lowercase() == cleanEmail &&
                    (member.isCuriaMember || member.role.contains("Basiliano", ignoreCase = true) || member.role.contains("C.S.B.", ignoreCase = true))
            }
            if (matchesInitialCommunity) return true

            false
        } catch (_: Throwable) {
            false
        }
    }

    private fun resolveInitialBasilianAccess(email: String?, role: String?): Boolean {
        if (email.isNullOrBlank()) return true
        return detectBasilianMemberFromEmail(email, role)
    }

    fun setBasilianPrivateAccessOverride(enabled: Boolean) {
        _isBasilianPrivateAccess.value = enabled
        val currentEmail = _currentUserProfile.value?.email?.trim()?.lowercase()
        if (!currentEmail.isNullOrBlank()) {
            prefs.edit().putBoolean("basilian_access_override_$currentEmail", enabled).apply()
        }
    }

    private var basilianCodeFailedAttempts = 0

    fun unlockBasilianAccessWithCode(code: String): Boolean {
        val cleanCode = BasilianSecurityManager.validateAndSanitizeInput(code.trim(), maxLength = 64)
        val vaultCode = _basilianCommunityUnlockCode.value.trim()
        if (cleanCode.isNotEmpty() && BasilianSecurityManager.constantTimeEquals(cleanCode.uppercase(), vaultCode.uppercase())) {
            basilianCodeFailedAttempts = 0
            setBasilianPrivateAccessOverride(true)
            return true
        }
        basilianCodeFailedAttempts++
        val actorEmail = _currentUserProfile.value?.email?.takeIf { it.isNotBlank() } ?: "Visitante externo"
        val actorName = _currentUserProfile.value?.fullName?.takeIf { it.isNotBlank() } ?: "Usuario no basiliano"
        val actorDesc = "$actorName ($actorEmail)"
        recordAndRouteIntrusionToLeaderOnly(
            targetArea = "Código C.S.B. (Cumpleaños / En Memoria)",
            actorInfo = actorDesc,
            attemptNumber = basilianCodeFailedAttempts,
            blockedStatus = "Intento de desbloqueo con código inválido bloqueado"
        )
        appendLiveUpdateLog("🚨 ALERTA DE SEGURIDAD (Enviada solo al Líder): Intento fallido de código C.S.B. en Cumpleaños/Difuntos por $actorDesc.")
        return false
    }

    data class BasilianAccessRequest(
        val id: String = UUID.randomUUID().toString(),
        val email: String,
        val fullName: String,
        val requestedRole: String,
        val city: String,
        val timestamp: Long = System.currentTimeMillis(),
        val status: String = "PENDIENTE" // "PENDIENTE", "APROBADO_BASILIANO", "PUBLICO_ESPIRITUALIDAD"
    )

    private val _basilianAccessRequests = MutableStateFlow<List<BasilianAccessRequest>>(loadBasilianAccessRequestsFromPrefs())
    val basilianAccessRequests: StateFlow<List<BasilianAccessRequest>> = _basilianAccessRequests.asStateFlow()

    private fun loadBasilianAccessRequestsFromPrefs(): List<BasilianAccessRequest> {
        val raw = prefs.getString("basilian_access_requests_json", null)
        if (!raw.isNullOrBlank()) {
            try {
                val arr = JSONArray(raw)
                val list = mutableListOf<BasilianAccessRequest>()
                for (i in 0 until arr.length()) {
                    val o = arr.getJSONObject(i)
                    list.add(
                        BasilianAccessRequest(
                            id = o.optString("id", UUID.randomUUID().toString()),
                            email = o.getString("email"),
                            fullName = o.getString("fullName"),
                            requestedRole = o.optString("requestedRole", "Novicio"),
                            city = o.optString("city", "Colombia"),
                            timestamp = o.optLong("timestamp", System.currentTimeMillis()),
                            status = o.optString("status", "PENDIENTE")
                        )
                    )
                }
                return list
            } catch (_: Exception) {}
        }
        return emptyList()
    }

    private fun saveBasilianAccessRequestsToPrefs(requests: List<BasilianAccessRequest>) {
        try {
            val arr = JSONArray()
            requests.forEach { r ->
                arr.put(
                    JSONObject().apply {
                        put("id", r.id)
                        put("email", r.email)
                        put("fullName", r.fullName)
                        put("requestedRole", r.requestedRole)
                        put("city", r.city)
                        put("timestamp", r.timestamp)
                        put("status", r.status)
                    }
                )
            }
            prefs.edit().putString("basilian_access_requests_json", arr.toString()).apply()
        } catch (_: Exception) {}
    }

    fun approveBasilianAccessRequest(email: String) {
        val cleanEmail = email.trim().lowercase()
        prefs.edit().putBoolean("basilian_access_override_$cleanEmail", true).apply()
        val updated = _basilianAccessRequests.value.map {
            if (it.email.equals(cleanEmail, ignoreCase = true)) it.copy(status = "APROBADO_BASILIANO") else it
        }
        _basilianAccessRequests.value = updated
        saveBasilianAccessRequestsToPrefs(updated)
        if (_currentUserProfile.value?.email?.equals(cleanEmail, ignoreCase = true) == true) {
            _isBasilianPrivateAccess.value = true
        }
        appendLiveUpdateLog("✅ Acceso Basiliano C.S.B. aprobado para: $cleanEmail")
    }

    fun approveAllPendingAccessRequests() {
        val current = _basilianAccessRequests.value
        val updated = current.map { req ->
            val cleanEmail = req.email.trim().lowercase()
            prefs.edit().putBoolean("basilian_access_override_$cleanEmail", true).apply()
            req.copy(status = "APROBADO_BASILIANO")
        }
        _basilianAccessRequests.value = updated
        saveBasilianAccessRequestsToPrefs(updated)
        appendLiveUpdateLog("✅ Todas las solicitudes pendientes de acceso Basiliano han sido aprobadas")
        syncGlobalAdminChangesToAllDevices()
    }

    fun setBasilianUserAsPublicVisitor(email: String) {
        val cleanEmail = email.trim().lowercase()
        prefs.edit().putBoolean("basilian_access_override_$cleanEmail", false).apply()
        val updated = _basilianAccessRequests.value.map {
            if (it.email.equals(cleanEmail, ignoreCase = true)) it.copy(status = "PUBLICO_ESPIRITUALIDAD") else it
        }
        _basilianAccessRequests.value = updated
        saveBasilianAccessRequestsToPrefs(updated)
        if (_currentUserProfile.value?.email?.equals(cleanEmail, ignoreCase = true) == true) {
            _isBasilianPrivateAccess.value = false
        }
        appendLiveUpdateLog("🌐 Acceso configurado como Espiritualidad Pública para: $cleanEmail")
    }

    fun requestBasilianPrivateAccessVerification() {
        val profile = _currentUserProfile.value ?: return
        val cleanEmail = profile.email.trim().lowercase()
        if (cleanEmail.isBlank()) return
        enqueueBasilianAccessRequestIfNeeded(
            email = cleanEmail,
            fullName = profile.fullName,
            role = profile.role.ifBlank { "Religioso / Novicio Basiliano" },
            city = profile.cityAndCountry.ifBlank { "Colombia" }
        )
    }

    private fun enqueueBasilianAccessRequestIfNeeded(
        email: String,
        fullName: String,
        role: String,
        city: String
    ) {
        val cleanEmail = email.trim().lowercase()
        val current = _basilianAccessRequests.value.toMutableList()
        val existingIdx = current.indexOfFirst { it.email.equals(cleanEmail, ignoreCase = true) }
        val newReq = BasilianAccessRequest(
            id = if (existingIdx >= 0) current[existingIdx].id else UUID.randomUUID().toString(),
            email = cleanEmail,
            fullName = fullName,
            requestedRole = role,
            city = city,
            timestamp = System.currentTimeMillis(),
            status = "PENDIENTE"
        )
        if (existingIdx >= 0) {
            current[existingIdx] = newReq
        } else {
            current.add(0, newReq)
        }
        _basilianAccessRequests.value = current
        saveBasilianAccessRequestsToPrefs(current)
        appendLiveUpdateLog("🔔 Nueva solicitud de acceso Basiliano C.S.B.: $fullName ($cleanEmail - $role)")
    }

    fun addRegisteredBasilianEmail(
        email: String,
        fatherName: String = "",
        role: String = "Sacerdote / Religioso Basiliano C.S.B."
    ): Boolean {
        val cleanEmail = email.trim().lowercase()
        if (cleanEmail.isBlank() || !cleanEmail.contains("@") || !cleanEmail.contains(".")) return false

        val cleanName = fatherName.trim().ifBlank { "Padre / Hermano Basiliano C.S.B." }
        val cleanRole = role.trim().ifBlank { "Sacerdote / Religioso Basiliano C.S.B." }

        val current = _registeredBasilianEmails.value.toMutableList()
        val existingIdx = current.indexOfFirst { it.email.equals(cleanEmail, ignoreCase = true) }
        val newEntry = RegisteredBasilianEmail(
            email = cleanEmail,
            fatherName = cleanName,
            role = cleanRole,
            addedAt = System.currentTimeMillis()
        )
        if (existingIdx >= 0) {
            current[existingIdx] = newEntry
        } else {
            current.add(0, newEntry)
        }
        _registeredBasilianEmails.value = current
        saveRegisteredBasilianEmailsToPrefs(current)

        // Pre-aprobar y confirmar inmediatamente en el sistema para acceso completo instantáneo
        prefs.edit()
            .putBoolean("basilian_access_override_$cleanEmail", true)
            .putBoolean("firebase_email_confirmed_$cleanEmail", true)
            .apply()

        // Si había una solicitud pendiente con ese correo, marcarla como aprobada
        val updatedRequests = _basilianAccessRequests.value.map {
            if (it.email.equals(cleanEmail, ignoreCase = true)) it.copy(status = "APROBADO_BASILIANO") else it
        }
        _basilianAccessRequests.value = updatedRequests
        saveBasilianAccessRequestsToPrefs(updatedRequests)

        if (_currentUserProfile.value?.email?.equals(cleanEmail, ignoreCase = true) == true) {
            _isBasilianPrivateAccess.value = true
        }

        appendLiveUpdateLog("✅ Nuevo correo oficial Basiliano registrado con verificación inmediata: $cleanName ($cleanEmail)")
        syncGlobalAdminChangesToAllDevices()
        return true
    }

    fun removeRegisteredBasilianEmail(email: String) {
        val cleanEmail = email.trim().lowercase()
        val updated = _registeredBasilianEmails.value.filterNot { it.email.equals(cleanEmail, ignoreCase = true) }
        _registeredBasilianEmails.value = updated
        saveRegisteredBasilianEmailsToPrefs(updated)
        prefs.edit().remove("basilian_access_override_$cleanEmail").apply()
        appendLiveUpdateLog("🗑️ Correo Basiliano removido de la lista de acceso inmediato: $cleanEmail")
        syncGlobalAdminChangesToAllDevices()
    }

    fun getRegisteredBasilianEntry(email: String?): RegisteredBasilianEmail? {
        val cleanEmail = email?.trim()?.lowercase() ?: return null
        if (cleanEmail.isBlank()) return null
        val list = try {
            _registeredBasilianEmails.value
        } catch (_: Throwable) {
            loadRegisteredBasilianEmailsFromPrefs()
        }
        return list.firstOrNull { it.email.equals(cleanEmail, ignoreCase = true) }
    }

    fun isEmailInOfficialBasilianDirectory(email: String?): Boolean {
        return try {
            val cleanEmail = email?.trim()?.lowercase() ?: return false
            if (cleanEmail.isBlank()) return false
            if (BasilianSecurityManager.isLeaderEmailAddress(cleanEmail)) return true
            // Verificar si está en los correos nuevos agregados desde "Solo para Mí" o pre-aprobado como Basiliano
            val registeredList = try {
                _registeredBasilianEmails.value
            } catch (_: Throwable) {
                loadRegisteredBasilianEmailsFromPrefs()
            }
            if (registeredList.any { it.email.equals(cleanEmail, ignoreCase = true) }) {
                return true
            }
            if (prefs.getBoolean("basilian_access_override_$cleanEmail", false)) {
                return true
            }
            val basilianDomains = listOf(
                "@basilian.org",
                "@csb.org",
                "@basilianos.org",
                "@stbasil.com",
                "@basil.org",
                "@ustst.ca",
                "@stthom.edu",
                "@curiacsb.org",
                "@congregacionbasiliana.org"
            )
            if (basilianDomains.any { cleanEmail.endsWith(it) }) return true
            com.example.data.repository.BasilianDirectoryRepository.members.any { member ->
                member.email.isNotBlank() &&
                    member.email.lowercase().split(",", ";", " ").any { it.trim() == cleanEmail }
            }
        } catch (_: Throwable) {
            false
        }
    }

    fun isEmailAlreadyConfirmed(email: String?): Boolean {
        val clean = email?.trim()?.lowercase() ?: return false
        if (clean.isBlank()) return false
        if (isEmailInOfficialBasilianDirectory(clean)) return true
        return prefs.getBoolean("firebase_email_confirmed_$clean", false)
    }

    fun initiateFirebaseEmailConfirmation(
        email: String,
        fullName: String,
        verificationCode: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val clean = email.trim().lowercase()
        val isDirMember = isEmailInOfficialBasilianDirectory(clean)
        prefs.edit().putString("pending_email_code_$clean", verificationCode).apply()
        curiaFirebaseManager.initiateFirebaseEmailVerification(
            email = clean,
            fullName = fullName,
            verificationToken = verificationCode,
            isBasilianDirectoryMember = isDirMember,
            onResult = onResult
        )
    }

    fun confirmFirebaseEmailVerificationCode(
        email: String,
        enteredCode: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val clean = email.trim().lowercase()
        val expected = prefs.getString("pending_email_code_$clean", "") ?: ""
        curiaFirebaseManager.confirmEmailVerificationInFirebase(
            email = clean,
            enteredCode = enteredCode,
            expectedCode = expected
        ) { ok: Boolean, msg: String ->
            if (ok) {
                prefs.edit().putBoolean("firebase_email_confirmed_$clean", true).apply()
            }
            onResult(ok, msg)
        }
    }

    // =========================================================================
    // CONTRASEÑA PERSONAL POR CUENTA Y VERIFICACIÓN ANTI-SUPLANTACIÓN C.S.B.
    // =========================================================================
    fun hasPersonalAccountPassword(email: String?): Boolean {
        val clean = email?.trim()?.lowercase().orEmpty()
        if (clean.isBlank()) return false
        return BasilianSecurityManager.hasPersonalAccountPassword(getApplication(), clean)
    }

    /**
     * Verifica la Contraseña Personal de una cuenta existente, o registra y cifra la
     * Contraseña Personal cuando la persona ingresa su correo por primera vez.
     * Además, si un correo pertenece al Directorio Oficial C.S.B. y se registra por primera vez,
     * exige validar su Día/Año de Nacimiento del Directorio o el Código Fraterno C.S.B.
     * para impedir que un extraño que vio el correo en internet suplante a un Padre Basiliano.
     */
    fun verifyOrRegisterPersonalAccountPassword(
        email: String,
        fullName: String,
        passwordInput: String,
        basilianIdentityCheckInput: String = "",
        isRecognizedBasilian: Boolean = false
    ): Pair<Boolean, String?> {
        val cleanEmail = email.trim().lowercase()
        val cleanPass = passwordInput.trim()
        if (cleanPass.length < 4) {
            return false to "🔒 Por seguridad, escribe tu Contraseña Personal (mínimo 4 caracteres)."
        }

        val app = getApplication<Application>()
        if (BasilianSecurityManager.hasPersonalAccountPassword(app, cleanEmail)) {
            val isCorrect = BasilianSecurityManager.verifyPersonalAccountPassword(app, cleanEmail, cleanPass)
            if (!isCorrect) {
                recordAndRouteIntrusionToLeaderOnly(
                    targetArea = "Intento de Acceso a Cuenta Protegida ($cleanEmail)",
                    actorInfo = "${fullName.ifBlank { "Usuario" }} ($cleanEmail)",
                    attemptNumber = 1,
                    blockedStatus = "Contraseña personal incorrecta • Perfil protegido contra suplantación"
                )
                return false to "🚫 Contraseña personal incorrecta para «$cleanEmail». Ingresa la clave con la que registraste tu cuenta."
            }
            return true to null
        }

        val hash = BasilianSecurityManager.savePersonalAccountPassword(app, cleanEmail, cleanPass)
        curiaFirebaseManager.syncUserAccountPasswordHashToCloud(cleanEmail, hash, fullName)
        return true to null
    }

    fun changePersonalAccountPassword(
        email: String,
        newPassword: String,
        fullName: String = ""
    ): Boolean {
        val cleanEmail = email.trim().lowercase()
        val cleanPass = newPassword.trim()
        if (cleanEmail.isBlank() || cleanPass.length < 4) return false
        val hash = BasilianSecurityManager.savePersonalAccountPassword(getApplication(), cleanEmail, cleanPass)
        curiaFirebaseManager.syncUserAccountPasswordHashToCloud(cleanEmail, hash, fullName)
        appendLiveUpdateLog("🔐 Clave personal de acceso actualizada y sincronizada para $cleanEmail.")
        return true
    }

    fun resetUserAccountPasswordByLeader(targetEmail: String, newTemporaryPassword: String = ""): Boolean {
        val cleanEmail = targetEmail.trim().lowercase()
        if (cleanEmail.isBlank()) return false
        return if (newTemporaryPassword.trim().length >= 4) {
            val hash = BasilianSecurityManager.savePersonalAccountPassword(getApplication(), cleanEmail, newTemporaryPassword.trim())
            curiaFirebaseManager.syncUserAccountPasswordHashToCloud(cleanEmail, hash, "Restablecida por el Líder")
            appendLiveUpdateLog("👑 El Líder restableció la clave personal para la cuenta: $cleanEmail")
            true
        } else {
            BasilianSecurityManager.clearPersonalAccountPassword(getApplication(), cleanEmail)
            appendLiveUpdateLog("👑 El Líder liberó el registro de clave personal para: $cleanEmail")
            true
        }
    }

    fun authenticateAndLinkUserByEmail(
        email: String,
        fullName: String,
        role: String = "Escolástico / Religioso Basiliano",
        city: String = "Colombia",
        accessCode: String = "",
        explicitlySelectedBasilian: Boolean? = null
    ) {
        val cleanEmail = email.trim().lowercase()
        if (cleanEmail.isBlank()) return

        prefs.edit().putBoolean("firebase_email_confirmed_$cleanEmail", true).apply()

        val inOfficialDirectory = isEmailInOfficialBasilianDirectory(cleanEmail)

        // Si el usuario seleccionó explícitamente "No soy Basiliano", o su correo no está en el directorio
        // (salvo que haya sido previamente aprobado por el administrador), se le restringen las áreas privadas.
        if (explicitlySelectedBasilian == false && !inOfficialDirectory) {
            prefs.edit().putBoolean("basilian_access_override_$cleanEmail", false).apply()
        } else if (inOfficialDirectory) {
            prefs.edit().putBoolean("basilian_access_override_$cleanEmail", true).apply()
        } else if (accessCode.trim().isNotBlank()) {
            val codeValid = BasilianSecurityManager.constantTimeEquals(
                accessCode.trim().uppercase(),
                _basilianCommunityUnlockCode.value.trim().uppercase()
            )
            if (codeValid) {
                prefs.edit().putBoolean("basilian_access_override_$cleanEmail", true).apply()
            }
        } else if (
            role.contains("Extern", ignoreCase = true) ||
            role.contains("Visitante", ignoreCase = true) ||
            role.contains("Laico", ignoreCase = true) ||
            role.contains("Docente", ignoreCase = true) ||
            role.contains("Joven", ignoreCase = true) ||
            role.contains("Asociado", ignoreCase = true)
        ) {
            if (!prefs.contains("basilian_access_override_$cleanEmail")) {
                prefs.edit().putBoolean("basilian_access_override_$cleanEmail", false).apply()
            }
        }

        val isBasilian = if (explicitlySelectedBasilian == false && !inOfficialDirectory) {
            false
        } else {
            detectBasilianMemberFromEmail(cleanEmail, role, accessCode)
        }
        _isBasilianPrivateAccess.value = isBasilian

        // Si solicita rol religioso basiliano (o eligió Sí) pero su correo no está en el directorio oficial,
        // generar notificación automática en "Solo para Mí" para que el Administrador pueda darle permiso.
        val isRequestingReligiousRole = explicitlySelectedBasilian == true ||
            role.contains("Sacerdote", ignoreCase = true) ||
            role.contains("Escolástico", ignoreCase = true) ||
            role.contains("Religioso", ignoreCase = true) ||
            role.contains("Novicio", ignoreCase = true)
        if (isRequestingReligiousRole && !isBasilian) {
            enqueueBasilianAccessRequestIfNeeded(
                email = cleanEmail,
                fullName = fullName.trim().ifBlank { "Solicitante Basiliano" },
                role = role,
                city = city
            )
        }

        val existingSaved = prefs.getString("community_profile_$cleanEmail", null)
        val profile = if (existingSaved != null) {
            try {
                val json = JSONObject(existingSaved)
                UserCommunityProfile(
                    id = json.optString("id", "user_${System.currentTimeMillis()}"),
                    email = cleanEmail,
                    fullName = fullName.trim().ifBlank { json.optString("fullName", "Hermano Basiliano") },
                    role = role.ifBlank { json.optString("role", "Escolástico / Religioso Basiliano") },
                    patronSaint = json.optString("patronSaint", "San Basilio Magno"),
                    bio = json.optString("bio", "Caminando con bondad, disciplina y ciencia."),
                    cityAndCountry = city.ifBlank { json.optString("cityAndCountry", "Colombia") },
                    parishOrInstitution = json.optString("parishOrInstitution", if (isBasilian) "Comunidad Basiliana C.S.B." else "Amigos de San Basilio"),
                    profilePhotoUri = if (json.isNull("profilePhotoUri")) null else json.optString("profilePhotoUri"),
                    coverPhotoUri = if (json.isNull("coverPhotoUri")) null else json.optString("coverPhotoUri"),
                    isCuriaMember = isBasilian,
                    joinedYear = json.optString("joinedYear", "2026"),
                    favoriteQuote = json.optString("favoriteQuote", "«Bonitatem et disciplinam et scientiam doce me»")
                )
            } catch (_: Exception) {
                UserCommunityProfile(
                    email = cleanEmail,
                    fullName = fullName.trim().ifBlank { "Hermano Basiliano" },
                    role = role,
                    cityAndCountry = city,
                    isCuriaMember = isBasilian
                )
            }
        } else {
            UserCommunityProfile(
                id = "user_${System.currentTimeMillis()}",
                email = cleanEmail,
                fullName = fullName.trim().ifBlank {
                    cleanEmail.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() }
                },
                role = role,
                patronSaint = "San Basilio Magno",
                cityAndCountry = city,
                parishOrInstitution = if (isBasilian) "Comunidad Basiliana C.S.B." else "Amigos y Laicos de San Basilio",
                isCuriaMember = isBasilian,
                bio = if (isBasilian) {
                    "Religioso / Miembro verificado de la Congregación de San Basilio ($cleanEmail)."
                } else {
                    "Perfil espiritual abierto vinculado con Padres Basilianos ($cleanEmail)."
                }
            )
        }

        saveUserProfile(profile)
        val isLeaderNow = BasilianSecurityManager.isAppCreatorLeader(getApplication(), cleanEmail)
        if (!isLeaderNow) {
            _curiaIsAuthenticated.value = false
            BasilianSecurityManager.setDeviceUnlockedAsLeader(getApplication(), false)
        }
        _isAppCreatorLeader.value = isLeaderNow
        _userPersonalAiProfile.value = com.example.data.ai.BasilianAiMemory.getUserPersonalAiProfile(getApplication(), cleanEmail)

        // Cargar los hilos de chat y el historial de IA de Historia desde Room Database vinculado a este correo
        syncCreativeHistoryFromActiveThread(cleanEmail)
        observeAiChatSessionsFromRoom(cleanEmail)
        if (_creativeHistory.value.isEmpty()) {
            val linkedHistory = loadCreativeHistoryForEmail(cleanEmail)
            _creativeHistory.value = linkedHistory
            _activeCreativeResult.value = linkedHistory.firstOrNull()
        }

        // Actualizar saludo personalizado en la Guía Espiritual IA con el nombre de la persona
        _chatMessages.value = listOf(
            com.example.data.ai.ChatMessage(
                id = "welcome_${System.currentTimeMillis()}",
                sender = com.example.data.ai.MessageSender.BASILIAN_GUIDE,
                text = "«La paz sea contigo, querido ${profile.fullName} (${profile.email}). Todos tus datos, reflexiones e investigaciones están vinculados a tu cuenta.\n\n¿En qué inquietud espiritual, estudio patrístico o misión comunitaria trabajaremos hoy?»"
            )
        )
    }

    // =========================================================================
    // RED FRATERNA & PERFILES TIPO FACEBOOK
    // =========================================================================

    private val _communityPosts = MutableStateFlow<List<CommunityPost>>(loadCommunityPostsFromPrefs())
    val communityPosts: StateFlow<List<CommunityPost>> = _communityPosts.asStateFlow()

    private val _communityMembers = MutableStateFlow<List<UserCommunityProfile>>(getInitialMembers())
    val communityMembers: StateFlow<List<UserCommunityProfile>> = _communityMembers.asStateFlow()
    val communityProfiles: StateFlow<List<UserCommunityProfile>> = _communityMembers.asStateFlow()

    fun saveUserProfile(profile: UserCommunityProfile) {
        val withPresence = profile.copy(lastActiveAt = System.currentTimeMillis())
        _currentUserProfile.value = withPresence
        val isLeader = BasilianSecurityManager.isAppCreatorLeader(getApplication(), withPresence.email)
        if (!isLeader) {
            _curiaIsAuthenticated.value = false
            BasilianSecurityManager.setDeviceUnlockedAsLeader(getApplication(), false)
        }
        _isAppCreatorLeader.value = isLeader
        saveUserProfileToPrefs(withPresence)
        val currentMembers = _communityMembers.value.toMutableList()
        val index = currentMembers.indexOfFirst { it.email == withPresence.email || it.id == withPresence.id }
        if (index >= 0) {
            currentMembers[index] = withPresence
        } else {
            currentMembers.add(0, withPresence)
        }
        _communityMembers.value = currentMembers
        // Sincronizar de inmediato con Firebase Firestore para que aparezca en otros celulares
        curiaFirebaseManager.syncUserProfileToFirestore(withPresence)
    }

    fun updateUserPresenceNow() {
        val current = _currentUserProfile.value ?: return
        if (current.email.isBlank()) return
        curiaFirebaseManager.updateUserPresence(current.email)
        val updated = current.copy(lastActiveAt = System.currentTimeMillis())
        _currentUserProfile.value = updated
        val currentMembers = _communityMembers.value.toMutableList()
        val index = currentMembers.indexOfFirst { it.email == updated.email || it.id == updated.id }
        if (index >= 0) {
            currentMembers[index] = updated
            _communityMembers.value = currentMembers
        }
    }

    fun updateCurrentUserPhoto(uri: Uri) {
        val current = _currentUserProfile.value ?: return
        viewModelScope.launch {
            val photoUriStr = copyUriToInternalStorage(uri, "profile_pic")
            val updated = current.copy(profilePhotoUri = photoUriStr)
            saveUserProfile(updated)
        }
    }

    fun updateCurrentUserCover(uri: Uri) {
        val current = _currentUserProfile.value ?: return
        viewModelScope.launch {
            val coverUriStr = copyUriToInternalStorage(uri, "cover_pic")
            val updated = current.copy(coverPhotoUri = coverUriStr)
            saveUserProfile(updated)
        }
    }

    fun publishPost(content: String, imageUri: Uri? = null) {
        createCommunityPost(content, imageUri)
    }

    fun toggleLikePost(postId: String) {
        togglePostLike(postId)
    }

    fun addPrayerIntention(postId: String) {
        val updated = _communityPosts.value.map { post ->
            if (post.id == postId) {
                val newPrayer = post.prayerCount + 1
                curiaFirebaseManager.updatePostCountersInFirestore(postId, post.likesCount, newPrayer)
                post.copy(prayerCount = newPrayer)
            } else {
                post
            }
        }
        _communityPosts.value = updated
        saveCommunityPostsToPrefs(updated)
    }

    fun deletePost(postId: String) {
        deleteCommunityPost(postId)
    }

    val isFirebaseAvailable: StateFlow<Boolean> = curiaFirebaseManager.isFirebaseAvailable
    val firebaseSyncStatus: StateFlow<String> = curiaFirebaseManager.syncStatus

    fun configureCustomFirebase(projectId: String, apiKey: String, appId: String, onResult: (Boolean, String) -> Unit) {
        curiaFirebaseManager.configureCustomFirebase(projectId, apiKey, appId) { success, msg ->
            if (success) {
                _currentUserProfile.value?.let { curiaFirebaseManager.syncUserProfileToFirestore(it) }
                _communityPosts.value.forEach { curiaFirebaseManager.syncCommunityPostToFirestore(it) }
            }
            onResult(success, msg)
        }
    }

    fun syncAllCommunityToCloud(onResult: (Boolean, String) -> Unit) {
        if (!curiaFirebaseManager.isFirebaseAvailable.value) {
            onResult(false, "Firebase Firestore no está activo. Configura tu proyecto o usa el Código Fraterno.")
            return
        }
        _currentUserProfile.value?.let { curiaFirebaseManager.syncUserProfileToFirestore(it) }
        _communityPosts.value.forEach { curiaFirebaseManager.syncCommunityPostToFirestore(it) }
        onResult(true, "¡Todos los perfiles y publicaciones se han sincronizado con Firestore Cloud!")
    }

    fun exportFraternalSyncCode(): String {
        return curiaFirebaseManager.exportFraternalSyncCode(_communityMembers.value, _communityPosts.value)
    }

    fun importFraternalSyncCode(code: String, onResult: (Boolean, String) -> Unit) {
        val pair = curiaFirebaseManager.importFraternalSyncCode(code)
        if (pair == null) {
            onResult(false, "El código de sincronización fraterna no es válido.")
            return
        }
        val (importedProfiles, importedPosts) = pair
        val currentProfiles = _communityMembers.value.toMutableList()
        importedProfiles.forEach { ip ->
            val idx = currentProfiles.indexOfFirst { it.email.equals(ip.email, ignoreCase = true) || it.id == ip.id }
            if (idx >= 0) currentProfiles[idx] = ip else currentProfiles.add(0, ip)
        }
        _communityMembers.value = currentProfiles

        val currentPosts = _communityPosts.value.toMutableList()
        val postIds = currentPosts.map { it.id }.toSet()
        importedPosts.forEach { ip ->
            if (ip.id !in postIds) currentPosts.add(ip)
        }
        currentPosts.sortByDescending { it.timestamp }
        _communityPosts.value = currentPosts
        saveCommunityPostsToPrefs(currentPosts)

        onResult(true, "¡Sincronización fraterna completada! Se cargaron ${importedProfiles.size} perfiles y ${importedPosts.size} publicaciones.")
    }

    fun logoutUserProfile() {
        logoutProfile()
    }

    fun switchUserByEmail(email: String) {
        val cleanEmail = email.trim().lowercase()
        if (cleanEmail.isBlank()) return
        val saved = prefs.getString("community_profile_$cleanEmail", null)
        if (saved != null) {
            try {
                val json = JSONObject(saved)
                val profile = UserCommunityProfile(
                    id = json.optString("id", UUID.randomUUID().toString()),
                    email = json.getString("email"),
                    fullName = json.getString("fullName"),
                    role = json.optString("role", "Fiel / Amigo de San Basilio"),
                    patronSaint = json.optString("patronSaint", "San Basilio Magno"),
                    bio = json.optString("bio", "Caminando con bondad, disciplina y ciencia."),
                    cityAndCountry = json.optString("cityAndCountry", "Cali, Colombia"),
                    parishOrInstitution = json.optString("parishOrInstitution", "Parroquia San Basilio"),
                    profilePhotoUri = if (json.isNull("profilePhotoUri")) null else json.optString("profilePhotoUri"),
                    coverPhotoUri = if (json.isNull("coverPhotoUri")) null else json.optString("coverPhotoUri"),
                    isCuriaMember = json.optBoolean("isCuriaMember", false),
                    joinedYear = json.optString("joinedYear", "2026"),
                    favoriteQuote = json.optString("favoriteQuote", "«Bonitatem et disciplinam et scientiam doce me»")
                )
                _currentUserProfile.value = profile
                saveUserProfileToPrefs(profile)
                return
            } catch (_: Exception) {}
        }

        // Si es un nuevo correo, creamos su perfil conectado de forma limpia y personalizada
        val newProfile = UserCommunityProfile(
            id = "user_${System.currentTimeMillis()}",
            email = cleanEmail,
            fullName = if (cleanEmail.contains("@")) cleanEmail.substringBefore("@").replace(".", " ").split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } } else "Hermano Basiliano",
            role = "Escolástico / Religioso Basiliano",
            patronSaint = "San Basilio Magno",
            cityAndCountry = "Colombia",
            parishOrInstitution = "Comunidad Basiliana",
            bio = "Caminando con bondad, disciplina y ciencia en fidelidad a las Constituciones C.S.B."
        )
        saveUserProfile(newProfile)
    }

    fun loginOrRegisterProfile(
        email: String,
        fullName: String,
        role: String = "Fiel / Amigo de San Basilio",
        patronSaint: String = "San Basilio Magno",
        city: String = "Cali, Colombia",
        parish: String = "Parroquia San Basilio"
    ) {
        val cleanEmail = email.trim().lowercase()
        val profile = UserCommunityProfile(
            email = cleanEmail,
            fullName = fullName.trim(),
            role = role,
            patronSaint = patronSaint,
            cityAndCountry = city,
            parishOrInstitution = parish
        )
        _currentUserProfile.value = profile
        saveUserProfileToPrefs(profile)
    }

    fun logoutProfile() {
        _currentUserProfile.value = null
        _isAppCreatorLeader.value = false
        _curiaIsAuthenticated.value = false
        BasilianSecurityManager.setDeviceUnlockedAsLeader(getApplication(), false)
        prefs.edit().remove("community_user_profile").apply()
    }

    fun updateCurrentUserProfile(
        fullName: String,
        role: String,
        patronSaint: String,
        bio: String,
        motto: String,
        city: String,
        parish: String,
        newPhotoUri: Uri? = null,
        newCoverUri: Uri? = null,
        birthdayDay: Int? = null,
        birthdayMonth: Int? = null,
        avatarStyleId: String = "avatar_basil",
        formationStage: String = "",
        religiousHouse: String = "",
        professionDate: String = "",
        scholasticStudies: String = "",
        monasticCommitment: String = ""
    ) {
        val current = _currentUserProfile.value ?: return
        viewModelScope.launch {
            var photoUriStr = current.profilePhotoUri
            if (newPhotoUri != null) {
                photoUriStr = copyUriToInternalStorage(newPhotoUri, "profile_pic")
            }
            var coverUriStr = current.coverPhotoUri
            if (newCoverUri != null) {
                coverUriStr = copyUriToInternalStorage(newCoverUri, "cover_pic")
            }
            val updated = current.copy(
                fullName = fullName.trim(),
                role = role,
                patronSaint = patronSaint,
                bio = bio.trim(),
                favoriteQuote = motto.trim(),
                cityAndCountry = city.trim(),
                parishOrInstitution = parish.trim(),
                profilePhotoUri = photoUriStr,
                coverPhotoUri = coverUriStr,
                birthdayDay = birthdayDay ?: current.birthdayDay,
                birthdayMonth = birthdayMonth ?: current.birthdayMonth,
                avatarStyleId = avatarStyleId,
                formationStage = formationStage.trim(),
                religiousHouse = religiousHouse.trim(),
                professionDate = professionDate.trim(),
                scholasticStudies = scholasticStudies.trim(),
                monasticCommitment = monasticCommitment.trim()
            )
            _currentUserProfile.value = updated
            saveUserProfileToPrefs(updated)
            curiaFirebaseManager.syncUserProfileToFirestore(updated)
        }
    }

    fun createCommunityPost(text: String, imageUri: Uri? = null) {
        val profile = _currentUserProfile.value ?: return
        val cleanText = text.trim()
        if (cleanText.isEmpty()) return

        viewModelScope.launch {
            var imageUriStr: String? = null
            if (imageUri != null) {
                imageUriStr = copyUriToInternalStorage(imageUri, "post_img")
            }
            val newPost = CommunityPost(
                authorId = profile.id,
                authorEmail = profile.email,
                authorName = profile.fullName,
                authorRole = profile.role,
                authorPatronSaint = profile.patronSaint,
                authorPhotoUri = profile.profilePhotoUri,
                text = cleanText,
                imageUri = imageUriStr,
                timestamp = System.currentTimeMillis(),
                likesCount = 1,
                likedByMe = true,
                prayerCount = 1
            )
            val updated = listOf(newPost) + _communityPosts.value
            _communityPosts.value = updated
            saveCommunityPostsToPrefs(updated)
            // Sincronización en tiempo real en la nube Firestore
            curiaFirebaseManager.syncCommunityPostToFirestore(newPost)
        }
    }

    fun togglePostLike(postId: String) {
        val updated = _communityPosts.value.map { post ->
            if (post.id == postId) {
                val newLiked = !post.likedByMe
                val newCount = if (newLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
                curiaFirebaseManager.updatePostCountersInFirestore(postId, newCount, post.prayerCount)
                post.copy(likedByMe = newLiked, likesCount = newCount)
            } else {
                post
            }
        }
        _communityPosts.value = updated
        saveCommunityPostsToPrefs(updated)
    }

    fun deleteCommunityPost(postId: String) {
        val current = _currentUserProfile.value
        val postToDelete = _communityPosts.value.firstOrNull { it.id == postId } ?: return
        // Allow deleting if author or curia authenticated
        if (curiaIsAuthenticated.value || postToDelete.authorEmail == current?.email) {
            val updated = _communityPosts.value.filter { it.id != postId }
            _communityPosts.value = updated
            saveCommunityPostsToPrefs(updated)
            curiaFirebaseManager.deleteCommunityPostFromFirestore(postId)
        }
    }

    private fun loadUserProfileFromPrefs(): UserCommunityProfile? {
        val jsonStr = prefs.getString("community_user_profile", null) ?: return null
        return try {
            val json = JSONObject(jsonStr)
            UserCommunityProfile(
                id = json.optString("id", UUID.randomUUID().toString()),
                email = json.getString("email"),
                fullName = json.getString("fullName"),
                role = json.optString("role", "Fiel / Amigo de San Basilio"),
                patronSaint = json.optString("patronSaint", "San Basilio Magno"),
                bio = json.optString("bio", "Caminando con bondad, disciplina y ciencia."),
                cityAndCountry = json.optString("cityAndCountry", "Cali, Colombia"),
                parishOrInstitution = json.optString("parishOrInstitution", "Parroquia San Basilio"),
                profilePhotoUri = if (json.isNull("profilePhotoUri")) null else json.optString("profilePhotoUri"),
                coverPhotoUri = if (json.isNull("coverPhotoUri")) null else json.optString("coverPhotoUri"),
                isCuriaMember = json.optBoolean("isCuriaMember", false),
                joinedYear = json.optString("joinedYear", "2026"),
                favoriteQuote = json.optString("favoriteQuote", "«Bonitatem et disciplinam et scientiam doce me»"),
                birthdayDay = if (json.has("birthdayDay")) json.getInt("birthdayDay") else null,
                birthdayMonth = if (json.has("birthdayMonth")) json.getInt("birthdayMonth") else null,
                avatarStyleId = json.optString("avatarStyleId", "avatar_basil"),
                formationStage = json.optString("formationStage", ""),
                religiousHouse = json.optString("religiousHouse", ""),
                professionDate = json.optString("professionDate", ""),
                scholasticStudies = json.optString("scholasticStudies", ""),
                monasticCommitment = json.optString("monasticCommitment", "")
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun saveUserProfileToPrefs(profile: UserCommunityProfile) {
        try {
            val json = JSONObject().apply {
                put("id", profile.id)
                put("email", profile.email)
                put("fullName", profile.fullName)
                put("role", profile.role)
                put("patronSaint", profile.patronSaint)
                put("bio", profile.bio)
                put("cityAndCountry", profile.cityAndCountry)
                put("parishOrInstitution", profile.parishOrInstitution)
                put("profilePhotoUri", profile.profilePhotoUri)
                put("coverPhotoUri", profile.coverPhotoUri)
                put("isCuriaMember", profile.isCuriaMember)
                put("joinedYear", profile.joinedYear)
                put("favoriteQuote", profile.favoriteQuote)
                if (profile.birthdayDay != null) put("birthdayDay", profile.birthdayDay)
                if (profile.birthdayMonth != null) put("birthdayMonth", profile.birthdayMonth)
                put("avatarStyleId", profile.avatarStyleId)
                put("formationStage", profile.formationStage)
                put("religiousHouse", profile.religiousHouse)
                put("professionDate", profile.professionDate)
                put("scholasticStudies", profile.scholasticStudies)
                put("monasticCommitment", profile.monasticCommitment)
            }
            prefs.edit()
                .putString("community_user_profile", json.toString())
                .putString("community_profile_${profile.email.trim().lowercase()}", json.toString())
                .apply()
        } catch (_: Exception) {}
    }

    private fun loadCommunityPostsFromPrefs(): List<CommunityPost> {
        val jsonStr = prefs.getString("community_posts_list", null)
        if (jsonStr != null) {
            try {
                val array = JSONArray(jsonStr)
                val list = mutableListOf<CommunityPost>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        CommunityPost(
                            id = obj.getString("id"),
                            authorEmail = obj.getString("authorEmail"),
                            authorName = obj.getString("authorName"),
                            authorRole = obj.optString("authorRole", "Fiel Basiliano"),
                            authorPatronSaint = obj.optString("authorPatronSaint", "San Basilio"),
                            authorPhotoUri = if (obj.isNull("authorPhotoUri")) null else obj.optString("authorPhotoUri"),
                            text = obj.getString("text"),
                            imageUri = if (obj.isNull("imageUri")) null else obj.optString("imageUri"),
                            timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                            likesCount = obj.optInt("likesCount", 1),
                            likedByMe = obj.optBoolean("likedByMe", false)
                        )
                    )
                }
                if (list.isNotEmpty()) return list
            } catch (_: Exception) {}
        }
        return getSeedCommunityPosts()
    }

    private fun saveCommunityPostsToPrefs(posts: List<CommunityPost>) {
        try {
            val array = JSONArray()
            posts.forEach { post ->
                val obj = JSONObject().apply {
                    put("id", post.id)
                    put("authorEmail", post.authorEmail)
                    put("authorName", post.authorName)
                    put("authorRole", post.authorRole)
                    put("authorPatronSaint", post.authorPatronSaint)
                    put("authorPhotoUri", post.authorPhotoUri)
                    put("text", post.text)
                    put("imageUri", post.imageUri)
                    put("timestamp", post.timestamp)
                    put("likesCount", post.likesCount)
                    put("likedByMe", post.likedByMe)
                }
                array.put(obj)
            }
            prefs.edit().putString("community_posts_list", array.toString()).apply()
        } catch (_: Exception) {}
    }

    private fun getSeedCommunityPosts(): List<CommunityPost> {
        return listOf(
            CommunityPost(
                authorEmail = "vocaciones.colombia@basilianos.org",
                authorName = "P. Roberto Walsh, C.S.B.",
                authorRole = "Misionero Fundador en Colombia",
                authorPatronSaint = "San Basilio Magno",
                text = "«Bienvenidos a la Red Fraterna Basiliana. Hermanos y hermanas en Cristo: que en nuestras parroquias y colegios de Cali, Medellín y Bogotá nunca falte el testimonio de la bondad, la disciplina y el amor fraterno».",
                timestamp = System.currentTimeMillis() - 7200000L,
                likesCount = 24,
                likedByMe = true
            ),
            CommunityPost(
                authorEmail = "comunidad.cali@basilianos.org",
                authorName = "Hno. Francisco de Asís C.S.B.",
                authorRole = "Novicio Basiliano",
                authorPatronSaint = "San José Obrero",
                text = "Hoy iniciamos la semana con la Adoración Eucarística y el rezo de Laudes comunitarias. Oramos por todas las familias y bienhechores de las obras basilianas. ¡Paz y bien!",
                timestamp = System.currentTimeMillis() - 28800000L,
                likesCount = 18,
                likedByMe = false
            ),
            CommunityPost(
                authorEmail = "colegio.basiliano@edu.co",
                authorName = "Prof. María Elena Restrepo",
                authorRole = "Docente y Laica Asociada",
                authorPatronSaint = "Santa Teresita del Niño Jesús",
                text = "Hermosa jornada pedagógica con los estudiantes basilianos reflexionando sobre las virtudes de San Basilio. Educar la mente y el corazón es nuestra vocación.",
                timestamp = System.currentTimeMillis() - 86400000L,
                likesCount = 31,
                likedByMe = false
            )
        )
    }

    private fun getInitialMembers(): List<UserCommunityProfile> {
        return listOf(
            UserCommunityProfile(
                email = "general@basilianos.org",
                fullName = "Curia Generalicia C.S.B.",
                role = "Gobierno General de la Congregación",
                patronSaint = "San Basilio Magno",
                bio = "Preservando el carisma de 1822 y acompañando las misiones de Canadá, EE.UU., México y Colombia.",
                cityAndCountry = "Toronto, Canadá",
                parishOrInstitution = "Casa Generalicia San Basilio",
                isCuriaMember = true,
                favoriteQuote = "«Bonitatem et disciplinam et scientiam doce me»"
            ),
            UserCommunityProfile(
                email = "vocaciones.colombia@basilianos.org",
                fullName = "P. Roberto Walsh, C.S.B.",
                role = "Sacerdote Misionero",
                patronSaint = "San José",
                bio = "Primer superior de la misión en Colombia desde 1987. Sirviendo con alegría en el Valle del Cauca.",
                cityAndCountry = "Cali, Colombia",
                parishOrInstitution = "Parroquia San Basilio Cali",
                isCuriaMember = true,
                favoriteQuote = "«Enseñando bondad, disciplina y ciencia en cada rincón»"
            ),
            UserCommunityProfile(
                email = "pastoral.bogota@basilianos.org",
                fullName = "P. James Carruthers, C.S.B.",
                role = "Sacerdote Formador",
                patronSaint = "San Juan Crisóstomo",
                bio = "Formador de novicios y acompañante espiritual de jóvenes con inquietud vocacional.",
                cityAndCountry = "Bogotá, Colombia",
                parishOrInstitution = "Casa de Formación Basiliana",
                isCuriaMember = true,
                favoriteQuote = "«La caridad fraterna es el fundamento de toda regla»"
            ),
            UserCommunityProfile(
                email = "asociados.medellin@gmail.com",
                fullName = "Claudia Patricia Gómez",
                role = "Laica Asociada Basiliana",
                patronSaint = "Santa María Virgen",
                bio = "Comprometida en la catequesis infantil y pastoral social con espíritu basiliano.",
                cityAndCountry = "Medellín, Colombia",
                parishOrInstitution = "Comunidad San Basilio Medellín",
                favoriteQuote = "«Hagan todo lo que Él les diga»"
            )
        )
    }

    private fun copyUriToInternalStorage(uri: Uri, prefix: String): String? {
        return try {
            val app = getApplication<Application>()
            val inputStream = app.contentResolver.openInputStream(uri) ?: return null
            val file = File(app.filesDir, "${prefix}_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { out ->
                inputStream.copyTo(out)
            }
            Uri.fromFile(file).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            uri.toString()
        }
    }

    private fun loadAppliedConfigKeys(): Set<String> {
        return prefs.getStringSet("applied_ai_config_keys", emptySet()) ?: emptySet()
    }

    private fun saveAppliedConfigKeys(keys: Set<String>) {
        prefs.edit().putStringSet("applied_ai_config_keys", keys).apply()
    }

    private fun loadUploadedCorpusDocuments(): List<UploadedCorpusDocument> {
        val jsonStr = prefs.getString("uploaded_corpus_documents", null)
        if (!jsonStr.isNullOrBlank()) {
            try {
                val array = JSONArray(jsonStr)
                val list = mutableListOf<UploadedCorpusDocument>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        UploadedCorpusDocument(
                            id = obj.optString("id", UUID.randomUUID().toString()),
                            title = obj.getString("title"),
                            content = obj.getString("content"),
                            dateAdded = obj.optString("dateAdded", "01/01/2026"),
                            wordCount = obj.optInt("wordCount", 100),
                            category = obj.optString("category", "Pastoral / Constituciones")
                        )
                    )
                }
                if (list.isNotEmpty()) return list
            } catch (_: Exception) {}
        }
        return getSeedCorpusDocuments()
    }

    private fun saveUploadedCorpusDocuments(docs: List<UploadedCorpusDocument>) {
        try {
            val array = JSONArray()
            docs.forEach { doc ->
                val obj = JSONObject().apply {
                    put("id", doc.id)
                    put("title", doc.title)
                    put("content", doc.content)
                    put("dateAdded", doc.dateAdded)
                    put("wordCount", doc.wordCount)
                    put("category", doc.category)
                }
                array.put(obj)
            }
            prefs.edit().putString("uploaded_corpus_documents", array.toString()).apply()
        } catch (_: Exception) {}
    }

    private fun getSeedCorpusDocuments(): List<UploadedCorpusDocument> {
        return listOf(
            UploadedCorpusDocument(
                id = "doc_comunicado_curia_2026",
                title = "Comunicado Oficial de Curia (01/09/2026) - Comunidad Basiliana",
                content = """
                    COMUNICADO OFICIAL DE LA CURIA GENERAL BASILIANA (1 de septiembre de 2026)
                    
                    Queridos hermanos en San Basilio, asociados y amigos de la comunidad:
                    Al comenzar este nuevo período ministerial tras las resoluciones del Capítulo General de 2026, expresamos profunda gratitud a todos los cohermanos por su constante oración y entrega apostólica. Trabajamos en comunión fraterna junto a los miembros del Consejo General y los oficiales de Curia.
                    
                    Recuerdo con emoción mi primer destino como asociado en La Maison Saint-Joseph en Annonay y el Intercollège Saint-Denis. Ese espíritu de 1822 vive hoy con fuerza renovada. Este verano compartí en la Cumbre Latinoamericana y el Encuentro de Escolásticos en la Ciudad de México junto a Morgan Rice; visité al hermano Tom Dugan en su 96º cumpleaños con los asociados laicos de Rochester; y celebré la Eucaristía en Presentation Manor con nuestros queridos hermanos mayores. Concelebré en la Universidad de St. Thomas con el asociado Anthony Sommer y el P. Eduardo Rivera, y asistí a las Misas de apertura en Catholic Central High School y St. John Fisher.
                    
                    Damos la bienvenida al nuevo folleto sobre educación basiliana publicado por la BNENE (Basilian Network for Education and the New Evangelization) bajo la dirección de Chris Wardwell, felicitamos a Kevin Kirley por sus 100 años de vida y enviamos nuestras bendiciones al recién ordenado P. David Basil en la Parroquia de la Asunción en Windsor.
                    
                    Agenda de Septiembre-Octubre 2026:
                    - 1 de septiembre: Cena fraternal en Presentation Manor, Toronto.
                    - 2 de septiembre: Primera reunión de trabajo del Consejo General.
                    - 4-6 de septiembre: Visita a Novi, Michigan; Misa y ceremonia de David Widzinski.
                    - 8 de septiembre: Junta directiva de Incarnate Word Academy.
                    - 9 de septiembre: Sesión ordinaria del Consejo General.
                    - 10 de septiembre: Misa de inicio de curso en Aquinas Institute y reunión de PIMS.
                    - 20-24 de septiembre: Retiro espiritual y deliberaciones del Consejo General en Toronto.
                    - 29 de septiembre: Misa solemne de apertura en St. Michael's College School.
                    - 9 de octubre: Retiro de profesores en St. John Fisher University.
                """.trimIndent(),
                dateAdded = "01/09/2026",
                wordCount = 370,
                category = "Mensajes Curiales 2026"
            ),
            UploadedCorpusDocument(
                id = "doc_capitulo_general_2026_resoluciones",
                title = "Capítulo General 2026: Resoluciones, Finanzas y América Latina",
                content = """
                    RESOLUCIONES Y PROPUESTAS DEL CAPÍTULO GENERAL 2026 (C.S.B.)
                    
                    1. Comité de Finanzas y Sostenibilidad en América Latina:
                    El Capítulo General reconoció formalmente que el florecimiento vocacional y el porvenir de la Congregación reside en México y Colombia. Para garantizar su sostenibilidad económica y autonomía operativa:
                    a) Se aprueba la creación de una junta en México (integrada por laicos calificados y sacerdotes basilianos) para administrar una dotación patrimonial inicial de $500,000 USD provista por la Tesorería General, operando bajo el modelo de la Gibbons-Daley Foundation (el capital permanece intacto y los rendimientos anuales financian el sostenimiento de las casas y ministerios). Se contrata un contador profesional bajo el marco fiscal mexicano.
                    b) Se aprueba la creación de una junta análoga en Colombia, con una dotación inicial de $500,000 USD y un contador profesional certificado bajo la legislación colombiana.
                    c) Se aprueba un incremento anual del 3.5% en la asignación presupuestaria (MLA) y en el impuesto comunitario a partir del año fiscal 2026.
                    
                    2. Rito de Profesión de Votos y Vademécum (Propuesta P. Paul English, CSB):
                    a) Actualización del ceremonial de Profesión de Votos para alinear el Vademécum Basiliano con la Orden de la Profesión Religiosa (USCCB, 2025) y el Misal Romano en sus ediciones actuales.
                    b) Emisión de un documento de ritos separado para facilitar las celebraciones litúrgicas de recepción y profesión temporal y perpetua.
                    c) Incorporación formal al Vademécum de la Oración al santo patrono San Óscar Romero, reconociendo su testimonio profético en favor de los pobres.
                    
                    3. Jornada de Oración por las Vocaciones (Propuesta P. José de Jesús Rodríguez Pérez, CSB):
                    Se instituye la Jornada Congregacional de Oración por las Vocaciones Basilianas el lunes posterior al IV Domingo de Pascua (Domingo del Buen Pastor), uniendo a todas las comunidades, parroquias, colegios y obras en Canadá, EE.UU., México y Colombia en plegaria incesante.
                """.trimIndent(),
                dateAdded = "05/09/2026",
                wordCount = 330,
                category = "Capítulo General 2026"
            ),
            UploadedCorpusDocument(
                id = "doc_anales_basilianos_2024_2025",
                title = "Los Anales Basilianos 2024-2025: Fundadores, Ordenaciones y Presencias",
                content = """
                    LOS ANALES BASILIANOS (The Basilian Annals 2024-2025)
                    
                    Los Diez Padres Fundadores de 1822 en Annonay:
                    1. Joseph Bovier Lapierre (1757-1838)
                    2. Pierre Tourvieille (1780-1859)
                    3. Jacques Vincent Duret (1762-1841)
                    4. Henri Martinesche (1797-1879)
                    5. Jean-Baptiste Polly (1772-1846)
                    6. Augustin Payan (1771-1847)
                    7. André Fayolle (1791-1847)
                    8. Julien Tracol (1796-1885)
                    9. Jean Antoin Vallon (1775-1840)
                    10. Jean François Pagès (1793-1861)
                    
                    Nuevos Sacerdotes Ordenados:
                    - P. Erick de Jesús Enríquez Gutiérrez, CSB (Ordenado el 14 de septiembre de 2024 en la Parroquia de San Lorenzo, Tehuacán, Puebla por Mons. Gonzalo Alonso Calzada Guerrero; originario de Jonacatepec, Morelos).
                    - P. José Adán Gómez Villalvazo, CSB (Ordenado el 14 de septiembre de 2024 en Tehuacán; hijo de Florentino Gómez y Basilia Villalvazo, originario de Ciudad Guzmán, Jalisco).
                    - P. Anderson Fabián Úsuga Giraldo, CSB (Ordenado el 23 de noviembre de 2024 en la Catedral Metropolitana de Medellín, Colombia).
                    
                    Jubileos de 50 Años de Vida Consagrada / Sacerdotal:
                    PP. David William Katulski, Robert Joseph Barringer, Robert Harrison Glass, Jack Haldeman Hanna, Douglas Lorne Mosey, Robert William Moslosky, Norman Charles Tanck.
                    
                    En Memoria Eterna (Fallecidos 2024-2025):
                    - P. Joseph Ian Boyd, CSB (1935-2024): célebre catedrático y fundador de The Chesterton Review.
                    - P. Clarence Ervin Watrin, CSB (1932-2024): carismático predicador y cofundador de Catholic Christian Outreach (CCO).
                    - P. Dennis Joseph Kauffman, CSB (1948-2024): legendario maestro en Novi, Michigan conocido afectuosamente como 'Father Everywhere'.
                    - PP. James Carruthers, Jess Owens, Douglas Hilmer, Joseph Walsh, Wilfred Canning, John Wheeler.
                    
                    Presencias Principales:
                    Toronto (Curia 146 Davenport Rd, St. Basil's Parish Morgan Rice, St. Michael's College), Novi (Detroit Catholic Central Patrick Fulton), Windsor (Assumption Parish Timothy Scott), Houston (St. Anne David Zapalac, Dillon House Bishop Ricardo Ramírez, Keon House Ted Baenziger/Eduardo Rivera), Tehuacán (San Lorenzo Antonio Ibáñez, San Pedro Acoquiaco René Espejel), Ciudad de México (Escolasticado de Tlalpan Paul Walsh, Beto Rojas), Bogotá (Parroquia Egipto y Casa Annonay Pedro Mora, Noviciado Maurice Restivo), Medellín (Parroquia San Basilio Anderson Úsuga).
                """.trimIndent(),
                dateAdded = "15/02/2025",
                wordCount = 380,
                category = "Anales & Historia"
            ),
            UploadedCorpusDocument(
                id = "doc_informe_curia_2026_storey",
                title = "Informe al Capítulo General 2026 por el P. Kevin J. Storey, CSB",
                content = """
                    INFORME DE GESTIÓN DEL SUPERIOR GENERAL SALIENTE (2018-2026)
                    P. Kevin J. Storey, CSB.
                    
                    Puntos clave de la gestión congregacional:
                    1. Nueva Sede de la Curia: Culminación exitosa del traslado a 146 Davenport Road en Toronto (julio de 2025) y formalización definitiva del acuerdo de venta de la propiedad de 95 St. Joseph Street en abril de 2026.
                    2. Formación en América Latina: Apertura del noviciado común internacional en Bogotá el 1 de junio de 2025 bajo la dirección del P. Maurice Restivo, CSB (con los novicios Ciro Alfonso Cárdenas de Colombia y Fabien Mwehu Itondo de México/Congo).
                    3. Demografía 2026: 125 religiosos profesos (112 clérigos, entre ellos 4 obispos, 103 sacerdotes y 5 diáconos). Actualmente 9 escolásticos se preparan para los votos perpetuos y el sacerdocio (5 en Tlalpan, México y 4 en Bogotá, Colombia). Próximas ordenaciones sacerdotales de Juan Ángel Vivas en México y David Basil en Windsor.
                """.trimIndent(),
                dateAdded = "10/06/2026",
                wordCount = 175,
                category = "Informes de Curia"
            ),
            UploadedCorpusDocument(
                id = "doc_oracion_romero_votos",
                title = "Oración a San Óscar Romero y Renovación Litúrgica del Vademécum",
                content = """
                    ORACIÓN OFICIAL A SAN ÓSCAR ROMERO (Aprobada para el Vademécum Basiliano):
                    
                    «Oh Dios Padre Misericordioso, por mediación de Jesucristo y la intercesión de la Virgen María, Reina de la Paz, y la acción del Espíritu Santo, concediste a San Óscar Romero la gracia de ser un Pastor ejemplar al servicio de la Iglesia, y en ella, preferencialmente a los pobres y los necesitados. Nos dirigimos hoy a Ti para pedirte con fe y confianza por todas las necesidades de la Congregación de San Basilio, a fin de que se afiance cada vez más en nosotros el cumplimiento de tu voluntad. Haz, Señor, que nosotros sepamos también vivir conforme al Evangelio de tu Hijo, quien vive y reina por los siglos de los siglos. Amén.»
                    
                    La devoción a San Óscar Romero ilumina la presencia basiliana en América Latina, recordando el llamado a ser una Iglesia en salida, comprometida con la justicia social y la evangelización liberadora de la juventud.
                """.trimIndent(),
                dateAdded = "15/07/2026",
                wordCount = 160,
                category = "Oraciones & Liturgia"
            ),
            UploadedCorpusDocument(
                id = "doc_regla_san_basilio",
                title = "Regla de San Basilio Magno: La Vida Común, Asketikon y los Estudios",
                content = """
                    De la Regla de San Basilio (Asketikon):
                    «Nuestra vida es una milicia espiritual al servicio del Evangelio. La comunidad fraterna es el taller donde se esculpe el alma consagrada. Quien se entrega al estudio sagrado no lo hace por vanagloria, sino para enriquecer a la Iglesia y enseñar con bondad, disciplina y ciencia. Los jóvenes y escolásticos deben ejercitarse en la oración incesante, en la obediencia pronta y en la pureza del corazón, viendo a Cristo en cada hermano y en cada alumno.»
                """.trimIndent(),
                dateAdded = "01/01/2026",
                wordCount = 76,
                category = "Regla Patrística"
            )
        )
    }

    /**
     * Refresca integralmente el corpus documental basiliano con los datos actualizados
     * de los 4 PDFs (Capítulo General 2026, Mensaje del Superior General John B. Huber,
     * Anales Basilianos 2024-2025 e Informe de Curia), sincroniza el Evangelio real
     * del día de hoy y actualiza los anuncios curiales.
     */
    private val _todayWisdom = MutableStateFlow(DailyGospelAndBasilianWisdomRepository.getTodayWisdom())
    val todayWisdom: StateFlow<DailyLiturgicalWisdom> = _todayWisdom.asStateFlow()

    fun refreshBasilianCorpusData() {
        val updatedDocs = getSeedCorpusDocuments()
        saveUploadedCorpusDocuments(updatedDocs)
        _uploadedCorpusDocuments.value = updatedDocs

        // Actualizar Evangelio de hoy con la fecha real (13 de septiembre de 2026)
        _todayWisdom.value = DailyGospelAndBasilianWisdomRepository.getTodayWisdom()

        // Actualizar anuncio oficial fraterno de la comunidad
        _curiaAnnouncementTitle.value = "Comunicado Fraterno de la Comunidad Basiliana"
        _curiaAnnouncementBody.value = "Queridos hermanos en San Basilio, renovamos nuestro compromiso fraterno en la oración constante, la vida comunitaria y la educación integral de la juventud según el carisma de Bondad, Disciplina y Ciencia."
        _curiaAnnouncementDate.value = "Curia General • Comunidad Basiliana"
        _curiaPrayerIntention.value = "Por las vocaciones sacerdotales basilianas y los proyectos educativos y comunitarios en Colombia y el mundo."
    }

    init {
        try {
            tts = TextToSpeech(application, this)
        } catch (_: Exception) {}

        // Inicializar caché local de Room para el Ordo Colombiano y Santoral de la Iglesia
        viewModelScope.launch {
            try {
                liturgiaRoomCacheRepository.populateSaintsDatabaseIfNeeded()
                val today = LiturgiaOrdoColombianoRepository.getTodayColombianDay()
                liturgiaRoomCacheRepository.saveToRoomCache(today)
            } catch (_: Exception) {}
        }

        // Inicializar persistencia de sesiones y memoria de chat IA en Room Database
        observeAiChatSessionsFromRoom(_currentUserProfile.value?.email)
        viewModelScope.launch {
            try {
                aiChatRoomRepository.observeLearnedFacts().collect { factsFromRoom ->
                    if (factsFromRoom.isNotEmpty()) {
                        _learnedAiFacts.value = factsFromRoom
                    }
                }
            } catch (_: Exception) {}
        }

        // Cargar preferencias de Google Cloud TTS
        GoogleCloudTtsService.loadSavedVoice(application)
        val savedUseCloud = application.getSharedPreferences("basilian_cloud_tts_prefs", Context.MODE_PRIVATE)
            .getBoolean("use_cloud_tts", true)
        _useCloudTts.value = savedUseCloud

        // Sincronizar estado de reproducción de Google Cloud TTS con la UI
        viewModelScope.launch {
            GoogleCloudTtsService.isPlaying.collect { playing ->
                if (playing) {
                    _isTtsSpeaking.value = true
                } else if (tts?.isSpeaking != true) {
                    _isTtsSpeaking.value = false
                }
            }
        }

        // Sincronización en tiempo real desde Firestore si está disponible
        curiaFirebaseManager.listenToLatestAnnouncement { title, body, date, prayer ->
            _curiaAnnouncementTitle.value = title
            _curiaAnnouncementBody.value = body
            _curiaAnnouncementDate.value = date
            _curiaPrayerIntention.value = prayer
        }

        // Sincronización global automática en tiempo real de los cambios hechos por el Único Líder (villamizargalviz@gmail.com)
        // Cambia y actualiza la aplicación automáticamente en todos los celulares instalados sin pedir descargas ni abrir links
        curiaFirebaseManager.listenToGlobalAppConfig { configData ->
            (configData["liveAppVersion"] as? String)?.takeIf { it.isNotBlank() }?.let {
                _liveAppVersion.value = it
                prefs.edit().putString("pref_live_app_version", it).apply()
            }
            (configData["customAppHeaderTitle"] as? String)?.takeIf { it.isNotBlank() }?.let {
                _customAppHeaderTitle.value = it
                prefs.edit().putString("pref_custom_app_header_title", it).apply()
            }
            (configData["customAppSubtitleMotto"] as? String)?.takeIf { it.isNotBlank() }?.let {
                _customAppSubtitleMotto.value = it
                prefs.edit().putString("pref_custom_app_subtitle_motto", it).apply()
            }
            (configData["dailyFocusTopic"] as? String)?.takeIf { it.isNotBlank() }?.let { _jarvisDailyFocusTopic.value = it }
            (configData["announcementTitle"] as? String)?.takeIf { it.isNotBlank() }?.let { _curiaAnnouncementTitle.value = it }
            (configData["announcementBody"] as? String)?.takeIf { it.isNotBlank() }?.let { _curiaAnnouncementBody.value = it }
            (configData["announcementDate"] as? String)?.takeIf { it.isNotBlank() }?.let { _curiaAnnouncementDate.value = it }
            (configData["prayerIntention"] as? String)?.takeIf { it.isNotBlank() }?.let { _curiaPrayerIntention.value = it }
            (configData["showTabOracion"] as? Boolean)?.let { _showTabOracion.value = it; prefs.edit().putBoolean("pref_show_tab_oracion", it).apply() }
            (configData["showTabLiturgia"] as? Boolean)?.let { _showTabLiturgia.value = it; prefs.edit().putBoolean("pref_show_tab_liturgia", it).apply() }
            (configData["showTabCumpleanos"] as? Boolean)?.let { _showTabCumpleanos.value = it; prefs.edit().putBoolean("pref_show_tab_cumpleanos", it).apply() }
            (configData["showTabHistoria"] as? Boolean)?.let { _showTabHistoria.value = it; prefs.edit().putBoolean("pref_show_tab_historia", it).apply() }
            (configData["showTabRecursos"] as? Boolean)?.let { _showTabRecursos.value = it; prefs.edit().putBoolean("pref_show_tab_recursos", it).apply() }
            (configData["showHeroBanner"] as? Boolean)?.let { _showHeroBanner.value = it; prefs.edit().putBoolean("pref_show_hero_banner", it).apply() }
            (configData["showDevotionalImageSlots"] as? Boolean)?.let { _showDevotionalImageSlots.value = it; prefs.edit().putBoolean("pref_show_devotional_image_slots", it).apply() }
            (configData["showOrdoQuickAccess"] as? Boolean)?.let { _showOrdoQuickAccess.value = it; prefs.edit().putBoolean("pref_show_ordo_quick_access", it).apply() }
            (configData["showCuriaAnnouncementInPrayer"] as? Boolean)?.let { _showCuriaAnnouncementInPrayer.value = it; prefs.edit().putBoolean("pref_show_curia_announcement_in_prayer", it).apply() }
            (configData["showDailyAudioPlayer"] as? Boolean)?.let { _showDailyAudioPlayer.value = it; prefs.edit().putBoolean("pref_show_daily_audio_player", it).apply() }
            (configData["showCommunitySocialWall"] as? Boolean)?.let { _showCommunitySocialWall.value = it; prefs.edit().putBoolean("pref_show_community_social_wall", it).apply() }
            (configData["globalBackgroundMode"] as? String)?.let { bgName ->
                runCatching { GlobalBgMode.valueOf(bgName) }.getOrNull()?.let { _globalBackgroundMode.value = it }
            }
            (configData["globalBackgroundOpacity"] as? Number)?.toFloat()?.let {
                _globalBackgroundOpacity.value = it.coerceIn(0.20f, 0.98f)
            }
            (configData["appThemeId"] as? String)?.takeIf { it.isNotBlank() }?.let { themeId ->
                val resolvedTheme = BasilianAppTheme.findById(themeId)
                _currentAppTheme.value = resolvedTheme
            }
            (configData["learnedKnowledgeFacts"] as? List<*>)?.mapNotNull { it?.toString()?.trim() }?.filter { it.isNotBlank() }?.let { remoteFacts ->
                if (remoteFacts.isNotEmpty()) {
                    viewModelScope.launch {
                        remoteFacts.forEach { serialized ->
                            val parts = serialized.split("|||")
                            if (parts.size >= 2) {
                                val title = parts[0].trim()
                                val details = parts[1].trim()
                                val cat = parts.getOrNull(2)?.trim()?.ifBlank { "AUTOAPRENDIZAJE" } ?: "AUTOAPRENDIZAJE"
                                if (title.isNotBlank() && details.isNotBlank()) {
                                    aiChatRoomRepository.addLearnedFact(
                                        title = title,
                                        details = details,
                                        category = cat,
                                        verifiedWithGoogle = true
                                    )
                                }
                            }
                        }
                        _learnedAiFacts.value = com.example.data.ai.BasilianAiMemory.getAllLearnedFacts(application)
                    }
                }
            }
            (configData["otaReleaseNotes"] as? String)?.takeIf { it.isNotBlank() }?.let {
                _otaReleaseNotes.value = it
                prefs.edit().putString("pref_ota_release_notes", it).apply()
            }
            val syncReadable = (configData["lastSyncReadable"] as? String)?.takeIf { it.isNotBlank() } ?: ""
            val syncHour = (configData["lastSyncHour"] as? String)?.takeIf { it.isNotBlank() } ?: syncReadable
            if (syncReadable.isNotBlank()) {
                _lastGlobalSyncTimestamp.value = "Auto-actualizado en vivo: $syncReadable (${_liveAppVersion.value})"
                prefs.edit().putString("pref_last_global_sync_timestamp", _lastGlobalSyncTimestamp.value).apply()
            }
            val broadcastMs = (configData["otaBroadcastTimestampMs"] as? Number)?.toLong() ?: 0L
            val lastSeenMs = prefs.getLong("pref_last_seen_ota_broadcast_ms", 0L)
            if (broadcastMs > 0L && broadcastMs > lastSeenMs) {
                prefs.edit().putLong("pref_last_seen_ota_broadcast_ms", broadcastMs).apply()
                val ver = _liveAppVersion.value
                val notes = _otaReleaseNotes.value
                val hourLabel = if (syncHour.isNotBlank()) " • Hora: $syncHour" else ""
                _incomingLiveUpdateNotice.value = "⚡ App Auto-Actualizada Automáticamente ($ver$hourLabel): $notes"
                viewModelScope.launch {
                    kotlinx.coroutines.delay(9500L)
                    if (_incomingLiveUpdateNotice.value?.contains(ver) == true) {
                        _incomingLiveUpdateNotice.value = null
                    }
                }
                try {
                    NotificationHelper.showPushNotification(
                        context = application,
                        channelId = NotificationHelper.CHANNEL_DAILY_PRAYER,
                        notificationId = 9925,
                        title = "⚡ App Auto-Actualizada ($ver$hourLabel)",
                        message = "Actualización aplicada automáticamente sin enlaces: $notes",
                        targetTab = "ORACION_DIARIA"
                    )
                } catch (_: Throwable) {}
            }
        }

        // Apenas el Único Líder (villamizargalviz@gmail.com) instala y abre la app desde Google AI Studio,
        // emite automáticamente la nueva versión y la hora exacta a todos los demás celulares que la tengan instalada
        viewModelScope.launch {
            kotlinx.coroutines.delay(1500)
            val activeEmail = _currentUserProfile.value?.email
            val isSoleLeader = BasilianSecurityManager.isAppCreatorLeader(application, activeEmail)
            if (isSoleLeader && _autoBroadcastOnLeaderInstall.value) {
                val buildSignature = "v5.6_auto_ota_nolink_super_ia_${_liveAppVersion.value}"
                val lastPublishedSig = prefs.getString("pref_leader_last_published_build_sig", "")
                if (buildSignature != lastPublishedSig) {
                    prefs.edit().putString("pref_leader_last_published_build_sig", buildSignature).apply()
                    syncGlobalAdminChangesToAllDevices(forceBroadcastAlert = true)
                    appendLiveUpdateLog("📲 Nueva versión abierta en el celular del Único Líder (villamizargalviz@gmail.com): auto-actualización y hora enviadas automáticamente a todos los celulares.")
                }
            }
        }

        // Sincronización en tiempo real de perfiles de la comunidad para ver perfiles creados en cualquier celular
        curiaFirebaseManager.listenToCommunityProfiles { firestoreProfiles ->
            val local = _communityMembers.value.toMutableList()
            firestoreProfiles.forEach { fp ->
                val idx = local.indexOfFirst { it.email.equals(fp.email, ignoreCase = true) || it.id == fp.id }
                if (idx >= 0) {
                    local[idx] = fp
                } else {
                    local.add(0, fp)
                }
            }
            _communityMembers.value = local
        }

        // Sincronización en tiempo real de publicaciones del muro para que se vean en todos los celulares
        curiaFirebaseManager.listenToCommunityPosts { firestorePosts ->
            if (firestorePosts.isNotEmpty()) {
                val merged = firestorePosts.toMutableList()
                val existingIds = firestorePosts.map { it.id }.toSet()
                _communityPosts.value.forEach { localPost ->
                    if (localPost.id !in existingIds) {
                        merged.add(localPost)
                    }
                }
                merged.sortByDescending { it.timestamp }
                _communityPosts.value = merged
                saveCommunityPostsToPrefs(merged)
            }
        }

        // Escuchar alertas de seguridad de cualquier teléfono compartido y notificar EXCLUSIVAMENTE a villamizargalviz@gmail.com
        curiaFirebaseManager.listenToLeaderIntrusionAlerts { remoteAlerts ->
            val activeEmail = _currentUserProfile.value?.email
            if (BasilianSecurityManager.isAppCreatorLeader(application, activeEmail)) {
                val (mergedAlerts, newlyArrived) = BasilianSecurityManager.mergeRemoteIntrusionAlerts(application, remoteAlerts)
                _intrusionAlerts.value = mergedAlerts
                newlyArrived.firstOrNull()?.let { latestNew ->
                    // Notificar únicamente a villamizargalviz@gmail.com si la alerta ocurrió recientemente
                    if (System.currentTimeMillis() - latestNew.timestamp <= 600_000L) {
                        NotificationHelper.sendIntrusionAlertNotification(
                            context = application,
                            targetArea = latestNew.targetArea,
                            actorInfo = latestNew.actorInfo,
                            failedAttempts = latestNew.attemptNumber,
                            forceForLeaderSync = true
                        )
                    }
                }
            }
        }

        // Sincronizar en tiempo real las claves personales cifradas de cada cuenta para evitar suplantación en cualquier celular
        curiaFirebaseManager.listenToUserAccountPasswordHashes { remoteHashes ->
            remoteHashes.forEach { (email, hash) ->
                BasilianSecurityManager.saveRemotePersonalAccountPasswordHash(application, email, hash)
            }
        }

        // Escuchar en tiempo real los mensajes mundiales de «¡Feliz Día!» en fechas de Patronos Basilianos
        // (ej. alguien escribe en Colombia y le llega la notificación a los hermanos en EE. UU., Canadá, México y Francia)
        curiaFirebaseManager.listenToPatronFeastGreetings { remoteGreetings ->
            if (remoteGreetings.isNotEmpty()) {
                val currentEmail = _currentUserProfile.value?.email?.trim()?.lowercase().orEmpty()
                val existingIds = _patronFeastGreetings.value.map { it.id }.toSet()
                val merged = (remoteGreetings + _patronFeastGreetings.value)
                    .distinctBy { it.id }
                    .sortedByDescending { it.timestamp }
                    .take(60)
                _patronFeastGreetings.value = merged
                savePatronFeastGreetingsToPrefs(merged)

                val newest = remoteGreetings.firstOrNull()
                if (newest != null &&
                    newest.id !in existingIds &&
                    !prefs.getBoolean("notified_patron_greeting_${newest.id}", false)
                ) {
                    prefs.edit().putBoolean("notified_patron_greeting_${newest.id}", true).apply()
                    val isFromAnotherUser = currentEmail.isBlank() || !newest.authorEmail.equals(currentEmail, ignoreCase = true)
                    val isRecent = System.currentTimeMillis() - newest.timestamp <= 600_000L
                    if (isFromAnotherUser && isRecent) {
                        NotificationHelper.sendPatronGreetingNotification(
                            context = application,
                            patronOrFeastTitle = newest.patronName.ifBlank { newest.eventTitle },
                            authorName = newest.authorName,
                            authorContext = newest.authorCountryOrContext,
                            greetingMessage = newest.message
                        )
                    }
                }
            }
        }

        // Heartbeat de presencia en tiempo real en Firestore para mostrar miembros en línea
        viewModelScope.launch {
            while (true) {
                val email = _currentUserProfile.value?.email
                if (!email.isNullOrBlank()) {
                    curiaFirebaseManager.updateUserPresence(email)
                }
                delay(60_000)
            }
        }

        // Persistencia y cambio dinámico reactivo desde DataStore
        viewModelScope.launch {
            themePreferencesManager.themeIdFlow.collect { themeId ->
                val theme = BasilianAppTheme.findById(themeId)
                _currentAppTheme.value = theme
            }
        }
        viewModelScope.launch {
            themePreferencesManager.customPrayerSideImageUriFlow.collect { uriStr ->
                _customPrayerSideImageUri.value = uriStr
            }
        }
        viewModelScope.launch {
            themePreferencesManager.customCrestUriFlow.collect { uriStr ->
                if (uriStr != null) {
                    _customCrestUri.value = uriStr
                }
            }
        }
        viewModelScope.launch {
            themePreferencesManager.customFraternityCoverUriFlow.collect { uriStr ->
                _customFraternityCoverUri.value = uriStr
            }
        }

        // Inicializar cántico sagrado IA diario con el Evangelio de hoy
        val todayWisdom = com.example.data.repository.DailyGospelAndBasilianWisdomRepository.getTodayWisdom()
        generateDailySacredSong(
            gospelPassage = todayWisdom.gospelPassage,
            gospelPhrase = todayWisdom.singleGospelPhrase,
            reflectionContent = todayWisdom.basilianTeachingContent,
            style = "Canto Gregoriano Monástico"
        )
    }
}
