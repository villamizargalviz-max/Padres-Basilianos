package com.example.data.repository

import com.example.data.model.*
import java.text.SimpleDateFormat
import java.util.*

object LiturgiaOrdoColombianoRepository {

    fun getTodayColombianDay(): ColombianLiturgicalDay {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) // 0-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return when {
            month == Calendar.JULY && day == 9 -> chiquinquiraSolemnity
            month == Calendar.SEPTEMBER && day == 9 -> sanPedroClaverFeast
            month == Calendar.OCTOBER && day == 9 -> sanLuisBeltranFeast
            month == Calendar.OCTOBER && day == 21 -> santaLauraMontoyaFeast
            month == Calendar.JANUARY && day == 2 -> sanBasilioMagnoSolemnity
            else -> buildDynamicOrdinaryDay(calendar)
        }
    }

    fun buildSynchronizedDay(onlineInfo: com.example.data.service.LiturgiaDataService.OnlineCalendarInfo): ColombianLiturgicalDay {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Check if there is an official Colombian solemnity/feast first
        val specificDay = when {
            month == Calendar.JULY && day == 9 -> chiquinquiraSolemnity
            month == Calendar.SEPTEMBER && day == 9 -> sanPedroClaverFeast
            month == Calendar.OCTOBER && day == 9 -> sanLuisBeltranFeast
            month == Calendar.OCTOBER && day == 21 -> santaLauraMontoyaFeast
            month == Calendar.JANUARY && day == 2 -> sanBasilioMagnoSolemnity
            else -> null
        }
        if (specificDay != null) return specificDay

        return buildDynamicOrdinaryDay(calendar)
    }

    fun getAllFeaturedColombianCelebrations(): List<ColombianLiturgicalDay> = listOf(
        getTodayColombianDay(),
        chiquinquiraSolemnity,
        sanPedroClaverFeast,
        sanLuisBeltranFeast,
        santaLauraMontoyaFeast,
        sanBasilioMagnoSolemnity
    )

    private fun buildDynamicOrdinaryDay(calendar: Calendar): ColombianLiturgicalDay {
        val sdfEs = SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "CO"))
        val sdfEn = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US)
        val formattedDateEs = sdfEs.format(calendar.time).replaceFirstChar { it.uppercase() }
        val formattedDateEn = sdfEn.format(calendar.time)

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val weekNumber = ((calendar.get(Calendar.WEEK_OF_YEAR) % 4) + 1)
        val psalterEs = when (weekNumber) {
            1 -> "Semana I del Salterio"
            2 -> "Semana II del Salterio"
            3 -> "Semana III del Salterio"
            else -> "Semana IV del Salterio"
        }
        val psalterEn = "Week $weekNumber of the Psalter"

        val hoursMap = mutableMapOf<LiturgicalHourType, BilingualLiturgicalHour>()
        LiturgicalHourType.values().forEach { hourType ->
            hoursMap[hourType] = buildOrdinaryBilingualHour(hourType, psalterEs, psalterEn)
        }

        return ColombianLiturgicalDay(
            id = "hoy_dinamico",
            dateDisplayEs = formattedDateEs,
            dateDisplayEn = formattedDateEn,
            titleEs = "Feria del Tiempo Ordinario • Ordo Colombiano (CEC)",
            titleEn = "Weekday in Ordinary Time • Colombian Ordo (CEC)",
            rankEs = "Feria Litúrgica • Color Verde",
            rankEn = "Liturgical Weekday • Green Vestments",
            color = LiturgicalColor.VERDE,
            season = LiturgicalSeason.TIEMPO_ORDINARIO,
            psalterWeekEs = psalterEs,
            psalterWeekEn = psalterEn,
            ordoNotesEs = "Ordo Colombiano (Conferencia Episcopal de Colombia - CEC): Oficio de la feria con salmodia del día corriente y oraciones de súplica por la paz y la reconciliación en Colombia.",
            ordoNotesEn = "Colombian Ordo (Episcopal Conference of Colombia): Weekday Office with current psalter and intercessions for peace and reconciliation in Colombia.",
            basilianConnectionEs = "La comunidad de los Padres Basilianos en Colombia une su oración matutina y vespertina con la Iglesia peregrina en Bogotá, Medellín, Cali y el mundo.",
            basilianConnectionEn = "The Basilian Fathers community in Colombia joins morning and evening prayer with the pilgrim Church in Colombia and worldwide.",
            hours = hoursMap
        )
    }

    private fun buildOrdinaryBilingualHour(
        hourType: LiturgicalHourType,
        psalterEs: String,
        psalterEn: String
    ): BilingualLiturgicalHour {
        val es = when (hourType) {
            LiturgicalHourType.INVITATORIO -> HourContent(
                hourName = "Invitatorio",
                ordoRubricNote = "V. Señor, abre mis labios. R. Y mi boca proclamará tu alabanza.",
                introductoryVerse = "V. Señor, abre mis labios.\nR. Y mi boca proclamará tu alabanza.",
                hymnTitle = "Venid a la presencia del Señor",
                hymnText = "Venid, adoremos al Señor, fuente de toda sabiduría y santidad.\nÉl nos reúne como un solo rebaño en Cristo.",
                psalms = listOf(
                    PsalmItem(
                        title = "Salmo 94 (95)",
                        subtitle = "Invitación a la alabanza y advertencia",
                        antiphon = "Ant. Al Señor que nos creó, venid, adoremos.",
                        psalmText = "Venid, aclamemos al Señor, demos vítores a la Roca que nos salva.\nEntremos a su presencia dándole gracias, aclamándolo con cantos.\nPorque el Señor es un Dios grande, soberano de todos los dioses.",
                        gloriaPatri = "Gloria al Padre, y al Hijo, y al Espíritu Santo. Como era en el principio, ahora y siempre, por los siglos de los siglos. Amén."
                    )
                ),
                briefReading = ReadingItem("Proverbios 3, 5-6", "Pr 3, 5-6", "Confía en el Señor de todo corazón y no te apoyes en tu propia prudencia.", "V. Enséñame, Señor, tus caminos.", "R. Y guíame por sendas rectas."),
                ourFather = "Padre nuestro, que estás en el cielo...",
                concludingPrayer = "Dios todopoderoso, derrama tu gracia en nuestros corazones al despuntar este nuevo día, para que guiados por tu luz te sirvamos con pureza y verdad. Por Jesucristo nuestro Señor. Amén.",
                blessing = "El Señor nos bendiga, nos guarde de todo mal y nos lleve a la vida eterna. Amén."
            )
            LiturgicalHourType.LAUDES -> HourContent(
                hourName = "Laudes (Oración de la Mañana)",
                ordoRubricNote = "Ordo Colombiano: Alabanza matinal y consagración del trabajo y la jornada.",
                introductoryVerse = "V. Dios mío, ven en mi auxilio.\nR. Señor, date prisa en socorrerme.\nGloria al Padre...",
                hymnTitle = "Alborada de Luz y Esperanza",
                hymnText = "La luz se levanta sobre nuestra tierra colombiana,\nlas montañas y valles bendicen al Creador.\nSeñor Jesús, Luz del mundo, disipa las tinieblas de la discordia\ny enciende en nosotros el fuego de tu amor.",
                psalms = listOf(
                    PsalmItem(
                        title = "Salmo 62, 2-9",
                        subtitle = "El alma sedienta de Dios",
                        antiphon = "Ant. Oh Dios, tú eres mi Dios, por ti madrugo; mi alma está sedienta de ti.",
                        psalmText = "Oh Dios, tú eres mi Dios, por ti madrugo, mi alma está sedienta de ti;\nmi carne tiene ansia de ti, como tierra reseca, agostada, sin agua.\n¡Cómo te contemplaba en el santuario viendo tu fuerza y tu gloria!\nTu gracia vale más que la vida, te alabarán mis labios.",
                        gloriaPatri = "Gloria al Padre, y al Hijo, y al Espíritu Santo..."
                    ),
                    PsalmItem(
                        title = "Cántico de Daniel (Dn 3, 57-88)",
                        subtitle = "Toda la creación alabe al Señor",
                        antiphon = "Ant. Criaturas todas del Señor, bendecid al Señor, ensalzadlo con himnos por los siglos.",
                        psalmText = "Bendecid al Señor, ángeles del Señor; cielos, bendecid al Señor.\nAguas del espacio, bendecid al Señor; ejércitos del cielo, bendecid al Señor.\nSol y luna, bendecid al Señor; astros del cielo, bendecid al Señor.",
                        gloriaPatri = "Bendigamos al Padre, al Hijo y al Espíritu Santo..."
                    )
                ),
                briefReading = ReadingItem("Romanos 12, 1-2", "Rm 12, 1-2", "Os exhorto, hermanos, por la misericordia de Dios, a presentar vuestros cuerpos como ofrenda viva, santa, agradable a Dios.", "V. El Señor es mi pastor, nada me falta.", "R. En verdes praderas me hace recostar."),
                canticleTitle = "Cántico de Zacarías (Benedictus) - Lc 1, 68-79",
                canticleAntiphon = "Ant. Por la entrañable misericordia de nuestro Dios, nos visitará el sol que nace de lo alto.",
                canticleText = "Bendito sea el Señor, Dios de Israel, porque ha visitado y redimido a su pueblo,\nsuscitándonos una fuerza de salvación en la casa de David, su siervo,\nsegún lo había predicho desde antiguo por boca de sus santos profetas.",
                preces = PrecesItem(
                    intro = "Invoquemos a Cristo, luz del mundo y salvador de Colombia, y digámosle con fe:",
                    petitions = listOf(
                        "Por la Iglesia en Colombia y sus pastores" to "Concede sabiduría y valentía a los obispos y sacerdotes para anunciar la paz.",
                        "Por los gobernantes y líderes sociales" to "Que busquen siempre la justicia, el bien común y la protección de los más vulnerables.",
                        "Por los Padres Basilianos y su ministerio educativo y pastoral" to "Que la bondad, disciplina y ciencia florezcan en los corazones de la juventud.",
                        "Por los enfermos, desplazados y necesitados en nuestra patria" to "Sana sus heridas y suscita samaritanos generosos que los acompañen."
                    ),
                    conclusion = "Unidos como hermanos en la fe, oremos como el Maestro nos enseñó:"
                ),
                ourFather = "Padre nuestro, que estás en el cielo, santificado sea tu nombre; venga a nosotros tu reino; hágase tu voluntad en la tierra como en el cielo. Danos hoy nuestro pan de cada día; perdona nuestras ofensas, como también nosotros perdonamos a los que nos ofenden; no nos dejes caer en la tentación, y líbranos del mal. Amén.",
                concludingPrayer = "Señor Dios, rey del cielo y de la tierra, dirige y santifica, gobierna y guía en este día nuestros corazones y nuestros cuerpos, nuestros pensamientos, palabras y acciones conforme a tu ley y al cumplimiento de tus mandamientos, para que aquí y en la eternidad merezcamos ser salvados por ti. Por Jesucristo nuestro Señor. Amén.",
                blessing = "El Señor todopoderoso nos bendiga, nos libre de todo mal y nos lleve a la vida eterna. Amén."
            )
            LiturgicalHourType.VISPERAS -> HourContent(
                hourName = "Vísperas (Oración del Atardecer)",
                ordoRubricNote = "Ordo Colombiano: Acción de gracias por el día transcurrido y ofrenda del descanso.",
                introductoryVerse = "V. Dios mío, ven en mi auxilio.\nR. Señor, date prisa en socorrerme.\nGloria al Padre...",
                hymnTitle = "Luz Alegre de la Santa Gloria",
                hymnText = "Al declinar la luz de esta jornada terrena,\nte bendecimos, Padre de bondad y misericordia.\nQue el Espíritu Santo selle nuestro corazón con su paz,\ny que la noche nos acoja bajo tu santo amparo.",
                psalms = listOf(
                    PsalmItem(
                        title = "Salmo 140, 1-9",
                        subtitle = "Oración vespertina en el peligro",
                        antiphon = "Ant. Suba mi oración, Señor, como incienso en tu presencia; el alzar de mis manos como ofrenda de la tarde.",
                        psalmText = "Señor, te estoy llamando, ven de prisa, escucha mi voz cuando te llamo.\nSuba mi oración como incienso en tu presencia, el alzar de mis manos como ofrenda de la tarde.\nPon, Señor, una guardia a mi boca, un centinela a la puerta de mis labios.",
                        gloriaPatri = "Gloria al Padre, y al Hijo, y al Espíritu Santo..."
                    ),
                    PsalmItem(
                        title = "Cántico del Nuevo Testamento (Flp 2, 6-11)",
                        subtitle = "Cristo, siervo de Dios, en su misterio pascual",
                        antiphon = "Ant. Al nombre de Jesús toda rodilla se doble en el cielo, en la tierra y en el abismo.",
                        psalmText = "Cristo, a pesar de su condición divina, no hizo alarde de su categoría de Dios;\nal contrario, se despojó de su rango y tomó la condición de esclavo,\npasando por uno de tantos. Y así, actuando como un hombre cualquiera, se rebajó hasta someterse incluso a la muerte, y una muerte de cruz.",
                        gloriaPatri = "Gloria al Padre, y al Hijo, y al Espíritu Santo..."
                    )
                ),
                briefReading = ReadingItem("1 Pedro 5, 5-7", "1 P 5, 5-7", "Revestíos todos de humildad en el trato mutuo, porque Dios resiste a los soberbios y da su gracia a los humildes. Descargad en Él todo vuestro agobio, porque Él cuida de vosotros.", "V. El Señor es clemente y misericordioso.", "R. Lento a la cólera y rico en amor."),
                canticleTitle = "Cántico de la Santísima Virgen María (Magnificat) - Lc 1, 46-55",
                canticleAntiphon = "Ant. Proclama mi alma la grandeza del Señor, se alegra mi espíritu en Dios, mi salvador.",
                canticleText = "Proclama mi alma la grandeza del Señor, se alegra mi espíritu en Dios, mi salvador;\nporque ha mirado la humillación de su esclava.\nDesde ahora me felicitarán todas las generaciones,\nporque el Poderoso ha hecho obras grandes por mí: su nombre es santo,\ny su misericordia llega a sus fieles de generación en generación.",
                preces = PrecesItem(
                    intro = "Elevemos nuestras súplicas a Cristo Jesús, que con su muerte y resurrección ilumina la noche de los hombres:",
                    petitions = listOf(
                        "Por las familias de Colombia" to "Sé su fortaleza y consuelo, aleja la violencia y haz florecer el perdón.",
                        "Por los jóvenes y estudiantes" to "Ilumina sus mentes para que consagren sus talentos al servicio del prójimo.",
                        "Por las vocaciones a la Congregación de San Basilio" to "Concede generosidad y fidelidad a quienes llamas a consagrar su vida.",
                        "Por los difuntos, especialmente nuestros hermanos basilianos y bienhechores" to "Acógelos en el banquete celestial de tu Reino eterno."
                    ),
                    conclusion = "Como hijos confiados, digamos la oración que Jesús nos enseñó:"
                ),
                ourFather = "Padre nuestro, que estás en el cielo...",
                concludingPrayer = "Al caer la tarde, Señor, te damos gracias por tus dones inagotables y te pedimos que la luz de tu verdad no se apague jamás en nuestras vidas. Por Jesucristo, nuestro Señor. Amén.",
                blessing = "El Señor nos bendiga, nos guarde de todo mal y nos lleve a la vida eterna. Amén."
            )
            LiturgicalHourType.COMPLETAS -> HourContent(
                hourName = "Completas (Oración antes del Descanso)",
                ordoRubricNote = "Ordo Colombiano: Breve examen de conciencia nocturno y descanso en la paz de Dios.",
                introductoryVerse = "V. Dios mío, ven en mi auxilio.\nR. Señor, date prisa en socorrerme.\nGloria al Padre...",
                examinationOfConscience = "Momento de silencio para revisar ante Dios las obras, palabras y omisiones del día transcurrido. 'Yo confieso ante Dios todopoderoso y ante vosotros, hermanos, que he pecado mucho...'",
                hymnTitle = "Antes que la Luz se Apague (Te Lucis Ante Terminum)",
                hymnText = "Antes que la luz se apague, oh Creador de todas las cosas,\nte pedimos con clemencia que por tu misericordia\nseas nuestro custodio y guardia en la noche.",
                psalms = listOf(
                    PsalmItem(
                        title = "Salmo 90 (91)",
                        subtitle = "Bajo el amparo del Altísimo",
                        antiphon = "Ant. Con sus plumas te cubrirá, bajo sus alas te refugiarás.",
                        psalmText = "Tú que habitas al amparo del Altísimo, que vives a la sombra del Omnipotente,\ndi al Señor: 'Refugio mío, alcázar mío, Dios mío en quien confío'.\nÉl te librará de la red del cazador, de la peste funesta;\nte cubrirá con sus plumas, bajo sus alas te refugiarás.",
                        gloriaPatri = "Gloria al Padre, y al Hijo, y al Espíritu Santo..."
                    )
                ),
                briefReading = ReadingItem("Apocalipsis 22, 4-5", "Ap 22, 4-5", "Verán el rostro del Señor y su nombre estará en sus frentes. La noche no existirá más, ni necesitarán luz de lámpara ni luz del sol, porque el Señor Dios los iluminará.", "V. En tus manos, Señor, encomiendo mi espíritu.", "R. Tú, el Dios leal, nos librarás."),
                canticleTitle = "Cántico de Simeón (Nunc Dimittis) - Lc 2, 29-32",
                canticleAntiphon = "Ant. Sálvanos, Señor, despiertos; guárdanos mientras dormimos, para que velemos con Cristo y descansemos en paz.",
                canticleText = "Ahora, Señor, según tu promesa, puedes dejar a tu siervo irse en paz.\nPorque mis ojos han visto a tu Salvador,\na quien has presentado ante todos los pueblos:\nluz para alumbrar a las naciones y gloria de tu pueblo Israel.",
                ourFather = "Padre nuestro...",
                concludingPrayer = "Visita, Señor, esta habitación nuestra y aleja de ella todas las asechanzas del enemigo; que tus santos ángeles habiten en ella y nos guarden en paz, y que tu bendición esté siempre sobre nosotros. Por Jesucristo nuestro Señor. Amén.",
                blessing = "El Señor todopoderoso nos conceda una noche tranquila y una santa muerte. Amén.",
                marianAntiphon = "Salve, Regina, Mater misericordiae, vita, dulcedo, et spes nostra, salve. Ad te clamamus, exsules filii Hevae. Ad te suspiramus, gementes et flentes in hac lacrimarum valle. Eia ergo, advocata nostra, illos tuos misericordes oculos ad nos converte. Et Iesum, benedictum fructum ventris tui, nobis post hoc exsilium ostende. O clemens, o pia, o dulcis Virgo Maria."
            )
            LiturgicalHourType.OFICIO_DE_LECTURA -> HourContent(
                hourName = "Oficio de Lectura",
                ordoRubricNote = "Ordo Colombiano: Meditación profunda de la Palabra de Dios y textos patrísticos.",
                introductoryVerse = "V. Dios mío, ven en mi auxilio. R. Señor, date prisa en socorrerme.",
                hymnTitle = "Señor de la Sabiduría",
                hymnText = "En el silencio de la noche y del amanecer,\nbuscamos tu rostro, Señor de la Verdad.\nTu Palabra es lámpara para nuestros pasos y luz en nuestro sendero.",
                psalms = listOf(
                    PsalmItem("Salmo 18, 2-7", "Alabanza al Creador del universo", "Ant. El cielo proclama la gloria de Dios.", "El cielo proclama la gloria de Dios, el firmamento pregona la obra de sus manos...", "Gloria al Padre...")
                ),
                briefReading = ReadingItem("Carta a los Colosenses 1, 9-14", "Col 1, 9-14", "No cesamos de orar por vosotros y pedir que alcancéis el pleno conocimiento de su voluntad con toda sabiduría e inteligencia espiritual.", "V. Tu palabra es antorcha para mis pies.", "R. Y luz en mi camino."),
                secondReading = ReadingItem("Tratado de San Basilio Magno sobre el Espíritu Santo", "De Spiritu Sancto, Cap. 9", "A través del Espíritu Santo se nos devuelve el paraíso, el ascenso al Reino de los Cielos, la adopción como hijos. Quien contempla al Espíritu Santo es transformado de claridad en claridad.", "V. Danos, Señor, un corazón sabio.", "R. Para conocer tus maravillas."),
                ourFather = "Padre nuestro...",
                concludingPrayer = "Dios todopoderoso y eterno, que en San Basilio Magno y los santos pastores diste luz admirable a tu Iglesia, concédenos beber de la fuente de tu verdad y dar testimonio de amor fraterno. Por Cristo nuestro Señor. Amén.",
                blessing = "Bendigamos al Señor. Demos gracias a Dios."
            )
            LiturgicalHourType.TERCIA, LiturgicalHourType.SEXTA, LiturgicalHourType.NONA -> HourContent(
                hourName = hourType.titleEs,
                ordoRubricNote = "Ordo Colombiano: Pausa en el trabajo diario para elevar el corazón al Señor.",
                introductoryVerse = "V. Dios mío, ven en mi auxilio. R. Señor, date prisa en socorrerme.",
                hymnTitle = "Consagración de la Hora",
                hymnText = "Oh Dios de bondad infinita, en medio de las fatigas y tareas del día, volvemos a ti los ojos para pedir tu bendición.",
                psalms = listOf(
                    PsalmItem("Salmo 118 (Alef y Bet)", "Amor a la ley divina", "Ant. Tu palabra, Señor, permanece para siempre.", "Dichosos los que con vida intachable caminan en la ley del Señor...", "Gloria al Padre...")
                ),
                briefReading = ReadingItem("Jeremías 17, 7-8", "Jr 17, 7-8", "Bendito quien confía en el Señor y pone en el Señor su esperanza. Será como árbol plantado junto al agua.", "V. En ti, Señor, confío.", "R. No quede yo defraudado para siempre."),
                ourFather = "Padre nuestro...",
                concludingPrayer = "Señor Jesús, que en esta hora del día te ofreciste en la cruz por la redención del mundo, concédenos trabajar con rectitud y amor por tu Reino. Amén.",
                blessing = "Bendigamos al Señor. Demos gracias a Dios."
            )
        }

        val en = when (hourType) {
            LiturgicalHourType.INVITATORIO -> HourContent(
                hourName = "Invitatory",
                ordoRubricNote = "V. Lord, open my lips. R. And my mouth will declare your praise.",
                introductoryVerse = "V. Lord, open my lips.\nR. And my mouth will declare your praise.",
                hymnTitle = "Come Before the Lord",
                hymnText = "Come, let us worship the Lord, wellspring of wisdom and holy grace.\nHe gathers us as one faithful flock in Jesus Christ.",
                psalms = listOf(
                    PsalmItem(
                        title = "Psalm 95 (94)",
                        subtitle = "A call to praise and obedience",
                        antiphon = "Ant. Come, let us worship the Lord who made us.",
                        psalmText = "Come, let us sing to the Lord; let us make a joyful noise to the rock of our salvation!\nLet us come into his presence with thanksgiving; let us make a joyful noise to him with songs of praise!\nFor the Lord is a great God, and a great King above all gods.",
                        gloriaPatri = "Glory to the Father, and to the Son, and to the Holy Spirit, as it was in the beginning, is now, and will be forever. Amen."
                    )
                ),
                briefReading = ReadingItem("Proverbs 3:5-6", "Pr 3:5-6", "Trust in the Lord with all your heart, and do not rely on your own insight.", "V. Teach me your way, O Lord.", "R. And lead me on a level path."),
                ourFather = "Our Father, who art in heaven...",
                concludingPrayer = "Almighty God, pour out your grace upon our hearts at the break of this new day, so that guided by your light we may serve you with pure hearts. Through Christ our Lord. Amen.",
                blessing = "May the Lord bless us, protect us from all evil and bring us to everlasting life. Amen."
            )
            LiturgicalHourType.LAUDES -> HourContent(
                hourName = "Morning Prayer (Lauds)",
                ordoRubricNote = "Colombian Ordo (CEC): Morning praise and offering of work and daily labor.",
                introductoryVerse = "V. God, come to my assistance.\nR. Lord, make haste to help me.\nGlory to the Father...",
                hymnTitle = "Now that the Daylight Fills the Sky",
                hymnText = "Now that the daylight fills the sky, we lift our hearts to God on high,\nthat he, in all we do or say, would keep us free from harm today.\nMay light divine on Colombia shine, and peace in every heart recline.",
                psalms = listOf(
                    PsalmItem(
                        title = "Psalm 63:2-9",
                        subtitle = "The soul thirsting for God",
                        antiphon = "Ant. O God, you are my God, for you I long; my body pines for you.",
                        psalmText = "O God, you are my God, for you I long; for you my soul is thirsting.\nMy body pines for you like a dry, weary land without water.\nSo I gaze on you in the sanctuary to see your strength and your glory.\nFor your love is better than life, my lips will speak your praise.",
                        gloriaPatri = "Glory to the Father, and to the Son, and to the Holy Spirit..."
                    ),
                    PsalmItem(
                        title = "Canticle of Daniel (Dn 3:57-88)",
                        subtitle = "All creation, praise the Lord",
                        antiphon = "Ant. All you works of the Lord, bless the Lord; praise and exalt him above all forever.",
                        psalmText = "Bless the Lord, all you works of the Lord; praise and exalt him above all forever.\nAngels of the Lord, bless the Lord; you heavens, bless the Lord.\nSun and moon, bless the Lord; stars of heaven, bless the Lord.",
                        gloriaPatri = "Let us bless the Father and the Son with the Holy Spirit..."
                    )
                ),
                briefReading = ReadingItem("Romans 12:1-2", "Rom 12:1-2", "I appeal to you, brothers and sisters, by the mercies of God, to present your bodies as a living sacrifice, holy and acceptable to God.", "V. The Lord is my shepherd, I shall not want.", "R. In green pastures he makes me lie down."),
                canticleTitle = "Canticle of Zechariah (Benedictus) - Luke 1:68-79",
                canticleAntiphon = "Ant. Through the tender mercy of our God, the dawn from on high shall break upon us.",
                canticleText = "Blessed be the Lord, the God of Israel; he has come to his people and set them free.\nHe has raised up for us a mighty savior, born of the house of his servant David.\nThrough his holy prophets he promised of old that he would save us from our enemies.",
                preces = PrecesItem(
                    intro = "Let us call upon Christ, Light of the world and Savior of Colombia:",
                    petitions = listOf(
                        "For the Church in Colombia and its pastors" to "Grant wisdom and courage to our bishops and priests to proclaim reconciliation and peace.",
                        "For civic authorities and community leaders" to "May they uphold justice, the common good, and protect the vulnerable.",
                        "For the Basilian Fathers in their educational and pastoral apostolates" to "May goodness, discipline, and knowledge take root in youthful hearts.",
                        "For the sick, displaced families, and poor in our nation" to "Heal their wounds and send generous hands to comfort them."
                    ),
                    conclusion = "United as brothers and sisters in faith, let us pray as our Savior taught us:"
                ),
                ourFather = "Our Father, who art in heaven, hallowed be thy name; thy kingdom come; thy will be done on earth as it is in heaven. Give us this day our daily bread; and forgive us our trespasses, as we forgive those who trespass against us; and lead us not into temptation, but deliver us from evil. Amen.",
                concludingPrayer = "Lord God, King of heaven and earth, direct and sanctify, rule and govern our hearts and bodies this day in your law and in the works of your commandments, that here and in eternity we may be preserved and saved by your grace. Through Christ our Lord. Amen.",
                blessing = "May the Lord bless us, protect us from all evil, and bring us to everlasting life. Amen."
            )
            LiturgicalHourType.VISPERAS -> HourContent(
                hourName = "Evening Prayer (Vespers)",
                ordoRubricNote = "Colombian Ordo (CEC): Evening thanksgiving for the gifts of the day and supplication for peace.",
                introductoryVerse = "V. God, come to my assistance.\nR. Lord, make haste to help me.\nGlory to the Father...",
                hymnTitle = "O Gladsome Light of Holy Glory",
                hymnText = "O gladsome light of holy glory, radiant splendor of the immortal Father,\nwe sing of your salvation as eventide descends.\nMay the Holy Spirit envelop our homeland with peace and fraternal concord.",
                psalms = listOf(
                    PsalmItem(
                        title = "Psalm 141 (140):1-9",
                        subtitle = "Evening prayer in time of trial",
                        antiphon = "Ant. Let my prayer rise like incense before you, O Lord; the lifting up of my hands like an evening offering.",
                        psalmText = "I have called to you, Lord; hasten to help me! Hear my voice when I cry to you.\nLet my prayer arise before you like incense, the raising of my hands like an evening sacrifice.\nSet, O Lord, a guard over my mouth; keep watch at the door of my lips.",
                        gloriaPatri = "Glory to the Father, and to the Son, and to the Holy Spirit..."
                    ),
                    PsalmItem(
                        title = "New Testament Canticle (Philippians 2:6-11)",
                        subtitle = "Christ, the servant of God",
                        antiphon = "Ant. At the name of Jesus every knee shall bend in heaven and on earth.",
                        psalmText = "Though he was in the form of God, Jesus did not deem equality with God something to be grasped at.\nRather, he emptied himself, taking the form of a slave, coming in human likeness.\nAnd being found in human appearance, he humbled himself, becoming obedient to death, even death on a cross.",
                        gloriaPatri = "Glory to the Father, and to the Son, and to the Holy Spirit..."
                    )
                ),
                briefReading = ReadingItem("1 Peter 5:5-7", "1 Pt 5:5-7", "Clothe yourselves with humility in your dealings with one another, for God opposes the proud, but gives grace to the humble. Cast all your anxiety on him, because he cares for you.", "V. The Lord is gracious and merciful.", "R. Slow to anger and abounding in love."),
                canticleTitle = "Canticle of Mary (Magnificat) - Luke 1:46-55",
                canticleAntiphon = "Ant. My soul proclaims the greatness of the Lord, for the Almighty has done great things for me.",
                canticleText = "My soul proclaims the greatness of the Lord, my spirit rejoices in God my Savior;\nfor he has looked with favor on his lowly servant.\nFrom this day all generations will call me blessed:\nthe Almighty has done great things for me, and holy is his Name.\nHe has mercy on those who fear him in every generation.",
                preces = PrecesItem(
                    intro = "Let us lift our evening supplications to Jesus Christ, the eternal light that never fades:",
                    petitions = listOf(
                        "For the families of Colombia" to "Be their sanctuary and refuge, banish domestic violence and heal every division.",
                        "For teachers, catechists, and students" to "Enlighten them with wisdom, virtue, and zeal for Christian education.",
                        "For vocations to the Congregation of St. Basil" to "Inspire young men to respond courageously to the call of the priesthood and religious life.",
                        "For our deceased brothers, benefactors, and loved ones" to "Welcome them into the perpetual light and joy of your heavenly banquet."
                    ),
                    conclusion = "As brothers and sisters, we pray the words our Savior gave us:"
                ),
                ourFather = "Our Father, who art in heaven...",
                concludingPrayer = "As the light of day departs, Lord, we thank you for your bountiful gifts and pray that your peace may guard our minds and hearts in Christ Jesus. Who lives and reigns with you and the Holy Spirit, God, forever and ever. Amen.",
                blessing = "May the Lord bless us, protect us from all evil, and bring us to everlasting life. Amen."
            )
            LiturgicalHourType.COMPLETAS -> HourContent(
                hourName = "Night Prayer (Compline)",
                ordoRubricNote = "Colombian Ordo (CEC): Nightly examination of conscience and resting under God's protection.",
                introductoryVerse = "V. God, come to my assistance.\nR. Lord, make haste to help me.\nGlory to the Father...",
                examinationOfConscience = "Brief pause for examination of conscience before God: 'I confess to almighty God and to you, my brothers and sisters, that I have greatly sinned in my thoughts and in my words...'",
                hymnTitle = "To You, Before the Close of Day (Te Lucis Ante Terminum)",
                hymnText = "To you, before the close of day, Creator of all things, we pray\nthat in your constant saving love, you guard us from your throne above.",
                psalms = listOf(
                    PsalmItem(
                        title = "Psalm 91 (90)",
                        subtitle = "Safe in God's sheltering care",
                        antiphon = "Ant. He will conceal you with his wings; you will not fear the terror of the night.",
                        psalmText = "He who dwells in the shelter of the Most High and abides in the shade of the Almighty\nsays to the Lord: 'My refuge, my stronghold, my God in whom I trust!'\nHe will free you from the snare of the fowler, from the destroying pestilence;\nhe will conceal you with his pinions and under his wings you will find refuge.",
                        gloriaPatri = "Glory to the Father, and to the Son, and to the Holy Spirit..."
                    )
                ),
                briefReading = ReadingItem("Revelation 22:4-5", "Rev 22:4-5", "They will look upon his face, and his name will be on their foreheads. Night will be no more, nor will they need light from lamp or sun, for the Lord God shall give them light.", "V. Into your hands, Lord, I commend my spirit.", "R. You have redeemed us, Lord God of truth."),
                canticleTitle = "Canticle of Simeon (Nunc Dimittis) - Luke 2:29-32",
                canticleAntiphon = "Ant. Protect us, Lord, as we stay awake; watch over us as we sleep, that awake we may keep watch with Christ, and asleep, rest in his peace.",
                canticleText = "Now, Master, you may let your servant go in peace, according to your word,\nfor my eyes have seen your salvation,\nwhich you prepared in sight of all the peoples:\na light for revelation to the Gentiles, and glory for your people Israel.",
                ourFather = "Our Father...",
                concludingPrayer = "Visit this house, O Lord, and drive far from it all snares of the enemy. May your holy angels dwell here to keep us in peace, and may your blessing be always upon us. Through Christ our Lord. Amen.",
                blessing = "May the all-powerful Lord grant us a restful night and a peaceful death. Amen.",
                marianAntiphon = "Hail, Holy Queen, Mother of Mercy, our life, our sweetness, and our hope. To you do we cry, poor banished children of Eve. To you do we send up our sighs, mourning and weeping in this valley of tears. Turn then, most gracious advocate, your eyes of mercy toward us, and after this our exile, show unto us the blessed fruit of your womb, Jesus. O clement, O loving, O sweet Virgin Mary."
            )
            LiturgicalHourType.OFICIO_DE_LECTURA -> HourContent(
                hourName = "Office of Readings",
                ordoRubricNote = "Colombian Ordo (CEC): Deep meditation on the Word of God and patristic writings.",
                introductoryVerse = "V. God, come to my assistance. R. Lord, make haste to help me.",
                hymnTitle = "Eternal Lord of Wisdom",
                hymnText = "In the silent stillness of dawn and night, we seek your holy face, O Lord of truth.\nYour Word is a lamp unto our feet and a light unto our path.",
                psalms = listOf(
                    PsalmItem("Psalm 19:2-7", "Praise to the Creator", "Ant. The heavens proclaim the glory of God.", "The heavens declare the glory of God; the skies proclaim the work of his hands...", "Glory to the Father...")
                ),
                briefReading = ReadingItem("Colossians 1:9-14", "Col 1:9-14", "We do not cease praying for you and asking that you may be filled with the knowledge of God's will in all spiritual wisdom and understanding.", "V. Your word is a lamp to my feet.", "R. And a light to my path."),
                secondReading = ReadingItem("From the Treatise by St. Basil the Great on the Holy Spirit", "On the Holy Spirit, Ch. 9", "Through the Holy Spirit comes our restoration to paradise, our ascension into the kingdom of heaven, our return to the adoption of sons. The Spirit grants us wisdom and divine light.", "V. Grant us, Lord, a wise and discerning heart.", "R. To proclaim your marvelous deeds."),
                ourFather = "Our Father...",
                concludingPrayer = "Almighty and eternal God, who made Saint Basil the Great an illustrious teacher of truth, grant that we may drink from the fountain of your wisdom. Through Christ our Lord. Amen.",
                blessing = "Let us bless the Lord. Thanks be to God."
            )
            LiturgicalHourType.TERCIA, LiturgicalHourType.SEXTA, LiturgicalHourType.NONA -> HourContent(
                hourName = hourType.titleEn,
                ordoRubricNote = "Colombian Ordo (CEC): Midday pause to offer our labor to the Lord.",
                introductoryVerse = "V. God, come to my assistance. R. Lord, make haste to help me.",
                hymnTitle = "Midday Dedication",
                hymnText = "O God of infinite goodness, amidst the labor of this day, we turn our gaze to you.",
                psalms = listOf(
                    PsalmItem("Psalm 119", "Love for the divine commandments", "Ant. Your word, Lord, stands firm forever.", "Blessed are those whose way is blameless, who walk in the law of the Lord...", "Glory to the Father...")
                ),
                briefReading = ReadingItem("Jeremiah 17:7-8", "Jer 17:7-8", "Blessed are those who trust in the Lord, whose trust is the Lord. They shall be like a tree planted by water.", "V. In you, O Lord, I place my trust.", "R. Let me never be put to shame."),
                ourFather = "Our Father...",
                concludingPrayer = "Lord Jesus Christ, who at this hour stretched out your arms upon the cross for our salvation, guide our work in your love and peace. Amen.",
                blessing = "Let us bless the Lord. Thanks be to God."
            )
        }

        return BilingualLiturgicalHour(hourType, es, en)
    }

    // --- COLOMBIAN SOLEMNITIES & FEASTS ACCORDING TO CEC ORDO ---

    val chiquinquiraSolemnity: ColombianLiturgicalDay by lazy {
        val hours = mutableMapOf<LiturgicalHourType, BilingualLiturgicalHour>()
        LiturgicalHourType.values().forEach { hourType ->
            val es = HourContent(
                hourName = "${hourType.titleEs} • Nuestra Señora de Chiquinquirá",
                ordoRubricNote = "Ordo CEC Colombia: Solemnidad nacional. Patrona principal de Colombia. Oficio de la solemnidad.",
                introductoryVerse = "V. Dios mío, ven en mi auxilio. R. Señor, date prisa en socorrerme.",
                hymnTitle = "Himno a la Reina y Patrona de Colombia",
                hymnText = "Reina de Colombia por siempre serás;\nes este tu pueblo que te ama y te canta.\nEn Chiquinquirá renovaste tu lienzo sagrado,\nrenueva en la patria la paz y la concordia fraterna.",
                psalms = listOf(
                    PsalmItem("Salmo 44, 11-18", "La reina a la derecha del Rey", "Ant. Virgen santísima de Chiquinquirá, ruega por nosotros ante el trono de Dios.", "Escucha, hija, mira: inclina el oído, olvida tu pueblo y la casa paterna...", "Gloria al Padre...")
                ),
                briefReading = ReadingItem("Isaías 61, 10", "Is 61, 10", "Desbordo de gozo con el Señor, y me alegro con mi Dios: porque me ha vestido un traje de gala y me ha envuelto en un manto de triunfo.", "V. Elegida de Dios y bendita.", "R. La Virgen María intercede por Colombia."),
                canticleTitle = "Cántico de Zacarías / Magníficat",
                canticleAntiphon = "Ant. Dichosa eres tú, Virgen María, que creíste al Señor; por ti nos vino la reconciliación.",
                canticleText = "Proclama mi alma la grandeza del Señor...",
                preces = PrecesItem("A María, intercesora y patrona de Colombia, encomendemos nuestra patria:", listOf("Por la paz en cada región y vereda de Colombia" to "Reina de la paz, aleja la discordia y reúne a tus hijos en el amor.", "Por la Iglesia en Colombia y la misión de los Padres Basilianos" to "Haz fecundo su servicio a la juventud y a los necesitados."), "Oremos con el Padre Nuestro:"),
                ourFather = "Padre nuestro...",
                concludingPrayer = "Dios todopoderoso y eterno, que concediste al pueblo colombiano la maternal protección de la Virgen María en su milagrosa advocación de Chiquinquirá, haz que guiados por su intercesión construyamos caminos de paz, verdad y reconciliación. Por Jesucristo nuestro Señor. Amén.",
                blessing = "El Señor bendiga a Colombia y nos guarde en su paz. Amén."
            )
            val en = HourContent(
                hourName = "${hourType.titleEn} • Our Lady of Chiquinquirá",
                ordoRubricNote = "Colombian Ordo (CEC): National Solemnity. Principal Patroness of Colombia. Proper Office.",
                introductoryVerse = "V. God, come to my assistance. R. Lord, make haste to help me.",
                hymnTitle = "Hymn to Our Lady, Queen of Colombia",
                hymnText = "Queen of Colombia forever you shall reign,\nyour children praise you with songs of filial devotion.\nAt Chiquinquirá you renewed your miraculous canvas,\nrenew in our nation the dawn of lasting peace.",
                psalms = listOf(
                    PsalmItem("Psalm 45:11-18", "The queen in gold of Ophir", "Ant. Most holy Virgin of Chiquinquirá, intercede for Colombia before the throne of grace.", "Hear, O daughter, and consider, incline your ear: forget your people and your father's house...", "Glory to the Father...")
                ),
                briefReading = ReadingItem("Isaiah 61:10", "Is 61:10", "I will greatly rejoice in the Lord, my whole being shall exult in my God; for he has clothed me with garments of salvation.", "V. Blessed are you among women.", "R. And blessed is the fruit of your womb."),
                canticleTitle = "Benedictus / Magnificat",
                canticleAntiphon = "Ant. Blessed are you, Virgin Mary, for you believed that the Lord's promise would be fulfilled.",
                canticleText = "My soul proclaims the greatness of the Lord...",
                preces = PrecesItem("Through Mary, Patroness of Colombia, let us bring our petitions to Christ:", listOf("For peace and justice across Colombia" to "Queen of Peace, heal our land and unite your children in love.", "For the Basilian Fathers and their mission" to "Bless their apostolate with faith and apostolic zeal."), "Our Father:"),
                ourFather = "Our Father, who art in heaven...",
                concludingPrayer = "Almighty God, who bestowed upon the people of Colombia the tender patronage of the Blessed Virgin Mary under the title of Chiquinquirá, grant that by her prayers we may advance the kingdom of justice and peace. Through Christ our Lord. Amen.",
                blessing = "May the Lord bless Colombia and keep us in his peace. Amen."
            )
            hours[hourType] = BilingualLiturgicalHour(hourType, es, en)
        }
        ColombianLiturgicalDay(
            id = "chiquinquira",
            dateDisplayEs = "9 de Julio • Solemnidad Nacional",
            dateDisplayEn = "July 9 • National Solemnity",
            titleEs = "Nuestra Señora del Rosario de Chiquinquirá, Reina y Patrona de Colombia",
            titleEn = "Our Lady of the Rosary of Chiquinquirá, Queen and Patroness of Colombia",
            rankEs = "Solemnidad Nacional • Ordo CEC Colombia (Color Blanco)",
            rankEn = "National Solemnity • CEC Colombia Ordo (White Vestments)",
            color = LiturgicalColor.BLANCO,
            season = LiturgicalSeason.TIEMPO_ORDINARIO,
            psalterWeekEs = "Salterio de la Solemnidad",
            psalterWeekEn = "Solemnity Psalter",
            ordoNotesEs = "Ordo CEC: Solemnidad de precepto en Colombia. Misa propia con Gloria y Credo. En la Liturgia de las Horas se toman todos los elementos del Común de la Virgen María con antífonas y oraciones propias de Chiquinquirá.",
            ordoNotesEn = "CEC Ordo: Holy day of obligation in Colombia. Proper Mass with Gloria and Creed. Breviary from the Common of the Blessed Virgin Mary with proper antiphons and collects for Chiquinquirá.",
            basilianConnectionEs = "Los Padres Basilianos veneran entrañablemente a la Virgen de Chiquinquirá, consagrando a su amparo sus centros educativos y comunidades en Colombia.",
            basilianConnectionEn = "The Basilian Fathers venerate Our Lady of Chiquinquirá, entrusting their schools and communities in Colombia to her maternal care.",
            hours = hours
        )
    }

    val sanPedroClaverFeast: ColombianLiturgicalDay by lazy {
        val hours = mutableMapOf<LiturgicalHourType, BilingualLiturgicalHour>()
        LiturgicalHourType.values().forEach { hourType ->
            val es = HourContent(
                hourName = "${hourType.titleEs} • San Pedro Claver",
                ordoRubricNote = "Ordo CEC Colombia: Fiesta nacional. Apóstol de los esclavos y patrono de los Derechos Humanos en Colombia.",
                introductoryVerse = "V. Dios mío, ven en mi auxilio. R. Señor, date prisa en socorrerme.",
                hymnTitle = "Siervo de los Esclavos para Siempre",
                hymnText = "En Cartagena de Indias tu caridad brilló como un faro divino,\nlavaste las heridas del hermano marginado y esclavo.\nSan Pedro Claver, enséñanos a amar sin medida,\ny a reconocer en cada ser humano la dignidad del hijo de Dios.",
                psalms = listOf(
                    PsalmItem("Salmo 111", "Dicha del justo y caritativo", "Ant. San Pedro Claver se hizo esclavo de los esclavos por amor a Cristo.", "Dichoso quien teme al Señor y ama de corazón sus mandatos...", "Gloria al Padre...")
                ),
                briefReading = ReadingItem("1 Corintios 9, 19.22", "1 Co 9, 19.22", "Me he hecho esclavo de todos para ganar a los más posibles. Me he hecho débil con los débiles para ganar a los débiles.", "V. El justo se compadece y da con generosidad.", "R. Su recuerdo perdurará por los siglos."),
                canticleTitle = "Benedictus / Magníficat",
                canticleAntiphon = "Ant. Cuanto hicisteis a uno de estos hermanos míos más pequeños, a mí me lo hicisteis.",
                canticleText = "Bendito sea el Señor, Dios de Israel...",
                preces = PrecesItem("Por intercesión de San Pedro Claver, clamemos a Dios Padre:", listOf("Por la defensa incondicional de los Derechos Humanos en Colombia" to "Haznos defensores valientes de la dignidad de todo hombre y mujer.", "Por las comunidades afrodescendientes e indígenas" to "Que sean respetados sus derechos, tierras y riquezas culturales."), "Padre Nuestro:"),
                ourFather = "Padre nuestro...",
                concludingPrayer = "Oh Dios, que hiciste a San Pedro Claver esclavo de los esclavos para llevarles tu consuelo y tu fe, concédenos por su ejemplo e intercesión buscar en todos a Cristo y amar a nuestro prójimo con obras y de verdad. Por Jesucristo nuestro Señor. Amén.",
                blessing = "El Señor bendiga nuestro empeño por la caridad y la justicia. Amén."
            )
            val en = HourContent(
                hourName = "${hourType.titleEn} • St. Peter Claver",
                ordoRubricNote = "Colombian Ordo (CEC): National Feast. Slave of the slaves and patron of Human Rights.",
                introductoryVerse = "V. God, come to my assistance. R. Lord, make haste to help me.",
                hymnTitle = "Servant of the Enslaved Forever",
                hymnText = "In Cartagena de Indias your charity shone as a holy beacon,\nwashing the sores of the oppressed and captive brothers.\nSt. Peter Claver, teach us to love without condition,\nand behold in every human person the divine likeness of God.",
                psalms = listOf(
                    PsalmItem("Psalm 112", "Blessing of the generous just man", "Ant. St. Peter Claver became the slave of the slaves for the love of Christ.", "Praise the Lord! Blessed is the man who fears the Lord, who greatly delights in his commandments...", "Glory to the Father...")
                ),
                briefReading = ReadingItem("1 Corinthians 9:19,22", "1 Cor 9:19,22", "Though I am free from all men, I have made myself a slave to all, that I might win more of them.", "V. The righteous shows mercy and gives freely.", "R. His memory will endure forever."),
                canticleTitle = "Benedictus / Magnificat",
                canticleAntiphon = "Ant. Whatever you did for one of the least of these brothers of mine, you did for me.",
                canticleText = "Blessed be the Lord, the God of Israel...",
                preces = PrecesItem("Through the intercession of St. Peter Claver, let us pray:", listOf("For human rights and racial justice in Colombia" to "Make us fearless advocates for the marginalized.", "For the Basilian apostolate with the vulnerable" to "Infuse our ministry with genuine compassion."), "Our Father:"),
                ourFather = "Our Father, who art in heaven...",
                concludingPrayer = "O God, who made Saint Peter Claver a slave of slaves to comfort them with your truth and love, grant through his intercession that we may seek Christ in our neighbor and serve him in spirit and truth. Through Christ our Lord. Amen.",
                blessing = "May the Lord bless our efforts for justice and reconciliation. Amen."
            )
            hours[hourType] = BilingualLiturgicalHour(hourType, es, en)
        }
        ColombianLiturgicalDay(
            id = "pedro_claver",
            dateDisplayEs = "9 de Septiembre • Fiesta Nacional",
            dateDisplayEn = "September 9 • National Feast",
            titleEs = "San Pedro Claver, Presbítero • Apóstol de los Esclavos",
            titleEn = "Saint Peter Claver, Priest • Apostle of the Slaves",
            rankEs = "Fiesta Nacional • Ordo CEC Colombia (Color Blanco)",
            rankEn = "National Feast • CEC Colombia Ordo (White Vestments)",
            color = LiturgicalColor.BLANCO,
            season = LiturgicalSeason.TIEMPO_ORDINARIO,
            psalterWeekEs = "Salterio de la Fiesta",
            psalterWeekEn = "Feast Psalter",
            ordoNotesEs = "Ordo CEC: Fiesta en toda Colombia. Día Nacional de los Derechos Humanos. Oficio de pastores o de santos varones con textos propios.",
            ordoNotesEn = "CEC Ordo: Feast throughout Colombia. National Human Rights Day. Office of Pastors with proper prayers.",
            basilianConnectionEs = "Los Basilianos encuentran en San Pedro Claver el modelo sublime de entrega sacerdotal y educación libertadora para los desposeídos.",
            basilianConnectionEn = "The Basilians revere St. Peter Claver as a shining model of priestly self-giving and liberating education for the oppressed.",
            hours = hours
        )
    }

    val santaLauraMontoyaFeast: ColombianLiturgicalDay by lazy {
        val hours = mutableMapOf<LiturgicalHourType, BilingualLiturgicalHour>()
        LiturgicalHourType.values().forEach { hourType ->
            val es = HourContent(
                hourName = "${hourType.titleEs} • Santa Laura Montoya",
                ordoRubricNote = "Ordo CEC Colombia: Fiesta en Colombia. Primera Santa Colombiana, madre de los indígenas de la selva.",
                introductoryVerse = "V. Dios mío, ven en mi auxilio. R. Señor, date prisa en socorrerme.",
                hymnTitle = "Madre Laura, Flor de las Selvas",
                hymnText = "Desde Jericó hasta el Dabeiba profundo,\ncaminaste descalza llevando el Evangelio con amor maternal.\nSanta Laura Montoya, maestra intrépida y santa,\nintercede por las mujeres y comunidades originarias de Colombia.",
                psalms = listOf(
                    PsalmItem("Salmo 33", "El Señor cuida de sus fieles", "Ant. Santa Laura proclamó la misericordia de Dios en las selvas de Colombia.", "Bendigo al Señor en todo momento, su alabanza está siempre en mi boca...", "Gloria al Padre...")
                ),
                briefReading = ReadingItem("Gálatas 2, 20", "Ga 2, 20", "Estoy crucificado con Cristo; vivo, pero no soy yo el que vive, es Cristo quien vive en mí.", "V. Sediento estoy de ti, Dios de mi vida.", "R. Guíame a las almas que aguardan tu luz."),
                canticleTitle = "Benedictus / Magníficat",
                canticleAntiphon = "Ant. ¡Tengo sed, Señor, de hacerte conocer entre los más abandonados!",
                canticleText = "Proclama mi alma la grandeza del Señor...",
                preces = PrecesItem("Por intercesión de Santa Laura Montoya, oremos:", listOf("Por los pueblos indígenas y campesinos de Colombia" to "Que sus culturas y derechos sean protegidos con amor fraterno.", "Por los educadores y catequistas" to "Infúndeles paciencia, pedagogía y fe inquebrantable."), "Padre Nuestro:"),
                ourFather = "Padre nuestro...",
                concludingPrayer = "Dios de infinita bondad, que suscitaste en Santa Laura Montoya un celo ardiente para evangelizar a los pueblos indígenas de Colombia, concédenos por su ejemplo gastar la vida en anunciar a Cristo y formar a las nuevas generaciones en la verdad y el amor. Por Cristo nuestro Señor. Amén.",
                blessing = "El Señor nos colme de celo misionero. Amén."
            )
            val en = HourContent(
                hourName = "${hourType.titleEn} • St. Laura Montoya",
                ordoRubricNote = "Colombian Ordo (CEC): Feast in Colombia. First Colombian Saint, mother of the indigenous peoples.",
                introductoryVerse = "V. God, come to my assistance. R. Lord, make haste to help me.",
                hymnTitle = "Mother Laura, Flower of the Rainforest",
                hymnText = "From Jericó to the remote forests of Dabeiba,\nyou walked tirelessly bearing the Gospel with motherly warmth.\nSaint Laura Montoya, fearless teacher and apostle of indigenous souls,\npray for Colombia and all educators of the faith.",
                psalms = listOf(
                    PsalmItem("Psalm 34", "The Lord delivers the humble", "Ant. Saint Laura proclaimed God's saving love to the indigenous peoples.", "I will bless the Lord at all times; his praise shall continually be in my mouth...", "Glory to the Father...")
                ),
                briefReading = ReadingItem("Galatians 2:20", "Gal 2:20", "I have been crucified with Christ; it is no longer I who live, but Christ who lives in me.", "V. My soul is thirsting for you, O Lord.", "R. Lead me to those who hunger for your love."),
                canticleTitle = "Benedictus / Magnificat",
                canticleAntiphon = "Ant. I thirst, Lord, to make you known among the most forgotten!",
                canticleText = "My soul proclaims the greatness of the Lord...",
                preces = PrecesItem("Through Saint Laura Montoya, let us raise our prayers:", listOf("For indigenous and rural communities in Colombia" to "May their dignity and heritage be cherished and defended.", "For Catholic teachers and missionaries" to "Grant them patience, pedagogical wisdom, and missionary fervor."), "Our Father:"),
                ourFather = "Our Father, who art in heaven...",
                concludingPrayer = "O God, who gave Saint Laura Montoya an ardent missionary zeal to bring your word to the indigenous peoples of Colombia, grant through her intercession that we may spend our lives teaching your truth and serving the poor. Through Christ our Lord. Amen.",
                blessing = "May the Lord fill our hearts with apostolic zeal. Amen."
            )
            hours[hourType] = BilingualLiturgicalHour(hourType, es, en)
        }
        ColombianLiturgicalDay(
            id = "laura_montoya",
            dateDisplayEs = "21 de Octubre • Fiesta Nacional",
            dateDisplayEn = "October 21 • National Feast",
            titleEs = "Santa Laura Montoya Upegui, Virgen • Primera Santa Colombiana",
            titleEn = "Saint Laura Montoya Upegui, Virgin • First Colombian Saint",
            rankEs = "Fiesta Nacional • Ordo CEC Colombia (Color Blanco)",
            rankEn = "National Feast • CEC Colombia Ordo (White Vestments)",
            color = LiturgicalColor.BLANCO,
            season = LiturgicalSeason.TIEMPO_ORDINARIO,
            psalterWeekEs = "Salterio de la Fiesta",
            psalterWeekEn = "Feast Psalter",
            ordoNotesEs = "Ordo CEC: Fiesta en todas las diócesis de Colombia. Oficio de vírgenes o santos educadores con oraciones propias aprobadas por la Santa Sede y la CEC.",
            ordoNotesEn = "CEC Ordo: Feast in all Colombian dioceses. Office of Virgins or Educators with proper prayers approved by the Holy See and CEC.",
            basilianConnectionEs = "La vocación pedagógica de Santa Laura Montoya resuena en el lema basiliano: «Bonitatem et disciplinam et scientiam doce me» (Salmo 118).",
            basilianConnectionEn = "Saint Laura's educational mission reflects the Basilian motto: 'Teach me goodness, discipline, and knowledge' (Psalm 119).",
            hours = hours
        )
    }

    val sanLuisBeltranFeast: ColombianLiturgicalDay by lazy {
        val hours = mutableMapOf<LiturgicalHourType, BilingualLiturgicalHour>()
        LiturgicalHourType.values().forEach { hourType ->
            val es = HourContent(
                hourName = "${hourType.titleEs} • San Luis Beltrán",
                ordoRubricNote = "Ordo CEC Colombia: Fiesta en Colombia. Patrono principal de la Nueva Granada y de Colombia.",
                introductoryVerse = "V. Dios mío, ven en mi auxilio. R. Señor, date prisa en socorrerme.",
                hymnTitle = "Predicador Fiel en el Nuevo Mundo",
                hymnText = "En las tierras del Caribe y las riberas del Magdalena,\ntu palabra encendió la fe con el fuego de la justicia.\nSan Luis Beltrán, patrono de Colombia,\nprotege a este pueblo que te venera como pastor de almas.",
                psalms = listOf(
                    PsalmItem("Salmo 95", "El Señor rey del universo", "Ant. Id por todo el mundo y proclamad el Evangelio.", "Cantad al Señor un cántico nuevo, cantad al Señor toda la tierra...", "Gloria al Padre...")
                ),
                briefReading = ReadingItem("2 Timoteo 4, 1-2", "2 Tm 4, 1-2", "Proclama la palabra, insiste a tiempo y a destiempo, reprende, reprocha, exhorta con toda paciencia y doctrina.", "V. Qué hermosos son sobre los montes.", "R. Los pies del mensajero que anuncia la paz."),
                canticleTitle = "Benedictus / Magníficat",
                canticleAntiphon = "Ant. San Luis Beltrán bautizó y defendió a los indefensos en tierras colombianas.",
                canticleText = "Bendito sea el Señor, Dios de Israel...",
                preces = PrecesItem("Por intercesión de San Luis Beltrán, suplicamos:", listOf("Por los misioneros en Colombia y América Latina" to "Infúndeles audacia apostólica y caridad.", "Por la erradicación de toda injusticia y opresión" to "Que brille la verdad del Evangelio."), "Padre Nuestro:"),
                ourFather = "Padre nuestro...",
                concludingPrayer = "Dios todopoderoso, que hiciste a San Luis Beltrán un predicador admirable del Evangelio en las tierras de Colombia, concédenos por su intercesión permanecer firmes en la fe católica y dar frutos de santidad. Por Cristo nuestro Señor. Amén.",
                blessing = "El Señor bendiga nuestra vocación misionera. Amén."
            )
            val en = HourContent(
                hourName = "${hourType.titleEn} • St. Louis Bertrand",
                ordoRubricNote = "Colombian Ordo (CEC): Feast in Colombia. Principal Patron of Colombia.",
                introductoryVerse = "V. God, come to my assistance. R. Lord, make haste to help me.",
                hymnTitle = "Faithful Preacher of the New World",
                hymnText = "Across the Caribbean coasts and the banks of the Magdalena,\nyour preaching ignited the light of Christ with zealous justice.\nSaint Louis Bertrand, patron of Colombia,\nprotect the faith of this beloved nation.",
                psalms = listOf(
                    PsalmItem("Psalm 96", "The Lord is King of all the earth", "Ant. Go into the whole world and proclaim the good news.", "O sing to the Lord a new song; sing to the Lord, all the earth...", "Glory to the Father...")
                ),
                briefReading = ReadingItem("2 Timothy 4:1-2", "2 Tim 4:1-2", "Proclaim the message; be persistent whether the time is favorable or unfavorable; convince, rebuke, and encourage, with the utmost patience and teaching.", "V. How beautiful upon the mountains.", "R. Are the feet of the messenger who announces peace."),
                canticleTitle = "Benedictus / Magnificat",
                canticleAntiphon = "Ant. Saint Louis Bertrand baptized and defended the humble in Colombia.",
                canticleText = "Blessed be the Lord, the God of Israel...",
                preces = PrecesItem("Through Saint Louis Bertrand, let us pray:", listOf("For missionaries in Colombia" to "Inspire them with pastoral courage.", "For justice and unity in the Americas" to "Guide leaders in the light of the Gospel."), "Our Father:"),
                ourFather = "Our Father, who art in heaven...",
                concludingPrayer = "Almighty God, who made Saint Louis Bertrand an illustrious preacher of your Gospel in Colombia, grant by his prayers that we may remain steadfast in true faith and abound in charity. Through Christ our Lord. Amen.",
                blessing = "May the Lord bless our missionary journey. Amen."
            )
            hours[hourType] = BilingualLiturgicalHour(hourType, es, en)
        }
        ColombianLiturgicalDay(
            id = "luis_beltran",
            dateDisplayEs = "9 de Octubre • Fiesta en Colombia",
            dateDisplayEn = "October 9 • Feast in Colombia",
            titleEs = "San Luis Beltrán, Presbítero • Patrono Principal de Colombia",
            titleEn = "Saint Louis Bertrand, Priest • Principal Patron of Colombia",
            rankEs = "Fiesta • Ordo CEC Colombia (Color Blanco)",
            rankEn = "Feast • CEC Colombia Ordo (White Vestments)",
            color = LiturgicalColor.BLANCO,
            season = LiturgicalSeason.TIEMPO_ORDINARIO,
            psalterWeekEs = "Salterio de la Fiesta",
            psalterWeekEn = "Feast Psalter",
            ordoNotesEs = "Ordo CEC: Fiesta en toda Colombia. Proclamado Patrono de Colombia por el Papa Alejandro VII en 1690.",
            ordoNotesEn = "CEC Ordo: Feast throughout Colombia. Declared Patron of Colombia by Pope Alexander VII in 1690.",
            basilianConnectionEs = "Los Basilianos continúan el celo evangélico de San Luis Beltrán en sus comunidades de Bogotá y Medellín.",
            basilianConnectionEn = "The Basilians continue Saint Louis Bertrand's evangelizing fervor in Bogotá and Medellín.",
            hours = hours
        )
    }

    val sanBasilioMagnoSolemnity: ColombianLiturgicalDay by lazy {
        val hours = mutableMapOf<LiturgicalHourType, BilingualLiturgicalHour>()
        LiturgicalHourType.values().forEach { hourType ->
            val es = HourContent(
                hourName = "${hourType.titleEs} • San Basilio Magno y San Gregorio Nacianceno",
                ordoRubricNote = "Ordo Basilio & CEC: Memoria obligatoria universal / Solemnidad propia de la Congregación de San Basilio (C.S.B.).",
                introductoryVerse = "V. Dios mío, ven en mi auxilio. R. Señor, date prisa en socorrerme.",
                hymnTitle = "Padre del Monacato y Doctor de la Caridad",
                hymnText = "Oh San Basilio, columna firme de la Ortodoxia y Padre santo,\nen la Basiliada nos diste el modelo supremo de amor al hermano desvalido.\nTu regla monástica inspiró a diez sacerdotes en Annonay en 1822,\ny hoy tus hijos en Colombia cantan tu gloria con fidelidad.",
                psalms = listOf(
                    PsalmItem("Salmo 1", "Los dos caminos", "Ant. Dichoso el que medita la ley del Señor día y noche: como San Basilio, dará fruto a su tiempo.", "Dichoso el hombre que no sigue el consejo de los impíos...", "Gloria al Padre...")
                ),
                briefReading = ReadingItem("Hebreos 13, 7-9a", "Hb 13, 7-9a", "Acordaos de vuestros guías, que os anunciaron la palabra de Dios; considerad el desenlace de su vida e imitad su fe. Jesucristo es el mismo ayer, hoy y siempre.", "V. En medio de la asamblea abrió su boca.", "R. Y el Señor lo llenó del espíritu de sabiduría y entendimiento."),
                canticleTitle = "Benedictus / Magníficat",
                canticleAntiphon = "Ant. San Basilio Magno: «El pan que guardas pertenece al hambriento; el vestido en tu armario, al desnudo».",
                canticleText = "Bendito sea el Señor, Dios de Israel...",
                preces = PrecesItem("Alabemos a Cristo, Pastor Supremo, que nos dio a San Basilio Magno como guía espiritual:", listOf("Por la Congregación de San Basilio (C.S.B.) en Colombia y el mundo" to "Consérvalos en unidad fraterna, caridad y amor por la enseñanza.", "Por los novicios, escolásticos y formadores basilianos" to "Que crezcan en bondad, disciplina y ciencia (Bonitatem, disciplinam et scientiam)."), "Padre Nuestro:"),
                ourFather = "Padre nuestro...",
                concludingPrayer = "Señor Dios, que iluminaste a tu Iglesia con los admirables ejemplos y enseñanzas de los santos obispos Basilio Magno y Gregorio Nacianceno, concédenos aprender humildemente tu verdad y vivirla con fidelidad en la caridad fraterna. Por Jesucristo nuestro Señor. Amén.",
                blessing = "La bendición de San Basilio Magno descienda sobre nosotros. Amén."
            )
            val en = HourContent(
                hourName = "${hourType.titleEn} • Sts. Basil the Great and Gregory Nazianzen",
                ordoRubricNote = "Basilian & Universal Ordo: Solemnity for the Congregation of St. Basil (C.S.B.) / Memorial in the Universal Church.",
                introductoryVerse = "V. God, come to my assistance. R. Lord, make haste to help me.",
                hymnTitle = "Father of Monastic Life and Doctor of Charity",
                hymnText = "O Saint Basil, pillar of orthodoxy and venerable Father,\nin the Basiliad you offered the world the supreme icon of mercy.\nYour monastic Rule inspired ten founding priests at Annonay in 1822,\nand today your sons in Colombia and worldwide praise your holy memory.",
                psalms = listOf(
                    PsalmItem("Psalm 1", "The two ways", "Ant. Blessed is the one who meditates on the law of the Lord day and night; like St. Basil, he yields fruit in due season.", "Blessed is the man who walks not in the counsel of the wicked...", "Glory to the Father...")
                ),
                briefReading = ReadingItem("Hebrews 13:7-9a", "Heb 13:7-9a", "Remember your leaders, those who spoke to you the word of God; consider the outcome of their way of life, and imitate their faith. Jesus Christ is the same yesterday and today and forever.", "V. In the midst of the assembly he opened his mouth.", "R. And the Lord filled him with the spirit of wisdom and understanding."),
                canticleTitle = "Benedictus / Magnificat",
                canticleAntiphon = "Ant. St. Basil the Great: 'The bread you hold belongs to the hungry; the coat in your closet belongs to the naked.'",
                canticleText = "Blessed be the Lord, the God of Israel...",
                preces = PrecesItem("Let us praise Christ, the eternal Shepherd, who granted us Saint Basil the Great as patron:", listOf("For the Congregation of Saint Basil (C.S.B.)" to "Preserve our brothers in fraternal love and dedication to teaching.", "For vocations and students in Basilian institutions" to "May they flourish in goodness, discipline, and knowledge."), "Our Father:"),
                ourFather = "Our Father, who art in heaven...",
                concludingPrayer = "O God, who enlightened your Church through the luminous example and doctrine of Saints Basil the Great and Gregory Nazianzen, grant that we may humbly receive your truth and faithfully manifest it in holy fraternal love. Through our Lord Jesus Christ, your Son. Amen.",
                blessing = "May the blessing of Saint Basil rest upon us. Amen."
            )
            hours[hourType] = BilingualLiturgicalHour(hourType, es, en)
        }
        ColombianLiturgicalDay(
            id = "san_basilio_magno",
            dateDisplayEs = "2 de Enero • Solemnidad Titular CSB",
            dateDisplayEn = "January 2 • Titular Solemnity CSB",
            titleEs = "San Basilio Magno y San Gregorio Nacianceno • Patronos Basilianos",
            titleEn = "Saints Basil the Great and Gregory Nazianzen • Basilian Patron Saints",
            rankEs = "Solemnidad Propia de la Congregación de San Basilio (C.S.B.) • Color Blanco",
            rankEn = "Solemnity of the Congregation of Saint Basil (C.S.B.) • White Vestments",
            color = LiturgicalColor.BLANCO,
            season = LiturgicalSeason.NAVIDAD,
            psalterWeekEs = "Salterio de la Solemnidad",
            psalterWeekEn = "Solemnity Psalter",
            ordoNotesEs = "Ordo Basilio & CEC: Solemnidad propia de primer orden para los Padres Basilianos en Colombia, Canadá, EE.UU. y México. Misa propia con Gloria y Credo.",
            ordoNotesEn = "Basilian & CEC Ordo: First-class Solemnity for the Basilian Fathers in Colombia, Canada, USA, and Mexico. Proper Mass with Gloria and Creed.",
            basilianConnectionEs = "San Basilio Magno es el Santo Titular y Guía espiritual de la Congregación fundada en Annonay (Francia) en 1822 y presente en Colombia.",
            basilianConnectionEn = "Saint Basil the Great is the titular patron and spiritual Father of the Congregation founded in Annonay in 1822.",
            hours = hours
        )
    }
}
