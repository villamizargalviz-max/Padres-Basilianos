package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.components.FuturisticDesignPalette
import com.example.ui.components.futuristic3DCard
import com.example.ui.viewmodel.BasilianViewModel
import java.util.Calendar

data class DailySaintEntry(
    val month: Int, // 1..12
    val day: Int,   // 1..31
    val saintName: String,
    val liturgicalRank: String,
    val liturgicalColorName: String,
    val liturgicalColorHex: Color,
    val patronage: String,
    val biography: String,
    val spiritualQuote: String,
    val dailyPrayer: String,
    val isBasilianHighlight: Boolean = false
)

object SantoralCatolicoRepository {

    val monthNamesEs = listOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Déciciembre".replace("Déciciembre", "Diciembre")
    )

    val daysPerMonth = mapOf(
        1 to 31, 2 to 29, 3 to 31, 4 to 30, 5 to 31, 6 to 30,
        7 to 31, 8 to 31, 9 to 30, 10 to 31, 11 to 30, 12 to 31
    )

    val featuredSaints: List<DailySaintEntry> = listOf(
        // ENERO
        DailySaintEntry(
            month = 1, day = 1,
            saintName = "Santa María, Madre de Dios (Theotokos)",
            liturgicalRank = "Solemnidad • Patrona de la Congregación",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Madre de la Iglesia, Reina de la Paz y Protectora de la Congregación de San Basilio",
            biography = "En la octava de Navidad, la Iglesia celebra a María como verdadera Madre de Dios. San Basilio Magno predicó con ardor sobre la maternidad divina de María y los Padres Basilianos la honran bajo los títulos de la Asunción y Nuestra Señora del Perpetuo Socorro.",
            spiritualQuote = "«Dios envió a su Hijo, nacido de mujer, para que recibiéramos la adopción filial.» (Gálatas 4, 4)",
            dailyPrayer = "Santa María, Madre de Dios y Madre nuestra, bendice este nuevo año y guía a la Congregación de San Basilio en la bondad, la disciplina y la ciencia. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 1, day = 2,
            saintName = "San Basilio Magno y San Gregorio Nacianceno",
            liturgicalRank = "Fiesta Patronal C.S.B. • Obispos y Doctores de la Iglesia",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Patrono Principal de los Padres Basilianos (C.S.B.), Educadores Cristianos y Monjes de Oriente",
            biography = "Nacido en Cesarea de Capadocia (330–379), San Basilio Magno fue obispo, teólogo luminoso de la Santísima Trinidad y del Espíritu Santo, autor de las Reglas Monásticas, maestro de los jóvenes y fundador de la 'Basiliada' (ciudad de la misericordia para pobres y enfermos).",
            spiritualQuote = "«Como las abejas extraen de las flores la miel más pura, así el alma sabia recoge de toda enseñanza la verdad y la virtud.» — San Basilio Magno",
            dailyPrayer = "Santo padre Basilio, patrón de nuestra Congregación, que tiene como apostolado la enseñanza cristiana: sé nuestro modelo en la enseñanza y apoyo en nuestro ministerio. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 1, day = 24,
            saintName = "San Francisco de Sales",
            liturgicalRank = "Memoria Obligatoria • Obispo y Doctor de la Iglesia",
            liturgicalColorName = "Blanco",
            liturgicalColorHex = Color(0xFF00F0FF),
            patronage = "Escritores, Periodistas y Educadores de la Dulzura Cristiana",
            biography = "Obispo de Ginebra y autor de la 'Introducción a la vida devota'. Enseñó que la santidad florece en todas las vocaciones mediante la amabilidad y la caridad paciente.",
            spiritualQuote = "«Se atrapan más moscas con una gota de miel que con un barril de vinagre.»",
            dailyPrayer = "Señor, que hiciste a San Francisco de Sales todo para todos, concédenos servir a nuestros hermanos con la mansedumbre de tu amor. Amén."
        ),
        DailySaintEntry(
            month = 1, day = 28,
            saintName = "Santo Tomás de Aquino",
            liturgicalRank = "Memoria • Patrono de la Congregación C.S.B.",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Universidades, Colegios Católicos, Estudiantes y Teólogos (Patrono C.S.B.)",
            biography = "El Doctor Angélico (1225–1274), autor de la Suma Teológica, armonizó la fe y la razón mostrando que toda verdad procede del Espíritu Santo. Da nombre a universidades y colegios basilianos como la University of St. Thomas y St. Thomas High School en Houston.",
            spiritualQuote = "«Concede, Señor, agudeza para entender, capacidad para retener, método y facultad para aprender.»",
            dailyPrayer = "Oh Dios, que iluminaste a tu Iglesia con la sabiduría de Santo Tomás de Aquino, haz que busquemos siempre tu verdad con humildad y ciencia. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 1, day = 31,
            saintName = "San Juan Bosco",
            liturgicalRank = "Memoria • Patrono de la Congregación C.S.B.",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Padre y Maestro de la Juventud, Educadores y Aprendices (Patrono C.S.B.)",
            biography = "Sacerdote piamontés (1815–1888) que entregó su vida a los jóvenes mediante el Sistema Preventivo basado en la razón, la religión y el amor bondadoso. Es uno de los santos patronos oficiales de la Congregación de San Basilio.",
            spiritualQuote = "«La educación es cosa del corazón, y solo Dios es su dueño.»",
            dailyPrayer = "San Juan Bosco, amigo y maestro de los jóvenes, enséñanos a educar con alegría, paciencia y profunda bondad cristiana. Amén.",
            isBasilianHighlight = true
        ),

        // FEBRERO
        DailySaintEntry(
            month = 2, day = 2,
            saintName = "La Presentación del Señor (La Candelaria)",
            liturgicalRank = "Fiesta del Señor • Jornada de la Vida Consagrada",
            liturgicalColorName = "Blanco",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Religiosos y Vida Consagrada",
            biography = "Cristo es presentado en el Templo por María y José y reconocido por Simeón y Ana como 'Luz para alumbrar a las naciones'.",
            spiritualQuote = "«Mis ojos han visto a tu Salvador, luz para iluminar a las naciones.» (Lucas 2, 30-32)",
            dailyPrayer = "Señor Jesús, Luz verdadera, renueva la entrega de todos los consagrados y sacerdotes basilianos. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 2, day = 11,
            saintName = "Nuestra Señora de Lourdes",
            liturgicalRank = "Memoria • Jornada Mundial del Enfermo",
            liturgicalColorName = "Blanco",
            liturgicalColorHex = Color(0xFF00F0FF),
            patronage = "Enfermos, Médicos y Centros de Salud",
            biography = "Aparición de la Inmaculada Concepción a Santa Bernardita Soubirous en Francia en 1858, invitando a la oración, la conversión y el consuelo de los enfermos.",
            spiritualQuote = "«Yo soy la Inmaculada Concepción.»",
            dailyPrayer = "Madre de Lourdes, acompaña con ternura a los enfermos y bendice las obras médicas y sociales de nuestra comunidad. Amén."
        ),

        // MARZO
        DailySaintEntry(
            month = 3, day = 19,
            saintName = "San José, Esposo de la Santísima Virgen María",
            liturgicalRank = "Solemnidad • Patrono de la Congregación C.S.B.",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Iglesia Universal, Familias, Trabajadores y Congregación de San Basilio",
            biography = "Varón justo y custodio fiel del Redentor y de María en Nazaret. En su taller enseñó a Jesús la dignidad del trabajo humano y la obediencia amorosa al Padre.",
            spiritualQuote = "«José hizo como el ángel del Señor le había mandado.» (Mateo 1, 24)",
            dailyPrayer = "Glorioso Patriarca San José, custodio de nuestras casas y colegios basilianos, protégenos en la fe y el trabajo diario. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 3, day = 25,
            saintName = "La Anunciación del Señor",
            liturgicalRank = "Solemnidad del Señor",
            liturgicalColorName = "Blanco",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Encarnación del Verbo y Defensa de la Vida",
            biography = "El arcángel Gabriel anuncia a María el misterio de la Encarnación y con su 'Hágase' el Hijo de Dios asume nuestra naturaleza humana.",
            spiritualQuote = "«He aquí la esclava del Señor; hágase en mí según tu palabra.» (Lucas 1, 38)",
            dailyPrayer = "Señor, concédenos responder con la misma disponibilidad de María a tu llamado cada día. Amén."
        ),

        // ABRIL
        DailySaintEntry(
            month = 4, day = 25,
            saintName = "San Marcos Evangelista",
            liturgicalRank = "Fiesta",
            liturgicalColorName = "Rojo",
            liturgicalColorHex = Color(0xFFEF4444),
            patronage = "Evangelizadores, Intérpretes y Estudiantes de la Escritura",
            biography = "Discípulo de San Pedro y San Pablo, proclamó con viveza el Evangelio de Jesucristo, Hijo de Dios.",
            spiritualQuote = "«Id al mundo entero y proclamad el Evangelio a toda la creación.» (Marcos 16, 15)",
            dailyPrayer = "Dios nuestro, que llamaste a San Marcos a anunciar el Evangelio, haz que nuestra vida dé testimonio de Cristo. Amén."
        ),

        // MAYO
        DailySaintEntry(
            month = 5, day = 14,
            saintName = "San Matías Apóstol",
            liturgicalRank = "Fiesta",
            liturgicalColorName = "Rojo",
            liturgicalColorHex = Color(0xFFEF4444),
            patronage = "Vocaciones Apostólicas y Perseverancia",
            biography = "Elegido por los Apóstoles para completar el colegio de los Doce como testigo de la Resurrección de Cristo.",
            spiritualQuote = "«No sois vosotros los que me habéis elegido, soy yo quien os he elegido.» (Juan 15, 16)",
            dailyPrayer = "Señor, suscita abundantes vocaciones sacerdotales y religiosas para tu Iglesia y la Congregación de San Basilio. Amén."
        ),
        DailySaintEntry(
            month = 5, day = 31,
            saintName = "La Visitación de la Santísima Virgen María",
            liturgicalRank = "Fiesta Mariana",
            liturgicalColorName = "Blanco",
            liturgicalColorHex = Color(0xFF00F0FF),
            patronage = "Misioneros, Caridad Fraterna y Servicio al Prójimo",
            biography = "María se pone en camino con prontitud hacia las montañas de Judea para servir a su prima Isabel y entona el cántico del Magnificat.",
            spiritualQuote = "«Proclama mi alma la grandeza del Señor, se alegra mi espíritu en Dios, mi salvador.» (Lucas 1, 46-47)",
            dailyPrayer = "Virgen de la Visitación, enséñanos a salir al encuentro de quienes más necesitan consuelo y educación. Amén."
        ),

        // JUNIO
        DailySaintEntry(
            month = 6, day = 14,
            saintName = "Conmemoración Oriental de San Basilio Magno",
            liturgicalRank = "Memoria Histórica de su Ordenación Episcopal",
            liturgicalColorName = "Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Unidad de la Iglesia, Caridad Social y Enseñanza Cristiana",
            biography = "Tradicional conmemoración de la consagración episcopal de San Basilio Magno en Cesarea de Capadocia en el año 370.",
            spiritualQuote = "«El pan que tú retienes le pertenece al hambriento; el manto que guardas en tu arca le pertenece al desnudo.» — San Basilio Magno",
            dailyPrayer = "Señor, enciende en nosotros el celo pastoral y la caridad ardiente de nuestro padre San Basilio. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 6, day = 27,
            saintName = "Nuestra Señora del Perpetuo Socorro",
            liturgicalRank = "Fiesta Mariana Basiliana",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Protectora Especial de las Comunidades y Colegios Basilianos",
            biography = "Advocación mariana bizantina profundamente venerada en las casas, parroquias y colegios de la Congregación de San Basilio.",
            spiritualQuote = "«Madre del Perpetuo Socorro, no nos desampares en nuestras pruebas y trabajos.»",
            dailyPrayer = "Madre del Perpetuo Socorro, mira con ternura a todos los estudiantes, familias y religiosos de nuestra familia basiliana. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 6, day = 29,
            saintName = "San Pedro y San Pablo, Apóstoles",
            liturgicalRank = "Solemnidad",
            liturgicalColorName = "Rojo",
            liturgicalColorHex = Color(0xFFEF4444),
            patronage = "Columnas de la Iglesia Universal y del Ministerio Apostólico",
            biography = "Pedro, roca de la fe de la Iglesia, y Pablo, Apóstol de las naciones, entregaron su vida en Roma por amor a Jesucristo.",
            spiritualQuote = "«He combatido bien mi combate, he corrido hasta la meta, he mantenido la fe.» (2 Timoteo 4, 7)",
            dailyPrayer = "Dios nuestro, guarda a tu Iglesia firme sobre el cimiento de los Apóstoles Pedro y Pablo. Amén."
        ),

        // JULIO
        DailySaintEntry(
            month = 7, day = 9,
            saintName = "Nuestra Señora del Rosario de Chiquinquirá",
            liturgicalRank = "Solemnidad • Reina y Patrona de Colombia",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Patrona Principal de Colombia y de la Paz Nacional",
            biography = "El milagroso lienzo renovado en Chiquinquirá en 1586 acompaña la fe del pueblo colombiano y las misiones basilianas en Cali, Medellín, Bogotá y Riohacha.",
            spiritualQuote = "«Haced todo lo que Él os diga.» (Juan 2, 5)",
            dailyPrayer = "Virgen Santísima de Chiquinquirá, Reina de Colombia, bendice nuestras parroquias, colegios y familias con la paz de tu Hijo. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 7, day = 19,
            saintName = "Santa Macrina la Joven (Hermana de San Basilio Magno)",
            liturgicalRank = "Memoria Familiar Basiliana",
            liturgicalColorName = "Blanco",
            liturgicalColorHex = Color(0xFF00F0FF),
            patronage = "Vida Monástica Femenina, Educadoras y Familia de San Basilio",
            biography = "Hermana mayor y maestra espiritual de San Basilio Magno y San Gregorio de Nisa en el Ponto. Su sabiduría y vida ascética inspiraron las reglas monásticas de Basilio.",
            spiritualQuote = "«Tú nos liberaste del temor de la muerte y transformaste nuestro fin en el principio de la verdadera vida.» — Oración de Santa Macrina",
            dailyPrayer = "Señor, por intercesión de Santa Macrina, bendice a las mujeres educadoras y consagradas que edifican tu Reino. Amén.",
            isBasilianHighlight = true
        ),

        // AGOSTO
        DailySaintEntry(
            month = 8, day = 15,
            saintName = "La Asunción de la Santísima Virgen María",
            liturgicalRank = "Solemnidad Patronal • Aniversario de los Basilianos en Colombia (1987)",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Titular de la Primera Parroquia y Colegio Basiliano en Colombia (INSA Cali)",
            biography = "María es llevada en cuerpo y alma a la gloria del cielo. El 15 de agosto de 1987 los Padres Basilianos iniciaron oficialmente su misión en Colombia en la Parroquia e Instituto Nuestra Señora de la Asunción en Cali.",
            spiritualQuote = "«El Poderoso ha hecho obras grandes por mí; su nombre es santo.» (Lucas 1, 49)",
            dailyPrayer = "Nuestra Señora de la Asunción, bendice la misión de la Congregación de San Basilio en Colombia y en todo el mundo. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 8, day = 28,
            saintName = "San Agustín de Hipona",
            liturgicalRank = "Memoria • Obispo y Doctor de la Iglesia",
            liturgicalColorName = "Blanco",
            liturgicalColorHex = Color(0xFF00F0FF),
            patronage = "Buscadores de la Verdad, Filósofos y Comunidades Religiosas",
            biography = "Obispo de Hipona y autor de las 'Confesiones' y 'La Ciudad de Dios'. Su búsqueda apasionada de la Verdad ilumina toda pedagogía cristiana.",
            spiritualQuote = "«Nos hiciste, Señor, para ti, y nuestro corazón está inquieto hasta que descanse en ti.»",
            dailyPrayer = "Renueva en tu Iglesia, Señor, el espíritu de verdad y caridad que infundiste en San Agustín. Amén."
        ),

        // SEPTIEMBRE
        DailySaintEntry(
            month = 9, day = 9,
            saintName = "San Pedro Claver",
            liturgicalRank = "Solemnidad en Colombia • Patrono de los Derechos Humanos",
            liturgicalColorName = "Blanco",
            liturgicalColorHex = Color(0xFF10B981),
            patronage = "Defensor de los Esclavos, Afrodescendientes y Derechos Humanos en Colombia",
            biography = "Misionero en Cartagena de Indias que se declaró 'esclavo de los esclavos para siempre', curando heridas, bautizando y defendiendo la dignidad de cada ser humano.",
            spiritualQuote = "«Primero hay que hablarles con las manos, dándoles remedio y alimento, antes de hablarles con los labios.»",
            dailyPrayer = "Señor, danos el corazón compasivo y valiente de San Pedro Claver para servir a los más vulnerables. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 9, day = 29,
            saintName = "Santos Arcángeles Miguel, Gabriel y Rafael",
            liturgicalRank = "Fiesta • Patrono de St. Michael's College (Toronto)",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "St. Michael's College School y University of St. Michael's College (Toronto)",
            biography = "San Miguel Arcángel ('¿Quién como Dios?') es el patrono histórico de St. Michael's College en Toronto, fundado por los Padres Basilianos en 1852.",
            spiritualQuote = "«¿Quién como Dios? Fortaleza, medicina y anuncio de salvación.»",
            dailyPrayer = "San Miguel, San Gabriel y San Rafael, proteged nuestras escuelas, parroquias y familias en el camino del bien. Amén.",
            isBasilianHighlight = true
        ),

        // OCTUBRE
        DailySaintEntry(
            month = 10, day = 1,
            saintName = "Santa Teresita del Niño Jesús",
            liturgicalRank = "Memoria • Doctora de la Iglesia y Patrona de las Misiones",
            liturgicalColorName = "Blanco",
            liturgicalColorHex = Color(0xFF00F0FF),
            patronage = "Misiones, Vocaciones y el Caminito de la Confianza en Dios",
            biography = "Carmelita de Lisieux (1873–1897) que descubrió que en el corazón de la Iglesia su vocación era el Amor, santificando lo cotidiano con sencillez.",
            spiritualQuote = "«En el corazón de la Iglesia, mi Madre, yo seré el Amor.»",
            dailyPrayer = "Santa Teresita, enséñanos a realizar cada tarea educativa y fraterna con un amor inmenso. Amén."
        ),
        DailySaintEntry(
            month = 10, day = 4,
            saintName = "San Francisco de Asís",
            liturgicalRank = "Memoria • Patrono de la Congregación C.S.B.",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Pobreza Evangélica, Fraternidad Universal, Ecología y Patrono C.S.B.",
            biography = "El Pobrecillo de Asís (1182–1226) renovó la Iglesia con su alegría evangélica, fraternidad y amor por los pobres. Es uno de los santos patronos oficiales de los Padres Basilianos.",
            spiritualQuote = "«Señor, haz de mí un instrumento de tu paz.»",
            dailyPrayer = "San Francisco de Asís, intercede por nosotros para que vivamos en alegre fraternidad, sencillez y servicio a los pobres. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 10, day = 9,
            saintName = "San Luis Beltrán",
            liturgicalRank = "Fiesta en Colombia • Misionero en el Caribe Colombiano",
            liturgicalColorName = "Blanco",
            liturgicalColorHex = Color(0xFF10B981),
            patronage = "Patrono Secundario de Colombia y Misionero de la Costa Caribe",
            biography = "Predicador dominico que evangelizó incansablemente el Caribe colombiano y defendió a los pueblos indígenas.",
            spiritualQuote = "«La caridad de Cristo nos apremia a anunciar su Evangelio con obras de justicia.»",
            dailyPrayer = "Señor, por intercesión de San Luis Beltrán, bendice nuestras misiones educativas y pastorales en Colombia. Amén."
        ),
        DailySaintEntry(
            month = 10, day = 21,
            saintName = "Santa Laura Montoya Upegui (Santa Laura de Jesús)",
            liturgicalRank = "Fiesta en Colombia • Primera Santa Colombiana y Educadora",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Maestros, Educadores Católicos, Misioneros y Pueblos Indígenas de Colombia",
            biography = "Nacida en Jericó, Antioquia (1874–1949), fue maestra normalista, pedagoga brillante y fundadora de las Misioneras de María Inmaculada y Santa Catalina de Sena. Entregó su vida a la educación y evangelización de las comunidades indígenas y marginadas con su 'Pedagogía del Amor'.",
            spiritualQuote = "«¡Tengo sed de hacer conocer a Dios a todos los que no lo conocen! Educar amando y amar educando.» — Santa Laura Montoya",
            dailyPrayer = "Santa Laura Montoya, maestra y misionera colombiana, inspira a todos los educadores de la Red Basiliana para enseñar siempre con bondad, disciplina y ciencia. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 10, day = 22,
            saintName = "San Juan Pablo II, Papa",
            liturgicalRank = "Memoria • Impulsor de la Nueva Evangelización y de las Universidades Católicas",
            liturgicalColorName = "Blanco",
            liturgicalColorHex = Color(0xFF00F0FF),
            patronage = "Jóvenes, Familias y Educación Superior Católica (Ex Corde Ecclesiae)",
            biography = "El Papa peregrino (1920–2005) visitó la Universidad de St. Michael's College y promovió las Jornadas Mundiales de la Juventud y la Nueva Evangelización que inspira a la red BNENE.",
            spiritualQuote = "«¡No tengáis miedo! ¡Abrid de par en par las puertas a Cristo!»",
            dailyPrayer = "San Juan Pablo II, bendice a los jóvenes de nuestros colegios y universidades para que sean testigos valientes del Evangelio. Amén.",
            isBasilianHighlight = true
        ),

        // NOVIEMBRE
        DailySaintEntry(
            month = 11, day = 1,
            saintName = "Solemnidad de Todos los Santos",
            liturgicalRank = "Solemnidad de Toda la Iglesia",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Vocación Universal a la Santidad",
            biography = "La Iglesia celebra en una única fiesta gozosa a todos los santos canonizados y a la multitud inmensa de hermanos que ya gozan de la presencia de Dios.",
            spiritualQuote = "«Bienaventurados los limpios de corazón, porque ellos verán a Dios.» (Mateo 5, 8)",
            dailyPrayer = "Dios todopoderoso, concédenos caminar con alegría hacia la santidad junto con todos tus santos patronos. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 11, day = 21,
            saintName = "Presentación de la Virgen María • Fundación de la Congregación (Annonay 1822)",
            liturgicalRank = "Aniversario Fundacional de los Padres Basilianos C.S.B.",
            liturgicalColorName = "Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Fundación de la Congregación de San Basilio (21 de noviembre de 1822)",
            biography = "En la fiesta de la Presentación de la Virgen María, el 21 de noviembre de 1822, los diez sacerdotes profesores de Annonay (Francia) sellaron el pacto fundacional de la Congregación de San Basilio bajo la protección de San Basilio Magno.",
            spiritualQuote = "«Bonitatem et disciplinam et scientiam doce me — Enséñame la bondad, la disciplina y la ciencia.»",
            dailyPrayer = "Te damos gracias, Señor, por los más de 200 años de historia de la Congregación de San Basilio; bendice a nuestros sacerdotes, escolásticos, novicios y educadores. Amén.",
            isBasilianHighlight = true
        ),

        // DICIEMBRE
        DailySaintEntry(
            month = 12, day = 8,
            saintName = "La Inmaculada Concepción de la Santísima Virgen María",
            liturgicalRank = "Solemnidad Mariana",
            liturgicalColorName = "Blanco / Azul",
            liturgicalColorHex = Color(0xFF00F0FF),
            patronage = "Pureza de Corazón, Seminarios y Familias Cristianas",
            biography = "María, preservada de toda mancha de pecado original desde el primer instante de su concepción, brilla como modelo de santidad y entrega total a Dios.",
            spiritualQuote = "«Alégrate, llena de gracia, el Señor está contigo.» (Lucas 1, 28)",
            dailyPrayer = "Inmaculada Virgen María, conserva puros nuestros corazones y guía nuestros pasos hacia Jesús. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 12, day = 12,
            saintName = "Nuestra Señora de Guadalupe, Emperatriz de América",
            liturgicalRank = "Fiesta Continental • Patrona de América Latina y Misiones de México",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "México, Colombia, Toda América y el Apostolado Basiliano en Latinoamérica",
            biography = "En el Tepeyac (1531), la Virgen Morena se manifestó a San Juan Diego como Madre compasiva de todos los pueblos de América, acompañando de modo especial la misión basiliana en México y Colombia.",
            spiritualQuote = "«¿No estoy yo aquí, que soy tu Madre? ¿No estás bajo mi sombra y resguardo?»",
            dailyPrayer = "Virgen Santísima de Guadalupe, bendice a nuestras comunidades en México, Colombia y todo el continente americano. Amén.",
            isBasilianHighlight = true
        ),
        DailySaintEntry(
            month = 12, day = 25,
            saintName = "La Natividad de Nuestro Señor Jesucristo (Navidad)",
            liturgicalRank = "Solemnidad del Señor",
            liturgicalColorName = "Blanco / Dorado",
            liturgicalColorHex = Color(0xFFFFD700),
            patronage = "Salvador del Mundo, Príncipe de la Paz y Maestro Divino",
            biography = "El Verbo eterno de Dios nace en Belén para iluminar a la humanidad. San Basilio Magno llamó a la Navidad 'la fiesta de la regeneración de toda la creación'.",
            spiritualQuote = "«Y el Verbo se hizo carne y habitó entre nosotros.» (Juan 1, 14)",
            dailyPrayer = "Señor Jesús, Niño de Belén, nace hoy en cada hogar, escuela y parroquia de nuestra familia basiliana. Amén.",
            isBasilianHighlight = true
        )
    )

    // Nombres de santos para cubrir los 365 días del año cuando no coincide con una solemnidad destacada
    private val dailyCommemorationCycle = listOf(
        "San Gregorio de Nisa, Obispo y Hermano de San Basilio" to "Maestro de la vida espiritual y contemplativa en Capadocia.",
        "San Juan Crisóstomo, Obispo y Doctor de la Iglesia" to "Gran amigo de juventud de San Basilio Magno y predicador de la justicia social.",
        "Santa Emilia de Cesarea y San Basilio el Viejo" to "Padres santos de San Basilio Magno, Santa Macrina y San Gregorio de Nisa; modelo de iglesia doméstica.",
        "San Pedro de Sebaste, Obispo" to "Hermano menor de San Basilio Magno, médico de almas y cuerpos en Armenia.",
        "San Lorenzo Diácono y Mártir" to "Patrono de la Parroquia Basiliana en Tehuacán (México); reconoció en los pobres el verdadero tesoro de la Iglesia.",
        "Santa Ana, Madre de la Virgen María" to "Patrona de comunidades y parroquias basilianas en Norteamérica; educadora en la fe.",
        "San Atanasio de Alejandría, Obispo y Doctor" to "Defensor intrépido de la divinidad de Cristo en comunión con San Basilio Magno.",
        "San Martín de Porres, Religioso" to "Testigo luminoso de humildad, caridad con los enfermos y fraternidad en América Latina.",
        "Santa Rosa de Lima, Virgen" to "Primera flor de santidad en América, modelo de oración ardiente y compasión.",
        "San Ignacio de Loyola, Sacerdote" to "Maestro del discernimiento espiritual y de la educación para la mayor gloria de Dios.",
        "San Felipe Neri, Sacerdote" to "El santo de la alegría cristiana y formador de la juventud; titular de la Casa San Felipe en México.",
        "San Carlos Borromeo, Obispo" to "Reformador de seminarios, escuelas de doctrina cristiana y servidor heroico de los enfermos.",
        "Santa Catalina de Siena, Virgen y Doctora" to "Consejera de paz y unidad en la Iglesia, enamorada de la Verdad.",
        "San Antonio de Padua, Doctor Evangélico" to "Predicador insigne de la Palabra y amigo de los pobres.",
        "San Benito Abad, Patriarca del Monacato Occidental" to "Heredero en Occidente de la Regla de nuestro padre San Basilio Magno."
    )

    fun getSaintForDate(month: Int, day: Int): DailySaintEntry {
        val exact = featuredSaints.find { it.month == month && it.day == day }
        if (exact != null) return exact

        val idx = ((month * 31) + day) % dailyCommemorationCycle.size
        val (name, bio) = dailyCommemorationCycle[idx]
        return DailySaintEntry(
            month = month,
            day = day,
            saintName = "$name (Santoral del $day de ${monthNamesEs.getOrElse(month - 1) { "Mes" }})",
            liturgicalRank = "Memoria del Santoral Católico y Tradición Basiliana",
            liturgicalColorName = "Verde / Blanco",
            liturgicalColorHex = Color(0xFF10B981),
            patronage = "Vida Cristiana, Educación en la Fe y Fraternidad",
            biography = "$bio En este día $day de ${monthNamesEs.getOrElse(month - 1) { "" }}, la Iglesia y la familia de San Basilio recuerdan su testimonio de fidelidad al Evangelio, oración perseverante y servicio generoso al prójimo.",
            spiritualQuote = "«Enséñame la bondad, la disciplina y la ciencia, porque creo en tus mandamientos.» (Salmo 118, 66)",
            dailyPrayer = "Señor Dios nuestro, que en tus santos nos ofreces ejemplos luminosos de fe y caridad, concédenos hoy imitar las virtudes de $name en nuestra misión cotidiana. Amén.",
            isBasilianHighlight = false
        )
    }
}

@Composable
fun SantoralDiarioScreen(
    viewModel: BasilianViewModel,
    appLanguage: AppLanguage = AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val todayCal = remember { Calendar.getInstance() }
    val todayMonth = remember { todayCal.get(Calendar.MONTH) + 1 }
    val todayDay = remember { todayCal.get(Calendar.DAY_OF_MONTH) }

    var selectedMonth by remember { mutableIntStateOf(todayMonth) }
    var selectedDay by remember { mutableIntStateOf(todayDay) }
    var searchQuery by remember { mutableStateOf("") }

    val isSpeaking by viewModel.isTtsSpeaking.collectAsState()

    val maxDaysInSelectedMonth = SantoralCatolicoRepository.daysPerMonth[selectedMonth] ?: 31
    LaunchedEffect(selectedMonth) {
        if (selectedDay > maxDaysInSelectedMonth) {
            selectedDay = maxDaysInSelectedMonth
        }
    }

    val currentSaint = remember(selectedMonth, selectedDay) {
        SantoralCatolicoRepository.getSaintForDate(selectedMonth, selectedDay)
    }

    val filteredSaintsList = remember(selectedMonth, searchQuery) {
        if (searchQuery.isNotBlank()) {
            SantoralCatolicoRepository.featuredSaints.filter {
                it.saintName.contains(searchQuery, ignoreCase = true) ||
                    it.patronage.contains(searchQuery, ignoreCase = true) ||
                    it.biography.contains(searchQuery, ignoreCase = true)
            }
        } else {
            SantoralCatolicoRepository.featuredSaints.filter { it.month == selectedMonth }
        }
    }

    fun stepDay(delta: Int) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.MONTH, selectedMonth - 1)
            set(Calendar.DAY_OF_MONTH, selectedDay)
            add(Calendar.DAY_OF_MONTH, delta)
        }
        selectedMonth = cal.get(Calendar.MONTH) + 1
        selectedDay = cal.get(Calendar.DAY_OF_MONTH)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("santoral_diario_screen"),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Barra de Navegación Rápida de Fecha (◀ Día Anterior | HOY | Día Siguiente ▶)
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFF06152D),
                border = BorderStroke(1.2.dp, FuturisticDesignPalette.NeonGold.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { stepDay(-1) },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Día anterior",
                                tint = FuturisticDesignPalette.NeonCyan
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (selectedMonth == todayMonth && selectedDay == todayDay) {
                                    "✨ SANTO DE HOY • ${selectedDay} DE ${SantoralCatolicoRepository.monthNamesEs[selectedMonth - 1].uppercase()}"
                                } else {
                                    "📅 SANTORAL DEL ${selectedDay} DE ${SantoralCatolicoRepository.monthNamesEs[selectedMonth - 1].uppercase()}"
                                },
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = FuturisticDesignPalette.NeonGold,
                                letterSpacing = 0.7.sp
                            )
                            if (selectedMonth != todayMonth || selectedDay != todayDay) {
                                TextButton(
                                    onClick = {
                                        selectedMonth = todayMonth
                                        selectedDay = todayDay
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                    modifier = Modifier.height(24.dp)
                                ) {
                                    Text(
                                        text = "Volver al Santo de Hoy ($todayDay ${SantoralCatolicoRepository.monthNamesEs[todayMonth - 1]})",
                                        fontSize = 10.5.sp,
                                        color = FuturisticDesignPalette.NeonCyan,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = { stepDay(1) },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Día siguiente",
                                tint = FuturisticDesignPalette.NeonCyan
                            )
                        }
                    }

                    // Selector de Mes
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(12) { idx ->
                            val m = idx + 1
                            val isSelected = selectedMonth == m
                            Surface(
                                onClick = { selectedMonth = m },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) FuturisticDesignPalette.NeonCyan.copy(alpha = 0.22f) else Color(0xFF0A1D3B),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) FuturisticDesignPalette.NeonCyan else Color.White.copy(alpha = 0.15f)
                                )
                            ) {
                                Text(
                                    text = SantoralCatolicoRepository.monthNamesEs[idx],
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                    color = if (isSelected) FuturisticDesignPalette.NeonCyan else Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }

                    // Selector de Día (1..31)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(maxDaysInSelectedMonth) { idx ->
                            val d = idx + 1
                            val isSelected = selectedDay == d
                            val hasHighlight = SantoralCatolicoRepository.featuredSaints.any {
                                it.month == selectedMonth && it.day == d
                            }
                            Surface(
                                onClick = { selectedDay = d },
                                shape = CircleShape,
                                color = when {
                                    isSelected -> FuturisticDesignPalette.NeonGold
                                    hasHighlight -> FuturisticDesignPalette.NeonGold.copy(alpha = 0.18f)
                                    else -> Color(0xFF0A1D3B)
                                },
                                border = BorderStroke(
                                    1.dp,
                                    when {
                                        isSelected -> Color.White
                                        hasHighlight -> FuturisticDesignPalette.NeonGold.copy(alpha = 0.7f)
                                        else -> Color.White.copy(alpha = 0.15f)
                                    }
                                ),
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "$d",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSelected) Color(0xFF020814) else Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Tarjeta Holográfica 3D Principal del Santo del Día Seleccionado
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .futuristic3DCard(
                        accentColor = currentSaint.liturgicalColorHex,
                        secondaryColor = FuturisticDesignPalette.NeonCyan,
                        cornerRadius = 22.dp,
                        tiltDegrees = 1.8f
                    )
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF071A38),
                                Color(0xFF030C1C)
                            )
                        )
                    )
                    .padding(18.dp)
                    .testTag("daily_saint_spotlight_card")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = currentSaint.liturgicalColorHex.copy(alpha = 0.18f),
                            border = BorderStroke(1.dp, currentSaint.liturgicalColorHex)
                        ) {
                            Text(
                                text = "🌟 ${currentSaint.liturgicalRank}",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = currentSaint.liturgicalColorHex,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color.White.copy(alpha = 0.08f)
                        ) {
                            Text(
                                text = "Color: ${currentSaint.liturgicalColorName}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE2E8F0),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = currentSaint.saintName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Text(
                        text = "🛡️ Patronazgo: ${currentSaint.patronage}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = FuturisticDesignPalette.NeonCyan
                    )

                    Text(
                        text = currentSaint.biography,
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = Color(0xFFE2E8F0)
                    )

                    // Cita espiritual del Santo
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = FuturisticDesignPalette.NeonGold.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, FuturisticDesignPalette.NeonGold.copy(alpha = 0.55f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "💬 PALABRA E INSPIRACIÓN DEL SANTO",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = FuturisticDesignPalette.NeonGold,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = currentSaint.spiritualQuote,
                                fontSize = 12.5.sp,
                                fontStyle = FontStyle.Italic,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    // Oración del día al Santo
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = FuturisticDesignPalette.NeonCyan.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, FuturisticDesignPalette.NeonCyan.copy(alpha = 0.45f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "🙏 ORACIÓN AL SANTO DE ESTE DÍA",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = FuturisticDesignPalette.NeonCyan,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = currentSaint.dailyPrayer,
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = Color(0xFFE2E8F0)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (isSpeaking) {
                                viewModel.stopSpeaking()
                            } else {
                                viewModel.speakText(
                                    "Santoral del día ${currentSaint.day} de ${SantoralCatolicoRepository.monthNamesEs[currentSaint.month - 1]}. " +
                                        "${currentSaint.saintName}. ${currentSaint.biography} " +
                                        "Oración del día: ${currentSaint.dailyPrayer}"
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FuturisticDesignPalette.NeonGold,
                            contentColor = Color(0xFF020814)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSpeaking) "Detener Lectura" else "Escuchar Vida y Oración del Santo del Día",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        // 3. Buscador y Festividades Destacadas del Mes
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        "Buscar santo o patrono (ej. Basilio, Laura, Tomás, José...)",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = FuturisticDesignPalette.NeonCyan)
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar", tint = Color.White)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FuturisticDesignPalette.NeonCyan,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text(
                text = if (searchQuery.isNotBlank()) {
                    "🔍 RESULTADOS EN EL SANTORAL (${filteredSaintsList.size})"
                } else {
                    "🌟 FESTIVIDADES Y SANTOS DESTACADOS DE ${SantoralCatolicoRepository.monthNamesEs[selectedMonth - 1].uppercase()}"
                },
                fontSize = 11.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = FuturisticDesignPalette.NeonGold,
                letterSpacing = 0.7.sp
            )
        }

        items(filteredSaintsList, key = { "${it.month}_${it.day}_${it.saintName}" }) { entry ->
            Surface(
                onClick = {
                    selectedMonth = entry.month
                    selectedDay = entry.day
                },
                shape = RoundedCornerShape(16.dp),
                color = if (entry.month == selectedMonth && entry.day == selectedDay) Color(0xFF0E2952) else Color(0xFF061326),
                border = BorderStroke(
                    width = 1.2.dp,
                    color = if (entry.isBasilianHighlight) FuturisticDesignPalette.NeonGold.copy(alpha = 0.65f) else FuturisticDesignPalette.NeonCyan.copy(alpha = 0.35f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = entry.liturgicalColorHex.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, entry.liturgicalColorHex),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${entry.day}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = SantoralCatolicoRepository.monthNamesEs[entry.month - 1].take(3).uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = entry.liturgicalColorHex
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = entry.saintName,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = entry.liturgicalRank,
                            fontSize = 11.sp,
                            color = FuturisticDesignPalette.NeonGold,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = entry.patronage,
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
