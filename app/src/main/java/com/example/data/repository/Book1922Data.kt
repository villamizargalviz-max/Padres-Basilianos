package com.example.data.repository

import com.example.data.model.Book1922Chapter
import com.example.data.model.BookSectionContent
import com.example.data.model.GovernmentEvolutionEra
import com.example.data.model.SuperiorGeneralHistoricalRecord

object Book1922Data {

    const val bookTitle = "1922: Antes y Después"
    const val originalTitle = "1922: Before and After"
    const val author = "P. Kevin J. Kirley, CSB"
    const val publicationDate = "21 de Noviembre de 1992"
    const val provenance = "Parroquia San Basilio • Medellín, Antioquia, Colombia"
    const val translator = "Traducido al español para la misión basiliana"

    val chapters: List<Book1922Chapter> = listOf(
        Book1922Chapter(
            id = "intro",
            partNumber = "Introducción",
            partTitle = "Mandato Histórico y Propósito",
            chapterTitle = "La Mirada al Punto Crítico de Nuestra Historia",
            subtitle = "Para el Capítulo General de 1993",
            pageRange = "Páginas 1 – 3",
            summary = "El P. Kevin Kirley CSB expone el mandato del Superior General Jack Gallagher: conocer a fondo los acontecimientos de 1922 y sus antecedentes para iluminar con verdad y caridad el rumbo futuro de la Congregación de San Basilio.",
            sections = listOf(
                BookSectionContent(
                    title = "El Mandato del Superior General",
                    paragraphs = listOf(
                        "Sería presuntuoso, como lo sugiere el título, consignar la totalidad de nuestra historia Basiliana en unas pocas páginas, si no fuera por el claro mandato que este autor recibiera del superior general: «Si toda la congregación conociera mejor nuestra historia, hasta 1922 e inmediatamente después, esto le adjudicaría más perspectivas a nuestras discusiones hasta el capítulo de 1993».",
                        "En verdad, Jack Gallagher tenía en mente un período limitado de tiempo antes y después de 1922, como dijera él, con la lente dirigida a «los esfuerzos positivos cumplidos para poner un orden relativamente grande a cierto caos existente».",
                        "Sin embargo, para apreciar este punto crítico de nuestra historia, 1922, tenemos que retrotraernos por lo menos un siglo o más para revisar como mínimo los hechos relevantes, y luego, con el fin de hacer una evaluación de su impacto, tenemos que mirar la manera como nuestra congregación ha evolucionado desde los días del generalato de Frank Forster hasta hoy.",
                        "Esta obra se divide en tres partes: la primera parte se dedica a los esfuerzos y realizaciones de nuestros primeros cien y algunos años más, 1800-1922; la segunda parte se concentra en las razones de por qué 1922 es una fecha significativa en la historia Basiliana; la tercera intenta medir el espacio recorrido en el entretanto."
                    )
                ),
                BookSectionContent(
                    title = "Alcance y Fuentes Archivísticas",
                    paragraphs = listOf(
                        "Para los historiadores de la iglesia las fuentes de esta obra parecerán limitadas, quizás lo suficiente para relegarla a la categoría de «un ensayo fundamentado en archivos». Concedido. El alcance de esta obra es específico y abiertamente Basiliano: lo que algunos Basilianos han escrito acerca de nuestra historia, los registros de reuniones de nuestros administradores, capítulos, cartas, periódicos, al igual que las tradiciones y costumbres que han pasado de una generación Basiliana a otra.",
                        "Puede que no sea una historia brillante o sorprendente, en relación con la de otras órdenes e institutos, pero es nuestra historia y no nos avergonzamos de ella. La manera de interpretarla como ayuda para un desarrollo futuro es un asunto de nuestra absoluta responsabilidad."
                    )
                )
            ),
            keyTakeaways = listOf(
                "La historia no se oculta ni se maquilla: es el fundamento para discernir el presente y futuro.",
                "El estudio abarca desde la fundación clandestina en 1800 hasta la consolidación tras el Concilio Vaticano II.",
                "Publicado en la Parroquia San Basilio de Medellín como puente fraterno entre América del Norte y Colombia."
            ),
            quotes = listOf(
                "«Es nuestra historia y no nos avergonzamos de ella. La manera de interpretarla como ayuda para un desarrollo futuro es asunto de nuestra absoluta responsabilidad.» — P. Kevin J. Kirley, CSB"
            )
        ),
        Book1922Chapter(
            id = "parte1",
            partNumber = "Parte I",
            partTitle = "1800 – 1922: Un Siglo o Más de Esfuerzo",
            chapterTitle = "Desde la Revolución Francesa hasta la Expansión Misionera",
            subtitle = "Saint-Symphorien, Annonay, Toronto y la Prueba del Exilio",
            pageRange = "Páginas 4 – 12",
            summary = "Describe los orígenes clandestinos de la escuela en 1797, el traslado a Annonay en 1802, las escuelas Sainte-Barbe y Sainte-Claire, la fundación oficial el 21 de noviembre de 1822 en Maison-Seule, el viaje transatlántico a Canadá en 1852 y las persecuciones de 1903 en Francia.",
            sections = listOf(
                BookSectionContent(
                    title = "Orígenes Clandestinos en el Terror Revolucionario (1797–1802)",
                    paragraphs = listOf(
                        "Quien quiera que haya leído el libro de Charles Roume sobre los primeros 50 años de la Historia Basiliana no puede dejar de sorprenderse por la manera como nuestra congregación sobrevivió. En varias oportunidades el final parecía inminente. Pero la divina Providencia, a través de personas comunes y corrientes, siempre encontró una solución a las más angustiosas situaciones.",
                        "Desde el comienzo, los Basilianos tuvieron que enfrentar tensiones, enjuiciamientos, incluso el caos. Después de todo, las dos personas responsables del trabajo original de la comunidad, el arzobispo D'Aviau de Vienne y Joseph Bouvier Lapierre, aparecían como personas fuera de la ley a los ojos del Estado Revolucionario Francés. Fueron sacerdotes rebeldes, apátridas, uno de los cuales rehusó prestar juramento de lealtad a la Constitución Civil del Clero en 1790; el otro, que sí lo prestó, inmediatamente se retractó.",
                        "Era ambicioso, valeroso y extremadamente peligroso promocionar la educación cristiana en las fauces del Reino del Terror, pero eso fue exactamente lo que hizo el arzobispo cuando se puso en contacto por primera vez con Lapierre en 1797 y le ordenara organizar una escuela presbiteral clandestina en Saint-Symphorien-de-Mahun, y ocuparse de esa parroquia.",
                        "La atmósfera anticlerical que prevalecía en gran parte de Francia entre los líderes civiles y políticos puede percibirse en el informe del Comisionado de la Policía para la Ardèche cuando escribía en 1799: «Los sacerdotes son los únicos enemigos del gobierno y tan pronto sean extirpados de la sociedad no habrá ni terrorismo, ni monarquismo, ni crímenes en Francia»."
                    )
                ),
                BookSectionContent(
                    title = "El Colegio de Annonay y la Fundación en Maison-Seule (1802–1822)",
                    paragraphs = listOf(
                        "En 1802, la escuela se trasladó a Annonay con 57 internos y 75 externos. Los sacerdotes profesores abrieron una segunda escuela en Annonay en 1805, Sainte-Barbe, un seminario menor para los hijos de familias pobres fundado por el P. Jacques-Vincent Duret. Una tercera escuela se abrió en 1813, Sainte-Claire, para estudiantes que se dedicarían al comercio y la industria.",
                        "En septiembre de 1822 estos mismos «caballeros» le presentaron al obispo Brulley de la Brunière una petición para ser reconocidos como una asociación de sacerdotes de la diócesis dedicados al trabajo apostólico de la enseñanza y la predicación.",
                        "La asociación entró en vigor oficial en la fiesta de Nuestra Señora de la Presentación, el 21 de Noviembre de 1822, fecha en la cual los padres sostuvieron su primer capítulo general en Maison-Seule (Annonay) y eligieron a Joseph Lapierre como primer superior general. Después de algunas discusiones escogieron a San Basilio Magno como patrono, lo que les identificó en adelante como la Asociación de Sacerdotes de San Basilio.",
                        "«Éramos hombres con ideas y temperamentos diferentes y vimos la necesidad de asociarnos. Ese era nuestro proyecto; pero ¿cuáles eran las bases de ese proyecto y cuáles los medios de realizarlo? Estábamos muy confundidos. Lo que sí podíamos decir era que teníamos intenciones claras y puras, al igual que los fines.» (P. Pierre Tourvieille)."
                    )
                ),
                BookSectionContent(
                    title = "Misión Transatlántica a Toronto (1852) y Aprobación Pontificia (1863)",
                    paragraphs = listOf(
                        "Para junio de 1851, la Asociación ascendía a 21 miembros. Tourvieille autorizó a Patrick Moloney para que acompañara al obispo Armand de Charbonnel (antiguo alumno de Annonay) al Nuevo Mundo. En este momento le pedían que enviara 4 miembros con el fin de montar una escuela en Toronto.",
                        "En los años subsiguientes la casa matriz en Annonay tuvo que realizar ingentes sacrificios tanto en recursos humanos como en dinero para sentar las bases de la misión en Canadá: Toronto (St. Michael's College, 1852), Sandwich (Assumption College, 1856), Amherstburg (1878) y Owen Sound (1863).",
                        "En 1863, bajo el generalato del P. Julien Actorie, la Santa Sede promulgó el Decreto de Aprobación Pontificia emitido por el Papa Pío IX, confiriendo a los Basilianos el estatus canónico universal."
                    )
                ),
                BookSectionContent(
                    title = "La Persecución Religiosa y el Frenesí de 1903",
                    paragraphs = listOf(
                        "Las leyes laicistas francesas de 1880 afectaron a las órdenes religiosas. En los albores del siglo XX, el gobierno republicano bajo Emile Combes llevó a cabo una barrida total que obligó a las comunidades a dispersarse. Edificios y mobiliario fueron rematados en subasta pública; casi todos los ingresos sirvieron para pagar tarifas legales.",
                        "A la muerte del P. Adrien Fayolle en 1898, le correspondió al P. Noël Durand presidir los momentos más dolorosos de la dispersión de 1903. La comunidad francesa sufrió un golpe devastador, obligando a muchos a buscar refugio en Inglaterra (Beaconfield) o a emigrar a América."
                    )
                )
            ),
            keyTakeaways = listOf(
                "La Congregación nació en la clandestinidad del martirio y la persecución política.",
                "El 21 de noviembre de 1822 se fundó formalmente en Annonay eligiendo a San Basilio Magno.",
                "En 1852 cruzaron el Atlántico a Canadá para fundar St. Michael's College.",
                "La crisis anticlerical de 1903 en Francia dispersó la casa matriz y aceleró el eje hacia América."
            ),
            quotes = listOf(
                "«Los sacerdotes son los únicos enemigos del gobierno y tan pronto sean extirpados no habrá ni terrorismo ni crímenes.» — Informe policial de 1799 sobre los primeros basilianos",
                "«Éramos hombres con ideas y temperamentos diferentes y vimos la necesidad de asociarnos... teníamos intenciones claras y puras.» — P. Pierre Tourvieille"
            )
        ),
        Book1922Chapter(
            id = "parte2",
            partNumber = "Parte II",
            partTitle = "La Importancia del Año 1922",
            chapterTitle = "La Separación Canónica y el Renacimiento en América",
            subtitle = "El Decreto de Separación de 1922 y el Liderazgo de Francis Forster",
            pageRange = "Páginas 13 – 23",
            summary = "Analiza las tensiones administrativas y culturales entre Francia y América, el Decreto de Separación de la Santa Sede del 14 de junio de 1922, y la colosal obra organizativa y espiritual del P. Francis Forster para implantar el Código Canónico de 1917 y la vida común.",
            sections = listOf(
                BookSectionContent(
                    title = "El Crecimiento en el Nuevo Mundo y las Tensiones Administrativas",
                    paragraphs = listOf(
                        "Después de que el primer canadiense, Miguel Ferguson, se ordenara en 1863, el crecimiento continuó en forma lenta pero firme. En 1922 el número de miembros en América ascendía a 91 (59 sacerdotes y 32 profesores).",
                        "Las dificultades administrativas surgieron principalmente debido a la falta de precisión de las constituciones en la sección de gobierno. Los miembros canadienses y norteamericanos se sentían incómodos con un gobierno centralizado en Francia que desconocía las realidades locales.",
                        "Surgió un debate profundo sobre la naturaleza de los votos y la formación de los jóvenes. Los padres Daniel Cushing, Robert McBrady, Henry Carr y Michael V. Kelly lideraron la solicitud de reformas ante el visitador apostólico Joseph Schwarz CSSR en 1911."
                    )
                ),
                BookSectionContent(
                    title = "El Decreto de Separación del 14 de Junio de 1922",
                    paragraphs = listOf(
                        "La Santa Sede emitió el Decreto de Separación el 14 de Junio de 1922, por el cual la comunidad Basiliana se convirtió en dos distintas congregaciones religiosas:",
                        "1) Los Padres de San Basilio de Viviers (Francia), eligiendo como superior general a Julien Giraud.",
                        "2) Los Padres de San Basilio de Toronto (Canadá/EE.UU.), eligiendo como superior general a Francis Forster.",
                        "Esta separación amistosa permitió a la rama americana adoptar con celeridad las normas del Código de Derecho Canónico de 1917 y expandir su apostolado educativo universitario sin trabas burocráticas transcontinentales."
                    )
                ),
                BookSectionContent(
                    title = "La Tarea que Acometió Forster: Voto de Pobreza y Vida Común",
                    paragraphs = listOf(
                        "Forster estaba al frente de una tarea enorme: enseñar a los hermanos lo que significaba ser religioso de acuerdo con las prescripciones del Código Canónico de 1917: pobreza, castidad y obediencia.",
                        "Hasta 1922, los Basilianos habían vivido bajo un sistema de voto modificado donde retenían posesión de bienes y recibían una asignación anual. El capítulo general de 1922 votó 14 contra 2 a favor de convertir la comunidad en una congregación religiosa canónica con voto simple de pobreza y vida común ('mensae communis').",
                        "Todo estipendio y ganancia se entregaba a partir de entonces al tesoro de la comunidad; los fondos personales debían ser compartidos en beneficio de los estudiantes pobres y la misión."
                    )
                ),
                BookSectionContent(
                    title = "Expansión Institucional bajo Forster (1922–1929)",
                    paragraphs = listOf(
                        "Entre 1922 y 1929, los Basilianos fundaron Strawberry Island (1922), trabajaron en Aquinas Institute en Rochester (1927), abrieron Catholic Central High School en Detroit (1928), asumieron la Parroquia Santa Ana en Houston (1928), y en 1929 fundaron el afamado Institute of Mediaeval Studies en Toronto.",
                        "Para 1928 los Basilianos contaban en América con 147 miembros: 85 padres, 39 profesos y 23 novicios. Debido a su trágica muerte por ahogamiento en 1929, Forster no vio la aprobación romana definitiva de 1938, pero sentó las bases indestructibles de la congregación moderna."
                    )
                )
            ),
            keyTakeaways = listOf(
                "El 14 de junio de 1922 Roma decretó la división en dos ramas fraternas (Viviers y Toronto).",
                "El P. Francis Forster transformó la comunidad en una verdadera congregación religiosa canónica.",
                "Se instauró la vida común estricta y el desprendimiento evangélico del voto de pobreza.",
                "Nacieron obras emblemáticas como Catholic Central Detroit, Santa Ana Houston y el Instituto de Estudios Medievales de Toronto."
            ),
            quotes = listOf(
                "«Sin la observancia fiel de la constitución, ninguna sociedad religiosa puede continuar existiendo... Debemos tenerla constantemente ante nuestros ojos y seguir sus prescripciones.» — P. Francis Forster, CSB (1923)"
            )
        ),
        Book1922Chapter(
            id = "parte3",
            partNumber = "Parte III",
            partTitle = "¿Hasta Dónde Hemos Llegado?",
            chapterTitle = "El Legado de Forster, la Reunificación de 1955 y el Concilio",
            subtitle = "Henry Carr, McCorkell, Flahiff y la Forma de Vida Basiliana",
            pageRange = "Páginas 24 – 31",
            summary = "Recorre la consolidación con Henry Carr y Edmund McCorkell, el histórico Decreto de Unión del 29 de septiembre de 1955 que reunificó a Francia y América, la renovación del Concilio Vaticano II y la aprobación definitiva de la Forma de Vida Basiliana.",
            sections = listOf(
                BookSectionContent(
                    title = "El Generalato de Henry Carr (1930–1942)",
                    paragraphs = listOf(
                        "Henry Carr tenía una mente pastoral y misionera. Creía que la ley debía adaptarse al espíritu de familia y a las necesidades reales de los pueblos.",
                        "Envió sacerdotes a estudios de posgrado en Europa, fortaleció los colegios y, con espíritu evangelizador, impulsó en 1935 el apostolado con los inmigrantes hispanos y mexicanos en el área de Houston y Rosenberg (Texas), con un área misionera de más de 5,000 millas cuadradas.",
                        "Abrió el St. Thomas More College en Saskatoon (1936) y expandió la presencia intelectual de los Basilianos en Norteamérica."
                    )
                ),
                BookSectionContent(
                    title = "Edmund McCorkell y la Gran Reunificación de 1955",
                    paragraphs = listOf(
                        "Bajo Edmund McCorkell (1942–1954) el número de miembros creció de 171 a 531 en 1954 (312 sacerdotes, 196 profesos, 23 novicios). Se fundaron la Universidad de St. Thomas en Houston (1947), St. John Fisher College en Rochester (1951) y St. Mark's College en Vancouver.",
                        "Con la colaboración de Charles Roume en Francia, se preparó la histórica reunificación.",
                        "El 29 de Septiembre de 1955, fiesta de San Miguel Arcángel, en Annonay (Francia), George Flahiff y Charles Roume firmaron y sellaron el Decreto de Unión. Tras 33 años de separación fraterna, las ramas francesa y americana volvieron a ser una sola familia religiosa en todo el orbe católico."
                    )
                ),
                BookSectionContent(
                    title = "Del Concilio Vaticano II a la 'Forma de Vida Basiliana'",
                    paragraphs = listOf(
                        "El Concilio Vaticano II convocado por San Juan XXIII impulsó la renovación de los institutos religiosos, volviendo a las fuentes del carisma original.",
                        "Entre 1967 y 1983, bajo los superiores generales Joseph Wey, James Hanrahan y Ulysse Paré, los Basilianos redactaron la 'Forma de Vida Basiliana' y el 'Gobierno y Costumbres Basilianas', documentos aprobados plenamente por la Santa Sede en 1977 y ratificados conforme al Código Canónico de 1983.",
                        "Como lo llamó Ulysse Paré, la Forma de Vida es «un vínculo viviente de unidad e inspiración compartida por toda la familia Basiliana»."
                    )
                ),
                BookSectionContent(
                    title = "Conclusión: El Celo Renovado por el Reino",
                    paragraphs = listOf(
                        "Comparada con la de los Benedictinos, Cistercienses o Franciscanos, nuestra historia Basiliana es corta. Probablemente no sea edificante en todos sus aspectos, pero tampoco es oscurecedora. Es nuestro compromiso con el Señor, realizado a través de ensayos y trabajos durante casi dos siglos.",
                        "Las dos épocas durante las cuales nuestra comunidad pareció florecer y atraer nuevos elementos, después de la muerte de Julien Actorie en 1865 y Francis Forster en 1929, coinciden con su renovado esfuerzo por parte de sus miembros para vivir de acuerdo con un ideal religioso.",
                        "Si todavía hoy creemos que el Espíritu Santo puede mover a las personas precisas a decir las cosas precisas a la hora precisa, y a convertir la palabra en acción, entonces los Basilianos de hoy y los jóvenes que se nos unan en el futuro nos dispondremos, con celo renovado, a edificar «el reino de Jesús, el reino de la verdad y la justicia, del amor y de la paz»."
                    )
                )
            ),
            keyTakeaways = listOf(
                "Henry Carr inició el ministerio misionero hispano en Texas en 1935.",
                "El 29 de septiembre de 1955 se celebró la Reunificación total en Annonay.",
                "La Forma de Vida Basiliana (1977/1983) condensó la renovación conciliar con el carisma de los fundadores.",
                "La historia convoca a los basilianos de hoy (incluyendo Colombia y México) a edificar la justicia y la paz."
            ),
            quotes = listOf(
                "«El 29 de septiembre de 1955, el día de San Miguel Arcángel, George Flahiff selló el decreto de unión en Annonay... este feliz suceso añadió miembros jóvenes y llenos de energía a la congregación.»",
                "«Nos dispondremos, con celo renovado, a edificar el reino de Jesús, el reino de la verdad y la justicia, del amor y de la paz.» — Oración preparatoria al Capítulo General"
            )
        )
    )

    val superiorsGeneralSince1822: List<SuperiorGeneralHistoricalRecord> = listOf(
        SuperiorGeneralHistoricalRecord(
            name = "P. Joseph Bouvier Lapierre",
            years = "1757 – 1838",
            period = "1822 – 1838",
            jurisdiction = "Congregación Unificada (Francia)",
            keyMilestones = "Fundador de la escuela clandestina en Saint-Symphorien (1797). Elegido primer Superior General el 21 de nov de 1822 en Annonay."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Pierre François Tourvieille",
            years = "1780 – 1859",
            period = "1838 – 1859",
            jurisdiction = "Congregación Unificada (Francia y Canadá)",
            keyMilestones = "Gran pedagogo y matemático. Redactó las primeras reglas y envió en 1852 a los primeros misioneros a fundar St. Michael's en Toronto."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Julien Actorie",
            years = "1803 – 1864",
            period = "1859 – 1864",
            jurisdiction = "Congregación Unificada",
            keyMilestones = "Obtuvo del Papa Pío IX el Decreto de Aprobación Pontificia formal de la Congregación de San Basilio en 1863."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Jean-Mathieu Soulerin",
            years = "1807 – 1879",
            period = "1865 – 1879",
            jurisdiction = "Congregación Unificada",
            keyMilestones = "Primer superior en haber vivido y trabajado en Canadá. Expandió colegios en Francia, Canadá y Argelia."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Adrien Fayolle",
            years = "1830 – 1898",
            period = "1880 – 1898",
            jurisdiction = "Congregación Unificada",
            keyMilestones = "Enfrentó las leyes antirreligiosas francesas de 1880; fundó la casa de exilio en Plymouth y Beaconfield (Inglaterra)."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Noël Durand",
            years = "1850 – 1928",
            period = "1898 – 1910",
            jurisdiction = "Congregación Unificada",
            keyMilestones = "Resistió el frenesí de expulsión y remate de bienes de 1903 bajo Emile Combes en Francia."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Victorin Marijon",
            years = "1845 – 1932",
            period = "1910 – 1914",
            jurisdiction = "Congregación Unificada",
            keyMilestones = "Gobernó durante la Visita Apostólica y el Capítulo de Ginebra de 1910."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. James Player",
            years = "1858 – 1930",
            period = "1914 – 1922",
            jurisdiction = "Superior General Suplente",
            keyMilestones = "Gobernó durante los difíciles años de la Primera Guerra Mundial hasta la separación canónica de 1922."
        ),
        // Período de Separación 1922 - 1955
        SuperiorGeneralHistoricalRecord(
            name = "P. Francis Forster (América)",
            years = "1875 – 1929",
            period = "1922 – 1929",
            jurisdiction = "Padres de San Basilio de Toronto (América)",
            keyMilestones = "Pilar de la congregación moderna en América. Instauró el voto de pobreza canónico, la vida común y centros universitarios de vanguardia."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Julien Giraud (Francia)",
            years = "1860 – 1935",
            period = "1922 – 1923",
            jurisdiction = "Padres de San Basilio de Viviers (Francia)",
            keyMilestones = "Primer superior de la rama francesa tras el Decreto de Separación de 1922."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Victorin Marijon (Francia)",
            years = "1845 – 1932",
            period = "1923 – 1932",
            jurisdiction = "Rama de Viviers (Francia)",
            keyMilestones = "Segundo mandato al frente de las obras educativas y vocacionales francesas."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Henry Carr (América)",
            years = "1880 – 1963",
            period = "1930 – 1942",
            jurisdiction = "Padres de San Basilio de Toronto (América)",
            keyMilestones = "Filósofo insigne; impulsó el Instituto Pontificio de Estudios Medievales y el apostolado con hispanos en Texas (1935)."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Octave Descellière (Francia)",
            years = "1878 – 1960",
            period = "1932 – 1950",
            jurisdiction = "Rama de Viviers (Francia)",
            keyMilestones = "Mantuvo vivas las escuelas en Annonay y Aubenas durante la Segunda Guerra Mundial."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Edmund McCorkell (América)",
            years = "1891 – 1978",
            period = "1942 – 1954",
            jurisdiction = "Padres de San Basilio de Toronto (América)",
            keyMilestones = "Multiplicó la congregación a más de 500 miembros; fundó la Universidad de St. Thomas (Houston) y preparó la reunificación."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Charles Roume (Francia)",
            years = "1895 – 1982",
            period = "1950 – 1955",
            jurisdiction = "Rama de Viviers (Francia)",
            keyMilestones = "Co-firmante del histórico Decreto de Reunificación de 1955 en Annonay."
        ),
        // Después de la Reunificación de 1955
        SuperiorGeneralHistoricalRecord(
            name = "Cardenal George B. Flahiff",
            years = "1905 – 1989",
            period = "1954 – 1961",
            jurisdiction = "Congregación Reunificada Universal",
            keyMilestones = "Firmó el Decreto de Unión el 29 de septiembre de 1955 en Annonay. Nombrado Arzobispo de Winnipeg y luego Cardenal de la Iglesia."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Joseph Wey",
            years = "1912 – 1999",
            period = "1961 – 1973",
            jurisdiction = "Congregación Reunificada",
            keyMilestones = "Guio a la Congregación durante todo el Concilio Vaticano II y abrió la histórica misión a México (1961)."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. James Hanrahan",
            years = "1926 – 2012",
            period = "1973 – 1981",
            jurisdiction = "Congregación Reunificada",
            keyMilestones = "Promulgó la 'Forma de Vida Basiliana' en 1977 adaptando el carisma a la renovación conciliar."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Ulysse E. Paré",
            years = "1923 – 2016",
            period = "1981 – 1989",
            jurisdiction = "Congregación Reunificada",
            keyMilestones = "Confirmó las Constituciones con el Código Canónico de 1983; bajo su gobierno se fundó la misión en Colombia (Cali, 1987)."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Jack Gallagher",
            years = "1932 – 2020",
            period = "1989 – 1997",
            jurisdiction = "Congregación Reunificada",
            keyMilestones = "Encargó al P. Kevin Kirley la redacción de '1922: Antes y Después'; consolidó la presencia en Colombia (Bogotá y Medellín)."
        ),
        SuperiorGeneralHistoricalRecord(
            name = "P. Kevin J. Storey",
            years = "1960 – Presente",
            period = "2018 – 2026",
            jurisdiction = "Congregación Reunificada (Superior General Actual)",
            keyMilestones = "Lideró las celebraciones del Bicentenario Basiliano (1822–2022) y el traslado de la Curia General al 146 Davenport Rd en Toronto."
        )
    )

    val governmentEvolution: List<GovernmentEvolutionEra> = listOf(
        GovernmentEvolutionEra(
            era = "1822 – 1922",
            title = "Primer Siglo: Superior General y Consejo Unificado",
            structureDescription = "Un Superior General central en Francia con 4 Consejeros Generales, Secretario y Tesorero General.",
            details = listOf(
                "Administración Provincial en Canadá y EE.UU. creada en 1883 con Charles Vincent como primer provincial.",
                "Provincia de Francia erigida formalmente en 1910.",
                "Tensiones por la lejanía de las decisiones en Annonay respecto al vertiginoso ritmo escolar canadiense."
            )
        ),
        GovernmentEvolutionEra(
            era = "1922 – 1955",
            title = "Separación Canónica Fraterna",
            structureDescription = "Dos ramas autónomas aprobadas por Roma: Padres Basilianos de Viviers (Francia) y Padres Basilianos de Toronto (América).",
            details = listOf(
                "Cada rama con su propio Superior General y Consejo General independiente.",
                "Rama americana adopta el Código Canónico de 1917, voto simple de pobreza y vida común ('mensae communis').",
                "Rama francesa mantiene los colegios de Annonay y Aubenas en medio de las guerras europeas."
            )
        ),
        GovernmentEvolutionEra(
            era = "1955 – 1967",
            title = "La Gran Reunificación",
            structureDescription = "Decreto de Unión sellado en Annonay el 29 de septiembre de 1955 bajo George Flahiff y Charles Roume.",
            details = listOf(
                "Un solo Superior General para el mundo entero con 4 Consejeros Generales.",
                "Entrada de nuevas vocaciones y expansión hacia América Latina (México 1961).",
                "Preparación para el Concilio Vaticano II."
            )
        ),
        GovernmentEvolutionEra(
            era = "1967 – Presente",
            title = "Gobierno Colegiado e Intercultural",
            structureDescription = "Superior General, Vicario General / Primer Asistente, Segundo Asistente y Cuatro Representantes Regionales.",
            details = listOf(
                "Representantes de: Este de Canadá, Oeste de Canadá, Estados Unidos, y Región de América Latina (Colombia y México).",
                "Secretaría General y Procuraduría General.",
                "Constitución aprobada por la Santa Sede en la 'Forma de Vida Basiliana'."
            )
        )
    )
}
