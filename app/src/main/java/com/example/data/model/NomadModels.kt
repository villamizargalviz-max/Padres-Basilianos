package com.example.data.model

/**
 * Modos de transporte para la Espiritualidad en Movimiento (Modo Nómada)
 */
enum class TransportMode(
    val label: String,
    val iconEmoji: String,
    val averageSpeedKmh: Float,
    val spiritualMotto: String
) {
    WALKING(
        label = "Caminata",
        iconEmoji = "🚶",
        averageSpeedKmh = 4.5f,
        spiritualMotto = "Pasos de contemplación y presencia divina"
    ),
    CYCLING(
        label = "Bicicleta",
        iconEmoji = "🚲",
        averageSpeedKmh = 18.0f,
        spiritualMotto = "Pedaleo con templanza, ritmo y esfuerzo ofrecido"
    ),
    TRANSIT(
        label = "Transporte Urbano",
        iconEmoji = "🚌",
        averageSpeedKmh = 28.0f,
        spiritualMotto = "Monasterio invisible en el bullicio de la ciudad"
    )
}

/**
 * Cápsula de Sabiduría Basiliana en audio para trayectos en movimiento
 */
data class WisdomCapsule(
    val id: String,
    val title: String,
    val subtitle: String,
    val durationMinutes: Int,
    val recommendedMode: TransportMode,
    val triggerKmThreshold: Float,
    val quote: String,
    val author: String,
    val patristicSource: String,
    val audioScript: String,
    val reflectionPrompt: String
)

/**
 * Hito de la Peregrinación Virtual a través de los viajes históricos de San Basilio
 */
data class PilgrimageMilestone(
    val id: String,
    val stepNumber: Int,
    val targetKm: Float,
    val title: String,
    val geographicRegion: String,
    val period: String,
    val historicalContext: String,
    val patristicQuote: String,
    val exclusiveText: String,
    val contemplativeExercise: String,
    val iconEmoji: String = "⛪"
)

/**
 * Estado en tiempo real del trayecto activo
 */
data class ActiveTripState(
    val isRunning: Boolean = false,
    val mode: TransportMode = TransportMode.CYCLING,
    val elapsedSeconds: Long = 0L,
    val distanceKm: Float = 0f,
    val currentSpeedKmh: Float = 0f,
    val estimatedCalories: Int = 0,
    val activeCapsule: WisdomCapsule? = null
)

/**
 * Banco oficial de Cápsulas de Sabiduría Basilianas para la Espiritualidad en Movimiento
 */
object NomadWisdomData {
    val capsules = listOf(
        WisdomCapsule(
            id = "capsule_walking_breath",
            title = "El Ritmo de los Pasos y la Presencia Interior",
            subtitle = "Para caminatas urbanas y desplazamientos a pie",
            durationMinutes = 3,
            recommendedMode = TransportMode.WALKING,
            triggerKmThreshold = 1.0f,
            quote = "«Atiende a ti mismo: que cada paso de tu cuerpo recuerde al alma su vocación eterna».",
            author = "San Basilio Magno",
            patristicSource = "Homilía 'In illud: Attende tibi ipsi', PG 31",
            audioScript = "Hermano caminante: el caminar es la forma más antigua de oración. San Basilio nos enseñó a no dispersar los sentidos en el tumulto de la plaza pública. Al poner un pie tras otro sobre el asfalto, sincroniza tu respiración con la paz de Dios. Inhala reconociendo la gracia que sostiene tu vida; exhala soltando la prisa, la ansiedad del trabajo y la dispersión mental. Cada persona que cruzas en la acera lleva la imagen de Cristo; bendícelos en silencio al pasar.",
            reflectionPrompt = "¿Qué prisa innecesaria puedes entregar en este momento del camino?"
        ),
        WisdomCapsule(
            id = "capsule_cycling_effort",
            title = "Pedalear con Templanza y Disciplina Consagrada",
            subtitle = "Para trayectos en bicicleta hacia el estudio o trabajo",
            durationMinutes = 4,
            recommendedMode = TransportMode.CYCLING,
            triggerKmThreshold = 3.0f,
            quote = "«El cuerpo es el instrumento del alma; su templanza otorga ligereza y libertad al espíritu».",
            author = "San Basilio Magno",
            patristicSource = "Reglas Menores 128, PG 31",
            audioScript = "Pedalear por la ciudad exige atención plena, equilibrio y fortaleza. Para los Padres Basilianos, el esfuerzo físico no es vanidad, sino ofrenda y escuela de sobriedad evangélica. Mientras impulsas los pedales, ofrece la fuerza de tus piernas por aquellos que hoy no pueden moverse: los enfermos en los hospitales, los ancianos y los marginados. Que tu bicicleta sea un templo en movimiento, donde la disciplina física se convierta en una melodía de acción de gracias.",
            reflectionPrompt = "Ofrece los próximos kilómetros de tu pedaleo por una persona concreta que esté sufriendo hoy."
        ),
        WisdomCapsule(
            id = "capsule_transit_invisible_monastery",
            title = "El Monasterio Invisible en el Transporte Urbano",
            subtitle = "Para el autobús, metro o trayectos entre la multitud",
            durationMinutes = 3,
            recommendedMode = TransportMode.TRANSIT,
            triggerKmThreshold = 2.0f,
            quote = "«No busques la soledad en el desierto geográfico si tu corazón está lleno de ruido. Erige un claustro interior en medio de la muchedumbre».",
            author = "San Basilio Magno",
            patristicSource = "Carta 2 a San Gregorio Nacianceno",
            audioScript = "Estás rodeado de personas en el transporte: rostros cansados, miradas clavadas en pantallas, trabajadores madrugadores. Este vagón o este autobús es tu claustro monástico por los próximos minutos. San Basilio nos enseñó que la vida fraterna comienza mirando con compasión al hermano. En lugar de irritarte por la estrechez o el tráfico, mira los rostros a tu alrededor con los ojos de Dios. Reza un 'Padre Nuestro' silencioso por el chofer y por cada pasajero que viaja a tu lado.",
            reflectionPrompt = "Mira a tres personas desconocidas en este trayecto y pide una bendición secreta para sus familias."
        ),
        WisdomCapsule(
            id = "capsule_long_distance_wisdom",
            title = "De Capadocia a Atenas: Sabiduría en las Rutas del Mundo",
            subtitle = "Para trayectos largos y viajes interurbanos",
            durationMinutes = 5,
            recommendedMode = TransportMode.CYCLING,
            triggerKmThreshold = 8.0f,
            quote = "«Como las abejas se posan sobre las flores pero solo extraen de ellas la miel más pura, así debe el sabio transitar por el mundo».",
            author = "San Basilio Magno",
            patristicSource = "Discurso a los Jóvenes sobre la Literatura Griega",
            audioScript = "En el siglo cuarto, el joven Basilio emprendió un viaje marítimo y terrestre de miles de kilómetros desde Cesarea hasta Atenas para formarse. En los caminos conoció peligros, fatigas y la tentación del orgullo intelectual. Pero supo discernir qué conocimientos edificaban el bien y cuáles dañaban el alma. En tu viaje de hoy hacia la universidad o tu empresa, pregúntate: ¿qué influencias, noticias y conversaciones te acercan a la verdad, y cuáles te intoxican con amargura? Extrae solo la miel de la bondad, la disciplina y la ciencia.",
            reflectionPrompt = "¿Qué miel pura de conocimiento o servicio vas a producir hoy en tu destino?"
        ),
        WisdomCapsule(
            id = "capsule_return_gratitude",
            title = "El Retorno al Hogar: Gratitud del Peregrino al Ocaso",
            subtitle = "Para el regreso a casa al final de la jornada",
            durationMinutes = 4,
            recommendedMode = TransportMode.WALKING,
            triggerKmThreshold = 5.0f,
            quote = "«Bendito el Señor que guardó tu salida por la mañana y tu entrada al caer la tarde».",
            author = "Liturgia de las Horas Basiliana",
            patristicSource = "Oración del Lucernario de San Basilio",
            audioScript = "La jornada va terminando y tus pasos o ruedas te conducen de regreso al hogar. Los Padres de Annonay en 1822 regresaban de las aulas campesinas para compartir el pan en la mesa común con alegría sencilla. Deja atrás las tensiones del día. No lleves resentimientos ni frustraciones a tu casa. Agradece por haber llegado con bien. Al cruzar el umbral, serás un portador de paz para quienes te esperan.",
            reflectionPrompt = "Nombra tres dones o momentos de gracia que recibiste hoy durante tu jornada."
        )
    )

    val milestones = listOf(
        PilgrimageMilestone(
            id = "milestone_1_caesarea",
            stepNumber = 1,
            targetKm = 20.0f,
            title = "Cesarea de Capadocia: El Hogar de los Santos",
            geographicRegion = "Capadocia (Turquía Central)",
            period = "329 - 345 d.C.",
            historicalContext = "Nacido en el seno de una familia excepcional donde florecieron santos (su abuela Macrina la Mayor, su madre Emilia, su hermana Macrina la Joven y sus hermanos Gregorio de Nisa y Pedro de Sebaste). Aquí aprendió que la santidad germina en el calor del hogar.",
            patristicQuote = "«Tuve como maestra de fe a mi abuela Macrina, quien me transmitió las enseñanzas del gran Gregorio Taumaturgo».",
            exclusiveText = "DESBLOQUEO EXCLUSIVO: 'La Célula Familiar como Monasterio Primigenio'.\n\nSan Basilio no nació ermitaño; aprendió la compasión viendo a su madre y a su hermana vestir a los huérfanos y hornear pan para los desposeídos durante los inviernos gélidos de Capadocia. El primer monasterio del cristiano es su propia casa: el respeto en la mesa compartida, el perdón inmediato entre hermanos y la plegaria antes de apagar las lámparas nocturnas.",
            contemplativeExercise = "Llama o escribe hoy un mensaje de gratitud a un miembro de tu familia que te haya transmitido la fe.",
            iconEmoji = "🏡"
        ),
        PilgrimageMilestone(
            id = "milestone_2_athens",
            stepNumber = 2,
            targetKm = 60.0f,
            title = "La Gran Ruta a Atenas: Estudio y Santa Amistad",
            geographicRegion = "Atenas (Grecia Clásica)",
            period = "351 - 356 d.C.",
            historicalContext = "En la célebre academia de Atenas, Basilio estudió retórica, astronomía y filosofía junto a San Gregorio Nacianceno. Su amistad se convirtió en leyenda cristiana: compartían habitación, libros y el mismo ideal de virtud.",
            patristicQuote = "«Teníamos todas las cosas en común; éramos dos cuerpos animados por una sola alma. Conocíamos solo dos calles: la que llevaba a la iglesia y la que conducía a la escuela».",
            exclusiveText = "DESBLOQUEO EXCLUSIVO: 'La Teología de la Amistad Consagrada'.\n\nEn un mundo dominado por la vanagloria y la competitividad entre estudiantes, Basilio y Gregorio demostraron que el intelecto solo brilla cuando está coronado por la humildad y la amistad santa. Una amistad verdadera no busca el provecho mutuo, sino acompañarse mutuamente en el camino hacia la santidad.",
            contemplativeExercise = "Haz una pausa en tu camino para orar por tus amigos más cercanos: que su fe no flaquee en los momentos difíciles.",
            iconEmoji = "🏛️"
        ),
        PilgrimageMilestone(
            id = "milestone_3_deserts",
            stepNumber = 3,
            targetKm = 120.0f,
            title = "La Peregrinación a Egipto, Palestina y Siria",
            geographicRegion = "Yermo de Tebaida y Valle del Nilo",
            period = "357 - 358 d.C.",
            historicalContext = "Inquieto por una entrega total, Basilio emprendió a pie y en caravana una larga travesía por los desiertos de Oriente para conocer de primera mano a los Padres del Desierto y los cenobios de San Pacomio.",
            patristicQuote = "«Admiré su abstinencia en la comida, su paciencia en las vigilias y su oración incesante. Pero comprendí que la soledad absoluta encierra un peligro sutil: si vives solo, ¿a quién lavarás los pies?».",
            exclusiveText = "DESBLOQUEO EXCLUSIVO: 'El Nacimiento de la Vida Comunitaria Activa'.\n\nAquí radica la genialidad revolucionaria de San Basilio: al contemplar a los anacoretas aislados, discernió que el Evangelio es esencialmente fraterno. El cristiano necesita de sus hermanos para ejercitar la paciencia, la caridad y el servicio mutuo. La comunidad es el crisol donde el orgullo se purifica.",
            contemplativeExercise = "Identifica una persona en tu entorno de estudio o trabajo a quien habitualmente evitas. Decide tener un gesto concreto de servicio hacia ella.",
            iconEmoji = "🏜️"
        ),
        PilgrimageMilestone(
            id = "milestone_4_iris_river",
            stepNumber = 4,
            targetKm = 180.0f,
            title = "El Retiro en el Río Iris (Ponto) y las Reglas",
            geographicRegion = "Annesi, Ponto (Costa del Mar Negro)",
            period = "358 - 364 d.C.",
            historicalContext = "A orillas del torrente Iris, en un paraje boscoso y escarpado, fundó su primer monasterio. Allí redactó las Reglas Mayores (55 capítulos) y las Reglas Menores (313 preceptos), cimiento del monacato oriental y occidental.",
            patristicQuote = "«El trabajo manual es un deber sagrado. Quien no trabaja, que no coma; y quien trabaja, que comparta el fruto de sus manos con el desamparado».",
            exclusiveText = "DESBLOQUEO EXCLUSIVO: 'Ora et Labora Basiliano: La Espiritualidad de las Manos Limpias'.\n\nMucho antes que San Benito en Occidente, San Basilio integró la oración litúrgica con la carpintería, el cultivo de la tierra y la educación de los huérfanos. Para Basilio, no hay trabajo indigno si se realiza con pureza de intención.",
            contemplativeExercise = "Dedica hoy 15 minutos de tu jornada a realizar una labor manual humilde (limpieza, arreglo, cocina) ofreciéndola en contemplación silenciosa.",
            iconEmoji = "🌲"
        ),
        PilgrimageMilestone(
            id = "milestone_5_basiliad",
            stepNumber = 5,
            targetKm = 240.0f,
            title = "La Gran Basiliada de Cesarea: La Ciudad de la Caridad",
            geographicRegion = "Cesarea de Capadocia",
            period = "370 - 379 d.C.",
            historicalContext = "Ya como Arzobispo de Cesarea, durante una devastadora hambruna construyó una verdadera ciudadela de misericordia a las afueras de la ciudad, con hospital, leprosería, albergue para extranjeros y talleres de oficios.",
            patristicQuote = "«El pan que guardas en tu alacena pertenece al hambriento; el vestido colgado en tu ropero pertenece al desnudo; el dinero que sepultas bajo tierra es el rescate del pobre».",
            exclusiveText = "DESBLOQUEO EXCLUSIVO: 'La Teología Social de la Basiliada'.\n\nLa Basiliada fue el primer hospital público y gratuito de la historia de la humanidad. San Basilio no delegó la caridad: él mismo vendaba las llagas purulentas de los leprosos y cocinaba lentejas en calderos gigantes para alimentar a la multitud hambrienta. La fe cristiana se verifica en el amor encarnado.",
            contemplativeExercise = "Revisa tu armario o despensa y aparta ropa o alimentos de buena calidad para donarlos directamente a alguien que los necesite esta semana.",
            iconEmoji = "🏥"
        ),
        PilgrimageMilestone(
            id = "milestone_6_annonay_america",
            stepNumber = 6,
            targetKm = 300.0f,
            title = "Annonay 1822 y la Misión Nómada en América",
            geographicRegion = "Francia, Canadá, EE.UU., México y Colombia",
            period = "1822 - 2026",
            historicalContext = "Diez sacerdotes clandestinos en Annonay resucitaron el espíritu de San Basilio fundando la Congregación. Décadas después, cruzaron el Atlántico en barcos de vapor para fundar colegios en Toronto (1852), Houston, Tehuacán y llegar a Colombia en 1987.",
            patristicQuote = "«Bonitatem et disciplinam et scientiam doce me. Enséñame, Señor, la bondad, la disciplina y la ciencia para servir al mundo».",
            exclusiveText = "DESBLOQUEO CUMBRE: 'La Pedagogía Nómada del Siglo XXI'.\n\n¡Has completado los 300 kilómetros de la Peregrinación Virtual Basiliana! Has unido tus desplazamientos diarios al legado milenario de los caminantes del Evangelio. Los sacerdotes basilianos en Colombia (Bogotá, Medellín, Cali) siguen siendo educadores itinerantes que llevan la luz de Cristo a las universidades, parroquias y barriadas. Ahora eres un peregrino consagrado por la perseverancia.",
            contemplativeExercise = "Descarga o lee la 'Oración Final del Peregrino Basiliano' y ofrece una misa o un momento solemne de agradecimiento ante el Sagrario.",
            iconEmoji = "🛡️"
        )
    )
}
