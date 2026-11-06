package com.example.data.repository

import java.util.Calendar

data class DailyLiturgicalWisdom(
    val dayOfMonth: Int,
    val monthOfYear: Int,
    val dateDisplay: String,
    val liturgicalSeason: String,
    val gospelPassage: String,
    val singleGospelPhrase: String,
    val gospelText: String,
    val gospelImageTheme: String,
    val basilianTeachingTitle: String,
    val basilianTeachingContent: String,
    val basilianDailyQuote: String,
    val patristicSource: String
) {
    val basilianSpiritualityTitle: String get() = basilianTeachingTitle
    val basilianSpiritualityContent: String get() = basilianTeachingContent
}

object DailyGospelAndBasilianWisdomRepository {

    data class GospelWisdomItem(
        val passage: String,
        val phrase: String,
        val fullText: String,
        val imageTheme: String,
        val basilianTitle: String,
        val basilianSpirituality: String,
        val basilianQuote: String,
        val patristicSource: String
    )

    private val gospelCalendarCycle = listOf(
        GospelWisdomItem(
            passage = "San Mateo 5, 1-12a",
            phrase = "«Bienaventurados los limpios de corazón, porque ellos verán a Dios; bienaventurados los constructores de paz, porque serán llamados hijos de Dios.»",
            fullText = "Al ver a la multitud, Jesús subió a la montaña, se sentó, y sus discípulos se le acercaron. Entonces tomó la palabra y comenzó a enseñarles: «Bienaventurados los pobres de espíritu, porque de ellos es el Reino de los Cielos...»",
            imageTheme = "BIENAVENTURANZAS",
            basilianTitle = "La Pureza del Corazón y la Paz Comunitaria",
            basilianSpirituality = "San Basilio Magno enseña en su Asketikon que la pureza de corazón no es una teoría solitaria, sino la libertad interior ganada en la caridad fraterna. Quien limpia su mirada del orgullo y del apego egoísta a los bienes temporales, comienza a ver a Dios reflejado en el rostro de cada hermano con quien comparte la mesa y el trabajo.",
            basilianQuote = "«Quien tiene el corazón limpio no guarda rencores ni mide favores: se hace transparente para que la luz de Cristo resplandezca en la comunidad.»",
            patristicSource = "San Basilio Magno • Homilía sobre las Bienaventuranzas"
        ),
        GospelWisdomItem(
            passage = "San Juan 15, 9-17",
            phrase = "«Nadie tiene amor más grande que el que da la vida por sus amigos: este es mi mandamiento, que os améis los unos a los otros como yo os he amado.»",
            fullText = "Como el Padre me amó, yo también os he amado; permaneced en mi amor... No sois vosotros los que me habéis elegido, soy yo quien os he elegido y os he destinado para que vayáis y deis fruto, y vuestro fruto permanezca.",
            imageTheme = "AMOR_FRATERNO",
            basilianTitle = "La Comunidad Fraterna como Crisol de Caridad",
            basilianSpirituality = "Para San Basilio, el amor de Cristo es la ley suprema de la congregación: «Si vives solo, ¿a quién le lavarás los pies? ¿De quién cuidarás cuando esté enfermo?». La fraternidad fundada en Annonay y extendida hoy por los Padres Basilianos se nutre de este amor oblativo que se entrega enteramente en la educación y en el ministerio sacerdotal.",
            basilianQuote = "«El amor verdadero no busca su propio interés, sino que se parte y comparte como el pan en la mesa de los hermanos.»",
            patristicSource = "San Basilio Magno • Reglas Mayores, Pregunta 7"
        ),
        GospelWisdomItem(
            passage = "San Lucas 10, 25-37",
            phrase = "«Un samaritano que iba de viaje lo vio y se conmovió profundamente; se acercó, vendó sus heridas ungiéndolas con óleo y vino... Ve y haz tú lo mismo.»",
            fullText = "¿Quién es mi prójimo? Jesús respondió: Un hombre bajaba de Jerusalén a Jericó y cayó en manos de unos salteadores. Un samaritano, al verlo, tuvo compasión... «¿Cuál de estos tres te parece que fue prójimo? El que tuvo compasión de él. Jesús le dijo: Ve y haz tú lo mismo».",
            imageTheme = "BUEN_SAMARITANO",
            basilianTitle = "La Gran Basiliada: Compasión Activa hacia el Herido",
            basilianSpirituality = "Inspirado en el Buen Samaritano, San Basilio edificó en Cesarea la 'Basiliada', una ciudadela de hospitalidad donde los leprosos, huérfanos y peregrinos eran acogidos y curados con sus propias manos episcopales. La espiritualidad basiliana no contempla el dolor de lejos: venda las heridas de la sociedad con bondad, ciencia médica y presencia consoladora.",
            basilianQuote = "«El pan que guardas pertenece al hambriento; el manto que no usas abriga al desnudo. La compasión es la liturgia viva del creyente.»",
            patristicSource = "San Basilio Magno • Homilía en Tiempos de Hambre y Peste"
        ),
        GospelWisdomItem(
            passage = "San Juan 10, 11-16",
            phrase = "«Yo soy el Buen Pastor: el buen pastor da su vida por las ovejas, conoce a las suyas y las suyas lo conocen.»",
            fullText = "Yo soy el buen pastor. El asalariado, que no es pastor ni dueño de las ovejas, ve venir al lobo, abandona las ovejas y huye... Yo conozco a mis ovejas y las mías me conocen a mí, como el Padre me conoce y yo conozco al Padre.",
            imageTheme = "BUEN_PASTOR",
            basilianTitle = "El Celo Pastoral y la Cercanía con el Rebaño",
            basilianSpirituality = "San Basilio recordaba a los superiores y sacerdotes que la autoridad en la Iglesia es pastoreo de servicio humilde y desvelo incansable. El pastor basiliano no impone cargas pesadas, sino que camina adelante con mansedumbre, sosteniendo al débil y buscando a quien se extravía en los caminos de la soledad y la desesperanza.",
            basilianQuote = "«El pastor no conduce con gritos ni con aspereza, sino con la dulzura del ejemplo y el calor de la fidelidad silenciosa.»",
            patristicSource = "San Basilio Magno • Carta 28 a los Pastores de Cesarea"
        ),
        GospelWisdomItem(
            passage = "San Mateo 25, 31-40",
            phrase = "«Tuve hambre y me disteis de comer; fui forastero y me hospedasteis... Cuanto hicisteis a uno de estos hermanos míos más pequeños, a mí me lo hicisteis.»",
            fullText = "Cuando el Hijo del Hombre venga en su gloria dirá: Venid, benditos de mi Padre... Porque tuve hambre y me disteis de comer; estuve desnudo y me vestisteis; enfermo y en la cárcel y vinisteis a verme...",
            imageTheme = "CRISTO_EN_EL_POBRE",
            basilianTitle = "Encontrar al Rostro de Cristo en los Desheredados",
            basilianSpirituality = "Para la espiritualidad basiliana, el sacramento del altar y el sacramento del hermano necesitado son inseparables. Cuando los primeros sacerdotes basilianos se unieron en Annonay y cuando llegaron a las barriadas de Cali, reconocieron en los jóvenes de sectores populares el mismo cuerpo doliente de Jesús que clama por educación y dignidad.",
            basilianQuote = "«No te engañes honrando a Cristo en el templo con ricos lienzos si luego lo dejas tiritar de frío a la intemperie a las puertas de tu morada.»",
            patristicSource = "San Basilio Magno • Sobre el Desapego de las Riquezas"
        ),
        GospelWisdomItem(
            passage = "San Mateo 11, 28-30",
            phrase = "«Venid a mí todos los que estáis cansados y agobiados, y yo os aliviaré; aprended de mí, que soy manso y humilde de corazón.»",
            fullText = "Tomad mi yugo sobre vosotros y aprended de mí, que soy manso y humilde de corazón, y hallaréis descanso para vuestras almas. Porque mi yugo es suave y mi carga ligera.",
            imageTheme = "MANSO_Y_HUMILDE",
            basilianTitle = "La Mansedumbre como Fortaleza del Consagrado",
            basilianSpirituality = "San Basilio enseñaba que la mansedumbre no es cobardía, sino el dominio soberano de las pasiones que permite mantener la serenidad aun ante la persecución imperial y las incomprensiones. La 'disciplina' del lema basiliano es precisamente esta docilidad interior ante el Maestro que renueva las fuerzas del apóstol.",
            basilianQuote = "«La paciencia despojada de ira desarma al adversario y deposita en el alma una paz que ninguna tormenta puede arrebatar.»",
            patristicSource = "San Basilio Magno • Homilía contra la Ira"
        ),
        GospelWisdomItem(
            passage = "San Juan 8, 12",
            phrase = "«Yo soy la Luz del mundo; el que me sigue no caminará en tinieblas, sino que tendrá la luz de la vida.»",
            fullText = "Jesús les habló de nuevo diciendo: «Yo soy la luz del mundo; el que me sigue no caminará en tinieblas, sino que tendrá la luz de la vida».",
            imageTheme = "LUZ_DEL_MUNDO",
            basilianTitle = "La Iluminación por el Espíritu y la Ciencia Sagrada",
            basilianSpirituality = "En su inmortal tratado 'De Spiritu Sancto', San Basilio proclama que el Espíritu Santo es la luz celestial que clarifica el entendimiento y despierta el apetito por la verdad divina. El pilar basiliano de la 'Scientia' busca disipar las tinieblas de la ignorancia a través de una educación impregnada del Evangelio.",
            basilianQuote = "«Como el sol baña los ojos abiertos, así el Espíritu ilumina a las mentes que buscan la verdad con rectitud y sencillez.»",
            patristicSource = "San Basilio Magno • Tratado sobre el Espíritu Santo, IX"
        ),
        GospelWisdomItem(
            passage = "San Juan 6, 48-51",
            phrase = "«Yo soy el Pan vivo bajado del cielo: el que coma de este pan vivirá para siempre; y el pan que yo daré es mi carne para la vida del mundo.»",
            fullText = "Vuestros padres comieron el maná en el desierto y murieron; este es el pan que baja del cielo, para que quien lo coma no muera. Yo soy el pan vivo... Si alguno come de este pan, vivirá eternamente.",
            imageTheme = "PAN_DE_VIDA",
            basilianTitle = "La Eucaristía: Banquete de Fraternidad y Comunión Diaria",
            basilianSpirituality = "San Basilio promovió en Cesarea la comunión frecuente (hasta cuatro veces por semana: domingo, miércoles, viernes y sábado), viendo en la Eucaristía el principio animador de la concordia comunitaria. No se puede comulgar del mismo Cáliz y mantener barreras de rencor o desunión entre hermanos.",
            basilianQuote = "«Unidos por el mismo Pan divino, somos convocados a ser un solo corazón y una sola alma en la alabanza a la Santísima Trinidad.»",
            patristicSource = "San Basilio Magno • Carta 93 sobre la Comunión Eucarística"
        ),
        GospelWisdomItem(
            passage = "San Marcos 4, 30-32",
            phrase = "«El Reino de Dios es como un grano de mostaza: la más pequeña de las semillas, pero cuando crece supera a las hortalizas y las aves anidan a su sombra.»",
            fullText = "¿Con qué compararemos el Reino de Dios? Es como una semilla de mostaza sembrada en la tierra... pero una vez sembrada crece y se convierte en un arbusto frondoso donde los pájaros encuentran cobijo.",
            imageTheme = "GRANO_DE_MOSTAZA",
            basilianTitle = "La Semilla Humilde de Annonay y la Esperanza",
            basilianSpirituality = "Cuando diez sacerdotes educadores en Annonay decidieron vivir juntos en 1822 sin grandes recursos humanos, confiaron plenamente en la fuerza silenciosa del grano de mostaza. San Basilio nos exhorta a no despreciar los comienzos pequeños: la fidelidad en lo mínimo engendra árboles robustos de salvación en la Iglesia.",
            basilianQuote = "«No mires la pequeñez del inicio, contempla la infinita fidelidad de Dios que fecunda toda obra comenzada en su santo nombre.»",
            patristicSource = "San Basilio Magno • Comentario a los Salmos"
        ),
        GospelWisdomItem(
            passage = "San Juan 14, 1-6",
            phrase = "«No se turbe vuestro corazón; creed en Dios y creed también en mí... Yo soy el Camino, la Verdad y la Vida.»",
            fullText = "En la casa de mi Padre hay muchas moradas. Si no fuera así, ¿os habría dicho que voy a prepararos un lugar? Y cuando me haya ido y os haya preparado un lugar, volveré y os llevaré conmigo... Tomás le dijo: Señor, no sabemos adónde vas, ¿cómo podemos saber el camino? Jesús le respondió: Yo soy el Camino, la Verdad y la Vida.",
            imageTheme = "CAMINO_VERDAD_VIDA",
            basilianTitle = "Cristo, Maestro Único y Guía de la Mente Humana",
            basilianSpirituality = "San Basilio escribió a los jóvenes estudiantes de Cesarea que todas las artes, ciencias y literaturas tienen valor si nos encaminan a Cristo, Verdad Suprema. No hay sabiduría plena al margen de Jesús: Él ordena nuestro discernimiento y convierte el estudio en un acto de adoración y servicio humilde.",
            basilianQuote = "«Caminar en Cristo es tener la brújula firme en medio de las mareas confusas del mundo.»",
            patristicSource = "San Basilio Magno • Discurso a los Jóvenes (Ad Adolescentes)"
        ),
        GospelWisdomItem(
            passage = "San Lucas 1, 46-55",
            phrase = "«Proclama mi alma la grandeza del Señor, se alegra mi espíritu en Dios mi salvador, porque ha mirado la humildad de su sierva.»",
            fullText = "María dijo: Mi alma glorifica al Señor y mi espíritu se llena de júbilo en Dios mi Salvador, porque ha puesto sus ojos en la pequeñez de su esclava... Derriba del trono a los poderosos y enaltece a los humildes; colma de bienes a los hambrientos y a los ricos despide vacíos.",
            imageTheme = "MAGNIFICAT",
            basilianTitle = "La Alabanza Continua de María y la Humildad Servidora",
            basilianSpirituality = "Bajo el patrocinio de Nuestra Señora de la Asunción, los Padres Basilianos veneran a la Virgen María como la primera discípula y modelo de consagración. San Basilio destaca la humildad de María como la puerta por donde la gracia redentora ingresa en la historia humana.",
            basilianQuote = "«La humildad no achica el alma, la ensancha para que el Dios todopoderoso pueda habitar en ella con toda su plenitud.»",
            patristicSource = "San Basilio Magno • Homilía sobre la Encarnación del Verbo"
        ),
        GospelWisdomItem(
            passage = "San Marcos 1, 14-20",
            phrase = "«El tiempo se ha cumplido y el Reino de Dios está cerca: convertíos y creed en el Evangelio... Venid en pos de mí y os haré pescadores de hombres.»",
            fullText = "Pasando junto al mar de Galilea vio a Simón y a su hermano Andrés que echaban las redes al mar, pues eran pescadores. Jesús les dijo: Venid en pos de mí y os haré pescadores de hombres. Y al instante, dejando las redes, lo siguieron.",
            imageTheme = "VOCACION_REDES",
            basilianTitle = "La Prontitud en el Seguimiento de la Vocación",
            basilianSpirituality = "San Basilio admiraba la determinación sin titubeos de los apóstoles. Al oír la voz de Jesús, no postergaron la respuesta ni consultaron a la prudencia mundana. La vocación sacerdotal y religiosa basiliana nace de esta disponibilidad gozosa a dejar redes y comodidades para navegar mar adentro en la educación y la misión.",
            basilianQuote = "«Quien escucha la llamada del Rey celestial no se entretiene recogiendo migajas en la orilla: despliega las velas de la fe con santa audacia.»",
            patristicSource = "San Basilio Magno • Reglas Breves, Respuesta 1"
        ),
        GospelWisdomItem(
            passage = "San Lucas 6, 27-36",
            phrase = "«Amad a vuestros enemigos, haced el bien y prestad sin esperar nada a cambio: sed compasivos como vuestro Padre es compasivo.»",
            fullText = "A vosotros los que me escucháis os digo: Amad a vuestros enemigos, bendecid a los que os maldicen, orad por los que os calumnian... Sed misericordiosos, como vuestro Padre es misericordioso. No juzguéis y no seréis juzgados.",
            imageTheme = "MISERICORDIA_AMOR",
            basilianTitle = "La Bondad Sin Condiciones (Bonitas)",
            basilianSpirituality = "El primer vocablo del escudo basiliano es 'Bonitatem'. Para San Basilio, la bondad cristiana no es una debilidad sentimental, sino el reflejo activo del Padre que hace salir su sol sobre buenos y malos. Educar y servir con bondad desarma el odio y abre los corazones más endurecidos a la reconciliación.",
            basilianQuote = "«La bondad sincera es el bálsamo que sana las fracturas de la sociedad y siembra la paz donde antes reinaba la discordia.»",
            patristicSource = "San Basilio Magno • Tratado sobre el Juicio de Dios"
        ),
        GospelWisdomItem(
            passage = "San Juan 13, 34-35",
            phrase = "«Os doy un mandamiento nuevo: que os améis los unos a los otros; en esto conocerán todos que sois mis discípulos, si tenéis amor entre vosotros.»",
            fullText = "Hijos míos, ya no estaré mucho tiempo con vosotros... Un mandamiento nuevo os doy: que os améis los unos a los otros; que como yo os he amado, así también os améis mutuamente. En esto conocerán todos que sois mis discípulos.",
            imageTheme = "LAVATORIO_PIES",
            basilianTitle = "El Testimonio de la Fraternidad como Predicación Viva",
            basilianSpirituality = "San Basilio escribió que el mejor sermón es una comunidad unida y afectuosa: «Los hombres no se convierten por discursos elocuentes, sino al ver a los hermanos compartiendo un solo corazón en caridad sincera». Esta comunión de vida es el corazón de la espiritualidad sacerdotal basiliana.",
            basilianQuote = "«Donde mora el amor fraterno, allí habita el Espíritu Santo derramando bendición y vida para siempre.»",
            patristicSource = "San Basilio Magno • Reglas Mayores, Pregunta 3"
        ),
        GospelWisdomItem(
            passage = "San Mateo 6, 25-34",
            phrase = "«Buscad primero el Reino de Dios y su justicia, y todo lo demás se os dará por añadidura: mirad las aves del cielo y los lirios del campo.»",
            fullText = "Por eso os digo: No os angustiéis por vuestra vida, qué comeréis o qué beberéis; ni por vuestro cuerpo, con qué os vestiréis... Mirad los lirios del campo cómo crecen: no se fatigan ni hilan; pero ni Salomón con toda su gloria se vistió como uno de ellos.",
            imageTheme = "LIRIOS_Y_AVES",
            basilianTitle = "La Confianza Providencial y la Sencillez de Vida",
            basilianSpirituality = "San Basilio exhortaba a vivir con sobriedad y libertad frente al afán de acumular riquezas. Dios cuida con ternura paterna de los suyos. Los Padres Basilianos en sus colegios y parroquias renuevan esta confianza abandonando vanas ambiciones para centrarse en la salvación y dignidad del prójimo.",
            basilianQuote = "«Quien confía enteramente en la Providencia de Dios camina ligero y libre, con las manos listas para bendecir y repartir.»",
            patristicSource = "San Basilio Magno • Homilía sobre la Avaricia"
        ),
        GospelWisdomItem(
            passage = "San Marcos 10, 42-45",
            phrase = "«El que quiera ser grande entre vosotros que sea vuestro servidor; porque el Hijo del Hombre no vino a ser servido, sino a servir y dar su vida.»",
            fullText = "Sabéis que los que son tenidos por gobernantes de las naciones se enseñorean de ellas... Pero entre vosotros no debe ser así: antes bien, el que quiera ser grande entre vosotros será vuestro servidor, y el que quiera ser el primero entre vosotros será esclavo de todos.",
            imageTheme = "CRISTO_SERVIDOR",
            basilianTitle = "El Liderazgo de Servicio en la Educación y el Sacerdocio",
            basilianSpirituality = "En la regla de San Basilio, el superior es llamado 'el servidor de la comunidad' y debe imitar a Cristo ciñéndose la toalla. Quien enseña o administra en una institución basiliana está llamado a despojarse de privilegios para lavar los pies a sus hermanos y alumnos más vulnerables.",
            basilianQuote = "«La verdadera grandeza del consagrado se mide por la profundidad de su abnegación y su alegría para servir en lo escondido.»",
            patristicSource = "San Basilio Magno • Asketikon, Capítulo 30"
        ),
        GospelWisdomItem(
            passage = "San Mateo 7, 7-11",
            phrase = "«Pedid y se os dará; buscad y hallaréis; llamad y se os abrirá: porque todo el que pide recibe, el que busca halla y al que llama se le abre.»",
            fullText = "¿O qué hombre hay entre vosotros que, si su hijo le pide pan, le dará una piedra? ¿O si le pide pescado, le dará una serpiente? Pues si vosotros, siendo malos, sabéis dar buenas dádivas a vuestros hijos, ¿cuánto más vuestro Padre celestial dará cosas buenas a quienes se las pidan?",
            imageTheme = "ORACION_CONFIADA",
            basilianTitle = "La Oración Continua y la Perseverancia Incesante",
            basilianSpirituality = "San Basilio enseñaba que la oración no es una interrupción del trabajo, sino el aliento mismo del alma. Al trabajar la tierra, dar clase o preparar el pan, la mente puede invocar al Creador. Quien persevera llamando a la puerta de la misericordia divina jamás se retirará con las manos vacías.",
            basilianQuote = "«Haz de cada latido una alabanza y de cada acto de bondad una oración que ascienda sin cesar al cielo.»",
            patristicSource = "San Basilio Magno • Homilía sobre San Julita y la Oración Incesante"
        ),
        GospelWisdomItem(
            passage = "San Lucas 12, 32-34",
            phrase = "«No temas, pequeño rebaño, porque a vuestro Padre le ha placido daros el Reino: donde esté vuestro tesoro, allí estará también vuestro corazón.»",
            fullText = "Vended vuestras posesiones y dad limosna; haceos bolsas que no se desgasten, un tesoro inagotable en los cielos, donde ladrón no llega ni polilla destruye. Porque donde esté vuestro tesoro, allí estará vuestro corazón.",
            imageTheme = "TESORO_CELESTIAL",
            basilianTitle = "El Desapego Evangélico de los Bienes Materiales",
            basilianSpirituality = "La pobreza profesada por los Basilianos no es miseria, sino una opción lúcida por la riqueza suprema de Dios. San Basilio nos recuerda que el verdadero tesoro es Cristo y la fraternidad: las cosas terrenales pasan pronto, pero el bien sembrado en el corazón de un niño o de un pobre permanece para la eternidad.",
            basilianQuote = "«Coloca tu tesoro en el regazo de los desvalidos y el mismo Dios será el custodio eterno de tu recompensa.»",
            patristicSource = "San Basilio Magno • Exhortación a la Renuncia del Mundo"
        )
    )

    // Calendario Litúrgico Específico Diario (Leccionario Católico Romano y Ciclo Anual)
    private val specificLiturgicalCalendar: Map<Pair<Int, Int>, GospelWisdomItem> = mapOf(
        // 13 de Septiembre (XXIV Domingo del Tiempo Ordinario - Ciclo A)
        Pair(9, 13) to GospelWisdomItem(
            passage = "San Mateo 18, 21-35",
            phrase = "«Se adelantó Pedro y le dijo a Jesús: \"Señor, ¿cuántas veces tendré que perdonar a mi hermano las ofensas que me haga? ¿Hasta siete veces?\". Jesús le respondió: \"No te digo hasta siete veces, sino hasta setenta veces siete\".» (Mt 18, 21-22)",
            fullText = "En aquel tiempo, se adelantó Pedro y dijo a Jesús: «Señor, ¿cuántas veces tendré que perdonar a mi hermano las ofensas que me haga? ¿Hasta siete veces?». Jesús le responde: «No te digo hasta siete veces, sino hasta setenta veces siete. Por eso, el reino de los cielos se parece a un rey que quiso ajustar las cuentas con sus criados. Al empezar a ajustarlas, le presentaron uno que debía diez mil talentos. Como no tenía con qué pagar, el señor mandó que lo vendieran a él con su mujer y sus hijos y todas sus posesiones, y que pagara así. El criado, arrojándose a sus pies, le suplicaba diciendo: 'Ten paciencia conmigo y te lo pagaré todo'. Se compadeció el señor de aquel criado y lo dejó marchar, perdonándole la deuda.\n\nPero, al salir, aquel criado encontró a uno de sus compañeros que le debía cien denarios y, agarrándolo, lo estrangulaba, diciendo: 'Págame lo que me debes'. El compañero, arrojándose a sus pies, le rogaba diciendo: 'Ten paciencia conmigo y te lo pagaré'. Pero él se negó y fue y lo metió en la cárcel hasta que pagara la deuda.\n\nSus compañeros, al ver lo ocurrido, quedaron consternados y fueron a contarle a su señor todo lo sucedido. Entonces el señor lo llamó y le dijo: '¡Siervo malvado! Toda aquella deuda te la perdoné porque me lo rogaste. ¿No debías tú también tener compasión de tu compañero, como yo tuve compasión de ti?'. Y el señor, indignado, lo entregó a los verdugos hasta que pagara toda la deuda.\n\nLo mismo hará con vosotros mi Padre celestial, si cada uno no perdona de corazón a su hermano».",
            imageTheme = "PERDON_MISERICORDIA",
            basilianTitle = "El Perdón Fraterno sin Límites: Setenta Veces Siete",
            basilianSpirituality = "San Basilio Magno enseña en sus Reglas Morales (Regla 52 y Regla 33) y en sus Tratados Ascéticos que el perdón fraterno no es una concesión humana opcional ni un cálculo numérico, sino el reflejo directo de la misericordia inagotable de Dios que canceló la deuda infinita de nuestros pecados. Para los religiosos basilianos en sus comunidades y colegios, vivir en comunidad exige desterrar todo rencor y resentimiento, perdonando de corazón a los hermanos y educando a la juventud en la bondad, la disciplina y la ciencia con entrañas de compasión evangélica.",
            basilianQuote = "«Quien no perdona de corazón a su hermano las ofensas recibidas, cierra con sus propias manos las puertas de la clemencia divina. La medida con que perdonamos es la medida en que Dios nos acoge.»",
            patristicSource = "San Basilio Magno • Reglas Morales 52, 1-3 y Homilía sobre la Ira y la Mansedumbre"
        ),
        // 14 de Septiembre (Fiesta de la Exaltación de la Santa Cruz)
        Pair(9, 14) to GospelWisdomItem(
            passage = "San Juan 3, 13-17",
            phrase = "«Tanto amó Dios al mundo, que entregó a su Unigénito, para que todo el que cree en él no perezca, sino que tenga vida eterna.» (Jn 3, 16)",
            fullText = "En aquel tiempo, dijo Jesús a Nicodemo: «Nadie ha subido al cielo sino el que bajó del cielo, el Hijo del hombre. Y como Moisés levantó la serpiente en el desierto, así tiene que ser levantado el Hijo del hombre, para que todo el que cree en él tenga vida eterna. Porque tanto amó Dios al mundo, que entregó a su Unigénito, para que todo el que cree en él no perezca, sino que tenga vida eterna. Porque Dios no envió a su Hijo al mundo para juzgar al mundo, sino para que el mundo se salve por él».",
            imageTheme = "SANTA_CRUZ",
            basilianTitle = "La Cruz Gloriosa como Manantial de Amor Redentor",
            basilianSpirituality = "En el escudo basiliano resplandece la Cruz como estandarte supremo de redención. San Basilio nos recuerda que el madero de la Cruz es el árbol de la reconciliación del universo, donde el amor infinito de Dios disipa todo rencor humano.",
            basilianQuote = "«Al contemplar la Cruz, el alma comprende que el amor de Dios no conoce límites y que la salvación brota de la entrega generosa.»",
            patristicSource = "San Basilio Magno • De Spiritu Sancto, Cap. 15"
        ),
        // 15 de Septiembre (Nuestra Señora de los Dolores)
        Pair(9, 15) to GospelWisdomItem(
            passage = "San Juan 19, 25-27",
            phrase = "«Junto a la cruz de Jesús estaban su madre... Jesús dijo a su madre: 'Mujer, ahí tienes a tu hijo'. Luego dijo al discípulo: 'Ahí tienes a tu madre'.» (Jn 19, 25-27)",
            fullText = "En aquel tiempo, junto a la cruz de Jesús estaban su madre, la hermana de su madre, María la de Cleofás, y María Magdalena. Al ver a su madre y junto a ella al discípulo que amaba, Jesús dijo a su madre: «Mujer, ahí tienes a tu hijo». Luego dijo al discípulo: «Ahí tienes a tu madre». Y desde aquella hora, el discípulo la recibió en su casa.",
            imageTheme = "VIRGEN_DOLORES",
            basilianTitle = "María al Pie de la Cruz y la Maternidad Espiritual",
            basilianSpirituality = "María permanece firme al pie de la cruz enseñándonos la fidelidad en la prueba. Los Basilianos se consagran a su protección maternal en la misión parroquial y escolar.",
            basilianQuote = "«Acoge a María como tu más dulce protectora; quien camina con la Madre nunca se extravía del sendero del Hijo.»",
            patristicSource = "San Basilio Magno • Homilía sobre la Encarnación del Señor"
        ),
        // 16 de Septiembre (Santos Cornelio y Cipriano)
        Pair(9, 16) to GospelWisdomItem(
            passage = "San Lucas 7, 31-35",
            phrase = "«La sabiduría de Dios ha quedado justificada por todos sus hijos.» (Lc 7, 35)",
            fullText = "En aquel tiempo, dijo el Señor: «¿A quién se parecen los hombres de esta generación? Son como niños sentados en la plaza que se gritan: Os hemos tocado la flauta y no habéis bailado; os hemos cantado lamentos y no habéis llorado. Vino Juan el Bautista, que no comía pan ni bebía vino, y decís: Tiene demonio. Vino el Hijo del hombre, que come y bebe, y decís: Mirad qué comilón y bebedor, amigo de publicanos y pecadores. Pero la sabiduría se ha acreditado por todos sus hijos».",
            imageTheme = "SABIDURIA_EVANGELICA",
            basilianTitle = "La Verdadera Sabiduría (Scientia) que Nace de la Fe",
            basilianSpirituality = "La ciencia auténtica proclamada en el lema basiliano («Scientiam doce me») no es jactancia intelectual, sino el discernimiento que reconoce los signos de Dios en la historia y en los sencillos.",
            basilianQuote = "«La ciencia que no sirve para amar y glorificar a Dios es solo viento; la que ilumina el corazón edifica la comunidad.»",
            patristicSource = "San Basilio Magno • Discurso a los Jóvenes sobre las Letras Clásicas"
        ),
        // 20 de Septiembre (XXV Domingo del Tiempo Ordinario - Ciclo B)
        Pair(9, 20) to GospelWisdomItem(
            passage = "San Marcos 9, 30-37",
            phrase = "«Quien quiera ser el primero, que sea el último de todos y el servidor de todos. El que acoge a un niño en mi nombre, a mí me acoge.» (Mc 9, 35.37)",
            fullText = "En aquel tiempo, Jesús y sus discípulos atravesaron Galilea... Jesús les preguntó: «¿De qué discutíais por el camino?». Ellos callaban, pues por el camino habían discutido quién era el más grande. Se sentó, llamó a los Doce y les dijo: «Quien quiera ser el primero, que sea el último de todos y el servidor de todos». Y tomando a un niño, lo puso en medio de ellos, lo abrazó y les dijo: «El que acoge a uno de estos niños en mi nombre, me acoge a mí; y el que me acoge a mí, no me acoge a mí, sino al que me envió».",
            imageTheme = "SERVICIO_HUMILDAD",
            basilianTitle = "El Servicio Fraterno: El Mayor Honor en la Comunidad",
            basilianSpirituality = "En la Constitución Basiliana y las Reglas de San Basilio, el hermano que preside debe ser el siervo de todos, lavando los pies y acogiendo a la juventud con amor paternal.",
            basilianQuote = "«La cumbre del sacerdocio basiliano no son los honores terrenos, sino arrodillarse ante el hermano necesitado para servirle en el silencio.»",
            patristicSource = "San Basilio Magno • Reglas Mayores, Pregunta 30"
        ),
        // 21 de Septiembre (Fiesta de San Mateo Apóstol y Evangelista)
        Pair(9, 21) to GospelWisdomItem(
            passage = "San Mateo 9, 9-13",
            phrase = "«Misericordia quiero y no sacrificios: no he venido a llamar a justos, sino a pecadores.» (Mt 9, 13)",
            fullText = "En aquel tiempo, vio Jesús al pasar a un hombre llamado Mateo sentado al mostrador de los impuestos, y le dijo: «Sígueme». Él se levantó y lo siguió... Jesús les dijo: «No tienen necesidad de médico los sanos, sino los enfermos. Id y aprended lo que significa: Misericordia quiero y no sacrificios».",
            imageTheme = "VOCACION_MISERICORDIA",
            basilianTitle = "La Llamada Vocacional y la Fuente Inagotable de Misericordia",
            basilianSpirituality = "La vocación religiosa basiliana nace de una mirada misericordiosa de Cristo que nos llama desde nuestras debilidades para hacernos instrumentos de gracia y consolación entre los jóvenes.",
            basilianQuote = "«El Señor no mira el pasado de quien llama, sino el amor con que está dispuesto a seguirle a partir de hoy.»",
            patristicSource = "San Basilio Magno • Homilía sobre el Bautismo y la Vocación"
        ),
        // 2 de Enero (Solemnidad de San Basilio Magno y San Gregorio Nacianceno)
        Pair(1, 2) to GospelWisdomItem(
            passage = "San Mateo 5, 13-19",
            phrase = "«Vosotros sois la sal de la tierra y la luz del mundo... Brille así vuestra luz delante de los hombres, para que vean vuestras buenas obras y den gloria a vuestro Padre celestial.»",
            fullText = "Vosotros sois la sal de la tierra... No se enciende una lámpara para meterla debajo del celemín, sino para ponerla en el candelero y que alumbre a todos los de la casa. Brille así vuestra luz ante los hombres.",
            imageTheme = "SAN_BASILIO_LUZ",
            basilianTitle = "Solemnidad de Nuestro Padre San Basilio Magno: Luz de la Iglesia",
            basilianSpirituality = "San Basilio unió la contemplación más sublime de la Trinidad con la obra social más audaz de la cristiandad en su célebre 'Basiliada'. Enseña que la caridad cristiana es la luz viva que disipa las tinieblas de la injusticia y la soledad en el mundo.",
            basilianQuote = "«El pan que no usas es del hambriento; el abrigo guardado en tu armario es del desnudo; el calzado que se pudre es del descalzo.»",
            patristicSource = "San Basilio Magno • Homilía contra la Riqueza y el Asketikon"
        ),
        // 21 de Noviembre (Fundación Basiliana en Annonay 1822 y Presentación de María)
        Pair(11, 21) to GospelWisdomItem(
            passage = "San Lucas 1, 39-56",
            phrase = "«Proclama mi alma la grandeza del Señor, y mi espíritu se alegra en Dios, mi Salvador, porque ha mirado la humillación de su sierva.»",
            fullText = "María se levantó y se puso en camino de prisa hacia la montaña... Isabel exclamó: «¡Bendita tú entre las mujeres y bendito el fruto de tu vientre!». Y María dijo: «El Poderoso ha hecho obras grandes por mí, su nombre es santo».",
            imageTheme = "FUNDACION_ANNONAY_1822",
            basilianTitle = "Aniversario de Fundación en Annonay (1822) y el Sí de María",
            basilianSpirituality = "El 21 de noviembre de 1822, diez sacerdotes guiados por Joseph Lapierre fundaron la Congregación en Annonay consagrándola a la Presentación de María y a San Basilio. Nuestro carisma es proclamar las maravillas de Dios en la educación y la fraternidad sacerdotal.",
            basilianQuote = "«En la sencillez de una pequeña escuela en Francia comenzó un fuego de fe que hoy abraza a América y al mundo entero.»",
            patristicSource = "Constituciones Históricas C.S.B. de 1822 • Artículo 1"
        )
    )

    fun getTodayWisdom(): DailyLiturgicalWisdom {
        val calendar = Calendar.getInstance()
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val monthOfYear = calendar.get(Calendar.MONTH) + 1 // 1-12
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

        val daysOfWeek = listOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
        val dayName = daysOfWeek.getOrElse(dayOfWeek - 1) { "Domingo" }

        val months = listOf(
            "enero", "febrero", "marzo", "abril", "mayo", "junio",
            "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
        )
        val monthName = months.getOrElse(monthOfYear - 1) { "septiembre" }
        val dateDisplay = "$dayName, $dayOfMonth de $monthName de ${calendar.get(Calendar.YEAR)}"

        // Determinación canónica del tiempo litúrgico según fecha
        val season = when {
            monthOfYear == 9 && dayOfMonth == 13 -> "XXIV Domingo del Tiempo Ordinario (Ciclo A)"
            monthOfYear == 9 && dayOfMonth == 14 -> "Fiesta de la Exaltación de la Santa Cruz"
            monthOfYear == 9 && dayOfMonth == 15 -> "Memoria de Nuestra Señora de los Dolores"
            monthOfYear == 9 && dayOfMonth == 20 -> "XXV Domingo del Tiempo Ordinario (Ciclo B)"
            monthOfYear == 9 && dayOfMonth == 21 -> "Fiesta de San Mateo, Apóstol y Evangelista"
            monthOfYear == 9 && dayOfMonth == 27 -> "XXVI Domingo del Tiempo Ordinario (Ciclo B)"
            monthOfYear == 1 && dayOfMonth == 2 -> "Solemnidad de San Basilio Magno y San Gregorio Nacianceno"
            monthOfYear == 11 && dayOfMonth == 21 -> "Solemnidad de la Fundación Basiliana en Annonay (1822)"
            monthOfYear == 12 -> if (dayOfMonth < 25) "Tiempo de Adviento" else "Tiempo de Navidad"
            monthOfYear == 1 -> if (dayOfMonth < 10) "Tiempo de Navidad" else "Tiempo Ordinario"
            monthOfYear in 2..3 -> "Tiempo de Cuaresma"
            monthOfYear in 4..5 -> "Tiempo Pascual de Resurrección"
            else -> "Tiempo Ordinario Litúrgico"
        }

        // 1. Búsqueda prioritaria por fecha fija del Leccionario Católico
        val specificItem = specificLiturgicalCalendar[Pair(monthOfYear, dayOfMonth)]
        val item = specificItem ?: run {
            val itemIndex = (dayOfYear + dayOfMonth) % gospelCalendarCycle.size
            gospelCalendarCycle[itemIndex]
        }

        return DailyLiturgicalWisdom(
            dayOfMonth = dayOfMonth,
            monthOfYear = monthOfYear,
            dateDisplay = dateDisplay,
            liturgicalSeason = season,
            gospelPassage = item.passage,
            singleGospelPhrase = item.phrase,
            gospelText = item.fullText,
            gospelImageTheme = item.imageTheme,
            basilianTeachingTitle = item.basilianTitle,
            basilianTeachingContent = item.basilianSpirituality,
            basilianDailyQuote = item.basilianQuote,
            patristicSource = item.patristicSource
        )
    }
}

