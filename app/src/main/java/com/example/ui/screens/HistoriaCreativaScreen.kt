package com.example.ui.screens

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.audio.GoogleCloudTtsService
import com.example.data.ai.BasilianAiMemory
import com.example.data.ai.BasilianAiService
import com.example.data.ai.CreativeHistoryMode
import com.example.data.ai.GeminiThinkingDepth
import com.example.data.ai.UserAiDialogueStyle
import com.example.data.model.AppLanguage
import com.example.data.security.BasilianSecurityManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.ui.components.Futuristic3DSpatialOverlay
import com.example.ui.components.FuturisticDesignPalette
import com.example.ui.components.FuturisticFormattedTextWithQuotes
import com.example.ui.components.JarvisFuturistaCanvas
import com.example.ui.components.futuristic3DCard
import com.example.ui.i18n.AppStrings
import com.example.ui.viewmodel.BasilianViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.sin

fun CreativeHistoryMode.getLocalizedLabel(strings: AppStrings): String {
    return when (this) {
        CreativeHistoryMode.HISTORIA_COMPLETA -> strings.historiaIa.modeFullHistory
        CreativeHistoryMode.RELATO_EPICO -> strings.historiaIa.modeEpicChronicle
        CreativeHistoryMode.HOMILIA_CHARLA -> strings.historiaIa.modeHomily
        CreativeHistoryMode.TRIVIA_QUIZ -> strings.historiaIa.modeTrivia
        CreativeHistoryMode.CREATIVO_LIBRE -> strings.historiaIa.modeFreeCreative
        CreativeHistoryMode.CREAR_IMAGENES -> strings.historiaIa.modeCreateImages
        CreativeHistoryMode.CREAR_MUSICA -> strings.historiaIa.modeMusic
    }
}

private val HoloCyan = Color(0xFF00F0FF)
private val HoloElectricBlue = Color(0xFF0284C7)
private val HoloGold = Color(0xFFF59E0B)
private val HoloGoldLight = Color(0xFFFDE68A)
private val HoloEmerald = Color(0xFF10B981)
private val HoloMagenta = Color(0xFFE879F9)
private val HoloGlassCard = Color(0xFF051329).copy(alpha = 0.92f)
private val HoloGlassSurface = Color(0xFF0A1D3B).copy(alpha = 0.88f)

data class LiveVoiceConversationTurn(
    val id: String,
    val userText: String,
    val aiResponse: String,
    val timestampLabel: String,
    val generatedImageSubject: String? = null,
    val isMusicCreation: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoriaCreativaScreen(
    viewModel: BasilianViewModel,
    appLanguage: AppLanguage = AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val prefs = remember { context.getSharedPreferences("basilian_prefs", Context.MODE_PRIVATE) }
    val isSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val isLoading by viewModel.isCreativeAiLoading.collectAsState()
    val activeResult by viewModel.activeCreativeResult.collectAsState()
    val historyList by viewModel.creativeHistory.collectAsState()
    val currentUserProfile by viewModel.currentUserProfile.collectAsState()
    val selectedCloudVoice by viewModel.cloudVoice.collectAsState()
    val isSacredAudioPlaying by viewModel.isSacredAudioPlaying.collectAsState()
    val geminiThinkingDepth by viewModel.geminiThinkingDepth.collectAsState()
    val aiChatThreads by viewModel.aiChatThreads.collectAsState()
    val activeAiChatId by viewModel.activeAiChatId.collectAsState()
    val learnedAiFacts by viewModel.learnedAiFacts.collectAsState()
    val lastAutoLearnedFactTitle by viewModel.lastAutoLearnedFactTitle.collectAsState()
    val userPersonalAiProfile by viewModel.userPersonalAiProfile.collectAsState()

    var autoSpeakAiResponse by remember { mutableStateOf(true) }
    var lastSpokenResponseId by remember { mutableStateOf<String?>(null) }

    // Estados del menú [+] en la primera esquina y diálogos de herramientas
    var showPlusToolsSheet by remember { mutableStateOf(false) }
    var showUploadTextDialog by remember { mutableStateOf(false) }
    var showTeachDialog by remember { mutableStateOf(false) }
    var showChatThreadsDialog by remember { mutableStateOf(false) }
    var showPersonalStyleDialog by remember { mutableStateOf(false) }
    var customUserStyleInput by remember(userPersonalAiProfile.customWordsOrToneInstruction) {
        mutableStateOf(userPersonalAiProfile.customWordsOrToneInstruction)
    }
    var newPersonalMemoryNoteInput by remember { mutableStateOf("") }
    var showVoicePickerDialog by remember { mutableStateOf(false) }
    var showElevenLabsDialog by remember { mutableStateOf(false) }
    var showCreateMusicDialog by remember { mutableStateOf(false) }
    var showCreateImageDialog by remember { mutableStateOf(false) }
    var showFocusedCreativityDialog by remember { mutableStateOf(false) }
    var showMagisteriumAiDialog by remember { mutableStateOf(false) }
    var showAccountDialog by remember { mutableStateOf(false) }

    // Estado para conexión combinada Magisterio de la Iglesia (Magisterium AI) + Gemini
    var customMagisteriumKey by remember {
        val savedKey = prefs.getString("magisterium_api_key", "") ?: ""
        BasilianAiService.customMagisteriumApiKey = savedKey
        mutableStateOf(savedKey)
    }

    // Estado para Subir texto
    var uploadedTextTitle by remember { mutableStateOf("") }
    var uploadedTextContent by remember { mutableStateOf("") }
    var alsoLearnFromUploadedText by remember { mutableStateOf(true) }

    // Estado para Enseñar a la IA cosas que no sabía
    var teachFactTitle by remember { mutableStateOf("") }
    var teachFactDetails by remember { mutableStateOf("") }
    var learnedFactsCount by remember(learnedAiFacts) {
        mutableIntStateOf(learnedAiFacts.size)
    }

    // Estado para Crear música
    var musicPromptInput by remember { mutableStateOf("") }
    var selectedMusicStyle by remember { mutableStateOf("Himno Inspirador & Acústico") }

    // Estado para Crear una imagen
    var imagePromptInput by remember { mutableStateOf("") }
    var lastCreatedImageSubject by remember { mutableStateOf<String?>(null) }

    // Estado para Creatividad (Educación, Pastoral o Trabajo)
    var selectedCreativityArea by remember { mutableStateOf("Educación") }
    var creativityPromptInput by remember { mutableStateOf("") }

    var customElevenLabsKey by remember {
        mutableStateOf(GoogleCloudTtsService.getEffectiveElevenLabsKey(context))
    }
    var isElevenLabsConfigured by remember {
        mutableStateOf(GoogleCloudTtsService.getEffectiveElevenLabsKey(context).isNotBlank())
    }

    val strings = remember(appLanguage) { AppStrings.get(appLanguage) }

    val userName = remember(currentUserProfile) {
        currentUserProfile?.fullName?.takeIf { it.isNotBlank() } ?: "Hermano"
    }
    val userEmail = remember(currentUserProfile) {
        currentUserProfile?.email?.takeIf { it.isNotBlank() } ?: "comunidad@basilianos.org"
    }

    var userPrompt by remember { mutableStateOf("") }
    var selectedMode by remember { mutableStateOf(CreativeHistoryMode.HISTORIA_COMPLETA) }

    // Historial de turnos del Cuadro de Conversación
    val liveConversationTurns = remember { mutableStateListOf<LiveVoiceConversationTurn>() }

    // =========================================================================
    // MOTOR DE CONVERSACIÓN POR VOZ EN VIVO
    // Al activar el micrófono, la IA dice ÚNICAMENTE:
    // "Hola, [Nombre], qué alegría escucharte. ¿Qué necesitas hoy?"
    // y abre inmediatamente la escucha de voz.
    // =========================================================================
    var isGoogleLiveSessionActive by remember { mutableStateOf(false) }
    var isAiGreetingSpeaking by remember { mutableStateOf(false) }
    var isMicListeningNow by remember { mutableStateOf(false) }
    var keepMicOpenUntilUserStops by remember { mutableStateOf(false) }
    var accumulatedDictation by remember { mutableStateOf("") }
    var livePartialSpeech by remember { mutableStateOf("") }
    val shortFirstName = remember(userName) {
        BasilianAiService.resolvePersonalFirstName(userName)
            ?: userName.trim().split(Regex("\\s+")).firstOrNull()?.takeIf { it.isNotBlank() }
            ?: "Hermano"
    }
    var liveVoiceStatusText by remember(shortFirstName) { mutableStateOf("Hola, $shortFirstName") }
    var liveAudioRms by remember { mutableFloatStateOf(0f) }
    var speechRecognizerRef by remember { mutableStateOf<SpeechRecognizer?>(null) }
    var speechSilenceAutoSendJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }
    var lastSentUserMessage by remember { mutableStateOf("") }

    // Función unificada para enviar cualquier mensaje hablado o escrito a la IA
    fun sendUserMessageToAi(
        rawMessage: String,
        modeOverride: CreativeHistoryMode = selectedMode,
        imageSubjectTag: String? = null
    ) {
        val clean = rawMessage.trim()
        if (clean.isBlank()) return
        speechSilenceAutoSendJob?.cancel()
        speechSilenceAutoSendJob = null
        lastSentUserMessage = clean
        if (imageSubjectTag != null) {
            lastCreatedImageSubject = imageSubjectTag
        }
        autoSpeakAiResponse = true
        liveVoiceStatusText = "Procesando consulta..."
        viewModel.generateCreativeHistory(clean, modeOverride)
    }

    // Saludo breve e inmediato al ingresar a Historia IA: únicamente "Hola, [Nombre]"
    LaunchedEffect(shortFirstName) {
        val sessionGreeting = "Hola, $shortFirstName"
        liveVoiceStatusText = sessionGreeting
        val alreadyGreetedSession = prefs.getBoolean("historia_ia_greeted_session_v3", false)
        if (!alreadyGreetedSession) {
            prefs.edit().putBoolean("historia_ia_greeted_session_v3", true).apply()
            delay(300)
            viewModel.speakTextImmediate(sessionGreeting)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            keepMicOpenUntilUserStops = false
            speechSilenceAutoSendJob?.cancel()
            try {
                speechRecognizerRef?.stopListening()
                speechRecognizerRef?.destroy()
                speechRecognizerRef = null
            } catch (_: Exception) {}
        }
    }

    val langTag = remember(appLanguage) {
        when (appLanguage) {
            AppLanguage.EN -> "en-US"
            AppLanguage.FR -> "fr-FR"
            AppLanguage.ES -> "es-ES"
        }
    }

    // Respaldo nativo del sistema Android para reconocimiento de voz (por si SpeechRecognizer en línea falla)
    val systemSpeechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        val spoken = matches?.firstOrNull()?.trim().orEmpty()
        if (spoken.isNotBlank()) {
            accumulatedDictation = if (accumulatedDictation.isBlank()) spoken else "$accumulatedDictation $spoken"
            userPrompt = accumulatedDictation
            livePartialSpeech = ""
        }
        isMicListeningNow = false
        keepMicOpenUntilUserStops = false
    }

    fun launchSystemSpeechDialogFallback() {
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, langTag)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora y presiona el micrófono cuando termines")
            }
            systemSpeechLauncher.launch(intent)
        } catch (_: Exception) {
            isMicListeningNow = false
            keepMicOpenUntilUserStops = false
            liveVoiceStatusText = "🎙️ Escribe en el cuadro de abajo o toca el micrófono para hablar"
        }
    }

    fun stopInlineMicrophone() {
        keepMicOpenUntilUserStops = false
        speechSilenceAutoSendJob?.cancel()
        speechSilenceAutoSendJob = null
        isMicListeningNow = false
        liveAudioRms = 0f
        try {
            speechRecognizerRef?.stopListening()
            speechRecognizerRef?.destroy()
            speechRecognizerRef = null
        } catch (_: Exception) {}
    }

    fun startInlineMicrophoneListening(resetAccumulated: Boolean = false) {
        speechSilenceAutoSendJob?.cancel()
        speechSilenceAutoSendJob = null
        try {
            speechRecognizerRef?.stopListening()
            speechRecognizerRef?.destroy()
            speechRecognizerRef = null
        } catch (_: Exception) {}

        viewModel.stopSpeaking()
        isAiGreetingSpeaking = false
        keepMicOpenUntilUserStops = true
        isMicListeningNow = true
        if (resetAccumulated) {
            accumulatedDictation = userPrompt.trim()
        }
        livePartialSpeech = ""
        liveVoiceStatusText = "🎙️ Micrófono activo: habla con calma. Oprime el botón del micrófono cuando termines."

        val isRecAvailable = try {
            SpeechRecognizer.isRecognitionAvailable(context)
        } catch (_: Exception) {
            false
        }

        if (isRecAvailable) {
            try {
                val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
                speechRecognizerRef = recognizer
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, langTag)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, langTag)
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                    // Mantener ventana amplia de silencio y reanudar automáticamente hasta que el usuario oprima el botón
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 15000L)
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000L)
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 30000L)
                }

                fun restartRecognizerIfStillHoldingMic() {
                    if (!keepMicOpenUntilUserStops) return
                    coroutineScope.launch {
                        delay(120)
                        if (keepMicOpenUntilUserStops) {
                            startInlineMicrophoneListening(resetAccumulated = false)
                        }
                    }
                }

                recognizer.setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        if (keepMicOpenUntilUserStops) {
                            isMicListeningNow = true
                            liveVoiceStatusText = "🎙️ Escuchándote... (Oprime el botón del micrófono cuando ya no quieras decir nada más)"
                        }
                    }

                    override fun onBeginningOfSpeech() {
                        if (keepMicOpenUntilUserStops) {
                            isMicListeningNow = true
                            liveVoiceStatusText = "🗣️ Escuchando tu voz... (No se cortará al pausar; oprime el micrófono al terminar)"
                        }
                    }

                    override fun onRmsChanged(rmsdB: Float) {
                        liveAudioRms = (rmsdB.coerceIn(0f, 12f) / 12f)
                    }

                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        // NO cerrar el micrófono ni auto-enviar cuando el usuario hace una pausa al hablar.
                        // El micrófono permanece activo esperando el siguiente segmento o que el usuario oprima el botón.
                        if (keepMicOpenUntilUserStops) {
                            isMicListeningNow = true
                        }
                    }

                    override fun onError(error: Int) {
                        if (!keepMicOpenUntilUserStops) {
                            isMicListeningNow = false
                            return
                        }
                        // Si había texto parcial antes de la pausa, acumularlo sin cerrar el micrófono
                        val partial = livePartialSpeech.trim()
                        if (partial.isNotBlank()) {
                            accumulatedDictation = if (accumulatedDictation.isBlank()) partial else "$accumulatedDictation $partial"
                            userPrompt = accumulatedDictation
                            livePartialSpeech = ""
                        }

                        if (error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS) {
                            stopInlineMicrophone()
                            launchSystemSpeechDialogFallback()
                        } else {
                            // Ante silencios o pausas del hablante (ERROR_SPEECH_TIMEOUT, ERROR_NO_MATCH, etc.),
                            // reiniciar inmediatamente la escucha para que el micrófono nunca se vaya solo.
                            restartRecognizerIfStillHoldingMic()
                        }
                    }

                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val spoken = matches?.firstOrNull()?.trim().orEmpty().ifBlank { livePartialSpeech.trim() }
                        if (spoken.isNotBlank()) {
                            accumulatedDictation = if (accumulatedDictation.isBlank()) spoken else "$accumulatedDictation $spoken"
                            userPrompt = accumulatedDictation
                            livePartialSpeech = ""
                        }
                        if (keepMicOpenUntilUserStops) {
                            // Seguir escuchando continuamente hasta que el usuario oprima el botón del micrófono
                            restartRecognizerIfStillHoldingMic()
                        } else {
                            isMicListeningNow = false
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val partial = matches?.firstOrNull()?.trim().orEmpty()
                        if (partial.isNotBlank() && keepMicOpenUntilUserStops) {
                            livePartialSpeech = partial
                            userPrompt = if (accumulatedDictation.isBlank()) partial else "$accumulatedDictation $partial"
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })

                recognizer.startListening(intent)
            } catch (_: Exception) {
                launchSystemSpeechDialogFallback()
            }
        } else {
            launchSystemSpeechDialogFallback()
        }
    }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startInlineMicrophoneListening(resetAccumulated = true)
        } else {
            launchSystemSpeechDialogFallback()
        }
    }

    fun requestAndStartInlineMic() {
        val hasPerm = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        if (hasPerm) {
            startInlineMicrophoneListening(resetAccumulated = true)
        } else {
            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    // Control manual del micrófono: el micrófono NO se apaga cuando el usuario hace pausas al hablar;
    // únicamente se detiene (y envía lo dicho) cuando el propio usuario vuelve a oprimir el botón del micrófono.
    fun triggerGoogleStyleVoiceGreetingAndListen() {
        if (isMicListeningNow || keepMicOpenUntilUserStops) {
            val partial = livePartialSpeech.trim()
            val combined = when {
                accumulatedDictation.isNotBlank() && partial.isNotBlank() && !accumulatedDictation.endsWith(partial) ->
                    "$accumulatedDictation $partial"
                accumulatedDictation.isNotBlank() -> accumulatedDictation
                partial.isNotBlank() -> partial
                else -> userPrompt.trim()
            }.trim()

            stopInlineMicrophone()
            accumulatedDictation = ""
            livePartialSpeech = ""
            if (combined.isNotBlank()) {
                userPrompt = ""
                sendUserMessageToAi(combined, selectedMode)
            } else {
                isGoogleLiveSessionActive = false
                liveVoiceStatusText = "🎙️ Micrófono detenido."
            }
            return
        }

        if (isSpeaking) {
            viewModel.stopSpeaking()
        }

        isGoogleLiveSessionActive = true
        autoSpeakAiResponse = true
        requestAndStartInlineMic()
    }

    // Selector de archivos de texto (.txt)
    val textFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val content = reader.readText()
                    if (content.isNotBlank()) {
                        uploadedTextContent = content.take(12000)
                        if (uploadedTextTitle.isBlank()) {
                            uploadedTextTitle = "Texto subido"
                        }
                        showUploadTextDialog = true
                        Toast.makeText(
                            context,
                            "📄 Texto cargado (${content.length} caracteres)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (_: Exception) {
                showUploadTextDialog = true
            }
        }
    }

    // Sincroniza automáticamente cada nueva respuesta de la IA con el cuadro de conversación y la voz de ElevenLabs
    LaunchedEffect(activeResult?.id, autoSpeakAiResponse) {
        val currentItem = activeResult
        if (currentItem != null && currentItem.response.isNotBlank()) {
            val rawP = currentItem.prompt
            val isImg = rawP.startsWith("[CREAR_IMAGEN]") || currentItem.mode == CreativeHistoryMode.CREAR_IMAGENES
            val isMus = rawP.startsWith("[CREAR_MUSICA]") || currentItem.mode == CreativeHistoryMode.CREAR_MUSICA
            val displayPrompt = rawP
                .removePrefix("[RESUMIR_TEXTO]")
                .removePrefix("[SUBIR_TEXTO]")
                .removePrefix("[CREAR_MUSICA]")
                .removePrefix("[CREAR_IMAGEN]")
                .trim()
                .let { if (it.length > 140) it.take(140) + "..." else it }

            if (liveConversationTurns.none { it.id == currentItem.id }) {
                val nowCal = Calendar.getInstance()
                val timeStr = "%02d:%02d".format(
                    nowCal.get(Calendar.HOUR_OF_DAY),
                    nowCal.get(Calendar.MINUTE)
                )
                val imgSubj = if (isImg) (lastCreatedImageSubject ?: displayPrompt) else null
                liveConversationTurns.add(
                    0,
                    LiveVoiceConversationTurn(
                        id = currentItem.id,
                        userText = displayPrompt,
                        aiResponse = currentItem.response,
                        timestampLabel = timeStr,
                        generatedImageSubject = imgSubj,
                        isMusicCreation = isMus
                    )
                )
            }
            if (autoSpeakAiResponse && currentItem.id != lastSpokenResponseId) {
                lastSpokenResponseId = currentItem.id
                liveVoiceStatusText = "🔊 Respondiéndote con voz..."
                viewModel.speakTextImmediate(currentItem.response)
            }
        }
    }

    val promptSuggestions = strings.historiaIa.suggestionList

    if (showAccountDialog) {
        UserLinkedAccountDialog(
            viewModel = viewModel,
            onDismiss = { showAccountDialog = false }
        )
    }

    // =========================================================================
    // MENÚ DE OPCIONES Y HERRAMIENTAS [+] (MODOS, CHATS, APRENDIZAJE, VOZ Y TEXTO)
    // =========================================================================
    if (showPlusToolsSheet) {
        AlertDialog(
            onDismissRequest = { showPlusToolsSheet = false },
            containerColor = Color(0xFF06152D),
            titleContentColor = HoloCyan,
            textContentColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = null, tint = HoloGold)
                    Column {
                        Text(
                            text = "Opciones y Herramientas (+)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Modos de respuesta, historial de chats, aprendizaje y voz",
                            fontSize = 11.sp,
                            color = HoloCyan
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 0. Botón directo para Iniciar Nuevo Chat
                    Button(
                        onClick = {
                            if (isMicListeningNow || keepMicOpenUntilUserStops) {
                                stopInlineMicrophone()
                            }
                            accumulatedDictation = ""
                            livePartialSpeech = ""
                            userPrompt = ""
                            liveConversationTurns.clear()
                            viewModel.startNewAiChatThread()
                            showPlusToolsSheet = false
                            Toast.makeText(context, "✨ Nuevo chat iniciado", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HoloCyan,
                            contentColor = Color(0xFF020617)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tool_start_new_chat")
                    ) {
                        Icon(Icons.Default.AddComment, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("✨ Iniciar Nuevo Chat", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    }

                    // 1. Selector de Modos de Respuesta (⚡ Modo Flash | ✨ Modo Estándar | 🧠 Modo Profundo)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF040D1E),
                        border = BorderStroke(1.2.dp, HoloCyan.copy(alpha = 0.55f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("gemini_thinking_depth_selector_bar")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "🎛️ Modo de Respuesta de la Guía IA",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = HoloGoldLight
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                GeminiThinkingDepth.values().forEach { depthOption ->
                                    val isDepthSelected = geminiThinkingDepth == depthOption
                                    Surface(
                                        onClick = { viewModel.setGeminiThinkingDepth(depthOption) },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isDepthSelected) {
                                            when (depthOption) {
                                                GeminiThinkingDepth.FLASH_RAPIDO -> HoloGold.copy(alpha = 0.28f)
                                                GeminiThinkingDepth.EQUILIBRADO -> HoloCyan.copy(alpha = 0.28f)
                                                GeminiThinkingDepth.PENSAMIENTO_PROFUNDO -> HoloEmerald.copy(alpha = 0.28f)
                                            }
                                        } else {
                                            Color(0xFF0A1D3B).copy(alpha = 0.75f)
                                        },
                                        border = BorderStroke(
                                            width = if (isDepthSelected) 1.5.dp else 1.dp,
                                            color = if (isDepthSelected) {
                                                when (depthOption) {
                                                    GeminiThinkingDepth.FLASH_RAPIDO -> HoloGold
                                                    GeminiThinkingDepth.EQUILIBRADO -> HoloCyan
                                                    GeminiThinkingDepth.PENSAMIENTO_PROFUNDO -> HoloEmerald
                                                }
                                            } else {
                                                Color.White.copy(alpha = 0.18f)
                                            }
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("gemini_depth_${depthOption.name.lowercase()}")
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = depthOption.label,
                                                fontSize = 10.sp,
                                                fontWeight = if (isDepthSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                                color = if (isDepthSelected) Color.White else Color(0xFFBAE6FD),
                                                textAlign = TextAlign.Center,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                            Text(
                                text = geminiThinkingDepth.subtitle,
                                fontSize = 10.5.sp,
                                color = Color(0xFFBAE6FD)
                            )
                        }
                    }

                    // 2. Historial de Chats Guardados (Room Database)
                    Surface(
                        onClick = {
                            showPlusToolsSheet = false
                            showChatThreadsDialog = true
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = HoloGold.copy(alpha = 0.14f),
                        border = BorderStroke(1.2.dp, HoloGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tool_open_saved_chats")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Forum,
                                contentDescription = "Mis Chats",
                                tint = HoloGoldLight,
                                modifier = Modifier.size(26.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "💬 Mis Conversaciones (${aiChatThreads.size} chats)",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp,
                                    color = HoloGoldLight
                                )
                                Text(
                                    text = "Abre, retoma o administra tus sesiones anteriores guardadas.",
                                    fontSize = 11.sp,
                                    color = Color(0xFFFDE68A),
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }

                    // 3. Autoaprendizaje y Enseñar a la IA
                    Surface(
                        onClick = {
                            showPlusToolsSheet = false
                            showTeachDialog = true
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = HoloEmerald.copy(alpha = 0.14f),
                        border = BorderStroke(1.2.dp, HoloEmerald),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tool_teach_ai_new_knowledge")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = Color(0xFF6EE7B7),
                                modifier = Modifier.size(26.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "🧠 Autoaprendizaje y Enseñar a la IA ($learnedFactsCount)",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF6EE7B7)
                                )
                                Text(
                                    text = "Enséñale datos nuevos o revisa lo que ha aprendido en tus conversaciones.",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }

                    // 4. Estilo de Voz (Santiago / Voces Humanas — Único lugar donde aparece)
                    Surface(
                        onClick = {
                            showPlusToolsSheet = false
                            showVoicePickerDialog = true
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = HoloCyan.copy(alpha = 0.14f),
                        border = BorderStroke(1.2.dp, HoloCyan),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tool_select_elevenlabs_voice")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = HoloCyan,
                                modifier = Modifier.size(26.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "🎙️ Voz de lectura (${selectedCloudVoice.displayName})",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "Cambia entre las 3 voces femeninas y 2 masculinas disponibles.",
                                    fontSize = 11.sp,
                                    color = Color(0xFFBAE6FD),
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }

                    // 5. Subir texto o documento
                    Surface(
                        onClick = {
                            showPlusToolsSheet = false
                            showUploadTextDialog = true
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, HoloCyan.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tool_upload_text_to_summarize")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.UploadFile,
                                contentDescription = null,
                                tint = HoloCyan,
                                modifier = Modifier.size(24.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "📄 Subir texto o documento",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "Pega un texto o sube un archivo para resumirlo o trabajar sobre él.",
                                    fontSize = 11.sp,
                                    color = Color(0xFFBAE6FD),
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }

                    // 6. Puente Combinado: Magisterio de la Iglesia, Patrística & Apologética + Gemini
                    Surface(
                        onClick = {
                            showPlusToolsSheet = false
                            showMagisteriumAiDialog = true
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = HoloGold.copy(alpha = 0.14f),
                        border = BorderStroke(1.2.dp, HoloGold.copy(alpha = 0.7f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tool_magisterium_apologetics_ai")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = HoloGoldLight,
                                modifier = Modifier.size(24.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "⛪ Magisterio, Patrística & Apologética + Gemini",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.5.sp,
                                    color = HoloGoldLight
                                )
                                Text(
                                    text = if (customMagisteriumKey.isNotBlank())
                                        "Conectado: Magisterium AI + Corpus Patrístico de San Basilio + Gemini activos."
                                    else
                                        "Corpus Patrístico y Magisterial integrado con Gemini (opción de API Magisterium AI).",
                                    fontSize = 11.sp,
                                    color = Color(0xFFFDE68A),
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                    // 7. Nuevo Chat y Conversaciones Guardadas (accesibles desde [+])
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                showPlusToolsSheet = false
                                if (isMicListeningNow || keepMicOpenUntilUserStops) {
                                    stopInlineMicrophone()
                                }
                                accumulatedDictation = ""
                                livePartialSpeech = ""
                                userPrompt = ""
                                liveConversationTurns.clear()
                                viewModel.startNewAiChatThread()
                                Toast.makeText(context, "✨ Nuevo chat iniciado", Toast.LENGTH_SHORT).show()
                            },
                            border = BorderStroke(1.dp, HoloCyan),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = HoloCyan),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.AddComment, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Nuevo Chat", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                showPlusToolsSheet = false
                                showChatThreadsDialog = true
                            },
                            border = BorderStroke(1.dp, HoloGold),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = HoloGoldLight),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Forum, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Historial (${aiChatThreads.size})", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPlusToolsSheet = false }) {
                    Text("Cerrar", color = HoloGoldLight, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // =========================================================================
    // DIÁLOGO: CONEXIÓN COMBINADA MAGISTERIO DE LA IGLESIA / APOLOGÉTICA + GEMINI
    // =========================================================================
    if (showMagisteriumAiDialog) {
        AlertDialog(
            onDismissRequest = { showMagisteriumAiDialog = false },
            containerColor = Color(0xFF06152D),
            titleContentColor = HoloGoldLight,
            textContentColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = HoloGold)
                    Text(
                        text = "Magisterio, Patrística & Apologética + Gemini",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Basilio IA ya tiene integrado el Corpus Patrístico de San Basilio Magno, los Padres de la Iglesia (Capadocios, Orientales y Occidentales), los Concilios Ecuménicos y el Catecismo de la Iglesia Católica trabajando en conjunto con Gemini.",
                        fontSize = 12.sp,
                        color = Color(0xFFBAE6FD),
                        lineHeight = 17.sp
                    )
                    Text(
                        text = "Si además cuentas con una llave de Magisterium AI (magisterium.com/api), puedes ingresarla aquí para que ambas IAs consulten en paralelo documentos pontificios y apologéticos y los sinteticen desde la espiritualidad de San Basilio y de la C.S.B.:",
                        fontSize = 11.5.sp,
                        color = Color(0xFFFDE68A),
                        lineHeight = 16.sp
                    )
                    OutlinedTextField(
                        value = customMagisteriumKey,
                        onValueChange = { customMagisteriumKey = it },
                        label = { Text("API Key de Magisterium AI (opcional)", color = HoloGold) },
                        placeholder = { Text("sk_magisterium_...", color = Color.Gray) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = HoloGold,
                            unfocusedBorderColor = HoloGold.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val trimmed = customMagisteriumKey.trim()
                        prefs.edit().putString("magisterium_api_key", trimmed).apply()
                        BasilianAiService.customMagisteriumApiKey = trimmed
                        showMagisteriumAiDialog = false
                        Toast.makeText(
                            context,
                            if (trimmed.isNotBlank()) "⛪ Magisterium AI + Gemini vinculados en conjunto"
                            else "⛪ Corpus Patrístico y Magisterial con Gemini activo",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HoloGold, contentColor = Color(0xFF020617))
                ) {
                    Text("Guardar Configuración", fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showMagisteriumAiDialog = false }) {
                    Text("Cerrar", color = HoloGoldLight)
                }
            }
        )
    }

    // =========================================================================
    // DIÁLOGO 1: SUBIR TEXTO
    // =========================================================================
    if (showUploadTextDialog) {
        AlertDialog(
            onDismissRequest = { showUploadTextDialog = false },
            containerColor = Color(0xFF06152D),
            titleContentColor = HoloCyan,
            textContentColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.UploadFile, contentDescription = null, tint = HoloCyan)
                    Text(
                        text = "Subir texto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { textFilePickerLauncher.launch("text/*") },
                        border = BorderStroke(1.2.dp, HoloGold),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = HoloGoldLight),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Seleccionar archivo (.txt)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    OutlinedTextField(
                        value = uploadedTextTitle,
                        onValueChange = { uploadedTextTitle = it },
                        label = { Text("Título del texto (opcional)", color = HoloGold) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = HoloGold,
                            unfocusedBorderColor = HoloGold.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = uploadedTextContent,
                        onValueChange = { uploadedTextContent = it },
                        label = { Text("Pega o escribe aquí el texto *", color = HoloCyan) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = HoloCyan,
                            unfocusedBorderColor = HoloCyan.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 5,
                        maxLines = 9
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { alsoLearnFromUploadedText = !alsoLearnFromUploadedText }
                    ) {
                        Checkbox(
                            checked = alsoLearnFromUploadedText,
                            onCheckedChange = { alsoLearnFromUploadedText = it },
                            colors = CheckboxDefaults.colors(checkedColor = HoloCyan)
                        )
                        Text(
                            text = "Guardar también en la memoria de la IA",
                            fontSize = 11.5.sp,
                            color = Color.White
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cleanBody = uploadedTextContent.trim()
                        if (cleanBody.isNotBlank()) {
                            val titleLabel = uploadedTextTitle.trim().ifBlank { "Texto de $userName" }
                            if (alsoLearnFromUploadedText) {
                                BasilianAiMemory.addLearnedFact(
                                    context = context,
                                    title = titleLabel,
                                    details = cleanBody.take(900),
                                    category = "LECTURA",
                                    verifiedWithGoogle = true
                                )
                                learnedFactsCount = BasilianAiMemory.getAllLearnedFacts(context).size
                            }
                            val summaryPrompt = "[SUBIR_TEXTO] Título: $titleLabel.\nTexto: $cleanBody"
                            sendUserMessageToAi(summaryPrompt, CreativeHistoryMode.HISTORIA_COMPLETA)
                            showUploadTextDialog = false
                            uploadedTextTitle = ""
                            uploadedTextContent = ""
                        }
                    },
                    enabled = uploadedTextContent.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = HoloCyan, contentColor = Color(0xFF020617))
                ) {
                    Text("Subir y Procesar", fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUploadTextDialog = false }) {
                    Text("Cancelar", color = HoloGoldLight)
                }
            }
        )
    }

    // =========================================================================
    // DIÁLOGO 2: ENSEÑAR A LA IA COSAS QUE NO SABÍA
    // =========================================================================
    if (showTeachDialog) {
        val currentLearnedFacts = remember(learnedFactsCount) {
            BasilianAiMemory.getAllLearnedFacts(context)
        }
        AlertDialog(
            onDismissRequest = { showTeachDialog = false },
            containerColor = Color(0xFF06152D),
            titleContentColor = HoloCyan,
            textContentColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = HoloGold)
                    Text(
                        text = "Enseñar a la IA cosas que no sabía",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Lo que le enseñes aquí quedará guardado en su memoria para que aprenda, crezca y responda con mayor sabiduría.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFBAE6FD),
                        lineHeight = 16.sp
                    )
                    OutlinedTextField(
                        value = teachFactTitle,
                        onValueChange = { teachFactTitle = it },
                        label = { Text("Tema o título", color = HoloCyan) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = HoloCyan,
                            unfocusedBorderColor = HoloCyan.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = teachFactDetails,
                        onValueChange = { teachFactDetails = it },
                        label = { Text("¿Qué quieres enseñarle a la IA? *", color = HoloGold) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = HoloGold,
                            unfocusedBorderColor = HoloGold.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 6
                    )

                    if (currentLearnedFacts.isNotEmpty()) {
                        Text(
                            text = "📚 Conocimientos aprendidos (${currentLearnedFacts.size}):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = HoloGoldLight
                        )
                        currentLearnedFacts.take(3).forEach { fact ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White.copy(alpha = 0.06f),
                                border = BorderStroke(1.dp, HoloCyan.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "• ${fact.title}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = HoloCyan
                                    )
                                    Text(
                                        text = fact.details,
                                        fontSize = 10.5.sp,
                                        color = Color.White.copy(alpha = 0.85f),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (teachFactDetails.isNotBlank()) {
                            val titleToSave = teachFactTitle.trim().ifBlank { "Enseñanza de $userName" }
                            val detailsToSave = teachFactDetails.trim()
                            BasilianAiMemory.addLearnedFact(
                                context = context,
                               title = titleToSave,
                                details = detailsToSave,
                                category = "COMUNIDAD",
                                verifiedWithGoogle = true
                            )
                            learnedFactsCount = BasilianAiMemory.getAllLearnedFacts(context).size

                            val confirmationMsg = "Gracias por enseñarme esto, $userName. Ya he guardado en mi memoria: «$titleToSave — $detailsToSave»."
                            val nowCal = Calendar.getInstance()
                            val timeStr = "%02d:%02d".format(
                                nowCal.get(Calendar.HOUR_OF_DAY),
                                nowCal.get(Calendar.MINUTE)
                            )
                            liveConversationTurns.add(
                                0,
                                LiveVoiceConversationTurn(
                                    id = "learned_${System.currentTimeMillis()}",
                                    userText = "🧠 Te enseño: $titleToSave",
                                    aiResponse = confirmationMsg,
                                    timestampLabel = timeStr
                                )
                            )
                            viewModel.speakTextImmediate(confirmationMsg)
                            showTeachDialog = false
                            teachFactTitle = ""
                            teachFactDetails = ""
                        }
                    },
                    enabled = teachFactDetails.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = HoloCyan, contentColor = Color.Black)
                ) {
                    Text("Guardar y Aprender", fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTeachDialog = false }) {
                    Text(strings.common.close, color = HoloGoldLight)
                }
            }
        )
    }

    // =========================================================================
    // DIÁLOGO 3: ESTILO DE VOCES HUMANAS (5 VOCES: 3 MUJERES Y 2 HOMBRES • LATAM ES/EN/FR)
    // =========================================================================
    if (showVoicePickerDialog) {
        val allVoices = remember { GoogleCloudTtsService.CloudVoiceOption.values().toList() }
        AlertDialog(
            onDismissRequest = { showVoicePickerDialog = false },
            containerColor = Color(0xFF06152D),
            titleContentColor = HoloCyan,
            textContentColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = HoloGold)
                    Column {
                        Text(
                            text = "Estilo de Voces Humanas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "5 voces humanas (3 mujeres y 2 hombres) • Latinoamericanas, profundas y tranquilas (ES / EN / FR)",
                            fontSize = 11.sp,
                            color = HoloCyan
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = HoloCyan.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, HoloGold.copy(alpha = 0.45f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "✨ Estilo de voz agregado a la app: Voces humanas naturales, profundas y tranquilas con acento latinoamericano cálido (sin tono robótico), capaces de hablar Español, Inglés y Francés.",
                            fontSize = 11.sp,
                            color = Color(0xFFFDE68A),
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    allVoices.forEach { voiceOpt ->
                        val isSelected = selectedCloudVoice == voiceOpt
                        Surface(
                            onClick = {
                                viewModel.selectCloudVoice(voiceOpt)
                                viewModel.speakTextImmediate(
                                    "Hola $userName, te hablo con una voz humana, profunda y tranquila. Estoy contigo para ayudarte en español, inglés o francés."
                                )
                            },
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) HoloGold.copy(alpha = 0.22f) else Color(0xFF0A1D3B),
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) HoloGold else HoloCyan.copy(alpha = 0.35f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "🎙️ ${voiceOpt.displayName}",
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSelected) HoloGoldLight else Color.White
                                    )
                                    Text(
                                        text = voiceOpt.styleDescription,
                                        fontSize = 10.5.sp,
                                        color = Color(0xFFBAE6FD)
                                    )
                                }
                                Icon(
                                    imageVector = if (isSelected) Icons.Default.VolumeUp else Icons.Default.PlayCircleOutline,
                                    contentDescription = "Probar voz",
                                    tint = if (isSelected) HoloGoldLight else HoloCyan,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }

                    // Botones rápidos para probar la voz seleccionada en Español, Inglés y Francés
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.speakTextImmediate(
                                    "Hola $userName, la paz esté contigo. Mi voz es tranquila, profunda y natural."
                                )
                            },
                            border = BorderStroke(1.dp, HoloGold),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = HoloGoldLight),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🌎 Español", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = {
                                viewModel.speakTextImmediate(
                                    "Hello $userName, peace be with you. My voice is calm, deep, and natural."
                                )
                            },
                            border = BorderStroke(1.dp, HoloCyan),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = HoloCyan),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🇺🇸 English", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = {
                                viewModel.speakTextImmediate(
                                    "Bonjour $userName, que la paix soit avec vous. Ma voix est calme, profonde et naturelle."
                                )
                            },
                            border = BorderStroke(1.dp, HoloEmerald),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = HoloEmerald),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🇫🇷 Français", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        onClick = {
                            viewModel.resetVoiceConflictAndUseNaturalVoice()
                            customElevenLabsKey = ""
                            isElevenLabsConfigured = GoogleCloudTtsService.getEffectiveElevenLabsKey(context).isNotBlank()
                            viewModel.speakTextImmediate(
                                "Hola $userName, he solucionado el conflicto de audio y activado la voz natural fluida para ti."
                            )
                            Toast.makeText(
                                context,
                                "✅ Conflicto de voz solucionado. Voz natural activa.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HoloEmerald,
                            contentColor = Color(0xFF020617)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("fix_voice_conflict_button")
                    ) {
                        Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Solucionar Conflicto de Voz y Probar", fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    OutlinedButton(
                        onClick = {
                            showVoicePickerDialog = false
                            showElevenLabsDialog = true
                        },
                        border = BorderStroke(1.dp, HoloCyan),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = HoloCyan),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Configurar Clave API (Oculta / Encriptada)", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showVoicePickerDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = HoloCyan, contentColor = Color(0xFF020617))
                ) {
                    Text("Listo", fontWeight = FontWeight.ExtraBold)
                }
            }
        )
    }

    // =========================================================================
    // DIÁLOGO 4: CREAR MÚSICA (CUALQUIER MÚSICA O CANCIÓN QUE PIDA EL USUARIO)
    // =========================================================================
    if (showCreateMusicDialog) {
        val musicStyles = listOf(
            "Himno Inspirador & Acústico",
            "Canto Litúrgico & Coral",
            "Balada Espiritual Contemporánea",
            "Melodía Instrumental de Paz",
            "Canción Educativa / Juvenil Alegre"
        )
        AlertDialog(
            onDismissRequest = { showCreateMusicDialog = false },
            containerColor = Color(0xFF06152D),
            titleContentColor = HoloMagenta,
            textContentColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.MusicNote, contentDescription = null, tint = HoloMagenta)
                    Text(
                        text = "Crear música",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Escribe sobre qué tema, sentimiento o celebración quieres que la IA componga la letra, los acordes y reproduzca la melodía:",
                        fontSize = 12.sp,
                        color = Color(0xFFF5D0FE)
                    )

                    musicStyles.forEach { style ->
                        val selected = selectedMusicStyle == style
                        Surface(
                            onClick = { selectedMusicStyle = style },
                            shape = RoundedCornerShape(10.dp),
                            color = if (selected) HoloMagenta.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.05f),
                            border = BorderStroke(1.dp, if (selected) HoloMagenta else Color.White.copy(alpha = 0.2f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🎶 $style",
                                fontSize = 11.5.sp,
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Normal,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = musicPromptInput,
                        onValueChange = { musicPromptInput = it },
                        label = { Text("¿Qué música o canción quieres crear? *", color = HoloMagenta) },
                        placeholder = { Text("Ej: Una canción de esperanza para jóvenes / Un himno a San Basilio...", color = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = HoloMagenta,
                            unfocusedBorderColor = HoloMagenta.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val req = musicPromptInput.trim()
                        if (req.isNotBlank()) {
                            // Generar partitura/letra en la conversación y activar también el sintetizador musical
                            viewModel.generateDailySacredSong(
                                gospelPassage = req,
                                gospelPhrase = req,
                                reflectionContent = "Composición musical estilo $selectedMusicStyle sobre $req",
                                style = selectedMusicStyle
                            )
                            sendUserMessageToAi(
                                rawMessage = "[CREAR_MUSICA] Estilo: $selectedMusicStyle. Tema: $req",
                                modeOverride = CreativeHistoryMode.CREAR_MUSICA
                            )
                            showCreateMusicDialog = false
                            musicPromptInput = ""
                        }
                    },
                    enabled = musicPromptInput.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = HoloMagenta, contentColor = Color(0xFF020617))
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Componer Música", fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateMusicDialog = false }) {
                    Text("Cancelar", color = HoloGoldLight)
                }
            }
        )
    }

    // =========================================================================
    // DIÁLOGO 5: CREAR UNA IMAGEN (CON CUIDADO AL SER HUMANO Y LIENZO VISUAL)
    // =========================================================================
    if (showCreateImageDialog) {
        AlertDialog(
            onDismissRequest = { showCreateImageDialog = false },
            containerColor = Color(0xFF06152D),
            titleContentColor = HoloCyan,
            textContentColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Palette, contentDescription = null, tint = HoloCyan)
                    Text(
                        text = "Crear una imagen",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Describe la imagen que deseas crear. La IA generará la obra visual en pantalla cuidando siempre el respeto y bienestar del ser humano:",
                        fontSize = 12.sp,
                        color = Color(0xFFBAE6FD)
                    )
                    OutlinedTextField(
                        value = imagePromptInput,
                        onValueChange = { imagePromptInput = it },
                        label = { Text("Describe tu imagen *", color = HoloCyan) },
                        placeholder = { Text("Ej: San Basilio enseñando bajo un cielo estrellado / Un paisaje de paz...", color = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = HoloCyan,
                            unfocusedBorderColor = HoloCyan.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val req = imagePromptInput.trim()
                        if (req.isNotBlank()) {
                            sendUserMessageToAi(
                                rawMessage = "[CREAR_IMAGEN] $req",
                                modeOverride = CreativeHistoryMode.CREAR_IMAGENES,
                                imageSubjectTag = req
                            )
                            showCreateImageDialog = false
                            imagePromptInput = ""
                        }
                    },
                    enabled = imagePromptInput.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = HoloCyan, contentColor = Color(0xFF020617))
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Crear Imagen", fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateImageDialog = false }) {
                    Text("Cancelar", color = HoloGoldLight)
                }
            }
        )
    }

    // =========================================================================
    // DIÁLOGO 6: CREATIVIDAD (ENFOCADA EN EDUCACIÓN, PASTORAL O TRABAJO)
    // =========================================================================
    if (showFocusedCreativityDialog) {
        val areas = listOf("Educación", "Pastoral", "Trabajo")
        AlertDialog(
            onDismissRequest = { showFocusedCreativityDialog = false },
            containerColor = Color(0xFF06152D),
            titleContentColor = HoloGoldLight,
            textContentColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = HoloGold)
                    Text(
                        text = "Creatividad",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Elige el ámbito en el que deseas que la IA diseñe ideas innovadoras, dinámicas o soluciones creativas:",
                        fontSize = 12.sp,
                        color = Color(0xFFFDE68A)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        areas.forEach { area ->
                            val selected = selectedCreativityArea == area
                            FilterChip(
                                selected = selected,
                                onClick = { selectedCreativityArea = area },
                                label = {
                                    Text(
                                        text = area,
                                        fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Normal,
                                        fontSize = 11.5.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = HoloGold.copy(alpha = 0.28f),
                                    selectedLabelColor = HoloGoldLight,
                                    containerColor = Color.White.copy(alpha = 0.06f),
                                    labelColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = creativityPromptInput,
                        onValueChange = { creativityPromptInput = it },
                        label = { Text("¿Qué idea o proyecto de $selectedCreativityArea necesitas? *", color = HoloGold) },
                        placeholder = {
                            Text(
                                when (selectedCreativityArea) {
                                    "Educación" -> "Ej: Dinámica creativa para enseñar valores a jóvenes de colegio..."
                                    "Pastoral" -> "Ej: Encuentro juvenil o actividad parroquial innovadora..."
                                    else -> "Ej: Estrategia para organizar un equipo de trabajo con armonía..."
                                },
                                color = Color.Gray
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = HoloGold,
                            unfocusedBorderColor = HoloGold.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val req = creativityPromptInput.trim()
                        if (req.isNotBlank()) {
                            sendUserMessageToAi(
                                rawMessage = "[CREATIVIDAD_ENFOCADA: $selectedCreativityArea] $req",
                                modeOverride = CreativeHistoryMode.CREATIVO_LIBRE
                            )
                            showFocusedCreativityDialog = false
                            creativityPromptInput = ""
                        }
                    },
                    enabled = creativityPromptInput.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = HoloGold, contentColor = Color(0xFF020617))
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Generar Creatividad", fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showFocusedCreativityDialog = false }) {
                    Text("Cancelar", color = HoloGoldLight)
                }
            }
        )
    }

    // Diálogo de Configuración de Voz Humana & API Encriptada (Oculta con AES-256-GCM)
    if (showElevenLabsDialog) {
        AlertDialog(
            onDismissRequest = { showElevenLabsDialog = false },
            containerColor = Color(0xFF06152D),
            titleContentColor = HoloCyan,
            textContentColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.GraphicEq, contentDescription = null, tint = HoloGold)
                    Text(
                        text = "Estilo de Voz Humana & API Encriptada",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "🔒 Tu clave API se almacena encriptada con Android Keystore (AES-256-GCM) y jamás se expone en texto plano. Las 5 voces humanas (3 mujeres y 2 hombres) hablan español latinoamericano, inglés y francés con tono profundo y tranquilo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFBAE6FD),
                        lineHeight = 17.sp
                    )
                    if (customElevenLabsKey.isNotBlank()) {
                        Text(
                            text = "Clave protegida activa: ${BasilianSecurityManager.maskApiKey(customElevenLabsKey)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = HoloEmerald
                        )
                    }
                    OutlinedTextField(
                        value = customElevenLabsKey,
                        onValueChange = { customElevenLabsKey = it },
                        label = { Text("API Key Oculta (sk-tts-... / xi-api-key)", color = HoloCyan) },
                        placeholder = { Text("sk-tts-••••••••••••", color = Color.Gray) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = HoloCyan,
                            unfocusedBorderColor = HoloCyan.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedButton(
                        onClick = {
                            viewModel.resetVoiceConflictAndUseNaturalVoice()
                            customElevenLabsKey = ""
                            isElevenLabsConfigured = GoogleCloudTtsService.getEffectiveElevenLabsKey(context).isNotBlank()
                            showElevenLabsDialog = false
                            viewModel.speakTextImmediate(
                                "Hola $userName, el conflicto de la API de voz ha sido resuelto. Ahora me escuchas con total claridad."
                            )
                            Toast.makeText(
                                context,
                                "✅ Conflicto de API resuelto y caché de voz depurada.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        border = BorderStroke(1.dp, HoloEmerald),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = HoloEmerald),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Solucionar Conflicto de Voz / Restaurar", fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        GoogleCloudTtsService.setCustomApiKey(context, customElevenLabsKey.trim())
                        isElevenLabsConfigured = GoogleCloudTtsService.getEffectiveElevenLabsKey(context).isNotBlank()
                        showElevenLabsDialog = false
                        viewModel.speakTextImmediate("Hola $userName, tu configuración de voz está lista.")
                        Toast.makeText(
                            context,
                            "🔒 Clave API encriptada y voz vinculada.",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HoloCyan, contentColor = Color(0xFF020617))
                ) {
                    Text("Guardar Seguro", fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showElevenLabsDialog = false }) {
                    Text("Cerrar", color = HoloGoldLight)
                }
            }
        )
    }

    // =========================================================================
    // DIÁLOGO DE HILOS DE CHAT Y MEMORIA CONVERSACIONAL (ESTILO GEMINI)
    // =========================================================================
    if (showChatThreadsDialog) {
        AlertDialog(
            onDismissRequest = { showChatThreadsDialog = false },
            containerColor = Color(0xFF06152D),
            titleContentColor = HoloCyan,
            textContentColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Forum, contentDescription = null, tint = HoloCyan)
                    Column {
                        Text(
                            text = "Mis Conversaciones & Memoria IA",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Basilio Estándar recuerda cada chat y aprende automáticamente",
                            fontSize = 11.sp,
                            color = HoloGoldLight
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.startNewAiChatThread()
                            liveConversationTurns.clear()
                            showChatThreadsDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HoloCyan,
                            contentColor = Color(0xFF020617)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AddComment, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Iniciar Nuevo Chat (Conservando Memoria)", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = HoloEmerald.copy(alpha = 0.14f),
                        border = BorderStroke(1.dp, HoloEmerald.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = Color(0xFF6EE7B7), modifier = Modifier.size(20.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "🧠 Autoaprendizaje Activo: ${learnedAiFacts.size} conocimientos",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF6EE7B7)
                                )
                                Text(
                                    text = "La IA conecta lo hablado en todos tus chats y aprende de tus correcciones, preguntas sarcásticas e hipótesis.",
                                    fontSize = 10.5.sp,
                                    color = Color.White.copy(alpha = 0.88f),
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }

                    aiChatThreads.forEach { thread ->
                        val isCurrent = thread.id == activeAiChatId
                        Surface(
                            onClick = {
                                viewModel.selectAiChatThread(thread.id)
                                liveConversationTurns.clear()
                                showChatThreadsDialog = false
                            },
                            shape = RoundedCornerShape(14.dp),
                            color = if (isCurrent) HoloCyan.copy(alpha = 0.2f) else Color(0xFF0A1D3B),
                            border = BorderStroke(
                                width = if (isCurrent) 1.5.dp else 1.dp,
                                color = if (isCurrent) HoloCyan else Color.White.copy(alpha = 0.18f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        if (isCurrent) {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = HoloGold.copy(alpha = 0.25f),
                                                border = BorderStroke(1.dp, HoloGold)
                                            ) {
                                                Text(
                                                    text = "ACTIVO",
                                                    fontSize = 8.5.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = HoloGoldLight,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = thread.title,
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = "${thread.turns.size} mensajes guardados en memoria",
                                        fontSize = 10.5.sp,
                                        color = HoloGoldLight
                                    )
                                }
                                if (aiChatThreads.size > 1) {
                                    IconButton(
                                        onClick = { viewModel.deleteAiChatThread(thread.id) },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = "Eliminar chat",
                                            tint = Color(0xFFFCA5A5),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showChatThreadsDialog = false }) {
                    Text("Cerrar", color = HoloGoldLight, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // =========================================================================
    // DIÁLOGO DE ENTRENAMIENTO Y ESTILO PERSONAL POR USUARIO (MEMORIA GEMINI PERSONAL)
    // =========================================================================
    if (showPersonalStyleDialog) {
        AlertDialog(
            onDismissRequest = { showPersonalStyleDialog = false },
            containerColor = Color(0xFF06152D),
            titleContentColor = HoloCyan,
            textContentColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = HoloGold)
                    Column {
                        Text(
                            text = "Entrenar Mi Estilo Personal de IA",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Memoria exclusiva para ${currentUserProfile?.email ?: "tu cuenta"} • Esencia 100% Basiliana",
                            fontSize = 10.5.sp,
                            color = HoloGoldLight
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Aunque Basilio IA es la inteligencia general de los Basilianos, aquí puedes entrenarla para que dialogue contigo a tu manera (académico, pastoralista, sencillo o con ejemplos) sin perder la cercanía fraterna:",
                        fontSize = 11.5.sp,
                        color = Color(0xFFBAE6FD),
                        lineHeight = 15.sp
                    )

                    UserAiDialogueStyle.entries.forEach { styleOption ->
                        val isSelected = userPersonalAiProfile.preferredStyle == styleOption
                        Surface(
                            onClick = {
                                viewModel.setUserPersonalAiStyle(styleOption)
                                Toast.makeText(
                                    context,
                                    "✨ Estilo personal activado: ${styleOption.badgeTitle}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) HoloCyan.copy(alpha = 0.22f) else Color(0xFF0A1D3B),
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) HoloCyan else Color.White.copy(alpha = 0.18f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = styleOption.badgeTitle,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSelected) HoloCyan else Color.White
                                    )
                                    Text(
                                        text = styleOption.subtitle,
                                        fontSize = 10.5.sp,
                                        color = Color.White.copy(alpha = 0.82f),
                                        lineHeight = 13.5.sp
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Seleccionado",
                                        tint = HoloCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = customUserStyleInput,
                        onValueChange = { customUserStyleInput = it },
                        label = { Text("Palabras o modo como quieres que te hable", color = HoloGoldLight, fontSize = 11.sp) },
                        placeholder = {
                            Text(
                                "Ej: Háblame con ejemplos de colegio, usa palabras cercanas, sé más académico o pastoral conmigo...",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = HoloGold,
                            unfocusedBorderColor = HoloGold.copy(alpha = 0.45f)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedTextField(
                            value = newPersonalMemoryNoteInput,
                            onValueChange = { newPersonalMemoryNoteInput = it },
                            label = { Text("Guardar en mi memoria personal", color = HoloCyan, fontSize = 11.sp) },
                            placeholder = { Text("Ej: Soy formador en Bogotá / Me interesa la patrística...", color = Color.Gray, fontSize = 11.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = HoloCyan,
                                unfocusedBorderColor = HoloCyan.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = {
                                if (newPersonalMemoryNoteInput.isNotBlank()) {
                                    viewModel.addUserPersonalAiMemoryNote(newPersonalMemoryNoteInput)
                                    newPersonalMemoryNoteInput = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HoloEmerald, contentColor = Color(0xFF020617)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Añadir", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                        }
                    }

                    if (userPersonalAiProfile.personalMemoryNotes.isNotEmpty()) {
                        Text(
                            text = "🧠 Lo que tu IA personal recuerda de ti (${userPersonalAiProfile.personalMemoryNotes.size}):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = HoloGoldLight
                        )
                        userPersonalAiProfile.personalMemoryNotes.forEach { note ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF0A1D3B),
                                border = BorderStroke(1.dp, HoloGold.copy(alpha = 0.35f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "• $note",
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { viewModel.removeUserPersonalAiMemoryNote(note) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Quitar nota",
                                            tint = Color(0xFFFCA5A5),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setUserCustomAiInstruction(customUserStyleInput)
                        showPersonalStyleDialog = false
                        Toast.makeText(
                            context,
                            "✅ Tu entrenamiento y memoria personal de IA han sido guardados.",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HoloCyan, contentColor = Color(0xFF020617))
                ) {
                    Text("Guardar Mi Estilo", fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPersonalStyleDialog = false }) {
                    Text("Cerrar", color = HoloGoldLight)
                }
            }
        )
    }

    val chronologicalChatItems = remember(historyList) {
        historyList.reversed()
    }
    val itemsToDisplay = remember(chronologicalChatItems, activeResult) {
        if (chronologicalChatItems.isNotEmpty()) {
            chronologicalChatItems
        } else {
            listOfNotNull(activeResult)
        }
    }
    val conversationListState = rememberLazyListState()

    LaunchedEffect(itemsToDisplay.size, activeResult?.id) {
        if (itemsToDisplay.isNotEmpty()) {
            try {
                conversationListState.animateScrollToItem(itemsToDisplay.lastIndex.coerceAtLeast(0))
            } catch (_: Exception) {}
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isCompactPhone = maxWidth < 360.dp || maxHeight < 640.dp
        val horizontalPad = if (isCompactPhone) 8.dp else 12.dp
        val verticalPad = if (isCompactPhone) 4.dp else 6.dp

        // Fondo Holográfico Espacial
        JarvisFuturistaCanvas(
            modifier = Modifier.fillMaxSize(),
            isActivePulse = isLoading || isSpeaking || isMicListeningNow
        )
        Futuristic3DSpatialOverlay(
            isDark = true,
            accentColor = HoloCyan,
            goldColor = HoloGold
        )

        // =========================================================================
        // DISEÑO LIMPIO, ADAPTATIVO A CUALQUIER TELÉFONO Y CON AMPLIO ESPACIO DE LECTURA:
        // Solo dos elementos:
        // 1. La IA y sus respuestas con máximo espacio de lectura y desplazamiento fluido (weight 1f)
        // 2. El cuadro inferior para preguntarle a la IA con todo (+, campo de texto, enviar y micrófono)
        // =========================================================================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPad, vertical = verticalPad),
            verticalArrangement = Arrangement.spacedBy(if (isCompactPhone) 6.dp else 8.dp)
        ) {
            // =====================================================================
            // 1. ÁREA DE LECTURA DE BASILIO IA (AMPLIA, ORGANIZADA Y ADAPTADA A CUALQUIER PANTALLA)
            // =====================================================================
            LazyColumn(
                state = conversationListState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 4.dp, bottom = 14.dp, start = 2.dp, end = 2.dp)
            ) {
                // Bienvenida inmediata de Basilio IA cuando aún no se ha enviado pregunta
                if (itemsToDisplay.isEmpty()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color(0xFF071A36).copy(alpha = 0.95f),
                            border = BorderStroke(1.2.dp, HoloCyan.copy(alpha = 0.55f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("creative_response_card")
                        ) {
                            Column(
                                modifier = Modifier.padding(if (isCompactPhone) 14.dp else 18.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = HoloGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Basilio IA • Padres Basilianos (C.S.B.)",
                                        fontSize = if (isCompactPhone) 15.sp else 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = HoloCyan
                                    )
                                }
                                Text(
                                    text = "**$shortFirstName**, estoy listo para responder tus preguntas sobre la **Congregación de los Padres Basilianos (C.S.B.)**, **San Basilio Magno**, los **5 libros oficiales**, nuestra historia desde Annonay (1822) hasta hoy, el Directorio y la Necrología Basiliana. Escribe tu pregunta en el cuadro de abajo o toca una sugerencia:",
                                    fontSize = if (isCompactPhone) 13.sp else 14.sp,
                                    color = Color(0xFFE2E8F0),
                                    lineHeight = if (isCompactPhone) 18.5.sp else 20.sp
                                )

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(promptSuggestions) { (promptText, mode) ->
                                        Surface(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable {
                                                    selectedMode = mode
                                                    autoSpeakAiResponse = true
                                                    sendUserMessageToAi(promptText, mode)
                                                },
                                            color = HoloGlassSurface,
                                            border = BorderStroke(1.dp, HoloCyan.copy(alpha = 0.4f)),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.AutoAwesome,
                                                    contentDescription = null,
                                                    tint = HoloGold,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Text(
                                                    text = promptText,
                                                    fontSize = 11.5.sp,
                                                    color = Color.White,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                items(
                    items = itemsToDisplay,
                    key = { it.id }
                ) { item ->
                    val isLatestOrActive = (activeResult?.id == item.id) || (item.id == itemsToDisplay.lastOrNull()?.id)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // 1. Mensaje del usuario (alineado a la derecha)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Surface(
                                shape = RoundedCornerShape(18.dp, 4.dp, 18.dp, 18.dp),
                                color = HoloCyan.copy(alpha = 0.18f),
                                border = BorderStroke(1.dp, HoloCyan.copy(alpha = 0.55f)),
                                modifier = Modifier.widthIn(max = 310.dp)
                            ) {
                                Text(
                                    text = item.prompt.removePrefix("[RESUMIR_TEXTO]").trim(),
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White,
                                    lineHeight = 18.5.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)
                                )
                            }
                        }

                        // 2. Único bloque claro de respuesta de la IA (sin sub-cuadros ni etiquetas de meta-análisis)
                        Surface(
                            shape = RoundedCornerShape(6.dp, 20.dp, 20.dp, 20.dp),
                            color = Color(0xFF071A36).copy(alpha = 0.96f),
                            border = BorderStroke(
                                width = if (isLatestOrActive) 1.4.dp else 0.9.dp,
                                color = if (isLatestOrActive) HoloCyan.copy(alpha = 0.7f) else HoloCyan.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(
                                    if (isLatestOrActive) Modifier.testTag("creative_response_card")
                                    else Modifier
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Si este turno creó una imagen ética, renderizar el lienzo artístico
                                if (item.mode == CreativeHistoryMode.CREAR_IMAGENES &&
                                    !item.response.contains("no puedo generar ese contenido")
                                ) {
                                    AiGeneratedArtworkCanvasCard(subject = item.prompt)
                                }

                                // Si este turno creó música, mostrar botón para reproducir/detener
                                if (item.mode == CreativeHistoryMode.CREAR_MUSICA) {
                                    Button(
                                        onClick = {
                                            if (isSacredAudioPlaying) viewModel.stopSacredSong()
                                            else viewModel.playSacredSong()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = HoloMagenta,
                                            contentColor = Color(0xFF020617)
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = if (isSacredAudioPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isSacredAudioPlaying) "Detener melodía musical" else "🎵 Escuchar melodía musical generada",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                // Texto completo de la respuesta en un único bloque fluido
                                FuturisticFormattedTextWithQuotes(
                                    rawText = item.response,
                                    primaryAccent = HoloCyan,
                                    quoteAccent = HoloGold
                                )

                                // Acciones discretas al pie del bloque (Escuchar, Copiar, Reenviar)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (isSpeaking) {
                                                viewModel.stopSpeaking()
                                            } else {
                                                viewModel.speakTextImmediate(item.response)
                                            }
                                        },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isSpeaking) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                            contentDescription = if (isSpeaking) strings.historiaIa.stopReadingTooltip else strings.historiaIa.readAloudTooltip,
                                            tint = HoloGold,
                                            modifier = Modifier.size(17.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("Respuesta Basilio IA", item.response)
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, strings.historiaIa.copiedToast, Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = strings.historiaIa.copyTooltip,
                                            tint = HoloCyan,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            if (!isLoading) {
                                                sendUserMessageToAi(item.prompt, item.mode)
                                            }
                                        },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = "Reenviar",
                                            tint = HoloGoldLight,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Indicador visual limpio cuando la IA está generando la respuesta
                if (isLoading) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF0A1F3D),
                            border = BorderStroke(1.dp, HoloGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = HoloCyan,
                                    strokeWidth = 2.5.dp,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Escribiendo respuesta...",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HoloGoldLight
                                )
                            }
                        }
                    }
                }
            }

            // =====================================================================
            // 3. CUADRITO INFERIOR COMPACTO DE PREGUNTA Y VOZ
            // =====================================================================
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = HoloGlassCard),
                border = BorderStroke(
                    width = 1.8.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(HoloCyan, HoloGold, HoloEmerald, HoloCyan)
                    )
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bottom_ai_conversation_box_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (isMicListeningNow || keepMicOpenUntilUserStops || livePartialSpeech.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = HoloCyan.copy(alpha = 0.14f),
                            border = BorderStroke(1.dp, Color(0xFF4ADE80)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (userPrompt.isNotBlank() || livePartialSpeech.isNotBlank()) {
                                    "🎙️ Escuchando (no se cortará al pausar) • Oprime «Detener» cuando termines: «${userPrompt.ifBlank { livePartialSpeech }}»"
                                } else {
                                    "🎙️ Micrófono activo: habla con calma y oprime el botón del micrófono cuando ya no quieras decir nada más"
                                },
                                fontSize = 11.sp,
                                color = Color(0xFF4ADE80),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    // Barra fija inferior: [+] Herramientas | Campo de Pregunta | Enviar | Micrófono Hablar/Detener
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. PRIMERA ESQUINA: Botón [+] de herramientas
                        FilledIconButton(
                            onClick = { showPlusToolsSheet = true },
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = HoloGold,
                                contentColor = Color(0xFF020617)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("plus_tools_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Herramientas (+)",
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // 2. CENTRO: Campo de texto para escribirle cualquier cosa a la IA
                        OutlinedTextField(
                            value = userPrompt,
                            onValueChange = {
                                userPrompt = it
                                if (keepMicOpenUntilUserStops) {
                                    accumulatedDictation = it
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("creative_prompt_input"),
                            placeholder = {
                                Text(
                                    text = "Pregunta aquí a Basilio IA...",
                                    fontSize = 12.sp,
                                    color = Color(0xFFBAE6FD).copy(alpha = 0.75f)
                                )
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    if (userPrompt.isNotBlank() && !isLoading) {
                                        if (isMicListeningNow || keepMicOpenUntilUserStops) {
                                            stopInlineMicrophone()
                                            accumulatedDictation = ""
                                            livePartialSpeech = ""
                                        }
                                        val textToSend = userPrompt.trim()
                                        userPrompt = ""
                                        sendUserMessageToAi(textToSend, selectedMode)
                                    }
                                }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = HoloCyan,
                                unfocusedBorderColor = HoloCyan.copy(alpha = 0.45f),
                                cursorColor = HoloCyan,
                                focusedContainerColor = Color(0xFF040D1E),
                                unfocusedContainerColor = Color(0xFF040D1E)
                            ),
                            trailingIcon = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (userPrompt.isNotBlank()) {
                                        IconButton(
                                            onClick = {
                                                if (!isLoading && userPrompt.isNotBlank()) {
                                                    if (isMicListeningNow || keepMicOpenUntilUserStops) {
                                                        stopInlineMicrophone()
                                                        accumulatedDictation = ""
                                                        livePartialSpeech = ""
                                                    }
                                                    val promptToSend = userPrompt.trim()
                                                    userPrompt = ""
                                                    sendUserMessageToAi(promptToSend, selectedMode)
                                                }
                                            },
                                            modifier = Modifier
                                                .size(34.dp)
                                                .testTag("generate_creative_history_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.Send,
                                                contentDescription = strings.historiaIa.consultAiBtn,
                                                tint = HoloCyan,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                userPrompt = ""
                                                accumulatedDictation = ""
                                                livePartialSpeech = ""
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = strings.common.close,
                                                tint = Color.Gray,
                                                modifier = Modifier.size(15.dp)
                                            )
                                        }
                                    } else {
                                        IconButton(
                                            onClick = {
                                                if (userPrompt.isNotBlank() && !isLoading) {
                                                    val promptToSend = userPrompt.trim()
                                                    userPrompt = ""
                                                    sendUserMessageToAi(promptToSend, selectedMode)
                                                }
                                            },
                                            enabled = userPrompt.isNotBlank() && !isLoading,
                                            modifier = Modifier
                                                .size(30.dp)
                                                .testTag("generate_creative_history_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.Send,
                                                contentDescription = strings.historiaIa.consultAiBtn,
                                                tint = HoloCyan.copy(alpha = 0.4f),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            maxLines = 3
                        )

                        // 3. ÚLTIMA ESQUINA: Micrófono Manual (No se apaga al pausar; el usuario lo oprime para terminar)
                        val micActive = isMicListeningNow || keepMicOpenUntilUserStops
                        Button(
                            onClick = { triggerGoogleStyleVoiceGreetingAndListen() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (micActive) Color(0xFFEF4444) else HoloCyan,
                                contentColor = if (micActive) Color.White else Color(0xFF020617)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                            modifier = Modifier
                                .height(48.dp)
                                .testTag("start_live_voice_chat_btn")
                        ) {
                            Icon(
                                imageVector = if (micActive) Icons.Default.Stop else Icons.Default.Mic,
                                contentDescription = if (micActive) "Detener micrófono y enviar" else "Hablar con la IA",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (micActive) "Detener" else "Hablar",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    // Aviso breve en la parte inferior
                    Text(
                        text = "La IA puede cometer errores.",
                        fontSize = 10.5.sp,
                        lineHeight = 13.5.sp,
                        color = Color(0xFF94A3B8),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp, start = 4.dp, end = 4.dp)
                            .testTag("ai_bottom_disclaimer_text")
                    )
                }
            }
        }
    }
}

/**
 * Orbe de Ondas Acústicas en Vivo (Estilo Google Live) que reacciona al hablar, pensar o escuchar.
 */
@Composable
private fun LiveAcousticWaveOrb(
    isListening: Boolean,
    isThinking: Boolean,
    isSpeaking: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "live_voice_orb")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (isListening || isSpeaking) 1800 else 5200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "orb_phase"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.82f,
        targetValue = if (isListening || isSpeaking) 1.24f else 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (isListening || isSpeaking) 480 else 1400,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_pulse"
    )

    val primaryOrbColor = when {
        isListening -> Color(0xFF4ADE80)
        isThinking -> HoloGold
        isSpeaking -> HoloCyan
        else -> HoloCyan
    }
    val secondaryOrbColor = when {
        isListening -> HoloCyan
        isThinking -> HoloMagenta
        isSpeaking -> HoloGold
        else -> HoloGold
    }

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val maxR = size.minDimension / 2f

            // Halo acústico exterior
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryOrbColor.copy(alpha = 0.55f * (pulse / 1.2f)),
                        secondaryOrbColor.copy(alpha = 0.22f),
                        Color.Transparent
                    )
                ),
                radius = maxR
            )

            // Anillos concéntricos de onda de voz
            drawCircle(
                color = primaryOrbColor.copy(alpha = 0.85f),
                radius = maxR * 0.78f * (0.9f + 0.08f * sin(Math.toRadians(phase.toDouble())).toFloat()),
                style = Stroke(width = 2.5.dp.toPx())
            )
            drawCircle(
                color = secondaryOrbColor.copy(alpha = 0.70f),
                radius = maxR * 0.62f * (0.9f + 0.08f * cos(Math.toRadians(phase.toDouble())).toFloat()),
                style = Stroke(width = 1.8.dp.toPx())
            )

            // Arcos dinámicos giratorios
            for (i in 0 until 4) {
                drawArc(
                    color = if (i % 2 == 0) primaryOrbColor else secondaryOrbColor,
                    startAngle = phase + i * 90f,
                    sweepAngle = 52f,
                    useCenter = false,
                    style = Stroke(width = 3.2.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Barras de ecualizador central cuando habla o escucha
            if (isListening || isSpeaking) {
                val barCount = 5
                val spacing = 5.dp.toPx()
                val startX = cx - (barCount - 1) * spacing / 2f
                for (b in 0 until barCount) {
                    val wave = kotlin.math.abs(sin(Math.toRadians((phase * 2f + b * 42f).toDouble()))).toFloat()
                    val barHalfH = (4.dp.toPx() + wave * 12.dp.toPx())
                    val bx = startX + b * spacing
                    drawLine(
                        color = Color.White,
                        start = Offset(bx, cy - barHalfH),
                        end = Offset(bx, cy + barHalfH),
                        strokeWidth = 2.6.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            } else {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primaryOrbColor.copy(alpha = 0.85f),
                            Color(0xFF041022)
                        )
                    ),
                    radius = maxR * 0.42f
                )
            }
        }

        if (!isListening && !isSpeaking) {
            Icon(
                imageVector = if (isThinking) Icons.Default.AutoAwesome else Icons.Default.Mic,
                contentDescription = "Iniciar conversación por voz",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Orbe Holográfico Animado reutilizable.
 */
@Composable
fun JarvisAiCoreOrb(
    isBusy: Boolean,
    modifier: Modifier = Modifier
) {
    LiveAcousticWaveOrb(
        isListening = false,
        isThinking = isBusy,
        isSpeaking = false,
        modifier = modifier
    )
}

/**
 * Lienzo artístico generativo para la herramienta «🎨 Crear una imagen» del menú [+]
 */
@Composable
private fun AiGeneratedArtworkCanvasCard(
    subject: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ai_artwork_glow")
    val shimmerPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "art_shimmer"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF040D1E),
        border = BorderStroke(
            width = 1.6.dp,
            brush = Brush.linearGradient(
                listOf(HoloGold, HoloCyan, Color(0xFFF472B6))
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2f
                    val cy = h / 2f

                    // Fondo cósmico sacral
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF1E1B4B),
                                Color(0xFF091936),
                                Color(0xFF020617)
                            ),
                            center = Offset(cx, cy),
                            radius = maxOf(w, h) * 0.75f
                        )
                    )

                    // Rayos solares dorados giratorios
                    for (i in 0 until 12) {
                        val angleRad = Math.toRadians((shimmerPhase + i * 30f).toDouble())
                        val endX = cx + cos(angleRad).toFloat() * w * 0.6f
                        val endY = cy + sin(angleRad).toFloat() * h * 0.6f
                        drawLine(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    HoloGold.copy(alpha = 0.35f),
                                    Color.Transparent
                                ),
                                start = Offset(cx, cy),
                                end = Offset(endX, endY)
                            ),
                            start = Offset(cx, cy),
                            end = Offset(endX, endY),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }

                    // Halo central bizantino / futurista
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                HoloGold.copy(alpha = 0.55f),
                                HoloCyan.copy(alpha = 0.25f),
                                Color.Transparent
                            ),
                            center = Offset(cx, cy - 8.dp.toPx()),
                            radius = h * 0.42f
                        ),
                        radius = h * 0.42f,
                        center = Offset(cx, cy - 8.dp.toPx())
                    )

                    drawCircle(
                        color = HoloGold,
                        radius = h * 0.25f,
                        center = Offset(cx, cy - 8.dp.toPx()),
                        style = Stroke(width = 2.dp.toPx())
                    )
                    drawCircle(
                        color = HoloCyan.copy(alpha = 0.75f),
                        radius = h * 0.31f,
                        center = Offset(cx, cy - 8.dp.toPx()),
                        style = Stroke(
                            width = 1.2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), shimmerPhase)
                        )
                    )

                    // Silueta de horizonte montañoso de Annonay / Capadocia
                    val mountainPath = Path().apply {
                        moveTo(0f, h)
                        lineTo(0f, h * 0.76f)
                        lineTo(w * 0.22f, h * 0.62f)
                        lineTo(w * 0.42f, h * 0.73f)
                        lineTo(w * 0.68f, h * 0.58f)
                        lineTo(w * 0.88f, h * 0.70f)
                        lineTo(w, h * 0.64f)
                        lineTo(w, h)
                        close()
                    }
                    drawPath(
                        path = mountainPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0F294A).copy(alpha = 0.88f),
                                Color(0xFF020617)
                            )
                        )
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF040D1E).copy(alpha = 0.75f),
                        border = BorderStroke(1.5.dp, HoloGold)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = HoloGold,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(26.dp)
                        )
                    }
                    Text(
                        text = subject.take(70),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Surface(
                        color = HoloCyan.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, HoloCyan.copy(alpha = 0.7f)),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "🎨 Lienzo Artístico Generado por IA • C.S.B.",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = HoloCyan,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

