package com.example.data.repository

import com.example.data.model.AppLanguage
import com.example.data.model.BasilianCalendarDate
import com.example.data.model.DailyInspiration
import com.example.data.model.Founder
import com.example.data.model.PatronSaint
import com.example.data.model.PrayerItem

/**
 * Universal content localization engine for the Basilian app.
 * Translates Daily Inspirations, Official Prayers, Saint Basil Quotes,
 * and Fraternal Pastoral Accompaniments across Spanish, English, and French.
 */
object BasilianContentLocalization {

    fun getLocalizedSaintBasilQuote(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "A good deed is never lost: he who sows courtesy reaps friendship, and he who plants kindness gathers love."
        AppLanguage.FR -> "Un acte de bonté n'est jamais perdu : qui sème la courtoisie récolte l'amitié, et qui plante la bienveillance recueille l'amour."
        AppLanguage.ES -> "Un acto de bondad nunca se pierde: los que siembran cortesía cosechan amistades, y los que plantan cordialidad recogen amor."
    }

    private data class InspirationTranslation(
        val scriptureTextEn: String,
        val scriptureTextFr: String,
        val mottoFocusEn: String,
        val mottoFocusFr: String,
        val wisdomEn: String,
        val wisdomFr: String,
        val actionEn: String,
        val actionFr: String
    )

    private val inspirationTranslations: Map<Int, InspirationTranslation> = mapOf(
        1 to InspirationTranslation(
            scriptureTextEn = "Teach me good judgment and knowledge, for I believe in your commandments.",
            scriptureTextFr = "Enseigne-moi le bon jugement et la sagesse, car j'ai foi en tes commandements.",
            mottoFocusEn = "Goodness (Bonitatem)",
            mottoFocusFr = "Bonté (Bonitatem)",
            wisdomEn = "A tree is known by its fruit; a man by his deeds. A good deed is never lost.",
            wisdomFr = "Un arbre se reconnaît à ses fruits, un homme à ses actes. Un geste de bonté n'est jamais perdu.",
            actionEn = "Do a discreet kindness or say a warm word to the person you least expect today.",
            actionFr = "Faites une délicatesse discrète ou ayez une parole bienveillante envers la personne la plus inattendue aujourd'hui."
        ),
        2 to InspirationTranslation(
            scriptureTextEn = "I consider everything a loss because of the surpassing worth of knowing Christ Jesus my Lord.",
            scriptureTextFr = "Je considère tout comme une perte à cause de l'excellence de la connaissance du Christ Jésus mon Seigneur.",
            mottoFocusEn = "Knowledge (Scientiam)",
            mottoFocusFr = "Science (Scientiam)",
            wisdomEn = "Just as bees draw nectar from the finest flowers, so we must draw from learning everything that builds the soul in truth.",
            wisdomFr = "De même que les abeilles extraient le nectar des plus belles fleurs, nous devons tirer de l'étude tout ce qui édifie l'âme dans la vérité.",
            actionEn = "Set aside 15 minutes today to read a text that nourishes your intellect and deepens your faith.",
            actionFr = "Consacrez 15 minutes aujourd'hui à lire un texte qui nourrit votre esprit et approfondit votre foi."
        ),
        3 to InspirationTranslation(
            scriptureTextEn = "Be devoted to one another in love. Honor one another above yourselves.",
            scriptureTextFr = "Aimez-vous tendrement les uns les autres ; que chacun estime les autres supérieurs à soi-même.",
            mottoFocusEn = "Discipline (Disciplinam)",
            mottoFocusFr = "Discipline (Disciplinam)",
            wisdomEn = "Inner order begets outward peace. Whoever masters their impulses is free to serve God.",
            wisdomFr = "L'ordre intérieur engendre la paix extérieure. Qui sait maîtriser ses impulsions est libre pour servir Dieu.",
            actionEn = "Complete your first duty of the day punctually as a quiet offering to God.",
            actionFr = "Accomplissez ponctuellement votre premier devoir de la journée comme une offrande silencieuse à Dieu."
        ),
        4 to InspirationTranslation(
            scriptureTextEn = "Truly I tell you, whatever you did for one of the least of these brothers and sisters of mine, you did for me.",
            scriptureTextFr = "En vérité je vous le dis, chaque fois que vous l'avez fait à l'un de ces plus petits de mes frères, c'est à moi que vous l'avez fait.",
            mottoFocusEn = "Goodness (Bonitatem)",
            mottoFocusFr = "Bonté (Bonitatem)",
            wisdomEn = "The bread in your cupboard belongs to the hungry; the coat hanging in your closet belongs to the naked.",
            wisdomFr = "Le pain que vous gardez appartient à celui qui a faim ; le manteau suspendu dans votre armoire appartient à celui qui est nu.",
            actionEn = "Look in your pantry or wardrobe for something you don't use and give it to someone in need.",
            actionFr = "Regardez dans votre garde-manger ou penderie quelque chose que vous n'utilisez pas et donnez-le à quelqu'un dans le besoin."
        ),
        5 to InspirationTranslation(
            scriptureTextEn = "I no longer call you servants, but friends, for everything that I learned from my Father I have made known to you.",
            scriptureTextFr = "Je ne vous appelle plus serviteurs, mais amis, car tout ce que j'ai appris de mon Père, je vous l'ai fait connaître.",
            mottoFocusEn = "Goodness (Bonitatem)",
            mottoFocusFr = "Bonté (Bonitatem)",
            wisdomEn = "Christian friendship reflects the steadfast love of God; it strengthens our path toward holiness.",
            wisdomFr = "L'amitié chrétienne est le reflet de l'amour inaltérable de Dieu ; elle fortifie le chemin de la sainteté.",
            actionEn = "Send a message or call a friend to sincerely thank them for their presence in your life.",
            actionFr = "Écrivez ou appelez un ami pour le remercier sincèrement de sa présence dans votre vie."
        ),
        6 to InspirationTranslation(
            scriptureTextEn = "Love is patient, love is kind. It does not envy, it does not boast, it is not proud.",
            scriptureTextFr = "L'amour est patient, l'amour est plein de bonté ; l'amour n'est pas envieux, ne se vante pas, ne s'enfle pas d'orgueil.",
            mottoFocusEn = "Discipline (Disciplinam)",
            mottoFocusFr = "Discipline (Disciplinam)",
            wisdomEn = "Our community life must be a model of genuine reconciliation, overcoming all individualism.",
            wisdomFr = "Notre vie fraternelle doit être un modèle de véritable réconciliation, dépassant tout individualisme.",
            actionEn = "When encountering friction or misunderstanding today, respond with calm and let go of having the last word.",
            actionFr = "Face à un malentendu aujourd'hui, répondez avec sérénité et renoncez à avoir le dernier mot."
        ),
        7 to InspirationTranslation(
            scriptureTextEn = "Martha, Martha, you are worried and upset about many things, but few things are needed—or indeed only one.",
            scriptureTextFr = "Marthe, Marthe, tu t'inquiètes et tu t'agites pour beaucoup de choses, mais une seule chose est nécessaire.",
            mottoFocusEn = "Knowledge (Scientiam)",
            mottoFocusFr = "Science (Scientiam)",
            wisdomEn = "Silence is the beginning of the soul's purification. In quiet stillness, the soul contemplates God.",
            wisdomFr = "Le silence est le commencement de la purification de l'âme. Dans le calme, l'âme contemple Dieu.",
            actionEn = "Turn off screens 20 minutes before bedtime and dedicate that time to quiet gratitude and stillness.",
            actionFr = "Éteignez vos écrans 20 minutes avant de dormir et consacrez ce temps au recueillement et à la gratitude."
        ),
        8 to InspirationTranslation(
            scriptureTextEn = "He has shown you, O mortal, what is good. What does the Lord require of you? To act justly, love mercy, and walk humbly with your God.",
            scriptureTextFr = "On t'a fait connaître ce qui est bien et ce que le Seigneur demande de toi : pratiquer la justice, aimer la miséricorde et marcher humblement avec ton Dieu.",
            mottoFocusEn = "Goodness (Bonitatem)",
            mottoFocusFr = "Bonté (Bonitatem)",
            wisdomEn = "Do not excuse your lack of charity by saying you have not enough. Whoever shares their little with purity of heart enriches all humanity.",
            wisdomFr = "N'excusez pas votre manque de charité en disant que vous n'avez pas assez. Partager le peu avec un cœur pur enrichit le monde entier.",
            actionEn = "Practice quiet generosity: give up your seat or offer a helping hand without seeking praise.",
            actionFr = "Pratiquez la générosité discrète : cédez votre place ou aidez quelqu'un sans attendre de compliments."
        ),
        9 to InspirationTranslation(
            scriptureTextEn = "The beginning of wisdom is this: Get wisdom. Though it cost all you have, get understanding.",
            scriptureTextFr = "Voici le commencement de la sagesse : Acquiers la sagesse, et avec tout ce que tu possèdes acquiers l'intelligence.",
            mottoFocusEn = "Knowledge (Scientiam)",
            mottoFocusFr = "Science (Scientiam)",
            wisdomEn = "Learning without charity puffs up; but knowledge enlightened by the Gospel builds up communities and consoles the downtrodden.",
            wisdomFr = "Le savoir sans amour enfle le cœur ; mais la science éclairée par l'Évangile édifie les communautés et console les humbles.",
            actionEn = "Learn something new today and share it with patient, welcoming pedagogical warmth.",
            actionFr = "Apprenez quelque chose de nouveau aujourd'hui et partagez-le avec patience et clarté fraternelle."
        ),
        10 to InspirationTranslation(
            scriptureTextEn = "Let us not become weary in doing good, for at the proper time we will reap a harvest if we do not give up.",
            scriptureTextFr = "Ne nous lassons pas de faire le bien ; car nous moissonnerons au temps convenable, si nous ne nous relâchons pas.",
            mottoFocusEn = "Discipline (Disciplinam)",
            mottoFocusFr = "Discipline (Disciplinam)",
            wisdomEn = "The helmsman proves skill in the storm, and the Christian demonstrates fidelity in daily perseverance.",
            wisdomFr = "Le pilote fait preuve de son art dans la tempête, et le chrétien prouve sa foi dans la persévérance quotidienne.",
            actionEn = "Finish that pending task today that you have been putting off.",
            actionFr = "Terminez aujourd'hui ce devoir en attente que vous aviez reporté."
        ),
        11 to InspirationTranslation(
            scriptureTextEn = "In the same way, faith by itself, if it is not accompanied by action, is dead.",
            scriptureTextFr = "Il en est ainsi de la foi : si elle n'a pas les œuvres, elle est morte en elle-même.",
            mottoFocusEn = "Goodness (Bonitatem)",
            mottoFocusFr = "Bonté (Bonitatem)",
            wisdomEn = "In the Basiliad we learned that binding a wound and breaking bread is the liturgy most pleasing in God's eyes.",
            wisdomFr = "À la Basiliade nous avons appris que soigner une blessure et partager le pain est la liturgie la plus agréable à Dieu.",
            actionEn = "Spend time listening deeply to someone going through sadness, loneliness, or distress.",
            actionFr = "Consacrez un moment à écouter attentivement une personne qui traverse la solitude ou l'épreuve."
        ),
        12 to InspirationTranslation(
            scriptureTextEn = "Clothe yourselves with compassion, kindness, humility, gentleness and patience.",
            scriptureTextFr = "Revêtez-vous d'entrailles de miséricorde, de bonté, d'humilité, de douceur, de patience.",
            mottoFocusEn = "Discipline (Disciplinam)",
            mottoFocusFr = "Discipline (Disciplinam)",
            wisdomEn = "True greatness of soul consists in refusing to be provoked by insults and answering pride with gentleness.",
            wisdomFr = "La vraie grandeur d'âme consiste à ne point s'irriter de l'injure et à répondre à l'arrogance par la douceur.",
            actionEn = "If tempted to judge or criticize today, replace the complaint with a quiet prayer for that person.",
            actionFr = "Si vous ressentez l'envie de juger quelqu'un aujourd'hui, remplacez la critique par une prière silencieuse pour lui."
        ),
        13 to InspirationTranslation(
            scriptureTextEn = "Then you will know the truth, and the truth will set you free.",
            scriptureTextFr = "Vous connaîtrez la vérité, et la vérité vous affranchira.",
            mottoFocusEn = "Knowledge (Scientiam)",
            mottoFocusFr = "Science (Scientiam)",
            wisdomEn = "He who loves truth does not fear the light. In the truth of Christ rest all longings of the human heart.",
            wisdomFr = "Celui qui aime la vérité ne craint pas la lumière. Dans la vérité du Christ reposent toutes les aspirations de l'homme.",
            actionEn = "Be completely transparent, truthful, and sincere in every word you speak today.",
            actionFr = "Soyez pleinement authentique, véridique et sincère dans chaque parole prononcée aujourd'hui."
        ),
        14 to InspirationTranslation(
            scriptureTextEn = "Blessed are the peacemakers, for they will be called children of God.",
            scriptureTextFr = "Heureux ceux qui procurent la paix, car ils seront appelés fils de Dieu.",
            mottoFocusEn = "Goodness (Bonitatem)",
            mottoFocusFr = "Bonté (Bonitatem)",
            wisdomEn = "Peace is not merely absence of conflict; it is the living harmony of charity and mutual respect.",
            wisdomFr = "La paix n'est pas simplement l'absence de conflit ; elle est l'harmonie vivante de l'amour fraternel.",
            actionEn = "Be an instrument of reconciliation today where there has been tension.",
            actionFr = "Soyez un artisan de paix et d'apaisement là où règne la tension aujourd'hui."
        )
    )

    fun getLocalizedDailyInspiration(inspiration: DailyInspiration, lang: AppLanguage): DailyInspiration {
        if (lang == AppLanguage.ES) return inspiration
        val t = inspirationTranslations[inspiration.dayOfYear] ?: return inspiration
        return when (lang) {
            AppLanguage.EN -> inspiration.copy(
                scriptureText = t.scriptureTextEn,
                basilianMottoFocus = t.mottoFocusEn,
                stBasilWisdom = t.wisdomEn,
                practicalActionForToday = t.actionEn
            )
            AppLanguage.FR -> inspiration.copy(
                scriptureText = t.scriptureTextFr,
                basilianMottoFocus = t.mottoFocusFr,
                stBasilWisdom = t.wisdomFr,
                practicalActionForToday = t.actionFr
            )
            AppLanguage.ES -> inspiration
        }
    }

    private data class PrayerTranslation(
        val titleEn: String,
        val titleFr: String,
        val subtitleEn: String,
        val subtitleFr: String,
        val timeOfDayEn: String,
        val timeOfDayFr: String,
        val prayerTextEn: String,
        val prayerTextFr: String,
        val reflectionEn: String,
        val reflectionFr: String
    )

    private val prayerTranslations: Map<String, PrayerTranslation> = mapOf(
        "laudes" to PrayerTranslation(
            titleEn = "Lauds (Morning Prayer)",
            titleFr = "Laudes (Prière du Matin)",
            subtitleEn = "Upon waking: offering the day to the Creator",
            subtitleFr = "Au réveil : offrande de la journée au Créateur",
            timeOfDayEn = "Morning",
            timeOfDayFr = "Matin",
            prayerTextEn = "Lord, open my lips, and my mouth shall proclaim your praise.\n\n" +
                    "Blessed are you, Lord, God of our fathers, who dispel the darkness of night and grant us to see the light of a new day. We consecrate to you all our deeds, thoughts, and words today.\n\n" +
                    "Following the example of our holy father Basil, may we live this day rooted in Goodness toward our brothers and sisters, in the Discipline of our duties, and in the Knowledge of your divine truth. Send forth your Holy Spirit that we may be a healing, peaceful presence to everyone we meet.\n\n" +
                    "Glory to the Father, and to the Son, and to the Holy Spirit. As it was in the beginning, is now, and ever shall be, world without end. Amen.",
            prayerTextFr = "Seigneur, ouvre mes lèvres, et ma bouche publiera ta louange.\n\n" +
                    "Béni sois-tu, Seigneur, Dieu de nos pères, qui dissipes les ténèbres de la nuit et nous accordes de voir la clarté d'un jour nouveau. Nous te consacrons nos œuvres, nos pensées et nos paroles de ce jour.\n\n" +
                    "À l'exemple de notre saint père Basile, puissions-nous vivre cette journée enracinés dans la Bonté fraternelle, dans la Discipline de nos devoirs et dans la Science de ta vérité. Envoie ton Esprit Saint pour que nous soyons une présence de paix et d'espérance pour tous ceux que nous rencontrerons.\n\n" +
                    "Gloire au Père, et au Fils, et au Saint-Esprit. Comme il était au commencement, maintenant et toujours, dans les siècles des siècles. Amen.",
            reflectionEn = "Lauds consecrate the first hours and vitality of the day to the service of God's Kingdom.",
            reflectionFr = "Les Laudes consacrent les premières énergies et la fraîcheur du jour au service du Royaume de Dieu."
        ),
        "angelus" to PrayerTranslation(
            titleEn = "The Angelus & Midday Prayer",
            titleFr = "L'Angélus & Prière du Midi",
            subtitleEn = "At midday: memory of the Incarnation and devotion to Saint Basil",
            subtitleFr = "À midi : mémoire de l'Incarnation et dévotion à Saint Basile",
            timeOfDayEn = "Midday",
            timeOfDayFr = "Midi",
            prayerTextEn = "V. The Angel of the Lord declared unto Mary.\nR. And she conceived of the Holy Spirit.\n(Hail Mary...)\n\n" +
                    "V. Behold the handmaid of the Lord.\nR. Be it done unto me according to thy word.\n(Hail Mary...)\n\n" +
                    "V. And the Word was made flesh.\nR. And dwelt among us.\n(Hail Mary...)\n\n" +
                    "V. Pray for us, O Holy Mother of God.\nR. That we may be made worthy of the promises of Christ.\n\n" +
                    "Let us pray: Pour forth, we beseech thee, O Lord, thy grace into our hearts, that we to whom the Incarnation of Christ thy Son was made known by the message of an Angel, may by his Passion and Cross be brought to the glory of his Resurrection. Through Christ our Lord. Amen.\n\n" +
                    "Antiphon: We fly to thy patronage, O holy Mother of God; despise not our petitions in our necessities, but deliver us always from all dangers, O glorious and blessed Virgin.\n\n" +
                    "Prayer to Saint Basil: Holy Father Basil, pray for us and for vocations to the Church and the Congregation.",
            prayerTextFr = "V. L'Ange du Seigneur apporta l'annonce à Marie.\nR. Et elle conçut du Saint-Esprit.\n(Je vous salue Marie...)\n\n" +
                    "V. Voici la servante du Seigneur.\nR. Qu'il me soit fait selon ta parole.\n(Je vous salue Marie...)\n\n" +
                    "V. Et le Verbe s'est fait chair.\nR. Et il a habité parmi nous.\n(Je vous salue Marie...)\n\n" +
                    "V. Priez pour nous, Sainte Mère de Dieu.\nR. Afin que nous devenions dignes des promesses du Christ.\n\n" +
                    "Prions : Que ta grâce, Seigneur notre Père, se répande en nos cœurs : par le message de l'Ange, tu nous as fait connaître l'Incarnation de ton Fils bien-aimé ; conduis-nous, par sa Passion et par sa Croix, jusqu'à la gloire de la Résurrection. Par le Christ notre Seigneur. Amen.\n\n" +
                    "Antienne : Sous l'abri de ta miséricorde, nous nous réfugions, Sainte Mère de Dieu.\n\n" +
                    "Prière à Saint Basile : Saint Père Basile, priez pour nous et pour les vocations dans l'Église et la Congrégation.",
            reflectionEn = "At noon, Basilians pause their labor to remember that God became human in Jesus and place the day under Mary's protection.",
            reflectionFr = "À midi, les Basiliens suspendent leurs travaux pour se souvenir de l'Incarnation et placer leur ministère sous la sollicitude maternelle de Marie."
        ),
        "mesa" to PrayerTranslation(
            titleEn = "Blessing of the Table & Meals",
            titleFr = "Bénédiction de la Table & des Repas",
            subtitleEn = "Before lunch and dinner: gratitude for shared bread",
            subtitleFr = "Avant le repas : gratitude pour le pain partagé",
            timeOfDayEn = "Midday / Evening",
            timeOfDayFr = "Midi / Soir",
            prayerTextEn = "The eyes of all look to you, Lord, and you give them their food in due season. You open your generous hand and satisfy the desires of every living thing.\n\n" +
                    "Bless us, O Lord, and these your gifts which we are about to receive from your bountiful goodness. Give bread to those who are hungry, and hunger for justice and for You to those who have bread. Through Christ our Lord.\n\n" +
                    "Give thanks to the Lord, for he is good; his mercy endures forever. Amen.",
            prayerTextFr = "Les yeux de tous espèrent en toi, Seigneur, et tu leur donnes la nourriture en son temps. Tu ouvres ta main bienfaisante et tu rassasies tout être vivant.\n\n" +
                    "Bénis-nous, Seigneur, et bénis cette nourriture que nous allons recevoir de ta bonté. Donne du pain à ceux qui ont faim, et la faim de justice et de Toi à ceux qui ont du pain. Par le Christ notre Seigneur.\n\n" +
                    "Rendons grâce au Seigneur car il est bon, éternel est son amour. Amen.",
            reflectionEn = "For Basilians, the dining room is 'Mensa Communis', a domestic altar deepening fraternity, simplicity, and hospitality.",
            reflectionFr = "La table est pour les Basiliens la 'Mensa Communis', un autel fraternel où s'approfondissent la simplicité et l'hospitalité."
        ),
        "visperas" to PrayerTranslation(
            titleEn = "Vespers (Evening Prayer)",
            titleFr = "Vêpres (Prière du Soir)",
            subtitleEn = "At sunset: thanksgiving for the accomplished day",
            subtitleFr = "Au coucher du soleil : action de grâce pour la journée vécue",
            timeOfDayEn = "Evening",
            timeOfDayFr = "Soir",
            prayerTextEn = "O God, come to my assistance; O Lord, make haste to help me.\n\n" +
                    "As the sun sets, we give you thanks, Good Father, for our finished labors, for fraternal encounters, and for challenges overcome through your grace.\n\n" +
                    "We entrust to you the poor, the sick, educators, and all young people seeking purpose and hope. May the simplicity and faithfulness of the Basilian Fathers continue to shine as light in our world.\n\n" +
                    "My soul magnifies the Lord, and my spirit rejoices in God my Savior... (Magnificat).\n\n" +
                    "Glory to the Father, and to the Son, and to the Holy Spirit. Amen.",
            prayerTextFr = "Dieu, viens à mon aide ; Seigneur, à notre secours.\n\n" +
                    "Au déclin du jour, nous te rendons grâce, Père infiniment bon, pour le travail accompli, pour la communion fraternelle et pour les épreuves surmontées avec ta grâce.\n\n" +
                    "Nous te confions les pauvres, les malades, les enseignants et les jeunes en quête d'espérance. Que le témoignage de simplicité des Pères Basiliens demeure une lumière réconfortante dans ce monde.\n\n" +
                    "Mon âme exalte le Seigneur, exulte mon esprit en Dieu, mon Sauveur... (Cantique du Magnificat).\n\n" +
                    "Gloire au Père, au Fils et au Saint-Esprit. Amen.",
            reflectionEn = "In Vespers, we surrender the fruits of our labor and rest in God's eternal faithfulness.",
            reflectionFr = "Aux Vêpres, nous remettons le fruit de nos efforts et nous reposons dans la fidélité de Dieu."
        ),
        "completas" to PrayerTranslation(
            titleEn = "Compline (Night Prayer & Nunc Dimittis)",
            titleFr = "Complies (Prière de la Nuit & Cantique de Siméon)",
            subtitleEn = "Before sleep: peaceful abandonment into the Father's hands",
            subtitleFr = "Avant le repos : abandon confiant entre les mains du Père",
            timeOfDayEn = "Night",
            timeOfDayFr = "Nuit",
            prayerTextEn = "May the Lord almighty grant us a quiet night and a peaceful death. Amen.\n\n" +
                    "Canticle of Simeon (Nunc Dimittis - Lk 2:29-32):\n" +
                    "Now, Master, you may let your servant go in peace, according to your word; for my eyes have seen your salvation, which you prepared in sight of all the peoples: a light for revelation to the Gentiles, and glory for your people Israel.\n\n" +
                    "Concluding Prayer:\n" +
                    "Visit this home, we beseech you, Lord, and drive far from it all snares of the enemy; may your holy angels dwell herein to preserve us in peace, and may your blessing be upon us always. Through Christ our Lord. Amen.\n\n" +
                    "Final Marian Antiphon:\n" +
                    "Hail, holy Queen, Mother of mercy, our life, our sweetness, and our hope... Turn then, most gracious advocate, thine eyes of mercy toward us, O clement, O loving, O sweet Virgin Mary!",
            prayerTextFr = "Que le Seigneur tout-puissant nous accorde une nuit paisible et une sainte fin. Amen.\n\n" +
                    "Cantique de Siméon (Nunc Dimittis - Lc 2, 29-32) :\n" +
                    "Maintenant, ô Maître souverain, tu peux laisser ton serviteur s'en aller en paix, selon ta parole. Car mes yeux ont vu le salut que tu préparais à la face des peuples : lumière qui se révèle aux nations et gloire de ton peuple Israël.\n\n" +
                    "Prière finale :\n" +
                    "Visite cette demeure, Seigneur, et repousse au loin toutes les embûches de l'ennemi ; que tes saints anges y habitent pour nous garder dans la paix, et que ta bénédiction demeure toujours sur nous. Par Jésus, le Christ, notre Seigneur. Amen.\n\n" +
                    "Antienne Mariale Finale :\n" +
                    "Sainte Reine, Mère de miséricorde, notre vie, notre douceur et notre espérance, salut !... Tourne vers nous tes regards miséricordieux, ô clémente, ô bienveillante, ô douce Vierge Marie !",
            reflectionEn = "At the end of Compline in Basilian houses, candles are gently dimmed and the Marian antiphon is sung, resting in peaceful surrender.",
            reflectionFr = "Dans les maisons basiliennes, les complies s'achèvent à la lueur des bougies par l'antienne à la Vierge Marie, confiant le repos dans la paix."
        ),
        "oracion_san_basilio" to PrayerTranslation(
            titleEn = "Prayer to Saint Basil the Great",
            titleFr = "Prière à Saint Basile le Grand",
            subtitleEn = "Official patronal prayer of the Congregation of Saint Basil",
            subtitleFr = "Prière patronale officielle de la Congrégation de Saint-Basile",
            timeOfDayEn = "Any time",
            timeOfDayFr = "Tout moment",
            prayerTextEn = "Holy father, Basil,\npatron of our Congregation,\nwhose apostolate is Christian education:\nbe our model in teaching\nand our support in ministry.\n\nGrant us that love for truth\nwhich you manifested in your life,\nthat we may be instruments\nin leading others to supreme truth.\n\nBe mindful of your Congregation,\nwhich you yourself have cared for from its beginning.\nAmen.",
            prayerTextFr = "Saint père, Basile,\npatron de notre Congrégation,\nqui as pour apostolat l'enseignement chrétien :\nsois notre modèle dans l'enseignement\net notre soutien dans le ministère.\n\nDonne-nous cet amour de la vérité\nque tu as manifesté dans ta vie,\nafin que nous soyons des instruments\npour conduire les autres à la vérité suprême.\n\nVeille sur ta Congrégation,\nque toi-même as gardée depuis ses origines.\nAmen.",
            reflectionEn = "This prayer encapsulates Basilian identity: to educate in Christian faith with unwavering love for truth and pastoral closeness.",
            reflectionFr = "Cette prière résume l'identité basilienne : éduquer chrétiennement avec un amour inconditionnel de la vérité et une bonté pastorale."
        ),
        "vida_comun" to PrayerTranslation(
            titleEn = "Prayer for Life in Common",
            titleFr = "Prière pour la Vie en Communauté",
            subtitleEn = "To foster charity, sincerity, and peace in family and community",
            subtitleFr = "Pour nourrir la charité, la sincérité et la paix dans le foyer et la communauté",
            timeOfDayEn = "Any time",
            timeOfDayFr = "Tout moment",
            prayerTextEn = "Lord, grant us:\nthat in love we may be sincere;\nthat we may abhor what is evil and hold fast to what is good;\n\nthat in fraternal love\nwe may show deep affection to one another;\n\nthat in mutual respect\nwe may esteem others as more worthy;\n\nthat in fulfilling our duties we may not be sluggish;\nthat in spirit we may be fervent;\nthat we may rejoice in hope;\nthat in trials we may be patient;\nthat with those in need we may share generously;\nthat toward strangers we may be eager to welcome them home;\n\nLord, that we may bless those who persecute us;\nthat we may bless and not curse;\n\nthat we may rejoice with those who rejoice\nand weep with those who weep;\n\nthat we may live in harmony with everyone;\nthat we may not rely on our own prideful wisdom\nbut strive to do what is noble in the sight of all;\n\nthat so far as it depends on us,\nwe may live in peace with everyone.\nAmen.",
            prayerTextFr = "Seigneur, accorde-nous :\nque dans l'amour nous soyons sincères ;\nque nous ayons le mal en horreur et nous attachions au bien ;\n\nque dans l'amour fraternel\nnous ayons de l'affection les uns pour les autres ;\n\nque par respect\nnous estimions les autres supérieurs à nous-mêmes ;\n\nque dans l'accomplissement du devoir nous ne soyons point paresseux ;\nque dans l'esprit nous soyons fervents ;\nque nous soyons joyeux dans l'espérance ;\nque dans les épreuves nous soyons patients ;\nque nous partagions avec ceux qui sont dans le besoin ;\nque nous soyons empressés à pratiquer l'hospitalité ;\n\nSeigneur, que nous bénissions ceux qui nous persécutent ;\nque nous bénissions et ne maudissions pas ;\n\nque nous nous réjouissions avec ceux qui se réjouissent,\net que nous pleurions avec ceux qui pleurent ;\n\nque nous vivions en plein accord les uns avec les autres ;\nque nous ne nous complaisions pas dans notre propre sagesse,\nmais cherchions ce qui est bien devant tous ;\n\nque s'il est possible, autant que cela dépend de nous,\nnous soyons en paix avec tous les hommes.\nAmen.",
            reflectionEn = "Inspired by Romans 12 and the traditions of the Annonay founders, this prayer is the guiding charter of authentic fraternity.",
            reflectionFr = "Inspirée de Romains 12 et des coutumes des fondateurs d'Annonay, cette prière est la règle d'or de la fraternité sans feinte."
        ),
        "espiritu_santo_capitulo" to PrayerTranslation(
            titleEn = "Prayer to the Holy Spirit for Discernment",
            titleFr = "Prière à l'Esprit Saint pour le Discernement",
            subtitleEn = "Invoked before major decisions and elections",
            subtitleFr = "Invoquée avant les décisions majeures et les chapitres",
            timeOfDayEn = "Any time",
            timeOfDayFr = "Tout moment",
            prayerTextEn = "Holy Spirit, our advocate and guide, whom Jesus sent to lead the Church into all truth, come upon us.\n\n" +
                    "Enlighten our minds with your wisdom, that we may choose and discern that which best serves our community and the Church.\n\n" +
                    "Fill our hearts with love and peace, that in unity we may embrace the pathways of your divine will.\n\n" +
                    "Come, Holy Spirit, come now among us as Jesus promised. Amen.",
            prayerTextFr = "Esprit Saint, notre défenseur et guide, que Jésus a envoyé à l'Église pour la conduire dans la vérité tout entière, viens en nous.\n\n" +
                    "Illumine nos esprits de ta sagesse afin que nous puissions choisir et discerner ce qui servira au mieux notre communauté et l'Église.\n\n" +
                    "Remplis nos cœurs d'amour et de paix afin que nous acceptions unis les chemins de ta sainte volonté.\n\n" +
                    "Viens, Esprit Saint, viens dès maintenant en nous comme Jésus nous l'a promis. Amen.",
            reflectionEn = "Prayed by all delegates before chapter elections and critical moments of communal discernment.",
            reflectionFr = "Priée par tous les capitulants avant chaque élection et discernement communautaire."
        ),
        "vocaciones_basilianas" to PrayerTranslation(
            titleEn = "Prayer for Basilian Vocations",
            titleFr = "Prière pour les Vocations Basiliennes",
            subtitleEn = "Petitioning the Lord of the Harvest for new servants of education and the Gospel",
            subtitleFr = "Pétition au Maître de la Moisson pour de nouveaux serviteurs de l'éducation et de l'Évangile",
            timeOfDayEn = "Any time",
            timeOfDayFr = "Tout moment",
            prayerTextEn = "Heavenly Father, the harvest is plentiful but the laborers are few.\n\n" +
                    "We humbly ask you to send generous, courageous, and faithful young men into the Congregation of the Basilian Fathers.\n\n" +
                    "Drawn by the example of Saint Basil and our holy founders, may they desire to consecrate their lives to Christian education, community life, and service to the poor, living with joy goodness, discipline, and knowledge. Through Christ our Lord. Amen.",
            prayerTextFr = "Père Céleste, la moisson est abondante mais les ouvriers sont peu nombreux.\n\n" +
                    "Nous te prions humblement d'envoyer des jeunes généreux, courageux et pleins de foi dans la Congrégation des Pères Basiliens.\n\n" +
                    "Attirés par l'exemple de Saint Basile et de nos fondateurs, qu'ils consacrent leur vie à l'éducation chrétienne, à la vie commune et au service des démunis, en vivant avec joie la bonté, la discipline et la science. Par le Christ notre Seigneur. Amen.",
            reflectionEn = "Vocational ministry is born from the patient, faithful prayer of all God's people.",
            reflectionFr = "La pastorale des vocations prend sa source dans la prière persévérante du peuple de Dieu."
        ),
        "oracion_san_oscar_romero" to PrayerTranslation(
            titleEn = "Prayer to Saint Óscar Romero",
            titleFr = "Prière à Saint Óscar Romero",
            subtitleEn = "Approved by the 2026 General Chapter for the Basilian Vademecum",
            subtitleFr = "Approuvée au Chapitre Général 2026 pour le Vademecum Basilien",
            timeOfDayEn = "Any time",
            timeOfDayFr = "Tout moment",
            prayerTextEn = "O God, Merciful Father,\nthrough the mediation of Jesus Christ,\nthe intercession of the Virgin Mary, Queen of Peace,\nand the action of the Holy Spirit,\nyou granted Saint Óscar Romero the grace to be an exemplary Shepherd serving the Church,\nand within it, reaching out preferentially to the poor and needy.\n\nWe come before you today with faith and trust for all the needs of the Congregation of Saint Basil,\nthat your holy will may be ever more firmly rooted among us.\n\nGrant, Lord, that we too may live according to the Gospel of your Son,\nwho lives and reigns forever and ever.\nAmen.",
            prayerTextFr = "Ô Dieu, Père Miséricordieux,\npar la médiation de Jésus-Christ,\nl'intercession de la Vierge Marie, Reine de la Paix,\net l'action de l'Esprit Saint,\ntu as accordé à Saint Óscar Romero la grâce d'être un Pasteur exemplaire au service de l'Église,\net en son sein, de façon préférentielle envers les pauvres et les nécessiteux.\n\nNous nous tournons vers Toi aujourd'hui avec foi et confiance pour tous les besoins de la Congrégation de Saint-Basile,\nafin que s'affermisse toujours plus en nous l'accomplissement de ta volonté.\n\nFais, Seigneur, que nous sachions nous aussi vivre selon l'Évangile de ton Fils,\nqui vit et règne pour les siècles des siècles.\nAmen.",
            reflectionEn = "Formally approved by the 2026 General Chapter to entrust pastoral zeal, preferential option for the poor, and prophetic courage to Saint Óscar Romero.",
            reflectionFr = "Approuvée formellement au Chapitre Général 2026 pour confier le zèle pastoral et l'option préférentielle pour les pauvres à Saint Óscar Romero."
        )
    )

    fun getLocalizedPrayer(prayer: PrayerItem, lang: AppLanguage): PrayerItem {
        if (lang == AppLanguage.ES) return prayer
        val t = prayerTranslations[prayer.id] ?: return prayer
        return when (lang) {
            AppLanguage.EN -> prayer.copy(
                title = t.titleEn,
                subtitle = t.subtitleEn,
                timeOfDay = t.timeOfDayEn,
                prayerText = t.prayerTextEn,
                reflection = t.reflectionEn
            )
            AppLanguage.FR -> prayer.copy(
                title = t.titleFr,
                subtitle = t.subtitleFr,
                timeOfDay = t.timeOfDayFr,
                prayerText = t.prayerTextFr,
                reflection = t.reflectionFr
            )
            AppLanguage.ES -> prayer
        }
    }

    /**
     * Heart and Spiritual Accompaniment data to make the app profoundly human,
     * fraternal, warm, and comforting for users in different states of life.
     */
    data class HeartAccompaniment(
        val stateId: String,
        val icon: String,
        val emotionLabel: String,
        val pastoralWord: String,
        val biblicalPromise: String,
        val shortSpokenPrayer: String
    )

    fun getHeartAccompaniments(lang: AppLanguage): List<HeartAccompaniment> = when (lang) {
        AppLanguage.EN -> listOf(
            HeartAccompaniment(
                stateId = "peace",
                icon = "🕊️",
                emotionLabel = "Peace & Gratitude",
                pastoralWord = "Dear brother, dear sister: savor this gift. Saint Basil taught that peace of soul is the mirror wherein God's face is reflected. When your table has bread and your heart has calm, look around and see whose burden you can lighten today.",
                biblicalPromise = "«Give thanks in all circumstances; for this is God's will for you in Christ Jesus.» (1 Thess 5:18)",
                shortSpokenPrayer = "Lord, thank you for this peaceful hour. May your peace flow through my hands into someone who feels alone today. Amen."
            ),
            HeartAccompaniment(
                stateId = "weary",
                icon = "🌧️",
                emotionLabel = "Weary & Heavy-Laden",
                pastoralWord = "Breathe gently. You do not have to carry everything by your own strength. In our Basilian community, the founders learned that fatigue is not defeat—it is a gentle reminder to lean back upon the Father's shoulder.",
                biblicalPromise = "«Come to me, all you who are weary and burdened, and I will give you rest.» (Matt 11:28)",
                shortSpokenPrayer = "Jesus, I lay down my exhaustion at your feet. Breathe your quiet calm into my heart and renew my strength. Amen."
            ),
            HeartAccompaniment(
                stateId = "seeking",
                icon = "🧭",
                emotionLabel = "Seeking Direction",
                pastoralWord = "When the path seems unclear, quiet the noise. Saint Basil wrote: 'In the stillness of prayer, the soul is restored to harmony.' You do not need to see all ten steps ahead; only take the next step in faith, goodness, and love.",
                biblicalPromise = "«I will instruct you and teach you in the way you should go; I will counsel you with my loving eye on you.» (Psalm 32:8)",
                shortSpokenPrayer = "Holy Spirit, light my next step. Give me wisdom to choose with patience and love. Amen."
            ),
            HeartAccompaniment(
                stateId = "sorrow",
                icon = "💔",
                emotionLabel = "Grieving or Hurting",
                pastoralWord = "Your pain is sacred and not invisible to God. You are held in fraternal prayer. In our history, during hardship and loss, our community discovered that Christ draws closest precisely when the heart is bruised.",
                biblicalPromise = "«The Lord is close to the brokenhearted and saves those who are crushed in spirit.» (Psalm 34:18)",
                shortSpokenPrayer = "Merciful Father, hold my tears in your hands. Wrap me in your tender embrace and grant me comfort today. Amen."
            ),
            HeartAccompaniment(
                stateId = "apostolic",
                icon = "🔥",
                emotionLabel = "Apostolic Joy",
                pastoralWord = "Rejoice in this holy fire! Channel your enthusiasm into concrete acts of service: teaching, encouraging, visiting the lonely, and building communion as our ten founders did in Annonay.",
                biblicalPromise = "«I have come to bring fire on the earth, and how I wish it were already kindled!» (Luke 12:49)",
                shortSpokenPrayer = "Lord, set my hands to work in goodness, discipline, and science. May your love shine through me today. Amen."
            )
        )
        AppLanguage.FR -> listOf(
            HeartAccompaniment(
                stateId = "peace",
                icon = "🕊️",
                emotionLabel = "Paix & Gratitude",
                pastoralWord = "Cher frère, chère sœur : savoure ce don précieux. Saint Basile enseignait que la paix de l'âme est le miroir où se reflète la tendresse de Dieu. Quand ton cœur est en paix, partage cette joie avec celui qui vacille.",
                biblicalPromise = "« Rendez grâce en toute circonstance : c'est la volonté de Dieu dans le Christ Jésus. » (1 Th 5, 18)",
                shortSpokenPrayer = "Seigneur, merci pour cette heure paisible. Que ta paix déborde de mon cœur vers mon prochain. Amen."
            ),
            HeartAccompaniment(
                stateId = "weary",
                icon = "🌧️",
                emotionLabel = "Fatigue & Fardeau",
                pastoralWord = "Respire doucement. Tu n'as pas à porter le monde entier sur tes épaules. Les fondateurs d'Annonay ont appris que la fatigue est l'invitation filiale à s'abandonner dans les bras du Père.",
                biblicalPromise = "« Venez à moi, vous tous qui peinez sous le poids du fardeau, et moi, je vous procurerai le repos. » (Mt 11, 28)",
                shortSpokenPrayer = "Jésus, je dépose ma lassitude entre tes mains. Donne-moi ton repos et renouvelle mon courage. Amen."
            ),
            HeartAccompaniment(
                stateId = "seeking",
                icon = "🧭",
                emotionLabel = "En Recherche & Doute",
                pastoralWord = "Quand la brume se lève, apaise les bruits extérieurs. Saint Basile écrivait : 'C'est dans le silence que l'âme contemple Dieu'. Tu n'as pas besoin de tout comprendre d'un coup ; fais simplement le pas suivant dans l'amour.",
                biblicalPromise = "« Je t'instruirai, je te montrerai la route que tu dois suivre ; j'aurai les yeux sur toi. » (Psaume 32, 8)",
                shortSpokenPrayer = "Esprit Saint, guide mes pas. Accorde-moi la sagesse et la sérénité pour discerner avec confiance. Amen."
            ),
            HeartAccompaniment(
                stateId = "sorrow",
                icon = "💔",
                emotionLabel = "Peine ou Deuil",
                pastoralWord = "Ta souffrance est accueillie avec respect et amour. Tu n'es pas seul dans cette vallée. Le Christ s'est fait notre frère précisément pour partager nos larmes et nous ouvrir un chemin de consolation.",
                biblicalPromise = "« Proche est le Seigneur de ceux qui ont le cœur brisé ; il sauve les esprits abattus. » (Psaume 34, 19)",
                shortSpokenPrayer = "Père des miséricordes, console mon âme blessée. Enveloppe-moi de ta tendresse et donne-moi l'espérance. Amen."
            ),
            HeartAccompaniment(
                stateId = "apostolic",
                icon = "🔥",
                emotionLabel = "Élan Apostolique",
                pastoralWord = "Réjouis-toi de cette sainte ardeur ! Traduis ton enthousiasme en gestes concrets d'éducation, de service des pauvres et de charité inventive, comme nos dix pères à Annonay.",
                biblicalPromise = "« C'est un feu que je suis venu apporter sur la terre, et comme je voudrais qu'il soit déjà allumé ! » (Luc 12, 49)",
                shortSpokenPrayer = "Seigneur, fais de moi un instrument de ta paix et de ta vérité. Que ton amour rayonne par mes actes. Amen."
            )
        )
        AppLanguage.ES -> listOf(
            HeartAccompaniment(
                stateId = "peace",
                icon = "🕊️",
                emotionLabel = "Paz y Gratitud",
                pastoralWord = "Querido hermano, querida hermana: saborea este momento de gracia. San Basilio nos enseñaba que la paz interior es el espejo donde Dios contempla su propia bondad. Cuando tu alma reposa serena, mira a tu alrededor y fíjate a quién puedes regalarle hoy una sonrisa o un oído atento.",
                biblicalPromise = "«Den gracias en toda ocasión: esto es lo que Dios quiere de ustedes en Cristo Jesús.» (1 Tes 5, 18)",
                shortSpokenPrayer = "Señor, gracias por este remanso de paz en mi vida. Que tu serenidad guíe mis pasos y me haga sembrador de calma para mis hermanos. Amén."
            ),
            HeartAccompaniment(
                stateId = "weary",
                icon = "🌧️",
                emotionLabel = "Cansancio o Agobio",
                pastoralWord = "Suelta las cargas por un instante y respira. No tienes que salvar al mundo con tus solas fuerzas: Dios sostiene tu vida. Como aprendieron los primeros diez padres basilianos en la Francia de 1822, el descanso confiado es un acto supremo de humildad.",
                biblicalPromise = "«Vengan a mí todos los que están fatigados y sobrecargados, y yo les daré descanso.» (Mt 11, 28)",
                shortSpokenPrayer = "Jesús, pongo mi cansancio en tus manos abiertas. Devuélveme el aliento y la paz del corazón. Amén."
            ),
            HeartAccompaniment(
                stateId = "seeking",
                icon = "🧭",
                emotionLabel = "Búsqueda y Dudas",
                pastoralWord = "Cuando no veas claro el horizonte, aquieta el ruido. San Basilio escribió en su regla: 'El alma que calla para escuchar a Dios encuentra en la sencillez la senda verdadera'. No necesitas tener todas las respuestas hoy, basta con dar el próximo paso en la caridad.",
                biblicalPromise = "«Te instruiré y te enseñaré el camino que debes seguir; fijaré mis ojos en ti.» (Salmo 32, 8)",
                shortSpokenPrayer = "Espíritu Santo, derrama tu luz en mi mente y corazón. Dame discernimiento y paciencia para caminar en tu voluntad. Amén."
            ),
            HeartAccompaniment(
                stateId = "sorrow",
                icon = "💔",
                emotionLabel = "Dolor, Soledad o Duelo",
                pastoralWord = "Tu dolor no es invisible a los ojos del Señor ni a los de tus hermanos. En la comunidad basiliana nadie sufre solo. En las horas más difíciles, el crucificado se hace compañía tierna que enjuga en silencio tus lágrimas.",
                biblicalPromise = "«El Señor está cerca de los que tienen el corazón roto, salva a los espíritus hundidos.» (Salmo 34, 19)",
                shortSpokenPrayer = "Padre amoroso, acoge mi herida y mi tristeza. Rodéame con tu ternura fraterna y no permitas que pierda la esperanza. Amén."
            ),
            HeartAccompaniment(
                stateId = "apostolic",
                icon = "🔥",
                emotionLabel = "Alegría Apostólica",
                pastoralWord = "¡Qué bendición sentir arder el corazón por el Evangelio! Canaliza este santo entusiasmo en obras de bondad, disciplina y ciencia: enseña con paciencia, acompaña a un joven, sirve al necesitado y contagia la alegría del Reino.",
                biblicalPromise = "«He venido a traer fuego a la tierra, ¡y cómo desearía que ya estuviera ardiendo!» (Lc 12, 49)",
                shortSpokenPrayer = "Señor, enciende en mí un celo incansable por servir y educar en tu amor. ¡Aquí estoy, envíame! Amén."
            )
        )
    }

    fun getLocalizedPatron(patron: PatronSaint, lang: AppLanguage): PatronSaint {
        if (lang == AppLanguage.ES) return patron
        return when (lang) {
            AppLanguage.EN -> when (patron.id) {
                "san_basilio" -> patron.copy(
                    name = "Saint Basil the Great",
                    feastDay = "January 2 (and June 14 traditional feast)",
                    title = "Father and Titular Patron of the Congregation, Doctor of the Church",
                    biography = "Born in Caesarea of Cappadocia (329 - 379 AD). Priest, scholar, and Bishop of Caesarea. Tireless defender of orthodox faith, father of Eastern cenobitic monasticism, and builder of the 'Basiliad' charity hospital. Unified deep intellectual study with Gospel love.",
                    connectionToBasilians = "Chosen in 1822 by the ten founders because he taught fraternal Christian living suitable for the times and prized human learning in the light of faith.",
                    virtueForDailyLife = "Sincere love for truth, compassionate care for the poor, and a life discipline uniting mind and heart with God.",
                    patronPrayer = "Holy Father Basil, patron of our Congregation dedicated to Christian education, be our model in teaching and guide in ministry. Grant us your passionate love for truth, that we may lead others to Christ. Keep watch over your Congregation, which you have sheltered from its beginning. Amen."
                )
                "virgen_maria" -> patron.copy(
                    name = "The Blessed Virgin Mary",
                    feastDay = "August 15 (Assumption) and November 21 (Presentation)",
                    title = "Mother of God and Principal Protectress of the Basilians",
                    biography = "Mary, Mother of Jesus Christ and of the Church. Model of prayerful listening to the Word, unconditional surrender to God's will, and fidelity at the foot of the Cross.",
                    connectionToBasilians = "On November 21, 1822, feast of the Presentation of Mary in the Temple, the founders chose their first superior and formally formed the association.",
                    virtueForDailyLife = "Prayerful humility, readiness for God's call, and pondering all things in the quiet of the heart.",
                    patronPrayer = "We fly to thy patronage, O holy Mother of God; despise not our petitions in our necessities, but deliver us always from all dangers, O glorious and blessed Virgin. Amen."
                )
                "san_jose" -> patron.copy(
                    name = "Saint Joseph",
                    feastDay = "March 19 (Solemnity)",
                    title = "Spouse of the Virgin Mary, Guardian of the Redeemer and Labor",
                    biography = "Just, silent, and faithful guardian of the Holy Family in Nazareth, teaching the Child Jesus with honest, humble daily labor.",
                    connectionToBasilians = "Venerated for his quiet obedience, unassuming courage, and fatherly protection of religious houses and schools.",
                    virtueForDailyLife = "Quiet fidelity in daily duty, fairness to all, and tender care for those placed in our hands.",
                    patronPrayer = "Saint Joseph, caring guardian and just man, teach us to work honestly, to love our families and brothers in community, and to welcome God's will with serenity. Amen."
                )
                "san_francisco" -> patron.copy(
                    name = "Saint Francis of Assisi",
                    feastDay = "October 4",
                    title = "The Poverello, Model of Gospel Poverty and Fraternal Peace",
                    biography = "Born in Assisi (1181/1182 - 1226). Renounced worldly wealth to embrace poverty, preaching reconciliation, fraternity, and tender love for all creation.",
                    connectionToBasilians = "The Annonay school was founded in the former Franciscan convent. Francis inspires the vow of poverty as joyful freedom and solidarity with the marginalized.",
                    virtueForDailyLife = "Simplicity of life, detachment from materialism, and peaceful stewardship of God's creation.",
                    patronPrayer = "Lord, make me an instrument of your peace. Where there is hatred, let me sow love; where there is injury, pardon; where there is doubt, faith; where there is despair, hope. Amen."
                )
                "santo_tomas" -> patron.copy(
                    name = "Saint Thomas Aquinas",
                    feastDay = "January 28",
                    title = "Angelic Doctor, Master of Thought and the Harmony of Faith & Reason",
                    biography = "Dominican friar (1225 - 1274), apex of Christian philosophy and theology. Author of the Summa Theologiae and Eucharistic hymnographer.",
                    connectionToBasilians = "Patron of Basilian commitment to higher learning and intellectual rigor in schools and universities (St. Michael's in Toronto, UST in Houston).",
                    virtueForDailyLife = "Passionate love for diligent study, intellectual humility, and the tireless search for truth.",
                    patronPrayer = "Ineffable Creator, shed a ray of your light upon the darkness of my intellect. Grant me quickness to understand, capacity to retain, and grace in expressing the truth. Amen."
                )
                "san_juan_bosco" -> patron.copy(
                    name = "Saint John Bosco",
                    feastDay = "January 31",
                    title = "Father and Teacher of Youth",
                    biography = "Priest and educator of Turin (1815 - 1888). Founder of the preventive system of education built on reason, faith, and loving-kindness.",
                    connectionToBasilians = "Patron of Basilian youth and educational pastoral ministry in classrooms, neighborhoods, and youth groups.",
                    virtueForDailyLife = "Cheerful joy, pedagogical patience, and selfless dedication to vulnerable youth.",
                    patronPrayer = "Saint John Bosco, friend of youth: help us guide students with kindness, understand their longings, and spread the joy of the Gospel. Amen."
                )
                "aldemar_rodriguez" -> patron.copy(
                    name = "Aldemar Rodríguez Carvajal",
                    feastDay = "Holy Wednesday (Annual memorial in Holy Week)",
                    title = "Young Catechist, Martyr of Assumption Parish (Cali)",
                    biography = "Born in Neiva in 1972. Catechist at Our Lady of the Assumption Parish in Cali, servant of the sick, and champion of human dignity. Martyred in 1992 for standing with the poor.",
                    connectionToBasilians = "Collaborated closely with Basilian fathers Francisco Amico, Robert Seguin, and Rafael Lopera. Honored as a radiant witness of faith and peace in Colombia.",
                    virtueForDailyLife = "Prophetic courage, total self-giving to the poor, and unyielding consistency between faith in Christ and justice.",
                    patronPrayer = "Aldemar, brother and witness: you who united the Gospel with self-giving love for the vulnerable, pray for your pilgrim brothers and sisters as we build peace. Amen."
                )
                "oscar_romero" -> patron.copy(
                    name = "Saint Óscar Arnulfo Romero",
                    feastDay = "March 24",
                    title = "Martyr Archbishop of San Salvador, Voice of the Voiceless",
                    biography = "Archbishop of San Salvador (1917 - 1980), canonized by Pope Francis. Defended the oppressed with prophetic courage until shedding his blood at the altar.",
                    connectionToBasilians = "Central inspiration in Basilian Latin American missions, embodying the shepherd who gives his life for his flock.",
                    virtueForDailyLife = "Courageous speaking for justice with charity, listening to the humble, and trusting God.",
                    patronPrayer = "Saint Óscar Romero, courageous pastor and witness: grant us hearts attentive to the wounds of our people and courage to sow fraternity where pain reigns. Amen."
                )
                else -> patron
            }
            AppLanguage.FR -> when (patron.id) {
                "san_basilio" -> patron.copy(
                    name = "Saint Basile le Grand",
                    feastDay = "2 Janvier (et 14 Juin fête traditionnelle)",
                    title = "Père et Patron Titulaire de la Congrégation, Docteur de l'Église",
                    biography = "Né à Césarée de Cappadoce (329 - 379). Évêque, théologien et père du monachisme cénobitique oriental. Fondateur de la 'Basiliade', cité de charité pour les pauvres et les malades. Intégra le savoir intellectuel et la lumière évangélique.",
                    connectionToBasilians = "Choisi en 1822 par les dix fondateurs pour son modèle de vie fraternelle et son estime pour la formation chrétienne intégrale.",
                    virtueForDailyLife = "Amour sincère de la vérité, service inconditionnel des plus pauvres et discipline unissant le cœur et l'esprit à Dieu.",
                    patronPrayer = "Saint Père Basile, patron de notre Congrégation dédiée à l'enseignement chrétien, sois notre modèle et notre soutien. Donne-nous ton amour de la vérité et veille sur ta famille religieuse. Amen."
                )
                "virgen_maria" -> patron.copy(
                    name = "La Très Sainte Vierge Marie",
                    feastDay = "15 Août (Assomption) et 21 Novembre (Présentation)",
                    title = "Mère de Dieu et Protectrice Principale des Basiliens",
                    biography = "Marie, Mère de Jésus et de l'Église. Modèle d'écoute méditative de la Parole, d'obéissance totale au dessein divin et de fidélité au pied de la Croix.",
                    connectionToBasilians = "Le 21 novembre 1822, fête de la Présentation de Marie, les fondateurs élurent leur premier supérieur et initièrent la vie commune.",
                    virtueForDailyLife = "Humilité orante, empressement au service de Dieu et recueillement intérieur.",
                    patronPrayer = "Sous l'abri de ta miséricorde, nous nous réfugions, Sainte Mère de Dieu. Délivre-nous de tout péril, ô Vierge glorieuse et bénie. Amen."
                )
                "san_jose" -> patron.copy(
                    name = "Saint Joseph",
                    feastDay = "19 Mars (Solennité)",
                    title = "Époux de la Vierge Marie, Gardien du Rédempteur et du Travail",
                    biography = "Homme juste, silencieux et fidèle, gardien de la Sainte Famille à Nazareth, enseignant l'Enfant Jésus dans le travail humble de chaque jour.",
                    connectionToBasilians = "Vénéré pour sa discrétion, son obéissance paisible et sa garde paternelle des écoles et communautés basiliennes.",
                    virtueForDailyLife = "Fidélité silencieuse au devoir, droiture et bienveillance envers tous.",
                    patronPrayer = "Saint Joseph, père très attentif et homme juste, apprends-nous à travailler avec honnêteté et à accueillir avec paix la volonté du Père. Amen."
                )
                "san_francisco" -> patron.copy(
                    name = "Saint François d'Assise",
                    feastDay = "4 Octobre",
                    title = "Le Poverello, Modèle de Pauvreté Évangélique et de Paix",
                    biography = "Né à Assise (1181/1182 - 1226). Quitta ses richesses pour épouser 'Dame Pauvreté', prêchant la réconciliation et l'amour de la Création.",
                    connectionToBasilians = "Le collège d'Annonay est né dans l'ancien couvent des Cordeliers franciscains. François inspire la pauvreté comme liberté fraternelle.",
                    virtueForDailyLife = "Simplicité de vie, détachement des biens matériels et esprit de paix.",
                    patronPrayer = "Seigneur, fais de moi un instrument de ta paix. Là où est la haine, que je mette l'amour ; là où est l'offense, le pardon. Amen."
                )
                "santo_tomas" -> patron.copy(
                    name = "Saint Thomas d'Aquin",
                    feastDay = "28 Janvier",
                    title = "Docteur Angélique, Maître de la Pensée et de l'Harmonie Foi-Raison",
                    biography = "Frère dominicain (1225 - 1274), sommet de la philosophie et de la théologie chrétiennes. Auteur de la Somme Théologique.",
                    connectionToBasilians = "Patron de l'engagement intellectuel basilien pour la 'Science' et les études universitaires rigoureuses.",
                    virtueForDailyLife = "Amour passionné de l'étude, humilité intellectuelle et recherche inlassable de la vérité divine.",
                    patronPrayer = "Créateur ineffable, répands sur les ténèbres de mon esprit un rayon de ta clarté. Accorde-moi la vivacité de comprendre et la grâce de parler avec sagesse. Amen."
                )
                "san_juan_bosco" -> patron.copy(
                    name = "Saint Jean Bosco",
                    feastDay = "31 Janvier",
                    title = "Père et Éducateur de la Jeunesse",
                    biography = "Prêtre éducateur de Turin (1815 - 1888). Fondateur du système préventif fondé sur la raison, la foi et l'affection (amorevolezza).",
                    connectionToBasilians = "Patron de la pastorale éducative basilienne auprès des jeunes et des quartiers populaires.",
                    virtueForDailyLife = "Joie évangélique communicative, douceur éducative et zèle pour les jeunes.",
                    patronPrayer = "Saint Jean Bosco, ami des jeunes : aide-nous à éduquer avec bonté et à communiquer la joie de l'Évangile dans nos maisons. Amen."
                )
                "aldemar_rodriguez" -> patron.copy(
                    name = "Aldemar Rodríguez Carvajal",
                    feastDay = "Mercredi Saint (Mémoire annuelle)",
                    title = "Jeune Catéchiste, Martyr de la Paroisse de l'Assomption (Cali)",
                    biography = "Né en 1972. Catéchiste à la paroisse de l'Assomption à Cali, défenseur des humbles et des droits humains. Martyr en 1992 pour son option en faveur des pauvres.",
                    connectionToBasilians = "Compagnon très cher des pères basiliens en Colombie, vénéré comme modèle de fidélité au Christ et de réconciliation.",
                    virtueForDailyLife = "Courage prophétique et don total de soi pour les plus démunis.",
                    patronPrayer = "Aldemar, frère et témoin : toi qui as uni l'Évangile à la défense de la vie et des pauvres, prie pour nous afin que nous soyons des artisans de paix. Amen."
                )
                "oscar_romero" -> patron.copy(
                    name = "Saint Óscar Arnulfo Romero",
                    feastDay = "24 Mars",
                    title = "Archevêque Martyr de San Salvador, Voix des Sans-Voix",
                    biography = "Archevêque de San Salvador (1917 - 1980), canonisé par le pape François. Défendit les persécutés jusqu'au don suprême de sa vie à l'autel.",
                    connectionToBasilians = "Guide spirituel de la pastorale basilienne en Amérique Latine pour son témoignage de pasteur qui donne sa vie pour ses brebis.",
                    virtueForDailyLife = "Écoute attentive des pauvres et confiance indéfectible en Dieu.",
                    patronPrayer = "Saint Óscar Romero, pasteur généreux : accorde-nous un cœur sensible aux blessures de nos frères et le courage de semer l'espérance. Amen."
                )
                else -> patron
            }
            AppLanguage.ES -> patron
        }
    }

    fun getLocalizedFounder(founder: Founder, lang: AppLanguage): Founder {
        if (lang == AppLanguage.ES) return founder
        return when (lang) {
            AppLanguage.EN -> when (founder.id) {
                "joseph_lapierre" -> founder.copy(
                    role = "First Superior General (1822 - 1838)",
                    title = "The Prudent Leader and Pioneer of the Foundation",
                    keyContribution = "Guided the community with serene wisdom through its foundational civil and ecclesiastical constitution.",
                    spiritualVirtue = "Prudence and Steadfast Patience",
                    quoteOrEpitaph = "«In fraternal unity and quiet trust, God builds what human weakness could never accomplish alone.»"
                )
                "pierre_fayolle" -> founder.copy(
                    role = "Cofounder and Educator",
                    title = "Teacher of Languages and Guardian of Youth",
                    keyContribution = "Formed generations of young scholars in Annonay with gentle pedagogy and academic devotion.",
                    spiritualVirtue = "Gentleness and Dedication",
                    quoteOrEpitaph = "«To educate a young soul is to plant the seed of the Kingdom of Heaven.»"
                )
                else -> founder.copy(
                    role = if (founder.isFrenchFounder) "French Cofounder (Annonay 1822)" else "Pioneer of Colombia Mission (1987)",
                    title = if (founder.isFrenchFounder) "Founding Father of the Congregation of Saint Basil" else "Missionary Pioneer in Cali, Colombia"
                )
            }
            AppLanguage.FR -> when (founder.id) {
                "joseph_lapierre" -> founder.copy(
                    role = "Premier Supérieur Général (1822 - 1838)",
                    title = "Le Sage Pasteur et Guide des Origines",
                    keyContribution = "Dirigea la communauté naissante avec douceur et persévérance à Annonay.",
                    spiritualVirtue = "Prudence et Patience Filiale",
                    quoteOrEpitaph = "« Dans la concorde fraternelle, Dieu accomplit ce que la faiblesse humaine ne saurait faire seule. »"
                )
                "pierre_fayolle" -> founder.copy(
                    role = "Cofondateur et Pédagogue",
                    title = "Maître des Lettres et Protecteur de la Jeunesse",
                    keyContribution = "Forma des générations d'élèves avec dévouement chrétien et rigueur.",
                    spiritualVirtue = "Douceur et Zèle Éducatif",
                    quoteOrEpitaph = "« Éduquer une âme, c'est semer pour l'éternité. »"
                )
                else -> founder.copy(
                    role = if (founder.isFrenchFounder) "Cofondateur Français (Annonay 1822)" else "Pionnier de la Mission en Colombie (1987)",
                    title = if (founder.isFrenchFounder) "Père Fondateur de la Congrégation de Saint-Basile" else "Pionnier Missionnaire à Cali, Colombie"
                )
            }
            AppLanguage.ES -> founder
        }
    }

    fun getLocalizedCalendarDate(date: BasilianCalendarDate, lang: AppLanguage): BasilianCalendarDate = when (lang) {
        AppLanguage.EN -> {
            val (locTitle, locBrief, locRank) = when (date.id) {
                "st_basil_gregory" -> Triple(
                    "Solemnity of Saints Basil the Great and Gregory Nazianzen",
                    "Patron of the Congregation of Saint Basil • Doctor of the Church and Monastic Patriarch.",
                    "Patronal Solemnity"
                )
                "st_thomas_aquinas" -> Triple(
                    "Saint Thomas Aquinas, Priest and Doctor of the Church",
                    "Official Patron of Basilian Intellectual and Academic Life.",
                    "Feast"
                )
                "st_john_bosco" -> Triple(
                    "Saint John Bosco, Priest and Educator",
                    "Official Patron of Youth and Christian Education.",
                    "Memorial"
                )
                "annonay_foundation" -> Triple(
                    "Foundation of the Basilian Fathers in Annonay (1822)",
                    "Ten French diocesan priests associate to teach and form youth in the aftermath of the French Revolution.",
                    "Historical Anniversary"
                )
                "colombia_arrival_1987" -> Triple(
                    "Arrival of the Basilian Fathers in Colombia (1987)",
                    "Fr. Bob Seguin and Fr. Frank Amico arrive in Cali to begin parish and missionary life in Aguablanca.",
                    "Historical Anniversary"
                )
                "st_joseph" -> Triple(
                    "Solemnity of Saint Joseph, Spouse of the Blessed Virgin Mary",
                    "Protector of the Universal Church and Faithful Guardian of the Congregation.",
                    "Solemnity"
                )
                "separation_decree_1922" -> Triple(
                    "Separation Decree and Centenary Document (1922)",
                    "Autonomous government established between France and America, reunited in 1955.",
                    "Historical Memorial"
                )
                "st_francis_assisi" -> Triple(
                    "Saint Francis of Assisi, Deacon and Founder",
                    "Official Patron of Evangelical Poverty, Joy, and Creation Care.",
                    "Feast"
                )
                else -> Triple(date.title, date.briefDescription, date.liturgicalRank)
            }
            date.copy(
                title = locTitle,
                briefDescription = locBrief,
                liturgicalRank = locRank,
                basilianRelationship = date.basilianRelationship?.let { rel ->
                    when {
                        rel.contains("Patrono titular") -> "Titular patron and namesake of the Congregation of Saint Basil."
                        rel.contains("Patrono de los estudios") -> "Patron of theological study and academic dedication."
                        rel.contains("Patrono de los jóvenes") -> "Patron of schools and youth ministry."
                        rel.contains("Pobreza evangélica") -> "Patron of simplicity, peace, and evangelical joy."
                        else -> rel
                    }
                }
            )
        }
        AppLanguage.FR -> {
            val (locTitle, locBrief, locRank) = when (date.id) {
                "st_basil_gregory" -> Triple(
                    "Solennité de Saint Basile le Grand et Saint Grégoire de Nazianze",
                    "Patron de la Congrégation de Saint-Basile • Docteur de l'Église et Père du monachisme.",
                    "Solennité Patronale"
                )
                "st_thomas_aquinas" -> Triple(
                    "Saint Thomas d'Aquin, Prêtre et Docteur de l'Église",
                    "Patron officiel de la vie intellectuelle et académique basilienne.",
                    "Fête"
                )
                "st_john_bosco" -> Triple(
                    "Saint Jean Bosco, Prêtre et Éducateur",
                    "Patron officiel de la jeunesse et de l'éducation chrétienne.",
                    "Mémoire"
                )
                "annonay_foundation" -> Triple(
                    "Fondation des Pères Basiliens à Annonay (1822)",
                    "Dix prêtres diocésains français s'unissent pour éduquer la jeunesse après la Révolution.",
                    "Anniversaire Historique"
                )
                "colombia_arrival_1987" -> Triple(
                    "Arrivée des Pères Basiliens en Colombie (1987)",
                    "Les Pères Bob Seguin et Frank Amico arrivent à Cali pour débuter la pastorale à Aguablanca.",
                    "Anniversaire Historique"
                )
                "st_joseph" -> Triple(
                    "Solennité de Saint Joseph, Époux de la Bienheureuse Vierge Marie",
                    "Protecteur de l'Église universelle et gardien fidèle de la Congrégation.",
                    "Solennité"
                )
                "separation_decree_1922" -> Triple(
                    "Décret de Séparation et Document du Centenaire (1922)",
                    "Gouvernement autonome entre la France et l'Amérique, réuni en 1955.",
                    "Mémoire Historique"
                )
                "st_francis_assisi" -> Triple(
                    "Saint François d'Assise, Diacre et Fondateur",
                    "Patron officiel de la pauvreté évangélique, de la paix et de la création.",
                    "Fête"
                )
                else -> Triple(date.title, date.briefDescription, date.liturgicalRank)
            }
            date.copy(
                title = locTitle,
                briefDescription = locBrief,
                liturgicalRank = locRank,
                basilianRelationship = date.basilianRelationship?.let { rel ->
                    when {
                        rel.contains("Patrono titular") -> "Patron titulaire et guide spirituel de la Congrégation de Saint-Basile."
                        rel.contains("Patrono de los estudios") -> "Patron des études théologiques et de la discipline académique."
                        rel.contains("Patrono de los jóvenes") -> "Patron des éducateurs et de la jeunesse."
                        rel.contains("Pobreza evangélica") -> "Patron de la pauvreté évangélique et de la joie fraternelle."
                        else -> rel
                    }
                }
            )
        }
        AppLanguage.ES -> date
    }
}

