package com.example.data.repository

import com.example.data.model.AppLanguage
import com.example.data.model.BasilianCouncilMember
import com.example.data.model.BasilianDirectoryMember
import com.example.data.model.BasilianHouseInfo
import com.example.data.model.DeceasedBrother
import com.example.data.model.DeceasedCategory

/**
 * Localization dictionary and helper functions for Directorio Oficial and En Memoria / In Memoriam.
 * Provides complete English and French translations for all deceased confreres, council members,
 * houses, and directory cards ("cuadros").
 */
object BasilianDirectoryLocalization {

    private data class DeceasedTranslation(
        val titleEn: String,
        val titleFr: String,
        val dateEn: String,
        val dateFr: String,
        val quoteEn: String,
        val quoteFr: String,
        val yearsEn: String? = null,
        val yearsFr: String? = null
    )

    private val deceasedTranslations: Map<String, DeceasedTranslation> = mapOf(
        "dec_carruthers" to DeceasedTranslation(
            titleEn = "Basilian Priest in Canada",
            titleFr = "Prêtre Basilien au Canada",
            dateEn = "August 8, 2025",
            dateFr = "8 août 2025",
            quoteEn = "Faithful witness to evangelical goodness and dedication in the Christian education of generations in Canada.",
            quoteFr = "Fidèle témoin de la bonté et du dévouement évangélique dans l'éducation chrétienne de générations au Canada."
        ),
        "dec_owens" to DeceasedTranslation(
            titleEn = "Basilian Priest",
            titleFr = "Prêtre Basilien",
            dateEn = "August 17, 2025",
            dateFr = "17 août 2025",
            quoteEn = "A life consecrated to study, patience, and youth formation with the heart of a true father.",
            quoteFr = "Une vie consacrée à l'étude, à la patience et à la formation de la jeunesse avec le cœur d'un vrai père."
        ),
        "dec_hilmer" to DeceasedTranslation(
            titleEn = "Basilian Priest",
            titleFr = "Prêtre Basilien",
            dateEn = "November 6, 2025",
            dateFr = "6 novembre 2025",
            quoteEn = "Beloved brother, generous in parish pastoral care, always ready to extend a helping hand with joy and fraternal closeness.",
            quoteFr = "Frère bien-aimé, généreux dans la pastorale paroissiale, toujours prêt à tendre la main avec joie et proximité fraternelle."
        ),
        "dec_walsh_joseph" to DeceasedTranslation(
            titleEn = "Basilian Priest",
            titleFr = "Prêtre Basilien",
            dateEn = "November 6, 2025",
            dateFr = "6 novembre 2025",
            quoteEn = "Diligent servant of the Gospel, man of deep prayer and fruitful silence who cherished community life.",
            quoteFr = "Serviteur diligent de l'Évangile, homme de prière profonde et de silence fécond qui a su aimer la vie communautaire."
        ),
        "dec_canning" to DeceasedTranslation(
            titleEn = "Nonagenarian Basilian Priest",
            titleFr = "Prêtre Basilien Nonagénaire",
            dateEn = "November 19, 2025",
            dateFr = "19 novembre 2025",
            quoteEn = "Nearly a century of life filled with grace, fidelity to Saint Basil's charism, and joy throughout his priestly journey.",
            quoteFr = "Près d'un siècle de vie comblé de grâce, fidélité au charisme de Saint Basile et joie dans son cheminement sacerdotal."
        ),
        "dec_wheeler" to DeceasedTranslation(
            titleEn = "Basilian Priest",
            titleFr = "Prêtre Basilien",
            dateEn = "December 9, 2025",
            dateFr = "9 décembre 2025",
            quoteEn = "Affability, academic wisdom, and a heart grateful for the heritage received from the founders of Annonay.",
            quoteFr = "Affabilité, sagesse académique et un cœur reconnaissant pour l'héritage reçu des fondateurs d'Annonay."
        ),
        "dec_thompson_vicente" to DeceasedTranslation(
            titleEn = "Missionary in Colombia for 12 years",
            titleFr = "Missionnaire en Colombie pendant 12 ans",
            dateEn = "2016",
            dateFr = "2016",
            quoteEn = "Dedicated twelve years of his life among Colombians, offering an exemplary witness to the presence and tender love of Jesus Christ.",
            quoteFr = "A consacré douze années de sa vie parmi les Colombiens, témoignant de façon exemplaire de la présence et de l'amour de Jésus-Christ."
        ),
        "dec_platt_felipe" to DeceasedTranslation(
            titleEn = "Author of Triduums and Vademecum in Colombia",
            titleFr = "Auteur de Triduums et Vademecum en Colombie",
            dateEn = "In God's Glory",
            dateFr = "Dans la Gloire de Dieu",
            quoteEn = "With generosity and dedication, composed and bequeathed the devotions, prayers, and triduums that unite our community in Colombia today.",
            quoteFr = "Avec générosité et dévouement, a rédigé et légué les dévotions, prières et triduums qui unissent aujourd'hui la communauté en Colombie.",
            yearsEn = "Missionary Tradition",
            yearsFr = "Tradition Missionnaire"
        ),
        "dec_lapierre" to DeceasedTranslation(
            titleEn = "First Superior General (1822-1838)",
            titleFr = "Premier Supérieur Général (1822-1838)",
            dateEn = "August 16, 1838",
            dateFr = "16 août 1838",
            quoteEn = "Man of heroic prudence during the French Revolution. Founded the clandestine school of Saint-Symphorien and guided the first ten.",
            quoteFr = "Homme d'une prudence héroïque pendant la Révolution française. Fonda l'école clandestine de Saint-Symphorien et guida les dix premiers."
        ),
        "dec_tourvieille" to DeceasedTranslation(
            titleEn = "Second Superior General (1838-1859)",
            titleFr = "Deuxième Supérieur Général (1838-1859)",
            dateEn = "August 6, 1859",
            dateFr = "6 août 1859",
            quoteEn = "Great mathematician and educator. Advanced the transatlantic mission by sending the first Basilians to Canada in 1852.",
            quoteFr = "Grand mathématicien et éducateur. A promu la mission transatlantique en envoyant les premiers Basiliens au Canada en 1852."
        ),
        "dec_tracol" to DeceasedTranslation(
            titleEn = "First Historian and Secretary General (37 years)",
            titleFr = "Premier Historien et Secrétaire Général (37 ans)",
            dateEn = "June 3, 1885",
            dateFr = "3 juin 1885",
            quoteEn = "The last of the old guard: modest, wise, custodian of the memory and sacred history of our origins.",
            quoteFr = "Le dernier de la vieille garde: modeste, sage, gardien de la mémoire et de l'histoire sacrée de nos origines."
        ),
        "dec_duret" to DeceasedTranslation(
            titleEn = "Founding Father • First Assistant",
            titleFr = "Père Fondateur • Premier Assistant",
            dateEn = "June 3, 1841",
            dateFr = "3 juin 1841",
            quoteEn = "Man of peace and utter prudence. Cared for poor vocations and the Sisters of Providence guarded his heart.",
            quoteFr = "Homme de paix et de prudence absolue. Prit soin des vocations pauvres et les sœurs de la Providence gardèrent son cœur."
        ),
        "dec_payan" to DeceasedTranslation(
            titleEn = "Founding Father • First Master of Novices",
            titleFr = "Père Fondateur • Premier Maître des Novices",
            dateEn = "April 8, 1847",
            dateFr = "8 avril 1847",
            quoteEn = "Described as kindness personified. Spiritual director and tireless confessor of young students and seminarians.",
            quoteFr = "Décrit comme la bonté incarnée. Directeur spirituel et confesseur inlassable des jeunes étudiants et séminaristes."
        ),
        "dec_polly" to DeceasedTranslation(
            titleEn = "Founding Father • Second Assistant",
            titleFr = "Père Fondateur • Deuxième Assistant",
            dateEn = "March 28, 1846",
            dateFr = "28 mars 1846",
            quoteEn = "Former mayor who risked his life protecting priests. Iron constitution, warm heart, and methodical teacher.",
            quoteFr = "Ancien maire ayant risqué sa vie pour protéger des prêtres. Constitution de fer, cœur chaleureux et professeur méthodique."
        ),
        "dec_pages" to DeceasedTranslation(
            titleEn = "Founding Father",
            titleFr = "Père Fondateur",
            dateEn = "August 27, 1861",
            dateFr = "27 août 1861",
            quoteEn = "A giant with a monk's demeanor. Man of rigorous prayer, fasting, and overflowing charity toward the most disadvantaged.",
            quoteFr = "Un géant à l'allure de moine. Homme de prière rigoureuse, de jeûne et d'une charité débordante envers les plus démunis."
        ),
        "dec_martinesche" to DeceasedTranslation(
            titleEn = "Founding Father • Apostle of Vaudevant",
            titleFr = "Père Fondateur • Apôtre de Vaudevant",
            dateEn = "February 24, 1879",
            dateFr = "24 février 1879",
            quoteEn = "Deprived himself of necessities to feed and clothe the poor. In his parish he was loved as a father and venerated as a saint.",
            quoteFr = "Se privait du nécessaire pour nourrir et vêtir les pauvres. Dans sa paroisse, il était aimé comme un père et vénéré comme un saint."
        ),
        "dec_vallon" to DeceasedTranslation(
            titleEn = "Founding Father",
            titleFr = "Père Fondateur",
            dateEn = "August 13, 1840",
            dateFr = "13 août 1840",
            quoteEn = "Living example of religious rule and discipline. Surrendered his soul at the foot of the altar where he was to celebrate Holy Mass.",
            quoteFr = "Exemple vivant de la règle religieuse et de la discipline. Rendit son âme au pied de l'autel où il devait célébrer la Sainte Messe."
        ),
        "dec_fayolle" to DeceasedTranslation(
            titleEn = "Founding Father • Superior of Collège de Privas",
            titleFr = "Père Fondateur • Supérieur du Collège de Privas",
            dateEn = "April 27, 1867",
            dateFr = "27 avril 1867",
            quoteEn = "More a father than a teacher to his students. Created a true family around the table and the altar.",
            quoteFr = "Plus un père qu'un maître pour ses élèves. Créa une véritable famille autour de la table et de l'autel."
        ),
        "dec_actorie" to DeceasedTranslation(
            titleEn = "Third Superior General (1859-1864)",
            titleFr = "Troisième Supérieur Général (1859-1864)",
            dateEn = "1864",
            dateFr = "1864",
            quoteEn = "Guarded the expansion of the first houses and the faithful living of the Constitutions.",
            quoteFr = "Veilla sur l'expansion des premières maisons et le respect fidèle des Constitutions."
        ),
        "dec_soulerin" to DeceasedTranslation(
            titleEn = "Fourth Superior General (1865-1879) • Pioneer in Toronto",
            titleFr = "Quatrième Supérieur Général (1865-1879) • Pionnier à Toronto",
            dateEn = "1879",
            dateFr = "1879",
            quoteEn = "First superior of St. Michael's College in Toronto in 1852. Built the bridge between Europe and the New World.",
            quoteFr = "Premier supérieur de St. Michael's College à Toronto en 1852. A jeté un pont entre l'Europe et le Nouveau Monde."
        ),
        "dec_carr_henry" to DeceasedTranslation(
            titleEn = "Superior General of Toronto (1930-1942)",
            titleFr = "Supérieur Général de Toronto (1930-1942)",
            dateEn = "1963",
            dateFr = "1963",
            quoteEn = "Visionary of Catholic higher education in Canada. Integrated Catholic colleges into secular universities.",
            quoteFr = "Visionnaire de l'enseignement supérieur catholique au Canada. Intégra les collèges catholiques aux universités laïques."
        ),
        "dec_flahiff_cardinal" to DeceasedTranslation(
            titleEn = "Superior General (1954-1961) • Cardinal Archbishop of Winnipeg",
            titleFr = "Supérieur Général (1954-1961) • Cardinal Archevêque de Winnipeg",
            dateEn = "1989",
            dateFr = "1989",
            quoteEn = "Achieved the 1955 Decree of Reunification between Basilians of France and North America. Council Father at Vatican II.",
            quoteFr = "Obtint le Décret de Réunification de 1955 entre les Basiliens de France et d'Amérique du Nord. Père conciliaire à Vatican II."
        ),
        "dec_decker_kenneth" to DeceasedTranslation(
            titleEn = "Superior General (2002-2010)",
            titleFr = "Supérieur Général (2002-2010)",
            dateEn = "2015",
            dateFr = "2015",
            quoteEn = "Founder of 'Becoming Neighbors' for immigrants and refugees. Great promoter of solidarity and social justice.",
            quoteFr = "Fondateur de 'Becoming Neighbors' pour les migrants et réfugiés. Grand promoteur de la solidarité et de la justice sociale."
        ),
        "dec_smith_george" to DeceasedTranslation(
            titleEn = "Superior General (2010-2018)",
            titleFr = "Supérieur Général (2010-2018)",
            dateEn = "2020",
            dateFr = "2020",
            quoteEn = "Fraternal leader, promoter of vocational pastoral care in Colombia and Mexico, and guardian of the charism in the new millennium.",
            quoteFr = "Leader fraternel, promoteur de la pastorale vocationnelle en Colombie et au Mexique, et gardien du charisme dans le nouveau millénaire."
        ),
        "dec_vincent_charles" to DeceasedTranslation(
            titleEn = "Pioneer and Provincial of Toronto",
            titleFr = "Pionnier et Provincial de Toronto",
            dateEn = "1898",
            dateFr = "1898",
            quoteEn = "First novice ordained in Canada. Consolidated St. Michael's College and the formation of generations of priests.",
            quoteFr = "Premier novice ordonné au Canada. A consolidé le St. Michael's College et la formation de générations de prêtres."
        ),
        "dec_oconnor_denis" to DeceasedTranslation(
            titleEn = "Archbishop of Toronto • Bishop of London (Ontario)",
            titleFr = "Archevêque de Toronto • Évêque de London (Ontario)",
            dateEn = "1911",
            dateFr = "1911",
            quoteEn = "First Basilian bishop and archbishop in North America. Man of unyielding principles and great pastoral zeal.",
            quoteFr = "Premier évêque et archevêque basilien en Amérique du Nord. Homme aux principes inébranlables et au grand zèle pastoral."
        ),
        "dec_charbonnel_armand" to DeceasedTranslation(
            titleEn = "Bishop of Toronto • Protector of the Basilians",
            titleFr = "Évêque de Toronto • Protecteur des Basiliens",
            dateEn = "1891",
            dateFr = "1891",
            quoteEn = "Invited his former teachers from Annonay to establish the Toronto mission in 1852. Later donned the Capuchin habit in France.",
            quoteFr = "Invita ses anciens maîtres d'Annonay à fonder la mission de Toronto en 1852. Prit plus tard l'habit capucin en France."
        ),
        "dec_mohan_thomas" to DeceasedTranslation(
            titleEn = "Rector and Missionary in Colombia",
            titleFr = "Recteur et Missionnaire en Colombie",
            dateEn = "2013",
            dateFr = "2013",
            quoteEn = "Tirelessly supported the missions in Cali and Bogotá with warm charism, generosity, and priestly joy.",
            quoteFr = "Soutint inlassablement les missions de Cali et Bogotá avec un charisme chaleureux, générosité et joie sacerdotale."
        ),
        "dec_macdonell_ronald" to DeceasedTranslation(
            titleEn = "Missionary and Linguist in Latin America",
            titleFr = "Missionnaire et Linguiste en Amérique Latine",
            dateEn = "2018",
            dateFr = "2018",
            quoteEn = "Dedicated his life to indigenous communities and inculturated evangelization in Latin America with deep evangelical love.",
            quoteFr = "Consacra sa vie aux communautés indigènes et à l'évangélisation inculturée en Amérique Latine avec un profond amour évangélique."
        ),
        "dec_coughlin_leo" to DeceasedTranslation(
            titleEn = "Formator of Missionaries",
            titleFr = "Formateur de Missionnaires",
            dateEn = "2013",
            dateFr = "2013",
            quoteEn = "Accompanied the first Colombian Basilian priests on their formative journey with patience and wisdom.",
            quoteFr = "Accompagna avec patience et sagesse les premiers prêtres basiliens colombiens dans leur cheminement formatif."
        ),
        "dec_phelan_george" to DeceasedTranslation(
            titleEn = "Co-founder of the Pontifical Institute of Mediaeval Studies (PIMS)",
            titleFr = "Co-fondateur du Pontifical Institute of Mediaeval Studies (PIMS)",
            dateEn = "1961",
            dateFr = "1961",
            quoteEn = "Illustrious philosopher and Thomistic theologian. Brought the thought of Saint Basil and Saint Thomas to the heights of world academia.",
            quoteFr = "Illustre philosophe et théologien thomiste. Porta la pensée de Saint Basile et Saint Thomas au sommet académique mondial."
        ),
        "dec_mcconica_james" to DeceasedTranslation(
            titleEn = "Oxford Historian and President of PIMS",
            titleFr = "Historien d'Oxford et Président du PIMS",
            dateEn = "2021",
            dateFr = "2021",
            quoteEn = "Officer of the Order of Canada and Erasmian scholar. United the highest scholarship with piety and priestly humility.",
            quoteFr = "Officier de l'Ordre du Canada et érudit érasmien. Unit la plus haute science à la piété et à l'humilité sacerdotale."
        ),
        "dec_irwin_william" to DeceasedTranslation(
            titleEn = "Founder of Catholic Social Services",
            titleFr = "Fondateur des Services Sociaux Catholiques",
            dateEn = "1997",
            dateFr = "1997",
            quoteEn = "Inspired by Saint Basil's Basiliad, founded shelters and charities for the homeless, women, and refugees in Edmonton.",
            quoteFr = "Inspiré par la Basiliade de Saint Basile, fonda des œuvres d'accueil pour les sans-abri, les femmes et les réfugiés à Edmonton."
        ),
        "dec_holmes_norman" to DeceasedTranslation(
            titleEn = "Priest and Educator",
            titleFr = "Prêtre et Éducateur",
            dateEn = "2022",
            dateFr = "2022",
            quoteEn = "Faithful pastor in Basilian parishes and schools, remembered for his fatherly kindness and comforting smile.",
            quoteFr = "Fidèle pasteur dans les paroisses et collèges basiliens, dont on se souvient pour sa bonté paternelle et son sourire réconfortant."
        )
    )

    fun getLocalizedTitle(brother: DeceasedBrother, lang: AppLanguage): String {
        val tr = deceasedTranslations[brother.id]
        if (tr != null) {
            return when (lang) {
                AppLanguage.EN -> tr.titleEn
                AppLanguage.FR -> tr.titleFr
                else -> brother.titleOrRole
            }
        }
        return when (lang) {
            AppLanguage.EN -> brother.titleOrRole
                .replace("Sacerdote Basiliano (Tradición Francesa • Necrología Oficial)", "Basilian Priest (French Tradition • Official Necrology)")
                .replace("Sacerdote Basiliano (Necrología Oficial C.S.B.)", "Basilian Priest (Official C.S.B. Necrology)")
                .replace("Superior / Padre Basiliano Ilustre (Necrología Oficial)", "Very Rev. Basilian Superior (Official Necrology)")
                .replace("Escolástico / Hermano Basiliano en Formación (Necrología Oficial)", "Basilian Scholastic / Brother in Formation (Official Necrology)")
                .replace("Obispo / Prelado Basiliano (Necrología Oficial)", "Basilian Bishop / Prelate (Official Necrology)")
                .replace("Sacerdote Basiliano", "Basilian Priest")
            AppLanguage.FR -> brother.titleOrRole
                .replace("Sacerdote Basiliano (Tradición Francesa • Necrología Oficial)", "Prêtre Basilien (Tradition Française • Nécrologie Officielle)")
                .replace("Sacerdote Basiliano (Necrología Oficial C.S.B.)", "Prêtre Basilien (Nécrologie Officielle C.S.B.)")
                .replace("Superior / Padre Basiliano Ilustre (Necrología Oficial)", "Très Révérend Père Basilien (Nécrologie Officielle)")
                .replace("Escolástico / Hermano Basiliano en Formación (Necrología Oficial)", "Scolastique / Frère Basilien en Formation (Nécrologie Officielle)")
                .replace("Obispo / Prelado Basiliano (Necrología Oficial)", "Évêque / Prélat Basilien (Nécrologie Officielle)")
                .replace("Sacerdote Basiliano", "Prêtre Basilien")
            else -> brother.titleOrRole
        }
    }

    fun getLocalizedDatePassed(brother: DeceasedBrother, lang: AppLanguage): String {
        val tr = deceasedTranslations[brother.id]
        if (tr != null) {
            return when (lang) {
                AppLanguage.EN -> tr.dateEn
                AppLanguage.FR -> tr.dateFr
                else -> brother.datePassed
            }
        }
        return when (lang) {
            AppLanguage.EN -> brother.datePassed
                .replace(" de enero de ", " January ")
                .replace(" de febrero de ", " February ")
                .replace(" de marzo de ", " March ")
                .replace(" de abril de ", " April ")
                .replace(" de mayo de ", " May ")
                .replace(" de junio de ", " June ")
                .replace(" de julio de ", " July ")
                .replace(" de agosto de ", " August ")
                .replace(" de septiembre de ", " September ")
                .replace(" de octubre de ", " October ")
                .replace(" de noviembre de ", " November ")
                .replace(" de diciembre de ", " December ")
            AppLanguage.FR -> brother.datePassed
                .replace(" de enero de ", " janvier ")
                .replace(" de febrero de ", " février ")
                .replace(" de marzo de ", " mars ")
                .replace(" de abril de ", " avril ")
                .replace(" de mayo de ", " mai ")
                .replace(" de junio de ", " juin ")
                .replace(" de julio de ", " juillet ")
                .replace(" de agosto de ", " août ")
                .replace(" de septiembre de ", " septembre ")
                .replace(" de octubre de ", " octobre ")
                .replace(" de noviembre de ", " novembre ")
                .replace(" de diciembre de ", " décembre ")
            else -> brother.datePassed
        }
    }

    fun getLocalizedYears(brother: DeceasedBrother, lang: AppLanguage): String {
        val tr = deceasedTranslations[brother.id]
        if (tr?.yearsEn != null && lang == AppLanguage.EN) return tr.yearsEn
        if (tr?.yearsFr != null && lang == AppLanguage.FR) return tr.yearsFr
        return brother.birthAndDeathYears
    }

    fun getLocalizedQuote(brother: DeceasedBrother, lang: AppLanguage): String {
        val tr = deceasedTranslations[brother.id]
        if (tr != null) {
            return when (lang) {
                AppLanguage.EN -> tr.quoteEn
                AppLanguage.FR -> tr.quoteFr
                else -> brother.memorialQuote
            }
        }
        return when (lang) {
            AppLanguage.EN -> "«Laudemus viros gloriosos — Let us now praise famous men.» Remembered in the Official Basilian Necrology for his fidelity to goodness, discipline, and knowledge."
            AppLanguage.FR -> "«Laudemus viros gloriosos — Faisons l’éloge des hommes illustres.» Commémoré dans la Nécrologie Basilienne Officielle pour sa fidélité à la bonté, la discipline et la science."
            else -> brother.memorialQuote
        }
    }

    fun getLocalizedName(brother: DeceasedBrother, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.EN -> {
                brother.name
                    .replace("P. ", "Fr. ")
                    .replace("Mons. ", "Bishop ")
                    .replace("Cardenal ", "Cardinal ")
            }
            AppLanguage.FR -> {
                brother.name
                    .replace("P. ", "Père ")
                    .replace("Mons. ", "Mgr ")
                    .replace("Cardenal ", "Cardinal ")
            }
            else -> brother.name
        }
    }

    fun getLocalizedCategoryLabel(category: DeceasedCategory, lang: AppLanguage): String {
        return when (category) {
            DeceasedCategory.TODOS -> when (lang) {
                AppLanguage.EN -> "All"
                AppLanguage.FR -> "Tous"
                else -> "Todos"
            }
            DeceasedCategory.RECENTES_2025_2026 -> when (lang) {
                AppLanguage.EN -> "Recent Easter (2022-2026)"
                AppLanguage.FR -> "Pâques Récentes (2022-2026)"
                else -> "Pascua Reciente (2022-2026)"
            }
            DeceasedCategory.COLOMBIA_MEMORIA -> when (lang) {
                AppLanguage.EN -> "Colombia & LatAm Mission"
                AppLanguage.FR -> "Mission Colombie & Am. Latine"
                else -> "Misión Colombia & LatAm"
            }
            DeceasedCategory.FUNDADORES_1822 -> when (lang) {
                AppLanguage.EN -> "10 Founding Fathers (1822)"
                AppLanguage.FR -> "10 Pères Fondateurs (1822)"
                else -> "10 Padres Fundadores (1822)"
            }
            DeceasedCategory.SUPERIORES_GENERALES -> when (lang) {
                AppLanguage.EN -> "Superiors & Bishops"
                AppLanguage.FR -> "Supérieurs & Évêques"
                else -> "Superiores & Obispos"
            }
            DeceasedCategory.NECROLOGIA_GENERAL -> when (lang) {
                AppLanguage.EN -> "Historical Necrology (1822-2026)"
                AppLanguage.FR -> "Nécrologie Historique (1822-2026)"
                else -> "Necrología Histórica (1822-2026)"
            }
        }
    }

    // Curia and Council members localization
    fun getLocalizedCouncilRole(member: BasilianCouncilMember, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.EN -> when {
                member.roleTitle.contains("Superior General", ignoreCase = true) && !member.roleTitle.contains("Vicario") -> "Superior General"
                member.roleTitle.contains("Vicario General") -> "Vicar General & 1st Councillor"
                member.roleTitle.contains("Segundo Consejero") -> "Second General Councillor"
                member.roleTitle.contains("Tercer Consejero") -> "Third General Councillor"
                member.roleTitle.contains("Cuarto Consejero") -> "Fourth General Councillor"
                member.roleTitle.contains("Secretario General") -> "General Secretary"
                member.roleTitle.contains("Tesorero General") || member.roleTitle.contains("Económo") -> "General Treasurer"
                member.roleTitle.contains("Procurador") -> "Procurator to the Holy See"
                else -> member.roleTitle
            }
            AppLanguage.FR -> when {
                member.roleTitle.contains("Superior General", ignoreCase = true) && !member.roleTitle.contains("Vicario") -> "Supérieur Général"
                member.roleTitle.contains("Vicario General") -> "Vicaire Général & 1er Conseiller"
                member.roleTitle.contains("Segundo Consejero") -> "Deuxième Conseiller Général"
                member.roleTitle.contains("Tercer Consejero") -> "Troisième Conseiller Général"
                member.roleTitle.contains("Cuarto Consejero") -> "Quatrième Conseiller Général"
                member.roleTitle.contains("Secretario General") -> "Secrétaire Général"
                member.roleTitle.contains("Tesorero General") || member.roleTitle.contains("Económo") -> "Économe Général"
                member.roleTitle.contains("Procurador") -> "Procureur auprès du Saint-Siège"
                else -> member.roleTitle
            }
            else -> member.roleTitle
        }
    }

    fun getLocalizedCouncilDescription(member: BasilianCouncilMember, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.EN -> when (member.id) {
                "c_huber" -> "Elected Superior General of the Congregation of St. Basil at the 2026 General Chapter."
                "c_restivo" -> "First General Councillor and Vicar. Overseer of the mission in Colombia and formation of novices."
                "c_timm" -> "Second General Councillor. Parish pastoral leadership in North America."
                "c_baker" -> "Third General Councillor. Educational apostolate and youth ministry."
                "c_hanson" -> "Fourth General Councillor. Academic formation and institutional mission."
                "c_chinnappa" -> "General Secretary of the Congregation. Official communications and chapter acts."
                "c_smith_treasurer" -> "General Treasurer. Stewardship of congregational resources and works."
                "c_procurator" -> "Procurator to the Holy See. Liaison with Vatican dicasteries in Rome."
                else -> member.description
            }
            AppLanguage.FR -> when (member.id) {
                "c_huber" -> "Élu Supérieur Général de la Congrégation de Saint-Basile au Chapitre Général 2026."
                "c_restivo" -> "Premier Conseiller Général et Vicaire. Gardien de la mission en Colombie et formation des novices."
                "c_timm" -> "Deuxième Conseiller Général. Pastorale paroissiale en Amérique du Nord."
                "c_baker" -> "Troisième Conseiller Général. Apostolat éducatif et pastorale des jeunes."
                "c_hanson" -> "Quatrième Conseiller Général. Formation académique et mission institutionnelle."
                "c_chinnappa" -> "Secrétaire Général de la Congrégation. Communications officielles et actes capitulaires."
                "c_smith_treasurer" -> "Économe Général. Gestion des ressources et des œuvres de la congrégation."
                "c_procurator" -> "Procureur auprès du Saint-Siège. Relations avec les dicastères romains."
                else -> member.description
            }
            else -> member.description
        }
    }

    fun getLocalizedCouncilTerm(member: BasilianCouncilMember, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.EN -> member.term.replace("presente (Capítulo General 2026)", "present (General Chapter 2026)")
            AppLanguage.FR -> member.term.replace("presente (Capítulo General 2026)", "présent (Chapitre Général 2026)")
            else -> member.term
        }
    }

    fun getLocalizedCouncilLocation(member: BasilianCouncilMember, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.EN -> member.location
                .replace("Curia Generalicia", "General Curia")
                .replace("Canadá", "Canada")
                .replace("Casa Annonay", "Annonay House")
                .replace("Bogotá", "Bogota")
            AppLanguage.FR -> member.location
                .replace("Curia Generalicia", "Curie Générale")
                .replace("Casa Annonay", "Maison Annonay")
            else -> member.location
        }
    }

    // Directory member roles localization
    fun getLocalizedMemberRole(role: String, lang: AppLanguage): String {
        if (role.isBlank()) return ""
        return when (lang) {
            AppLanguage.EN -> {
                role.replace("Párroco y Formador", "Pastor & Formator")
                    .replace("Párroco", "Pastor")
                    .replace("Vicario Parroquial", "Parochial Vicar")
                    .replace("Vicario General de la Congregación de San Basilio", "Vicar General of the Congregation of St. Basil")
                    .replace("Primer Consultor General", "First General Consultor")
                    .replace("Vicario", "Vicar")
                    .replace("Superior Local", "Local Superior")
                    .replace("Superior", "Superior")
                    .replace("Educador", "Educator")
                    .replace("Profesor", "Professor")
                    .replace("Formador", "Formator")
                    .replace("Maestro de Novicios", "Novice Master")
                    .replace("Novicio Basiliano", "Basilian Novice")
                    .replace("Novicio", "Novice")
                    .replace("Escolástico Basiliano en Formación Teológica", "Basilian Scholastic in Theological Formation")
                    .replace("Escolástico", "Scholastic")
                    .replace("Estudiante", "Student")
                    .replace("Capellán", "Chaplain")
                    .replace("Misionero", "Missionary")
                    .replace("Rector", "Rector")
                    .replace("Consejero", "Councillor")
                    .replace("Económo", "Treasurer")
            }
            AppLanguage.FR -> {
                role.replace("Párroco y Formador", "Curé & Formateur")
                    .replace("Párroco", "Curé")
                    .replace("Vicario Parroquial", "Vicaire Paroissial")
                    .replace("Vicario General de la Congregación de San Basilio", "Vicaire Général de la Congrégation de Saint-Basile")
                    .replace("Primer Consultor General", "Premier Consultor Général")
                    .replace("Vicario", "Vicaire")
                    .replace("Superior Local", "Supérieur Local")
                    .replace("Superior", "Supérieur")
                    .replace("Educador", "Éducateur")
                    .replace("Profesor", "Professeur")
                    .replace("Formador", "Formateur")
                    .replace("Maestro de Novicios", "Maître des Novices")
                    .replace("Novicio Basiliano", "Novice Basilien")
                    .replace("Novicio", "Novice")
                    .replace("Escolástico Basiliano en Formación Teológica", "Scolastique Basilien en Formation Théologique")
                    .replace("Escolástico", "Scolastique")
                    .replace("Estudiante", "Étudiant")
                    .replace("Capellán", "Aumônier")
                    .replace("Misionero", "Missionnaire")
                    .replace("Rector", "Recteur")
                    .replace("Consejero", "Conseiller")
                    .replace("Económo", "Économe")
            }
            else -> role
        }
    }

    // Profession date localization
    fun getLocalizedProfessionDate(rawDate: String, lang: AppLanguage): String {
        if (rawDate.isBlank()) return ""
        return when (lang) {
            AppLanguage.EN -> {
                rawDate.replace("Noviciado Canónico", "Canonical Novitiate")
                    .replace("(Primera Profesión de Votos)", "(First Profession of Vows)")
                    .replace("Profesión Perpetua", "Perpetual Profession")
                    .replace("Enero", "January")
                    .replace("Febrero", "February")
                    .replace("Marzo", "March")
                    .replace("Abril", "April")
                    .replace("Mayo", "May")
                    .replace("Junio", "June")
                    .replace("Julio", "July")
                    .replace("Agosto", "August")
                    .replace("Septiembre", "September")
                    .replace("Octubre", "October")
                    .replace("Noviembre", "November")
                    .replace("Diciembre", "December")
            }
            AppLanguage.FR -> {
                rawDate.replace("Noviciado Canónico", "Noviciat Canonique")
                    .replace("(Primera Profesión de Votos)", "(Première Profession des Vœux)")
                    .replace("Profesión Perpetua", "Profession Perpétuelle")
                    .replace("January", "Janvier").replace("Enero", "Janvier")
                    .replace("February", "Février").replace("Febrero", "Février")
                    .replace("March", "Mars").replace("Marzo", "Mars")
                    .replace("April", "Avril").replace("Abril", "Avril")
                    .replace("May", "Mai").replace("Mayo", "Mai")
                    .replace("June", "Juin").replace("Junio", "Juin")
                    .replace("July", "Juillet").replace("Julio", "Juillet")
                    .replace("August", "Août").replace("Agosto", "Août")
                    .replace("September", "Septembre").replace("Septiembre", "Septembre")
                    .replace("October", "Octobre").replace("Octubre", "Octobre")
                    .replace("November", "Novembre").replace("Noviembre", "Novembre")
                    .replace("December", "Décembre").replace("Diciembre", "Décembre")
            }
            else -> rawDate
        }
    }

    // Ordination date localization
    fun getLocalizedOrdinationDate(rawDate: String, lang: AppLanguage): String {
        if (rawDate.isBlank()) return ""
        return when (lang) {
            AppLanguage.EN -> {
                rawDate.replace("Enero", "January")
                    .replace("Febrero", "February")
                    .replace("Marzo", "March")
                    .replace("Abril", "April")
                    .replace("Mayo", "May")
                    .replace("Junio", "June")
                    .replace("Julio", "July")
                    .replace("Agosto", "August")
                    .replace("Septiembre", "September")
                    .replace("Octubre", "October")
                    .replace("Noviembre", "November")
                    .replace("Diciembre", "December")
            }
            AppLanguage.FR -> {
                rawDate.replace("January", "Janvier").replace("Enero", "Janvier")
                    .replace("February", "Février").replace("Febrero", "Février")
                    .replace("March", "Mars").replace("Marzo", "Mars")
                    .replace("April", "Avril").replace("Abril", "Avril")
                    .replace("May", "Mai").replace("Mayo", "Mai")
                    .replace("June", "Juin").replace("Junio", "Juin")
                    .replace("July", "Juillet").replace("Julio", "Juillet")
                    .replace("August", "Août").replace("Agosto", "Août")
                    .replace("September", "Septembre").replace("Septiembre", "Septembre")
                    .replace("October", "Octobre").replace("Octubre", "Octobre")
                    .replace("November", "Novembre").replace("Noviembre", "Novembre")
                    .replace("December", "Décembre").replace("Diciembre", "Décembre")
            }
            else -> rawDate
        }
    }

    // House name localization
    fun getLocalizedHouseName(rawName: String, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.EN -> {
                rawName.replace("Curia Generalicia Basiliana", "Basilian General Curia")
                    .replace("Casa Annonay & Noviciado Internacional", "Annonay House & International Novitiate")
                    .replace("Noviciado Casa Annonay", "Annonay House Novitiate")
                    .replace("Casa Annonay", "Annonay House")
                    .replace("Comunidad & Parroquia San Basilio", "St. Basil Community & Parish")
                    .replace("Escolasticado Basiliano San Basilio", "St. Basil Basilian Scholasticate")
                    .replace("Escolasticado Basiliano", "Basilian Scholasticate")
                    .replace("Curia Generalicia", "General Curia")
                    .replace("Casa de Formación / Teologado", "Formation House / Theologate")
                    .replace("Parroquia Nuestra Señora de Egipto", "Our Lady of Egypt Parish")
                    .replace("Comunidad de Annonay (Cuna Fundacional 1822)", "Annonay Community (1822 Founding Cradle)")
            }
            AppLanguage.FR -> {
                rawName.replace("Curia Generalicia Basiliana", "Curie Générale Basilienne")
                    .replace("Casa Annonay & Noviciado Internacional", "Maison Annonay & Noviciat International")
                    .replace("Noviciado Casa Annonay", "Noviciat Maison Annonay")
                    .replace("Casa Annonay", "Maison Annonay")
                    .replace("Comunidad & Parroquia San Basilio", "Communauté & Paroisse Saint-Basile")
                    .replace("Escolasticado Basiliano San Basilio", "Scholasticat Basilien Saint-Basile")
                    .replace("Escolasticado Basiliano", "Scholasticat Basilien")
                    .replace("Curia Generalicia", "Curie Générale")
                    .replace("Casa de Formación / Teologado", "Maison de Formation / Théologat")
                    .replace("Parroquia Nuestra Señora de Egipto", "Paroisse Notre-Dame d'Égypte")
                    .replace("Comunidad de Annonay (Cuna Fundacional 1822)", "Communauté d'Annonay (Berceau Fondateur 1822)")
            }
            else -> rawName
        }
    }

    // Superior / Rector title localization
    fun getLocalizedSuperior(rawSuperior: String, lang: AppLanguage): String {
        if (rawSuperior.isBlank()) return ""
        return when (lang) {
            AppLanguage.EN -> {
                rawSuperior.replace("(Superior General)", "(Superior General)")
                    .replace("(Maestro de Novicios)", "(Novice Master)")
                    .replace("(Párroco)", "(Pastor)")
                    .replace("(Rector)", "(Rector)")
                    .replace("(Superior Local)", "(Local Superior)")
                    .replace("(Superior)", "(Superior)")
                    .replace("Muy Rev. P.", "Very Rev. Fr.")
                    .replace("Rev. P.", "Rev. Fr.")
            }
            AppLanguage.FR -> {
                rawSuperior.replace("(Superior General)", "(Supérieur Général)")
                    .replace("(Maestro de Novicios)", "(Maître des Novices)")
                    .replace("(Párroco)", "(Curé)")
                    .replace("(Rector)", "(Recteur)")
                    .replace("(Superior Local)", "(Supérieur Local)")
                    .replace("(Superior)", "(Supérieur)")
                    .replace("Muy Rev. P.", "Très Rév. P.")
                    .replace("Rev. P.", "Rév. P.")
            }
            else -> rawSuperior
        }
    }

    // House category localization
    fun getLocalizedHouseCategory(category: String, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.EN -> when (category) {
                "Sede de Gobierno General" -> "General Governance Headquarters"
                "Centro Internacional de Misiones" -> "International Mission Center"
                "Comunidad Residencial" -> "Residential Community"
                "Residencia de Cuidado & Ancianos" -> "Eldercare & Assisted Living Residence"
                "Santuario Histórico & Cuna Fundacional" -> "Historic Sanctuary & Founding Cradle"
                "Curia Generalicia" -> "General Curia"
                "Comunidad" -> "Community"
                "Parroquia" -> "Parish"
                "Colegio" -> "School / College"
                "Universidad" -> "University"
                "Noviciado" -> "Novitiate"
                "Casa de Formación" -> "House of Formation"
                "Casa de Formación & Noviciado" -> "Formation House & Novitiate"
                "Parroquia & Pastoral Comunitaria" -> "Parish & Community Pastoral"
                "Casa de Estudios Teológicos & Filosofía" -> "House of Theological & Philosophical Studies"
                "Casa de Retiro" -> "Retreat House"
                else -> category
            }
            AppLanguage.FR -> when (category) {
                "Sede de Gobierno General" -> "Siège du Gouvernement Général"
                "Centro Internacional de Misiones" -> "Centre International des Missions"
                "Comunidad Residencial" -> "Communauté Résidentielle"
                "Residencia de Cuidado & Ancianos" -> "Résidence de Soins & Aînés"
                "Santuario Histórico & Cuna Fundacional" -> "Sanctuaire Historique & Berceau Fondateur"
                "Curia Generalicia" -> "Curie Générale"
                "Comunidad" -> "Communauté"
                "Parroquia" -> "Paroisse"
                "Colegio" -> "Collège"
                "Universidad" -> "Université"
                "Noviciado" -> "Noviciat"
                "Casa de Formación" -> "Maison de Formation"
                "Casa de Formación & Noviciado" -> "Maison de Formation & Noviciat"
                "Parroquia & Pastoral Comunitaria" -> "Paroisse & Pastorale Communautaire"
                "Casa de Estudios Teológicos & Filosofía" -> "Maison d'Études Théologiques & Philosophie"
                "Casa de Retiro" -> "Maison de Retraite"
                else -> category
            }
            else -> category
        }
    }
}
