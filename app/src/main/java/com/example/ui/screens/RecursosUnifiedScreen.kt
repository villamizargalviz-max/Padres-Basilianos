package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.ApuntesChapter
import com.example.data.model.ApuntesSection
import com.example.data.model.AppLanguage
import com.example.data.repository.ApuntesColombiaData
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import com.example.ui.components.BasilianCrestView
import com.example.ui.components.FuturisticDesignPalette
import com.example.ui.components.FuturisticQuoteCalloutCard
import com.example.ui.components.futuristic3DCard
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

enum class RecursosSubTab(val icon: ImageVector) {
    FUNDADORES_PATRONOS(Icons.Default.AutoAwesome),
    HISTORIA_LIBROS(Icons.Default.MenuBook),
    REGLA_POLITICAS(Icons.Default.Shield),
    CALENDARIO_ORACIONES(Icons.Default.CalendarMonth)
}

fun RecursosSubTab.getLocalizedLabel(strings: AppStrings): String {
    return when (this) {
        RecursosSubTab.HISTORIA_LIBROS -> strings.recursos.subTabHistory
        RecursosSubTab.FUNDADORES_PATRONOS -> strings.recursos.subTabFounders
        RecursosSubTab.REGLA_POLITICAS -> strings.recursos.subTabRule
        RecursosSubTab.CALENDARIO_ORACIONES -> strings.recursos.subTabCalendar
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecursosUnifiedScreen(
    viewModel: BasilianViewModel,
    initialSubTab: RecursosSubTab = RecursosSubTab.FUNDADORES_PATRONOS,
    activeAppTab: com.example.ui.viewmodel.AppTab = com.example.ui.viewmodel.AppTab.RECURSOS,
    appLanguage: AppLanguage = AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableStateOf(initialSubTab) }
    val strings = remember(appLanguage) { AppStrings.get(appLanguage) }

    val context = LocalContext.current
    val appTheme by viewModel.currentAppTheme.collectAsState()
    val customRecursosImage by viewModel.customRecursosImageUri.collectAsState()
    val recursosImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.setCustomRecursosImage(uri)
        }
    }

    // Sub-segmentos internos para cada sección (siempre visibles en todos los celulares)
    var historiaSegment by remember { mutableStateOf(0) } // 0: Colombia (1987-1997), 1: Libro 1922, 2: Cronología (379-2026)
    var fundadoresSegment by remember { mutableStateOf(0) } // 0: Santos Patronos, 1: 10 Fundadores, 2: Galería
    var reglaSegment by remember { mutableStateOf(0) } // 0: Forma de Vida, 1: Costumbres 2022, 2: Políticas 2026
    var devocionarioSegment by remember { mutableStateOf(0) } // 0: Santo de Cada Día, 1: Calendario Litúrgico, 2: Oraciones Basilianas
    val customGallerySlots by viewModel.customGallerySlots.collectAsState()

    // Sincronizar con la pestaña solicitada desde Inicio o Barra Inferior
    LaunchedEffect(activeAppTab) {
        when (activeAppTab) {
            com.example.ui.viewmodel.AppTab.PATRONOS -> {
                selectedSubTab = RecursosSubTab.FUNDADORES_PATRONOS
                fundadoresSegment = 0
            }
            com.example.ui.viewmodel.AppTab.FUNDADORES -> {
                selectedSubTab = RecursosSubTab.FUNDADORES_PATRONOS
                fundadoresSegment = 1
            }
            com.example.ui.viewmodel.AppTab.HISTORIA -> {
                selectedSubTab = RecursosSubTab.HISTORIA_LIBROS
                historiaSegment = 0
            }
            com.example.ui.viewmodel.AppTab.LIBRO_1922 -> {
                selectedSubTab = RecursosSubTab.HISTORIA_LIBROS
                historiaSegment = 1
            }
            com.example.ui.viewmodel.AppTab.FORMA_DE_VIDA -> {
                selectedSubTab = RecursosSubTab.REGLA_POLITICAS
                reglaSegment = 0
            }
            com.example.ui.viewmodel.AppTab.COSTUMBRES -> {
                selectedSubTab = RecursosSubTab.REGLA_POLITICAS
                reglaSegment = 1
            }
            com.example.ui.viewmodel.AppTab.POLITICAS_2026 -> {
                selectedSubTab = RecursosSubTab.REGLA_POLITICAS
                reglaSegment = 2
            }
            com.example.ui.viewmodel.AppTab.ORACIONES, com.example.ui.viewmodel.AppTab.CALENDARIO -> {
                selectedSubTab = RecursosSubTab.CALENDARIO_ORACIONES
            }
            else -> {}
        }
    }

    val allSubTabs = remember {
        listOf(
            RecursosSubTab.FUNDADORES_PATRONOS,
            RecursosSubTab.HISTORIA_LIBROS,
            RecursosSubTab.REGLA_POLITICAS,
            RecursosSubTab.CALENDARIO_ORACIONES
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Selector superior compacto 2x2 adaptable a CUALQUIER tamaño de celular (sin recortes horizontales)
        Surface(
            color = if (appTheme.isDark) appTheme.surfaceColor else appTheme.primaryColor,
            tonalElevation = 3.dp,
            border = BorderStroke(0.8.dp, appTheme.goldColor.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    allSubTabs.take(2).forEach { tab ->
                        val isSelected = selectedSubTab == tab
                        val shortLabel = when (tab) {
                            RecursosSubTab.FUNDADORES_PATRONOS -> when (appLanguage) {
                                AppLanguage.EN -> "Patrons & Founders"
                                AppLanguage.FR -> "Patrons & Fondateurs"
                                AppLanguage.ES -> "Patronos y Fundadores"
                            }
                            RecursosSubTab.HISTORIA_LIBROS -> when (appLanguage) {
                                AppLanguage.EN -> "History & Books"
                                AppLanguage.FR -> "Histoire & Livres"
                                AppLanguage.ES -> "Historia y Libros"
                            }
                            else -> tab.getLocalizedLabel(strings)
                        }
                        Surface(
                            onClick = { selectedSubTab = tab },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) appTheme.goldColor.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.07f),
                            border = BorderStroke(
                                width = if (isSelected) 1.4.dp else 0.8.dp,
                                color = if (isSelected) appTheme.goldColor else Color.White.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("recursos_tab_${tab.name.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp),
                                    tint = if (isSelected) appTheme.goldColor else Color.White.copy(alpha = 0.8f)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = shortLabel,
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                    color = if (isSelected) appTheme.goldColor else Color.White.copy(alpha = 0.9f),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    allSubTabs.drop(2).forEach { tab ->
                        val isSelected = selectedSubTab == tab
                        val shortLabel = when (tab) {
                            RecursosSubTab.REGLA_POLITICAS -> when (appLanguage) {
                                AppLanguage.EN -> "Way of Life, Customs & Policies"
                                AppLanguage.FR -> "Forme de Vie, Coutumes & Politiques"
                                AppLanguage.ES -> "Forma de Vida, Costumbres y Políticas"
                            }
                            RecursosSubTab.CALENDARIO_ORACIONES -> when (appLanguage) {
                                AppLanguage.EN -> "Saints & Prayers"
                                AppLanguage.FR -> "Saints & Prières"
                                AppLanguage.ES -> "Santoral y Oraciones"
                            }
                            else -> tab.getLocalizedLabel(strings)
                        }
                        Surface(
                            onClick = { selectedSubTab = tab },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) appTheme.goldColor.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.07f),
                            border = BorderStroke(
                                width = if (isSelected) 1.4.dp else 0.8.dp,
                                color = if (isSelected) appTheme.goldColor else Color.White.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("recursos_tab_${tab.name.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp),
                                    tint = if (isSelected) appTheme.goldColor else Color.White.copy(alpha = 0.8f)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = shortLabel,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                    color = if (isSelected) appTheme.goldColor else Color.White.copy(alpha = 0.9f),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Barra de Sub-Secciones Directas (100% visibles en pantalla sin ocultar ninguna opción)
        val inactiveSegTextColor = if (appTheme.isDark) Color.White.copy(alpha = 0.85f) else appTheme.primaryColor
        Surface(
            color = appTheme.surfaceColor,
            tonalElevation = 1.dp,
            border = BorderStroke(0.8.dp, appTheme.goldColor.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.basilian.org/"))
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    },
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = appTheme.primaryColor.copy(alpha = 0.12f),
                    border = BorderStroke(1.2.dp, appTheme.goldColor),
                    modifier = Modifier
                        .size(30.dp)
                        .testTag("btn_escudo_basilian_org")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        BasilianCrestView(size = 20.dp, showMottoText = false)
                    }
                }

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (selectedSubTab) {
                        RecursosSubTab.FUNDADORES_PATRONOS -> {
                            val items = listOf(
                                Triple(0, "✨ Patronos y Fotos", "seg_patronos"),
                                Triple(1, "👥 10 Fundadores", "seg_fundadores"),
                                Triple(2, "📸 Galería", "seg_galeria")
                            )
                            items.forEach { (idx, title, tag) ->
                                val active = fundadoresSegment == idx
                                Surface(
                                    onClick = { fundadoresSegment = idx },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (active) appTheme.primaryColor else Color.Transparent,
                                    border = BorderStroke(1.dp, if (active) appTheme.goldColor else appTheme.goldColor.copy(alpha = 0.35f)),
                                    modifier = Modifier.weight(1f).testTag(tag)
                                ) {
                                    Text(
                                        text = title,
                                        fontSize = 10.5.sp,
                                        fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Medium,
                                        color = if (active) Color.White else inactiveSegTextColor,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        maxLines = 1,
                                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)
                                    )
                                }
                            }
                        }

                        RecursosSubTab.HISTORIA_LIBROS -> {
                            val items = listOf(
                                Triple(0, "📖 Colombia 87-97", "seg_hist_col"),
                                Triple(1, "📜 Libro 1922", "seg_libro_1922"),
                                Triple(2, "⏳ Cronología", "seg_cronologia")
                            )
                            items.forEach { (idx, title, tag) ->
                                val active = historiaSegment == idx
                                Surface(
                                    onClick = { historiaSegment = idx },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (active) appTheme.primaryColor else Color.Transparent,
                                    border = BorderStroke(1.dp, if (active) appTheme.goldColor else appTheme.goldColor.copy(alpha = 0.35f)),
                                    modifier = Modifier.weight(1f).testTag(tag)
                                ) {
                                    Text(
                                        text = title,
                                        fontSize = 10.5.sp,
                                        fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Medium,
                                        color = if (active) Color.White else inactiveSegTextColor,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        maxLines = 1,
                                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)
                                    )
                                }
                            }
                        }

                        RecursosSubTab.REGLA_POLITICAS -> {
                            val items = listOf(
                                Triple(0, "⚖️ Forma de Vida", "seg_forma_vida"),
                                Triple(1, "🌿 Costumbres", "seg_costumbres"),
                                Triple(2, "🛡️ Políticas 2026", "seg_politicas")
                            )
                            items.forEach { (idx, title, tag) ->
                                val active = reglaSegment == idx
                                Surface(
                                    onClick = { reglaSegment = idx },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (active) appTheme.primaryColor else Color.Transparent,
                                    border = BorderStroke(1.dp, if (active) appTheme.goldColor else appTheme.goldColor.copy(alpha = 0.35f)),
                                    modifier = Modifier.weight(1f).testTag(tag)
                                ) {
                                    Text(
                                        text = title,
                                        fontSize = 10.5.sp,
                                        fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Medium,
                                        color = if (active) Color.White else inactiveSegTextColor,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        maxLines = 1,
                                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)
                                    )
                                }
                            }
                        }

                        RecursosSubTab.CALENDARIO_ORACIONES -> {
                            val items = listOf(
                                Triple(0, "✨ Santo de Hoy", "seg_santoral"),
                                Triple(1, "📅 Calendario", "seg_calendario"),
                                Triple(2, "🙏 Oraciones", "seg_oraciones")
                            )
                            items.forEach { (idx, title, tag) ->
                                val active = devocionarioSegment == idx
                                Surface(
                                    onClick = { devocionarioSegment = idx },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (active) appTheme.primaryColor else Color.Transparent,
                                    border = BorderStroke(1.dp, if (active) appTheme.goldColor else appTheme.goldColor.copy(alpha = 0.35f)),
                                    modifier = Modifier.weight(1f).testTag(tag)
                                ) {
                                    Text(
                                        text = title,
                                        fontSize = 10.5.sp,
                                        fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Medium,
                                        color = if (active) Color.White else inactiveSegTextColor,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        maxLines = 1,
                                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Contenedor principal adaptable con weight(1f) para garantizar espacio completo de lectura en todo celular
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (selectedSubTab) {
                RecursosSubTab.HISTORIA_LIBROS -> {
                    when (historiaSegment) {
                        0 -> ApuntesColombiaView(viewModel = viewModel, appLanguage = appLanguage)
                        1 -> Book1922Screen(viewModel = viewModel, appLanguage = appLanguage)
                        else -> HistoryScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                    }
                }

                RecursosSubTab.FUNDADORES_PATRONOS -> {
                    when (fundadoresSegment) {
                        0 -> PatronsScreen(viewModel = viewModel, appLanguage = appLanguage)
                        1 -> FoundersScreen(viewModel = viewModel, appLanguage = appLanguage)
                        else -> {
                            val recursosSlotSpecs = remember {
                                listOf(
                                    com.example.ui.components.FuturisticPhotoSlotSpec(
                                        slotKey = "recursos_slot_1",
                                        title = "Casa de Formación C.S.B.",
                                        subtitle = "Sube foto de seminario o escolasticado",
                                        accentColor = Color(0xFF00F0FF)
                                    ),
                                    com.example.ui.components.FuturisticPhotoSlotSpec(
                                        slotKey = "recursos_slot_2",
                                        title = "Apostolado Educativo",
                                        subtitle = "Sube imagen de colegio o misión",
                                        accentColor = Color(0xFFFFD700)
                                    ),
                                    com.example.ui.components.FuturisticPhotoSlotSpec(
                                        slotKey = "recursos_slot_3",
                                        title = "Liturgia & Comunidad",
                                        subtitle = "Sube foto de celebración solemne",
                                        accentColor = Color(0xFF10B981)
                                    ),
                                    com.example.ui.components.FuturisticPhotoSlotSpec(
                                        slotKey = "recursos_slot_4",
                                        title = "Archivo & Recuerdos C.S.B.",
                                        subtitle = "Sube captura o imagen comunitaria",
                                        accentColor = Color(0xFFA855F7)
                                    )
                                )
                            }
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                item {
                                    com.example.ui.components.FuturisticCustomPhotoGallerySection(
                                        sectionTitle = "📸 Galería Personalizada (Sube tus Imágenes)",
                                        sectionSubtitle = "Agrega tus propias fotos o crea más espacios tocando '+ Espacio'",
                                        slots = recursosSlotSpecs,
                                        uploadedMap = customGallerySlots,
                                        onUploadPhoto = { key, uri -> viewModel.setCustomGallerySlotPhoto(key, uri) },
                                        onRemovePhoto = { key -> viewModel.removeCustomGallerySlotPhoto(key) }
                                    )
                                }
                            }
                        }
                    }
                }

                RecursosSubTab.REGLA_POLITICAS -> {
                    when (reglaSegment) {
                        0 -> FormaDeVidaScreen(
                            viewModel = viewModel,
                            appLanguage = appLanguage,
                            initialSubTab = FormaDeVidaSubTab.CONSTITUCION_FVB
                        )
                        1 -> CostumbresBasilianasScreen(
                            viewModel = viewModel,
                            appLanguage = appLanguage
                        )
                        else -> Politicas2026Screen(
                            viewModel = viewModel,
                            appLanguage = appLanguage
                        )
                    }
                }

                RecursosSubTab.CALENDARIO_ORACIONES -> {
                    when (devocionarioSegment) {
                        0 -> SantoralDiarioScreen(viewModel = viewModel, appLanguage = appLanguage)
                        1 -> CalendarScreen(viewModel = viewModel, appLanguage = appLanguage)
                        else -> PrayersScreen(viewModel = viewModel, appLanguage = appLanguage)
                    }
                }
            }
        }
    }
}

private fun getLocalizedApuntesBookTitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Notes for a History of the Congregation of Saint Basil in Colombia"
    AppLanguage.FR -> "Notes pour une histoire de la Congrégation de Saint-Basile en Colombie"
    AppLanguage.ES -> "Apuntes para una historia de la Congregación de San Basilio en Colombia"
}

private fun getLocalizedApuntesBookSubtitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "In its first ten years (1987 - August 15 - 1997)"
    AppLanguage.FR -> "Dans ses dix premières années (1987 - 15 août - 1997)"
    AppLanguage.ES -> "En sus diez primeros años (1987 - 15 de agosto - 1997)"
}

private fun getLocalizedApuntesChapterTitle(chapter: ApuntesChapter, lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> when (chapter.id) {
        "homenajes" -> "Tribute to Founders and Patrons"
        "cap_1" -> "Gestation of the Foundation in Colombia"
        "cap_2" -> "Foundation and Insertion in Cali"
        "cap_3" -> "Medellín and Initial Formation"
        "cap_4" -> "Consolidation, Parishes and Vocations"
        else -> chapter.title
    }
    AppLanguage.FR -> when (chapter.id) {
        "homenajes" -> "Hommage aux Fondateurs et Patrons"
        "cap_1" -> "Gestation de l'Idée de Fondation en Colombie"
        "cap_2" -> "Fondation et Insertion à Cali"
        "cap_3" -> "Medellin et la Formation Initiale"
        "cap_4" -> "Consolidation, Paroisses et Vocations"
        else -> chapter.title
    }
    AppLanguage.ES -> chapter.title
}

private fun getLocalizedApuntesChapterSubtitle(chapter: ApuntesChapter, lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> when (chapter.id) {
        "homenajes" -> "Annonay 1822 • Colombia 1987 • Patron Saints"
        "cap_1" -> "First contacts: November 1985 to July 1988"
        "cap_2" -> "Parish Our Lady of the Assumption (Andrés Sanín)"
        "cap_3" -> "Formation House, Novitiate and Scholasticate"
        "cap_4" -> "Bogotá foundation (Egipto) and First Solemn Professions"
        else -> chapter.subtitle
    }
    AppLanguage.FR -> when (chapter.id) {
        "homenajes" -> "Annonay 1822 • Colombie 1987 • Saints Patrons"
        "cap_1" -> "Premiers contacts: Novembre 1985 à Juillet 1988"
        "cap_2" -> "Paroisse Notre-Dame de l'Assomption (Andrés Sanín)"
        "cap_3" -> "Maison de formation, noviciat et scholasticat"
        "cap_4" -> "Fondation à Bogota et premières professions solennelles"
        else -> chapter.subtitle
    }
    AppLanguage.ES -> chapter.subtitle
}

private fun getLocalizedApuntesChapterSummary(chapter: ApuntesChapter, lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> when (chapter.id) {
        "homenajes" -> "Recognition of the 10 French founders, pioneers in Colombia (1987) and prayer to Saint Basil the Great."
        "cap_1" -> "Initial discernment of Superior General Ulisse Paré, dialogue with CLAR and invitation by Archbishop Pedro Rubiano to Cali."
        "cap_2" -> "Arrival in Cali of Fr. Bob Seguin and Fr. Frank Amico, parish apostolate in Andrés Sanín and education."
        "cap_3" -> "Establishment of the formation house and novitiate in Medellín, scholastic studies and first vocations."
        "cap_4" -> "Arrival in Bogotá (Egipto), growth of Colombian vocations, first solemn professions and apostolic fruits."
        else -> chapter.summary
    }
    AppLanguage.FR -> when (chapter.id) {
        "homenajes" -> "Hommage aux 10 fondateurs d'Annonay, pionniers en Colombie (1987) et prière à Saint Basile le Grand."
        "cap_1" -> "Discernement du Supérieur Général Ulisse Paré, consultation avec la CLAR et invitation à Cali."
        "cap_2" -> "Arrivée à Cali des Pères Bob Seguin et Frank Amico, pastorale paroissiale et éducation à Andrés Sanín."
        "cap_3" -> "Maison de formation et noviciat à Medellin, vocations et études théologiques."
        "cap_4" -> "Implantation à Bogota (Égypte), professions perpétuelles et croissance des vocations colombiennes."
        else -> chapter.summary
    }
    AppLanguage.ES -> chapter.summary
}

private fun getLocalizedSectionTitle(title: String, lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> when (title) {
        "Los Diez Fundadores Franceses (Annonay 1822)" -> "The Ten French Founders (Annonay 1822)"
        "Los Fundadores en Colombia (1987)" -> "The Founders in Colombia (1987)"
        "Santos Patronos y Oración a San Basilio" -> "Patron Saints and Prayer to Saint Basil"
        "El anhelo misionero en América Latina" -> "The Missionary Longing in Latin America"
        "El puente providencial: Hna. Hermengarda Alves (CLAR)" -> "The Providential Bridge: Sr. Hermengarda Alves (CLAR)"
        "La decisión en favor de Cali" -> "The Decision in Favor of Cali"
        "Primeras impresiones y el choque de la realidad" -> "First Impressions and the Shock of Reality"
        "Una casa en abandono transformada en hogar" -> "An Abandoned House Transformed into a Home"
        "De las Costumbres Basilianas en Colombia" -> "Of the Basilian Customs in Colombia"
        "Oración por la Vida en Común (Texto Completo)" -> "Prayer for Life in Common (Full Text)"
        "P. Lorenzo Moran y P. Juan Malo" -> "Fr. Lorenzo Moran and Fr. Juan Malo"
        "Las Comunidades Eclesiales de Base (CEBs)" -> "Basic Ecclesial Communities (CEBs)"
        "El Ancianato «Cristo de la Paz» y Centro Médico San Basilio" -> "The «Christ of Peace» Senior Home and San Basilio Medical Center"
        "El Instituto Nuestra Señora de la Asunción (INSA)" -> "Our Lady of the Assumption Institute (INSA)"
        "El secuestro en Andrés Sanín" -> "The Kidnapping in Andrés Sanín"
        "Marchas de silencio y liberación" -> "Silent Processions and Liberation"
        "El joven catequista de la Asunción" -> "The Young Catechist of the Assumption"
        "El martirio en Semana Santa de 1992" -> "The Martyrdom in Holy Week 1992"
        "Homilía de Francisco Amico C.S.B. (23 de abril de 1992)" -> "Homily of Fr. Francisco Amico C.S.B. (April 23, 1992)"
        "El Noviciado y la Parroquia El Ecce Homo" -> "The Novitiate and El Ecce Homo Parish"
        "Simbología del San Basilio Caleño" -> "Symbolism of the Cali Saint Basil Painting"
        else -> title
    }
    AppLanguage.FR -> when (title) {
        "Los Diez Fundadores Franceses (Annonay 1822)" -> "Les Dix Fondateurs Français (Annonay 1822)"
        "Los Fundadores en Colombia (1987)" -> "Les Fondateurs en Colombie (1987)"
        "Santos Patronos y Oración a San Basilio" -> "Saints Patrons et Prière à Saint Basile"
        "El anhelo misionero en América Latina" -> "L'Aspiration Missionnaire en Amérique Latine"
        "El puente providencial: Hna. Hermengarda Alves (CLAR)" -> "Le Pont Providentiel: Sœur Hermengarda Alves (CLAR)"
        "La decisión en favor de Cali" -> "La Décision en Faveur de Cali"
        "Primeras impresiones y el choque de la realidad" -> "Premières Impressions et le Choc de la Réalité"
        "Una casa en abandono transformada en hogar" -> "Une Maison Abandonnée Devenue un Foyer"
        "De las Costumbres Basilianas en Colombia" -> "Des Coutumes Basiliennes en Colombie"
        "Oración por la Vida en Común (Texto Completo)" -> "Prière pour la Vie en Commun (Texte Intégral)"
        "P. Lorenzo Moran y P. Juan Malo" -> "P. Lorenzo Moran et P. Juan Malo"
        "Las Comunidades Eclesiales de Base (CEBs)" -> "Les Communautés Ecclésiales de Base (CEB)"
        "El Ancianato «Cristo de la Paz» y Centro Médico San Basilio" -> "La Maison de Retraite «Christ de la Paix» et Centre Médical"
        "El Instituto Nuestra Señora de la Asunción (INSA)" -> "L'Institut Notre-Dame de l'Assomption (INSA)"
        "El secuestro en Andrés Sanín" -> "L'Enlèvement à Andrés Sanín"
        "Marchas de silencio y liberación" -> "Marches Silencieuses et Libération"
        "El joven catequista de la Asunción" -> "Le Jeune Catéchiste de l'Assomption"
        "El martirio en Semana Santa de 1992" -> "Le Martyre pendant la Semaine Sainte 1992"
        "Homilía de Francisco Amico C.S.B. (23 de abril de 1992)" -> "Homélie de Francisco Amico C.S.B. (23 avril 1992)"
        "El Noviciado y la Parroquia El Ecce Homo" -> "Le Noviciat et la Paroisse El Ecce Homo"
        "Simbología del San Basilio Caleño" -> "Symbolisme du Saint Basile de Cali"
        else -> title
    }
    AppLanguage.ES -> title
}

private fun getLocalizedSectionContent(sec: ApuntesSection, lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> when {
        sec.title.contains("Diez Fundadores Franceses") ->
            "• Fr. Joseph Lapierre\n• Fr. Vincent Duret\n• Fr. Augustin Payan\n• Fr. François Polly\n• Fr. Pierre Tourvieille\n• Fr. Julien Tracol\n• Fr. André Fayolle\n• Fr. Henri Martinesche\n• Fr. Jean François Pages\n• Jean Antoine Vallon\n\n«We praise our fathers who were men deeply committed to their times, educators of the youth and ministers of the Gospel.»"
        sec.title.contains("Fundadores en Colombia") ->
            "• Fr. Francisco Amico Ferrari, C.S.B.\n• Fr. Roberto Seguin Bartolo, C.S.B.\n• Fr. Mauricio Luke Restivo Bidet, C.S.B.\n• Fr. Rafael Ignacio Lopera Montoya, C.S.B.\n• Mr. Eduardo Cuadra Campos\n• Mr. Martín Escobedo Martínez"
        sec.title.contains("Santos Patronos") ->
            "Patrons:\n1. Saint Basil the Great\n2. Saint Francis of Assisi\n3. Saint Thomas Aquinas\n4. Saint John Bosco\n5. Saint Joseph\n6. Holy Mary, Mother of God\n\nPRAYER TO SAINT BASIL:\n«Holy father, Basil, patron of our Congregation whose apostolate is Christian education, be our model in teaching and strength in our ministry. Grant us that deep love for divine truth which you revealed in your life, so that we may be instruments leading all souls to the fullness of truth. Look kindly upon your Congregation, which you yourself have guided from its inception. Amen.»"
        sec.title.contains("anhelo misionero") ->
            "Fr. Ulisse Paré, Superior General at the time, recalls: «While Father Hanrahan was still Superior General, he and I discussed the possibility of starting a second mission project in Latin America. After evaluating initiatives in Africa (ISAID) that lacked direct evangelization, the calling to Colombia emerged. Francisco Amico and Rafael Ignacio Lopera traveled to explore dioceses in need»."
        sec.title.contains("puente providencial") ->
            "The providential encounter with Sr. Hermengarda Alves, General Secretary of CLAR, opened doors across the Colombian episcopate. Her guidance directed the Basilians to Archbishop Pedro Rubiano Sáenz in the Archdiocese of Cali."
        sec.title.contains("decisión en favor de Cali") ->
            "Archbishop Pedro Rubiano warmly welcomed the Basilians to Cali, entrusting them with the pastoral care of the newly populated parish of Nuestra Señora de la Asunción in the district of Andrés Sanín (Distrito de Aguablanca)."
        sec.title.contains("Primeras impresiones") ->
            "Fr. Bob Seguin and Fr. Frank Amico arrived in Cali on August 15, 1987. They encountered poverty, dust, muddy unpaved roads, and thousands of displaced families, but above all a vibrant faith and welcoming community."
        sec.title.contains("casa en abandono") ->
            "With the support of the local community, the fathers transformed an abandoned parish dwelling into a welcoming house of prayer, brotherhood, and human dignity."
        sec.title.contains("Costumbres Basilianas en Colombia") ->
            "Living together in simplicity, sharing daily prayer and meals, welcoming youth and parishioners into the community table, faithful to the Basilian motto: Goodness, Discipline, and Knowledge."
        sec.title.contains("Oración por la Vida en Común") ->
            "«Lord Jesus, teach us to love our brothers with sincerity of heart. May our community life be a luminous witness to the Gospel, where forgiveness, patience, and mutual charity reign every day. Amen.»"
        sec.title.contains("Lorenzo Moran") ->
            "Fr. Lorenzo Moran and Fr. Juan Malo joined the mission, contributing their pastoral zeal, teaching talents, and deep fraternal communion to the growing Colombian insertion."
        sec.title.contains("Comunidades Eclesiales de Base") ->
            "The Base Ecclesial Communities became the beating heart of parish life in Andrés Sanín, bringing together families to reflect on the Word of God and organize communal solidarity."
        sec.title.contains("Ancianato") ->
            "Foundation of the 'Christ of Peace' Senior Home and the Saint Basil Medical Dispensary, providing food, healthcare, and dignified care to vulnerable elders and children."
        sec.title.contains("Instituto Nuestra Señora de la Asunción") ->
            "Foundation of INSA, providing high-quality Christian education and technical training to thousands of underprivileged youth in Aguablanca, transforming generations."
        sec.title.contains("secuestro") ->
            "In a time of intense social turbulence, the kidnapping of a community member shook the mission, but the community responded with unwavering faith, silent marches, and prayer."
        sec.title.contains("Marchas de silencio") ->
            "Thousands of parishioners walked the streets in silence, bearing candles and praying the Rosary for life, peace, and the release of captives."
        sec.title.contains("joven catequista") ->
            "Testimony of faith and self-giving of young Colombian catechists who dedicated their youth to teaching catechism and serving the poorest."
        sec.title.contains("martirio") ->
            "During Holy Week 1992, violence struck close to the heart of the community. In the face of suffering, the Basilian fathers chose to stay, forgiving and sowing seeds of resurrection."
        sec.title.contains("Homilía de Francisco Amico") ->
            "«We are not here to flee; we are here to walk with you through the cross into the dawn of Easter. Christ's love is stronger than death.»"
        sec.title.contains("Noviciado") ->
            "Establishment of the Basilian Novitiate and House of Formation in Medellín, linked to El Ecce Homo parish, receiving the first native vocations."
        sec.title.contains("Simbología del San Basilio Caleño") ->
            "The famous icon created in Cali depicting Saint Basil surrounded by the flora, fauna, and people of the Valle del Cauca, a living symbol of liturgical inculturation."
        else -> sec.content
    }
    AppLanguage.FR -> when {
        sec.title.contains("Diez Fundadores Franceses") ->
            "• P. Joseph Lapierre\n• P. Vincent Duret\n• P. Augustin Payan\n• P. François Polly\n• P. Pierre Tourvieille\n• P. Julien Tracol\n• P. André Fayolle\n• P. Henri Martinesche\n• P. Jean François Pages\n• Jean Antoine Vallon\n\n«Louons nos pères qui furent des hommes engagés dans leur temps, éducateurs de la jeunesse et serviteurs de l'Évangile.»"
        sec.title.contains("Fundadores en Colombia") ->
            "• P. Francisco Amico Ferrari, C.S.B.\n• P. Roberto Seguin Bartolo, C.S.B.\n• P. Mauricio Luke Restivo Bidet, C.S.B.\n• P. Rafael Ignacio Lopera Montoya, C.S.B.\n• M. Eduardo Cuadra Campos\n• M. Martín Escobedo Martínez"
        sec.title.contains("Santos Patronos") ->
            "Patrons :\n1. Saint Basile le Grand\n2. Saint François d'Assise\n3. Saint Thomas d'Aquin\n4. Saint Jean Bosco\n5. Saint Joseph\n6. Sainte Marie, Mère de Dieu\n\nPRIÈRE À SAINT BASILE :\n«Saint père Basile, patron de notre Congrégation dont l'apostolat est l'éducation chrétienne, sois notre modèle dans l'enseignement et notre soutien dans le ministère. Donne-nous cet amour de la vérité que tu as manifesté dans ta vie, afin que nous soyons des instruments conduisant les âmes à la plénitude de la vérité. Veille sur ta Congrégation, que tu as toi-même guidée depuis ses origines. Amen.»"
        sec.title.contains("anhelo misionero") ->
            "Le Père Ulisse Paré, alors Supérieur Général, témoigne : «Alors que le Père Hanrahan était encore Supérieur Général, nous discutions de la possibilité d'ouvrir une seconde mission en Amérique Latine. Après avoir évalué des projets en Afrique, l'appel de la Colombie s'est imposé. Les Pères Francisco Amico et Rafael Lopera sont partis explorer les diocèses». "
        sec.title.contains("puente providencial") ->
            "La rencontre providentielle avec Sœur Hermengarda Alves, Secrétaire Générale de la CLAR, a ouvert les portes de l'épiscopat colombien et orienté les Basiliens vers Mgr Pedro Rubiano à Cali."
        sec.title.contains("decisión en favor de Cali") ->
            "Mgr Pedro Rubiano a accueilli chaleureusement les Basiliens à Cali, leur confiant la paroisse Notre-Dame de l'Assomption à Andrés Sanín (Aguablanca)."
        sec.title.contains("Primeras impresiones") ->
            "Les Pères Bob Seguin et Frank Amico sont arrivés à Cali le 15 août 1987. Ils ont découvert la pauvreté, la boue et des milliers de familles déplacées, mais surtout une foi vivante et un accueil chaleureux."
        sec.title.contains("casa en abandono") ->
            "Avec l'aide de la communauté, les pères ont transformé une maison paroissiale délabrée en un lieu de prière, de fraternité et de dignité."
        sec.title.contains("Costumbres Basilianas en Colombia") ->
            "Vie commune dans la simplicité, prière quotidienne partagée, accueil des jeunes à la table fraternelle, fidèles à la devise : Bonté, Discipline et Science."
        sec.title.contains("Oración por la Vida en Común") ->
            "«Seigneur Jésus, apprends-nous à aimer nos frères avec sincérité de cœur. Que notre vie commune soit un témoignage lumineux de l'Évangile où règnent le pardon, la patience et la charité mutuelle. Amen.»"
        sec.title.contains("Lorenzo Moran") ->
            "Les Pères Lorenzo Moran et Juan Malo ont rejoint la mission, apportant leur zèle pastoral, leur talent d'éducateurs et une profonde communion fraternelle."
        sec.title.contains("Comunidades Eclesiales de Base") ->
            "Les Communautés Ecclésiales de Base sont devenues le cœur battant de la paroisse, réunissant les familles pour méditer la Parole de Dieu et s'entraider."
        sec.title.contains("Ancianato") ->
            "Création du foyer pour personnes âgées «Christ de la Paix» et du dispensaire médical Saint-Basile, offrant soins et dignité aux plus vulnérables."
        sec.title.contains("Instituto Nuestra Señora de la Asunción") ->
            "Fondation de l'INSA, dispensant une éducation chrétienne de grande qualité et une formation technique à des milliers de jeunes d'Aguablanca."
        sec.title.contains("secuestro") ->
            "Dans une période de fortes tensions sociales, l'enlèvement d'un membre de la communauté a éprouvé la mission, mais la communauté a réagi avec foi et prière."
        sec.title.contains("Marchas de silencio") ->
            "Des milliers de paroissiens ont défilé en silence, bougies en main, priant le Rosaire pour la paix et la libération des captifs."
        sec.title.contains("joven catequista") ->
            "Témoignage de foi et de dévouement des jeunes catéchistes colombiens au service des plus démunis."
        sec.title.contains("martirio") ->
            "Pendant la Semaine Sainte 1992, la violence a frappé la paroisse. Face à l'épreuve, les Pères Basiliens ont choisi de rester, pardonnant et semant l'espérance."
        sec.title.contains("Homilía de Francisco Amico") ->
            "«Nous ne sommes pas ici pour fuir ; nous sommes ici pour porter la croix avec vous jusqu'à l'aube de Pâques. L'amour du Christ est plus fort que la mort.»"
        sec.title.contains("Noviciado") ->
            "Création du Noviciat et de la Maison de Formation Basilienne à Medellin, accueillant les premières vocations colombiennes."
        sec.title.contains("Simbología del San Basilio Caleño") ->
            "Le célèbre tableau de saint Basile créé à Cali, entouré de la flore, de la faune et du peuple du Valle del Cauca, symbole vivant d'inculturation."
        else -> sec.content
    }
    AppLanguage.ES -> sec.content
}

private fun getLocalizedSectionQuote(sec: ApuntesSection, lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> when {
        sec.quote.contains("miedo principal") ->
            "I suppose our main fear had to do with the extreme loneliness and isolation of the first founders... The other great fear was the threat of violence."
        sec.quote.contains("no estamos aquí para huir") ->
            "We are not here to flee; we are here to walk with our people through every cross and triumph."
        else -> sec.quote
    }
    AppLanguage.FR -> when {
        sec.quote.contains("miedo principal") ->
            "Je suppose que notre principale crainte concernait l'extrême solitude et l'isolement des premiers fondateurs... L'autre grande peur était la menace de la violence."
        sec.quote.contains("no estamos aquí para huir") ->
            "Nous ne sommes pas ici pour fuir ; nous sommes ici pour marcher avec notre peuple à travers chaque épreuve."
        else -> sec.quote
    }
    AppLanguage.ES -> sec.quote
}

/**
 * Vista completa de "Apuntes para una historia: Los Padres Basilianos en Colombia (1987-1997)"
 * con buscador, lector y traducción completa.
 */
@Composable
fun ApuntesColombiaView(
    viewModel: BasilianViewModel,
    appLanguage: AppLanguage = AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val strings = remember(appLanguage) { AppStrings.get(appLanguage) }
    var selectedApuntesChapter by remember { mutableStateOf<ApuntesChapter?>(null) }
    var apuntesSearchQuery by remember { mutableStateOf("") }
    val apuntesBook = remember(appLanguage) {
        com.example.data.repository.BasilianDocumentsLocalizer.localizeApuntesColombiaBook(ApuntesColombiaData.book, appLanguage)
    }

    val filteredChapters = remember(apuntesSearchQuery, apuntesBook) {
        if (apuntesSearchQuery.isBlank()) apuntesBook.chapters
        else apuntesBook.chapters.filter { ch ->
            ch.title.contains(apuntesSearchQuery, ignoreCase = true) ||
                    ch.subtitle.contains(apuntesSearchQuery, ignoreCase = true) ||
                    ch.summary.contains(apuntesSearchQuery, ignoreCase = true) ||
                    ch.sections.any { it.title.contains(apuntesSearchQuery, ignoreCase = true) || it.content.contains(apuntesSearchQuery, ignoreCase = true) }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Cita Histórica Rescatada del Corpus Basiliano (Cuadro 3D Multicolor)
        item {
            FuturisticQuoteCalloutCard(
                quoteText = when (appLanguage) {
                    AppLanguage.EN -> "We praise our fathers who were men deeply committed to their times, educators of the youth and ministers of the Gospel, who did not flee before crosses but sowed Easter hope."
                    AppLanguage.FR -> "Louons nos pères qui furent des hommes profondément engagés dans leur temps, éducateurs de la jeunesse et serviteurs de l'Évangile, semant l'espérance de Pâques."
                    AppLanguage.ES -> "Alabemos a nuestros padres que fueron hombres profundamente comprometidos con su tiempo, educadores de la juventud y ministros del Evangelio que sembraron esperanza pascual."
                },
                authorOrSource = "Annonay 1822 • Misión Colombia 1987 (C.S.B.)",
                categoryTag = "CITA HISTÓRICA DESTACADA",
                accentColor = FuturisticDesignPalette.NeonGold,
                secondaryColor = FuturisticDesignPalette.NeonCyan,
                onSpeak = {
                    viewModel.speakText("Alabemos a nuestros padres que fueron hombres profundamente comprometidos con su tiempo, educadores de la juventud y ministros del Evangelio.")
                }
            )
        }

        // Portada 3D del libro histórico
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .futuristic3DCard(
                        accentColor = Color(0xFF00F0FF),
                        secondaryColor = Color(0xFFFFD700),
                        cornerRadius = 22.dp,
                        tiltDegrees = 1.8f
                    ),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(
                    1.8.dp,
                    Brush.linearGradient(listOf(Color(0xFF00F0FF), Color(0xFFFFD700), Color(0xFF10B981)))
                ),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF061326).copy(alpha = 0.96f),
                                    Color(0xFF0E2341).copy(alpha = 0.94f)
                                )
                            )
                        )
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFD700).copy(alpha = 0.18f),
                            border = BorderStroke(1.dp, Color(0xFFFFD700))
                        ) {
                            Text(
                                text = "✦ ${strings.recursos.completeHistoricalWork.uppercase()}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        BasilianCrestView(size = 38.dp, showMottoText = false)
                    }

                    Text(
                        text = getLocalizedApuntesBookTitle(appLanguage),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 17.sp,
                        fontFamily = FontFamily.Serif
                    )

                    Text(
                        text = "✍️ ${strings.recursos.byAuthorPrefix} ${apuntesBook.author}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF00F0FF),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${strings.recursos.prologueByPrefix} ${apuntesBook.prologueAuthor} (${apuntesBook.prologueDate})",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCBD5E1)
                    )

                    OutlinedTextField(
                        value = apuntesSearchQuery,
                        onValueChange = { apuntesSearchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("apuntes_search_input"),
                        placeholder = { Text(strings.recursos.searchChaptersPlaceholder, color = Color(0xFF94A3B8)) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF00F0FF))
                        },
                        trailingIcon = {
                            if (apuntesSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { apuntesSearchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = strings.common.close, tint = Color.White)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF00F0FF),
                            unfocusedBorderColor = Color(0xFF00F0FF).copy(alpha = 0.45f),
                            focusedContainerColor = Color(0xFF091B34),
                            unfocusedContainerColor = Color(0xFF091B34)
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            }
        }

        // Lista de capítulos filtrados con colores vivos por capítulo
        items(filteredChapters.indices.toList()) { idx ->
            val chapter = filteredChapters[idx]
            val chapTitle = getLocalizedApuntesChapterTitle(chapter, appLanguage)
            val chapSubtitle = getLocalizedApuntesChapterSubtitle(chapter, appLanguage)
            val chapSummary = getLocalizedApuntesChapterSummary(chapter, appLanguage)
            val firstQuoteSec = chapter.sections.firstOrNull { it.quote.isNotBlank() }
            val featuredQuote = firstQuoteSec?.let { getLocalizedSectionQuote(it, appLanguage) }.orEmpty()
            val featuredQuoteAuthor = firstQuoteSec?.quoteAuthor.orEmpty()

            ApuntesChapterCard(
                chapter = chapter,
                chapterIndex = idx,
                localizedTitle = chapTitle,
                localizedSubtitle = chapSubtitle,
                localizedSummary = chapSummary,
                featuredQuote = featuredQuote,
                featuredQuoteAuthor = featuredQuoteAuthor,
                chapterLabel = strings.recursos.chapterLabel,
                listenLabel = strings.recursos.listenSummaryBtn,
                onClick = { selectedApuntesChapter = chapter },
                onReadAloud = {
                    val text = "$chapTitle. $chapSubtitle. $chapSummary"
                    viewModel.speakText(text)
                }
            )
        }
    }

    // Diálogo con el Capítulo Completo y Citas Destacadas en Color
    selectedApuntesChapter?.let { ch ->
        val dlgTitle = getLocalizedApuntesChapterTitle(ch, appLanguage)
        val dlgSubtitle = getLocalizedApuntesChapterSubtitle(ch, appLanguage)
        val dlgSummary = getLocalizedApuntesChapterSummary(ch, appLanguage)

        AlertDialog(
            onDismissRequest = { selectedApuntesChapter = null },
            containerColor = Color(0xFF071529),
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = {
                Column {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFFD700).copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, Color(0xFFFFD700))
                    ) {
                        Text(
                            text = "✦ ${strings.recursos.chapterLabel} ${ch.romanNumeral} • ${ch.years}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = dlgTitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif
                    )
                    if (dlgSubtitle.isNotBlank()) {
                        Text(
                            text = dlgSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF00F0FF),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = dlgSummary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE2E8F0)
                        )
                    }

                    items(ch.sections.indices.toList()) { secIdx ->
                        val sec = ch.sections[secIdx]
                        val secAccent = FuturisticDesignPalette.accentForIndex(secIdx)
                        val secSecondary = FuturisticDesignPalette.secondaryForIndex(secIdx)
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1E38)),
                            border = BorderStroke(1.2.dp, secAccent.copy(alpha = 0.65f))
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "✦ ${getLocalizedSectionTitle(sec.title, appLanguage)}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = secAccent
                                )
                                Text(
                                    text = getLocalizedSectionContent(sec, appLanguage),
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 22.sp,
                                    color = Color(0xFFF8FAFC)
                                )
                                val localizedQuote = getLocalizedSectionQuote(sec, appLanguage)
                                if (localizedQuote.isNotBlank()) {
                                    FuturisticQuoteCalloutCard(
                                        quoteText = localizedQuote,
                                        authorOrSource = sec.quoteAuthor,
                                        categoryTag = "CITA DOCUMENTAL C.S.B.",
                                        accentColor = secAccent,
                                        secondaryColor = secSecondary,
                                        onSpeak = { viewModel.speakText(localizedQuote) }
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val full = buildString {
                        append(dlgTitle)
                        append(". ")
                        ch.sections.forEach { s ->
                            append(s.title)
                            append(". ")
                            append(s.content)
                            append(" ")
                        }
                    }
                    viewModel.speakText(full)
                }) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFF00F0FF))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(strings.recursos.listenSummaryBtn, color = Color(0xFF00F0FF), fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedApuntesChapter = null }) {
                    Text(strings.common.close, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun ApuntesChapterCard(
    chapter: ApuntesChapter,
    chapterIndex: Int = 0,
    localizedTitle: String = chapter.title,
    localizedSubtitle: String = chapter.subtitle,
    localizedSummary: String = chapter.summary,
    featuredQuote: String = "",
    featuredQuoteAuthor: String = "",
    chapterLabel: String,
    listenLabel: String = "Escuchar",
    onClick: () -> Unit,
    onReadAloud: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = remember(chapterIndex) {
        FuturisticDesignPalette.accentForIndex(chapterIndex)
    }
    val secondaryColor = remember(chapterIndex) {
        FuturisticDesignPalette.secondaryForIndex(chapterIndex)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .futuristic3DCard(
                accentColor = accentColor,
                secondaryColor = secondaryColor,
                cornerRadius = 20.dp,
                tiltDegrees = 1.6f
            )
            .clickable(onClick = onClick)
            .testTag("apuntes_chap_${chapter.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(6.dp),
        border = BorderStroke(
            1.5.dp,
            Brush.linearGradient(listOf(accentColor, secondaryColor))
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF061325).copy(alpha = 0.96f),
                            Color(0xFF0D213D).copy(alpha = 0.94f),
                            accentColor.copy(alpha = 0.14f)
                        )
                    )
                )
        ) {
            // Barra lateral luminosa 3D de color por capítulo
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .heightIn(min = 140.dp)
                    .background(Brush.verticalGradient(listOf(accentColor, secondaryColor, accentColor)))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = accentColor.copy(alpha = 0.20f),
                        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.8f))
                    ) {
                        Text(
                            text = "✦ $chapterLabel ${chapter.romanNumeral} • ${chapter.years}",
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        onClick = onReadAloud,
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = accentColor.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.7f)),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = listenLabel,
                                tint = accentColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Text(
                    text = localizedTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontFamily = FontFamily.Serif
                )

                if (localizedSubtitle.isNotBlank()) {
                    Text(
                        text = localizedSubtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Text(
                    text = localizedSummary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFE2E8F0),
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp
                )

                // Si el capítulo tiene una cita importante, la rescatamos en un cuadro colorido 3D
                if (featuredQuote.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = secondaryColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, secondaryColor.copy(alpha = 0.75f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "«$featuredQuote»",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic,
                                color = Color(0xFFF8FAFC),
                                lineHeight = 17.sp
                            )
                            if (featuredQuoteAuthor.isNotBlank()) {
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "— $featuredQuoteAuthor",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFFFD700)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
