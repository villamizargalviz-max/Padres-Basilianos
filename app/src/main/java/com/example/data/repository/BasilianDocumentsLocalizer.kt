package com.example.data.repository

import com.example.data.model.*

/**
 * Complete EN / FR / ES localizer for all Basilian historical, constitutional,
 * canonical, and archival documents:
 * - FormaDeVidaData (153 Articles, Prefaces, Decree, Abbreviations, Index)
 * - CostumbresBasilianasData (Customs 2022)
 * - Politicas2026Data (Policies & Procedures 2026)
 * - ApuntesColombiaData (Colombia Mission Notes 1987-1997)
 * - Book1922Data (1922: Before and After — Chapters, Superiors, Government Evolution)
 */
object BasilianDocumentsLocalizer {

    // =========================================================================
    // 1. FORMA DE VIDA BASILIANA (153 ARTICLES, PREFACES, DECREE, INDEX)
    // =========================================================================
    fun localizeFormaDeVidaDocument(doc: FormaDeVidaDocument, lang: AppLanguage): FormaDeVidaDocument {
        if (lang == AppLanguage.ES) return doc
        return doc.copy(
            title = when (lang) {
                AppLanguage.EN -> "The Basilian Way of Life"
                AppLanguage.FR -> "Le Mode de Vie Basilien"
                AppLanguage.ES -> doc.title
            },
            subtitle = when (lang) {
                AppLanguage.EN -> "Constitutional Basic Document of the Congregation of Priests of Saint Basil"
                AppLanguage.FR -> "Document Constitutionnel de Base de la Congrégation des Prêtres de Saint-Basile"
                AppLanguage.ES -> doc.subtitle
            },
            institution = when (lang) {
                AppLanguage.EN -> "Congregation of Saint Basil (The Basilian Fathers)"
                AppLanguage.FR -> "Congrégation de Saint-Basile (Les Pères Basiliens)"
                AppLanguage.ES -> doc.institution
            },
            cityAndYears = when (lang) {
                AppLanguage.EN -> "Toronto • 1983 • 2000 • 2002 • 2013"
                AppLanguage.FR -> "Toronto • 1983 • 2000 • 2002 • 2013"
                AppLanguage.ES -> doc.cityAndYears
            },
            prefaces = getLocalizedFvbPrefaces(lang),
            decree = getLocalizedFvbDecree(lang),
            abbreviations = getLocalizedFvbAbbreviations(lang),
            articles = doc.articles.map { localizeFvbArticle(it, lang) },
            index = getLocalizedFvbIndex(lang)
        )
    }

    fun getLocalizedFvbChapterName(chapter: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> when (chapter.trim()) {
            "El Camino del Señor" -> "The Way of the Lord"
            "Los Padres Basilianos" -> "The Basilian Fathers"
            "San Basilio Magno" -> "Saint Basil the Great"
            "El Espíritu de los Fundadores" -> "The Spirit of the Founders"
            "El Llamado de Cristo" -> "The Call of Christ"
            "Castidad" -> "Chastity"
            "Pobreza" -> "Poverty"
            "Obediencia" -> "Obedience"
            "Vida Comunitaria" -> "Community Life"
            "El Sacerdocio" -> "The Priesthood"
            "La Oración" -> "Prayer"
            "Nuestro Trabajo" -> "Our Work"
            "Vocaciones" -> "Vocations"
            "Asociados" -> "Associates"
            "El Noviciado" -> "The Novitiate"
            "Los Escolásticos" -> "The Scholastics"
            "Salidas de la Comunidad" -> "Departures from the Community"
            "Educación Continuada" -> "Ongoing Formation"
            "Gobierno General" -> "General Government"
            "El Capítulo General" -> "The General Chapter"
            "El Superior General" -> "The Superior General"
            "El Consejo General" -> "The General Council"
            "La Curia General" -> "The General Curia"
            "Gobierno Local" -> "Local Government"
            "Nuestra Forma de Vida" -> "Our Way of Life"
            else -> chapter
        }
        AppLanguage.FR -> when (chapter.trim()) {
            "El Camino del Señor" -> "Le Chemin du Seigneur"
            "Los Padres Basilianos" -> "Les Pères Basiliens"
            "San Basilio Magno" -> "Saint Basile le Grand"
            "El Espíritu de los Fundadores" -> "L'Esprit des Fondateurs"
            "El Llamado de Cristo" -> "L'Appel du Christ"
            "Castidad" -> "Chasteté"
            "Pobreza" -> "Pauvreté"
            "Obediencia" -> "Obéissance"
            "Vida Comunitaria" -> "Vie Communautaire"
            "El Sacerdocio" -> "Le Sacerdoce"
            "La Oración" -> "La Prière"
            "Nuestro Trabajo" -> "Notre Travail"
            "Vocaciones" -> "Vocations"
            "Asociados" -> "Associés"
            "El Noviciado" -> "Le Noviciat"
            "Los Escolásticos" -> "Les Scolastiques"
            "Salidas de la Comunidad" -> "Départs de la Communauté"
            "Educación Continuada" -> "Formation Permanente"
            "Gobierno General" -> "Gouvernement Général"
            "El Capítulo General" -> "Le Chapitre Général"
            "El Superior General" -> "Le Supérieur Général"
            "El Consejo General" -> "Le Conseil Général"
            "La Curia General" -> "La Curie Générale"
            "Gobierno Local" -> "Gouvernement Local"
            "Nuestra Forma de Vida" -> "Notre Forme de Vie"
            else -> chapter
        }
        AppLanguage.ES -> chapter
    }

    fun getLocalizedFvbDecree(lang: AppLanguage): List<String> = when (lang) {
        AppLanguage.EN -> listOf(
            "Congregatio Pro Religiosis et Institutis Saecularibus • Prot. n. T 67 — 1/79",
            "The Congregation of Saint Basil, headquartered in Toronto, seeks to glorify God in every form of priestly activity compatible with life in common, especially in the works of education and evangelization.",
            "In accordance with the directives of the Second Vatican Council, the Institute has prepared a new text of Constitutions, called The Basic Document. Following the favorable vote of the Congress on January 4, 1983, this decree approves and confirms the Constitutions.",
            "May fidelity to the Basilian charism lead its members to form a vital Christian community that is a living sign of simplicity and fraternity, and to establish Christian communities in which all may experience the joy and peace that come from brotherhood in Christ.",
            "Rome, May 12, Solemnity of the Ascension of Our Lord, in the year 1983. (+ E. Card. Pironio, Pref. / + Augustine Mayer, Sec.)"
        )
        AppLanguage.FR -> listOf(
            "Congregatio Pro Religiosis et Institutis Saecularibus • Prot. n. T 67 — 1/79",
            "La Congrégation de Saint-Basile, dont le siège est à Toronto, cherche à glorifier Dieu dans toute forme d'activité sacerdotale compatible avec la vie commune, spécialement dans les œuvres d'éducation et d'évangélisation.",
            "Conformément aux directives du Concile Vatican II, l'Institut a préparé un nouveau texte de Constitutions, appelé Le Document de Base. Suite au vote favorable du Congrès du 4 janvier 1983, ce décret approuve et confirme les Constitutions.",
            "Que la fidélité au charisme basilien conduise les membres à former une communauté chrétienne vivante, signe de simplicité et de fraternité, et à établir des communautés où tous puissent expérienter la joie et la paix de la fraternité dans le Christ.",
            "Rome, le 12 mai, Solennité de l'Ascension de Notre Seigneur, en l'an 1983. (+ E. Card. Pironio, Préf. / + Augustine Mayer, Sec.)"
        )
        AppLanguage.ES -> FormaDeVidaData.document.decree
    }

    fun getLocalizedFvbPrefaces(lang: AppLanguage): List<FvbPreface> = when (lang) {
        AppLanguage.EN -> listOf(
            FvbPreface(
                year = "2013",
                title = "Preface to the 2013 Edition",
                author = "Fr. George T. Smith, CSB • Superior General",
                dateOrContext = "Solemnity of Saint Basil the Great • January 2, 2013 • Toronto",
                content = listOf(
                    "The Basilian Way of Life is our Basic Constitutional Document. It was first approved by the Holy See on the Feast of the Ascension in 1983. Subsequent General Chapters made minor amendments approved by the Congregation for Institutes of Consecrated Life and Societies of Apostolic Life.",
                    "In 2012, the General Chapter approved changes to Articles 103, 128, and 131, reducing the number of General Councilors from five to four and omitting article 135 (Vatican Prot. n. T 67-1/2001). Every Basilian is invited to meditate daily upon this rule of life."
                )
            ),
            FvbPreface(
                year = "1983",
                title = "Preface to the 1983 Version",
                author = "Fr. Ulysse E. Paré, CSB • Superior General",
                dateOrContext = "Feast of Saint Basil • June 14, 1983 • Toronto",
                content = listOf(
                    "In obedience to the Second Vatican Council, the Congregation of Saint Basil began a long period of reflection and renewal. The result was The Basilian Way of Life, first adopted at the Chapter of 1977 and definitively approved by the Holy See on May 12, 1983.",
                    "This text expresses our religious and priestly identity in the language of the Gospel. May the Holy Spirit grant us to live with fidelity what we profess with joy."
                )
            ),
            FvbPreface(
                year = "2000",
                title = "Preface to the 2000 Edition",
                author = "Fr. Ronald Fabbro, CSB • Superior General",
                dateOrContext = "Feast of the Assumption • August 15, 2000 • Toronto",
                content = listOf(
                    "This edition incorporates the amendments approved by the General Chapters of 1993 and 1997 and ratified by the Holy See, adapting our governance norms while preserving intact the spiritual heart of our Rule."
                )
            ),
            FvbPreface(
                year = "2002",
                title = "Preface to the 2002 Edition",
                author = "Fr. Kenneth J. Decker, CSB • Superior General",
                dateOrContext = "Feast of the Presentation of Mary • November 21, 2002 • Toronto",
                content = listOf(
                    "The 2002 General Chapter approved an amendment to Article 104 granting greater flexibility in the selection of delegates (approved by Rome Prot. n. T 67-1/2001). Let us pray that through our fidelity to The Basilian Way of Life we may attain the maturity and fullness of Christ (Ephesians 4:13)."
                )
            )
        )
        AppLanguage.FR -> listOf(
            FvbPreface(
                year = "2013",
                title = "Préface à l'Édition de 2013",
                author = "P. George T. Smith, CSB • Supérieur Général",
                dateOrContext = "Solennité de Saint Basile le Grand • 2 janvier 2013 • Toronto",
                content = listOf(
                    "Le Mode de Vie Basilien est notre Document Constitutionnel de Base. Il a été approuvé pour la première fois par le Saint-Siège en la fête de l'Ascension 1983. Les Chapitres Généraux ultérieurs ont apporté des modifications approuvées par Rome.",
                    "En 2012, le Chapitre Général a approuvé des modifications aux articles 103, 128 et 131, réduisant le nombre de Conseillers Généraux de cinq à quatre et omettant l'article 135 (Prot. n. T 67-1/2001). Chaque Basilien est invité à méditer quotidiennement cette règle de vie."
                )
            ),
            FvbPreface(
                year = "1983",
                title = "Préface à la Version de 1983",
                author = "P. Ulysse E. Paré, CSB • Supérieur Général",
                dateOrContext = "Fête de Saint Basile • 14 juin 1983 • Toronto",
                content = listOf(
                    "En obéissance au Concile Vatican II, la Congrégation de Saint-Basile a entrepris une longue période de réflexion et de renouveau. Le fruit en est Le Mode de Vie Basilien, adopté au Chapitre de 1977 et approuvé définitivement par le Saint-Siège le 12 mai 1983.",
                    "Ce texte exprime notre identité religieuse et sacerdotale dans le langage de l'Évangile. Que l'Esprit Saint nous accorde de vivre fidèlement ce que nous professons avec joie."
                )
            ),
            FvbPreface(
                year = "2000",
                title = "Préface à l'Édition de 2000",
                author = "P. Ronald Fabbro, CSB • Supérieur Général",
                dateOrContext = "Fête de l'Assomption • 15 août 2000 • Toronto",
                content = listOf(
                    "Cette édition intègre les amendements approuvés par les Chapitres Généraux de 1993 et 1997 et ratifiés par le Saint-Siège, adaptant nos normes de gouvernement tout en préservant le cœur spirituel de notre Règle."
                )
            ),
            FvbPreface(
                year = "2002",
                title = "Préface à l'Édition de 2002",
                author = "P. Kenneth J. Decker, CSB • Supérieur Général",
                dateOrContext = "Fête de la Présentation de Marie • 21 novembre 2002 • Toronto",
                content = listOf(
                    "Le Chapitre Général de 2002 a approuvé un changement à l'article 104 accordant plus de souplesse dans le mode de sélection des délégués (approuvé par Rome Prot. n. T 67-1/2001). Prions pour qu'à travers notre fidélité au Mode de Vie Basilien nous atteignions la plénitude du Christ (Éphésiens 4, 13)."
                )
            )
        )
        AppLanguage.ES -> FormaDeVidaData.document.prefaces
    }

    fun getLocalizedFvbAbbreviations(lang: AppLanguage): List<Pair<String, String>> = when (lang) {
        AppLanguage.EN -> listOf(
            "AA" to "Apostolicam actuositatem (Apostolate of the Laity)",
            "DV" to "Dei Verbum (Divine Revelation)",
            "GS" to "Gaudium et spes (The Church in the Modern World)",
            "LG" to "Lumen gentium (The Church)",
            "OT" to "Optatam totius (Priestly Training)",
            "PC" to "Perfectae caritatis (Renewal of Religious Life)",
            "PO" to "Presbyterorum ordinis (Ministry of Priests)",
            "SC" to "Sacrosanctum concilium (Sacred Liturgy)",
            "C. / Cc." to "Canons of the 1983 Code of Canon Law",
            "Const." to "Constitutions of the Congregation of Saint Basil (1956)",
            "Rule" to "The Rule of the Basilian Fathers (1962)"
        )
        AppLanguage.FR -> listOf(
            "AA" to "Apostolicam actuositatem (Apostolat des Laïcs)",
            "DV" to "Dei Verbum (Révélation Divine)",
            "GS" to "Gaudium et spes (L'Église dans le Monde de ce Temps)",
            "LG" to "Lumen gentium (L'Église)",
            "OT" to "Optatam totius (Formation Sacerdotale)",
            "PC" to "Perfectae caritatis (Renouveau de la Vie Religieuse)",
            "PO" to "Presbyterorum ordinis (Ministère et Vie des Prêtres)",
            "SC" to "Sacrosanctum concilium (Sainte Liturgie)",
            "C. / Cc." to "Canons du Code de Droit Canonique de 1983",
            "Const." to "Constitutions de la Congrégation de Saint-Basile (1956)",
            "Règle" to "La Règle des Pères Basiliens (1962)"
        )
        AppLanguage.ES -> FormaDeVidaData.document.abbreviations
    }

    fun getLocalizedFvbIndex(lang: AppLanguage): List<FvbIndexItem> = when (lang) {
        AppLanguage.EN -> listOf(
            FvbIndexItem("Associates", "76, 77"),
            FvbIndexItem("Authority of the Pope", "99"),
            FvbIndexItem("General Chapter (Composition & Elections)", "104, 106, 110, 113"),
            FvbIndexItem("Evangelical Chastity", "9, 10, 11, 12, 13"),
            FvbIndexItem("General & Local Councilors", "110, 125, 128, 130, 131, 146"),
            FvbIndexItem("Accountability & Common Fund", "20, 21"),
            FvbIndexItem("Education & Schools", "3, 5, 64, 65"),
            FvbIndexItem("Ongoing Formation", "94, 95, 96, 97"),
            FvbIndexItem("The Spirit of the Founders of Annonay", "5, 65"),
            FvbIndexItem("The Call of Christ & Profession", "1, 2, 6, 7, 8, 28, 53, 56, 61, 153"),
            FvbIndexItem("Scholastics & Formation", "86, 87, 88, 89, 90, 91, 92"),
            FvbIndexItem("Formula of Religious Vows", "85"),
            FvbIndexItem("Christian & Fraternal Hospitality", "39"),
            FvbIndexItem("Novitiate & Formation of Novices", "78, 79, 80, 81, 82, 83, 84, 85"),
            FvbIndexItem("Loving Obedience", "25, 26, 27, 28, 29, 30"),
            FvbIndexItem("Prayer & Daily Hour of Personal Prayer", "45, 46, 47, 48, 50, 51, 52, 53, 54, 55, 58"),
            FvbIndexItem("Patrons of the Community (Saint Basil & Saints)", "4, 51"),
            FvbIndexItem("Evangelical Poverty & Stewardship of Goods", "14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24"),
            FvbIndexItem("Priesthood & Communion with Bishops", "40, 41, 42, 43, 44"),
            FvbIndexItem("Departures from the Community", "93"),
            FvbIndexItem("Superior General (Election & Authority)", "109, 114, 115, 116, 117, 118, 119, 121, 122, 124, 126, 127"),
            FvbIndexItem("Local Superiors & Rectors", "144, 145, 147, 148, 149, 150, 151, 152"),
            FvbIndexItem("Apostolic Work & Education", "59, 60, 61, 62, 63, 64, 65, 66, 67"),
            FvbIndexItem("Community Life (Meals, Dialogue, Sick)", "31, 32, 33, 34, 35, 36, 37, 38, 39, 74, 143"),
            FvbIndexItem("Vocations to the Congregation", "68, 69, 70, 71, 72, 73, 74, 75")
        )
        AppLanguage.FR -> listOf(
            FvbIndexItem("Associés", "76, 77"),
            FvbIndexItem("Autorité du Pape", "99"),
            FvbIndexItem("Chapitre Général (Composition et Élections)", "104, 106, 110, 113"),
            FvbIndexItem("Chasteté Évangélique", "9, 10, 11, 12, 13"),
            FvbIndexItem("Conseillers Généraux et Locaux", "110, 125, 128, 130, 131, 146"),
            FvbIndexItem("Devoirs et Fonds Commun", "20, 21"),
            FvbIndexItem("Éducation et Écoles", "3, 5, 64, 65"),
            FvbIndexItem("Formation Permanente", "94, 95, 96, 97"),
            FvbIndexItem("L'Esprit des Fondateurs d'Annonay", "5, 65"),
            FvbIndexItem("L'Appel du Christ et Profession", "1, 2, 6, 7, 8, 28, 53, 56, 61, 153"),
            FvbIndexItem("Scolastiques et Formation", "86, 87, 88, 89, 90, 91, 92"),
            FvbIndexItem("Formule des Vœux Religieux", "85"),
            FvbIndexItem("Hospitalité Chrétienne et Fraternelle", "39"),
            FvbIndexItem("Noviciat et Formation des Novices", "78, 79, 80, 81, 82, 83, 84, 85"),
            FvbIndexItem("Obéissance Aimante", "25, 26, 27, 28, 29, 30"),
            FvbIndexItem("Prière et Heure Quotidienne d'Oraison", "45, 46, 47, 48, 50, 51, 52, 53, 54, 55, 58"),
            FvbIndexItem("Patrons de la Communauté (Saint Basile et Saints)", "4, 51"),
            FvbIndexItem("Pauvreté Évangélique et Gestion des Biens", "14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24"),
            FvbIndexItem("Sacerdoce et Communion avec les Évêques", "40, 41, 42, 43, 44"),
            FvbIndexItem("Départs de la Communauté", "93"),
            FvbIndexItem("Supérieur Général (Élection et Facultés)", "109, 114, 115, 116, 117, 118, 119, 121, 122, 124, 126, 127"),
            FvbIndexItem("Supérieurs Locaux et Recteurs", "144, 145, 147, 148, 149, 150, 151, 152"),
            FvbIndexItem("Travail Apostolique et Éducation", "59, 60, 61, 62, 63, 64, 65, 66, 67"),
            FvbIndexItem("Vie Communautaire (Repas, Dialogue, Malades)", "31, 32, 33, 34, 35, 36, 37, 38, 39, 74, 143"),
            FvbIndexItem("Vocations à la Congrégation", "68, 69, 70, 71, 72, 73, 74, 75")
        )
        AppLanguage.ES -> FormaDeVidaData.document.index
    }

    fun localizeFvbArticle(article: FvbArticle, lang: AppLanguage): FvbArticle {
        if (lang == AppLanguage.ES) return article
        val locChapter = getLocalizedFvbChapterName(article.chapter, lang)
        val locText = when (lang) {
            AppLanguage.EN -> fvbArticleEn[article.number] ?: translateConstitutionalSpanish(article.text, AppLanguage.EN)
            AppLanguage.FR -> fvbArticleFr[article.number] ?: translateConstitutionalSpanish(article.text, AppLanguage.FR)
            AppLanguage.ES -> article.text
        }
        val locFootnotes = UniversalContentLocalizer.translateCommonTheologicalText(article.biblicalFootnotes, lang)
            .replace("Hechos", if (lang == AppLanguage.EN) "Acts" else "Actes")
            .replace("Gén.", if (lang == AppLanguage.EN) "Gen." else "Gn")
            .replace("Gál.", if (lang == AppLanguage.EN) "Gal." else "Ga")
            .replace("Fil.", if (lang == AppLanguage.EN) "Phil." else "Ph")
            .replace("Regla", if (lang == AppLanguage.EN) "Rule" else "Règle")
            .replace("Constituciones", "Constitutions")
        return article.copy(
            chapter = locChapter,
            text = locText,
            biblicalFootnotes = locFootnotes
        )
    }

    private val fvbArticleEn = mapOf(
        1 to "The first Christians called themselves 'followers of the Way'; they were instructed in the Way of the Lord. Every Christian life must find its center in Christ Our Lord, who is the Way, the Truth, and the Life, and in the authentic teaching of the Gospels. This is the primary and essential guide for all who wish to follow this way of life.",
        2 to "Religious communities have been called by the Holy Spirit to follow Christ in a special way, each in its own manner: there is a variety of gifts, all coming from the one and same Holy Spirit, and many forms of service that may be carried out to please the one and same Lord. The present work, The Basilian Way of Life, comes from one of these religious families, the Basilian Fathers. It is an outline of our way of religious life, a guide to be used alongside other authentic sources of instruction on Christian living and the general directives of the Church for religious communities.",
        3 to "The Congregation of Saint Basil, the Basilian Fathers, is a community of priests and students for the priesthood. Ours is an active and apostolic community with simple vows, seeking the glory of God in every form of priestly activity compatible with common life, especially in the works of education and evangelization.",
        4 to "Saint Basil was a priest and teacher, and for nine years Bishop of Caesarea in Asia Minor. All his life he suffered from poor health and died in the year 379 before reaching the age of fifty. Yet he was tireless in teaching true doctrine and working for the unity of the Church. We have chosen him as our patron saint because of his holiness and example, and for two aspects of his life and teaching: imbued with the spirit of the Gospels, he taught men and women how to live a Christian life in community suited to the conditions of their time; and he found, even in pagan wisdom, values which, seen in the light of revelation, could contribute to a full Christian life.",
        5 to "The Basilian Fathers were founded in 1822 by a group of ten diocesan priests in Annonay, France, so that through the strength conferred by their association in Christ and in the Church they might better serve the People of God through priestly ministry, especially in Christian education and preaching. Their devotion to work and commitment to the Church, the simplicity of their life, and their awareness of being called to a deeper community life animated the priests of Saint Basil when they professed formal vows in 1852, and these charisms still inspire Basilians today.",
        6 to "According to the mystery of God's love, we are called to follow His Son, Jesus Christ Our Lord. Just as Christ's disciples learned his teachings through their companionship with Him, so too we, through our intimate and personal union with the Lord Jesus, learn to love and to serve. The love He imparts to us is the center and wellspring of all that we are and all that we do.",
        7 to "As disciples, we listen to the Lord speaking to us in the Scriptures, through the Church, in our community, in the signs of the times, and by the grace of His Spirit. Impelled by that same love that called us, we respond with a total gift of ourselves, our time, our talents, and our gifts—freely, generously, and joyfully—through service to our brothers and all the children of God.",
        8 to "When we dedicate ourselves to the service of God within this community, we undertake a continual conversion of life, turning away from sin and worldly values to follow Jesus. We ratify and seal our offering by the three vows of chastity, poverty, and obedience from the common tradition of the Church. In making this profession, we leave all things for the sake of the Kingdom, following Christ within a brotherhood of mutual love and support.",
        9 to "By the vow of chastity we commit ourselves to a life in which, following the example of the Lord, we dedicate ourselves to the work of the Lord with an undivided heart. Calling us to follow the chastity of Christ, He frees us from the responsibilities of family life so that we may respond to His love by dedicating our lives to His service and the service of His people.",
        10 to "In making this vow we renounce an aspect of human life that is natural and good, yet what we do is far more than mere renunciation. We consecrate our chastity as an offering of praise and adoration to the Most Holy Trinity, committing ourselves to remain celibate for the sake of the Kingdom of God.",
        11 to "The consecration of our chastity gives us a mission and the strength to bear witness, in our common life and friendships within and outside the community, to the new life of the risen Christ. Chastity is best safeguarded within a community where true fraternal love flourishes, marked not by severity or reserve, but by the meekness and modesty of Christ.",
        12 to "The love of God and neighbor that we hope to attain through this vow is like that which Christ showed in person: wholehearted, generous, and ready for any sacrifice for the sake of the Kingdom of Heaven, rooted deeply in the mystery of the Cross.",
        13 to "Lived in this way, our chastity will be a vigorous witness to the primacy of the things of God. Yet this virtue can never be taken for granted; we constantly need God's grace and our own faithful effort.",
        14 to "'Blessed are the poor in spirit.' Since Our Lord spoke these words and showed us by example what they meant, His followers have sought the beatitude promised to the poor through a simple life, generosity toward the needy, and complete dependence on the providence of the Father.",
        15 to "With the help of the vow of poverty we seek to embrace the poverty of Christ. We promise to dedicate our whole life to working for Christ and sharing all that we have with our community, finding Him present in our midst and in all who suffer need or poverty.",
        16 to "The purpose of our vow of poverty is to free us to follow Our Lord in whatever work we do for Him. Possessions and personal security take second place as we care for one another and share everything in common, 'bearing one another's burdens and so fulfilling the law of Christ.'",
        17 to "By our vow of poverty we renounce not the capacity to own material goods, but our independent freedom to use them. In disposing of or using material goods, we bind ourselves not to act without the consent, at least reasonably implied, of our legitimate superiors.",
        18 to "A Basilian may retain ownership of any patrimony he possessed before vows or that comes to him afterward, but he may not administer or use it for his own purposes. Before perpetual profession, each member must make a will freely bequeathing all his goods.",
        19 to "'Go, sell all that you have and give to the poor... then come, follow me.' In light of these words of Jesus, the Superior General with the consent of his Council may grant a perpetually professed Basilian permission to renounce all patrimony.",
        20 to "All that we possess apart from personal patrimony we hold in common, and all that we earn belongs to the common fund. All salaries, pensions, stipends, and other income must be placed in the common fund, from which the community provides for the legitimate needs of the brothers in simplicity and accountability.",
        25 to "Our vow and practice of obedience is a response to Our Lord's words: 'If you love me, you will keep my commandments.' We dedicate ourselves as individuals and as a community to live in loving obedience to Christ wherever we meet Him: in the Gospels, in the Church, in our communities, in our brothers, and in the poor.",
        26 to "By the vow of obedience we commit ourselves to submit humbly to our superiors in a spirit of faith and love for God's will, in accordance with The Basilian Way of Life and the Basilian Customs, contributing our intelligence and will to the building up of the Body of Christ.",
        31 to "Like the first believers in Jerusalem who were of one heart and one soul and held all things in common, we are called to live together as brothers in Christ, supporting one another in prayer, work, and fraternal charity.",
        40 to "The Congregation of Saint Basil is a priestly community. In communion with the bishops, we share in the priesthood of Christ to proclaim the Word of God, celebrate the sacraments—above all the Holy Eucharist—and shepherd God's people.",
        45 to "Prayer is the breath of our community and personal life. Every day we gather as brothers for communal prayer, especially Morning and Evening Prayer (Liturgy of the Hours) and the daily celebration of the Eucharist, and each Basilian dedicates a full hour every day to personal prayer and meditation.",
        59 to "Our apostolic work springs from our union with Christ and our life in common. Following our founders in Annonay, we dedicate ourselves above all to Christian education in schools, colleges, and universities, as well as parish and missionary ministry.",
        85 to "Formula of Vows: 'In the presence of Almighty God, Father, Son, and Holy Spirit, I, N.N., vow to God perpetual chastity, poverty, and obedience in the Congregation of Priests of Saint Basil, according to The Basilian Way of Life. May the grace of God, the intercession of the Blessed Virgin Mary and of our holy father Basil, and the prayers of my brothers help me to fulfill this offering. Amen.'",
        153 to "The People of God have here no lasting city, but seek the city that is to come. Our way of life as Basilians gives us freedom from earthly anxieties, reveals the heavenly goods already present among us, and witnesses to the new and eternal life won by Christ. For us, our way of life is the closest imitation possible of the life in which Jesus fulfilled the will of His Father."
    )

    private val fvbArticleFr = mapOf(
        1 to "Les premiers chrétiens s'appelaient eux-mêmes « disciples du Chemin » ; ils étaient instruits dans le Chemin du Seigneur. Toute vie chrétienne doit trouver son centre dans le Christ Notre Seigneur, qui est le Chemin, la Vérité et la Vie, et dans l'enseignement authentique des Évangiles. Tel est le guide premier et essentiel pour quiconque veut suivre cette forme de vie.",
        2 to "Les communautés religieuses ont été appelées par l'Esprit Saint à suivre le Christ d'une manière spéciale : il y a diversité de dons, mais le même Esprit. Le présent ouvrage, Le Mode de Vie Basilien, provient de l'une de ces familles religieuses, les Pères Basiliens. C'est l'esquisse de notre forme de vie religieuse.",
        3 to "La Congrégation de Saint-Basile, les Pères Basiliens, est une communauté de prêtres et d'étudiants pour le sacerdoce. La nôtre est une communauté active et apostolique à vœux simples, cherchant la gloire de Dieu dans toute activité sacerdotale compatible avec la vie commune, spécialement dans l'éducation et l'évangélisation.",
        4 to "Saint Basile fut prêtre, enseignant et, pendant neuf ans, évêque de Césarée en Asie Mineure. Malgré une santé fragile jusqu'à sa mort en 379, il fut infatigable dans l'enseignement de la vraie doctrine et l'unité de l'Église. Nous l'avons choisi comme saint patron pour sa sainteté et parce qu'il a enseigné à vivre l'Évangile en communauté et à discerner dans la sagesse humaine les valeurs qui contribuent à une vie chrétienne plénière.",
        5 to "Les Pères Basiliens ont été fondés en 1822 par dix prêtres diocésains à Annonay, en France, afin de mieux servir le Peuple de Dieu par leur union dans le Christ, spécialement dans l'éducation chrétienne et la prédication. Leur dévouement au travail, la simplicité de leur vie et leur amour de la vie commune animent encore les Basiliens aujourd'hui.",
        6 to "Selon le mystère de l'amour de Dieu, nous sommes appelés à suivre son Fils, Jésus-Christ Notre Seigneur. Par notre union intime et personnelle avec le Seigneur Jésus, nous apprenons à aimer et à servir. L'amour qu'Il nous communique est le centre et la source de tout ce que nous sommes et faisons.",
        7 to "En disciples, nous écoutons le Seigneur qui nous parle dans les Écritures, à travers l'Église, dans notre communauté et dans les signes des temps. Poussés par cet amour, nous répondons par le don total de notre être, de notre temps et de nos talents, avec liberté, générosité et joie.",
        8 to "En nous consacrant au service de Dieu dans cette communauté, nous entreprenons une conversion continuelle. Nous scellons notre offrande par les trois vœux de chasteté, de pauvreté et d'obéissance, quittant tout pour le Royaume à la suite du Christ au sein d'une fraternité d'amour et de soutien mutuel.",
        9 to "Par le vœu de chasteté, nous nous engageons à une vie où, suivant l'exemple du Seigneur, nous nous consacrons à son œuvre de tout notre cœur, libres de nous donner entièrement au service de Dieu et de son peuple.",
        10 to "En faisant ce vœu, nous consacrons notre chasteté en offrande de louange et d'adoration à la Très Sainte Trinité, nous engageant à garder le célibat pour le Royaume de Dieu.",
        11 to "La consécration de notre chasteté nous confie la mission de témoigner, dans notre vie commune et nos amitiés, de la vie nouvelle du Christ ressuscité. La chasteté est sauvegardée là où fleurit une vraie charité fraternelle, empreinte de la douceur et de la modestie du Christ.",
        14 to "« Heureux les pauvres de cœur. » Depuis que Notre Seigneur a prononcé ces paroles, ses disciples ont cherché cette béatitude par une vie simple, généreuse envers les démunis et totalement confiante dans la providence du Père.",
        15 to "Avec l'aide du vœu de pauvreté, nous cherchons à adopter la pauvreté du Christ, promettant de travailler toute notre vie pour Lui et de partager tout ce que nous avons avec notre communauté et avec les pauvres.",
        20 to "Tout ce que nous possédons, hormis le patrimoine personnel, nous le possédons en commun et tout ce que nous gagnons appartient au fonds commun. Tous les salaires, pensions et honoraires doivent être versés au fonds commun, qui pourvoit aux besoins légitimes des frères dans un esprit de vraie simplicité.",
        25 to "Notre vœu et notre pratique de l'obéissance sont une réponse aux paroles de Notre Seigneur : « Si vous m'aimez, vous garderez mes commandements ». Nous nous consacrons à vivre dans l'obéissance aimante au Christ là où nous le rencontrons : dans l'Évangile, l'Église, nos frères et les pauvres.",
        26 to "Par le vœu d'obéissance, nous nous engageons à nous soumettre humblement à nos supérieurs dans un esprit de foi et d'amour pour la volonté de Dieu, selon Le Mode de Vie Basilien et les Coutumes Basiliennes.",
        45 to "La prière est le souffle de notre vie communautaire et personnelle. Chaque jour, nous nous rassemblons pour la prière commune, l'Eucharistie et la Liturgie des Heures, et chaque Basilien consacre une heure entière par jour à l'oraison personnelle.",
        85 to "Formule des Vœux : « En présence de Dieu Tout-Puissant, Père, Fils et Saint-Esprit, moi, N.N., je fais vœu à Dieu de chasteté, de pauvreté et d'obéissance perpétuelles dans la Congrégation des Prêtres de Saint-Basile, selon Le Mode de Vie Basilien. Que la grâce de Dieu, l'intercession de la Vierge Marie et de notre saint père Basile, et les prières de mes frères m'aident à accomplir cette offrande. Amen. »",
        153 to "Le Peuple de Dieu n'a pas ici-bas de cité permanente, mais il est en quête de la cité future. Notre forme de vie comme Basiliens témoigne de la vie nouvelle et éternelle que le Christ nous a acquise. Nous acceptons avec joie ces normes, exprimant à la manière basilienne la seigneurie de Jésus-Christ et la puissance de l'Esprit Saint dans l'Église."
    )

    private fun translateConstitutionalSpanish(text: String, lang: AppLanguage): String {
        if (lang == AppLanguage.ES) return text
        var res = UniversalContentLocalizer.translateCommonTheologicalText(text, lang)
        if (lang == AppLanguage.EN) {
            val rules = listOf(
                "Los miembros de la comunidad" to "The members of the community",
                "En nuestra vida en común" to "In our life in common",
                "Cada uno de nosotros" to "Each one of us",
                "El Superior General" to "The Superior General",
                "El Consejo General" to "The General Council",
                "El Capítulo General" to "The General Chapter",
                "El superior local" to "The local superior",
                "Los superiores" to "Superiors",
                "nuestros hermanos" to "our brothers",
                "la comunidad local" to "the local community",
                "la vida religiosa" to "religious life",
                "la vida común" to "common life",
                "la oración personal" to "personal prayer",
                "la oración comunitaria" to "communal prayer",
                "una hora diaria" to "one hour daily",
                "todos los días" to "every day",
                "votos perpetuos" to "perpetual vows",
                "primeros votos" to "first vows",
                "votos simples" to "simple vows",
                "de acuerdo con" to "in accordance with",
                "por lo menos" to "at least",
                "sin embargo" to "however",
                "también" to "also",
                "mediante" to "through",
                "nuestra" to "our",
                "nuestro" to "our"
            )
            for ((es, en) in rules) res = res.replace(es, en)
        } else if (lang == AppLanguage.FR) {
            val rules = listOf(
                "Los miembros de la comunidad" to "Les membres de la communauté",
                "En nuestra vida en común" to "Dans notre vie en commun",
                "Cada uno de nosotros" to "Chacun d'entre nous",
                "El Superior General" to "Le Supérieur Général",
                "El Consejo General" to "Le Conseil Général",
                "El Capítulo General" to "Le Chapitre Général",
                "El superior local" to "Le supérieur local",
                "Los superiores" to "Les supérieurs",
                "nuestros hermanos" to "nos frères",
                "la comunidad local" to "la communauté locale",
                "la vida religiosa" to "la vie religieuse",
                "la vida común" to "la vie commune",
                "la oración personal" to "l'oraison personnelle",
                "la oración comunitaria" to "la prière communautaire",
                "una hora diaria" to "une heure chaque jour",
                "todos los días" to "tous les jours",
                "votos perpetuos" to "vœux perpétuels",
                "primeros votos" to "premiers vœux",
                "votos simples" to "vœux simples",
                "de acuerdo con" to "conformément à",
                "por lo menos" to "au moins",
                "sin embargo" to "cependant",
                "también" to "aussi",
                "mediante" to "par",
                "nuestra" to "notre",
                "nuestro" to "notre"
            )
            for ((es, fr) in rules) res = res.replace(es, fr)
        }
        return res
    }

    // =========================================================================
    // 2. COSTUMBRES BASILIANAS 2022 LOCALIZATION
    // =========================================================================
    fun localizeCostumbresDocument(doc: CostumbresDocument, lang: AppLanguage): CostumbresDocument {
        if (lang == AppLanguage.ES) return doc
        val locPreface = when (lang) {
            AppLanguage.EN -> doc.preface.copy(
                title = "Preface to the 2022 Revision of the Basilian Customs",
                approvedBy = "Approved by the 2022 General Chapter (incorporating amendments from 1989, 1993, 1997, 2001, 2012, 2014, and 2018)",
                reviewCommittee = doc.preface.reviewCommittee.map { it.replace("Padre ", "Fr. ") },
                translators = doc.preface.translators.map { it.replace("Padre ", "Fr. ") },
                keyAmendments2022 = listOf(
                    "Dependencies and therapeutic accompaniment (E.21)",
                    "Possible years for exclaustration (I.06)",
                    "Voting procedure for delegates to the General Chapter (J.06)",
                    "End-of-term protocol for the General Council (J.33)",
                    "Clarification of Regional Councils' relationship with the General Council (N.21 & N.22)",
                    "Formal removal of references to the postulancy (L.08(g) & H.16)"
                ),
                author = "Fr. Kevin Storey, CSB",
                authorTitle = "Superior General",
                content = listOf(
                    "This version of the Basilian Customs, the non-constitutional legislative document of the Congregation of Saint Basil, was approved by the 2022 General Chapter. It includes amendments enacted by the Chapters of 1989, 1993, 1997, 2001, 2012, 2014, and 2018.",
                    "A thorough revision of the Customs was presented to the 2014 Chapter by the Review Committee (Frs. Timothy Scott, Daniel Callam, William Irwin, Gordon Judd, and Dennis Noelke), with Spanish translations prepared by Frs. Jack Hanna, Juan Carlos Rojas, Paul Walsh, and Bob Barringer.",
                    "The 2022 Chapter authorized six key amendments concerning therapeutic support for dependencies (E.21), exclaustration (I.06), delegate voting (J.06), General Council transition protocols (J.33), Regional Councils in Colombia and Mexico (N.21–N.22), and the suppression of outdated references to postulancy.",
                    "The Basilian Way of Life and the Basilian Customs form our essential guiding documents. May the Lord continue to bless our service to the People of God. Praise be to the Lord!"
                )
            )
            AppLanguage.FR -> doc.preface.copy(
                title = "Préface à la révision de 2022 des Coutumes Basiliennes",
                approvedBy = "Approuvée par le Chapitre Général de 2022 (incluant les modifications de 1989, 1993, 1997, 2001, 2012, 2014 et 2018)",
                reviewCommittee = doc.preface.reviewCommittee.map { it.replace("Padre ", "P. ") },
                translators = doc.preface.translators.map { it.replace("Padre ", "P. ") },
                keyAmendments2022 = listOf(
                    "Dépendances et accompagnement thérapeutique (E.21)",
                    "Années possibles pour l'exclaustration (I.06)",
                    "Mode de scrutin pour les délégués au Chapitre Général (J.06)",
                    "Protocole de fin de mandat pour le Conseil Général (J.33)",
                    "Relations des Conseils Régionaux avec le Conseil Général (N.21 et N.22)",
                    "Suppression formelle des références au postulat (L.08(g) et H.16)"
                ),
                author = "P. Kevin Storey, CSB",
                authorTitle = "Supérieur Général",
                content = listOf(
                    "Cette version des Coutumes Basiliennes, le document non constitutionnel de la Congrégation de Saint-Basile, a été approuvée par le Chapitre Général de 2022 et intègre les amendements des Chapitres de 1989 à 2018.",
                    "Une révision complète fut présentée au Chapitre de 2014 par le Comité de Révision (les Pères Timothy Scott, Daniel Callam, William Irwin, Gordon Judd et Dennis Noelke), avec la collaboration des Pères Jack Hanna, Juan Carlos Rojas, Paul Walsh et Bob Barringer.",
                    "Le Chapitre de 2022 a autorisé six amendements portant sur l'accompagnement des dépendances (E.21), l'exclaustration (I.06), l'élection des délégués (J.06), le protocole du Conseil Général (J.33) et les Conseils Régionaux du Mexique et de la Colombie (N.21 et N.22).",
                    "Le Mode de Vie Basilien et les Coutumes Basiliennes constituent nos documents essentiels. Que le Seigneur continue de bénir notre service auprès du Peuple de Dieu !"
                )
            )
            AppLanguage.ES -> doc.preface
        }
        return doc.copy(
            title = if (lang == AppLanguage.EN) "Basilian Customs" else "Coutumes Basiliennes",
            subtitle = if (lang == AppLanguage.EN) "The Non-Constitutional Document of the Congregation of Priests of Saint Basil (Basilian Fathers)" else "Le Document non constitutionnel de la Congrégation des Prêtres de Saint-Basile (Pères Basiliens)",
            institution = if (lang == AppLanguage.EN) "Congregation of Priests of Saint Basil" else "Congrégation des Prêtres de Saint-Basile",
            cityAndYear = if (lang == AppLanguage.EN) "Toronto, Canada • 2022" else "Toronto, Canada • 2022",
            bicentennialNote = if (lang == AppLanguage.EN) "Bicentennial Year (1822 - 2022)" else "Année du Bicentenaire (1822 - 2022)",
            preface = locPreface,
            sections = doc.sections.map { localizeCostumbresSection(it, lang) }
        )
    }

    fun localizeCostumbresSection(sec: CostumbresSection, lang: AppLanguage): CostumbresSection {
        if (lang == AppLanguage.ES) return sec
        val locTitle = when (sec.id) {
            "intro" -> if (lang == AppLanguage.EN) "Introduction" else "Introduction"
            "castidad" -> if (lang == AppLanguage.EN) "Chastity" else "La Chasteté"
            "pobreza" -> if (lang == AppLanguage.EN) "Poverty" else "La Pauvreté"
            "obediencia" -> if (lang == AppLanguage.EN) "Obedience" else "L'Obéissance"
            "vida_comunidad" -> if (lang == AppLanguage.EN) "Community Life" else "La Vie en Communauté"
            "sacerdocio" -> if (lang == AppLanguage.EN) "Priesthood, Apostolic Work & Education" else "Sacerdoce, Apostolat et Éducation"
            "oracion" -> if (lang == AppLanguage.EN) "Prayer" else "La Prière"
            "formacion" -> if (lang == AppLanguage.EN) "Initial Formation" else "La Formation Initiale"
            "gobierno" -> if (lang == AppLanguage.EN) "Governance of the Congregation" else "Le Gouvernement de la Congrégation"
            "superior_consejo" -> if (lang == AppLanguage.EN) "The Superior General & General Council" else "Le Supérieur Général et le Conseil Général"
            "gobierno_local_colombia" -> if (lang == AppLanguage.EN) "Local Governance & Regional Superiors (Mexico & Colombia)" else "Gouvernement Local et Supérieurs du Mexique et de la Colombie"
            else -> UniversalContentLocalizer.translateCommonTheologicalText(sec.title, lang)
        }
        val locSubtitle = when (sec.id) {
            "pobreza" -> if (lang == AppLanguage.EN) "Personal and Communal" else "Personnelle et Communautaire"
            "formacion" -> if (lang == AppLanguage.EN) "Associates, Novices & Scholastics" else "Associés, Novices et Scolastiques"
            "gobierno" -> if (lang == AppLanguage.EN) "General Chapter & Elections" else "Chapitre Général et Élections"
            else -> UniversalContentLocalizer.translateCommonTheologicalText(sec.subtitle, lang)
        }
        val locRef = sec.fvbReference.replace("ver FVB", if (lang == AppLanguage.EN) "see BWL" else "voir MVB")
        val locArticles = sec.articles.map { art ->
            val (t, c) = getLocalizedCostumbresArticle(art.code, art.title, art.content, lang)
            art.copy(
                title = t,
                content = c,
                categoryTitle = locTitle,
                fvbReference = art.fvbReference.replace("ver FVB", if (lang == AppLanguage.EN) "see BWL" else "voir MVB")
            )
        }
        return sec.copy(
            title = locTitle,
            subtitle = locSubtitle,
            fvbReference = locRef,
            articles = locArticles
        )
    }

    private fun getLocalizedCostumbresArticle(code: String, defaultTitle: String, defaultContent: String, lang: AppLanguage): Pair<String, String> {
        if (lang == AppLanguage.EN) {
            costumbresEn[code]?.let { return it }
            return translateConstitutionalSpanish(defaultTitle, AppLanguage.EN) to translateConstitutionalSpanish(defaultContent, AppLanguage.EN)
        } else if (lang == AppLanguage.FR) {
            costumbresFr[code]?.let { return it }
            return translateConstitutionalSpanish(defaultTitle, AppLanguage.FR) to translateConstitutionalSpanish(defaultContent, AppLanguage.FR)
        }
        return defaultTitle to defaultContent
    }

    private val costumbresEn = mapOf(
        "A.01" to ("The Basilian Way of Life and the Customs" to "The Basilian Way of Life is our constitutional document, definitively approved by the Holy See in 1983. This document, Basilian Customs, contains practical norms adapted to different historical moments, cultures, apostolates, and localities."),
        "A.02" to ("Fidelity to God's Call" to "By following The Basilian Way of Life and the Basilian Customs, we express our fidelity to God who calls us in Jesus Christ and through the Holy Spirit."),
        "A.03" to ("Archives and Documents in Local Houses" to "In addition to The Basilian Way of Life and the Basilian Customs, every local community must keep important current and historical documents of the Congregation accessible to its members, including the Constitutions, the Rule, General Chapter reports, Basilian Annals, Directories, and Newsletters."),
        "A.04" to ("Annual Communal Reading" to "Local communities shall make provision for the annual reading of The Basilian Way of Life and the Customs in a context of prayer and reflection."),
        "A.05" to ("Local Inculturation" to "Local communities should discuss how to live The Basilian Way of Life and the Customs in their local circumstances. Any deviation for just cause requires the approval of the Superior General."),
        "A.06" to ("Amendments and Interpretations" to "Changes or interpretations of the Basilian Customs determining membership in the General Chapter require a two-thirds vote of the Chapter; other changes require a simple majority. Between Chapters, interpretation belongs to the General Council."),
        "B.01" to ("Consecrated Love and Generosity" to "Our life together and our relationships with others must express the depth of love expected of men who, through consecrated chastity, follow the way of the Lord in total self-giving, reflecting generosity, kindness, and solidarity."),
        "B.02" to ("Mutual Support and Common Life" to "To live the gift of chastity fully, we must give and receive the strength and support of our community through our shared presence at prayer, meals, recreation, and house meetings. 'Let love be genuine; love one another with mutual affection' (Romans 12:9–10)."),
        "B.03" to ("Authentic Christian Friendship" to "Authentic Christian friendship reflects God's steadfast love. Our friendships within and outside the community should never be exclusive or separate us from community or ministry, but rather strengthen our love for God and neighbor."),
        "B.04" to ("Divine Aid and the Sacraments" to "Conscious of our fragility, we pray frequently for perseverance in chastity, invoking the Blessed Virgin Mary and Saint Joseph, and drawing grace from the Eucharist, the Sacrament of Reconciliation, and spiritual direction."),
        "B.05" to ("Treasure in Earthen Vessels" to "'We have this treasure in earthen vessels' (2 Cor 4:7). Prudence, love for solitude and prayer, and faithful dedication to our work help preserve the gift of chastity."),
        "B.06" to ("Zero Tolerance and Protection of the Vulnerable" to "Sexual abuse is a grave moral evil that harms victims and causes scandal in society, the Church, and our Community. To protect children and vulnerable adults, confreres shall strictly comply with all civil laws and diocesan and Basilian safeguarding policies."),
        "C.01" to ("Imitation of Christ and Social Justice" to "Our profession of evangelical poverty commits us to a communal and personal life that imitates Jesus Christ, who was humble of spirit and poor in material goods, and calls us to participate in the Church's struggle for justice and care for the poor."),
        "C.02" to ("Personal Poverty and Detachment" to "We have promised to be poor and must live everywhere as poor men, working to free our hearts from the desire for material possessions."),
        "C.03" to ("Sharing with Our Brothers" to "As religious, we share what we have with our brothers. We depend on the Community and consider nothing at our private disposal without the approval of our superiors."),
        "C.04" to ("Methods of Personal Support" to "Local communities may provide for members' needs through specific requests, a periodic allowance, or an approved annual personal expense budget."),
        "C.05" to ("Sobriety and Fraternal Correction" to "Neither personal needs nor monetary gifts may allow a member to live a lifestyle exceeding that of his brothers. A superior, with fraternal love, must correct any extravagance contrary to religious poverty."),
        "C.06" to ("Communal Witness of Poverty" to "Members and local communities must regularly evaluate their communal standard of living (food, clothing, housing, furnishings, vehicles, vacations) so as never to give counter-witness to evangelical poverty."),
        "C.07" to ("Bank Accounts" to "Members who hold personal bank accounts must designate another confrere as co-signer on the account."),
        "C.08" to ("Credit and Debit Cards" to "While the use of credit and debit cards is often necessary, members must avoid extravagance and use such cards only with their superior's permission."),
        "C.09" to ("Unauthorized Debts" to "If a member incurs a large debt without his superior's authorization, neither the local nor the general community is liable, nor may a local house pay it without the Superior General's authorization."),
        "C.10" to ("Publication Royalties" to "Unless the Superior General decides otherwise, royalties from a member's publications belong to the General Treasury rather than the local house."),
        "C.11" to ("Inter-House Visits" to "When a member visits another Basilian house, he is welcomed without charge; for visits lasting more than two or three days, an agreement on expenses should be made with the local superior."),
        "C.12" to ("Basilians Living Apart" to "A Basilian living outside a community house maintains the same poverty accountability directly with the General Council as a member of a local house."),
        "C.13" to ("Evangelical Financial Transparency" to "All that we have is a gift from God for the service of Christ and His Church. Every member shall be duly informed of the financial condition of his local community."),
        "C.14" to ("Avoiding Unnecessary Accumulation" to "Local communities must avoid accumulating unnecessary surpluses. Care for the poor is the sign of our poverty, and surplus funds must be remitted to the General Treasury."),
        "C.17" to ("Buildings and Austerity" to "Even when apostolic needs require managing large facilities or funds, we must guard against adopting attitudes unbecoming followers of Christ, who was poor."),
        "C.19" to ("Donations and Bequests" to "Valuable donations or bequests made to a confrere as a Basilian must be remitted to the General Treasury unless the donor explicitly specifies otherwise."),
        "D.01" to ("Twofold Dimension of Obedience" to "Jesus obeyed the Father in all things. Our obedience is twofold: constant fidelity to Jesus Christ and our Way of Life in the Church, and specific obedience to the decisions of the Congregation and our legitimate superiors."),
        "D.02" to ("A Prompt and Lively Spirit" to "Through continual obedience we grow in union with Christ. Let us obey always and everywhere, in small matters and great, without hesitation or complaint, with a prompt and lively spirit (cf. Rule of 1962; Constitutions of 1863)."),
        "D.03" to ("Authority Exercised with Gentleness" to "Religious obedience springs from love. Superiors shall exercise their authority with kindness and gentleness, even when correcting a brother."),
        "D.05" to ("Right to Express Objections" to "Even after a superior has made a decision requiring obedience, a member has the right—and sometimes the obligation—to express reasons why implementation may be harmful or overly difficult, with recourse to the Superior General."),
        "D.06" to ("Conciliation Process" to "When a member believes he has suffered an injustice from a local superior or the Superior General and informal dialogue fails, he may invoke the Conciliation Process established by the General Chapter."),
        "D.07" to ("Obedience in Apostolic Institutions" to "When working in schools, parishes, or institutions directed by other confreres or lay authorities, the spirit of obedience calls us to offer generous cooperation."),
        "E.01" to ("Charity as the Foundation" to "Like every form of Christian communal life, our life is founded upon charity, the greatest of the virtues."),
        "E.03" to ("Minimum Number in a House" to "As a concrete expression of common life, a local community shall normally consist of at least three members."),
        "E.05" to ("Mutual Care and Admonition" to "Common life requires mutual care: we love, forgive, support, and correct one another as brothers in the Lord. No one except the superior may issue a public admonition."),
        "E.07" to ("Fraternal Address Without Academic Titles" to "Our conversation among brothers should be familiar and free of academic distinctions. When formal address is appropriate, priests are called 'Father' and unordained brothers 'Mister'."),
        "E.09" to ("Silence for Prayer and Study" to "Times of silence are essential in our community life for study, rest, and above all prayer, so that each may hear the voice of God speaking within his heart."),
        "E.14" to ("Daily Schedule and Common Prayer" to "The daily schedule in our houses must include common meals, Morning and Evening Prayer, daily Eucharist (including a weekly concelebrated Mass), and regular house meetings."),
        "E.15" to ("Mensa Communis" to "Since we are ordained under the canonical title of mensa communis (common table), we regard the principal meal of the day as a vital moment of our community life."),
        "E.20" to ("Moderation and Mutual Hospitality" to "Moderate use of alcoholic beverages may aid relaxation and celebration, while full respect and hospitality must always be shown toward brothers who choose to abstain."),
        "E.21" to ("2022 Amendment: Dependencies and Therapeutic Support" to "When there are indications that a member struggles with a dependency (such as pornography, substance abuse, gambling, or other forms), he must take responsibility through psychological and spiritual counseling. Confreres should express concern with charity; if therapeutic treatment is advisable, the matter is referred to the Superior General."),
        "E.25" to ("Basilian Hospitality" to "Basilian life includes warm hospitality: welcoming family members, fellow clergy and religious, Basilian Lay Associates, and pastoral collaborators to our table."),
        "E.26" to ("Basilian Lay Associates" to "Basilian Lay Associates live out their baptismal vocation through formal association with the Congregation (Basic Document of Lay Associates – 2007), sharing mutual prayer and collaboration."),
        "E.30" to ("Promoting Vocations in Community" to "Every local community bears a special responsibility to foster vocations by inviting young men interested in Basilian life to visit and share our prayer and table."),
        "E.43" to ("Fraternal Care for the Sick" to "'Our fraternal love is expressed most clearly in our care for the sick and support for those suffering difficulties' (Rule of Léorat-Picansel, 1802; Letter of Fr. Actorie, 1863)."),
        "E.48" to ("Death of a Brother" to "The death of a confrere is a moment of sorrow yet also of Christian hope and peace, marked by a vigil and Funeral Mass with confreres, family, and friends."),
        "E.50" to ("Simple Casket and Modest Headstone" to "The casket shall be simple and the grave marked by a modest headstone in the nearest Basilian cemetery plot."),
        "F.01" to ("Saving Action of Christ" to "As priests and religious, we share in Christ's saving mission, proclaiming the Gospel in our communities and pastoral ministry."),
        "F.02" to ("Witness of Goodness, Discipline, and Knowledge" to "Our communal life must be a model of true Christian community, leading those we serve into the goodness, discipline, and wisdom of the Lord."),
        "F.05" to ("Service in Parishes" to "From the beginning, Basilians have served in parishes entrusted to us by diocesan bishops, bringing our distinctive dedication to Christian education."),
        "F.06" to ("Christian Education of Youth" to "Apostolic concern for the Christian education of youth is the great Basilian tradition, continually adapting to the needs of God's People."),
        "F.08" to ("Preference for Disadvantaged Students" to "Care for students who struggle academically or lack financial resources is essential to the Basilian educational apostolate through financial aid and academic support."),
        "F.10" to ("Advanced Studies" to "To support Christian education in all its forms, we encourage qualified confreres to pursue advanced degrees in pastoral studies, Scripture, philosophy, languages, literature, and the sciences."),
        "F.26" to ("Social Justice and Preferential Love for the Poor" to "'I invite all members of Institutes devoted to education to be faithful to their founding charism, aware that preferential love for the poor finds special application in freeing people from the grave misery of a lack of cultural and religious formation' (St. John Paul II, Vita Consecrata #97)."),
        "F.32" to ("Mature Living Allowance (MLA)" to "The Mature Living Allowance (MLA) is the stipend set by the General Council and transferred to local houses for the dignified support of retired confreres."),
        "G.01" to ("Loving Response to God" to "God loves us and reveals His love through His Son. Prayer is our loving response that unites us to the Father, through Christ in the Holy Spirit."),
        "G.02" to ("Regular Communal Eucharist" to "Communal Eucharist shall be celebrated regularly in our houses, uniting ordained and unordained brothers around a single altar of thanksgiving."),
        "G.04" to ("Liturgy of the Hours" to "As priests and deacons we have received from the Church the sacred obligation to celebrate the Liturgy of the Hours daily."),
        "G.05" to ("Living Morning and Evening Prayer" to "Lauds and Vespers follow the Liturgy of the Hours. Through study, meditation, shared reflection, and song, we make our daily prayer vibrant rather than routine."),
        "G.10" to ("Annual Congregational Retreat" to "The annual retreat is of vital importance for every Basilian. Each confrere must participate every year in the Superior General's retreat or another communal retreat."),
        "G.11" to ("Feasts of Saint Basil" to "Both the liturgical feast of Saint Basil and Saint Gregory Nazianzen on January 2 and the traditional Basilian feast on June 14 are days of special prayer for the Congregation."),
        "G.14" to ("Traditional Devotions" to "Devotion to the Blessed Sacrament, the Sacred Heart, the Blessed Virgin Mary, our patron saints, the Rosary, and the Way of the Cross deeply nourish Basilian spirituality."),
        "G.16" to ("Prayer as the Test of Vitality" to "The witness of history is clear: fidelity to prayer or its neglect is the test of the vitality or decline of religious life."),
        "H.01" to ("Formation Team" to "A Formation Team is responsible for the progressive incorporation of new members into the Congregation and their preparation for vows and ordination."),
        "H.04" to ("Admission to the Associate Program" to "Application to the Associate Program is made by personal letter to the Superior General accompanied by certificates and recommendations."),
        "H.13" to ("Application to the Novitiate" to "An Associate applies for admission to the Novitiate by personal letter to the Superior General after consultation with the local community."),
        "H.18" to ("Master of Novices" to "The Novice Master must be a priest in perpetual vows, at least thirty years old, capable of teaching novices by word and example."),
        "H.21" to ("Twelve-Month Canonical Novitiate" to "During the twelve months of the canonical novitiate, novices participate in apostolic and volunteer work according to the approved formation program."),
        "H.26" to ("Scholastics and Theological Preparation" to "The Director of Scholastics guides scholastics in solid spiritual and intellectual preparation for the priesthood and religious life."),
        "H.27" to ("Temporary Vows and Perpetual Profession" to "Perpetual profession requires a prior period of temporary vows of not less than three years and not more than six years."),
        "J.01" to ("Collegial Responsibility" to "Responsibility for governing the Congregation rests upon all Basilians through the General Chapter, the Superior General, and the General Council, and locally through the house superior or rector and his council."),
        "J.02" to ("Religious Function of the General Chapter" to "The General Chapter has a deeply spiritual purpose: to deepen our understanding of Christ's mission on earth and strengthen our commitment to it."),
        "J.04" to ("Representative Composition" to "In principle, the General Chapter consists of approximately twenty percent of the professed members of the Congregation."),
        "J.06" to ("Regional Representation of Colombia and Mexico" to "The Regional Superiors of Mexico and Colombia are ex officio members of the General Chapter. Among the scholastics, at least one delegate is elected from Canada/USA, one from Mexico, and one from Colombia."),
        "J.25" to ("Prayer Before Elections" to "Before elections, all chapter delegates pray: 'Holy Spirit, our Protector, whom Jesus sent to guide the Church into all truth, come to us. Illumine our minds with wisdom so that we may elect the one who will best serve our community. Fill our hearts with love and peace so that united we may accept the results of this election. Amen.'"),
        "J.29" to ("Solemn Profession of the Superior General Elect" to "The newly elected Superior General declares before God and the General Chapter: 'I promise that, as Superior General, I will serve this Congregation with all my heart, adhering to The Basilian Way of Life and the Basilian Customs and strengthening my brothers...' Then the Te Deum is sung in thanksgiving."),
        "K.01" to ("Constitutional Guardian and Pastor" to "The Superior General is responsible for fostering the spiritual life of the brothers and serves as guardian of the constitutional practices of the Congregation."),
        "K.06" to ("Visitations to All Communities" to "The Superior General strives to be accessible to all confreres and visits every community at least twice during his four-year term."),
        "K.07" to ("No Personal Privileges" to "The Superior General holds no personal privileges in community life regarding clothing, food, or transportation."),
        "L.01" to ("Composition of the General Council" to "The Superior General and the General Councilors form the General Council, elected by the General Chapter for a four-year term."),
        "L.08" to ("Deliberative Vote of the General Council" to "The General Council exercises a deliberative vote in convoking an extraordinary Chapter, admitting to perpetual vows and holy orders, opening or suppressing houses, approving budgets, accepting parishes, and appointing superiors and formation directors."),
        "L.13" to ("Canonical Visitations" to "Canonical visitation is a primary way in which the General Council helps a local community strengthen its commitment to religious life and ministry."),
        "N.01" to ("Service Modeled on Christ" to "The local superior is entrusted with the well-being of the local community, taking as his model Jesus Christ, who came to serve and not to be served."),
        "N.05" to ("Regular Meetings of the Local Council" to "The local council must meet frequently, at least five times a year, keeping the local community informed of its decisions."),
        "N.10" to ("Local Treasurer and Domestic Administration" to "Under the direction of the local council, the house treasurer is responsible for the maintenance of the house, food provision, and care of rooms."),
        "N.19" to ("Recognition of Regional Superiors in Colombia and Mexico" to "The superiors in Mexico and Colombia are officially recognized as Regional Superiors, bearing responsibility for civil and ecclesiastical affairs in their regions."),
        "N.20" to ("Four-Year Term" to "The term of the Regional Superiors of Colombia and Mexico is four years, appointed after each General Chapter together with their Regional Councilors."),
        "N.21" to ("Meetings and Minutes of Regional Councils" to "Before the Superior General and Council consider major decisions and appointments, the Regional Councils meet to discuss ministerial and formation realities and submit formal recommendations."),
        "N.22" to ("Appointment Cycle in Colombia and Mexico" to "Appointments in Colombia normally take effect on January 1, whereas appointments in Mexico normally begin on July 1. Consistent communication between the Superior General and the Regional Superiors is essential for good governance.")
    )

    private val costumbresFr = mapOf(
        "A.01" to ("Le Mode de Vie Basilien et les Coutumes" to "Le Mode de Vie Basilien est notre document constitutionnel, approuvé définitivement par le Saint-Siège en 1983. Ce document, les Coutumes Basiliennes, comprend des normes et pratiques adaptées aux différentes époques, cultures, apostolats et localités."),
        "A.02" to ("Fidélité à l'Appel de Dieu" to "En suivant Le Mode de Vie Basilien et les Coutumes Basiliennes, nous exprimons notre fidélité à Dieu qui nous appelle en Jésus-Christ et par l'Esprit Saint."),
        "A.03" to ("Archives et Documents dans les Maisons Locales" to "Outre Le Mode de Vie Basilien et les Coutumes, chaque communauté locale met à la disposition de ses membres les documents importants de la Congrégation : Constitutions, Règle, rapports des Chapitres Généraux, Annales Basiliennes et Directoires."),
        "A.04" to ("Lecture Annuelle en Communauté" to "Les communautés locales prennent les dispositions nécessaires pour la lecture annuelle du Mode de Vie Basilien et des Coutumes dans un climat de prière et de réflexion."),
        "A.05" to ("Incarnation Locale" to "Les communautés locales discutent de la manière de vivre Le Mode de Vie Basilien et les Coutumes dans leur contexte local. Toute dérogation pour motif grave requiert l'approbation du Supérieur Général."),
        "A.06" to ("Modifications et Interprétations" to "Les modifications aux Coutumes déterminant la composition du Chapitre Général requièrent une majorité des deux tiers ; les autres changements requièrent la majorité simple. Entre les Chapitres, l'interprétation appartient au Conseil Général."),
        "B.01" to ("Amour et Générosité Consacrée" to "Notre vie commune et nos relations doivent exprimer la profondeur d'amour attendue d'hommes qui, par leur chasteté consacrée, suivent le Seigneur dans un don total d'eux-mêmes, reflétant générosité, bonté et solidarité."),
        "B.02" to ("Soutien Mutuel et Vie Commune" to "Pour vivre pleinement le don de la chasteté, nous devons donner et recevoir la force de notre communauté par notre présence fidèle à la prière, aux repas, à la détente et aux réunions fraternelles (Romains 12, 9-10)."),
        "B.06" to ("Tolérance Zéro et Protection des Personnes Vulnérables" to "L'abus sexuel est un mal moral grave qui blesse les victimes et cause un grand scandale. Pour protéger les enfants et les adultes vulnérables, les confrères respectent strictement les lois civiles et les règlements diocésains et basiliens."),
        "C.01" to ("Imitation du Christ et Justice Sociale" to "Notre profession de pauvreté évangélique exige un engagement personnel et communautaire à l'imitation du Christ humble et pauvre, ainsi qu'une participation active à l'engagement de l'Église pour la justice et les démunis."),
        "D.02" to ("Un Esprit Prompt et Vivace" to "Par notre obéissance continue, nous grandissons dans l'union au Christ. Obéissons toujours et partout, dans les petites comme dans les grandes choses, sans hésitation ni murmure, avec un esprit prompt et vivace (Règle de 1962 ; Constitutions de 1863)."),
        "E.15" to ("Mensa Communis" to "Étant ordonnés sous le titre canonique de la mensa communis (table commune), nous considérons le repas principal du jour comme un moment privilégié de notre vie fraternelle."),
        "E.21" to ("Amendement 2022 : Dépendances et Soutien Thérapeutique" to "Lorsqu'il apparaît qu'un membre souffre d'une dépendance (pornographie, substances, jeux ou autres), il doit s'engager dans un accompagnement psychologique et spirituel avec le soutien fraternel de la communauté et du Supérieur Général."),
        "F.06" to ("Éducation Chrétienne de la Jeunesse" to "Le souci apostolique de l'éducation chrétienne des jeunes est une grande tradition basilienne, marquée par une adaptabilité constante aux besoins du Peuple de Dieu."),
        "G.11" to ("Fêtes de Saint Basile" to "La fête liturgique de Saint Basile et Saint Grégoire de Nazianze le 2 janvier, ainsi que la fête traditionnelle de Saint Basile le 14 juin, sont des journées de prière spéciale pour toute la Congrégation."),
        "J.06" to ("Représentation Régionale de la Colombie et du Mexique" to "Les supérieurs du Mexique et de la Colombie sont capitulants de droit. Parmi les scolastiques, au moins un délégué est élu pour le Canada/États-Unis, un pour le Mexique et un pour la Colombie."),
        "J.25" to ("Prière Avant les Élections" to "Tous les capitulants prient : « Esprit Saint, notre Protecteur, que Jésus a envoyé à l'Église pour la conduire vers la Vérité tout entière, viens à nous. Illumine nos esprits de sagesse pour élire celui qui servira le mieux notre communauté. Amen. »"),
        "N.19" to ("Reconnaissance des Supérieurs Régionaux en Colombie et au Mexique" to "Les supérieurs au Mexique et en Colombie sont reconnus comme Supérieurs Régionaux et assument la responsabilité des affaires civiles et ecclésiastiques dans leurs régions."),
        "N.22" to ("Cycle des Nominations en Colombie et au Mexique" to "Les nominations en Colombie prennent normalement effet le 1er janvier, tandis qu'au Mexique elles débutent le 1er juillet. Une communication constante entre le Supérieur Général et les Supérieurs Régionaux est essentielle.")
    )

    // =========================================================================
    // 3. POLÍTICAS Y PROCEDIMIENTOS 2026 LOCALIZATION
    // =========================================================================
    fun localizePoliticas2026Sections(sections: List<PoliticaSection>, lang: AppLanguage): List<PoliticaSection> {
        if (lang == AppLanguage.ES) return sections
        return sections.map { localizePoliticaSection(it, lang) }
    }

    fun localizePoliticaSection(sec: PoliticaSection, lang: AppLanguage): PoliticaSection {
        if (lang == AppLanguage.ES) return sec
        return when (sec.id) {
            "parte_1_politicas" -> when (lang) {
                AppLanguage.EN -> sec.copy(
                    title = "I. Fundamental Safeguarding Policies",
                    subtitle = "Scope, Intent & Definitions of Sexual Misconduct",
                    category = "General Policy",
                    articles = listOf(
                        PoliticaArticle(
                            number = "1.0",
                            title = "Scope and Intent of These Policies",
                            content = "Sexual misconduct is a grave violation of justice and charity that causes severe harm. It is never acceptable behavior. When perpetrated by professed religious, priests, or other ministers, it causes profound spiritual and psychological trauma and alienates victims and families from the message of Jesus Christ and the Church.\n\nThis document reflects the commitment of the Congregation of Saint Basil to comply with all civil laws, the Code of Canon Law, and The Basilian Way of Life. It applies to professed members, novices, and employees across Canada, Colombia, France, Mexico, and the United States.",
                            highlights = listOf(
                                "The primary concern is always the victim.",
                                "Applies to all professed members, novices, and collaborators.",
                                "Aligned with Praesidium Accreditation Standards."
                            )
                        ),
                        PoliticaArticle(
                            number = "2.1",
                            title = "Sexual Abuse of Minors",
                            content = "Any sexual involvement with a person under 18 years of age under civil or canon law. The possession, downloading, distribution, or viewing of child sexual abuse material is legally and canonically equivalent to the sexual abuse of a minor, in accordance with the norms of the Holy See (Dicastery for the Doctrine of the Faith).",
                            highlights = listOf("Zero tolerance: immediate removal from ministry and canonical suspension.")
                        ),
                        PoliticaArticle(
                            number = "2.2",
                            title = "Sexual Exploitation and Abuse of Power",
                            content = "Sexual contact with a vulnerable adult or anyone receiving pastoral care, counseling, or educational services. Basilians in pastoral or educational roles hold an inherent power imbalance; any sexual relationship in this context is an exploitation of trust and a betrayal of sacred ministry.",
                            highlights = listOf("Inherent power imbalance nullifies any claim of consent in pastoral contexts.")
                        ),
                        PoliticaArticle(
                            number = "2.3",
                            title = "Sexual Harassment",
                            content = "Unwelcome sexualized conduct or language, physical or verbal advances, requests for favors, or creation of a hostile, intimidating, or offensive environment in workplaces, schools, or community residences.",
                            highlights = listOf("Strictly prohibited in all educational works, parishes, and religious houses.")
                        ),
                        PoliticaArticle(
                            number = "2.4",
                            title = "Grooming Behaviors",
                            content = "An observable pattern of behaviors aimed at establishing inappropriate intimacy with a minor or vulnerable adult: unjustified favoritism, excessive gifts, private trips, secret messages, and emotional dependency. Grooming must be confronted immediately.",
                            highlights = listOf("Isolated preferential treatment or secret gifts to minors are strictly forbidden.")
                        ),
                        PoliticaArticle(
                            number = "2.5",
                            title = "Boundary Violations (Ministerial Boundaries)",
                            content = "Behaviors that transgress physical, emotional, psychological, or spiritual boundaries in pastoral and professional relationships, managed under Protocol S10.",
                            highlights = listOf("Fraternal community vigilance and mandatory accountability.")
                        ),
                        PoliticaArticle(
                            number = "2.6",
                            title = "Public Misconduct and the Vow of Chastity",
                            content = "Lewd actions, exhibitionism, or behaviors contrary to consecrated celibacy. By his evangelical vow before God (The Basilian Way of Life, arts. 9–13), a religious can never regard sexual activity as compatible with his consecration.",
                            highlights = listOf("Chastity is a sacred witness to the primacy of the things of God.")
                        ),
                        PoliticaArticle(
                            number = "3.0",
                            title = "Absolute Priority of the Victim & Mandatory Reporting",
                            content = "Any minor victim and their family have the right to an immediate, compassionate, and supportive response. Every Basilian who receives an allegation or has reasonable grounds for suspicion MUST immediately notify the competent civil authorities and the Case Manager.",
                            highlights = listOf("Inescapable civil and moral obligation to report immediately to civil authorities.")
                        )
                    )
                )
                AppLanguage.FR -> sec.copy(
                    title = "I. Politiques Fondamentales de Sauvegarde",
                    subtitle = "Portée, Intention et Définition des Conduites Inappropriées",
                    category = "Normes Générales",
                    articles = listOf(
                        PoliticaArticle(
                            number = "1.0",
                            title = "Portée et Intention de ces Politiques",
                            content = "L'inconduite sexuelle est une grave violation de la justice et de la charité qui cause un préjudice profond. Ce document reflète l'engagement de la Congrégation de Saint-Basile à respecter toutes les lois civiles, le Code de Droit Canonique et Le Mode de Vie Basilien au Canada, en Colombie, en France, au Mexique et aux États-Unis.",
                            highlights = listOf(
                                "La préoccupation première sera toujours la victime.",
                                "S'applique aux membres profès, novices et collaborateurs.",
                                "Coordonné avec les normes d'accréditation Praesidium."
                            )
                        ),
                        PoliticaArticle(
                            number = "2.1",
                            title = "Abus Sexuel sur Mineurs",
                            content = "Toute implication sexuelle avec une personne de moins de 18 ans selon le droit civil ou canonique. La possession, le téléchargement ou le visionnage de matériel d'abus pédopornographique est canoniquement et pénalement assimilé à un abus sexuel sur mineur.",
                            highlights = listOf("Tolérance zéro : retrait immédiat et suspension canonique.")
                        ),
                        PoliticaArticle(
                            number = "2.2",
                            title = "Exploitation Sexuelle et Abus de Pouvoir",
                            content = "Contact sexuel avec un adulte vulnérable ou toute personne recevant un accompagnement pastoral ou éducatif. Le déséquilibre de pouvoir inhérent au ministère rend toute relation sexuelle abusive.",
                            highlights = listOf("Le déséquilibre de pouvoir annule toute légitimité de consentement en contexte pastoral.")
                        ),
                        PoliticaArticle(
                            number = "2.3",
                            title = "Harcèlement Sexuel",
                            content = "Comportement ou langage sexualisé non désiré, avances physiques ou verbales ou création d'un environnement hostile dans les écoles, paroisses ou maisons religieuses.",
                            highlights = listOf("Interdiction absolue dans toutes les œuvres éducatives et paroissiales.")
                        ),
                        PoliticaArticle(
                            number = "2.4",
                            title = "Grooming (Comportements de Séduction / Emprise)",
                            content = "Schéma observable visant à établir une intimité inappropriée avec un mineur ou adulte vulnérable : favoritisme, cadeaux excessifs, voyages privés, messages secrets. Doit être confronté immédiatement.",
                            highlights = listOf("Interdiction stricte des cadeaux secrets ou traitements préférentiels isolés.")
                        ),
                        PoliticaArticle(
                            number = "2.5",
                            title = "Violations des Limites Ministérielles",
                            content = "Comportements qui transgressent les limites physiques, émotionnelles, psychologiques ou spirituelles dans les relations pastorales, gérés selon le Protocole S10.",
                            highlights = listOf("Vigilance communautaire fraternelle et reddition de comptes obligatoire.")
                        ),
                        PoliticaArticle(
                            number = "2.6",
                            title = "Inconduite Publique et Vœu de Chasteté",
                            content = "Actes contraires au célibat consacré. Par sa promesse évangélique devant Dieu (Le Mode de Vie Basilien, art. 9–13), le religieux témoigne de la primauté des choses de Dieu.",
                            highlights = listOf("La chasteté est un témoignage sacré rendu au Royaume de Dieu.")
                        ),
                        PoliticaArticle(
                            number = "3.0",
                            title = "Priorité Absolue à la Victime et Signalement Obligatoire",
                            content = "Toute victime et sa famille ont droit à une réponse immédiate et compatissante. Tout Basilien recevant un signalement ou ayant un soupçon raisonnable DOIT informer immédiatement les autorités civiles compétentes et le Gestionnaire de Cas.",
                            highlights = listOf("Obligation civile et morale incontournable de signalement immédiat à la justice.")
                        )
                    )
                )
                AppLanguage.ES -> sec
            }
            "parte_2_procedimientos" -> when (lang) {
                AppLanguage.EN -> sec.copy(
                    title = "II. Investigation & Response Procedures",
                    subtitle = "Operational Structure: Case Manager, Promoter of Justice & Review Board",
                    category = "Action Protocols",
                    articles = listOf(
                        PoliticaArticle(
                            number = "1.1",
                            title = "Institutional Actors",
                            content = "The response system comprises clearly defined roles:\n• Superior General and General Council\n• Case Manager\n• Promoter of Justice (Basilian or independent external investigator)\n• Basilian Review Board (RB — organized with CMSM and Praesidium)\n• Diocesan Bishops of the place of the offense and current residence\n• Official Communications Director (sole authorized spokesperson).",
                            highlights = listOf("Collegial, transparent, and interdisciplinary structure.")
                        ),
                        PoliticaArticle(
                            number = "2.1",
                            title = "Response Sequence to an Allegation",
                            content = "1. Receipt of the complaint by the Case Manager or Superior General.\n2. Formal instruction of the Promoter of Justice.\n3. Prompt and rigorous investigation by the Promoter of Justice.\n4. Report and recommendations submitted to the Review Board (RB).\n5. Advisory assessment by the Review Board to the Superior General.\n6. Canonical and civil execution of the Superior General's determinations.",
                            highlights = listOf("Civil notification and full cooperation with law enforcement.")
                        ),
                        PoliticaArticle(
                            number = "2.4",
                            title = "Therapeutic and Pastoral Care for the Victim",
                            content = "The Congregation provides certified professional psychological counseling and trauma-informed healing support for the victim and immediate family, with initial financial coverage for at least six renewable months.",
                            highlights = listOf("Comprehensive therapeutic accompaniment offered with pastoral compassion.")
                        ),
                        PoliticaArticle(
                            number = "3.4",
                            title = "Administrative Leave & Referral to the Dicastery in Rome",
                            content = "Upon a credible allegation involving a minor, the religious is immediately removed from all pastoral or educational ministry ('precautionary administrative leave'). In accordance with 'Sacramentorum Sanctitatis Tutela' and 'Vos Estis Lux Mundi', the case file is obligatorily referred to the Dicastery for the Doctrine of the Faith in Rome.",
                            highlights = listOf("Mandatory precautionary removal from ministry to safeguard the community.")
                        )
                    )
                )
                AppLanguage.FR -> sec.copy(
                    title = "II. Procédures et Enquête",
                    subtitle = "Structure Opérationnelle : Gestionnaire de Cas, Promoteur de Justice et Comité de Révision",
                    category = "Protocoles d'Action",
                    articles = listOf(
                        PoliticaArticle(
                            number = "1.1",
                            title = "Acteurs Institutionnels",
                            content = "Le système de réponse comprend des rôles définis :\n• Supérieur Général et Conseil Général\n• Gestionnaire de Cas (Case Manager)\n• Promoteur de Justice (enquêteur basilien ou externe indépendant)\n• Comité de Révision Basilien (Review Board organisé avec la CMSM et Praesidium)\n• Évêques diocésains du lieu des faits et de la résidence actuelle\n• Directeur officiel des communications.",
                            highlights = listOf("Structure collégiale, transparente et interdisciplinaire.")
                        ),
                        PoliticaArticle(
                            number = "2.1",
                            title = "Séquence de Réponse à une Plainte",
                            content = "1. Réception de la plainte par le Gestionnaire de Cas ou le Supérieur Général.\n2. Instruction formelle confiée au Promoteur de Justice.\n3. Enquête rapide et rigoureuse.\n4. Rapport et recommandations soumis au Comité de Révision (RB).\n5. Avis du Comité de Révision au Supérieur Général.\n6. Exécution canonique et civile des décisions du Supérieur Général.",
                            highlights = listOf("Notification civile rapide et pleine coopération avec la loi.")
                        ),
                        PoliticaArticle(
                            number = "2.4",
                            title = "Soutien Thérapeutique et Pastoral à la Victime",
                            content = "La Congrégation offre des services professionnels certifiés de soutien psychologique et d'accompagnement des traumatismes pour la victime et sa famille proche.",
                            highlights = listOf("Accompagnement thérapeutique intégral avec compassion pastorale.")
                        ),
                        PoliticaArticle(
                            number = "3.4",
                            title = "Congé Administratif et Renvoi au Dicastère à Rome",
                            content = "Face à une accusation vraisemblable impliquant un mineur, le religieux est immédiatement écarté de tout ministère pastoral ou éducatif. Conformément à 'Sacramentorum Sanctitatis Tutela' et 'Vos Estis Lux Mundi', le dossier est transmis au Dicastère pour la Doctrine de la Foi à Rome.",
                            highlights = listOf("Retrait préventif obligatoire pour sauvegarder la communauté.")
                        )
                    )
                )
                AppLanguage.ES -> sec
            }
            "parte_3_consecuencias" -> when (lang) {
                AppLanguage.EN -> sec.copy(
                    title = "III. Consequences, Safety Plans & Healing",
                    subtitle = "Restorative Justice, Lifelong Supervision & Rehabilitation",
                    category = "Disciplinary Measures & Care",
                    articles = listOf(
                        PoliticaArticle(
                            number = "1.0",
                            title = "Restorative Justice",
                            content = "The offender must seek explicit forgiveness from the victim and family when pastorally appropriate, and strive to repair the spiritual and moral harm caused to the Church and neighbor.",
                            highlights = listOf("Humble and active pursuit of forgiveness and reconciliation.")
                        ),
                        PoliticaArticle(
                            number = "2.1",
                            title = "S20 Safety Plans (Strict Supervision)",
                            content = "If a religious is found guilty or administratively sanctioned, he is placed under a permanent Safety Plan approved by the Review Board: absolute prohibition of unsupervised contact with minors or public pastoral ministry, and assignment of a personal supervisor submitting mandatory quarterly reports.",
                            highlights = listOf("Lifelong supervision for offenders against minors.")
                        ),
                        PoliticaArticle(
                            number = "3.2",
                            title = "Canonical Sanctions (Code of Canon Law)",
                            content = "Violation of restrictions may result in dismissal from the clerical state, suspension from ministry, and perpetual expulsion from the Congregation of Saint Basil (Canons 695, 696, and 697).",
                            highlights = listOf("Strict canonical rigor enforced by the General Curia.")
                        ),
                        PoliticaArticle(
                            number = "4.0",
                            title = "False Accusation and Restoration of Reputation",
                            content = "If after a thorough investigation an allegation proves unfounded, the Congregation will make every effort to restore the good name and reputation of the falsely accused member.",
                            highlights = listOf("Defense of truth and restoration of priestly honor.")
                        )
                    )
                )
                AppLanguage.FR -> sec.copy(
                    title = "III. Conséquences, Plans de Sécurité et Guérison",
                    subtitle = "Justice Réparatrice, Supervision à Vie et Réhabilitation",
                    category = "Mesures Disciplinaires et Soins",
                    articles = listOf(
                        PoliticaArticle(
                            number = "1.0",
                            title = "Justice Réparatrice",
                            content = "L'auteur doit demander pardon à la victime et à sa famille lorsque cela est pastoralement approprié, et chercher à réparer le tort spirituel et moral causé.",
                            highlights = listOf("Recherche humble et active du pardon et de la réconciliation.")
                        ),
                        PoliticaArticle(
                            number = "2.1",
                            title = "Plans de Sécurité S20 (Supervision Stricte)",
                            content = "Tout religieux sanctionné est soumis à un Plan de Sécurité permanent approuvé par le Comité de Révision : interdiction absolue de tout contact non supervisé avec des mineurs ou de ministère public, et désignation d'un superviseur avec rapports trimestriels obligatoires.",
                            highlights = listOf("Supervision à vie pour les auteurs d'abus sur mineurs.")
                        ),
                        PoliticaArticle(
                            number = "3.2",
                            title = "Sanctions Canoniques (Code de Droit Canonique)",
                            content = "Le non-respect des restrictions peut entraîner le renvoi de l'état clérical, la suspension du ministère et l'expulsion perpétuelle de la Congrégation de Saint-Basile (Canons 695, 696 et 697).",
                            highlights = listOf("Rigueur canonique stricte appliquée par la Curie Générale.")
                        ),
                        PoliticaArticle(
                            number = "4.0",
                            title = "Fausse Accusation et Réparation de la Vérité",
                            content = "Si, après une enquête approfondie, une plainte s'avère infondée, la Congrégation déploiera tous les efforts nécessaires pour rétablir la réputation du membre faussement accusé.",
                            highlights = listOf("Défense de la vérité et restauration de l'honneur sacerdotal.")
                        )
                    )
                )
                AppLanguage.ES -> sec
            }
            "apendices_praesidium" -> when (lang) {
                AppLanguage.EN -> sec.copy(
                    title = "IV. Appendices & Praesidium Standards",
                    subtitle = "S8 Interaction Norms, Electronic Pornography & S7 Visiting Members",
                    category = "Praesidium Standards",
                    articles = listOf(
                        PoliticaArticle(
                            number = "S8-A",
                            title = "Physical Interaction Standards with Minors",
                            content = "• Appropriate: Brief side hugs, handshakes, pats on the shoulder, high-fives, kneeling to eye level with children in open group settings.\n• Inappropriate & Prohibited: Kissing on the lips, seating children over age 4 on laps, massages, touching in bedrooms or closed areas, sleeping in the same room or tent.",
                            highlights = listOf("Open environments and always in the presence of at least two responsible adults.")
                        ),
                        PoliticaArticle(
                            number = "S8-B",
                            title = "Electronic Communications and Social Media",
                            content = "Private messaging channels (WhatsApp, personal chats, 1-on-1 video calls) with minors are prohibited without explicit copy and consent of parents or guardians. Never send or request content with double entendre or intimate emotional connotation.",
                            highlights = listOf("Total transparency in digital communications with youth.")
                        ),
                        PoliticaArticle(
                            number = "Ap. 2",
                            title = "Policy on Electronically Generated Pornography",
                            content = "Using, downloading, or viewing pornography is a grave offense against consecrated chastity (Benedict XVI, Deus Caritas Est #5). Community computers and devices are goods for apostolic service. Child pornography constitutes a serious federal crime and a canonical delict reserved to the Holy See.",
                            highlights = listOf("A religious's computer is a communal good subject to evangelical ethics.")
                        ),
                        PoliticaArticle(
                            number = "S7",
                            title = "Visiting and International Religious (S7 R6_R7)",
                            content = "Every external priest or religious residing in a Basilian house must present a letter of good standing from his Major Superior, complete Praesidium Academy modules, and undergo criminal background screening prior to any apostolic work.",
                            highlights = listOf("Guaranteed safe environment across all Basilian houses worldwide.")
                        )
                    )
                )
                AppLanguage.FR -> sec.copy(
                    title = "IV. Appendices et Normes Praesidium",
                    subtitle = "Normes d'Interaction S8, Pornographie Électronique et Membres Visiteurs S7",
                    category = "Standards Praesidium",
                    articles = listOf(
                        PoliticaArticle(
                            number = "S8-A",
                            title = "Normes d'Interaction Physique avec les Mineurs",
                            content = "• Approprié : Brèves accolades latérales, poignées de main, tapes amicales sur l'épaule, en groupe ouvert.\n• Inapproprié et Interdit : Baisers sur la bouche, asseoir des enfants de plus de 4 ans sur les genoux, massages, caresses dans des chambres ou lieux fermés, dormir dans la même chambre ou tente.",
                            highlights = listOf("Lieux ouverts et toujours en présence d'au moins deux adultes responsables.")
                        ),
                        PoliticaArticle(
                            number = "S8-B",
                            title = "Communications Électroniques et Réseaux Sociaux",
                            content = "Il est interdit de communiquer par canaux privés (WhatsApp, chats personnels, appels vidéo 1 à 1) avec des mineurs sans copie et consentement explicite des parents ou tuteurs.",
                            highlights = listOf("Transparence totale dans les communications numériques avec les jeunes.")
                        ),
                        PoliticaArticle(
                            number = "Ap. 2",
                            title = "Politique sur la Pornographie Électronique",
                            content = "L'utilisation, le téléchargement ou le visionnage de pornographie est une faute très grave contre la chasteté consacrée (Benoît XVI, Deus Caritas Est #5). La pédopornographie constitue un crime pénal et un délit canonique réservé au Saint-Siège.",
                            highlights = listOf("L'ordinateur personnel du religieux est un bien communautaire soumis à l'éthique évangélique.")
                        ),
                        PoliticaArticle(
                            number = "S7",
                            title = "Religieux Visiteurs et Internationaux (S7 R6_R7)",
                            content = "Tout prêtre ou religieux externe résidant dans une maison basilienne doit présenter une lettre de recommandation de son Supérieur Majeur, valider les cours Praesidium Academy et se soumettre à une vérification des antécédents judiciaires.",
                            highlights = listOf("Garantie d'un environnement sûr dans toutes les maisons basiliennes du monde.")
                        )
                    )
                )
                AppLanguage.ES -> sec
            }
            else -> sec
        }
    }

    // =========================================================================
    // 4. APUNTES COLOMBIA DATA LOCALIZATION (BOOK & CHAPTERS)
    // =========================================================================
    fun localizeApuntesColombiaBook(book: ApuntesColombiaBook, lang: AppLanguage): ApuntesColombiaBook {
        if (lang == AppLanguage.ES) return book
        return when (lang) {
            AppLanguage.EN -> book.copy(
                title = "Notes for a History of the Congregation of Saint Basil in Colombia",
                subtitle = "In Its First Ten Years (August 15, 1987 – 1997)",
                author = "Luis Fernando Sánchez Hurtado, C.S.B. (with the collaboration of Efraín Ferrer)",
                prologueAuthor = "Fr. Robert Barringer, C.S.B. • Superior General",
                prologueDate = "June 24, 1997 • Toronto",
                prologueText = listOf(
                    "The beginnings of the Congregation of Saint Basil in Colombia are so recent that it might seem unusual to compile its chronicles contemporaneously with the events themselves. Yet the lived experience of the Basilian community in Colombia and its vision for the future deserve to be known and understood right now by Basilians throughout the entire Congregation.",
                    "This work offers a fine example of the initiative and energy that young Colombian Basilians bring not only to the growth of the Church in their own country, but also to the future of the whole Congregation of Saint Basil.",
                    "In the name of the entire Congregation, I thank all who labored on this work: it helps all of us who are not Colombian to know something of what God has done there, and what He continues to do through the hands and dreams of our brothers."
                ),
                introductionText = listOf(
                    "Why write a history of the Congregation of Saint Basil in Colombia? A necessary starting point is the urgency of reflecting on its life and mission through the years. The proposal sprang up after Fr. Francisco Amico shared his experiences with the Basilian novices in Medellín.",
                    "Historical data was gathered through interviews with those whose effort and courage forged this path, letters exchanged from the very beginning with bishops, and the vital contribution of Sister Hermengarda Alves (General Secretary of CLAR).",
                    "This work seeks to help read the footsteps of God through our history in Colombia, and to evaluate our response to God the Father who wished to make us instruments of His love among this portion of His people."
                ),
                chapters = book.chapters.map { localizeApuntesChapter(it, AppLanguage.EN) }
            )
            AppLanguage.FR -> book.copy(
                title = "Notes pour une Histoire de la Congrégation de Saint-Basile en Colombie",
                subtitle = "Durant ses dix premières années (15 août 1987 – 1997)",
                author = "Luis Fernando Sánchez Hurtado, C.S.B. (avec la collaboration d'Efraín Ferrer)",
                prologueAuthor = "P. Robert Barringer, C.S.B. • Supérieur Général",
                prologueDate = "24 juin 1997 • Toronto",
                prologueText = listOf(
                    "Les débuts de la Congrégation de Saint-Basile en Colombie sont si récents qu'il peut paraître singulier d'en rédiger les chroniques si tôt. Cependant, l'expérience vécue par la communauté basilienne en Colombie et sa vision de l'avenir méritent d'être connues et comprises dès maintenant par toute la Congrégation.",
                    "Cet ouvrage offre un bel exemple de l'initiative et de l'énergie avec lesquelles les jeunes Basiliens colombiens contribuent non seulement au développement de l'Église dans leur pays, mais aussi à l'avenir de toute la Congrégation de Saint-Basile.",
                    "Au nom de toute la Congrégation, je remercie tous ceux qui ont œuvré à cet ouvrage : il nous aide à connaître ce que Dieu a accompli là-bas et ce qu'Il continue de faire par les mains et les rêves de nos frères."
                ),
                introductionText = listOf(
                    "Pourquoi une histoire de la Congrégation de Saint-Basile en Colombie ? La proposition est née après que le Père Francisco Amico a partagé ses souvenirs avec les novices basiliens à Medellín.",
                    "Les données ont été recueillies par des entretiens avec les pionniers, les lettres échangées avec les évêques et l'apport fondamental de Sœur Hermengarda Alves (secrétaire générale de la CLAR).",
                    "Cet ouvrage veut aider à lire le passage de Dieu dans notre histoire en Colombie et à évaluer notre réponse à Dieu le Père qui a voulu faire de nous des instruments de son amour."
                ),
                chapters = book.chapters.map { localizeApuntesChapter(it, AppLanguage.FR) }
            )
            AppLanguage.ES -> book
        }
    }

    private fun localizeApuntesChapter(ch: ApuntesChapter, lang: AppLanguage): ApuntesChapter {
        if (lang == AppLanguage.ES) return ch
        return when (ch.id) {
            "homenajes" -> when (lang) {
                AppLanguage.EN -> ch.copy(
                    title = "Tribute to Founders and Patrons",
                    subtitle = "Annonay 1822 • Colombia 1987 • Patron Saints",
                    summary = "Tribute to the 10 French founders, the pioneers in Colombia, and the Prayer to Saint Basil the Great.",
                    sections = listOf(
                        ApuntesSection(
                            title = "The Ten French Founders (Annonay 1822)",
                            content = "• Fr. Joseph Lapierre\n• Fr. Vincent Duret\n• Fr. Augustin Payan\n• Fr. François Polly\n• Fr. Pierre Tourvieille\n• Fr. Julien Tracol\n• Fr. André Fayolle\n• Fr. Henri Martinesche\n• Fr. Jean François Pages\n• Jean Antoine Vallon\n\n«We praise our fathers who were men committed to their time...»"
                        ),
                        ApuntesSection(
                            title = "The Founders in Colombia (1987)",
                            content = "• Fr. Francisco Amico Ferrari, C.S.B.\n• Fr. Robert Seguin Bartolo, C.S.B.\n• Fr. Maurice Luke Restivo Bidet, C.S.B.\n• Fr. Rafael Ignacio Lopera Montoya, C.S.B.\n• Mr. Eduardo Cuadra Campos\n• Mr. Martín Escobedo Martínez"
                        ),
                        ApuntesSection(
                            title = "Patron Saints & Prayer to Saint Basil",
                            content = "Patrons:\n1. Saint Basil the Great\n2. Saint Francis of Assisi\n3. Saint Thomas Aquinas\n4. Saint John Bosco\n5. Saint Joseph\n6. Saint Mary, Mother of God\n\nPRAYER TO SAINT BASIL:\n«Holy Father Basil, patron of our Congregation whose apostolate is Christian education, be our model in teaching and our support in ministry. Grant us that love for the truth which you manifested throughout your life, that we may be instruments in leading others to the supreme Truth. Watch over your Congregation, which you yourself have cared for from its beginning. Amen.»"
                        )
                    )
                )
                AppLanguage.FR -> ch.copy(
                    title = "Hommage aux Fondateurs et Saints Patrons",
                    subtitle = "Annonay 1822 • Colombie 1987 • Saints Patrons",
                    summary = "Reconnaissance aux 10 fondateurs français, aux pionniers en Colombie et prière à Saint Basile le Grand.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Les Dix Fondateurs Français (Annonay 1822)",
                            content = "• P. Joseph Lapierre\n• P. Vincent Duret\n• P. Augustin Payan\n• P. François Polly\n• P. Pierre Tourvieille\n• P. Julien Tracol\n• P. André Fayolle\n• P. Henri Martinesche\n• P. Jean François Pages\n• Jean Antoine Vallon\n\n« Nous louons nos pères qui furent des hommes engagés dans leur temps... »"
                        ),
                        ApuntesSection(
                            title = "Les Fondateurs en Colombie (1987)",
                            content = "• P. Francisco Amico Ferrari, C.S.B.\n• P. Robert Seguin Bartolo, C.S.B.\n• P. Maurice Luke Restivo Bidet, C.S.B.\n• P. Rafael Ignacio Lopera Montoya, C.S.B.\n• M. Eduardo Cuadra Campos\n• M. Martín Escobedo Martínez"
                        ),
                        ApuntesSection(
                            title = "Saints Patrons et Prière à Saint Basile",
                            content = "Patrons :\n1. Saint Basile le Grand\n2. Saint François d'Assise\n3. Saint Thomas d'Aquin\n4. Saint Jean Bosco\n5. Saint Joseph\n6. Sainte Marie, Mère de Dieu\n\nPRIÈRE À SAINT BASILE :\n« Saint Père Basile, patron de notre Congrégation qui a pour apostolat l'enseignement chrétien, sois notre modèle dans l'enseignement et notre soutien dans le ministère. Donne-nous cet amour de la vérité que tu as manifesté dans ta vie, afin que nous conduisions les autres à la Vérité suprême. Veille sur ta Congrégation que tu as toi-même gardée depuis ses débuts. Amen. »"
                        )
                    )
                )
                AppLanguage.ES -> ch
            }
            "cap_1" -> when (lang) {
                AppLanguage.EN -> ch.copy(
                    title = "Conceiving the Foundation in Colombia",
                    subtitle = "First Contacts: November 1985 to July 1988",
                    summary = "Initial discernment by Superior General Ulysse Paré, the meeting with Sr. Hermengarda Alves (CLAR), and Archbishop Pedro Rubiano's invitation to Cali.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Missionary Desire in Latin America",
                            content = "Fr. Ulysse Paré, then Superior General, recounts: «While Fr. Hanrahan was still Superior General, he and I spoke of starting a second project in Latin America. After evaluating projects in Africa that lacked direct evangelization, the call to Colombia emerged. Francisco Amico and Rafael Ignacio Lopera traveled to explore needy dioceses.»",
                            quote = "Our main concern had to do with the extreme isolation of the first founders... The other great concern was the threat of violence.",
                            quoteAuthor = "Fr. Ulysse Paré, C.S.B."
                        ),
                        ApuntesSection(
                            title = "The Providential Bridge: Sr. Hermengarda Alves (CLAR)",
                            content = "In November 1985, Fr. Francisco wrote to CLAR in Bogotá. Sister Hermengarda Alves, General Secretary, welcomed him warmly and advised contacting several bishops: Sincelejo, Cartagena, Barranquilla, Medellín, and Cali. The doors opened widest in Cali with Archbishop Pedro Rubiano Sáenz."
                        ),
                        ApuntesSection(
                            title = "The Decision in Favor of Cali",
                            content = "Archbishop Pedro Rubiano wrote: «I pray to the Lord that the decision of the General Council inclines toward the Foundation in Cali, and I repeat that here I await you—not only as a friendly bishop, but as a brother in Christ.» In November 1986, the General Council in Toronto voted to assume Our Lady of the Assumption Parish in Cali."
                        )
                    )
                )
                AppLanguage.FR -> ch.copy(
                    title = "Genèse de l'Idée de Fondation en Colombie",
                    subtitle = "Les premiers contacts : novembre 1985 à juillet 1988",
                    summary = "Le discernement initial du Supérieur Général Ulysse Paré, la rencontre avec Sœur Hermengarda Alves et l'invitation de Mgr Pedro Rubiano à Cali.",
                    sections = listOf(
                        ApuntesSection(
                            title = "L'Élan Missionnaire en Amérique Latine",
                            content = "Le Père Ulysse Paré, alors Supérieur Général, raconte : « Du temps où le Père Hanrahan était encore Supérieur Général, nous parlions de lancer un second projet en Amérique Latine. Francisco Amico et Rafael Ignacio Lopera ont voyagé pour explorer les diocèses ayant les plus grands besoins pastoraux. »",
                            quote = "Notre crainte principale concernait l'isolement des premiers fondateurs... L'autre grande crainte était la menace de la violence.",
                            quoteAuthor = "P. Ulysse Paré, C.S.B."
                        ),
                        ApuntesSection(
                            title = "Le Pont Providentiel : Sœur Hermengarda Alves (CLAR)",
                            content = "En novembre 1985, le P. Francisco écrivit à la CLAR (Bogotá). Sœur Hermengarda Alves lui offrit un accueil fraternel et l'orienta vers plusieurs évêques, notamment Mgr Pedro Rubiano Sáenz à Cali."
                        ),
                        ApuntesSection(
                            title = "La Décision en Faveur de Cali",
                            content = "Mgr Pedro Rubiano écrivit : « Je prie le Seigneur que la décision du Conseil Général s'incline vers la fondation à Cali ; vous avez ici non seulement un évêque ami, mais un frère dans le Christ. » En novembre 1986, le Conseil Général à Toronto vota l'acceptation de la paroisse Notre-Dame de l'Assomption à Cali."
                        )
                    )
                )
                AppLanguage.ES -> ch
            }
            "cap_2" -> when (lang) {
                AppLanguage.EN -> ch.copy(
                    title = "The Arrival of the Basilians in Cali",
                    subtitle = "July 1987: First Impressions and the Reality of Andrés Sanín",
                    summary = "The arrival of Francisco Amico, Robert Seguin, Maurice Restivo, Rafael Lopera, and the first lay collaborators in eastern Cali.",
                    sections = listOf(
                        ApuntesSection(
                            title = "First Impressions and the Shock of Reality",
                            content = "Fr. Maurice Restivo recalls: «The first thing that struck me was the heat, dust, burning trash, potholes... Yet at the same time I was deeply moved by the people: so kind, open, and welcoming.»",
                            quote = "Suddenly in the distance, a breathtaking landscape: the snow-capped Nevado del Huila volcano. God showed them that even amid poverty and violence, beauty was possible.",
                            quoteAuthor = "Fr. Robert Seguin & Fr. Francisco Amico"
                        ),
                        ApuntesSection(
                            title = "An Abandoned Rectory Transformed into a Home",
                            content = "The parish house was desolate, without dishes or kitchen, full of leaks. Sr. Ángela Betancur recalls: «I admired them! Every single one of them took up mop and broom and completely transformed the house. Basilian order and dedication immediately shone through.»",
                            quote = "To promote unity in the midst of a people divided by poverty and violence.",
                            quoteAuthor = "Doña Adelaida Cabezas (Legion of Mary)"
                        )
                    )
                )
                AppLanguage.FR -> ch.copy(
                    title = "L'Arrivée des Basiliens à Cali",
                    subtitle = "Juillet 1987 : des premières impressions à la réalité d'Andrés Sanín",
                    summary = "L'arrivée de Francisco Amico, Robert Seguin, Maurice Restivo, Rafael Lopera et des premiers collaborateurs dans l'est de Cali.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Premières Impressions et Choc de la Réalité",
                            content = "Le P. Maurice Restivo se souvient : « Ce qui m'a frappé d'abord, c'est la chaleur et la poussière... Et en même temps, j'ai été touché par les gens : si aimables, si ouverts et accueillants. »",
                            quote = "Soudain, au loin, un paysage magnifique : le volcan enneigé du Huila. Dieu leur montrait qu'au milieu de la pauvreté, la beauté était possible.",
                            quoteAuthor = "P. Robert Seguin et P. Francisco Amico"
                        ),
                        ApuntesSection(
                            title = "Une Maison Abandonnée Transformée en Foyer",
                            content = "Le presbytère était délabré. Sœur Ángela Betancur raconte : « Je les admirais ! Tous sans exception ont pris balai et serpillière et ont transformé la maison. L'ordre et le dévouement basiliens rayonnaient déjà. »",
                            quote = "Promouvoir l'unité au milieu d'un peuple blessé par la pauvreté et la violence.",
                            quoteAuthor = "Doña Adelaida Cabezas (Légion de Marie)"
                        )
                    )
                )
                AppLanguage.ES -> ch
            }
            "cap_3" -> when (lang) {
                AppLanguage.EN -> ch.copy(
                    title = "Shaping Community Life",
                    subtitle = "Criteria, Customs, and Communal Prayer in Colombia",
                    summary = "Liturgical life, midday visit to the Blessed Sacrament, the Basilian habit in Colombia, Sunday Taizé adoration, and the Prayer for Common Life.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Basilian Customs in Colombia",
                            content = "1. Common prayers: Daily Lauds and Vespers; Compline at night sung in Latin by candlelight with the seasonal Marian antiphon. At midday, visit to the Blessed Sacrament with a psalm, Prayer to Saint Basil, and 'Sub Tuum Praesidium', plus the Angelus for vocations.\n2. Basilian habit for professed members in Colombia: Cream-colored Basilian guayabera shirt, wooden cross bearing the crest of the Congregation, and sandals.\n3. Sunday night prayer: Community gathering at 9:00 p.m. for adoration of Jesus, Bread of Life, in the style of Taizé."
                        ),
                        ApuntesSection(
                            title = "Prayer for Life in Common (Full Text)",
                            content = "«Lord, grant that our love may be sincere;\nthat we may abhor evil and hold fast to all that is good;\nthat in brotherly love we may show tender affection for one another;\nthat in respect we may esteem others as more worthy than ourselves;\nthat in fulfilling our duty we may never grow slack;\nthat in spirit we may be fervent;\nthat we may rejoice in hope, be patient in tribulation, and share generously with those in need;\nthat we may eagerly welcome guests and strangers into our home;\n\nLord, let us bless those who persecute us—bless and never curse;\nlet us rejoice with those who rejoice and weep with those who weep;\nlet us live in harmony with all, doing what is good in the sight of all, and doing everything in our power to live in peace with everyone. Amen.»"
                        )
                    )
                )
                AppLanguage.FR -> ch.copy(
                    title = "Façonner la Vie Communautaire",
                    subtitle = "Critères, coutumes et prière communautaire en Colombie",
                    summary = "La vie liturgique, la visite au Saint-Sacrement, l'habit basilien en Colombie, la prière de Taizé et la Prière pour la Vie Commune.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Des Coutumes Basiliennes en Colombie",
                            content = "1. Prières communes : Laudes et Vêpres quotidiennes ; Complies chantées en latin à la lueur d'un cierge. À midi, visite au Saint-Sacrement avec psaume, Prière à Saint Basile et Angélus pour les vocations.\n2. Habit basilien en Colombie : Chemise couleur crème, croix de bois avec le blason de la Congrégation et sandales.\n3. Prière du dimanche soir : Adoration à 21h00 dans l'esprit de Taizé."
                        ),
                        ApuntesSection(
                            title = "Prière pour la Vie en Commun (Texte Intégral)",
                            content = "« Seigneur, accorde-nous que notre amour soit sincère ;\nque nous ayons horreur du mal et prenions soin de tout ce qui est bien ;\nque dans l'amour fraternel nous nous témoignions de l'affection les uns aux autres ;\nque dans le respect nous estimions les autres plus que nous-mêmes ;\nque nous soyons fervents d'esprit, joyeux dans l'espérance, patients dans l'épreuve et empressés à partager avec ceux qui sont dans le besoin et à accueillir les hôtes ;\n\nSeigneur, que nous bénissions ceux qui nous persécutent, que nous nous réjouissions avec ceux qui sont dans la joie et pleurions avec ceux qui pleurent, faisant tout notre possible pour vivre en paix avec tous. Amen. »"
                        )
                    )
                )
                AppLanguage.ES -> ch
            }
            "cap_4" -> when (lang) {
                AppLanguage.EN -> ch.copy(
                    title = "Other Brothers Who Arrived",
                    subtitle = "Vocations and Missionaries Who Forged the Presence",
                    summary = "Fr. Francis Serafín Mauricio, Fr. Lawrence Moran, Fr. Kevin Anderson, Fr. John Malo, Fr. Philip Platt, and Fr. Vincent Thompson.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Fr. Lawrence Moran, Fr. John Malo & Fr. Philip Platt",
                            content = "• Fr. Lawrence Moran (1989–1991): Fondly remembered for his work with the poorest families in La Playa and his magic tricks that delighted the children.\n• Fr. John Malo (1992–1993): English teacher, altar server chaplain, and promoter of base communities in La Playa («the only essential language is love»).\n• Fr. Philip Platt (1991–1996): Scholar and historian, author of the 'Brief Outline of Basilian History 1822–2000'.\n• Fr. Vincent Thompson (1995): Formator and cooperator in Medellín and Bogotá."
                        )
                    )
                )
                AppLanguage.FR -> ch.copy(
                    title = "Les Autres Frères Qui Sont Arrivés",
                    subtitle = "Vocations et missionnaires qui ont forgé la présence",
                    summary = "P. Francisco Serafín Mauricio, P. Lawrence Moran, P. Kevin Anderson, P. Jean Malo, P. Philip Platt et P. Vincent Thompson.",
                    sections = listOf(
                        ApuntesSection(
                            title = "P. Lawrence Moran, P. Jean Malo et P. Philip Platt",
                            content = "• P. Lawrence Moran (1989–1991) : Rappelé avec joie pour son travail auprès des plus pauvres à La Playa.\n• P. Jean Malo (1992–1993) : Professeur d'anglais et animateur des communautés ecclésiales (« le seul langage essentiel est l'amour »).\n• P. Philip Platt (1991–1996) : Érudit et historien, auteur de l'« Esquisse d'Histoire Basilienne 1822–2000 ».\n• P. Vincent Thompson (1995) : Formateur et coopérateur à Medellín et Bogotá."
                        )
                    )
                )
                AppLanguage.ES -> ch
            }
            "cap_5" -> when (lang) {
                AppLanguage.EN -> ch.copy(
                    title = "Developing an Integral Pastoral Ministry",
                    subtitle = "Base Communities, Eldercare Home, Medical Center, Library & INSA School",
                    summary = "Social and educational works born from the Basilian charism of education and service to the most vulnerable.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Base Ecclesial Communities (CEBs)",
                            content = "Born from the parish census and home visits in Siete de Agosto, Puerto Mallarino, and Andrés Sanín. Families gathered every Tuesday and Wednesday to pray with the Bible read from daily life, share bread, and build solidarity."
                        ),
                        ApuntesSection(
                            title = "«Christ of Peace» Eldercare Home & Saint Basil Medical Center",
                            content = "• «Cristo de la Paz» Home for the Elderly (December 1989): Founded alongside Mother Teresa's Missionaries of Charity to shelter abandoned seniors in eastern Cali.\n• Saint Basil Medical Center (1990): Medical, dental, and psychological care for low-income families across from the parish church.\n• Aldemar Rodríguez Carvajal Community Library and Cultural Center (1997)."
                        ),
                        ApuntesSection(
                            title = "Our Lady of the Assumption Institute (INSA)",
                            content = "Founded in 1991 to provide quality Christian education to children and youth of limited resources, centered on family, human dignity, and social commitment."
                        )
                    )
                )
                AppLanguage.FR -> ch.copy(
                    title = "Développer une Pastorale Intégrale",
                    subtitle = "CEBs, Foyer pour Aînés, Centre Médical, Bibliothèque et Institut INSA",
                    summary = "Les œuvres sociales et éducatives nées du charisme basilien au service des plus démunis.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Les Communautés Ecclésiales de Base (CEBs)",
                            content = "Nées du recensement paroissial et des visites à domicile à Siete de Agosto, Puerto Mallarino et Andrés Sanín. Les familles se réunissent chaque semaine pour prier la Parole de Dieu et tisser des liens fraternels."
                        ),
                        ApuntesSection(
                            title = "Foyer « Christ de la Paix » et Centre Médical Saint-Basile",
                            content = "• Foyer « Cristo de la Paz » (décembre 1989) : Initiative conjointe avec les Missionnaires de la Charité de Mère Teresa pour accueillir les personnes âgées abandonnées.\n• Centre Médical Saint-Basile (1990) : Soins médicaux, dentaires et psychologiques.\n• Bibliothèque Communautaire et Centre Culturel Aldemar Rodríguez Carvajal (1997)."
                        ),
                        ApuntesSection(
                            title = "L'Institut Notre-Dame de l'Assomption (INSA)",
                            content = "Fondé en 1991 pour offrir une éducation chrétienne de qualité aux enfants et jeunes de milieux modestes."
                        )
                    )
                )
                AppLanguage.ES -> ch
            }
            "cap_8" -> when (lang) {
                AppLanguage.EN -> ch.copy(
                    title = "Trials Put Our Presence to the Test",
                    subtitle = "The Kidnapping of Fr. Francisco Amico, C.S.B. (February 15, 1990)",
                    summary = "The painful kidnapping of the pastor by an armed group, the mobilization of faith of the people of Cali, and his courageous return.",
                    sections = listOf(
                        ApuntesSection(
                            title = "The Kidnapping in Andrés Sanín",
                            content = "On the eve of the 1990 Cartagena summit, armed members of the ELN kidnapped Fr. Francisco Amico from his parish. The news shocked Cali and the international Church, and the entire parish entered into permanent prayer vigil."
                        ),
                        ApuntesSection(
                            title = "Silent Marches and Release",
                            content = "The people of Andrés Sanín and Puerto Mallarino held torchlight marches and prayer chains. After five days of captivity, Fr. Francisco was released on the night of February 20, 1990. Entering the church amid tears and ringing bells, he exclaimed: «I am not leaving; I am staying with my people, because here is my world and my family.»",
                            quote = "I will not abandon my parishioners; I will stay with the people I love and who need me.",
                            quoteAuthor = "Fr. Francisco Amico Ferrari, C.S.B."
                        )
                    )
                )
                AppLanguage.FR -> ch.copy(
                    title = "L'Épreuve Met Notre Présence à l'Essai",
                    subtitle = "L'enlèvement du P. Francisco Amico, C.S.B. (15 février 1990)",
                    summary = "Le douloureux enlèvement du curé, la mobilisation de foi du peuple de Cali et son retour courageux.",
                    sections = listOf(
                        ApuntesSection(
                            title = "L'Enlèvement à Andrés Sanín",
                            content = "Le 15 février 1990, des membres armés de l'ELN ont enlevé le P. Francisco Amico dans sa paroisse. La nouvelle a bouleversé Cali et l'Église internationale, et toute la communauté s'est unie en vigile permanente."
                        ),
                        ApuntesSection(
                            title = "Marches du Silence et Libération",
                            content = "Le peuple d'Andrés Sanín et de Puerto Mallarino a organisé des marches aux flambeaux et des chaînes de prière. Libéré la nuit du 20 février 1990, le P. Francisco s'écria dans l'église : « Je ne pars pas, je reste avec mon peuple ! »",
                            quote = "Je n'abandonnerai pas mes paroissiens, je resterai avec les gens que j'aime et qui ont besoin de moi.",
                            quoteAuthor = "P. Francisco Amico Ferrari, C.S.B."
                        )
                    )
                )
                AppLanguage.ES -> ch
            }
            "cap_9" -> when (lang) {
                AppLanguage.EN -> ch.copy(
                    title = "Aldemar Rodríguez Carvajal, Our Martyr",
                    subtitle = "A Young Witness Who United Christian Faith and Social Justice",
                    summary = "The life, dedication, and martyrdom of the 20-year-old catechist during Holy Week of 1992, and Fr. Francisco Amico's homily.",
                    sections = listOf(
                        ApuntesSection(
                            title = "The Young Catechist of the Assumption",
                            content = "Aldemar (born in Neiva in 1972) was a Confirmation catechist and human rights leader. Committed to the poor, he animated Base Communities in La Playa and dreamed of a society built on the Gospel."
                        ),
                        ApuntesSection(
                            title = "Martyrdom in Holy Week 1992",
                            content = "On Holy Wednesday of 1992, he was abducted, tortured, and murdered by death squads. His body was waked in the Church of the Assumption amid grief and paschal hope."
                        ),
                        ApuntesSection(
                            title = "Homily of Fr. Francisco Amico, C.S.B. (April 23, 1992)",
                            content = "«Aldemar was a catechist of this community where bread is broken. Every week he went to La Playa to bring life to the humble. Aldemar came frequently to the Eucharist; he received Christ so as to perform the deeds of Christ... Is that why they killed him?\n\nAldemar, martyr of the Assumption, pray for us your pilgrim brothers, so that we may walk firmly and share generously until one day we enjoy true brotherhood here in Colombia. Aldemar, friend and companion, teacher and martyr, we will never forget you! Amen.»"
                        )
                    )
                )
                AppLanguage.FR -> ch.copy(
                    title = "Aldemar Rodríguez Carvajal, Notre Martyr",
                    subtitle = "Témoin qui a uni engagement chrétien et justice sociale",
                    summary = "La vie, le don de soi et le martyre du jeune catéchiste de 20 ans durant la Semaine Sainte de 1992, et l'homélie de Francisco Amico.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Le Jeune Catéchiste de l'Assomption",
                            content = "Aldemar (né à Neiva en 1972) était catéchiste de confirmation et défenseur des droits humains, animant les communautés de base à La Playa."
                        ),
                        ApuntesSection(
                            title = "Le Martyre durant la Semaine Sainte de 1992",
                            content = "Le Mercredi Saint de 1992, il fut enlevé, torturé et assassiné. Son corps fut veillé dans l'église de l'Assomption entre douleur et espérance pascale."
                        ),
                        ApuntesSection(
                            title = "Homélie de Francisco Amico, C.S.B. (23 avril 1992)",
                            content = "« Aldemar était catéchiste de cette communauté où l'on partage le pain. Chaque semaine il allait à La Playa porter la vie aux humbles. Il recevait le Christ dans l'Eucharistie pour accomplir les gestes du Christ... Est-ce pour cela qu'on l'a tué ?\n\nAldemar, martyr de l'Assomption, prie pour nous tes frères pèlerins, afin que nous marchions fermement vers une fraternité véritable en Colombie. Aldemar, ami et compagnon, maître et martyr, nous ne t'oublierons jamais ! Amen. »"
                        )
                    )
                )
                AppLanguage.ES -> ch
            }
            "cap_10" -> when (lang) {
                AppLanguage.EN -> ch.copy(
                    title = "The Second Foundation: Medellín",
                    subtitle = "Saint Basil Novitiate (1995) & El Ecce Homo Parish (1996)",
                    summary = "Opening of the formation house in Barrio Prado and pastoral care of the parish in the hillside neighborhood of Blanquizal.",
                    sections = listOf(
                        ApuntesSection(
                            title = "The Novitiate and El Ecce Homo Parish",
                            content = "On July 30, 1995, Archbishop Héctor Rueda Hernández authorized the canonical erection of Saint Basil Novitiate in Medellín. Shortly afterward, on September 16, 1996, the Congregation assumed El Ecce Homo Parish in Blanquizal, a working-class hillside neighborhood populated by displaced families."
                        )
                    )
                )
                AppLanguage.FR -> ch.copy(
                    title = "La Deuxième Fondation : Medellín",
                    subtitle = "Noviciat Saint-Basile (1995) et Paroisse El Ecce Homo (1996)",
                    summary = "Ouverture de la maison de formation dans le quartier Prado et de la paroisse dans le secteur populaire de Blanquizal.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Le Noviciat et la Paroisse El Ecce Homo",
                            content = "Le 30 juillet 1995, Mgr Héctor Rueda Hernández a autorisé l'érection canonique du Noviciat Saint-Basile à Medellín. Peu après, le 16 septembre 1996, la Congrégation a assumé la paroisse El Ecce Homo à Blanquizal."
                        )
                    )
                )
                AppLanguage.ES -> ch
            }
            "cap_16" -> when (lang) {
                AppLanguage.EN -> ch.copy(
                    title = "Appendix: The Painting of Saint Basil in Cali",
                    subtitle = "Inculturated Symbolism by Efraín Ferrer de la Torre",
                    summary = "Explanation of the iconic oil painting in which Saint Basil wears traditional Colombian attire and carries the banner of truth and justice.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Symbolism of the Cali Saint Basil",
                            content = "Painted by Efraín Ferrer at the Parish of the Assumption:\n• Saint Basil wears an Antioquian carriel satchel over his left shoulder, a coastal sombrero vueltiao hat, and sandals.\n• He holds the Bible in his right hand and wears the Basilian cross.\n• In the background: the Hill of the Three Crosses of Cali, the humble homes of Andrés Sanín and La Playa, the parish tower, and the map of Colombia.\n• Depiction of Aldemar Rodríguez playing an Andean quena flute with blood-stained wings as a martyr angel of justice.\n• Motto: «Bonitatem, Disciplinam et Scientiam doce me»."
                        )
                    )
                )
                AppLanguage.FR -> ch.copy(
                    title = "Appendice : Le Tableau de Saint Basile à Cali",
                    subtitle = "Symbolisme inculturé par Efraín Ferrer de la Torre",
                    summary = "Explication de l'huile emblématique où Saint Basile porte des vêtements typiques de Colombie et l'étendard de la vérité et de la justice.",
                    sections = listOf(
                        ApuntesSection(
                            title = "Symbolisme du Saint Basile de Cali",
                            content = "Peint par Efraín Ferrer à la Paroisse de l'Assomption :\n• Saint Basile porte la sacoche traditionnelle (carriel paisa), le chapeau sombrero vueltiao et des sandales.\n• Il tient la Bible dans sa main droite et porte la croix basilienne.\n• En arrière-plan : la colline des Trois Croix de Cali, les maisons humbles d'Andrés Sanín et de La Playa, le clocher paroissial et la carte de la Colombie.\n• Représentation d'Aldemar Rodríguez avec une flûte andine (quena) et des ailes ensanglantées comme ange martyr de la justice.\n• Devise : « Bonitatem, Disciplinam et Scientiam doce me »."
                        )
                    )
                )
                AppLanguage.ES -> ch
            }
            else -> ch
        }
    }

    // =========================================================================
    // 5. BOOK 1922 DATA LOCALIZATION (1922: BEFORE AND AFTER)
    // =========================================================================
    fun localizePoliticasSections(sections: List<PoliticaSection>, lang: AppLanguage): List<PoliticaSection> =
        localizePoliticas2026Sections(sections, lang)

    fun localizeBook1922Chapters(chapters: List<Book1922Chapter>, lang: AppLanguage): List<Book1922Chapter> {
        if (lang == AppLanguage.ES) return chapters
        return chapters.map { localizeBookChapter(it, lang) }
    }

    fun localizeBook1922Superiors(superiors: List<SuperiorGeneralHistoricalRecord>, lang: AppLanguage): List<SuperiorGeneralHistoricalRecord> =
        localizeSuperiorsGeneral(superiors, lang)

    fun localizeBook1922GovernmentEras(eras: List<GovernmentEvolutionEra>, lang: AppLanguage): List<GovernmentEvolutionEra> =
        localizeGovernmentEvolution(eras, lang)

    fun localizeSuperiorsGeneral(superiors: List<SuperiorGeneralHistoricalRecord>, lang: AppLanguage): List<SuperiorGeneralHistoricalRecord> {
        if (lang == AppLanguage.ES) return superiors
        return superiors.map { localizeSuperiorRecord(it, lang) }
    }

    fun localizeGovernmentEvolution(eras: List<GovernmentEvolutionEra>, lang: AppLanguage): List<GovernmentEvolutionEra> {
        if (lang == AppLanguage.ES) return eras
        return eras.map { localizeGovernmentEra(it, lang) }
    }

    fun localizeBookChapter(chapter: Book1922Chapter, lang: AppLanguage): Book1922Chapter {
        if (lang == AppLanguage.ES) return chapter
        return when (chapter.id) {
            "intro" -> when (lang) {
                AppLanguage.EN -> chapter.copy(
                    partNumber = "Introduction",
                    partTitle = "Historical Mandate & Purpose",
                    chapterTitle = "A Look at the Turning Point of Our History",
                    subtitle = "Written by Fr. Kevin J. Kirley, CSB for the 1993 General Chapter",
                    pageRange = "Pages 1 – 3",
                    summary = "How ten diocesan priests in Annonay (1822) grew into an international religious congregation across France and North America, facing the canonical crisis and watershed of 1922.",
                    quotes = listOf("«It is our history and we are not ashamed of it. How we interpret it as an aid to future development is a matter of our full responsibility.» — Fr. Kevin J. Kirley, CSB"),
                    keyTakeaways = listOf(
                        "History is neither hidden nor embellished: it is the foundation for discerning present and future.",
                        "The study spans from the clandestine school of 1800 to post-Vatican II consolidation.",
                        "Published at Saint Basil Parish in Medellín as a fraternal bridge between North America and Colombia."
                    ),
                    sections = listOf(
                        BookSectionContent(
                            title = "1. The Mandate of the Superior General",
                            paragraphs = listOf(
                                "In preparation for the 1993 General Chapter, Superior General Fr. Jack Gallagher, CSB asked Fr. Kevin J. Kirley, CSB to write a clear historical synthesis of the events leading up to and following the year 1922.",
                                "For a full century (1822–1922), the Priests of Saint Basil lived under a unique rule approved in 1863: they professed vows of obedience and chastity, lived in community, and taught in schools, yet did not take a canonical vow of poverty.",
                                "When the Holy See promulgated the 1917 Code of Canon Law, every religious institute was required to align its constitutions with universal canon law. This requirement brought to a head long-standing differences between the French houses and the rapidly growing North American province."
                            )
                        ),
                        BookSectionContent(
                            title = "2. Scope and Archival Sources",
                            paragraphs = listOf(
                                "This work draws directly from Basilian archives: records of General Chapters and council meetings, letters, annals, and traditions handed down from one Basilian generation to the next.",
                                "It is divided into three parts: Part I covers the first century of effort (1800–1922); Part II examines why 1922 was a decisive watershed; Part III traces the road traveled from reunification to the contemporary era."
                            )
                        )
                    )
                )
                AppLanguage.FR -> chapter.copy(
                    partNumber = "Introduction",
                    partTitle = "Mandat Historique et Objectif",
                    chapterTitle = "Regard sur le Point Critique de Notre Histoire",
                    subtitle = "Rédigé par le P. Kevin J. Kirley, CSB pour le Chapitre Général de 1993",
                    pageRange = "Pages 1 – 3",
                    summary = "Comment dix prêtres diocésains d'Annonay (1822) sont devenus une congrégation internationale en France et en Amérique du Nord, jusqu'au tournant décisif de 1922.",
                    quotes = listOf("« C'est notre histoire et nous n'en avons pas honte. La manière de l'interpréter pour notre développement futur relève de notre entière responsabilité. » — P. Kevin J. Kirley, CSB"),
                    keyTakeaways = listOf(
                        "Fondation le 21 novembre 1822 à Annonay (Ardèche) par dix prêtres diocésains.",
                        "1852 : Arrivée à Toronto (Canada) à l'invitation de Mgr Armand de Charbonnel.",
                        "1922 marque le passage canonique aux trois vœux simples de religion."
                    ),
                    sections = listOf(
                        BookSectionContent(
                            title = "1. Le Mandat du Supérieur Général",
                            paragraphs = listOf(
                                "En préparation au Chapitre Général de 1993, le Supérieur Général P. Jack Gallagher, CSB a demandé au P. Kevin J. Kirley, CSB de rédiger une synthèse historique claire des événements entourant l'année 1922.",
                                "Pendant tout un siècle (1822–1922), les Prêtres de Saint-Basile ont vécu sous une règle originale : ils professaient les vœux d'obéissance et de chasteté et vivaient en communauté, mais sans vœu canonique de pauvreté.",
                                "Lorsque le Saint-Siège a promulgué le Code de Droit Canonique de 1917, chaque institut a dû harmoniser ses constitutions, révélant les différences de contexte entre la France et l'Amérique du Nord."
                            )
                        ),
                        BookSectionContent(
                            title = "2. Portée et Sources Archivistiques",
                            paragraphs = listOf(
                                "Cet ouvrage puise dans les archives basiliennes : registres des Chapitres Généraux, correspondances, annales et traditions transmises d'une génération basilienne à l'autre."
                            )
                        )
                    )
                )
                AppLanguage.ES -> chapter
            }
            "parte1" -> when (lang) {
                AppLanguage.EN -> chapter.copy(
                    partNumber = "Part I",
                    partTitle = "1800 – 1922: A Century or More of Effort",
                    chapterTitle = "From the French Revolution to Missionary Expansion",
                    subtitle = "Saint-Symphorien, Annonay, Toronto and the French Anti-Clerical Laws",
                    pageRange = "Pages 4 – 12",
                    summary = "The clandestine school of Saint-Symphorien-de-Mahun (1797), the 1802 move to Annonay, the official foundation on November 21, 1822 at Maison-Seule, the 1852 foundation of St. Michael's College in Toronto, and the crisis caused by the 1903 French expulsion laws.",
                    quotes = listOf(
                        "«Priests are the sole enemies of the government...» — 1799 Ardèche police report during the French Revolution",
                        "«We were men of different ideas and temperaments and saw the need to associate... we had clear and pure intentions.» — Fr. Pierre Tourvieille"
                    ),
                    keyTakeaways = listOf(
                        "1797–1822: Clandestine seminary instruction in the mountains of Ardèche led by Fr. Joseph Lapierre.",
                        "November 21, 1822: Official foundation at Maison-Seule in Annonay under the patronage of Saint Basil.",
                        "1852: Fr. Patrick Moloney and Fr. Jean-Mathieu Soulerin found St. Michael's College in Toronto.",
                        "1903: French anti-clerical laws under Émile Combes close Basilian schools in France, shifting growth to Canada and Texas."
                    ),
                    sections = listOf(
                        BookSectionContent(
                            title = "1. Clandestine Origins in the Reign of Terror (1797–1802)",
                            paragraphs = listOf(
                                "In 1797, Archbishop d'Aviau of Vienne entrusted Fr. Joseph Bouvier Lapierre with organizing a clandestine presbyteral school in Saint-Symphorien-de-Mahun in the mountains of Ardèche.",
                                "In 1802, the school moved to Annonay, followed by Sainte-Barbe minor seminary (1805, founded by Fr. Jacques-Vincent Duret) and Sainte-Claire (1813)."
                            )
                        ),
                        BookSectionContent(
                            title = "2. The College of Annonay & Foundation at Maison-Seule (1822)",
                            paragraphs = listOf(
                                "On the Feast of the Presentation of Mary, November 21, 1822, the ten founders held their first General Chapter at Maison-Seule in Annonay, elected Joseph Lapierre as first Superior General, and chose Saint Basil the Great as patron."
                            )
                        ),
                        BookSectionContent(
                            title = "3. Transatlantic Mission to Toronto (1852) & Papal Approval (1863)",
                            paragraphs = listOf(
                                "At the request of Bishop Armand de Charbonnel of Toronto—a former student of Annonay—the Basilians crossed the Atlantic in 1852 and founded St. Michael's College in Toronto and Assumption College in Windsor (1856).",
                                "In 1863, under Superior General Fr. Julien Actorie, Pope Pius IX issued the Decree of Papal Approval."
                            )
                        ),
                        BookSectionContent(
                            title = "4. Religious Persecution in France (1903)",
                            paragraphs = listOf(
                                "Under the anti-clerical laws of 1903, Basilian schools in France were confiscated and auctioned, forcing many French Basilians into exile or mission in England, Canada, and the United States."
                            )
                        )
                    )
                )
                AppLanguage.FR -> chapter.copy(
                    partNumber = "Partie I",
                    partTitle = "1800 – 1922 : Un Siècle ou Plus d'Effort",
                    chapterTitle = "De la Révolution Française à l'Expansion Missionnaire",
                    subtitle = "Saint-Symphorien, Annonay, Toronto et l'Épreuve des Lois Anticléricales",
                    pageRange = "Pages 4 – 12",
                    summary = "L'école clandestine de Saint-Symphorien-de-Mahun (1797), le transfert à Annonay en 1802, la fondation officielle le 21 novembre 1822 à Maison-Seule, la fondation de St. Michael's College à Toronto en 1852 et la crise des lois françaises de 1903.",
                    quotes = listOf(
                        "« Nous étions des hommes aux idées et tempéraments différents et nous avons vu la nécessité de nous associer... nous avions des intentions claires et pures. » — P. Pierre Tourvieille"
                    ),
                    keyTakeaways = listOf(
                        "1797–1822 : École clandestine dans les montagnes d'Ardèche autour de M. Joseph Lapierre.",
                        "21 novembre 1822 : Fondation officielle à Maison-Seule (Annonay) sous le patronage de Saint Basile.",
                        "1852 : Fondation de St. Michael's College à Toronto à l'appel de Mgr de Charbonnel.",
                        "1903 : Les lois Combes entraînent la fermeture des collèges en France et l'essor au Canada et au Texas."
                    ),
                    sections = listOf(
                        BookSectionContent(
                            title = "1. Origines Clandestines sous la Terreur (1797–1802)",
                            paragraphs = listOf(
                                "En 1797, Mgr d'Aviau de Vienne demanda à Joseph Bouvier Lapierre d'organiser une école presbytérale clandestine à Saint-Symphorien-de-Mahun en Ardèche.",
                                "En 1802, l'école fut transférée à Annonay, suivie par le petit séminaire Sainte-Barbe (1805, fondé par le P. Jacques-Vincent Duret) et l'école Sainte-Claire (1813)."
                            )
                        ),
                        BookSectionContent(
                            title = "2. Le Collège d'Annonay et la Fondation à Maison-Seule (1822)",
                            paragraphs = listOf(
                                "En la fête de la Présentation de Marie, le 21 novembre 1822, les dix fondateurs tinrent leur premier Chapitre Général à Maison-Seule (Annonay), élurent Joseph Lapierre comme premier Supérieur Général et choisirent Saint Basile le Grand pour patron."
                            )
                        ),
                        BookSectionContent(
                            title = "3. Mission Transatlantique à Toronto (1852) et Approbation Pontificale (1863)",
                            paragraphs = listOf(
                                "À l'appel de Mgr Armand de Charbonnel, évêque de Toronto et ancien élève d'Annonay, les Basiliens traversèrent l'Atlantique en 1852 pour fonder St. Michael's College à Toronto et Assumption College à Windsor.",
                                "En 1863, sous le généralat du P. Julien Actorie, le pape Pie IX promulgua le Décret d'Approbation Pontificale."
                            )
                        )
                    )
                )
                AppLanguage.ES -> chapter
            }
            "parte2" -> when (lang) {
                AppLanguage.EN -> chapter.copy(
                    partNumber = "Part II",
                    partTitle = "The Significance of the Year 1922",
                    chapterTitle = "Canonical Separation and Rebirth in North America",
                    subtitle = "The Decree of Separation of June 14, 1922 and the Leadership of Francis Forster",
                    pageRange = "Pages 13 – 23",
                    summary = "In 1922, the Holy See issued the decree dividing the Congregation into two autonomous branches—Toronto and Viviers—allowing the North American branch under Fr. Francis Forster to adopt the 1917 Code of Canon Law and the vow of poverty immediately.",
                    quotes = listOf("«What seemed in 1922 to be a painful division became, in God's providence, the path that preserved both branches until their joyful reunion in 1955.»"),
                    keyTakeaways = listOf(
                        "June 14, 1922: Roman Decree establishes the Basilians of Toronto and the Basilians of Viviers.",
                        "Fr. Francis Forster elected Superior General in Toronto, instituting the canonical vow of poverty and common life.",
                        "1929: Foundation of the Pontifical Institute of Mediaeval Studies (PIMS) with Étienne Gilson."
                    ),
                    sections = listOf(
                        BookSectionContent(
                            title = "1. The 1922 Decree and the Era of Fr. Francis Forster",
                            paragraphs = listOf(
                                "Because elderly French confreres living in secularized post-war France depended on small personal stipends to survive and care for relatives, while North American confreres desired full religious vows under the 1917 Code, Rome decreed an amicable canonical separation on June 14, 1922.",
                                "Under the visionary leadership of Fr. Francis Forster and Fr. Henry Carr in Toronto, the North American branch embraced the vow of poverty, experienced a dramatic boom in vocations, founded the Pontifical Institute of Mediaeval Studies (1929), and began Hispanic missions in Texas and Mexico.",
                                "Meanwhile, in France, Frs. Victorin Marijon, Octave Descellière, and Charles Roume heroically sustained the schools in Annonay and Aubenas through the Great Depression and World War II, eventually adopting the vow of poverty as well."
                            )
                        )
                    )
                )
                AppLanguage.FR -> chapter.copy(
                    partNumber = "Partie II",
                    partTitle = "L'Importance de l'Année 1922",
                    chapterTitle = "La Séparation Canonique et le Renouveau en Amérique",
                    subtitle = "Le Décret de Séparation du 14 juin 1922 et le Leadership de Francis Forster",
                    pageRange = "Pages 13 – 23",
                    summary = "Le 14 juin 1922, le Saint-Siège a établi deux branches autonomes — Toronto et Viviers — permettant à la branche nord-américaine sous le P. Francis Forster d'adopter immédiatement le Code de 1917 et le vœu canonique de pauvreté.",
                    quotes = listOf("« Ce qui semblait en 1922 une séparation douloureuse devint, dans la providence de Dieu, le chemin qui préserva les deux branches jusqu'à leur réunion joyeuse en 1955. »"),
                    keyTakeaways = listOf(
                        "14 juin 1922 : Le Décret de Rome établit les Basiliens de Toronto et les Basiliens de Viviers.",
                        "Le P. Francis Forster est élu Supérieur Général à Toronto et instaure le vœu de pauvreté.",
                        "1929 : Fondation de l'Institut Pontifical d'Études Médiévales (PIMS) avec Étienne Gilson."
                    ),
                    sections = listOf(
                        BookSectionContent(
                            title = "1. Le Décret de 1922 et l'Essor des Deux Branches",
                            paragraphs = listOf(
                                "Compte tenu de la situation légale difficile en France après les lois de séparation et du désir des confrères d'Amérique d'adopter intégralement le Code de 1917 avec le vœu de pauvreté, Rome a autorisé le 14 juin 1922 une séparation canonique fraternelle en deux branches.",
                                "Sous l'impulsion des Pères Francis Forster et Henry Carr à Toronto, la branche américaine a connu un essor remarquable de vocations, fondé l'Institut Pontifical d'Études Médiévales (1929) et ouvert l'apostolat auprès des communautés hispaniques au Texas.",
                                "En France, les Pères Victorin Marijon, Octave Descellière et Charles Roume ont maintenu avec courage les collèges d'Annonay et d'Aubenas à travers la Seconde Guerre mondiale, adoptant eux aussi le vœu de pauvreté."
                            )
                        )
                    )
                )
                AppLanguage.ES -> chapter
            }
            "parte3" -> when (lang) {
                AppLanguage.EN -> chapter.copy(
                    partNumber = "Part III",
                    partTitle = "1955 – Present: Reunion and Global Mission",
                    chapterTitle = "Reunification, Vatican II and Contemporary Mission",
                    subtitle = "The 1955 Decree of Union, Vatican II, Mexico (1961) and Colombia (1987)",
                    pageRange = "Pages 24 – 34",
                    summary = "On September 29, 1955, Superior General George Flahiff and Fr. Charles Roume celebrated the papal Decree of Reunification in Annonay, launching a new era of renewal and Latin American missions.",
                    quotes = listOf("«In Annonay, on September 29, 1955, French and North American Basilians embraced as one single family once again under the patronage of Saint Basil.»"),
                    keyTakeaways = listOf(
                        "September 29, 1955: Historic reunification of the French and American branches in Annonay.",
                        "1961 & 1987: Opening of the Basilian missions in Mexico and Colombia.",
                        "1983: Papal approval of 'The Basilian Way of Life' constitutional document."
                    ),
                    sections = listOf(
                        BookSectionContent(
                            title = "1. The 1955 Reunification and the Latin American Horizon",
                            paragraphs = listOf(
                                "Once both branches shared the same three simple vows of poverty, chastity, and obedience, Superior General George B. Flahiff (later Cardinal Archbishop of Winnipeg) and Fr. Charles Roume petitioned Pius XII for reunification. The Decree of Union was celebrated in Annonay on September 29, 1955.",
                                "Following the Second Vatican Council, the Congregation drafted 'The Basilian Way of Life' (approved in 1983) and expanded its missionary heart to Mexico (1961) and Colombia (Cali 1987, Medellín, and Bogotá).",
                                "Today, more than two centuries after 1822, the Congregation of Saint Basil lives as an intercultural brotherhood across Canada, the United States, Mexico, Colombia, and France."
                            )
                        )
                    )
                )
                AppLanguage.FR -> chapter.copy(
                    partNumber = "Partie III",
                    partTitle = "1955 – Présent : Réunion et Mission Mondiale",
                    chapterTitle = "Réunification, Vatican II et Mission Contemporaine",
                    subtitle = "Le Décret d'Union de 1955, Vatican II, le Mexique (1961) et la Colombie (1987)",
                    pageRange = "Pages 24 – 34",
                    summary = "Le 29 septembre 1955, le P. George Flahiff et le P. Charles Roume ont célébré à Annonay le Décret pontifical de Réunification, ouvrant l'ère du renouveau conciliaire et des missions en Amérique Latine.",
                    quotes = listOf("« À Annonay, le 29 septembre 1955, les Basiliens de France et d'Amérique se sont embrassés comme une seule famille sous le patronage de Saint Basile. »"),
                    keyTakeaways = listOf(
                        "29 septembre 1955 : Réunification historique des branches française et américaine à Annonay.",
                        "1961 et 1987 : Ouverture des missions basiliennes au Mexique et en Colombie.",
                        "1983 : Approbation pontificale du document constitutionnel « Le Mode de Vie Basilien »."
                    ),
                    sections = listOf(
                        BookSectionContent(
                            title = "1. La Réunification de 1955 et l'Élan vers l'Amérique Latine",
                            paragraphs = listOf(
                                "Les deux branches partageant désormais les trois mêmes vœux simples de pauvreté, chasteté et obéissance, le Supérieur Général George B. Flahiff (futur Cardinal Archevêque de Winnipeg) et le P. Charles Roume ont obtenu de Pie XII le Décret d'Union, célébré solennellement à Annonay le 29 septembre 1955.",
                                "Après le Concile Vatican II, la Congrégation a rédigé « Le Mode de Vie Basilien » (approuvé en 1983) et déployé son élan missionnaire au Mexique (1961) et en Colombie (Cali 1987, Medellín et Bogotá).",
                                "Aujourd'hui, plus de deux siècles après 1822, la Congrégation de Saint-Basile vit comme une fraternité interculturelle présente au Canada, aux États-Unis, au Mexique, en Colombie et en France."
                            )
                        )
                    )
                )
                AppLanguage.ES -> chapter
            }
            else -> chapter
        }
    }

    fun localizeSuperiorRecord(sup: SuperiorGeneralHistoricalRecord, lang: AppLanguage): SuperiorGeneralHistoricalRecord {
        if (lang == AppLanguage.ES) return sup
        val locName = when (lang) {
            AppLanguage.EN -> sup.name.replace("P. ", "Fr. ").replace("Cardenal", "Cardinal").replace("(América)", "(Americas)").replace("(Francia)", "(France)")
            AppLanguage.FR -> sup.name.replace("Cardenal", "Cardinal").replace("(América)", "(Amériques)").replace("(Francia)", "(France)")
            AppLanguage.ES -> sup.name
        }
        val locYears = sup.years.replace("Presente", if (lang == AppLanguage.EN) "Present" else "Présent")
        val locJurisdiction = when (lang) {
            AppLanguage.EN -> sup.jurisdiction
                .replace("Congregación Unificada", "Unified Congregation")
                .replace("Superior General Suplente", "Acting Superior General")
                .replace("Padres de San Basilio de Toronto (América)", "Basilian Fathers of Toronto (Americas)")
                .replace("Padres de San Basilio de Viviers (Francia)", "Basilian Fathers of Viviers (France)")
                .replace("Rama de Viviers (Francia)", "Viviers Branch (France)")
                .replace("Congregación Reunificada Universal", "Universal Reunified Congregation")
                .replace("Congregación Reunificada (Superior General Actual)", "Reunified Congregation (Former Superior General)")
                .replace("Congregación Reunificada", "Reunified Congregation")
            AppLanguage.FR -> sup.jurisdiction
                .replace("Congregación Unificada", "Congrégation Unifiée")
                .replace("Superior General Suplente", "Supérieur Général Suppléant")
                .replace("Padres de San Basilio de Toronto (América)", "Pères de Saint-Basile de Toronto (Amériques)")
                .replace("Padres de San Basilio de Viviers (Francia)", "Pères de Saint-Basile de Viviers (France)")
                .replace("Rama de Viviers (Francia)", "Branche de Viviers (France)")
                .replace("Congregación Reunificada Universal", "Congrégation Réunifiée Universelle")
                .replace("Congregación Reunificada (Superior General Actual)", "Congrégation Réunifiée (Ancien Supérieur Général)")
                .replace("Congregación Reunificada", "Congrégation Réunifiée")
            AppLanguage.ES -> sup.jurisdiction
        }
        val locMilestones = when (lang) {
            AppLanguage.EN -> when {
                sup.name.contains("Lapierre") -> "Leader of the ten founders of Annonay (1822) and director of the clandestine school of Saint-Symphorien during the French Revolution."
                sup.name.contains("Tourvieille") -> "Second Superior General; dispatched the first Basilian missionaries to Toronto, Canada in 1852."
                sup.name.contains("Actorie") -> "Guided the expansion of the French colleges and secured papal approval of the 1863 Constitutions."
                sup.name.contains("Soulerin") -> "Pioneer founder of St. Michael's College in Toronto (1852) and fourth Superior General in France."
                sup.name.contains("Mallet") -> "Fostered vocations and established the North American Provincialate in 1883."
                sup.name.contains("Durand") -> "Defended the Congregation during the French anti-clerical laws and school closures of 1901–1904."
                sup.name.contains("Plomer") -> "Governed during the Apostolic Visitation and the historic 1910 General Chapter in Geneva."
                sup.name.contains("Player") -> "Governed during the difficult years of World War I until the canonical separation of 1922."
                sup.name.contains("Forster") -> "Pillar of the modern Congregation in North America. Instituted the canonical vow of poverty, common life, and university centers."
                sup.name.contains("Giraud") -> "First Superior General of the French branch following the 1922 Decree of Separation."
                sup.name.contains("Marijon") -> "Second term leading the French educational and vocational works."
                sup.name.contains("Carr") -> "Distinguished philosopher; promoted the Pontifical Institute of Mediaeval Studies (PIMS) and Hispanic ministry in Texas (1935)."
                sup.name.contains("Descellière") -> "Kept the schools in Annonay and Aubenas alive throughout World War II."
                sup.name.contains("McCorkell") -> "Grew the Congregation to over 500 members; founded the University of St. Thomas (Houston) and prepared reunification."
                sup.name.contains("Roume") -> "Co-signer of the historic 1955 Decree of Reunification in Annonay and historian of the Congregation."
                sup.name.contains("Flahiff") -> "Signed the Decree of Union on September 29, 1955 in Annonay. Appointed Archbishop of Winnipeg and Cardinal of the Church."
                sup.name.contains("Wey") -> "Guided the Congregation throughout the Second Vatican Council and opened the mission in Mexico (1961)."
                sup.name.contains("Hanrahan") -> "Promulgated 'The Basilian Way of Life' in 1977, adapting the charism to conciliar renewal."
                sup.name.contains("Paré") -> "Confirmed the Constitutions under the 1983 Code of Canon Law; inaugurated the mission in Colombia (Cali, 1987)."
                sup.name.contains("Gallagher") -> "Commissioned Fr. Kevin Kirley to write '1922: Before and After'; consolidated the presence in Colombia (Bogotá and Medellín)."
                sup.name.contains("Storey") -> "Led the Basilian Bicentennial celebrations (1822–2022) and the relocation of the General Curia to 146 Davenport Rd in Toronto."
                else -> translateConstitutionalSpanish(sup.keyMilestones, AppLanguage.EN)
            }
            AppLanguage.FR -> when {
                sup.name.contains("Lapierre") -> "Chef de file des dix fondateurs d'Annonay (1822) et directeur de l'école clandestine de Saint-Symphorien sous la Révolution."
                sup.name.contains("Tourvieille") -> "Deuxième Supérieur Général ; a envoyé les premiers missionnaires basiliens à Toronto (Canada) en 1852."
                sup.name.contains("Actorie") -> "A guidé l'expansion des collèges français et obtenu l'approbation des Constitutions de 1863."
                sup.name.contains("Soulerin") -> "Pionnier fondateur de St. Michael's College à Toronto (1852) et quatrième Supérieur Général."
                sup.name.contains("Mallet") -> "A soutenu la croissance des vocations et créé la Province d'Amérique du Nord en 1883."
                sup.name.contains("Durand") -> "A défendu la Congrégation durant les lois d'expulsion et de fermeture des écoles en France (1901–1904)."
                sup.name.contains("Plomer") -> "A gouverné durant la Visite Apostolique et le Chapitre Général de Genève en 1910."
                sup.name.contains("Player") -> "A gouverné durant les dures années de la Première Guerre mondiale jusqu'à la séparation de 1922."
                sup.name.contains("Forster") -> "Pilier de la Congrégation moderne en Amérique. A instauré le vœu canonique de pauvreté, la vie commune et l'essor universitaire."
                sup.name.contains("Giraud") -> "Premier supérieur de la branche française après le Décret de Séparation de 1922."
                sup.name.contains("Marijon") -> "Deuxième mandat à la tête des œuvres éducatives et vocationnelles en France."
                sup.name.contains("Carr") -> "Philosophe éminent ; a promu l'Institut Pontifical d'Études Médiévales (PIMS) et l'apostolat hispanique au Texas (1935)."
                sup.name.contains("Descellière") -> "A maintenu vivantes les écoles d'Annonay et d'Aubenas tout au long de la Seconde Guerre mondiale."
                sup.name.contains("McCorkell") -> "A porté la Congrégation à plus de 500 membres ; a fondé l'Université St. Thomas (Houston) et préparé la réunification."
                sup.name.contains("Roume") -> "Cosignataire du Décret historique de Réunification de 1955 à Annonay et historien de la Congrégation."
                sup.name.contains("Flahiff") -> "A signé le Décret d'Union le 29 septembre 1955 à Annonay. Nommé Archevêque de Winnipeg puis Cardinal de l'Église."
                sup.name.contains("Wey") -> "A guidé la Congrégation durant tout le Concile Vatican II et ouvert la mission au Mexique (1961)."
                sup.name.contains("Hanrahan") -> "A promulgué « Le Mode de Vie Basilien » en 1977, adaptant le charisme au renouveau conciliaire."
                sup.name.contains("Paré") -> "A confirmé les Constitutions selon le Code de 1983 ; sous son mandat fut fondée la mission en Colombie (Cali, 1987)."
                sup.name.contains("Gallagher") -> "A demandé au P. Kevin Kirley la rédaction de « 1922 : Avant et Après » ; a consolidé la présence à Bogotá et Medellín."
                sup.name.contains("Storey") -> "A présidé les célébrations du Bicentenaire Basilien (1822–2022) et le transfert de la Curie Générale au 146 Davenport Rd à Toronto."
                else -> translateConstitutionalSpanish(sup.keyMilestones, AppLanguage.FR)
            }
            AppLanguage.ES -> sup.keyMilestones
        }
        return sup.copy(
            name = locName,
            years = locYears,
            jurisdiction = locJurisdiction,
            keyMilestones = locMilestones
        )
    }

    fun localizeGovernmentEra(era: GovernmentEvolutionEra, lang: AppLanguage): GovernmentEvolutionEra {
        if (lang == AppLanguage.ES) return era
        return when (era.era) {
            "1822 – 1922" -> when (lang) {
                AppLanguage.EN -> era.copy(
                    title = "First Century: Superior General & Unified Council",
                    structureDescription = "One central Superior General in France with 4 General Councilors, Secretary General, and Treasurer General.",
                    details = listOf(
                        "Provincial Administration in Canada and the USA established in 1883 with Fr. Charles Vincent as first Provincial.",
                        "Province of France formally erected in 1910.",
                        "Growing need to adapt European governance to the rapid university and school expansion in North America."
                    )
                )
                AppLanguage.FR -> era.copy(
                    title = "Premier Siècle : Supérieur Général et Conseil Unifié",
                    structureDescription = "Un Supérieur Général central en France assisté de 4 Conseillers Généraux, du Secrétaire et de l'Économe Général.",
                    details = listOf(
                        "Administration Provinciale au Canada et aux États-Unis créée en 1883 avec le P. Charles Vincent comme premier provincial.",
                        "Province de France érigée formellement en 1910.",
                        "Nécessité d'adapter le gouvernement européen au rythme rapide du développement universitaire nord-américain."
                    )
                )
                AppLanguage.ES -> era
            }
            "1922 – 1955" -> when (lang) {
                AppLanguage.EN -> era.copy(
                    title = "Fraternal Canonical Separation",
                    structureDescription = "Two autonomous branches approved by Rome: Basilian Fathers of Viviers (France) and Basilian Fathers of Toronto (Americas).",
                    details = listOf(
                        "Each branch governed by its own independent Superior General and General Council.",
                        "American branch adopts the 1917 Code of Canon Law, simple vow of poverty, and common table ('mensa communis').",
                        "French branch sustains the schools of Annonay and Aubenas through the European wars."
                    )
                )
                AppLanguage.FR -> era.copy(
                    title = "Séparation Canonique Fraternelle",
                    structureDescription = "Deux branches autonomes approuvées par Rome : Pères Basiliens de Viviers (France) et Pères Basiliens de Toronto (Amériques).",
                    details = listOf(
                        "Chaque branche dispose de son propre Supérieur Général et Conseil Général indépendant.",
                        "La branche américaine adopte le Code de 1917, le vœu simple de pauvreté et la vie commune ('mensa communis').",
                        "La branche française maintient les collèges d'Annonay et d'Aubenas au milieu des guerres européennes."
                    )
                )
                AppLanguage.ES -> era
            }
            "1955 – 1967" -> when (lang) {
                AppLanguage.EN -> era.copy(
                    title = "The Great Reunification",
                    structureDescription = "Decree of Union sealed in Annonay on September 29, 1955 under George Flahiff and Charles Roume.",
                    details = listOf(
                        "A single Superior General for the entire world assisted by 4 General Councilors.",
                        "Flourishing of new vocations and missionary expansion into Latin America (Mexico 1961).",
                        "Active preparation and participation in the Second Vatican Council."
                    )
                )
                AppLanguage.FR -> era.copy(
                    title = "La Grande Réunification",
                    structureDescription = "Décret d'Union scellé à Annonay le 29 septembre 1955 sous George Flahiff et Charles Roume.",
                    details = listOf(
                        "Un seul Supérieur Général pour le monde entier assisté de 4 Conseillers Généraux.",
                        "Essor de nouvelles vocations et expansion vers l'Amérique Latine (Mexique 1961).",
                        "Préparation et participation au Concile Vatican II."
                    )
                )
                AppLanguage.ES -> era
            }
            else -> when (lang) {
                AppLanguage.EN -> era.copy(
                    era = "1967 – Present",
                    title = "Collegial & Intercultural Governance",
                    structureDescription = "Superior General, Vicar General / First Councilor, and General Councilors representing the international Congregation.",
                    details = listOf(
                        "Intercultural representation across Canada, the United States, Mexico, Colombia, and France.",
                        "General Secretariat, General Treasury, and St. Joseph's Mission Center.",
                        "Constitutions approved by the Holy See in 'The Basilian Way of Life'."
                    )
                )
                AppLanguage.FR -> era.copy(
                    era = "1967 – Présent",
                    title = "Gouvernement Collégial et Interculturel",
                    structureDescription = "Supérieur Général, Vicaire Général / Premier Conseiller et Conseillers Généraux au service de la Congrégation internationale.",
                    details = listOf(
                        "Représentation interculturelle au Canada, aux États-Unis, au Mexique, en Colombie et en France.",
                        "Secrétariat Général, Économat Général et Centre des Missions Saint-Joseph.",
                        "Constitutions approuvées par le Saint-Siège dans « Le Mode de Vie Basilien »."
                    )
                )
                AppLanguage.ES -> era
            }
        }
    }
}
