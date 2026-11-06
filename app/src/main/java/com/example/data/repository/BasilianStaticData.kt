package com.example.data.repository

import com.example.data.model.*

object BasilianStaticData {

    val saintBasilQuote = "Un acto de bondad nunca se pierde: los que siembran cortesía cosechan amistades, y los que plantan cordialidad recogen amor."
    val basilianMotto = "Bonitatem et disciplinam et scientiam doce me"
    val basilianMottoTranslation = "Enséñame bondad, disciplina y ciencia"
    val basilianMottoScripture = "Salmo 119:66"

    val patrons: List<PatronSaint> = listOf(
        PatronSaint(
            id = "san_basilio",
            name = "San Basilio Magno",
            feastDay = "2 de Enero (y 14 de Junio fiesta tradicional)",
            title = "Padre y Patrono Titular de la Congregación, Doctor de la Iglesia",
            iconName = "menu_book",
            biography = "Nacido en Cesarea de Capadocia (329 - 379 d.C.). Sacerdote, maestro y durante 9 años Obispo de Cesarea. Padeció de mala salud pero fue incansable en la defensa de la fe ortodoxa y la unidad eclesial. Padre del monaquismo oriental cenobítico, organizador de la 'Basiliada' (ciudadela de caridad para pobres y enfermos). Demostró cómo integrar la sabiduría del estudio humano y la revelación evangélica.",
            connectionToBasilians = "Escogido en 1822 por los 10 fundadores por dos razones primordiales: enseñó a vivir en comunidad cristiana de acuerdo a los tiempos, y valoró el saber humano a la luz de la fe para una educación cristiana integral.",
            virtueForDailyLife = "Amor sincero por la verdad, servicio incondicional a los pobres y búsqueda de una disciplina de vida que una la mente y el corazón con Dios.",
            patronPrayer = "Santo padre, Basilio, patrón de nuestra Congregación que tiene como apostolado la enseñanza cristiana, sé nuestro modelo en la enseñanza y apoyo en nuestro ministerio. Danos ese amor por la verdad que tú manifestaste en tu vida, para que seamos instrumentos al llevar a los demás a la verdad máxima. Ten presente a tu Congregación, que Tú mismo has cuidado desde su inicio. Amén."
        ),
        PatronSaint(
            id = "virgen_maria",
            name = "La Santísima Virgen María",
            feastDay = "15 de Agosto (Asunción) y 21 de Noviembre (Presentación)",
            title = "Madre de Dios y Protectora Principal de los Basilianos",
            iconName = "auto_awesome",
            biography = "María, Madre de Jesucristo y de la Iglesia. Modelo de escucha orante de la Palabra, disponibilidad incondicional al plan de Dios y fidelidad al pie de la Cruz.",
            connectionToBasilians = "El 21 de noviembre de 1822, fiesta de la Presentación de Nuestra Señora en el Templo, los fundadores eligieron a su primer superior y comenzaron formalmente su asociación. La Asunción es la patrona de misiones insignes como en Cali y parroquias basilianas.",
            virtueForDailyLife = "Humildad orante, prontitud para el servicio de Dios y capacidad de guardar y meditar todas las cosas en el corazón.",
            patronPrayer = "Bajo tu amparo nos acogemos, Santa Madre de Dios; no deseches las súplicas que te dirigimos en nuestras necesidades, antes bien, líbranos de todo peligro, ¡oh Virgen gloriosa y bendita! Ruega por nosotros para que seamos dignos de alcanzar las promesas de Jesucristo."
        ),
        PatronSaint(
            id = "san_jose",
            name = "San José",
            feastDay = "19 de Marzo (Solemnidad)",
            title = "Esposo de la Virgen María, Custodio del Redentor y del Trabajo",
            iconName = "handyman",
            biography = "Hombre justo, silencioso y fiel de la estirpe de David. Custodio de la Sagrada Familia en Nazaret, educador del Niño Jesús en el trabajo humilde y honesto de cada día.",
            connectionToBasilians = "Patrono venerado por su sencillez, obediencia pronta sin ostentaciones y protección paternal de las comunidades religiosas y colegios basilianos.",
            virtueForDailyLife = "Fidelidad silenciosa en el deber cotidiano, justicia en el trato y cuidado bondadoso de quienes nos son encomendados.",
            patronPrayer = "San José, padre solícito y hombre justo, enséñanos a trabajar con honestidad, a cuidar con amor de nuestras familias y hermanos en comunidad, y a acoger en silencio la voluntad de Dios en nuestras vidas. Amén."
        ),
        PatronSaint(
            id = "san_francisco",
            name = "San Francisco de Asís",
            feastDay = "4 de Octubre",
            title = "El Poverello, Modelo de Pobreza Evangélica y Fraternidad Universal",
            iconName = "park",
            biography = "Nacido en Asís (1181/1182 - 1226). Renunció a las riquezas materiales para abrazar a la 'Señora Pobreza' y predicar la paz, la reconciliación y el amor a toda la Creación divina.",
            connectionToBasilians = "El Colegio de Annonay fue fundado en el antiguo convento de los Franciscanos (Cordeliers). San Francisco inspira la vivencia del voto de pobreza no como privación amarga, sino como libertad fraterna y cercanía al Cristo crucificado y a los marginados.",
            virtueForDailyLife = "Sencillez de vida, desapego del consumismo, espíritu de paz y contemplación agradecida de la naturaleza creada por Dios.",
            patronPrayer = "Señor, hazme un instrumento de tu paz. Donde haya odio, siembre yo amor; donde haya injuria, perdón; donde haya duda, fe; donde haya desesperación, esperanza; donde haya tinieblas, luz; donde haya tristeza, alegría. ¡Oh Maestro Divino! Haz que no busque tanto ser consolado como consolar; ser comprendido como comprender; ser amado como amar. Amén."
        ),
        PatronSaint(
            id = "santo_tomas",
            name = "Santo Tomás de Aquino",
            feastDay = "28 de Enero",
            title = "Doctor Angélico, Maestro del Pensamiento y de la Síntesis Fe-Razón",
            iconName = "school",
            biography = "Fraile dominico (1225 - 1274), cumbre de la teología y filosofía cristiana. Autor de la Suma Teológica e insigne adorador del Santísimo Sacramento con bellísimos himnos eucarísticos.",
            connectionToBasilians = "Patrono del compromiso intelectual basiliano con la 'Ciencia' y los estudios superiores. Inspira el rigor académico en los colegios, universidades (Univ. de San Miguel en Toronto, Univ. Santo Tomás en Houston) e institutos medievales.",
            virtueForDailyLife = "Amor apasionado por el estudio serio, humildad intelectual y búsqueda incansable de la verdad como camino que conduce al Creador.",
            patronPrayer = "Creador inefable, que de los tesoros de tu sabiduría ordenaste las partes del universo, infunde sobre las tinieblas de mi entendimiento un rayo de tu claridad. Dame agudeza para entender, capacidad para retener, método y facilidad para aprender, y gracia abundante para hablar. Amén."
        ),
        PatronSaint(
            id = "san_juan_bosco",
            name = "San Juan Bosco",
            feastDay = "31 de Enero",
            title = "Padre y Maestro de la Juventud",
            iconName = "groups",
            biography = "Sacerdote educador de Turín (1815 - 1888). Creador del 'sistema preventivo' basado en la razón, la religión y el amor (amorvolezza), rescatando a miles de jóvenes huérfanos y obreros.",
            connectionToBasilians = "Patrono de la pastoral educativa juvenil basiliana. Los Basilianos ven en él el corazón apasionado por acompañar a los jóvenes en los barrios, colegios y obras sociales.",
            virtueForDailyLife = "Alegría constante, pedagogía del cariño y la paciencia, y celo ardiente por la salvación y el bienestar de los jóvenes más vulnerables.",
            patronPrayer = "San Juan Bosco, amigo entrañable de los jóvenes y sembrador de esperanza: ayúdanos a educar guiando con bondad, a comprender a la juventud en sus búsquedas y a contagiar la alegría del Evangelio en nuestros hogares y aulas. Amén."
        ),
        PatronSaint(
            id = "oscar_romero",
            name = "San Óscar Arnulfo Romero",
            feastDay = "24 de Marzo",
            title = "Arzobispo Mártir de San Salvador, Voz de los sin Voz",
            iconName = "record_voice_over",
            biography = "Arzobispo de San Salvador (1917 - 1980), canonizado por el Papa Francisco. Defendió con su palabra profética a los campesinos y perseguidos en El Salvador hasta derramar su sangre en el altar.",
            connectionToBasilians = "Figura central en la espiritualidad de la misión basiliana en América Latina (honrado especialmente en las capillas de Bogotá, Medellín y Tehuacán) por encarnar el pastor que da la vida por su pueblo.",
            virtueForDailyLife = "Denuncia de la injusticia con caridad, escucha atenta del clamor de los humildes y entrega confiada en Dios.",
            patronPrayer = "San Óscar Romero, pastor generoso y valiente testigo del Reino: concédenos un corazón sensible a las heridas de nuestro pueblo y el coraje de sembrar fraternidad y esperanza allí donde reinan el dolor y la marginación. Amén."
        )
    )

    val founders: List<Founder> = listOf(
        Founder(
            id = "joseph_lapierre",
            name = "P. Joseph Bouvier Lapierre",
            years = "1757 – 1838",
            role = "Primer Superior General (1822–1838)",
            title = "El Fundador Prudente y Protector Clandestino",
            biography = "Durante el clímax de la Revolución Francesa, arriesgó su vida al negarse al juramento cívico cismático y fundó una escuela presbiteral clandestina en Saint-Symphorien-de-Mahun (1797–1799). En 1802 trasladó la escuela a Annonay con Mons. d'Aviau. Elegido por unanimidad el 21 de noviembre de 1822 como primer superior de la Asociación.",
            keyContribution = "Piedra angular de la primera comunidad; mantuvo unida la escuela ante persecuciones políticas y hostilidades civiles, guiando con prudencia y paciencia paternal.",
            spiritualVirtue = "Prudencia, magnanimidad y fortaleza en tiempos de prueba.",
            quoteOrEpitaph = "Señores, ustedes ven que hacemos aquí algún bien. Porque ya que nosotros estamos aquí, continuemos aún un año más; luego si no podemos ir más lejos, nos retiraremos."
        ),
        Founder(
            id = "pierre_tourvieille",
            name = "P. Pierre Tourvieille",
            years = "1780 – 1859",
            role = "Segundo Superior General (1838–1859)",
            title = "El Gran Pedagogo, Legislador y Matemático",
            biography = "Nacido en Joannas (Vivarais). Educador brillante, apasionado por las matemáticas y las humanidades. Formado en Grenoble, fue el alma académica del Colegio de Annonay y el arquitecto de las primeras normas y vida comunitaria. Bajo su generalato se expandió la congregación y en 1852 envió a los primeros misioneros a Toronto, Canadá.",
            keyContribution = "Redactó los primeros reglamentos basilianos, impulsó Sainte-Claire y Sainte-Barbe, y promovió el salto transatlántico a Norteamérica para fundar St. Michael's College.",
            spiritualVirtue = "Dedicación al estudio como servicio a Dios y constancia de gobierno.",
            quoteOrEpitaph = "Las constituciones son el fruto de la experiencia; es por las lecciones de esta gran maestra que llegaremos a las nuestras con el tiempo."
        ),
        Founder(
            id = "julien_actorie",
            name = "P. Julien Actorie",
            years = "1803 – 1864",
            role = "Tercer Superior General (1859–1864)",
            title = "El Organizador y Obtentor de la Aprobación Pontificia",
            biography = "Ingresó en Annonay y fue prefecto de estudios y profesor de teología y filosofía. Sucedió al P. Tourvieille en 1859. Con visión lúcida, consolidó la disciplina religiosa formal, redactó constituciones precisas y en 1863 obtuvo del Papa Pío IX el Decreto de Aprobación de la Congregación de San Basilio.",
            keyContribution = "Consolidó el reconocimiento canónico pontificio universal y profesionalizó la formación teológica y escolástica de los basilianos.",
            spiritualVirtue = "Fervor doctrinal, disciplina comunitaria y esperanza renovada.",
            quoteOrEpitaph = "Nuestro amor fraternal se expresa más claramente en nuestro cuidado por los enfermos y el apoyo que brindamos a los que sufren dificultades."
        ),
        Founder(
            id = "jacques_vincent_duret",
            name = "P. Jacques-Vincent Duret",
            years = "1762 – 1845",
            role = "Vicario y Fundador de Sainte-Barbe",
            title = "El Hombre de Paz y Consejo Conciliador",
            biography = "Vicario de Annonay y gran amigo del párroco Léorat-Picansel. En 1805 fundó el Seminario Menor Sainte-Barbe para apoyar a estudiantes y seminaristas de familias pobres con pocos recursos. Su lema personal fue: 'Inquire pacem et persequere eam' (Busca la paz y persíguela).",
            keyContribution = "Aseguró que la congregación mantuviera una opción real por los estudiantes pobres y sin medios económicos, ofreciéndoles techo y becas.",
            spiritualVirtue = "Mansedumbre, sabiduría en el consejo y generosidad con los humildes.",
            quoteOrEpitaph = "Todo en su andar y en sus palabras anunciaba la sabiduría; busca la paz y empéñate en alcanzarla."
        ),
        Founder(
            id = "augustin_payan",
            name = "P. Augustin Payan",
            years = "1771 – 1847",
            role = "Maestro de Disciplina y Guía Espiritual",
            title = "El Hombre de la Oración Perpetua y Obediencia Perfecta",
            biography = "Nativo de Chassiers, estudió con los jesuitas antes de la Revolución. Profesor desde los primeros días en Saint-Symphorien-de-Mahun. Modelo indiscutible de fidelidad y piedad comunitaria. Los cronistas lo describen unánimemente como 'el hombre de la oración perpetua y de la perfecta obediencia'.",
            keyContribution = "Encarnó la santidad y la reverencia litúrgica cotidiana; fue el guía espiritual que infundió el espíritu contemplativo en la docencia.",
            spiritualVirtue = "Oración incesante, humildad profunda y serenidad de espíritu.",
            quoteOrEpitaph = "La unión con el Señor en la oración es la que hace fecundo cualquier ministerio y sostiene la caridad entre los hermanos."
        ),
        Founder(
            id = "jean_baptiste_polly",
            name = "P. Jean-Baptiste Polly",
            years = "1772 – 1856",
            role = "Profesor, Ex-Alcalde de Mahun y Pastor Fiel",
            title = "El Héroe Audaz que Salvó a los Perseguidos",
            biography = "Joven seminarista durante el Terror revolucionario. Aceptó ser alcalde de Saint-Symphorien-de-Mahun para proteger secretamente a los sacerdotes clandestinos refugiados en las montañas, arriesgando su vida ante la Guardia Nacional con ingenio y valentía. Tras el Concordato fue ordenado sacerdote en 1801 y se unió de lleno a la comunidad.",
            keyContribution = "Símbolo de audacia y lealtad heroica a la Iglesia y a sus hermanos durante los años más oscuros del martirio en Francia.",
            spiritualVirtue = "Valentía cristiana, lealtad inquebrantable y astucia evangélica para el bien.",
            quoteOrEpitaph = "La Providencia vela por los justos; jamás abandonemos al hermano en su noche de dolor."
        ),
        Founder(
            id = "andre_fayolle",
            name = "P. André Fayolle",
            years = "1792 – 1856",
            role = "Primer Superior de Maison-Seule (1822) y Educador",
            title = "El Formador Joven de Maison-Seule",
            biography = "Sobrino de Tourvieille, nació en Montréal (Ardèche). Profesor de humanidades y literatura francesa en Annonay. En 1822 fue designado como el primer superior de la casa rural de Maison-Seule, donde se formaba a los jóvenes aspirantes en un ambiente de recogimiento y estudio.",
            keyContribution = "Estableció las bases del internado formativo de Maison-Seule y la pedagogía humanista en la naciente sociedad.",
            spiritualVirtue = "Celo por la juventud, docilidad pastoral y entusiasmo misionero.",
            quoteOrEpitaph = "Enseñar a la juventud con amor es abrir senderos a la gracia y al entendimiento de Dios."
        ),
        Founder(
            id = "henri_martinesche",
            name = "P. Henri Martinesche",
            years = "1797 – 1872",
            role = "Cofundador de Maison-Seule y Profesor de Letras",
            title = "El Trabajador Silencioso y Consagrado",
            biography = "Nativo de Vinezac, con solo 25 años en 1822 fue uno de los entusiastas cofundadores enviados a Maison-Seule. Se distinguió por su entrega en las labores comunitarias, su atención a los jóvenes y su vida de sencillez sin doblez.",
            keyContribution = "Aportó frescura y sacrificio abnegado en los difíciles comienzos de la vida común de los sacerdotes basilianos.",
            spiritualVirtue = "Sencillez de corazón, laboriosidad incansable y espíritu de comunión.",
            quoteOrEpitaph = "El trabajo honesto y la oración en común transforman cualquier casa en hogar de Dios."
        ),
        Founder(
            id = "jean_antoine_vallon",
            name = "P. Jean-Antoine Vallon",
            years = "1775 – 1840",
            role = "Profesor de Humanidades y Vigilante del Orden",
            title = "El Guardián de la Disciplina y el Rigor",
            biography = "Nativo de Lafarre, sacerdote serio y comprometido. Profesor de latín y humanidades. Hombre de rectitud intachable, velaba por la seriedad de los estudios y el cumplimiento fiel de los horarios y de la vida regular comunitaria.",
            keyContribution = "Aseguró que la excelencia académica y la formación moral de los alumnos fueran la carta de presentación del Colegio de Annonay.",
            spiritualVirtue = "Rectitud moral, puntualidad y celo por el crecimiento de los alumnos.",
            quoteOrEpitaph = "Sin disciplina la ciencia se extravía; con bondad la disciplina se hace fecunda."
        ),
        Founder(
            id = "jean_francois_pages",
            name = "P. Jean-François Pagès",
            years = "1793 – 1862",
            role = "Director de Sainte-Claire y Maestro de Humanidades",
            title = "El Gigante de Bondad y Rostro Monacal",
            biography = "Nacido en Malbosc. Los contemporáneos lo describían como un hombre alto y majestuoso de 'fisonomía monacal', pero dotado de una bondad y cordialidad extraordinarias. En 1823 fue nombrado director de la escuela Sainte-Claire en Annonay.",
            keyContribution = "Dirigió con éxito la rama de estudios modernos y comerciales de Sainte-Claire, acogiendo a jóvenes que se preparaban para la vida profesional y civil.",
            spiritualVirtue = "Dulzura en el trato, serenidad contemplativa y cercanía fraterna.",
            quoteOrEpitaph = "La verdadera grandeza no está en la apariencia, sino en la capacidad de servir con mansedumbre."
        )
    )

    val historicalTimeline: List<HistoricalEvent> = listOf(
        HistoricalEvent(
            year = "379",
            dateOrPeriod = "1 de Enero de 379",
            title = "Pascua de San Basilio Magno",
            location = "Cesarea de Capadocia",
            summary = "Muere San Basilio Magno, Obispo de Cesarea y Doctor de la Iglesia, dejando un legado imperecedero sobre la vida comunitaria, la caridad hacia los marginados y la integración del saber clásico con el Evangelio.",
            significance = "Inspirador titular y patrono de los Padres Basilianos.",
            category = HistoryCategory.FOUNDATION
        ),
        HistoricalEvent(
            year = "1797",
            dateOrPeriod = "1797 – 1799",
            title = "Seminario Clandestino en Saint-Symphorien-de-Mahun",
            location = "Montañas de Vivarais, Francia",
            summary = "En plena persecución religiosa de la Revolución Francesa, Mons. d'Aviau (arzobispo de Vienne) y el P. Joseph Lapierre abren una escuela presbiteral clandestina para formar futuros sacerdotes católicos.",
            significance = "La semilla clandestina que daría origen a la Congregación.",
            category = HistoryCategory.FOUNDATION
        ),
        HistoricalEvent(
            year = "1802",
            dateOrPeriod = "Septiembre de 1802",
            title = "Apertura del Colegio de Annonay",
            location = "Annonay, Francia",
            summary = "Trasladada la escuela de Mahun a la ciudad de Annonay con el Concordato, los sacerdotes se instalan en el antiguo convento franciscano de los Cordeliers bajo la dirección del párroco Léorat-Picansel.",
            significance = "Cuna fundacional de la comunidad docente y sacerdotal basiliana.",
            category = HistoryCategory.FOUNDATION
        ),
        HistoricalEvent(
            year = "1822",
            dateOrPeriod = "21 de Noviembre de 1822",
            title = "Fundación de la Congregación de San Basilio",
            location = "Annonay, Francia",
            summary = "En la fiesta de la Presentación de la Virgen María, diez sacerdotes diocesanos educadores se reúnen, aprueban su vida en común, eligen al P. Joseph Lapierre como su primer superior y se colocan bajo el patronazgo de San Basilio.",
            significance = "Nacimiento canónico e histórico oficial de la Congregación.",
            category = HistoryCategory.FOUNDATION
        ),
        HistoricalEvent(
            year = "1837",
            dateOrPeriod = "15 de Septiembre de 1837",
            title = "Decretum Laudis (Decreto de Alabanza)",
            location = "Roma, Santa Sede",
            summary = "El Papa Gregorio XVI emite el Decreto de Alabanza reconociendo a la Sociedad de Sacerdotes de San Basilio por su valiosa labor educativa y sacerdotal.",
            significance = "Primer gran aval de la Iglesia universal a la obra basiliana.",
            category = HistoryCategory.PONTIFICAL_APPROVAL
        ),
        HistoricalEvent(
            year = "1852",
            dateOrPeriod = "Noviembre de 1852",
            title = "Primeros Votos Religiosos y Misión a Canadá",
            location = "Annonay y Toronto (Canadá)",
            summary = "Los sacerdotes basilianos emiten formalmente sus votos religiosos de pobreza, castidad y obediencia el 21 de noviembre. Al mismo tiempo, por llamado de Mons. de Charbonnel, envían a los primeros cuatro misioneros a Toronto para fundar el St. Michael's College.",
            significance = "Transición a instituto religioso pleno y salto internacional a América.",
            category = HistoryCategory.EXPANSION_AMERICA
        ),
        HistoricalEvent(
            year = "1863",
            dateOrPeriod = "23 de Noviembre de 1863",
            title = "Decreto de Aprobación Pontificia",
            location = "Roma, Papa Pío IX",
            summary = "Bajo el generalato del P. Julien Actorie, la Santa Sede promulga el Decreto de Aprobación definitivo que eleva a la Congregación a Instituto de Derecho Pontificio.",
            significance = "Reconocimiento pontificio pleno de la Iglesia Católica universal.",
            category = HistoryCategory.PONTIFICAL_APPROVAL
        ),
        HistoricalEvent(
            year = "1922",
            dateOrPeriod = "14 de Junio de 1922",
            title = "Decreto de Separación Canónica",
            location = "Viviers y Toronto",
            summary = "Debido a las leyes anticlericales en Francia y al enorme crecimiento de las obras en Canadá y EE.UU., Roma autoriza la separación de la Congregación en dos ramas: Basilianos de Viviers (Francia) y Basilianos de Toronto (América).",
            significance = "Momento crítico que permitió la supervivencia y florecimiento en el Nuevo Mundo.",
            category = HistoryCategory.EXPANSION_AMERICA
        ),
        HistoricalEvent(
            year = "1938",
            dateOrPeriod = "14 de Octubre de 1938",
            title = "Aprobación Definitiva de Constituciones",
            location = "Roma, Vaticano",
            summary = "La Santa Sede otorga la aprobación final a las Constituciones de la Congregación de Sacerdotes de San Basilio de Toronto.",
            significance = "Estatuto canónico consolidado para el ministerio sacerdotal y educativo.",
            category = HistoryCategory.PONTIFICAL_APPROVAL
        ),
        HistoricalEvent(
            year = "1955",
            dateOrPeriod = "14 de Junio / 29 de Septiembre de 1955",
            title = "Decreto de Reunión de la Familia Basiliana",
            location = "Annonay y Toronto",
            summary = "Tras más de 30 años de separación, las dos ramas (Francia y Norteamérica) se reúnen con inmenso gozo bajo un solo Superior General (P. George Flahiff, futuro cardenal), sellando la unidad definitiva el día de San Miguel Arcángel.",
            significance = "Restablecimiento de la unidad universal de la Congregación.",
            category = HistoryCategory.EXPANSION_AMERICA
        ),
        HistoricalEvent(
            year = "1961",
            dateOrPeriod = "1961",
            title = "Fundación de la Misión en México",
            location = "Tehuacán y Ciudad de México",
            summary = "Los Basilianos inician su presencia en México, asumiendo parroquias populares (San Lorenzo Teotipilco) y casas de formación para sacerdotes hispanoamericanos.",
            significance = "Apertura decisiva al mundo hispanoamericano y la catequesis familiar.",
            category = HistoryCategory.EXPANSION_AMERICA
        ),
        HistoricalEvent(
            year = "1983",
            dateOrPeriod = "12 de Mayo de 1983",
            title = "Aprobación de 'La Forma de Vida Basiliana'",
            location = "Roma, Solemnidad de la Ascensión",
            summary = "La Santa Sede aprueba formalmente el nuevo Documento Básico Constitucional post-Concilio Vaticano II, fruto de 16 años de reflexión sinodal y redacción del P. Frederick Black y el P. Thomas Hanrahan.",
            significance = "Carta magna viva de la espiritualidad y regla basiliana actual.",
            category = HistoryCategory.RENEWAL_BICENTENARY
        ),
        HistoricalEvent(
            year = "1987",
            dateOrPeriod = "15 de Agosto de 1987",
            title = "Llegada de los Basilianos a Colombia (Cali)",
            location = "Cali, Parroquia Ntra. Sra. de la Asunción",
            summary = "Los padres Francisco Amico, Roberto Seguín, Mauricio Restivo y el candidato colombiano Rafael Ignacio Lopera asumen la parroquia de la Asunción en el oriente de Cali (barrios Andrés Sanín, Siete de Agosto, La Playa).",
            significance = "Comienzo de la vibrante historia basiliana en Suramérica.",
            category = HistoryCategory.COLOMBIA_MISSION
        ),
        HistoricalEvent(
            year = "1990",
            dateOrPeriod = "15 al 20 de Febrero de 1990",
            title = "Secuestro y Liberación del P. Francisco Amico",
            location = "Cali, Colombia",
            summary = "El P. Francisco Amico es secuestrado por una célula del ELN. Una masiva vigilia de oración con velas organizada por los feligreses y mediación eclesial logran su liberación sin daño alguno. El padre declara: 'No me voy, me quedo con mi pueblo'.",
            significance = "Prueba de fuego que selló la fidelidad y el lazo del amor de la congregación con Colombia.",
            category = HistoryCategory.COLOMBIA_MISSION
        ),
        HistoricalEvent(
            year = "1992",
            dateOrPeriod = "15 de Abril de 1992 (Miércoles Santo)",
            title = "Martirio de Aldemar Rodríguez Carvajal",
            location = "Cali / Riofrío, Colombia",
            summary = "El joven catequista de 20 años de la Asunción y servidor de los pobres es desaparecido y asesinado por su compromiso cristiano y defensa de los derechos humanos. Su cuerpo es velado en la Asunción y su testimonio ilumina a la comunidad.",
            significance = "Testigo profético que entregó su vida por el Evangelio.",
            category = HistoryCategory.COLOMBIA_MISSION
        ),
        HistoricalEvent(
            year = "1995",
            dateOrPeriod = "15 de Agosto de 1995",
            title = "Apertura del Noviciado en Medellín",
            location = "Medellín, Barrio Prado / Blanquizal",
            summary = "Se funda la segunda presencia en Colombia: el Noviciado San Basilio en Medellín y posteriormente en 1996 la Parroquia El Ecce Homo en el barrio Blanquizal.",
            significance = "Consolidación de las etapas de formación vocacional nativa en Colombia.",
            category = HistoryCategory.COLOMBIA_MISSION
        ),
        HistoricalEvent(
            year = "1997",
            dateOrPeriod = "4 de Agosto de 1997",
            title = "Fundación de Casa Annonay en Bogotá",
            location = "Bogotá, Barrio Egipto",
            summary = "Apertura del Escolasticado Basiliano 'Casa Annonay' en Bogotá para los estudios teológicos en la Universidad Javeriana, coincidiendo con los primeros 10 años de los Basilianos en Colombia.",
            significance = "Centro teológico y formativo de los escolásticos basilianos.",
            category = HistoryCategory.COLOMBIA_MISSION
        ),
        HistoricalEvent(
            year = "2022",
            dateOrPeriod = "Año 2022",
            title = "Bicentenario de la Fundación (1822 – 2022)",
            location = "Mundial (Toronto, Annonay, Bogotá, México, Houston)",
            summary = "Celebración de los 200 años de fundación de la Congregación de San Basilio. El Capítulo General de 2022 renueva las Costumbres Basilianas e impulsa la sinodalidad y la interculturalidad.",
            significance = "200 años de servicio en bondad, disciplina y ciencia.",
            category = HistoryCategory.RENEWAL_BICENTENARY
        ),
        HistoricalEvent(
            year = "2023",
            dateOrPeriod = "Año 2023",
            title = "Parroquia Nuestra Señora de Egipto en Bogotá",
            location = "Bogotá, Colombia",
            summary = "La Arquidiócesis de Bogotá encomienda a los Padres Basilianos la histórica Parroquia Nuestra Señora de Egipto, ubicada justo frente a Casa Annonay.",
            significance = "Expansión pastoral en el corazón de Bogotá con el P. Pedro Mora como párroco.",
            category = HistoryCategory.RECENT_2026
        ),
        HistoricalEvent(
            year = "2025",
            dateOrPeriod = "Julio 2025 – 2026",
            title = "Nueva Sede Curial y Misiones Interculturales",
            location = "Toronto, Houma LA, CDMX",
            summary = "La Curia General se traslada al 146 de Davenport Road en Toronto. Se abre la comunidad en Houma-Thibodaux (Luisiana) con ministerio hispano, se asume la parroquia histórica San Pedro de Verona en Tlalpan (México) y se inicia el segundo noviciado conjunto en Bogotá.",
            significance = "La vitalidad misionera actual de la Congregación de San Basilio hoy en día.",
            category = HistoryCategory.RECENT_2026
        )
    )

    val dailyPrayers: List<PrayerItem> = listOf(
        PrayerItem(
            id = "laudes",
            title = "Laudes (Oración de la Mañana)",
            subtitle = "Al despertar: ofrecimiento del día al Creador",
            timeOfDay = "Mañana",
            sourceDocument = "Costumbres Basilianas E.14, G.05 y Forma de Vida 46",
            latinTitle = "Laudes Matutinae",
            prayerText = "Señor, abre mis labios, y mi boca proclamará tu alabanza.\n\n" +
                    "Bendito seas, Señor, Dios de nuestros padres, que disipas las tinieblas de la noche y nos concedes ver la luz de un nuevo día. Te consagramos nuestras obras, nuestros pensamientos y nuestras palabras de hoy.\n\n" +
                    "Que a ejemplo de nuestro santo padre Basilio, vivamos este día arraigados en la Bondad con el hermano, en la Disciplina de nuestro deber y en la Ciencia de tu verdad. Envía tu Espíritu Santo para que seamos presencia sanadora y pacífica para quienes encontremos en nuestro camino.\n\n" +
                    "Gloria al Padre, y al Hijo, y al Espíritu Santo. Como era en el principio, ahora y siempre, por los siglos de los siglos. Amén.",
            reflection = "Las Laudes marcan el despertar de la creación y consagran las primeras energías del día al servicio del Reino de Dios."
        ),
        PrayerItem(
            id = "angelus",
            title = "Ángelus y Oración del Mediodía",
            subtitle = "Al mediodía: memoria de la Encarnación y devoción a San Basilio",
            timeOfDay = "Mediodía",
            sourceDocument = "Costumbres Basilianas en Colombia 2.1.3 - 2.1.5",
            latinTitle = "Angelus Domini",
            prayerText = "V. El Ángel del Señor anunció a María.\nR. Y concibió por obra y gracia del Espíritu Santo.\n(Ave María...)\n\n" +
                    "V. He aquí la esclava del Señor.\nR. Hágase en mí según tu palabra.\n(Ave María...)\n\n" +
                    "V. Y el Verbo se hizo carne.\nR. Y habitó entre nosotros.\n(Ave María...)\n\n" +
                    "V. Ruega por nosotros, Santa Madre de Dios.\nR. Para que seamos dignos de alcanzar las promesas de Jesucristo.\n\n" +
                    "Oremos: Derrama, Señor, tu gracia en nuestras almas, para que cuantos hemos conocido por el anuncio del Ángel la Encarnación de tu Hijo Jesucristo, por su Pasión y Cruz seamos llevados a la gloria de su Resurrección. Por Jesucristo Nuestro Señor. Amén.\n\n" +
                    "Antífona: Bajo tu amparo nos acogemos, Santa Madre de Dios; no desoigas nuestras súplicas en las necesidades, antes bien, líbranos siempre de todo peligro, Virgen gloriosa y bendita.\n\n" +
                    "Oración a San Basilio: Santo Padre Basilio, ruega por nosotros y por las vocaciones a la Iglesia y a la Congregación.",
            reflection = "Al mediodía, los basilianos detienen sus labores para recordar que Dios se hizo humano en Jesús y poner la jornada bajo la mirada materna de María."
        ),
        PrayerItem(
            id = "mesa",
            title = "Bendición de la Mesa y los Alimentos",
            subtitle = "Antes del almuerzo y de la cena: gratitud por el pan compartido",
            timeOfDay = "Mediodía / Noche",
            sourceDocument = "Costumbres Basilianas E.15, E.18 y Tradición en Colombia 2.1.5",
            latinTitle = "Benedictio Mensae",
            prayerText = "Los ojos de todos tienen puesta la mirada en Ti, Señor, y Tú les das la comida a su debido tiempo. Abres tu mano generosa y sacias de favores a todo ser viviente.\n\n" +
                    "Bendícenos, Señor, y bendice estos alimentos que por tu infinita bondad vamos a tomar. Concede pan a los que tienen hambre, y hambre de justicia y de Ti a los que tienen pan. Te lo pedimos por Jesucristo Nuestro Señor.\n\n" +
                    "Demos gracias al Señor porque es bueno; porque es eterna su misericordia. Amén.",
            reflection = "El comedor es para los Basilianos 'Mensa Communis', un altar doméstico donde se estrechan los lazos de fraternidad, sencillez y hospitalidad."
        ),
        PrayerItem(
            id = "visperas",
            title = "Vísperas (Oración de la Tarde)",
            subtitle = "Al caer la tarde: acción de gracias por la jornada cumplida",
            timeOfDay = "Tarde",
            sourceDocument = "Costumbres Basilianas G.05 y Forma de Vida 46",
            latinTitle = "Vesperae",
            prayerText = "Dios mío, ven en mi auxilio; Señor, date prisa en socorrerme.\n\n" +
                    "Al declinar el sol, te damos gracias, Padre Bueno, por el trabajo concluido, por los encuentros fraternos y por las dificultades que con tu gracia supimos afrontar.\n\n" +
                    "Te encomendamos a los pobres, a los enfermos, a los educadores y a los jóvenes que buscan sentido en su vida. Que el testimonio de la sencillez de los Padres Basilianos siga siendo luz en medio de las pruebas de este mundo.\n\n" +
                    "Proclama mi alma la grandeza del Señor, y se alegra mi espíritu en Dios, mi Salvador, porque ha mirado la humillación de su sierva... (Cántico del Magníficat).\n\n" +
                    "Gloria al Padre, al Hijo y al Espíritu Santo. Amén.",
            reflection = "En las Vísperas entregamos el fruto del trabajo y descansamos en la fidelidad eterna del Señor."
        ),
        PrayerItem(
            id = "completas",
            title = "Completas y Cántico de Simeón",
            subtitle = "Antes de dormir: abandono en las manos del Padre",
            timeOfDay = "Noche",
            sourceDocument = "Costumbres Basilianas en Colombia 2.1.1 y 2.3",
            latinTitle = "Completorium",
            prayerText = "El Señor todopoderoso nos conceda una noche tranquila y una muerte santa. Amén.\n\n" +
                    "Cántico de Simeón (Nunc Dimittis - Lc 2, 29-32):\n" +
                    "Ahora, Señor, según tu promesa, puedes dejar a tu siervo irse en paz, porque mis ojos han visto a tu Salvador, a quien has presentado ante todos los pueblos: luz para alumbrar a las naciones y gloria de tu pueblo Israel.\n\n" +
                    "Oración final:\n" +
                    "Visita, Señor, esta habitación y aparta de ella todas las insidias del enemigo; tus santos ángeles habiten en ella para custodiarnos en paz, y tu bendición esté siempre con nosotros. Por Jesucristo Nuestro Señor. Amén.\n\n" +
                    "Antífona Mariana Final:\n" +
                    "Dios te salve, Reina y Madre de misericordia, vida, dulzura y esperanza nuestra... Vuelve a nosotros esos tus ojos misericordiosos, ¡oh clemente, oh piadosa, oh dulce Virgen María!",
            reflection = "En las casas basilianas de Colombia y el mundo, al terminar las completas se canta a la luz de una vela la antífona a la Virgen, entregando el descanso en paz."
        ),
        PrayerItem(
            id = "oracion_san_basilio",
            title = "Oración a San Basilio Magno",
            subtitle = "Plegaria patronal oficial de la Congregación",
            timeOfDay = "Cualquier momento",
            sourceDocument = "Apuntes Históricos p. 5 y Costumbres Basilianas G.11",
            latinTitle = "Oratio ad Sanctum Basilium",
            prayerText = "Santo padre, Basilio,\npatrón de nuestra Congregación,\nque tiene como apostolado la enseñanza cristiana:\nsé nuestro modelo en la enseñanza\ny apoyo en nuestro ministerio.\n\nDanos ese amor por la verdad\nque tú manifestaste en tu vida,\npara que seamos instrumentos\nal llevar a los demás a la verdad máxima.\n\nTen presente a tu Congregación,\nque Tú mismo has cuidado desde su inicio.\nAmén.",
            reflection = "Esta oración resume la identidad basiliana: educar cristianamente con amor incondicional a la verdad y celo pastoral por los fieles."
        ),
        PrayerItem(
            id = "vida_comun",
            title = "Oración por la Vida en Común",
            subtitle = "Para fomentar la caridad, la sinceridad y la paz en el hogar y la comunidad",
            timeOfDay = "Cualquier momento",
            sourceDocument = "Apuntes de Colombia p. 35 y Costumbres Basilianas E.01-E.05",
            latinTitle = "Oratio pro Vita Communi",
            prayerText = "Señor, concédenos:\nque en el amor seamos sinceros;\nque aborrezcamos el mal y cuidemos todo lo bueno;\n\nque en el amor entre hermanos\nnos demostremos cariño unos a otros;\n\nque en el respeto estimemos\na los otros como más dignos;\n\nque en el cumplimiento del deber no seamos flojos;\nque en el espíritu seamos fervorosos;\nque tengamos esperanza y estemos alegres;\nque en las pruebas seamos pacientes;\nque con los necesitados compartamos con ellos;\nque con los que estén de paso seamos solícitos para recibirlos en casa;\n\nSeñor, que bendigamos a quienes nos persiguen;\nque bendigamos y no maldigamos;\n\nque nos alegremos con los que están alegres\ny lloremos con los que lloran;\n\nque vivamos en armonía con todos;\nque no confiemos en la propia sabiduría\nsino que procuremos hacer el bien delante de todos;\n\nque hagamos todo lo posible\nen cuanto a nosotros dependa,\npara vivir en paz con todos.\nAmén.",
            reflection = "Inspirada en Romanos 12 y en las tradiciones de los fundadores basilianos, esta plegaria es la guía maestra de la fraternidad sin hipocresía."
        ),
        PrayerItem(
            id = "espiritu_santo_capitulo",
            title = "Oración de Discernimiento al Espíritu Santo",
            subtitle = "Invocación antes de decisiones importantes y elecciones",
            timeOfDay = "Cualquier momento",
            sourceDocument = "Costumbres Basilianas J.25",
            latinTitle = "Veni Creator Spiritus",
            prayerText = "Espíritu Santo, protector Nuestro, a quien Jesús envió a la Iglesia para dirigirla hacia la Verdad Entera, ven a nosotros.\n\n" +
                    "Ilumina nuestras mentes con la sabiduría para que nosotros podamos elegir y discernir aquello que ha de servir de la mejor manera a nuestra comunidad y a la Iglesia.\n\n" +
                    "Llena nuestros corazones de amor y de paz para que podamos aceptar unidos los caminos de tu voluntad.\n\n" +
                    "Ven, Espíritu Santo, ven ahora a nosotros como Jesús nos ha prometido. Amén.",
            reflection = "Rezada por todos los capitulantes antes de cada elección y en los momentos clave de discernimiento de los Padres Basilianos."
        ),
        PrayerItem(
            id = "vocaciones_basilianas",
            title = "Oración por las Vocaciones Basilianas",
            subtitle = "Petición al Dueño de la Mies por nuevos servidores de la educación y el Evangelio",
            timeOfDay = "Cualquier momento",
            sourceDocument = "Forma de Vida Basiliana 68 y Costumbres H.01",
            latinTitle = "Oratio pro Vocationibus",
            prayerText = "Padre Celestial, la mies es abundante y los obreros son pocos.\n\n" +
                    "Te pedimos humildemente que envíes jóvenes generosos, valientes y llenos de fe a la Congregación de los Padres Basilianos.\n\n" +
                    "Que atraídos por el ejemplo de San Basilio y de nuestros santos fundadores, deseen consagrar sus vidas a la enseñanza cristiana, a la vida en común y al servicio de los empobrecidos, viviendo con gozo la bondad, la disciplina y la ciencia. Por Cristo Nuestro Señor. Amén.",
            reflection = "La pastoral vocacional nace de la oración perseverante de todo el pueblo de Dios."
        ),
        PrayerItem(
            id = "oracion_san_oscar_romero",
            title = "Oración a San Óscar Romero",
            subtitle = "Aprobada en el Capítulo General 2026 para el Vademécum Basiliano",
            timeOfDay = "Cualquier momento",
            sourceDocument = "Capítulo General 2026 • Propuesta P. Paul English CSB",
            latinTitle = "Oratio ad Sanctum Oscar Romero",
            prayerText = "Oh Dios Padre Misericordioso,\npor mediación de Jesucristo\ny la intercesión de la Virgen María, Reina de la Paz,\ny la acción del Espíritu Santo,\nconcediste a San Óscar Romero la gracia de ser un Pastor ejemplar al servicio de la Iglesia,\ny en ella, preferencialmente a los pobres y los necesitados.\n\nNos dirigimos hoy a Ti para pedirte con fe y confianza por todas las necesidades de la Congregación de San Basilio,\na fin de que se afiance cada vez más en nosotros el cumplimiento de tu voluntad.\n\nHaz, Señor, que nosotros sepamos también vivir conforme al Evangelio de tu Hijo,\nquien vive y reina por los siglos de los siglos.\nAmén.",
            reflection = "Aprobada formalmente por el Capítulo General de 2026 para encomendar a San Óscar Romero, santo patrono de la Congregación de San Basilio, el celo pastoral, la opción preferencial por los pobres y la valentía profética en América Latina y el mundo."
        )
    )

    val dailyInspirations: List<DailyInspiration> = listOf(
        DailyInspiration(
            dayOfYear = 1,
            scriptureRef = "Salmo 119:66",
            scriptureText = "Enséñame buen juicio y sabiduría, pues confío en tus mandamientos.",
            basilianMottoFocus = "Bondad (Bonitatem)",
            stBasilWisdom = "Un árbol se conoce por sus frutos, un hombre por sus obras. Un acto de bondad nunca se pierde.",
            practicalActionForToday = "Haz un favor discreto o ten una palabra amable con la persona que menos esperas hoy."
        ),
        DailyInspiration(
            dayOfYear = 2,
            scriptureRef = "Filipenses 3:8",
            scriptureText = "Todo lo considero pérdida ante la sublime excelencia de conocer a Cristo Jesús, mi Señor.",
            basilianMottoFocus = "Ciencia (Scientiam)",
            stBasilWisdom = "Así como las abejas extraen el néctar de las mejores flores, nosotros debemos tomar de las ciencias y del estudio todo aquello que edifique el alma en la verdad.",
            practicalActionForToday = "Dedica 15 minutos hoy a leer un texto que nutra tu intelecto y profundice tu fe."
        ),
        DailyInspiration(
            dayOfYear = 3,
            scriptureRef = "Romanos 12:10",
            scriptureText = "Ámense cordialmente los unos a los otros, estimando más a los otros que a uno mismo.",
            basilianMottoFocus = "Disciplina (Disciplinam)",
            stBasilWisdom = "El orden interior engendra la paz exterior. Quien sabe dominar sus impulsos es libre para servir a Dios.",
            practicalActionForToday = "Cumple puntualmente tu primera tarea del día como una ofrenda silenciosa a Dios."
        ),
        DailyInspiration(
            dayOfYear = 4,
            scriptureRef = "Mateo 25:40",
            scriptureText = "En verdad les digo: cuanto hicieron a uno de estos hermanos míos más pequeños, a mí me lo hicieron.",
            basilianMottoFocus = "Bondad (Bonitatem)",
            stBasilWisdom = "El pan que tú guardas pertenece al hambriento; el abrigo que cuelga en tu armario pertenece al desnudo; el dinero que escondes pertenece al indigente.",
            practicalActionForToday = "Revisa en tu despensa o ropero algo que no uses y dónalo a alguien que realmente lo necesite."
        ),
        DailyInspiration(
            dayOfYear = 5,
            scriptureRef = "Juan 15:15",
            scriptureText = "Ya no los llamo siervos, sino amigos, porque les he dado a conocer todo lo que oí de mi Padre.",
            basilianMottoFocus = "Bondad (Bonitatem)",
            stBasilWisdom = "La amistad cristiana es un reflejo del amor inalterable de Dios; fortalece el camino hacia la santidad.",
            practicalActionForToday = "Escribe o llama a un amigo para agradecerle sinceramente su presencia en tu vida."
        ),
        DailyInspiration(
            dayOfYear = 6,
            scriptureRef = "1 Corintios 13:4-7",
            scriptureText = "El amor es paciente, es servicial; el amor no es envidioso, no es jactancioso, no se engríe.",
            basilianMottoFocus = "Disciplina (Disciplinam)",
            stBasilWisdom = "Nuestra vida comunitaria debe ser un modelo de verdadera reconciliación, superando el individualismo.",
            practicalActionForToday = "Frente a un malentendido o roce hoy, responde con serenidad y renuncia a tener la última palabra."
        ),
        DailyInspiration(
            dayOfYear = 7,
            scriptureRef = "Lucas 10:41-42",
            scriptureText = "Marta, Marta, te preocupas y te agitas por muchas cosas, pero solo una es necesaria.",
            basilianMottoFocus = "Ciencia (Scientiam)",
            stBasilWisdom = "El silencio es el inicio de la purificación del alma. En el sosiego, el alma contempla a Dios.",
            practicalActionForToday = "Apaga las pantallas 20 minutos antes de dormir y dedica ese tiempo al agradecimiento y al silencio."
        ),
        DailyInspiration(
            dayOfYear = 8,
            scriptureRef = "Miqueas 6:8",
            scriptureText = "Se te ha indicado, oh hombre, lo que es bueno y lo que el Señor exige de ti: practicar la justicia, amar la misericordia y caminar humildemente con tu Dios.",
            basilianMottoFocus = "Bondad (Bonitatem)",
            stBasilWisdom = "No excuses tu falta de caridad diciendo que no tienes suficiente. Quien comparte lo poco que tiene con pureza de corazón enriquece a la humanidad entera.",
            practicalActionForToday = "Practica la generosidad silenciosa: cede tu lugar o ayuda a alguien sin esperar felicitaciones."
        ),
        DailyInspiration(
            dayOfYear = 9,
            scriptureRef = "Proverbios 4:7",
            scriptureText = "El principio de la sabiduría es: adquiere sabiduría; y por sobre todas tus posesiones adquiere inteligencia.",
            basilianMottoFocus = "Ciencia (Scientiam)",
            stBasilWisdom = "La instrucción sin caridad hincha el corazón; pero el saber iluminado por el Evangelio edifica comunidades y consuela a los desvalidos.",
            practicalActionForToday = "Aprende hoy algo nuevo y compártelo de manera didáctica y paciente con un compañero o familiar."
        ),
        DailyInspiration(
            dayOfYear = 10,
            scriptureRef = "Gálatas 6:9",
            scriptureText = "No nos cansemos de hacer el bien, porque a su debido tiempo cosecharemos si no desmayamos.",
            basilianMottoFocus = "Disciplina (Disciplinam)",
            stBasilWisdom = "El timonel demuestra su pericia en la tormenta, y el cristiano demuestra su fidelidad en la perseverancia cotidiana.",
            practicalActionForToday = "Termina hoy ese deber pendiente que has venido posponiendo con pereza."
        ),
        DailyInspiration(
            dayOfYear = 11,
            scriptureRef = "Santiago 2:17",
            scriptureText = "Así también la fe, si no tiene obras, está completamente muerta en sí misma.",
            basilianMottoFocus = "Bondad (Bonitatem)",
            stBasilWisdom = "En la Basiliada aprendimos que curar una herida y compartir el pan es la liturgia más agradable a los ojos de Dios.",
            practicalActionForToday = "Dedica tiempo hoy a escuchar a una persona que atraviese un momento de desánimo o soledad."
        ),
        DailyInspiration(
            dayOfYear = 12,
            scriptureRef = "Colosenses 3:12",
            scriptureText = "Vístanse, pues, como escogidos de Dios, de entrañas de misericordia, de benignidad, de humildad, de mansedumbre, de paciencia.",
            basilianMottoFocus = "Disciplina (Disciplinam)",
            stBasilWisdom = "La verdadera grandeza del alma consiste en no dejarse irritar por la injuria y en responder con mansedumbre a la altivez.",
            practicalActionForToday = "Cuando sientas deseos de quejarte o juzgar a alguien hoy, sustituye la queja por una oración secreta por esa persona."
        ),
        DailyInspiration(
            dayOfYear = 13,
            scriptureRef = "Juan 8:32",
            scriptureText = "Y conocerán la verdad, y la verdad los hará libres.",
            basilianMottoFocus = "Ciencia (Scientiam)",
            stBasilWisdom = "Quien ama la verdad no teme la luz. En la verdad de Cristo descansan todas las aspiraciones del corazón humano.",
            practicalActionForToday = "Sé absolutamente transparente y veraz en cada palabra que pronuncies durante la jornada."
        ),
        DailyInspiration(
            dayOfYear = 14,
            scriptureRef = "Mateo 5:9",
            scriptureText = "Bienaventurados los que trabajan por la paz, porque ellos serán llamados hijos de Dios.",
            basilianMottoFocus = "Bondad (Bonitatem)",
            stBasilWisdom = "Nada es tan propio de un cristiano como ser constructor de concordia y reconciliador de los enemistados.",
            practicalActionForToday = "Tiende hoy un puente de paz en tu familia o trabajo, disipando cualquier tensión con amabilidad."
        )
    )

    // Current Basilian Worldwide Presence (Directory 2025-2026 & Boletín 2026)
    val curiaOffice = "146 Davenport Road, Toronto, Ontario M5R 1J2, Canadá"
    val superiorGeneral = "Muy Rev. John B. Huber, CSB (Superior General 2026 - presente)"
    val previousSuperiorGeneral = "Muy Rev. Kevin J. Storey, CSB (Superior General 2018 - 2026)"
    val generalCouncilors = listOf(
        "Rev. Maurice Restivo, CSB (Consejero General)",
        "Rev. Pedro M. Mora, CSB (Consejero General, Párroco Nuestra Señora de Egipto, Bogotá)",
        "Rev. Terry Kersch, CSB (Consejero General)",
        "Rev. Paul F. English, CSB (Consejero General, Parroquia St. Anne, Houston TX)"
    )
    val officers = listOf(
        "Secretario General: Rev. Jefferson M. Thompson, CSB (2024)",
        "Tesorero General: Rev. Andrew F. B. Leung, CSB (2024)",
        "Procurador ante la Santa Sede: Rev. Sean O. Sheridan, TOR (Roma)"
    )
    val colombiaLeadership = listOf(
        "Superior Regional en Colombia: Rev. Juan Carlos Rojas Ramos, CSB (2023)",
        "Tesorero en Colombia: Rev. Carlos Arturo Rodríguez Marciales, CSB (2024)"
    )
    val mexicoLeadership = listOf(
        "Superior Regional en México: Rev. Paul Francis Walsh, CSB",
        "Tesorero en México: Rev. José de Jesús Rodríguez Pérez, CSB (2026)"
    )
    val statistics2026 = listOf(
        "Total de sacerdotes en la Congregación: 105 sacerdotes",
        "Obispos Basilianos: 4 (1 diocesano: Mons. Ronald Fabbro en London Ontario; 1 auxiliar: Mons. Robert Kasun en Toronto; 2 eméritos: Mons. J. Michael Miller en Vancouver y Mons. Ricardo Ramírez en Las Cruces)",
        "Distribución de sacerdotes: Canadá (48), Estados Unidos (38), México (10), Colombia (7), Francia (2)",
        "Edad mediana congregacional: 75 años",
        "Escolásticos en formación: 11 (Tlalpan México, Casa Annonay Bogotá y Teologado: Juan Gabriel Galvis Villamizar, Ciro Alfonso Cárdenas Balaguera, Fabien Mwehu Itondo, José Enrique Hoyos, Amaury Ramos, Germán Suárez y hermanos de México)",
        "Novicios en Casa Annonay (2026-2027): Diego Alejandro Martínez López, José Yonni Villa de Jesús y Jorge Mario Rodríguez Peña"
    )
    val keyInstitutions = listOf(
        "University of St. Michael's College (Toronto, Canadá - 1852)",
        "St. Michael's College School (Toronto, Canadá - 1852)",
        "Pontifical Institute of Mediaeval Studies (PIMS, Toronto - 1929)",
        "University of St. Thomas (Houston, Texas - 1947)",
        "St. Thomas High School (Houston, Texas - 1900)",
        "Detroit Catholic Central High School (Novi, Michigan - 1928)",
        "St. John Fisher University (Rochester, New York - 1948)",
        "Assumption University y Assumption Parish (Windsor, Canadá - 1870)",
        "St. Joseph's College en Universidad de Alberta (Edmonton, Canadá)",
        "Ensemble Scolaire Saint-Basile (Annonay, Francia - cuna fundacional)",
        "Casa Annonay & Parroquia Nuestra Señora de Egipto (Bogotá, Colombia)",
        "Parroquia San Basilio (Medellín, Colombia)",
        "Escolasticado Basiliano y Parroquia San Pedro de Verona (Ciudad de México)",
        "Casa San Felipe y Parroquia San Lorenzo (Tehuacán, México)"
    )

    val calendarDates: List<BasilianCalendarDate> = listOf(
        BasilianCalendarDate(
            id = "cal_pascua_san_basilio",
            month = 1,
            day = 1,
            dateFormatted = "1 de Enero",
            title = "Pascua de San Basilio Magno (379 d.C.)",
            category = CalendarCategory.SPIRITUAL_MEMORIAL,
            briefDescription = "Tránsito al Cielo de San Basilio Magno, Obispo de Cesarea y Doctor de la Iglesia.",
            detailedHistory = "El 1 de enero del año 379, a los 49 años de edad, San Basilio entregó su alma a Dios en Cesarea tras una vida consagrada a la unidad eclesial, la defensa de la divinidad del Espíritu Santo, la regla monástica y la caridad en la Basiliada. Toda la ciudad acudió a su sepelio reconociéndolo como padre de los pobres y luz de la Iglesia.",
            liturgicalRank = "Memoria Solemne y Solemnidad de Santa María Madre de Dios",
            suggestedPrayerId = "oracion_san_basilio",
            relatedPatronOrFounderId = "san_basilio"
        ),
        BasilianCalendarDate(
            id = "cal_fiesta_san_basilio",
            month = 1,
            day = 2,
            dateFormatted = "2 de Enero",
            title = "Fiesta de San Basilio Magno y San Gregorio Nacianceno",
            category = CalendarCategory.PATRON_FEAST,
            briefDescription = "Solemnidad Titular de la Congregación de San Basilio en todo el mundo.",
            detailedHistory = "Fiesta litúrgica de San Basilio Magno junto a su amigo del alma San Gregorio Nacianceno. Para los Padres Basilianos es la Solemnidad central en colegios, parroquias y casas religiosas, renovando el compromiso con el lema 'Bonitatem et disciplinam et scientiam doce me'.",
            liturgicalRank = "Solemnidad Titular Congregacional",
            suggestedPrayerId = "oracion_san_basilio",
            relatedPatronOrFounderId = "san_basilio"
        ),
        BasilianCalendarDate(
            id = "cal_santo_tomas_aquino",
            month = 1,
            day = 28,
            dateFormatted = "28 de Enero",
            title = "Memoria de Santo Tomás de Aquino",
            category = CalendarCategory.PATRON_FEAST,
            briefDescription = "Doctor Angélico, patrono del estudio y del empeño intelectual basiliano (Scientia).",
            detailedHistory = "Santo Tomás de Aquino (1225–1274) representa la cumbre de la síntesis entre la fe revelada y la razón humana. La tradición basiliana lo honra especialmente en la Universidad de Santo Tomás (Houston) y en el Instituto Pontificio de Estudios Medievales (Toronto).",
            liturgicalRank = "Memoria Obligatoria y Patrono de los Estudios",
            suggestedPrayerId = "laudes",
            relatedPatronOrFounderId = "santo_tomas"
        ),
        BasilianCalendarDate(
            id = "cal_san_juan_bosco",
            month = 1,
            day = 31,
            dateFormatted = "31 de Enero",
            title = "Memoria de San Juan Bosco",
            category = CalendarCategory.PATRON_FEAST,
            briefDescription = "Padre y maestro de la juventud, protector de la labor pedagógica con los jóvenes.",
            detailedHistory = "San Juan Bosco (1815–1888) inspira la pedagogía del afecto y el acompañamiento preventivo a la juventud vulnerable. Los sacerdotes basilianos encuentran en Don Bosco el modelo para animar la pastoral juvenil en colegios y barrios populares.",
            liturgicalRank = "Memoria Obligatoria",
            suggestedPrayerId = "vocaciones_basilianas",
            relatedPatronOrFounderId = "san_juan_bosco"
        ),
        BasilianCalendarDate(
            id = "cal_vigilia_cali_amico",
            month = 2,
            day = 15,
            dateFormatted = "15 de Febrero",
            title = "Jornada de Solidaridad y Resiliencia en Cali (1990)",
            category = CalendarCategory.MISSION_COLOMBIA,
            briefDescription = "Conmemoración de la heroica vigilia popular tras el secuestro del P. Francisco Amico en Cali.",
            detailedHistory = "El 15 de febrero de 1990 el párroco basiliano P. Francisco Amico fue secuestrado. Miles de feligreses del oriente de Cali realizaron vigilias multitudinarias con velas exigiendo su libertad inmediata. Tras cinco días fue liberado ileso, declarando su decisión irrevocable de permanecer junto al pueblo de la Asunción.",
            liturgicalRank = "Aniversario Histórico y Memorial de Paz",
            suggestedPrayerId = "vida_comun",
            relatedPatronOrFounderId = "francisco_amico"
        ),
        BasilianCalendarDate(
            id = "cal_san_jose",
            month = 3,
            day = 19,
            dateFormatted = "19 de Marzo",
            title = "Solemnidad de San José, Esposo de la Virgen María",
            category = CalendarCategory.PATRON_FEAST,
            briefDescription = "Patrono del trabajo honrado, custodio de la Iglesia y protector de los Basilianos.",
            detailedHistory = "San José es modelo de silencio fecundo, obediencia sin dilación y dedicación esmerada al cuidado de la familia de Nazaret. Los colegios e internados basilianos le encomiendan las vocaciones de hermanos laicos y bienhechores.",
            liturgicalRank = "Solemnidad de la Iglesia Universal",
            suggestedPrayerId = "mesa",
            relatedPatronOrFounderId = "san_jose"
        ),
        BasilianCalendarDate(
            id = "cal_oscar_romero",
            month = 3,
            day = 24,
            dateFormatted = "24 de Marzo",
            title = "Fiesta de San Óscar Arnulfo Romero",
            category = CalendarCategory.PATRON_FEAST,
            briefDescription = "Arzobispo mártir, voz profética de los sin voz en América Latina.",
            detailedHistory = "San Óscar Romero fue asesinado en el altar en 1980 por defender con amor a los campesinos y pobres perseguidos. Es patrono inspirador de la pastoral social de los Padres Basilianos en Bogotá, Medellín y Tehuacán.",
            liturgicalRank = "Fiesta y Memoria de Mártires Latinoamericanos",
            suggestedPrayerId = "vida_comun",
            relatedPatronOrFounderId = "oscar_romero"
        ),
        BasilianCalendarDate(
            id = "cal_forma_de_vida",
            month = 5,
            day = 12,
            dateFormatted = "12 de Mayo",
            title = "Aprobación de 'La Forma de Vida Basiliana' (1983)",
            category = CalendarCategory.FOUNDATION_ANNIVERSARY,
            briefDescription = "Promulgación pontificia de las Constituciones renovadas post-Concilio Vaticano II.",
            detailedHistory = "En la Solemnidad de la Ascensión de 1983, la Santa Sede aprobó formalmente 'La Forma de Vida Basiliana', redactada tras años de consulta sinodal. Define la misión educadora sacerdotal en comunidad fraterna y servicio a los desfavorecidos.",
            liturgicalRank = "Jubileo Constitucional",
            suggestedPrayerId = "espiritu_santo_capitulo"
        ),
        BasilianCalendarDate(
            id = "cal_fiesta_tradicional_basilio",
            month = 6,
            day = 14,
            dateFormatted = "14 de Junio",
            title = "Fiesta Tradicional de San Basilio Magno & Aniversario de Reunión",
            category = CalendarCategory.PATRON_FEAST,
            briefDescription = "Conmemoración de la consagración episcopal de San Basilio (370 d.C.) y decreto de unión (1955).",
            detailedHistory = "Tradicionalmente celebrada como la fecha de ordenación episcopal de San Basilio Magno en Cesarea. En esta misma fecha de 1955 se firmó el decreto preliminar que reunificó las ramas de Francia y Norteamérica bajo un solo gobierno general.",
            liturgicalRank = "Fiesta Congregacional y Memorial de Unidad",
            suggestedPrayerId = "oracion_san_basilio",
            relatedPatronOrFounderId = "san_basilio"
        ),
        BasilianCalendarDate(
            id = "cal_casa_annonay_bogota",
            month = 8,
            day = 4,
            dateFormatted = "4 de Agosto",
            title = "Fundación de Casa Annonay en Bogotá (1997)",
            category = CalendarCategory.MISSION_COLOMBIA,
            briefDescription = "Apertura del Escolasticado Basiliano y casa de formación teológica en Bogotá.",
            detailedHistory = "El 4 de agosto de 1997 los Padres Basilianos bendijeron Casa Annonay en el barrio Egipto de Bogotá. Desde entonces alberga a los escolásticos que cursan teología en la Pontificia Universidad Javeriana y hoy atienden la Parroquia Nuestra Señora de Egipto.",
            liturgicalRank = "Aniversario Local de Fundación",
            suggestedPrayerId = "vocaciones_basilianas"
        ),
        BasilianCalendarDate(
            id = "cal_asuncion_virgen_colombia",
            month = 8,
            day = 15,
            dateFormatted = "15 de Agosto",
            title = "Solemnidad de la Asunción & Llegada a Colombia (1987)",
            category = CalendarCategory.MISSION_COLOMBIA,
            briefDescription = "Patrona de la Congregación y aniversario de la fundación basiliana en Cali (1987) y Medellín (1995).",
            detailedHistory = "Doble celebración magna: la Solemnidad de la Asunción de la Virgen María (protectora principal de los Basilianos) y la llegada en 1987 de los primeros cuatro misioneros a la Parroquia de la Asunción en Cali. En esta misma fecha de 1995 se inauguró el Noviciado en Medellín.",
            liturgicalRank = "Solemnidad Mayor y Aniversario Fundacional en Colombia",
            suggestedPrayerId = "angelus",
            relatedPatronOrFounderId = "virgen_maria"
        ),
        BasilianCalendarDate(
            id = "cal_decretum_laudis",
            month = 9,
            day = 15,
            dateFormatted = "15 de Septiembre",
            title = "Decretum Laudis concedido por Gregorio XVI (1837)",
            category = CalendarCategory.FOUNDATION_ANNIVERSARY,
            briefDescription = "Primer reconocimiento oficial pontificio de la Santa Sede a los Padres Basilianos.",
            detailedHistory = "Apenas quince años después de la fundación en Annonay, el Papa Gregorio XVI otorgó el Decreto de Alabanza a la Sociedad de Sacerdotes de San Basilio, elogiando su celo educativo, su vida fraterna en común y su fidelidad a la Iglesia.",
            liturgicalRank = "Aniversario Pontificio",
            suggestedPrayerId = "laudes"
        ),
        BasilianCalendarDate(
            id = "cal_san_miguel_reunion",
            month = 9,
            day = 29,
            dateFormatted = "29 de Septiembre",
            title = "Fiesta de San Miguel Arcángel & Decreto de Unión (1955)",
            category = CalendarCategory.FOUNDATION_ANNIVERSARY,
            briefDescription = "Reunificación plena de la Congregación y patrono de St. Michael's College.",
            detailedHistory = "En la fiesta de San Miguel de 1955 entró en vigor pleno el decreto de la Santa Sede que unificó a los Basilianos de Viviers (Francia) y de Toronto (América), finalizando más de treinta años de dolorosa separación territorial.",
            liturgicalRank = "Fiesta de San Miguel y Día de la Unidad Congregacional",
            suggestedPrayerId = "vida_comun"
        ),
        BasilianCalendarDate(
            id = "cal_san_francisco_asis",
            month = 10,
            day = 4,
            dateFormatted = "4 de Octubre",
            title = "Memoria de San Francisco de Asís",
            category = CalendarCategory.PATRON_FEAST,
            briefDescription = "Patrono del espíritu de pobreza evangélica, paz y fraternidad universal.",
            detailedHistory = "El Colegio de Annonay se originó en el antiguo convento franciscano de los Cordeliers. Los fundadores eligieron a San Francisco como patrono para cultivar un desapego alegre de los bienes materiales y una opción sincera por los pobres.",
            liturgicalRank = "Memoria Obligatoria y Patrono Especial",
            suggestedPrayerId = "visperas",
            relatedPatronOrFounderId = "san_francisco"
        ),
        BasilianCalendarDate(
            id = "cal_constituciones_1938",
            month = 10,
            day = 14,
            dateFormatted = "14 de Octubre",
            title = "Aprobación Definitiva de Constituciones (1938)",
            category = CalendarCategory.FOUNDATION_ANNIVERSARY,
            briefDescription = "Aprobación pontificia definitiva de las Constituciones Basilianas en Roma.",
            detailedHistory = "El 14 de octubre de 1938 la Santa Sede aprobó de forma definitiva las Constituciones de la Congregación de Sacerdotes de San Basilio, consolidando la estructura canónica y la identidad apostólica en la educación superior y colegios.",
            liturgicalRank = "Aniversario Canónico",
            suggestedPrayerId = "espiritu_santo_capitulo"
        ),
        BasilianCalendarDate(
            id = "cal_fundacion_annonay_1822",
            month = 11,
            day = 21,
            dateFormatted = "21 de Noviembre",
            title = "Día de la Fundación Basiliana en Annonay (1822)",
            category = CalendarCategory.FOUNDATION_ANNIVERSARY,
            briefDescription = "Presentación de la Santísima Virgen María y nacimiento canónico de la Congregación de San Basilio.",
            detailedHistory = "El 21 de noviembre de 1822, en el oratorio del Colegio de Annonay (Francia), diez sacerdotes educadores aprobaron asociarse formalmente en vida común bajo el patrocinio de San Basilio Magno y eligieron al P. Joseph Lapierre como superior. Exactamente 30 años después, el 21 de noviembre de 1852, se emitieron los primeros votos perpetuos.",
            liturgicalRank = "Solemnidad Congregacional Mayor (Día de la Fundación)",
            suggestedPrayerId = "oracion_san_basilio",
            relatedPatronOrFounderId = "joseph_lapierre"
        ),
        BasilianCalendarDate(
            id = "cal_aprobacion_pontificia_1863",
            month = 11,
            day = 23,
            dateFormatted = "23 de Noviembre",
            title = "Decreto de Aprobación Pontificia por Pío IX (1863)",
            category = CalendarCategory.FOUNDATION_ANNIVERSARY,
            briefDescription = "La Santa Sede eleva formalmente a los Padres Basilianos a Instituto de Derecho Pontificio.",
            detailedHistory = "Gracias a las gestiones del P. Julien Actorie, el beato Papa Pío IX concedió el 23 de noviembre de 1863 el Decreto de Aprobación Canónica formal para la Congregación de San Basilio, otorgándole jurisdicción y misión apostólica universal.",
            liturgicalRank = "Aniversario Pontificio Solemne",
            suggestedPrayerId = "oracion_san_basilio",
            relatedPatronOrFounderId = "julien_actorie"
        ),
        BasilianCalendarDate(
            id = "cal_san_juan_evangelista",
            month = 12,
            day = 27,
            dateFormatted = "27 de Diciembre",
            title = "Fiesta de San Juan Apóstol y Evangelista",
            category = CalendarCategory.PATRON_FEAST,
            briefDescription = "El discípulo amado, testigo de la comunión fraterna y el mandamiento del amor.",
            detailedHistory = "San Juan Evangelista enseñó que 'Dios es amor y quien permanece en el amor permanece en Dios'. Los Basilianos honran su fiesta meditando la pureza de intención, la intimidad con Jesús Eucarístico y la lealtad fraterna.",
            liturgicalRank = "Fiesta del Calendario Litúrgico",
            suggestedPrayerId = "completas"
        )
    )

    val generalCuria2026: List<String> = listOf(
        "P. John B. Huber, CSB — Superior General (2026–presente)",
        "P. Kevin J. Storey, CSB — Superior General Emérito (2018–2026)",
        "P. Maurice Restivo, CSB — Consejero General",
        "P. Pedro M. Mora, CSB — Consejero General (Párroco en Ntra. Sra. de Egipto, Bogotá)",
        "P. Terry Kersch, CSB — Consejero General",
        "P. Paul F. English, CSB — Consejero General (Houston)",
        "P. Jefferson M. Thompson, CSB — Secretario General",
        "P. Andrew F. B. Leung, CSB — Tesorero General",
        "Sede de la Curia General: 146 Davenport Road, Toronto, Ontario, Canadá M5R 1J2"
    )

    val colombiaPresence: List<String> = listOf(
        "Bogotá D.C. — Parroquia Nuestra Señora de Egipto: centro litúrgico, caritativo y comunitario en el centro histórico.",
        "Bogotá D.C. — Casa Annonay y Teologado: casa central de los Basilianos en Colombia, Noviciado Internacional y escolasticado teológico.",
        "Medellín (Antioquia) — Parroquia San Basilio: labor pastoral parroquial, grupos juveniles y catequesis familiar.",
        "Raíz Histórica Fundacional en Cali (1987): llegada de los primeros misioneros basilianos a la Parroquia de la Asunción y Aguablanca (hoy entregada a la Arquidiócesis; actualmente los Padres Basilianos ya no están en Cali).",
        "Pastoral Vocacional Colombia: acompañamiento continuo a jóvenes discernientes para el sacerdocio y la vida consagrada basiliana."
    )
}
