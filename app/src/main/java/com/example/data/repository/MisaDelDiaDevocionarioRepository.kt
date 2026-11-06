package com.example.data.repository

import com.example.data.model.BasilianDirectoryMember
import com.example.data.model.ColombianLiturgicalDay

data class DailyMassReadings(
    val id: String,
    val celebrationTitleEs: String,
    val celebrationTitleEn: String,
    val lectionaryCycleNoteEs: String,
    val lectionaryCycleNoteEn: String,
    val firstReadingCitation: String,
    val firstReadingTitleEs: String,
    val firstReadingTextEs: String,
    val firstReadingTextEn: String,
    val responsorialPsalmCitation: String,
    val responsorialPsalmResponseEs: String,
    val responsorialPsalmResponseEn: String,
    val responsorialPsalmVersesEs: String,
    val responsorialPsalmVersesEn: String,
    val gospelAcclamationEs: String,
    val gospelAcclamationEn: String,
    val gospelCitation: String,
    val gospelTitleEs: String,
    val gospelTextEs: String,
    val gospelTextEn: String,
    val basilianHomilyReflectionEs: String,
    val basilianHomilyReflectionEn: String
)

data class BasilianDevotionalPrayer(
    val id: String,
    val categoryEs: String,
    val categoryEn: String,
    val titleEs: String,
    val titleEn: String,
    val subtitleEs: String,
    val subtitleEn: String,
    val bodyEs: String,
    val bodyEn: String,
    val latinMottoOrClosing: String
)

object MisaDelDiaDevocionarioRepository {

    fun getMassReadingsForDay(day: ColombianLiturgicalDay): DailyMassReadings {
        return when (day.id) {
            "chiquinquira_jul_9" -> DailyMassReadings(
                id = "misa_chiquinquira",
                celebrationTitleEs = "Solemnidad de Nuestra Señora del Rosario de Chiquinquirá, Patrona de Colombia",
                celebrationTitleEn = "Solemnity of Our Lady of the Rosary of Chiquinquirá, Patroness of Colombia",
                lectionaryCycleNoteEs = "Leccionario Propio de Colombia (CEC) · Misal Romano para Colombia",
                lectionaryCycleNoteEn = "Proper Lectionary of Colombia (CEC) · Roman Missal for Colombia",
                firstReadingCitation = "Isaías 9, 1-6",
                firstReadingTitleEs = "Lectura del libro del profeta Isaías",
                firstReadingTextEs = "El pueblo que caminaba en tinieblas vio una luz grande; habitaban tierra de sombras, y una luz les brilló. Acrecentaste la alegría, aumentaste el gozo; porque un niño nos ha nacido, un hijo se nos ha dado: lleva al hombro el principado, y es su nombre: «Maravilla de Consejero, Príncipe de la Paz».",
                firstReadingTextEn = "The people who walked in darkness have seen a great light; upon those who dwelt in the land of gloom a light has shone. For a child is born to us, a son is given us; upon his shoulder dominion rests. They name him Wonder-Counselor, Prince of Peace.",
                responsorialPsalmCitation = "Salmo 44 (45)",
                responsorialPsalmResponseEs = "R/. De pie a tu derecha está la Reina, vestida de oro fino.",
                responsorialPsalmResponseEn = "R/. The Queen stands at your right hand, arrayed in gold.",
                responsorialPsalmVersesEs = "Escucha, hija, mira: inclina el oído, el Rey está prendado de tu belleza. Con alegría y regocijo entran en el palacio real.",
                responsorialPsalmVersesEn = "Hear, O daughter, and see; turn your ear, the King desires your beauty. With gladness and joy they enter the palace of the King.",
                gospelAcclamationEs = "¡Aleluya, aleluya! Dichosa tú, Virgen María, que creíste, porque se cumplirá lo que te fue anunciado de parte del Señor. ¡Aleluya!",
                gospelAcclamationEn = "Alleluia, alleluia! Blessed are you, O Virgin Mary, who believed that what was spoken to you by the Lord would be fulfilled. Alleluia!",
                gospelCitation = "Juan 2, 1-11",
                gospelTitleEs = "✠ Lectura del santo Evangelio según san Juan",
                gospelTextEs = "En aquel tiempo, había una boda en Caná de Galilea, y la madre de Jesús estaba allí. Jesús y sus discípulos estaban también invitados a la boda. Faltó el vino, y la madre de Jesús le dijo: «No tienen vino». Su madre dijo a los sirvientes: «Hagan lo que Él les diga». Así, en Caná de Galilea, Jesús comenzó sus signos, manifestó su gloria y creció la fe de sus discípulos en Él.",
                gospelTextEn = "At that time, there was a wedding at Cana in Galilee, and the mother of Jesus was there. When the wine ran short, the mother of Jesus said to him, 'They have no wine.' His mother said to the servers, 'Do whatever he tells you.' Jesus did this as the beginning of his signs at Cana in Galilee and so revealed his glory.",
                basilianHomilyReflectionEs = "En el lienzo renovado de Chiquinquirá, María nos invita como pueblo colombiano y como familia basiliana a escuchar siempre la voz de su Hijo: «Hagan lo que Él les diga». Allí donde parece agotarse el vino de la esperanza o de la reconciliación, Cristo transforma nuestra agua cotidiana en el vino nuevo de la fraternidad.",
                basilianHomilyReflectionEn = "Before the renewed canvas of Chiquinquirá, Mary invites Colombia and the Basilian family to listen to her Son: 'Do whatever He tells you.' Where hope seems to run dry, Christ transforms our daily water into the new wine of fraternity."
            )
            "pedro_claver_sep_9" -> DailyMassReadings(
                id = "misa_pedro_claver",
                celebrationTitleEs = "Solemnidad de San Pedro Claver, Patrono de los Derechos Humanos en Colombia",
                celebrationTitleEn = "Solemnity of St. Peter Claver, Patron of Human Rights in Colombia",
                lectionaryCycleNoteEs = "Leccionario Propio de Colombia (CEC) · 9 de Septiembre",
                lectionaryCycleNoteEn = "Proper Lectionary of Colombia (CEC) · September 9",
                firstReadingCitation = "Isaías 58, 6-11",
                firstReadingTitleEs = "Lectura del libro del profeta Isaías",
                firstReadingTextEs = "¿No es este el ayuno que yo quiero: desatar las ligaduras de maldad, soltar las cargas opresoras, dejar libres a los quebrantados y romper todo yugo? Partir tu pan con el hambriento y albergar a los pobres sin techo; entonces brillará tu luz como la aurora.",
                firstReadingTextEn = "Is this not the fast that I choose: releasing those bound unjustly, untying the thongs of the yoke, setting free the oppressed? Sharing your bread with the hungry, sheltering the oppressed and the homeless; then your light shall break forth like the dawn.",
                responsorialPsalmCitation = "Salmo 145 (146)",
                responsorialPsalmResponseEs = "R/. El Señor hace justicia a los oprimidos y da pan a los hambrientos.",
                responsorialPsalmResponseEn = "R/. The Lord secures justice for the oppressed and gives food to the hungry.",
                responsorialPsalmVersesEs = "El Señor mantiene su fidelidad perpetuamente, el Señor liberta a los cautivos, el Señor abre los ojos al ciego y endereza a los que ya se doblan.",
                responsorialPsalmVersesEn = "The Lord keeps faith forever, sets captives free, gives sight to the blind, and raises up those who were bowed down.",
                gospelAcclamationEs = "¡Aleluya, aleluya! Les doy un mandamiento nuevo: que se amen los unos a los otros como yo los he amado. ¡Aleluya!",
                gospelAcclamationEn = "Alleluia, alleluia! I give you a new commandment: love one another as I have loved you. Alleluia!",
                gospelCitation = "Mateo 25, 31-40",
                gospelTitleEs = "✠ Lectura del santo Evangelio según san Mateo",
                gospelTextEs = "En aquel tiempo, dijo Jesús a sus discípulos: «Vengan, benditos de mi Padre; porque tuve hambre y me dieron de comer, tuve sed y me dieron de beber, fui forastero y me hospedaron, estuve enfermo y me visitaron. En verdad les digo que cuanto hicieron a uno de estos hermanos míos más pequeños, a mí me lo hicieron».",
                gospelTextEn = "Jesus said to his disciples: 'Come, you who are blessed by my Father. For I was hungry and you gave me food, I was thirsty and you gave me drink, a stranger and you welcomed me, ill and you cared for me. Amen, I say to you, whatever you did for one of these least brothers of mine, you did for me.'",
                basilianHomilyReflectionEs = "San Pedro Claver en Cartagena de Indias y San Basilio Magno en la Basiliada de Cesarea comparten el mismo Evangelio vivo: reconocer la dignidad sagrada de cada ser humano herido o descartado.",
                basilianHomilyReflectionEn = "St. Peter Claver in Cartagena and St. Basil the Great in Caesarea share the same living Gospel: recognizing the sacred dignity of every wounded human being."
            )
            else -> DailyMassReadings(
                id = "misa_ordo_colombia_hoy",
                celebrationTitleEs = "Misa del Día según el Ordo Litúrgico de Colombia (CEC)",
                celebrationTitleEn = "Daily Mass according to the Colombian Liturgical Ordo (CEC)",
                lectionaryCycleNoteEs = "Leccionario de Colombia (Conferencia Episcopal de Colombia - CEC) · ${day.psalterWeekEs}",
                lectionaryCycleNoteEn = "Lectionary of Colombia (Episcopal Conference of Colombia - CEC) · ${day.psalterWeekEn}",
                firstReadingCitation = "Colosenses 3, 12-17",
                firstReadingTitleEs = "Lectura de la carta del apóstol san Pablo a los Colosenses",
                firstReadingTextEs = "Hermanos: Como elegidos de Dios, santos y amados, revístanse de compasión entrañable, bondad, humildad, mansedumbre y paciencia. Sobrellévense mutuamente y perdónense cuando alguno tenga quejas contra otro; el Señor los ha perdonado: hagan ustedes lo mismo. Y por encima de todo esto, revístanse del amor, que es el ceñidor de la unidad consumada. Que la palabra de Cristo habite entre ustedes en toda su riqueza; enséñense y exhórtense unos a otros con toda sabiduría.",
                firstReadingTextEn = "Brothers and sisters: Put on, as God's chosen ones, holy and beloved, heartfelt compassion, kindness, humility, gentleness, and patience, bearing with one another and forgiving one another. And over all these put on love, that is, the bond of perfection. Let the word of Christ dwell in you richly, as in all wisdom you teach and admonish one another.",
                responsorialPsalmCitation = "Salmo 118 (119), 65-68",
                responsorialPsalmResponseEs = "R/. Enséñame, Señor, la bondad, la disciplina y la ciencia.",
                responsorialPsalmResponseEn = "R/. Teach me, O Lord, goodness, discipline, and knowledge.",
                responsorialPsalmVersesEs = "Has tratado bien a tu siervo, Señor, según tu palabra.\nEnséñame la bondad, la disciplina y la ciencia, porque creo en tus mandamientos.\nTú eres bueno y haces el bien; instrúyeme en tus decretos.",
                responsorialPsalmVersesEn = "You have dealt well with your servant, O Lord, according to your word.\nTeach me goodness, discipline, and knowledge, for I believe in your commandments.\nYou are good and bountiful; teach me your statutes.",
                gospelAcclamationEs = "¡Aleluya, aleluya! Ustedes son la luz del mundo; brille su luz ante los hombres para que den gloria a su Padre del cielo. ¡Aleluya!",
                gospelAcclamationEn = "Alleluia, alleluia! You are the light of the world; let your light shine before others, so that they may give glory to your Heavenly Father. Alleluia!",
                gospelCitation = "Mateo 5, 13-16",
                gospelTitleEs = "✠ Lectura del santo Evangelio según san Mateo",
                gospelTextEs = "En aquel tiempo, dijo Jesús a sus discípulos: «Ustedes son la sal de la tierra. Pero si la sal se vuelve sosa, ¿con qué la salarán? Ustedes son la luz del mundo. No se puede ocultar una ciudad puesta en lo alto de un monte. Tampoco se enciende una lámpara para meterla debajo del celemín, sino para ponerla en el candelero y que alumbre a todos los de la casa. Brille así su luz ante los hombres, para que vean sus buenas obras y den gloria a su Padre que está en los cielos».",
                gospelTextEn = "Jesus said to his disciples: 'You are the salt of the earth. You are the light of the world. A city set on a mountain cannot be hidden. Nor do they light a lamp and then put it under a bushel basket; it is set on a lampstand, where it gives light to all in the house. Just so, your light must shine before others, that they may see your good deeds and glorify your heavenly Father.'",
                basilianHomilyReflectionEs = "En comunión con la Iglesia que peregrina en Colombia, el Señor nos llama hoy a ser sal que preserva la esperanza y luz que educa en la verdad y la caridad. Como enseña San Basilio Magno, la fe auténtica se traduce en bondad activa hacia el prójimo, estudio constante de la Sabiduría divina y servicio humilde a la comunidad.",
                basilianHomilyReflectionEn = "In communion with the Church in Colombia, the Lord calls us today to be salt that preserves hope and light that educates in truth and charity. As St. Basil the Great teaches, authentic faith becomes active goodness toward our neighbor."
            )
        }
    }

    val devotionalPrayers: List<BasilianDevotionalPrayer> = listOf(
        BasilianDevotionalPrayer(
            id = "oracion_san_basilio",
            categoryEs = "Espiritualidad Basiliana",
            categoryEn = "Basilian Spirituality",
            titleEs = "Oración a San Basilio Magno",
            titleEn = "Prayer to Saint Basil the Great",
            subtitleEs = "Patrono de la Congregación de San Basilio (C.S.B.) y Doctor de la Iglesia",
            subtitleEn = "Patron of the Congregation of St. Basil (C.S.B.) and Doctor of the Church",
            bodyEs = "Oh Dios, que te dignaste iluminar a tu Iglesia con las enseñanzas y el ejemplo heroico de caridad de San Basilio Magno:\n\nConcédenos buscar con humildad tu verdad, enseñar con paciencia la sabiduría del Evangelio y servir con ternura a los más necesitados. Que, animados por tu Espíritu Santo, vivamos en comunión fraterna, llevando bondad, disciplina y ciencia a nuestras comunidades en Colombia y en el mundo entero. Por Jesucristo, nuestro Señor. Amén.",
            bodyEn = "O God, who deigned to illumine Your Church with the teachings and heroic charity of Saint Basil the Great:\n\nGrant that we may humbly seek Your truth, patiently teach the wisdom of the Gospel, and tenderly serve those most in need. Animated by Your Holy Spirit, may we bring goodness, discipline, and knowledge to our communities in Colombia and throughout the world. Through Christ our Lord. Amen.",
            latinMottoOrClosing = "Bonitatem et disciplinam et scientiam doce me (Salmo 118, 66)"
        ),
        BasilianDevotionalPrayer(
            id = "oracion_vocaciones_csb",
            categoryEs = "Vocaciones C.S.B.",
            categoryEn = "C.S.B. Vocations",
            titleEs = "Oración por las Vocaciones Basilianas",
            titleEn = "Prayer for Basilian Vocations",
            subtitleEs = "Por los jóvenes llamados a la vida sacerdotal y educativa en la Congregación",
            subtitleEn = "For young people called to priestly and educational life in the Congregation",
            bodyEs = "Señor Jesús, Buen Pastor y Maestro divino, que llamaste a los primeros apóstoles a dejarlo todo para seguirte y anunciar tu Reino:\n\nMira con amor a nuestra Iglesia en Colombia y a la Congregación de San Basilio. Suscita en el corazón de muchos jóvenes el deseo generoso de consagrar su vida a la educación cristiana, a la predicación de tu Palabra y a la vida comunitaria. Dales valentía para responder a tu llamado y fidelidad perseverante en tu amor. María, Madre del Buen Consejo y Reina de Chiquinquirá, acompaña cada vocación. Amén.",
            bodyEn = "Lord Jesus, Good Shepherd and Divine Teacher, look with love upon our Church in Colombia and the Congregation of St. Basil. Awaken in the hearts of many young people the generous desire to consecrate their lives to Christian education, the preaching of Your Word, and community life. Amen.",
            latinMottoOrClosing = "San Basilio Magno, ruega por nuestras vocaciones."
        ),
        BasilianDevotionalPrayer(
            id = "consagracion_chiquinquira",
            categoryEs = "Devoción Colombiana",
            categoryEn = "Colombian Devotion",
            titleEs = "Oración a Nuestra Señora del Rosario de Chiquinquirá",
            titleEn = "Prayer to Our Lady of the Rosary of Chiquinquirá",
            subtitleEs = "Reina y Patrona de Colombia · Ordo Colombiano CEC",
            subtitleEn = "Queen and Patroness of Colombia · Colombian CEC Ordo",
            bodyEs = "Santísima Virgen María del Rosario de Chiquinquirá, Madre de Dios y Reina de Colombia:\n\nAnte tu imagen renovada ponemos hoy nuestras familias, nuestras comunidades educativas, nuestros sacerdotes y todo el pueblo colombiano. Renueva en nuestra patria el tejido de la reconciliación, la justicia y la paz. Guíanos siempre hacia tu Hijo Jesucristo para que nuestras obras reflejen la luz del Evangelio. Amén.",
            bodyEn = "Most Holy Virgin Mary of the Rosary of Chiquinquirá, Mother of God and Queen of Colombia: Before your renewed image we place today our families, our schools, our priests, and all the people of Colombia. Renew in our homeland reconciliation, justice, and peace. Amen.",
            latinMottoOrClosing = "Nuestra Señora del Rosario de Chiquinquirá, ruega por Colombia."
        ),
        BasilianDevotionalPrayer(
            id = "lectio_divina_guiada",
            categoryEs = "Oración Bíblica",
            categoryEn = "Biblical Prayer",
            titleEs = "Lectio Divina Guiada en 4 Pasos",
            titleEn = "4-Step Guided Lectio Divina",
            subtitleEs = "Método monástico patrístico para orar con la Palabra del Día",
            subtitleEn = "Patristic monastic method for praying with the Word of the Day",
            bodyEs = "1. LECTIO (Lectura — ¿Qué dice el texto?):\nLee despacio el Evangelio de hoy dos veces en voz suave. Detente en la frase o palabra que más toque tu corazón.\n\n2. MEDITATIO (Meditación — ¿Qué me dice Dios a mí?):\nRumia esa palabra en tu interior. ¿Qué situación de tu vida actual ilumina hoy el Señor?\n\n3. ORATIO (Oración — ¿Qué le respondo al Señor?):\nHabla con Dios con total confianza como un amigo habla con su amigo: agradece, pide perdón o intercede por quienes amas.\n\n4. CONTEMPLATIO (Contemplación y Acción — ¿A qué me compromete?):\nDescansa en silencio ante la presencia de Dios y elige un gesto concreto de bondad para practicar hoy.",
            bodyEn = "1. LECTIO (Reading — What does the text say?): Read today's Gospel slowly.\n2. MEDITATIO (Meditation — What does God say to me?): Ponder the word in your heart.\n3. ORATIO (Prayer — How do I respond to the Lord?): Speak to God with filial trust.\n4. CONTEMPLATIO (Contemplation & Action): Rest in God's presence and choose one concrete act of kindness today.",
            latinMottoOrClosing = "«Que la Palabra de Cristo habite entre ustedes en toda su riqueza» (Col 3, 16)."
        ),
        BasilianDevotionalPrayer(
            id = "oracion_paz_serenidad_basilio",
            categoryEs = "Paz Interior",
            categoryEn = "Inner Peace",
            titleEs = "Oración de San Basilio por la Serenidad y la Creación",
            titleEn = "St. Basil's Prayer for Serenity and Creation",
            subtitleEs = "Inspirada en las Homilías sobre el Hexamerón de San Basilio",
            subtitleEn = "Inspired by St. Basil's Homilies on the Hexaemeron",
            bodyEs = "Oh Dios Creador y Padre de misericordia, que todo lo hiciste con sabiduría y armonía:\n\nEnsancha nuestro corazón para contemplar tu huella en cada criatura y en cada hermano. Serena nuestras inquietudes, disipa todo temor con la luz de tu Espíritu y haznos constructores de paz allí donde haya discordia o cansancio. Que al comenzar y al terminar este día podamos descansar en tu providencia paterna. Amén.",
            bodyEn = "O Creator God and Father of mercy, enlarge our hearts to contemplate Your trace in every creature and every brother and sister. Calm our anxieties, dispel every fear with the light of Your Spirit, and make us peacemakers. Amen.",
            latinMottoOrClosing = "Gloria al Padre, y al Hijo, y al Espíritu Santo."
        )
    )

    fun buildLiturgicalShareCardText(
        day: ColombianLiturgicalDay,
        massReadings: DailyMassReadings,
        isEnglish: Boolean = false
    ): String {
        return if (isEnglish) {
            """
            🇨🇴✨ *COLOMBIAN LITURGICAL ORDO (CEC) & DAILY GOSPEL* ✨🇨🇴
            📅 ${day.dateDisplayEn}
            ⛪ *${day.titleEn}*
            🎨 Liturgical Color: ${day.color.nameEn} · ${day.psalterWeekEn}

            📖 *Gospel of the Day (${massReadings.gospelCitation}):*
            "${massReadings.gospelTextEn}"

            💡 *Basilian Reflection:*
            ${massReadings.basilianHomilyReflectionEn}

            🙏 _Bonitatem et disciplinam et scientiam doce me_ — Congregation of St. Basil (C.S.B.)
            """.trimIndent()
        } else {
            """
            🇨🇴✨ *ORDO LITÚRGICO DE COLOMBIA (CEC) Y EVANGELIO DEL DÍA* ✨🇨🇴
            📅 ${day.dateDisplayEs}
            ⛪ *${day.titleEs}*
            🎨 Color Litúrgico: ${day.color.nameEs} · ${day.psalterWeekEs}

            📖 *Evangelio del Día (${massReadings.gospelCitation}):*
            «${massReadings.gospelTextEs}»

            💡 *Reflexión Basiliana:*
            ${massReadings.basilianHomilyReflectionEs}

            🙏 _«Enséñame la bondad, la disciplina y la ciencia»_ — Congregación de San Basilio (C.S.B.)
            """.trimIndent()
        }
    }

    fun buildBasilianBirthdayCardText(member: BasilianDirectoryMember): String {
        return """
        🎂✝️ *FELICITACIÓN FRATERNA DE CUMPLEAÑOS — C.S.B.* ✝️🎂

        Querido hermano *${member.name}*:

        En comunión fraterna dentro de la Congregación de San Basilio (C.S.B.), damos gracias a Dios por el don de tu vida, tu vocación y tu ministerio en *${member.currentHouse}* (${member.country}).

        Que por intercesión de San Basilio Magno y de Nuestra Señora, el Señor te conceda abundante salud, alegría interior y sabiduría en este nuevo año de vida.

        📘 _«Bonitatem et disciplinam et scientiam doce me»_ (Salmo 118, 66)
        ¡Feliz y bendecido cumpleaños! 🙌🎉
        """.trimIndent()
    }
}
