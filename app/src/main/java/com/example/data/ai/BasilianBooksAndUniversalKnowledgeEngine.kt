package com.example.data.ai

import com.example.data.repository.ApuntesColombiaData
import com.example.data.repository.BasilianDirectoryRepository
import com.example.data.repository.BasilianStaticData
import com.example.data.repository.Book1922Data
import com.example.data.repository.CostumbresBasilianasData
import com.example.data.repository.FormaDeVidaData
import com.example.data.repository.Politicas2026Data
import java.text.Normalizer

/**
 * Motor Integral de Búsqueda en los Libros Oficiales de la App y Conocimiento Básico Universal.
 *
 * Conecta directamente a Superbasiliano IA con:
 * 1. La Forma de Vida Basiliana (Constituciones: 153 artículos, prefacios y decreto pontificio).
 * 2. Costumbres Basilianas (Edición Bicentenario 2022: Secciones A-N y todos sus artículos).
 * 3. Libro «1922: Antes y Después» (P. Kevin J. Kirley, CSB — capítulos, secciones, páginas y Superiores Generales).
 * 4. Libro «Apuntes para una historia de la Congregación de San Basilio en Colombia (1987-1997)» (Luis Fernando Sánchez Hurtado, CSB).
 * 5. Políticas y Procedimientos sobre la Conducta Sexual Inapropiada (Edición 2026 / Estándares Praesidium).
 * 6. Directorio Oficial CSB (Sacerdotes, Escolásticos, Novicios, Casas Basilianas) y Necrología Histórica (1822-2026).
 * 7. Base de Conocimiento Científico, Histórico, Bíblico y de Cultura General Básica (sin inventar datos).
 */
object BasilianBooksAndUniversalKnowledgeEngine {

    private fun normalizeText(input: String): String {
        val decomposed = Normalizer.normalize(input.lowercase(), Normalizer.Form.NFD)
        return decomposed.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
    }

    private val stopWords = setOf(
        "que", "cual", "cuales", "como", "cuando", "donde", "quien", "quienes", "por", "para",
        "con", "sin", "sobre", "entre", "hacia", "hasta", "desde", "del", "las", "los", "una",
        "uno", "unos", "unas", "este", "esta", "estos", "estas", "ese", "esa", "esos", "esas",
        "aquel", "aquella", "fue", "fueron", "son", "era", "eran", "ser", "estar", "esta",
        "estan", "hay", "tiene", "tienen", "dice", "dicen", "habla", "trata", "explica",
        "dime", "cuentame", "podrias", "puedes", "decir", "saber", "quiero", "necesito",
        "pregunta", "preguntando", "favor", "acerca", "segun", "libro", "libros", "historia",
        "basiliano", "basilianos", "basiliana", "basilianas", "padres", "congregacion", "san", "basilio"
    )

    private fun extractKeywords(query: String): List<String> {
        val norm = normalizeText(query)
        return norm.split(Regex("[^a-z0-9]+"))
            .filter { it.length >= 3 && it !in stopWords }
            .distinct()
    }

    /**
     * 1. RESPUESTAS CLARAS Y CONCRETAS A PREGUNTAS BÁSICAS SIEMPRE ENFOCADAS EXCLUSIVAMENTE EN LOS PADRES BASILIANOS (C.S.B.).
     * Como IA Basiliana, aunque conozca datos científicos, históricos o bíblicos universales,
     * SIEMPRE responde desde la identidad, pedagogía (*Bonitatem, Disciplinam et Scientiam*),
     * libros oficiales y espiritualidad de los Padres Basilianos y de San Basilio Magno.
     */
    fun answerUniversalBasicAndScientificQuestion(
        userMessage: String,
        uName: String
    ): String? {
        val norm = normalizeText(userMessage).trim()

        // 1. ¿Cuál fue el primer ser vivo? / Origen de la vida / Primer organismo vivo
        if (norm.contains("primer ser vivo") ||
            norm.contains("primer organismo") ||
            norm.contains("primeros seres vivos") ||
            norm.contains("primera forma de vida") ||
            norm.contains("origen de la vida") ||
            norm.contains("como surgio la vida") ||
            norm.contains("como nacio la vida") ||
            norm.contains("como empezo la vida") ||
            (norm.contains("primer") && norm.contains("ser") && norm.contains("vivo")) ||
            norm.contains("abiogenesis") ||
            norm.contains("luca ") || norm.endsWith("luca")
        ) {
            return buildString {
                append("**$uName**, como **Padre y Hermano Basiliano (C.S.B.)**, te respondo sobre **cuál fue el primer ser vivo** enfocado siempre desde la enseñanza de los **Padres Basilianos** y de nuestro patrono **San Basilio Magno**:\n\n")
                append("• **1. La Doctrina de San Basilio Magno en *El Hexamerón* (Nuestra Raíz Basiliana):**\n")
                append("  En las **9 Homilías sobre el *Hexamerón*** (los seis días de la Creación), **San Basilio Magno** enseña que Dios es la Causa Primera de toda vida. Cuando el Creador pronunció su Palabra —*«Produzca la tierra seres vivientes»* y *«Bullan las aguas de seres vivientes»* (Gn 1, 11.20)— depositó en las aguas y en la materia una **fuerza vital germinativa** que hizo brotar la vida desde sus formas más simples y primeras hasta el ser humano.\n\n")
                append("• **2. Lo que enseñamos los Padres Basilianos en nuestras Aulas (`Scientiam` — *Forma de Vida Basiliana*, Art. 14 y 54):**\n")
                append("  Fieles a nuestro lema basiliano ***«Bonitatem et disciplinam et scientiam doce me»*** (*Enséñame la bondad, la disciplina y la ciencia*, Sal 118, 66), en los colegios y universidades de la **Congregación de San Basilio** enseñamos con exactitud científica cómo se manifestó esa primera semilla de vida creada por Dios:\n")
                append("  — **Primeros seres vivos:** Fueron **microorganismos unicelulares procariotas** (similares a las bacterias y arqueas), surgidos en los océanos primitivos hace unos **3.800 a 4.000 millones de años**.\n")
                append("  — **LUCA (*Último Antepasado Común Universal*):** Es el primer microorganismo unicelular del cual desciende toda la vida biológica en la Tierra, seguido por las primeras **cianobacterias** (estromatolitos hace ~3.500 millones de años).\n\n")
                append("• **3. Síntesis Exclusivamente Basiliana:**\n")
                append("  Para todo **Padre Basiliano**, la ciencia (*Scientiam*) nos muestra que el primer ser vivo físico fue un microorganismo unicelular procariota (**LUCA**), y nuestra fe basiliana (*El Hexamerón*) contempla en ese primer destello celular la Sabiduría creadora de Dios.\n\n")
                append("¿Te gustaría que profundicemos más en las **Homilías del *Hexamerón* de San Basilio Magno** o en cómo los **Padres Basilianos** integramos la ciencia y la fe en nuestra misión educativa?")
            }
        }

        // 2. Origen del Universo / Big Bang / Edad de la Tierra
        if (norm.contains("origen del universo") ||
            norm.contains("como se creo el universo") ||
            norm.contains("como surgio el universo") ||
            norm.contains("big bang") ||
            norm.contains("edad del universo") ||
            norm.contains("edad de la tierra") ||
            norm.contains("como se formo la tierra")
        ) {
            return buildString {
                append("**$uName**, desde la identidad y el magisterio educativo de los **Padres Basilianos (Congregación de San Basilio - C.S.B.)**, te explico el **origen del universo y de la Tierra** a la luz de **San Basilio Magno**:\n\n")
                append("• **1. La Enseñanza de San Basilio Magno (*Homilía I del Hexamerón*):**\n")
                append("  Nuestro padre espiritual **San Basilio Magno**, al comentar *«En el principio creó Dios el cielo y la tierra»* (Gn 1,1), demostró frente a los filósofos paganos que el cosmos no es eterno ni fruto del azar, sino que tuvo un **comienzo temporal** brotado de la Voluntad y la Sabiduría de Dios.\n\n")
                append("• **2. La Ciencia que Enseñan los Padres Basilianos (`Scientiam`):**\n")
                append("  En las universidades y colegios de los **Padres Basilianos** (*St. Michael's College*, *University of St. Thomas*, *St. John Fisher*), bajo el lema *«Bonitatem et disciplinam et scientiam doce me»*, explicamos que ese principio temporal del cosmos corresponde físicamente al **Big Bang** (hace **13.800 millones de años**, teoría formulada por el sacerdote católico **Mons. Georges Lemaître**), y que la **Tierra** se formó hace **4.540 millones de años**.\n\n")
                append("¿Deseas que revisemos juntos cómo explica **San Basilio Magno** la armonía del universo en el *Hexamerón* o qué dice nuestra *Forma de Vida Basiliana* sobre la búsqueda de la verdad?")
            }
        }

        // 3. ¿Cuál fue el primer ser humano / primeros humanos (Adán y Eva / Homo sapiens)?
        if (norm.contains("primer ser humano") ||
            norm.contains("primer hombre") ||
            norm.contains("primeros humanos") ||
            norm.contains("origen del hombre") ||
            norm.contains("origen del ser humano") ||
            norm.contains("adan y eva") ||
            norm.contains("homo sapiens")
        ) {
            return buildString {
                append("**$uName**, enfocando tu pregunta exclusivamente desde la antropología y espiritualidad de los **Padres Basilianos (C.S.B.)** y de **San Basilio Magno**:\n\n")
                append("• **1. Tratado *«Sobre el origen del hombre»* de San Basilio Magno:**\n")
                append("  En sus homilías sobre la creación del ser humano (*«Hagamos al hombre a nuestra imagen y semejanza»*, Gn 1,26), **San Basilio Magno** enseña que el primer ser humano (**Adán y Eva** en la revelación bíblica) recibe de Dios el soplo del **alma espiritual, la libertad y la vocación a la comunión fraterna**.\n\n")
                append("• **2. Enfoque Educativo de los Padres Basilianos (`Scientiam` y `Bonitatem`):**\n")
                append("  En la pedagogía de la **Congregación de San Basilio** (*Forma de Vida Basiliana*, Art. 13–15), enseñamos que mientras la paleoantropología estudia el desarrollo biológico corporal (la aparición del género *Homo* hace ~2,5 millones de años y del *Homo sapiens* hace ~300.000 años en África), nuestra fe basiliana reconoce en **Adán y Eva** a los primeros portadores de la imagen divina, llamados a vivir en fraternidad como una sola familia de hijos de Dios.\n\n")
                append("¿Quieres que profundicemos en la visión del ser humano según **San Basilio Magno** y nuestra *Forma de Vida Basiliana*?")
            }
        }

        // 4. ¿Qué es la célula / el ADN / la evolución / la fotosíntesis?
        if (norm.contains("que es la celula") || norm.contains("que es una celula") || norm.contains("que es el adn") || norm.contains("que es la fotosintesis") || norm.contains("teoria de la evolucion")) {
            return buildString {
                append("**$uName**, respondiendo desde la vocación académica y espiritual de los **Padres Basilianos (C.S.B.)** y nuestro lema ***«Bonitatem et disciplinam et scientiam doce me»***:\n\n")
                when {
                    norm.contains("adn") -> {
                        append("• **El ADN contemplado desde la Ciencia Basiliana (`Scientiam`):**\n")
                        append("  En las aulas de los **Padres Basilianos**, enseñamos que el **ADN (Ácido Desoxirribonucleico)** es la molécula de doble hélice (formada por Adenina, Timina, Citosina y Guanina) que guarda la información hereditaria de los seres vivos. Como predicaba **San Basilio Magno** en el *Hexamerón*, hasta en el diseño más diminuto de la naturaleza resplandece el orden admirable del Creador.")
                    }
                    norm.contains("fotosintesis") -> {
                        append("• **La Fotosíntesis y la Contemplación de San Basilio Magno (*Homilía V del Hexamerón*):**\n")
                        append("  Científicamente, la **fotosíntesis** es el proceso por el cual las plantas y cianobacterias transforman la luz solar, el CO₂ y el agua en energía vital y liberan oxígeno. **San Basilio Magno**, en su *Homilía V sobre la germinación de las plantas*, invitaba a sus monjes y discípulos a admirar cómo cada hoja y cada planta cumplen fielmente la ley de vida que Dios les trazó.")
                    }
                    norm.contains("evolucion") -> {
                        append("• **La Evolución Biológica en la Pedagogía de los Padres Basilianos:**\n")
                        append("  En los colegios y universidades de la **Congregación de San Basilio**, explicamos la **evolución biológica** (la diversificación de las especies a partir de antepasados comunes y las leyes genéticas descubiertas por el religioso Gregor Mendel) en plena armonía con *El Hexamerón* de **San Basilio Magno**, quien enseñó que Dios dotó a la creación de leyes dinámicas para desplegar la vida.")
                    }
                    else -> {
                        append("• **La Célula desde la Enseñanza de los Padres Basilianos:**\n")
                        append("  La **célula** es la unidad viva fundamental (procariota sin núcleo definido, como las primeras bacterias; y eucariota con núcleo, presente en plantas, animales y el ser humano). Para los **Padres Basilianos**, estudiar la unidad armónica de la célula recuerda también la enseñanza de **San Basilio Magno** en el *Asketikon*: así como las células cooperan para dar vida al cuerpo, los hermanos en comunidad forman un solo Cuerpo en Cristo.")
                    }
                }
                append("\n\n¿Te gustaría relacionar este tema con algún capítulo de los libros de los **Padres Basilianos**?")
            }
        }

        // 5. ¿Quién fue el primer Papa de la Iglesia? / Sucesión de San Pedro
        if (norm.contains("primer papa") ||
            (norm.contains("quien fue") && norm.contains("papa") && norm.contains("primero"))
        ) {
            return buildString {
                append("**$uName**, desde la eclesiología y fidelidad de los **Padres Basilianos (Congregación de San Basilio - C.S.B.)**, te respondo:\n\n")
                append("• **1. El Primer Papa de la Iglesia (San Pedro):**\n")
                append("  El primer Papa fue el Apóstol **San Pedro**, a quien Cristo confió las llaves del Reino (*Mateo 16, 18-19*) y el pastoreo universal (*Juan 21, 15-17*), entregando su vida en el martirio en Roma (64–67 d.C.).\n\n")
                append("• **2. Vínculo Histórico con los Padres Basilianos:**\n")
                append("  La **Congregación de San Basilio**, fundada en Annonay (1822), ha vivido siempre en comunión filial con el Sucesor de San Pedro: el **Papa Gregorio XVI** nos aprobó en 1837, **Pío IX** confirmó nuestras Constituciones en 1863, **San Juan Pablo II** aprobó *La Forma de Vida Basiliana* en 1983, y hoy oramos en comunión con el **Papa León XIV**.\n\n")
                append("¿Quieres conocer más sobre cómo los Papas acompañaron la historia de los **Padres Basilianos** desde 1822 hasta hoy?")
            }
        }

        // 6. ¿Cuál es el primer libro de la Biblia / cuántos libros tiene la Biblia?
        if (norm.contains("primer libro de la biblia") ||
            norm.contains("cuantos libros tiene la biblia") ||
            norm.contains("libros de la biblia")
        ) {
            return buildString {
                append("**$uName**, desde la espiritualidad bíblica de **San Basilio Magno** y de los **Padres Basilianos (C.S.B.)**:\n\n")
                append("• **1. El Canon Bíblico que Meditamos los Padres Basilianos:**\n")
                append("  La Sagrada Biblia Católica consta de **73 libros** (**46 del Antiguo Testamento** y **27 del Nuevo Testamento**). El **primer libro es el Génesis** —comentado magistralmente por **San Basilio Magno** en sus 9 Homilías sobre *El Hexamerón*— y el último es el **Apocalipsis**.\n\n")
                append("• **2. La Palabra de Dios en *La Forma de Vida Basiliana*:**\n")
                append("  En las *Reglas Morales* de **San Basilio Magno** y en los **Artículos 39 al 46 de *La Forma de Vida Basiliana***, la Sagrada Escritura es la regla suprema de nuestra vida comunitaria, de nuestra oración diaria y de nuestro lema tomado del **Salmo 118, 66**: *«Bonitatem et disciplinam et scientiam doce me»*.\n\n")
                append("¿Te gustaría profundizar en las citas bíblicas de nuestra ***Forma de Vida Basiliana***?")
            }
        }

        return null
    }

    /**
     * 2. MOTOR DE BÚSQUEDA PROFUNDA EN TODOS LOS LIBROS Y REPOSITORIOS DE LA APP.
     * Si el usuario pregunta por cualquier artículo, capítulo, costumbre, fecha, padre basiliano
     * o tema contenido en los libros de la aplicación, responde con los datos reales y exactos del libro.
     */
    fun searchAllBasilianBooksAndRepositories(
        userMessage: String,
        uName: String
    ): String? {
        val norm = normalizeText(userMessage).trim()
        val keywords = extractKeywords(userMessage)

        // 0) Si pregunta qué libros tiene la aplicación / cuáles son los libros basilianos disponibles
        if ((norm.contains("que libros") || norm.contains("cuales libros") || norm.contains("cuales son los libros") || norm.contains("libros tienes") || norm.contains("libros de la app") || norm.contains("documentos tienes")) &&
            (norm.contains("tienes") || norm.contains("hay") || norm.contains("aplicacion") || norm.contains("app") || norm.contains("basilian"))
        ) {
            return buildString {
                append("**$uName**, tengo integrados y disponibles para consulta inmediata todos los **libros oficiales, constituciones e historia documental de la Congregación de San Basilio (C.S.B.)**:\n\n")
                append("• **1. *La Forma de Vida Basiliana* (Constituciones CSB):** Incluye el Decreto Pontificio de 1983, los Prefacios de los Superiores Generales (P. George Smith 1983, P. Ulisse Paré 1993, P. Ronald Fabbro 2002) y los **153 Artículos Constitucionales** íntegros.\n")
                append("• **2. *Costumbres Basilianas* (Edición del Bicentenario 2022):** Incluye el prefacio del P. Kevin Storey y las **14 Secciones (A hasta N)** con todas las normas de vida común, oración, sufragios, estudios, vestimenta y gobierno.\n")
                append("• **3. Libro *«1922: Antes y Después»* (P. Kevin J. Kirley, CSB):** Historia documentada del primer siglo en Francia (1822–1922), la separación canónica de 1922 entre Francia y América, la Gran Reunificación de 1955 y el registro de todos los Superiores Generales.\n")
                append("• **4. Libro *«Apuntes para una historia de la Congregación de San Basilio en Colombia (1987–1997)»* (Luis Fernando Sánchez Hurtado, CSB, con prólogo del P. Bob Barringer, CSB):** Crónica completa de la llegada a Colombia en 1987 (etapa fundacional en Cali, apertura en Medellín en 1995 y Casa Annonay en Bogotá en 1997).\n")
                append("• **5. *Políticas y Procedimientos sobre la Conducta Sexual Inapropiada (Edición 2026)*:** Normativa ética, canónica y estándares de acreditación Praesidium.\n")
                append("• **6. *Directorio Oficial CSB (2026)* y *Catálogo de la Necrología Basiliana (1822–2026)*:** Fichas de todos los sacerdotes, obispos, escolásticos, novicios, casas basilianas y hermanos difuntos.\n\n")
                append("Puedes preguntarme por cualquier artículo (ej. *«¿Qué dice el artículo 25 de la Forma de Vida?»*), costumbre (ej. *«Costumbre E.18»*), capítulo histórico o hermano basiliano.")
            }
        }

        // A) Búsqueda directa de un artículo de «La Forma de Vida Basiliana» (Artículos 1 al 153)
        val fvbMatch = Regex("(?:articulo|art\\.?|numeral|numero|fvb)\\s*(\\d{1,3})\\b").find(norm)
        if (fvbMatch != null && (norm.contains("forma de vida") || norm.contains("constitucion") || norm.contains("fvb") || !norm.contains("politica"))) {
            val artNum = fvbMatch.groupValues[1].toIntOrNull()
            if (artNum != null) {
                val article = FormaDeVidaData.document.articles.find { it.number == artNum }
                if (article != null) {
                    return buildString {
                        append("**$uName**, aquí tienes el texto oficial íntegro del **Artículo ${article.number}** de ***La Forma de Vida Basiliana*** (Constituciones de la Congregación de San Basilio):\n\n")
                        append("📖 **Capítulo:** ${article.chapter}\n")
                        append("📜 **Artículo ${article.number}:**\n")
                        append("«${article.text}»\n\n")
                        if (article.biblicalFootnotes.isNotBlank()) {
                            append("✝️ **Referencias Bíblicas y Canónicas del Artículo ${article.number}:** ${article.biblicalFootnotes}\n")
                        }
                    }
                } else {
                    return "**$uName**, *La Forma de Vida Basiliana* consta exactamente de **153 artículos** oficiales (numerados del 1 al 153). El número **$artNum** no existe en las Constituciones. Puedes consultarme cualquier artículo entre el **1 y el 153**."
                }
            }
        }

        // B) Búsqueda directa de un código de «Costumbres Basilianas» (ej. A.01, B.03, E.18, N.10)
        val costumbreCodeMatch = Regex("\\b([a-n])\\.(\\d{1,2})\\b").find(norm)
        if (costumbreCodeMatch != null) {
            val letter = costumbreCodeMatch.groupValues[1].uppercase()
            val num = costumbreCodeMatch.groupValues[2].padStart(2, '0')
            val targetCode = "$letter.$num"
            val allCustomArticles = CostumbresBasilianasData.document.sections.flatMap { it.articles }
            val foundCustom = allCustomArticles.find { it.code.equals(targetCode, ignoreCase = true) }
            if (foundCustom != null) {
                return buildString {
                    append("**$uName**, aquí tienes el texto oficial de la costumbre **${foundCustom.code}** del libro ***Costumbres Basilianas (Edición Bicentenario 2022)***:\n\n")
                    append("📘 **Sección:** ${foundCustom.categoryTitle}\n")
                    append("📌 **${foundCustom.code} — ${foundCustom.title}:**\n")
                    append("«${foundCustom.content}»\n")
                    if (foundCustom.fvbReference.isNotBlank()) {
                        append("\n🔗 **Referencia Constitucional:** ${foundCustom.fvbReference}")
                    }
                }
            }
        }

        // C) Búsqueda de un Sacerdote, Escolástico, Novicio o Hermano Difunto en el Directorio Oficial / Necrología
        val memberHit = searchDirectoryAndNecrology(norm, keywords, uName)
        if (memberHit != null) {
            return memberHit
        }

        // D) Búsqueda semántica y textual en los 5 Libros Oficiales de la App:
        if (keywords.isNotEmpty()) {
            val scoredHits = collectScoredBookExcerpts(norm, keywords)
            if (scoredHits.isNotEmpty() && scoredHits.first().score >= 5) {
                val topHits = scoredHits.take(4)
                return buildString {
                    append("**$uName**, basándome directamente en los **libros y documentos oficiales de la Congregación de San Basilio** contenidos en esta aplicación, aquí tienes la respuesta exacta y documentada:\n\n")
                    topHits.forEachIndexed { index, hit ->
                        append("• **${index + 1}. ${hit.bookTitle} — ${hit.sectionReference}:**\n")
                        append("  ${hit.content}\n\n")
                    }
                    if (norm.contains("cali") || topHits.any { normalizeText(it.content).contains("cali") }) {
                        append("📌 **Nota de Actualidad Verificada (Colombia):** Aunque el libro histórico *Apuntes para una historia en Colombia (1987–1997)* documenta la primera etapa misional en **Cali** (Parroquia de la Asunción en el Distrito de Aguablanca en 1987), **actualmente los Padres Basilianos ya no están en Cali** (esa obra fue entregada a la Arquidiócesis de Cali); hoy la presencia basiliana en Colombia se concentra exclusivamente en **Bogotá** (Casa Annonay y Parroquia Nuestra Señora de Egipto) y en **Medellín** (Parroquia San Basilio).")
                    }
                }.trim()
            }
        }

        return null
    }

    data class BookExcerptHit(
        val bookTitle: String,
        val sectionReference: String,
        val content: String,
        val score: Int
    )

    /**
     * Genera un resumen documental de los libros internos para inyectar en el prompt de Gemini
     * o para responder con precisión textual verificada.
     */
    fun buildBookContextForPrompt(userMessage: String): String {
        val norm = normalizeText(userMessage).trim()
        val keywords = extractKeywords(userMessage)
        if (keywords.isEmpty()) return ""

        val hits = collectScoredBookExcerpts(norm, keywords).take(5)
        if (hits.isEmpty()) return ""

        return buildString {
            append("=== EXTRACTOS VERIFICADOS DE LOS LIBROS OFICIALES DE LA APP ===\n")
            hits.forEachIndexed { idx, hit ->
                append("[${idx + 1}] ${hit.bookTitle} (${hit.sectionReference}): ${hit.content}\n")
            }
        }
    }

    private fun searchDirectoryAndNecrology(
        normQuery: String,
        keywords: List<String>,
        uName: String
    ): String? {
        if (keywords.isEmpty()) return null

        // Buscar miembros activos en el Directorio Oficial 2026
        val matchedMembers = BasilianDirectoryRepository.members.mapNotNull { member ->
            val normName = normalizeText(member.name)
            val nameTokens = normName.split(Regex("[^a-z0-9]+")).filter { it.length >= 3 }
            val matchedCount = nameTokens.count { token -> keywords.contains(token) }
            val strongSurnameMatch = nameTokens.firstOrNull()?.let { firstSurname ->
                firstSurname.length >= 4 && keywords.contains(firstSurname)
            } ?: false
            if (matchedCount >= 2 || (strongSurnameMatch && (normQuery.contains("padre") || normQuery.contains("quien es") || normQuery.contains("donde vive") || normQuery.contains("correo") || normQuery.contains("telefono") || normQuery.contains("cumpleanos") || normQuery.contains("ordenacion")))) {
                Pair(member, matchedCount * 5 + if (strongSurnameMatch) 4 else 0)
            } else {
                null
            }
        }.sortedByDescending { it.second }

        if (matchedMembers.isNotEmpty()) {
            val topMembers = matchedMembers.take(3).map { it.first }
            return buildString {
                append("**$uName**, según el **Directorio Oficial de la Congregación de San Basilio (Edición 2026)** incluido en la aplicación, aquí tienes los datos exactos:\n\n")
                topMembers.forEach { m ->
                    append("• **${m.name}, CSB** (${m.category.label} — ${m.country})\n")
                    append("  — **Fecha de Nacimiento:** ${m.birthDateRaw} (${m.getFormattedBirthday()})\n")
                    if (m.professionDate.isNotBlank()) {
                        append("  — **Primera Profesión:** ${m.professionDate}\n")
                    }
                    if (m.ordinationDate.isNotBlank()) {
                        append("  — **Ordenación Sacerdotal:** ${m.ordinationDate}\n")
                    }
                    append("  — **Comunidad / Residencia Actual:** ${m.currentHouse}")
                    if (m.address.isNotBlank()) append(" (${m.address})")
                    append("\n")
                    if (m.rolesOrMinistries.isNotBlank()) {
                        append("  — **Ministerio / Cargo:** ${m.rolesOrMinistries}\n")
                    }
                    if (m.email.isNotBlank()) {
                        append("  — **Correo:** ${m.email}\n")
                    }
                    if (m.cellPhone.isNotBlank() || m.officePhone.isNotBlank()) {
                        val phones = listOf(m.cellPhone, m.officePhone).filter { it.isNotBlank() }.joinToString(" / ")
                        append("  — **Teléfono:** $phones\n")
                    }
                    append("\n")
                }
            }.trim()
        }

        // Buscar hermanos difuntos en la Necrología Oficial (1822-2026)
        val matchedDeceased = BasilianDirectoryRepository.deceasedBrothers.mapNotNull { bro ->
            val normName = normalizeText(bro.name)
            val nameTokens = normName.split(Regex("[^a-z0-9]+")).filter { it.length >= 4 && it !in setOf("rev", "padre", "mons", "obispo", "cardenal") }
            val matchedCount = nameTokens.count { token -> keywords.contains(token) }
            if (matchedCount >= 2 || (matchedCount == 1 && (normQuery.contains("murio") || normQuery.contains("fallecio") || normQuery.contains("necrologia") || normQuery.contains("difunto")))) {
                Pair(bro, matchedCount)
            } else {
                null
            }
        }.sortedByDescending { it.second }

        if (matchedDeceased.isNotEmpty()) {
            val topDeceased = matchedDeceased.take(3).map { it.first }
            return buildString {
                append("**$uName**, según el **Catálogo Oficial de la Necrología Basiliana (1822–2026)** de la aplicación, aquí tienes el registro histórico:\n\n")
                topDeceased.forEach { d ->
                    append("• **${d.name}** (${d.birthAndDeathYears})\n")
                    append("  — **Fecha de Pascua (Fallecimiento):** ${d.datePassed}")
                    if (d.age != null && d.age > 0) append(" (a los ${d.age} años)")
                    append("\n")
                    append("  — **Ministerio / Legado:** ${d.titleOrRole}\n")
                    if (d.memorialQuote.isNotBlank()) {
                        append("  — **Memoria:** ${d.memorialQuote}\n")
                    }
                    append("\n")
                }
            }.trim()
        }

        return null
    }

    private fun collectScoredBookExcerpts(
        normQuery: String,
        keywords: List<String>
    ): List<BookExcerptHit> {
        val results = mutableListOf<BookExcerptHit>()

        fun scoreText(titleText: String, bodyText: String, bonusIfBookNamed: Boolean = false): Int {
            val normTitle = normalizeText(titleText)
            val normBody = normalizeText(bodyText)
            var score = 0
            for (kw in keywords) {
                if (normTitle.contains(kw)) score += 5
                if (normBody.contains(kw)) score += 3
            }
            if (bonusIfBookNamed && score > 0) score += 4
            return score
        }

        val mentions1922Book = normQuery.contains("1922") || normQuery.contains("kirley") || normQuery.contains("antes y despues") || normQuery.contains("separacion") || normQuery.contains("reunificacion") || normQuery.contains("viviers")
        val mentionsColombiaBook = normQuery.contains("apuntes") || normQuery.contains("sanchez") || normQuery.contains("barringer") || normQuery.contains("colombia") || normQuery.contains("aguablanca") || normQuery.contains("medellin") || normQuery.contains("egipto")
        val mentionsFvb = normQuery.contains("forma de vida") || normQuery.contains("constitucion") || normQuery.contains("votos") || normQuery.contains("pobreza") || normQuery.contains("castidad") || normQuery.contains("obediencia")
        val mentionsCostumbres = normQuery.contains("costumbre") || normQuery.contains("sufragio") || normQuery.contains("vacaciones") || normQuery.contains("habito") || normQuery.contains("vestimenta") || normQuery.contains("jubileo")
        val mentionsPoliticas = normQuery.contains("politica") || normQuery.contains("praesidium") || normQuery.contains("conducta") || normQuery.contains("abuso") || normQuery.contains("menores") || normQuery.contains("junta de revision")

        // 1. Libro «1922: Antes y Después» (P. Kevin J. Kirley, CSB)
        for (chapter in Book1922Data.chapters) {
            val chapHeader = "${chapter.partTitle} - ${chapter.chapterTitle} (${chapter.subtitle})"
            val chapScore = scoreText(chapHeader, chapter.summary + " " + chapter.keyTakeaways.joinToString(" "), mentions1922Book)
            if (chapScore >= 5) {
                results.add(
                    BookExcerptHit(
                        bookTitle = "Libro «1922: Antes y Después» (P. Kevin J. Kirley, CSB)",
                        sectionReference = "${chapter.chapterTitle} (${chapter.pageRange})",
                        content = "${chapter.summary} Puntos clave: ${chapter.keyTakeaways.joinToString(" • ")}",
                        score = chapScore
                    )
                )
            }
            for (sec in chapter.sections) {
                val secBody = sec.paragraphs.joinToString(" ")
                val secScore = scoreText("${chapter.chapterTitle} ${sec.title}", secBody, mentions1922Book)
                if (secScore >= 5) {
                    results.add(
                        BookExcerptHit(
                            bookTitle = "Libro «1922: Antes y Después» (P. Kevin J. Kirley, CSB)",
                            sectionReference = "${chapter.chapterTitle} — ${sec.title} (${chapter.pageRange})",
                            content = secBody,
                            score = secScore + 1
                        )
                    )
                }
            }
        }

        // Superiores Generales en Book1922Data
        for (sg in Book1922Data.superiorsGeneralSince1822) {
            val sgScore = scoreText(sg.name + " " + sg.period, sg.jurisdiction + " " + sg.keyMilestones, mentions1922Book)
            if (sgScore >= 5) {
                results.add(
                    BookExcerptHit(
                        bookTitle = "Libro «1922: Antes y Después» (Registro de Superiores Generales)",
                        sectionReference = "${sg.name} (${sg.period})",
                        content = "Años de vida: ${sg.years}. Jurisdicción: ${sg.jurisdiction}. Hitos: ${sg.keyMilestones}",
                        score = sgScore + 2
                    )
                )
            }
        }

        // 2. Libro «Apuntes para una historia de la Congregación de San Basilio en Colombia (1987-1997)»
        for (chapter in ApuntesColombiaData.book.chapters) {
            for (sec in chapter.sections) {
                val quoteText = if (sec.quote.isNotBlank()) " Cita: «${sec.quote}» (${sec.quoteAuthor})." else ""
                val fullSec = "${sec.content}$quoteText"
                val secScore = scoreText("${chapter.title} ${sec.title} ${chapter.years}", fullSec, mentionsColombiaBook)
                if (secScore >= 5) {
                    results.add(
                        BookExcerptHit(
                            bookTitle = "Libro «Apuntes para una historia en Colombia (1987–1997)» (Luis Fernando Sánchez, CSB)",
                            sectionReference = "Cap. ${chapter.romanNumeral}: ${chapter.title} — ${sec.title}",
                            content = fullSec,
                            score = secScore + 1
                        )
                    )
                }
            }
        }

        // 3. «La Forma de Vida Basiliana» (153 Artículos Constitucionales)
        for (art in FormaDeVidaData.document.articles) {
            val artScore = scoreText("Articulo ${art.number} ${art.chapter}", art.text, mentionsFvb)
            if (artScore >= 5) {
                val notes = if (art.biblicalFootnotes.isNotBlank()) " [Ref: ${art.biblicalFootnotes}]" else ""
                results.add(
                    BookExcerptHit(
                        bookTitle = "«La Forma de Vida Basiliana» (Constituciones CSB)",
                        sectionReference = "Artículo ${art.number} (${art.chapter})",
                        content = "«${art.text}»$notes",
                        score = artScore
                    )
                )
            }
        }

        // 4. «Costumbres Basilianas» (Edición Bicentenario 2022)
        for (sec in CostumbresBasilianasData.document.sections) {
            for (art in sec.articles) {
                val cScore = scoreText("${art.code} ${art.title} ${sec.title}", art.content, mentionsCostumbres)
                if (cScore >= 5) {
                    val ref = if (art.fvbReference.isNotBlank()) " (${art.fvbReference})" else ""
                    results.add(
                        BookExcerptHit(
                            bookTitle = "«Costumbres Basilianas» (Edición 2022)",
                            sectionReference = "Costumbre ${art.code}: ${art.title}$ref",
                            content = "«${art.content}»",
                            score = cScore
                        )
                    )
                }
            }
        }

        // 5. «Políticas y Procedimientos sobre la Conducta Sexual Inapropiada (2026)»
        for (sec in Politicas2026Data.sections) {
            for (art in sec.articles) {
                val pScore = scoreText("${art.number} ${art.title} ${sec.title}", art.content, mentionsPoliticas)
                if (pScore >= 5) {
                    results.add(
                        BookExcerptHit(
                            bookTitle = "«Políticas y Procedimientos sobre la Conducta Sexual Inapropiada (2026)»",
                            sectionReference = "${sec.title} — Art. ${art.number}: ${art.title}",
                            content = "${art.content} Puntos clave: ${art.highlights.joinToString("; ")}",
                            score = pScore
                        )
                    )
                }
            }
        }

        // 6. Fundadores, Patronos e Hitos Históricos en BasilianStaticData
        for (founder in BasilianStaticData.founders) {
            val fScore = scoreText("${founder.name} ${founder.title}", "${founder.biography} ${founder.keyContribution}", false)
            if (fScore >= 5) {
                results.add(
                    BookExcerptHit(
                        bookTitle = "Archivo Histórico Fundacional CSB (1822)",
                        sectionReference = "${founder.name} (${founder.years}) — ${founder.role}",
                        content = "${founder.biography} Aporte clave: ${founder.keyContribution}",
                        score = fScore + 2
                    )
                )
            }
        }

        for (patron in BasilianStaticData.patrons) {
            val patScore = scoreText("${patron.name} ${patron.title}", "${patron.biography} ${patron.connectionToBasilians}", false)
            if (patScore >= 5) {
                results.add(
                    BookExcerptHit(
                        bookTitle = "Santos Patronos de la Congregación de San Basilio",
                        sectionReference = "${patron.name} (Fiesta: ${patron.feastDay})",
                        content = "${patron.biography} Vínculo con los Basilianos: ${patron.connectionToBasilians}",
                        score = patScore + 1
                    )
                )
            }
        }

        return results.sortedByDescending { it.score }
    }
}
