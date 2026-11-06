package com.example.data.repository

import com.example.data.model.*

object FormaDeVidaData {

    val document: FormaDeVidaDocument by lazy {
        FormaDeVidaDocument(
            title = "La Forma de Vida Basiliana",
            subtitle = "El Documento Básico Constitucional de la Congregación de Sacerdotes de San Basilio (Padres Basilianos)",
            institution = "Congregatio Presbyterorum a Sancto Basilio (C.S.B.)",
            cityAndYears = "Toronto, Canadá • 1983, 2000, 2002, 2013",
            prefaces = listOf(
                FvbPreface(
                    year = "2013",
                    title = "Prefacio para la edición de 2013",
                    author = "Reverendo Padre George T. Smith, C.S.B. • Superior General",
                    dateOrContext = "Octubre 1, 2013 • Memorial de Santa Teresa del Niño Jesús",
                    content = listOf(
                        "Primero y antes que nada, conviene decir que esta edición de 2013 no contiene ningún cambio de la edición anterior; es simplemente una reimpresión. En la edición de 2002, había unos cambios hechos en el protocolo para la elección para los delegados al Capítulo General, La Forma de Vida Basiliana no ha sufrido cambios mayores desde que el documento fue presentado a la Congregación en 1969 y terminado en 1972. Un documento como éste que define e inspira a una comunidad de discípulos del Señor debe de reflejar la constancia de Jesucristo en la historia de nuestra salvación. Constantes cambios indicaran un espíritu inquieto.",
                        "Al mismo tiempo, esto no indica que nuestra Congregación rechaza el cambio o que no estamos abiertos a crecer en el Espíritu. No somos la misma Congregación que existía en 1972 pero nuestra dedicación a las exhortaciones dinámicas de La Forma de Vida Basiliana no se han cambiado. Esta maravillosa paradoja – nuestra dedicación a la constancia y el cambio – es parte de nuestra identidad Basiliana. Es lo que nos deja crecer en amor uno por el otro y al mismo tiempo quedarnos fieles a todo lo que nuestros hermanos que nos precedieron al reino han podido lograr.",
                        "Existe nuestro instituto de la vida consagrada para que los miembros crezcamos en la santidad a través de nuestros votos y nuestro ministerio en la Iglesia. La Forma de Vida Basiliana nos facilita esta santificación y este servicio. Por eso es un documento sagrado. Leyéndolo detenidamente, como individuos y en común, es una fuente de gracia, pero sólo si lo hacemos en un espíritu de oración. También, es un lazo importante que nos liga uno al otro. No hay una verdadera vida en común sin este documento. En un mundo que se hace más virtual y más digital, que la versión de La Forma de Vida Basiliana siga siendo un signo tangible del amor fraterno que nos une en Jesucristo, nuestro Señor.",
                        "San Basilio, ruega por nosotros."
                    )
                ),
                FvbPreface(
                    year = "1983",
                    title = "Prefacio para la versión de 1983",
                    author = "(M.R.P.) U.E. Paré, C.S.B. • Superior General",
                    dateOrContext = "Toronto • 1983 tras la Aprobación Pontificia",
                    content = listOf(
                        "La Congregación de San Basilio fue fundada en Annonay, Francia en 1822. Recibió el Decreto de Alabanza de la Santa Sede el 15 de septiembre de 1837 y el Decreto de Aprobación el 23 de noviembre de 1863. Las Constituciones fueron aprobadas por un período de siete años el 11 de septiembre de 1913. Recibió la aprobación final de la Santa Sede el 14 de octubre de 1938 para la Congregación de Sacerdotes de San Basilio de Toronto. El 14 de junio de 1955 la Congregación de Sacerdotes de San Basilio de la Diócesis de Viviers se reunió con la de Toronto, con lo cual terminó la división que había tenido lugar en 1922 y de éste modo las constituciones fueron adoptadas por la totalidad de la Congregación Basiliana.",
                        "Después del Concilio Vaticano II, los Padres Basilianos emprendieron la ardua tarea de rehacer los documentos de acuerdo con las normas de dicho Concilio: «... que se re-editen de forma adecuada las constituciones, directorios, libros de costumbres, libros de oraciones y ceremonias y semejantes y se adapten, suprimiendo las leyes obsoletas, a los decretos de este sagrado sínodo» (Perfectae Caritatis #3).",
                        "El Capítulo Basiliano de 1967 se apegó a esta petición según Ecclesiae sanctae. El Capítulo comentó específicamente: «Quisiéramos tener un documento que tocara el alma, capaz de inspirar a quienes lo leyesen, y que reflejara la sencillez y la carencia de pretensiones que, según creemos, es parte de nuestra tradición Basiliana».",
                        "Una versión preliminar apareció en 1969 y fue completada en 1972. Durante los Capítulos de 1973 y 1977 dedicamos bastante tiempo a discutir y revisar el documento. Debe hacerse mención muy especial del Padre F. A. Black, quién con inteligencia, paciencia y amabilidad guió este texto básico conservando la energía espiritual y sencillez evangélica, así como al Padre T. J. Hanrahan, Superior General.",
                        "Este es, pues, el texto definitivo de la Forma de Vida Basiliana que se sometió a Roma y recibió la aprobación de la Santa Sede el 12 de mayo de 1983. Roguemos a Dios que nunca llegue a convertirse en letra muerta, sino en fuente viva de renovación, junto con las Costumbres Basilianas, en vínculo vivo de unidad que comparta toda la familia Basiliana."
                    )
                ),
                FvbPreface(
                    year = "2000",
                    title = "Prefacio para la edición de 2000",
                    author = "R.P. Ronald P. Fabbro, C.S.B. • Superior General",
                    dateOrContext = "Noviembre 21, 2000 • Presentación de la Virgen María",
                    content = listOf(
                        "Es un honor para mí presentar la edición 2000 de La Forma de Vida Basiliana. Un punto importante en esta renovación fue la reunión de toda la Comunidad en la asamblea del sínodo en Julio de 1996, en el Colegio de St. John Fisher en Rochester, Nueva York. El Capítulo General de 1997 dio la aprobación a las correcciones aprobadas por Roma (Prot. n. 4225/97 del 26 de Febrero de 1999).",
                        "Agradezco a los Padres William H. Irwin CSB, Peter J. M. Swan CSB y Daniel J. Zorzi CSB por la preparación, actualización de referencias al Código de Derecho Canónico de 1983 y a Vita Consecrata de Juan Pablo II. Que sea una fuente fresca de inspiración que nos una más profundamente en nuestra misión común de educación y evangelización."
                    )
                ),
                FvbPreface(
                    year = "2002",
                    title = "Prefacio para la edición de 2002",
                    author = "R.P. Ronald P. Fabbro, C.S.B. • Superior General",
                    dateOrContext = "19 de marzo de 2002 • Solemnidad de San José",
                    content = listOf(
                        "El Capítulo General de 2002 aprobó un cambio en el artículo 104 otorgando mayor autoridad para adoptar el método de selección de delegados (aprobado por Roma Prot. n. T 67-1/2001). Oremos para que a través de nuestra fidelidad a La Forma de Vida Basiliana alcancemos la madurez y plenitud de Cristo (Efesios 4:13)."
                    )
                )
            ),
            decree = listOf(
                "Congregatio Pro Religiosis et Institutis Saecularibus • Prot. n. T 67 — 1/79",
                "La Congregación de San Basilio, cuya sede está en Toronto, busca glorificar a Dios en toda forma de actividad sacerdotal compatible con la vida en común, especialmente en las obras de educación y evangelización.",
                "De acuerdo con las directivas del Concilio Vaticano II, el Instituto ha preparado un nuevo texto de Constituciones, llamado El Documento Básico. Tras el voto favorable del Congresso del 4 de enero de 1983, aprueba y confirma mediante este decreto las Constituciones.",
                "Que la fidelidad al carisma Basiliano conduzca a los miembros a formar una comunidad cristiana vital que sea signo viviente de sencillez y fraternidad, y a establecer comunidades cristianas en las que todos puedan experimentar la alegría y la paz que provienen de su hermandad en Cristo.",
                "Roma, 12 de mayo, Solemnidad de la Ascensión de Nuestro Señor, en el año de 1983. (+ E. Card. Pironio, Pref. / + Augustine Mayer, Sec.)"
            ),
            abbreviations = listOf(
                "AA" to "Apostolicam actuositatem (Apostolado de los Laicos)",
                "DV" to "Dei Verbum (Revelación Divina)",
                "GS" to "Gaudium et spes (Iglesia en el Mundo Moderno)",
                "LG" to "Lumen gentium (La Iglesia)",
                "OT" to "Optatam totius (Formación Sacerdotal)",
                "PC" to "Perfectae caritatis (Renovación de la Vida Religiosa)",
                "PO" to "Presbyterorum ordinis (Ministerio de los Sacerdotes)",
                "SC" to "Sacrosanctum concilium (Sagrada Liturgia)",
                "C. / Cc." to "Cánones del Código de Derecho Canónico de 1983",
                "Const." to "Constituciones de la Congregación de San Basilio (1956)",
                "Regla" to "La Regla de los Padres Basilianos (1962)"
            ),
            articles = generateAll153Articles(),
            index = listOf(
                FvbIndexItem("Asociados", "76, 77"),
                FvbIndexItem("Autoridad del Papa", "99"),
                FvbIndexItem("Capítulo General (Composición y Elecciones)", "104, 106, 110, 113"),
                FvbIndexItem("Castidad Evangélica", "9, 10, 11, 12, 13"),
                FvbIndexItem("Consejeros Generales y Locales", "110, 125, 128, 130, 131, 146"),
                FvbIndexItem("Deberes y Fondo Común", "20, 21"),
                FvbIndexItem("Educación y Escuelas", "3, 5, 64, 65"),
                FvbIndexItem("Educación Continuada", "94, 95, 96, 97"),
                FvbIndexItem("El Espíritu de los Fundadores de Annonay", "5, 65"),
                FvbIndexItem("El Llamado de Cristo y Profesión", "1, 2, 6, 7, 8, 28, 53, 56, 61, 153"),
                FvbIndexItem("Escolásticos y Formación", "86, 87, 88, 89, 90, 91, 92"),
                FvbIndexItem("Fórmula de los Votos Religiosos", "85"),
                FvbIndexItem("Hospitalidad Cristiana y Fraterna", "39"),
                FvbIndexItem("Noviciado y Formación de Novicios", "78, 79, 80, 81, 82, 83, 84, 85"),
                FvbIndexItem("Obediencia Amorosa", "25, 26, 27, 28, 29, 30"),
                FvbIndexItem("Oración y Hora Diaria de Oración Personal", "45, 46, 47, 48, 50, 51, 52, 53, 54, 55, 58"),
                FvbIndexItem("Patronos de la Comunidad (San Basilio y Santos)", "4, 51"),
                FvbIndexItem("Pobreza Evangélica y Administración de Bienes", "14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24"),
                FvbIndexItem("Sacerdocio y Comunión con los Obispos", "40, 41, 42, 43, 44"),
                FvbIndexItem("Salidas y Bajas de la Comunidad", "93"),
                FvbIndexItem("Superior General (Elección y Facultades)", "109, 114, 115, 116, 117, 118, 119, 121, 122, 124, 126, 127"),
                FvbIndexItem("Superiores Locales y Rectores", "144, 145, 147, 148, 149, 150, 151, 152"),
                FvbIndexItem("Trabajo Apostólico y Educación", "59, 60, 61, 62, 63, 64, 65, 66, 67"),
                FvbIndexItem("Vida Comunitaria (Comida, Diálogo, Enfermos)", "31, 32, 33, 34, 35, 36, 37, 38, 39, 74, 143"),
                FvbIndexItem("Vocaciones a la Congregación", "68, 69, 70, 71, 72, 73, 74, 75")
            )
        )
    }

    private fun generateAll153Articles(): List<FvbArticle> {
        return listOf(
            FvbArticle(
                1,
                "El Camino del Señor",
                "Los primeros cristianos se llamaban a sí mismos «seguidores del Camino»; tenían instrucción en el Camino del Señor. Toda vida cristiana debe encontrar su centro en Cristo Nuestro Señor, que es el Camino, la Verdad y la Vida, y la enseñanza auténtica de los Evangelios. Esta es la guía primera y esencial para todo el que quiera seguir esta forma de vida.",
                "Jn. 14:6; Hechos 9:2; 18:25"
            ),
            FvbArticle(
                2,
                "El Camino del Señor",
                "Las comunidades religiosas han sido llamadas por el Espíritu Santo para que sigan a Cristo en una forma especial, cada una en la suya propia: hay toda una variedad de dones, provenientes todos del solo y único Espíritu Santo, y muchos tipos de servicios que se pueden llevar a cabo para agradar al solo y único Señor. La presente obra, La Forma de Vida Basiliana, proviene de una de estas familias de religiosos, los Padres Basilianos. Es un esbozo de nuestra forma de vida religiosa, una guía que debe utilizarse en adición a otras fuentes auténticas de instrucción acerca de la vida cristiana y de las directivas generales de la Iglesia para comunidades religiosas.",
                "1 Cor. 12:4-6; PC 1-2"
            ),
            FvbArticle(
                3,
                "Los Padres Basilianos",
                "La Congregación de San Basilio, los Padres Basilianos, es una comunidad de sacerdotes y estudiantes para el sacerdocio. La nuestra es una comunidad activa y apostólica con votos simples, que busca la gloria de Dios en toda forma de actividad sacerdotal compatible con la vida común, especialmente en las obras de educación y evangelización.",
                "Constituciones de 1863; Const. 1-2; Juan Pablo II, Vita consecrata 9"
            ),
            FvbArticle(
                4,
                "San Basilio Magno",
                "San Basilio fue sacerdote y maestro y durante nueve años Obispo de Cesarea en Asia Menor. Toda su vida padeció de mala salud y murió, en el año 379, antes de cumplir cincuenta años. Sin embargo se mostró incansable en la enseñanza de la doctrina verdadera y en su labor en favor de la unidad de la Iglesia. No vivió lo suficiente para ver los frutos de su trabajo, pero se le ha reconocido como uno de los grandes Doctores de la Iglesia. Lo hemos escogido como nuestro santo patrono debido a su santidad y al ejemplo que nos da y debido a dos aspectos de su vida y enseñanza: imbuido con el espíritu de los Evangelios, enseñó a hombres y mujeres cómo llevar una vida cristiana en comunidad de acuerdo con las condiciones de su tiempo; y encontró, incluso en la sabiduría de los paganos, valores que vistos a la luz de la revelación podrían contribuir a una vida cristiana plena.",
                "Cf. C. Roume, A History of the Congregation of St. Basil to 1864, 158"
            ),
            FvbArticle(
                5,
                "El Espíritu de los Fundadores",
                "Los Padres Basilianos fueron fundados en 1822 por un grupo de diez sacerdotes diocesanos en Annonay, Francia con la intención de poder, gracias a las fuerzas que les confería su asociación en Cristo y en la Iglesia, servir mejor al Pueblo de Dios mediante su labor sacerdotal, especialmente en las áreas de la educación cristiana y la predicación. Su espíritu de devoción al trabajo y de compromiso con la Iglesia, la simplicidad de su vida y la conciencia de estar llamados a una vida comunitaria más profunda, animaban a los sacerdotes de San Basilio cuando hicieron sus votos formales en 1852 y estos carismas aún inspiran a los Basilianos hoy día.",
                "Carta de Septiembre 15, 1832; Roume, A History, 152-154"
            ),
            FvbArticle(
                6,
                "El Llamado de Cristo",
                "De acuerdo con el misterio del amor de Dios, se nos llama a que sigamos a Su Hijo, Jesucristo Nuestro Señor. Al igual que los discípulos de Cristo aprendieron sus enseñanzas gracias a su asociación con Él, así también nosotros, mediante nuestra unión íntima y personal con el Señor Jesús, aprendemos a amar y a servir. El amor que Él nos transmite es el centro y manantial de todo lo que somos y todo lo que hacemos. Para nosotros, la vida es Cristo, con Él hemos pasado de la muerte a la vida, cuyos miembros somos, cuya obra salvadora se nos ha llamado a llevar a cabo, y a cuyo Reino se nos ha llamado por obra de su amor desinteresado.",
                "Mc. 1:17; Jn. 1:35-51; Fil. 1:21; Jn. 5:24; 1 Cor. 12:27; Jn. 14:12; Col. 1:13"
            ),
            FvbArticle(
                7,
                "El Llamado de Cristo",
                "Como discípulos que somos escuchamos al Señor que nos habla en las Escrituras, a través de la Iglesia, en nuestra comunidad, en las señales del tiempo y por gracia de su Espíritu. Entonces, impulsados por ese mismo amor que nos ha llamado, respondemos con una entrega total de nuestro ser, de nuestro tiempo, de nuestros talentos y dones, con libertad, generosidad y alegría, al Señor, mediante el servicio en favor de nuestros hermanos y de todos los hijos de Dios. Confiamos en que Él, que ha comenzado esta buena obra en nuestro interior, nos ayude a perseverar en su gracia y se encargue de que haya sido completada cuando llegue el día de Cristo Jesús.",
                "Mt. 17:5; Mc. 9:7; Lc. 9:35; Vaticano II, DV 2-6; Fil. 1:6"
            ),
            FvbArticle(
                8,
                "El Llamado de Cristo",
                "Cuando nos dedicamos al servicio de Dios dentro de esta comunidad, emprendemos una conversión continua en nuestra vida, alejándonos del pecado y de los valores del mundo para seguir a Jesús. Ratificamos y sellamos nuestra ofrenda mediante los tres votos de castidad, pobreza y obediencia que nos vienen de la tradición común de la Iglesia como modos adecuados de profesar los consejos evangélicos en respuesta al llamado que Cristo nos hace a ser Basilianos. Al hacer esta profesión, esperamos que Cristo purifique nuestros corazones y nos dé libertad de espíritu y estímulo para nuestro amor, y sobre todo, que nos haga conformes con esa clase de vida pobre y virginal que escogió para Sí mismo. De esta manera, dejamos todas las cosas, en favor del Reino, siguiendo a Cristo dentro de una hermandad de amor y apoyo mutuo.",
                "Mt. 4:17; Hechos 3:19; LG 44; PC 5; Lc. 18:28-30; Vita consecrata 21"
            ),
            FvbArticle(
                9,
                "Castidad",
                "Mediante el voto de castidad nos comprometemos a una vida en la que, siguiendo el ejemplo del Señor, nos dedicamos a la obra del Señor con todo nuestro corazón. Esta vida es un don de Dios y surge de su gracia y nuestra respuesta a ella. Respondemos a su amor con el amor que nos da en nuestra unión con Su Hijo. Al llamarnos a que sigamos la castidad de Cristo nos libera de las responsabilidades y las preocupaciones de la vida familiar, de modo que podamos corresponder a su amor de manera especial dedicando nuestras vidas a su servicio y a servir a su pueblo.",
                "1 Cor. 7:32-35; 1 Jn. 4:10-11; PC 12; LG 42"
            ),
            FvbArticle(
                10,
                "Castidad",
                "Al hacer este voto renunciamos a un aspecto de la vida humana que es natural y bueno, pero en lo que hacemos hay mucho más que una simple renuncia. Consagramos nuestra castidad como una ofrenda en alabanza y adoración de la Santísima Trinidad. Con nuestro voto de castidad nos comprometemos a permanecer solteros por amor al Reino de Dios. También aceptamos la obligación de observar de manera perfecta en pensamiento y acción la castidad que corresponde a los que han adoptado la soltería y de hacer esto en virtud del voto.",
                "Mt. 19:12; LG 44; Constituciones 71"
            ),
            FvbArticle(
                11,
                "Castidad",
                "La consagración de nuestra castidad nos confiere una misión (así como fuerza para llevarla a cabo) de dar testimonio, en nuestra vida en común y en nuestras amistades dentro y fuera de la comunidad, de la nueva vida del Cristo resucitado. Debemos recordar que la castidad está mejor salvaguardada dentro de una comunidad donde un verdadero amor fraternal florece entre sus miembros y que las señales de una castidad verdadera no deben ser la misoginia, la timidez excesiva, ni la reserva cargada de severidad sino la mansedumbre y la modestia de Cristo.",
                "Vaticano II, PC 12; Mt. 11:29; Regla 42"
            ),
            FvbArticle(
                12,
                "Castidad",
                "El amor a Dios y a nuestro prójimo que esperamos lograr por medio de este voto es semejante al que Cristo nos mostró en persona: de todo corazón, generoso, y dispuesto a cualquier sacrificio por amor al Reino Celestial. Enraizado de manera profunda en el misterio de la cruz, requiere la muerte del egoísmo propio y un abrirse por amor a Cristo y a los demás. Ha de verse como una empresa fuerte y vigorosa que se enfrenta a las reglas y valores del mundo.",
                "Jn. 13:34-35; 1 Cor. 13:4-7; Mt. 16:24-26; Pablo VI, Evangelica testificatio 13"
            ),
            FvbArticle(
                13,
                "Castidad",
                "Si es vivida de esta manera, nuestra castidad será testigo vigoroso de la importancia de las cosas de Dios. Debemos recordar, sin embargo, que esta virtud nunca se puede dar por descontada. Necesitamos constantemente de la ayuda de Dios y de nuestros propios esfuerzos para que crezca nuestra dedicación.",
                "LG 42; Pablo VI, Evangelica testificatio 15"
            ),
            FvbArticle(
                14,
                "Pobreza",
                "«Felices los que tienen espíritu de pobre.» Desde que Nuestro Señor pronunció estas palabras y nos mostró mediante el ejemplo lo que significaban, sus seguidores han tratado de ganarse la bienaventuranza prometida a los pobres con una vida sencilla, generosa para con los necesitados, y una dependencia absoluta de la providencia del Padre de todos. En las comunidades religiosas, esto ha hecho surgir la costumbre de hacer un voto con el que hombres y mujeres se comprometen a imitar a Jesús, que tuvo espíritu de pobre, en sus condiciones de vida.",
                "Mt. 5:3; Lc. 6:20; Mt. 8:20; Lc. 9:58; PC 13; Constituciones 81"
            ),
            FvbArticle(
                15,
                "Pobreza",
                "Con ayuda del voto de pobreza buscamos adoptar esta pobreza de Cristo. Prometemos dedicar toda nuestra vida a trabajar por Cristo y compartir todo lo que tenemos, por Él, con nuestra comunidad, encontrándolo a Él presente en medio de nosotros y en todos aquéllos que sufren necesidad o pobreza.",
                "Mt. 25:34-46"
            ),
            FvbArticle(
                16,
                "Pobreza",
                "Lo que se pretende con nuestro voto de pobreza es librarnos para que podamos seguir a Nuestro Señor en cualquier labor que por Él hagamos. Consideramos que es lo único necesario y en consecuencia las posesiones y la propia seguridad asumen menor importancia. Hacemos esto en nuestra vida comunitaria, en la que nos preocupamos los unos por los otros y compartimos todo unos con otros, «ayudándonos mutuamente a llevar nuestras cargas y así cumplir la ley de Cristo».",
                "Lc. 10:41-42; Gál. 6:2"
            ),
            FvbArticle(
                17,
                "Pobreza",
                "Con nuestro voto de pobreza renunciamos no a la capacidad de ser dueños de cosas materiales sino a nuestra libertad de hacer uso de ellas. Nos sujetamos, para disponer de nuestros bienes materiales o hacer cualquier uso de ellos, a no actuar sin el consentimiento, al menos razonablemente implícito, de nuestras autoridades legítimas.",
                "Constituciones 73; Canon 668"
            ),
            FvbArticle(
                18,
                "Pobreza",
                "Un Basiliano puede, por lo tanto, seguir siendo dueño de cualquier patrimonio que haya poseído antes de hacer sus votos o que le llegue como tal aún después de hacerlos. No puede, sin embargo, administrarlo o utilizarlo para sus propios fines. Antes de hacer sus primeros votos, un novicio que sea dueño de algún patrimonio debe hacer arreglos de acuerdo a la ley común de la Iglesia. Antes de hacer su profesión perpetua, cada miembro deberá hacer un testamento en el que legue libremente todos sus bienes.",
                "Canon 668.1, 668.2"
            ),
            FvbArticle(
                19,
                "Pobreza",
                "«Anda a vender todo lo que posees y dáselo a los pobres... Y luego, sígueme.» En vista de estas palabras de Jesús al joven en el Evangelio, el Superior General, con el consentimiento de su Consejo, puede dar a un Basiliano que hace votos perpetuos permiso de deshacerse de todo patrimonio que posea o que le pueda ser legado en el futuro.",
                "Mt. 19:21; Canon 668.4; PC 13"
            ),
            FvbArticle(
                20,
                "Pobreza",
                "Todo lo que poseemos, aparte del patrimonio personal, lo poseemos en común y todo lo que ganamos pertenece al fondo común, y las necesidades de cada uno deben ser sufragadas por este fondo. Todos los salarios, pensiones, estipendios y cualquier otro tipo de entrada deben entregarse al fondo común. La comunidad llenará las necesidades legítimas de los hermanos en cuanto a lectura, alimentación, vestido, muebles, viajes, atención médica y demás. Cada uno de nosotros podrá conservar ciertas cosas para su uso personal, pero la propiedad de las mismas será en común. En las cosas que utilicemos de manera individual, no debemos buscar la uniformidad sino la sencillez verdadera. Cada uno de nosotros debe dar cuenta del uso que hace del fondo común.",
                "Cc. 668, 668.4; PC 13"
            ),
            FvbArticle(
                21,
                "Pobreza",
                "Estos mismos criterios de necesidad y deber de dar cuenta también deben aplicarse al uso de los regalos personales. Tenemos la responsabilidad de mostrar nuestro amor por la vida en común compartiendo unos con otros las cosas que poseemos.",
                "Constituciones 80"
            ),
            FvbArticle(
                22,
                "Pobreza",
                "Las comunidades, al igual que los individuos, deben ser pobres tanto de hecho como de espíritu para que nuestra obra complazca al Señor. Nuestra manera de vivir debe ser simple y práctica, de acuerdo con nuestras necesidades de trabajo. Las comunidades locales que hallen prosperidad en sus finanzas deben compartir su abundancia con las casas que se hallen en necesidad, ya sea ayudándolas directamente o a través del Fondo General. La comunidad debe sentir como propias las necesidades de los pobres y adoptar medidas prácticas que las subsanen.",
                "Cc. 600, 640; PC 13"
            ),
            FvbArticle(
                23,
                "Pobreza",
                "Los diferentes miembros y las diferentes comunidades locales pueden variar en lo que poseen, en lo que necesitan y en lo que consideran necesitar. Debemos mostrarnos renuentes a juzgarnos unos a otros en dichos asuntos; la pobreza de espíritu es un don de Dios. Ya que es una tendencia muy humana el gastar más si tenemos más, debemos juzgarnos a nosotros mismos recordando que Nuestro Señor nos previno contra la riqueza.",
                "Mt. 19:23-26; Lc. 6:24"
            ),
            FvbArticle(
                24,
                "Pobreza",
                "Todo lo que proviene de la mano creadora de Dios es bueno. El Padre ha colocado al hombre al frente de su creación para que sea su administrador y sacerdote y para que se encargue de que los bienes creados se utilicen para el máximo beneficio de todos. Nuestro llamado a la pobreza evangélica requiere que adjudiquemos el valor adecuado a cada uno de los dones de Dios. «Por ustedes se hizo pobre, siendo rico, para hacerlos ricos con su pobreza».",
                "Gén. 1:31; GS 12, 34; 2 Cor. 8:9"
            ),
            FvbArticle(
                25,
                "Obediencia",
                "Nuestro voto y práctica de obediencia es una respuesta a las palabras de Nuestro Señor: «Si ustedes me aman, guardarán mis mandamientos». Estas palabras iban dirigidas a todos sus seguidores, pero nosotros nos dedicamos de manera especial como individuos y como comunidad de vivir en amorosa obediencia a Cristo, dondequiera que lo encontremos en los Evangelios, en la Iglesia, en nuestras comunidades, en nuestros hermanos, en los pobres.",
                "Jn. 14:15; PC 14; Ecclesiae sanctae II, 18"
            ),
            FvbArticle(
                26,
                "Obediencia",
                "Mediante el voto de obediencia nos comprometemos a someternos con humildad a nuestros superiores en un espíritu de fe y amor a la voluntad de Dios y de acuerdo con La Forma de Vida Basiliana y las Costumbres Basilianas. Debemos ejercitar toda nuestra inteligencia y toda nuestra voluntad en el cumplimiento de los preceptos que se nos imponen y en la ejecución de las tareas que se nos encargan, dándonos cuenta que de esta manera contribuimos a la formación del Cuerpo de Cristo.",
                "Canon 601; Lc. 22:39-44; Jn. 12:27-28; PC 14"
            ),
            FvbArticle(
                27,
                "Obediencia",
                "El primer sitio en el que debemos buscar lo que el Padre quiere que hagamos es en las Santas Escrituras, la Palabra de Dios. Los Evangelios deben ser para cada uno de nosotros a diario una fuente viviente de las enseñanzas de Cristo.",
                "Vaticano II, DV 25; Jn. 8:31; Heb. 2:1"
            ),
            FvbArticle(
                28,
                "Obediencia",
                "Nuestra obediencia nos obliga, tanto como familia religiosa dentro de la Iglesia como en forma individual, a escuchar y obedecer la voz de Cristo en la Iglesia, tanto en la jerarquía, cuando nos instruye y gobierna, como en el Pueblo de Dios, conforme lo servimos en nuestra labor apostólica, especialmente en los pobres y en todos los que se encuentren afligidos o en dificultad. Como sacerdotes, al predicar el Evangelio debemos tener cuidado de hablar con la voz auténtica de Cristo.",
                "DV 8; PC 14; PO 4, 6"
            ),
            FvbArticle(
                29,
                "Obediencia",
                "Nuestro voto nos compromete ante Dios con una familia religiosa particular; aceptamos sus metas, su forma de vida, su obra y las tareas particulares que nos asignen nuestros superiores. Debemos estar bien dispuestos a ir a donde se nos mande. Pero también debemos sentirnos en libertad de comunicarles con franqueza a nuestros superiores nuestros intereses y habilidades, junto con nuestras esperanzas y los temores. Con esta doble libertad la comunidad puede responder verdaderamente al Espíritu Santo.",
                "PC 14; Pablo VI, Evangelica testificatio 25, 27; Const. 67"
            ),
            FvbArticle(
                30,
                "Obediencia",
                "Al profesar obediencia aceptamos nuestra responsabilidad como miembros de una comunidad religiosa autogobernada dentro de la Iglesia. Esto requiere nuestra participación activa en la elección de los que han de guiarnos y en la adopción de decisiones que servirán como lineamientos de nuestra vida y obra en común. Nos exige de manera especial que obedezcamos con generosidad y sin tardanza a nuestros superiores, tanto locales como generales.",
                "PC 14; LG 45; Evangelica testificatio 23-28"
            ),
            FvbArticle(
                31,
                "La Vida Comunitaria",
                "«Que todos sean uno como Tú, Padre, estás en Mí, y Yo en Ti. Sean también uno en nosotros: así el mundo creerá que tú me has enviado.» La Forma de Vida Basiliana es vida dentro de una comunidad. Queremos ser reflejo de la vida interna de la Santísima Trinidad, esforzándonos a vivir para las demás personas, dando y recibiendo, compartiendo todo lo que somos y todo lo que tenemos: «Dios mandó a nuestros corazones el Espíritu de su propio Hijo, que clama: ¡Abbá, Padre!».",
                "Jn. 17:21; Gál. 4:6; PC 15"
            ),
            FvbArticle(
                32,
                "La Vida Comunitaria",
                "Como Basilianos estamos llamados a vivir y expresar esta naturaleza comunal de la Iglesia mediante nuestro compromiso de unos con otros como hermanos. Hemos sido llamados a vivir de acuerdo con el Evangelio, siguiendo el ejemplo de las primeras comunidades cristianas y las tradiciones de la vida religiosa en la Iglesia. Para lograr este alto ideal de vida comunitaria confiamos en la ayuda de Dios y en la de nuestros hermanos.",
                "Hechos 2:42-47; 4:32-35; Canon 602; PC 15"
            ),
            FvbArticle(
                33,
                "La Vida Comunitaria",
                "Vivimos juntos una vida de fe; es decir, una vida de dependencia de Cristo nuestro Señor, presente para nosotros en los Evangelios, en la Iglesia, en nuestros hermanos y en todos aquéllos a quienes servimos. Siempre debemos estarle agradecidos a Dios por este don de la fe y rogarle que lo conserve vivo en nosotros.",
                "PC 6"
            ),
            FvbArticle(
                34,
                "La Vida Comunitaria",
                "«En el amor entre hermanos: demuéstrense cariño unos a otros. En el respeto: estimen a los otros como más dignos.» El corazón de la vida cristiana para los Basilianos es la casa local, donde vivimos juntos en la hermandad que Cristo adquirió para nosotros en la cruz. Juntos adoramos a Dios, juntos comemos y juntos nos recreamos, esforzándonos por estar unidos en alma y corazón. Las discusiones amistosas y francas de lo que hacemos juntos constituyen parte importante de la vida comunitaria y deben tener lugar a intervalos regulares.",
                "Rom. 12:10 (Regla de Leorat-Picansel 1802); Hechos 4:32; PC 15; Vita consecrata 42"
            ),
            FvbArticle(
                35,
                "La Vida Comunitaria",
                "«Ustedes no me escogieron a mí. Soy yo quien los escogí a ustedes.» Nosotros no escogemos a nuestros hermanos en Cristo; es Dios quien nos los da. Se nos ha llamado para que vivamos con ellos como hermanos, encontrando en ellos la presencia viva de Cristo, perdonándoles sus faltas y rehusándonos a juzgarlos o condenarlos. Encontraremos deleite en los períodos de silencio que son esenciales para nuestra vida en común, y sin embargo hablaremos cada vez que el amor así lo requiera.",
                "Jn. 15:16; Lc. 6:36; Gál. 2:20; Ef. 4:25-5:2; Col. 3:1-17; Const. 121"
            ),
            FvbArticle(
                36,
                "La Vida Comunitaria",
                "El valor de la contribución de cada miembro a nuestra comunidad no depende de lo que pueda hacer según los criterios del mundo. Vivimos juntos con el mismo llamado de Dios para que nos demos fuerzas unos a otros mediante la oración, el ejemplo y el sufrimiento. Las diferencias de edad, opinión y temperamento deben enriquecer nuestra hermandad. Los hermanos enfermos o ancianos deben suscitar de modo especial nuestro amor y cuidados.",
                "Col. 3:11; Gál. 5:22; PC 15; Evangelica testificatio 39-41"
            ),
            FvbArticle(
                37,
                "La Vida Comunitaria",
                "La vida comunitaria tiene lugar no sólo en las casas locales, sino también en la totalidad de la Congregación, donde se refleja en un sentimiento de solidaridad con lo que está haciendo todo el resto de los Basilianos, un interés por el pasado y el futuro de la comunidad, y una buena disposición para hacer los sacrificios que sean necesarios para el bien común.",
                "PC 2"
            ),
            FvbArticle(
                38,
                "La Vida Comunitaria",
                "El amor que nos une para formar una comunidad es el amor que nos da Cristo Nuestro Señor cuando, al compartir su muerte, dejamos de vivir para nosotros mismos y encontramos una nueva vida en Cristo junto con nuestros hermanos. Esto debe conducirnos a otras amistades y a participar en otras comunidades, particularmente aquéllas que se relacionan con nuestra labor apostólica, sin descuidar nuestra vida comunitaria.",
                "Jn. 13:34-35; Rom. 6:5-11; 2 Tim. 2:11; 2 Cor. 8:9"
            ),
            FvbArticle(
                39,
                "La Vida Comunitaria",
                "La hospitalidad es una virtud cristiana y una tradición Basiliana. Debemos reconocer al Señor no sólo en los extraños y aquéllos que se encuentran en necesidad, sino también en todos aquéllos que visitan nuestras comunidades. Los Basilianos de otras casas y los asociados Basilianos siempre deben encontrarse a gusto entre nosotros.",
                "Heb. 13:2; Mt. 25:34-36; Evangelica testificatio 46; Regla 59"
            ),
            FvbArticle(
                40,
                "Sacerdocio",
                "Los Basilianos sirven a la Iglesia como una comunidad de sacerdotes. Cuando recibimos el Sacramento del Orden Sagrado, se nos designa para que actuemos en nombre de Cristo, alimentando a los miembros de la Iglesia con la Palabra y la gracia de Dios. Esta misión sacerdotal se lleva a cabo en cualquier labor que desempeñamos. Tenemos una obligación especial de vivir juntos la vida, centrada en el Sacrificio Eucarístico y los Evangelios, que predicamos a los demás.",
                "LG 28; PO 2, 14"
            ),
            FvbArticle(
                41,
                "Sacerdocio",
                "Como sacerdotes estamos llamados a conformar el Cuerpo de Cristo para la gloria de Dios. Para este fin, se nos designa para que seamos ministros de la Palabra de Dios, a través de la predicación y la enseñanza, a través de la celebración de los sacramentos con los que la Palabra Hecha Carne se revela y se entrega a su pueblo, y a través del testimonio de una vida sacerdotal vivida de acuerdo con el Evangelio.",
                "PO 4-5"
            ),
            FvbArticle(
                42,
                "Sacerdocio",
                "Nuestro servicio sacerdotal encuentra su fuente y su expresión más alta en la celebración de la Eucaristía, la cual se nos ha designado a presidir. En esto más que en ninguna otra cosa prestamos servicio al crecimiento y la unidad del Cuerpo de la Iglesia, alimentándolo con la predicación de la Palabra y conduciendo la gran oración Eucarística.",
                "LG 11, 28; PO 5"
            ),
            FvbArticle(
                43,
                "Sacerdocio",
                "En todas las diócesis en las que nos encontramos establecidos, los sacerdotes Basilianos formamos, junto con todos los demás sacerdotes bajo la guía del obispo, un sólo cuerpo sacerdotal. Aportamos a este cuerpo nuestra vida y obra propias, reconociendo al obispo como autoridad y cooperando con los demás sacerdotes.",
                "LG 28; PO 7; Constituciones 135"
            ),
            FvbArticle(
                44,
                "Sacerdocio",
                "Un lazo especial de afecto nos une a los sacerdotes diocesanos, con los cuáles hemos prestado servicio a la Iglesia durante tantos años, y de entre las filas de los cuáles se originó nuestra comunidad. Debemos aprovechar las oportunidades que se presenten para reforzar la hermandad sacerdotal con todos los demás sacerdotes.",
                "PO 8; Constituciones de 1863, II, 2"
            ),
            FvbArticle(
                45,
                "Oración",
                "La vida toda y toda virtud nos vienen del Padre a través de Su Hijo, por don del Espíritu Santo. La oración es una respuesta a este llamado; para nosotros es una necesidad absoluta. La gente a cuyo servicio hemos sido llamados cuenta con nosotros para que ofrezcamos al Padre gracias y alabanzas continuas y para que intercedamos los unos por los otros. Sin verdadera oración, la vida y la obra a las que hemos sido llamados estarán más allá de nuestras fuerzas.",
                "Sant. 1:17; Ef. 3:14-19; LG 44; PC 2, 6, 8; Canon 663"
            ),
            FvbArticle(
                46,
                "Oración",
                "Una expresión de nuestra oración se halla en el culto público. En cada una de nuestras casas este culto consiste en el diario ofrecimiento de la Eucaristía y de Laudes y Vísperas de acuerdo con la Liturgia de las Horas. Se exhorta vehementemente a los sacerdotes a que celebren a diario la misa y a los miembros no ordenados a que asistan a la misma.",
                "Pablo VI, Evangelica testificatio 47; Constituciones 93; Regla 26"
            ),
            FvbArticle(
                47,
                "Oración",
                "En la celebración de la Eucaristía, el Espíritu Santo junta a todos los cristianos en estrecha unión; por tal razón, debemos disponer nuestro ofrecimiento de la Eucaristía de manera que lo celebremos siempre que sea posible unos en compañía de otros, así como de aquéllos con los que laboramos.",
                "Canon 608; SC 48; LG 42"
            ),
            FvbArticle(
                48,
                "Oración",
                "Todos los miembros de una casa están involucrados en el culto público de la comunidad, dependen de él, y comparten la responsabilidad por el mismo. En cada casa debe hacerse un esfuerzo especial para celebrar la Eucaristía comunitaria una vez por semana.",
                "S.C.C.D., In celebratione Missae (1972)"
            ),
            FvbArticle(
                49,
                "Oración",
                "Aquéllos que se encuentran enfermos dependen de manera especial de la vida sacramental de nuestras comunidades. Compartimos con Nuestro Señor la compasión que Él siente por los enfermos cuando rezamos por nuestros hermanos enfermos, y especialmente cuando les ofrecemos la Eucaristía, la Reconciliación y la Unción de los Enfermos. Cuando un hermano muere debemos reunirnos para orar por él y ofrecer los sufragios especificados en Costumbres Basilianas.",
                "Rito de la Unción de los Enfermos (1972)"
            ),
            FvbArticle(
                50,
                "Oración",
                "En nuestros actos de culto público escuchamos la voz de Dios que nos habla, y le respondemos. Debemos prestar mucha atención en nuestro culto público y en nuestras oraciones comunitarias de no llevarlos a cabo de manera descuidada, sino de tal forma que glorifique a Dios y refuerce la fe de todos.",
                "SC 33"
            ),
            FvbArticle(
                51,
                "Oración",
                "La Santísima Virgen María, San José, San Basilio, San Francisco de Asís, Santo Tomás de Aquino y San Juan Bosco son los patronos especiales de la comunidad, y sus fiestas son ocasiones de oración especial. La fiesta de la Presentación de la Virgen María (21 de noviembre) y la de San Basilio (2 de enero) deben ser días de celebración particular.",
                "Constituciones 5; Regla 106"
            ),
            FvbArticle(
                52,
                "Oración",
                "De vez en cuando el Superior General pide a los miembros que recen de manera especial por las necesidades de la Congregación. Se alienta a las comunidades locales a que planeen otras formas de oración en común en forma regular. De modo particular, el retiro anual, tan importante para nuestro desarrollo espiritual, debe enriquecer nuestra oración.",
                "Constituciones 100; Regla 106"
            ),
            FvbArticle(
                53,
                "Oración",
                "Se nos llama a que sigamos a Nuestro Señor y compartamos su amistad. Si hemos de lograr unirnos a Él en la forma en que vivimos y en la labor que desempeñamos, cada uno de nosotros habrá de esforzarse por hacer de su oración personal el centro de su vida diaria.",
                "Mt. 4:19; Jn. 15:15; Evangelica testificatio 42-45"
            ),
            FvbArticle(
                54,
                "Oración",
                "Tenemos mucho recurso para nuestra oración: el conocimiento de que Dios es verdaderamente nuestro Padre, de que Cristo es nuestro modelo y maestro, de que contamos con el Espíritu Santo para que nos anime y nos ilumine y con la Palabra en las Santas Escrituras; la vida sacramental con la Eucaristía y la Reconciliación frecuente; la protección de la Madre de Dios; las formas de orar de los santos y la presencia de Cristo en el Santísimo.",
                "PC 6"
            ),
            FvbArticle(
                55,
                "Oración",
                "Nuestra oración no tendrá valor si es descuidada o superficial. Debemos acercarnos a Dios con lo mejor de que dispongamos: una atención total por parte de nuestra mente y el afecto más profundo de nuestro corazón. Será difícil lograr esto si no le otorgamos tiempo, a diario y con generosidad, a la oración mental. Una hora diaria de oración personal debe ser considerada el mínimo.",
                "Pablo VI, Ecclesiae sanctae II, 21"
            ),
            FvbArticle(
                56,
                "Oración",
                "«El que quiera seguirme, que renuncie a sí mismo, que cargue con su cruz y que me siga.» Nuestra comunidad nunca ha impuesto prácticas de penitencia específicas, mas siempre nos ha alentado a que sigamos a Cristo laborando con empeño y según lo dicte el fervor religioso con prudencia. Es precisamente en nuestra vida de oración donde podemos llegar a comprender en qué debe consistir nuestra cruz individual y aceptarla mediante la auto-renuncia.",
                "Fil. 2:5-11; Mt. 17:24"
            ),
            FvbArticle(
                57,
                "Oración",
                "«Vivan orando y suplicando», nos dice San Pablo. «Oren en todo tiempo según les inspire el Espíritu.» No podemos estar pensando en Dios todo el tiempo, pero debemos de tratar de meter todo nuestro día en nuestra vida de oración, de modo que estemos en comunión con Dios de manera habitual.",
                "Ef. 6:18"
            ),
            FvbArticle(
                58,
                "Oración",
                "La oración litúrgica, comunitaria y privada, alimenta y expresa esa oración habitual que consiste en vivir en la presencia de Dios, compartiendo por medio del poder del Espíritu el designio del Padre de reunir todo en unión con la gloria del Cristo Resucitado. Esta comunión es lo que inspira al cristiano a colaborar en la misión de Cristo de renovar todas las cosas.",
                "Ef. 1:10; Apoc. 21:5"
            ),
            FvbArticle(
                59,
                "Nuestra Labor Apostólica",
                "Cristo fundó la Iglesia para que todos los hombres compartieran la salvación de Su gracia redentora mediante la propagación de su reinado en todas partes para gloria de Dios Padre. En nuestra labor cooperamos con los obispos y con los laicos de tal manera que el resultado sea un respeto mutuo y el crecimiento de todos en Cristo.",
                "AA 2; LG 20"
            ),
            FvbArticle(
                60,
                "Nuestra Labor Apostólica",
                "Como comunidad religiosa dedicada al apostolado estamos unidos de manera especial al misterio de la Iglesia en su papel de símbolo e instrumento de la unión íntima con Dios y de la unidad de toda la humanidad. El dar testimonio es parte medular de nuestra existencia. Nuestra labor apostólica da forma a la textura de nuestra vida.",
                "LG 44; PC 8; Vita consecrata 9"
            ),
            FvbArticle(
                61,
                "Nuestra Labor Apostólica",
                "No es sólo en razón de nosotros mismos que se nos ha llamado a seguir «el Camino del Señor». Dicho llamado bautismal es también un llamado a la misión y al ministerio dentro de la Iglesia. Nuestros votos, nuestra vida comunitaria y nuestro sacerdocio nos dan nuestro propio apostolado dentro de la Iglesia.",
                "Hechos 9:2; 18:25; LG 44"
            ),
            FvbArticle(
                62,
                "Nuestra Labor Apostólica",
                "«El Hijo del Hombre no vino para que lo sirvieran, sino para servir»; el ministerio es esencial para nuestra vida en Cristo. Llevamos a cabo un ministerio los unos para con los otros cuando rezamos, cuando escuchamos, cuando nos regocijamos o sufrimos con ellos. El ministerio permea nuestras vidas; nunca se detiene, ni siquiera cuando estamos enfermos o hemos envejecido.",
                "Mc. 10:45"
            ),
            FvbArticle(
                63,
                "Nuestra Labor Apostólica",
                "Cada quien tiene una labor individual que llevar a cabo y, puesto que es la labor de Dios, tenemos que hacerla lo mejor que podamos, ayudándonos al máximo con la oración. «Así pues, hermanos míos muy amados, sigan firmes y no se dejen extraviar. Progresen siempre en la obra del Señor, sabiendo que él no deja sacrificios sin premio».",
                "1 Cor. 15:58"
            ),
            FvbArticle(
                64,
                "Nuestra Labor Apostólica",
                "La primera labor de nuestra Congregación fue la de la educación y la predicación cristiana, y siempre hemos sido una comunidad dedicada a la enseñanza. Con este punto de partida, nos hemos adaptado para servir al Pueblo de Dios en escuelas, universidades, posgrados, publicaciones, parroquias y misiones: «la construcción del Cuerpo de Cristo». «El que planta y el que riega son una sola cosa... Juntos trabajamos en la obra de Dios».",
                "Ef. 4:12; 1 Cor. 3:8-9; Vita consecrata 96-98"
            ),
            FvbArticle(
                65,
                "Nuestra Labor Apostólica",
                "Nuestros fundadores se preocuparon de manera especial por servir a los pobres proveyéndolos con la educación cristiana que tanto necesitaban. Tenemos que hacer todo lo posible para asegurarnos de que nuestras escuelas no excluyan a los pobres, y de que en ninguno de nuestros apostolados transijamos con ningún tipo de injusticia social. Debemos sentir como propio el drama de la miseria humana y las demandas de justicia social.",
                "Pablo VI, Evangelica testificatio 18"
            ),
            FvbArticle(
                66,
                "Nuestra Labor Apostólica",
                "Un Basiliano no escoge por sí mismo la labor apostólica que llevará a cabo; trabajamos conjuntamente, como comunidad. Si alguno de nosotros desease proponer nuevos tipos de apostolado, debe presentar sus ideas ante la comunidad local, al Superior General y su Consejo o al Capítulo General, consultando con el obispo local.",
                "PC 8, 14; LG 45; Constituciones 135-137; Regla 108"
            ),
            FvbArticle(
                67,
                "Nuestra Labor Apostólica",
                "Debemos estar dispuestos y abiertos a escuchar proposiciones nuevas. Al evaluar nuevos apostolados, hemos de tener en cuenta las necesidades de la Iglesia, nuestros carismas y recursos Basilianos particulares, nuestros compromisos previos y la compatibilidad de la labor con la vida comunitaria.",
                "Hechos 15:6, 28"
            ),
            FvbArticle(
                68,
                "Vocaciones",
                "«La cosecha es grande, y pocos los obreros. Por eso rueguen al dueño de la siembra que mande obreros para hacer la cosecha.» Debemos orar y actuar para que el Señor mande nuevos trabajadores a esa parte de la cosecha que nos ha encomendado.",
                "Mt. 28:19; 9:37-38; Regla 319"
            ),
            FvbArticle(
                69,
                "Vocaciones",
                "Tanto como comunidad como de manera individual debemos tratar de impulsar las vocaciones a nuestra forma de vida. El buen ejemplo de nuestras propias vidas constituye la máxima recomendación ante la comunidad y la invitación más atractiva a abrazar la vida religiosa.",
                "PC 24; PO 11"
            ),
            FvbArticle(
                70,
                "Vocaciones",
                "Las comunidades locales deben discutir con frecuencia la posibilidad de que haya vocaciones entre aquéllos con quienes trabajan, dando estímulos y directivas a todo joven que muestre señales de vocación.",
                "OT 2; Regla 320-321"
            ),
            FvbArticle(
                71,
                "Vocaciones",
                "Al aconsejar a jóvenes o recomendar candidatos, debemos evitar tanto la excesiva tolerancia que no critique nada como la exigencia de una perfección irreal. Debemos buscar las cualidades esenciales de un buen candidato.",
                "Regla 322"
            ),
            FvbArticle(
                72,
                "Vocaciones",
                "Un candidato debe tener una gran fe en Dios, devoción a la Iglesia y a la Eucaristía, y un deseo sincero de orar como base de la vida cristiana.",
                "2 Tim. 1:5; LG 44"
            ),
            FvbArticle(
                73,
                "Vocaciones",
                "Debe poseer amor apostólico y el deseo vehemente de proclamar a Jesucristo como sacerdote Basiliano en bien del pueblo de Dios.",
                "Lc. 12:50; 2 Cor. 8:16-17"
            ),
            FvbArticle(
                74,
                "Vocaciones",
                "Debe desear vivir en comunidad fraterna y consagrarse en castidad, pobreza y obediencia.",
                "PC 14-15"
            ),
            FvbArticle(
                75,
                "Vocaciones",
                "Debe mostrar evidencia de salud física, madurez emocional, capacidad para llevarse bien con la gente y habilidad intelectual suficiente para las exigencias del apostolado.",
                "S.C.R.I.S., Renovationis causam 11"
            ),
            FvbArticle(
                76,
                "Asociados",
                "Antes de entrar al noviciado, el candidato participa en el programa de asociados (postulantado) para profundizar su conocimiento de la vida y misión Basiliana. El derecho a aceptar candidatos para este programa corresponde al Superior General con consentimiento de su Consejo.",
                "Renovationis causam 4, 11"
            ),
            FvbArticle(
                77,
                "Asociados",
                "Cuando un asociado ha completado la preparación adecuada y mutuo conocimiento, puede solicitar su admisión formal al noviciado.",
                "Renovationis causam 4, 14"
            ),
            FvbArticle(
                78,
                "Novicios",
                "El año de noviciado es la iniciación formal a la vida religiosa e incluye una decisión madura de dar dirección definitiva a su vida.",
                "Renovationis causam 13"
            ),
            FvbArticle(
                79,
                "Novicios",
                "El interés primordial es el crecimiento espiritual, el discernimiento vocacional, la vida de oración y la práctica de los consejos evangélicos bajo la guía del Maestro de Novicios.",
                "Constituciones 33"
            ),
            FvbArticle(
                80,
                "Novicios",
                "El programa del noviciado incluye estudio de las Sagradas Escrituras, teología de la vida religiosa y votos, historia y tradiciones de la Congregación y de La Forma de Vida Basiliana, bajo un Maestro de Novicios con votos perpetuos.",
                "Renovationis causam 15"
            ),
            FvbArticle(
                81,
                "Novicios",
                "La admisión al noviciado compete al Superior General con su Consejo, asegurando madurez y aptitud requeridas.",
                "Const. 10, 12"
            ),
            FvbArticle(
                82,
                "Novicios",
                "Los requisitos para la validez de la admisión son los señalados en el Derecho Canónico y en Costumbres Basilianas.",
                "Cc. 641-645; Constituciones 13"
            ),
            FvbArticle(
                83,
                "Novicios",
                "El noviciado válido exige doce meses continuos en la casa de noviciado, con posibilidad de períodos apostólicos supervisados que no excedan en total dos años.",
                "Renovationis causam 22-25"
            ),
            FvbArticle(
                84,
                "Novicios",
                "Al término del noviciado, si es idóneo, el candidato es llamado a emitir votos temporales de castidad, pobreza y obediencia.",
                "Canon 655"
            ),
            FvbArticle(
                85,
                "Fórmula de Profesión",
                "FÓRMULA SOLEMNE DE LOS VOTOS:\n«Yo, [nombre], para gloria de Dios, movido por el firme propósito de consagrarme a Él más íntimamente siguiendo más de cerca a Cristo toda la vida, ante los hermanos aquí presentes, y ante ti, [nombre], Superior General, hago voto [temporal / perpetuo] de castidad, pobreza y obediencia según la Regla y las Constituciones de la Congregación de los Padres Basilianos, y me entrego de todo corazón a esta Congregación, para alcanzar la caridad perfecta en el servicio de Dios y de la Iglesia, con la gracia del Espíritu Santo y la ayuda de la Bienaventurada Virgen María.»\nRespuesta del Superior: «Y yo, por la potestad que tengo, en nombre de la Iglesia, recibo los votos que has emitido en la Congregación de los Padres Basilianos y te encomiendo vivamente a Dios, para que puedas cumplir tu ofrenda asociada al Sacrificio Eucarístico.»",
                "Constituciones 56"
            ),
            FvbArticle(
                86,
                "Escolásticos",
                "El escolástico es un miembro profeso que se prepara para el sacerdocio. Su formación integral es guiada por el Maestro de Escolásticos.",
                "Cc. 659-661; OT 4"
            ),
            FvbArticle(
                87,
                "Escolásticos",
                "La vida en las casas locales asignadas nutre su desarrollo espiritual y su comunión con el apostolado.",
                "OT 8-9"
            ),
            FvbArticle(
                88,
                "Escolásticos",
                "Seguirán estudios teológicos y filosóficos aprobados por las Conferencias Episcopales y la Congregación para conformarse a Cristo Buen Pastor.",
                "OT 4"
            ),
            FvbArticle(
                89,
                "Escolásticos",
                "La disciplina personal e interior debe caracterizar al escolástico, lejos de la mera conformidad externa.",
                "OT 11"
            ),
            FvbArticle(
                90,
                "Escolásticos",
                "Los formadores brindan acompañamiento espiritual y discernimiento objetivo para clarificar la autenticidad vocacional.",
                "OT 6"
            ),
            FvbArticle(
                91,
                "Escolásticos",
                "Los votos temporales se renuevan periódicamente hasta la profesión perpetua tras al menos tres años, sin exceder nueve años en total.",
                "Renovationis causam 6, 9, 37"
            ),
            FvbArticle(
                92,
                "Escolásticos",
                "Antes de renovar votos, profesión perpetua u órdenes sagradas, se consulta debidamente a la comunidad local del candidato.",
                "Constituciones"
            ),
            FvbArticle(
                93,
                "Salidas de la Comunidad",
                "El compromiso religioso es de por vida; cualquier salida o dispensa debe tramitarse con estricto apego al Derecho Canónico y Costumbres Basilianas.",
                "Cc. 686-704; Pablo VI, Cum admotae n. 14"
            ),
            FvbArticle(
                94,
                "Educación Continua",
                "Todo Basiliano debe continuar su formación intelectual, teológica, bíblica, litúrgica y pastoral a lo largo de toda su vida sacerdotal.",
                "Canon 279; PC 18; PO 19; Vita consecrata 98"
            ),
            FvbArticle(
                95,
                "Educación Continua",
                "Cada hermano reservará tiempo habitual para estudio, lectura personal y dirección espiritual competente.",
                "Cc. 662-664; PC 12-13"
            ),
            FvbArticle(
                96,
                "Educación Continua",
                "Los superiores y la comunidad facilitarán y estimularán los medios para el crecimiento espiritual y académico de los hermanos.",
                "Evangelica testificatio 38-39"
            ),
            FvbArticle(
                97,
                "Educación Continua",
                "La preparación para nuevas formas de ministerio y para los años de retiro debe planificarse con suficiente anticipación fraterna.",
                "PC 3"
            ),
            FvbArticle(
                98,
                "Gobierno de la Congregación",
                "La Congregación de San Basilio es un instituto clerical de derecho pontificio, autogobernado dentro de la Iglesia.",
                "Cc. 573-589"
            ),
            FvbArticle(
                99,
                "Gobierno de la Congregación",
                "Toda autoridad proviene de la voluntad de Dios revelada en Jesucristo. Profesamos filial obediencia al Sumo Pontífice y a los Obispos diocesanos en su jurisdicción.",
                "Cc. 590, 593; LG 45; PC 14"
            ),
            FvbArticle(
                100,
                "Gobierno de la Congregación",
                "La finalidad del gobierno es servir a la misión de la comunidad, asistiendo a cada hermano a vivir su consagración y coordinando con la Iglesia.",
                "PC 14"
            ),
            FvbArticle(
                101,
                "Gobierno de la Congregación",
                "Los superiores gobiernan en espíritu de servicio evangélico, discernimiento orante, consulta fraterna y docilidad al Espíritu Santo.",
                "Cc. 617-618; PC 14"
            ),
            FvbArticle(
                102,
                "Gobierno de la Congregación",
                "Deben existir canales fluidos de comunicación y escucha entre quienes gobiernan y sus hermanos.",
                "PC 14"
            ),
            FvbArticle(
                103,
                "Gobierno de la Congregación",
                "El Capítulo General es el órgano supremo que expresa la corresponsabilidad de todos los miembros en el bien común.",
                "PC 14; Ecclesiae sanctae II, 18"
            ),
            FvbArticle(
                104,
                "El Capítulo General",
                "El Capítulo General se celebra al finalizar el mandato del Superior General o quedar vacante. Está compuesto por miembros ex-oficio y delegados electos cuyo número nunca será menor al de los ex-oficio.",
                "Constituciones 210, 213; Aprobación Roma 2001"
            ),
            FvbArticle(
                105,
                "El Capítulo General",
                "Debe prepararse con amplia participación de todos los Basilianos para recoger los anhelos de la comunidad.",
                "Ecclesiae sanctae II, 18"
            ),
            FvbArticle(
                106,
                "El Capítulo General",
                "Su tarea es evaluar el estado de la Congregación, legislar, planificar el futuro y elegir al Superior General y a los Consejeros Generales.",
                "PC 4; Regla 336"
            ),
            FvbArticle(
                107,
                "El Capítulo General",
                "Toda legislación capitular debe encuadrarse en las leyes de la Iglesia y las directrices de la Santa Sede.",
                "Cc. 631-633"
            ),
            FvbArticle(
                108,
                "El Capítulo General",
                "La elección del Superior General es presidida por el Superior General saliente o el Vicario General.",
                "Constituciones 237"
            ),
            FvbArticle(
                109,
                "El Capítulo General",
                "Para ser elegido Superior General se requiere ser sacerdote con votos perpetuos y al menos 35 años de edad, requiriéndose mayoría absoluta según el escrutinio normado.",
                "Cc. 164-183, 626; Const. 235-249"
            ),
            FvbArticle(
                110,
                "El Capítulo General",
                "Posteriormente se eligen en votaciones sucesivas el Vicario General, el Segundo, Tercer y Cuarto Consejero General (sacerdotes, votos perpetuos, mínimo 30 años).",
                "Canon 631.2; Constituciones 254-260"
            ),
            FvbArticle(
                111,
                "El Capítulo General",
                "El Capítulo no puede alterar La Forma de Vida Basiliana por sí mismo; cualquier enmienda sustantiva requiere aprobación de las dos terceras partes y la confirmación de la Santa Sede.",
                "LG 45; Ecclesiae sanctae II, 8"
            ),
            FvbArticle(
                112,
                "El Capítulo General",
                "Los decretos del Capítulo rigen hasta el siguiente Capítulo General que podrá confirmarlos o enmendarlos.",
                "Constituciones 266"
            ),
            FvbArticle(
                113,
                "El Capítulo General",
                "Las decisiones capitulares deben ser acogidas y practicadas con amor por todos los hermanos para que den fruto.",
                "Sant. 1:25"
            ),
            FvbArticle(
                114,
                "El Superior General",
                "El Superior General es elegido por cuatro años y puede ser reelegido solo para un segundo período consecutivo.",
                "Constituciones"
            ),
            FvbArticle(
                115,
                "El Superior General",
                "Sirve a toda la comunidad y a cada hermano con amor generoso a imagen de Cristo Buen Pastor.",
                "PC 14; Evangelica testificatio 25-29"
            ),
            FvbArticle(
                116,
                "El Superior General",
                "Reza diariamente por toda la Congregación, vivos y difuntos, familiares y bienhechores, y dispone la Eucaristía diaria por estas intenciones.",
                "Constituciones 281"
            ),
            FvbArticle(
                117,
                "El Superior General",
                "Cuida con paternal solicitud el progreso y acompañamiento de asociados, novicios y escolásticos.",
                "OT 5; Regla 346"
            ),
            FvbArticle(
                118,
                "El Superior General",
                "Supervisa diligentemente la administración de los bienes temporales respetando la labor del Ecónomo General y superiores locales.",
                "Canon 636; Constituciones 280"
            ),
            FvbArticle(
                119,
                "El Superior General",
                "Es accesible a todos los hermanos, acoge al afligido («no quebrará la caña cascada») y vela con firmeza por el orden y la disciplina.",
                "Is. 42:3; Mt. 11:28-30; Lc. 15:1-7"
            ),
            FvbArticle(
                120,
                "El Superior General",
                "Ejerce la corrección fraterna con bondad, prudencia y firmeza evangélica.",
                "Constituciones"
            ),
            FvbArticle(
                121,
                "El Superior General",
                "Concede dispensas disciplinarias e impone reglamentos suplementarios con su Consejo entre capítulos.",
                "Constituciones 272, 283-285"
            ),
            FvbArticle(
                122,
                "El Superior General",
                "Su autoridad abarca toda la Congregación, subordinada a los Evangelios, leyes de la Iglesia, la Forma de Vida y Costumbres Basilianas.",
                "Cc. 133, 617-622"
            ),
            FvbArticle(
                123,
                "El Superior General",
                "No confiere ni revoca la voz activa y pasiva fuera de lo determinado por el derecho canónico.",
                "Constituciones 287"
            ),
            FvbArticle(
                124,
                "El Superior General",
                "Nombra los cargos necesarios, transfiere a los hermanos previa consulta y realiza la visita canónica a cada casa al menos cada tres años.",
                "Cc. 596, 617, 627"
            ),
            FvbArticle(
                125,
                "El Superior General",
                "Solicita el consentimiento y parecer de su Consejo según lo estipulado en las Costumbres Basilianas.",
                "Regulaciones 249-250"
            ),
            FvbArticle(
                126,
                "El Superior General",
                "En caso de renuncia o remoción, el asunto se tramita directamente ante la Santa Sede.",
                "Constituciones 291-293"
            ),
            FvbArticle(
                127,
                "El Superior General",
                "Al vacar el cargo, el Vicario General asume el gobierno interino y convoca Capítulo en máximo seis meses.",
                "Constituciones 291"
            ),
            FvbArticle(
                128,
                "El Consejo General",
                "El Consejo General consta de cuatro Consejeros electos por el Capítulo General para asistir al Superior General.",
                "Canon 627"
            ),
            FvbArticle(
                129,
                "El Consejo General",
                "[Artículo reservado por el Capítulo General para futuras enmiendas]",
                "Capítulo General"
            ),
            FvbArticle(
                130,
                "El Consejo General",
                "Los Consejeros pueden recibir delegaciones especiales y representan a la comunidad ante conferencias eclesiales.",
                "Constituciones"
            ),
            FvbArticle(
                131,
                "El Consejo General",
                "En caso de vacante entre los consejeros, se provee la sucesión ordenada establecida en las normas.",
                "Constituciones 304"
            ),
            FvbArticle(
                132,
                "El Consejo General",
                "Celebra sesiones regulares con el Superior General para tratar nombramientos y asuntos de importancia.",
                "PC 14; Const. 295"
            ),
            FvbArticle(
                133,
                "El Consejo General",
                "El Consejo se reúne formalmente al menos cinco veces al año a intervalos regulares.",
                "Constituciones 294"
            ),
            FvbArticle(
                134,
                "El Consejo General",
                "El Primer Consejero ejerce como Vicario General con facultades sustitutivas en ausencia del Superior.",
                "Constituciones 257"
            ),
            FvbArticle(
                135,
                "El Consejo General",
                "[Artículo reservado por el Capítulo General]",
                "Capítulo General"
            ),
            FvbArticle(
                136,
                "El Consejo General",
                "Cada casa local de la comunidad recibirá una visita oficial por lo menos cada dos años.",
                "Cc. 628.1, 628.3; Const. 275"
            ),
            FvbArticle(
                137,
                "El Consejo General",
                "Los Consejeros visitarán las casas escuchando las aspiraciones y talentos de cada hermano para armonizarlos con la misión común.",
                "PC 14; Evangelica testificatio 25"
            ),
            FvbArticle(
                138,
                "La Curia General",
                "El Secretario General y el Tesorero (Ecónomo) General desempeñan servicios esenciales nombrados por el Superior General con su Consejo.",
                "Constituciones"
            ),
            FvbArticle(
                139,
                "La Curia General",
                "El Secretario General debe ser un sacerdote de al menos 30 años con votos perpetuos, cargo incompatible con el de Vicario General.",
                "Constituciones 255"
            ),
            FvbArticle(
                140,
                "La Curia General",
                "Tanto la Congregación como las casas individuales tienen capacidad jurídica de adquirir, poseer y administrar bienes.",
                "Cc. 634-635; Const. 308"
            ),
            FvbArticle(
                141,
                "La Curia General",
                "Los bienes de la Congregación son administrados por el Tesorero General bajo la dirección del Superior General y Consejo.",
                "Cc. 636, 1273; Const. 309"
            ),
            FvbArticle(
                142,
                "La Curia General",
                "Para enajenaciones que superen los límites diocesanos fijados por la Santa Sede o la Conferencia Episcopal, se requiere licencia de Roma.",
                "Cc. 1291-1295"
            ),
            FvbArticle(
                143,
                "La Curia General",
                "Las asambleas y reuniones comunitarias periódicas son medios vitales de consulta y comunión fraterna.",
                "Evangelica testificatio 25"
            ),
            FvbArticle(
                144,
                "Gobierno Local",
                "El gobierno local coordina los esfuerzos comunitarios para cultivar una vida de fe, comunión y misión según el Evangelio y las Costumbres.",
                "Evangelica testificatio 25-26, 33-36"
            ),
            FvbArticle(
                145,
                "Gobierno Local",
                "Los Superiores (casas con 6 o más miembros) y Rectores (casas con menos de 6 miembros) son nombrados por 3 años, renovables por otro período.",
                "Canon 624; Const. 341-344; Cum admotae 19"
            ),
            FvbArticle(
                146,
                "Gobierno Local",
                "El Superior cuenta con consejeros locales elegidos por la casa para deliberar en los asuntos comunes.",
                "Canon 627.1"
            ),
            FvbArticle(
                147,
                "Gobierno Local",
                "El superior local debe ser generoso, paciente, promotor del bien común y servidor de todos a ejemplo de Cristo.",
                "Evangelica testificatio 23-29; Regla 458"
            ),
            FvbArticle(
                148,
                "Gobierno Local",
                "Debe escuchar con afecto, sin prejuicios, atendiendo las fortalezas y fragilidades de cada hermano.",
                "PC 14"
            ),
            FvbArticle(
                149,
                "Gobierno Local",
                "Favorece el diálogo y la corresponsabilidad sin menoscabar su deber de decisión y amparo de la libertad personal.",
                "Canon 619; PC 14"
            ),
            FvbArticle(
                150,
                "Gobierno Local",
                "Aplica La Forma de Vida y Costumbres con caridad y prudencia pastoral, con potestad de conceder dispensas oportunas.",
                "Constituciones 351-352, 465; Regla 1-4"
            ),
            FvbArticle(
                151,
                "Gobierno Local",
                "Las decisiones que afecten el futuro de la casa se coordinan previamente con el Superior General y su Consejo.",
                "Regulaciones 84-91"
            ),
            FvbArticle(
                152,
                "Gobierno Local",
                "Al fallecer un hermano, el superior notifica a los familiares y al Superior General y dispone las honras fúnebres fraternas.",
                "Canon 514"
            ),
            FvbArticle(
                153,
                "Nuestra Forma de Vida",
                "El Pueblo de Dios no tiene una ciudad perdurable en este mundo, sino que se halla en busca de la ciudad que ha de venir. Nuestra forma de vida como Basilianos nos da cierta medida de libertad respecto a las preocupaciones de la existencia terrenal. Revela los bienes celestiales presentes entre nosotros y testimonia la vida nueva y eterna que Cristo nos alcanzó con su obra redentora. Para nosotros, nuestra forma de vida es una imitación, lo más cercana que nos sea posible, de la vida en que Jesús cumplió la voluntad de su Padre. Aceptamos con gozo estas normas, expresando en forma Basiliana la realidad de Jesucristo como Señor nuestro y el poder del Espíritu Santo que actúa de manera maravillosa en el seno de la Iglesia.",
                "Vaticano II, LG 44; Heb. 13:14"
            )
        )
    }
}
