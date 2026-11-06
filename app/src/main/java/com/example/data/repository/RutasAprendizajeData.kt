package com.example.data.repository

enum class PerfilFormacion(val id: String, val label: String, val description: String) {
    ASPIRANTE("aspirante", "Joven Vocacional", "Primer contacto con el carisma de Annonay"),
    ASOCIADO("asociado", "Asociado Basiliano (S5)", "Discernimiento y vida comunitaria previa al noviciado"),
    NOVICIO("novicio", "Novicio en Houston (S6)", "Año canónico intensivo de oración, ascética y regla"),
    ESCOLASTICO("escolastico", "Escolástico en Teología", "Estudios filosófico-teológicos y misión pastoral"),
    ADULTO("adulto", "Formación Permanente", "Sacerdotes y laicos comprometidos en misión")
}

enum class RitmoNomada(val id: String, val label: String, val durationMinutes: Int, val description: String) {
    AGIL("agil", "Paso Ligero Nómada", 7, "Píldoras breves de sabiduría ascética para el día"),
    SERENO("sereno", "Caminata Contemplativa", 15, "Lectura reposada, análisis bíblico y silencio orante"),
    PROFUNDO("profundo", "Retiro del Desierto", 30, "Estudio teológico, discernimiento y meta-aprendizaje")
}

data class ModuloAprendizaje(
    val id: String,
    val eje: String, // "Psicología Espiritual", "Virtudes Basilianas", "Ascética y Oración Nómada"
    val titulo: String,
    val subtitulo: String,
    val citasFuente: String,
    val contenidoTeorico: String,
    val aplicacionPractica: String,
    val preguntasMetaAprendizaje: List<String>
)

object RutasAprendizajeData {
    val modulos: List<ModuloAprendizaje> = listOf(
        ModuloAprendizaje(
            id = "psi_1",
            eje = "Psicología Espiritual",
            titulo = "Autoconocimiento y la Ilusión del Ego",
            subtitulo = "Mirar el alma a la luz de la verdad de Dios sin máscaras",
            citasFuente = "San Basilio Magno: «Homilía sobre el 'Atiende a ti mismo'» (Dt 15,9)",
            contenidoTeorico = "San Basilio enseña que la mayor causa de caída espiritual no son los enemigos externos, sino la falta de vigilancia sobre el propio mundo interior. El 'Atiende a ti mismo' no es egocentrismo, sino la toma de conciencia compasiva de nuestras flaquezas, impulsos y motivaciones secretas. La psicología espiritual basiliana une la gracia de Dios con la madurez psicológica: no podemos consagrar a Dios lo que nos empeñamos en ocultar o negar de nosotros mismos.",
            aplicacionPractica = "Dedica 5 minutos antes de dormir a examinar qué emoción dominó tu jornada (miedo, orgullo, alegría, resentimiento) y ofrécela a Cristo sin justificarte.",
            preguntasMetaAprendizaje = listOf(
                "¿Qué verdad incómoda sobre mis reacciones descubrí hoy en mis relaciones comunitarias?",
                "¿En qué momentos actué buscando la aprobación de otros antes que la voluntad del Señor?",
                "¿Cómo puedo integrar mi fragilidad humana con mi vocación de servicio?"
            )
        ),
        ModuloAprendizaje(
            id = "psi_2",
            eje = "Psicología Espiritual",
            titulo = "Integración Afectiva y Castidad Célibe",
            subtitulo = "Un corazón indiviso consagrado a la libertad para amar (FVB #9-13)",
            citasFuente = "Forma de Vida Basiliana arts. 9-13; Benedicto XVI Deus Caritas Est",
            contenidoTeorico = "La castidad consagrada en la tradición basiliana nunca es represión estéril, sino canalización fecunda del amor afectivo hacia la fraternidad y el apostolado. La madurez psicosexual exige honestidad sobre la soledad, el anhelo de intimidad y el cuidado de los límites ministeriales. En comunidad encontramos el eco del amor fraterno que cura el individualismo y nos previene de compensaciones afectivas desordenadas.",
            aplicacionPractica = "Reconoce hoy tus momentos de vulnerabilidad o cansancio; en lugar de aislarte, comparte un momento sincero con un hermano o director espiritual.",
            preguntasMetaAprendizaje = listOf(
                "¿De qué manera vivo mi celibato: como una ausencia dolorosa o como un don desbordante para servir?",
                "¿He mantenido límites saludables y transparentes en mis interacciones cotidianas?",
                "¿A quién acudo cuando experimento soledad o sequedad afectiva?"
            )
        ),
        ModuloAprendizaje(
            id = "virtud_1",
            eje = "Virtudes Basilianas",
            titulo = "La Caridad Activa: La Regla de las Reglas",
            subtitulo = "«Si vives solo, ¿a quién le lavarás los pies?»",
            citasFuente = "San Basilio: Reglas Mayores (Pregunta 7)",
            contenidoTeorico = "Frente a los anacoretas solitarios del desierto, San Basilio defendió con vehemencia la vida cenobítica y fraterna. Preguntaba con audacia: 'Si vives solo, ¿de quién serás servidor? ¿A quién lavarás los pies? ¿Quién corregirá tus faltas?'. Para los Padres Basilianos de 1822 y hoy, la caridad no es una teoría: se encarna en la docencia con jóvenes necesitados, la ayuda a ancianos desvalidos y la vida en mesa común.",
            aplicacionPractica = "Realiza hoy un acto de servicio concreto y discreto hacia la persona más difícil o postergada de tu entorno.",
            preguntasMetaAprendizaje = listOf(
                "¿Cómo se traduce mi fe en obras tangibles de caridad y paciencia hacia quienes me rodean?",
                "¿En qué medida el carisma educador basiliano inspira mi forma de escuchar y enseñar?",
                "¿Qué resistencia interior experimento cuando debo ceder en mi voluntad por el bien fraterno?"
            )
        ),
        ModuloAprendizaje(
            id = "virtud_2",
            eje = "Virtudes Basilianas",
            titulo = "Pobreza Evangélica y Sobriedad Comunitaria",
            subtitulo = "Tener en común lo necesario y dar a los pobres lo superfluo",
            citasFuente = "Regla de San Basilio; Costumbres Basilianas 2022 (Cap. Vida Económica)",
            contenidoTeorico = "En la Basiliada de Cesarea, los basilianos fundaron el primer complejo hospitalario, albergue de peregrinos y escuela técnica de la historia de la Iglesia. Para Basilio, 'el pan que tienes guardado pertenece al hambriento; el abrigo en tu armario, al que está desnudo'. El voto de pobreza es comunión de bienes y desapego generoso de la tecnología y el consumismo.",
            aplicacionPractica = "Revisa tus pertenencias y dispositivos electrónicos; despréndete de algo útil para donarlo o moderar el tiempo invertido en comodidades.",
            preguntasMetaAprendizaje = listOf(
                "¿Mi estilo de vida refleja la austeridad y sencillez del Evangelio?",
                "¿Comparto con libertad mis recursos, talentos y tiempo con los demás?",
                "¿Cómo puedo evitar caer en el apego a la comodidad material?"
            )
        ),
        ModuloAprendizaje(
            id = "ascetica_1",
            eje = "Ascética y Oración Nómada",
            titulo = "Pedagogía Nómada: El Camino y el Desierto Interior",
            subtitulo = "Aprender en el camino, habitando la presencia de Dios en cualquier latitud",
            citasFuente = "San Basilio: Carta II a San Gregorio de Nacianzo",
            contenidoTeorico = "La pedagogía nómada asume que el cristiano es peregrino en continua conversión. No nos aferramos a certezas fijas ni a estructuras rígidas: nos movemos dócilmente al soplo del Espíritu Santo. Como los diez sacerdotes fundadores de Annonay en 1822 que se reunieron en la clandestinidad y luego enviaron misioneros a Canadá, Estados Unidos, Colombia y México, el alma basiliana es misionera e itinerante.",
            aplicacionPractica = "Camina 15 minutos en silencio hoy contemplando la naturaleza, invocando el nombre de Jesús a cada paso.",
            preguntasMetaAprendizaje = listOf(
                "¿Qué apegos me impiden estar verdaderamente disponible para la misión a la que Dios me llama?",
                "¿Soy capaz de orar y mantener el recogimiento interior aun en medio del ajetreo?",
                "¿Qué significa para mí ser un 'nómada del Evangelio' en la sociedad actual?"
            )
        ),
        ModuloAprendizaje(
            id = "ascetica_2",
            eje = "Ascética y Oración Nómada",
            titulo = "El Combate contra la Acedia y el Desánimo",
            subtitulo = "La fidelidad en la aridez espiritual y el don de la perseverancia",
            citasFuente = "San Basilio: Reglas Menores; San Juan Clímaco y la tradición ascética",
            contenidoTeorico = "La acedia es el cansancio interior que susurra al religioso o formador que su entrega no tiene fruto, que la oración no sirve y que la vida consagrada es rutinaria. San Basilio enseñaba a combatir este tedio con el ritmo fiel del Oficio Divino, el trabajo manual o intelectual bien hecho y la apertura honesta con el superior o confesor. La perseverancia en la noche es el crisol del oro más puro.",
            aplicacionPractica = "Cuando sientas pereza espiritual o desaliento hoy, arrodíllate 3 minutos y repite con devoción: «Señor Jesús, Hijo de Dios vivo, ten piedad de mí que soy pecador».",
            preguntasMetaAprendizaje = listOf(
                "¿Cuáles son los momentos de la semana en que me asalta el desánimo o la fatiga espiritual?",
                "¿Cómo respondo ante la tentación de abandonar la oración cuando no siento consuelo?",
                "¿Qué estrategias de meta-aprendizaje empleo para renovar mi entusiasmo misionero?"
            )
        )
    )
}
