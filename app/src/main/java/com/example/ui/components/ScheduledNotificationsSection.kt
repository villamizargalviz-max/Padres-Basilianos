package com.example.ui.components

import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.HumanVoiceEngine
import com.example.audio.GoogleCloudTtsService
import com.example.data.model.CustomBirthday
import com.example.data.model.DailyPrayerReminderConfig
import com.example.data.model.LiturgicalSolemnityReminder
import com.example.ui.theme.BasilianAppTheme
import com.example.ui.viewmodel.BasilianViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduledNotificationsSection(
    viewModel: BasilianViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appTheme by viewModel.currentAppTheme.collectAsState()
    val dailyPrayers by viewModel.dailyPrayerConfigs.collectAsState()
    val solemnities by viewModel.solemnityReminders.collectAsState()
    val customBirthdays by viewModel.customBirthdays.collectAsState()
    val birthdaysEnabledGeneral by viewModel.birthdaysEnabledGeneral.collectAsState()
    val voicePace by viewModel.voicePace.collectAsState()
    val isTtsSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val useCloudTts by viewModel.useCloudTts.collectAsState()
    val cloudVoice by viewModel.cloudVoice.collectAsState()
    val isCloudTtsLoading by viewModel.isCloudTtsLoading.collectAsState()
    val cloudTtsLastError by viewModel.cloudTtsLastError.collectAsState()

    var showAddBirthdayDialog by remember { mutableStateOf(false) }
    var showApiKeyDialog by remember { mutableStateOf(false) }
    var showTimePickerForPrayer by remember { mutableStateOf<DailyPrayerReminderConfig?>(null) }
    var selectedCategoryTab by remember { mutableIntStateOf(0) } // 0 = Oración Diaria, 1 = Solemnidades, 2 = Cumpleaños, 3 = Voz Humana

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("scheduled_notifications_section"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card informativo
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = appTheme.primaryColor.copy(alpha = 0.08f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, appTheme.primaryColor.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = appTheme.primaryColor.copy(alpha = 0.18f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = appTheme.primaryColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Notificaciones Litúrgicas y Fraternas",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Recordatorios programados en tu dispositivo",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Recibe avisos puntuales para consagrar el día en oración (Laudes, Ángelus, Vísperas), no olvidar las grandes solemnidades y felicitar a los cohermanos y seres queridos en sus cumpleaños.",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Botones de Prueba Rápida
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.sendInstantTestPush("DAILY_PRAYER", context)
                                Toast.makeText(context, "🔔 Notificación de Oración enviada", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp)
                        ) {
                            Icon(Icons.Default.Notifications, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Probar Oración", fontSize = 11.sp, maxLines = 1)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.sendInstantTestPush("BIRTHDAY", context)
                                Toast.makeText(context, "🎂 Notificación de Cumpleaños enviada", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp)
                        ) {
                            Icon(Icons.Default.Cake, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Probar Cumple", fontSize = 11.sp, maxLines = 1)
                        }
                    }
                }
            }
        }

        // Sub-tabs de gestión de recordatorios
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryTab,
                edgePadding = 0.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = appTheme.primaryColor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedCategoryTab == 0,
                    onClick = { selectedCategoryTab = 0 },
                    text = { Text("🌅 Oración Diaria (${dailyPrayers.count { it.isEnabled }}/3)") }
                )
                Tab(
                    selected = selectedCategoryTab == 1,
                    onClick = { selectedCategoryTab = 1 },
                    text = { Text("✝️ Solemnidades (${solemnities.count { it.isEnabled }})") }
                )
                Tab(
                    selected = selectedCategoryTab == 2,
                    onClick = { selectedCategoryTab = 2 },
                    text = { Text("🎂 Cumpleaños (${customBirthdays.size})") }
                )
                Tab(
                    selected = selectedCategoryTab == 3,
                    onClick = { selectedCategoryTab = 3 },
                    text = { Text("🕊️ Voz Humana") }
                )
            }
        }

        // TAB 0: ORACIÓN DIARIA
        if (selectedCategoryTab == 0) {
            item {
                Text(
                    text = "Horarios de Oración Litúrgica Diaria",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items(dailyPrayers) { prayer ->
                DailyPrayerConfigCard(
                    prayer = prayer,
                    appTheme = appTheme,
                    onToggle = { enabled ->
                        viewModel.toggleDailyPrayer(prayer.type, enabled, context)
                    },
                    onChangeTime = {
                        showTimePickerForPrayer = prayer
                    },
                    onTestNotification = {
                        viewModel.sendInstantTestPush("DAILY_PRAYER", context)
                    }
                )
            }
        }

        // TAB 1: SOLEMNIDADES LITÚRGICAS
        if (selectedCategoryTab == 1) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Solemnidades y Fiestas Patronales",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Recordatorio a las 8:00 AM",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }

            items(solemnities) { solemnity ->
                SolemnityReminderCard(
                    solemnity = solemnity,
                    appTheme = appTheme,
                    onToggle = {
                        viewModel.toggleSolemnityReminder(solemnity, context)
                    }
                )
            }
        }

        // TAB 2: CUMPLEAÑOS Y ANIVERSARIOS
        if (selectedCategoryTab == 2) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp),
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
                                text = "Avisos de Cumpleaños a las 8:30 AM",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Aviso fraterno para orar y felicitar al cohermano o amigo",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Switch(
                            checked = birthdaysEnabledGeneral,
                            onCheckedChange = { enabled ->
                                viewModel.toggleAllBirthdays(enabled, context)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = appTheme.primaryColor)
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cumpleaños Registrados (${customBirthdays.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    FilledTonalButton(
                        onClick = { showAddBirthdayDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Agregar Cumpleaños", fontSize = 12.sp)
                    }
                }
            }

            items(customBirthdays) { birthday ->
                BirthdayReminderCard(
                    birthday = birthday,
                    appTheme = appTheme,
                    onToggleReminder = {
                        viewModel.toggleCustomBirthdayReminder(birthday, context)
                    },
                    onDelete = {
                        viewModel.deleteCustomBirthday(birthday.id, context)
                    }
                )
            }
        }

        // TAB 3: CALIBRACIÓN DE VOZ HUMANA ORANTE
        if (selectedCategoryTab == 3) {
            item {
                // Card 1: Google Cloud Text-to-Speech (Voces Neural / Studio / WaveNet)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, appTheme.primaryColor.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = Icons.Default.SpatialAudio,
                                    contentDescription = null,
                                    tint = appTheme.primaryColor,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Google Cloud Text-to-Speech",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Voces humanas Neural (Studio, Journey y WaveNet)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = appTheme.goldColor
                                    )
                                }
                            }
                            Switch(
                                checked = useCloudTts,
                                onCheckedChange = { viewModel.setUseCloudTts(it) }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Estilo de voces humanas agregado a la app: 5 voces naturales (3 de mujeres y 2 de hombres) con hablado latinoamericano profundo, tranquilo y sereno, con soporte fluido en Español, Inglés y Francés.",
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 19.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        if (useCloudTts) {
                            Text(
                                text = "Elige el Estilo de Voz Humana (3 Mujeres • 2 Hombres):",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            GoogleCloudTtsService.CloudVoiceOption.values().forEach { voice ->
                                val isSelected = cloudVoice == voice
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) appTheme.primaryColor.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) appTheme.primaryColor else Color.Transparent
                                    ),
                                    onClick = { viewModel.selectCloudVoice(voice) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = voice.displayName,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isSelected) appTheme.primaryColor else MaterialTheme.colorScheme.onSurface,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(
                                                    color = if (voice.isFemale) appTheme.goldColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer,
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = voice.voiceType,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (voice.isFemale) appTheme.goldColor else MaterialTheme.colorScheme.onPrimaryContainer,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                            Text(
                                                text = voice.styleDescription,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray,
                                                fontSize = 11.sp
                                            )
                                        }
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { viewModel.selectCloudVoice(voice) }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Clave API y estado de conexión
                            val hasKey = viewModel.getEffectiveCloudTtsApiKey().isNotBlank()
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (hasKey) Color(0xFF1B5E20).copy(alpha = 0.1f) else Color(0xFFE65100).copy(alpha = 0.1f),
                                border = BorderStroke(1.dp, if (hasKey) Color(0xFF2E7D32).copy(alpha = 0.4f) else Color(0xFFEF6C00).copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Icon(
                                            imageVector = if (hasKey) Icons.Default.CheckCircle else Icons.Default.Key,
                                            contentDescription = null,
                                            tint = if (hasKey) Color(0xFF2E7D32) else Color(0xFFEF6C00),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (hasKey) "Google Cloud TTS configurado y listo" else "Clave API de Google Cloud no configurada",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (hasKey) Color(0xFF2E7D32) else Color(0xFFEF6C00),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    TextButton(onClick = { showApiKeyDialog = true }) {
                                        Text(if (hasKey) "Cambiar" else "Configurar", fontSize = 12.sp)
                                    }
                                }
                            }

                            if (cloudTtsLastError != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Aviso: $cloudTtsLastError (usando motor local sereno como respaldo)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFC62828),
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Botón de Prueba Google Cloud
                            Button(
                                onClick = {
                                    if (isTtsSpeaking) {
                                        viewModel.stopSpeaking()
                                    } else {
                                        viewModel.previewSereneVoice()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isTtsSpeaking) Color(0xFFC62828) else appTheme.primaryColor
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (isCloudTtsLoading) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Generando voz neural de estudio...", fontWeight = FontWeight.Bold)
                                } else {
                                    Icon(
                                        imageVector = if (isTtsSpeaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isTtsSpeaking) "Detener Reproducción" else "🎧 Probar Voz ${cloudVoice.displayName}",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card 2: Motor Local Modulado (Fallback sin conexión)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, appTheme.primaryColor.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = appTheme.primaryColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Motor Local con Modulación Humana (Offline)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Respaldo automático sin consumo de datos ni internet",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Cadencia y respiración aplicadas al sintetizador del dispositivo:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        HumanVoiceEngine.VoicePace.values().forEach { pace ->
                            val isSelected = voicePace == pace
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) appTheme.primaryColor.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) appTheme.primaryColor else Color.Transparent
                                ),
                                onClick = { viewModel.setVoicePace(pace) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = pace.label,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) appTheme.primaryColor else MaterialTheme.colorScheme.onSurface,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            text = "Velocidad: ${(pace.speechRate * 100).toInt()}% • Tono: ${(pace.pitch * 100).toInt()}%",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray,
                                            fontSize = 11.sp
                                        )
                                    }
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { viewModel.setVoicePace(pace) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo para Agregar Cumpleaños
    if (showAddBirthdayDialog) {
        AddBirthdayDialog(
            appTheme = appTheme,
            onDismiss = { showAddBirthdayDialog = false },
            onAdd = { name, day, month, role, blessing ->
                viewModel.addCustomBirthday(name, day, month, role, blessing, context)
                showAddBirthdayDialog = false
                Toast.makeText(context, "🎂 Cumpleaños agregado con recordatorio programado", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Diálogo para Configurar API Key de Google Cloud TTS
    if (showApiKeyDialog) {
        CloudTtsApiKeyDialog(
            currentKey = viewModel.getEffectiveCloudTtsApiKey(),
            appTheme = appTheme,
            onDismiss = { showApiKeyDialog = false },
            onSave = { newKey ->
                viewModel.setCustomCloudTtsApiKey(newKey)
                showApiKeyDialog = false
                Toast.makeText(context, "Clave API de Google Cloud guardada", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // TimePicker para Oración Diaria
    showTimePickerForPrayer?.let { prayer ->
        val calendar = Calendar.getInstance()
        val timePicker = remember {
            TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    viewModel.updateDailyPrayerTime(prayer.type, hourOfDay, minute, context)
                    showTimePickerForPrayer = null
                    Toast.makeText(context, "Hora actualizada a las $hourOfDay:${if (minute < 10) "0$minute" else minute}", Toast.LENGTH_SHORT).show()
                },
                prayer.defaultHour,
                prayer.defaultMinute,
                false
            ).apply {
                setOnDismissListener { showTimePickerForPrayer = null }
            }
        }

        LaunchedEffect(prayer) {
            timePicker.show()
        }
    }
}

@Composable
private fun DailyPrayerConfigCard(
    prayer: DailyPrayerReminderConfig,
    appTheme: BasilianAppTheme,
    onToggle: (Boolean) -> Unit,
    onChangeTime: () -> Unit,
    onTestNotification: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(prayer.iconEmoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = prayer.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = prayer.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Switch(
                    checked = prayer.isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(checkedThumbColor = appTheme.primaryColor)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onChangeTime,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Schedule, null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Hora: ${prayer.formattedTime}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                TextButton(
                    onClick = onTestNotification,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Send, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Enviar aviso ahora", fontSize = 11.5.sp)
                }
            }
        }
    }
}

@Composable
private fun SolemnityReminderCard(
    solemnity: LiturgicalSolemnityReminder,
    appTheme: BasilianAppTheme,
    onToggle: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
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
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = appTheme.primaryColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = solemnity.formattedDate,
                        style = MaterialTheme.typography.labelSmall,
                        color = appTheme.primaryColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = solemnity.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = solemnity.briefDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Switch(
                checked = solemnity.isEnabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(checkedThumbColor = appTheme.primaryColor)
            )
        }
    }
}

@Composable
private fun BirthdayReminderCard(
    birthday: CustomBirthday,
    appTheme: BasilianAppTheme,
    onToggleReminder: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = appTheme.secondaryColor.copy(alpha = 0.15f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Cake,
                                contentDescription = null,
                                tint = appTheme.secondaryColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = birthday.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${birthday.formattedDate} • ${birthday.relationOrRole}",
                            style = MaterialTheme.typography.bodySmall,
                            color = appTheme.primaryColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Switch(
                    checked = birthday.isReminderEnabled,
                    onCheckedChange = { onToggleReminder() },
                    colors = SwitchDefaults.colors(checkedThumbColor = appTheme.secondaryColor)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "«${birthday.personalBlessing}»",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

            if (birthday.id.startsWith("bday_custom_")) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddBirthdayDialog(
    appTheme: BasilianAppTheme,
    onDismiss: () -> Unit,
    onAdd: (name: String, day: Int, month: Int, role: String, blessing: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dayText by remember { mutableStateOf("") }
    var month by remember { mutableIntStateOf(1) }
    var role by remember { mutableStateOf("") }
    var blessing by remember { mutableStateOf("") }

    val months = listOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Cake, null, tint = appTheme.secondaryColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nuevo Cumpleaños")
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del cohermano o ser querido") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = dayText,
                        onValueChange = { if (it.length <= 2) dayText = it },
                        label = { Text("Día (1-31)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    var expandedMonth by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.weight(1.5f)) {
                        OutlinedButton(
                            onClick = { expandedMonth = true },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text(months[month - 1], maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }

                        DropdownMenu(
                            expanded = expandedMonth,
                            onDismissRequest = { expandedMonth = false }
                        ) {
                            months.forEachIndexed { index, mName ->
                                DropdownMenuItem(
                                    text = { Text(mName) },
                                    onClick = {
                                        month = index + 1
                                        expandedMonth = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Relación / Título (ej: Párroco, Amigo, Cohermano)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = blessing,
                    onValueChange = { blessing = it },
                    label = { Text("Mensaje o Bendición para ese día") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val dayNum = dayText.toIntOrNull() ?: 1
                    if (name.isNotBlank() && dayNum in 1..31) {
                        onAdd(name, dayNum, month, role, blessing)
                    }
                },
                enabled = name.isNotBlank() && (dayText.toIntOrNull() in 1..31)
            ) {
                Text("Guardar y Programar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun CloudTtsApiKeyDialog(
    currentKey: String,
    appTheme: BasilianAppTheme,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var keyText by remember { mutableStateOf("") }
    val maskedCurrent = remember(currentKey) {
        com.example.data.security.BasilianSecurityManager.maskApiKey(currentKey)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VpnKey, null, tint = appTheme.primaryColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Conexión Segura de Voz (Cifrada)")
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Estado actual: $maskedCurrent\nPor seguridad, las claves API permanecen ocultas y encriptadas con AES-256.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = keyText,
                    onValueChange = { keyText = it },
                    label = { Text("Nueva Clave API (Oculta)") },
                    placeholder = { Text("••••••••••••••••") },
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Nota: Puedes dejarlo vacío y pulsar «Restaurar Voz del Servidor» si una clave anterior presenta conflicto.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedButton(
                    onClick = { onSave("") }
                ) {
                    Text("Restaurar Servidor")
                }
                Button(
                    onClick = { onSave(keyText.trim()) }
                ) {
                    Text("Guardar Cifrado")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

