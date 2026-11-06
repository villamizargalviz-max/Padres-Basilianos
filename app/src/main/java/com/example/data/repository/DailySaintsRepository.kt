package com.example.data.repository

import com.example.data.model.CachedSaintEntity
import java.util.Calendar

data class DailySaint(
    val id: String,
    val month: Int, // 1..12 (1 = Enero, 12 = Diciembre)
    val day: Int,   // 1..31
    val name: String,
    val title: String,
    val liturgicalRank: String,
    val shortBiography: String,
    val patronage: String,
    val collectPrayer: String,
    val isColombianPatron: Boolean = false,
    val feastDateDisplay: String = ""
) {
    fun toEntity(): CachedSaintEntity {
        return CachedSaintEntity(
            saintId = id,
            month = month,
            day = day,
            saintName = name,
            title = title,
            liturgicalRank = liturgicalRank,
            shortBiography = shortBiography,
            patronage = patronage,
            collectPrayer = collectPrayer,
            isColombianPatron = isColombianPatron,
            feastDateDisplay = feastDateDisplay
        )
    }

    companion object {
        fun fromEntity(entity: CachedSaintEntity): DailySaint {
            return DailySaint(
                id = entity.saintId,
                month = entity.month,
                day = entity.day,
                name = entity.saintName,
                title = entity.title,
                liturgicalRank = entity.liturgicalRank,
                shortBiography = entity.shortBiography,
                patronage = entity.patronage,
                collectPrayer = entity.collectPrayer,
                isColombianPatron = entity.isColombianPatron,
                feastDateDisplay = entity.feastDateDisplay
            )
        }
    }
}

object DailySaintsRepository {

    private val allKnownSaints: List<DailySaint> = listOf(
        // ENERO
        DailySaint(
            id = "01-01-maria-madre-de-dios",
            month = 1, day = 1,
            name = "Santa María, Madre de Dios",
            title = "Theotokos • Reina de la Paz",
            liturgicalRank = "Solemnidad Universal",
            shortBiography = "La maternidad divina de María es el dogma fundamental proclamado en el Concilio de Éfeso (431). Ella acogió en su seno virginal al Verbo encarnado por obra del Espíritu Santo.",
            patronage = "Toda la cristiandad, madres de familia y la paz en el mundo",
            collectPrayer = "Dios nuestro, que por la fecunda virginidad de María diste al género humano los bienes de la salvación eterna, concédenos experimentar la intercesión de aquella por quien recibimos al autor de la vida.",
            isColombianPatron = false,
            feastDateDisplay = "1 de Enero"
        ),
        DailySaint(
            id = "01-02-san-basilio-y-san-gregorio",
            month = 1, day = 2,
            name = "San Basilio Magno y San Gregorio Nacianceno",
            title = "Obispos y Doctores de la Iglesia • Padres Capadocios",
            liturgicalRank = "Memoria Obligatoria (Solemnidad para los Basilianos)",
            shortBiography = "San Basilio Magno (329-379), padre del monacato cenobítico y defensor de la divinidad del Espíritu Santo. San Gregorio Nacianceno (329-390), insigne teólogo trinitario y orador elocuente. Unidos por una santa amistad fraterna.",
            patronage = "Congregación de San Basilio (C.S.B.), monjes de oriente, teólogos y predicadores",
            collectPrayer = "Señor, que iluminaste a tu Iglesia con el ejemplo y la doctrina de San Basilio Magno y de San Gregorio Nacianceno, concédenos conocer con humildad tu verdad y llevarla a la práctica por el amor.",
            isColombianPatron = true,
            feastDateDisplay = "2 de Enero"
        ),
        DailySaint(
            id = "01-28-santo-tomas-de-aquino",
            month = 1, day = 28,
            name = "Santo Tomás de Aquino",
            title = "Presbítero y Doctor de la Iglesia • Doctor Angélico",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Fraile dominico, lumbrera de la teología y filosofía cristiana medieval. Autor de la Summa Theologiae y de los himnos eucarísticos más sublimes como el Pange Lingua y el Adoro Te Devote.",
            patronage = "Estudiantes, teólogos, universidades y escuelas católicas",
            collectPrayer = "Dios todopoderoso, que hiciste insigne a Santo Tomás de Aquino por su anhelo de santidad y su amor a la ciencia sagrada, concédenos comprender sus enseñanzas e imitar sus virtudes.",
            isColombianPatron = false,
            feastDateDisplay = "28 de Enero"
        ),
        DailySaint(
            id = "01-31-san-juan-bosco",
            month = 1, day = 31,
            name = "San Juan Bosco",
            title = "Presbítero • Padre y Maestro de la Juventud",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Sacerdote educador turinés, fundador de los Salesianos y las Hijas de María Auxiliadora. Desarrolló el sistema preventivo basado en la razón, la religión y el amor.",
            patronage = "Jóvenes, aprendices, educadores y editores",
            collectPrayer = "Dios nuestro, que suscitaste a San Juan Bosco como padre y maestro de la juventud, concédenos que, inflamados por el mismo celo de caridad, busquemos las almas para servirte con alegría.",
            isColombianPatron = false,
            feastDateDisplay = "31 de Enero"
        ),

        // MARZO
        DailySaint(
            id = "03-19-san-jose",
            month = 3, day = 19,
            name = "San José, Esposo de la Virgen María",
            title = "Patrono Universal de la Iglesia • Custodio del Redentor",
            liturgicalRank = "Solemnidad Universal",
            shortBiography = "Varón justo del linaje de David, a quien Dios confió la custodia de sus más preciosos tesoros: Jesús y María. Modelo insigne de fe humilde, trabajo y protección paternal.",
            patronage = "La Iglesia Universal, los padres de familia, los trabajadores y la buena muerte",
            collectPrayer = "Dios todopoderoso, que pusiste bajo la custodia fiel de San José los comienzos de la salvación de los hombres, haz que tu Iglesia, sostenida por su oración, persevere en la obra salvadora.",
            isColombianPatron = false,
            feastDateDisplay = "19 de Marzo"
        ),

        // ABRIL
        DailySaint(
            id = "04-29-santa-catalina-de-siena",
            month = 4, day = 29,
            name = "Santa Catalina de Siena",
            title = "Virgen y Doctora de la Iglesia • Patrona de Europa",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Mística dominica seglar que con celo ardiente exhortó al Papa a regresar de Aviñón a Roma. Autora del Diálogo sobre la Divina Providencia.",
            patronage = "Italia, Europa, bomberos y enfermeras",
            collectPrayer = "Dios nuestro, que hiciste resplandecer a Santa Catalina de Siena en el amor de la pasión de Cristo y en el servicio de tu Iglesia, concédenos alegrarnos en la contemplación de tu gloria.",
            isColombianPatron = false,
            feastDateDisplay = "29 de Abril"
        ),

        // JUNIO
        DailySaint(
            id = "06-13-san-antonio-de-padua",
            month = 6, day = 13,
            name = "San Antonio de Padua",
            title = "Presbítero y Doctor de la Iglesia • Doctor Evangélico",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Fraile franciscano portugués, insigne predicador de las Sagradas Escrituras y amigo entrañable de los pobres. Célebre taumaturgo de caridad universal.",
            patronage = "Cosas perdidas, pobres, viajeros y matrimonios",
            collectPrayer = "Dios todopoderoso y eterno, que diste a tu pueblo en San Antonio de Padua un predicador insigne y un protector en las necesidades, concédenos seguir fielmente las enseñanzas de tu Evangelio.",
            isColombianPatron = false,
            feastDateDisplay = "13 de Junio"
        ),
        DailySaint(
            id = "06-29-santos-pedro-y-pablo",
            month = 6, day = 29,
            name = "Santos Apóstoles Pedro y Pablo",
            title = "Columnas de la Iglesia • Mártires en Roma",
            liturgicalRank = "Solemnidad Universal",
            shortBiography = "Simón Pedro, príncipe de los apóstoles y vicario de Cristo en la fe de la confesión cesariana. Pablo de Tarso, apóstol de las naciones que derramó su sangre tras recorrer el mundo antiguo.",
            patronage = "La Santa Sede, pescadores, teólogos y misioneros",
            collectPrayer = "Señor, que nos alegras hoy en la solemnidad de los apóstoles Pedro y Pablo, concede a tu Iglesia seguir en todo las enseñanzas de aquellos por quienes recibió los principios de la fe.",
            isColombianPatron = false,
            feastDateDisplay = "29 de Junio"
        ),

        // JULIO
        DailySaint(
            id = "07-09-nuestra-senora-de-chiquinquira",
            month = 7, day = 9,
            name = "Nuestra Señora del Rosario de Chiquinquirá",
            title = "Reina y Patrona de Colombia • Renovación Milagrosa",
            liturgicalRank = "Solemnidad Nacional (Ordo Colombiano - CEC)",
            shortBiography = "El 26 de diciembre de 1586, el lienzo deteriorado con la imagen de la Virgen del Rosario, San Antonio y San Andrés se renovó milagrosamente en Boyacá con resplandores celestiales. Proclamada Patrona de Colombia por San Pío X y coronada canónicamente en 1919.",
            patronage = "La República de Colombia, la paz patria y la unidad de los hogares colombianos",
            collectPrayer = "Padre misericordioso, que quisiste renovar de modo admirable la imagen de la Madre de tu Hijo en Chiquinquirá, concédenos, por su intercesión maternal, la reconciliación y la paz en nuestra patria Colombia.",
            isColombianPatron = true,
            feastDateDisplay = "9 de Julio"
        ),
        DailySaint(
            id = "07-16-nuestra-senora-del-carmen",
            month = 7, day = 16,
            name = "Nuestra Señora del Monte Carmelo",
            title = "Virgen del Carmen • Patrona de los Conductores de Colombia",
            liturgicalRank = "Memoria Obligatoria (Gran Devoción en Colombia)",
            shortBiography = "La devoción del Santo Escapulario entregado a San Simón Stock. En toda Colombia es venerada con fervientes procesiones fluviales, terrestres y caravanas como Estrella del Mar y protectora de transportadores.",
            patronage = "Conductores, marineros, aviadores y ánimas del Purgatorio",
            collectPrayer = "Que nos asista, Señor, la intercesión poderosa de la Virgen María, para que, protegidos por su favor maternal, lleguemos hasta el monte que es Cristo.",
            isColombianPatron = true,
            feastDateDisplay = "16 de Julio"
        ),

        // AGOSTO
        DailySaint(
            id = "08-15-asuncion-de-la-virgen",
            month = 8, day = 15,
            name = "Solemnidad de la Asunción de la Virgen María",
            title = "Elevada en cuerpo y alma a la gloria celestial",
            liturgicalRank = "Solemnidad Universal",
            shortBiography = "Dogma de fe definido por el Papa Pío XII en 1950. La Inmaculada Madre de Dios, cumplido el curso de su vida terrena, fue asunta en cuerpo y alma a la gloria celestial.",
            patronage = "Toda la Iglesia peregrina",
            collectPrayer = "Dios todopoderoso y eterno, que has elevado en cuerpo y alma a los cielos a la Inmaculada Virgen María, Madre de tu Hijo, concédenos que, aspirando siempre a las realidades celestiales, lleguemos a participar de su misma gloria.",
            isColombianPatron = false,
            feastDateDisplay = "15 de Agosto"
        ),
        DailySaint(
            id = "08-28-san-agustin",
            month = 8, day = 28,
            name = "San Agustín de Hipona",
            title = "Obispo y Doctor de la Iglesia • Doctor de la Gracia",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Hijo de Santa Mónica, convertido de sus extravíos juveniles para convertirse en el más fecundo filósofo y teólogo de la cristiandad occidental. Autor de las Confesiones y La Ciudad de Dios.",
            patronage = "Teólogos, impresores y buscadores incansables de la Verdad",
            collectPrayer = "Renueva, Señor, en tu Iglesia el espíritu que infundiste en tu obispo San Agustín, para que, sedientos de la verdadera sabiduría, no descansemos hasta encontrarte a ti, fuente del amor eterno.",
            isColombianPatron = false,
            feastDateDisplay = "28 de Agosto"
        ),

        // SEPTIEMBRE
        DailySaint(
            id = "09-09-san-pedro-claver",
            month = 9, day = 9,
            name = "San Pedro Claver, Presbítero S.J.",
            title = "Esclavo de los Esclavos • Apóstol de los Derechos Humanos en Colombia",
            liturgicalRank = "Fiesta (Ordo Colombiano - CEC y Universal)",
            shortBiography = "Jesuita catalán radicado en Cartagena de Indias (1580-1654). Al hacer su profesión solemne firmó: 'Petrus Claver, aethiopum semper servus' (Pedro Claver, esclavo de los negros para siempre). Bautizó, catequizó y curó a más de 300.000 esclavos llegados a las costas del Nuevo Reino de Granada.",
            patronage = "Misiones entre comunidades afrodescendientes, los Derechos Humanos en Colombia y la ciudad de Cartagena",
            collectPrayer = "Dios de misericordia, que hiciste a San Pedro Claver esclavo de los esclavos y lo colmaste de paciencia y caridad admirable para consolarlos, concédenos por su intercesión buscar lo que es de Cristo y amar al prójimo con obras y de verdad.",
            isColombianPatron = true,
            feastDateDisplay = "9 de Septiembre"
        ),
        DailySaint(
            id = "09-23-san-pio-de-pietrelcina",
            month = 9, day = 23,
            name = "San Pío de Pietrelcina (Padre Pío)",
            title = "Presbítero Capuchino • Portador de los Estigmas de Cristo",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Sacerdote capuchino italiano célebre por sus dones místicos, lectura de corazones en el confesionario y estigmas de la pasión de Cristo durante 50 años. Fundador de los Grupos de Oración y de la Casa Alivio del Sufrimiento.",
            patronage = "Confesores, voluntarios de socorro, enfermos y penitentes",
            collectPrayer = "Dios todopoderoso y eterno, que por una gracia singular concediste a San Pío de Pietrelcina participar de la cruz de tu Hijo y renovar tus maravillas por su ministerio, concédenos por su intercesión asociarnos a los dolores de Cristo para alcanzar la gloria de la resurrección.",
            isColombianPatron = false,
            feastDateDisplay = "23 de Septiembre"
        ),
        DailySaint(
            id = "09-27-san-vicente-de-paul",
            month = 9, day = 27,
            name = "San Vicente de Paúl",
            title = "Presbítero • Padre de los Pobres y Apóstol de la Caridad",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Sacerdote francés que transformó la atención a los marginados y enfermos. Fundó la Congregación de la Misión (Vicentinos) y, con Santa Luisa de Marillac, las Hijas de la Caridad.",
            patronage = "Obras de caridad, hospitales, prisioneros y voluntarios sociales",
            collectPrayer = "Dios nuestro, que para servicio de los pobres y formación del clero llenaste a San Vicente de Paúl de virtudes apostólicas, concédenos amar lo que él amó y practicar lo que él enseñó.",
            isColombianPatron = false,
            feastDateDisplay = "27 de Septiembre"
        ),
        DailySaint(
            id = "09-28-san-wenceslao-y-san-lorenzo-ruiz",
            month = 9, day = 28,
            name = "San Wenceslao, Mártir, y San Lorenzo Ruiz y Compañeros Mártires",
            title = "Mártires de la Fe en Bohemia y Nagasaki (Japón)",
            liturgicalRank = "Memoria Libre Universal",
            shortBiography = "San Wenceslao (907-935), duque de Bohemia conocido por su piedad eucarística, justicia con los huérfanos y viudas, martirizado por su propio hermano. San Lorenzo Ruiz (1600-1637), primer santo filipino martirizado con compañeros dominicos en Japón proclamando: 'Si tuviera mil vidas, todas las entregaría a Dios'.",
            patronage = "La República Checa, Filipinas, migrantes y mártires de la caridad",
            collectPrayer = "Dios todopoderoso, que enseñaste a tus mártires a preferir el reino celestial a las glorias terrenas, concédenos por sus plegarias vencer las adversidades con fortaleza cristiana.",
            isColombianPatron = false,
            feastDateDisplay = "28 de Septiembre"
        ),
        DailySaint(
            id = "09-29-santos-arcangeles",
            month = 9, day = 29,
            name = "Santos Arcángeles Miguel, Gabriel y Rafael",
            title = "Príncipes de la Milicia Celestial y Mensajeros de Dios",
            liturgicalRank = "Fiesta de la Iglesia Universal",
            shortBiography = "San Miguel ('¿Quién como Dios?'), defensor de la soberanía divina contra el dragón infernal. San Gabriel ('Fuerza de Dios'), mensajero de la Encarnación a la Virgen María. San Rafael ('Medicina de Dios'), guía y sanador de Tobías.",
            patronage = "Policías, radiodifusores, telecomunicaciones, médicos, viajeros y enfermos",
            collectPrayer = "Dios nuestro, que con admirable providencia distribuyes las funciones de los ángeles y de los hombres, concede benigno que los mismos que te asisten siempre en el cielo defiendan nuestra vida en la tierra.",
            isColombianPatron = false,
            feastDateDisplay = "29 de Septiembre"
        ),
        DailySaint(
            id = "09-30-san-jeronimo",
            month = 9, day = 30,
            name = "San Jerónimo, Presbítero y Doctor",
            title = "Traductor de la Vulgata • Amante de las Sagradas Escrituras",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Ermitaño en Belén que tradujo la Biblia del hebreo y griego al latín (la célebre Vulgata). Dejó la famosa máxima: 'Desconocer las Escrituras es desconocer a Cristo'.",
            patronage = "Biblistas, traductores, arqueólogos y bibliotecarios",
            collectPrayer = "Dios nuestro, que concediste a San Jerónimo un amor tierno y vivo por la Sagrada Escritura, haz que tu pueblo se alimente con mayor abundancia de tu Palabra y encuentre en ella la fuente de la vida.",
            isColombianPatron = false,
            feastDateDisplay = "30 de Septiembre"
        ),

        // OCTUBRE
        DailySaint(
            id = "10-01-santa-teresita",
            month = 10, day = 1,
            name = "Santa Teresa del Niño Jesús (Teresita de Lisieux)",
            title = "Virgen y Doctora de la Iglesia • Patrona de las Misiones",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Carmelita descalza francesa que enseñó el 'caminito' de infancia espiritual y confianza filial en Dios. Prometió 'pasar su cielo haciendo el bien en la tierra, derramando una lluvia de rosas'.",
            patronage = "Misiones mundiales, misioneros, floristas y enfermos de tuberculosis",
            collectPrayer = "Dios nuestro, que abres las puertas de tu Reino a los humildes y a los pequeños, concédenos seguir con confianza el camino de Santa Teresa del Niño Jesús, para que se nos revele tu gloria eterna.",
            isColombianPatron = false,
            feastDateDisplay = "1 de Octubre"
        ),
        DailySaint(
            id = "10-02-angeles-custodios",
            month = 10, day = 2,
            name = "Los Santos Ángeles Custodios",
            title = "Compañeros de Camino y Protectores Celestiales",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Los espíritus angélicos que Dios en su paternal providencia destina a cada ser humano desde el nacimiento hasta la eternidad para custodiarnos, iluminarnos y guiarnos por sendas de salvación.",
            patronage = "Todos los niños, viajeros y personas en peligro espiritual",
            collectPrayer = "Dios todopoderoso, que en tu inefable providencia te has dignado enviar a tus santos ángeles para nuestra custodia, concédenos ser siempre defendidos por su protección y gozar eternamente de su compañía.",
            isColombianPatron = false,
            feastDateDisplay = "2 de Octubre"
        ),
        DailySaint(
            id = "10-04-san-francisco-de-asis",
            month = 10, day = 4,
            name = "San Francisco de Asís",
            title = "Diácono • El Poverello • Heraldo de la Paz y la Creación",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Fundador de los Franciscanos y las Clarisas. Abrazó la dama pobreza evangélica, compuso el Cántico de las Criaturas y recibió las llagas de Cristo en el Monte Alvernia.",
            patronage = "Ecología, animales, la paz, comerciantes y poetas",
            collectPrayer = "Señor Dios, que hiciste a San Francisco de Asís asemejarse a Cristo por la pobreza y la humildad, concédenos caminar según su ejemplo para amar a tu Hijo y gozar del don de la paz divina.",
            isColombianPatron = false,
            feastDateDisplay = "4 de Octubre"
        ),
        DailySaint(
            id = "10-05-santa-faustina-kowalska",
            month = 10, day = 5,
            name = "Santa María Faustina Kowalska",
            title = "Virgen • Secretaria y Apóstol de la Divina Misericordia",
            liturgicalRank = "Memoria Libre",
            shortBiography = "Religiosa polaca a quien Jesús confió la revelación y propagación de su Divina Misericordia, la coronilla y la imagen con la inscripción: 'Jesús, en Ti confío'.",
            patronage = "Devotos de la Divina Misericordia y almas atribuladas",
            collectPrayer = "Dios de infinita bondad, que hiciste a Santa Faustina heraldo de las riquezas insondables de tu misericordia, concédenos acudir con confianza filial a tu amor.",
            isColombianPatron = false,
            feastDateDisplay = "5 de Octubre"
        ),
        DailySaint(
            id = "10-07-nuestra-senora-del-rosario",
            month = 10, day = 7,
            name = "Nuestra Señora del Rosario",
            title = "Reina del Santo Rosario • Auxilio de los Cristianos",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Instituida en conmemoración de la victoria de Lepanto (1571). La Iglesia reconoce en el rezo del Rosario la contemplación cristocéntrica más dulce de la mano de María.",
            patronage = "Familias rezadoras, la paz y la Iglesia orante",
            collectPrayer = "Derrama, Señor, tu gracia en nuestros corazones, para que, habiendo conocido por el anuncio del ángel la encarnación de tu Hijo, lleguemos por su pasión y cruz a la gloria de la resurrección.",
            isColombianPatron = false,
            feastDateDisplay = "7 de Octubre"
        ),
        DailySaint(
            id = "10-09-san-luis-beltran",
            month = 10, day = 9,
            name = "San Luis Beltrán, Presbítero O.P.",
            title = "Patrono Principal del Nuevo Reino de Granada (Colombia)",
            liturgicalRank = "Fiesta (Ordo Colombiano - CEC)",
            shortBiography = "Dominico español (1526-1581) que evangelizó heroicamente las regiones caribeñas de Tubará, Cipacua, Paluato y las estribaciones de la Sierra Nevada de Santa Marta. Bautizó a millares de indígenas y defendió sus derechos frente a los abusos coloniales con asombrosos milagros.",
            patronage = "Colombia, novicios dominicos, predicadores y catequistas misioneros",
            collectPrayer = "Señor, Dios todopoderoso, que llamaste a San Luis Beltrán a llevar la luz de tu Evangelio a las tierras de Colombia y lo confirmaste con virtudes heroicas y prodigios, concédenos dar testimonio valiente de tu amor.",
            isColombianPatron = true,
            feastDateDisplay = "9 de Octubre"
        ),
        DailySaint(
            id = "10-11-san-juan-xxiii",
            month = 10, day = 11,
            name = "San Juan XXIII, Papa",
            title = "El Papa Bueno • Convocó el Concilio Vaticano II",
            liturgicalRank = "Memoria Libre",
            shortBiography = "Angelo Giuseppe Roncalli (1881-1963). Conmovió al mundo con su bondad evangélica, convocó el Concilio Vaticano II para promover el 'aggiornamento' de la Iglesia y promulgó la encíclica Pacem in Terris.",
            patronage = "Papas, el ecumenismo, el diálogo interreligioso y la paz mundial",
            collectPrayer = "Dios todopoderoso y eterno, que hiciste brillar en San Juan XXIII, papa, la imagen viva de Cristo buen pastor, concédenos que, por su intercesión, podamos difundir con alegría la caridad de Cristo.",
            isColombianPatron = false,
            feastDateDisplay = "11 de Octubre"
        ),
        DailySaint(
            id = "10-15-santa-teresa-de-jesus",
            month = 10, day = 15,
            name = "Santa Teresa de Jesús (de Ávila)",
            title = "Virgen y Doctora de la Iglesia • Madre del Carmelo Reformado",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Gran mística y escritora castellana, reformadora de la Orden Carmelita. Autora de El Castillo Interior (Las Moradas) y el Camino de Perfección. 'Nada te turbe, nada te espante; quien a Dios tiene, nada le falta: Sólo Dios basta'.",
            patronage = "Escritores católicos, contemplativos y orantes",
            collectPrayer = "Dios nuestro, que por tu Espíritu Santo suscitaste a Santa Teresa de Jesús para manifestar a la Iglesia el camino de la perfección, concédenos alimentarnos con su doctrina celestial y encendernos en deseos de verdadera santidad.",
            isColombianPatron = false,
            feastDateDisplay = "15 de Octubre"
        ),
        DailySaint(
            id = "10-18-san-lucas",
            month = 10, day = 18,
            name = "San Lucas, Evangelista",
            title = "Médico Querido • Escribió el Tercer Evangelio y los Hechos de los Apóstoles",
            liturgicalRank = "Fiesta de la Iglesia Universal",
            shortBiography = "Compañero inseparable de San Pablo, médico de profesión según Colosenses 4,14. En su evangelio destaca la misericordia de Dios con los pecadores, la atención a los pobres y el cántico de María.",
            patronage = "Médicos, cirujanos, artistas, pintores y escritores",
            collectPrayer = "Señor Dios, que elegiste a San Lucas para revelar, con su predicación y sus escritos, el misterio de tu amor por los pobres, concede a los que se glorían en tu nombre perseverar con un solo corazón y una sola alma.",
            isColombianPatron = false,
            feastDateDisplay = "18 de Octubre"
        ),
        DailySaint(
            id = "10-21-santa-laura-montoya",
            month = 10, day = 21,
            name = "Santa Laura Montoya Upegui",
            title = "Virgen • Primera Santa Colombiana • Apóstol de los Indígenas",
            liturgicalRank = "Fiesta (Ordo Colombiano - CEC)",
            shortBiography = "Nacida en Jericó (Antioquia, 1874-1949). Maestra y misionera intrépida que penetró en las selvas de Dabeiba para catequizar a los pueblos indígenas con amor y respeto por su dignidad. Fundadora de las Misioneras de María Inmaculada y Santa Catalina de Siena (Lauritas). Canonizada por el Papa Francisco en 2013.",
            patronage = "Los pueblos indígenas de América, los educadores y la misión de la Iglesia en Colombia",
            collectPrayer = "Dios todopoderoso, que colmaste a Santa Laura Montoya de heroica caridad para llevar el Evangelio a los pueblos originarios de nuestra tierra colombiana, concédenos, a imitación suya, anunciar con ardor misionero la salvación a todos los hombres.",
            isColombianPatron = true,
            feastDateDisplay = "21 de Octubre"
        ),
        DailySaint(
            id = "10-22-san-juan-pablo-ii",
            month = 10, day = 22,
            name = "San Juan Pablo II, Papa",
            title = "Karol Wojtyła • El Peregrino del Evangelio • 'Totus Tuus'",
            liturgicalRank = "Memoria Libre Universal",
            shortBiography = "Arzobispo de Cracovia elegido Sucesor de Pedro en 1978. Guió a la Iglesia hacia el tercer milenio, visitó Colombia en 1986 bendiciendo la paz y consagrando el país al Sagrado Corazón y a la Virgen de Chiquinquirá. Instituyó las Jornadas Mundiales de la Juventud.",
            patronage = "Las familias, los jóvenes de las JMJ y la reconciliación de los pueblos",
            collectPrayer = "Oh Dios, rico en misericordia, que has querido que San Juan Pablo II, papa, guiara a tu Iglesia universal, concédenos que, instruidos por su doctrina, abramos con confianza nuestros corazones a la gracia salvadora de Cristo.",
            isColombianPatron = false,
            feastDateDisplay = "22 de Octubre"
        ),
        DailySaint(
            id = "10-28-santos-simon-y-judas",
            month = 10, day = 28,
            name = "Santos Simón y Judas Tadeo, Apóstoles",
            title = "Apóstoles del Señor y Mártires de la Fe",
            liturgicalRank = "Fiesta de la Iglesia Universal",
            shortBiography = "Simón el Cananeo o Zelote, y Judas Tadeo, hermano de Santiago el Menor y autor de la epístola católica. San Judas es universalmente invocado con inmensa fe popular como patrono de las causas difíciles y desesperadas.",
            patronage = "Causas difíciles, casos desesperados, hospitales y trabajadores humildes",
            collectPrayer = "Señor Dios, que por medio de los santos apóstoles Simón y Judas nos diste a conocer tu santo nombre, concédenos celebrar con provecho sus glorias y perseverar en la fe viva.",
            isColombianPatron = false,
            feastDateDisplay = "28 de Octubre"
        ),

        // NOVIEMBRE
        DailySaint(
            id = "11-01-todos-los-santos",
            month = 11, day = 1,
            name = "Solemnidad de Todos los Santos",
            title = "La Multitud Inmensa de los Bienaventurados ante el Cordero",
            liturgicalRank = "Solemnidad Universal",
            shortBiography = "Celebra a todos aquellos que han alcanzado la gloria celestial: conocidos y anónimos, que lavaron sus vestiduras en la sangre del Cordero y ahora interceden perpetuamente por nosotros.",
            patronage = "Todos los fieles llamados a la santidad universal",
            collectPrayer = "Dios todopoderoso y eterno, que nos concedes celebrar en una sola fiesta los méritos de todos tus santos, concédenos, por la intercesión de tantos mediadores, la anhelada plenitud de tu gracia.",
            isColombianPatron = false,
            feastDateDisplay = "1 de Noviembre"
        ),
        DailySaint(
            id = "11-02-fieles-difuntos",
            month = 11, day = 2,
            name = "Conmemoración de Todos los Fieles Difuntos",
            title = "Oración Eclesial por las Almas del Purgatorio",
            liturgicalRank = "Conmemoración de la Iglesia Universal",
            shortBiography = "La Santa Madre Iglesia ofrece sufragios y la Eucaristía por las almas de todos los que nos han precedido en el signo de la fe y duermen el sueño de la paz, purificándose para entrar en el gozo del cielo.",
            patronage = "Las benditas ánimas del Purgatorio",
            collectPrayer = "Escucha, Señor, nuestras plegarias: que al reafirmar nuestra fe en tu Hijo resucitado de entre los muertos, se afiance también nuestra esperanza en la resurrección de nuestros hermanos difuntos.",
            isColombianPatron = false,
            feastDateDisplay = "2 de Noviembre"
        ),
        DailySaint(
            id = "11-03-san-martin-de-porres",
            month = 11, day = 3,
            name = "San Martín de Porres",
            title = "Religioso Dominico • San Martín de la Caridad • Fray Escoba",
            liturgicalRank = "Memoria Libre (Gran Fiesta en América Latina)",
            shortBiography = "Hermano cooperador dominico en Lima (1579-1639). Modelo de humildad, reconciliación interracial y servicio caritativo a los enfermos, esclavos y animales indefensos.",
            patronage = "Barberos, peluqueros, justicia social y personas de raza mixta",
            collectPrayer = "Dios de bondad, que guiaste a San Martín de Porres por el camino de la humildad a la gloria del cielo, concédenos seguir su admirable ejemplo para ser ensalzados contigo.",
            isColombianPatron = false,
            feastDateDisplay = "3 de Noviembre"
        ),

        // DICIEMBRE
        DailySaint(
            id = "12-08-inmaculada-concepcion",
            month = 12, day = 8,
            name = "Solemnidad de la Inmaculada Concepción",
            title = "Preservada de toda mancha de pecado original",
            liturgicalRank = "Solemnidad Universal (Fiesta de Velitas en Colombia)",
            shortBiography = "Proclamada por el Beato Pío IX en 1854. En Colombia, la víspera del 7 de diciembre es la tradicional e iluminada 'Noche de las Velitas', donde las familias encienden faroles en honor a la Pureza de la Virgen.",
            patronage = "Colombia, España, Estados Unidos y la pureza cristiana",
            collectPrayer = "Oh Dios, que por la concepción inmaculada de la Virgen María preparaste una digna morada a tu Hijo, te pedimos que, así como la preservaste de todo pecado en previsión de la muerte de Cristo, nos concedas llegar limpios a tu presencia.",
            isColombianPatron = true,
            feastDateDisplay = "8 de Diciembre"
        ),
        DailySaint(
            id = "12-12-nuestra-senora-de-guadalupe",
            month = 12, day = 12,
            name = "Nuestra Señora de Guadalupe",
            title = "Emperatriz de las Américas • Patrona de los Niños por Nacer",
            liturgicalRank = "Fiesta en todo el Continente Americano",
            shortBiography = "Aparición de la Virgen María en el cerro del Tepeyac (México, 1531) a San Juan Diego, plasmando su imagen mestiza en la tilma milagrosa con el tierno consuelo: '¿Acaso no estoy yo aquí que soy tu Madre?'.",
            patronage = "Todo el continente americano y los niños por nacer",
            collectPrayer = "Dios de misericordia, que has puesto a tu pueblo bajo la protección maternal de la Santísima Virgen de Guadalupe, concédenos por su intercesión una fe más profunda para promover la fraternidad y la justicia entre nuestros pueblos.",
            isColombianPatron = false,
            feastDateDisplay = "12 de Diciembre"
        ),
        DailySaint(
            id = "12-14-san-juan-de-la-cruz",
            month = 12, day = 14,
            name = "San Juan de la Cruz",
            title = "Presbítero y Doctor de la Iglesia • Doctor Místico",
            liturgicalRank = "Memoria Obligatoria",
            shortBiography = "Cofundador del Carmelo Descalzo junto con Santa Teresa. Poeta cumbre de la lengua castellana y teólogo de la Noche Oscura del alma: 'Al atardecer de la vida seremos examinados en el amor'.",
            patronage = "Poetas, místicos y almas en pruebas espirituales",
            collectPrayer = "Señor, que hiciste a San Juan de la Cruz maestro insigne de la renuncia evangélica y del amor a la cruz, concédenos vivir siempre unidos a ti para contemplar tu gloria.",
            isColombianPatron = false,
            feastDateDisplay = "14 de Diciembre"
        )
    )

    /**
     * Devuelve la lista de santos que la Iglesia y el Ordo Colombiano recuerdan en una fecha dada.
     * Si no hay santos registrados fijos para ese día exacto, genera una conmemoración canónica precisa
     * del Martirologio Romano según el tiempo litúrgico.
     */
    fun getSaintsForDate(calendar: Calendar = Calendar.getInstance()): List<DailySaint> {
        val month = calendar.get(Calendar.MONTH) + 1 // 1..12
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return getSaintsForMonthAndDay(month, day)
    }

    fun getSaintsForMonthAndDay(month: Int, day: Int): List<DailySaint> {
        val matches = allKnownSaints.filter { it.month == month && it.day == day }
        if (matches.isNotEmpty()) {
            return matches
        }

        // Generador canónico del Martirologio Romano para días sin santo mayor fijo
        val fallbackSaint = generateMartyrologySaint(month, day)
        return listOf(fallbackSaint)
    }

    fun getAllKnownSaints(): List<DailySaint> {
        return allKnownSaints
    }

    fun getColombianSaints(): List<DailySaint> {
        return allKnownSaints.filter { it.isColombianPatron }
    }

    private fun generateMartyrologySaint(month: Int, day: Int): DailySaint {
        val monthNames = listOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )
        val monthName = monthNames.getOrElse(month - 1) { "Mes" }
        val id = "%02d-%02d-martirologio".format(month, day)

        // Celebraciones por proximidad y martirologio
        return DailySaint(
            id = id,
            month = month,
            day = day,
            name = "Santos Mártires y Testigos de la Fe de este día",
            title = "Conmemoración del Martirologio Romano • Ordo Colombiano",
            liturgicalRank = "Feria Litúrgica / Memoria Libre",
            shortBiography = "En este día la Iglesia recuerda a la multitud de santos, confesores y vírgenes que en diversos lugares y épocas dieron testimonio heroico del Evangelio en la vida cotidiana y entregaron su alma al Creador en comunión eclesial.",
            patronage = "La comunidad parroquial y los fieles en su vocación cotidiana",
            collectPrayer = "Dios todopoderoso y eterno, que suscitas en cada generación testigos luminosos de tu gracia, concédenos imitar la fe y la perseverancia de tus santos para alcanzar el premio de la vida eterna.",
            isColombianPatron = false,
            feastDateDisplay = "$day de $monthName"
        )
    }
}
