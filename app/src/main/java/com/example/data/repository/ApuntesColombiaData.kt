package com.example.data.repository

import com.example.data.model.*

object ApuntesColombiaData {

    val book: ApuntesColombiaBook by lazy {
        ApuntesColombiaBook(
            title = "Apuntes para una historia de la Congregación de San Basilio en Colombia",
            subtitle = "En sus diez primeros años (1987 - 15 de agosto - 1997)",
            years = "1987 - 1997",
            author = "Luis Fernando Sánchez Hurtado, C.S.B. (con la colaboración de Efraín Ferrer)",
            prologueAuthor = "P. Roberto Barringer, C.S.B. • Superior General",
            prologueDate = "24 de junio de 1997 • Toronto",
            prologueText = listOf(
                "Los inicios de la Congregación de San Basilio en Colombia son tan recientes que a muchos les puede parecer curioso el hecho de empezar a elaborar sus crónicas contemporáneamente con los sucesos mismos. Sin embargo, la vivencia de la comunidad basiliana en Colombia y su visión del futuro ameritan que sea conocida y entendida ahora mismo por los otros Basilianos en la Congregación entera.",
                "El presente trabajo provee un ejemplo escogido de esa iniciativa y energía con la que pueden contribuir los jóvenes Basilianos colombianos, no sólo para el desarrollo de la Iglesia en su propio país, sino también para el futuro de toda la Congregación de San Basilio.",
                "En el nombre de toda la Congregación agradezco a todos los que trabajaron en la elaboración de esta obra: nos ayuda a todos los que no somos colombianos a conocer algo de lo que Dios hizo allí, y lo que aún sigue haciendo por medio de las manos y de los sueños de nuestros hermanos."
            ),
            introductionText = listOf(
                "¿Por qué una historia de la Congregación de San Basilio en Colombia? Un punto de partida necesario es la urgencia de volver sobre su vida y quehacer a través de los años. La propuesta brotó después de que el Padre Francisco Amico compartiera sus vivencias con los novicios basilianos en Medellín.",
                "Se recopilaron datos a través de entrevistas hechas a quienes con su esfuerzo y pujanza han labrado este caminar, cartas cruzadas desde el inicio con algunos obispos y el aporte fundamental de la hermana Hermengarda Alves (secretaria general de la CLAR).",
                "Esta obra quiere ser un elemento que contribuya a la lectura del paso de Dios por nuestra historia en Colombia, y un medio para evaluar cómo ha sido nuestra respuesta a Dios Padre que quiso hacernos instrumentos de su amor en esta parte de su pueblo."
            ),
            chapters = listOf(
                ApuntesChapter(
                    id = "homenajes",
                    romanNumeral = "•",
                    title = "Homenaje a Fundadores y Patronos",
                    subtitle = "Annonay 1822 • Colombia 1987 • Santos Patronos",
                    years = "1822 - 1987",
                    summary = "Reconocimiento a los 10 fundadores franceses, los pioneros en Colombia y la oración a San Basilio Magno.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Los Diez Fundadores Franceses (Annonay 1822)",
                            content = "• P. Joseph Lapierre\n• P. Vincent Duret\n• P. Augustin Payan\n• P. François Polly\n• P. Pierre Tourvieille\n• P. Julien Tracol\n• P. André Fayolle\n• P. Henri Martinesche\n• P. Jean François Pages\n• Jean Antoine Vallon\n\n«Alabamos a nuestros padres quienes eran hombres comprometidos en su tiempo...»"
                        ),
                        ApuntesSection(
                            title = "Los Fundadores en Colombia (1987)",
                            content = "• P. Francisco Amico Ferrari, C.S.B.\n• P. Roberto Seguin Bartolo, C.S.B.\n• P. Mauricio Luke Restivo Bidet, C.S.B.\n• P. Rafael Ignacio Lopera Montoya, C.S.B.\n• Sr. Eduardo Cuadra Campos\n• Sr. Martín Escobedo Martínez"
                        ),
                        ApuntesSection(
                            title = "Santos Patronos y Oración a San Basilio",
                            content = "Patronos:\n1. San Basilio Magno\n2. San Francisco de Asís\n3. Santo Tomás de Aquino\n4. San Juan Bosco\n5. San José\n6. Santa María, Madre de Dios\n\nORACIÓN A SAN BASILIO:\n«Santo padre, Basilio, patrón de nuestra Congregación que tiene como apostolado la enseñanza cristiana, sé nuestro modelo en la enseñanza y apoyo en nuestro ministerio. Danos ese amor por la verdad que tú manifestaste en tu vida, para que seamos instrumentos al llevar a los demás a la verdad máxima. Ten presente a tu Congregación, que Tú mismo has cuidado desde su inicio. Amén.»"
                        )
                    )
                ),
                ApuntesChapter(
                    id = "cap_1",
                    romanNumeral = "I",
                    title = "Gestando la Idea de Fundación en Colombia",
                    subtitle = "Los primeros contactos: noviembre de 1985 a julio de 1988",
                    years = "1985 - 1988",
                    summary = "El discernimiento inicial del Superior General Ulisse Paré, el encuentro con la Hna. Hermengarda Alves y la invitación de Mons. Pedro Rubiano a Cali.",
                    sections = listOf(
                        ApuntesSection(
                            title = "El anhelo misionero en América Latina",
                            content = "El Padre Ulisse Paré, superior general en esos momentos, relata: «Estando aún el padre Hanrahan como superior general, él y yo hablábamos de la posibilidad de iniciar un segundo proyecto en América Latina. Tras evaluar proyectos en África (ISAID) que carecían de evangelización directa, surgió el llamado a Colombia. Francisco Amico y Rafael Ignacio Lopera viajaron a explorar diócesis necesitadas».",
                            quote = "Supongo que nuestro miedo principal tenía que ver con la soledad extrema y el aislamiento de los primeros fundadores... El otro miedo grande era la amenaza de la violencia.",
                            quoteAuthor = "P. Ulisse Paré, C.S.B."
                        ),
                        ApuntesSection(
                            title = "El puente providencial: Hna. Hermengarda Alves (CLAR)",
                            content = "En noviembre de 1985 el P. Francisco escribió a la CLAR (Bogotá). La hermana Hermengarda Alves, secretaria general, le brindó acogida fraternal y aconsejó comunicarse con varios obispos: Sincelejo, Cartagena, Barranquilla, Medellín y Cali. Las puertas se abrieron especialmente en Cali con Mons. Pedro Rubiano Sáenz."
                        ),
                        ApuntesSection(
                            title = "La decisión en favor de Cali",
                            content = "Mons. Pedro Rubiano escribió: «Ruego al Señor que la decisión del Consejo General se incline por la Fundación en Cali y le repito que aquí lo espero, que no solamente tiene este obispo amigo, sino también a un hermano en Cristo». En noviembre de 1986 el Consejo General en Toronto votó a favor de asumir la Parroquia Nuestra Señora de la Asunción en Cali."
                        )
                    )
                ),
                ApuntesChapter(
                    id = "cap_2",
                    romanNumeral = "II",
                    title = "El Arribo de los Basilianos a Cali",
                    subtitle = "Julio de 1987: de las primeras impresiones a la realidad de Andrés Sanín",
                    years = "1987",
                    summary = "La llegada de Francisco Amico, Roberto Seguin, Mauricio Restivo, Rafael Lopera y los primeros colaboradores a los barrios del oriente de Cali.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Primeras impresiones y el choque de la realidad",
                            content = "El P. Mauricio Restivo recuerda: «Lo primero que me impactó fue el calor, había mucho polvo, estaban quemando basura, muchos huecos en la autopista... Al llegar a la parroquia me impactaron los zancudos, el polvo y el calor. A la vez me impactó la gente: me parecían tan amables, tan abiertos y curiosos de mí».",
                            quote = "Si nos crucificaran, sería mucho mejor que vivir este momento tan difícil... De pronto a lo lejos, un hermoso paisaje: el volcán nevado del Huila. Dios les mostraba que aún en medio de tanta pobreza y violencia, era posible la belleza.",
                            quoteAuthor = "P. Roberto Seguin y P. Francisco Amico"
                        ),
                        ApuntesSection(
                            title = "Una casa en abandono transformada en hogar",
                            content = "La casa parroquial estaba desolada, sin loza, sin cocina, llena de goteras. La Hna. Ángela Betancur (Hijas de Nuestra Señora de las Misericordias) recuerda: «¡Los admiraba! El Padre Francisco tan elegante venir a esta mugre... Todos sin excepción, trapeador y escoba y le dieron un vuelco total a la casa. Se empezaba a notar el orden y la dedicación basiliana».",
                            quote = "Promover la unidad en medio de un pueblo dividido por la pobreza y la violencia.",
                            quoteAuthor = "Doña Adelaida Cabezas (Legión de María)"
                        )
                    )
                ),
                ApuntesChapter(
                    id = "cap_3",
                    romanNumeral = "III",
                    title = "Perfilando la Vivencia Comunitaria",
                    subtitle = "Criterios, costumbres y oración comunitaria en Colombia",
                    years = "1987 - 1997",
                    summary = "La vida litúrgica, la visita al Santísimo, el hábito basiliano, la oración de Taizé los domingos y la célebre Oración por la Vida en Común.",
                    sections = listOf(
                        ApuntesSection(
                            title = "De las Costumbres Basilianas en Colombia",
                            content = "1. Oraciones comunes: Laudes y Vísperas diarias; Completas al terminar el día cantadas en latín a la luz de una vela con la antífona mariana del tiempo litúrgico. A mediodía, visita al Santísimo Sacramento con salmo, Oración a San Basilio y «Bajo tu Amparo». Ángelus al mediodía para pedir vocaciones.\n2. Hábito basiliano para profesos en Colombia: Camisa color crema estilo basiliano, cruz de madera con el escudo de la Congregación y sandalias.\n3. Oración del domingo en la noche: Reunión comunitaria a las 9:00 p.m. para la adoración a Jesús, Pan de Vida, al estilo de Taizé (media hora, velas tenues, incienso, cántico de Simeón y Salve Regina)."
                        ),
                        ApuntesSection(
                            title = "Oración por la Vida en Común (Texto Completo)",
                            content = "«Señor, concédenos que en el amor seamos sinceros;\nque aborrezcamos el mal y cuidemos todo lo bueno;\nque en el amor entre hermanos nos demostremos cariño unos a otros;\nque en el respeto estimemos a los otros como más dignos;\nque en el cumplimiento del deber no seamos flojos;\nque en el espíritu seamos fervorosos;\nque tengamos esperanza y estemos alegres;\nque en las pruebas seamos pacientes;\nque con los necesitados compartamos con ellos;\nque con los que estén de paso seamos solícitos para recibirlos en casa;\n\nSeñor, que bendigamos a quienes nos persiguen; que bendigamos y no maldigamos;\nque nos alegremos con los que están alegres y lloremos con los que lloran;\nque vivamos en armonía con todos; que no confiemos en la propia sabiduría sino que procuremos hacer el bien delante de todos;\nque hagamos todo lo posible en cuanto a nosotros dependa, para vivir en paz con todos. Amén.»"
                        )
                    )
                ),
                ApuntesChapter(
                    id = "cap_4",
                    romanNumeral = "IV",
                    title = "Los Otros Hermanos que Fueron Llegando",
                    subtitle = "Vocaciones y misioneros que forjaron la presencia",
                    years = "1988 - 1996",
                    summary = "P. Francisco Serafín Mauricio, P. Lorenzo Moran, P. Kevin Anderson, P. Juan Malo, P. Felipe Platt y P. Vicente Thompson.",
                    sections = listOf(
                        ApuntesSection(
                            title = "P. Lorenzo Moran y P. Juan Malo",
                            content = "• P. Lorenzo Moran (1989-1991): Recordado con alegría por su trabajo con los más pobres en La Playa y sus trucos de magia que cautivaban a los niños.\n• P. Juan Malo (1992-1993): Maestro de inglés, acompañante de monaguillos y promotor de comunidades eclesiales en La Playa («el único lenguaje esencial es el amor»).\n• P. Felipe Platt (1991-1996): Gran estudioso e historiador, redactor del «Pequeño Esquema de Historia Basilianos 1822-2000».\n• P. Vicente Thompson (1995): Formador y cooperador en Medellín y Bogotá."
                        )
                    )
                ),
                ApuntesChapter(
                    id = "cap_5",
                    romanNumeral = "V",
                    title = "Desarrollando una Pastoral Integral",
                    subtitle = "CEBs, Ancianato, Centro Médico, Biblioteca e INSA",
                    years = "1989 - 1997",
                    summary = "Las obras sociales nacidas del carisma basiliano de educación y servicio a los más necesitados.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Las Comunidades Eclesiales de Base (CEBs)",
                            content = "Nacidas del censo parroquial y de visitas casa por casa en Siete de Agosto, Puerto Mallarino y Andrés Sanín. Familias reunidas los martes y miércoles para orar con la Biblia leída desde la realidad, compartir el pan y tejer lazos fraternos solidarios."
                        ),
                        ApuntesSection(
                            title = "El Ancianato «Cristo de la Paz» y Centro Médico San Basilio",
                            content = "• Ancianato «Cristo de la Paz» (Diciembre 1989): Iniciativa junto a las Misioneras de la Caridad de la Madre Teresa de Calcuta para albergar ancianos abandonados del oriente caleño.\n• Centro Médico San Basilio (1990): Atención médica, odontológica y psicológica para los sectores más humildes frente al templo parroquial.\n• Biblioteca Comunitaria y Centro Cultural Aldemar Rodríguez Carvajal (1997)."
                        ),
                        ApuntesSection(
                            title = "El Instituto Nuestra Señora de la Asunción (INSA)",
                            content = "Fundado en 1991 para dar educación de calidad con pedagogía liberadora y cristiana a los jóvenes y niños de escasos recursos. Con un Proyecto Educativo Institucional (PEI) centrado en la familia, valores humanos y compromiso social."
                        )
                    )
                ),
                ApuntesChapter(
                    id = "cap_8",
                    romanNumeral = "VIII",
                    title = "El Destino Pone a Prueba Nuestra Presencia",
                    subtitle = "El secuestro del P. Francisco Amico C.S.B. (15 de febrero de 1990)",
                    years = "1990",
                    summary = "El doloroso secuestro del párroco por un comando del ELN, la movilización de fe del pueblo caleño y su valiente regreso.",
                    sections = listOf(
                        ApuntesSection(
                            title = "El secuestro en Andrés Sanín",
                            content = "En la víspera de la cumbre antidrogas con George Bush en Cartagena, miembros armados del ELN secuestraron al P. Francisco Amico en su parroquia. La noticia conmocionó a Cali y a la Iglesia internacional. La comunidad entera se declaró en vigilia permanente."
                        ),
                        ApuntesSection(
                            title = "Marchas de silencio y liberación",
                            content = "El pueblo de Andrés Sanín y Puerto Mallarino realizó marchas de antorchas y cadenas de oración. Tras cinco días de cautiverio, el P. Francisco fue liberado la noche del 20 de febrero de 1990. Al ingresar al templo entre lágrimas y repiques de campana exclamó: «No me voy, me quedo con mi gente; esta parroquia cuenta con una comunidad trabajadora y luchadora que no puede dejarse, porque aquí está mi mundo y mi gente».",
                            quote = "No voy a abandonar a mis feligreses, me quedaré con la gente que quiero y me necesita.",
                            quoteAuthor = "P. Francisco Amico Ferrari, C.S.B."
                        )
                    )
                ),
                ApuntesChapter(
                    id = "cap_9",
                    romanNumeral = "IX",
                    title = "Aldemar Rodríguez Carvajal, Nuestro Mártir",
                    subtitle = "Testigo que supo articular compromiso cristiano con justicia social",
                    years = "1992",
                    summary = "La vida, entrega y martirio del catequista de 20 años en Semana Santa de 1992, y la célebre homilía de Francisco Amico.",
                    sections = listOf(
                        ApuntesSection(
                            title = "El joven catequista de la Asunción",
                            content = "Aldemar (nacido en Neiva en 1972) era catequista de confirmación y líder de derechos humanos. Comprometido con la opción por los empobrecidos, animaba las CEBs en La Playa y soñaba con una sociedad justa según el Evangelio."
                        ),
                        ApuntesSection(
                            title = "El martirio en Semana Santa de 1992",
                            content = "El Miércoles Santo de 1992 fue secuestrado, torturado y asesinado por escuadrones de la muerte. Su cuerpo fue velado en el templo de la Asunción entre el dolor y la esperanza pascual."
                        ),
                        ApuntesSection(
                            title = "Homilía de Francisco Amico C.S.B. (23 de abril de 1992)",
                            content = "«Aldemar fue catequista de esta comunidad donde se parte el pan. Iba semanalmente a La Playa a dar vida a los humildes, a reírse y bailar con ellos. Aldemar venía a la Eucaristía con frecuencia; recibía a Cristo para hacer los gestos de Cristo... ¿Será por eso que lo mataron?\n\nAldemar trabajaba con Derechos Humanos aquí en Cali, comprometido con la justicia y la paz. ¿Será por eso que lo mataron?\n\nAldemar, mártir de la Asunción, ruega por nosotros tus hermanos peregrinos, para que firmes caminemos y compartamos y así podamos un día gozar aquí en Colombia una fraternidad verdadera. Aldemar, amigo y compañero, maestro y mártir, ¡nunca te olvidaremos! Amén.»"
                        )
                    )
                ),
                ApuntesChapter(
                    id = "cap_10",
                    romanNumeral = "X",
                    title = "La Segunda Fundación: Medellín",
                    subtitle = "Noviciado San Basilio (1995) y Parroquia El Ecce Homo (1996)",
                    years = "1995 - 1996",
                    summary = "Apertura de la casa de formación en Barrio Prado y la parroquia en el humilde sector de Blanquizal.",
                    sections = listOf(
                        ApuntesSection(
                            title = "El Noviciado y la Parroquia El Ecce Homo",
                            content = "El 30 de julio de 1995 Mons. Héctor Rueda Hernández autorizó la erección canónica del Noviciado San Basilio en Medellín. Poco después, el 16 de septiembre de 1996, la Congregación asumió la Parroquia El Ecce Homo en Blanquizal, un sector montañoso y popular poblado por desplazados y campesinos."
                        )
                    )
                ),
                ApuntesChapter(
                    id = "cap_16",
                    romanNumeral = "XVI",
                    title = "Apéndice: La Pintura de San Basilio en Cali",
                    subtitle = "Simbología inculturada por Efraín Ferrer de la Torre",
                    years = "1997",
                    summary = "Explicación del óleo emblemático donde San Basilio viste atuendos típicos de Colombia y porta la bandera de la verdad y la justicia.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Simbología del San Basilio Caleño",
                            content = "Pintado por Efraín Ferrer en la Parroquia de la Asunción:\n• San Basilio viste carriel paisa al hombro izquierdo, sombrero vueltiao costeño y sandalias.\n• Porta la Biblia en su mano derecha y la cruz basiliana.\n• Al fondo: el cerro de las Tres Cruces de Cali, las casas humildes de Andrés Sanín y La Playa, la torre parroquial y el mapa de Colombia.\n• Representación de Aldemar Rodríguez con quena andina y alas ensangrentadas como ángel mártir de la justicia.\n• Lema: «Bonitatem, Disciplinam et Scientiam doce me»."
                        )
                    )
                )
            )
        )
    }
}
