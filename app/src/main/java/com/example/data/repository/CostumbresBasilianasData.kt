package com.example.data.repository

import com.example.data.model.CostumbresArticle
import com.example.data.model.CostumbresDocument
import com.example.data.model.CostumbresPreface
import com.example.data.model.CostumbresSection

object CostumbresBasilianasData {

    val preface = CostumbresPreface(
        title = "Prefacio a la revisión de 2022 de las Costumbres Basilianas",
        approvedBy = "Aprobada por el Capítulo General de 2022 (incluye cambios aprobados en 1989, 1993, 1997, 2001, 2012, 2014 y 2018)",
        reviewCommittee = listOf(
            "Padre Timothy Scott, CSB",
            "Padre Daniel Callam, CSB",
            "Padre William Irwin, CSB",
            "Padre Gordon Judd, CSB",
            "Padre Dennis Noelke, CSB"
        ),
        translators = listOf(
            "Padre Jack Hanna, CSB",
            "Padre Juan Carlos Rojas, CSB",
            "Padre Paul Walsh, CSB",
            "Padre Bob Barringer, CSB"
        ),
        keyAmendments2022 = listOf(
            "Dependencia y acompañamiento terapéutico (E.21)",
            "Años posibles para la exclaustración (I.06)",
            "Forma de votación para los delegados al Capítulo General (J.06)",
            "Protocolo para el Consejo General a final de su término (J.33)",
            "Aclaraciones de la relación de los Consejos Regionales con el Consejo General (N.21 y N.22)",
            "Supresión formal de referencias al postulantado (L.08(g) y H.16)"
        ),
        author = "Padre Kevin Storey CSB",
        authorTitle = "Superior General",
        content = listOf(
            "Esta versión de las Costumbres Basilianas, el documento no-constitucional de la Congregación de San Basilio, fue aprobada por el Capítulo General de 2022. Esta revisión de 2022 incluye los cambios aprobados por los Capítulos de 1989, 1993, 1997, 2001, 2012, 2014 y 2018.",
            "Una cuidadosa y completa revisión de las Costumbres fue presentada al Capítulo de 2014 por el Comité de Revisión. Los padres Timothy Scott, Daniel Callam, William Irwin, Gordon Judd, y Dennis Noelke formaron el comité. Ellos no querían producir nueva legislación; ellos redactaron y dieron más claridad al documento y eliminaron trozos no necesarios. Les agradezco a los padres Jack Hanna, Juan Carlos Rojas, Paul Walsh, y Bob Barringer que colaboraron en la traducción de las varias versiones al español.",
            "El Capítulo de 2022 autorizó seis enmiendas y cambios a las Costumbres. Estos se verán en el artículo que tiene que ver con las varias formas de dependencia (E.21), en los años posibles para la exclaustración (I.06), en la forma de votación para los delegados al Capítulo General (J.06), en el protocolo para el Consejo General a final de su término (J.33), y con unas aclaraciones de la relación de los Consejos Regionales con el Consejo General (N.21 y N.22). Después del Capítulo, el Consejo General autorizó la supresión de dos referencias al 'postulantado' (L.08(g) y H.16) ya que el postulantado formal no existe en nuestra Congregación.",
            "La Forma de Vida Basiliana y las Costumbres Basilianas forman nuestros documentos esenciales; estos documentos guían a nuestro Consejo General y a la Congregación entera. Que el Señor nos siga bendiciendo en nuestro servicio al Pueblo de Dios. ¡Alabado sea el Señor!"
        )
    )

    val document = CostumbresDocument(
        title = "Costumbres Basilianas",
        subtitle = "El Documento no-constitucional de la Congregación de Sacerdotes de San Basilio (Padres Basilianos)",
        institution = "Congregación de Sacerdotes de San Basilio",
        cityAndYear = "Toronto, Canadá • 2022",
        bicentennialNote = "Año del Bicentenario (1822 - 2022)",
        preface = preface,
        sections = listOf(
            CostumbresSection(
                id = "intro",
                title = "Introducción",
                pageNumber = 1,
                fvbReference = "ver FVB 1-8",
                articles = listOf(
                    CostumbresArticle("A.01", "La Forma de Vida Basiliana y las Costumbres", "La Forma de Vida Basiliana es nuestro documento constitucional, dado la aprobación definitiva por la Santa Sede en 1983. Este documento, Costumbres Basilianas incluye normas y prácticas aptas para adaptarse a diferentes momentos históricos, diferentes culturas, diferentes apostolados y diferentes localidades.", "intro", "Introducción", "ver FVB 1-8"),
                    CostumbresArticle("A.02", "Fidelidad a la llamada de Dios", "Al seguir La Forma de Vida Basiliana y las Costumbres Basilianas expresamos nuestra fidelidad a nuestro Dios que nos llama en Jesucristo y por el Espíritu Santo.", "intro", "Introducción", "ver FVB 1-8"),
                    CostumbresArticle("A.03", "Archivo y documentos en comunidades locales", "Además de La Forma de Vida Basiliana y Costumbres Basilianas, cada comunidad local debe tener a disposición de sus miembros documentos importantes de la Congregación, tanto los actuales como los del pasado. En esta colección se puede incluir ediciones de las Constituciones, la Regla, el Vademécum, informes de diferentes Capítulos Generales, la serie de Anales Basilianos, Directorios Basilianos, Newsletters, y diferentes documentos considerados importantes por el Capítulo General o el Consejo General.", "intro", "Introducción", "ver FVB 1-8"),
                    CostumbresArticle("A.04", "Lectura anual en comunidad", "Se deben hacer las provisiones necesarias en las comunidades locales para la lectura anual de La Forma de Vida Basiliana y las Costumbres, en el contexto de oración y reflexión.", "intro", "Introducción", "ver FVB 1-8"),
                    CostumbresArticle("A.05", "Encarnación local", "Las comunidades locales deben discutir cómo vivir La Forma de Vida Basiliana y las Costumbres en las circunstancias locales. Si una comunidad local tiene una buena razón para desviarse de las prácticas en nuestros documentos, puede hacerlo sólo con la aprobación del Superior General.", "intro", "Introducción", "ver FVB 1-8"),
                    CostumbresArticle("A.06", "Modificaciones e interpretaciones", "Para cambios o interpretaciones de Costumbres Basilianas determinando membrecía en el Capítulo General, se requiere un voto de dos tercios del Capítulo. Para otros cambios o interpretaciones, una simple mayoría del Capítulo es suficiente. La interpretación de las Costumbres Basilianas entre los Capítulos es reservada al Consejo General.", "intro", "Introducción", "ver FVB 1-8")
                )
            ),
            CostumbresSection(
                id = "castidad",
                title = "La castidad",
                pageNumber = 1,
                fvbReference = "ver FVB 9-13",
                articles = listOf(
                    CostumbresArticle("B.01", "Amor y generosidad consagrada", "Nuestra convivencia y nuestras relaciones con los demás deben expresar la calidad y la profundidad del amor que se espera de hombres que, debido a su castidad consagrada, siguen el camino del Señor en una vida de entrega total y completa. Si hacemos esto, entregaremos a los demás, hombres y mujeres, un espíritu de generosidad, bondad y solidaridad que será un reflejo de ese amor.", "castidad", "La castidad", "ver FVB 9-13"),
                    CostumbresArticle("B.02", "Apoyo mutuo y vida en común", "Si queremos vivir plenamente el don de la castidad, tenemos que dar y recibir la fuerza y el apoyo de nuestra comunidad. La presencia de cada uno de nosotros con los demás en la oración, en las comidas, en la recreación, en las reuniones y en otros momentos de nuestra vida en común da sentido y ánimo al sacrificio que implica nuestra aceptación de la castidad. Tenemos que buscar formas de hacer más efectiva nuestra presencia en la comunidad. 'Su caridad sea sin fingimiento… ámense cordialmente los unos a los otros, estimando más a los otros que a uno mismo.' (Romanos 12:9-10)", "castidad", "La castidad", "ver FVB 9-13"),
                    CostumbresArticle("B.03", "Auténtica amistad cristiana", "La auténtica amistad cristiana es una reflexión del amor constante de Dios. Vamos a encontrar amigos, tanto dentro de la comunidad como afuera de ella, por nuestro temperamento y nuestros intereses. Estas amistades, sin embargo, no deben ser exclusivas; no deben separarnos de nuestra comunidad, de nuestro ministerio o de nuestro amor por Dios. Al contrario, deben fortalecer y ampliar nuestro amor por Dios y por el prójimo y apoyar nuestro compromiso por la castidad.", "castidad", "La castidad", "ver FVB 9-13"),
                    CostumbresArticle("B.04", "Auxilio divino y sacramentos", "El llamado a una vida de castidad es un don especial de Dios y anticipa en esta vida la vida en el Reino de los Cielos. Siempre conscientes de nuestra propia fragilidad, rezaremos frecuentemente para perseverar en esta virtud, invocando la ayuda de la Santísima Virgen María y de San José. Por nuestra celebración de la Eucaristía y del Sacramento de la Reconciliación junto con la fidelidad a la dirección espiritual buscaremos la gracia de la fidelidad a nuestro llamado.", "castidad", "La castidad", "ver FVB 9-13"),
                    CostumbresArticle("B.05", "Tesoro en vasijas de barro", "'Cargamos este tesoro en vasijas de barro.' (2 Cor. 4:7) La prudencia, el cuidado, el amor a la soledad y a la oración y una dedicación fiel a nuestras labores nos ayudarán a preservar nuestro don de la castidad.", "castidad", "La castidad", "ver FVB 9-13"),
                    CostumbresArticle("B.06", "Tolerancia cero y protección de personas vulnerables", "El abuso sexual es un vicio moral y daña a las víctimas. Es un escándalo grande en la sociedad civil, en nuestra Iglesia y en nuestra Comunidad. En nuestros esfuerzos para proteger a los niños y a los adultos vulnerables, los hermanos cumplirán con las leyes civiles y con las regulaciones diocesanas y Basilianas tocante la conducta sexual inapropiada.", "castidad", "La castidad", "ver FVB 9-13")
                )
            ),
            CostumbresSection(
                id = "pobreza",
                title = "La pobreza",
                subtitle = "Personal y Comunitaria",
                pageNumber = 2,
                fvbReference = "ver FVB 14-24",
                articles = listOf(
                    CostumbresArticle("C.01", "Imitación de Cristo y justicia", "Nuestra profesión de la pobreza evangélica exige nuestro compromiso con una vida en comunidad y una vida personal que imita la vida de Cristo Jesús, quien fue humilde en su espíritu y pobre en bienes materiales. También la profesión de la pobreza exige que nosotros participemos en la lucha de la Iglesia por la justicia en el mundo y que respondamos a las necesidades de los pobres y los desamparados.", "pobreza", "La pobreza", "ver FVB 14-24"),
                    CostumbresArticle("C.02", "Pobreza personal y desapego", "Hemos prometido ser pobres y tenemos que vivir siempre en todas partes como hombres pobres. En esta forma trabajaremos para librar nuestros corazones del deseo de posesiones materiales.", "pobreza", "La pobreza personal", "ver FVB 14-24"),
                    CostumbresArticle("C.03", "Compartir con los hermanos", "Como religiosos, compartimos lo que tenemos con nuestros hermanos. Dependemos en la Comunidad y no consideramos que nada esté a nuestra propia disposición sin la aprobación de nuestros superiores.", "pobreza", "La pobreza personal", "ver FVB 14-24"),
                    CostumbresArticle("C.04", "Métodos de sustento", "En la comunidad se pueden usar diversos métodos para satisfacer las necesidades de los miembros. Ellos pueden pedir cantidades específicas según sus necesidades, pueden recibir una suma convenida periódicamente o bien pueden presentar un presupuesto personal de gastos para un año y recibir fondos periódicos sobre dicho presupuesto.", "pobreza", "La pobreza personal", "ver FVB 14-24"),
                    CostumbresArticle("C.05", "Sobriedad y corrección fraterna", "Las necesidades personales de cada miembro serán un elemento en decidir cuánto va a recibir del fondo común. Pueden variar estas necesidades, pero ni estas necesidades ni regalos de dinero recibidos deben dejar que un miembro viva un estilo de vida que exceda al de sus hermanos. Un superior, con amor fraternal, debe llamarle la atención a un hermano extravagante para no sobrepasar los límites de la pobreza de un religioso.", "pobreza", "La pobreza personal", "ver FVB 14-24"),
                    CostumbresArticle("C.06", "Responsabilidad comunitaria", "Un miembro no es responsable solamente por su práctica personal de la pobreza, sino que comparte una responsabilidad para la práctica de la pobreza en su comunidad. En esto, los superiores tienen responsabilidad especial. Los miembros y las comunidades locales deben evaluar la vida comunitaria (comida, vestuario, casa, mobiliario, transporte, vacaciones, etc.) para no dar testimonio que va en contra de la pobreza evangélica.", "pobreza", "La pobreza personal", "ver FVB 14-24"),
                    CostumbresArticle("C.07", "Cuentas bancarias", "Miembros que tienen cuentas bancarias personales deben escoger a otro hermano para que este pueda firmar cheques en la cuenta.", "pobreza", "La pobreza personal", "ver FVB 14-24"),
                    CostumbresArticle("C.08", "Tarjetas de crédito y débito", "El uso de tarjetas de crédito y de débito es frecuentemente necesario, los miembros deben evitar ser extravagantes y hacer uso de tales tarjetas sólo con el permiso de su superior.", "pobreza", "La pobreza personal", "ver FVB 14-24"),
                    CostumbresArticle("C.09", "Deudas no autorizadas", "Si un miembro contrae una deuda grande sin la autorización de su superior, ni la comunidad local ni la comunidad general será responsable por dicha deuda y la comunidad local no puede pagar tal deuda sin la autorización del Superior General.", "pobreza", "La pobreza personal", "ver FVB 14-24"),
                    CostumbresArticle("C.10", "Ganancias de publicaciones", "A menos que el Superior General decida algo distinto, las ganancias de publicaciones de un miembro deben ir a la Tesorería General y no a la comunidad local.", "pobreza", "La pobreza personal", "ver FVB 14-24"),
                    CostumbresArticle("C.11", "Visitas entre casas", "Cuando un miembro visita a una casa Basiliana, debe ser acogido sin costo. Sin embargo, si la visita dura más de dos o tres días, el miembro debe llegar a un acuerdo con el superior local sobre la manera adecuada de pagar sus gastos.", "pobreza", "La pobreza personal", "ver FVB 14-24"),
                    CostumbresArticle("C.12", "Basilianos que viven aparte", "Un Basiliano que vive aparte de la Congregación tendrá una relación con el Consejo General que corresponderá, en cuanto al voto de pobreza, a la de un miembro de una casa local.", "pobreza", "La pobreza personal", "ver FVB 14-24"),
                    CostumbresArticle("C.13", "Responsabilidad evangélica comunitaria", "En el espíritu de responsabilidad evangélica, hay que entender que en el uso de nuestros recursos todo lo que tenemos es un don de Dios y que debemos usarlo para el servicio de Cristo y de su Iglesia. Cada miembro será informado debidamente sobre la condición financiera de su comunidad local.", "pobreza", "La pobreza en comunidades", "ver FVB 14-24"),
                    CostumbresArticle("C.14", "Evitar acumulación innecesaria", "Las comunidades locales deben evitar la acumulación innecesaria de recursos. La preocupación por los pobres debe ser un signo de nuestra propia pobreza y una comunidad local que tiene más de lo necesario debe recordar este signo al elaborar su presupuesto. Lo sobrante debe remitirse a la Tesorería General.", "pobreza", "La pobreza en comunidades", "ver FVB 14-24"),
                    CostumbresArticle("C.17", "Inmuebles y austeridad", "Las necesidades y los gastos del apostolado requerirán que algunos Basilianos manejen cantidades de dinero que sobrepasan los recursos de la mayoría. Algunos vivimos y trabajamos en edificios amplios. Debemos cuidarnos de no adoptar actitudes indebidas para los seguidores de Cristo, el pobre por excelencia.", "pobreza", "La pobreza en comunidades", "ver FVB 14-24"),
                    CostumbresArticle("C.19", "Donaciones y legados", "Las donaciones o legados de valor que se hagan a un hermano como miembro de los Padres Basilianos deberán remitirse a la Tesorería General y no a la comunidad local, a menos que el donante o testador indique lo contrario.", "pobreza", "La pobreza en comunidades", "ver FVB 14-24")
                )
            ),
            CostumbresSection(
                id = "obediencia",
                title = "La obediencia",
                pageNumber = 5,
                fvbReference = "ver FVB 25-30",
                articles = listOf(
                    CostumbresArticle("D.01", "Doble dimensión de la obediencia", "Jesús obedecía al Padre en todo, y él nos invita a cada uno a la obediencia. Nuestra expresión de esta obediencia será en forma doble: en nuestra fidelidad a Jesucristo y a nuestra Forma de Vida en la Iglesia será constante y habitual; en nuestra respuesta a decisiones específicas de la Congregación o lo que nos manda un superior será particular y limitada.", "obediencia", "La obediencia", "ver FVB 25-30"),
                    CostumbresArticle("D.02", "Espíritu rápido y vivaz", "Por nuestra obediencia continua, crecemos en la unión con Cristo y fortalecemos los vínculos que nos unen con nuestra Congregación y con la Iglesia. Así pues, que obedezcamos siempre y en todo lugar, solos o con nuestros hermanos, tanto en los asuntos pequeños como en los grandes. Obedezcamos sin vacilación y sin queja, con un espíritu rápido y vivaz. (cf. Regla de 1962; Constituciones de 1863)", "obediencia", "La obediencia", "ver FVB 25-30"),
                    CostumbresArticle("D.03", "Autoridad con gentileza y amor", "La obediencia religiosa brota del amor. Es nuestra respuesta al amor que nos demostró Dios Padre por su Hijo. Los superiores, siguiendo este patrón, mostrarán un amor generoso para que los miembros obedezcan en el mismo espíritu. Los superiores deben ejercer su autoridad con gentileza y bondad aún en momentos cuando tienen que corregir a un hermano.", "obediencia", "La obediencia", "ver FVB 25-30"),
                    CostumbresArticle("D.05", "Derecho a expresar objeciones", "Aún después de que el superior ha tomado una decisión que requiera la obediencia, un miembro tiene el derecho y a veces hasta la obligación de expresar objeciones a ella si él piensa que su implementación será dañina o demasiado difícil. El superior debe tratar de llegar a un entendimiento mutuo, y el miembro tiene derecho de recurrir al Superior General.", "obediencia", "La obediencia", "ver FVB 25-30"),
                    CostumbresArticle("D.06", "Proceso de Conciliación", "Cuando un miembro percibe que el superior local o el Superior General le han hecho una injusticia, y cuando todas las vías de conciliación han resultado sin éxito, el miembro debe seguir el Proceso de Conciliación establecido por el Capítulo General.", "obediencia", "La obediencia", "ver FVB 25-30"),
                    CostumbresArticle("D.07", "Obediencia en instituciones apostólicas", "Frecuentemente, nuestro trabajo no se desarrolla bajo la dirección inmediata del superior local, sino en instituciones bajo la dirección de otros hermanos o de personal no Basiliano. El espíritu de obediencia nos invita a darles a estas autoridades la misma cooperación generosa.", "obediencia", "La obediencia", "ver FVB 25-30")
                )
            ),
            CostumbresSection(
                id = "vida_comunidad",
                title = "La vida en comunidad",
                pageNumber = 6,
                fvbReference = "ver FVB 31-39",
                articles = listOf(
                    CostumbresArticle("E.01", "La Caridad como fundamento", "Como todas las formas de la vida cristiana comunitaria, nuestra vida se funda en la caridad, la más grande de las virtudes.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.03", "Número mínimo", "Como expresión concreta de la vida en común, la comunidad local tendrá normalmente un mínimo de tres miembros.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.05", "Cuidado mutuo y amonestación", "La vida en común exige el cuidado mutuo. Como hermanos en el Señor, nos amamos, nos perdonamos, nos respaldamos, nos apoyamos e inclusive nos corregimos unos a otros. Recordemos que nadie, a excepción del superior, debe hacer amonestaciones en público.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.07", "Trato fraterno sin títulos", "Nuestras conversaciones deben estar regidas por un tratamiento familiar, ausente de distinciones académicas o de otra índole. Cuando haya necesidad de un tratamiento más formal, a los sacerdotes se les debe llamar 'padre' y a los hermanos no ordenados 'señor'.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.09", "Silencio para la oración y labor", "Los momentos de silencio son esenciales en nuestra vida comunitaria. Necesitamos el silencio para nuestra labor y para nuestro descanso. Sobre todo, necesitamos el silencio para nuestra oración, de modo que cada uno escuche la voz de Dios 'hablando dentro de nuestro corazón'.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.14", "Programa diario y oración común", "El programa diario en nuestras casas debe incluir las comidas en común, las Oraciones de la Mañana y las Oraciones de la Tarde y tiempo para la celebración de la Eucaristía. Se debe programar un día para una eucaristía concelebrada. Hay que programar con regularidad reuniones comunitarias.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.15", "Mensa Communis", "Dado que estamos ordenados bajo el título de mensa comunis, debemos considerar la comida principal del día como un momento importante en nuestra vida en comunidad.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.20", "Uso moderado de bebidas y respeto", "El uso moderado de las bebidas alcohólicas puede ser una ayuda para el descanso y la celebración en común. Algunos miembros escogen no consumir bebidas alcohólicas. La forma de tratar esta situación será una señal de hospitalidad y solicitud mutua.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.21", "Enmienda 2022: Dependencias y apoyo terapéutico", "Cuando haya indicaciones de que un miembro tiene una dependencia (por ejemplo, con la pornografía, abuso de sustancias, juegos de azar, u otras formas), el hermano deberá responsabilizarse con asesoramiento psicológico y/o espiritual. Los hermanos deben expresarle claramente su preocupación con caridad; si el tratamiento terapéutico parece aconsejable, el asunto será remitido al Superior General.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.25", "Hospitalidad Basiliana", "La vida Basiliana incluye la práctica de la hospitalidad. En varios momentos, debemos invitar a nuestra mesa a nuestros familiares, a otros sacerdotes, a religiosos y religiosas, a los Asociados Laicos Basilianos, a los que colaboran con nosotros en el ministerio y a otros.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.26", "Asociados Laicos Basilianos", "Los Asociados Laicos Basilianos desean responder más completamente a su vocación bautismal con una asociación formal con la Congregación (Documento Básico de los Asociados Laicos – 2007). Los hermanos Basilianos y los Asociados Laicos deben apoyarse unos a otros por la oración y la colaboración.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.30", "Fomento de vocaciones en comunidad", "La comunidad local tiene una especial responsabilidad de fomentar vocaciones a nuestra vida en comunidad. Debemos invitar a los jóvenes que demuestran interés en la vida Basiliana a visitarnos y a compartir nuestra vida de oración.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.43", "Cuidado fraterno de los enfermos", "'Nuestro amor fraternal se expresa más claramente en nuestro cuidado por los enfermos y el apoyo que brindamos a los que están sufriendo dificultades.' (Cita de la regla de Léorat-Picansel de 1802 y carta del padre Actorie en 1863)", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.48", "Muerte de un hermano", "La muerte de un hermano es un momento de tristeza para toda la comunidad Basiliana, pero, al mismo tiempo, es un momento de esperanza y de paz. Se celebrarán un velorio y una misa funeraria con presencia de hermanos, familia y amigos.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39"),
                    CostumbresArticle("E.50", "Ataúd sencillo y lápida modesta", "El ataúd será sencillo; sobre la tumba se colocará una lápida modesta. El entierro se llevará a cabo en el recinto Basiliano más cercano.", "vida_comunidad", "La vida en comunidad", "ver FVB 31-39")
                )
            ),
            CostumbresSection(
                id = "sacerdocio",
                title = "El sacerdocio, labor apostólica y educación",
                pageNumber = 12,
                fvbReference = "ver FVB 40-44, 59-67, 94-97",
                articles = listOf(
                    CostumbresArticle("F.01", "Acción salvífica de Cristo", "Como sacerdotes y religiosos, participamos en la acción salvífica de Cristo. Buscamos anunciar el evangelio en las situaciones en que vivimos y en la labor pastoral que compartimos.", "sacerdocio", "El sacerdocio y la misión", "ver FVB 40-44"),
                    CostumbresArticle("F.02", "Testimonio de bondad, disciplina y sabiduría", "Nuestra vida comunal debe ser un modelo de la verdadera comunidad cristiana y una inspiración a los que servimos. Buscamos incorporarlos en la bondad, la disciplina y la sabiduría del Señor por nuestro amor por Cristo y su Evangelio.", "sacerdocio", "El sacerdocio y la misión", "ver FVB 40-44"),
                    CostumbresArticle("F.05", "Servicio en parroquias", "Desde el principio, los Basilianos han servido en parroquias entregadas a nuestro cuidado por una diócesis. Nuestra dedicación a la educación cristiana da un carácter especial a las parroquias donde servimos.", "sacerdocio", "El sacerdocio y la misión", "ver FVB 59-67"),
                    CostumbresArticle("F.06", "Educación cristiana de la juventud", "La preocupación apostólica por la educación cristiana de la juventud ha sido una gran tradición Basiliana; ha tomado diferentes formas y ha sido marcada por una continua adaptabilidad a las distintas necesidades del pueblo de Dios.", "sacerdocio", "El sacerdocio y la misión", "ver FVB 59-67"),
                    CostumbresArticle("F.08", "Preferencia por los desfavorecidos", "Una preocupación por los estudiantes que no son muy dotados académicamente y los que vienen con pocos recursos financieros es algo esencial en el apostolado educativo Basiliano. Por la ayuda financiera y apoyo académico hacemos el esfuerzo de incluir estos estudiantes en nuestras escuelas y universidades.", "sacerdocio", "El sacerdocio y la misión", "ver FVB 59-67"),
                    CostumbresArticle("F.10", "Estudios avanzados para hermanos", "Para apoyar la misión de educación cristiana en todas sus formas, alentaremos a hermanos que tienen el talento e interés necesarios que emprendan estudios avanzados en varias disciplinas: estudios pastorales, sagrada escritura, filosofía, lenguas, literaturas y ciencias.", "sacerdocio", "El sacerdocio y la misión", "ver FVB 59-67"),
                    CostumbresArticle("F.26", "Justicia social y amor preferencial por los pobres", "'Invito a todos los miembros de los Institutos que se dedican a la educación a que sean fieles a su carisma originario y a sus tradiciones, conscientes de que el amor preferencial por los pobres tiene una singular aplicación en la elección de los medios adecuados para liberar a los hombres de esa grave miseria que es la falta de formación cultural y religiosa.' (San Juan Pablo II, Vita Consecrata #97)", "sacerdocio", "El sacerdocio y la misión", "ver FVB 59-67"),
                    CostumbresArticle("F.32", "Mature Living Allowance (MLA)", "El Subsidio para los de tercera edad (Mature Living Allowance o MLA) es la cantidad fijada por el Consejo General y transferida a las comunidades locales para el digno mantenimiento de los hermanos jubilados.", "sacerdocio", "El sacerdocio y la misión", "ver FVB 94-97")
                )
            ),
            CostumbresSection(
                id = "oracion",
                title = "La oración",
                pageNumber = 16,
                fvbReference = "ver FVB 45-58",
                articles = listOf(
                    CostumbresArticle("G.01", "Respuesta amorosa a Dios", "Dios nos ama y nos muestra su amor a través de su Hijo. La oración es nuestra contestación amorosa a esa iniciativa divina. Es la oración que nos une al Padre, por Cristo en el Espíritu Santo. Así pues, los Basilianos tenemos que ser hombres dedicados a la oración.", "oracion", "La oración", "ver FVB 45-58"),
                    CostumbresArticle("G.02", "Eucaristía comunitaria regular", "Se debe celebrar una Eucaristía comunitaria regularmente en nuestras casas. Generalmente, esta celebración será una misa concelebrada con toda la comunidad local, tanto ordenados como no-ordenados. Hay un solo sacrificio y una sola acción de gracias alrededor de un solo altar.", "oracion", "La oración", "ver FVB 45-58"),
                    CostumbresArticle("G.04", "Liturgia de las Horas", "Como sacerdotes y diáconos hemos recibido de la Iglesia la obligación de celebrar la Liturgia de las Horas diariamente.", "oracion", "La oración", "ver FVB 45-58"),
                    CostumbresArticle("G.05", "Laudes y Vísperas vivas", "Las Laudes y las Vísperas siguen el patrón general de la Liturgia de las Horas. En esta oración diaria no debemos quedarnos contentos con una recitación apresurada, rutinaria o monótona. Mediante el estudio, la meditación, la reflexión compartida y el canto debemos hacer la oración verdadera y vital.", "oracion", "La oración", "ver FVB 45-58"),
                    CostumbresArticle("G.10", "Retiro anual de la Congregación", "El retiro anual es de gran importancia para todos los Basilianos. El retiro del Superior General se llevará a cabo anualmente y todos los miembros están invitados. Cada hermano debe participar en uno de los retiros comunitarios.", "oracion", "La oración", "ver FVB 45-58"),
                    CostumbresArticle("G.11", "Fiestas de San Basilio", "Además de las fiestas mencionadas en La Forma de Vida Basiliana, la fiesta de San Basilio y San Gregorio Nacianceno el día 2 de enero, y la fiesta tradicional de San Basilio el día 14 de junio, deben ser días de oración especial para la Congregación.", "oracion", "La oración", "ver FVB 45-58"),
                    CostumbresArticle("G.14", "Devociones tradicionales", "La devoción al Santísimo Sacramento, al Sagrado Corazón, a la Santísima Virgen María y a nuestros santos patronos, tanto como el Rosario y el Vía Crucis, han tenido una influencia profunda en muchos Basilianos.", "oracion", "La oración", "ver FVB 45-58"),
                    CostumbresArticle("G.16", "La oración como prueba de vitalidad", "El testimonio de la historia es claro: la fidelidad a la oración o su abandono es la prueba de la vitalidad o de la decadencia de la vida religiosa. Sin la oración no podremos entender el valor ni de la vida religiosa ni de la vida cristiana; tampoco tendremos las fuerzas para seguir adelante.", "oracion", "La oración", "ver FVB 45-58")
                )
            ),
            CostumbresSection(
                id = "formacion",
                title = "La formación inicial",
                subtitle = "Asociados, Novicios y Escolásticos",
                pageNumber = 19,
                fvbReference = "ver FVB 68-92",
                articles = listOf(
                    CostumbresArticle("H.01", "Equipo de Formación", "Un Equipo de Formación tendrá la responsabilidad para la incorporación progresiva de nuevos miembros a la Congregación y para los preparativos de éstos para los votos y la ordenación.", "formacion", "La formación inicial", "ver FVB 68-75"),
                    CostumbresArticle("H.04", "Ingreso al Programa de Asociación", "La solicitud de ingreso al Programa de asociación se hará por medio de una carta personal dirigida al Superior General con certificados y recomendaciones.", "formacion", "Asociados", "ver FVB 76-77"),
                    CostumbresArticle("H.13", "Solicitud para el Noviciado", "Quien haya sido asociado hará una solicitud de ingreso al noviciado por medio de una carta personal al Superior General tras consulta con los miembros de la comunidad local.", "formacion", "Novicios", "ver FVB 78-85"),
                    CostumbresArticle("H.18", "Maestro de Novicios", "El Maestro de Novicios debe ser un sacerdote con votos perpetuos, de por lo menos treinta años y capaz de enseñarles a los novicios con su palabra y con su ejemplo.", "formacion", "Novicios", "ver FVB 78-85"),
                    CostumbresArticle("H.21", "Noviciado canónico de doce meses", "Durante los doce meses del noviciado canónico, los Novicios participarán en trabajo apostólico y voluntario de acuerdo con las normas aprobadas en el programa de formación.", "formacion", "Novicios", "ver FVB 78-85"),
                    CostumbresArticle("H.26", "Escolásticos y preparación teológica", "El Director de Escolásticos tiene la responsabilidad de darles una firme preparación espiritual e intelectual para el sacerdocio y la vida religiosa en la Congregación.", "formacion", "Escolásticos", "ver FVB 86-92"),
                    CostumbresArticle("H.27", "Votos temporales y profesión perpetua", "La profesión perpetua requiere un período previo de profesión temporal de no menos de tres años y no más de seis años.", "formacion", "Escolásticos", "ver FVB 86-92")
                )
            ),
            CostumbresSection(
                id = "gobierno",
                title = "El gobierno de la Congregación",
                subtitle = "Capítulo General y Elecciones",
                pageNumber = 25,
                fvbReference = "ver FVB 98-113",
                articles = listOf(
                    CostumbresArticle("J.01", "Responsabilidad colegiada", "La responsabilidad del gobierno de la Congregación recae sobre todos los Basilianos a través del Capítulo General, el Superior General y el Consejo General; a nivel local contamos con el superior o rector de cada casa y su consejo.", "gobierno", "El gobierno", "ver FVB 98-103"),
                    CostumbresArticle("J.02", "Función religiosa del Capítulo General", "El Capítulo General tiene una función religiosa: hacer más profunda nuestra comprensión del significado de la misión de Cristo en la tierra y hacer más completo nuestro compromiso con ella.", "gobierno", "El Capítulo General", "ver FVB 104-113"),
                    CostumbresArticle("J.04", "Composición representativa", "Como principio, el Capítulo General debe consistir en aproximadamente el veinte por ciento de los miembros profesos de la Congregación.", "gobierno", "El Capítulo General", "ver FVB 104-113"),
                    CostumbresArticle("J.06", "Representación regional de Colombia y México", "Los superiores de México y Colombia son capitulantes ex officio. De entre los escolásticos se elegirá por lo menos un delegado de Canadá/EE.UU., un delegado de México y un delegado de Colombia.", "gobierno", "El Capítulo General", "ver FVB 104-113"),
                    CostumbresArticle("J.25", "Oración previa a las elecciones", "Todos los capitulantes rezarán: 'Espíritu Santo, protector Nuestro, a quien Jesús envió a la Iglesia para dirigirla hacia la Verdad Entera, ven a nosotros. Ilumina nuestras mentes con la sabiduría para que nosotros podamos elegir a aquél que ha de servir de la mejor manera a nuestra comunidad. Llena nuestros corazones de amor y de paz para que podamos aceptar unidos los resultados de esta elección. Ven, Espíritu Santo, ven ahora a nosotros como Jesús ha prometido. Amén.'", "gobierno", "El Capítulo General", "ver FVB 104-113"),
                    CostumbresArticle("J.29", "Profesión solemne del Superior General electo", "El Superior General recién electo pronuncia: 'En presencia de Dios y de mis hermanos en el Capítulo General de la Congregación de San Basilio, yo, N.N., prometo que, como Superior General, serviré de todo corazón a esta Congregación. Adheriré a La Forma de Vida Basiliana y a las Costumbres Basilianas y me esforzaré por fortalecer a mis hermanos... En mi oración diaria, encomendaré nuestra hermandad al Padre para que ella se fortalezca en Cristo y sea fructífera para la Iglesia.' Se cantará el Te Deum de acción de gracias.", "gobierno", "El Capítulo General", "ver FVB 104-113")
                )
            ),
            CostumbresSection(
                id = "superior_consejo",
                title = "El Superior General y Consejo General",
                pageNumber = 32,
                fvbReference = "ver FVB 114-137",
                articles = listOf(
                    CostumbresArticle("K.01", "Guardián constitucional y pastor", "El Superior General tiene la responsabilidad de fomentar la vida espiritual de los hermanos. En virtud de su cargo, él es el guardián de las prácticas constitucionales de la Congregación.", "superior_consejo", "El Superior General", "ver FVB 114-127"),
                    CostumbresArticle("K.06", "Visitas a todas las comunidades", "El Superior General se esforzará por ser accesible a sus hermanos. Tratará de visitar informalmente todas las comunidades por lo menos dos veces durante el período de su ministerio.", "superior_consejo", "El Superior General", "ver FVB 114-127"),
                    CostumbresArticle("K.07", "Sin privilegios personales", "El Superior General no tendrá privilegios en la vida comunitaria en cuanto al vestuario, al alimento, al transporte, etc.", "superior_consejo", "El Superior General", "ver FVB 114-127"),
                    CostumbresArticle("L.01", "Composición del Consejo General", "El Superior General y los Consejeros Generales forman el Consejo General, elegidos por el Capítulo General por cuatro años.", "superior_consejo", "El Consejo General", "ver FVB 128-137"),
                    CostumbresArticle("L.08", "Voto Deliberativo del Consejo General", "El Consejo General tiene voto deliberativo en: convocatoria a Capítulo extraordinario, admisión a votos perpetuos y órdenes, establecimiento o supresión de casas, aprobación de presupuestos, aceptación de parroquias y nombramiento de superiores y directores de formación.", "superior_consejo", "El Consejo General", "ver FVB 128-137"),
                    CostumbresArticle("L.13", "Visitaciones canónicas", "La visitación canónica es una forma importante por la cual el Consejo General ayuda a una comunidad local a hacer más firme su compromiso a la vida religiosa y al ministerio.", "superior_consejo", "Las visitaciones canónicas", "ver FVB 128-137")
                )
            ),
            CostumbresSection(
                id = "gobierno_local_colombia",
                title = "El gobierno local y Superiores de México y Colombia",
                pageNumber = 38,
                fvbReference = "ver FVB 144-152",
                articles = listOf(
                    CostumbresArticle("N.01", "Servicio a imagen de Cristo", "Al superior local se le confía el bienestar de la comunidad local. Debe tomar como modelo a nuestro Señor Jesucristo que vino a servir y no a ser servido.", "gobierno_local_colombia", "El gobierno local", "ver FVB 144-152"),
                    CostumbresArticle("N.05", "Reuniones periódicas del consejo local", "El consejo local debe reunirse frecuentemente, por lo menos cinco veces al año. Las decisiones deben mantener informada a la comunidad local.", "gobierno_local_colombia", "El gobierno local", "ver FVB 144-152"),
                    CostumbresArticle("N.10", "Tesorero local y administración doméstica", "Bajo la dirección del consejo local, el tesorero será responsable del cuidado y mantenimiento de la casa, de la provisión de alimentos y del arreglo de las habitaciones.", "gobierno_local_colombia", "El gobierno local", "ver FVB 144-152"),
                    CostumbresArticle("N.19", "Reconocimiento de Superiores Regionales en Colombia y México", "Se reconoce a los superiores en México y en Colombia como Superiores Regionales; en cuanto a los asuntos civiles y eclesiásticos, son ellos los responsables.", "gobierno_local_colombia", "Superiores de México y Colombia", "ver FVB 144-152"),
                    CostumbresArticle("N.20", "Término de cuatro años", "El término de los Superiores Regionales de Colombia y de México es de cuatro años; se nombra a estos Superiores después de cada Capítulo General. El término de los Consejeros Regionales corresponde al término del Superior Regional.", "gobierno_local_colombia", "Superiores de México y Colombia", "ver FVB 144-152"),
                    CostumbresArticle("N.21", "Reuniones y actas de los Consejos Regionales", "Antes de que el Superior General y su Consejo consideren decisiones importantes y nombramientos, los consejos regionales se reunirán, discutirán las realidades ministeriales y de formación, y harán recomendaciones debidamente justificadas.", "gobierno_local_colombia", "Superiores de México y Colombia", "ver FVB 144-152"),
                    CostumbresArticle("N.22", "Ciclo de nombramientos en Colombia y México", "Los nombramientos en Colombia comienzan normalmente el 1 de enero, mientras que los nombramientos en México comienzan normalmente el 1 de julio. La comunicación consistente entre el Superior General y los Superiores Regionales de México y Colombia es esencial para el buen gobierno.", "gobierno_local_colombia", "Superiores de México y Colombia", "ver FVB 144-152")
                )
            )
        )
    )

    const val closingMottoFrench = "Christ notre maître"
    const val closingMottoSpanish = "Cristo nuestro maestro"
    const val closingMottoEnglish = "Christ our teacher"
}
