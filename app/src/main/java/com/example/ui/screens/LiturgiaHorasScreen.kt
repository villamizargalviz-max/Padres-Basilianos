package com.example.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.AppLanguage
import com.example.ui.components.futuristic3DCard
import com.example.ui.viewmodel.BasilianViewModel
import java.net.URLEncoder
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

// ============================================================================
// MODELOS DE LA RED DE BASILIANOS PARA LA EDUCACIÓN Y OBRAS EN EL MUNDO 3D
// ============================================================================

enum class BasilianCountryFilter(
    val id: String,
    val label: String,
    val labelEn: String,
    val labelFr: String,
    val flag: String,
    val targetLonDeg: Float,
    val targetLatDeg: Float,
    val discreetAvailabilityNote: String,
    val discreetAvailabilityNoteEn: String,
    val discreetAvailabilityNoteFr: String
) {
    ALL(
        id = "ALL",
        label = "Mundo 3D",
        labelEn = "3D World",
        labelFr = "Monde 3D",
        flag = "🌎",
        targetLonDeg = -75f,
        targetLatDeg = 18f,
        discreetAvailabilityNote = "Nota discreta: La Congregación de San Basilio organiza sus obras según la misión de cada país: Universidades y Colegios en Canadá y EE. UU.; Parroquias, Pastoral Educativa y Casas de Formación en Colombia (Bogotá y Medellín) y México (Tehuacán); y su Cuna Histórica en Francia (Annonay, 1822).",
        discreetAvailabilityNoteEn = "Note: The Congregation of St. Basil organizes its works according to each country's mission: Universities and High Schools in Canada and the USA; Parishes, Educational Ministry, and Formation Houses in Colombia (Bogotá and Medellín) and Mexico (Tehuacán); and its Historical Cradle in France (Annonay, 1822).",
        discreetAvailabilityNoteFr = "Note : La Congrégation de Saint-Basile organise ses œuvres selon la mission de chaque pays : Universités et Collèges au Canada et aux États-Unis ; Paroisses, Pastorale Éducative et Maisons de Formation en Colombie (Bogotá et Medellín) et au Mexique (Tehuacán) ; et son Berceau Historique en France (Annonay, 1822)."
    ),
    COLOMBIA(
        id = "Colombia",
        label = "Colombia",
        labelEn = "Colombia",
        labelFr = "Colombie",
        flag = "🇨🇴",
        targetLonDeg = -75.0f,
        targetLatDeg = 5.2f,
        discreetAvailabilityNote = "Nota discreta en Colombia: Actualmente la presencia activa de los Padres Basilianos está exclusivamente en Bogotá y Medellín (Parroquias, Pastoral Educativa/Social y Casas de Formación). Los Padres Basilianos ya NO están en Cali (donde inició la misión en 1987 y la etapa histórica del INSA) ni en Riohacha, tras haber entregado dichas obras a las diócesis locales.",
        discreetAvailabilityNoteEn = "Note on Colombia: Currently, the active presence of the Basilian Fathers is exclusively in Bogotá and Medellín (Parishes, Educational/Social Ministry, and Formation Houses). The Basilian Fathers are NO LONGER in Cali (where the mission began in 1987 alongside the historical INSA stage) or Riohacha, having handed those works over to the local dioceses.",
        discreetAvailabilityNoteFr = "Note sur la Colombie : Actuellement, la présence active des Pères Basiliens se trouve exclusivement à Bogotá et Medellín (Paroisses, Pastorale Éducative/Sociale et Maisons de Formation). Les Pères Basiliens ne sont PLUS à Cali (où la mission a commencé en 1987 avec l'étape historique de l'INSA) ni à Riohacha, après avoir confié ces œuvres aux diocèses locaux."
    ),
    MEXICO(
        id = "México",
        label = "México",
        labelEn = "Mexico",
        labelFr = "Mexique",
        flag = "🇲🇽",
        targetLonDeg = -97.42f,
        targetLatDeg = 18.47f,
        discreetAvailabilityNote = "Nota discreta en México: La presencia basiliana se concentra en Parroquias, Misiones Comunitarias y Formación Vocacional (Tehuacán, Puebla). Actualmente no cuenta con colegios ni universidades en territorio mexicano.",
        discreetAvailabilityNoteEn = "Note on Mexico: The Basilian presence focuses on Parishes, Community Missions, and Vocational Formation (Tehuacán, Puebla). It currently does not operate high schools or universities in Mexico.",
        discreetAvailabilityNoteFr = "Note sur le Mexique : La présence basilienne se concentre sur les Paroisses, les Missions Communautaires et la Formation Vocationnelle (Tehuacán, Puebla). Elle ne gère pas actuellement d'écoles ou d'universités au Mexique."
    ),
    CANADA(
        id = "Canadá",
        label = "Canadá",
        labelEn = "Canada",
        labelFr = "Canada",
        flag = "🇨🇦",
        targetLonDeg = -82.0f,
        targetLatDeg = 44.5f,
        discreetAvailabilityNote = "Nota discreta en Canadá: Sede de grandes Universidades federadas, Colegios históricos, Parroquias y la Curia General en Toronto, Windsor y Edmonton.",
        discreetAvailabilityNoteEn = "Note on Canada: Home to major federated Universities, historic High Schools, Parishes, and the General Curia in Toronto, Windsor, and Edmonton.",
        discreetAvailabilityNoteFr = "Note sur le Canada : Siège de grandes Universités fédérées, de Collèges historiques, de Paroisses et de la Curie Générale à Toronto, Windsor et Edmonton."
    ),
    USA(
        id = "EE. UU.",
        label = "EE. UU.",
        labelEn = "USA",
        labelFr = "États-Unis",
        flag = "🇺🇸",
        targetLonDeg = -89.0f,
        targetLatDeg = 34.0f,
        discreetAvailabilityNote = "Nota discreta en EE. UU.: Cuenta con Universidades, Colegios Secundarios de excelencia ( miembro de la red BNENE ) y Parroquias en Texas, Míchigan y Nueva York.",
        discreetAvailabilityNoteEn = "Note on the USA: Features leading Catholic Universities, Secondary Schools (members of the BNENE network), and Parishes in Texas, Michigan, and New York.",
        discreetAvailabilityNoteFr = "Note sur les États-Unis : Comprend des Universités catholiques, des Lycées d'excellence (membres du réseau BNENE) et des Paroisses au Texas, au Michigan et à New York."
    ),
    FRANCE(
        id = "Francia",
        label = "Francia",
        labelEn = "France",
        labelFr = "France",
        flag = "🇫🇷",
        targetLonDeg = 4.67f,
        targetLatDeg = 45.24f,
        discreetAvailabilityNote = "Nota discreta en Francia: Patrimonio espiritual y educativo en Annonay (cuna fundacional de 1822). Hoy funciona como referente histórico y vínculo fraterno de toda la Red Basiliana.",
        discreetAvailabilityNoteEn = "Note on France: Spiritual and educational heritage in Annonay (foundational cradle of 1822). Today it serves as a historical landmark and fraternal bond for the entire Basilian Network.",
        discreetAvailabilityNoteFr = "Note sur la France : Patrimoine spirituel et éducatif à Annonay (berceau fondateur de 1822). Aujourd'hui, il constitue un repère historique et un lien fraternel pour tout le Réseau Basilien."
    );

    fun localizedLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> labelEn
        AppLanguage.FR -> labelFr
        AppLanguage.ES -> label
    }

    fun localizedNote(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> discreetAvailabilityNoteEn
        AppLanguage.FR -> discreetAvailabilityNoteFr
        AppLanguage.ES -> discreetAvailabilityNote
    }
}

enum class BasilianWorkCategory(
    val label: String,
    val labelEn: String,
    val labelFr: String,
    val shortLabel: String,
    val shortLabelEn: String,
    val shortLabelFr: String,
    val emoji: String,
    val badgeColor: Color
) {
    COLEGIO(
        label = "Colegios Basilianos",
        labelEn = "Basilian High Schools",
        labelFr = "Collèges & Lycées Basiliens",
        shortLabel = "Colegios",
        shortLabelEn = "Schools",
        shortLabelFr = "Collèges",
        emoji = "🎓",
        badgeColor = Color(0xFF00F0FF)
    ),
    UNIVERSIDAD(
        label = "Universidades Basilianas",
        labelEn = "Basilian Universities",
        labelFr = "Universités Basiliennes",
        shortLabel = "Universidades",
        shortLabelEn = "Universities",
        shortLabelFr = "Universités",
        emoji = "🏛️",
        badgeColor = Color(0xFFFBBF24)
    ),
    PARROQUIA_MISION(
        label = "Parroquias y Pastorales",
        labelEn = "Parishes & Ministries",
        labelFr = "Paroisses & Pastorales",
        shortLabel = "Parroquias y Pastoral",
        shortLabelEn = "Parishes & Ministry",
        shortLabelFr = "Paroisses & Pastorale",
        emoji = "⛪",
        badgeColor = Color(0xFF34D399)
    );

    fun localizedLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> labelEn
        AppLanguage.FR -> labelFr
        AppLanguage.ES -> label
    }

    fun localizedShortLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> shortLabelEn
        AppLanguage.FR -> shortLabelFr
        AppLanguage.ES -> shortLabel
    }
}

data class BasilianGlobalWork(
    val id: String,
    val name: String,
    val nameEn: String = name,
    val nameFr: String = name,
    val shortPinLabel: String,
    val shortPinLabelEn: String = shortPinLabel,
    val shortPinLabelFr: String = shortPinLabel,
    val category: BasilianWorkCategory,
    val country: String,
    val countryEn: String = country,
    val countryFr: String = country,
    val city: String,
    val address: String,
    val flag: String,
    val foundedYear: String,
    val foundedYearEn: String = foundedYear,
    val foundedYearFr: String = foundedYear,
    val description: String,
    val descriptionEn: String = description,
    val descriptionFr: String = description,
    val recentNews2025: String,
    val recentNews2025En: String = recentNews2025,
    val recentNews2025Fr: String = recentNews2025,
    val websiteUrl: String,
    val secondaryUrlLabel: String? = null,
    val secondaryUrl: String? = null,
    val latitudeDeg: Float,
    val longitudeDeg: Float,
    val mottoOrHighlight: String,
    val mottoOrHighlightEn: String = mottoOrHighlight,
    val mottoOrHighlightFr: String = mottoOrHighlight,
    val panoramicImageUrl: String,
    val googleMapsQuery: String
) {
    fun localizedName(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> nameEn
        AppLanguage.FR -> nameFr
        AppLanguage.ES -> name
    }

    fun localizedShortPin(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> shortPinLabelEn
        AppLanguage.FR -> shortPinLabelFr
        AppLanguage.ES -> shortPinLabel
    }

    fun localizedCountry(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> countryEn
        AppLanguage.FR -> countryFr
        AppLanguage.ES -> country
    }

    fun localizedFoundedYear(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> foundedYearEn
        AppLanguage.FR -> foundedYearFr
        AppLanguage.ES -> foundedYear
    }

    fun localizedDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> descriptionEn
        AppLanguage.FR -> descriptionFr
        AppLanguage.ES -> description
    }

    fun localizedRecentNews(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> recentNews2025En
        AppLanguage.FR -> recentNews2025Fr
        AppLanguage.ES -> recentNews2025
    }

    fun localizedMotto(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> mottoOrHighlightEn
        AppLanguage.FR -> mottoOrHighlightFr
        AppLanguage.ES -> mottoOrHighlight
    }
}

object BasilianEducationAndWorksRepository {

    val allWorks: List<BasilianGlobalWork> = listOf(
        // ==================== COLOMBIA (Obras y Pastorales Vigentes en Medellín y Bogotá) ====================
        BasilianGlobalWork(
            id = "co_medellin_san_basilio",
            name = "Parroquia San Basilio y Escolasticado San Basilio Magno",
            nameEn = "St. Basil Parish & St. Basil the Great Scholasticate",
            nameFr = "Paroisse Saint-Basile & Scolasticat Saint-Basile-le-Grand",
            shortPinLabel = "Medellín · San Basilio",
            shortPinLabelEn = "Medellín · St. Basil",
            shortPinLabelFr = "Medellín · Saint-Basile",
            category = BasilianWorkCategory.PARROQUIA_MISION,
            country = "Colombia",
            countryEn = "Colombia",
            countryFr = "Colombie",
            city = "Medellín, Antioquia",
            address = "Calle 18 Sur # 43A-29 / Sector Santa María de los Ángeles y Barrio Yarumo, Medellín, Antioquia, Colombia",
            flag = "🇨🇴",
            foundedYear = "1987 (Misión en Colombia)",
            foundedYearEn = "1987 (Mission in Colombia)",
            foundedYearFr = "1987 (Mission en Colombie)",
            description = "Corazón de la misión basiliana en Medellín, Colombia. Integra la Parroquia San Basilio con intensa pastoral juvenil, catequética, familiar y educativa comunitaria, junto al Escolasticado donde se forman los seminaristas de teología.",
            descriptionEn = "Heart of the Basilian mission in Medellín, Colombia. Integrates St. Basil Parish with vibrant youth, catechetical, family, and community educational ministry, alongside the Scholasticate where theology seminarians are formed.",
            descriptionFr = "Cœur de la mission basilienne à Medellín, en Colombie. Intègre la Paroisse Saint-Basile avec une pastorale active des jeunes, de la famille et de l'éducation communautaire, ainsi que le Scolasticat où sont formés les séminaristes en théologie.",
            recentNews2025 = "Impulsa los semilleros juveniles basilianos, talleres formativos para familias y acompañamiento espiritual y académico para jóvenes de Medellín.",
            recentNews2025En = "Promotes Basilian youth leadership groups, family formation workshops, and spiritual and academic accompaniment for young people in Medellín.",
            recentNews2025Fr = "Encourage les groupes de jeunes basiliens, les ateliers de formation familiale et l'accompagnement spirituel et académique des jeunes de Medellín.",
            websiteUrl = "https://www.basilian.org/es/",
            secondaryUrlLabel = "Directorio CSB",
            secondaryUrl = "https://www.basilian.org/es/donde-estamos/",
            latitudeDeg = 6.1975f,
            longitudeDeg = -75.5733f,
            mottoOrHighlight = "Pastoral Parroquial, Juvenil y Formación Teológica en Medellín",
            mottoOrHighlightEn = "Parish, Youth & Theological Formation Ministry in Medellín",
            mottoOrHighlightFr = "Pastorale Paroissiale, des Jeunes et Formation Théologique à Medellín",
            panoramicImageUrl = "https://images.unsplash.com/photo-1548625361-16800b328a79?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "Parroquia San Basilio Medellin Antioquia Colombia"
        ),
        BasilianGlobalWork(
            id = "co_bogota_egipto",
            name = "Parroquia Nuestra Señora de Egipto y Pastoral Educativa",
            nameEn = "Our Lady of Egypt Parish & Educational Ministry",
            nameFr = "Paroisse Notre-Dame d'Égypte & Pastorale Éducative",
            shortPinLabel = "Bogotá · Ntra. Sra. Egipto",
            shortPinLabelEn = "Bogotá · Our Lady of Egypt",
            shortPinLabelFr = "Bogotá · N.-D. d'Égypte",
            category = BasilianWorkCategory.PARROQUIA_MISION,
            country = "Colombia",
            countryEn = "Colombia",
            countryFr = "Colombie",
            city = "Bogotá D.C.",
            address = "Carrera 4 Este # 10-02, Barrio Egipto, Localidad La Candelaria, Bogotá D.C., Colombia",
            flag = "🇨🇴",
            foundedYear = "Presencia CSB en Bogotá",
            foundedYearEn = "CSB Presence in Bogotá",
            foundedYearFr = "Présence CSB à Bogotá",
            description = "Histórica parroquia en los cerros orientales de Bogotá acompañada por los Padres Basilianos. Desarrolla programas de refuerzo escolar, pastoral infantil y juvenil, promoción cultural y acompañamiento comunitario en La Candelaria.",
            descriptionEn = "Historic parish in the eastern hills of Bogotá served by the Basilian Fathers. Develops after-school tutoring, children and youth ministry, cultural promotion, and community accompaniment in La Candelaria.",
            descriptionFr = "Paroisse historique dans les collines orientales de Bogotá animée par les Pères Basiliens. Développe le soutien scolaire, la pastorale de l'enfance et de la jeunesse, la promotion culturelle et l'accompagnement communautaire à La Candelaria.",
            recentNews2025 = "Fortalece la pastoral social, el apoyo escolar comunitario y las celebraciones tradicionales junto a las familias y jóvenes del sector.",
            recentNews2025En = "Strengthens social outreach, community school tutoring, and traditional celebrations alongside local families and youth.",
            recentNews2025Fr = "Renforce la pastorale sociale, le soutien scolaire communautaire et les célébrations traditionnelles avec les familles et les jeunes du quartier.",
            websiteUrl = "https://www.basilian.org/es/",
            secondaryUrlLabel = "Conocer Misión",
            secondaryUrl = "https://www.basilian.org/es/quienes-somos/",
            latitudeDeg = 4.5936f,
            longitudeDeg = -74.0686f,
            mottoOrHighlight = "Fe, Cultura y Apoyo Educativo Comunitario en Bogotá",
            mottoOrHighlightEn = "Faith, Culture & Community Educational Support in Bogotá",
            mottoOrHighlightFr = "Foi, Culture et Soutien Éducatif Communautaire à Bogotá",
            panoramicImageUrl = "https://images.unsplash.com/photo-1568632234157-ce7aecd03d0d?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "Iglesia Nuestra Señora de Egipto Bogota Colombia"
        ),
        BasilianGlobalWork(
            id = "co_bogota_annonay",
            name = "Casa Annonay — Pastoral Vocacional y Comunidad Basiliana",
            nameEn = "Annonay House — Vocation Ministry & Basilian Community",
            nameFr = "Maison Annonay — Pastorale des Vocations & Communauté Basilienne",
            shortPinLabel = "Bogotá · Casa Annonay",
            shortPinLabelEn = "Bogotá · Annonay House",
            shortPinLabelFr = "Bogotá · Maison Annonay",
            category = BasilianWorkCategory.PARROQUIA_MISION,
            country = "Colombia",
            countryEn = "Colombia",
            countryFr = "Colombie",
            city = "Bogotá D.C.",
            address = "Sector Norte / Suba, Casa de Formación Annonay, Bogotá D.C., Colombia",
            flag = "🇨🇴",
            foundedYear = "Formación CSB",
            foundedYearEn = "CSB Formation",
            foundedYearFr = "Formation CSB",
            description = "Casa de discernimiento vocacional, formación espiritual y vida comunitaria de la Congregación de San Basilio en Bogotá, Colombia, inspirada en los diez fundadores de Annonay.",
            descriptionEn = "House of vocational discernment, spiritual formation, and community life of the Congregation of St. Basil in Bogotá, Colombia, inspired by the ten founders of Annonay.",
            descriptionFr = "Maison de discernement vocationnel, de formation spirituelle et de vie communautaire de la Congrégation de Saint-Basile à Bogotá, en Colombie, inspirée par les dix fondateurs d'Annonay.",
            recentNews2025 = "Encuentros vocacionales, retiros juveniles y acompañamiento personalizado para jóvenes con inquietud por la vida sacerdotal y educadora basiliana.",
            recentNews2025En = "Vocational encounters, youth retreats, and personalized accompaniment for young men discerning Basilian priestly and educational life.",
            recentNews2025Fr = "Rencontres vocationnelles, retraites de jeunes et accompagnement personnalisé pour les jeunes discernant la vie sacerdotale et éducative basilienne.",
            websiteUrl = "https://www.basilian.org/es/",
            latitudeDeg = 4.7110f,
            longitudeDeg = -74.0721f,
            mottoOrHighlight = "Discernimiento Vocacional y Vida Comunitaria en Bogotá",
            mottoOrHighlightEn = "Vocational Discernment & Community Life in Bogotá",
            mottoOrHighlightFr = "Discernement Vocationnel et Vie Communautaire à Bogotá",
            panoramicImageUrl = "https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "Padres Basilianos Bogota Colombia"
        ),
        BasilianGlobalWork(
            id = "co_medellin_espiritualidad",
            name = "Casa de Formación y Apostolado Educativo C.S.B. Medellín",
            nameEn = "C.S.B. Formation & Educational Apostolate Center Medellín",
            nameFr = "Centre de Formation et d'Apostolat Éducatif C.S.B. Medellín",
            shortPinLabel = "Medellín · Apostolado CSB",
            shortPinLabelEn = "Medellín · CSB Apostolate",
            shortPinLabelFr = "Medellín · Apostolat CSB",
            category = BasilianWorkCategory.PARROQUIA_MISION,
            country = "Colombia",
            countryEn = "Colombia",
            countryFr = "Colombie",
            city = "Medellín, Antioquia",
            address = "Sector El Poblado / Santa María de los Ángeles, Medellín, Antioquia, Colombia",
            flag = "🇨🇴",
            foundedYear = "Sede Provincial Colombia",
            foundedYearEn = "Colombia Mission Hub",
            foundedYearFr = "Siège de Mission Colombie",
            description = "Centro de articulación pastoral, académica y vocacional de los Padres Basilianos en Medellín. Promueve la formación integral de laicos, jóvenes y seminaristas bajo el carisma de Bondad, Disciplina y Ciencia (tras haber culminado la etapa histórica de Cali 1987 / INSA).",
            descriptionEn = "Pastoral, academic, and vocational hub of the Basilian Fathers in Medellín. Promotes integral formation for laypeople, youth, and seminarians under the charism of Goodness, Discipline, and Knowledge (following the completion of the historical 1987 Cali / INSA stage).",
            descriptionFr = "Centre d'articulation pastorale, académique et vocationnelle des Pères Basiliens à Medellín. Promeut la formation intégrale des laïcs, des jeunes et des séminaristes selon le charisme de Bonté, Discipline et Science (après l'achèvement de l'étape historique de Cali 1987 / INSA).",
            recentNews2025 = "Coordina los programas de formación bíblica, acompañamiento juvenil y misión educativa comunitaria de los Basilianos en Antioquia.",
            recentNews2025En = "Coordinates biblical formation programs, youth accompaniment, and community educational outreach of the Basilians in Antioquia.",
            recentNews2025Fr = "Coordonne les programmes de formation biblique, d'accompagnement des jeunes et de mission éducative communautaire des Basiliens à Antioquia.",
            websiteUrl = "https://www.basilian.org/es/",
            latitudeDeg = 6.1912f,
            longitudeDeg = -75.5785f,
            mottoOrHighlight = "Formación Integral, Fe y Fraternidad en Medellín",
            mottoOrHighlightEn = "Integral Formation, Faith & Fraternity in Medellín",
            mottoOrHighlightFr = "Formation Intégrale, Foi et Fraternité à Medellín",
            panoramicImageUrl = "https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "Santa Maria de los Angeles El Poblado Medellin Colombia"
        ),

        // ==================== MÉXICO ====================
        BasilianGlobalWork(
            id = "mx_tehuacan_san_lorenzo",
            name = "Parroquia San Lorenzo Diácono y Mártir & Casa San Felipe",
            nameEn = "St. Lawrence Deacon & Martyr Parish & St. Philip House",
            nameFr = "Paroisse Saint-Laurent Diacre et Martyr & Maison Saint-Philippe",
            shortPinLabel = "Tehuacán · San Lorenzo",
            shortPinLabelEn = "Tehuacán · St. Lawrence",
            shortPinLabelFr = "Tehuacán · Saint-Laurent",
            category = BasilianWorkCategory.PARROQUIA_MISION,
            country = "México",
            countryEn = "Mexico",
            countryFr = "Mexique",
            city = "Tehuacán, Puebla",
            address = "Plaza Principal s/n, San Lorenzo Teotipilco, C.P. 75855, Tehuacán, Puebla, México",
            flag = "🇲🇽",
            foundedYear = "1961 (Presencia en México)",
            foundedYearEn = "1961 (Presence in Mexico)",
            foundedYearFr = "1961 (Présence au Mexique)",
            description = "Centro vivo del apostolado basiliano en México en San Lorenzo Teotipilco, Tehuacán. Articula catequesis escolarizada, formación de laicos, pastoral juvenil y casa de formación vocacional.",
            descriptionEn = "Living hub of the Basilian apostolate in Mexico at San Lorenzo Teotipilco, Tehuacán. Coordinates structured catechesis, lay leadership formation, youth ministry, and a vocational formation house.",
            descriptionFr = "Centre vivant de l'apostolat basilien au Mexique à San Lorenzo Teotipilco, Tehuacán. Articule la catéchèse structurée, la formation des laïcs, la pastorale des jeunes et une maison de formation vocationnelle.",
            recentNews2025 = "Misiones juveniles, talleres bíblicos y programas de formación integral para niños, adolescentes y familias de Tehuacán.",
            recentNews2025En = "Youth missions, biblical workshops, and integral formation programs for children, adolescents, and families in Tehuacán.",
            recentNews2025Fr = "Missions de jeunes, ateliers bibliques et programmes de formation intégrale pour les enfants, les adolescents et les familles de Tehuacán.",
            websiteUrl = "https://www.basilian.org/es/",
            secondaryUrlLabel = "Vocaciones CSB",
            secondaryUrl = "https://www.basilian.org/es/vocaciones/",
            latitudeDeg = 18.4735f,
            longitudeDeg = -97.4228f,
            mottoOrHighlight = "Misión Evangelizadora y Pastoral Juvenil en México",
            mottoOrHighlightEn = "Evangelizing Mission & Youth Ministry in Mexico",
            mottoOrHighlightFr = "Mission Évangélisatrice et Pastorale des Jeunes au Mexique",
            panoramicImageUrl = "https://images.unsplash.com/photo-1518638150340-f706e86654de?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "Parroquia San Lorenzo Teotipilco Tehuacan Puebla Mexico"
        ),

        // ==================== CANADÁ ====================
        BasilianGlobalWork(
            id = "ca_toronto_smcs",
            name = "St. Michael's College School (SMCS)",
            nameEn = "St. Michael's College School (SMCS)",
            nameFr = "École Collégiale St. Michael's (SMCS)",
            shortPinLabel = "Toronto · St. Michael's School",
            shortPinLabelEn = "Toronto · St. Michael's School",
            shortPinLabelFr = "Toronto · Collège St. Michael's",
            category = BasilianWorkCategory.COLEGIO,
            country = "Canadá",
            countryEn = "Canada",
            countryFr = "Canada",
            city = "Toronto, Ontario",
            address = "1515 Bathurst St, Toronto, ON M5P 3H4, Canadá",
            flag = "🇨🇦",
            foundedYear = "1852",
            description = "Colegio católico basiliano de referencia en Canadá (grados 7 a 12). Forma líderes íntegros en ciencia, artes, deportes y fe bajo el lema 'Enséñame la Bondad, la Disciplina y la Ciencia'.",
            descriptionEn = "Flagship Basilian Catholic school in Canada (Grades 7 to 12). Forms leaders of integrity in science, arts, athletics, and faith under the motto 'Teach me Goodness, Discipline, and Knowledge'.",
            descriptionFr = "École catholique basilienne de référence au Canada (de la 7e à la 12e année). Forme des leaders intègres en sciences, arts, sports et foi sous la devise « Enseigne-moi la Bonté, la Discipline et la Science ».",
            recentNews2025 = "Lidera la Red Basiliana para la Educación (BNENE) con laboratorios STEM, artes escénicas y servicio apostólico estudiantil.",
            recentNews2025En = "Leads the Basilian Network for Education (BNENE) with STEM laboratories, performing arts, and student apostolic service.",
            recentNews2025Fr = "Anime le Réseau Basilien pour l'Éducation (BNENE) avec des laboratoires STEM, des arts de la scène et le service apostolique étudiant.",
            websiteUrl = "https://www.stmichaels.on.ca",
            secondaryUrlLabel = "Noticias SMCS",
            secondaryUrl = "https://www.stmichaels.on.ca/news-events/",
            latitudeDeg = 43.6841f,
            longitudeDeg = -79.4178f,
            mottoOrHighlight = "Colegio Insignia de la Red Basiliana de Educación",
            mottoOrHighlightEn = "Flagship School of the Basilian Network for Education",
            mottoOrHighlightFr = "Collège Phare du Réseau Basilien pour l'Éducation",
            panoramicImageUrl = "https://images.unsplash.com/photo-1541339907198-e08756dedf3f?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "St. Michael's College School 1515 Bathurst St Toronto Canada"
        ),
        BasilianGlobalWork(
            id = "ca_toronto_usmc",
            name = "University of St. Michael's College",
            nameEn = "University of St. Michael's College",
            nameFr = "Université St. Michael's College (Toronto)",
            shortPinLabel = "Toronto · Univ. St. Michael's",
            shortPinLabelEn = "Toronto · Univ. St. Michael's",
            shortPinLabelFr = "Toronto · Univ. St. Michael's",
            category = BasilianWorkCategory.UNIVERSIDAD,
            country = "Canadá",
            countryEn = "Canada",
            countryFr = "Canada",
            city = "Toronto, Ontario",
            address = "81 St Mary St, Toronto, ON M5S 1J4, Canadá",
            flag = "🇨🇦",
            foundedYear = "1852",
            description = "Universidad católica fundada por los Padres Basilianos y federada con la Universidad de Toronto. Alberga el prestigioso Pontifical Institute of Mediaeval Studies (PIMS) y la Facultad de Teología.",
            descriptionEn = "Catholic university founded by the Basilian Fathers and federated with the University of Toronto. Home to the prestigious Pontifical Institute of Mediaeval Studies (PIMS) and the Faculty of Theology.",
            descriptionFr = "Université catholique fondée par les Pères Basiliens et fédérée à l'Université de Toronto. Abrite le prestigieux Institut Pontifical d'Études Médiévales (PIMS) et la Faculté de Théologie.",
            recentNews2025 = "Promueve cátedras internacionales sobre ética de la inteligencia artificial, ecología integral y diálogo entre fe, cultura y ciencia.",
            recentNews2025En = "Promotes international chairs on AI ethics, integral ecology, and dialogue between faith, culture, and science.",
            recentNews2025Fr = "Promeut des chaires internationales sur l'éthique de l'intelligence artificielle, l'écologie intégrale et le dialogue entre foi, culture et science.",
            websiteUrl = "https://stmikes.utoronto.ca",
            secondaryUrlLabel = "Noticias USMC",
            secondaryUrl = "https://stmikes.utoronto.ca/news",
            latitudeDeg = 43.6644f,
            longitudeDeg = -79.3897f,
            mottoOrHighlight = "Excelencia Universitaria y Humanismo Cristiano en Toronto",
            mottoOrHighlightEn = "University Excellence & Christian Humanism in Toronto",
            mottoOrHighlightFr = "Excellence Universitaire et Humanisme Chrétien à Toronto",
            panoramicImageUrl = "https://images.unsplash.com/photo-1562774053-701939374585?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "University of St. Michael's College 81 St Mary St Toronto Canada"
        ),
        BasilianGlobalWork(
            id = "ca_toronto_curia_parish",
            name = "St. Basil's Catholic Parish & Sede General CSB",
            nameEn = "St. Basil's Catholic Parish & CSB Motherhouse",
            nameFr = "Paroisse Catholique St. Basil's & Maison Générale CSB",
            shortPinLabel = "Toronto · St. Basil's Parish",
            shortPinLabelEn = "Toronto · St. Basil's Parish",
            shortPinLabelFr = "Toronto · Paroisse St. Basil's",
            category = BasilianWorkCategory.PARROQUIA_MISION,
            country = "Canadá",
            countryEn = "Canada",
            countryFr = "Canada",
            city = "Toronto, Ontario",
            address = "50 St Joseph St / 360 Davenport Rd, Toronto, ON M5S 1J4, Canadá",
            flag = "🇨🇦",
            foundedYear = "1856",
            description = "Parroquia madre basiliana en Norteamérica y sede de la Curia General de la Congregación de San Basilio, desde donde se coordina la misión educativa y pastoral en los cinco países.",
            descriptionEn = "Basilian mother parish in North America and headquarters of the General Curia of the Congregation of St. Basil, coordinating the educational and pastoral mission across five countries.",
            descriptionFr = "Paroisse mère basilienne en Amérique du Nord et siège de la Curie Générale de la Congrégation de Saint-Basile, coordonnant la mission éducative et pastorale dans les cinq pays.",
            recentNews2025 = "Coordina los encuentros internacionales de la Red Basiliana para la Educación (BNENE) y las misiones de América Latina.",
            recentNews2025En = "Coordinates the international gatherings of the Basilian Network for Education (BNENE) and the missions in Latin America.",
            recentNews2025Fr = "Coordonne les rencontres internationales du Réseau Basilien pour l'Éducation (BNENE) et les missions en Amérique latine.",
            websiteUrl = "https://www.basilian.org/es/",
            secondaryUrlLabel = "Parroquia St. Basil",
            secondaryUrl = "https://www.stbasiltoronto.org",
            latitudeDeg = 43.6653f,
            longitudeDeg = -79.3886f,
            mottoOrHighlight = "Sede del Gobierno General y Parroquia Universitaria",
            mottoOrHighlightEn = "General Headquarters & University Parish",
            mottoOrHighlightFr = "Siège du Gouvernement Général et Paroisse Universitaire",
            panoramicImageUrl = "https://images.unsplash.com/photo-1548625149-fc4a29cf7092?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "St. Basil's Catholic Parish 50 St Joseph St Toronto Canada"
        ),
        BasilianGlobalWork(
            id = "ca_edmonton_st_joseph",
            name = "St. Joseph's College — University of Alberta",
            nameEn = "St. Joseph's College — University of Alberta",
            nameFr = "Collège Universitaire St. Joseph's — Université de l'Alberta",
            shortPinLabel = "Edmonton · St. Joseph's",
            shortPinLabelEn = "Edmonton · St. Joseph's",
            shortPinLabelFr = "Edmonton · St. Joseph's",
            category = BasilianWorkCategory.UNIVERSIDAD,
            country = "Canadá",
            countryEn = "Canada",
            countryFr = "Canada",
            city = "Edmonton, Alberta",
            address = "11325 89 Ave NW, Edmonton, AB T6G 2J5, Canadá",
            flag = "🇨🇦",
            foundedYear = "1926",
            description = "Colegio universitario católico de tradición basiliana en la Universidad de Alberta, con programas de ética, filosofía, teología, residencias universitarias y capellanía estudiantil.",
            descriptionEn = "Catholic university college in the Basilian tradition at the University of Alberta, offering courses in ethics, philosophy, and theology, student residences, and campus chaplaincy.",
            descriptionFr = "Collège universitaire catholique de tradition basilienne à l'Université de l'Alberta, offrant des programmes d'éthique, de philosophie et de théologie, des résidences et une aumônerie.",
            recentNews2025 = "Impulsa becas de liderazgo solidario y acompañamiento académico-espiritual para miles de jóvenes universitarios del oeste canadiense.",
            recentNews2025En = "Provides solidarity leadership scholarships and academic-spiritual accompaniment for thousands of university students in Western Canada.",
            recentNews2025Fr = "Soutient des bourses de leadership solidaire et l'accompagnement académique et spirituel de milliers d'étudiants de l'Ouest canadien.",
            websiteUrl = "https://www.ualberta.ca/st-josephs",
            latitudeDeg = 53.5244f,
            longitudeDeg = -113.5242f,
            mottoOrHighlight = "Comunidad Universitaria y Fe en el Oeste de Canadá",
            mottoOrHighlightEn = "University Community & Faith in Western Canada",
            mottoOrHighlightFr = "Communauté Universitaire et Foi dans l'Ouest Canadien",
            panoramicImageUrl = "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "St. Joseph's College University of Alberta Edmonton Canada"
        ),
        BasilianGlobalWork(
            id = "ca_windsor_assumption",
            name = "Assumption University & Parroquia de la Asunción",
            nameEn = "Assumption University & Our Lady of the Assumption Parish",
            nameFr = "Université Assumption & Paroisse de l'Assomption",
            shortPinLabel = "Windsor · Assumption Univ.",
            shortPinLabelEn = "Windsor · Assumption Univ.",
            shortPinLabelFr = "Windsor · Univ. Assumption",
            category = BasilianWorkCategory.UNIVERSIDAD,
            country = "Canadá",
            countryEn = "Canada",
            countryFr = "Canada",
            city = "Windsor, Ontario",
            address = "400 Huron Church Rd, Windsor, ON N9C 2J9, Canadá",
            flag = "🇨🇦",
            foundedYear = "1857",
            description = "Institución universitaria histórica vinculada al legado basiliano en Windsor, dedicada al posgrado en pastoral, ética social y diálogo interreligioso.",
            descriptionEn = "Historic university institution linked to the Basilian legacy in Windsor, dedicated to graduate studies in pastoral ministry, social ethics, and interfaith dialogue.",
            descriptionFr = "Institution universitaire historique liée à l'héritage basilien à Windsor, dédiée aux études supérieures en pastorale, éthique sociale et dialogue interreligieux.",
            recentNews2025 = "Desarrolla diplomados en liderazgo educativo católico y programas de historia basiliana en la frontera canadiense.",
            recentNews2025En = "Offers graduate diplomas in Catholic educational leadership and Basilian heritage programs on the Canadian border.",
            recentNews2025Fr = "Développe des diplômes en leadership éducatif catholique et des programmes d'histoire basilienne à la frontière canadienne.",
            websiteUrl = "https://assumptionu.ca",
            latitudeDeg = 42.3065f,
            longitudeDeg = -83.0668f,
            mottoOrHighlight = "Herencia Universitaria y Pastoral en Windsor",
            mottoOrHighlightEn = "University & Pastoral Heritage in Windsor",
            mottoOrHighlightFr = "Héritage Universitaire et Pastoral à Windsor",
            panoramicImageUrl = "https://images.unsplash.com/photo-1498243691581-b145c3f54a5a?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "Assumption University 400 Huron Church Rd Windsor Ontario Canada"
        ),

        // ==================== ESTADOS UNIDOS ====================
        BasilianGlobalWork(
            id = "us_houston_ust",
            name = "University of St. Thomas (UST)",
            nameEn = "University of St. Thomas (UST)",
            nameFr = "Université de St. Thomas (UST — Houston)",
            shortPinLabel = "Houston · Univ. St. Thomas",
            shortPinLabelEn = "Houston · Univ. St. Thomas",
            shortPinLabelFr = "Houston · Univ. St. Thomas",
            category = BasilianWorkCategory.UNIVERSIDAD,
            country = "EE. UU.",
            countryEn = "USA",
            countryFr = "États-Unis",
            city = "Houston, Texas",
            address = "3800 Montrose Blvd, Houston, TX 77006, Estados Unidos",
            flag = "🇺🇸",
            foundedYear = "1947",
            description = "La universidad católica de Houston fundada por los Padres Basilianos. Combina formación humanista con programas de vanguardia en enfermería, ingeniería, negocios, educación y teología.",
            descriptionEn = "Houston's Catholic university founded by the Basilian Fathers. Combines humanist formation with cutting-edge programs in nursing, engineering, business, education, and theology.",
            descriptionFr = "L'université catholique de Houston fondée par les Pères Basiliens. Allie formation humaniste et programmes de pointe en sciences infirmières, ingénierie, gestion, éducation et théologie.",
            recentNews2025 = "Expande sus programas de becas para estudiantes hispanos, innovación tecnológica ética y misiones universitarias internacionales.",
            recentNews2025En = "Expands scholarship programs for Hispanic students, ethical tech innovation, and international university missions.",
            recentNews2025Fr = "Développe ses programmes de bourses pour les étudiants hispaniques, l'innovation technologique éthique et les missions universitaires internationales.",
            websiteUrl = "https://www.stthom.edu",
            secondaryUrlLabel = "Noticias UST",
            secondaryUrl = "https://news.stthom.edu",
            latitudeDeg = 29.7380f,
            longitudeDeg = -95.3935f,
            mottoOrHighlight = "Crescamus in Christo — Universidad Católica de Houston",
            mottoOrHighlightEn = "Crescamus in Christo — Houston's Catholic University",
            mottoOrHighlightFr = "Crescamus in Christo — Université Catholique de Houston",
            panoramicImageUrl = "https://images.unsplash.com/photo-1592280771190-3e2e4d571952?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "University of St. Thomas 3800 Montrose Blvd Houston TX"
        ),
        BasilianGlobalWork(
            id = "us_houston_sths",
            name = "St. Thomas High School",
            nameEn = "St. Thomas High School",
            nameFr = "Lycée St. Thomas (Houston)",
            shortPinLabel = "Houston · St. Thomas HS",
            shortPinLabelEn = "Houston · St. Thomas HS",
            shortPinLabelFr = "Houston · Lycée St. Thomas",
            category = BasilianWorkCategory.COLEGIO,
            country = "EE. UU.",
            countryEn = "USA",
            countryFr = "États-Unis",
            city = "Houston, Texas",
            address = "4500 Memorial Dr, Houston, TX 77007, Estados Unidos",
            flag = "🇺🇸",
            foundedYear = "1900",
            description = "Prestigioso colegio secundario basiliano en Houston con más de 125 años formando jóvenes en liderazgo académico, investigación científica y compromiso cristiano.",
            descriptionEn = "Prestigious Basilian college-preparatory high school in Houston with over 125 years forming young men in academic leadership, scientific inquiry, and Christian service.",
            descriptionFr = "Prestigieux lycée basilien à Houston formant depuis plus de 125 ans des jeunes au leadership académique, à la recherche scientifique et à l'engagement chrétien.",
            recentNews2025 = "Celebra más de 125 años de excelencia con intercambios estudiantiles de la Red Basiliana (BNENE) y proyectos de servicio social en Texas.",
            recentNews2025En = "Celebrates over 125 years of excellence with student exchanges across the Basilian Network (BNENE) and social service projects in Texas.",
            recentNews2025Fr = "Célèbre plus de 125 ans d'excellence avec des échanges étudiants du Réseau Basilien (BNENE) et des projets de service social au Texas.",
            websiteUrl = "https://www.sths.org",
            latitudeDeg = 29.7661f,
            longitudeDeg = -95.4065f,
            mottoOrHighlight = "125+ Años de Educación Basiliana en Texas",
            mottoOrHighlightEn = "125+ Years of Basilian Education in Texas",
            mottoOrHighlightFr = "Plus de 125 Ans d'Éducation Basilienne au Texas",
            panoramicImageUrl = "https://images.unsplash.com/photo-1580582932707-520aed937b7b?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "St. Thomas High School 4500 Memorial Dr Houston TX"
        ),
        BasilianGlobalWork(
            id = "us_detroit_catholic_central",
            name = "Detroit Catholic Central High School",
            nameEn = "Detroit Catholic Central High School",
            nameFr = "Lycée Detroit Catholic Central",
            shortPinLabel = "Novi · Catholic Central HS",
            shortPinLabelEn = "Novi · Catholic Central HS",
            shortPinLabelFr = "Novi · Catholic Central",
            category = BasilianWorkCategory.COLEGIO,
            country = "EE. UU.",
            countryEn = "USA",
            countryFr = "États-Unis",
            city = "Novi (Detroit), Míchigan",
            address = "27225 Wixom Rd, Novi, MI 48374, Estados Unidos",
            flag = "🇺🇸",
            foundedYear = "1928",
            description = "El colegio secundario católico más grande de Míchigan, dirigido por los Padres Basilianos. Reconocido por sus programas de robótica FIRST, ciencias, deportes y fraternidad estudiantil.",
            descriptionEn = "Michigan's premier Catholic high school founded and animated by the Basilian Fathers. Renowned for its championship FIRST Robotics, STEM, athletics, and student brotherhood.",
            descriptionFr = "Le plus grand lycée catholique du Michigan, animé par les Pères Basiliens. Reconnu pour ses programmes de robotique FIRST, de sciences, de sports et de fraternité étudiante.",
            recentNews2025 = "Sus equipos de robótica e ingeniería destacan a nivel nacional mientras fortalecen las campañas solidarias comunitarias.",
            recentNews2025En = "Its robotics and engineering teams excel nationally while strengthening community solidarity campaigns.",
            recentNews2025Fr = "Ses équipes de robotique et d'ingénierie brillent au niveau national tout en renforçant les campagnes de solidarité communautaire.",
            websiteUrl = "https://www.catholiccentral.net",
            latitudeDeg = 42.4910f,
            longitudeDeg = -83.5356f,
            mottoOrHighlight = "Liderazgo en Ciencia, Robótica y Fraternidad en Míchigan",
            mottoOrHighlightEn = "Leadership in Science, Robotics & Brotherhood in Michigan",
            mottoOrHighlightFr = "Leadership en Sciences, Robotique et Fraternité au Michigan",
            panoramicImageUrl = "https://images.unsplash.com/photo-1509062522246-3755977927d7?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "Detroit Catholic Central High School 27225 Wixom Rd Novi MI"
        ),
        BasilianGlobalWork(
            id = "us_rochester_fisher_aquinas",
            name = "St. John Fisher University & The Aquinas Institute",
            nameEn = "St. John Fisher University & The Aquinas Institute",
            nameFr = "Université St. John Fisher & Institut Aquinas (Rochester)",
            shortPinLabel = "Rochester · Fisher & Aquinas",
            shortPinLabelEn = "Rochester · Fisher & Aquinas",
            shortPinLabelFr = "Rochester · Fisher & Aquinas",
            category = BasilianWorkCategory.UNIVERSIDAD,
            country = "EE. UU.",
            countryEn = "USA",
            countryFr = "États-Unis",
            city = "Rochester, Nueva York",
            address = "3690 East Ave & 1127 Dewey Ave, Rochester, NY 14618, Estados Unidos",
            flag = "🇺🇸",
            foundedYear = "1925 / 1948",
            description = "Dos baluartes educativos de raíz basiliana en el estado de Nueva York: The Aquinas Institute (educación media) y St. John Fisher University (educación superior en farmacia, educación y humanidades).",
            descriptionEn = "Two educational strongholds of Basilian heritage in New York State: The Aquinas Institute (secondary education) and St. John Fisher University (higher education in pharmacy, nursing, education, and humanities).",
            descriptionFr = "Deux bastions éducatifs d'héritage basilien dans l'État de New York : The Aquinas Institute (enseignement secondaire) et St. John Fisher University (enseignement supérieur en pharmacie, éducation et sciences humaines).",
            recentNews2025 = "Mantienen vivo el carisma de Bondad, Disciplina y Ciencia con programas de mentoría y servicio comunitario en Rochester.",
            recentNews2025En = "Keep the charism of Goodness, Discipline, and Knowledge alive through mentorship and community service programs in Rochester.",
            recentNews2025Fr = "Maintiennent vivant le charisme de Bonté, Discipline et Science grâce à des programmes de mentorat et de service communautaire à Rochester.",
            websiteUrl = "https://www.sjf.edu",
            secondaryUrlLabel = "Aquinas Institute",
            secondaryUrl = "https://www.aquinasinstitute.com",
            latitudeDeg = 43.1154f,
            longitudeDeg = -77.5110f,
            mottoOrHighlight = "Tradición Educativa Secundaria y Universitaria en Nueva York",
            mottoOrHighlightEn = "Secondary & University Educational Tradition in New York",
            mottoOrHighlightFr = "Tradition Éducative Secondaire et Universitaire à New York",
            panoramicImageUrl = "https://images.unsplash.com/photo-1564981797816-1043664bf78d?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "St. John Fisher University 3690 East Ave Rochester NY"
        ),
        BasilianGlobalWork(
            id = "us_texas_parishes",
            name = "Parroquias Basilianas en Texas (St. Anne, St. Laurence & Most Holy Trinity)",
            nameEn = "Basilian Parishes in Texas (St. Anne, St. Laurence & Most Holy Trinity)",
            nameFr = "Paroisses Basiliennes au Texas (St. Anne, St. Laurence & Most Holy Trinity)",
            shortPinLabel = "Texas · Parroquias CSB",
            shortPinLabelEn = "Texas · CSB Parishes",
            shortPinLabelFr = "Texas · Paroisses CSB",
            category = BasilianWorkCategory.PARROQUIA_MISION,
            country = "EE. UU.",
            countryEn = "USA",
            countryFr = "États-Unis",
            city = "Houston, Sugar Land y Angleton, Texas",
            address = "2140 Westheimer Rd, Houston, TX 77098 / 3100 Sweetwater Blvd, Sugar Land, TX 77479",
            flag = "🇺🇸",
            foundedYear = "1940",
            description = "Red de vibrantes parroquias bilingües y escuelas parroquiales acompañadas por los Padres Basilianos en el área metropolitana de Houston y el sur de Texas.",
            descriptionEn = "Network of vibrant bilingual parishes and parochial schools served by the Basilian Fathers across Greater Houston and South Texas.",
            descriptionFr = "Réseau de paroisses bilingues dynamiques et d'écoles paroissiales accompagnées par les Pères Basiliens dans la région de Houston et du sud du Texas.",
            recentNews2025 = "Ministerio hispano y angloparlante, escuelas parroquiales y misiones de solidaridad con comunidades migrantes.",
            recentNews2025En = "Hispanic and English-speaking ministry, parochial schools, and solidarity outreach with migrant communities.",
            recentNews2025Fr = "Ministère hispanophone et anglophone, écoles paroissiales et missions de solidarité auprès des communautés migrantes.",
            websiteUrl = "https://www.saintanne.org",
            secondaryUrlLabel = "St. Laurence",
            secondaryUrl = "https://stlaurence.org",
            latitudeDeg = 29.7426f,
            longitudeDeg = -95.4122f,
            mottoOrHighlight = "Comunidades Parroquiales Bilingües y Escuelas Parroquiales",
            mottoOrHighlightEn = "Bilingual Parish Communities & Parochial Schools",
            mottoOrHighlightFr = "Communautés Paroissiales Bilingues et Écoles Paroissiales",
            panoramicImageUrl = "https://images.unsplash.com/photo-1438032005730-c779502df39b?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "St. Anne Catholic Church 2140 Westheimer Rd Houston TX"
        ),

        // ==================== FRANCIA (Cuna Fundacional) ====================
        BasilianGlobalWork(
            id = "fr_annonay_cuna",
            name = "Collège d'Annonay & Cuna Histórica de la Congregación (1822)",
            nameEn = "Collège d'Annonay & Historical Cradle of the Congregation (1822)",
            nameFr = "Collège d'Annonay & Berceau Historique de la Congrégation (1822)",
            shortPinLabel = "Annonay · Cuna 1822",
            shortPinLabelEn = "Annonay · 1822 Cradle",
            shortPinLabelFr = "Annonay · Berceau 1822",
            category = BasilianWorkCategory.COLEGIO,
            country = "Francia",
            countryEn = "France",
            countryFr = "France",
            city = "Annonay, Ardèche",
            address = "Montée du Cardinal de Tournon / Rue Sadi Carnot, 07100 Annonay, Ardèche, Francia",
            flag = "🇫🇷",
            foundedYear = "1822",
            description = "En las montañas de Ardèche, diez sacerdotes educadores fundaron la Congregación de San Basilio el 21 de noviembre de 1822 para reconstruir la educación cristiana de la juventud.",
            descriptionEn = "In the mountains of Ardèche, ten priest-educators founded the Congregation of St. Basil on November 21, 1822, to rebuild the Christian education of youth.",
            descriptionFr = "Dans les montagnes de l'Ardèche, dix prêtres éducateurs ont fondé la Congrégation de Saint-Basile le 21 novembre 1822 pour reconstruire l'éducation chrétienne de la jeunesse.",
            recentNews2025 = "Lugar de peregrinación y memoria histórica para educadores, estudiantes y religiosos de toda la Red Basiliana Mundial.",
            recentNews2025En = "Place of pilgrimage and historical memory for educators, students, and religious across the Worldwide Basilian Network.",
            recentNews2025Fr = "Lieu de pèlerinage et de mémoire historique pour les éducateurs, les étudiants et les religieux de tout le Réseau Basilien Mondial.",
            websiteUrl = "https://www.basilian.org/es/quienes-somos/historia/",
            latitudeDeg = 45.2405f,
            longitudeDeg = 4.6708f,
            mottoOrHighlight = "Cuna Histórica donde Nació el Carisma Educador Basiliano (1822)",
            mottoOrHighlightEn = "Historical Cradle where the Basilian Educational Charism Was Born (1822)",
            mottoOrHighlightFr = "Berceau Historique où est Né le Charisme Éducatif Basilien (1822)",
            panoramicImageUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&fit=crop&w=1200&q=80",
            googleMapsQuery = "Annonay 07100 Ardeche France"
        )
    )
}

// ============================================================================
// PANTALLA PRINCIPAL: RED DE BASILIANOS PARA LA EDUCACIÓN Y MUNDO 3D FUTURISTA
// ============================================================================

@Composable
fun LiturgiaHorasScreen(
    viewModel: BasilianViewModel,
    appLanguage: AppLanguage = AppLanguage.ES,
    onBack: (() -> Unit)? = null
) {
    val context = LocalContext.current

    var selectedCountry by remember { mutableStateOf(BasilianCountryFilter.ALL) }
    var selectedCategory by remember { mutableStateOf<BasilianWorkCategory?>(null) }
    var selectedWork by remember {
        mutableStateOf<BasilianGlobalWork?>(BasilianEducationAndWorksRepository.allWorks.first())
    }
    var panoramicModalWork by remember { mutableStateOf<BasilianGlobalWork?>(null) }
    var isMeaningSectionExpanded by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredWorks = remember(selectedCountry, selectedCategory, searchQuery, appLanguage) {
        BasilianEducationAndWorksRepository.allWorks.filter { work ->
            val matchesCountry = selectedCountry == BasilianCountryFilter.ALL || work.country == selectedCountry.id
            val matchesCategory = selectedCategory == null || work.category == selectedCategory
            val matchesQuery = searchQuery.isBlank() ||
                work.localizedName(appLanguage).contains(searchQuery, ignoreCase = true) ||
                work.name.contains(searchQuery, ignoreCase = true) ||
                work.city.contains(searchQuery, ignoreCase = true) ||
                work.address.contains(searchQuery, ignoreCase = true) ||
                work.localizedCountry(appLanguage).contains(searchQuery, ignoreCase = true) ||
                work.localizedDescription(appLanguage).contains(searchQuery, ignoreCase = true)
            matchesCountry && matchesCategory && matchesQuery
        }
    }

    val countryScopedWorks = remember(selectedCountry) {
        if (selectedCountry == BasilianCountryFilter.ALL) {
            BasilianEducationAndWorksRepository.allWorks
        } else {
            BasilianEducationAndWorksRepository.allWorks.filter { it.country == selectedCountry.id }
        }
    }
    val colegiosCount = remember(countryScopedWorks) {
        countryScopedWorks.count { it.category == BasilianWorkCategory.COLEGIO }
    }
    val universidadesCount = remember(countryScopedWorks) {
        countryScopedWorks.count { it.category == BasilianWorkCategory.UNIVERSIDAD }
    }
    val parroquiasCount = remember(countryScopedWorks) {
        countryScopedWorks.count { it.category == BasilianWorkCategory.PARROQUIA_MISION }
    }

    val openUrl: (String) -> Unit = { url ->
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    val openGoogleMapsNavigation: (BasilianGlobalWork) -> Unit = { work ->
        try {
            val encoded = URLEncoder.encode("${work.name}, ${work.address}", "UTF-8")
            val uri = Uri.parse("geo:${work.latitudeDeg},${work.longitudeDeg}?q=$encoded")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.google.android.apps.maps")
            }
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            } else {
                openUrl("https://www.google.com/maps/search/?api=1&query=$encoded")
            }
        } catch (_: Exception) {
            openUrl("https://www.google.com/maps/search/?api=1&query=${work.latitudeDeg},${work.longitudeDeg}")
        }
    }

    val openStreetView360: (BasilianGlobalWork) -> Unit = { work ->
        try {
            val svUri = Uri.parse("google.streetview:cbll=${work.latitudeDeg},${work.longitudeDeg}")
            val svIntent = Intent(Intent.ACTION_VIEW, svUri).apply {
                setPackage("com.google.android.apps.maps")
            }
            if (svIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(svIntent)
            } else {
                openUrl("https://www.google.com/maps/@?api=1&map_action=pano&viewpoint=${work.latitudeDeg},${work.longitudeDeg}")
            }
        } catch (_: Exception) {
            openUrl("https://www.google.com/maps/@?api=1&map_action=pano&viewpoint=${work.latitudeDeg},${work.longitudeDeg}")
        }
    }

    val cyanAccent = Color(0xFF00F0FF)
    val goldAccent = Color(0xFFFBBF24)
    val emeraldAccent = Color(0xFF34D399)
    val violetAccent = Color(0xFFA78BFA)

    // Modal Panorámico e Imagen + Google Maps que se abre inmediatamente al tocar una obra en el Mundo 3D
    panoramicModalWork?.let { workToInspect ->
        ObraPanoramicaGoogleMapsModal(
            work = workToInspect,
            appLanguage = appLanguage,
            onDismiss = { panoramicModalWork = null },
            onOpenGoogleMapsApp = { openGoogleMapsNavigation(workToInspect) },
            onOpenStreetViewApp = { openStreetView360(workToInspect) },
            onOpenOfficialWebsite = { openUrl(workToInspect.websiteUrl) }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF020612),
                        Color(0xFF051129),
                        Color(0xFF081938),
                        Color(0xFF020612)
                    )
                )
            )
            .testTag("red_educacion_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ====================================================================
        // 1. PRESENTACIÓN FUTURISTA: RED DE BASILIANOS PARA LA EDUCACIÓN (ES/EN/FR)
        // ====================================================================
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .futuristic3DCard(
                        accentColor = cyanAccent,
                        secondaryColor = goldAccent,
                        cornerRadius = 26.dp,
                        tiltDegrees = 1.4f
                    )
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            if (onBack != null) {
                                IconButton(
                                    onClick = onBack,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.08f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = when (appLanguage) {
                                            AppLanguage.EN -> "Back"
                                            AppLanguage.FR -> "Retour"
                                            else -> "Volver"
                                        },
                                        tint = Color.White
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                cyanAccent.copy(alpha = 0.35f),
                                                goldAccent.copy(alpha = 0.30f)
                                            )
                                        )
                                    )
                                    .border(1.5.dp, cyanAccent.copy(alpha = 0.8f), RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Public,
                                    contentDescription = when (appLanguage) {
                                        AppLanguage.EN -> "Basilian Network for Education"
                                        AppLanguage.FR -> "Réseau Basilien d'Éducation"
                                        else -> "Red de Basilianos para la Educación"
                                    },
                                    tint = goldAccent,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Surface(
                                    color = cyanAccent.copy(alpha = 0.16f),
                                    border = BorderStroke(1.dp, cyanAccent.copy(alpha = 0.6f)),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = when (appLanguage) {
                                            AppLanguage.EN -> "CONGREGATION OF ST. BASIL · SINCE 1822"
                                            AppLanguage.FR -> "CONGRÉGATION DE SAINT-BASILE · DEPUIS 1822"
                                            else -> "CONGREGACIÓN DE SAN BASILIO · DESDE 1822"
                                        },
                                        color = cyanAccent,
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.1.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                    )
                                }
                                Text(
                                    text = when (appLanguage) {
                                        AppLanguage.EN -> "Basilian Network for Education (BNENE)"
                                        AppLanguage.FR -> "Réseau Basilien pour l'Éducation (BNENE)"
                                        else -> "Red de Basilianos para la Educación (BNENE)"
                                    },
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }
                    }

                    // Frase lema destacada con estética luminosa
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        goldAccent.copy(alpha = 0.18f),
                                        cyanAccent.copy(alpha = 0.12f)
                                    )
                                )
                            )
                            .border(1.dp, goldAccent.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "«Bonitatem et disciplinam et scientiam doce me»",
                                color = goldAccent,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontStyle = FontStyle.Italic,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = when (appLanguage) {
                                    AppLanguage.EN -> "“Teach me Goodness, Discipline, and Knowledge” (Psalm 119:66) — Global alliance of schools, universities, parishes, and missions of the Basilian Fathers across Canada, USA, Mexico, Colombia, and France."
                                    AppLanguage.FR -> "« Enseigne-moi la Bonté, la Discipline et la Science » (Psaume 118, 66) — Alliance mondiale d'écoles, universités, paroisses et missions des Pères Basiliens au Canada, aux États-Unis, au Mexique, en Colombie et en France."
                                    else -> "«Enséñame la Bondad, la Disciplina y la Ciencia» (Salmo 118, 66) — Alianza mundial de colegios, universidades, parroquias y misiones de los Padres Basilianos en Canadá, EE. UU., México, Colombia y Francia."
                                },
                                color = Color(0xFFE2E8F0),
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }

                    // Indicadores globales y acceso rápido a basilian.org
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = emeraldAccent.copy(alpha = 0.16f),
                                border = BorderStroke(1.dp, emeraldAccent.copy(alpha = 0.55f)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = when (appLanguage) {
                                        AppLanguage.EN -> "🌍 5 Countries · ${BasilianEducationAndWorksRepository.allWorks.size} Active Works"
                                        AppLanguage.FR -> "🌍 5 Pays · ${BasilianEducationAndWorksRepository.allWorks.size} Œuvres Actives"
                                        else -> "🌍 5 Países · ${BasilianEducationAndWorksRepository.allWorks.size} Obras Activas"
                                    },
                                    color = emeraldAccent,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { openUrl("https://www.basilian.org") },
                            border = BorderStroke(1.dp, goldAccent.copy(alpha = 0.7f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "basilian.org",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }

        // ====================================================================
        // 2. APARTADO: ¿QUÉ SIGNIFICA LA RED BASILIANA PARA LA EDUCACIÓN? (ES/EN/FR)
        // ====================================================================
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .futuristic3DCard(
                        accentColor = goldAccent,
                        secondaryColor = cyanAccent,
                        cornerRadius = 24.dp,
                        tiltDegrees = 1.0f
                    )
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isMeaningSectionExpanded = !isMeaningSectionExpanded },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(goldAccent.copy(alpha = 0.22f))
                                    .border(1.dp, goldAccent, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = goldAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = when (appLanguage) {
                                        AppLanguage.EN -> "IDENTITY & PEDAGOGICAL VISION"
                                        AppLanguage.FR -> "IDENTITÉ ET SENS PÉDAGOGIQUE"
                                        else -> "IDENTIDAD Y SENTIDO PEDAGÓGICO"
                                    },
                                    color = goldAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = when (appLanguage) {
                                        AppLanguage.EN -> "What does the Basilian Network for Education mean?"
                                        AppLanguage.FR -> "Que signifie le Réseau Basilien pour l'Éducation ?"
                                        else -> "¿Qué significa la Red Basiliana para la Educación?"
                                    },
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }

                        Icon(
                            imageVector = if (isMeaningSectionExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = when (appLanguage) {
                                AppLanguage.EN -> "Expand or collapse meaning"
                                AppLanguage.FR -> "Développer ou réduire"
                                else -> "Expandir o contraer significado"
                            },
                            tint = cyanAccent
                        )
                    }

                    AnimatedVisibility(
                        visible = isMeaningSectionExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = when (appLanguage) {
                                    AppLanguage.EN -> "The Basilian Network for Education and the New Evangelization (BNENE / B.E.N.) is the living communion of schools, universities, educational parishes, social outreach centers, and houses of formation animated by the Basilian Fathers since their founding in Annonay, France (1822). Its mission is to educate the whole human person—mind, heart, and spirit—uniting Catholic faith with intellectual excellence and commitment to social justice."
                                    AppLanguage.FR -> "Le Réseau Basilien pour l'Éducation et la Nouvelle Évangélisation (BNENE / B.E.N.) est la communion vivante des écoles, universités, paroisses éducatives, centres pastoraux et maisons de formation animés par les Pères Basiliens depuis leur fondation à Annonay, en France (1822). Sa mission est d'éduquer intégralement la personne humaine — esprit, cœur et âme — en unissant la foi catholique à l'excellence intellectuelle et à l'engagement pour la justice sociale."
                                    else -> "La Red Basiliana para la Educación y la Nueva Evangelización (BNENE / B.E.N.) es la comunión viva de las instituciones académicas, parroquias educativas, centros de pastoral social y casas de formación animadas por los Padres Basilianos desde su fundación en Annonay, Francia (1822). Su propósito es educar integralmente a la persona humana —mente, corazón y espíritu— uniendo la fe católica con la excelencia intelectual y el compromiso por la justicia social."
                                },
                                color = Color(0xFFE2E8F0),
                                fontSize = 12.5.sp,
                                lineHeight = 18.5.sp
                            )

                            // Los 3 Pilares del Carisma Educativo Basiliano (ES/EN/FR)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                PillarMeaningCard(
                                    modifier = Modifier.weight(1f),
                                    emoji = "💛",
                                    title = when (appLanguage) {
                                        AppLanguage.EN -> "GOODNESS"
                                        AppLanguage.FR -> "BONTÉ"
                                        else -> "BONDAD"
                                    },
                                    latin = "Bonitatem",
                                    description = when (appLanguage) {
                                        AppLanguage.EN -> "Fraternal charity, compassion, human dignity, and service to the most vulnerable."
                                        AppLanguage.FR -> "Charité fraternelle, compassion, dignité humaine et service des plus vulnérables."
                                        else -> "Caridad fraterna, compasión, dignidad humana y servicio a los más vulnerables."
                                    },
                                    accent = goldAccent
                                )
                                PillarMeaningCard(
                                    modifier = Modifier.weight(1f),
                                    emoji = "⚖️",
                                    title = when (appLanguage) {
                                        AppLanguage.EN -> "DISCIPLINE"
                                        AppLanguage.FR -> "DISCIPLINE"
                                        else -> "DISCIPLINA"
                                    },
                                    latin = "Disciplinam",
                                    description = when (appLanguage) {
                                        AppLanguage.EN -> "Character formation, inner freedom, ethical maturity, and community life."
                                        AppLanguage.FR -> "Formation du caractère, liberté intérieure, maturité éthique et vie fraternelle."
                                        else -> "Forja del carácter, libertad interior, madurez ética y vida en comunidad."
                                    },
                                    accent = cyanAccent
                                )
                                PillarMeaningCard(
                                    modifier = Modifier.weight(1f),
                                    emoji = "🔬",
                                    title = when (appLanguage) {
                                        AppLanguage.EN -> "KNOWLEDGE"
                                        AppLanguage.FR -> "SCIENCE"
                                        else -> "CIENCIA"
                                    },
                                    latin = "Scientiam",
                                    description = when (appLanguage) {
                                        AppLanguage.EN -> "Rigorous pursuit of truth and fruitful dialogue between faith, culture, and reason."
                                        AppLanguage.FR -> "Recherche rigoureuse de la vérité et dialogue fécond entre foi, culture et raison."
                                        else -> "Búsqueda rigurosa de la verdad y diálogo fecundo entre fe, cultura y razón."
                                    },
                                    accent = emeraldAccent
                                )
                            }
                        }
                    }
                }
            }
        }

        // ====================================================================
        // 3. PARTE SUPERIOR: PAÍSES + ORGANIZACIÓN DE COLEGIOS, UNIVERSIDADES,
        //    PARROQUIAS Y FORMACIÓN + NOTA DISCRETA DE LO QUE NO SE TIENE (ES/EN/FR)
        // ====================================================================
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF071329).copy(alpha = 0.94f))
                    .border(1.2.dp, cyanAccent.copy(alpha = 0.45f), RoundedCornerShape(24.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "1. COUNTRIES OF BASILIAN PRESENCE"
                            AppLanguage.FR -> "1. PAYS DE PRÉSENCE BASILIENNE"
                            else -> "1. PAÍSES DE PRESENCIA BASILIANA"
                        },
                        color = goldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Surface(
                        color = cyanAccent.copy(alpha = 0.16f),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, cyanAccent.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = when (appLanguage) {
                                AppLanguage.EN -> "${countryScopedWorks.size} active works"
                                AppLanguage.FR -> "${countryScopedWorks.size} œuvres actives"
                                else -> "${countryScopedWorks.size} obras activas"
                            },
                            color = cyanAccent,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                }

                // Selector de Países (traducido dinámicamente)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BasilianCountryFilter.entries.forEach { countryFilter ->
                        val isSelected = selectedCountry == countryFilter
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedCountry = countryFilter
                                val firstInCountry = BasilianEducationAndWorksRepository.allWorks.firstOrNull {
                                    countryFilter == BasilianCountryFilter.ALL || it.country == countryFilter.id
                                }
                                if (firstInCountry != null) {
                                    selectedWork = firstInCountry
                                }
                            },
                            label = {
                                Text(
                                    text = "${countryFilter.flag} ${countryFilter.localizedLabel(appLanguage)}",
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = cyanAccent.copy(alpha = 0.28f),
                                selectedLabelColor = Color.White,
                                containerColor = Color(0xFF0B1833),
                                labelColor = Color(0xFFCBD5E1)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = Color.White.copy(alpha = 0.16f),
                                selectedBorderColor = cyanAccent
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                Text(
                    text = when (appLanguage) {
                        AppLanguage.EN -> "2. ORGANIZATION OF WORKS (${selectedCountry.flag} ${selectedCountry.localizedLabel(appLanguage)})"
                        AppLanguage.FR -> "2. ORGANISATION DES ŒUVRES (${selectedCountry.flag} ${selectedCountry.localizedLabel(appLanguage)})"
                        else -> "2. ORGANIZACIÓN DE OBRAS (${selectedCountry.flag} ${selectedCountry.localizedLabel(appLanguage)})"
                    },
                    color = cyanAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )

                // Organización por Categorías (Colegios, Universidades, Parroquias/Pastoral)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CategoryCounterPill(
                        emoji = "🌐",
                        title = when (appLanguage) {
                            AppLanguage.EN -> "All"
                            AppLanguage.FR -> "Toutes"
                            else -> "Todas"
                        },
                        count = countryScopedWorks.size,
                        accent = Color.White,
                        isSelected = selectedCategory == null,
                        appLanguage = appLanguage,
                        onClick = { selectedCategory = null }
                    )
                    CategoryCounterPill(
                        emoji = BasilianWorkCategory.COLEGIO.emoji,
                        title = BasilianWorkCategory.COLEGIO.localizedShortLabel(appLanguage),
                        count = colegiosCount,
                        accent = BasilianWorkCategory.COLEGIO.badgeColor,
                        isSelected = selectedCategory == BasilianWorkCategory.COLEGIO,
                        appLanguage = appLanguage,
                        onClick = {
                            selectedCategory = if (selectedCategory == BasilianWorkCategory.COLEGIO) null else BasilianWorkCategory.COLEGIO
                        }
                    )
                    CategoryCounterPill(
                        emoji = BasilianWorkCategory.UNIVERSIDAD.emoji,
                        title = BasilianWorkCategory.UNIVERSIDAD.localizedShortLabel(appLanguage),
                        count = universidadesCount,
                        accent = BasilianWorkCategory.UNIVERSIDAD.badgeColor,
                        isSelected = selectedCategory == BasilianWorkCategory.UNIVERSIDAD,
                        appLanguage = appLanguage,
                        onClick = {
                            selectedCategory = if (selectedCategory == BasilianWorkCategory.UNIVERSIDAD) null else BasilianWorkCategory.UNIVERSIDAD
                        }
                    )
                    CategoryCounterPill(
                        emoji = BasilianWorkCategory.PARROQUIA_MISION.emoji,
                        title = BasilianWorkCategory.PARROQUIA_MISION.localizedShortLabel(appLanguage),
                        count = parroquiasCount,
                        accent = BasilianWorkCategory.PARROQUIA_MISION.badgeColor,
                        isSelected = selectedCategory == BasilianWorkCategory.PARROQUIA_MISION,
                        appLanguage = appLanguage,
                        onClick = {
                            selectedCategory = if (selectedCategory == BasilianWorkCategory.PARROQUIA_MISION) null else BasilianWorkCategory.PARROQUIA_MISION
                        }
                    )
                }

                // Nota discreta sobre lo que se tiene y lo que no se tiene en el país/categoría (ES/EN/FR)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0E1B38).copy(alpha = 0.90f))
                        .border(1.dp, goldAccent.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = when (appLanguage) {
                                AppLanguage.EN -> "Discreet presence note"
                                AppLanguage.FR -> "Note discrète de présence"
                                else -> "Nota discreta de presencia"
                            },
                            tint = goldAccent,
                            modifier = Modifier
                                .size(18.dp)
                                .padding(top = 2.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = selectedCountry.localizedNote(appLanguage),
                                color = Color(0xFFE2E8F0),
                                fontSize = 11.5.sp,
                                lineHeight = 16.sp,
                                fontStyle = FontStyle.Italic
                            )
                            if (selectedCategory != null && filteredWorks.isEmpty()) {
                                val catName = selectedCategory?.localizedShortLabel(appLanguage).orEmpty()
                                val countryName = selectedCountry.localizedLabel(appLanguage)
                                Text(
                                    text = when (appLanguage) {
                                        AppLanguage.EN -> "• In $countryName there are currently no active works in the “$catName” category. Tap “All” or select another country to explore other Basilian works."
                                        AppLanguage.FR -> "• En $countryName, il n'y a actuellement aucune œuvre active dans la catégorie « $catName ». Appuyez sur « Toutes » ou choisissez un autre pays pour explorer les autres œuvres."
                                        else -> "• En $countryName actualmente no se cuenta con obras en la categoría «$catName». Toca «Todas» o selecciona otro país para explorar las demás obras."
                                    },
                                    color = goldAccent,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // ====================================================================
        // 4. MUNDO 3D HOLOGRÁFICO FUTURISTA (CONTROL MANUAL SUAVE CON EL DEDO
        //    + AL OPRIMIR UNA OBRA ABRE IMAGEN, DIRECCIÓN Y GOOGLE MAPS PANORÁMICO)
        // ====================================================================
        item {
            Futuristic3DManualWorldSection(
                works = filteredWorks.ifEmpty { countryScopedWorks },
                selectedWork = selectedWork,
                selectedCountry = selectedCountry,
                appLanguage = appLanguage,
                onTapWorkPin = { work ->
                    selectedWork = work
                    panoramicModalWork = work
                },
                onSelectWorkFocusOnly = { work ->
                    selectedWork = work
                },
                onOpenPanoramicModal = { work ->
                    selectedWork = work
                    panoramicModalWork = work
                },
                onOpenGoogleMapsApp = openGoogleMapsNavigation,
                onOpenStreetViewApp = openStreetView360,
                onOpenWebsite = openUrl
            )
        }

        // ====================================================================
        // 5. BUSCADOR Y DIRECTORIO DETALLADO DE OBRAS CON ACCESO A GOOGLE MAPS
        // ====================================================================
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                HorizontalDivider(
                    color = cyanAccent.copy(alpha = 0.35f),
                    thickness = 1.dp
                )
                Text(
                    text = when (appLanguage) {
                        AppLanguage.EN -> "Directory of Works, Addresses & Panoramic View (${filteredWorks.size})"
                        AppLanguage.FR -> "Répertoire des Œuvres, Adresses et Vue Panoramique (${filteredWorks.size})"
                        else -> "Directorio de Obras, Direcciones y Vista Panorámica (${filteredWorks.size})"
                    },
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = when (appLanguage) {
                                AppLanguage.EN -> "Search school, university, parish, address, or city..."
                                AppLanguage.FR -> "Rechercher école, université, paroisse, adresse ou ville..."
                                else -> "Buscar colegio, universidad, parroquia, dirección o ciudad..."
                            },
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = when (appLanguage) {
                                AppLanguage.EN -> "Search"
                                AppLanguage.FR -> "Rechercher"
                                else -> "Buscar"
                            },
                            tint = cyanAccent
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = when (appLanguage) {
                                        AppLanguage.EN -> "Clear"
                                        AppLanguage.FR -> "Effacer"
                                        else -> "Limpiar"
                                    },
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = cyanAccent,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.18f),
                        focusedContainerColor = Color(0xFF091326),
                        unfocusedContainerColor = Color(0xFF091326),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_works_input")
                )
            }
        }

        if (filteredWorks.isEmpty()) {
            item {
                val countryLabel = selectedCountry.localizedLabel(appLanguage)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF0B152B))
                        .border(1.dp, goldAccent.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = when (appLanguage) {
                                AppLanguage.EN -> "${selectedCountry.flag} Works Presence in $countryLabel"
                                AppLanguage.FR -> "${selectedCountry.flag} Présence des Œuvres en $countryLabel"
                                else -> "${selectedCountry.flag} Discreción de Obras en $countryLabel"
                            },
                            color = goldAccent,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = selectedCountry.localizedNote(appLanguage),
                            color = Color(0xFFCBD5E1),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        )
                        FilledTonalButton(
                            onClick = {
                                selectedCategory = null
                                searchQuery = ""
                            },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = cyanAccent.copy(alpha = 0.2f),
                                contentColor = cyanAccent
                            )
                        ) {
                            Text(
                                when (appLanguage) {
                                    AppLanguage.EN -> "View all active works in $countryLabel"
                                    AppLanguage.FR -> "Voir toutes les œuvres actives en $countryLabel"
                                    else -> "Ver todas las obras activas en $countryLabel"
                                }
                            )
                        }
                    }
                }
            }
        } else {
            items(filteredWorks, key = { it.id }) { work ->
                BasilianWorkDetailCard(
                    work = work,
                    appLanguage = appLanguage,
                    isSelected = selectedWork?.id == work.id,
                    onOpenPanoramicAndMap = {
                        selectedWork = work
                        panoramicModalWork = work
                    },
                    onOpenGoogleMaps = { openGoogleMapsNavigation(work) },
                    onOpenUrl = openUrl
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun PillarMeaningCard(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    latin: String,
    description: String,
    accent: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF061124).copy(alpha = 0.9f))
            .border(1.dp, accent.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(text = emoji, fontSize = 15.sp)
            Column {
                Text(
                    text = title,
                    color = accent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = latin,
                    color = Color(0xFF94A3B8),
                    fontSize = 9.5.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
        Text(
            text = description,
            color = Color(0xFFCBD5E1),
            fontSize = 10.5.sp,
            lineHeight = 14.sp
        )
    }
}

@Composable
private fun CategoryCounterPill(
    emoji: String,
    title: String,
    count: Int,
    accent: Color,
    isSelected: Boolean,
    appLanguage: AppLanguage = AppLanguage.ES,
    onClick: () -> Unit
) {
    val hasItems = count > 0
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) accent.copy(alpha = 0.24f) else Color(0xFF0B152A),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) accent else Color.White.copy(alpha = if (hasItems) 0.16f else 0.07f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(text = emoji, fontSize = 14.sp)
            Column {
                Text(
                    text = title,
                    color = if (hasItems) Color.White else Color(0xFF64748B),
                    fontSize = 11.5.sp,
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold
                )
                Text(
                    text = if (count > 0) {
                        when (appLanguage) {
                            AppLanguage.EN -> "$count active"
                            AppLanguage.FR -> "$count actives"
                            else -> "$count activas"
                        }
                    } else {
                        when (appLanguage) {
                            AppLanguage.EN -> "Not present here"
                            AppLanguage.FR -> "Non présent ici"
                            else -> "No disponible aquí"
                        }
                    },
                    color = if (count > 0) accent else Color(0xFF64748B),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ============================================================================
// MUNDO 3D HOLOGRÁFICO FUTURISTA (GIRO MANUAL CON EL DEDO, SIN AUTO-ROTACIÓN)
// ============================================================================

private data class ProjectedWorkPin3D(
    val work: BasilianGlobalWork,
    val screenX: Float,
    val screenY: Float,
    val beaconTopX: Float,
    val beaconTopY: Float,
    val depthZ: Float
)

private enum class PlanetaryBackdropTheme(
    val id: String,
    val label: String,
    val labelEn: String,
    val labelFr: String,
    val emoji: String,
    val bgColors: List<Color>,
    val primaryGlow: Color,
    val secondaryGlow: Color,
    val rimColor: Color
) {
    AURORA_SANTUARIO(
        id = "aurora",
        label = "Aurora Dorada & Cúpula Celeste",
        labelEn = "Golden Aurora & Celestial Dome",
        labelFr = "Aurore Dorée & Dôme Céleste",
        emoji = "✨",
        bgColors = listOf(
            Color(0xFF1A2956),
            Color(0xFF0E3846),
            Color(0xFF17133B),
            Color(0xFF050B1C)
        ),
        primaryGlow = Color(0xFFFBBF24),
        secondaryGlow = Color(0xFF34D399),
        rimColor = Color(0xFF38BDF8)
    ),
    VITRAL_IMPERIAL(
        id = "vitral",
        label = "Cúpula Imperial de Cesarea",
        labelEn = "Imperial Dome of Caesarea",
        labelFr = "Dôme Impérial de Césarée",
        emoji = "🏛️",
        bgColors = listOf(
            Color(0xFF3B1D38),
            Color(0xFF28173D),
            Color(0xFF141E3C),
            Color(0xFF070B19)
        ),
        primaryGlow = Color(0xFFF59E0B),
        secondaryGlow = Color(0xFFF472B6),
        rimColor = Color(0xFFFDE047)
    ),
    SOL_ESMERALDA(
        id = "amanecer",
        label = "Amanecer Esmeralda & Sol",
        labelEn = "Emerald Dawn & Sun",
        labelFr = "Aube Émeraude & Soleil",
        emoji = "🌅",
        bgColors = listOf(
            Color(0xFF0F3D4C),
            Color(0xFF0B2E47),
            Color(0xFF081F38),
            Color(0xFF040D1E)
        ),
        primaryGlow = Color(0xFF10B981),
        secondaryGlow = Color(0xFFFBBF24),
        rimColor = Color(0xFF00F0FF)
    ),
    NEBULOSA_CRISTAL(
        id = "nebulosa",
        label = "Galaxia Diamante 5D",
        labelEn = "5D Diamond Galaxy",
        labelFr = "Galaxie Diamant 5D",
        emoji = "🌌",
        bgColors = listOf(
            Color(0xFF23154F),
            Color(0xFF0F2557),
            Color(0xFF081433),
            Color(0xFF030714)
        ),
        primaryGlow = Color(0xFF00F0FF),
        secondaryGlow = Color(0xFFE879F9),
        rimColor = Color(0xFFA78BFA)
    );

    fun localizedLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> labelEn
        AppLanguage.FR -> labelFr
        else -> label
    }
}

@Composable
private fun Futuristic3DManualWorldSection(
    works: List<BasilianGlobalWork>,
    selectedWork: BasilianGlobalWork?,
    selectedCountry: BasilianCountryFilter,
    appLanguage: AppLanguage = AppLanguage.ES,
    onTapWorkPin: (BasilianGlobalWork) -> Unit,
    onSelectWorkFocusOnly: (BasilianGlobalWork) -> Unit,
    onOpenPanoramicModal: (BasilianGlobalWork) -> Unit,
    onOpenGoogleMapsApp: (BasilianGlobalWork) -> Unit,
    onOpenStreetViewApp: (BasilianGlobalWork) -> Unit,
    onOpenWebsite: (String) -> Unit
) {
    val textMeasurer = rememberTextMeasurer()
    val infiniteTransition = rememberInfiniteTransition(label = "hologram_hud_effects")
    var selectedBackdrop by remember { mutableStateOf(PlanetaryBackdropTheme.AURORA_SANTUARIO) }

    // Efecto sutil de pulso para los anillos de energía, auroras y marcadores (el planeta NO gira solo)
    val hudPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hud_pulse"
    )

    val orbitalRingPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbital_ring_phase"
    )

    // Panorama inicial más amplio para contemplar todas las obras basilianas en el Planeta 5D
    var targetLonDeg by remember { mutableFloatStateOf(-62f) }
    var targetLatDeg by remember { mutableFloatStateOf(22f) }
    var globeZoom by remember { mutableFloatStateOf(0.95f) }

    // Cuando cambia el país, orienta el Planeta 5D con panorama amplio
    LaunchedEffect(selectedCountry) {
        if (selectedCountry == BasilianCountryFilter.ALL) {
            targetLonDeg = -62f
            targetLatDeg = 22f
            globeZoom = 0.95f
        } else {
            targetLonDeg = selectedCountry.targetLonDeg
            targetLatDeg = selectedCountry.targetLatDeg.coerceIn(-45f, 55f)
            globeZoom = 1.06f
        }
    }

    LaunchedEffect(selectedWork?.id) {
        selectedWork?.let { work ->
            targetLonDeg = work.longitudeDeg
            targetLatDeg = work.latitudeDeg.coerceIn(-45f, 55f)
        }
    }

    // Interpolación suave y controlada para que el giro con el dedo sea fluido y más despacio
    val smoothCenterLonDeg by animateFloatAsState(
        targetValue = targetLonDeg,
        animationSpec = spring(
            dampingRatio = 0.88f,
            stiffness = Spring.StiffnessLow
        ),
        label = "smooth_lon"
    )
    val smoothTiltLatDeg by animateFloatAsState(
        targetValue = targetLatDeg,
        animationSpec = spring(
            dampingRatio = 0.88f,
            stiffness = Spring.StiffnessLow
        ),
        label = "smooth_lat"
    )
    val smoothZoom by animateFloatAsState(
        targetValue = globeZoom,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "smooth_zoom"
    )

    var projectedPins by remember { mutableStateOf<List<ProjectedWorkPin3D>>(emptyList()) }

    val cyanAccent = Color(0xFF00F0FF)
    val goldAccent = Color(0xFFFBBF24)
    val emeraldAccent = Color(0xFF34D399)
    val magentaAccent = Color(0xFFE879F9)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .futuristic3DCard(
                accentColor = selectedBackdrop.primaryGlow,
                secondaryColor = selectedBackdrop.secondaryGlow,
                cornerRadius = 28.dp,
                tiltDegrees = 1.2f
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Cabecera del Núcleo Planetario 5D Realista y Futurista
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = selectedBackdrop.primaryGlow.copy(alpha = 0.22f),
                        border = BorderStroke(1.dp, selectedBackdrop.primaryGlow),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = when (appLanguage) {
                                AppLanguage.EN -> "5D PLANETARY SANCTUARY • C.S.B. ASTROLABE"
                                AppLanguage.FR -> "SANCTUAIRE PLANÉTAIRE 5D • ASTROLABE C.S.B."
                                else -> "SANTUARIO PLANETARIO 5D • ASTROLABIO C.S.B."
                            },
                            color = selectedBackdrop.primaryGlow,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.9.sp,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "• LIVE PANORAMA"
                            AppLanguage.FR -> "• PANORAMA EN DIRECT"
                            else -> "• PANORAMA VIVO"
                        },
                        color = selectedBackdrop.secondaryGlow,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (appLanguage) {
                        AppLanguage.EN -> "Rotate the globe with your finger over the Celestial Dome and tap any work to open its Panoramic Image and Google Maps"
                        AppLanguage.FR -> "Faites tourner le globe avec votre doigt sur le Dôme Céleste et touchez n'importe quelle œuvre pour ouvrir son Image Panoramique et Google Maps"
                        else -> "Gira el mundo con tu dedo sobre el Domo Celeste y toca cualquier obra para abrir su Imagen Panorámica y Google Maps"
                    },
                    color = Color(0xFFE2E8F0),
                    fontSize = 11.5.sp,
                    lineHeight = 16.sp
                )
            }
        }

        // Selector de Escenarios / Fondos Creativos 5D para el Mapa
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlanetaryBackdropTheme.entries.forEach { theme ->
                val isSelected = selectedBackdrop == theme
                Surface(
                    onClick = { selectedBackdrop = theme },
                    shape = RoundedCornerShape(50),
                    color = if (isSelected) theme.primaryGlow.copy(alpha = 0.24f) else Color(0xFF0A1428),
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) theme.primaryGlow else Color.White.copy(alpha = 0.16f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(text = theme.emoji, fontSize = 12.sp)
                        Text(
                            text = theme.localizedLabel(appLanguage),
                            color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                            fontSize = 10.5.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Barra de Controles de Panorama Amplio 5D, Zoom y Orientación Manual
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                onClick = {
                    // Vista Panorámica Mundial Amplia donde se aprecian América y Europa a la vez
                    globeZoom = 0.92f
                    targetLonDeg = -55f
                    targetLatDeg = 24f
                },
                color = Color(0xFF071936),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, goldAccent.copy(alpha = 0.7f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = goldAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "5D Wide Panorama"
                            AppLanguage.FR -> "Panorama Large 5D"
                            else -> "Panorama Amplio 5D"
                        },
                        color = goldAccent,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(
                    onClick = { globeZoom = (globeZoom - 0.16f).coerceAtLeast(0.78f) },
                    modifier = Modifier.size(32.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.08f),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = when (appLanguage) {
                            AppLanguage.EN -> "Zoom out"
                            AppLanguage.FR -> "Dézoomer"
                            else -> "Alejar"
                        },
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = "${(smoothZoom * 100).toInt()}%",
                    color = selectedBackdrop.primaryGlow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                FilledTonalIconButton(
                    onClick = { globeZoom = (globeZoom + 0.16f).coerceAtMost(1.85f) },
                    modifier = Modifier.size(32.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.08f),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = when (appLanguage) {
                            AppLanguage.EN -> "Zoom in"
                            AppLanguage.FR -> "Zoomer"
                            else -> "Acercar"
                        },
                        modifier = Modifier.size(16.dp)
                    )
                }

                FilledTonalIconButton(
                    onClick = {
                        globeZoom = 0.95f
                        targetLonDeg = selectedWork?.longitudeDeg ?: -62f
                        targetLatDeg = (selectedWork?.latitudeDeg ?: 22f).coerceIn(-45f, 55f)
                    },
                    modifier = Modifier.size(32.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = goldAccent.copy(alpha = 0.18f),
                        contentColor = goldAccent
                    )
                ) {
                    Icon(
                        Icons.Default.CenterFocusStrong,
                        contentDescription = when (appLanguage) {
                            AppLanguage.EN -> "Center"
                            AppLanguage.FR -> "Centrer"
                            else -> "Centrar"
                        },
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // ====================================================================
        // LIENZO HOLOGRÁFICO 5D DEL PLANETA TIERRA CON DOMO AURORA, VITRAL CELESTE
        // Y PEDESTAL DE LUZ BASILIANO
        // ====================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(460.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.verticalGradient(
                        colors = selectedBackdrop.bgColors
                    )
                )
                .border(
                    width = 2.2.dp,
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            selectedBackdrop.primaryGlow,
                            selectedBackdrop.secondaryGlow,
                            selectedBackdrop.rimColor,
                            goldAccent,
                            selectedBackdrop.primaryGlow
                        )
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val slowFactor = 0.13f / globeZoom.coerceAtLeast(0.85f)
                        var nextLon = targetLonDeg - dragAmount.x * slowFactor
                        if (nextLon > 180f) nextLon -= 360f
                        if (nextLon < -180f) nextLon += 360f
                        targetLonDeg = nextLon
                        targetLatDeg = (targetLatDeg + dragAmount.y * (slowFactor * 0.75f)).coerceIn(-55f, 65f)
                    }
                }
                .pointerInput(works, selectedWork, smoothCenterLonDeg, smoothTiltLatDeg, smoothZoom) {
                    detectTapGestures { tapOffset ->
                        val hit = projectedPins
                            .filter { it.depthZ > -0.08f }
                            .minByOrNull { pin ->
                                val distToBase = hypot(tapOffset.x - pin.screenX, tapOffset.y - pin.screenY)
                                val distToTop = hypot(tapOffset.x - pin.beaconTopX, tapOffset.y - pin.beaconTopY)
                                minOf(distToBase, distToTop)
                            }
                        if (hit != null) {
                            val minDistance = minOf(
                                hypot(tapOffset.x - hit.screenX, tapOffset.y - hit.screenY),
                                hypot(tapOffset.x - hit.beaconTopX, tapOffset.y - hit.beaconTopY)
                            )
                            if (minDistance <= 88f) {
                                onTapWorkPin(hit.work)
                            }
                        }
                    }
                }
                .testTag("interactive_3d_globe_canvas")
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val cx = w * 0.5f
                val cy = h * 0.50f
                val baseRadius = minOf(w, h) * 0.40f
                val globeRadius = baseRadius * smoothZoom

                // 1A. Cortinas de Aurora Boreal Celeste y Esmeralda en el Cielo Superior
                for (band in 0..3) {
                    val auroraPath = Path()
                    val baseY = h * (0.10f + band * 0.07f)
                    auroraPath.moveTo(0f, baseY)
                    for (step in 0..24) {
                        val t = step / 24f
                        val x = w * t
                        val wave = sin(t * PI.toFloat() * 3f + (orbitalRingPhase * PI.toFloat() / 180f) + band) * (18f + band * 5f)
                        auroraPath.lineTo(x, baseY + wave)
                    }
                    auroraPath.lineTo(w, 0f)
                    auroraPath.lineTo(0f, 0f)
                    auroraPath.close()
                    drawPath(
                        path = auroraPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                selectedBackdrop.secondaryGlow.copy(alpha = 0.16f - band * 0.03f),
                                selectedBackdrop.primaryGlow.copy(alpha = 0.11f - band * 0.02f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = baseY + 60f
                        )
                    )
                }

                // 1B. Corona de Rayos Solares Volumétricos («Rayos de Sabiduría de Cesarea») detrás del Planeta
                val rayCount = 16
                for (rayIdx in 0 until rayCount) {
                    val angleDeg = rayIdx * (360f / rayCount) + orbitalRingPhase * 0.25f
                    val angleRad = angleDeg * (PI.toFloat() / 180f)
                    val rayLen = maxOf(w, h) * 0.65f
                    val endX = cx + cos(angleRad) * rayLen
                    val endY = cy + sin(angleRad) * rayLen
                    drawLine(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                selectedBackdrop.primaryGlow.copy(alpha = if (rayIdx % 2 == 0) 0.22f else 0.10f),
                                selectedBackdrop.secondaryGlow.copy(alpha = 0.05f),
                                Color.Transparent
                            ),
                            start = Offset(cx, cy),
                            end = Offset(endX, endY)
                        ),
                        start = Offset(cx, cy),
                        end = Offset(endX, endY),
                        strokeWidth = if (rayIdx % 2 == 0) 26f else 14f,
                        cap = StrokeCap.Round
                    )
                }

                // 1C. Nebulosas Luminosas Multicolor en las Esquinas
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            selectedBackdrop.primaryGlow.copy(alpha = 0.24f),
                            selectedBackdrop.secondaryGlow.copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.18f, h * 0.18f),
                        radius = w * 0.60f
                    ),
                    radius = w * 0.60f,
                    center = Offset(w * 0.18f, h * 0.18f)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            selectedBackdrop.secondaryGlow.copy(alpha = 0.22f),
                            selectedBackdrop.rimColor.copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.84f, h * 0.80f),
                        radius = w * 0.56f
                    ),
                    radius = w * 0.56f,
                    center = Offset(w * 0.84f, h * 0.80f)
                )

                // 1D. Pedestal de Luz Holográfico en la Base del Santuario Planetario
                val pedestalY = h * 0.91f
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            selectedBackdrop.primaryGlow.copy(alpha = 0.42f * hudPulse.coerceIn(0.8f, 1.15f)),
                            selectedBackdrop.secondaryGlow.copy(alpha = 0.22f),
                            Color.Transparent
                        ),
                        center = Offset(cx, pedestalY),
                        radius = globeRadius * 1.35f
                    ),
                    topLeft = Offset(cx - globeRadius * 1.25f, pedestalY - 22f),
                    size = Size(globeRadius * 2.50f, 44f)
                )
                drawOval(
                    color = selectedBackdrop.primaryGlow.copy(alpha = 0.65f),
                    topLeft = Offset(cx - globeRadius * 0.95f, pedestalY - 12f),
                    size = Size(globeRadius * 1.90f, 24f),
                    style = Stroke(
                        width = 1.6f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), orbitalRingPhase)
                    )
                )
                // Haz ascendente desde el pedestal hacia el polo sur del planeta
                val beamPath = Path().apply {
                    moveTo(cx - globeRadius * 0.85f, pedestalY)
                    lineTo(cx - globeRadius * 0.55f, cy + globeRadius * 0.72f)
                    lineTo(cx + globeRadius * 0.55f, cy + globeRadius * 0.72f)
                    lineTo(cx + globeRadius * 0.85f, pedestalY)
                    close()
                }
                drawPath(
                    path = beamPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            selectedBackdrop.rimColor.copy(alpha = 0.04f),
                            selectedBackdrop.primaryGlow.copy(alpha = 0.18f)
                        ),
                        startY = cy + globeRadius * 0.70f,
                        endY = pedestalY
                    )
                )

                // 1E. Constelaciones Doradas y Estrellas Diamante en el Domo
                val stars = listOf(
                    Offset(0.06f, 0.09f), Offset(0.14f, 0.17f), Offset(0.23f, 0.07f),
                    Offset(0.85f, 0.09f), Offset(0.93f, 0.20f), Offset(0.76f, 0.06f),
                    Offset(0.07f, 0.80f), Offset(0.17f, 0.90f), Offset(0.91f, 0.83f),
                    Offset(0.77f, 0.92f), Offset(0.04f, 0.46f), Offset(0.96f, 0.48f),
                    Offset(0.35f, 0.05f), Offset(0.65f, 0.04f), Offset(0.49f, 0.95f),
                    Offset(0.12f, 0.62f), Offset(0.89f, 0.36f), Offset(0.27f, 0.87f)
                )
                // Líneas sutiles de constelación en las esquinas superiores
                drawLine(
                    color = goldAccent.copy(alpha = 0.28f),
                    start = Offset(w * 0.06f, h * 0.09f),
                    end = Offset(w * 0.14f, h * 0.17f),
                    strokeWidth = 1f
                )
                drawLine(
                    color = goldAccent.copy(alpha = 0.28f),
                    start = Offset(w * 0.14f, h * 0.17f),
                    end = Offset(w * 0.23f, h * 0.07f),
                    strokeWidth = 1f
                )
                drawLine(
                    color = cyanAccent.copy(alpha = 0.28f),
                    start = Offset(w * 0.76f, h * 0.06f),
                    end = Offset(w * 0.85f, h * 0.09f),
                    strokeWidth = 1f
                )
                drawLine(
                    color = cyanAccent.copy(alpha = 0.28f),
                    start = Offset(w * 0.85f, h * 0.09f),
                    end = Offset(w * 0.93f, h * 0.20f),
                    strokeWidth = 1f
                )
                stars.forEachIndexed { idx, s ->
                    val starColor = when (idx % 4) {
                        0 -> selectedBackdrop.primaryGlow.copy(alpha = 0.90f)
                        1 -> selectedBackdrop.secondaryGlow.copy(alpha = 0.85f)
                        2 -> selectedBackdrop.rimColor.copy(alpha = 0.80f)
                        else -> Color.White.copy(alpha = 0.75f)
                    }
                    val sx = s.x * w
                    val sy = s.y * h
                    drawCircle(
                        color = starColor,
                        radius = if (idx % 3 == 0) 3.2f else 2.0f,
                        center = Offset(sx, sy)
                    )
                    if (idx % 4 == 0) {
                        // Destello en cruz tipo estrella brillante
                        drawLine(starColor.copy(alpha = 0.6f), Offset(sx - 6f, sy), Offset(sx + 6f, sy), 1.1f)
                        drawLine(starColor.copy(alpha = 0.6f), Offset(sx, sy - 6f), Offset(sx, sy + 6f), 1.1f)
                    }
                }

                // Arcos Arquitectónicos Dorados en las Esquinas del Santuario
                val cornerLen = 28.dp.toPx()
                val pad = 12.dp.toPx()
                val hudStroke = 2.4.dp.toPx()
                drawLine(selectedBackdrop.primaryGlow.copy(alpha = 0.9f), Offset(pad, pad), Offset(pad + cornerLen, pad), hudStroke)
                drawLine(selectedBackdrop.primaryGlow.copy(alpha = 0.9f), Offset(pad, pad), Offset(pad, pad + cornerLen), hudStroke)
                drawLine(selectedBackdrop.primaryGlow.copy(alpha = 0.9f), Offset(w - pad, pad), Offset(w - pad - cornerLen, pad), hudStroke)
                drawLine(selectedBackdrop.primaryGlow.copy(alpha = 0.9f), Offset(w - pad, pad), Offset(w - pad, pad + cornerLen), hudStroke)
                drawLine(selectedBackdrop.secondaryGlow.copy(alpha = 0.9f), Offset(pad, h - pad), Offset(pad + cornerLen, h - pad), hudStroke)
                drawLine(selectedBackdrop.secondaryGlow.copy(alpha = 0.9f), Offset(pad, h - pad), Offset(pad, h - pad - cornerLen), hudStroke)
                drawLine(selectedBackdrop.secondaryGlow.copy(alpha = 0.9f), Offset(w - pad, h - pad), Offset(w - pad - cornerLen, h - pad), hudStroke)
                drawLine(selectedBackdrop.secondaryGlow.copy(alpha = 0.9f), Offset(w - pad, h - pad), Offset(w - pad, h - pad - cornerLen), hudStroke)

                // 2. Astrolabio Sagrado & Anillos Giroscópicos 5D alrededor del Mundo
                val astrolabeRadius = globeRadius * 1.24f
                drawCircle(
                    color = goldAccent.copy(alpha = 0.36f),
                    radius = astrolabeRadius,
                    center = Offset(cx, cy),
                    style = Stroke(width = 1.6f)
                )
                // Marcas graduadas del Astrolabio cada 15 grados
                for (deg in 0 until 360 step 15) {
                    val rad = (deg + orbitalRingPhase * 0.35f) * (PI.toFloat() / 180f)
                    val innerTick = if (deg % 45 == 0) astrolabeRadius - 9f else astrolabeRadius - 4.5f
                    val outerTick = astrolabeRadius + (if (deg % 45 == 0) 4f else 0f)
                    drawLine(
                        color = if (deg % 45 == 0) goldAccent.copy(alpha = 0.85f) else selectedBackdrop.rimColor.copy(alpha = 0.40f),
                        start = Offset(cx + cos(rad) * innerTick, cy + sin(rad) * innerTick),
                        end = Offset(cx + cos(rad) * outerTick, cy + sin(rad) * outerTick),
                        strokeWidth = if (deg % 45 == 0) 2.2f else 1.1f
                    )
                }

                drawOval(
                    color = selectedBackdrop.rimColor.copy(alpha = 0.34f),
                    topLeft = Offset(cx - globeRadius * 1.36f, cy - globeRadius * 0.45f),
                    size = Size(globeRadius * 2.72f, globeRadius * 0.90f),
                    style = Stroke(
                        width = 1.8f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 8f), orbitalRingPhase)
                    )
                )
                drawOval(
                    color = selectedBackdrop.secondaryGlow.copy(alpha = 0.25f),
                    topLeft = Offset(cx - globeRadius * 0.48f, cy - globeRadius * 1.30f),
                    size = Size(globeRadius * 0.96f, globeRadius * 2.60f),
                    style = Stroke(
                        width = 1.4f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), -orbitalRingPhase)
                    )
                )

                // Nodos satelitales 5D orbitando el anillo exterior
                for (satIdx in 0 until 4) {
                    val satRad = (orbitalRingPhase * 0.5f + satIdx * 90f) * (PI.toFloat() / 180f)
                    val sx = cx + cos(satRad) * globeRadius * 1.36f
                    val sy = cy + sin(satRad) * globeRadius * 0.45f
                    drawCircle(
                        color = if (satIdx % 2 == 0) selectedBackdrop.primaryGlow else selectedBackdrop.secondaryGlow,
                        radius = 4.2f,
                        center = Offset(sx, sy)
                    )
                }

                // 3. Corona Atmosférica Multinivel 3D Fotorealista (Halo Azul-Turquesa y Exosfera Solar)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF38BDF8).copy(alpha = 0.62f),
                            selectedBackdrop.rimColor.copy(alpha = 0.38f),
                            selectedBackdrop.primaryGlow.copy(alpha = 0.18f),
                            Color.Transparent
                        ),
                        center = Offset(cx, cy),
                        radius = globeRadius * 1.42f
                    ),
                    radius = globeRadius * 1.42f,
                    center = Offset(cx, cy)
                )

                // 4A. Esfera Oceánica Fotorealista 3D con batimetría, reflejo solar especular y curvatura
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFBAE6FD),                    // Reflejo especular solar directo
                            Color(0xFF0284C7),                    // Plataforma continental iluminada
                            Color(0xFF0369A1),                    // Océano Atlántico/Pacífico medio
                            Color(0xFF1E3A8A),                    // Cuenca oceánica profunda
                            Color(0xFF0B193C),                    // Penumbra oceánica
                            Color(0xFF04091A)                     // Terminador nocturno
                        ),
                        center = Offset(cx - globeRadius * 0.34f, cy - globeRadius * 0.32f),
                        radius = globeRadius * 1.52f
                    ),
                    radius = globeRadius,
                    center = Offset(cx, cy)
                )

                //Proyección esférica ortográfica 3D
                val rotLonRad = -smoothCenterLonDeg * (PI.toFloat() / 180f)
                val tiltLatRad = smoothTiltLatDeg * (PI.toFloat() / 180f)

                fun project3D(latDeg: Float, lonDeg: Float, radiusScale: Float = 1f): Triple<Float, Float, Float> {
                    val latRad = latDeg * (PI.toFloat() / 180f)
                    val lonRad = lonDeg * (PI.toFloat() / 180f) + rotLonRad

                    val x0 = cos(latRad) * sin(lonRad)
                    val y0 = sin(latRad)
                    val z0 = cos(latRad) * cos(lonRad)

                    val y1 = y0 * cos(tiltLatRad) - z0 * sin(tiltLatRad)
                    val z1 = y0 * sin(tiltLatRad) + z0 * cos(tiltLatRad)

                    val r = globeRadius * radiusScale
                    val screenX = cx + x0 * r
                    val screenY = cy - y1 * r
                    return Triple(screenX, screenY, z1)
                }

                // 5. Malla Geodésica Sutil de Precisión Cartográfica (Meridianos y Paralelos)
                for (lon in -180..160 step 15) {
                    val path = Path()
                    var started = false
                    for (lat in -80..80 step 5) {
                        val (px, py, pz) = project3D(lat.toFloat(), lon.toFloat())
                        if (pz > 0f) {
                            if (!started) {
                                path.moveTo(px, py)
                                started = true
                            } else {
                                path.lineTo(px, py)
                            }
                        } else {
                            started = false
                        }
                    }
                    drawPath(
                        path = path,
                        color = Color(0xFF7DD3FC).copy(alpha = 0.13f),
                        style = Stroke(width = 0.85f)
                    )
                }

                for (lat in -60..60 step 15) {
                    val path = Path()
                    var started = false
                    for (lon in -180..180 step 5) {
                        val (px, py, pz) = project3D(lat.toFloat(), lon.toFloat())
                        if (pz > 0f) {
                            if (!started) {
                                path.moveTo(px, py)
                                started = true
                            } else {
                                path.lineTo(px, py)
                            }
                        } else {
                            started = false
                        }
                    }
                    drawPath(
                        path = path,
                        color = if (lat == 0) goldAccent.copy(alpha = 0.38f) else Color(0xFF7DD3FC).copy(alpha = 0.13f),
                        style = Stroke(width = if (lat == 0) 1.6f else 0.85f)
                    )
                }

                // 6. Continentes Fotorealistas 3D con Plataformas Costeras, Bosques, Cordilleras y Polos
                val continentsLatLon: List<Pair<String, List<Pair<Float, Float>>>> = listOf(
                    "Norteamérica y Centroamérica" to listOf(
                        70f to -164f, 71f to -156f, 69f to -136f, 68f to -114f,
                        64f to -95f, 61f to -76f, 58f to -64f, 52f to -56f,
                        47f to -53f, 45f to -61f, 44f to -67f, 41f to -70f,
                        39f to -74f, 35f to -75f, 31f to -81f, 25f to -80f,
                        28f to -83f, 30f to -88f, 29f to -95f, 26f to -97f,
                        22f to -98f, 19f to -96f, 18f to -92f, 21f to -87f,
                        18f to -88f, 15f to -83f, 11f to -84f, 9f to -79f,
                        8f to -81f, 10f to -85f, 14f to -92f, 16f to -98f,
                        20f to -105f, 24f to -107f, 23f to -110f, 28f to -114f,
                        32f to -117f, 37f to -122f, 43f to -124f, 48f to -125f,
                        54f to -132f, 59f to -140f, 61f to -150f, 60f to -162f,
                        65f to -168f
                    ),
                    "Groenlandia" to listOf(
                        60f to -44f, 65f to -40f, 70f to -24f, 77f to -19f,
                        82f to -32f, 81f to -54f, 76f to -68f, 68f to -53f, 62f to -50f
                    ),
                    "Caribe" to listOf(
                        23f to -84f, 22.5f to -79f, 20f to -74f, 19.5f to -71f,
                        18.2f to -66f, 18.0f to -72f, 19.8f to -77f, 21.5f to -83f
                    ),
                    "Sudamérica" to listOf(
                        11.5f to -75f, 12.2f to -71f, 10.5f to -67f, 10.2f to -62f,
                        7.5f to -58f, 4.5f to -51f, 0.5f to -49f, -2.5f to -44f,
                        -5.5f to -35f, -9.5f to -35.5f, -13f to -38.5f, -18f to -39.5f,
                        -23f to -43f, -27f to -48.5f, -33f to -52.5f, -38f to -57.5f,
                        -42f to -64f, -47f to -66f, -52f to -68.5f, -55f to -67f,
                        -53f to -73f, -46f to -75f, -39f to -73.5f, -32f to -71.5f,
                        -23f to -70.5f, -18f to -71f, -14f to -76f, -8f to -79.5f,
                        -4.5f to -81.3f, 0.5f to -80f, 4.5f to -77.3f, 8.2f to -77.2f
                    ),
                    "Europa" to listOf(
                        36f to -9f, 43f to -9.2f, 43.5f to -1.8f, 48f to -4.5f,
                        49.5f to 1.5f, 53.5f to 7.5f, 57.5f to 8.5f, 62f to 5.5f,
                        68f to 15f, 71f to 26f, 67f to 36f, 60f to 30f,
                        55f to 21f, 47f to 32f, 42f to 28f, 38f to 24f,
                        40f to 19f, 40.5f to 17f, 38f to 15.5f, 42f to 12f,
                        44.2f to 8.5f, 43.2f to 3.5f, 39.5f to -0.3f, 36.2f to -5.4f
                    ),
                    "Islas Británicas" to listOf(
                        50.2f to -5.2f, 51.4f to 1.2f, 55.0f to -1.5f, 58.5f to -3.2f,
                        57.5f to -6.2f, 54.5f to -8.2f, 51.8f to -9.8f
                    ),
                    "África" to listOf(
                        35.8f to -5.8f, 37.0f to 10.2f, 32.5f to 24.0f, 31.5f to 32.5f,
                        22.0f to 37.0f, 12.0f to 51.0f, 2.0f to 45.5f, -4.0f to 39.8f,
                        -14.5f to 40.6f, -25.5f to 33.0f, -34.2f to 25.0f, -34.8f to 18.5f,
                        -28.5f to 16.5f, -18.0f to 12.0f, -9.0f to 13.2f, -1.0f to 9.2f,
                        4.5f to 8.8f, 6.2f to 1.5f, 4.5f to -7.5f, 10.5f to -14.5f,
                        14.8f to -17.5f, 21.0f to -17.0f, 28.0f to -13.0f
                    )
                )

                continentsLatLon.forEach { (continentName, coords) ->
                    val shallowWaterPath = Path()
                    val surfacePath = Path()
                    val highlandPath = Path()
                    var visibleCount = 0
                    coords.forEach { (lat, lon) ->
                        val (sx, sy, _) = project3D(lat, lon, 1.004f)
                        val (px, py, pz) = project3D(lat, lon, 1.0f)
                        val (rx, ry, _) = project3D(lat + 1.1f, lon - 0.8f, 1.011f)
                        if (pz > -0.10f) {
                            val dx = px - cx
                            val dy = py - cy
                            val dist = hypot(dx, dy)
                            val clampedX = if (dist > globeRadius * 0.990f) cx + (dx / dist) * globeRadius * 0.990f else px
                            val clampedY = if (dist > globeRadius * 0.990f) cy + (dy / dist) * globeRadius * 0.990f else py
                            val clampedSX = if (dist > globeRadius * 0.992f) clampedX else sx
                            val clampedSY = if (dist > globeRadius * 0.992f) clampedY else sy
                            val clampedRX = if (dist > globeRadius * 0.985f) clampedX else rx
                            val clampedRY = if (dist > globeRadius * 0.985f) clampedY else ry
                            if (visibleCount == 0) {
                                shallowWaterPath.moveTo(clampedSX, clampedSY)
                                surfacePath.moveTo(clampedX, clampedY)
                                highlandPath.moveTo(clampedRX, clampedRY)
                            } else {
                                shallowWaterPath.lineTo(clampedSX, clampedSY)
                                surfacePath.lineTo(clampedX, clampedY)
                                highlandPath.lineTo(clampedRX, clampedRY)
                            }
                            visibleCount++
                        }
                    }
                    if (visibleCount >= 3) {
                        shallowWaterPath.close()
                        surfacePath.close()
                        highlandPath.close()

                        val isIceLand = continentName == "Groenlandia"

                        // 6A. Plataforma continental de aguas poco profundas (turquesa luminoso alrededor de las costas)
                        drawPath(
                            path = shallowWaterPath,
                            color = Color(0xFF38BDF8).copy(alpha = 0.28f),
                            style = Stroke(width = 5.5f)
                        )

                        // 6B. Biomas continentales realistas (bosques esmeralda, llanuras verdes y cordilleras ocres/nevadas)
                        drawPath(
                            path = surfacePath,
                            brush = if (isIceLand) {
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFE0F2FE).copy(alpha = 0.88f),
                                        Color(0xFFBAE6FD).copy(alpha = 0.76f)
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF15803D).copy(alpha = 0.86f), // Selva y bosque profundo
                                        Color(0xFF16A34A).copy(alpha = 0.82f), // Llanura templada
                                        Color(0xFF65A30D).copy(alpha = 0.78f), // Meseta continental
                                        Color(0xFFD97706).copy(alpha = 0.65f)  // Relieve montañoso / árido
                                    ),
                                    start = Offset(cx - globeRadius * 0.6f, cy - globeRadius * 0.6f),
                                    end = Offset(cx + globeRadius * 0.6f, cy + globeRadius * 0.6f)
                                )
                            }
                        )

                        // 6C. Relieve topográfico 3D elevado (Andes, Rocosas, Alpes)
                        if (!isIceLand) {
                            drawPath(
                                path = highlandPath,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFBBF24).copy(alpha = 0.25f),
                                        Color(0xFF34D399).copy(alpha = 0.20f)
                                    )
                                )
                            )
                        }

                        // 6D. Línea costera nítida
                        drawPath(
                            path = surfacePath,
                            color = if (isIceLand) Color.White.copy(alpha = 0.90f) else Color(0xFF6EE7B7).copy(alpha = 0.88f),
                            style = Stroke(width = 1.6f)
                        )
                    }
                }

                // 6E. Nubes Atmosféricas Translúcidas 3D flotando sobre el planeta
                val cloudBands = listOf(
                    Triple(26f, -72f, 24f),
                    Triple(8f, -78f, 18f),
                    Triple(46f, -38f, 28f),
                    Triple(-14f, -52f, 22f),
                    Triple(48f, 6f, 20f),
                    Triple(36f, -94f, 21f)
                )
                cloudBands.forEachIndexed { idx, (cLat, cLon, spanDeg) ->
                    val driftLon = cLon + sin((orbitalRingPhase + idx * 45f) * (PI.toFloat() / 180f)) * 4f
                    val (cxCloud, cyCloud, czCloud) = project3D(cLat, driftLon, 1.022f)
                    if (czCloud > 0.12f) {
                        val cloudR = (spanDeg / 90f) * globeRadius * czCloud
                        drawOval(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.30f * czCloud),
                                    Color(0xFFE0F2FE).copy(alpha = 0.14f * czCloud),
                                    Color.Transparent
                                ),
                                center = Offset(cxCloud, cyCloud),
                                radius = cloudR * 1.4f
                            ),
                            topLeft = Offset(cxCloud - cloudR * 1.35f, cyCloud - cloudR * 0.52f),
                            size = Size(cloudR * 2.7f, cloudR * 1.04f)
                        )
                    }
                }

                // 6F. Sombra Esférica 3D (Terminador Día/Noche) y Reflejo Atmosférico Fresnel en el borde
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xFF020617).copy(alpha = 0.38f),
                            Color(0xFF020617).copy(alpha = 0.72f)
                        ),
                        center = Offset(cx - globeRadius * 0.28f, cy - globeRadius * 0.28f),
                        radius = globeRadius * 1.32f
                    ),
                    radius = globeRadius,
                    center = Offset(cx, cy)
                )

                // 7. Luces Doradas de Ciudades y Metrópolis Basilianas
                val cityLightClusters = listOf(
                    43.7f to -79.4f, 45.5f to -73.6f, 42.3f to -83.0f, 53.5f to -113.5f,
                    40.7f to -74.0f, 43.1f to -77.5f, 42.5f to -83.5f, 29.7f to -95.4f,
                    19.4f to -99.1f, 18.5f to -97.4f, 6.2f to -75.6f, 4.6f to -74.1f,
                    3.4f to -76.5f, 48.8f to 2.3f, 45.2f to 4.7f, -23.5f to -46.6f
                )
                cityLightClusters.forEach { (cLat, cLon) ->
                    val (lx, ly, lz) = project3D(cLat, cLon, 1.005f)
                    if (lz > 0.05f) {
                        drawCircle(
                            color = goldAccent.copy(alpha = 0.62f * lz),
                            radius = 5.8f * lz,
                            center = Offset(lx, ly)
                        )
                        drawCircle(
                            color = Color.White.copy(alpha = 0.90f * lz),
                            radius = 2.1f * lz,
                            center = Offset(lx, ly)
                        )
                    }
                }

                // 8. Arcos Luminosos 3D de la Red Basiliana (conectando todas las obras en el espacio)
                val networkHubs = listOf(
                    45.24f to 4.67f,    // Annonay, Francia
                    43.67f to -79.39f,  // Toronto, Canadá
                    42.31f to -83.07f,  // Windsor, Canadá
                    53.52f to -113.52f, // Edmonton, Canadá
                    42.49f to -83.54f,  // Detroit/Novi, EE. UU.
                    43.12f to -77.51f,  // Rochester, EE. UU.
                    29.74f to -95.39f,  // Houston, EE. UU.
                    18.47f to -97.42f,  // Tehuacán, México
                    6.20f to -75.57f,   // Medellín, Colombia
                    4.59f to -74.07f,   // Bogotá, Colombia
                    3.44f to -76.49f    // Cali, Colombia
                )
                for (i in 0 until networkHubs.size - 1) {
                    val (lat1, lon1) = networkHubs[i]
                    val (lat2, lon2) = networkHubs[i + 1]
                    val arcPath = Path()
                    var arcStarted = false
                    for (step in 0..22) {
                        val t = step / 22f
                        val iLat = lat1 + (lat2 - lat1) * t
                        val iLon = lon1 + (lon2 - lon1) * t
                        val elevation = 1f + 0.11f * sin(t * PI.toFloat())
                        val (px, py, pz) = project3D(iLat, iLon, elevation)
                        if (pz > 0.01f) {
                            if (!arcStarted) {
                                arcPath.moveTo(px, py)
                                arcStarted = true
                            } else {
                                arcPath.lineTo(px, py)
                            }
                        } else {
                            arcStarted = false
                        }
                    }
                    drawPath(
                        path = arcPath,
                        brush = Brush.linearGradient(
                            colors = listOf(cyanAccent.copy(alpha = 0.75f), goldAccent.copy(alpha = 0.85f))
                        ),
                        style = Stroke(width = 2.3f, cap = StrokeCap.Round)
                    )
                }

                // 9. Anillo Atmosférico de Horizonte 3D
                drawCircle(
                    brush = Brush.sweepGradient(
                        colors = listOf(Color(0xFF38BDF8), goldAccent, emeraldAccent, Color(0xFF7DD3FC), Color(0xFF38BDF8))
                    ),
                    radius = globeRadius,
                    center = Offset(cx, cy),
                    style = Stroke(width = 2.6f)
                )

                // 10. Torres Holográficas 5D (Beacons) para cada Obra Basiliana
                val newProjected = mutableListOf<ProjectedWorkPin3D>()
                works.forEach { work ->
                    val (baseX, baseY, pz) = project3D(work.latitudeDeg, work.longitudeDeg, 1.0f)
                    val isSelected = work.id == selectedWork?.id
                    val (topX, topY, _) = project3D(
                        work.latitudeDeg,
                        work.longitudeDeg,
                        if (isSelected) 1.17f else 1.10f
                    )
                    newProjected.add(
                        ProjectedWorkPin3D(
                            work = work,
                            screenX = baseX,
                            screenY = baseY,
                            beaconTopX = topX,
                            beaconTopY = topY,
                            depthZ = pz
                        )
                    )
                }
                projectedPins = newProjected

                newProjected
                    .filter { it.depthZ > -0.06f }
                    .sortedBy { if (it.work.id == selectedWork?.id) 2f else it.depthZ }
                    .forEach { pin ->
                        val isSelected = pin.work.id == selectedWork?.id
                        val pinColor = pin.work.category.badgeColor
                        val depthScale = (0.80f + 0.35f * pin.depthZ.coerceIn(0f, 1f))

                        // Onda expansiva 5D en la corteza terrestre
                        drawCircle(
                            color = pinColor.copy(alpha = if (isSelected) 0.50f else 0.28f),
                            radius = (if (isSelected) 16f * hudPulse else 10f) * depthScale,
                            center = Offset(pin.screenX, pin.screenY)
                        )
                        drawCircle(
                            color = pinColor.copy(alpha = 0.7f),
                            radius = (if (isSelected) 11f else 6.5f) * depthScale,
                            center = Offset(pin.screenX, pin.screenY),
                            style = Stroke(width = 1.4f)
                        )

                        // Columna de energía 5D
                        drawLine(
                            brush = Brush.linearGradient(
                                colors = listOf(pinColor, Color.White),
                                start = Offset(pin.screenX, pin.screenY),
                                end = Offset(pin.beaconTopX, pin.beaconTopY)
                            ),
                            start = Offset(pin.screenX, pin.screenY),
                            end = Offset(pin.beaconTopX, pin.beaconTopY),
                            strokeWidth = if (isSelected) 3.4f else 2.2f,
                            cap = StrokeCap.Round
                        )

                        // Punto base en la tierra
                        drawCircle(
                            color = Color.White,
                            radius = 3.8f * depthScale,
                            center = Offset(pin.screenX, pin.screenY)
                        )

                        // Esfera Holográfica 5D en la cima de la torre
                        if (isSelected) {
                            drawCircle(
                                color = pinColor.copy(alpha = 0.40f),
                                radius = 20f * hudPulse,
                                center = Offset(pin.beaconTopX, pin.beaconTopY)
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 12.5f,
                                center = Offset(pin.beaconTopX, pin.beaconTopY),
                                style = Stroke(width = 2.5f)
                            )
                        }

                        drawCircle(
                            color = pinColor,
                            radius = (if (isSelected) 9.5f else 7.0f) * depthScale,
                            center = Offset(pin.beaconTopX, pin.beaconTopY)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = (if (isSelected) 4.0f else 2.8f) * depthScale,
                            center = Offset(pin.beaconTopX - 1.5f, pin.beaconTopY - 1.5f)
                        )

                        // Etiqueta Holográfica Flotante (muestra un panorama amplio de sedes visibles)
                        if (isSelected || pin.depthZ > 0.35f) {
                            val pinShort = pin.work.localizedShortPin(appLanguage)
                            val tapHint = when (appLanguage) {
                                AppLanguage.EN -> "Tap: Open Site"
                                AppLanguage.FR -> "Toucher : Ouvrir"
                                else -> "Toca: Ir al Sitio"
                            }
                            val labelText = if (isSelected) {
                                "📍 $pinShort ($tapHint)"
                            } else {
                                "${pin.work.flag} $pinShort"
                            }
                            val measured = textMeasurer.measure(
                                text = labelText,
                                style = TextStyle(
                                    color = if (isSelected) Color(0xFF020617) else Color.White,
                                    fontSize = if (isSelected) 10.5.sp else 9.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )
                            val labelX = (pin.beaconTopX - measured.size.width / 2f)
                                .coerceIn(8f, w - measured.size.width - 8f)
                            val labelY = (pin.beaconTopY - measured.size.height - (if (isSelected) 16f else 11f))
                                .coerceIn(6f, h - measured.size.height - 6f)

                            drawRoundRect(
                                color = if (isSelected) goldAccent else Color(0xFF051229).copy(alpha = 0.90f),
                                topLeft = Offset(labelX - 7f, labelY - 4f),
                                size = Size(
                                    measured.size.width + 14f,
                                    measured.size.height + 8f
                                ),
                                cornerRadius = CornerRadius(10f, 10f)
                            )
                            drawText(
                                textLayoutResult = measured,
                                topLeft = Offset(labelX, labelY)
                            )
                        }
                    }
            }

            // Leyenda flotante en la parte inferior del Mundo 3D
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF030917).copy(alpha = 0.88f))
                    .border(1.dp, cyanAccent.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasilianWorkCategory.entries.forEach { cat ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(cat.badgeColor)
                        )
                        Text(
                            text = cat.localizedShortLabel(appLanguage),
                            color = Color(0xFFE2E8F0),
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // CARRUSEL DE TOQUE DIRECTO: Al tocar cualquiera lleva a la obra y abre su Panorámica + Dirección
        Text(
            text = when (appLanguage) {
                AppLanguage.EN -> "⚡ QUICK WORK SELECTOR (Tap any work to view its Image, Address & Live Panorama):"
                AppLanguage.FR -> "⚡ ACCÈS RAPIDE AUX ŒUVRES (Touchez une œuvre pour voir son Image, Adresse et Panorama) :"
                else -> "⚡ TOQUE RÁPIDO DE OBRAS (Oprime una obra para ver su Imagen, Dirección y Panorámica):"
            },
            color = goldAccent,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            works.forEach { work ->
                val isSelected = selectedWork?.id == work.id
                Surface(
                    onClick = { onTapWorkPin(work) },
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) work.category.badgeColor.copy(alpha = 0.26f) else Color(0xFF0A152B),
                    border = BorderStroke(
                        width = if (isSelected) 1.6.dp else 1.dp,
                        color = if (isSelected) work.category.badgeColor else Color.White.copy(alpha = 0.14f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = work.category.emoji, fontSize = 14.sp)
                        Column {
                            Text(
                                text = "${work.flag} ${work.localizedShortPin(appLanguage)}",
                                color = Color.White,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold
                            )
                            Text(
                                text = when (appLanguage) {
                                    AppLanguage.EN -> "View Panorama & Map →"
                                    AppLanguage.FR -> "Voir Panorama et Carte →"
                                    else -> "Ver Panorámica y Mapa →"
                                },
                                color = if (isSelected) goldAccent else cyanAccent,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // ====================================================================
        // PANEL INMEDIATO DE IMAGEN, DIRECCIÓN Y PANORÁMICA GOOGLE MAPS DE LA OBRA ACTIVA
        // ====================================================================
        selectedWork?.let { work ->
            ObraImmediatePanoramicCard(
                work = work,
                appLanguage = appLanguage,
                onOpenFullScreenPanoramic = { onOpenPanoramicModal(work) },
                onOpenGoogleMapsApp = { onOpenGoogleMapsApp(work) },
                onOpenStreetViewApp = { onOpenStreetViewApp(work) },
                onOpenWebsite = { onOpenWebsite(work.websiteUrl) }
            )
        }
    }
}

// ============================================================================
// TARJETA INMEDIATA CON IMAGEN, DIRECCIÓN Y GOOGLE MAPS PANORÁMICO EN VIVO
// ============================================================================

private enum class EmbeddedMapViewMode(
    val label: String,
    val labelEn: String,
    val labelFr: String
) {
    SATELLITE_PANORAMA(
        label = "🛰️ Panorámica Satelital 3D",
        labelEn = "🛰️ 3D Satellite Panorama",
        labelFr = "🛰️ Panorama Satellite 3D"
    ),
    STREET_MAP(
        label = "🗺️ Mapa y Dirección Exacta",
        labelEn = "🗺️ Street Map & Address",
        labelFr = "🗺️ Carte et Adresse Exacte"
    );

    fun localizedLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> labelEn
        AppLanguage.FR -> labelFr
        else -> label
    }
}

@Composable
private fun ObraImmediatePanoramicCard(
    work: BasilianGlobalWork,
    appLanguage: AppLanguage = AppLanguage.ES,
    onOpenFullScreenPanoramic: () -> Unit,
    onOpenGoogleMapsApp: () -> Unit,
    onOpenStreetViewApp: () -> Unit,
    onOpenWebsite: () -> Unit
) {
    var mapMode by remember { mutableStateOf(EmbeddedMapViewMode.SATELLITE_PANORAMA) }
    var showLiveMapInsideCard by remember { mutableStateOf(true) }

    val cyanAccent = Color(0xFF00F0FF)
    val goldAccent = Color(0xFFFBBF24)
    val emeraldAccent = Color(0xFF34D399)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        work.category.badgeColor.copy(alpha = 0.20f),
                        Color(0xFF08142C),
                        Color(0xFF040A18)
                    )
                )
            )
            .border(1.8.dp, work.category.badgeColor.copy(alpha = 0.75f), RoundedCornerShape(22.dp))
            .padding(16.dp)
            .testTag("selected_work_panoramic_card")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // 1. Fotografía / Imagen Visual de la Obra + Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF0B1730))
                    .border(1.dp, cyanAccent.copy(alpha = 0.45f), RoundedCornerShape(18.dp))
                    .clickable { onOpenFullScreenPanoramic() }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(work.panoramicImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = work.localizedName(appLanguage),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.25f),
                                    Color.Transparent,
                                    Color(0xFF020617).copy(alpha = 0.92f)
                                )
                            )
                        )
                )

                // Etiquetas superiores sobre la imagen
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color(0xFF020617).copy(alpha = 0.82f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, work.category.badgeColor)
                    ) {
                        Text(
                            text = "${work.flag} ${work.category.emoji} ${work.category.localizedLabel(appLanguage)}",
                            color = work.category.badgeColor,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        color = goldAccent,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.clickable { onOpenFullScreenPanoramic() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = null,
                                tint = Color(0xFF020617),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = when (appLanguage) {
                                    AppLanguage.EN -> "Expand Panorama"
                                    AppLanguage.FR -> "Agrandir Panorama"
                                    else -> "Ampliar Panorámica"
                                },
                                color = Color(0xFF020617),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                // Título sobre la base de la imagen
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = work.localizedName(appLanguage),
                        color = Color.White,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif
                    )
                    val foundedPrefix = when (appLanguage) {
                        AppLanguage.EN -> "Est."
                        AppLanguage.FR -> "Fond."
                        else -> "Fund."
                    }
                    Text(
                        text = "${work.city}, ${work.localizedCountry(appLanguage)} · $foundedPrefix ${work.localizedFoundedYear(appLanguage)}",
                        color = goldAccent,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 2. Cuadro Destacado de Dirección Exacta y Coordenadas GPS
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF051024))
                    .border(1.dp, cyanAccent.copy(alpha = 0.55f), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = when (appLanguage) {
                            AppLanguage.EN -> "Address"
                            AppLanguage.FR -> "Adresse"
                            else -> "Dirección"
                        },
                        tint = cyanAccent,
                        modifier = Modifier.size(22.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (appLanguage) {
                                AppLanguage.EN -> "EXACT ADDRESS & GPS LOCATION"
                                AppLanguage.FR -> "ADRESSE EXACTE ET LOCALISATION GPS"
                                else -> "DIRECCIÓN EXACTA Y UBICACIÓN GPS"
                            },
                            color = cyanAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = work.address,
                            color = Color.White,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 17.sp
                        )
                        Text(
                            text = when (appLanguage) {
                                AppLanguage.EN -> "Coordinates: ${work.latitudeDeg}°, ${work.longitudeDeg}°"
                                AppLanguage.FR -> "Coordonnées : ${work.latitudeDeg}°, ${work.longitudeDeg}°"
                                else -> "Coordenadas: ${work.latitudeDeg}°, ${work.longitudeDeg}°"
                            },
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // 3. Visor Panorámico / Google Maps Interactivo Integrado
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "🌍 GOOGLE MAPS & LIVE PANORAMIC VIEW"
                            AppLanguage.FR -> "🌍 GOOGLE MAPS ET VUE PANORAMIQUE EN DIRECT"
                            else -> "🌍 GOOGLE MAPS Y VISTA PANORÁMICA EN VIVO"
                        },
                        color = goldAccent,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        EmbeddedMapViewMode.entries.forEach { mode ->
                            val selected = mapMode == mode
                            Surface(
                                onClick = {
                                    mapMode = mode
                                    showLiveMapInsideCard = true
                                },
                                shape = RoundedCornerShape(8.dp),
                                color = if (selected) cyanAccent.copy(alpha = 0.25f) else Color(0xFF0B1528),
                                border = BorderStroke(
                                    1.dp,
                                    if (selected) cyanAccent else Color.White.copy(alpha = 0.15f)
                                )
                            ) {
                                Text(
                                    text = mode.localizedLabel(appLanguage),
                                    color = if (selected) Color.White else Color(0xFFCBD5E1),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                if (showLiveMapInsideCard) {
                    GoogleMapsPanoramicWebView(
                        work = work,
                        isSatellitePanorama = mapMode == EmbeddedMapViewMode.SATELLITE_PANORAMA,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.2.dp, cyanAccent.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                    )
                }
            }

            // 4. Descripción y Noticias de la Obra (traducidas en ES/EN/FR)
            Text(
                text = work.localizedDescription(appLanguage),
                color = Color(0xFFE2E8F0),
                fontSize = 12.5.sp,
                lineHeight = 18.sp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF050B16).copy(alpha = 0.88f))
                    .border(1.dp, goldAccent.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "📰 CURRENT NEWS & MISSION"
                            AppLanguage.FR -> "📰 ACTUALITÉ ET MISSION"
                            else -> "📰 ACTUALIDAD Y MISIÓN"
                        },
                        color = goldAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = work.localizedRecentNews(appLanguage),
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }

            // 5. Botones de Acción Inmediata: Street View 360°, Google Maps y Sitio Web
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onOpenStreetViewApp,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = goldAccent,
                        contentColor = Color(0xFF020617)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 9.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Streetview,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "360° Panorama"
                            AppLanguage.FR -> "Panorama 360°"
                            else -> "Panorámica 360°"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1
                    )
                }

                Button(
                    onClick = onOpenGoogleMapsApp,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = emeraldAccent,
                        contentColor = Color(0xFF020617)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 9.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Google Maps",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1
                    )
                }

                OutlinedButton(
                    onClick = onOpenWebsite,
                    border = BorderStroke(1.2.dp, cyanAccent),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = cyanAccent),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 9.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Web",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

// ============================================================================
// MODAL PANORÁMICO E IMAGEN + GOOGLE MAPS QUE SE ABRE AL OPRIMIR UNA OBRA EN EL MUNDO 3D
// ============================================================================

@Composable
private fun ObraPanoramicaGoogleMapsModal(
    work: BasilianGlobalWork,
    appLanguage: AppLanguage = AppLanguage.ES,
    onDismiss: () -> Unit,
    onOpenGoogleMapsApp: () -> Unit,
    onOpenStreetViewApp: () -> Unit,
    onOpenOfficialWebsite: () -> Unit
) {
    var isSatelliteMode by remember { mutableStateOf(true) }
    val cyanAccent = Color(0xFF00F0FF)
    val goldAccent = Color(0xFFFBBF24)
    val emeraldAccent = Color(0xFF34D399)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.86f))
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF06132C),
                                Color(0xFF040B1A)
                            )
                        )
                    )
                    .border(2.dp, cyanAccent.copy(alpha = 0.8f), RoundedCornerShape(26.dp))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Encabezado del Modal Panorámico
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = work.flag, fontSize = 22.sp)
                        Column {
                            Text(
                                text = when (appLanguage) {
                                    AppLanguage.EN -> "PANORAMIC VIEW & ADDRESS OF THE WORK"
                                    AppLanguage.FR -> "VUE PANORAMIQUE ET ADRESSE DE L'ŒUVRE"
                                    else -> "VISTA PANORÁMICA Y DIRECCIÓN DE LA OBRA"
                                },
                                color = cyanAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = work.localizedName(appLanguage),
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = when (appLanguage) {
                                AppLanguage.EN -> "Close"
                                AppLanguage.FR -> "Fermer"
                                else -> "Cerrar"
                            },
                            tint = Color.White
                        )
                    }
                }

                // 1. Imagen Panorámica de la Obra
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, goldAccent.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(work.panoramicImageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = work.localizedName(appLanguage),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xFF020617).copy(alpha = 0.88f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    ) {
                        val foundedPrefix = when (appLanguage) {
                            AppLanguage.EN -> "Est."
                            AppLanguage.FR -> "Fond."
                            else -> "Fund."
                        }
                        Text(
                            text = "${work.category.emoji} ${work.category.localizedLabel(appLanguage)} · $foundedPrefix ${work.localizedFoundedYear(appLanguage)}",
                            color = goldAccent,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = work.localizedMotto(appLanguage),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 2. Dirección Física Exacta
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF091936))
                        .border(1.dp, cyanAccent.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = goldAccent,
                            modifier = Modifier.size(22.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = when (appLanguage) {
                                    AppLanguage.EN -> "EXACT ADDRESS OF THIS WORK:"
                                    AppLanguage.FR -> "ADRESSE EXACTE DE CETTE ŒUVRE :"
                                    else -> "DIRECCIÓN DONDE SE ENCUENTRA LA OBRA:"
                                },
                                color = goldAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = work.address,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${work.city}, ${work.localizedCountry(appLanguage)} (GPS: ${work.latitudeDeg}, ${work.longitudeDeg})",
                                color = cyanAccent,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // 3. Google Maps Panorámico / Satelital en Vivo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "🛰️ LIVE GOOGLE MAPS PANORAMA"
                            AppLanguage.FR -> "🛰️ PANORAMA GOOGLE MAPS EN DIRECT"
                            else -> "🛰️ PANORÁMICA GOOGLE MAPS EN VIVO"
                        },
                        color = cyanAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(
                            selected = isSatelliteMode,
                            onClick = { isSatelliteMode = true },
                            label = {
                                Text(
                                    when (appLanguage) {
                                        AppLanguage.EN -> "3D Satellite"
                                        AppLanguage.FR -> "Satellite 3D"
                                        else -> "Satélite 3D"
                                    },
                                    fontSize = 11.sp
                                )
                            }
                        )
                        FilterChip(
                            selected = !isSatelliteMode,
                            onClick = { isSatelliteMode = false },
                            label = {
                                Text(
                                    when (appLanguage) {
                                        AppLanguage.EN -> "Street Map"
                                        AppLanguage.FR -> "Carte Rues"
                                        else -> "Mapa Calles"
                                    },
                                    fontSize = 11.sp
                                )
                            }
                        )
                    }
                }

                GoogleMapsPanoramicWebView(
                    work = work,
                    isSatellitePanorama = isSatelliteMode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.5.dp, cyanAccent.copy(alpha = 0.7f), RoundedCornerShape(18.dp))
                )

                // 4. Botones para abrir Street View 360°, Google Maps y Sitio Web
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onOpenStreetViewApp,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = goldAccent,
                            contentColor = Color(0xFF020617)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Streetview, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Street View 360°", fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    Button(
                        onClick = onOpenGoogleMapsApp,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = emeraldAccent,
                            contentColor = Color(0xFF020617)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            when (appLanguage) {
                                AppLanguage.EN -> "Open in Maps"
                                AppLanguage.FR -> "Ouvrir dans Maps"
                                else -> "Abrir en Maps"
                            },
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                OutlinedButton(
                    onClick = onOpenOfficialWebsite,
                    border = BorderStroke(1.2.dp, cyanAccent),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        when (appLanguage) {
                            AppLanguage.EN -> "Visit Official Website of the Work"
                            AppLanguage.FR -> "Visiter le Site Web Officiel de l'Œuvre"
                            else -> "Visitar Sitio Web Oficial de la Obra"
                        },
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun GoogleMapsPanoramicWebView(
    work: BasilianGlobalWork,
    isSatellitePanorama: Boolean,
    modifier: Modifier = Modifier
) {
    val mapType = if (isSatellitePanorama) "k" else "m" // k = satellite/aerial, m = standard map
    val zoom = if (isSatellitePanorama) 18 else 16
    val encodedQuery = remember(work.id) {
        try {
            URLEncoder.encode("${work.latitudeDeg},${work.longitudeDeg}", "UTF-8")
        } catch (_: Exception) {
            "${work.latitudeDeg},${work.longitudeDeg}"
        }
    }
    val embedUrl = remember(work.id, isSatellitePanorama) {
        "https://maps.google.com/maps?q=$encodedQuery&t=$mapType&z=$zoom&ie=UTF8&iwloc=&output=embed"
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                try {
                    setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)
                } catch (_: Exception) {}
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                webViewClient = object : WebViewClient() {
                    override fun onRenderProcessGone(
                        view: WebView?,
                        detail: android.webkit.RenderProcessGoneDetail?
                    ): Boolean {
                        try {
                            (view?.parent as? android.view.ViewGroup)?.removeView(view)
                            view?.destroy()
                        } catch (_: Exception) {}
                        return true
                    }
                }
                webChromeClient = WebChromeClient()
                loadUrl(embedUrl)
            }
        },
        update = { webView ->
            if (webView.url != embedUrl) {
                webView.loadUrl(embedUrl)
            }
        },
        onRelease = { webView ->
            try {
                webView.stopLoading()
                webView.onPause()
                webView.removeAllViews()
                webView.destroy()
            } catch (_: Exception) {}
        }
    )
}

// ============================================================================
// TARJETA INDIVIDUAL DEL DIRECTORIO DE OBRAS Y PASTORALES (ES / EN / FR)
// ============================================================================

@Composable
private fun BasilianWorkDetailCard(
    work: BasilianGlobalWork,
    appLanguage: AppLanguage = AppLanguage.ES,
    isSelected: Boolean,
    onOpenPanoramicAndMap: () -> Unit,
    onOpenGoogleMaps: () -> Unit,
    onOpenUrl: (String) -> Unit
) {
    val accent = work.category.badgeColor
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .futuristic3DCard(
                accentColor = if (isSelected) accent else accent.copy(alpha = 0.65f),
                secondaryColor = Color(0xFFFBBF24),
                cornerRadius = 20.dp,
                tiltDegrees = if (isSelected) 1.2f else 0.6f
            )
            .clickable { onOpenPanoramicAndMap() }
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = work.flag, fontSize = 18.sp)
                    Surface(
                        color = accent.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${work.category.emoji} ${work.category.localizedShortLabel(appLanguage)}",
                            color = accent,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Text(
                        text = "${work.city}, ${work.localizedCountry(appLanguage)}",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Text(
                text = work.localizedName(appLanguage),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif
            )

            val addressLabel = when (appLanguage) {
                AppLanguage.EN -> "Address"
                AppLanguage.FR -> "Adresse"
                else -> "Dirección"
            }
            Text(
                text = "📍 $addressLabel: ${work.address}",
                color = Color(0xFF38BDF8),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = work.localizedDescription(appLanguage),
                color = Color(0xFFCBD5E1),
                fontSize = 12.5.sp,
                lineHeight = 18.sp
            )

            Text(
                text = "📰 ${work.localizedRecentNews(appLanguage)}",
                color = Color(0xFFFDE68A),
                fontSize = 11.5.sp,
                lineHeight = 16.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = onOpenPanoramicAndMap,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = accent.copy(alpha = 0.22f),
                        contentColor = accent
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Streetview,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = when (appLanguage) {
                            AppLanguage.EN -> "Panorama & Image"
                            AppLanguage.FR -> "Panorama & Image"
                            else -> "Panorámica e Imagen"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                FilledTonalButton(
                    onClick = onOpenGoogleMaps,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFF34D399).copy(alpha = 0.20f),
                        contentColor = Color(0xFF34D399)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Google Maps",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { onOpenUrl(work.websiteUrl) },
                    border = BorderStroke(1.dp, accent.copy(alpha = 0.55f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Web",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
