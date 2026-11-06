package com.example.data.repository

import com.example.data.model.*

/**
 * Universal Content Localizer for all domain models, liturgical texts, saints,
 * prayer sessions, AR iconography, learning paths, and directory milestones
 * across Spanish (ES), English (EN), and French (FR).
 */
object UniversalContentLocalizer {

    // =========================================================================
    // 1. DATE & MONTH FORMATTING
    // =========================================================================
    private val monthsEs = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
    private val monthsEn = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private val monthsFr = listOf("Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre")

    fun formatMonthDay(month: Int, day: Int, lang: AppLanguage): String {
        val idx = (month - 1).coerceIn(0, 11)
        return when (lang) {
            AppLanguage.EN -> "${monthsEn[idx]} $day"
            AppLanguage.FR -> "$day ${monthsFr[idx]}"
            AppLanguage.ES -> "$day de ${monthsEs[idx]}"
        }
    }

    fun localizeDateString(rawDate: String, lang: AppLanguage): String {
        if (lang == AppLanguage.ES || rawDate.isBlank()) return rawDate
        var res = rawDate
        val daysEs = listOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
        val daysEn = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val daysFr = listOf("Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi")

        for (i in daysEs.indices) {
            val target = if (lang == AppLanguage.EN) daysEn[i] else daysFr[i]
            res = res.replace(daysEs[i], target, ignoreCase = true)
        }
        for (i in monthsEs.indices) {
            val mEs = monthsEs[i]
            val mEn = monthsEn[i]
            val mFr = monthsFr[i]
            if (lang == AppLanguage.EN) {
                // Convert "20 de octubre de 2026" -> "October 20, 2026"
                val regexWithYear = Regex("(\\d{1,2})\\s+de\\s+$mEs\\s+de\\s+(\\d{4})", RegexOption.IGNORE_CASE)
                res = regexWithYear.replace(res) { mr -> "$mEn ${mr.groupValues[1]}, ${mr.groupValues[2]}" }
                val regexNoYear = Regex("(\\d{1,2})\\s+de\\s+$mEs", RegexOption.IGNORE_CASE)
                res = regexNoYear.replace(res) { mr -> "$mEn ${mr.groupValues[1]}" }
                res = res.replace(mEs, mEn, ignoreCase = true)
            } else {
                val regexWithYear = Regex("(\\d{1,2})\\s+de\\s+$mEs\\s+de\\s+(\\d{4})", RegexOption.IGNORE_CASE)
                res = regexWithYear.replace(res) { mr -> "${mr.groupValues[1]} ${mFr.lowercase()} ${mr.groupValues[2]}" }
                val regexNoYear = Regex("(\\d{1,2})\\s+de\\s+$mEs", RegexOption.IGNORE_CASE)
                res = regexNoYear.replace(res) { mr -> "${mr.groupValues[1]} $mFr" }
                res = res.replace(mEs, mFr, ignoreCase = true)
            }
        }
        return res
    }

    // =========================================================================
    // 2. DAILY LITURGICAL WISDOM LOCALIZATION
    // =========================================================================
    private data class WisdomTranslation(
        val passageEn: String, val passageFr: String,
        val phraseEn: String, val phraseFr: String,
        val fullTextEn: String, val fullTextFr: String,
        val titleEn: String, val titleFr: String,
        val spiritualityEn: String, val spiritualityFr: String,
        val quoteEn: String, val quoteFr: String,
        val sourceEn: String, val sourceFr: String
    )

    private val wisdomByTheme: Map<String, WisdomTranslation> = mapOf(
        "VID_VERDADERA" to WisdomTranslation(
            passageEn = "John 15:1-8", passageFr = "Jean 15, 1-8",
            phraseEn = "«I am the vine, you are the branches. Whoever remains in me and I in him will bear much fruit, because without me you can do nothing.»",
            phraseFr = "« Je suis la vigne, et vous êtes les sarments. Celui qui demeure en moi et en qui je demeure porte beaucoup de fruit, car sans moi vous ne pouvez rien faire. »",
            fullTextEn = "Jesus said to his disciples: «I am the true vine, and my Father is the vine grower. Remain in me, as I remain in you. Just as a branch cannot bear fruit on its own unless it remains on the vine, so neither can you unless you remain in me.»",
            fullTextFr = "Jésus disait à ses disciples : « Moi, je suis la vraie vigne, et mon Père est le vigneron. Demeurez en moi, comme moi en vous. De même que le sarment ne peut pas porter de fruit par lui-même s'il ne demeure pas sur la vigne, de même vous non plus, si vous ne demeurez pas en moi. »",
            titleEn = "Vital Communion in Christ and Fraternal Life",
            titleFr = "La Communion Vitale au Christ et la Vie Fraternelle",
            spiritualityEn = "For Saint Basil the Great, Christian life is never solitary: we are branches united to the one Vine, Christ, and therefore deeply united to one another. In the Basilian tradition, community life and shared prayer nourish our apostolic fruitfulness.",
            spiritualityFr = "Pour Saint Basile le Grand, la vie chrétienne n'est jamais solitaire : nous sommes des sarments unis à l'unique Vigne qu'est le Christ, et donc profondément unis les uns aux autres. Dans la tradition basilienne, la vie commune et la prière partagée nourrissent notre fécondité apostolique.",
            quoteEn = "«Nothing is more proper to our nature than to enter into communion with one another, to need one another, and to love our kind.»",
            quoteFr = "« Rien n'est plus propre à notre nature que d'entrer en communion les uns avec les autres, d'avoir besoin les uns des autres et d'aimer nos semblables. »",
            sourceEn = "Saint Basil the Great • Longer Rules, Question 3",
            sourceFr = "Saint Basile le Grand • Grandes Règles, Question 3"
        ),
        "BUEN_PASTOR" to WisdomTranslation(
            passageEn = "John 10:11-16", passageFr = "Jean 10, 11-16",
            phraseEn = "«I am the good shepherd. A good shepherd lays down his life for the sheep. I know mine and mine know me.»",
            phraseFr = "« Moi, je suis le bon pasteur. Le bon pasteur donne sa vie pour ses brebis. Je connais mes brebis, et mes brebis me connaissent. »",
            fullTextEn = "Jesus said: «I am the good shepherd. A good shepherd lays down his life for the sheep. A hired man, who is not a shepherd and whose sheep are not his own, sees a wolf coming and leaves the sheep and runs away... I am the good shepherd, and I know mine and mine know me.»",
            fullTextFr = "Jésus déclarait : « Moi, je suis le bon pasteur, le vrai berger, qui donne sa vie pour ses brebis. Le berger mercenaire n'est pas le pasteur, les brebis ne sont pas à lui : s'il voit venir le loup, il abandonne les brebis et s'enfuit... Moi, je suis le bon pasteur ; je connais mes brebis et mes brebis me connaissent. »",
            titleEn = "Pastoral Charity and Accompaniment of Youth",
            titleFr = "La Charité Pastorale et l'Accompagnement des Jeunes",
            spiritualityEn = "In Basilian schools and parishes, the educator and pastor imitate the Good Shepherd: knowing each student and parishioner by name, binding up wounds with patience, and guiding souls with goodness, discipline, and knowledge.",
            spiritualityFr = "Dans les écoles et paroisses basiliennes, l'éducateur et le pasteur imitent le Bon Pasteur : connaissant chaque élève et paroissien par son nom, soignant les blessures avec patience et guidant les âmes avec bonté, discipline et science.",
            quoteEn = "«The shepherd must have a father's tenderness for the weak and a physician's diligence to heal every wounded soul.»",
            quoteFr = "« Le pasteur doit avoir la tendresse d'un père pour les faibles et la diligence d'un médecin pour guérir chaque âme blessée. »",
            sourceEn = "Saint Basil the Great • Moral Rules, 70",
            sourceFr = "Saint Basile le Grand • Règles Morales, 70"
        ),
        "SAL_Y_LUZ" to WisdomTranslation(
            passageEn = "Matthew 5:13-16", passageFr = "Matthieu 5, 13-16",
            phraseEn = "«You are the salt of the earth... You are the light of the world. Just so, your light must shine before others, that they may see your good deeds and glorify your heavenly Father.»",
            phraseFr = "« Vous êtes le sel de la terre... Vous êtes la lumière du monde. Que votre lumière brille devant les hommes : alors ils verront ce que vous faites de bien et rendront gloire à votre Père qui est aux cieux. »",
            fullTextEn = "Jesus said to his disciples: «You are the salt of the earth. But if salt loses its taste, with what can it be seasoned? You are the light of the world. A city set on a mountain cannot be hidden. Just so, your light must shine before others, that they may see your good deeds and glorify your heavenly Father.»",
            fullTextFr = "Jésus disait à ses disciples : « Vous êtes le sel de la terre. Mais si le sel devient fade, comment lui rendre de la saveur ? Vous êtes la lumière du monde. Une ville située sur une montagne ne peut être cachée. Que votre lumière brille devant les hommes : alors ils verront ce que vous faites de bien et rendront gloire à votre Père qui est aux cieux. »",
            titleEn = "Teaching by Example: Salt of Wisdom and Light of Goodness",
            titleFr = "Enseigner par l'Exemple : Sel de Sagesse et Lumière de Bonté",
            spiritualityEn = "Saint Basil teaches that Christian educators and believers must unite doctrine with life. Words instruct the ear, but a life radiant with goodness and discipline illuminates the whole community.",
            spiritualityFr = "Saint Basile enseigne que les éducateurs et croyants chrétiens doivent unir la doctrine à la vie. Les paroles instruisent l'oreille, mais une vie rayonnante de bonté et de discipline illumine toute la communauté.",
            quoteEn = "«Let your life be a shining lamp so that those who walk in darkness may find through your charity the path to truth.»",
            quoteFr = "« Que votre vie soit une lampe resplendissante afin que ceux qui marchent dans l'obscurité trouvent par votre charité le chemin de la vérité. »",
            sourceEn = "Saint Basil the Great • Homily on Psalm 33",
            sourceFr = "Saint Basile le Grand • Homélie sur le Psaume 33"
        ),
        "BIENAVENTURANZAS" to WisdomTranslation(
            passageEn = "Matthew 5:1-12", passageFr = "Matthieu 5, 1-12",
            phraseEn = "«Blessed are the poor in spirit, for theirs is the kingdom of heaven... Blessed are the peacemakers, for they will be called children of God.»",
            phraseFr = "« Heureux les pauvres de cœur, car le royaume des Cieux est à eux... Heureux les artisans de paix, car ils seront appelés fils de Dieu. »",
            fullTextEn = "When Jesus saw the crowds, he went up the mountain, and after he had sat down, his disciples came to him. He began to teach them, saying: «Blessed are the poor in spirit, for theirs is the kingdom of heaven. Blessed are the meek... Blessed are the merciful... Blessed are the clean of heart, for they will see God. Blessed are the peacemakers, for they will be called children of God.»",
            fullTextFr = "Voyant les foules, Jésus gravit la montagne. Il s'assit, et ses disciples s'approchèrent de lui. Alors, ouvrant la bouche, il les enseignait : « Heureux les pauvres de cœur, car le royaume des Cieux est à eux. Heureux les doux... Heureux les miséricordieux... Heureux les cœurs purs, car ils verront Dieu. Heureux les artisans de paix, car ils seront appelés fils de Dieu. »",
            titleEn = "The Charter of the Kingdom and Basilian Simplicity",
            titleFr = "La Charte du Royaume et la Simplicité Basilienne",
            spiritualityEn = "The Beatitudes are the heart of the Basilian Way of Life. Poverty of spirit, meekness, mercy, and peacemaking define the presence of the Basilian Fathers from Annonay to Toronto, Houston, Mexico, and Colombia.",
            spiritualityFr = "Les Béatitudes sont le cœur du Mode de Vie Basilien. La pauvreté d'esprit, la douceur, la miséricorde et l'engagement pour la paix définissent la présence des Pères Basiliens d'Annonay à Toronto, Houston, au Mexique et en Colombie.",
            quoteEn = "«Peace is the fruit of justice and humility; blessed is the soul that sows concord and seeks only the glory of God.»",
            quoteFr = "« La paix est le fruit de la justice et de l'humilité ; heureuse l'âme qui sème la concorde et ne cherche que la gloire de Dieu. »",
            sourceEn = "Saint Basil the Great • Homily on Humility",
            sourceFr = "Saint Basile le Grand • Homélie sur l'Humilité"
        ),
        "BUEN_SAMARITANO" to WisdomTranslation(
            passageEn = "Luke 10:25-37", passageFr = "Luc 10, 25-37",
            phraseEn = "«A Samaritan traveler who came upon him was moved with compassion at the sight. He approached the victim, poured oil and wine over his wounds and bandaged them... Go and do likewise.»",
            phraseFr = "« Un Samaritain, qui était en route, arriva près de lui ; il le vit et fut saisi de compassion. Il s'approcha, et pansa ses plaies en y versant de l'huile et du vin... Va, et toi aussi, fais de même. »",
            fullTextEn = "Jesus replied: «A man fell victim to robbers as he went down from Jerusalem to Jericho... But a Samaritan traveler who came upon him was moved with compassion at the sight. He approached the victim, poured oil and wine over his wounds and bandaged them. Then he lifted him up on his own animal, took him to an inn, and cared for him... Go and do likewise.»",
            fullTextFr = "Jésus reprit : « Un homme descendait de Jérusalem à Jéricho, et il tomba sur des bandits... Mais un Samaritain, qui était en route, arriva près de lui ; il le vit et fut saisi de compassion. Il s'approcha, et pansa ses plaies en y versant de l'huile et du vin ; puis il le chargea sur sa propre monture, le conduisit dans une auberge et prit soin de lui... Va, et toi aussi, fais de même. »",
            titleEn = "The Basiliad: Active Mercy Toward the Wounded",
            titleFr = "La Basiliade : La Miséricorde Active envers les Blessés",
            spiritualityEn = "In Caesarea, Saint Basil built the 'Basiliad'—a city of mercy with hospitals, hospices for travelers, and schools for orphans. True devotion at the altar always overflows into concrete compassion for the wounded along the road.",
            spiritualityFr = "À Césarée, Saint Basile a bâti la « Basiliade » — une cité de miséricorde comprenant hôpitaux, hospices pour voyageurs et écoles pour orphelins. La vraie dévotion à l'autel déborde toujours en compassion concrète pour les blessés du chemin.",
            quoteEn = "«Love for our neighbor is the royal road that leads directly to love for God; whoever binds a brother's wound touches the flesh of Christ.»",
            quoteFr = "« L'amour du prochain est la voie royale qui mène directement à l'amour de Dieu ; celui qui panse la plaie d'un frère touche la chair du Christ. »",
            sourceEn = "Saint Basil the Great • Longer Rules, Question 7",
            sourceFr = "Saint Basile le Grand • Grandes Règles, Question 7"
        ),
        "LUZ_DEL_MUNDO" to WisdomTranslation(
            passageEn = "John 8:12", passageFr = "Jean 8, 12",
            phraseEn = "«I am the light of the world. Whoever follows me will not walk in darkness, but will have the light of life.»",
            phraseFr = "« Moi, je suis la lumière du monde. Celui qui me suit ne marchera pas dans les ténèbres, il aura la lumière de la vie. »",
            fullTextEn = "Jesus spoke to them again, saying, «I am the light of the world. Whoever follows me will not walk in darkness, but will have the light of life.»",
            fullTextFr = "Jésus leur parla de nouveau : « Moi, je suis la lumière du monde. Celui qui me suit ne marchera pas dans les ténèbres, il aura la lumière de la vie. »",
            titleEn = "Illumination by the Spirit and Sacred Knowledge",
            titleFr = "L'Illumination par l'Esprit et la Science Sacrée",
            spiritualityEn = "In his immortal treatise 'On the Holy Spirit', Saint Basil proclaims that the Holy Spirit is the heavenly light that clarifies the mind and awakens a hunger for divine truth. The Basilian pillar of 'Scientia' dispels darkness through education steeped in the Gospel.",
            spiritualityFr = "Dans son immortel traité « Sur le Saint-Esprit », Saint Basile proclame que l'Esprit Saint est la lumière céleste qui éclaire l'intelligence et éveille la soif de la vérité divine. Le pilier basilien de la « Scientia » dissipe les ténèbres par une éducation imprégnée de l'Évangile.",
            quoteEn = "«Just as sunshine bathes open eyes, so the Spirit illuminates minds that seek truth with uprightness and simplicity.»",
            quoteFr = "« Comme le soleil baigne les yeux ouverts, ainsi l'Esprit illumine les intelligences qui cherchent la vérité avec droiture et simplicité. »",
            sourceEn = "Saint Basil the Great • On the Holy Spirit, IX",
            sourceFr = "Saint Basile le Grand • Traité sur le Saint-Esprit, IX"
        ),
        "PAN_DE_VIDA" to WisdomTranslation(
            passageEn = "John 6:48-51", passageFr = "Jean 6, 48-51",
            phraseEn = "«I am the living bread that came down from heaven; whoever eats this bread will live forever; and the bread that I will give is my flesh for the life of the world.»",
            phraseFr = "« Moi, je suis le pain vivant, qui est descendu du ciel : si quelqu'un mange de ce pain, il vivra éternellement. Et le pain que je donnerai, c'est ma chair, donnée pour la vie du monde. »",
            fullTextEn = "Your ancestors ate the manna in the desert, but they died; this is the bread that comes down from heaven so that one may eat it and not die. I am the living bread... Whoever eats this bread will live forever.",
            fullTextFr = "Vos pères ont mangé la manne dans le désert et ils sont morts ; mais ce pain-là descend du ciel pour que celui qui en mange ne meure pas. Moi, je suis le pain vivant... Si quelqu'un mange de ce pain, il vivra éternellement.",
            titleEn = "The Eucharist: Banquet of Fraternity and Daily Communion",
            titleFr = "L'Eucharistie : Banquet de Fraternité et Communion Quotidienne",
            spiritualityEn = "Saint Basil promoted frequent Communion in Caesarea, seeing in the Eucharist the life-giving principle of community harmony. One cannot partake of the same Chalice while keeping walls of resentment or division among brothers.",
            spiritualityFr = "Saint Basile a promu la communion fréquente à Césarée, voyant dans l'Eucharistie le principe vivifiant de la concorde communautaire. On ne peut communier au même Calice tout en maintenant des barrières de rancœur entre frères.",
            quoteEn = "«United by the same divine Bread, we are called to be one heart and one soul in praising the Most Holy Trinity.»",
            quoteFr = "« Unis par le même Pain divin, nous sommes appelés à n'être qu'un seul cœur et qu'une seule âme dans la louange de la Très Sainte Trinité. »",
            sourceEn = "Saint Basil the Great • Letter 93 on Eucharistic Communion",
            sourceFr = "Saint Basile le Grand • Lettre 93 sur la Communion Eucharistique"
        ),
        "GRANO_DE_MOSTAZA" to WisdomTranslation(
            passageEn = "Mark 4:30-32", passageFr = "Marc 4, 30-32",
            phraseEn = "«The Kingdom of God is like a mustard seed that, when it is sown in the ground, is the smallest of all the seeds on the earth. But once it is sown, it springs up and becomes the largest of plants.»",
            phraseFr = "« Le Royaume de Dieu est comparable à une graine de moutarde : quand on la sème en terre, elle est la plus petite de toutes les semences, mais quand elle a poussé, elle dépasse toutes les plantes potagères. »",
            fullTextEn = "To what shall we compare the kingdom of God? It is like a mustard seed that, when it is sown in the ground, is the smallest of all the seeds on the earth. But once it is sown, it springs up and puts forth large branches, so that the birds of the sky can dwell in its shade.",
            fullTextFr = "À quoi allons-nous comparer le règne de Dieu ? Il est comparable à une graine de moutarde : quand on la sème en terre, elle est la plus petite de toutes les semences. Mais, quand on l'a semée, elle pousse et donne de grandes branches, si bien que les oiseaux du ciel peuvent faire leur nid à son ombre.",
            titleEn = "The Humble Seed of Annonay and Hope",
            titleFr = "L'Humble Graine d'Annonay et l'Espérance",
            spiritualityEn = "When ten priest-educators in Annonay decided to live together in 1822 without great human resources, they trusted completely in the quiet power of the mustard seed. Saint Basil urges us never to despise small beginnings: fidelity in little things yields mighty trees of salvation.",
            spiritualityFr = "Lorsque dix prêtres éducateurs d'Annonay ont décidé de vivre ensemble en 1822 sans grandes ressources humaines, ils ont placé toute leur confiance dans la force silencieuse du grain de moutarde. Saint Basile nous exhorte à ne jamais mépriser les humbles commencements.",
            quoteEn = "«Do not look at the smallness of the beginning; contemplate the infinite faithfulness of God who makes fruitful every work begun in his holy name.»",
            quoteFr = "« Ne regarde pas la petitesse du commencement, contemple l'infinie fidélité de Dieu qui féconde toute œuvre entreprise en son saint nom. »",
            sourceEn = "Saint Basil the Great • Homilies on the Psalms",
            sourceFr = "Saint Basile le Grand • Homélies sur les Psaumes"
        ),
        "CAMINO_VERDAD_VIDA" to WisdomTranslation(
            passageEn = "John 14:1-6", passageFr = "Jean 14, 1-6",
            phraseEn = "«Do not let your hearts be troubled. You have faith in God; have faith also in me... I am the way and the truth and the life.»",
            phraseFr = "« Que votre cœur ne soit pas bouleversé : vous croyez en Dieu, croyez aussi en moi... Moi, je suis le Chemin, la Vérité et la Vie. »",
            fullTextEn = "In my Father's house there are many dwelling places. If there were not, would I have told you that I am going to prepare a place for you? Thomas said to him, 'Master, we do not know where you are going; how can we know the way?' Jesus said to him, 'I am the way and the truth and the life.'",
            fullTextFr = "Dans la maison de mon Père, il y a de nombreuses demeures ; sinon, vous aurais-je dit : 'Je pars vous préparer une place' ? Thomas lui dit : 'Seigneur, nous ne savons pas où tu vas. Comment pourrions-nous savoir le chemin ?' Jésus lui répond : 'Moi, je suis le Chemin, la Vérité et la Vie.'",
            titleEn = "Christ, the One Teacher and Guide of the Human Mind",
            titleFr = "Le Christ, Unique Maître et Guide de l'Esprit Humain",
            spiritualityEn = "Saint Basil wrote to the young students of Caesarea that all arts, sciences, and literature have value insofar as they lead us to Christ, Supreme Truth. There is no full wisdom apart from Jesus: He orders our discernment and turns study into an act of worship and humble service.",
            spiritualityFr = "Saint Basile écrivait aux jeunes étudiants de Césarée que tous les arts, sciences et lettres ont de la valeur s'ils nous acheminent vers le Christ, Vérité Suprême. Il n'y a pas de pleine sagesse en dehors de Jésus : Il transforme l'étude en acte d'adoration et de service.",
            quoteEn = "«To walk in Christ is to hold a steady compass amid the confusing tides of the world.»",
            quoteFr = "« Marcher dans le Christ, c'est tenir une boussole ferme au milieu des marées confuses du monde. »",
            sourceEn = "Saint Basil the Great • Address to Young Men (Ad Adolescentes)",
            sourceFr = "Saint Basile le Grand • Discours aux Jeunes Gens (Ad Adolescentes)"
        ),
        "MAGNIFICAT" to WisdomTranslation(
            passageEn = "Luke 1:46-55", passageFr = "Luc 1, 46-55",
            phraseEn = "«My soul proclaims the greatness of the Lord; my spirit rejoices in God my savior. For he has looked upon his handmaid's lowliness.»",
            phraseFr = "« Mon âme exalte le Seigneur, exulte mon esprit en Dieu, mon Sauveur ! Il s'est penché sur son humble servante. »",
            fullTextEn = "Mary said: «My soul proclaims the greatness of the Lord; my spirit rejoices in God my savior. For he has looked upon his handmaid's lowliness... He has thrown down the rulers from their thrones but lifted up the lowly. The hungry he has filled with good things.»",
            fullTextFr = "Marie dit alors : « Mon âme exalte le Seigneur, exulte mon esprit en Dieu, mon Sauveur ! Il s'est penché sur son humble servante... Il renverse les puissants de leurs trônes, il élève les humbles. Il comble de biens les affamés. »",
            titleEn = "Mary's Ceaseless Praise and Servant Humility",
            titleFr = "La Louange Continuelle de Marie et l'Humilité Servante",
            spiritualityEn = "Under the patronage of Our Lady of the Assumption and the Presentation, the Basilian Fathers venerate the Virgin Mary as the first disciple and model of consecration. Saint Basil highlights Mary's humility as the doorway through which redeeming grace enters human history.",
            spiritualityFr = "Sous le patronage de Notre-Dame de l'Assomption et de la Présentation, les Pères Basiliens vénèrent la Vierge Marie comme la première disciple et modèle de consécration. Saint Basile souligne l'humilité de Marie comme la porte par laquelle la grâce rédemptrice entre dans l'histoire.",
            quoteEn = "«Humility does not shrink the soul; it expands it so that Almighty God may dwell within it in all his fullness.»",
            quoteFr = "« L'humilité ne rétrécit pas l'âme, elle l'élargit pour que le Dieu tout-puissant puisse y habiter dans toute sa plénitude. »",
            sourceEn = "Saint Basil the Great • Homily on the Incarnation of the Word",
            sourceFr = "Saint Basile le Grand • Homélie sur l'Incarnation du Verbe"
        ),
        "VOCACION_REDES" to WisdomTranslation(
            passageEn = "Mark 1:14-20", passageFr = "Marc 1, 14-20",
            phraseEn = "«This is the time of fulfillment. The kingdom of God is at hand. Repent, and believe in the gospel... Come after me, and I will make you fishers of men.»",
            phraseFr = "« Les temps sont accomplis : le règne de Dieu est tout proche. Convertissez-vous et croyez à l'Évangile... Venez à ma suite, et je vous ferai devenir pêcheurs d'hommes. »",
            fullTextEn = "As he passed by the Sea of Galilee, he saw Simon and his brother Andrew casting their nets into the sea; they were fishermen. Jesus said to them, «Come after me, and I will make you fishers of men.» Then they abandoned their nets and followed him.",
            fullTextFr = "Passant le long de la mer de Galilée, Jésus vit Simon et André, le frère de Simon, en train de jeter les filets dans la mer, car c'étaient des pêcheurs. Il leur dit : « Venez à ma suite. Je vous ferai devenir pêcheurs d'hommes. » Aussitôt, laissant leurs filets, ils le suivirent.",
            titleEn = "Readiness in Answering the Call of Vocation",
            titleFr = "La Promptitude dans la Réponse à la Vocation",
            spiritualityEn = "Saint Basil admired the unhesitating determination of the apostles. Upon hearing Jesus' voice, they did not postpone their response. Basilian priestly and religious vocation is born from this joyful readiness to leave behind nets and comfort to set sail into education and mission.",
            spiritualityFr = "Saint Basile admirait la détermination sans hésitation des apôtres. En entendant la voix de Jésus, ils n'ont pas différé leur réponse. La vocation sacerdotale et religieuse basilienne naît de cette disponibilité joyeuse à quitter ses filets pour prendre le large dans l'éducation et la mission.",
            quoteEn = "«Whoever hears the call of the heavenly King does not linger gathering crumbs on the shore: he unfurls the sails of faith with holy boldness.»",
            quoteFr = "« Celui qui entend l'appel du Roi céleste ne s'attarde pas à ramasser des miettes sur le rivage : il déploie les voiles de la foi avec une sainte audace. »",
            sourceEn = "Saint Basil the Great • Shorter Rules, Question 1",
            sourceFr = "Saint Basile le Grand • Petites Règles, Question 1"
        ),
        "MISERICORDIA_AMOR" to WisdomTranslation(
            passageEn = "Luke 6:27-36", passageFr = "Luc 6, 27-36",
            phraseEn = "«Love your enemies, do good to those who hate you... Be merciful, just as your Father is merciful.»",
            phraseFr = "« Aimez vos ennemis, faites du bien et prêtez sans rien espérer en retour : soyez miséricordieux comme votre Père est miséricordieux. »",
            fullTextEn = "To you who hear I say, love your enemies, do good to those who hate you, bless those who curse you, pray for those who mistreat you... Be merciful, just as your Father is merciful. Stop judging and you will not be judged.",
            fullTextFr = "Je vous le dis, à vous qui m'écoutez : Aimez vos ennemis, faites du bien à ceux qui vous haïssent. Souhaitez du bien à ceux qui vous maudissent, priez pour ceux qui vous calomnient... Soyez miséricordieux comme votre Père est miséricordieux.",
            titleEn = "Unconditional Goodness (Bonitas)",
            titleFr = "La Bonté Sans Conditions (Bonitas)",
            spiritualityEn = "The first word on the Basilian crest is 'Bonitatem'. For Saint Basil, Christian goodness is not sentimental weakness, but the active reflection of the Father who makes his sun rise on the good and the bad. Educating and serving with goodness disarms hatred and opens hearts to reconciliation.",
            spiritualityFr = "Le premier mot sur le blason basilien est « Bonitatem ». Pour Saint Basile, la bonté chrétienne n'est pas une faiblesse sentimentale, mais le reflet actif du Père qui fait lever son soleil sur les bons et sur les méchants. Éduquer et servir avec bonté désarme la haine et ouvre les cœurs à la réconciliation.",
            quoteEn = "«Sincere goodness is the balm that heals society's fractures and sows peace where discord once reigned.»",
            quoteFr = "« La bonté sincère est le baume qui guérit les fractures de la société et sème la paix là où régnait la discorde. »",
            sourceEn = "Saint Basil the Great • On the Judgment of God",
            sourceFr = "Saint Basile le Grand • Traité sur le Jugement de Dieu"
        ),
        "LAVATORIO_PIES" to WisdomTranslation(
            passageEn = "John 13:34-35", passageFr = "Jean 13, 34-35",
            phraseEn = "«I give you a new commandment: love one another. As I have loved you, so you also should love one another. This is how all will know that you are my disciples.»",
            phraseFr = "« Je vous donne un commandement nouveau : c'est de vous aimer les uns les autres. Comme je vous ai aimés, vous aussi aimez-vous les uns les autres. À cela, tous reconnaîtront que vous êtes mes disciples. »",
            fullTextEn = "My children, I will be with you only a little while longer... I give you a new commandment: love one another. As I have loved you, so you also should love one another. This is how all will know that you are my disciples, if you have love for one another.",
            fullTextFr = "Petits enfants, c'est pour peu de temps encore que je suis avec vous... Je vous donne un commandement nouveau : c'est de vous aimer les uns les autres. Comme je vous ai aimés, vous aussi aimez-vous les uns les autres. À cela, tous reconnaîtront que vous êtes mes disciples.",
            titleEn = "The Witness of Fraternity as Living Preaching",
            titleFr = "Le Témoignage de la Fraternité comme Prédication Vivante",
            spiritualityEn = "Saint Basil wrote that the finest sermon is a united, affectionate community: «People are not converted by eloquent speeches, but by seeing brothers sharing a single heart in sincere charity». This communion of life is the core of Basilian priestly spirituality.",
            spiritualityFr = "Saint Basile a écrit que le meilleur sermon est une communauté unie et affectueuse : « Les hommes ne se convertissent pas par des discours éloquents, mais en voyant des frères partager un seul cœur dans une charité sincère ». Cette communion de vie est le cœur de la spiritualité basilienne.",
            quoteEn = "«Where fraternal love dwells, there the Holy Spirit abides, pouring forth blessing and life forever.»",
            quoteFr = "« Là où demeure l'amour fraternel, là habite l'Esprit Saint, répandant bénédiction et vie pour toujours. »",
            sourceEn = "Saint Basil the Great • Longer Rules, Question 3",
            sourceFr = "Saint Basile le Grand • Grandes Règles, Question 3"
        ),
        "LIRIOS_Y_AVES" to WisdomTranslation(
            passageEn = "Matthew 6:25-34", passageFr = "Matthieu 6, 25-34",
            phraseEn = "«Seek first the kingdom of God and his righteousness, and all these things will be given you besides. Look at the birds in the sky; learn from the way the wild flowers grow.»",
            phraseFr = "« Cherchez d'abord le royaume de Dieu et sa justice, et tout cela vous sera donné par surcroît : regardez les oiseaux du ciel et les lis des champs. »",
            fullTextEn = "Therefore I tell you, do not worry about your life, what you will eat or drink, or about your body, what you will wear... Learn from the way the wild flowers grow. They do not work or spin. But I tell you that not even Solomon in all his splendor was clothed like one of them.",
            fullTextFr = "C'est pourquoi je vous dis : ne vous inquiétez pas pour votre vie de ce que vous mangerez, ni pour votre corps de quoi vous le vêtirez... Observez comment poussent les lis des champs : ils ne travaillent pas, ils ne filent pas. Or je vous dis que Salomon lui-même, dans toute sa gloire, n'était pas habillé comme l'un d'entre eux.",
            titleEn = "Providential Trust and Simplicity of Life",
            titleFr = "La Confiance Providentielle et la Simplicité de Vie",
            spiritualityEn = "Saint Basil exhorted believers to live with sobriety and freedom from the anxiety of hoarding wealth. God cares with fatherly tenderness for his own. The Basilian Fathers in their schools and parishes renew this trust, setting aside worldly ambition to focus on the dignity of their neighbor.",
            spiritualityFr = "Saint Basile exhortait à vivre dans la sobriété et la liberté face au souci d'accumuler des richesses. Dieu prend soin des siens avec une tendresse paternelle. Les Pères Basiliens dans leurs collèges et paroisses renouvellent cette confiance en se consacrant au salut et à la dignité du prochain.",
            quoteEn = "«Whoever trusts wholly in God's Providence walks lightly and freely, with hands ready to bless and share.»",
            quoteFr = "« Celui qui se confie entièrement à la Providence de Dieu marche léger et libre, les mains prêtes à bénir et à partager. »",
            sourceEn = "Saint Basil the Great • Homily on Avarice",
            sourceFr = "Saint Basile le Grand • Homélie contre l'Avarice"
        ),
        "CRISTO_SERVIDOR" to WisdomTranslation(
            passageEn = "Mark 10:42-45", passageFr = "Marc 10, 42-45",
            phraseEn = "«Whoever wishes to be great among you will be your servant... For the Son of Man did not come to be served but to serve and to give his life.»",
            phraseFr = "« Celui qui veut devenir grand parmi vous sera votre serviteur ; car le Fils de l'homme n'est pas venu pour être servi, mais pour servir et donner sa vie. »",
            fullTextEn = "You know that those who are recognized as rulers over the Gentiles lord it over them... But it shall not be so among you. Rather, whoever wishes to be great among you will be your servant; whoever wishes to be first among you will be the slave of all.",
            fullTextFr = "Vous savez que ceux qu'on regarde comme les chefs des nations les commandent en maîtres... Parmi vous, il ne doit pas en être ainsi : celui qui veut devenir grand parmi vous sera votre serviteur, et celui qui veut être parmi vous le premier sera l'esclave de tous.",
            titleEn = "Servant Leadership in Education and Priesthood",
            titleFr = "Le Leadership de Service dans l'Éducation et le Sacerdoce",
            spiritualityEn = "In the Rule of Saint Basil, the superior is called 'the servant of the community' and must imitate Christ girding himself with the towel. Whoever teaches or leads in a Basilian institution is called to lay aside privileges and serve the most vulnerable brothers and students.",
            spiritualityFr = "Dans la Règle de Saint Basile, le supérieur est appelé « le serviteur de la communauté » et doit imiter le Christ ceignant le linge. Celui qui enseigne ou dirige dans une institution basilienne est appelé à se dépouiller de tout privilège pour servir ses frères et élèves les plus vulnérables.",
            quoteEn = "«The true greatness of a consecrated soul is measured by the depth of its self-giving and its joy in serving unseen.»",
            quoteFr = "« La vraie grandeur du consacré se mesure à la profondeur de son abnégation et à sa joie de servir dans le secret. »",
            sourceEn = "Saint Basil the Great • Asketikon, Chapter 30",
            sourceFr = "Saint Basile le Grand • Asketikon, Chapitre 30"
        ),
        "ORACION_CONFIADA" to WisdomTranslation(
            passageEn = "Matthew 7:7-11", passageFr = "Matthieu 7, 7-11",
            phraseEn = "«Ask and it will be given to you; seek and you will find; knock and the door will be opened to you. For everyone who asks, receives; and the one who seeks, finds.»",
            phraseFr = "« Demandez, on vous donnera ; cherchez, vous trouverez ; frappez, on vous ouvrira. En effet, quiconque demande reçoit ; qui cherche trouve ; à qui frappe, on ouvrira. »",
            fullTextEn = "Which one of you would hand his son a stone when he asks for a loaf of bread, or a snake when he asks for a fish? If you then, who are wicked, know how to give good gifts to your children, how much more will your heavenly Father give good things to those who ask him!",
            fullTextFr = "Quel est parmi vous l'homme qui donnera une pierre à son fils quand il lui demande du pain ? ou bien lui donnera un serpent, quand il lui demande un poisson ? Si donc vous savez donner de bonnes choses à vos enfants, combien plus votre Père qui est aux cieux donnera-t-il de bonnes choses à ceux qui les lui demandent !",
            titleEn = "Unceasing Prayer and Steadfast Perseverance",
            titleFr = "La Prière Continuelle et la Persévérance Incessante",
            spiritualityEn = "Saint Basil taught that prayer is not an interruption of work, but the very breath of the soul. Whether tilling the soil, teaching a class, or breaking bread, the mind can invoke the Creator. Whoever perseveres knocking at the door of divine mercy never leaves empty-handed.",
            spiritualityFr = "Saint Basile enseignait que la prière n'est pas une interruption du travail, mais la respiration même de l'âme. En travaillant la terre, en donnant un cours ou en préparant le pain, l'esprit peut invoquer le Créateur. Celui qui persévère à la porte de la miséricorde divine ne repart jamais les mains vides.",
            quoteEn = "«Make every heartbeat a song of praise and every act of kindness a prayer ascending unceasingly to heaven.»",
            quoteFr = "« Fais de chaque battement de cœur une louange et de chaque acte de bonté une prière qui monte sans cesse vers le ciel. »",
            sourceEn = "Saint Basil the Great • Homily on Saint Julitta and Unceasing Prayer",
            sourceFr = "Saint Basile le Grand • Homélie sur Sainte Julitte et la Prière Incessante"
        ),
        "TESORO_CELESTIAL" to WisdomTranslation(
            passageEn = "Luke 12:32-34", passageFr = "Luc 12, 32-34",
            phraseEn = "«Do not be afraid any longer, little flock, for your Father is pleased to give you the kingdom... For where your treasure is, there also will your heart be.»",
            phraseFr = "« Sois sans crainte, petit troupeau, car votre Père a trouvé bon de vous donner le Royaume : là où est votre trésor, là aussi sera votre cœur. »",
            fullTextEn = "Sell your belongings and give alms. Provide money bags for yourselves that do not wear out, an inexhaustible treasure in heaven that no thief can reach nor moth destroy. For where your treasure is, there also will your heart be.",
            fullTextFr = "Vendez ce que vous possédez et donnez-le en aumône. Faites-vous des bourses qui ne s'usent pas, un trésor inépuisable dans les cieux, là où le voleur n'approche pas, où la mite ne détruit pas. Car là où est votre trésor, là aussi sera votre cœur.",
            titleEn = "Evangelical Detachment from Material Goods",
            titleFr = "Le Détachement Évangélique des Biens Matériels",
            spiritualityEn = "The poverty professed by Basilians is not destitution, but a lucid choice for the supreme wealth of God. Saint Basil reminds us that our true treasure is Christ and fraternity: earthly things pass quickly, but the good sown in the heart of a child or a poor person endures for eternity.",
            spiritualityFr = "La pauvreté professée par les Basiliens n'est pas la misère, mais un choix lucide pour la richesse suprême de Dieu. Saint Basile nous rappelle que le vrai trésor est le Christ et la fraternité : les biens terrestres passent vite, mais le bien semé dans le cœur d'un enfant ou d'un pauvre demeure pour l'éternité.",
            quoteEn = "«Place your treasure in the lap of the needy, and God himself will be the eternal guardian of your reward.»",
            quoteFr = "« Dépose ton trésor entre les mains des démunis et Dieu lui-même sera le gardien éternel de ta récompense. »",
            sourceEn = "Saint Basil the Great • Exhortation on the Renunciation of the World",
            sourceFr = "Saint Basile le Grand • Exhortation au Renoncement du Monde"
        ),
        "PERDON_MISERICORDIA" to WisdomTranslation(
            passageEn = "Matthew 18:21-35", passageFr = "Matthieu 18, 21-35",
            phraseEn = "«Peter approached Jesus and asked him, 'Lord, if my brother sins against me, how often must I forgive him? As many as seven times?' Jesus answered, 'I say to you, not seven times but seventy-seven times.'»",
            phraseFr = "« Pierre s'approcha de Jésus pour lui demander : 'Seigneur, lorsque mon frère commettra des fautes contre moi, combien de fois dois-je lui pardonner ? Jusqu'à sept fois ?' Jésus lui répondit : 'Je ne te dis pas jusqu'à sept fois, mais jusqu'à soixante-dix fois sept fois.' »",
            fullTextEn = "Peter approached Jesus and asked him, «Lord, if my brother sins against me, how often must I forgive him? As many as seven times?» Jesus answered, «I say to you, not seven times but seventy-seven times. That is why the kingdom of heaven may be likened to a king who decided to settle accounts with his servants... Should you not have had pity on your fellow servant, as I had pity on you?»",
            fullTextFr = "Pierre s'approcha de Jésus pour lui demander : « Seigneur, lorsque mon frère commettra des fautes contre moi, combien de fois dois-je lui pardonner ? Jusqu'à sept fois ? » Jésus lui répondit : « Je ne te dis pas jusqu'à sept fois, mais jusqu'à soixante-dix fois sept fois... Ne devais-tu pas, toi aussi, avoir pitié de ton compagnon comme moi-même j'avais eu pitié de toi ? »",
            titleEn = "Boundless Fraternal Forgiveness: Seventy-Seven Times",
            titleFr = "Le Pardon Fraternel sans Limites : Soixante-dix Fois Sept Fois",
            spiritualityEn = "Saint Basil the Great teaches in his Moral Rules that fraternal forgiveness is not an optional human concession, but the direct reflection of God's inexhaustible mercy. For Basilians in their communities and schools, living in community requires banishing all resentment and forgiving brothers from the heart.",
            spiritualityFr = "Saint Basile le Grand enseigne dans ses Règles Morales que le pardon fraternel n'est pas une concession humaine facultative, mais le reflet direct de l'inépuisable miséricorde de Dieu. Pour les Basiliens dans leurs communautés et écoles, vivre en communauté exige de bannir toute rancœur.",
            quoteEn = "«Whoever does not forgive his brother from the heart closes with his own hands the gates of divine clemency.»",
            quoteFr = "« Celui qui ne pardonne pas de tout cœur à son frère ferme de ses propres mains les portes de la clémence divine. »",
            sourceEn = "Saint Basil the Great • Moral Rules 52, 1-3",
            sourceFr = "Saint Basile le Grand • Règles Morales 52, 1-3"
        )
    )

    fun localizeWisdom(wisdom: DailyLiturgicalWisdom, lang: AppLanguage): DailyLiturgicalWisdom {
        if (lang == AppLanguage.ES) return wisdom
        val locDate = localizeDateString(wisdom.dateDisplay, lang)
        val locSeason = when (lang) {
            AppLanguage.EN -> wisdom.liturgicalSeason
                .replace("Tiempo Ordinario Litúrgico", "Ordinary Time")
                .replace("Tiempo Ordinario", "Ordinary Time")
                .replace("Tiempo de Adviento", "Season of Advent")
                .replace("Tiempo de Navidad", "Christmas Season")
                .replace("Tiempo de Cuaresma", "Season of Lent")
                .replace("Tiempo Pascual de Resurrección", "Easter Season")
                .replace("Solemnidad", "Solemnity")
                .replace("Fiesta", "Feast")
                .replace("Memoria", "Memorial")
            AppLanguage.FR -> wisdom.liturgicalSeason
                .replace("Tiempo Ordinario Litúrgico", "Temps Ordinaire")
                .replace("Tiempo Ordinario", "Temps Ordinaire")
                .replace("Tiempo de Adviento", "Temps de l'Avent")
                .replace("Tiempo de Navidad", "Temps de Noël")
                .replace("Tiempo de Cuaresma", "Temps du Carême")
                .replace("Tiempo Pascual de Resurrección", "Temps Pascal")
                .replace("Solemnidad", "Solennité")
                .replace("Fiesta", "Fête")
                .replace("Memoria", "Mémoire")
            AppLanguage.ES -> wisdom.liturgicalSeason
        }
        val t = wisdomByTheme[wisdom.gospelImageTheme] ?: wisdomByTheme["VID_VERDADERA"]!!
        return when (lang) {
            AppLanguage.EN -> wisdom.copy(
                dateDisplay = locDate,
                liturgicalSeason = locSeason,
                gospelPassage = t.passageEn,
                singleGospelPhrase = t.phraseEn,
                gospelText = t.fullTextEn,
                basilianTeachingTitle = t.titleEn,
                basilianTeachingContent = t.spiritualityEn,
                basilianDailyQuote = t.quoteEn,
                patristicSource = t.sourceEn
            )
            AppLanguage.FR -> wisdom.copy(
                dateDisplay = locDate,
                liturgicalSeason = locSeason,
                gospelPassage = t.passageFr,
                singleGospelPhrase = t.phraseFr,
                gospelText = t.fullTextFr,
                basilianTeachingTitle = t.titleFr,
                basilianTeachingContent = t.spiritualityFr,
                basilianDailyQuote = t.quoteFr,
                patristicSource = t.sourceFr
            )
            AppLanguage.ES -> wisdom
        }
    }

    // =========================================================================
    // 3. REZANDO VOY SESSIONS LOCALIZATION
    // =========================================================================
    fun getLocalizedSessions(todayWisdom: DailyLiturgicalWisdom, lang: AppLanguage): List<RezandoVoySession> {
        return RezandoVoyData.getAllSessions(todayWisdom, lang)
    }

    // =========================================================================
    // 4. DAILY SAINTS LOCALIZATION (DailySaintsRepository)
    // =========================================================================
    fun localizeDailySaint(saint: DailySaint, lang: AppLanguage): DailySaint {
        if (lang == AppLanguage.ES) return saint
        val locDate = formatMonthDay(saint.month, saint.day, lang)

        if (saint.id.endsWith("-martirologio")) {
            return when (lang) {
                AppLanguage.EN -> saint.copy(
                    name = "Holy Martyrs and Witnesses of the Faith Today",
                    title = "Commemoration of the Roman Martyrology • Colombian Ordo",
                    liturgicalRank = "Liturgical Feria / Optional Memorial",
                    shortBiography = "On this day the Church remembers the multitude of saints, confessors, and virgins who in diverse places and ages bore heroic witness to the Gospel in daily life and surrendered their souls to the Creator in ecclesial communion.",
                    patronage = "The parish community and the faithful in their daily vocation",
                    collectPrayer = "Almighty and eternal God, who raise up in every generation luminous witnesses of your grace, grant that we may imitate the faith and perseverance of your saints to attain the reward of eternal life.",
                    feastDateDisplay = locDate
                )
                AppLanguage.FR -> saint.copy(
                    name = "Saints Martyrs et Témoins de la Foi de ce Jour",
                    title = "Commémoration du Martyrologe Romain • Ordo Colombien",
                    liturgicalRank = "Férie Liturgique / Mémoire Facultative",
                    shortBiography = "En ce jour, l'Église fait mémoire de la multitude des saints, confesseurs et vierges qui, en divers lieux et époques, ont rendu un témoignage héroïque à l'Évangile dans la vie quotidienne et ont remis leur âme au Créateur.",
                    patronage = "La communauté paroissiale et les fidèles dans leur vocation quotidienne",
                    collectPrayer = "Dieu tout-puissant et éternel, qui suscites à chaque génération des témoins lumineux de ta grâce, accorde-nous d'imiter la foi et la persévérance de tes saints pour parvenir à la vie éternelle.",
                    feastDateDisplay = locDate
                )
                AppLanguage.ES -> saint
            }
        }

        // Translate common ranks, titles, and names for known saints
        return when (lang) {
            AppLanguage.EN -> saint.copy(
                name = saint.name
                    .replace("Santa María, Madre de Dios", "Mary, the Holy Mother of God")
                    .replace("San Basilio Magno y San Gregorio Nacianceno", "Saints Basil the Great and Gregory Nazianzen")
                    .replace("Santo Tomás de Aquino", "Saint Thomas Aquinas")
                    .replace("San Juan Bosco", "Saint John Bosco")
                    .replace("San José, Esposo de la Virgen María", "Saint Joseph, Spouse of the Blessed Virgin Mary")
                    .replace("Santa Catalina de Siena", "Saint Catherine of Siena")
                    .replace("San Antonio de Padua", "Saint Anthony of Padua")
                    .replace("Santos Apóstoles Pedro y Pablo", "Saints Peter and Paul, Apostles")
                    .replace("Nuestra Señora del Rosario de Chiquinquirá", "Our Lady of the Rosary of Chiquinquirá")
                    .replace("Nuestra Señora del Monte Carmelo", "Our Lady of Mount Carmel")
                    .replace("Solemnidad de la Asunción de la Virgen María", "Solemnity of the Assumption of the Blessed Virgin Mary")
                    .replace("San Agustín de Hipona", "Saint Augustine of Hippo")
                    .replace("San Pedro Claver, Presbítero S.J.", "Saint Peter Claver, Priest S.J.")
                    .replace("San Pío de Pietrelcina (Padre Pío)", "Saint Pius of Pietrelcina (Padre Pio)")
                    .replace("San Vicente de Paúl", "Saint Vincent de Paul")
                    .replace("Santos Arcángeles Miguel, Gabriel y Rafael", "Saints Michael, Gabriel, and Raphael, Archangels")
                    .replace("San Jerónimo, Presbítero y Doctor", "Saint Jerome, Priest and Doctor")
                    .replace("Santa Teresa del Niño Jesús (Teresita de Lisieux)", "Saint Thérèse of the Child Jesus (of Lisieux)")
                    .replace("Los Santos Ángeles Custodios", "The Holy Guardian Angels")
                    .replace("San Francisco de Asís", "Saint Francis of Assisi")
                    .replace("Santa María Faustina Kowalska", "Saint Faustina Kowalska")
                    .replace("Nuestra Señora del Rosario", "Our Lady of the Rosary")
                    .replace("San Luis Beltrán, Presbítero O.P.", "Saint Louis Bertrand, Priest O.P.")
                    .replace("San Juan XXIII, Papa", "Saint John XXIII, Pope")
                    .replace("Santa Teresa de Jesús (de Ávila)", "Saint Teresa of Jesus (of Ávila)")
                    .replace("San Lucas, Evangelista", "Saint Luke, Evangelist")
                    .replace("Santa Laura Montoya Upegui", "Saint Laura Montoya Upegui")
                    .replace("San Juan Pablo II, Papa", "Saint John Paul II, Pope")
                    .replace("Santos Simón y Judas Tadeo, Apóstoles", "Saints Simon and Jude, Apostles")
                    .replace("Solemnidad de Todos los Santos", "Solemnity of All Saints")
                    .replace("Conmemoración de Todos los Fieles Difuntos", "Commemoration of All the Faithful Departed")
                    .replace("San Martín de Porres", "Saint Martin de Porres")
                    .replace("Solemnidad de la Inmaculada Concepción", "Solemnity of the Immaculate Conception")
                    .replace("Nuestra Señora de Guadalupe", "Our Lady of Guadalupe")
                    .replace("San Juan de la Cruz", "Saint John of the Cross")
                    .replace("San ", "Saint ").replace("Santa ", "Saint "),
                title = translateCommonTheologicalText(saint.title, AppLanguage.EN),
                liturgicalRank = saint.liturgicalRank
                    .replace("Solemnidad Universal", "Universal Solemnity")
                    .replace("Solemnidad Nacional", "National Solemnity")
                    .replace("Memoria Obligatoria", "Obligatory Memorial")
                    .replace("Memoria Libre", "Optional Memorial")
                    .replace("Fiesta de la Iglesia Universal", "Feast of the Universal Church")
                    .replace("Fiesta", "Feast")
                    .replace("Solemnidad para los Basilianos", "Solemnity for the Basilians"),
                shortBiography = translateCommonTheologicalText(saint.shortBiography, AppLanguage.EN),
                patronage = translateCommonTheologicalText(saint.patronage, AppLanguage.EN),
                collectPrayer = translateCommonTheologicalText(saint.collectPrayer, AppLanguage.EN),
                feastDateDisplay = locDate
            )
            AppLanguage.FR -> saint.copy(
                name = saint.name
                    .replace("Santa María, Madre de Dios", "Sainte Marie, Mère de Dieu")
                    .replace("San Basilio Magno y San Gregorio Nacianceno", "Saints Basile le Grand et Grégoire de Nazianze")
                    .replace("Santo Tomás de Aquino", "Saint Thomas d'Aquin")
                    .replace("San Juan Bosco", "Saint Jean Bosco")
                    .replace("San José, Esposo de la Virgen María", "Saint Joseph, Époux de la Vierge Marie")
                    .replace("Santa Catalina de Siena", "Sainte Catherine de Sienne")
                    .replace("San Antonio de Padua", "Saint Antoine de Padoue")
                    .replace("Santos Apóstoles Pedro y Pablo", "Saints Pierre et Paul, Apôtres")
                    .replace("Nuestra Señora del Rosario de Chiquinquirá", "Notre-Dame du Rosaire de Chiquinquirá")
                    .replace("Nuestra Señora del Monte Carmelo", "Notre-Dame du Mont-Carmel")
                    .replace("Solemnidad de la Asunción de la Virgen María", "Solennité de l'Assomption de la Vierge Marie")
                    .replace("San Agustín de Hipona", "Saint Augustin d'Hippone")
                    .replace("San Pedro Claver, Presbítero S.J.", "Saint Pierre Claver, Prêtre S.J.")
                    .replace("San Pío de Pietrelcina (Padre Pío)", "Saint Pie de Pietrelcina (Padre Pio)")
                    .replace("San Vicente de Paúl", "Saint Vincent de Paul")
                    .replace("Santos Arcángeles Miguel, Gabriel y Rafael", "Saints Michel, Gabriel et Raphaël, Archanges")
                    .replace("San Jerónimo, Presbítero y Doctor", "Saint Jérôme, Prêtre et Docteur")
                    .replace("Santa Teresa del Niño Jesús (Teresita de Lisieux)", "Sainte Thérèse de l'Enfant-Jésus (de Lisieux)")
                    .replace("Los Santos Ángeles Custodios", "Les Saints Anges Gardiens")
                    .replace("San Francisco de Asís", "Saint François d'Assise")
                    .replace("Santa María Faustina Kowalska", "Sainte Faustine Kowalska")
                    .replace("Nuestra Señora del Rosario", "Notre-Dame du Rosaire")
                    .replace("San Luis Beltrán, Presbítero O.P.", "Saint Louis Bertrand, Prêtre O.P.")
                    .replace("San Juan XXIII, Papa", "Saint Jean XXIII, Pape")
                    .replace("Santa Teresa de Jesús (de Ávila)", "Sainte Thérèse de Jésus (d'Ávila)")
                    .replace("San Lucas, Evangelista", "Saint Luc, Évangéliste")
                    .replace("Santa Laura Montoya Upegui", "Sainte Laura Montoya Upegui")
                    .replace("San Juan Pablo II, Papa", "Saint Jean-Paul II, Pape")
                    .replace("Santos Simón y Judas Tadeo, Apóstoles", "Saints Simon et Jude, Apôtres")
                    .replace("Solemnidad de Todos los Santos", "Solennité de Tous les Saints")
                    .replace("Conmemoración de Todos los Fieles Difuntos", "Commémoration de Tous les Fidèles Défunts")
                    .replace("San Martín de Porres", "Saint Martin de Porres")
                    .replace("Solemnidad de la Inmaculada Concepción", "Solennité de l'Immaculée Conception")
                    .replace("Nuestra Señora de Guadalupe", "Notre-Dame de Guadalupe")
                    .replace("San Juan de la Cruz", "Saint Jean de la Croix")
                    .replace("San ", "Saint ").replace("Santa ", "Sainte "),
                title = translateCommonTheologicalText(saint.title, AppLanguage.FR),
                liturgicalRank = saint.liturgicalRank
                    .replace("Solemnidad Universal", "Solennité Universelle")
                    .replace("Solemnidad Nacional", "Solennité Nationale")
                    .replace("Memoria Obligatoria", "Mémoire Obligatoire")
                    .replace("Memoria Libre", "Mémoire Facultative")
                    .replace("Fiesta de la Iglesia Universal", "Fête de l'Église Universelle")
                    .replace("Fiesta", "Fête")
                    .replace("Solemnidad para los Basilianos", "Solennité pour les Basiliens"),
                shortBiography = translateCommonTheologicalText(saint.shortBiography, AppLanguage.FR),
                patronage = translateCommonTheologicalText(saint.patronage, AppLanguage.FR),
                collectPrayer = translateCommonTheologicalText(saint.collectPrayer, AppLanguage.FR),
                feastDateDisplay = locDate
            )
            AppLanguage.ES -> saint
        }
    }

    // =========================================================================
    // 5. LITURGY OF THE HOURS (FRENCH + BILINGUAL SUPPORT)
    // =========================================================================
    fun getLocalizedHourTitle(hourType: LiturgicalHourType, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> hourType.titleEn
        AppLanguage.FR -> when (hourType) {
            LiturgicalHourType.INVITATORIO -> "Invitatoire"
            LiturgicalHourType.OFICIO_DE_LECTURA -> "Office des Lectures"
            LiturgicalHourType.LAUDES -> "Laudes (Prière du Matin)"
            LiturgicalHourType.TERCIA -> "Tierce (9h00)"
            LiturgicalHourType.SEXTA -> "Sexte (Midi)"
            LiturgicalHourType.NONA -> "None (15h00)"
            LiturgicalHourType.VISPERAS -> "Vêpres (Prière du Soir)"
            LiturgicalHourType.COMPLETAS -> "Complies (Prière de la Nuit)"
        }
        AppLanguage.ES -> hourType.titleEs
    }

    fun getLocalizedHourSubLabel(hourType: LiturgicalHourType, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> when (hourType) {
            LiturgicalHourType.INVITATORIO -> "Opening of the Office"
            LiturgicalHourType.OFICIO_DE_LECTURA -> "Biblical & Patristic Meditation"
            LiturgicalHourType.LAUDES -> "6:00 AM - 9:00 AM"
            LiturgicalHourType.TERCIA -> "9:00 AM"
            LiturgicalHourType.SEXTA -> "12:00 PM"
            LiturgicalHourType.NONA -> "3:00 PM"
            LiturgicalHourType.VISPERAS -> "6:00 PM - 8:00 PM"
            LiturgicalHourType.COMPLETAS -> "Before Night Rest"
        }
        AppLanguage.FR -> when (hourType) {
            LiturgicalHourType.INVITATORIO -> "Ouverture de l'Office"
            LiturgicalHourType.OFICIO_DE_LECTURA -> "Méditation biblique et patristique"
            LiturgicalHourType.LAUDES -> "Consécration de l'aube"
            LiturgicalHourType.TERCIA -> "Heure Mineure • Esprit Saint"
            LiturgicalHourType.SEXTA -> "Heure Mineure • Angélus"
            LiturgicalHourType.NONA -> "Heure Mineure • Miséricorde"
            LiturgicalHourType.VISPERAS -> "Action de grâce vespérale"
            LiturgicalHourType.COMPLETAS -> "Examen de conscience et repos"
        }
        AppLanguage.ES -> hourType.suggestedTime
    }

    fun getLocalizedHourContent(hourData: BilingualLiturgicalHour?, lang: AppLanguage): HourContent? {
        if (hourData == null) return null
        return when (lang) {
            AppLanguage.ES -> hourData.es
            AppLanguage.EN -> hourData.en
            AppLanguage.FR -> buildFrenchHourContent(hourData.hourType)
        }
    }

    private fun buildFrenchHourContent(hourType: LiturgicalHourType): HourContent = when (hourType) {
        LiturgicalHourType.INVITATORIO -> HourContent(
            hourName = "Invitatoire",
            ordoRubricNote = "V. Seigneur, ouvre mes lèvres. R. Et ma bouche publiera ta louange.",
            introductoryVerse = "V. Seigneur, ouvre mes lèvres.\nR. Et ma bouche publiera ta louange.",
            hymnTitle = "Venez, Approchons-nous du Seigneur",
            hymnText = "Venez, adorons le Seigneur, source de sagesse et de grâce sainte.\nIl nous rassemble en un seul troupeau fidèle dans le Christ Jésus.",
            psalms = listOf(
                PsalmItem(
                    title = "Psaume 94 (95)",
                    subtitle = "Invitation à la louange et à l'écoute",
                    antiphon = "Ant. Venez, adorons le Seigneur qui nous a faits.",
                    psalmText = "Venez, crions de joie pour le Seigneur, acclamons le Rocher qui nous sauve !\nAllons jusqu'à lui en rendant grâce, par nos hymnes de fête acclamons-le !\nOui, le grand Dieu, c'est le Seigneur, le grand Roi au-dessus de tous les dieux.",
                    gloriaPatri = "Gloire au Père, et au Fils, et au Saint-Esprit, pour les siècles des siècles. Amen."
                )
            ),
            briefReading = ReadingItem("Proverbes 3, 5-6", "Pr 3, 5-6", "Fais confiance au Seigneur de tout ton cœur, et ne t'appuie pas sur ta propre intelligence.", "V. Enseigne-moi ton chemin, Seigneur.", "R. Et conduis-moi sur une voie droite."),
            ourFather = "Notre Père, qui es aux cieux...",
            concludingPrayer = "Dieu tout-puissant, répands ta grâce dans nos cœurs à l'aube de ce jour nouveau, afin que guidés par ta lumière nous puissions te servir d'un cœur pur. Par le Christ notre Seigneur. Amen.",
            blessing = "Que le Seigneur nous bénisse, qu'il nous garde de tout mal et nous conduise à la vie éternelle. Amen."
        )
        LiturgicalHourType.LAUDES -> HourContent(
            hourName = "Laudes (Prière du Matin)",
            ordoRubricNote = "Ordo Colombien (CEC) : Louange matinale et offrande du travail quotidien.",
            introductoryVerse = "V. Dieu, viens à mon aide.\nR. Seigneur, à notre secours.\nGloire au Père...",
            hymnTitle = "Voici que la Clarté du Jour",
            hymnText = "Voici que la clarté du jour emplit le ciel, nous élevons nos cœurs vers Dieu,\nafin qu'en tout ce que nous faisons ou disons, il nous garde en sa paix aujourd'hui.\nQue la lumière divine brille sur la Colombie et sur notre famille basilienne.",
            psalms = listOf(
                PsalmItem(
                    title = "Psaume 62 (63), 2-9",
                    subtitle = "L'âme assoiffée de Dieu",
                    antiphon = "Ant. Dieu, tu es mon Dieu, je te cherche dès l'aube : mon âme a soif de toi.",
                    psalmText = "Dieu, tu es mon Dieu, je te cherche dès l'aube : mon âme a soif de toi ;\naprès toi languit ma chair, terre aride, altérée, sans eau.\nJe t'ai contemplé au sanctuaire, j'ai vu ta force et ta gloire.\nTon amour vaut mieux que la vie : tu seras la louange de mes lèvres !",
                    gloriaPatri = "Gloire au Père, et au Fils, et au Saint-Esprit..."
                ),
                PsalmItem(
                    title = "Cantique des Trois Enfants (Dn 3, 57-88)",
                    subtitle = "Que toute la création loue le Seigneur",
                    antiphon = "Ant. Toutes les œuvres du Seigneur, bénissez le Seigneur : à lui, haute gloire, louange éternelle !",
                    psalmText = "Toutes les œuvres du Seigneur, bénissez le Seigneur : à lui, haute gloire, louange éternelle !\nAnges du Seigneur, bénissez le Seigneur ; vous les cieux, bénissez le Seigneur.\nSoleil et lune, bénissez le Seigneur ; astres du ciel, bénissez le Seigneur.",
                    gloriaPatri = "Bénissons le Père, le Fils et l'Esprit Saint..."
                )
            ),
            briefReading = ReadingItem("Romains 12, 1-2", "Rm 12, 1-2", "Je vous exhorte donc, frères, par la tendresse de Dieu, à lui offrir votre personne et votre vie en sacrifice saint, capable de plaire à Dieu.", "V. Le Seigneur est mon berger : je ne manque de rien.", "R. Sur des prés d'herbe fraîche, il me fait reposer."),
            canticleTitle = "Cantique de Zacharie (Benedictus) - Luc 1, 68-79",
            canticleAntiphon = "Ant. Par la tendresse du cœur de notre Dieu, l'astre d'en haut vient nous visiter.",
            canticleText = "Béni soit le Seigneur, le Dieu d'Israël, qui visite et rachète son peuple.\nIl a fait surgir la force qui nous sauve dans la maison de David, son serviteur,\ncomme il l'avait dit par la bouche des saints, par ses prophètes, depuis les temps anciens.",
            preces = PrecesItem(
                intro = "Invoquons le Christ, Lumière du monde et Sauveur :",
                petitions = listOf(
                    "Pour l'Église en Colombie et ses pasteurs" to "Accorde sagesse et courage à nos évêques et prêtres pour annoncer la réconciliation et la paix.",
                    "Pour les responsables civils et communautaires" to "Qu'ils défendent la justice, le bien commun et protègent les plus vulnérables.",
                    "Pour les Pères Basiliens dans leurs apostolats éducatifs et pastoraux" to "Que la bonté, la discipline et la science prennent racine dans le cœur des jeunes.",
                    "Pour les malades, les familles déplacées et les pauvres" to "Guéris leurs blessures et envoie des mains généreuses pour les secourir."
                ),
                conclusion = "Unis comme frères et sœurs dans la foi, prions comme le Seigneur nous l'a enseigné :"
            ),
            ourFather = "Notre Père, qui es aux cieux, que ton nom soit sanctifié, que ton règne vienne, que ta volonté soit faite sur la terre comme au ciel. Donne-nous aujourd'hui notre pain de ce jour. Pardonne-nous nos offenses, comme nous pardonnons aussi à ceux qui nous ont offensés. Et ne nous laisse pas entrer en tentation, mais délivre-nous du Mal. Amen.",
            concludingPrayer = "Seigneur Dieu, Roi du ciel et de la terre, dirige et sanctifie, gouverne nos cœurs et nos corps en ce jour dans l'écoute de ta loi et la pratique de tes commandements. Par Jésus, le Christ, notre Seigneur. Amen.",
            blessing = "Que le Seigneur nous bénisse, qu'il nous garde de tout mal et nous conduise à la vie éternelle. Amen."
        )
        LiturgicalHourType.VISPERAS -> HourContent(
            hourName = "Vêpres (Prière du Soir)",
            ordoRubricNote = "Ordo Colombien (CEC) : Action de grâce du soir pour les dons de la journée et supplication pour la paix.",
            introductoryVerse = "V. Dieu, viens à mon aide.\nR. Seigneur, à notre secours.\nGloire au Père...",
            hymnTitle = "Joyeuse Lumière de la Sainte Gloire",
            hymnText = "Joyeuse lumière de la sainte gloire du Père immortel et céleste,\nau déclin du jour nous chantons ton salut.\nQue l'Esprit Saint enveloppe nos foyers de paix et de concorde fraternelle.",
            psalms = listOf(
                PsalmItem(
                    title = "Psaume 140 (141), 1-9",
                    subtitle = "Prière du soir dans l'épreuve",
                    antiphon = "Ant. Que ma prière devant toi s'élève comme un encens, et mes mains, comme l'offrande du soir.",
                    psalmText = "Seigneur, je t'appelle : accours vers moi ! Écoute ma voix qui t'appelle !\nQue ma prière devant toi s'élève comme un encens, et mes mains, comme l'offrande du soir.\nMets une garde à mes lèvres, Seigneur, veille au seuil de ma bouche.",
                    gloriaPatri = "Gloire au Père, et au Fils, et au Saint-Esprit..."
                ),
                PsalmItem(
                    title = "Cantique du Nouveau Testament (Philippiens 2, 6-11)",
                    subtitle = "Le Christ, serviteur de Dieu",
                    antiphon = "Ant. Au nom de Jésus, que tout genou fléchisse au ciel, sur terre et aux enfers.",
                    psalmText = "Le Christ Jésus, ayant la condition de Dieu, ne retint pas jalousement le rang qui l'égalait à Dieu.\nMais il s'est anéanti, prenant la condition de serviteur, devenant semblable aux hommes.\nReconnu homme à son aspect, il s'est abaissé, devenant obéissant jusqu'à la mort, et la mort de la croix.",
                    gloriaPatri = "Gloire au Père, et au Fils, et au Saint-Esprit..."
                )
            ),
            briefReading = ReadingItem("1 Pierre 5, 5-7", "1 P 5, 5-7", "Dans vos rapports mutuels, revêtez-vous tous d'humilité, car Dieu s'oppose aux orgueilleux, aux humbles il accorde sa grâce. Déchargez-vous sur lui de tous vos soucis, puisqu'il prend soin de vous.", "V. Le Seigneur est tendresse et pitié.", "R. Lent à la colère et plein d'amour."),
            canticleTitle = "Cantique de la Vierge Marie (Magnificat) - Luc 1, 46-55",
            canticleAntiphon = "Ant. Mon âme exalte le Seigneur, car le Puissant fit pour moi des merveilles.",
            canticleText = "Mon âme exalte le Seigneur, exulte mon esprit en Dieu, mon Sauveur !\nIl s'est penché sur son humble servante ;\ndésormais, tous les âges me diront bienheureuse.\nLe Puissant fit pour moi des merveilles ; Saint est son nom !\nSon amour s'étend d'âge en âge sur ceux qui le craignent.",
            preces = PrecesItem(
                intro = "Élevons nos supplications du soir vers Jésus-Christ, la lumière éternelle qui ne s'éteint jamais :",
                petitions = listOf(
                    "Pour les familles de Colombie et du monde" to "Sois leur sanctuaire et leur refuge, éloigne la violence et guéris toute division.",
                    "Pour les enseignants, catéchistes et étudiants" to "Illumine-les de sagesse, de vertu et de zèle pour l'éducation chrétienne.",
                    "Pour les vocations à la Congrégation de Saint-Basile" to "Inspire aux jeunes le courage de répondre à l'appel du sacerdoce et de la vie religieuse.",
                    "Pour nos frères défunts, bienfaiteurs et proches" to "Accueille-les dans la lumière perpétuelle et la joie de ton banquet céleste."
                ),
                conclusion = "En frères et sœurs, disons la prière que notre Sauveur nous a confiée :"
            ),
            ourFather = "Notre Père, qui es aux cieux...",
            concludingPrayer = "Au déclin du jour, Seigneur, nous te rendons grâce pour tes dons inépuisables et nous te prions pour que ta paix garde nos esprits et nos cœurs dans le Christ Jésus. Amen.",
            blessing = "Que le Seigneur nous bénisse, qu'il nous garde de tout mal et nous conduise à la vie éternelle. Amen."
        )
        LiturgicalHourType.COMPLETAS -> HourContent(
            hourName = "Complies (Prière de la Nuit)",
            ordoRubricNote = "Ordo Colombien (CEC) : Examen de conscience nocturne et repos sous la garde de Dieu.",
            introductoryVerse = "V. Dieu, viens à mon aide.\nR. Seigneur, à notre secours.\nGloire au Père...",
            examinationOfConscience = "Moment de silence pour l'examen de conscience devant Dieu : « Je confesse à Dieu tout-puissant, je reconnais devant vous, frères et sœurs, que j'ai péché en pensée, en parole, par action et par omission... »",
            hymnTitle = "Avant que la Lumière ne S'éteigne (Te Lucis Ante Terminum)",
            hymnText = "Avant la fin de la lumière, nous te prions, Dieu créateur,\npour que, fidèle à ta bonté, tu nous protèges, tu nous gardes.",
            psalms = listOf(
                PsalmItem(
                    title = "Psaume 90 (91)",
                    subtitle = "Sous l'abri du Très-Haut",
                    antiphon = "Ant. Il te couvrira de ses ailes, tu ne craindras pas les terreurs de la nuit.",
                    psalmText = "Quand je me tiens sous l'abri du Très-Haut et repose à l'ombre du Puissant,\nje dis au Seigneur : « Mon refuge, mon rempart, mon Dieu, dont je suis sûr ! »\nC'est lui qui te sauve des filets du chasseur et de la peste maléfique ;\nil te couvre et te protège, tu trouves sous son aile un refuge.",
                    gloriaPatri = "Gloire au Père, et au Fils, et au Saint-Esprit..."
                )
            ),
            briefReading = ReadingItem("Apocalypse 22, 4-5", "Ap 22, 4-5", "Ils verront le visage du Seigneur, et son nom sera sur leur front. Il n'y aura plus de nuit ; ils n'auront pas besoin de la lumière du flambeau ni de la lumière du soleil, parce que le Seigneur Dieu les illuminera.", "V. En tes mains, Seigneur, je remets mon esprit.", "R. Tu nous rachètes, Seigneur, Dieu de vérité."),
            canticleTitle = "Cantique de Siméon (Nunc Dimittis) - Luc 2, 29-32",
            canticleAntiphon = "Ant. Sauve-nous, Seigneur, quand nous veillons ; garde-nous quand nous dormons : nous veillerons avec le Christ, et nous reposerons en paix.",
            canticleText = "Maintenant, ô Maître souverain, tu peux laisser ton serviteur s'en aller en paix, selon ta parole.\nCar mes yeux ont vu le salut que tu préparais à la face des peuples :\nlumière qui se révèle aux nations et gloire de ton peuple Israël.",
            ourFather = "Notre Père...",
            concludingPrayer = "Visite cette maison, Seigneur, et repousse au loin toutes les embûches de l'ennemi ; que tes saints anges y habitent pour nous garder dans la paix, et que ta bénédiction demeure toujours sur nous. Par le Christ notre Seigneur. Amen.",
            blessing = "Que le Seigneur tout-puissant nous accorde une nuit tranquille et une sainte mort. Amen.",
            marianAntiphon = "Salut, Reine, Mère de miséricorde, notre vie, notre douceur et notre espérance, salut ! Enfants d'Ève, exilés, nous crions vers vous. Vers vous nous soupirons, gémissant et pleurant dans cette vallée de larmes. Ô vous, notre Avocate, tournez vers nous vos regards miséricordieux. Et, après cet exil, montrez-nous Jésus, le fruit béni de vos entrailles, ô clémente, ô charitable, ô douce Vierge Marie."
        )
        LiturgicalHourType.OFICIO_DE_LECTURA -> HourContent(
            hourName = "Office des Lectures",
            ordoRubricNote = "Ordo Colombien (CEC) : Méditation approfondie de la Parole de Dieu et des écrits patristiques.",
            introductoryVerse = "V. Dieu, viens à mon aide. R. Seigneur, à notre secours.",
            hymnTitle = "Seigneur Éternel de Sagesse",
            hymnText = "Dans le silence de la nuit et de l'aurore, nous cherchons ta sainte face, Seigneur de vérité.\nTa Parole est une lampe pour nos pas et une lumière sur notre sentier.",
            psalms = listOf(
                PsalmItem("Psaume 18 (19), 2-7", "Louange au Créateur de l'univers", "Ant. Les cieux proclament la gloire de Dieu.", "Les cieux proclament la gloire de Dieu, le firmament raconte l'ouvrage de ses mains...", "Gloire au Père...")
            ),
            briefReading = ReadingItem("Colossiens 1, 9-14", "Col 1, 9-14", "Nous ne cessons de prier pour vous et de demander que vous soyez remplis de la pleine connaissance de la volonté de Dieu, en toute sagesse et intelligence spirituelle.", "V. Ta parole est la lumière de mes pas.", "R. La lampe de ma route."),
            secondReading = ReadingItem("Du Traité de Saint Basile le Grand sur le Saint-Esprit", "Sur le Saint-Esprit, Ch. 9", "Par l'Esprit Saint nous est donnée la restauration au paradis, la montée dans le royaume des cieux, le retour à l'adoption filiale. Celui qui contemple l'Esprit Saint est transformé de clarté en clarté.", "V. Donne-nous, Seigneur, un cœur sage et attentif.", "R. Pour proclamer tes merveilles."),
            ourFather = "Notre Père...",
            concludingPrayer = "Dieu tout-puissant et éternel, qui as donné en Saint Basile le Grand un maître illustre de vérité, accorde-nous de puiser à la source de ta sagesse et de témoigner de l'amour fraternel. Par le Christ notre Seigneur. Amen.",
            blessing = "Bénissons le Seigneur. Nous rendons grâce à Dieu."
        )
        LiturgicalHourType.TERCIA, LiturgicalHourType.SEXTA, LiturgicalHourType.NONA -> HourContent(
            hourName = getLocalizedHourTitle(hourType, AppLanguage.FR),
            ordoRubricNote = "Ordo Colombien (CEC) : Pause au milieu des travaux du jour pour élever le cœur vers le Seigneur.",
            introductoryVerse = "V. Dieu, viens à mon aide. R. Seigneur, à notre secours.",
            hymnTitle = "Consécration de l'Heure",
            hymnText = "Ô Dieu d'infinie bonté, au cœur des travaux de cette journée, nous tournons nos regards vers toi pour implorer ta bénédiction.",
            psalms = listOf(
                PsalmItem("Psaume 118 (119)", "Amour de la loi divine", "Ant. Ta parole, Seigneur, demeure pour toujours.", "Heureux ceux qui marchent suivant la loi du Seigneur d'un cœur sans partage...", "Gloire au Père...")
            ),
            briefReading = ReadingItem("Jérémie 17, 7-8", "Jr 17, 7-8", "Béni soit l'homme qui met sa foi dans le Seigneur, dont le Seigneur est la confiance. Il sera comme un arbre planté près des eaux.", "V. En toi, Seigneur, j'ai mis mon espérance.", "R. Que je ne sois jamais confondu."),
            ourFather = "Notre Père...",
            concludingPrayer = "Seigneur Jésus, qui à cette heure du jour t'es offert sur la croix pour le salut du monde, accorde-nous de travailler avec droiture et amour pour ton Royaume. Amen.",
            blessing = "Bénissons le Seigneur. Nous rendons grâce à Dieu."
        )
    }

    fun getLocalizedOrdoTitle(day: ColombianLiturgicalDay, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> day.titleEn
        AppLanguage.FR -> translateCommonTheologicalText(day.titleEs, AppLanguage.FR)
        AppLanguage.ES -> day.titleEs
    }

    fun getLocalizedOrdoRank(day: ColombianLiturgicalDay, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> day.rankEn
        AppLanguage.FR -> translateCommonTheologicalText(day.rankEs, AppLanguage.FR)
        AppLanguage.ES -> day.rankEs
    }

    fun getLocalizedOrdoDateDisplay(day: ColombianLiturgicalDay, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> day.dateDisplayEn
        AppLanguage.FR -> localizeDateString(day.dateDisplayEs, AppLanguage.FR)
            .replace("Solemnidad Nacional", "Solennité Nationale")
            .replace("Fiesta Nacional", "Fête Nationale")
            .replace("Solemnidad Titular CSB", "Solennité Titulaire CSB")
        AppLanguage.ES -> day.dateDisplayEs
    }

    fun getLocalizedOrdoPsalterWeek(day: ColombianLiturgicalDay, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> day.psalterWeekEn
        AppLanguage.FR -> day.psalterWeekEs
            .replace("Semana", "Semaine")
            .replace("del Salterio", "du Psautier")
            .replace("Salterio de la Solemnidad", "Psautier de la Solennité")
            .replace("Salterio de la Fiesta", "Psautier de la Fête")
        AppLanguage.ES -> day.psalterWeekEs
    }

    fun getLocalizedOrdoNotes(day: ColombianLiturgicalDay, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> day.ordoNotesEn
        AppLanguage.FR -> translateCommonTheologicalText(day.ordoNotesEs, AppLanguage.FR)
        AppLanguage.ES -> day.ordoNotesEs
    }

    fun getLocalizedBasilianConnection(day: ColombianLiturgicalDay, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> day.basilianConnectionEn
        AppLanguage.FR -> translateCommonTheologicalText(day.basilianConnectionEs, AppLanguage.FR)
        AppLanguage.ES -> day.basilianConnectionEs
    }

    // =========================================================================
    // 6. AR ICONOGRAPHY HOTSPOTS LOCALIZATION
    // =========================================================================
    fun localizeHotspot(hotspot: IconHotspot, lang: AppLanguage): IconHotspot {
        if (lang == AppLanguage.ES) return hotspot
        return when (hotspot.id) {
            "hotspot_nimbus" -> when (lang) {
                AppLanguage.EN -> hotspot.copy(
                    title = "Golden Halo of Uncreated Light",
                    theologicalCategory = "Divine Light & Deification (Theosis)",
                    shortBadge = "Taboric Light",
                    description = "The radiant circular halo crowning the saint's head, crafted in burnished gold and edged with crimson filigree.",
                    theologicalExegesis = "In the Eastern patristic theology of Saint Basil and Saint Gregory Palamas, the halo is not a mere aesthetic crown. It represents the Uncreated Light of God that shone at the Transfiguration on Mount Tabor. Gold casts no earthly shadows: it testifies that the human person has been permeated by the sanctifying grace of the Holy Spirit unto 'theosis' (deification by grace).",
                    patristicSource = "Saint Basil the Great, 'On the Holy Spirit', Ch. IX, 23",
                    patristicQuote = "«Just as clear and transparent bodies, when struck by a ray of light, become radiant themselves, so souls illumined by the Spirit become spiritual and transmit grace to others.»",
                    audioNarrationScript = "Gaze upon the golden halo. In sacred iconography, gold is not an earthly color, but the very presence of the Uncreated Light of Mount Tabor. Saint Basil reminds us in his Treatise on the Holy Spirit that the believer who purifies their heart becomes like a clear crystal: receiving divine light and radiating it to their brothers and sisters."
                )
                AppLanguage.FR -> hotspot.copy(
                    title = "Auréole Dorée de Lumière Incréée",
                    theologicalCategory = "Lumière Divine & Divinisation (Théosis)",
                    shortBadge = "Lumière Thaborique",
                    description = "Le halo circulaire resplendissant qui couronne la tête du saint, tracé à l'or bruni et bordé de filigrane cramoisi.",
                    theologicalExegesis = "Dans la théologie patristique orientale de Saint Basile et Saint Grégoire Palamas, l'auréole n'est pas une simple couronne esthétique. Elle représente la Lumière Incréée de Dieu qui a resplendi lors de la Transfiguration au Mont Thabor. L'or témoigne que la personne humaine a été transfigurée par la grâce sanctifiante du Saint-Esprit jusqu'à la 'théosis'.",
                    patristicSource = "Saint Basile le Grand, 'Traité du Saint-Esprit', Chap. IX, 23",
                    patristicQuote = "« De même que les corps limpides et transparents, frappés par un rayon de lumière, deviennent eux-mêmes resplendissants, ainsi les âmes illuminées par l'Esprit deviennent spirituelles et transmettent la grâce aux autres. »",
                    audioNarrationScript = "Observez l'auréole dorée. Dans l'iconographie sacrée, l'or n'est pas une couleur terrestre, mais la présence même de la Lumière Incréée du Mont Thabor. Saint Basile nous rappelle que le croyant qui purifie son cœur devient semblable à un cristal transparent : il reçoit la lumière divine et la rayonne vers ses frères."
                )
                AppLanguage.ES -> hotspot
            }
            "hotspot_blessing_hand" -> when (lang) {
                AppLanguage.EN -> hotspot.copy(
                    title = "Hand in Byzantine Blessing Gesture (IC-XC)",
                    theologicalCategory = "Christology & Trinity",
                    shortBadge = "Trinitarian Blessing",
                    description = "The raised right hand with fingers carefully arranged to form the sacred monogram of Christ (Iesous Christos).",
                    theologicalExegesis = "The arrangement of the fingers is a compendium of Christian faith: the extended index finger (I) and curved middle finger (C) form 'IC'. The thumb and ring finger touch in a cross (X), while the curved little finger forms 'C', completing the Greek initials of JESUS CHRIST (IC-XC). Furthermore, the three upright fingers confess the Most Holy Trinity, and the two joined fingers confess the two natures of Christ: true God and true Man.",
                    patristicSource = "First Council of Nicaea (defended by Saint Basil)",
                    patristicQuote = "«I do not bless by my own authority, but in the Name of Him who conquered death and united divinity with our frail humanity.»",
                    audioNarrationScript = "Look closely at the fingers of Saint Basil's right hand. His fingers form the Greek letters Iota, Sigma, Chi, Sigma: the sacred Name of Jesus Christ, our Savior. At the same time, three fingers confess the Most Holy Trinity and two joined fingers proclaim that Jesus is true God and true Man."
                )
                AppLanguage.FR -> hotspot.copy(
                    title = "Main en Geste de Bénédiction Byzantine (IC-XC)",
                    theologicalCategory = "Christologie & Trinité",
                    shortBadge = "Bénédiction Trinitaire",
                    description = "La main droite levée avec les doigts soigneusement disposés pour former le monogramme sacré du Christ (Iesous Christos).",
                    theologicalExegesis = "La disposition des doigts est un abrégé de la foi chrétienne : l'index étendu (I) et le majeur courbé (C) forment 'IC'. Le pouce et l'annulaire se touchent en croix (X), tandis que l'auriculaire courbé forme 'C', complétant les initiales grecques de JÉSUS-CHRIST (IC-XC). En outre, les trois doigts dressés confessent la Très Sainte Trinité et les deux doigts unis confessent les deux natures du Christ.",
                    patristicSource = "Premier Concile de Nicée (défendu par Saint Basile)",
                    patristicQuote = "« Je ne bénis pas de ma propre autorité, mais au Nom de Celui qui a vaincu la mort et uni en Lui-même la divinité et notre fragile humanité. »",
                    audioNarrationScript = "Regardez attentivement les doigts de la main droite de Saint Basile. Ses doigts forment les lettres grecques Iota, Sigma, Chi, Sigma : le Nom sacré de Jésus-Christ. Lorsque l'évêque basilien bénit, c'est le Christ lui-même qui répand sa paix sur votre vie."
                )
                AppLanguage.ES -> hotspot
            }
            "hotspot_omophorion" -> when (lang) {
                AppLanguage.EN -> hotspot.copy(
                    title = "Episcopal Omophorion with Crosses Pattée",
                    theologicalCategory = "Pastoral Care of the Good Shepherd",
                    shortBadge = "The Good Shepherd",
                    description = "The wide band of pure white wool draped across the archbishop's chest and shoulders, embroidered with black and gold Greek crosses.",
                    theologicalExegesis = "The omophorion is the most sacred episcopal vestment of the Eastern Church. Woven from pure lamb's wool, it represents the lost sheep that the Good Shepherd tirelessly seeks out and carries upon his own shoulders back to the fold. Saint Basil embodied this symbol by personally rescuing the destitute, orphans, and lepers in the celebrated Basiliad.",
                    patristicSource = "Saint Isidore of Pelusium, 'Theological Epistles'",
                    patristicQuote = "«The bishop who wears the omophorion carries upon his shoulders the weakness and sorrows of all his people, ready to lay down his life for his sheep.»",
                    audioNarrationScript = "The white omophorion draped over Saint Basil's shoulders is woven from pure sheep's wool. It recalls the Gospel parable of the lost sheep: the Good Shepherd does not punish the stray sheep, but carries it upon his shoulders with infinite tenderness."
                )
                AppLanguage.FR -> hotspot.copy(
                    title = "Omophorion Épiscopal aux Croix Pattées",
                    theologicalCategory = "Pastorale du Bon Pasteur",
                    shortBadge = "Le Bon Pasteur",
                    description = "La large bande de laine blanche pure qui traverse la poitrine et les épaules de l'archevêque, brodée de croix grecques noires et dorées.",
                    theologicalExegesis = "L'omophorion est le vêtement épiscopal le plus sacré de l'Église orientale. Tissé de pure laine d'agneau, il représente la brebis perdue que le Bon Pasteur cherche sans relâche et porte sur ses propres épaules pour la ramener au bercail. Saint Basile a incarné ce symbole en secourant les démunis, les orphelins et les lépreux dans la célèbre Basiliade.",
                    patristicSource = "Saint Isidore de Péluse, 'Épîtres théologiques'",
                    patristicQuote = "« L'évêque qui porte l'omophorion porte sur ses épaules la faiblesse et les douleurs de tout son peuple, prêt à donner sa vie pour ses brebis. »",
                    audioNarrationScript = "L'omophorion blanc qui couvre les épaules de Saint Basile est tissé de pure laine de brebis. Il rappelle la parabole évangélique de la brebis égarée que le Bon Pasteur porte sur ses épaules avec une tendresse infinie."
                )
                AppLanguage.ES -> hotspot
            }
            "hotspot_gospel_book" -> when (lang) {
                AppLanguage.EN -> hotspot.copy(
                    title = "Book of the Holy Gospels (Golden Codex)",
                    theologicalCategory = "Living Word & Teaching Authority",
                    shortBadge = "Eternal Word",
                    description = "The heavy gold-bound codex set with rubies and emeralds that the saint holds firmly with his veiled left hand.",
                    theologicalExegesis = "The saint holds the Gospel with reverence, covering his hand with the fold of his liturgical vestment (velatio manuum) to signify that Sacred Scripture is a mystery to be approached with awe. The golden binding with four corner gems represents the four Evangelists (Matthew, Mark, Luke, and John) whose witness illuminates the four corners of the earth.",
                    patristicSource = "Saint Basil the Great, 'Moral Rules' 1, 1",
                    patristicQuote = "«Every word of Divine Scripture must be received not as human invention, but with the same reverence as if we heard God Himself speaking to us from heaven.»",
                    audioNarrationScript = "In his left hand, Saint Basil holds the jeweled Book of the Gospels. The four gems at the corners represent the four evangelists who carried the Good News to the four corners of the world. The book is held close to the heart to teach us that the Word of God must be lived in works of mercy."
                )
                AppLanguage.FR -> hotspot.copy(
                    title = "Livre des Saints Évangiles (Codex Doré)",
                    theologicalCategory = "Parole Vivante & Magistère",
                    shortBadge = "Parole Éternelle",
                    description = "Le lourd codex relié d'or et serti de rubis et d'émeraudes que le saint tient fermement de sa main gauche voilée par son manteau.",
                    theologicalExegesis = "Le saint tient l'Évangile avec révérence, couvrant sa main du pli de son vêtement liturgique (velatio manuum) pour indiquer que les Saintes Écritures sont un mystère sacré. La reliure dorée ornée de quatre gemmes aux coins représente les quatre Évangélistes (Matthieu, Marc, Luc et Jean) dont le témoignage illumine les quatre points cardinaux de la terre.",
                    patristicSource = "Saint Basile le Grand, 'Règles Morales' 1, 1",
                    patristicQuote = "« Toute parole de la Divine Écriture doit être reçue non comme une invention humaine, mais avec la même révérence que si nous écoutions Dieu Lui-même nous parler depuis les cieux. »",
                    audioNarrationScript = "Dans sa main gauche, Saint Basile tient le Livre des Évangiles orné de joyaux. Les quatre gemmes aux coins représentent les quatre évangélistes qui ont porté la Bonne Nouvelle aux quatre coins du monde."
                )
                AppLanguage.ES -> hotspot
            }
            "hotspot_ascetic_face" -> when (lang) {
                AppLanguage.EN -> hotspot.copy(
                    title = "Ascetic Countenance & Contemplative Brow",
                    theologicalCategory = "Interior Watchfulness (Nepsis)",
                    shortBadge = "Give Heed to Yourself",
                    description = "High, spacious forehead, almond-shaped eyes with a serene, penetrating gaze, cheekbones marked by fasting, and a long dark patriarchal beard with silver strands.",
                    theologicalExegesis = "Unlike worldly art that flatters the senses, the features of the Byzantine icon are purified of earthly passions. The enlarged eyes contemplate eternal realities. The small, closed mouth indicates contemplative silence and freedom from idle words. The broad brow denotes theological wisdom illumined by unceasing prayer and fasting.",
                    patristicSource = "Saint Basil the Great, 'Homily on Fasting' I, 10",
                    patristicQuote = "«Fasting begets prophets and strengthens the wise. Fasting gives wings to the prayer of the soul.»",
                    audioNarrationScript = "Gaze upon the face of Saint Basil: his broad forehead represents discernment and theological wisdom. His large, serene eyes reflect the purity of heart that contemplates God. His closed lips invite us to interior silence."
                )
                AppLanguage.FR -> hotspot.copy(
                    title = "Visage Ascétique et Front Contemplatif",
                    theologicalCategory = "Vigilance Intérieure (Nepsis)",
                    shortBadge = "Prends Garde à Toi-même",
                    description = "Front haut et spacieux, yeux en amande au regard serein et pénétrant, pommettes marquées par le jeûne et longue barbe patriarcale sombre mêlée d'argent.",
                    theologicalExegesis = "Contrairement à l'art profane, la physionomie de l'icône byzantine est purifiée des passions mondaines. Les grands yeux contemplent les réalités éternelles. La bouche petite et fermée indique le silence contemplatif. Le front large dénote la sagesse théologique illuminée par la prière incessante et le jeûne.",
                    patristicSource = "Saint Basile le Grand, 'Homélie sur le Jeûne' I, 10",
                    patristicQuote = "« Le jeûne engendre les prophètes et fortifie les sages. Le jeûne donne des ailes à la prière de l'âme. »",
                    audioNarrationScript = "Regardez le visage de Saint Basile : son front large représente le discernement et la science théologique. Ses grands yeux sereins reflètent la pureté du cœur qui contemple Dieu. Ses lèvres closes nous invitent au silence intérieur."
                )
                AppLanguage.ES -> hotspot
            }
            "hotspot_gold_ground" -> when (lang) {
                AppLanguage.EN -> hotspot.copy(
                    title = "Gold Leaf Background (The Eighth Day)",
                    theologicalCategory = "Eternity & Heavenly Kingdom",
                    shortBadge = "Divine Eternity",
                    description = "The continuous, radiant gold leaf background enveloping the entire composition without earthly horizon or cast shadows.",
                    theologicalExegesis = "In traditional Eastern iconography, no realistic three-dimensional backgrounds or shadows bind the figure to fleeting physical space. The pure gold ground represents the 'Eighth Day': consummated eternity where God is 'all in all' (1 Cor 15:28). The believer contemplating the icon is lifted into the heavenly liturgy.",
                    patristicSource = "Saint John of Damascus, 'On the Divine Images'",
                    patristicQuote = "«The icon is an open window onto eternity; through visible matter we are lifted up to invisible communion with God.»",
                    audioNarrationScript = "In this icon there are no shadows or earthly landscapes. Everything is surrounded by a radiant background of gold. Ancient master iconographers called this the 'Eighth Day', the time of eternity where there is no more night or sorrow, but the fullness of God's glory."
                )
                AppLanguage.FR -> hotspot.copy(
                    title = "Fond à la Feuille d'Or (Le Huitième Jour)",
                    theologicalCategory = "Éternité & Royaume Céleste",
                    shortBadge = "Éternité Divine",
                    description = "Le fond continu et rayonnant à la feuille d'or qui enveloppe toute la composition sans horizon terrestre ni ombres portées.",
                    theologicalExegesis = "Dans l'iconographie traditionnelle de l'Église orientale, le fond d'or pur représente le « Huitième Jour » : l'éternité accomplie où Dieu est « tout en tous » (1 Cor 15, 28). Le croyant qui contemple l'icône est transporté dans la liturgie céleste.",
                    patristicSource = "Saint Jean Damascène, 'Discours sur les Saintes Images'",
                    patristicQuote = "« L'icône est une fenêtre ouverte sur l'éternité ; à travers la matière visible, nous sommes élevés à la communion invisible avec Dieu. »",
                    audioNarrationScript = "Dans cette icône, il n'y a ni ombres ni paysages terrestres. Tout est entouré d'un fond rayonnant d'or. Les anciens maîtres iconographes appelaient cela le « Huitième Jour », le temps de l'éternité où resplendit la plénitude de la gloire de Dieu."
                )
                AppLanguage.ES -> hotspot
            }
            else -> hotspot
        }
    }

    // =========================================================================
    // 7. LEARNING PATHS LOCALIZATION (RutasAprendizajeData)
    // =========================================================================
    fun getLocalizedPerfilLabel(perfil: PerfilFormacion, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> when (perfil) {
            PerfilFormacion.ASPIRANTE -> "Vocational Inquirer"
            PerfilFormacion.ASOCIADO -> "Basilian Associate (S5)"
            PerfilFormacion.NOVICIO -> "Novice in Houston (S6)"
            PerfilFormacion.ESCOLASTICO -> "Scholastic in Theology"
            PerfilFormacion.ADULTO -> "Ongoing Formation"
        }
        AppLanguage.FR -> when (perfil) {
            PerfilFormacion.ASPIRANTE -> "Jeune en Discernement"
            PerfilFormacion.ASOCIADO -> "Associé Basilien (S5)"
            PerfilFormacion.NOVICIO -> "Novice à Houston (S6)"
            PerfilFormacion.ESCOLASTICO -> "Scolastique en Théologie"
            PerfilFormacion.ADULTO -> "Formation Permanente"
        }
        AppLanguage.ES -> perfil.label
    }

    fun getLocalizedPerfilDesc(perfil: PerfilFormacion, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> when (perfil) {
            PerfilFormacion.ASPIRANTE -> "First contact with the charism of Annonay"
            PerfilFormacion.ASOCIADO -> "Discernment and community life prior to the novitiate"
            PerfilFormacion.NOVICIO -> "Intensive canonical year of prayer, asceticism, and rule"
            PerfilFormacion.ESCOLASTICO -> "Philosophical-theological studies and pastoral mission"
            PerfilFormacion.ADULTO -> "Priests and committed lay associates in mission"
        }
        AppLanguage.FR -> when (perfil) {
            PerfilFormacion.ASPIRANTE -> "Premier contact avec le charisme d'Annonay"
            PerfilFormacion.ASOCIADO -> "Discernement et vie communautaire avant le noviciat"
            PerfilFormacion.NOVICIO -> "Année canonique intensive de prière, d'ascèse et de règle"
            PerfilFormacion.ESCOLASTICO -> "Études philosophiques et théologiques et mission pastorale"
            PerfilFormacion.ADULTO -> "Prêtres et laïcs engagés dans la mission"
        }
        AppLanguage.ES -> perfil.description
    }

    fun getLocalizedRitmoLabel(ritmo: RitmoNomada, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> when (ritmo) {
            RitmoNomada.AGIL -> "Light Nomad Pace"
            RitmoNomada.SERENO -> "Contemplative Walk"
            RitmoNomada.PROFUNDO -> "Desert Retreat"
        }
        AppLanguage.FR -> when (ritmo) {
            RitmoNomada.AGIL -> "Pas Léger Nomade"
            RitmoNomada.SERENO -> "Marche Contemplative"
            RitmoNomada.PROFUNDO -> "Retraite du Désert"
        }
        AppLanguage.ES -> ritmo.label
    }

    fun getLocalizedRitmoDesc(ritmo: RitmoNomada, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> when (ritmo) {
            RitmoNomada.AGIL -> "Brief capsules of ascetic wisdom for the day"
            RitmoNomada.SERENO -> "Unhurried reading, biblical analysis, and prayerful silence"
            RitmoNomada.PROFUNDO -> "Theological study, discernment, and meta-learning"
        }
        AppLanguage.FR -> when (ritmo) {
            RitmoNomada.AGIL -> "Brèves capsules de sagesse ascétique pour la journée"
            RitmoNomada.SERENO -> "Lecture posée, analyse biblique et silence priant"
            RitmoNomada.PROFUNDO -> "Étude théologique, discernement et méta-apprentissage"
        }
        AppLanguage.ES -> ritmo.description
    }

    fun getLocalizedEje(ejeEs: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> when (ejeEs) {
            "Todos" -> "All"
            "Psicología Espiritual" -> "Spiritual Psychology"
            "Virtudes Basilianas" -> "Basilian Virtues"
            "Ascética y Oración Nómada" -> "Asceticism & Nomad Prayer"
            else -> ejeEs
        }
        AppLanguage.FR -> when (ejeEs) {
            "Todos" -> "Tous"
            "Psicología Espiritual" -> "Psychologie Spirituelle"
            "Virtudes Basilianas" -> "Vertus Basiliennes"
            "Ascética y Oración Nómada" -> "Ascèse et Prière Nomade"
            else -> ejeEs
        }
        AppLanguage.ES -> ejeEs
    }

    fun localizeModulo(modulo: ModuloAprendizaje, lang: AppLanguage): ModuloAprendizaje {
        if (lang == AppLanguage.ES) return modulo
        val locEje = getLocalizedEje(modulo.eje, lang)
        return when (modulo.id) {
            "psi_1" -> when (lang) {
                AppLanguage.EN -> modulo.copy(
                    eje = locEje,
                    titulo = "Self-Knowledge and the Illusion of the Ego",
                    subtitulo = "Looking at the soul in the light of God's truth without masks",
                    citasFuente = "Saint Basil the Great: «Homily on 'Give Heed to Yourself'» (Deut 15:9)",
                    contenidoTeorico = "Saint Basil teaches that the greatest cause of spiritual downfall is not external enemies, but lack of watchfulness over one's own interior world. 'Give heed to yourself' is not self-centeredness, but compassionate awareness of our weaknesses, impulses, and hidden motivations. Basilian spiritual psychology unites God's grace with psychological maturity: we cannot consecrate to God what we insist on hiding or denying about ourselves.",
                    aplicacionPractica = "Spend 5 minutes before sleep examining which emotion dominated your day (fear, pride, joy, resentment) and offer it to Christ without making excuses.",
                    preguntasMetaAprendizaje = listOf(
                        "What uncomfortable truth about my reactions did I discover today in my community relationships?",
                        "In what moments did I act seeking the approval of others rather than the will of the Lord?",
                        "How can I integrate my human fragility with my vocation of service?"
                    )
                )
                AppLanguage.FR -> modulo.copy(
                    eje = locEje,
                    titulo = "Connaissance de Soi et l'Illusion de l'Ego",
                    subtitulo = "Regarder l'âme à la lumière de la vérité de Dieu sans masques",
                    citasFuente = "Saint Basile le Grand : « Homélie sur 'Prends garde à toi-même' » (Dt 15, 9)",
                    contenidoTeorico = "Saint Basile enseigne que la plus grande cause de chute spirituelle ne vient pas des ennemis extérieurs, mais du manque de vigilance sur son propre monde intérieur. « Prends garde à toi-même » n'est pas de l'égocentrisme, mais la prise de conscience compatissante de nos faiblesses, impulsions et motivations secrètes. La psychologie spirituelle basilienne unit la grâce de Dieu à la maturité psychologique.",
                    aplicacionPractica = "Consacrez 5 minutes avant de dormir à examiner quelle émotion a dominé votre journée (peur, orgueil, joie, ressentiment) et offrez-la au Christ sans vous justifier.",
                    preguntasMetaAprendizaje = listOf(
                        "Quelle vérité inconfortable sur mes réactions ai-je découverte aujourd'hui dans mes relations communautaires ?",
                        "À quels moments ai-je agi en cherchant l'approbation des autres plutôt que la volonté du Seigneur ?",
                        "Comment puis-je intégrer ma fragilité humaine à ma vocation de service ?"
                    )
                )
                AppLanguage.ES -> modulo
            }
            "psi_2" -> when (lang) {
                AppLanguage.EN -> modulo.copy(
                    eje = locEje,
                    titulo = "Affective Integration and Celibate Chastity",
                    subtitulo = "An undivided heart consecrated to the freedom to love (BWL #9-13)",
                    citasFuente = "Basilian Way of Life arts. 9-13; Benedict XVI Deus Caritas Est",
                    contenidoTeorico = "Consecrated chastity in the Basilian tradition is never sterile repression, but the fruitful channeling of affective love toward fraternity and the apostolate. Psychosexual maturity requires honesty about loneliness, the longing for intimacy, and ministerial boundaries. In community we find the fraternal love that heals individualism.",
                    aplicacionPractica = "Acknowledge your moments of vulnerability or fatigue today; instead of isolating yourself, share an honest conversation with a brother or spiritual director.",
                    preguntasMetaAprendizaje = listOf(
                        "How do I live my celibacy: as a painful absence or as an overflowing gift for service?",
                        "Have I maintained healthy, transparent boundaries in my daily interactions?",
                        "To whom do I turn when I experience loneliness or affective dryness?"
                    )
                )
                AppLanguage.FR -> modulo.copy(
                    eje = locEje,
                    titulo = "Intégration Affective et Chasteté Célibataire",
                    subtitulo = "Un cœur sans partage consacré à la liberté d'aimer (MVB #9-13)",
                    citasFuente = "Mode de Vie Basilien art. 9-13 ; Benoît XVI Deus Caritas Est",
                    contenidoTeorico = "La chasteté consacrée dans la tradition basilienne n'est jamais une répression stérile, mais une canalisation féconde de l'amour affectif vers la fraternité et l'apostolat. La maturité psychosexuelle exige l'honnêteté face à la solitude, au désir d'intimité et au respect des limites ministérielles.",
                    aplicacionPractica = "Reconnaissez aujourd'hui vos moments de vulnérabilité ou de fatigue ; au lieu de vous isoler, partagez un moment sincère avec un frère ou un accompagnateur spirituel.",
                    preguntasMetaAprendizaje = listOf(
                        "De quelle manière est-ce que je vis mon célibat : comme une absence douloureuse ou comme un don débordant pour servir ?",
                        "Ai-je maintenu des limites saines et transparentes dans mes interactions quotidiennes ?",
                        "Vers qui est-ce que je me tourne lorsque j'éprouve de la solitude ou une sécheresse affective ?"
                    )
                )
                AppLanguage.ES -> modulo
            }
            "virtud_1" -> when (lang) {
                AppLanguage.EN -> modulo.copy(
                    eje = locEje,
                    titulo = "Active Charity: The Rule of Rules",
                    subtitulo = "«If you live alone, whose feet will you wash?»",
                    citasFuente = "Saint Basil: Longer Rules (Question 7)",
                    contenidoTeorico = "Against solitary desert anchorites, Saint Basil passionately defended cenobitic, fraternal life. He boldly asked: 'If you live alone, whose servant will you be? Whose feet will you wash? Who will correct your faults?'. For the Basilian Fathers of 1822 and today, charity is not a theory: it is embodied in teaching needy youth, caring for the elderly, and sharing a common table.",
                    aplicacionPractica = "Perform a concrete, discreet act of service today toward the most difficult or overlooked person in your surroundings.",
                    preguntasMetaAprendizaje = listOf(
                        "How does my faith translate into tangible works of charity and patience toward those around me?",
                        "To what extent does the Basilian educational charism inspire the way I listen and teach?",
                        "What inner resistance do I experience when I must yield my own will for the good of the community?"
                    )
                )
                AppLanguage.FR -> modulo.copy(
                    eje = locEje,
                    titulo = "La Charité Active : La Règle des Règles",
                    subtitulo = "« Si tu vis seul, à qui laveras-tu les pieds ? »",
                    citasFuente = "Saint Basile : Grandes Règles (Question 7)",
                    contenidoTeorico = "Face aux anachorètes solitaires du désert, Saint Basile a défendu avec vigueur la vie cénobitique et fraternelle. Il demandait avec audace : « Si tu vis seul, de qui seras-tu le serviteur ? À qui laveras-tu les pieds ? ». Pour les Pères Basiliens de 1822 et d'aujourd'hui, la charité s'incarne dans l'enseignement aux jeunes défavorisés, l'aide aux personnes âgées et la table commune.",
                    aplicacionPractica = "Accomplissez aujourd'hui un acte de service concret et discret envers la personne la plus difficile ou oubliée de votre entourage.",
                    preguntasMetaAprendizaje = listOf(
                        "Comment ma foi se traduit-elle en œuvres tangibles de charité et de patience envers ceux qui m'entourent ?",
                        "Dans quelle mesure le charisme éducatif basilien inspire-t-il ma façon d'écouter et d'enseigner ?",
                        "Quelle résistance intérieure est-ce que j'éprouve lorsque je dois céder ma volonté pour le bien fraternel ?"
                    )
                )
                AppLanguage.ES -> modulo
            }
            "virtud_2" -> when (lang) {
                AppLanguage.EN -> modulo.copy(
                    eje = locEje,
                    titulo = "Evangelical Poverty and Community Sobriety",
                    subtitulo = "Holding necessities in common and giving the surplus to the poor",
                    citasFuente = "Rule of Saint Basil; Basilian Customs 2022 (Ch. Economic Life)",
                    contenidoTeorico = "In the Basiliad of Caesarea, Basilians founded the first hospital complex, pilgrim hospice, and technical school in Church history. For Basil, 'the bread you store away belongs to the hungry; the coat in your closet belongs to the naked'. The vow of poverty is a communion of goods and generous detachment from consumerism.",
                    aplicacionPractica = "Review your belongings and electronic devices today; set aside something useful to donate or moderate the time spent on comforts.",
                    preguntasMetaAprendizaje = listOf(
                        "Does my lifestyle reflect the simplicity and sobriety of the Gospel?",
                        "Do I freely share my resources, talents, and time with others?",
                        "How can I avoid falling into attachment to material comfort?"
                    )
                )
                AppLanguage.FR -> modulo.copy(
                    eje = locEje,
                    titulo = "Pauvreté Évangélique et Sobriété Communautaire",
                    subtitulo = "Avoir en commun le nécessaire et donner le superflu aux pauvres",
                    citasFuente = "Règle de Saint Basile ; Coutumes Basiliennes 2022 (Chap. Vie Économique)",
                    contenidoTeorico = "Dans la Basiliade de Césarée, Saint Basile a fondé le premier complexe hospitalier, hospice de pèlerins et école de métiers de l'histoire de l'Église. Pour Basile, « le pain que tu gardes appartient à l'affamé ; le manteau dans ton armoire, à celui qui est nu ». Le vœu de pauvreté est communion des biens et détachement généreux du consumérisme.",
                    aplicacionPractica = "Examinez vos biens et appareils électroniques ; détachez-vous de quelque chose d'utile pour le donner ou modérez le temps consacré au confort.",
                    preguntasMetaAprendizaje = listOf(
                        "Mon style de vie reflète-t-il la sobriété et la simplicité de l'Évangile ?",
                        "Est-ce que je partage librement mes ressources, mes talents et mon temps avec les autres ?",
                        "Comment puis-je éviter de tomber dans l'attachement au confort matériel ?"
                    )
                )
                AppLanguage.ES -> modulo
            }
            "ascetica_1" -> when (lang) {
                AppLanguage.EN -> modulo.copy(
                    eje = locEje,
                    titulo = "Nomad Pedagogy: The Way and the Interior Desert",
                    subtitulo = "Learning on the road, dwelling in God's presence at every latitude",
                    citasFuente = "Saint Basil: Letter II to Saint Gregory Nazianzen",
                    contenidoTeorico = "Nomad pedagogy recognizes that the Christian is a pilgrim in continual conversion. We do not cling to rigid structures: we move docilely at the breath of the Holy Spirit. Like the ten founding priests of Annonay in 1822 who gathered in secret and later sent missionaries to Canada, the United States, Colombia, and Mexico, the Basilian soul is missionary and itinerant.",
                    aplicacionPractica = "Walk for 15 minutes in silence today contemplating nature, invoking the name of Jesus with every step.",
                    preguntasMetaAprendizaje = listOf(
                        "What attachments keep me from being truly available for the mission to which God calls me?",
                        "Am I able to pray and maintain interior recollection even amid daily busyness?",
                        "What does it mean for me to be a 'nomad of the Gospel' in today's society?"
                    )
                )
                AppLanguage.FR -> modulo.copy(
                    eje = locEje,
                    titulo = "Pédagogie Nomade : Le Chemin et le Désert Intérieur",
                    subtitulo = "Apprendre en chemin, habiter la présence de Dieu sous toute latitude",
                    citasFuente = "Saint Basile : Lettre II à Saint Grégoire de Nazianze",
                    contenidoTeorico = "La pédagogie nomade reconnaît que le chrétien est un pèlerin en conversion continuelle. Nous nous mouvons docilement au souffle de l'Esprit Saint. Comme les dix prêtres fondateurs d'Annonay en 1822 qui ont envoyé des missionnaires au Canada, aux États-Unis, en Colombie et au Mexique, l'âme basilienne est missionnaire et itinérante.",
                    aplicacionPractica = "Marchez 15 minutes en silence aujourd'hui en contemplant la nature et en invoquant le nom de Jésus à chaque pas.",
                    preguntasMetaAprendizaje = listOf(
                        "Quels attachements m'empêchent d'être vraiment disponible pour la mission à laquelle Dieu m'appelle ?",
                        "Suis-je capable de prier et de garder le recueillement intérieur même au milieu de l'agitation ?",
                        "Que signifie pour moi être un « nomade de l'Évangile » dans la société actuelle ?"
                    )
                )
                AppLanguage.ES -> modulo
            }
            "ascetica_2" -> when (lang) {
                AppLanguage.EN -> modulo.copy(
                    eje = locEje,
                    titulo = "The Combat Against Acedia and Discouragement",
                    subtitulo = "Fidelity in spiritual dryness and the gift of perseverance",
                    citasFuente = "Saint Basil: Shorter Rules; Saint John Climacus & Ascetic Tradition",
                    contenidoTeorico = "Acedia is the inner weariness that whispers to the religious or formator that their dedication bears no fruit, that prayer is useless, and that consecrated life is routine. Saint Basil taught his monks to combat this weariness with the faithful rhythm of the Divine Office, manual or intellectual work well done, and honest openness with one's superior or confessor.",
                    aplicacionPractica = "Whenever you feel spiritual weariness or discouragement today, kneel for 3 minutes and repeat devoutly: «Lord Jesus Christ, Son of the living God, have mercy on me, a sinner.»",
                    preguntasMetaAprendizaje = listOf(
                        "What are the moments of the week when discouragement or spiritual fatigue assails me?",
                        "How do I respond to the temptation to abandon prayer when I feel no consolation?",
                        "What meta-learning strategies do I use to renew my missionary enthusiasm?"
                    )
                )
                AppLanguage.FR -> modulo.copy(
                    eje = locEje,
                    titulo = "Le Combat contre l'Acédie et le Découragement",
                    subtitulo = "La fidélité dans l'aridité spirituelle et le don de la persévérance",
                    citasFuente = "Saint Basile : Petites Règles ; Saint Jean Climaque et la tradition ascétique",
                    contenidoTeorico = "L'acédie est la lassitude intérieure qui murmure au religieux ou au formateur que son don ne porte pas de fruit et que la prière est inutile. Saint Basile enseignait à combattre cet ennui par le rythme fidèle de l'Office Divin, le travail manuel ou intellectuel bien accompli et l'ouverture sincère au supérieur ou confesseur.",
                    aplicacionPractica = "Lorsque vous ressentez de la paresse spirituelle ou du découragement aujourd'hui, mettez-vous à genoux pendant 3 minutes et répétez avec dévotion : « Seigneur Jésus-Christ, Fils du Dieu vivant, prends pitié de moi, pécheur. »",
                    preguntasMetaAprendizaje = listOf(
                        "Quels sont les moments de la semaine où le découragement ou la fatigue spirituelle m'assaille ?",
                        "Comment est-ce que je réponds à la tentation d'abandonner la prière lorsque je ne ressens aucune consolation ?",
                        "Quelles stratégies de méta-apprentissage est-ce que j'utilise pour renouveler mon enthousiasme missionnaire ?"
                    )
                )
                AppLanguage.ES -> modulo
            }
            else -> modulo
        }
    }

    // =========================================================================
    // 8. UNIVERSAL THEOLOGICAL & DOCUMENT TEXT TRANSLATOR
    // =========================================================================
    fun translateCommonTheologicalText(text: String, lang: AppLanguage): String {
        if (lang == AppLanguage.ES || text.isBlank()) return text
        var res = localizeDateString(text, lang)
        if (lang == AppLanguage.EN) {
            val replacements = listOf(
                "Congregación de Sacerdotes de San Basilio" to "Congregation of Priests of Saint Basil",
                "Congregación de San Basilio" to "Congregation of Saint Basil",
                "Padres Basilianos" to "Basilian Fathers",
                "La Forma de Vida Basiliana" to "The Basilian Way of Life",
                "Forma de Vida Basiliana" to "Basilian Way of Life",
                "Costumbres Basilianas" to "Basilian Customs",
                "San Basilio Magno" to "Saint Basil the Great",
                "San Basilio" to "Saint Basil",
                "San Gregorio Nacianceno" to "Saint Gregory Nazianzen",
                "Santo Tomás de Aquino" to "Saint Thomas Aquinas",
                "San Juan Bosco" to "Saint John Bosco",
                "San Francisco de Asís" to "Saint Francis of Assisi",
                "San José" to "Saint Joseph",
                "Santísima Virgen María" to "Blessed Virgin Mary",
                "Virgen María" to "Virgin Mary",
                "Nuestra Señora de la Asunción" to "Our Lady of the Assumption",
                "Nuestra Señora del Rosario de Chiquinquirá" to "Our Lady of the Rosary of Chiquinquirá",
                "Espíritu Santo" to "Holy Spirit",
                "Jesucristo" to "Jesus Christ",
                "Nuestro Señor" to "Our Lord",
                "Santa Sede" to "Holy See",
                "Concilio Vaticano II" to "Second Vatican Council",
                "Capítulo General" to "General Chapter",
                "Consejo General" to "General Council",
                "Superior General" to "Superior General",
                "Vicario General" to "Vicar General",
                "Curia Generalicia" to "General Curia",
                "Maestro de Novicios" to "Master of Novices",
                "Director de Escolásticos" to "Director of Scholastics",
                "Obispo y Doctor de la Iglesia" to "Bishop and Doctor of the Church",
                "Obispos y Doctores de la Iglesia" to "Bishops and Doctors of the Church",
                "Presbítero y Doctor de la Iglesia" to "Priest and Doctor of the Iglesia",
                "Virgen y Doctora de la Iglesia" to "Virgin and Doctor of the Church",
                "Doctor de la Iglesia" to "Doctor of the Church",
                "Presbítero" to "Priest",
                "Sacerdotes" to "Priests",
                "Sacerdote" to "Priest",
                "Escolásticos" to "Scholastics",
                "Escolástico" to "Scholastic",
                "Novicios" to "Novices",
                "Novicio" to "Novice",
                "Noviciado" to "Novitiate",
                "Parroquia" to "Parish",
                "Comunidad" to "Community",
                "Canadá" to "Canada",
                "Estados Unidos" to "United States",
                "México" to "Mexico",
                "Francia" to "France",
                "Bogotá" to "Bogota",
                "Medellín" to "Medellin",
                "vida en común" to "life in common",
                "vida comunitaria" to "community life",
                "pobreza evangélica" to "evangelical poverty",
                "castidad consagrada" to "consecrated chastity",
                "obediencia religiosa" to "religious obedience",
                "educación cristiana" to "Christian education",
                "Derechos Humanos" to "Human Rights",
                "Mártir" to "Martyr",
                "Mártires" to "Martyrs",
                "Patrono" to "Patron",
                "Patrona" to "Patroness",
                "Solemnidad" to "Solemnity",
                "Memoria" to "Memorial",
                "Fiesta" to "Feast",
                "Dios nuestro" to "O God",
                "Dios todopoderoso y eterno" to "Almighty and eternal God",
                "Dios todopoderoso" to "Almighty God",
                "Señor Dios" to "Lord God",
                "Por Jesucristo nuestro Señor" to "Through Jesus Christ our Lord",
                "Por Cristo nuestro Señor" to "Through Christ our Lord",
                "concédenos" to "grant us",
                "intercesión" to "intercession",
                "Amén" to "Amen"
            )
            for ((es, en) in replacements) {
                res = res.replace(es, en)
            }
        } else if (lang == AppLanguage.FR) {
            val replacements = listOf(
                "Congregación de Sacerdotes de San Basilio" to "Congrégation des Prêtres de Saint-Basile",
                "Congregación de San Basilio" to "Congrégation de Saint-Basile",
                "Padres Basilianos" to "Pères Basiliens",
                "La Forma de Vida Basiliana" to "Le Mode de Vie Basilien",
                "Forma de Vida Basiliana" to "Mode de Vie Basilien",
                "Costumbres Basilianas" to "Coutumes Basiliennes",
                "San Basilio Magno" to "Saint Basile le Grand",
                "San Basilio" to "Saint Basile",
                "San Gregorio Nacianceno" to "Saint Grégoire de Nazianze",
                "Santo Tomás de Aquino" to "Saint Thomas d'Aquin",
                "San Juan Bosco" to "Saint Jean Bosco",
                "San Francisco de Asís" to "Saint François d'Assise",
                "San José" to "Saint Joseph",
                "Santísima Virgen María" to "Très Sainte Vierge Marie",
                "Virgen María" to "Vierge Marie",
                "Nuestra Señora de la Asunción" to "Notre-Dame de l'Assomption",
                "Nuestra Señora del Rosario de Chiquinquirá" to "Notre-Dame du Rosaire de Chiquinquirá",
                "Espíritu Santo" to "Esprit Saint",
                "Jesucristo" to "Jésus-Christ",
                "Nuestro Señor" to "Notre Seigneur",
                "Santa Sede" to "Saint-Siège",
                "Concilio Vaticano II" to "Concile Vatican II",
                "Capítulo General" to "Chapitre Général",
                "Consejo General" to "Conseil Général",
                "Superior General" to "Supérieur Général",
                "Vicario General" to "Vicaire Général",
                "Curia Generalicia" to "Curie Générale",
                "Maestro de Novicios" to "Maître des Novices",
                "Director de Escolásticos" to "Directeur des Scolastiques",
                "Obispo y Doctor de la Iglesia" to "Évêque et Docteur de l'Église",
                "Obispos y Doctores de la Iglesia" to "Évêques et Docteurs de l'Église",
                "Presbítero y Doctor de la Iglesia" to "Prêtre et Docteur de l'Église",
                "Virgen y Doctora de la Iglesia" to "Vierge et Docteur de l'Église",
                "Doctor de la Iglesia" to "Docteur de l'Église",
                "Presbítero" to "Prêtre",
                "Sacerdotes" to "Prêtres",
                "Sacerdote" to "Prêtre",
                "Escolásticos" to "Scolastiques",
                "Escolástico" to "Scolastique",
                "Novicios" to "Novices",
                "Novicio" to "Novice",
                "Noviciado" to "Noviciat",
                "Parroquia" to "Paroisse",
                "Comunidad" to "Communauté",
                "Canadá" to "Canada",
                "Estados Unidos" to "États-Unis",
                "México" to "Mexique",
                "Francia" to "France",
                "Colombia" to "Colombie",
                "Bogotá" to "Bogota",
                "Medellín" to "Medellin",
                "vida en común" to "vie en commun",
                "vida comunitaria" to "vie communautaire",
                "pobreza evangélica" to "pauvreté évangélique",
                "castidad consagrada" to "chasteté consacrée",
                "obediencia religiosa" to "obéissance religieuse",
                "educación cristiana" to "éducation chrétienne",
                "Derechos Humanos" to "Droits Humains",
                "Mártir" to "Martyr",
                "Mártires" to "Martyrs",
                "Patrono" to "Patron",
                "Patrona" to "Patronne",
                "Solemnidad" to "Solennité",
                "Memoria" to "Mémoire",
                "Fiesta" to "Fête",
                "Dios nuestro" to "Dieu notre Père",
                "Dios todopoderoso y eterno" to "Dieu tout-puissant et éternel",
                "Dios todopoderoso" to "Dieu tout-puissant",
                "Señor Dios" to "Seigneur Dieu",
                "Por Jesucristo nuestro Señor" to "Par Jésus-Christ notre Seigneur",
                "Por Cristo nuestro Señor" to "Par le Christ notre Seigneur",
                "concédenos" to "accorde-nous",
                "intercesión" to "intercession",
                "Amén" to "Amen"
            )
            for ((es, fr) in replacements) {
                res = res.replace(es, fr)
            }
        }
        return res
    }
}
