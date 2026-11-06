package com.example.data.repository

data class PoliticaSection(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val articles: List<PoliticaArticle>
)

data class PoliticaArticle(
    val number: String,
    val title: String,
    val content: String,
    val highlights: List<String> = emptyList()
)

object Politicas2026Data {
    val documentTitle = "Políticas y Procedimientos sobre la Conducta Sexual Inapropiada"
    val documentSubtitle = "Congregación de San Basilio (Padres Basilianos - Toronto 2026)"
    val documentEdition = "Sexta Revisión 2025 - Vigencia 2026 • Estándares de Acreditación Praesidium"
    val curiaAddress = "Los Padres Basilianos de Toronto, 146 Davenport Road, Toronto, Ontario M5R 1J2, Canadá"

    val sections: List<PoliticaSection> = listOf(
        PoliticaSection(
            id = "parte_1_politicas",
            title = "I. Políticas Fundamentales",
            subtitle = "Alcance, Intención y Definición de Conductas Inapropiadas",
            category = "Normativa General",
            articles = listOf(
                PoliticaArticle(
                    number = "1.0",
                    title = "Alcance e Intención de estas Políticas",
                    content = "La conducta sexual inapropiada es una grave violación de la justicia y la caridad que causa un daño grave. Nunca es un comportamiento aceptable. Cuando es perpetrada por religiosos profesos, sacerdotes u otros ministros religiosos, puede causar graves daños espirituales y psicológicos, alejar a las víctimas y a sus familias del mensaje de Jesucristo y de la Iglesia, y causar un grave escándalo a otros.\n\nEste documento refleja la intención de la Congregación de San Basilio de cumplir con todas las leyes civiles, el Código de Derecho Canónico y el Modo de Vida Basiliano (documento constitucional). Se aplica a miembros profesos, novicios y empleados de la Congregación en Canadá, Colombia, Francia, México y Estados Unidos.",
                    highlights = listOf(
                        "La principal preocupación siempre será la víctima.",
                        "Se aplica a miembros profesos, novicios y colaboradores.",
                        "Coordinado con los Estándares de Acreditación del Praesidium."
                    )
                ),
                PoliticaArticle(
                    number = "2.1",
                    title = "Abuso Sexual de Menores",
                    content = "Implicación sexual con una persona menor de 18 años según la jurisdicción civil o canónica. La posesión, descarga, distribución o visualización de material de abuso sexual infantil y/o representaciones explícitas se considera jurídicamente y canónicamente abuso sexual de un menor, conforme a lo establecido por la Santa Sede (Dicasterio para la Doctrina de la Fe).",
                    highlights = listOf("Tolerancia cero: expulsión inmediata de formación y suspensión.")
                ),
                PoliticaArticle(
                    number = "2.2",
                    title = "Explotación Sexual y Abuso de Poder",
                    content = "Contacto sexual con un adulto vulnerable o con cualquier persona que reciba atención pastoral o servicios educativos. Los basilianos en funciones pastorales, educativas o de consejería ostentan un desequilibrio de poder inherente. Cualquier relación sexual en este contexto se considera explotación de la confianza y traición al ministerio sagrado, incluso si hubo aparente consentimiento.",
                    highlights = listOf("El desequilibrio de poder anula la legitimidad del consentimiento en contexto pastoral.")
                ),
                PoliticaArticle(
                    number = "2.3",
                    title = "Acoso Sexual",
                    content = "Conducta o lenguaje sexualizado no deseado, insinuaciones físicas o verbales, solicitudes de favores o creación de un ambiente hostil, intimidatorio u ofensivo en ámbitos laborales, educativos o residenciales comunitarios.",
                    highlights = listOf("Prohibición terminante en obras educativas, parroquias y casas religiosas.")
                ),
                PoliticaArticle(
                    number = "2.4",
                    title = "Grooming (Conductas de Acicalamiento / Seducción)",
                    content = "Patrón observable de conductas destinadas a establecer una intimidad inapropiada con un menor o adulto vulnerable: favoritismos injustificados, regalos excesivos, viajes privados, mensajes secretos y creación de un vínculo de dependencia emocional. Debe ser confrontado de inmediato porque es en sí mismo inapropiado y lesivo.",
                    highlights = listOf("Prohibido el trato preferencial aislado o regalos secretos a menores.")
                ),
                PoliticaArticle(
                    number = "2.5",
                    title = "Violaciones de Límites (Límites Ministeriales)",
                    content = "Comportamientos que transgreden los límites físicos, emocionales, psicológicos o espirituales en relaciones pastorales y profesionales. Debe gestionarse conforme al Protocolo S10 para responder a violaciones de límites.",
                    highlights = listOf("Vigilancia comunitaria fraterna y rendición de cuentas obligatoria.")
                ),
                PoliticaArticle(
                    number = "2.6",
                    title = "Mala Conducta Pública y Voto de Castidad",
                    content = "Acciones lascivas, exhibicionismo o conductas contrarias al celibato consagrado. Por su promesa evangélica ante Dios (Modo de Vida Basiliano arts. 9-13), el religioso no puede considerar jamás las actividades sexuales como compatibles con su consagración.",
                    highlights = listOf("La castidad es testimonio sagrado de las cosas de Dios.")
                ),
                PoliticaArticle(
                    number = "3.0",
                    title = "Prioridad Absoluta de la Víctima y Denuncia Obligatoria",
                    content = "La víctima menor de edad y su familia tienen derecho a una respuesta inmediata, compasiva y pastoralmente solidaria. Todo basiliano que reciba una denuncia o tenga motivos razonables de sospecha DEBE notificar inmediatamente a las autoridades civiles competentes y al Gestor de Casos. Ningún miembro puede escudarse en que la información es 'rumor' o carece de 'primera mano'.",
                    highlights = listOf("Obligación civil y moral ineludible de denuncia inmediata ante la justicia.")
                )
            )
        ),
        PoliticaSection(
            id = "parte_2_procedimientos",
            title = "II. Procedimientos e Investigación",
            subtitle = "Estructura Operativa: Gestor de Casos, Promotor de Justicia y Junta de Revisión",
            category = "Protocolos de Acción",
            articles = listOf(
                PoliticaArticle(
                    number = "1.1",
                    title = "Actores Institucionales",
                    content = "El sistema de respuesta cuenta con roles definidos:\n• Superior General y Consejo General\n• Gestor de Casos (Case Manager)\n• Promotor de la Justicia (investigador basiliano o externo independiente)\n• Junta de Revisión Basiliana (RB - Review Board organizada con CMSM y Praesidium)\n• Obispos diocesanos del lugar del hecho y de residencia actual\n• Director de Comunicaciones oficial (portavoz único autorizado).",
                    highlights = listOf("Estructura colegiada, transparente e interdisciplinar.")
                ),
                PoliticaArticle(
                    number = "2.1",
                    title = "Secuencia de Respuesta ante una Queja",
                    content = "1. Recepción de la queja por el Gestor de Casos o Superior General.\n2. El Gestor de Casos instruye formalmente al Promotor de Justicia.\n3. Investigación pronta y rigurosa por el Promotor de Justicia.\n4. Informe y recomendaciones elevados a la Junta de Revisión (RB).\n5. Asesoría de la Junta de Revisión al Superior General.\n6. Ejecución canónica y civil de las determinaciones del Superior General.",
                    highlights = listOf("Notificación civil en menos de 30 días y cooperación plena con la ley.")
                ),
                PoliticaArticle(
                    number = "2.4",
                    title = "Atención Terapéutica y Pastoral a la Víctima",
                    content = "La Congregación ofrece servicios profesionales y certificados de asesoramiento psicológico y sanación informada en trauma para la víctima y sus familiares directos, con cobertura económica inicial de al menos seis meses renovables según evolución clínica.",
                    highlights = listOf("Acompañamiento terapéutico integral sin admisión jurídica previa.")
                ),
                PoliticaArticle(
                    number = "3.4",
                    title = "Baja Administrativa y Remisión al Dicasterio en Roma",
                    content = "Ante una acusación verosímil que involucre a menores, el religioso es separado de inmediato de todo ministerio pastoral o educativo ('baja administrativa preventiva'). Conforme al Motu Proprio 'Sacramentorum Sanctitatis Tutela' de Juan Pablo II y 'Vos Estis Lux Mundi', el expediente se eleva obligatoriamente al Dicasterio para la Doctrina de la Fe en Roma.",
                    highlights = listOf("Separación cautelar obligatoria para salvaguardar a la comunidad.")
                )
            )
        ),
        PoliticaSection(
            id = "parte_3_consecuencias",
            title = "III. Consecuencias, Planes de Seguridad y Sanación",
            subtitle = "Justicia Restaurativa, Supervisión de por Vida y Rehabilitación",
            category = "Medidas Disciplinares y Cuidado",
            articles = listOf(
                PoliticaArticle(
                    number = "1.0",
                    title = "Justicia Restaurativa",
                    content = "El agresor debe solicitar perdón explícito a la víctima y a su familia cuando sea pastoralmente conveniente, y procurar resarcir el daño espiritual y moral causado a la Iglesia y al prójimo.",
                    highlights = listOf("Búsqueda humilde y activa del perdón y la reconciliación.")
                ),
                PoliticaArticle(
                    number = "2.1",
                    title = "Planes de Seguridad S20 (Supervisión Estricta)",
                    content = "Si un religioso es declarado culpable o administrativamente sancionado, queda bajo un Plan de Seguridad permanente aprobado por la Junta de Revisión. Prohibición absoluta de todo contacto no supervisado con menores o ministerio público pastoral. Asignación de un supervisor personal con informes trimestrales obligatorios.",
                    highlights = listOf("Supervisión de por vida para agresores de menores.")
                ),
                PoliticaArticle(
                    number = "3.2",
                    title = "Sanciones Canónicas (Código de Derecho Canónico)",
                    content = "El incumplimiento de las restricciones puede derivar en dimisión del estado clerical, suspensión del ministerio y expulsión perpetua de la Congregación de San Basilio (Cánones 695, 696 y 697).",
                    highlights = listOf("Rigor canónico estricto aplicado por la Curia Generalicia.")
                ),
                PoliticaArticle(
                    number = "4.0",
                    title = "Falsa Acusación y Reparación de la Verdad",
                    content = "Si tras una investigación exhaustiva una queja resulta infundada, la Congregación desplegará todos los esfuerzos necesarios para reparar la reputación del miembro falsamente acusado, informando a las comunidades y medios correspondientes.",
                    highlights = listOf("Defensa de la verdad y restauración del honor sacerdotal.")
                )
            )
        ),
        PoliticaSection(
            id = "apendices_praesidium",
            title = "IV. Apéndices & Estándares Praesidium",
            subtitle = "Normas de Interacción S8, Pornografía Electrónica y Miembros Visitantes S7",
            category = "Praesidium Standards",
            articles = listOf(
                PoliticaArticle(
                    number = "S8-A",
                    title = "Normas de Interacción Física con Menores",
                    content = "• Apropiadas: Abrazos laterales breves, apretón de manos, palmadas en hombro, chocar cinco, agacharse a la altura de niños en grupos.\n• Inapropiadas y Prohibidas: Besos en la boca, sentar a mayores de 4 años en el regazo, masajes corporales, caricias en dormitorios o lugares cerrados, dormir en la misma habitación o tienda de campaña.",
                    highlights = listOf("Entornos abiertos y siempre en presencia de dos adultos responsables.")
                ),
                PoliticaArticle(
                    number = "S8-B",
                    title = "Comunicaciones Electrónicas y Redes Sociales",
                    content = "Prohibido comunicarse por canales privados (WhatsApp, chats personales, videollamadas 1 a 1) con menores de edad sin copia o consentimiento explícito de sus padres o tutores. Jamás solicitar ni enviar contenido con doble sentido o connotación afectiva íntima.",
                    highlights = listOf("Transparencia total en comunicaciones digitales con jóvenes.")
                ),
                PoliticaArticle(
                    number = "Ap. 2",
                    title = "Política sobre Pornografía Generada Electrónicamente",
                    content = "El uso, descarga o visualización de pornografía es una gravísima falta contra la castidad consagrada (Benedicto XVI, Deus Caritas Est #5). Los equipos informáticos de la comunidad son bienes para el servicio apostólico, no para el consumo de fantasías denigrantes. La pornografía infantil constituye delito penal federal y delito canónico reservado a la Santa Sede.",
                    highlights = listOf("El ordenador personal del religioso es un bien comunitario sujeto a ética evangélica.")
                ),
                PoliticaArticle(
                    number = "S7",
                    title = "Religiosos Visitantes e Internacionales (S7 R6_R7)",
                    content = "Todo sacerdote o religioso externo que resida en una casa basiliana debe presentar carta de recomendación de su Superior Mayor, acreditar cursos en Praesidium Academy y someterse a verificación de antecedentes penales antes de cualquier labor apostólica.",
                    highlights = listOf("Garantía de entorno seguro en todas las casas basilianas del mundo.")
                )
            )
        )
    )
}
