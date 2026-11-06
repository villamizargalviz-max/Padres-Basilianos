package com.example.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class ThemeCategory(val title: String) {
    TODOS("Todos"),
    ESCUDO("Escudo C.S.B."),
    HARRY_POTTER("Harry Potter"),
    MARVEL("Marvel"),
    DISNEY("Disney & Cine"),
    ANIME("Anime")
}

enum class BasilianAppTheme(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val category: ThemeCategory,
    val primaryColor: Color,
    val onPrimaryColor: Color,
    val secondaryColor: Color,
    val onSecondaryColor: Color,
    val goldColor: Color,
    val backgroundColor: Color,
    val surfaceColor: Color,
    val onSurfaceColor: Color,
    val surfaceVariantColor: Color,
    val onSurfaceVariantColor: Color,
    val outlineColor: Color,
    val isDark: Boolean,
    val fontFamily: FontFamily,
    val fontName: String,
    val cardShape: CornerBasedShape,
    val cardBorderWidth: Dp,
    val themeTag: String,
    val themeMotto: String
) {
    // =========================================================================
    // 1. ESCUDO BASILIANO (Colores Oficiales C.S.B. • Luz Noble y Noche Solemne)
    // =========================================================================
    ESCUDO_BASILIANO(
        id = "escudo_basiliano",
        displayName = "Escudo Basiliano Oficial",
        subtitle = "Colores oficiales C.S.B.: Verde cáliz, Carmesí libro, Oro noble y Marfil cálido",
        category = ThemeCategory.ESCUDO,
        primaryColor = Color(0xFF1B6B3E),       // Verde bosque profundo y solemne
        onPrimaryColor = Color.White,
        secondaryColor = Color(0xFFB91C1C),     // Carmesí litúrgico
        onSecondaryColor = Color.White,
        goldColor = Color(0xFFB45309),          // Oro noble heráldico
        backgroundColor = Color(0xFFF9F7F2),    // Marfil pergamino noble
        surfaceColor = Color(0xFFFFFFFF),       // Superficie limpia y luminosa
        onSurfaceColor = Color(0xFF0F172A),     // Texto pizarra oscura (100% contraste)
        surfaceVariantColor = Color(0xFFF1EDE4),
        onSurfaceVariantColor = Color(0xFF475569),
        outlineColor = Color(0xFFE2D9CC),
        isDark = false,
        fontFamily = FontFamily.Serif,
        fontName = "Serif Litúrgico",
        cardShape = RoundedCornerShape(16.dp),
        cardBorderWidth = 1.dp,
        themeTag = "C.S.B. 1822",
        themeMotto = "Bonitatem et disciplinam et scientiam doce me"
    ),

    ESCUDO_NOCTURNO(
        id = "escudo_nocturno",
        displayName = "Escudo Basiliano Solemne",
        subtitle = "Vigilia monástica: Verde esmeralda resplandeciente, Carmesí vivo y Oro celestial",
        category = ThemeCategory.ESCUDO,
        primaryColor = Color(0xFF22C55E),       // Esmeralda luminosa sobre fondo oscuro
        onPrimaryColor = Color(0xFF052E16),
        secondaryColor = Color(0xFFEF4444),     // Carmesí vivo
        onSecondaryColor = Color.White,
        goldColor = Color(0xFFFBBF24),          // Oro celestial brillante
        backgroundColor = Color(0xFF09140E),    // Lienzo nocturno bosque
        surfaceColor = Color(0xFF13231A),       // Tarjeta elevada
        onSurfaceColor = Color(0xFFF8FAFC),     // Texto blanco cristalino
        surfaceVariantColor = Color(0xFF1B3225),
        onSurfaceVariantColor = Color(0xFFCBD5E1),
        outlineColor = Color(0xFF2E513D),
        isDark = true,
        fontFamily = FontFamily.Serif,
        fontName = "Serif Litúrgico",
        cardShape = RoundedCornerShape(16.dp),
        cardBorderWidth = 1.5.dp,
        themeTag = "VIGILIA SACRA",
        themeMotto = "En la quietud de la noche florece la oración y la fidelidad"
    ),

    // =========================================================================
    // 2. HARRY POTTER (Cine, Magia Épica & Armaduras de Honor)
    // =========================================================================
    HP_GRYFFINDOR(
        id = "hp_gryffindor",
        displayName = "Gryffindor: León de Oro y Grana",
        subtitle = "Escarlata noble, Oro antiguo & honor para caballeros valientes",
        category = ThemeCategory.HARRY_POTTER,
        primaryColor = Color(0xFFF59E0B),       // Oro de león brillante
        onPrimaryColor = Color(0xFF451A03),
        secondaryColor = Color(0xFFDC2626),     // Grana escarlata
        onSecondaryColor = Color.White,
        goldColor = Color(0xFFFDE047),          // Oro sol radiante
        backgroundColor = Color(0xFF150708),    // Granate profundo
        surfaceColor = Color(0xFF240E10),       // Sala común de Gryffindor
        onSurfaceColor = Color(0xFFFFF1F2),     // Texto marfil rosado legible
        surfaceVariantColor = Color(0xFF381519),
        onSurfaceVariantColor = Color(0xFFFECDD3),
        outlineColor = Color(0xFF78252B),
        isDark = true,
        fontFamily = FontFamily.Serif,
        fontName = "Serif Mágico",
        cardShape = RoundedCornerShape(12.dp),
        cardBorderWidth = 1.5.dp,
        themeTag = "GRYFFINDOR",
        themeMotto = "Fortuna Audaces Iuvat • Coraje, Lealtad y Valentía"
    ),

    HP_SLYTHERIN(
        id = "hp_slytherin",
        displayName = "Slytherin: Plata y Esmeralda",
        subtitle = "Verde esmeralda resplandeciente, Acero templado & astucia noble",
        category = ThemeCategory.HARRY_POTTER,
        primaryColor = Color(0xFF10B981),       // Esmeralda viva y brillante
        onPrimaryColor = Color(0xFF022C22),
        secondaryColor = Color(0xFF94A3B8),     // Acero plateado
        onSecondaryColor = Color(0xFF0F172A),
        goldColor = Color(0xFFE2E8F0),          // Plata pulida
        backgroundColor = Color(0xFF06140B),    // Mazmorra verde abisal
        surfaceColor = Color(0xFF0D2415),       // Sala de Slytherin
        onSurfaceColor = Color(0xFFF0FDF4),     // Texto blanco esmeralda cristalino
        surfaceVariantColor = Color(0xFF153822),
        onSurfaceVariantColor = Color(0xFFCBD5E1),
        outlineColor = Color(0xFF1F5434),
        isDark = true,
        fontFamily = FontFamily.Serif,
        fontName = "Serif Clásico",
        cardShape = CutCornerShape(8.dp),
        cardBorderWidth = 1.5.dp,
        themeTag = "SLYTHERIN",
        themeMotto = "Astucia, Determinación y Ambición de Grandeza"
    ),

    HP_RAVENCLAW(
        id = "hp_ravenclaw",
        displayName = "Ravenclaw: Zafiro y Bronce",
        subtitle = "Azul zafiro celestial, Bronce erudito & sabiduría infinita",
        category = ThemeCategory.HARRY_POTTER,
        primaryColor = Color(0xFF38BDF8),       // Zafiro celeste radiante
        onPrimaryColor = Color(0xFF082F49),
        secondaryColor = Color(0xFFD97706),     // Bronce antiguo
        onSecondaryColor = Color.White,
        goldColor = Color(0xFFFCD34D),          // Ámbar brillante
        backgroundColor = Color(0xFF08101E),    // Torre de medianoche
        surfaceColor = Color(0xFF101D33),       // Biblioteca de Ravenclaw
        onSurfaceColor = Color(0xFFF0F9FF),     // Texto blanco azulado
        surfaceVariantColor = Color(0xFF192C4D),
        onSurfaceVariantColor = Color(0xFFBAE6FD),
        outlineColor = Color(0xFF234475),
        isDark = true,
        fontFamily = FontFamily.Serif,
        fontName = "Serif Erudito",
        cardShape = RoundedCornerShape(14.dp),
        cardBorderWidth = 1.5.dp,
        themeTag = "RAVENCLAW",
        themeMotto = "Una mente sin límites es el mayor tesoro del hombre"
    ),

    HP_HOGWARTS_SOMBRAS(
        id = "hp_hogwarts_sombras",
        displayName = "Hogwarts: Sombras & Hechizos",
        subtitle = "Piedra rúnica medieval, Luz de velas & misticismo milenario",
        category = ThemeCategory.HARRY_POTTER,
        primaryColor = Color(0xFFF59E0B),       // Luz de candelabro
        onPrimaryColor = Color(0xFF451A03),
        secondaryColor = Color(0xFF8B5CF6),     // Púrpura hechizo arcano
        onSecondaryColor = Color.White,
        goldColor = Color(0xFFFBBF24),          // Oro mágico
        backgroundColor = Color(0xFF0D0F18),    // Castillo en la noche
        surfaceColor = Color(0xFF1A1D2B),       // Murallas del Gran Comedor
        onSurfaceColor = Color(0xFFF8FAFC),     // Texto blanco puro
        surfaceVariantColor = Color(0xFF272B3E),
        onSurfaceVariantColor = Color(0xFFCBD5E1),
        outlineColor = Color(0xFF3C4361),
        isDark = true,
        fontFamily = FontFamily.Serif,
        fontName = "Serif Gótico",
        cardShape = RoundedCornerShape(14.dp),
        cardBorderWidth = 1.5.dp,
        themeTag = "HOGWARTS",
        themeMotto = "Draco Dormiens Nunquam Titillandus"
    ),

    // =========================================================================
    // 3. MARVEL (Superhéroes, Aleaciones y Alta Tecnología)
    // =========================================================================
    MARVEL_IRON_MAN(
        id = "marvel_iron_man",
        displayName = "Iron Man: Carmesí y Oro Stark",
        subtitle = "Carmesí Mark 85, Oro de aleación & brillo Reactor Arc",
        category = ThemeCategory.MARVEL,
        primaryColor = Color(0xFFFBBF24),       // Oro hot-rod brillante Stark
        onPrimaryColor = Color(0xFF451A03),
        secondaryColor = Color(0xFFEF4444),     // Carmesí blindado
        onSecondaryColor = Color.White,
        goldColor = Color(0xFF06B6D4),          // Resplandor cian Reactor Arc
        backgroundColor = Color(0xFF140808),    // Aleación de titanio oscuro
        surfaceColor = Color(0xFF261010),       // Taller Stark
        onSurfaceColor = Color(0xFFFFFBEB),     // Texto oro suave legible
        surfaceVariantColor = Color(0xFF3D1A1A),
        onSurfaceVariantColor = Color(0xFFFED7AA),
        outlineColor = Color(0xFF7C2D2D),
        isDark = true,
        fontFamily = FontFamily.SansSerif,
        fontName = "Sans Tecnológico",
        cardShape = CutCornerShape(10.dp),
        cardBorderWidth = 1.5.dp,
        themeTag = "STARK TECH",
        themeMotto = "I am Iron Man • Tecnología, Precisión y Sacrificio"
    ),

    MARVEL_BLACK_PANTHER(
        id = "marvel_black_panther",
        displayName = "Black Panther: Wakanda Vibranium",
        subtitle = "Negro carbón táctico, Púrpura cinética real & plata vibranium",
        category = ThemeCategory.MARVEL,
        primaryColor = Color(0xFFA855F7),       // Púrpura cinético luminoso
        onPrimaryColor = Color(0xFF3B0764),
        secondaryColor = Color(0xFFCBD5E1),     // Plata vibranium
        onSecondaryColor = Color(0xFF0F172A),
        goldColor = Color(0xFFC084FC),          // Energía ancestral
        backgroundColor = Color(0xFF090611),    // Reino nocturno de Wakanda
        surfaceColor = Color(0xFF171026),       // Templo de la Pantera
        onSurfaceColor = Color(0xFFFAF5FF),     // Texto blanco púrpura
        surfaceVariantColor = Color(0xFF261B3D),
        onSurfaceVariantColor = Color(0xFFE9D5FF),
        outlineColor = Color(0xFF4C3575),
        isDark = true,
        fontFamily = FontFamily.SansSerif,
        fontName = "Sans Táctico",
        cardShape = RoundedCornerShape(20.dp),
        cardBorderWidth = 1.5.dp,
        themeTag = "WAKANDA",
        themeMotto = "Wakanda Forever • Nobleza, Fuerza y Sabiduría Ancestral"
    ),

    MARVEL_THOR(
        id = "marvel_thor",
        displayName = "Thor: Asgard y Trueno Mjolnir",
        subtitle = "Azul tormenta relámpago, Oro divino celestial & poder nórdico",
        category = ThemeCategory.MARVEL,
        primaryColor = Color(0xFF60A5FA),       // Relámpago azul brillante
        onPrimaryColor = Color(0xFF1E3A8A),
        secondaryColor = Color(0xFFF59E0B),     // Oro asgardiano
        onSecondaryColor = Color(0xFF451A03),
        goldColor = Color(0xFF93C5FD),          // Chispas de trueno
        backgroundColor = Color(0xFF070E1E),    // Bifrost cósmico
        surfaceColor = Color(0xFF0F1B35),       // Salones de Odín
        onSurfaceColor = Color(0xFFEFF6FF),     // Texto celestial
        surfaceVariantColor = Color(0xFF192C52),
        onSurfaceVariantColor = Color(0xFFBFDBFE),
        outlineColor = Color(0xFF2A4880),
        isDark = true,
        fontFamily = FontFamily.Serif,
        fontName = "Serif Divino",
        cardShape = RoundedCornerShape(8.dp),
        cardBorderWidth = 1.5.dp,
        themeTag = "ASGARD",
        themeMotto = "Por el Poder del Trueno • Protección y Fuerza Divina"
    ),

    MARVEL_CAPITAN_AMERICA(
        id = "marvel_capitan_america",
        displayName = "Capitán América: Soldado de Élite",
        subtitle = "Azul marino táctico, Escarlata del escudo & plata vibranium",
        category = ThemeCategory.MARVEL,
        primaryColor = Color(0xFF3B82F6),       // Azul escudo vibrante
        onPrimaryColor = Color.White,
        secondaryColor = Color(0xFFEF4444),     // Carmesí del escudo
        onSecondaryColor = Color.White,
        goldColor = Color(0xFFE2E8F0),          // Estrella de plata
        backgroundColor = Color(0xFF0A1224),    // Azul táctico medianoche
        surfaceColor = Color(0xFF14203A),       // Blindaje de combate
        onSurfaceColor = Color(0xFFF8FAFC),     // Texto blanco nítido
        surfaceVariantColor = Color(0xFF1F3054),
        onSurfaceVariantColor = Color(0xFFCBD5E1),
        outlineColor = Color(0xFF2C4373),
        isDark = true,
        fontFamily = FontFamily.SansSerif,
        fontName = "Sans Militar",
        cardShape = RoundedCornerShape(12.dp),
        cardBorderWidth = 1.5.dp,
        themeTag = "FIRST AVENGER",
        themeMotto = "I Can Do This All Day • Honor, Deber y Justicia"
    ),

    // =========================================================================
    // 4. DISNEY & CINE (Leyendas Guerreras y Universos Épicos)
    // =========================================================================
    DISNEY_MANDALORIAN(
        id = "disney_mandalorian",
        displayName = "Star Wars: The Mandalorian",
        subtitle = "Acero Beskar blindado, Carbón táctico & honor guerrero",
        category = ThemeCategory.DISNEY,
        primaryColor = Color(0xFFE2E8F0),       // Brillo del acero Beskar
        onPrimaryColor = Color(0xFF0F172A),
        secondaryColor = Color(0xFFF59E0B),     // Ámbar del Signo Mudhorn
        onSecondaryColor = Color(0xFF451A03),
        goldColor = Color(0xFF94A3B8),          // Forja de acero
        backgroundColor = Color(0xFF0D1016),    // Planeta Nevarro oscuro
        surfaceColor = Color(0xFF181F2A),       // Templo de la Forja
        onSurfaceColor = Color(0xFFF8FAFC),     // Texto blanco titanio
        surfaceVariantColor = Color(0xFF263040),
        onSurfaceVariantColor = Color(0xFFCBD5E1),
        outlineColor = Color(0xFF3D4B60),
        isDark = true,
        fontFamily = FontFamily.SansSerif,
        fontName = "Sans Beskar",
        cardShape = CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp, topEnd = 3.dp, bottomStart = 3.dp),
        cardBorderWidth = 1.5.dp,
        themeTag = "BESKAR STEEL",
        themeMotto = "This is the Way • Lealtad, Clan y Hermandad"
    ),

    DISNEY_TRON(
        id = "disney_tron",
        displayName = "Tron: Red Cibernética Legacy",
        subtitle = "Cian neón cibernético, Negro abisal & luz de circuitos",
        category = ThemeCategory.DISNEY,
        primaryColor = Color(0xFF22D3EE),       // Cian neón hipnótico
        onPrimaryColor = Color(0xFF083344),
        secondaryColor = Color(0xFF3B82F6),     // Azul digital de flujo
        onSecondaryColor = Color.White,
        goldColor = Color(0xFF67E8F9),          // Resplandor de circuito
        backgroundColor = Color(0xFF030712),    // The Grid abisal
        surfaceColor = Color(0xFF081226),       // Torre de control
        onSurfaceColor = Color(0xFFECFEFF),     // Texto cian blanco
        surfaceVariantColor = Color(0xFF102142),
        onSurfaceVariantColor = Color(0xFFA5F3FC),
        outlineColor = Color(0xFF1E3A6E),
        isDark = true,
        fontFamily = FontFamily.Monospace,
        fontName = "Monospace Digital",
        cardShape = CutCornerShape(6.dp),
        cardBorderWidth = 2.dp,
        themeTag = "THE GRID",
        themeMotto = "I fight for the Users • Circuitos de Luz y Verdad"
    ),

    DISNEY_PIRATAS(
        id = "disney_piratas",
        displayName = "Piratas del Caribe: La Perla Negra",
        subtitle = "Madera de corsario, Oro doblón azteca & bruma del mar abierto",
        category = ThemeCategory.DISNEY,
        primaryColor = Color(0xFFF59E0B),       // Oro doblón azteca
        onPrimaryColor = Color(0xFF451A03),
        secondaryColor = Color(0xFFD97706),     // Bronce naval
        onSecondaryColor = Color.White,
        goldColor = Color(0xFFFDE047),          // Brillo del botín
        backgroundColor = Color(0xFF0E0B08),    // Océano nocturno
        surfaceColor = Color(0xFF1C1611),       // Cubierta de la Perla Negra
        onSurfaceColor = Color(0xFFFFFBEB),     // Texto pergamino cálido
        surfaceVariantColor = Color(0xFF2E241C),
        onSurfaceVariantColor = Color(0xFFFDE68A),
        outlineColor = Color(0xFF523F30),
        isDark = true,
        fontFamily = FontFamily.Serif,
        fontName = "Serif Corsario",
        cardShape = RoundedCornerShape(12.dp),
        cardBorderWidth = 1.5.dp,
        themeTag = "BLACK PEARL",
        themeMotto = "Hacia el Horizonte • Libertad y Compañerismo"
    ),

    // =========================================================================
    // 5. ANIME (Guerreros Legendarios & Acción Heroica)
    // =========================================================================
    ANIME_DRAGON_BALL(
        id = "anime_dragon_ball",
        displayName = "Dragon Ball: Super Saiyan Ki",
        subtitle = "Ki dorado solar, Gi Kame naranja & azul índigo de combate",
        category = ThemeCategory.ANIME,
        primaryColor = Color(0xFFFACC15),       // Ki dorado solar
        onPrimaryColor = Color(0xFF422006),
        secondaryColor = Color(0xFFF97316),     // Naranja Gi Kame
        onSecondaryColor = Color.White,
        goldColor = Color(0xFFFEF08A),          // Aura resplandeciente
        backgroundColor = Color(0xFF120904),    // Espacio exterior oscuro
        surfaceColor = Color(0xFF241308),       // Templo de artes marciales
        onSurfaceColor = Color(0xFFFFFBEB),     // Texto luminoso
        surfaceVariantColor = Color(0xFF381F0D),
        onSurfaceVariantColor = Color(0xFFFED7AA),
        outlineColor = Color(0xFF6B3A14),
        isDark = true,
        fontFamily = FontFamily.SansSerif,
        fontName = "Sans Heroico",
        cardShape = RoundedCornerShape(18.dp),
        cardBorderWidth = 1.8.dp,
        themeTag = "SUPER SAIYAN",
        themeMotto = "Kamehameha • Superando todos los límites por los hermanos"
    ),

    ANIME_ATTACK_ON_TITAN(
        id = "anime_attack_on_titan",
        displayName = "Attack on Titan: Capa de la Libertad",
        subtitle = "Verde militar capa de la libertad, Cuero de titán & acero templado",
        category = ThemeCategory.ANIME,
        primaryColor = Color(0xFF22C55E),       // Verde alas de la libertad
        onPrimaryColor = Color(0xFF052E16),
        secondaryColor = Color(0xFFB45309),     // Cuero militar
        onSecondaryColor = Color.White,
        goldColor = Color(0xFFCBD5E1),          // Cuchillas de acero
        backgroundColor = Color(0xFF08120B),    // Muralla Maria de noche
        surfaceColor = Color(0xFF112217),       // Cuartel de exploración
        onSurfaceColor = Color(0xFFF0FDF4),     // Texto blanco militar
        surfaceVariantColor = Color(0xFF1A3524),
        onSurfaceVariantColor = Color(0xFFBBF7D0),
        outlineColor = Color(0xFF2D5C3E),
        isDark = true,
        fontFamily = FontFamily.Serif,
        fontName = "Serif Marcial",
        cardShape = RoundedCornerShape(6.dp),
        cardBorderWidth = 1.5.dp,
        themeTag = "SCOUT REGIMENT",
        themeMotto = "Shinzo wo Sasageyo • Entrega total por la humanidad"
    ),

    ANIME_NARUTO(
        id = "anime_naruto",
        displayName = "Naruto: Voluntad de Fuego Hokage",
        subtitle = "Rojo carmesí capa Hokage, Naranja ninja & sol de chakra",
        category = ThemeCategory.ANIME,
        primaryColor = Color(0xFFEF4444),       // Fuego Hokage radiante
        onPrimaryColor = Color.White,
        secondaryColor = Color(0xFFF97316),     // Llama ninja
        onSecondaryColor = Color.White,
        goldColor = Color(0xFFFDE047),          // Modo Sabio chakra
        backgroundColor = Color(0xFF140707),    // Aldea de la Hoja nocturna
        surfaceColor = Color(0xFF270F0F),       // Despacho del Hokage
        onSurfaceColor = Color(0xFFFFF1F2),     // Texto pergamino claro
        surfaceVariantColor = Color(0xFF3F1919),
        onSurfaceVariantColor = Color(0xFFFECDD3),
        outlineColor = Color(0xFF7A2929),
        isDark = true,
        fontFamily = FontFamily.SansSerif,
        fontName = "Sans Ninja",
        cardShape = RoundedCornerShape(16.dp),
        cardBorderWidth = 1.8.dp,
        themeTag = "HOKAGE",
        themeMotto = "Voluntad de Fuego • Jamás retroceder en mi palabra"
    ),

    ANIME_JUJUTSU_KAISEN(
        id = "anime_jujutsu_kaisen",
        displayName = "Jujutsu Kaisen: Expansión de Dominio",
        subtitle = "Azul hechicero oscuro, Carmesí Sukuna & energía maldita luminosa",
        category = ThemeCategory.ANIME,
        primaryColor = Color(0xFF818CF8),       // Púrpura infinito resplandeciente
        onPrimaryColor = Color(0xFF1E1B4B),
        secondaryColor = Color(0xFFDC2626),     // Carmesí del Rey Sukuna
        onSecondaryColor = Color.White,
        goldColor = Color(0xFFA5B4FC),          // Destello de energía
        backgroundColor = Color(0xFF080816),    // Vacío infinito
        surfaceColor = Color(0xFF12122A),       // Academia Metropolitana
        onSurfaceColor = Color(0xFFEEF2FF),     // Texto hechicero níveo
        surfaceVariantColor = Color(0xFF1E1E42),
        onSurfaceVariantColor = Color(0xFFC7D2FE),
        outlineColor = Color(0xFF34346B),
        isDark = true,
        fontFamily = FontFamily.SansSerif,
        fontName = "Sans Hechicero",
        cardShape = CutCornerShape(14.dp),
        cardBorderWidth = 1.8.dp,
        themeTag = "DOMINIO RYŌIKI",
        themeMotto = "Expansión de Dominio • Proteger a los inocentes con honor"
    );

    companion object {
        val HERALDICO = ESCUDO_BASILIANO
        val NOCTURNO_VIGILIA = ESCUDO_NOCTURNO

        fun findById(id: String): BasilianAppTheme {
            return values().firstOrNull { it.id.equals(id, ignoreCase = true) }
                ?: ESCUDO_BASILIANO
        }
    }
}

val BasilianAppTheme.navBarBgColor: Color
    get() = if (isDark) surfaceColor else Color(0xFFFAF8F5)

val BasilianAppTheme.accentRed: Color
    get() = secondaryColor

val BasilianAppTheme.screenBgColor: Color
    get() = backgroundColor
