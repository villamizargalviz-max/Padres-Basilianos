package com.example.data.model

/**
 * Puntos de interés interactivos para la Iconografía Sagrada en Realidad Aumentada (AR)
 * y estudio teológico del ícono bizantino de San Basilio Magno.
 */
data class IconHotspot(
    val id: String,
    val title: String,
    val greekTitle: String,
    val theologicalCategory: String,
    // Coordenadas normalizadas (0.0f a 1.0f) relativas al centro y dimensiones del ícono
    val normalizedX: Float,
    val normalizedY: Float,
    val shortBadge: String,
    val description: String,
    val theologicalExegesis: String,
    val patristicSource: String,
    val patristicQuote: String,
    val audioNarrationScript: String
)

object ArIconographyData {
    val hotspots = listOf(
        IconHotspot(
            id = "hotspot_nimbus",
            title = "Aureola Dorada de Luz Increada",
            greekTitle = "Φῶς Ἄκτιστον (Phos Aktiston)",
            theologicalCategory = "Luz Divina & Divinización (Teosis)",
            normalizedX = 0.50f,
            normalizedY = 0.22f,
            shortBadge = "Luz Taboriana",
            description = "El halo circular resplandeciente que corona la cabeza del santo, trazado con oro bruñido y orlado con filigrana carmesí.",
            theologicalExegesis = "En la teología patrística oriental de San Basilio y San Gregorio Palamas, la aureola no es una simple corona estética ni un símbolo solar pagano. Representa la Luz Increada de Dios que resplandeció en la Transfiguración del Monte Tabor. El oro no refleja sombras terrenales: atestigua que la persona humana ha sido traspasada por la gracia santificante del Espíritu Santo hasta alcanzar la 'theosis' (divinización por participación). El santo ya habita en el esplendor de la Jerusalén Celestial.",
            patristicSource = "San Basilio Magno, 'Tratado del Espíritu Santo', Cap. IX, 23",
            patristicQuote = "«Así como los cuerpos limpios y transparentes, heridos por un rayo de luz, se vuelven ellos mismos resplandecientes, así las almas iluminadas por el Espíritu se vuelven espirituales y transmiten la gracia a otros».",
            audioNarrationScript = "Observa la aureola dorada. En la iconografía sagrada, el dorado no es un color terrenal, sino la presencia misma de la Luz Increada del Monte Tabor. San Basilio nos recuerda en su Tratado del Espíritu Santo que el creyente que purifica su corazón se vuelve semejante a un cristal transparente: recibe la luz divina y la proyecta hacia sus hermanos. Al contemplar este halo, recuerda que tu propia alma está llamada a resplandecer en medio de las tinieblas del mundo."
        ),
        IconHotspot(
            id = "hotspot_blessing_hand",
            title = "Mano en Gesto de Bendición Bizantina (IC-XC)",
            greekTitle = "Χεὶρ Εὐλογίας (Cheir Eulogias)",
            theologicalCategory = "Cristología & Trinidad",
            normalizedX = 0.68f,
            normalizedY = 0.65f,
            shortBadge = "Bendición Trinitaria",
            description = "La mano derecha alzada con los dedos dispuestos cuidadosamente formando el monograma sagrado de Cristo (Iesous Christos).",
            theologicalExegesis = "La disposición de los dedos es un compendio de la fe ortodoxa y católica: el dedo índice extendido (I) y el dedo mayor curvado (C) forman 'IC'. El pulgar y el anular se tocan en cruz (X), mientras el meñique curvado forma 'C', completando las siglas griegas de JESUCRISTO (IC-XC). Además, los tres dedos erguidos confiesan la Santísima Trinidad (Padre, Hijo y Espíritu Santo), y los dos dedos unidos confiesan las dos naturalezas inconfundibles de Cristo: verdadero Dios y verdadero Hombre.",
            patristicSource = "Concilio de Nicea I (defendido por San Basilio contra el arrianismo)",
            patristicQuote = "«No bendigo con mi propia autoridad, sino en el Nombre de Aquel que venció la muerte y unió en Sí mismo la divinidad y nuestra frágil humanidad».",
            audioNarrationScript = "Mira con atención los dedos de la mano derecha de San Basilio. No es un saludo común; sus dedos forman las letras griegas Iota, Sigma, Chi, Sigma: el Nombre sagrado de Jesucristo, nuestro Salvador. Al mismo tiempo, los tres dedos confiesan a la Santísima Trinidad y los dos dedos unidos proclaman que Jesús es Dios y hombre verdadero. Cuando el obispo basiliano bendice, es el mismo Cristo quien derrama su paz sobre tu vida."
        ),
        IconHotspot(
            id = "hotspot_omophorion",
            title = "Omophorion Episcopal con Cruces Paté",
            greekTitle = "Ὠμοφόριον (Omophorion)",
            theologicalCategory = "Pastoral del Buen Pastor",
            normalizedX = 0.35f,
            normalizedY = 0.60f,
            shortBadge = "El Buen Pastor",
            description = "La ancha banda de lana blanca virgen que cruza el pecho y hombros del arzobispo, bordada con cruces griegas paté de color negro y oro.",
            theologicalExegesis = "El omophorion es la vestidura episcopal más sagrada de la Iglesia oriental. Fabricado con lana pura de cordero, representa a la oveja perdida que el Buen Pastor busca incansablemente en los riscos del mundo y carga sobre sus propios hombros para devolverla al redil. San Basilio, como Arzobispo de Cesarea, encarnó este símbolo al salir personalmente a rescatar a los desamparados, huérfanos y leprosos en la célebre Basiliada.",
            patristicSource = "San Isidoro de Pelusio, 'Epístolas teológicas'",
            patristicQuote = "«El obispo que porta el omophorion lleva sobre sus espaldas la flaqueza y los dolores de todo su pueblo, dispuesto a dar la vida por sus ovejas».",
            audioNarrationScript = "El omophorion blanco que cubre los hombros de San Basilio está tejido con lana pura de oveja. Recuerda la parábola evangélica de la oveja extraviada: el Buen Pastor no castiga a la oveja perdida, sino que la carga sobre sus hombros con infinita ternura. Como obispo de Cesarea, Basilio cargó los sufrimientos del pueblo empobrecido. Este signo nos convoca a no juzgar la fragilidad del prójimo, sino a sostenerlo con paciencia y caridad fraterna."
        ),
        IconHotspot(
            id = "hotspot_gospel_book",
            title = "Libro de los Santos Evangelios (Códice Dorado)",
            greekTitle = "Ἱερὸν Εὐαγγέλιον (Hieron Euaggelion)",
            theologicalCategory = "Palabra Viva & Magisterio",
            normalizedX = 0.32f,
            normalizedY = 0.76f,
            shortBadge = "Palabra Eterna",
            description = "El pesado códice encuadernado en oro y engastado con rubíes y esmeraldas que el santo sostiene firmemente con su mano izquierda protegida por el manto.",
            theologicalExegesis = "El santo sostiene el Evangelio con reverencia, cubriendo a menudo su mano con el pliegue del manto litúrgico (velatio manuum) para indicar que las Sagradas Escrituras son un misterio que no puede ser manoseado con ligereza humana. La encuadernación dorada con cuatro gemas en los extremos representa los cuatro Evangelistas (Mateo, Marcos, Lucas y Juan) cuyos testimonios iluminan los cuatro puntos cardinales de la tierra.",
            patristicSource = "San Basilio Magno, 'Reglas Morales' 1, 1",
            patristicQuote = "«Toda palabra de la Divina Escritura debe ser recibida no como invención humana, sino con la misma reverencia que si escucháramos al mismo Dios hablándonos desde los cielos».",
            audioNarrationScript = "En su mano izquierda, San Basilio sostiene el Libro de los Evangelios cerrado y enjoyado. Las cuatro gemas en las esquinas representan a los cuatro evangelistas que llevaron la Buena Nueva a los cuatro confines del mundo. El libro está cerrado para enseñarnos que la Palabra de Dios no es una teoría para discutir, sino un secreto sagrado de vida que debe abrirse en el corazón y vivirse en las obras de misericordia."
        ),
        IconHotspot(
            id = "hotspot_ascetic_face",
            title = "Rostro Ascético y Frente Contemplativa",
            greekTitle = "Πρόσωπον Ἀσκητικόν (Prosopon Asketikon)",
            theologicalCategory = "Vigilancia Interior (Nepsis)",
            normalizedX = 0.50f,
            normalizedY = 0.40f,
            shortBadge = "Atiende a ti mismo",
            description = "Frente espaciosa y alta, ojos almendrados de mirada serena y penetrante, pómulos marcados por el ayuno y larga barba patriarcal oscura con mechones plateados.",
            theologicalExegesis = "A diferencia del arte profano que busca halagar los sentidos carnales, la fisonomía del ícono bizantino está purificada de pasiones mundanas. Los ojos agrandados no miran al espectador con curiosidad, sino que contemplan las realidades eternas. La boca pequeña y cerrada indica el silencio contemplativo y la ausencia de palabras vanas. La frente amplia denota la sabiduría teológica iluminada por la oración incesante y el ayuno corporal.",
            patristicSource = "San Basilio Magno, 'Homilía sobre el Ayuno' I, 10",
            patristicQuote = "«El ayuno engendra profetas y robustece a los sabios. El ayuno otorga alas a la oración del alma».",
            audioNarrationScript = "Fíjate en el rostro de San Basilio: su frente ancha representa el discernimiento y la ciencia teológica que combatió las herejías de su tiempo. Sus ojos grandes y serenos reflejan la pureza del corazón que contempla a Dios. Sus labios cerrados nos invitan al silencio interior: quien domina su lengua domina todo su ser. Su lema era: 'Atiende a ti mismo', recordándonos guardar la paz del alma en todo momento."
        ),
        IconHotspot(
            id = "hotspot_gold_ground",
            title = "Fondo de Pan de Oro (El Octavo Día)",
            greekTitle = "Χρυσὸς Βυθός (Chrysos Bythos)",
            theologicalCategory = "Eternidad & Reino Celestial",
            normalizedX = 0.15f,
            normalizedY = 0.20f,
            shortBadge = "Eternidad Divina",
            description = "El fondo continuo y radiante de pan de oro que envuelve toda la composición sin horizonte terrenal ni sombras arrojadas.",
            theologicalExegesis = "En la iconografía tradicional de la Iglesia oriental, nunca se pintan sombras ni fondos realistas tridimensionales que aten la figura al espacio físico efímero. El fondo de oro puro representa el 'Octavo Día': la eternidad consumada donde Dios es 'todo en todos' (1 Cor 15,28). El creyente que contempla el ícono es transportado al ámbito de la liturgia celestial, donde los santos interceden perpetuamente ante el Trono de la Gracia.",
            patristicSource = "San Juan Damasceno, 'Discurso en defensa de los Santos Iconos'",
            patristicQuote = "«El icono es una ventana abierta a la eternidad; a través de la materia visible somos elevados a la comunión invisible con Dios».",
            audioNarrationScript = "En este icono no hay sombras ni paisajes terrenales. Todo está rodeado de un fondo radiante de oro. Los antiguos maestros iconógrafos llamaban a esto el 'Octavo Día', el tiempo de la eternidad donde ya no hay noche, ni dolor, ni muerte, sino la plenitud de la gloria de Dios. Cuando te detienes frente a un icono sagrado, abres una ventana a la eternidad en medio de tu vida cotidiana."
        )
    )
}
