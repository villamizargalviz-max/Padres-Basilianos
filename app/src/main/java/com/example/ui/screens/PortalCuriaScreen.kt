package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.ai.AppAnalysisReport
import com.example.data.ai.AppImprovementSuggestion
import com.example.data.repository.BasilianStaticData
import com.example.ui.components.BasilianCrestView
import com.example.ui.components.GlobalBgMode
import com.example.ui.components.CraftedSectionHeader
import com.example.ui.components.OrganicBrushUnderline
import com.example.ui.components.SanBasilioIconView
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel
import com.example.ui.viewmodel.UploadedCorpusDocument

@Composable
fun PortalCuriaScreen(
    viewModel: BasilianViewModel,
    modifier: Modifier = Modifier
) {
    val isAuthenticated by viewModel.curiaIsAuthenticated.collectAsState()
    val curiaTitle by viewModel.curiaAnnouncementTitle.collectAsState()
    val curiaBody by viewModel.curiaAnnouncementBody.collectAsState()
    val curiaDate by viewModel.curiaAnnouncementDate.collectAsState()
    val curiaPrayer by viewModel.curiaPrayerIntention.collectAsState()
    val syncStatus by viewModel.curiaSyncStatus.collectAsState()
    val adminEmail by viewModel.curiaAdminEmail.collectAsState()
    val isFirebaseAvailable by viewModel.curiaIsFirebaseAvailable.collectAsState()

    val customCrestUri by viewModel.customCrestUri.collectAsState()
    val customPhotos by viewModel.customPhotos.collectAsState()
    val curiaMasterPassword by viewModel.curiaMasterPassword.collectAsState()
    val basilianCommunityUnlockCode by viewModel.basilianCommunityUnlockCode.collectAsState()
    val ownerQuickPin by viewModel.ownerQuickPin.collectAsState()
    val vaultAutoLockMinutes by viewModel.vaultAutoLockMinutes.collectAsState()
    val personalPasswordVault by viewModel.personalPasswordVault.collectAsState()
    val intrusionAlerts by viewModel.intrusionAlerts.collectAsState()
    val leaderCreatorEmail by viewModel.leaderCreatorEmail.collectAsState()
    var leaderEmailInput by remember(leaderCreatorEmail) { mutableStateOf(leaderCreatorEmail) }
    val maxSecurityShieldEnabled by viewModel.maxSecurityShieldEnabled.collectAsState()
    val liveAppVersion by viewModel.liveAppVersion.collectAsState()
    val jarvisDailyFocusTopic by viewModel.jarvisDailyFocusTopic.collectAsState()
    val liveUpdateHistoryLog by viewModel.liveUpdateHistoryLog.collectAsState()
    var jarvisFocusInput by remember(jarvisDailyFocusTopic) { mutableStateOf(jarvisDailyFocusTopic) }
    val otaReleaseNotes by viewModel.otaReleaseNotes.collectAsState()
    val otaDownloadUrl by viewModel.otaDownloadUrl.collectAsState()
    val autoBroadcastOnLeaderInstall by viewModel.autoBroadcastOnLeaderInstall.collectAsState()
    val lastGlobalSyncTimestamp by viewModel.lastGlobalSyncTimestamp.collectAsState()
    var otaVersionInput by remember(liveAppVersion) { mutableStateOf(liveAppVersion) }
    var otaNotesInput by remember(otaReleaseNotes) { mutableStateOf(otaReleaseNotes) }
    var otaUrlInput by remember(otaDownloadUrl) { mutableStateOf(otaDownloadUrl) }

    var passwordInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    val basilianAccessRequests by viewModel.basilianAccessRequests.collectAsState()
    val registeredBasilianEmails by viewModel.registeredBasilianEmails.collectAsState()
    var manualApproveEmailInput by remember { mutableStateOf("") }
    var newBasilianEmailInput by remember { mutableStateOf("") }
    var newBasilianFatherNameInput by remember { mutableStateOf("") }

    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var newPasswordInput by remember { mutableStateOf("") }
    var newPasswordVisible by remember { mutableStateOf(false) }

    // Estados de la Bóveda Privada de Contraseñas («Solo para Mí» - Ocultas por defecto)
    var revealSavedMasterPass by remember { mutableStateOf(false) }
    var revealSavedCsbCode by remember { mutableStateOf(false) }
    var revealSavedQuickPin by remember { mutableStateOf(false) }
    var inlineNewMasterPassword by remember { mutableStateOf("") }
    var inlineNewMasterPassVisible by remember { mutableStateOf(false) }
    var inlineNewCsbCode by remember { mutableStateOf("") }
    var inlineNewCsbCodeVisible by remember { mutableStateOf(false) }
    var inlineNewQuickPin by remember { mutableStateOf("") }
    var inlineNewQuickPinVisible by remember { mutableStateOf(false) }
    var customNoteLabelInput by remember { mutableStateOf("") }
    var customNoteSecretInput by remember { mutableStateOf("") }
    var customNoteSecretVisible by remember { mutableStateOf(false) }
    var revealedVaultNoteIds by remember { mutableStateOf(setOf<String>()) }
    var photoCategoryFilter by remember { mutableStateOf(0) } // 0: Fundadores, 1: Misión Colombia, 2: Santos Patronos
    var selectedPhotoTargetId by remember { mutableStateOf<String?>(null) }
    var selectedPhotoTargetName by remember { mutableStateOf<String?>(null) }

    // Lanzador de selector de fotos para el Escudo Original
    val crestPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.setCustomCrest(uri)
            successMessage = "¡Escudo original subido y actualizado en toda la aplicación!"
        }
    }

    // Lanzador de selector de fotos para figuras basilianas
    val basilianPhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        val targetId = selectedPhotoTargetId
        if (uri != null && targetId != null) {
            viewModel.setCustomPhoto(targetId, uri)
            successMessage = "¡Foto de ${selectedPhotoTargetName ?: "figura basiliana"} actualizada con éxito!"
        }
        selectedPhotoTargetId = null
        selectedPhotoTargetName = null
    }

    val customPrayerSideImageUri by viewModel.customPrayerSideImageUri.collectAsState()

    // Lanzador de selector de fotos para Imagen Devocional de Oración
    val prayerSideImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.setCustomPrayerSideImage(uri)
            successMessage = "¡Imagen devocional de oración actualizada con éxito!"
        }
    }

    var editTitle by remember(curiaTitle) { mutableStateOf(curiaTitle) }
    var editBody by remember(curiaBody) { mutableStateOf(curiaBody) }
    var editDate by remember(curiaDate) { mutableStateOf(curiaDate) }
    var editPrayer by remember(curiaPrayer) { mutableStateOf(curiaPrayer) }

    val copilotReflection by viewModel.copilotReflection.collectAsState()
    val isCopilotGenerating by viewModel.isCopilotGenerating.collectAsState()
    val copilotCorpusCategory by viewModel.copilotCorpusCategory.collectAsState()
    var copilotPromptInput by remember { mutableStateOf("") }

    val appUpdateProposal by viewModel.appUpdateProposal.collectAsState()
    val isAppUpdateGenerating by viewModel.isAppUpdateGenerating.collectAsState()
    var appUpdateRequestInput by remember { mutableStateOf("") }

    val aiCommandResult by viewModel.aiCommandExecutionResult.collectAsState()
    val showTabOracion by viewModel.showTabOracion.collectAsState()
    val showTabLiturgia by viewModel.showTabLiturgia.collectAsState()
    val showTabCumpleanos by viewModel.showTabCumpleanos.collectAsState()
    val showTabHistoria by viewModel.showTabHistoria.collectAsState()
    val showTabRecursos by viewModel.showTabRecursos.collectAsState()

    val showHeroBanner by viewModel.showHeroBanner.collectAsState()
    val showDevotionalImageSlots by viewModel.showDevotionalImageSlots.collectAsState()
    val showOrdoQuickAccess by viewModel.showOrdoQuickAccess.collectAsState()
    val showCuriaAnnouncementInPrayer by viewModel.showCuriaAnnouncementInPrayer.collectAsState()
    val showDailyAudioPlayer by viewModel.showDailyAudioPlayer.collectAsState()
    val showCommunitySocialWall by viewModel.showCommunitySocialWall.collectAsState()
    val currentAppTheme by viewModel.currentAppTheme.collectAsState()

    val globalBgMode by viewModel.globalBackgroundMode.collectAsState()
    val globalCustomUri by viewModel.globalBackgroundCustomUri.collectAsState()
    val globalOpacity by viewModel.globalBackgroundOpacity.collectAsState()

    val appAnalysisReport by viewModel.appAnalysisReport.collectAsState()
    val isAnalyzingApp by viewModel.isAnalyzingApp.collectAsState()
    val appliedAiConfigKeys by viewModel.appliedAiConfigKeys.collectAsState()

    val uploadedCorpusDocs by viewModel.uploadedCorpusDocuments.collectAsState()

    var showAddDocDialog by remember { mutableStateOf(false) }
    var newDocTitle by remember { mutableStateOf("") }
    var newDocCategory by remember { mutableStateOf("Constituciones") }
    var newDocContent by remember { mutableStateOf("") }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    // Lanzador para seleccionar fondo personalizado de la galería
    val bgImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.setGlobalBackgroundCustomUri(uri.toString())
            viewModel.setGlobalBackgroundMode(GlobalBgMode.CUSTOM_IMAGE)
            successMessage = "¡Fondo sagrado personalizado actualizado para toda la aplicación!"
        }
    }

    // Diálogo para subir documento al corpus de la IA
    if (showAddDocDialog) {
        AlertDialog(
            onDismissRequest = { showAddDocDialog = false },
            title = {
                Text(
                    text = "Subir Documento al Corpus IA",
                    fontWeight = FontWeight.Bold,
                    color = ShieldGreenDark
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "El contenido de este documento se integrará al conocimiento y razonamiento de Basilio Estándar.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )

                    OutlinedTextField(
                        value = newDocTitle,
                        onValueChange = { newDocTitle = it },
                        label = { Text("Título del Documento") },
                        placeholder = { Text("Ej: Constituciones C.S.B. - Art. 12") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column {
                        Text("Categoría del Documento:", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            val categories = listOf("Constituciones", "Cartas Curiales", "Espiritualidad", "Directorio", "Santos Patronos")
                            items(categories) { cat ->
                                FilterChip(
                                    selected = newDocCategory == cat,
                                    onClick = { newDocCategory = cat },
                                    label = { Text(cat, fontSize = 11.sp) }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = newDocContent,
                        onValueChange = { newDocContent = it },
                        label = { Text("Texto / Extracto del Documento") },
                        placeholder = { Text("Pega aquí el contenido, directriz, circular o regla...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        maxLines = 6
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newDocTitle.isNotBlank() && newDocContent.isNotBlank()) {
                            viewModel.addCorpusDocument(
                                title = newDocTitle.trim(),
                                category = newDocCategory,
                                content = newDocContent.trim()
                            )
                            newDocTitle = ""
                            newDocContent = ""
                            showAddDocDialog = false
                            successMessage = "¡Documento añadido al corpus y conocimiento de la IA con éxito!"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark)
                ) {
                    Text("Incorporar a la IA")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDocDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showChangePasswordDialog) {
        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = {
                Text(
                    text = "Cambiar Contraseña de Acceso",
                    fontWeight = FontWeight.Bold,
                    color = ShieldGreenDark
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Establezca su clave personal exclusiva. No necesitará correo electrónico para ingresar.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                    OutlinedTextField(
                        value = newPasswordInput,
                        onValueChange = { newPasswordInput = it },
                        label = { Text("Nueva Contraseña") },
                        placeholder = { Text("Ingrese nueva clave confidencial") },
                        singleLine = true,
                        visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null, tint = ShieldGreenDark) },
                        trailingIcon = {
                            IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                                Icon(
                                    imageVector = if (newPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Alternar visibilidad"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPasswordInput.trim().isNotEmpty()) {
                            viewModel.setCuriaMasterPassword(newPasswordInput.trim())
                            showChangePasswordDialog = false
                            newPasswordInput = ""
                            successMessage = "¡Contraseña personal actualizada exitosamente!"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark)
                ) {
                    Text("Guardar Clave")
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePasswordDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    var selectedAdminCategory by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!isAuthenticated) {
            // Pantalla de Autenticación Minimalista: SOLO CANDADO Y CONTRASEÑA (Sin informes ni textos adicionales)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .testTag("curia_auth_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.5.dp, ShieldGold.copy(alpha = 0.65f)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(68.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(ShieldGreenDark.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Candado de Acceso Exclusivo",
                                tint = ShieldGreenDark,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Text(
                            text = "Acceso Exclusivo • Solo para Mí",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = ShieldGreenDark
                        )

                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = {
                                passwordInput = it
                                errorMessage = null
                            },
                            label = { Text("Contraseña") },
                            placeholder = { Text("••••••••") },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            leadingIcon = {
                                Icon(Icons.Default.Key, contentDescription = null, tint = ShieldGreenDark)
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Alternar visibilidad"
                                    )
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                val lockoutSec = viewModel.getCuriaLockoutRemainingSeconds()
                                if (lockoutSec > 0) {
                                    errorMessage = "🛡️ Protección Anti-Hackeo activa: Espera ${lockoutSec}s antes de intentar de nuevo."
                                } else {
                                    isSubmitting = true
                                    val ok = viewModel.authenticateCuria(passwordInput)
                                    isSubmitting = false
                                    if (!ok) {
                                        val newLockout = viewModel.getCuriaLockoutRemainingSeconds()
                                        errorMessage = if (newLockout > 0) {
                                            "🛡️ Demasiados intentos fallidos. Acceso bloqueado por ${newLockout}s por seguridad."
                                        } else {
                                            "Contraseña incorrecta."
                                        }
                                    }
                                }
                            }),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("curia_password_input"),
                            shape = RoundedCornerShape(14.dp)
                        )

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = ShieldRed,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        }

                        Button(
                            onClick = {
                                val lockoutSec = viewModel.getCuriaLockoutRemainingSeconds()
                                if (lockoutSec > 0) {
                                    errorMessage = "🛡️ Protección Anti-Hackeo activa: Espera ${lockoutSec}s antes de intentar de nuevo."
                                } else {
                                    isSubmitting = true
                                    val ok = viewModel.authenticateCuria(passwordInput)
                                    isSubmitting = false
                                    if (!ok) {
                                        val newLockout = viewModel.getCuriaLockoutRemainingSeconds()
                                        errorMessage = if (newLockout > 0) {
                                            "🛡️ Demasiados intentos fallidos. Acceso bloqueado por ${newLockout}s por seguridad."
                                        } else {
                                            "Contraseña incorrecta."
                                        }
                                    }
                                }
                            },
                            enabled = !isSubmitting,
                            colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_curia_login")
                        ) {
                            Icon(Icons.Default.VpnKey, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ingresar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // Cabecera Oficial del Portal Autenticado
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentAppTheme.isDark) Color(0xFF1E293B).copy(alpha = 0.88f) else Color.White.copy(alpha = 0.95f)
                    ),
                    border = BorderStroke(1.5.dp, ShieldGold.copy(alpha = 0.6f)),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            BasilianCrestView(size = 58.dp, showMottoText = false)
                            Column(modifier = Modifier.weight(1f)) {
                                CraftedSectionHeader(
                                    title = "Centro de Control • Solo para Mí",
                                    subtitle = "Edición global en tiempo real para todos los dispositivos",
                                    emoji = "🏛️",
                                    accentColor = ShieldGoldDark,
                                    underlineWidth = 160.dp
                                )
                            }
                        }

                        // Barra de estado y cierre / cambio de clave
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ShieldGreen.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, ShieldGreen.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VerifiedUser,
                                        contentDescription = null,
                                        tint = ShieldGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "Modo Administrador Activo",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = ShieldGreenDark
                                        )
                                        Text(
                                            text = "Tus cambios se reflejan en todos los celulares instalados",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.DarkGray,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    FilledTonalButton(
                                        onClick = { showChangePasswordDialog = true },
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = ShieldGreenDark.copy(alpha = 0.15f),
                                            contentColor = ShieldGreenDark
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Icon(Icons.Default.LockReset, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Clave", fontSize = 11.sp)
                                    }

                                    OutlinedButton(
                                        onClick = { viewModel.logoutCuria() },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldRed),
                                        border = BorderStroke(1.dp, ShieldRed),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("Salir", fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        // Botón de Sincronización Global Inmediata a Todos los Celulares
                        Button(
                            onClick = {
                                viewModel.syncGlobalAdminChangesToAllDevices(forceBroadcastAlert = true) { _, msg ->
                                    successMessage = msg
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_sync_all_devices_now")
                        ) {
                            Icon(Icons.Default.CloudSync, contentDescription = null, tint = ShieldGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sincronizar Mis Cambios en Todos los Celulares Ahora",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // =========================================================================
            // CENTRO DE ACTUALIZACIÓN GLOBAL EN VIVO (DE MI CELULAR A TODOS LOS CELULARES)
            // =========================================================================
            if (selectedAdminCategory == 0 || selectedAdminCategory == 4 || selectedAdminCategory == 1) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_global_live_ota_sync_all_phones"),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF041829)),
                        border = BorderStroke(1.8.dp, Color(0xFF00F0FF)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF00F0FF).copy(alpha = 0.18f),
                                    border = BorderStroke(1.5.dp, Color(0xFF00F0FF)),
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.SystemUpdate,
                                            contentDescription = null,
                                            tint = Color(0xFF00F0FF),
                                            modifier = Modifier.size(25.dp)
                                        )
                                    }
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "🚀 Actualización Global en Vivo • De Mi Celular a Todos los Celulares",
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "Exclusivo «Solo para Mí»: Cuando modifiques algo aquí o instales la nueva versión en tu celular, todos los celulares que tengan la app se actualizarán en tiempo real.",
                                        color = Color(0xFFBAE6FD),
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            // Estado actual de sincronización global
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF06283D),
                                border = BorderStroke(1.dp, Color(0xFF34D399).copy(alpha = 0.55f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudDone,
                                        contentDescription = null,
                                        tint = Color(0xFF34D399),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Versión Activa en la Red: $liveAppVersion",
                                            color = Color(0xFF6EE7B7),
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = lastGlobalSyncTimestamp,
                                            color = Color(0xFFE0F2FE),
                                            fontSize = 10.5.sp
                                        )
                                    }
                                }
                            }

                            // Interruptor de Auto-Sincronización al instalar en el celular del Líder
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF08223A),
                                border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "📲 Auto-Actualizar Todos los Celulares al Instalar en el Mío",
                                            color = Color(0xFFFDE68A),
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.5.sp
                                        )
                                        Text(
                                            text = "Apenas instales y abras la nueva versión en tu celular con tu correo de Líder, se propagan automáticamente todas las mejoras, textos, IA y avisos a todos los demás celulares.",
                                            color = Color(0xFFCBD5E1),
                                            fontSize = 10.5.sp,
                                            lineHeight = 14.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Switch(
                                        checked = autoBroadcastOnLeaderInstall,
                                        onCheckedChange = {
                                            viewModel.setAutoBroadcastOnLeaderInstall(it)
                                            successMessage = if (it) {
                                                "📲 Auto-Actualización Global al instalar en tu celular ACTIVADA."
                                            } else {
                                                "📲 Modo de actualización manual activado."
                                            }
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = Color(0xFF10B981)
                                        )
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = otaVersionInput,
                                onValueChange = { otaVersionInput = it },
                                label = { Text("Etiqueta de Versión Global", fontSize = 11.sp, color = Color(0xFF00F0FF)) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFF00F0FF),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = otaNotesInput,
                                onValueChange = { otaNotesInput = it },
                                label = { Text("Novedades y mejoras que verán todos los celulares", fontSize = 11.sp, color = Color(0xFFFDE68A)) },
                                maxLines = 3,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFFF59E0B),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Panel de Link Real y Verídico para Actualizar / Enviar a una Persona
                            val activeVerifiedUrl = otaUrlInput.trim().ifBlank {
                                BasilianViewModel.VERIFIED_SHARED_APP_URL
                            }
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF062038),
                                border = BorderStroke(1.5.dp, Color(0xFF00F0FF).copy(alpha = 0.85f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Verified,
                                                contentDescription = null,
                                                tint = Color(0xFF00F0FF),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Column {
                                                Text(
                                                    text = "🔗 LINK REAL Y VERÍDICO DE ACTUALIZACIÓN",
                                                    color = Color(0xFF00F0FF),
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 11.5.sp
                                                )
                                                Text(
                                                    text = "Accede directamente o envíaselo a cualquier persona para que actualice su aplicación de inmediato.",
                                                    color = Color(0xFFBAE6FD),
                                                    fontSize = 10.2.sp,
                                                    lineHeight = 13.sp
                                                )
                                            }
                                        }
                                        TextButton(
                                            onClick = {
                                                otaUrlInput = BasilianViewModel.VERIFIED_SHARED_APP_URL
                                                viewModel.setOtaDownloadUrl(BasilianViewModel.VERIFIED_SHARED_APP_URL)
                                                successMessage = "✅ Link oficial verídico restaurado."
                                            }
                                        ) {
                                            Text("Oficial", color = Color(0xFFFDE68A), fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    OutlinedTextField(
                                        value = otaUrlInput,
                                        onValueChange = {
                                            otaUrlInput = it
                                            viewModel.setOtaDownloadUrl(it)
                                        },
                                        label = { Text("Link Verídico para Actualizar / Descargar la App", fontSize = 10.5.sp, color = Color(0xFF34D399)) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                            focusedBorderColor = Color(0xFF34D399),
                                            unfocusedBorderColor = Color.White.copy(alpha = 0.35f)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("input_verified_update_link")
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        // 1. Abrir el link real inmediatamente
                                        Button(
                                            onClick = {
                                                try {
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(activeVerifiedUrl))
                                                    context.startActivity(intent)
                                                } catch (_: Exception) {
                                                    android.widget.Toast.makeText(context, "No se pudo abrir el navegador", android.widget.Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF10B981),
                                                contentColor = Color(0xFF022C22)
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("btn_open_verified_update_link")
                                        ) {
                                            Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Text("Abrir Link Real", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, maxLines = 1)
                                        }

                                        // 2. Enviar el link real a una persona (WhatsApp, Correo, etc.)
                                        Button(
                                            onClick = {
                                                val shareText = buildString {
                                                    append("🛡️ *Actualización Oficial — Padres Basilianos (C.S.B.)*\n")
                                                    append("Versión: $otaVersionInput\n")
                                                    if (otaNotesInput.isNotBlank()) {
                                                        append("Novedades: $otaNotesInput\n\n")
                                                    }
                                                    append("📲 Entra a este link oficial verídico para actualizar y abrir la aplicación inmediatamente:\n")
                                                    append(activeVerifiedUrl)
                                                }
                                                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                                    type = "text/plain"
                                                    putExtra(Intent.EXTRA_SUBJECT, "Actualización App Padres Basilianos ($otaVersionInput)")
                                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                                }
                                                context.startActivity(Intent.createChooser(sendIntent, "Enviar link de actualización a una persona"))
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFF59E0B),
                                                contentColor = Color(0xFF020617)
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("btn_share_verified_update_link")
                                        ) {
                                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Text("Enviar a Persona", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, maxLines = 1)
                                        }

                                        // 3. Copiar link al portapapeles
                                        OutlinedButton(
                                            onClick = {
                                                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                                clipboard.setPrimaryClip(android.content.ClipData.newPlainText("Link Actualización C.S.B.", activeVerifiedUrl))
                                                android.widget.Toast.makeText(context, "✅ Link verídico copiado al portapapeles", android.widget.Toast.LENGTH_SHORT).show()
                                                successMessage = "📋 Link copiado: $activeVerifiedUrl"
                                            },
                                            border = BorderStroke(1.dp, Color(0xFF00F0FF)),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00F0FF)),
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                                            modifier = Modifier.testTag("btn_copy_verified_update_link")
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = "Copiar link", modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    viewModel.broadcastNewVersionToAllPhonesNow(
                                        versionLabel = otaVersionInput,
                                        releaseNotes = otaNotesInput,
                                        downloadUrl = activeVerifiedUrl
                                    ) { _, msg ->
                                        successMessage = "⚡ $msg"
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00F0FF),
                                    contentColor = Color(0xFF020617)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_broadcast_new_version_all_phones")
                            ) {
                                Icon(Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "⚡ Autorizar y Auto-Actualizar Todos los Celulares Ahora",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.2.sp
                                )
                            }
                        }
                    }
                }
            }

            // Navegador Superior por Puntitos y Secciones con Nombre ("arriba por puntitos en los cuales yo los pueda encontrar muy bien")
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("curia_top_dots_navigation_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF071326)),
                    border = BorderStroke(1.5.dp, Color(0xFF00F0FF).copy(alpha = 0.65f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val categories = listOf(
                            0 to "Todo el Panel",
                            6 to "Seguridad & Claves (${intrusionAlerts.size})",
                            5 to "Permisos & Correos (${basilianAccessRequests.count { it.status == "PENDIENTE" }})",
                            1 to "IA Control Total",
                            2 to "Escudo & Fotos",
                            3 to "Oración & Avisos",
                            4 to "Nube & Descarga"
                        )
                        val activeLabel = categories.firstOrNull { it.first == selectedAdminCategory }?.second ?: "Todo el Panel"

                        Text(
                            text = "••• NAVEGACIÓN RÁPIDA POR PUNTITOS • $activeLabel •••",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF00F0FF),
                            letterSpacing = 0.7.sp,
                            textAlign = TextAlign.Center
                        )

                        // Hilera Superior de Puntitos Interactivos (toca cualquier puntito para saltar de sección)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            categories.forEachIndexed { index, (catId, catName) ->
                                val isSelected = selectedAdminCategory == catId
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                        .clickable { selectedAdminCategory = catId }
                                        .testTag("admin_dot_nav_$catId")
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(if (isSelected) 28.dp else 20.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) ShieldGold else Color(0xFF00F0FF).copy(alpha = 0.22f)
                                            )
                                            .border(
                                                width = if (isSelected) 2.dp else 1.dp,
                                                color = if (isSelected) Color.White else Color(0xFF00F0FF).copy(alpha = 0.65f),
                                                shape = CircleShape
                                            )
                                    ) {
                                        Text(
                                            text = "${index + 1}",
                                            fontSize = if (isSelected) 11.sp else 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isSelected) Color(0xFF020617) else Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = catName.substringBefore(" "),
                                        fontSize = 8.5.sp,
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                        color = if (isSelected) ShieldGold else Color(0xFFBAE6FD)
                                    )
                                }
                            }
                        }

                        // Etiquetas con nombre completo debajo de los puntitos
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categories.forEach { (idx, label) ->
                                val selected = selectedAdminCategory == idx
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = if (selected) ShieldGold else Color.White.copy(alpha = 0.08f),
                                    border = BorderStroke(1.dp, if (selected) ShieldGold else Color(0xFF00F0FF).copy(alpha = 0.3f)),
                                    modifier = Modifier.clickable { selectedAdminCategory = idx }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(7.dp)
                                                .clip(CircleShape)
                                                .background(if (selected) Color(0xFF020617) else Color(0xFF00F0FF))
                                        )
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) Color(0xFF0F172A) else Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // BÓVEDA PRIVADA DE CONTRASEÑAS («SOLO PARA MÍ») & SISTEMA ANTI-HACKEO
            // =========================================================================
            if (selectedAdminCategory == 0 || selectedAdminCategory == 6 || selectedAdminCategory == 5) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_private_password_vault_and_antihack"),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF051024)),
                        border = BorderStroke(
                            width = 1.8.dp,
                            color = if (intrusionAlerts.isNotEmpty()) Color(0xFFEF4444) else Color(0xFF10B981)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Encabezado del Escudo Anti-Hackeo
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (intrusionAlerts.isNotEmpty()) Color(0xFFEF4444).copy(alpha = 0.2f) else Color(0xFF10B981).copy(alpha = 0.2f),
                                    border = BorderStroke(1.5.dp, if (intrusionAlerts.isNotEmpty()) Color(0xFFEF4444) else Color(0xFF10B981)),
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (intrusionAlerts.isNotEmpty()) Icons.Default.GppBad else Icons.Default.Shield,
                                            contentDescription = null,
                                            tint = if (intrusionAlerts.isNotEmpty()) Color(0xFFF87171) else Color(0xFF34D399),
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "🛡️ Bóveda Privada de Contraseñas & Escudo Anti-Hackeo",
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "Espacio exclusivo «Solo para Mí». Tus contraseñas están guardadas aquí cifradas con AES-256-GCM para que jamás se te olviden y nadie externo pueda verlas.",
                                        color = Color(0xFFBAE6FD),
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            // Interruptor de Blindaje Máximo Anti-Intrusos
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF0A1E3C),
                                border = BorderStroke(1.dp, Color(0xFF34D399).copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Blindaje Máximo Anti-Hackeo (Activo 24/7)",
                                            color = Color(0xFF34D399),
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.5.sp
                                        )
                                        Text(
                                            text = "Verificación criptográfica en tiempo constante + bloqueo automático tras 2 intentos + notificación inmediata al celular si alguien intenta ingresar.",
                                            color = Color(0xFFCBD5E1),
                                            fontSize = 10.5.sp,
                                            lineHeight = 14.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Switch(
                                        checked = maxSecurityShieldEnabled,
                                        onCheckedChange = {
                                            viewModel.setMaxSecurityShieldEnabled(it)
                                            successMessage = if (it) {
                                                "🛡️ Blindaje Máximo Anti-Hackeo activado."
                                            } else {
                                                "🛡️ Blindaje configurado en modo estándar."
                                            }
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = Color(0xFF10B981)
                                        )
                                    )
                                }
                            }

                            // CONTROL EXCLUSIVO DEL LÍDER CREADOR Y APP COMPARTIDA (NOTIFICACIONES SOLO A MÍ)
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF0B2244),
                                border = BorderStroke(1.2.dp, Color(0xFFF59E0B).copy(alpha = 0.75f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AdminPanelSettings,
                                            contentDescription = null,
                                            tint = Color(0xFFFBBF24),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "👑 Único Líder Creador: villamizargalviz@gmail.com",
                                                color = Color(0xFFFDE68A),
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 13.sp
                                            )
                                            Text(
                                                text = "Este correo (villamizargalviz@gmail.com) es el ÚNICO Líder autorizado. Todas las notificaciones de intento de hackeo o acceso no autorizado llegan exclusivamente a este correo, y solo este correo tiene acceso total a «Solo para Mí».",
                                                color = Color(0xFFBAE6FD),
                                                fontSize = 10.5.sp,
                                                lineHeight = 14.sp
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = "villamizargalviz@gmail.com",
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Único Correo Líder Autorizado (Protegido)", fontSize = 11.sp, color = Color(0xFFFDE68A)) },
                                            singleLine = true,
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White,
                                                focusedBorderColor = Color(0xFFF59E0B),
                                                unfocusedBorderColor = Color(0xFFF59E0B).copy(alpha = 0.65f)
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Button(
                                            onClick = {
                                                viewModel.setLeaderCreatorEmail("villamizargalviz@gmail.com")
                                                successMessage = "👑 Blindado: villamizargalviz@gmail.com confirmado como único Líder."
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFF59E0B),
                                                contentColor = Color(0xFF020617)
                                            ),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("Blindado", fontWeight = FontWeight.ExtraBold, fontSize = 11.5.sp)
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF06152D),
                                        border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "📲 Modo App Compartida Activo: La llavecita «Solo para Mí» y las alertas de seguridad solo aparecen cuando ingresas con el único correo Líder (villamizargalviz@gmail.com). A los demás basilianos y usuarios jamás les aparece. Cada persona tiene su propia Clave Personal para que nadie suplante el correo de otro.",
                                                color = Color(0xFFE0F2FE),
                                                fontSize = 10.5.sp,
                                                lineHeight = 14.sp
                                            )
                                            var resetTargetEmail by remember { mutableStateOf("") }
                                            var resetNewPass by remember { mutableStateOf("") }
                                            Text(
                                                text = "🔑 Restablecer Clave Personal de un Hermano (si olvidó su contraseña):",
                                                color = Color(0xFFFDE68A),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                OutlinedTextField(
                                                    value = resetTargetEmail,
                                                    onValueChange = { resetTargetEmail = it },
                                                    label = { Text("Correo del hermano", fontSize = 10.sp, color = Color(0xFFBAE6FD)) },
                                                    singleLine = true,
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        focusedTextColor = Color.White,
                                                        unfocusedTextColor = Color.White,
                                                        focusedBorderColor = Color(0xFF38BDF8),
                                                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                                                    ),
                                                    modifier = Modifier.weight(1f)
                                                )
                                                OutlinedTextField(
                                                    value = resetNewPass,
                                                    onValueChange = { resetNewPass = it },
                                                    label = { Text("Nueva clave (o vacío)", fontSize = 10.sp, color = Color(0xFFFDE68A)) },
                                                    singleLine = true,
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        focusedTextColor = Color.White,
                                                        unfocusedTextColor = Color.White,
                                                        focusedBorderColor = Color(0xFFF59E0B),
                                                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                                                    ),
                                                    modifier = Modifier.weight(0.9f)
                                                )
                                                Button(
                                                    onClick = {
                                                        if (viewModel.resetUserAccountPasswordByLeader(resetTargetEmail, resetNewPass)) {
                                                            successMessage = if (resetNewPass.trim().length >= 4) {
                                                                "🔐 Clave restablecida para ${resetTargetEmail.trim().lowercase()}"
                                                            } else {
                                                                "🔓 Clave liberada para que ${resetTargetEmail.trim().lowercase()} cree una nueva al entrar"
                                                            }
                                                            resetTargetEmail = ""
                                                            resetNewPass = ""
                                                        }
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color(0xFF38BDF8),
                                                        contentColor = Color(0xFF020617)
                                                    ),
                                                    shape = RoundedCornerShape(8.dp),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                                                ) {
                                                    Text("Aplicar", fontWeight = FontWeight.ExtraBold, fontSize = 10.5.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // MONITOR DE ALERTAS DE INTRUSIÓN EN TIEMPO REAL
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (intrusionAlerts.isNotEmpty()) Color(0xFF450A0A).copy(alpha = 0.85f) else Color(0xFF064E3B).copy(alpha = 0.35f),
                                border = BorderStroke(
                                    1.dp,
                                    if (intrusionAlerts.isNotEmpty()) Color(0xFFF87171) else Color(0xFF34D399).copy(alpha = 0.4f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = if (intrusionAlerts.isNotEmpty()) Icons.Default.WarningAmber else Icons.Default.VerifiedUser,
                                                contentDescription = null,
                                                tint = if (intrusionAlerts.isNotEmpty()) Color(0xFFFCA5A5) else Color(0xFF6EE7B7),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                text = if (intrusionAlerts.isNotEmpty()) {
                                                    "🚨 ¡ALERTA! ${intrusionAlerts.size} Intento(s) de Ingreso No Autorizado"
                                                } else {
                                                    "✅ Sin intentos de intrusión • Sistema 100% Seguro"
                                                },
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 12.5.sp
                                            )
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                viewModel.triggerTestIntrusionAlertNotification()
                                                successMessage = "🔔 Aviso de seguridad enviado a tu barra de notificaciones."
                                            },
                                            border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFDE68A)),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text("Probar Aviso", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    if (intrusionAlerts.isNotEmpty()) {
                                        Text(
                                            text = "Alguien intentó ingresar con una clave incorrecta y fue bloqueado. Te recomendamos cambiar tu contraseña abajo inmediatamente:",
                                            color = Color(0xFFFECACA),
                                            fontSize = 11.sp
                                        )
                                        intrusionAlerts.take(5).forEach { alert ->
                                            val dateFormatted = remember(alert.timestamp) {
                                                java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault())
                                                    .format(java.util.Date(alert.timestamp))
                                            }
                                            Surface(
                                                shape = RoundedCornerShape(10.dp),
                                                color = Color.Black.copy(alpha = 0.35f),
                                                border = BorderStroke(1.dp, Color(0xFFF87171).copy(alpha = 0.4f)),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(modifier = Modifier.padding(10.dp)) {
                                                    Text(
                                                        text = "📍 ${alert.targetArea} • $dateFormatted",
                                                        color = Color(0xFFFDE68A),
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 11.sp
                                                    )
                                                    Text(
                                                        text = "Usuario/Origen: ${alert.actorInfo}",
                                                        color = Color.White,
                                                        fontSize = 11.sp
                                                    )
                                                    Text(
                                                        text = "Estado: ${alert.blockedStatus}",
                                                        color = Color(0xFFFCA5A5),
                                                        fontSize = 10.5.sp,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                }
                                            }
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TextButton(
                                                onClick = {
                                                    viewModel.clearIntrusionAlerts()
                                                    successMessage = "✅ Historial de alertas de intrusión limpiado."
                                                }
                                            ) {
                                                Text("Limpiar avisos", color = Color(0xFFFCA5A5), fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }

                            HorizontalDivider(color = Color.White.copy(alpha = 0.14f))

                            // -----------------------------------------------------------------
                            // 1. CLAVE PRINCIPAL «SOLO PARA MÍ» (GUARDADA Y OCULTA CON OJO)
                            // -----------------------------------------------------------------
                            Text(
                                text = "🔑 Mis Contraseñas Guardadas (Solo visibles para mí con el ícono del ojo)",
                                color = ShieldGold,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF0B1930),
                                border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.55f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "1. Contraseña Principal «Solo para Mí»",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = if (revealSavedMasterPass) curiaMasterPassword else "••••••••••••",
                                                color = if (revealSavedMasterPass) Color(0xFF4ADE80) else Color(0xFF94A3B8),
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 14.sp
                                            )
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            IconButton(
                                                onClick = { revealSavedMasterPass = !revealSavedMasterPass }
                                            ) {
                                                Icon(
                                                    imageVector = if (revealSavedMasterPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                    contentDescription = "Ver u ocultar mi contraseña guardada",
                                                    tint = ShieldGold
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    clipboardManager.setText(AnnotatedString(curiaMasterPassword))
                                                    successMessage = "✅ Contraseña principal copiada al portapapeles."
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copiar contraseña",
                                                    tint = Color(0xFF00F0FF)
                                                )
                                            }
                                        }
                                    }

                                    // Espacio directo para cambiar la contraseña principal
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = inlineNewMasterPassword,
                                            onValueChange = { inlineNewMasterPassword = it },
                                            label = { Text("Cambiar Contraseña «Solo para Mí»", fontSize = 11.sp, color = ShieldGold) },
                                            placeholder = { Text("Nueva clave secreta", fontSize = 11.sp, color = Color.Gray) },
                                            visualTransformation = if (inlineNewMasterPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                            trailingIcon = {
                                                IconButton(onClick = { inlineNewMasterPassVisible = !inlineNewMasterPassVisible }) {
                                                    Icon(
                                                        imageVector = if (inlineNewMasterPassVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                        contentDescription = null,
                                                        tint = Color.LightGray
                                                    )
                                                }
                                            },
                                            singleLine = true,
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White,
                                                focusedBorderColor = ShieldGold,
                                                unfocusedBorderColor = Color.White.copy(alpha = 0.28f)
                                            ),
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("input_change_master_password_vault")
                                        )
                                        Button(
                                            onClick = {
                                                val clean = inlineNewMasterPassword.trim()
                                                if (clean.length >= 4) {
                                                    viewModel.setCuriaMasterPassword(clean)
                                                    inlineNewMasterPassword = ""
                                                    successMessage = "🔐 ¡Nueva contraseña principal guardada en tu bóveda privada «Solo para Mí»!"
                                                } else {
                                                    errorMessage = "La contraseña debe tener al menos 4 caracteres."
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = ShieldGold,
                                                contentColor = Color(0xFF020617)
                                            )
                                        ) {
                                            Text("Guardar", fontWeight = FontWeight.ExtraBold, fontSize = 11.5.sp)
                                        }
                                    }
                                }
                            }

                            // -----------------------------------------------------------------
                            // 2. CÓDIGO COMUNITARIO BASILIANO C.S.B. (GUARDADO Y OCULTO CON OJO)
                            // -----------------------------------------------------------------
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF0B1930),
                                border = BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.45f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "2. Código de Desbloqueo Basiliano C.S.B. (Cumpleaños / Difuntos)",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = if (revealSavedCsbCode) basilianCommunityUnlockCode else "••••••••••••",
                                                color = if (revealSavedCsbCode) Color(0xFF38BDF8) else Color(0xFF94A3B8),
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 14.sp
                                            )
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            IconButton(
                                                onClick = { revealSavedCsbCode = !revealSavedCsbCode }
                                            ) {
                                                Icon(
                                                    imageVector = if (revealSavedCsbCode) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                    contentDescription = "Ver u ocultar código C.S.B.",
                                                    tint = Color(0xFF00F0FF)
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    clipboardManager.setText(AnnotatedString(basilianCommunityUnlockCode))
                                                    successMessage = "✅ Código C.S.B. copiado al portapapeles."
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copiar código C.S.B.",
                                                    tint = Color(0xFF00F0FF)
                                                )
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = inlineNewCsbCode,
                                            onValueChange = { inlineNewCsbCode = it },
                                            label = { Text("Cambiar Código C.S.B.", fontSize = 11.sp, color = Color(0xFF00F0FF)) },
                                            placeholder = { Text("Nuevo código C.S.B.", fontSize = 11.sp, color = Color.Gray) },
                                            visualTransformation = if (inlineNewCsbCodeVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                            trailingIcon = {
                                                IconButton(onClick = { inlineNewCsbCodeVisible = !inlineNewCsbCodeVisible }) {
                                                    Icon(
                                                        imageVector = if (inlineNewCsbCodeVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                        contentDescription = null,
                                                        tint = Color.LightGray
                                                    )
                                                }
                                            },
                                            singleLine = true,
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White,
                                                focusedBorderColor = Color(0xFF00F0FF),
                                                unfocusedBorderColor = Color.White.copy(alpha = 0.28f)
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Button(
                                            onClick = {
                                                val clean = inlineNewCsbCode.trim()
                                                if (clean.length >= 4) {
                                                    viewModel.setBasilianCommunityUnlockCode(clean)
                                                    inlineNewCsbCode = ""
                                                    successMessage = "🔐 ¡Nuevo código C.S.B. actualizado y guardado en tu espacio privado!"
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF00F0FF),
                                                contentColor = Color(0xFF020617)
                                            )
                                        ) {
                                            Text("Guardar", fontWeight = FontWeight.ExtraBold, fontSize = 11.5.sp)
                                        }
                                    }
                                }
                            }

                            // -----------------------------------------------------------------
                            // 2B. PIN RÁPIDO NUMÉRICO & CIERRE AUTOMÁTICO POR INACTIVIDAD
                            // -----------------------------------------------------------------
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF0B1930),
                                border = BorderStroke(1.dp, Color(0xFF34D399).copy(alpha = 0.45f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "3. PIN Rápido Numérico (Ingreso alternativo a Solo para Mí)",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = if (revealSavedQuickPin) ownerQuickPin else "••••••",
                                                color = if (revealSavedQuickPin) Color(0xFF34D399) else Color(0xFF94A3B8),
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 14.sp
                                            )
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            IconButton(
                                                onClick = { revealSavedQuickPin = !revealSavedQuickPin }
                                            ) {
                                                Icon(
                                                    imageVector = if (revealSavedQuickPin) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                    contentDescription = "Ver u ocultar PIN Rápido",
                                                    tint = Color(0xFF34D399)
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    clipboardManager.setText(AnnotatedString(ownerQuickPin))
                                                    successMessage = "✅ PIN Rápido copiado al portapapeles."
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copiar PIN Rápido",
                                                    tint = Color(0xFF00F0FF)
                                                )
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = inlineNewQuickPin,
                                            onValueChange = { inlineNewQuickPin = it.filter { ch -> ch.isDigit() }.take(8) },
                                            label = { Text("Cambiar PIN Rápido (4-8 dígitos)", fontSize = 11.sp, color = Color(0xFF34D399)) },
                                            placeholder = { Text("Ej: 4 a 8 números", fontSize = 11.sp, color = Color.Gray) },
                                            visualTransformation = if (inlineNewQuickPinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                            trailingIcon = {
                                                IconButton(onClick = { inlineNewQuickPinVisible = !inlineNewQuickPinVisible }) {
                                                    Icon(
                                                        imageVector = if (inlineNewQuickPinVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                        contentDescription = null,
                                                        tint = Color.LightGray
                                                    )
                                                }
                                            },
                                            singleLine = true,
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White,
                                                focusedBorderColor = Color(0xFF34D399),
                                                unfocusedBorderColor = Color.White.copy(alpha = 0.28f)
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Button(
                                            onClick = {
                                                val clean = inlineNewQuickPin.trim()
                                                if (clean.length in 4..8 && clean.all { it.isDigit() }) {
                                                    viewModel.setOwnerQuickPin(clean)
                                                    inlineNewQuickPin = ""
                                                    successMessage = "🔢 ¡PIN Rápido actualizado y cifrado en tu bóveda privada!"
                                                } else {
                                                    errorMessage = "El PIN Rápido debe tener entre 4 y 8 números."
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF34D399),
                                                contentColor = Color(0xFF020617)
                                            )
                                        ) {
                                            Text("Guardar", fontWeight = FontWeight.ExtraBold, fontSize = 11.5.sp)
                                        }
                                    }

                                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                                    Text(
                                        text = "⏱️ Bloqueo Automático por Inactividad (${vaultAutoLockMinutes} min):",
                                        color = Color(0xFFFDE68A),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.5.sp
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        listOf(2, 5, 15).forEach { mins ->
                                            val isSelected = vaultAutoLockMinutes == mins
                                            FilterChip(
                                                selected = isSelected,
                                                onClick = {
                                                    viewModel.setVaultAutoLockMinutes(mins)
                                                    successMessage = "⏱️ Bloqueo automático configurado en $mins minutos."
                                                },
                                                label = {
                                                    Text("$mins min", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = ShieldGold.copy(alpha = 0.25f),
                                                    selectedLabelColor = ShieldGold,
                                                    containerColor = Color(0xFF051024),
                                                    labelColor = Color.LightGray
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        OutlinedButton(
                                            onClick = { viewModel.logoutCuria() },
                                            border = BorderStroke(1.dp, Color(0xFFF87171)),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF87171)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Bloquear ya", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }

                            // -----------------------------------------------------------------
                            // 3. GUARDAR OTRAS CONTRASEÑAS PERSONALES PARA QUE NO SE OLVIDEN
                            // -----------------------------------------------------------------
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF0B1930),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = "3. Guardar otra contraseña o clave en mi espacio privado (para no olvidarla)",
                                        color = Color(0xFFE2E8F0),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = customNoteLabelInput,
                                            onValueChange = { customNoteLabelInput = it },
                                            label = { Text("Nombre (ej: Correo Curia)", fontSize = 10.5.sp, color = Color.LightGray) },
                                            singleLine = true,
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White,
                                                focusedBorderColor = ShieldGold,
                                                unfocusedBorderColor = Color.White.copy(alpha = 0.25f)
                                            ),
                                            modifier = Modifier.weight(0.9f)
                                        )
                                        OutlinedTextField(
                                            value = customNoteSecretInput,
                                            onValueChange = { customNoteSecretInput = it },
                                            label = { Text("Contraseña", fontSize = 10.5.sp, color = Color.LightGray) },
                                            visualTransformation = if (customNoteSecretVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                            trailingIcon = {
                                                IconButton(onClick = { customNoteSecretVisible = !customNoteSecretVisible }) {
                                                    Icon(
                                                        imageVector = if (customNoteSecretVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                        contentDescription = null,
                                                        tint = Color.LightGray
                                                    )
                                                }
                                            },
                                            singleLine = true,
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White,
                                                focusedBorderColor = ShieldGold,
                                                unfocusedBorderColor = Color.White.copy(alpha = 0.25f)
                                            ),
                                            modifier = Modifier.weight(1.1f)
                                        )
                                        Button(
                                            onClick = {
                                                if (customNoteSecretInput.trim().isNotEmpty()) {
                                                    viewModel.savePersonalPasswordToVault(
                                                        label = customNoteLabelInput.ifBlank { "Clave Personal" },
                                                        secretValue = customNoteSecretInput.trim()
                                                    )
                                                    customNoteLabelInput = ""
                                                    customNoteSecretInput = ""
                                                    successMessage = "🔐 Clave guardada bajo llave en «Solo para Mí»."
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF10B981),
                                                contentColor = Color.White
                                            )
                                        ) {
                                            Text("Añadir", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                        }
                                    }

                                    personalPasswordVault.forEach { item ->
                                        val isRevealed = item.id in revealedVaultNoteIds
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = Color.Black.copy(alpha = 0.28f),
                                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
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
                                                        text = item.label,
                                                        color = ShieldGold,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 11.5.sp
                                                    )
                                                    Text(
                                                        text = if (isRevealed) item.secretValue else "••••••••••••",
                                                        color = if (isRevealed) Color(0xFF4ADE80) else Color(0xFF94A3B8),
                                                        fontWeight = FontWeight.ExtraBold,
                                                        fontSize = 13.sp
                                                    )
                                                }
                                                Row {
                                                    IconButton(
                                                        onClick = {
                                                            revealedVaultNoteIds = if (isRevealed) {
                                                                revealedVaultNoteIds - item.id
                                                            } else {
                                                                revealedVaultNoteIds + item.id
                                                            }
                                                        }
                                                    ) {
                                                        Icon(
                                                            imageVector = if (isRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                            contentDescription = "Ver u ocultar clave",
                                                            tint = Color.White
                                                        )
                                                    }
                                                    IconButton(
                                                        onClick = {
                                                            clipboardManager.setText(AnnotatedString(item.secretValue))
                                                            successMessage = "✅ Clave «${item.label}» copiada."
                                                        }
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.ContentCopy,
                                                            contentDescription = "Copiar clave",
                                                            tint = Color(0xFF00F0FF)
                                                        )
                                                    }
                                                    IconButton(
                                                        onClick = {
                                                            viewModel.deletePersonalPasswordFromVault(item.id)
                                                        }
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.DeleteOutline,
                                                            contentDescription = "Eliminar clave",
                                                            tint = Color(0xFFF87171)
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

            // Tarjeta Exclusiva: Agregar Nuevos Correos de Padres Basilianos (Verificación Inmediata al Ingresar)
            if (selectedAdminCategory == 0 || selectedAdminCategory == 5 || selectedAdminCategory == 6) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_register_new_basilian_emails"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF062019)),
                        border = BorderStroke(1.5.dp, Color(0xFF10B981)),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF10B981).copy(alpha = 0.22f),
                                    border = BorderStroke(1.dp, Color(0xFF34D399)),
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.VerifiedUser,
                                            contentDescription = null,
                                            tint = Color(0xFF34D399),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "📧 Nuevos Correos de Padres Basilianos (Acceso Completo Inmediato)",
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "Registra aquí los correos nuevos o actualizados de los Padres y Hermanos Basilianos. En cuanto el Padre escriba su correo al entrar a la app, el sistema lo verificará de inmediato como Basiliano con acceso total.",
                                        color = Color(0xFFD1FAE5),
                                        fontSize = 11.5.sp,
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = newBasilianFatherNameInput,
                                onValueChange = { newBasilianFatherNameInput = it },
                                label = { Text("Nombre del Padre o Hermano C.S.B. (Opcional)", fontSize = 11.5.sp, color = Color(0xFF6EE7B7)) },
                                placeholder = { Text("Ej: P. Gabriel Villamizar, C.S.B.", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.45f)) },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF34D399))
                                },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFF34D399),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_new_basilian_father_name")
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = newBasilianEmailInput,
                                    onValueChange = { newBasilianEmailInput = it },
                                    label = { Text("Nuevo Correo del Padre Basiliano *", fontSize = 11.5.sp, color = Color(0xFF6EE7B7)) },
                                    placeholder = { Text("padre.nuevo@gmail.com", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.45f)) },
                                    leadingIcon = {
                                        Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF34D399))
                                    },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = Color(0xFF34D399),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_new_basilian_email")
                                )
                                Button(
                                    onClick = {
                                        val cleanMail = newBasilianEmailInput.trim().lowercase()
                                        val cleanName = newBasilianFatherNameInput.trim()
                                        if (viewModel.addRegisteredBasilianEmail(cleanMail, cleanName)) {
                                            newBasilianEmailInput = ""
                                            newBasilianFatherNameInput = ""
                                            successMessage = "✅ Correo $cleanMail guardado. Cuando el Padre lo ingrese será verificado de inmediato como Basiliano C.S.B."
                                        } else {
                                            errorMessage = "Por favor escribe un correo electrónico válido (con @ y dominio)."
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF10B981),
                                        contentColor = Color(0xFF022C22)
                                    ),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
                                    modifier = Modifier.testTag("btn_save_new_basilian_email")
                                ) {
                                    Text("Guardar Correo", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }

                            if (registeredBasilianEmails.isNotEmpty()) {
                                Text(
                                    text = "📋 Correos Basilianos Registrados para Verificación Inmediata (${registeredBasilianEmails.size}):",
                                    color = Color(0xFFA7F3D0),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                registeredBasilianEmails.forEach { entry ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color.Black.copy(alpha = 0.32f),
                                        border = BorderStroke(1.dp, Color(0xFF34D399).copy(alpha = 0.45f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 9.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = entry.fatherName.ifBlank { "Padre / Hermano Basiliano C.S.B." },
                                                    color = Color.White,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 13.sp
                                                )
                                                Text(
                                                    text = "${entry.email} • Verificación Inmediata Activa",
                                                    color = Color(0xFF6EE7B7),
                                                    fontSize = 11.5.sp
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    viewModel.removeRegisteredBasilianEmail(entry.email)
                                                    successMessage = "🗑️ Correo ${entry.email} removido de la lista."
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.DeleteOutline,
                                                    contentDescription = "Eliminar correo registrado",
                                                    tint = Color(0xFFF87171)
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

            // Tarjeta de Notificaciones de Acceso Basiliano C.S.B. & Control de Permisos
            if (selectedAdminCategory == 0 || selectedAdminCategory == 5) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_basilian_access_notifications"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF07152B)),
                        border = BorderStroke(1.5.dp, Color(0xFF00F0FF).copy(alpha = 0.7f)),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFF59E0B).copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.NotificationsActive,
                                            contentDescription = null,
                                            tint = Color(0xFFF59E0B),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "🔔 Notificaciones de Acceso Basiliano C.S.B.",
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "Autoriza a quienes son religiosos basilianos (acceso completo a Regla, datos internos e IA comunitaria) o mantén en modo público a visitantes.",
                                        color = Color(0xFFBAE6FD),
                                        fontSize = 11.sp,
                                        lineHeight = 14.sp
                                    )
                                }
                            }

                            // Autorización manual rápida por correo
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = manualApproveEmailInput,
                                    onValueChange = { manualApproveEmailInput = it },
                                    label = { Text("Correo a autorizar o restringir", fontSize = 11.sp, color = Color(0xFF00F0FF)) },
                                    placeholder = { Text("hermano@correo.com", fontSize = 11.sp, color = Color.White.copy(alpha = 0.4f)) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = Color(0xFF00F0FF),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                Button(
                                    onClick = {
                                        val mail = manualApproveEmailInput.trim().lowercase()
                                        if (mail.contains("@")) {
                                            viewModel.approveBasilianAccessRequest(mail)
                                            manualApproveEmailInput = ""
                                            successMessage = "✅ Correo $mail autorizado como Religioso Basiliano C.S.B."
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF10B981),
                                        contentColor = Color.White
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp)
                                ) {
                                    Text("Aprobar C.S.B.", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            if (basilianAccessRequests.isEmpty()) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White.copy(alpha = 0.06f),
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "No hay solicitudes pendientes en este momento. Cuando un usuario ingrese solicitando rol de Novicio, Escolástico o Sacerdote Basiliano, aparecerá aquí tu notificación para autorizarlo.",
                                        color = Color(0xFFBAE6FD),
                                        fontSize = 11.5.sp,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            } else {
                                basilianAccessRequests.forEach { req ->
                                    val isPending = req.status == "PENDIENTE"
                                    val isApproved = req.status == "APROBADO_BASILIANO"
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = when {
                                            isPending -> Color(0xFFF59E0B).copy(alpha = 0.14f)
                                            isApproved -> Color(0xFF064E3B).copy(alpha = 0.45f)
                                            else -> Color.White.copy(alpha = 0.06f)
                                        },
                                        border = BorderStroke(
                                            1.dp,
                                            when {
                                                isPending -> Color(0xFFF59E0B)
                                                isApproved -> Color(0xFF34D399)
                                                else -> Color.White.copy(alpha = 0.25f)
                                            }
                                        ),
                                        modifier = Modifier.fillMaxWidth()
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
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = "${req.fullName} • ${req.requestedRole}",
                                                        color = Color.White,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        fontSize = 13.sp
                                                    )
                                                    Text(
                                                        text = "${req.email} • ${req.city}",
                                                        color = Color(0xFF00F0FF),
                                                        fontSize = 11.sp
                                                    )
                                                }
                                                Surface(
                                                    shape = RoundedCornerShape(50),
                                                    color = when {
                                                        isPending -> Color(0xFFF59E0B)
                                                        isApproved -> Color(0xFF10B981)
                                                        else -> Color(0xFF0284C7)
                                                    }
                                                ) {
                                                    Text(
                                                        text = when {
                                                            isPending -> "PENDIENTE"
                                                            isApproved -> "BASILIANO C.S.B."
                                                            else -> "MODO PÚBLICO"
                                                        },
                                                        color = Color(0xFF020617),
                                                        fontWeight = FontWeight.ExtraBold,
                                                        fontSize = 9.5.sp,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                    )
                                                }
                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Button(
                                                    onClick = {
                                                        viewModel.approveBasilianAccessRequest(req.email)
                                                        successMessage = "✅ Acceso Basiliano C.S.B. concedido a ${req.fullName} (${req.email})"
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color(0xFF10B981),
                                                        contentColor = Color.White
                                                    ),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(15.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Autorizar como Basiliano", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                                }

                                                OutlinedButton(
                                                    onClick = {
                                                        viewModel.setBasilianUserAsPublicVisitor(req.email)
                                                        successMessage = "🌐 ${req.fullName} asignado al apartado público de Espiritualidad."
                                                    },
                                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFBBF24)),
                                                    border = BorderStroke(1.dp, Color(0xFFFBBF24)),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Icon(Icons.Default.Public, contentDescription = null, modifier = Modifier.size(15.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Dejar en Modo Público", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
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

            if (successMessage != null) {
                item {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ShieldGreen.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, ShieldGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ShieldGreen, modifier = Modifier.size(20.dp))
                                Text(text = successMessage!!, style = MaterialTheme.typography.bodySmall, color = ShieldGreenDark, fontWeight = FontWeight.SemiBold)
                            }
                            IconButton(onClick = { successMessage = null }) {
                                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = ShieldGreenDark, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // 1. SÚPER IA DE AUTOMEJORA, TÍTULOS, PESTAÑAS Y SECCIONES PERSONALIZADAS
            // =========================================================================
            if (selectedAdminCategory == 0 || selectedAdminCategory == 1) {
            item {
                var selectedAiModuleDot by remember { mutableIntStateOf(0) }
                val aiModuleCategories = listOf(
                    "⚡ Todo / Comando Directo",
                    "🔐 Accesos y Correos",
                    "🏛️ Títulos, Oración y Avisos",
                    "🎨 Fondos 3D y Pestañas",
                    "🤖 Saludo y Entrenamiento IA"
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_curia_ai_app_modifier"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF050F22)),
                    border = BorderStroke(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF00F0FF), ShieldGold, Color(0xFF38BDF8))
                        )
                    ),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF00F0FF).copy(alpha = 0.18f),
                                    border = BorderStroke(1.5.dp, Color(0xFF00F0FF)),
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = Color(0xFF00F0FF),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "IA Maestra Privada • Control Total",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF00F0FF)
                                    )
                                    Text(
                                        text = "Edita, autoriza correos y sincroniza toda la app en vivo",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ShieldGoldLight
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ShieldGold.copy(alpha = 0.22f),
                                border = BorderStroke(1.dp, ShieldGold)
                            ) {
                                Text(
                                    text = liveAppVersion,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ShieldGoldLight,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 9.5.sp
                                )
                            }
                        }

                        // Selector por puntitos dentro de la IA Privada para encontrar cada función al instante
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF0A192F),
                            border = BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.3f)),
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
                                    Text(
                                        text = "Módulos de la IA Privada (Toca un punto):",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ShieldGoldLight
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        aiModuleCategories.forEachIndexed { idx, _ ->
                                            val active = selectedAiModuleDot == idx
                                            Box(
                                                modifier = Modifier
                                                    .size(if (active) 10.dp else 7.dp)
                                                    .clip(CircleShape)
                                                    .background(if (active) Color(0xFF00F0FF) else Color.White.copy(alpha = 0.3f))
                                                    .clickable { selectedAiModuleDot = idx }
                                            )
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    aiModuleCategories.forEachIndexed { idx, label ->
                                        val active = selectedAiModuleDot == idx
                                        Surface(
                                            onClick = { selectedAiModuleDot = idx },
                                            shape = RoundedCornerShape(50),
                                            color = if (active) Color(0xFF00F0FF).copy(alpha = 0.22f) else Color.White.copy(alpha = 0.05f),
                                            border = BorderStroke(1.dp, if (active) Color(0xFF00F0FF) else Color.White.copy(alpha = 0.18f))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(6.dp)
                                                        .clip(CircleShape)
                                                        .background(if (active) ShieldGold else Color.Gray)
                                                )
                                                Text(
                                                    text = label,
                                                    fontSize = 11.sp,
                                                    fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Medium,
                                                    color = if (active) Color.White else Color.White.copy(alpha = 0.75f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Cuadro de orden directa en lenguaje natural para modificar cualquier parte de la aplicación
                        OutlinedTextField(
                            value = appUpdateRequestInput,
                            onValueChange = { appUpdateRequestInput = it },
                            label = { Text("Escribe cualquier cambio que quieras aplicar en la App", color = ShieldGoldLight) },
                            placeholder = {
                                Text(
                                    "Ej: Autorizar correo p.juan@basilianos.org como Basiliano • Cambiar título a Congregación de San Basilio • Actualizar oración diaria • Publicar aviso...",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF00F0FF),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.35f),
                                cursorColor = Color(0xFF00F0FF)
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            shape = RoundedCornerShape(14.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (appUpdateRequestInput.isNotBlank()) {
                                        viewModel.executeAiAppCommand(appUpdateRequestInput)
                                        successMessage = "⚡ ¡Orden ejecutada y sincronizada en todos los dispositivos!"
                                    }
                                },
                                enabled = !isAppUpdateGenerating && appUpdateRequestInput.isNotBlank(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00F0FF),
                                    contentColor = Color(0xFF020617)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("btn_generate_app_update_private")
                            ) {
                                if (isAppUpdateGenerating) {
                                    CircularProgressIndicator(
                                        color = Color(0xFF020617),
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Aplicando...", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                                } else {
                                    Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Ejecutar Cambio Ahora",
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.runSuperAiAppSelfUpgrade(appUpdateRequestInput)
                                    successMessage = "🚀 ¡Automejora global ejecutada y sincronizada en la nube!"
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldGoldLight),
                                border = BorderStroke(1.5.dp, ShieldGold),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .height(48.dp)
                                    .testTag("btn_super_ai_self_upgrade")
                            ) {
                                Icon(Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Super-Mejora", fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }

                        // Submódulo 1: Accesos y Correos
                        if (selectedAiModuleDot == 0 || selectedAiModuleDot == 1) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White.copy(alpha = 0.05f),
                                border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "🔐 Acciones Rápidas de Seguridad, Accesos y Correos:",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ShieldGoldLight
                                    )
                                    Row(
                                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                val cmd = "Aprobar todas las solicitudes pendientes de acceso Basiliano y sincronizar directorio"
                                                appUpdateRequestInput = cmd
                                                viewModel.approveAllPendingAccessRequests()
                                                viewModel.executeAiAppCommand(cmd)
                                            },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4ADE80)),
                                            border = BorderStroke(1.dp, Color(0xFF4ADE80)),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("✅ Aprobar Solicitudes Pendientes", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                appUpdateRequestInput = "Autorizar correo ejemplo@basilianos.org como sacerdote basiliano con acceso completo"
                                            },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00F0FF)),
                                            border = BorderStroke(1.dp, Color(0xFF00F0FF)),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("📧 Plantilla: Autorizar Correo Basiliano", fontSize = 11.sp)
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                appUpdateRequestInput = "Restringir correo visitante@correo.com a modo laico visitante"
                                            },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFFCDD2)),
                                            border = BorderStroke(1.dp, ShieldRed.copy(alpha = 0.7f)),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("🔒 Plantilla: Restringir Correo a Visitante", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Submódulo 2: Títulos, Oración y Avisos
                        if (selectedAiModuleDot == 0 || selectedAiModuleDot == 2) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White.copy(alpha = 0.05f),
                                border = BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.35f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "🏛️ Títulos, Oración Diaria y Circulares en Vivo:",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF00F0FF)
                                    )
                                    Row(
                                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                val prompt = "Actualiza la oración diaria pidiendo por las vocaciones sacerdotales basilianas y la paz en Colombia"
                                                appUpdateRequestInput = prompt
                                                viewModel.executeAiAppCommand(prompt)
                                            },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                            border = BorderStroke(1.dp, ShieldGold),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("🙏 Actualizar Oración por Vocaciones", fontSize = 11.sp)
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                appUpdateRequestInput = "Cambiar título a: Congregación de San Basilio"
                                            },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldGoldLight),
                                            border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.7f)),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("✏️ Cambiar Título Principal", fontSize = 11.sp)
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                appUpdateRequestInput = "Cambiar lema a: Enséñame la Bondad, la Disciplina y la Ciencia"
                                            },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                            border = BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.6f)),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("📜 Cambiar Lema Institucional", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Submódulo 3: Fondos 3D y Pestañas
                        if (selectedAiModuleDot == 0 || selectedAiModuleDot == 3) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White.copy(alpha = 0.05f),
                                border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.35f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "🎨 Apariencia 3D, Fondos y Pestañas de la App:",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ShieldGoldLight
                                    )
                                    Row(
                                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                val prompt = "Activar fondo holográfico futurista 3D en toda la aplicación"
                                                appUpdateRequestInput = prompt
                                                viewModel.executeAiAppCommand(prompt)
                                            },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00F0FF)),
                                            border = BorderStroke(1.dp, Color(0xFF00F0FF)),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("✨ Fondo Holograma 3D", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                val prompt = "Quitar pantalla blanca y poner fondo de vitral sacro resplandeciente"
                                                appUpdateRequestInput = prompt
                                                viewModel.executeAiAppCommand(prompt)
                                            },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                            border = BorderStroke(1.dp, ShieldGold),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("✨ Vitral Sacro", fontSize = 11.sp)
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                val prompt = "Restablece todas las pestañas y componentes de la app por defecto"
                                                appUpdateRequestInput = prompt
                                                viewModel.executeAiAppCommand(prompt)
                                            },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldGreenLight),
                                            border = BorderStroke(1.dp, ShieldGreen.copy(alpha = 0.6f)),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("✅ Mostrar Todas las Pestañas", fontSize = 11.sp)
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                val prompt = "Activar modo oscuro nocturno monástico"
                                                appUpdateRequestInput = prompt
                                                viewModel.executeAiAppCommand(prompt)
                                            },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                            border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.6f)),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("🌙 Modo Noche Monástica", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Submódulo 4: Saludo Superior de la IA de Historia
                        if (selectedAiModuleDot == 0 || selectedAiModuleDot == 4) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White.copy(alpha = 0.06f),
                                border = BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.45f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "🤖 Configurar Saludo y Misión Diaria en Historia IA:",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00F0FF)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = jarvisFocusInput,
                                            onValueChange = { jarvisFocusInput = it },
                                            placeholder = { Text("Ej: Estudio de las Reglas de San Basilio y Pastoral Educativa", color = Color.Gray, fontSize = 11.sp) },
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White,
                                                focusedBorderColor = Color(0xFF00F0FF),
                                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                                            ),
                                            singleLine = true,
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        Button(
                                            onClick = {
                                                viewModel.setJarvisDailyFocusTopic(jarvisFocusInput)
                                                viewModel.syncGlobalAdminChangesToAllDevices()
                                                successMessage = "🤖 ¡Saludo y misión diaria de la IA actualizados y sincronizados!"
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = ShieldGold,
                                                contentColor = Color.Black
                                            ),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("Aplicar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        // RESULTADO DE EJECUCIÓN INMEDIATA POR LA IA
                        aiCommandResult?.let { res ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F291E)),
                                border = BorderStroke(1.5.dp, ShieldGreen)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ShieldGreenLight, modifier = Modifier.size(22.dp))
                                        Text(
                                            text = "⚡ ¡Acciones Ejecutadas en Vivo por la Súper IA!",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ShieldGold
                                        )
                                    }
                                    Text(
                                        text = res.explanation,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.95f),
                                        lineHeight = 18.sp
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.Black.copy(alpha = 0.35f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text(
                                                text = "Ajustes aplicados en la app (${res.timestamp}):",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = ShieldGoldLight
                                            )
                                            res.actionsExecuted.forEach { act ->
                                                Text(
                                                    text = act,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color(0xFFE8F5E9),
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // REGISTRO DE ACTUALIZACIONES EN VIVO (CHANGELOG DE LA IA DE AUTOMEJORA)
                        if (liveUpdateHistoryLog.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Black.copy(alpha = 0.38f),
                                border = BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "📡 Registro de Automejoras Aplicadas en Vivo (${liveAppVersion}):",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00F0FF)
                                    )
                                    liveUpdateHistoryLog.take(5).forEach { logLine ->
                                        Text(
                                            text = "• $logLine",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.85f)
                                        )
                                    }
                                }
                            }
                        }

                        // =========================================================
                        // CENTRO DE CONTROL MANUAL: QUITAR O PONER EN LA APP
                        // =========================================================
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                            border = BorderStroke(1.2.dp, ShieldGold.copy(alpha = 0.7f))
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("🎛️", fontSize = 20.sp)
                                    Column {
                                        Text(
                                            text = "Interruptores de Control: Quitar / Poner",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ShieldGold
                                        )
                                        Text(
                                            text = "Control manual instantáneo de cada sección de la aplicación",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                HorizontalDivider(color = ShieldGold.copy(alpha = 0.3f))

                                Text("Pestañas de Navegación:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = ShieldGoldLight)

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("🕊️ Pestaña Oración Diaria", color = Color.White, fontSize = 12.sp)
                                    Switch(checked = showTabOracion, onCheckedChange = { viewModel.setShowTabOracion(it) })
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("📜 Pestaña Liturgia de las Horas (Portal CEC)", color = Color.White, fontSize = 12.sp)
                                    Switch(checked = showTabLiturgia, onCheckedChange = { viewModel.setShowTabLiturgia(it) })
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("🎂 Pestaña Cumpleaños & Directorio", color = Color.White, fontSize = 12.sp)
                                    Switch(checked = showTabCumpleanos, onCheckedChange = { viewModel.setShowTabCumpleanos(it) })
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("🏛️ Pestaña Historia & Cronista IA", color = Color.White, fontSize = 12.sp)
                                    Switch(checked = showTabHistoria, onCheckedChange = { viewModel.setShowTabHistoria(it) })
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("📚 Pestaña Recursos & Biblioteca", color = Color.White, fontSize = 12.sp)
                                    Switch(checked = showTabRecursos, onCheckedChange = { viewModel.setShowTabRecursos(it) })
                                }

                                HorizontalDivider(color = ShieldGold.copy(alpha = 0.3f))

                                Text("Componentes y Fondos:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = ShieldGoldLight)

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("🌟 Banner Heroico Superior", color = Color.White, fontSize = 12.sp)
                                    Switch(checked = showHeroBanner, onCheckedChange = { viewModel.setShowHeroBanner(it) })
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("📷 Espacios para Subir Fotos y Devociones", color = Color.White, fontSize = 12.sp)
                                    Switch(checked = showDevotionalImageSlots, onCheckedChange = { viewModel.setShowDevotionalImageSlots(it) })
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("🌐 Acceso Rápido al Portal Ordo CEC", color = Color.White, fontSize = 12.sp)
                                    Switch(checked = showOrdoQuickAccess, onCheckedChange = { viewModel.setShowOrdoQuickAccess(it) })
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("🔊 Reproductor de Audio Sagrado", color = Color.White, fontSize = 12.sp)
                                    Switch(checked = showDailyAudioPlayer, onCheckedChange = { viewModel.setShowDailyAudioPlayer(it) })
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("💬 Muro Social de Facebook", color = Color.White, fontSize = 12.sp)
                                    Switch(checked = showCommunitySocialWall, onCheckedChange = { viewModel.setShowCommunitySocialWall(it) })
                                }

                                Text("🎨 Selección de Fondo (Sin Pantalla Blanca):", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = ShieldGoldLight)
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    val bgList = listOf(
                                        GlobalBgMode.JARVIS_FUTURISTA to "✨ Holograma 3D",
                                        GlobalBgMode.VITRAL_SACRO to "✨ Vitral",
                                        GlobalBgMode.ESPIRITU_SANTO to "🕊️ Espíritu",
                                        GlobalBgMode.PERGAMINO_MONASTICO to "📜 Pergamino",
                                        GlobalBgMode.CALIZ_EUCARISTICO to "🍷 Cáliz",
                                        GlobalBgMode.NOCHE_MISTICA to "🌌 Noche"
                                    )
                                    items(bgList) { (mode, name) ->
                                        FilterChip(
                                            selected = globalBgMode == mode,
                                            onClick = { viewModel.setGlobalBackgroundMode(mode) },
                                            label = { Text(name, fontSize = 11.sp, color = Color.White) }
                                        )
                                    }
                                }

                                OutlinedButton(
                                    onClick = {
                                        viewModel.restoreAllAppSections()
                                        successMessage = "¡Todos los componentes, pestañas y fondos fueron restaurados por defecto!"
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldGoldLight),
                                    border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("🔄 Restaurar Todos los Componentes por Defecto", fontSize = 11.sp)
                                }
                            }
                        }

                        // Propuesta de cambio lista para aplicar
                        appUpdateProposal?.let { proposal ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                                border = BorderStroke(1.5.dp, ShieldGold)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ShieldGreenLight, modifier = Modifier.size(20.dp))
                                        Text(
                                            text = "Propuesta de Cambios Generada por IA",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ShieldGold
                                        )
                                    }

                                    Text(
                                        text = proposal.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.White.copy(alpha = 0.08f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(
                                                text = "📜 Circular Oficial Curial:",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = ShieldGoldLight
                                            )
                                            Text(
                                                text = proposal.announcementBody,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White.copy(alpha = 0.95f)
                                            )
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = ShieldGreenDark.copy(alpha = 0.35f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(
                                                text = "🙏 Oración del Día / Intención Comunitaria:",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = ShieldGoldLight
                                            )
                                            Text(
                                                text = proposal.prayerIntention,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White,
                                                fontStyle = FontStyle.Italic
                                            )
                                        }
                                    }

                                    if (proposal.scholasticRecommendation.isNotBlank()) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF334155),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Text(
                                                    text = "🎓 Exhortación para la Formación:",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = ShieldGoldLight
                                                )
                                                Text(
                                                    text = proposal.scholasticRecommendation,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.White.copy(alpha = 0.9f)
                                                )
                                            }
                                        }
                                    }

                                    // BOTÓN PRINCIPAL: APLICAR A TODA LA APP EN VIVO
                                    Button(
                                        onClick = {
                                            viewModel.applyProposalToCuriaAnnouncement()
                                            editTitle = proposal.title
                                            editBody = proposal.announcementBody
                                            editPrayer = proposal.prayerIntention
                                            successMessage = "¡Cambios aplicados exitosamente a toda la app! La oración del día y los comunicados oficiales se han actualizado en vivo."
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = ShieldGreenDark,
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("btn_apply_ai_changes_to_app")
                                    ) {
                                        Icon(Icons.Default.Bolt, contentDescription = null, tint = ShieldGold, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "✨ Aplicar Cambios a Toda la App Inmediatamente",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            viewModel.applyProposalToCommunityWall()
                                            successMessage = "¡Publicado en el muro comunitario con el hashtag oficial #BasilianosCSB!"
                                        },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldGoldLight),
                                        border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.6f)),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Publicar también en el Muro Fraterno", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            } // Fin categoría 1 (Súper IA & Textos)

            // =========================================================================
            // 4. SECCIÓN: AUTO-ACTUALIZACIÓN EN LA NUBE SIN LINKS PARA TODOS LOS CELULARES
            // =========================================================================
            if (selectedAdminCategory == 0 || selectedAdminCategory == 4) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_share_app_download"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.5.dp, Color(0xFF0284C7).copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CraftedSectionHeader(
                            title = "Auto-Actualización Directa en Todos los Celulares",
                            subtitle = "Sin enlaces de descarga: tú das el permiso aquí y se actualiza sola en cada celular",
                            emoji = "⚡",
                            accentColor = Color(0xFF0284C7),
                            underlineWidth = 180.dp
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF0F9FF),
                            border = BorderStroke(1.dp, Color(0xFFBAE6FD)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Estado de Sincronización Automática (${liveAppVersion}):",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF0369A1),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = lastGlobalSyncTimestamp,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Link Real y Verídico: ${otaUrlInput.ifBlank { BasilianViewModel.VERIFIED_SHARED_APP_URL }}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF0284C7),
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val currentRealUrl = otaUrlInput.trim().ifBlank { BasilianViewModel.VERIFIED_SHARED_APP_URL }
                            Button(
                                onClick = {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentRealUrl))
                                        context.startActivity(intent)
                                    } catch (_: Exception) {}
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Abrir Link Real", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
                            }

                            Button(
                                onClick = {
                                    val shareText = "🛡️ Actualización Oficial App Padres Basilianos ($liveAppVersion):\n📲 Entra a este link verídico para actualizar inmediatamente:\n$currentRealUrl"
                                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Enviar link de actualización"))
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Enviar Link", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
                            }
                        }

                        Button(
                            onClick = {
                                viewModel.broadcastNewVersionToAllPhonesNow(
                                    versionLabel = liveAppVersion,
                                    releaseNotes = otaReleaseNotes,
                                    downloadUrl = otaUrlInput.trim().ifBlank { BasilianViewModel.VERIFIED_SHARED_APP_URL }
                                ) { _, msg ->
                                    successMessage = "⚡ $msg"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("⚡ Dar Permiso y Auto-Actualizar Todos los Celulares Ahora", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                        }
                    }
                }
            }
            } // Fin categoría 4

            // =========================================================================
            // 2. SECCIÓN: FONDO SAGRADO, ESCUDO OFICIAL Y FOTOS DE BASILIANOS/SANTOS
            // =========================================================================
            if (selectedAdminCategory == 0 || selectedAdminCategory == 2) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_global_background_settings"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.5.dp, ShieldGold.copy(alpha = 0.7f)),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CraftedSectionHeader(
                            title = "Fondo Sagrado de la Aplicación",
                            subtitle = "Visible en todas las pantallas como lienzo sagrado",
                            emoji = "🕊️",
                            accentColor = ShieldGoldDark,
                            underlineWidth = 150.dp
                        )

                        Text(
                            text = "Selecciona el fondo espiritual de toda la aplicación: la paloma de luz celestial del Espíritu Santo, o sube cualquier imagen sacra de tu galería.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )

                        // Selector de modo
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = globalBgMode == GlobalBgMode.ESPIRITU_SANTO,
                                onClick = { viewModel.setGlobalBackgroundMode(GlobalBgMode.ESPIRITU_SANTO) },
                                label = { Text("🕊️ Espíritu Santo", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = globalBgMode == GlobalBgMode.CUSTOM_IMAGE,
                                onClick = {
                                    if (globalCustomUri.isNullOrBlank()) {
                                        bgImagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                    } else {
                                        viewModel.setGlobalBackgroundMode(GlobalBgMode.CUSTOM_IMAGE)
                                    }
                                },
                                label = { Text("🖼️ Mi Imagen", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = globalBgMode == GlobalBgMode.TEMATICO_PURO,
                                onClick = { viewModel.setGlobalBackgroundMode(GlobalBgMode.TEMATICO_PURO) },
                                label = { Text("🛡️ Temático", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Botón para subir/cambiar imagen si está en modo o tiene imagen
                        if (globalBgMode == GlobalBgMode.CUSTOM_IMAGE || !globalCustomUri.isNullOrBlank()) {
                            OutlinedButton(
                                onClick = {
                                    bgImagePickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, ShieldGreenDark)
                            ) {
                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = ShieldGreenDark, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (globalCustomUri.isNullOrBlank()) "Seleccionar Foto de Fondo de Galería" else "Cambiar Imagen de Fondo de la App",
                                    color = ShieldGreenDark,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Ajuste de presencia/opacidad del fondo
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Claridad / Opacidad del Fondo:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "${(globalOpacity * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ShieldGoldDark
                                )
                            }
                            Slider(
                                value = globalOpacity,
                                onValueChange = { viewModel.setGlobalBackgroundOpacity(it) },
                                valueRange = 0.20f..0.98f,
                                colors = SliderDefaults.colors(
                                    thumbColor = ShieldGoldDark,
                                    activeTrackColor = ShieldGold
                                )
                            )
                        }
                    }
                }
            }

            // =========================================================================
            // SECCIÓN 1: ESCUDO OFICIAL DE LA APLICACIÓN (SUBIR ORIGINAL DESDE EL CELULAR)
            // =========================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.5.dp, ShieldGold.copy(alpha = 0.6f)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CraftedSectionHeader(
                            title = "Escudo Oficial de la Congregación",
                            subtitle = if (customCrestUri != null) "✓ Escudo Original Personalizado Activo" else "Escudo Vectorial Predeterminado Activo",
                            emoji = "🛡️",
                            accentColor = ShieldGoldDark,
                            underlineWidth = 160.dp
                        )

                        Text(
                            text = "Aquí puede subir la imagen original y auténtica del escudo desde la galería de su celular. Esta imagen reemplazará el escudo en todas las pantallas de la aplicación.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Vista previa en vivo del escudo actual
                            BasilianCrestView(
                                size = 110.dp,
                                showMottoText = false,
                                customCrestUri = customCrestUri
                            )

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        crestPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Subir Escudo Original", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                if (customCrestUri != null) {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.resetCustomCrest()
                                            successMessage = "Se ha restaurado el escudo heráldico vectorial de 1822."
                                        },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldRed),
                                        border = BorderStroke(1.dp, ShieldRed.copy(alpha = 0.5f)),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Restablecer Predeterminado", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // SECCIÓN 1B: IMAGEN DEVOCIONAL EN ORACIÓN (REZANDO HOY)
            // =========================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.5.dp, ShieldGold.copy(alpha = 0.6f)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ShieldGold.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = ShieldGoldDark)
                            }
                            Column {
                                Text(
                                    text = "Imagen Devocional en «Rezando Hoy»",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ShieldGreenDark
                                )
                                Text(
                                    text = if (customPrayerSideImageUri != null) "✓ Imagen Devocional Personalizada Activa" else "Icono Oficial de San Basilio Magno Activo",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (customPrayerSideImageUri != null) ShieldGreen else Color.Gray,
                                    fontWeight = if (customPrayerSideImageUri != null) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }

                        Text(
                            text = "Seleccione una imagen sacra o devocional de su galería para acompañar el Evangelio y la oración matutina en la pantalla principal de oración.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Vista previa de la imagen devocional
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = ShieldGreenDark.copy(alpha = 0.08f),
                                border = BorderStroke(1.5.dp, ShieldGold.copy(alpha = 0.6f)),
                                shadowElevation = 2.dp,
                                modifier = Modifier.size(105.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    if (customPrayerSideImageUri != null) {
                                        AsyncImage(
                                            model = customPrayerSideImageUri,
                                            contentDescription = "Imagen de San Basilio o Devoción",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        SanBasilioIconView(canvasSize = 90.dp)
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        prayerSideImagePickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (customPrayerSideImageUri != null) "Cambiar Imagen" else "Subir Imagen Devocional",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (customPrayerSideImageUri != null) {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.resetCustomPrayerSideImage()
                                            successMessage = "Se ha restaurado el icono oficial de San Basilio Magno."
                                        },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldRed),
                                        border = BorderStroke(1.dp, ShieldRed.copy(alpha = 0.5f)),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Restaurar San Basilio Magno", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // SECCIÓN 2: GESTOR DE FOTOS DE LOS BASILIANOS Y SANTOS (SUBIR DESDE CELULAR)
            // =========================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.5.dp, ShieldGreen.copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        CraftedSectionHeader(
                            title = "Gestor del Archivo Fotográfico Basiliano",
                            subtitle = "Sustituya las imágenes del sistema por fotos reales y originales",
                            emoji = "📷",
                            accentColor = ShieldGreenDark,
                            underlineWidth = 160.dp
                        )

                        Text(
                            text = "Seleccione cualquier fundador, misionero o santo para subir su fotografía histórica o institucional desde su dispositivo móvil.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )

                        // Selector de categoría
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = photoCategoryFilter == 0,
                                onClick = { photoCategoryFilter = 0 },
                                label = { Text("Fundadores 1822", fontSize = 11.sp) },
                                leadingIcon = {
                                    if (photoCategoryFilter == 0) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            )
                            FilterChip(
                                selected = photoCategoryFilter == 1,
                                onClick = { photoCategoryFilter = 1 },
                                label = { Text("Misión Colombia", fontSize = 11.sp) },
                                leadingIcon = {
                                    if (photoCategoryFilter == 1) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            )
                            FilterChip(
                                selected = photoCategoryFilter == 2,
                                onClick = { photoCategoryFilter = 2 },
                                label = { Text("Santos Patronos", fontSize = 11.sp) },
                                leadingIcon = {
                                    if (photoCategoryFilter == 2) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            )
                        }

                        // Lista de personajes para subir foto
                        val itemsToShow: List<Triple<String, String, String>> = when (photoCategoryFilter) {
                            0 -> viewModel.foundersList.filter { it.isFrenchFounder }.map { Triple(it.name, it.role, "founder_${it.name.hashCode()}") }
                            1 -> viewModel.foundersList.filter { !it.isFrenchFounder }.map { Triple(it.name, it.role, "pioneer_${it.name.hashCode()}") }
                            else -> viewModel.patronsList.map { Triple(it.name, "${it.title} • ${it.feastDay}", "patron_${it.name.hashCode()}") }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            itemsToShow.forEach { item ->
                                val name = item.first
                                val subtitle = item.second
                                val id = item.third
                                val customPhotoUri = customPhotos[id]
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (customPhotoUri != null) ShieldGreen.copy(alpha = 0.08f) else Color(0xFFF9F9FB),
                                    border = BorderStroke(1.dp, if (customPhotoUri != null) ShieldGreen.copy(alpha = 0.4f) else Color(0xFFE2E8F0)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        // Foto actual (personalizada o predeterminada)
                                        Box(
                                            modifier = Modifier
                                                .size(46.dp)
                                                .clip(CircleShape)
                                                .background(ShieldGreen.copy(alpha = 0.15f))
                                                .border(1.5.dp, if (customPhotoUri != null) ShieldGreen else ShieldGold, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (customPhotoUri != null) {
                                                AsyncImage(
                                                    model = customPhotoUri,
                                                    contentDescription = name,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            } else {
                                                Icon(
                                                    imageVector = Icons.Default.Person,
                                                    contentDescription = null,
                                                    tint = ShieldGreenDark,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = name,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1E293B),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = subtitle,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.Gray,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            if (customPhotoUri != null) {
                                                Text(
                                                    text = "✓ Foto propia activa",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = ShieldGreen,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }

                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            FilledTonalButton(
                                                onClick = {
                                                    selectedPhotoTargetId = id
                                                    selectedPhotoTargetName = name
                                                    basilianPhotoPickerLauncher.launch(
                                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                    )
                                                },
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.filledTonalButtonColors(
                                                    containerColor = ShieldGreenDark.copy(alpha = 0.12f),
                                                    contentColor = ShieldGreenDark
                                                )
                                            ) {
                                                Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Subir", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }

                                            if (customPhotoUri != null) {
                                                IconButton(
                                                    onClick = {
                                                        viewModel.removeCustomPhoto(id)
                                                        successMessage = "Se ha restaurado la imagen predeterminada de $name."
                                                    },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = ShieldRed, modifier = Modifier.size(16.dp))
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
            } // Fin categoría 2 (Escudo, Fondo y Fotos)

            // =========================================================================
            // 3. MÓDULO INTERACTIVO DEL COPILOTO, CIRCULARES, ORACIÓN Y CORPUS IA
            // =========================================================================
            if (selectedAdminCategory == 0 || selectedAdminCategory == 3) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0B132B)),
                    border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.7f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CraftedSectionHeader(
                                title = "Copiloto • Basilio Estándar",
                                subtitle = "Regla de San Basilio, Constituciones & Hexamerón",
                                emoji = "🧠",
                                accentColor = ShieldGold,
                                underlineWidth = 160.dp
                            )

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ShieldGold.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "Basilio Estándar",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ShieldGoldLight,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "Genera reflexiones teológicas, homiléticas y comunitarias analizando directamente los textos cargados sobre la Regla de San Basilio (Asketikon), los 153 Artículos de la Forma de Vida, el Tratado del Hexamerón y la Pastoral Vocacional.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f),
                            lineHeight = 18.sp
                        )

                        // Selector de Corpus Documental
                        Text(
                            text = "Corpus de Referencia:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = ShieldGoldLight
                        )

                        val categories = listOf(
                            "Regla de San Basilio (Vida Fraterna)",
                            "Constitución CSB (153 Artículos)",
                            "El Hexamerón (Ciencia y Creación)",
                            "Pastoral Vocacional (Juventud)"
                        )

                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categories.forEach { cat ->
                                val isSelected = copilotCorpusCategory == cat
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.setCopilotCorpusCategory(cat) },
                                    label = { Text(cat, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ShieldGold,
                                        selectedLabelColor = Color.Black,
                                        containerColor = Color.White.copy(alpha = 0.08f),
                                        labelColor = Color.White.copy(alpha = 0.8f)
                                    )
                                )
                            }
                        }

                        // Campo de Texto para la Petición
                        OutlinedTextField(
                            value = copilotPromptInput,
                            onValueChange = { copilotPromptInput = it },
                            label = { Text("Instrucción o Tema para el Copiloto", color = ShieldGoldLight) },
                            placeholder = { Text("Ej: Redacta una reflexión sobre la castidad y sobriedad en la Regla...", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = ShieldGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                cursorColor = ShieldGold
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Atajos Rápidos de Consulta
                        Text(
                            text = "Atajos de Reflexión Inmediata:",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )

                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val prompt = "Redacta una reflexión de la Regla de San Basilio sobre la paciencia, humildad y corrección fraterna"
                                    copilotPromptInput = prompt
                                    viewModel.setCopilotCorpusCategory("Regla de San Basilio (Vida Fraterna)")
                                    viewModel.generateCopilotReflection(prompt)
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("📜 Regla: Fraternidad", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    val prompt = "Genera un artículo de astrofísica y creación basado en el Hexamerón de San Basilio"
                                    copilotPromptInput = prompt
                                    viewModel.setCopilotCorpusCategory("El Hexamerón (Ciencia y Creación)")
                                    viewModel.generateCopilotReflection(prompt)
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldGoldLight),
                                border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("🌌 Hexamerón & Cosmos", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    val prompt = "Genera un mensaje de discernimiento vocacional para jóvenes de Colombia"
                                    copilotPromptInput = prompt
                                    viewModel.setCopilotCorpusCategory("Pastoral Vocacional (Juventud)")
                                    viewModel.generateCopilotReflection(prompt)
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("🕊️ Pastoral Vocacional", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    val prompt = "Explica el valor del voto de pobreza según las Constituciones Basilianas"
                                    copilotPromptInput = prompt
                                    viewModel.setCopilotCorpusCategory("Constitución CSB (153 Artículos)")
                                    viewModel.generateCopilotReflection(prompt)
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("📖 Voto de Pobreza (Art. 14-19)", fontSize = 11.sp)
                            }
                        }

                        // Botón de Generación Principal
                        Button(
                            onClick = {
                                val prompt = if (copilotPromptInput.isNotBlank()) copilotPromptInput else "Redacta una reflexión espiritual y teológica para la comunidad"
                                viewModel.generateCopilotReflection(prompt)
                            },
                            enabled = !isCopilotGenerating,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ShieldGold,
                                contentColor = Color(0xFF0B132B)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("btn_generate_copilot")
                        ) {
                            if (isCopilotGenerating) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = Color(0xFF0B132B),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Consultando Basilio Estándar & Regla de San Basilio...", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Generar Reflexión con Basilio Estándar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        // Tarjeta de Resultado Generado
                        copilotReflection?.let { refl ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
                                border = BorderStroke(1.dp, ShieldGold)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ShieldGold, modifier = Modifier.size(16.dp))
                                        Text(
                                            text = "Reflexión Generada por Basilio Estándar",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = ShieldGold
                                        )
                                    }

                                    Text(
                                        text = refl.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color.White.copy(alpha = 0.12f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Fuente Patrística / Constitucional: ${refl.patristicSource}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = ShieldGoldLight,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    Text(
                                        text = refl.content,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.9f),
                                        lineHeight = 18.sp
                                    )

                                    Text(
                                        text = "Acción / Aplicación Pastoral: ${refl.practicalApplication}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ShieldGoldLight
                                    )

                                    // Botón para transferir directamente al formulario de Firestore
                                    Button(
                                        onClick = {
                                            viewModel.applyCopilotReflectionToAnnouncement()
                                            editTitle = refl.title
                                            editBody = "${refl.content}\n\n[Fuente: ${refl.patristicSource}]"
                                            editPrayer = refl.practicalApplication
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = ShieldGreen,
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("btn_apply_copilot_to_form")
                                    ) {
                                        Icon(Icons.Default.ContentPasteGo, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Transferir al Comunicado Oficial (Listo para Firestore)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =====================================================================
            // SECCIÓN IA: ACTUALIZADOR Y OPTIMIZADOR INTELIGENTE DE LA APP
            // =====================================================================
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_ai_app_optimizer"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    border = BorderStroke(1.5.dp, ShieldGold)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.AutoFixHigh,
                                    contentDescription = null,
                                    tint = ShieldGold,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column {
                                    Text(
                                        text = "Optimizador & Actualizador IA de la App",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = ShieldGold
                                    )
                                    Text(
                                        text = "Mejora y actualiza la app para los religiosos y escolásticos",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.75f)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ShieldGold.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, ShieldGold)
                            ) {
                                Text(
                                    text = "Curia IA",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ShieldGoldLight,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Text(
                            text = "Pide a la IA cualquier cambio, actualización de circular, lema o mejora para la vida basiliana. La IA generará una actualización oficial completa con circular, intención de oración del día y comunicado comunitario.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f),
                            lineHeight = 18.sp
                        )

                        OutlinedTextField(
                            value = appUpdateRequestInput,
                            onValueChange = { appUpdateRequestInput = it },
                            label = { Text("¿Qué deseas mejorar o actualizar en la app?", color = ShieldGoldLight) },
                            placeholder = { Text("Ej: Actualiza la app para los escolásticos con una exhortación de San Basilio sobre la castidad y el estudio...", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = ShieldGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                cursorColor = ShieldGold
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            shape = RoundedCornerShape(12.dp)
                        )

                        Text(
                            text = "Sugerencias de Actualización Inmediata:",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )

                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val req = "Mejora la app para los escolásticos basilianos: redacta una exhortación de San Basilio sobre el estudio riguroso, la vida fraterna en la casa de formación y una oración de consagración."
                                    appUpdateRequestInput = req
                                    viewModel.generateAppOptimizationWithAi(req)
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("🎓 Escolasticado & Estudios", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    val req = "Actualiza la intención de oración del día para toda la congregación por las vocaciones sacerdotales y misioneras basilianas."
                                    appUpdateRequestInput = req
                                    viewModel.generateAppOptimizationWithAi(req)
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("🙏 Nueva Oración del Día", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    val req = "Actualiza la app con motivo del espíritu de los 10 sacerdotes fundadores de Annonay (1822) y el carisma educativo de enseñar la doctrina cristiana."
                                    appUpdateRequestInput = req
                                    viewModel.generateAppOptimizationWithAi(req)
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldGoldLight),
                                border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.7f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("📜 Fundadores Annonay 1822", fontSize = 11.sp)
                            }
                        }

                        Button(
                            onClick = {
                                if (appUpdateRequestInput.isNotBlank()) {
                                    viewModel.generateAppOptimizationWithAi(appUpdateRequestInput)
                                }
                            },
                            enabled = !isAppUpdateGenerating && appUpdateRequestInput.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ShieldGoldDark,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_generate_app_update")
                        ) {
                            if (isAppUpdateGenerating) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Generando actualización con Basilio Estándar...", fontSize = 13.sp)
                            } else {
                                Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Actualizar & Optimizar App con IA",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Resultado de la Propuesta de Actualización
                        appUpdateProposal?.let { proposal ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                                border = BorderStroke(1.dp, ShieldGold)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ShieldGreenLight, modifier = Modifier.size(18.dp))
                                        Text(
                                            text = "Propuesta Oficial Generada por IA",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = ShieldGold
                                        )
                                    }

                                    Text(
                                        text = proposal.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color.White.copy(alpha = 0.08f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(
                                                text = "📜 Circular Curial:",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = ShieldGoldLight
                                            )
                                            Text(
                                                text = proposal.announcementBody,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White.copy(alpha = 0.9f)
                                            )
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = ShieldGreenDark.copy(alpha = 0.25f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(
                                                text = "🙏 Oración del Día Actualizada:",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = ShieldGoldLight
                                            )
                                            Text(
                                                text = proposal.prayerIntention,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White.copy(alpha = 0.95f),
                                                fontStyle = FontStyle.Italic
                                            )
                                        }
                                    }

                                    if (proposal.scholasticRecommendation.isNotBlank()) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0xFF334155),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Text(
                                                    text = "🎓 Exhortación para Escolásticos:",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = ShieldGoldLight
                                                )
                                                Text(
                                                    text = proposal.scholasticRecommendation,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.White.copy(alpha = 0.9f)
                                                )
                                            }
                                        }
                                    }

                                    // Botón 1: Aplicar a la app (Circular Oficial + Oración del Día)
                                    Button(
                                        onClick = {
                                            viewModel.applyProposalToCuriaAnnouncement()
                                            editTitle = proposal.title
                                            editBody = proposal.announcementBody
                                            editPrayer = proposal.prayerIntention
                                            successMessage = "¡Actualización oficial aplicada a toda la aplicación!"
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = ShieldGreen,
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "⚡ Aplicar Actualización a la App (Circular + Oración)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    // Botón 2: Publicar en el Muro Fraterno de la Comunidad
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.applyProposalToCommunityWall()
                                            successMessage = "¡Comunicado oficial publicado con éxito en el Muro Fraterno!"
                                        },
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = ShieldGoldLight
                                        ),
                                        border = BorderStroke(1.dp, ShieldGold),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "📢 Publicar Comunicado Oficial en el Muro Fraterno",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            } // Fin bloque Copiloto y Optimizador

            // =========================================================================
            // SECCIÓN IA: AUDITORÍA DE USO Y SUGERENCIAS DE MEJORA CONTINUA (GEMINI)
            // =========================================================================
            if (selectedAdminCategory == 0 || selectedAdminCategory == 4) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_ai_usage_analysis"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    border = BorderStroke(1.5.dp, ShieldGold)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CraftedSectionHeader(
                                title = "Auditoría de Uso & Mejoras",
                                subtitle = "Analiza el uso real y aplica correcciones con Basilio Estándar",
                                emoji = "⚡",
                                accentColor = ShieldGold,
                                underlineWidth = 160.dp
                            )

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ShieldGold.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, ShieldGold)
                            ) {
                                Text(
                                    text = "Basilio Estándar",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ShieldGoldLight,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Text(
                            text = "Basilio Estándar examina los datos operativos de la app (visitas al rezo diario, publicaciones fraternas, lectura de constituciones y tiempos de respuesta) y genera recomendaciones de configuración y corrección que puedes aplicar con un toque.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f),
                            lineHeight = 18.sp
                        )

                        Button(
                            onClick = { viewModel.analyzeApp() },
                            enabled = !isAnalyzingApp,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = ShieldGoldDark),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (isAnalyzingApp) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Analizando Telemetría y Salud de la App...", color = Color.White, fontSize = 12.sp)
                            } else {
                                Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ejecutar Análisis y Diagnóstico con IA", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        // Resultado del Informe de Análisis
                        appAnalysisReport?.let { report ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.08f),
                                border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Puntaje de Salud Operativa:",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = Color.White
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (report.overallHealthScore >= 90) ShieldGreen.copy(alpha = 0.3f) else ShieldGold.copy(alpha = 0.3f)
                                        ) {
                                            Text(
                                                text = "${report.overallHealthScore} / 100",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (report.overallHealthScore >= 90) ShieldGreenLight else ShieldGoldLight,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = report.summary,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )

                                    Text(
                                        text = "📊 Telemetría: ${report.telemetryNotes}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ShieldGoldLight,
                                        fontStyle = FontStyle.Italic
                                    )

                                    HorizontalDivider(color = Color.White.copy(alpha = 0.15f))

                                    Text(
                                        text = "Sugerencias de Optimización & Corrección (${report.suggestions.size}):",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = ShieldGold
                                    )

                                    report.suggestions.forEach { suggestion ->
                                        val isApplied = appliedAiConfigKeys.contains(suggestion.configKey)
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                                            border = BorderStroke(1.dp, if (isApplied) ShieldGreenLight else ShieldGold.copy(alpha = 0.4f))
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text(
                                                        text = suggestion.title,
                                                        style = MaterialTheme.typography.titleSmall,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White,
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                    Surface(
                                                        shape = RoundedCornerShape(6.dp),
                                                        color = Color.White.copy(alpha = 0.1f)
                                                    ) {
                                                        Text(
                                                            text = suggestion.category,
                                                            style = MaterialTheme.typography.labelSmall,
                                                            fontSize = 9.sp,
                                                            color = ShieldGoldLight,
                                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                }

                                                Text(
                                                    text = "🔍 Diagnóstico: ${suggestion.diagnosis}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.White.copy(alpha = 0.75f),
                                                    fontSize = 12.sp
                                                )

                                                Text(
                                                    text = "💡 Acción recomendada: ${suggestion.recommendedAction}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = ShieldGoldLight,
                                                    fontSize = 12.sp
                                                )

                                                if (isApplied) {
                                                    Surface(
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = ShieldGreen.copy(alpha = 0.25f),
                                                        border = BorderStroke(1.dp, ShieldGreenLight),
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Row(
                                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                        ) {
                                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ShieldGreenLight, modifier = Modifier.size(16.dp))
                                                            Text(
                                                                text = "Mejora Aplicada a la App (Activa en Configuración)",
                                                                style = MaterialTheme.typography.labelSmall,
                                                                fontWeight = FontWeight.Bold,
                                                                color = ShieldGreenLight
                                                            )
                                                        }
                                                    }
                                                } else {
                                                    Button(
                                                        onClick = {
                                                            viewModel.applyAiSuggestion(suggestion)
                                                            successMessage = "¡Mejora '${suggestion.title}' aplicada a la configuración de la app!"
                                                        },
                                                        colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark),
                                                        shape = RoundedCornerShape(8.dp),
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text("Aplicar Mejora Directamente a la App", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
            } // Fin Auditoría de Uso

            // =========================================================================
            // SECCIÓN: CARGA DE DOCUMENTOS, CIRCULARES Y ORACIÓN MENSUAL
            // =========================================================================
            if (selectedAdminCategory == 0 || selectedAdminCategory == 3) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_corpus_documents_manager"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.5.dp, ShieldGreen.copy(alpha = 0.6f)),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CraftedSectionHeader(
                                title = "Documentos & Corpus IA",
                                subtitle = "Actualiza el conocimiento doctrinal de Basilio Estándar",
                                emoji = "📖",
                                accentColor = ShieldGreenDark,
                                underlineWidth = 150.dp
                            )

                            FilledTonalButton(
                                onClick = { showAddDocDialog = true },
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = ShieldGreen.copy(alpha = 0.15f),
                                    contentColor = ShieldGreenDark
                                )
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Subir", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Text(
                            text = "Sube constituciones, circulares curiales, oraciones o reflexiones. La IA incorporará este corpus de inmediato para redactar homilías, asesorar a los escolásticos y analizar la comunidad.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )

                        if (uploadedCorpusDocs.isEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF8FAFC),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "No hay documentos cargados aún. Presiona 'Subir' para incorporar el primer texto al corpus.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        } else {
                            uploadedCorpusDocs.forEach { doc ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFF8FAFC),
                                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = ShieldGreenDark.copy(alpha = 0.12f)
                                                ) {
                                                    Text(
                                                        text = doc.category,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = ShieldGreenDark,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                                Text(
                                                    text = doc.dateAdded,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 10.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                            Text(
                                                text = doc.title,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1E293B)
                                            )
                                            Text(
                                                text = doc.contentPreview,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                viewModel.removeCorpusDocument(doc.id)
                                                successMessage = "Documento eliminado del corpus de la IA."
                                            }
                                        ) {
                                            Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = ShieldRed, modifier = Modifier.size(20.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Formulario de Publicación de Comunicados (CMS en Tiempo Real)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "1. Comunicado / Circular Oficial (Firestore CMS)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ShieldGreenDark
                        )

                        OutlinedTextField(
                            value = editTitle,
                            onValueChange = { editTitle = it },
                            label = { Text("Título del Comunicado") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = editDate,
                            onValueChange = { editDate = it },
                            label = { Text("Fecha y Lugar de Emisión") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = editBody,
                            onValueChange = { editBody = it },
                            label = { Text("Cuerpo del Comunicado / Mensaje") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 4,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            // Formulario de Intención de Oración Comunitaria
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "2. Intención de Oración Mensual de la Congregación",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ShieldGreenDark
                        )

                        OutlinedTextField(
                            value = editPrayer,
                            onValueChange = { editPrayer = it },
                            label = { Text("Intención de Oración Diaria Comunitaria") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            // Botón de Actualizar y Difundir con Firebase
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            viewModel.publishCuriaAnnouncementWithFirebase(
                                editTitle,
                                editBody,
                                editDate,
                                editPrayer
                            ) { success, msg ->
                                successMessage = msg
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ShieldRed),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_publish_curia_update")
                    ) {
                        Icon(Icons.Default.CloudSync, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Publicar en Firebase Firestore",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    if (successMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = ShieldGreenDark,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = successMessage!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
            } // Fin categoría 3 (Corpus, Circulares y Oración)
        }
    }
}

@Composable
fun FacebookVocacionalCard(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1877F2).copy(alpha = 0.08f)),
        border = BorderStroke(1.dp, Color(0xFF1877F2).copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(2.dp)
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
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1877F2),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "f",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Pastoral Vocacional",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ShieldGold.copy(alpha = 0.2f),
                            border = BorderStroke(0.5.dp, ShieldGold)
                        ) {
                            Text(
                                text = "Colombia C.S.B.",
                                style = MaterialTheme.typography.labelSmall,
                                color = ShieldGreenDark,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Padres Basilianos • Red Social Oficial en Facebook",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }

            Text(
                text = "Conoce las actividades pastorales, retiros vocacionales 'Ven y Verás', apostolado educativo y testimonios de discernimiento sacerdotal de la Congregación de San Basilio en Colombia.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray,
                lineHeight = 18.sp
            )

            Button(
                onClick = {
                    uriHandler.openUri("https://www.facebook.com/PastoralVocacionalBasilianosColombia/about")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_open_facebook_vocacional")
            ) {
                Icon(Icons.Default.Public, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Visitar Facebook Pastoral Vocacional",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

