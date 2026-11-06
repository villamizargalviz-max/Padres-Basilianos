package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.BasilianAiMemory
import com.example.data.ai.ChatMessage
import com.example.data.ai.MessageSender
import com.example.data.model.AppLanguage
import com.example.ui.components.BasilianCrestView
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

@Composable
fun GuiaEspiritualScreen(
    viewModel: BasilianViewModel,
    appLanguage: AppLanguage = AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val strings = remember(appLanguage) { AppStrings.get(appLanguage) }
    val gStrings = strings.guiaEspiritual

    val messages by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isAiLoading.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    var showTeachDialog by remember { mutableStateOf(false) }
    var teachFactText by remember { mutableStateOf("") }
    var teachFactTitle by remember { mutableStateOf("") }

    val learnedFacts = remember(messages.size) {
        BasilianAiMemory.getAllLearnedFacts(context)
    }

    // Speech-to-Text Dictation (Micrófono Robusto con Permisos y Fallback)
    val voiceController = com.example.ui.components.rememberVoiceDictation(
        appLanguage = appLanguage,
        onSpeechResult = { spoken ->
            inputText = if (inputText.isBlank()) spoken else "$inputText $spoken"
        }
    )

    // Auto-scroll al llegar nuevo mensaje
    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Diálogo para enseñar activamente nuevo dato a la IA
    if (showTeachDialog) {
        AlertDialog(
            onDismissRequest = { showTeachDialog = false },
            title = {
                Text(
                    text = gStrings.teachAiDialogTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = gStrings.teachAiPrompt,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = teachFactTitle,
                        onValueChange = { teachFactTitle = it },
                        label = { Text("Tema o Título") },
                        placeholder = { Text("Ej. Historia en Medellín / Aniversario") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = teachFactText,
                        onValueChange = { teachFactText = it },
                        label = { Text("Dato o Corrección Verificada") },
                        placeholder = { Text("Describe el dato para que la IA lo aprenda permanentemente...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (teachFactText.isNotBlank()) {
                            val title = teachFactTitle.ifBlank { "Aporte de la Comunidad" }
                            BasilianAiMemory.addLearnedFact(
                                context = context,
                                title = title,
                                details = teachFactText,
                                category = "COMUNIDAD",
                                verifiedWithGoogle = true
                            )
                            Toast.makeText(context, gStrings.memorySavedToast, Toast.LENGTH_SHORT).show()
                            showTeachDialog = false
                            teachFactTitle = ""
                            teachFactText = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark)
                ) {
                    Text(gStrings.saveBtn)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTeachDialog = false }) {
                    Text(gStrings.cancelBtn)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BasilianIvory)
    ) {
        // 1. ENCABEZADO DE LA GUÍA ESPIRITUAL
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        BasilianCrestView(size = 46.dp, showMottoText = false)
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = gStrings.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ShieldGreenDark,
                                    fontFamily = FontFamily.Serif
                                )
                                Surface(
                                    color = ShieldGold.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = gStrings.geminiBadge,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ShieldGoldDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = gStrings.subtitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldGreenDark.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Surface(
                            onClick = {
                                viewModel.clearChat()
                                Toast.makeText(context, "✨ Nuevo chat iniciado", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(50),
                            color = ShieldGreen.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, ShieldGreen),
                            modifier = Modifier.testTag("btn_clear_chat")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddComment,
                                    contentDescription = gStrings.clearChatTooltip,
                                    tint = ShieldGreenDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Nuevo Chat",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ShieldGreenDark
                                )
                            }
                        }
                    }
                }

                // Indicador de conexión a Google Search y Verificación en Vivo
                Surface(
                    color = Color(0xFFE8F5E9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2E7D32))
                            )
                            Text(
                                text = gStrings.googleSearchLive,
                                fontSize = 11.sp,
                                color = Color(0xFF1B5E20),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // 2. SUGERENCIAS RÁPIDAS DE DIÁLOGO
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = gStrings.quickTopicsTitle,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                letterSpacing = 1.sp
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(gStrings.quickQuestions) { question ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ShieldGreen.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, ShieldGreen.copy(alpha = 0.25f)),
                        modifier = Modifier.clickable {
                            viewModel.sendAiMessage(question)
                        }
                    ) {
                        Text(
                            text = question,
                            style = MaterialTheme.typography.labelMedium,
                            color = ShieldGreenDark,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // 3. LISTA DE MENSAJES DEL CHAT
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(
                    message = msg,
                    onSpeak = { text -> viewModel.speakText(text) }
                )
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = ShieldGold,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = gStrings.thinkingText,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }

        // 4. BARRA INFERIOR DE ENTRADA CON MICRÓFONO Y ENVIAR
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                com.example.ui.components.VoiceDictationMicButton(
                    controller = voiceController,
                    tooltip = gStrings.dictateTooltip,
                    primaryColor = ShieldGreenDark,
                    modifier = Modifier.size(42.dp),
                    testTag = "btn_dictate_speech_guia"
                )

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = gStrings.inputPlaceholder,
                            fontSize = 12.5.sp,
                            maxLines = 1
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ShieldGreen,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = BasilianIvory,
                        unfocusedContainerColor = BasilianIvory
                    ),
                    trailingIcon = {
                        if (inputText.isNotBlank()) {
                            IconButton(onClick = { inputText = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = strings.common.close, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    maxLines = 3
                )

                FilledIconButton(
                    onClick = {
                        val text = inputText
                        if (text.isNotBlank()) {
                            inputText = ""
                            viewModel.sendAiMessage(text)
                        }
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = ShieldGreenDark,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("btn_send_ai_message")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = gStrings.sendBtnDescription,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    onSpeak: ((String) -> Unit)? = null
) {
    val isUser = message.sender == MessageSender.USER
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(ShieldGreenDark)
                    .border(1.dp, ShieldGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = ShieldGoldLight,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) ShieldGreenDark else Color.White,
            border = if (!isUser) BorderStroke(1.dp, ShieldGold.copy(alpha = 0.35f)) else null,
            shadowElevation = 2.dp,
            modifier = Modifier.widthIn(max = 330.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                if (!isUser) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ASISTENTE BASILIANO",
                            style = MaterialTheme.typography.labelSmall,
                            color = ShieldRed,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = { onSpeak?.invoke(message.text) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Escuchar",
                                    tint = ShieldGreenDark,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("San Basilio", message.text)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Copiado", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copiar",
                                    tint = ShieldGreenDark,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUser) Color.White else Color(0xFF1E293B),
                    lineHeight = 22.sp
                )
            }
        }
    }
}
