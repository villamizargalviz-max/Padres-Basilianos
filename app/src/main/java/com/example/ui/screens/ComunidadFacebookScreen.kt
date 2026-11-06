package com.example.ui.screens

import android.net.Uri
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import coil.compose.AsyncImage
import com.example.data.model.CommunityPost
import com.example.data.model.UserCommunityProfile
import com.example.ui.components.BasilianCrestView
import com.example.ui.components.CraftedSectionHeader
import com.example.ui.components.OrganicBrushUnderline
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

/**
 * Campo de texto fraternal con letra NEGRA de alto contraste y legibilidad absoluta.
 * Garantiza que la letra escrita para correo, nombres y publicaciones sea 100% visible en negro.
 */
@Composable
fun FraternalOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(12.dp)
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        textStyle = androidx.compose.ui.text.TextStyle(
            color = Color.Black,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            disabledTextColor = Color(0xFF334155),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color(0xFFFAFAFA),
            cursorColor = Color.Black,
            focusedBorderColor = Color(0xFF1877F2),
            unfocusedBorderColor = Color(0xFF64748B),
            focusedLabelColor = Color(0xFF0F172A),
            unfocusedLabelColor = Color(0xFF334155),
            focusedPlaceholderColor = Color(0xFF64748B),
            unfocusedPlaceholderColor = Color(0xFF94A3B8)
        ),
        shape = shape,
        modifier = modifier
    )
}

/**
 * Indicador visual de presencia en tiempo real con punto verde / gris y texto de estado.
 */
@Composable
fun FraternalActiveBadge(
    isOnline: Boolean,
    statusText: String = if (isOnline) "En línea" else "Desconectado",
    modifier: Modifier = Modifier,
    showLabel: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(
                    color = if (isOnline) Color(0xFF22C55E) else Color(0xFF94A3B8),
                    shape = CircleShape
                )
                .border(1.5.dp, Color.White, CircleShape)
        )
        if (showLabel) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall,
                color = if (isOnline) Color(0xFF15803D) else Color(0xFF64748B),
                fontWeight = if (isOnline) FontWeight.Bold else FontWeight.Normal,
                fontSize = 10.5.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComunidadFacebookScreen(
    viewModel: BasilianViewModel,
    modifier: Modifier = Modifier
) {
    val currentProfile by viewModel.currentUserProfile.collectAsState()
    val allProfiles by viewModel.communityProfiles.collectAsState()
    val posts by viewModel.communityPosts.collectAsState()

    // Enviar latido de presencia en tiempo real al abrir la comunidad
    LaunchedEffect(Unit) {
        viewModel.updateUserPresenceNow()
    }

    var activeTab by remember { mutableStateOf(0) } // 0: Muro Fraterno, 1: Mi Perfil, 2: Directorio de Hermanos
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showSwitchEmailDialog by remember { mutableStateOf(false) }
    var showMultiDeviceSyncDialog by remember { mutableStateOf(false) }
    var showFirebaseConfigDialog by remember { mutableStateOf(false) }
    var showImportCodeField by remember { mutableStateOf(false) }
    var importCodeText by remember { mutableStateOf("") }
    var syncFeedbackMessage by remember { mutableStateOf<String?>(null) }

    val clipboardManager = LocalClipboardManager.current
    val isFirebaseAvailable by viewModel.isFirebaseAvailable.collectAsState()
    val firebaseSyncStatus by viewModel.firebaseSyncStatus.collectAsState()

    // Formularios para nuevo usuario (si no tiene perfil)
    var inputEmail by remember { mutableStateOf("") }
    var inputFullName by remember { mutableStateOf("") }
    var inputRole by remember { mutableStateOf("Fiel / Devoto de San Basilio") }
    var inputPatronSaint by remember { mutableStateOf("San Basilio Magno") }
    var inputCity by remember { mutableStateOf("Cali, Colombia") }
    var inputParish by remember { mutableStateOf("Parroquia San Basilio") }
    var inputBio by remember { mutableStateOf("Caminando en comunión fraterna, disciplina y búsqueda de Dios.") }
    var inputQuote by remember { mutableStateOf("«Bonitatem et disciplinam et scientiam doce me»") }

    // Compositor de publicación
    var postText by remember { mutableStateOf("") }
    var postImageUri by remember { mutableStateOf<Uri?>(null) }
    var isPosting by remember { mutableStateOf(false) }

    // Launcher para fotos del post
    val postImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            postImageUri = uri
        }
    }

    // Launcher para foto de perfil de usuario actual
    val profilePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.updateCurrentUserPhoto(uri)
        }
    }

    // Launcher para foto de portada de usuario actual
    val coverPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.updateCurrentUserCover(uri)
        }
    }

    // Roles disponibles con énfasis en Escolásticos y Religiosos
    val availableRoles = listOf(
        "Escolástico Basiliano (C.S.B.)",
        "Religioso Basiliano (C.S.B.)",
        "Sacerdote Basiliano (C.S.B.)",
        "Novicio Basiliano",
        "Postulante Basiliano",
        "Formador / Superior Basiliano",
        "Docente en Colegio Basiliano",
        "Laico Asociado Basiliano",
        "Joven de Pastoral Vocacional",
        "Fiel / Devoto de San Basilio"
    )

    // Sugerencias para vida religiosa y escolasticado
    val formationStageSuggestions = listOf(
        "Escolasticado (Filosofía)",
        "Escolasticado (Teología)",
        "Noviciado Canónico",
        "Postulantado",
        "Votos Temporales (Trienio)",
        "Profesión Solemne / Votos Perpetuos",
        "Diaconado en Tránsito",
        "Orden Sacerdotal (Presbítero)"
    )

    val religiousHouseSuggestions = listOf(
        "Casa de Formación San Basilio (Cali)",
        "Escolasticado San Basilio",
        "Parroquia Ntra. Sra. de la Asunción (Cali)",
        "Comunidad San Basilio (Medellín)",
        "Curia Generalicia (Toronto)",
        "Parroquia San Basilio (Bogotá)"
    )

    val monasticCommitmentSuggestions = listOf(
        "Liturgia de las Horas Comunitaria",
        "Oración de Jesús & Hesiquia",
        "Lectio Divina Matutina",
        "Servicio en la Basiliada de Caridad",
        "Docencia y Formación Juvenil"
    )

    // Sugerencias populares de santos patronos (el usuario puede escribir libremente cualquier otro)
    val popularPatronSuggestions = listOf(
        "San Basilio Magno",
        "Santa María Virgen",
        "San José",
        "San Judas Tadeo",
        "Santa Rita de Casia",
        "Divino Niño Jesús",
        "San Francisco de Asís",
        "Santa Teresita del Niño Jesús",
        "San Juan de Mata",
        "San Miguel Arcángel",
        "Beato Carlo Acutis",
        "San Benito",
        "Virgen del Carmen",
        "Virgen de Chiquinquirá",
        "San Charbel",
        "San Antonio de Padua"
    )

    // Diálogo para cambiar o conectar correo electrónico
    if (showSwitchEmailDialog) {
        var tempEmailInput by remember { mutableStateOf(currentProfile?.email ?: "") }
        AlertDialog(
            onDismissRequest = { showSwitchEmailDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF1877F2))
                    Text("Conectar con Correo", fontWeight = FontWeight.Bold, color = ShieldGreenDark)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Conecta la aplicación con tu correo personal para guardar tus publicaciones, tu foto de perfil y tu santo patrono, o para cambiar de usuario fácilmente.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )

                    FraternalOutlinedTextField(
                        value = tempEmailInput,
                        onValueChange = { tempEmailInput = it },
                        label = { Text("Tu Correo Electrónico") },
                        placeholder = { Text("correo@ejemplo.com") },
                        leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color(0xFF1877F2)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (tempEmailInput.trim().isNotEmpty()) {
                            viewModel.switchUserByEmail(tempEmailInput.trim())
                            showSwitchEmailDialog = false
                            activeTab = 1 // ir a Mi Perfil
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Conectar Perfil")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSwitchEmailDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo de Sincronización Multi-Celular (Nube & Código Fraterno)
    if (showMultiDeviceSyncDialog) {
        AlertDialog(
            onDismissRequest = {
                showMultiDeviceSyncDialog = false
                syncFeedbackMessage = null
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudSync,
                        contentDescription = null,
                        tint = if (isFirebaseAvailable) ShieldGreenDark else Color(0xFF1877F2)
                    )
                    Text(
                        text = "Sincronización Multi-Celular",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Estado de la nube
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isFirebaseAvailable) Color(0xFFE8F5E9) else Color(0xFFFFF8E1),
                        border = BorderStroke(
                            1.dp,
                            if (isFirebaseAvailable) ShieldGreen.copy(alpha = 0.5f) else Color(0xFFFFB300).copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (isFirebaseAvailable) ShieldGreenDark else Color(0xFFFFB300))
                            )
                            Column {
                                Text(
                                    text = if (isFirebaseAvailable) "Nube Firestore Conectada en Tiempo Real" else "Modo de Sincronización Local & Fraterna",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isFirebaseAvailable) ShieldGreenDark else Color(0xFF8D6E63)
                                )
                                Text(
                                    text = if (isFirebaseAvailable)
                                        "Los perfiles y publicaciones se transmiten automáticamente a todos los celulares."
                                    else
                                        "Puedes pasar todos tus datos de inmediato al otro celular copiando tu Código Fraterno o vinculando tu proyecto en la nube.",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }

                    if (syncFeedbackMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE3F2FD),
                            border = BorderStroke(1.dp, Color(0xFF90CAF9))
                        ) {
                            Text(
                                text = syncFeedbackMessage!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF0D47A1),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    // Botón 1: Sincronizar con la Nube
                    Button(
                        onClick = {
                            viewModel.syncAllCommunityToCloud { success, msg ->
                                syncFeedbackMessage = msg
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isFirebaseAvailable) ShieldGreenDark else Color(0xFF1877F2)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Subir y Sincronizar Todo a la Nube", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Divider(color = Color.LightGray.copy(alpha = 0.5f))

                    // SECCIÓN TRANSFERENCIA POR CÓDIGO FRATERNO (ENTRE CELULARES)
                    Text(
                        text = "Transferencia Directa entre Celulares:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = ShieldGreenDark
                    )
                    Text(
                        text = "Copia tu Código Fraterno y pégalo en el otro celular (por WhatsApp, mensaje o correo). No requiere internet ni configurar servidores:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        fontSize = 11.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val code = viewModel.exportFraternalSyncCode()
                                clipboardManager.setText(AnnotatedString(code))
                                syncFeedbackMessage = "¡Código de sincronización copiado al portapapeles! Pégalo en tu otro celular."
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copiar Código", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                showImportCodeField = !showImportCodeField
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Importar Código", fontSize = 12.sp)
                        }
                    }

                    if (showImportCodeField) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            FraternalOutlinedTextField(
                                value = importCodeText,
                                onValueChange = { importCodeText = it },
                                placeholder = { Text("Pega aquí el código copiado del otro celular...") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 4,
                                shape = RoundedCornerShape(10.dp)
                            )
                            Button(
                                onClick = {
                                    if (importCodeText.trim().isNotEmpty()) {
                                        viewModel.importFraternalSyncCode(importCodeText.trim()) { success, msg ->
                                            syncFeedbackMessage = msg
                                            if (success) {
                                                importCodeText = ""
                                                showImportCodeField = false
                                            }
                                        }
                                    }
                                },
                                enabled = importCodeText.trim().isNotEmpty(),
                                colors = ButtonDefaults.buttonColors(containerColor = ShieldGreen),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Cargar Datos del Otro Celular", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }

                    Divider(color = Color.LightGray.copy(alpha = 0.5f))

                    // Configurar Credenciales Firebase
                    TextButton(
                        onClick = {
                            showFirebaseConfigDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Configurar Proyecto Firebase en Nube", fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showMultiDeviceSyncDialog = false
                        syncFeedbackMessage = null
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cerrar")
                }
            }
        )
    }

    // Diálogo para Configuración de Credenciales de Firebase Cloud
    if (showFirebaseConfigDialog) {
        var inputProjectId by remember { mutableStateOf("") }
        var inputApiKey by remember { mutableStateOf("") }
        var inputAppId by remember { mutableStateOf("") }
        var configResult by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showFirebaseConfigDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Cloud, contentDescription = null, tint = ShieldGreenDark)
                    Text("Conectar Consola Firebase", fontWeight = FontWeight.Bold, color = ShieldGreenDark)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Si tienes un proyecto en Google Firebase Console, introduce tus datos para conectar la base de datos en tiempo real entre todos los celulares de la congregación.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )

                    if (configResult != null) {
                        Text(
                            text = configResult!!,
                            style = MaterialTheme.typography.labelSmall,
                            color = ShieldGreenDark,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    FraternalOutlinedTextField(
                        value = inputProjectId,
                        onValueChange = { inputProjectId = it },
                        label = { Text("Project ID (ej: basilianos-curia)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    FraternalOutlinedTextField(
                        value = inputApiKey,
                        onValueChange = { inputApiKey = it },
                        label = { Text("API Key de Firebase") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    FraternalOutlinedTextField(
                        value = inputAppId,
                        onValueChange = { inputAppId = it },
                        label = { Text("App ID de Firebase") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (inputProjectId.trim().isNotEmpty() && inputApiKey.trim().isNotEmpty() && inputAppId.trim().isNotEmpty()) {
                            viewModel.configureCustomFirebase(
                                inputProjectId.trim(),
                                inputApiKey.trim(),
                                inputAppId.trim()
                            ) { success, msg ->
                                configResult = msg
                                if (success) {
                                    showFirebaseConfigDialog = false
                                    syncFeedbackMessage = "¡Firebase configurado exitosamente y sincronizado!"
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Conectar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFirebaseConfigDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo de edición de perfil
    if (showEditProfileDialog) {
        val prof = currentProfile
        var editName by remember { mutableStateOf(prof?.fullName ?: "") }
        var editRole by remember { mutableStateOf(prof?.role ?: availableRoles.first()) }
        var editPatron by remember { mutableStateOf(prof?.patronSaint ?: "San Basilio Magno") }
        var editCity by remember { mutableStateOf(prof?.cityAndCountry ?: "") }
        var editParish by remember { mutableStateOf(prof?.parishOrInstitution ?: "") }
        var editBio by remember { mutableStateOf(prof?.bio ?: "") }
        var editQuote by remember { mutableStateOf(prof?.favoriteQuote ?: "") }
        var editFormationStage by remember { mutableStateOf(prof?.formationStage?.ifBlank { "Escolasticado (Filosofía & Teología)" } ?: "Escolasticado (Filosofía & Teología)") }
        var editReligiousHouse by remember { mutableStateOf(prof?.religiousHouse?.ifBlank { "Casa de Formación San Basilio (Cali)" } ?: "Casa de Formación San Basilio (Cali)") }
        var editProfessionDate by remember { mutableStateOf(prof?.professionDate ?: "") }
        var editScholasticStudies by remember { mutableStateOf(prof?.scholasticStudies ?: "") }
        var editMonasticCommitment by remember { mutableStateOf(prof?.monasticCommitment?.ifBlank { "Liturgia de las Horas Comunitaria" } ?: "Liturgia de las Horas Comunitaria") }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = {
                Text(
                    text = "Editar Perfil Basiliano & Religioso",
                    fontWeight = FontWeight.Bold,
                    color = ShieldGreenDark
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Botones directos para subir fotos desde el modal
                    Text(text = "Fotos del Perfil:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = ShieldGreenDark)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = {
                                profilePhotoLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF1877F2))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Foto Perfil", fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = {
                                coverPhotoLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp), tint = ShieldGreen)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Foto Portada", fontSize = 11.sp)
                        }
                    }

                    Divider(color = Color.LightGray.copy(alpha = 0.4f))

                    // SECCIÓN DE VIDA RELIGIOSA Y ESCOLASTICADO
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ShieldGold.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.School, contentDescription = null, tint = ShieldGoldDark, modifier = Modifier.size(18.dp))
                                Text(
                                    text = "Identidad Religiosa & Escolasticado",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ShieldGoldDark
                                )
                            }
                            Text(
                                text = "Configura tu etapa como escolástico, casa religiosa y estudios teológicos.",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.DarkGray
                            )
                        }
                    }

                    FraternalOutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Nombre y Apellidos") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Selector y edición de Rol
                    FraternalOutlinedTextField(
                        value = editRole,
                        onValueChange = { editRole = it },
                        label = { Text("Rol / Estado Eclesial") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(text = "Roles religiosos sugeridos:", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(availableRoles) { r ->
                            FilterChip(
                                selected = editRole == r,
                                onClick = { editRole = r },
                                label = { Text(r, fontSize = 11.sp) }
                            )
                        }
                    }

                    // Etapa de Formación Religiosa
                    FraternalOutlinedTextField(
                        value = editFormationStage,
                        onValueChange = { editFormationStage = it },
                        label = { Text("Etapa de Formación Religiosa") },
                        placeholder = { Text("Ej: Escolasticado (Filosofía & Teología)") },
                        leadingIcon = { Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = ShieldGoldDark) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(text = "Etapas sugeridas:", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(formationStageSuggestions) { s ->
                            FilterChip(
                                selected = editFormationStage.trim().equals(s, ignoreCase = true),
                                onClick = { editFormationStage = s },
                                label = { Text(s, fontSize = 11.sp) }
                            )
                        }
                    }

                    // Casa Religiosa / Comunidad
                    FraternalOutlinedTextField(
                        value = editReligiousHouse,
                        onValueChange = { editReligiousHouse = it },
                        label = { Text("Casa Religiosa / Comunidad de Asignación") },
                        placeholder = { Text("Ej: Casa de Formación San Basilio (Cali)") },
                        leadingIcon = { Icon(Icons.Default.HomeWork, contentDescription = null, tint = ShieldGreenDark) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(religiousHouseSuggestions) { h ->
                            FilterChip(
                                selected = editReligiousHouse.trim().equals(h, ignoreCase = true),
                                onClick = { editReligiousHouse = h },
                                label = { Text(h, fontSize = 11.sp) }
                            )
                        }
                    }

                    // Fecha de Profesión Religiosa o Entrada
                    FraternalOutlinedTextField(
                        value = editProfessionDate,
                        onValueChange = { editProfessionDate = it },
                        label = { Text("Fecha de Profesión Religiosa / Entrada") },
                        placeholder = { Text("Ej: 21 de Noviembre de 2023") },
                        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFF1877F2)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Estudios Filosóficos y Teológicos
                    FraternalOutlinedTextField(
                        value = editScholasticStudies,
                        onValueChange = { editScholasticStudies = it },
                        label = { Text("Estudios Escolásticos (Filosofía / Teología / Patrística)") },
                        placeholder = { Text("Ej: 3er Año Filosofía & Patrística Basiliana") },
                        leadingIcon = { Icon(Icons.Default.MenuBook, contentDescription = null, tint = ShieldGoldDark) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Compromiso Monástico / Litúrgico
                    FraternalOutlinedTextField(
                        value = editMonasticCommitment,
                        onValueChange = { editMonasticCommitment = it },
                        label = { Text("Compromiso Monástico / Apostólico Principal") },
                        placeholder = { Text("Ej: Liturgia de las Horas Comunitaria & Basiliada") },
                        leadingIcon = { Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = ShieldGreenDark) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(monasticCommitmentSuggestions) { c ->
                            FilterChip(
                                selected = editMonasticCommitment.trim().equals(c, ignoreCase = true),
                                onClick = { editMonasticCommitment = c },
                                label = { Text(c, fontSize = 11.sp) }
                            )
                        }
                    }

                    Divider(color = Color.LightGray.copy(alpha = 0.4f))

                    // Campo de texto LIBRE para Santo Patrono
                    FraternalOutlinedTextField(
                        value = editPatron,
                        onValueChange = { editPatron = it },
                        label = { Text("Santo Patrono Protector (Escribe el tuyo)") },
                        placeholder = { Text("Ej: San Basilio Magno, San Judas Tadeo, Virgen del Carmen...") },
                        leadingIcon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ShieldGoldDark) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(text = "Sugerencias rápidas (toca para elegir o escribe libremente):", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(popularPatronSuggestions) { s ->
                            FilterChip(
                                selected = editPatron.trim().equals(s, ignoreCase = true),
                                onClick = { editPatron = s },
                                label = { Text(s, fontSize = 11.sp) }
                            )
                        }
                    }

                    FraternalOutlinedTextField(
                        value = editParish,
                        onValueChange = { editParish = it },
                        label = { Text("Parroquia / Colegio / Institución") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    FraternalOutlinedTextField(
                        value = editCity,
                        onValueChange = { editCity = it },
                        label = { Text("Ciudad y País") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    FraternalOutlinedTextField(
                        value = editQuote,
                        onValueChange = { editQuote = it },
                        label = { Text("Lema o Frase Espiritual Favorita") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    FraternalOutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Biografía / Testimonio") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (prof != null) {
                            val updated = prof.copy(
                                fullName = editName.ifBlank { prof.fullName },
                                role = editRole.ifBlank { prof.role },
                                patronSaint = editPatron.ifBlank { prof.patronSaint },
                                parishOrInstitution = editParish.ifBlank { prof.parishOrInstitution },
                                cityAndCountry = editCity.ifBlank { prof.cityAndCountry },
                                bio = editBio.ifBlank { prof.bio },
                                favoriteQuote = editQuote.ifBlank { prof.favoriteQuote },
                                formationStage = editFormationStage.trim(),
                                religiousHouse = editReligiousHouse.trim(),
                                professionDate = editProfessionDate.trim(),
                                scholasticStudies = editScholasticStudies.trim(),
                                monasticCommitment = editMonasticCommitment.trim()
                            )
                            viewModel.saveUserProfile(updated)
                        }
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ShieldGreenDark)
                ) {
                    Text("Guardar Cambios")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
    ) {
        // Cabecera superior con marca fraterna persistente
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        BasilianCrestView(size = 38.dp, showMottoText = false)
                        Column {
                            Text(
                                text = "Comunidad Basiliana",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1877F2), // Azul Facebook oficial
                                fontFamily = FontFamily.SansSerif
                            )
                            Text(
                                text = "Red Fraterna de Fe • Estilo Red Social",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Botón de sincronización multi-celular directa y nube
                        IconButton(
                            onClick = { showMultiDeviceSyncDialog = true },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (isFirebaseAvailable) Icons.Default.CloudDone else Icons.Default.Sync,
                                contentDescription = "Sincronizar Celulares",
                                tint = if (isFirebaseAvailable) ShieldGreenDark else Color(0xFF1877F2),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        if (currentProfile != null) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = ShieldGreen.copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, ShieldGreen.copy(alpha = 0.3f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(ShieldGreen)
                                    )
                                    Text(
                                        text = currentProfile!!.fullName.split(" ").firstOrNull() ?: "En línea",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ShieldGreenDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Pestañas de navegación fraterna
                ScrollableTabRow(
                    selectedTabIndex = activeTab,
                    containerColor = Color.White,
                    contentColor = Color(0xFF1877F2),
                    edgePadding = 8.dp
                ) {
                    Tab(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        text = { Text("Muro Fraterno", fontWeight = if (activeTab == 0) FontWeight.Bold else FontWeight.Normal) },
                        icon = { Icon(Icons.Default.DynamicFeed, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        text = { Text("Mi Perfil", fontWeight = if (activeTab == 1) FontWeight.Bold else FontWeight.Normal) },
                        icon = { Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = activeTab == 2,
                        onClick = { activeTab = 2 },
                        text = { Text("Hermanos & Devotos (${allProfiles.size})", fontWeight = if (activeTab == 2) FontWeight.Bold else FontWeight.Normal) },
                        icon = { Icon(Icons.Default.PeopleAlt, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Caso: No tiene perfil y quiere crear uno (Registro con Correo)
            if (currentProfile == null && activeTab != 2) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.5.dp, Color(0xFF1877F2).copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1877F2).copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Color(0xFF1877F2), modifier = Modifier.size(30.dp))
                        }

                        Text(
                            text = "¡Crea tu Perfil Tipo Red Social!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Ingresa con tu correo electrónico y configura tu rol basiliano, tu santo patrono protector, tu parroquia y tu lema personal para compartir con la congregación.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center
                        )

                        FraternalOutlinedTextField(
                            value = inputEmail,
                            onValueChange = { inputEmail = it },
                            label = { Text("Correo Electrónico") },
                            placeholder = { Text("mi_correo@ejemplo.com") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF1877F2)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        FraternalOutlinedTextField(
                            value = inputFullName,
                            onValueChange = { inputFullName = it },
                            label = { Text("Nombre y Apellidos") },
                            placeholder = { Text("Ej: Hno. Carlos Mendoza") },
                            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = Color(0xFF1877F2)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Selector de Rol
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Tu Rol en la Comunidad:", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(availableRoles) { r ->
                                    FilterChip(
                                        selected = inputRole == r,
                                        onClick = { inputRole = r },
                                        label = { Text(r, fontSize = 11.sp) }
                                    )
                                }
                            }
                        }

                        // Selector y edición LIBRE de Santo Patrono
                        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            FraternalOutlinedTextField(
                                value = inputPatronSaint,
                                onValueChange = { inputPatronSaint = it },
                                label = { Text("Santo Patrono Protector (Escribe el que desees)") },
                                placeholder = { Text("Ej: San Basilio Magno, San Judas Tadeo, Santa Rita...") },
                                leadingIcon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ShieldGoldDark) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Text(
                                text = "Sugerencias populares (toca para elegir o escribe libremente):",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(popularPatronSuggestions) { p ->
                                    FilterChip(
                                        selected = inputPatronSaint.trim().equals(p, ignoreCase = true),
                                        onClick = { inputPatronSaint = p },
                                        label = { Text(p, fontSize = 11.sp) }
                                    )
                                }
                            }
                        }

                        FraternalOutlinedTextField(
                            value = inputParish,
                            onValueChange = { inputParish = it },
                            label = { Text("Parroquia / Institución / Obra") },
                            placeholder = { Text("Ej: Parroquia San Basilio, Cali") },
                            leadingIcon = { Icon(Icons.Default.Church, contentDescription = null, tint = ShieldGreen) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        FraternalOutlinedTextField(
                            value = inputCity,
                            onValueChange = { inputCity = it },
                            label = { Text("Ciudad y País") },
                            placeholder = { Text("Ej: Medellín, Colombia") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = ShieldRed) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Button(
                            onClick = {
                                if (inputEmail.trim().isNotEmpty() && inputFullName.trim().isNotEmpty()) {
                                    val newProf = UserCommunityProfile(
                                        id = "user_${System.currentTimeMillis()}",
                                        email = inputEmail.trim(),
                                        fullName = inputFullName.trim(),
                                        role = inputRole,
                                        patronSaint = inputPatronSaint,
                                        cityAndCountry = inputCity.trim(),
                                        parishOrInstitution = inputParish.trim(),
                                        bio = inputBio.trim(),
                                        favoriteQuote = inputQuote.trim()
                                    )
                                    viewModel.saveUserProfile(newProf)
                                    activeTab = 1 // Ir al perfil
                                }
                            },
                            enabled = inputEmail.trim().isNotEmpty() && inputFullName.trim().isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Crear Mi Perfil Fraterno", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // USUARIO CON PERFIL CONFIGURADO
            when (activeTab) {
                0 -> {
                    // =====================================================================
                    // PESTAÑA 0: MURO FRATERNO (ESTILO FACEBOOK FEED)
                    // =====================================================================

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            CraftedSectionHeader(
                                title = "Muro Fraterno de la Comunidad",
                                subtitle = "Publicaciones, testimonios y oraciones que se comparten entre todos los celulares",
                                emoji = "🕊️",
                                accentColor = Color(0xFF1877F2),
                                underlineWidth = 160.dp
                            )
                        }
                    }

                    // Banner de Estado de Sincronización Multi-Celular
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isFirebaseAvailable) Color(0xFFE8F5E9) else Color(0xFFFFF8E1)
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isFirebaseAvailable) ShieldGreen.copy(alpha = 0.4f) else Color(0xFFFFB300).copy(alpha = 0.4f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (isFirebaseAvailable) Icons.Default.CloudDone else Icons.Default.Sync,
                                        contentDescription = null,
                                        tint = if (isFirebaseAvailable) ShieldGreenDark else Color(0xFFE65100),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = if (isFirebaseAvailable)
                                            "Nube activa • Publicaciones sincronizadas en todos los celulares"
                                        else
                                            "Modo Fraterno • Toca para sincronizar con tu otro celular",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.5.sp,
                                        color = if (isFirebaseAvailable) ShieldGreenDark else Color(0xFF5D4037),
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                TextButton(
                                    onClick = { showMultiDeviceSyncDialog = true },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Sincronizar",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isFirebaseAvailable) ShieldGreenDark else Color(0xFFE65100)
                                    )
                                }
                            }
                        }
                    }

                    // Fila de Miembros Activos ("En Comunión Viva")
                    val activeProfiles = allProfiles.filter { it.isOnline }
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(1.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(9.dp)
                                                .background(Color(0xFF22C55E), CircleShape)
                                        )
                                        Text(
                                            text = "En Comunión Activa (${activeProfiles.size})",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E293B)
                                        )
                                    }
                                    Text(
                                        text = "Tiempo Real • Firestore",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF15803D),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 10.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(allProfiles) { prof ->
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.width(62.dp)
                                        ) {
                                            Box(modifier = Modifier.size(48.dp)) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(44.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFFE7F3FF))
                                                        .border(
                                                            width = if (prof.isOnline) 2.dp else 1.dp,
                                                            color = if (prof.isOnline) Color(0xFF22C55E) else Color(0xFFCBD5E1),
                                                            shape = CircleShape
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    if (prof.profilePhotoUri != null) {
                                                        AsyncImage(
                                                            model = prof.profilePhotoUri,
                                                            contentDescription = prof.fullName,
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier.fillMaxSize()
                                                        )
                                                    } else {
                                                        Icon(
                                                            Icons.Default.Person,
                                                            contentDescription = null,
                                                            tint = Color(0xFF1877F2),
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                    }
                                                }
                                                // Punto indicador visual de presencia
                                                Box(
                                                    modifier = Modifier
                                                        .size(13.dp)
                                                        .align(Alignment.BottomEnd)
                                                        .background(
                                                            color = if (prof.isOnline) Color(0xFF22C55E) else Color(0xFF94A3B8),
                                                            shape = CircleShape
                                                        )
                                                        .border(2.dp, Color.White, CircleShape)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = prof.fullName.split(" ").firstOrNull() ?: prof.fullName,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 10.sp,
                                                fontWeight = if (prof.isOnline) FontWeight.Bold else FontWeight.Normal,
                                                color = if (prof.isOnline) Color(0xFF1E293B) else Color(0xFF64748B),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Caja de Publicación Estilo Facebook ("¿Qué tienes en mente?")
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // Avatar
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(ShieldGreen.copy(alpha = 0.2f))
                                            .border(1.5.dp, Color(0xFF1877F2), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (currentProfile?.profilePhotoUri != null) {
                                            AsyncImage(
                                                model = currentProfile!!.profilePhotoUri,
                                                contentDescription = currentProfile!!.fullName,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        } else {
                                            Icon(Icons.Default.Person, contentDescription = null, tint = ShieldGreenDark)
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = currentProfile?.fullName ?: "Hermano Basiliano",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E293B)
                                        )
                                        Text(
                                            text = "${currentProfile?.role} • Santo: ${currentProfile?.patronSaint?.split(" ")?.take(2)?.joinToString(" ")}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.Gray,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                FraternalOutlinedTextField(
                                    value = postText,
                                    onValueChange = { postText = it },
                                    placeholder = { Text("Comparte una reflexión, intención de oración o testimonio fraterno...") },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3,
                                    shape = RoundedCornerShape(12.dp)
                                )

                                if (postImageUri != null) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                                    ) {
                                        AsyncImage(
                                            model = postImageUri,
                                            contentDescription = "Foto adjunta",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        IconButton(
                                            onClick = { postImageUri = null },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(6.dp)
                                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                                .size(28.dp)
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = "Quitar foto", tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }

                                Divider(color = Color(0xFFE2E8F0))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    FilledTonalButton(
                                        onClick = {
                                            postImageLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        },
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = Color(0xFFE7F3FF),
                                            contentColor = Color(0xFF1877F2)
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Añadir Foto", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    }

                                    Button(
                                        onClick = {
                                            if (postText.trim().isNotEmpty() || postImageUri != null) {
                                                isPosting = true
                                                viewModel.publishPost(
                                                    content = postText.trim(),
                                                    imageUri = postImageUri
                                                )
                                                postText = ""
                                                postImageUri = null
                                                isPosting = false
                                            }
                                        },
                                        enabled = (postText.trim().isNotEmpty() || postImageUri != null) && !isPosting,
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Publicar", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Feed de Publicaciones
                    items(posts, key = { it.id }) { post ->
                        val authorProfile = allProfiles.find { it.id == post.authorId || it.email.equals(post.authorId, ignoreCase = true) }
                        val isOnline = authorProfile?.isOnline ?: (post.authorId == currentProfile?.id)
                        FacebookPostCard(
                            post = post,
                            onLike = { viewModel.toggleLikePost(post.id) },
                            onPray = { viewModel.addPrayerIntention(post.id) },
                            onDelete = if (post.authorId == currentProfile?.id || viewModel.curiaIsAuthenticated.value) {
                                { viewModel.deletePost(post.id) }
                            } else null,
                            isAuthorOnline = isOnline
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                1 -> {
                    // =====================================================================
                    // PESTAÑA 1: MI PERFIL ESTILO FACEBOOK (PORTADA + AVATAR + DETALLES)
                    // =====================================================================
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            CraftedSectionHeader(
                                title = "Mi Perfil Basiliano",
                                subtitle = "Identidad personal, votos y santo patrono protector",
                                emoji = "👤",
                                accentColor = ShieldGreenDark,
                                underlineWidth = 140.dp
                            )
                        }
                    }

                    item {
                        FacebookProfileHeader(
                            profile = currentProfile!!,
                            onEditProfile = { showEditProfileDialog = true },
                            onSwitchEmail = { showSwitchEmailDialog = true },
                            onChangeAvatar = {
                                profilePhotoLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            onChangeCover = {
                                coverPhotoLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            onLogout = {
                                viewModel.logoutUserProfile()
                            }
                        )
                    }

                    // Publicaciones de este usuario
                    val myPosts = posts.filter { it.authorId == currentProfile?.id }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            CraftedSectionHeader(
                                title = "Mis Publicaciones (${myPosts.size})",
                                subtitle = "Testimonios e intenciones compartidas con la comunidad",
                                emoji = "📜",
                                accentColor = ShieldGoldDark,
                                underlineWidth = 130.dp
                            )
                        }
                    }

                    items(myPosts, key = { it.id }) { post ->
                        FacebookPostCard(
                            post = post,
                            onLike = { viewModel.toggleLikePost(post.id) },
                            onPray = { viewModel.addPrayerIntention(post.id) },
                            onDelete = { viewModel.deletePost(post.id) },
                            isAuthorOnline = currentProfile?.isOnline ?: true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                2 -> {
                    // =====================================================================
                    // PESTAÑA 2: DIRECTORIO DE HERMANOS Y DEVOTOS
                    // =====================================================================
                    item {
                        Surface(
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            shape = RoundedCornerShape(14.dp),
                            shadowElevation = 2.dp
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                CraftedSectionHeader(
                                    title = "Directorio de Hermanos & Devotos",
                                    subtitle = "Religiosos, sacerdotes, formadores y laicos consagrados (${allProfiles.size} miembros)",
                                    emoji = "👥",
                                    accentColor = ShieldGreenDark,
                                    underlineWidth = 180.dp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Todos los perfiles creados en cualquier celular se sincronizan aquí para que podamos orar y caminar en fraternidad viva.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    fontSize = 11.5.sp
                                )
                            }
                        }
                    }

                    val sortedProfiles = allProfiles.sortedWith(
                        compareByDescending<UserCommunityProfile> { it.isOnline }
                            .thenBy { it.fullName }
                    )
                    items(sortedProfiles, key = { it.id }) { prof ->
                        CommunityMemberCard(profile = prof)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
}

// =============================================================================
// COMPONENTES ESTILO FACEBOOK
// =============================================================================

@Composable
fun FacebookProfileHeader(
    profile: UserCommunityProfile,
    onEditProfile: () -> Unit,
    onSwitchEmail: () -> Unit,
    onChangeAvatar: () -> Unit,
    onChangeCover: () -> Unit,
    onLogout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column {
            // Foto de Portada con botón de cámara
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                if (profile.coverPhotoUri != null) {
                    AsyncImage(
                        model = profile.coverPhotoUri,
                        contentDescription = "Portada de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF1877F2), ShieldGreenDark, ShieldGoldDark)
                                )
                            )
                    )
                }

                // Botón cambiar portada
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.55f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .clickable(onClick = onChangeCover)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Text("Cambiar Portada", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Avatar + Información Central
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar circular que se superpone a la portada
                Box(
                    modifier = Modifier
                        .offset(y = (-50).dp)
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(4.dp, Color.White, CircleShape)
                        .clickable(onClick = onChangeAvatar),
                    contentAlignment = Alignment.Center
                ) {
                    if (profile.profilePhotoUri != null) {
                        AsyncImage(
                            model = profile.profilePhotoUri,
                            contentDescription = profile.fullName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFE7F3FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF1877F2), modifier = Modifier.size(54.dp))
                        }
                    }

                    // Icono de cámara para cambiar foto
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1877F2))
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }

                // Nombre y verificación
                Column(
                    modifier = Modifier
                        .offset(y = (-40).dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = profile.fullName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Icon(Icons.Default.Verified, contentDescription = "Verificado", tint = Color(0xFF1877F2), modifier = Modifier.size(20.dp))
                    }

                    // Indicador de presencia en tiempo real (Firestore)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (profile.isOnline) Color(0xFFDCFCE7) else Color(0xFFF1F5F9),
                        border = BorderStroke(1.dp, if (profile.isOnline) Color(0xFF86EFAC) else Color(0xFFE2E8F0)),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = if (profile.isOnline) Color(0xFF22C55E) else Color(0xFF94A3B8),
                                        shape = CircleShape
                                    )
                            )
                            Text(
                                text = if (profile.isOnline) "En línea ahora • Sincronizado en Vivo" else profile.presenceStatusText,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (profile.isOnline) Color(0xFF15803D) else Color(0xFF64748B),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Correo Conectado (Toca para cambiar o conectar otro)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable(onClick = onSwitchEmail)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF1877F2), modifier = Modifier.size(14.dp))
                            Text(
                                text = profile.email,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF334155),
                                fontWeight = FontWeight.Medium
                            )
                            Icon(Icons.Default.Sync, contentDescription = "Cambiar cuenta", tint = Color.Gray, modifier = Modifier.size(13.dp))
                        }
                    }

                    // Botón con sentido real para el correo: Enviar/Respaldar Ficha a la bandeja de entrada
                    val profileContext = LocalContext.current
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:${profile.email}")
                                putExtra(Intent.EXTRA_SUBJECT, "Ficha Basiliana C.S.B. - ${profile.fullName}")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    """
                                    🕊️ CONGREGACIÓN DE SAN BASILIO (C.S.B.)
                                    Ficha Fraterna y Vocacional:
                                    
                                    Nombre: ${profile.fullName}
                                    Correo Registrado: ${profile.email}
                                    Rol: ${profile.role}
                                    ${if (profile.formationStage.isNotBlank()) "Etapa de Formación: ${profile.formationStage}\n" else ""}
                                    Santo Patrono: ${profile.patronSaint}
                                    Comunidad: ${profile.parishOrInstitution} (${profile.cityAndCountry})
                                    Lema de Vida: ${profile.favoriteQuote}
                                    
                                    «Bonitatem et disciplinam et scientiam doce me»
                                    """.trimIndent()
                                )
                            }
                            try {
                                profileContext.startActivity(Intent.createChooser(intent, "Enviar ficha a mi correo"))
                            } catch (_: Exception) {}
                        },
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .height(32.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = Color(0xFF1877F2), modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Enviar Ficha a mi Correo", fontSize = 11.sp, color = Color(0xFF1877F2), fontWeight = FontWeight.SemiBold)
                    }

                    // Rol Insignia
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1877F2).copy(alpha = 0.1f),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = profile.role,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1877F2),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    // Santo Patrono (Con botón directo de editar)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ShieldGold.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .clickable(onClick = onEditProfile)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ShieldGoldDark, modifier = Modifier.size(15.dp))
                            Text(
                                text = "Santo Patrono: ${profile.patronSaint}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = ShieldGoldDark
                            )
                            Icon(Icons.Default.Edit, contentDescription = "Editar Santo", tint = ShieldGoldDark, modifier = Modifier.size(13.dp))
                        }
                    }

                    // Tarjeta de Vida Consagrada & Escolasticado Basiliano
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFFAF8F2),
                        border = BorderStroke(1.dp, ShieldGold.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .clickable(onClick = onEditProfile)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.School, contentDescription = null, tint = ShieldGoldDark, modifier = Modifier.size(18.dp))
                                    Text(
                                        text = "Vida Consagrada & Escolasticado C.S.B.",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = ShieldGoldDark
                                    )
                                }
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = ShieldGoldDark, modifier = Modifier.size(14.dp))
                            }

                            if (profile.formationStage.isNotBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = ShieldGreenDark, modifier = Modifier.size(14.dp))
                                    Text(
                                        text = "Etapa: ${profile.formationStage}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1E293B)
                                    )
                                }
                            }

                            if (profile.religiousHouse.isNotBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.HomeWork, contentDescription = null, tint = ShieldGreenDark, modifier = Modifier.size(14.dp))
                                    Text(
                                        text = "Casa: ${profile.religiousHouse}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF334155)
                                    )
                                }
                            }

                            if (profile.professionDate.isNotBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFF1877F2), modifier = Modifier.size(14.dp))
                                    Text(
                                        text = "Profesión / Entrada: ${profile.professionDate}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF334155)
                                    )
                                }
                            }

                            if (profile.scholasticStudies.isNotBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = ShieldGoldDark, modifier = Modifier.size(14.dp))
                                    Text(
                                        text = "Estudios: ${profile.scholasticStudies}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF334155)
                                    )
                                }
                            }

                            if (profile.monasticCommitment.isNotBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = ShieldGreenDark, modifier = Modifier.size(14.dp))
                                    Text(
                                        text = "Compromiso: ${profile.monasticCommitment}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF334155)
                                    )
                                }
                            }
                        }
                    }

                    // Botones directos para subir fotos en el perfil
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onChangeAvatar,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1877F2))
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Subir Foto Perfil", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onChangeCover,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ShieldGreenDark)
                        ) {
                            Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Cambiar Portada", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Ubicación & Parroquia
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (profile.parishOrInstitution.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                Icon(Icons.Default.Church, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                Text(text = profile.parishOrInstitution, style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
                            }
                        }
                        if (profile.cityAndCountry.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                Text(text = profile.cityAndCountry, style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
                            }
                        }
                    }

                    // Lema espiritual / Frase
                    if (profile.favoriteQuote.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                text = profile.favoriteQuote,
                                style = MaterialTheme.typography.bodySmall,
                                fontStyle = FontStyle.Italic,
                                color = ShieldGreenDark,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    // Biografía
                    if (profile.bio.isNotBlank()) {
                        Text(
                            text = profile.bio,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF475569),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }

                    // Botones de acción estilo Facebook
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onEditProfile,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1.2f)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Editar Perfil", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        FilledTonalButton(
                            onClick = onSwitchEmail,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1.1f)
                        ) {
                            Icon(Icons.Default.SwitchAccount, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Conectar", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }

                        IconButton(
                            onClick = onLogout,
                            modifier = Modifier.background(ShieldRed.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                        ) {
                            Icon(Icons.Default.Logout, contentDescription = "Cerrar sesión", tint = ShieldRed)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FacebookPostCard(
    post: CommunityPost,
    onLike: () -> Unit,
    onPray: () -> Unit,
    onDelete: (() -> Unit)? = null,
    isAuthorOnline: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Cabecera del post
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(modifier = Modifier.size(46.dp)) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE7F3FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (post.authorPhotoUri != null) {
                                AsyncImage(
                                    model = post.authorPhotoUri,
                                    contentDescription = post.authorName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF1877F2), modifier = Modifier.size(24.dp))
                            }
                        }
                        if (isAuthorOnline) {
                            Box(
                                modifier = Modifier
                                    .size(13.dp)
                                    .align(Alignment.BottomEnd)
                                    .background(Color(0xFF22C55E), CircleShape)
                                    .border(2.dp, Color.White, CircleShape)
                            )
                        }
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = post.authorName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF1877F2), modifier = Modifier.size(14.dp))
                            if (isAuthorOnline) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFDCFCE7),
                                    border = BorderStroke(0.5.dp, Color(0xFF86EFAC))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Box(modifier = Modifier.size(5.dp).background(Color(0xFF22C55E), CircleShape))
                                        Text("En línea", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                                    }
                                }
                            }
                        }
                        Text(
                            text = "${post.authorRole} • ${post.authorPatronSaint.split(" ").take(2).joinToString(" ")}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = post.timeAgo,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.LightGray,
                            fontSize = 10.sp
                        )
                    }
                }

                if (onDelete != null) {
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = ShieldRed, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // Contenido de texto
            if (post.content.isNotBlank()) {
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF334155),
                    lineHeight = 20.sp
                )
            }

            // Imagen adjunta a la publicación
            if (post.imageUri != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 180.dp, max = 320.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    AsyncImage(
                        model = post.imageUri,
                        contentDescription = "Foto compartida",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Divider(color = Color(0xFFF1F5F9))

            // Barra de interacciones estilo Facebook (Amén, Orar, Compartir)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón Amén (Like)
                TextButton(
                    onClick = onLike,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (post.isLikedByMe) Color(0xFF1877F2) else Color.DarkGray
                    )
                ) {
                    Icon(
                        imageVector = if (post.isLikedByMe) Icons.Default.ThumbUp else Icons.Default.ThumbUpOffAlt,
                        contentDescription = "Me gusta",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (post.likesCount > 0) "Amén (${post.likesCount})" else "Amén",
                        fontWeight = if (post.isLikedByMe) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp
                    )
                }

                // Botón Me Uno en Oración
                TextButton(
                    onClick = onPray,
                    colors = ButtonDefaults.textButtonColors(contentColor = ShieldGreenDark)
                ) {
                    Icon(Icons.Default.VolunteerActivism, contentDescription = "Oración", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (post.prayerCount > 0) "Oración (${post.prayerCount})" else "Orar",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CommunityMemberCard(profile: UserCommunityProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.5.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(54.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE7F3FF))
                        .border(1.5.dp, Color(0xFF1877F2), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (profile.profilePhotoUri != null) {
                        AsyncImage(
                            model = profile.profilePhotoUri,
                            contentDescription = profile.fullName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF1877F2), modifier = Modifier.size(28.dp))
                    }
                }
                // Punto indicador visual de presencia en tiempo real de Firestore
                Box(
                    modifier = Modifier
                        .size(15.dp)
                        .align(Alignment.BottomEnd)
                        .background(
                            color = if (profile.isOnline) Color(0xFF22C55E) else Color(0xFF94A3B8),
                            shape = CircleShape
                        )
                        .border(2.dp, Color.White, CircleShape)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile.fullName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = profile.role,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF1877F2),
                        fontWeight = FontWeight.SemiBold
                    )
                    FraternalActiveBadge(
                        isOnline = profile.isOnline,
                        statusText = profile.presenceStatusText,
                        showLabel = true
                    )
                }
                Text(
                    text = "Patrono: ${profile.patronSaint} • ${profile.cityAndCountry}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
                if (profile.favoriteQuote.isNotBlank()) {
                    Text(
                        text = profile.favoriteQuote,
                        style = MaterialTheme.typography.labelSmall,
                        fontStyle = FontStyle.Italic,
                        color = ShieldGreenDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
