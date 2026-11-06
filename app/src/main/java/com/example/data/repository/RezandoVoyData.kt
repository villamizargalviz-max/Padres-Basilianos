package com.example.data.repository

import com.example.data.model.AppLanguage
import com.example.data.model.RezandoVoySession
import com.example.data.model.RezandoVoyStep

object RezandoVoyData {

    fun getTodaySession(rawWisdom: DailyLiturgicalWisdom, lang: AppLanguage = AppLanguage.ES): RezandoVoySession {
        val wisdom = UniversalContentLocalizer.localizeWisdom(rawWisdom, lang)
        val (dayOfWeek, dateDisplay, mottoFocus, songTitle) = when (lang) {
            AppLanguage.EN -> Quadruple(
                "Today",
                "Basilian Meditation & Guided Prayer • ${wisdom.dateDisplay}",
                "Goodness, Discipline, and Knowledge",
                "«Misericordias Domini in aeternum cantabo» (Liturgical Chant)"
            )
            AppLanguage.FR -> Quadruple(
                "Aujourd'hui",
                "Méditation et Prière Guidée Basilienne • ${wisdom.dateDisplay}",
                "Bonté, Discipline et Science",
                "«Misericordias Domini in aeternum cantabo» (Chant Liturgique)"
            )
            AppLanguage.ES -> Quadruple(
                "Hoy",
                "Meditación y Oración Basiliana • ${wisdom.dateDisplay}",
                "Bondad, Disciplina y Ciencia",
                "«Misericordias Domini in aeternum cantabo» (Canto Litúrgico)"
            )
        }

        val steps = when (lang) {
            AppLanguage.EN -> listOf(
                RezandoVoyStep(
                    title = "1. We prepare in stillness and quiet reverence",
                    subtitle = "Place yourself in the loving presence of God our Father",
                    iconName = "spa",
                    body = "Find a peaceful and comfortable posture. Gently close your eyes and breathe slowly and deeply three times. Lay aside the rush, pending chores, and worries of the day. Recall Saint Basil the Great's wisdom: «The soul in silence is like a tranquil lake reflecting the light of God». Settle your heart to listen with faith to Jesus Christ who comes to meet you.",
                    audioDurationSeconds = 85
                ),
                RezandoVoyStep(
                    title = "2. Sacred Scripture of the Day",
                    subtitle = wisdom.gospelPassage,
                    iconName = "auto_stories",
                    body = "${wisdom.gospelText}\n\nThe Gospel of the Lord. Praise to you, Lord Jesus Christ.",
                    audioDurationSeconds = 135
                ),
                RezandoVoyStep(
                    title = "3. Reflection & Heart Meditation",
                    subtitle = "What is the Word speaking to your life today?",
                    iconName = "lightbulb",
                    body = "• Living Word that resounds:\n${wisdom.singleGospelPhrase}\n\n• Prayerful contemplation:\nJesus does not speak to us with cold decrees, but as the faithful Friend who reveals the joyful mysteries of the Kingdom. Today, where is the Lord directing your loving gaze and peaceful intentions?\n\n• For your daily journey:\nIn what concrete corner of your family, work, studies, or community is God inviting you to show greater compassion, tenderness, and freedom of spirit?",
                    audioDurationSeconds = 130
                ),
                RezandoVoyStep(
                    title = "4. Wisdom of Saint Basil the Great",
                    subtitle = wisdom.basilianTeachingTitle,
                    iconName = "volunteer_activism",
                    body = "${wisdom.basilianTeachingContent}\n\n«${wisdom.basilianDailyQuote}»\n— Source: ${wisdom.patristicSource}",
                    audioDurationSeconds = 120
                ),
                RezandoVoyStep(
                    title = "5. Fraternal Prayer, Colloquy & Blessing",
                    subtitle = "Speak with the Lord as a friend speaks with a friend",
                    iconName = "send",
                    body = "Lord Jesus, we thank you for your Holy Word and for the charism you inspired in Saint Basil the Great and our ten founders. Grant us today the grace to walk in your peace, forgiving with an open heart, comforting the sorrowful, and being joyful witnesses of your mercy.\n\nOur Father, who art in heaven, hallowed be thy name; thy kingdom come; thy will be done on earth as it is in heaven. Give us this day our daily bread; and forgive us our trespasses, as we forgive those who trespass against us; and lead us not into temptation, but deliver us from evil. Amen.\n\nSaint Basil the Great, pray for us.\nIn the name of the Father, and of the Son, and of the Holy Spirit. Amen.",
                    audioDurationSeconds = 95
                )
            )
            AppLanguage.FR -> listOf(
                RezandoVoyStep(
                    title = "1. Recueillement intérieur et silence paisible",
                    subtitle = "Placez-vous sous le regard aimant de Dieu le Père",
                    iconName = "spa",
                    body = "Trouvez une posture calme et confortable. Fermez doucement les yeux et respirez lentement et profondément à trois reprises. Déposez vos fardeaux, vos hâte et vos soucis. Souvenez-vous de la parole de saint Basile le Grand : « L'âme dans le silence est comme un lac paisible où se reflète la clarté de Dieu ». Disposez votre cœur à écouter le Seigneur Jésus avec amour et foi.",
                    audioDurationSeconds = 85
                ),
                RezandoVoyStep(
                    title = "2. Sainte Écriture du Jour",
                    subtitle = wisdom.gospelPassage,
                    iconName = "auto_stories",
                    body = "${wisdom.gospelText}\n\nAcclamons la Parole de Dieu. Louange à toi, Seigneur Jésus.",
                    audioDurationSeconds = 135
                ),
                RezandoVoyStep(
                    title = "3. Pistes de Méditation et de Prière",
                    subtitle = "Que vous dit la Parole aujourd'hui pour votre vie ?",
                    iconName = "lightbulb",
                    body = "• Parole vivante qui résonne dans votre cœur :\n${wisdom.singleGospelPhrase}\n\n• Méditation orante :\nJésus ne nous instruit pas avec de froides doctrines, mais comme l'Ami fidèle qui nous dévoile la joie du Royaume des Cieux. Aujourd'hui, vers qui le Seigneur oriente-t-il votre bienveillance ?\n\n• Application dans votre journée :\nDans quel aspect de votre famille, travail ou communauté ressentez-vous l'appel du Christ à faire preuve de plus de douceur, de patience et de vérité fraternelle ?",
                    audioDurationSeconds = 130
                ),
                RezandoVoyStep(
                    title = "4. Sagesse de Saint Basile le Grand",
                    subtitle = wisdom.basilianTeachingTitle,
                    iconName = "volunteer_activism",
                    body = "${wisdom.basilianTeachingContent}\n\n«${wisdom.basilianDailyQuote}»\n— Source : ${wisdom.patristicSource}",
                    audioDurationSeconds = 120
                ),
                RezandoVoyStep(
                    title = "5. Prière Fraternelle, Colloque & Envoi",
                    subtitle = "Parlez avec le Seigneur comme un ami parle à son ami",
                    iconName = "send",
                    body = "Seigneur Jésus, merci pour ta sainte Parole et pour le charisme d'éducation et de charité inspiré à saint Basile et à nos dix fondateurs. Accorde-nous de vivre ce jour dans ta paix, en pardonnant avec tendresse et en servant les plus vulnérables avec joie.\n\nNotre Père, qui es aux cieux, que ton nom soit sanctifié, que ton règne vienne, que ta volonté soit faite sur la terre comme au ciel. Donne-nous aujourd'hui notre pain de ce jour. Pardonne-nous nos offenses, comme nous pardonnons aussi à ceux qui nous ont offensés. Et ne nous laisse pas entrer en tentation, mais délivre-nous du Mal. Amen.\n\nSaint Basile le Grand, priez pour nous.\nAu nom du Père, et du Fils, et du Saint-Esprit. Amen.",
                    audioDurationSeconds = 95
                )
            )
            AppLanguage.ES -> listOf(
                RezandoVoyStep(
                    title = "1. Nos preparamos en silencio y recogimiento",
                    subtitle = "Ponte en la presencia de Dios Padre y abre tu corazón a la gracia",
                    iconName = "spa",
                    body = "Busca una postura cómoda y serena. Cierra suavemente los ojos y respira lenta y profundamente tres veces. Deja a un lado las prisas, las tareas pendientes y las distracciones del día. Recuerda la enseñanza de San Basilio Magno: «El alma en silencio es como un lago tranquilo donde se refleja la luz de Dios». Disponte a escuchar con fe al Señor Jesús que te sale al encuentro.",
                    audioDurationSeconds = 85
                ),
                RezandoVoyStep(
                    title = "2. Lectura Sagrada de la Palabra",
                    subtitle = wisdom.gospelPassage,
                    iconName = "auto_stories",
                    body = "${wisdom.gospelText}\n\nPalabra del Señor.",
                    audioDurationSeconds = 135
                ),
                RezandoVoyStep(
                    title = "3. Pistas de Reflexión y Meditación",
                    subtitle = "¿Qué nos dice la Palabra hoy a nuestra vida?",
                    iconName = "lightbulb",
                    body = "• Palabra viva que resuena:\n${wisdom.singleGospelPhrase}\n\n• Meditación orante:\nJesús no nos habla como quien da normas frías, sino como el amigo que revela los misterios del Reino de los Cielos. En este día, ¿hacia dónde orienta el Señor tu mirada y tus intenciones?\n\n• Aplicación a tu camino:\n¿En qué aspecto concreto de tu familia, comunidad, estudio o trabajo sientes la invitación de Dios a actuar con mayor compasión, verdad y libertad interior?",
                    audioDurationSeconds = 130
                ),
                RezandoVoyStep(
                    title = "4. Sabiduría de San Basilio Magno",
                    subtitle = wisdom.basilianTeachingTitle,
                    iconName = "volunteer_activism",
                    body = "${wisdom.basilianTeachingContent}\n\n«${wisdom.basilianDailyQuote}»\n— Fuente: ${wisdom.patristicSource}",
                    audioDurationSeconds = 120
                ),
                RezandoVoyStep(
                    title = "5. Coloquio, Oración y Envío",
                    subtitle = "Habla con el Señor como un amigo con su amigo y recibe la bendición",
                    iconName = "send",
                    body = "Señor Jesús, te damos gracias por tu Santa Palabra y por el carisma que inspiraste en San Basilio Magno y en los fundadores de nuestra comunidad. Concédenos caminar hoy en tu paz, perdonando de corazón a quienes nos ofenden, consolando a los afligidos y siendo testigos alegres de tu misericordia.\n\nPadre Nuestro, que estás en el cielo, santificado sea tu Nombre; venga a nosotros tu Reino; hágase tu voluntad en la tierra como en el cielo. Danos hoy nuestro pan de cada día; perdona nuestras ofensas, como también nosotros perdonamos a los que nos ofenden; no nos dejes caer en la tentación, y líbranos del mal. Amén.\n\nSan Basilio Magno, ruega por nosotros.\nEn el nombre del Padre, y del Hijo, y del Espíritu Santo. Amén.",
                    audioDurationSeconds = 95
                )
            )
        }

        return RezandoVoySession(
            id = "hoy_meditacion_${wisdom.monthOfYear}_${wisdom.dayOfMonth}",
            dayOfWeek = dayOfWeek,
            dateDisplay = dateDisplay,
            scriptureReference = wisdom.gospelPassage,
            mottoFocus = mottoFocus,
            hymnOrSongTitle = songTitle,
            steps = steps
        )
    }

    fun getAllSessions(wisdom: DailyLiturgicalWisdom, lang: AppLanguage = AppLanguage.ES): List<RezandoVoySession> {
        val todaySession = getTodaySession(wisdom, lang)
        return listOf(todaySession) + getThematicSessions(lang)
    }

    private fun getThematicSessions(lang: AppLanguage): List<RezandoVoySession> = when (lang) {
        AppLanguage.EN -> listOf(
            RezandoVoySession(
                id = "martes_camino",
                dayOfWeek = "Tuesday",
                dateDisplay = "Praying Today • The Call of Christ & Fraternal Life",
                scriptureReference = "John 15: 12-17",
                mottoFocus = "Goodness (Bonitatem)",
                hymnOrSongTitle = "«Ubi Caritas et Amor, Deus ibi est» (Taizé Chant)",
                steps = listOf(
                    RezandoVoyStep(
                        title = "1. We enter peaceful stillness",
                        subtitle = "Silence the noise and rest in the Father's tender embrace",
                        iconName = "spa",
                        body = "Take a comfortable posture. Breathe slowly and deeply three times. Lay down your hurry and worries. Hold firmly the truth that God loves you dearly. Hear within your heart the Basilian motto: «Teach me, O Lord, goodness, discipline, and knowledge». Christ meets you right here as a cherished friend.",
                        audioDurationSeconds = 90
                    ),
                    RezandoVoyStep(
                        title = "2. The Holy Word of God",
                        subtitle = "Sacred Reading: Saint John (15: 12-17)",
                        iconName = "auto_stories",
                        body = "Jesus said to his disciples: «This is my commandment: love one another as I have loved you. Greater love has no one than this: to lay down one's life for one's friends. You are my friends if you do what I command. I no longer call you servants, because a servant does not know his master's business. Instead, I have called you friends, for everything that I learned from my Father I have made known to you. You did not choose me, but I chose you and appointed you so that you might go and bear fruit—fruit that will last. This is my command: Love each other». The Gospel of the Lord.",
                        audioDurationSeconds = 110
                    ),
                    RezandoVoyStep(
                        title = "3. Contemplative Clues",
                        subtitle = "What is Jesus inviting you to through His Word?",
                        iconName = "lightbulb",
                        body = "• «You did not choose me, but I chose you»: Remember with gratitude how God entered your story. You are not alone; you were dreamed and called by unconditional love.\n\n• «Love each other as I have loved you»: As our Basilian Constitutions remind us, the heart of Christian life is the fraternal home where together we worship, dine, and live with sincere joy.\n\n• With whom do you find it hard to show patience or kindness today? Ask Jesus for his meek and gentle heart.",
                        audioDurationSeconds = 130
                    ),
                    RezandoVoyStep(
                        title = "4. Basilian Community Prayer",
                        subtitle = "Prayer for Life in Common (CSB Colombia Tradition)",
                        iconName = "volunteer_activism",
                        body = "Lord, grant that in love we may be sincere;\nthat we abhor evil and hold fast to all that is good;\nthat in fraternal love we show genuine affection to one another;\nthat in mutual respect we honor others above ourselves;\nthat in the fulfillment of our duty we never grow lazy;\nthat in spirit we remain fervent;\nthat we maintain joyful hope and patient perseverance;\nand that we generously welcome those in need and travelers passing by.\n\nLord, enable us to live in peaceful harmony with all people. Amen.",
                        audioDurationSeconds = 95
                    ),
                    RezandoVoyStep(
                        title = "5. Colloquy & Sending Forth",
                        subtitle = "Speak with Jesus as a friend speaks with a friend",
                        iconName = "send",
                        body = "Talk spontaneously with Jesus. Thank Him for this sacred pause. Ask for the grace to bring kindness, warmth, and peace to your family, studies, or workplace today.\n\nGlory be to the Father, and to the Son, and to the Holy Spirit. As it was in the beginning, is now, and ever shall be, world without end. Amen.\n\nSaint Basil the Great, pray for us.",
                        audioDurationSeconds = 75
                    )
                )
            ),
            RezandoVoySession(
                id = "domingo_taize",
                dayOfWeek = "Sunday",
                dateDisplay = "Praying Today • Sunday Evening Candlelight Adoration (Taizé)",
                scriptureReference = "Luke 24: 28-35",
                mottoFocus = "Wisdom & Knowledge (Scientiam)",
                hymnOrSongTitle = "«Jesus, Bread of Life» • Candlelight Stillness",
                steps = listOf(
                    RezandoVoyStep(
                        title = "1. Entering the Night",
                        subtitle = "At 9:00 p.m. • In communion with the tradition of Basilian houses",
                        iconName = "nightlight",
                        body = "As is the tradition in Basilian communities, at the fall of Sunday night we gather at the feet of Jesus in the Blessed Sacrament. The gentle light of a candle and quiet stillness invite us to bring before God all that we lived this week: the joys, the weariness, the struggles, and the shared hopes.",
                        audioDurationSeconds = 100
                    ),
                    RezandoVoyStep(
                        title = "2. The Holy Word of God",
                        subtitle = "Sacred Reading: Saint Luke (24: 28-32)",
                        iconName = "auto_stories",
                        body = "As they approached the village to which they were going, Jesus acted as if he were going farther. But they urged him strongly: «Stay with us, for it is nearly evening; the day is almost over». So he went in to stay with them. When he was at the table with them, he took bread, gave thanks, broke it and began to give it to them. Then their eyes were opened and they recognized him, and he disappeared from their sight. They asked each other: «Were not our hearts burning within us while he talked with us on the road and opened the Scriptures to us?». The Gospel of the Lord.",
                        audioDurationSeconds = 115
                    ),
                    RezandoVoyStep(
                        title = "3. Quiet Adoration",
                        subtitle = "«Stay with us, Lord»",
                        iconName = "lightbulb",
                        body = "• Look back over the past week: where did your heart burn with quiet joy? Where did you sense the discreet presence of the Risen Christ?\n\n• Entrust to Jesus the sick, the lonely, those undergoing anxiety or sorrow, and young people searching for meaning.\n\n• Rest peacefully in the sanctuary of His love.",
                        audioDurationSeconds = 130
                    ),
                    RezandoVoyStep(
                        title = "4. Song of Simeon (Nunc Dimittis)",
                        subtitle = "Evening Liturgy of the Church",
                        iconName = "volunteer_activism",
                        body = "Now, Master, you may let your servant go in peace, according to your word;\nfor my eyes have seen your salvation,\nwhich you prepared in the sight of all the peoples:\na light for revelation to the Gentiles,\nand the glory of your people Israel.\n\nGlory to the Father, and to the Son, and to the Holy Spirit. Amen.",
                        audioDurationSeconds = 85
                    ),
                    RezandoVoyStep(
                        title = "5. Salve Regina & Restful Night",
                        subtitle = "Under the maternal mantle of the Blessed Virgin Mary",
                        iconName = "send",
                        body = "Hail, holy Queen, mother of mercy, hail, our life, our sweetness, and our hope. To thee do we cry, poor banished children of Eve. To thee do we send up our sighs, mourning and weeping in this valley of tears. Turn then, most gracious advocate, thine eyes of mercy toward us, and after this our exile, show unto us the blessed fruit of thy womb, Jesus. O clement, O loving, O sweet Virgin Mary. Pray for us, O Holy Mother of God, that we may be made worthy of the promises of Christ. May the Lord grant us a restful night and a peaceful end. Amen.",
                        audioDurationSeconds = 90
                    )
                )
            )
        )
        AppLanguage.FR -> listOf(
            RezandoVoySession(
                id = "martes_camino",
                dayOfWeek = "Mardi",
                dateDisplay = "Prier Aujourd'hui • L'Appel du Christ & la Vie Fraternelle",
                scriptureReference = "Saint Jean 15, 12-17",
                mottoFocus = "Bonté (Bonitatem)",
                hymnOrSongTitle = "«Ubi Caritas et Amor, Deus ibi est» (Chant de Taizé)",
                steps = listOf(
                    RezandoVoyStep(
                        title = "1. Silence et paix intérieure",
                        subtitle = "Déposez vos bruits et reposez-vous sous le regard du Père",
                        iconName = "spa",
                        body = "Prenez une posture paisible. Respirez lentement et profondément à trois reprises. Déposez vos hâte et vos soucis. Gardez la certitude que Dieu vous aime tendrement. Écoutez au fond de votre cœur la devise basilienne : « Enseigne-moi, Seigneur, la bonté, la discipline et la science ». Le Christ vient à votre rencontre comme un ami très cher.",
                        audioDurationSeconds = 90
                    ),
                    RezandoVoyStep(
                        title = "2. La Sainte Parole de Dieu",
                        subtitle = "Sainte Lecture : Saint Jean (15, 12-17)",
                        iconName = "auto_stories",
                        body = "En ce temps-là, Jésus disait à ses disciples : « Mon commandement, le voici : Aimez-vous les uns les autres comme je vous ai aimés. Il n'y a pas de plus grand amour que de donner sa vie pour ses amis. Vous êtes mes amis si vous faites ce que je vous commande. Je ne vous appelle plus serviteurs, car le serviteur ne sait pas ce que fait son maître ; je vous appelle amis, parce que tout ce que j'ai appris de mon Père, je vous l'ai fait connaître. Ce n'est pas vous qui m'avez choisi, c'est moi qui vous ai choisis et établis afin que vous alliez, que vous portiez du fruit, et que votre fruit demeure. Ce que je vous commande, c'est de vous aimer les uns les autres ». Parole du Seigneur.",
                        audioDurationSeconds = 110
                    ),
                    RezandoVoyStep(
                        title = "3. Pistes de Méditation Fraternelle",
                        subtitle = "À quoi Jésus vous invite-t-il par sa Parole ?",
                        iconName = "lightbulb",
                        body = "• « Ce n'est pas vous qui m'avez choisi, c'est moi qui vous ai choisis » : Rappelez-vous avec gratitude la manière dont Dieu est entré dans votre vie. Vous n'êtes pas seul ; vous avez été espéré et aimé.\n\n• « Aimez-vous comme je vous ai aimés » : Comme nous le rappellent nos Constitutions, le cœur de la vie chrétienne est la maison fraternelle où nous partageons la prière et la joie simple.\n\n• Envers qui avez-vous du mal à faire preuve de patience aujourd'hui ? Demandez au Christ sa douceur.",
                        audioDurationSeconds = 130
                    ),
                    RezandoVoyStep(
                        title = "4. Prière Communautaire Basilienne",
                        subtitle = "Prière pour la Vie en Commun (Tradition CSB)",
                        iconName = "volunteer_activism",
                        body = "Seigneur, accorde-nous d'être sincères dans l'amour ;\nde rejeter le mal et de chérir tout ce qui est bon ;\nd'avoir une affection chaleureuse les uns pour les autres ;\nd'estimer les autres supérieurs à nous-mêmes ;\nd'être zélés dans nos devoirs et fervents dans l'Esprit ;\nde garder l'espérance et la joie dans l'épreuve ;\net d'accueillir avec générosité celui qui frappe à notre porte.\n\nSeigneur, fais de nous des artisans de réconciliation et de paix. Amen.",
                        audioDurationSeconds = 95
                    ),
                    RezandoVoyStep(
                        title = "5. Colloque & Bénédiction",
                        subtitle = "Parlez avec Jésus comme un ami parle à son ami",
                        iconName = "send",
                        body = "Parlez spontanément au Seigneur Jésus. Remerciez-le pour cet instant de paix. Demandez-lui la grâce de porter la bonté et l'écoute dans votre journée.\n\nGloire au Père, et au Fils, et au Saint-Esprit. Comme il était au commencement, maintenant et toujours, dans les siècles des siècles. Amen.\n\nSaint Basile le Grand, priez pour nous.",
                        audioDurationSeconds = 75
                    )
                )
            ),
            RezandoVoySession(
                id = "domingo_taize",
                dayOfWeek = "Dimanche",
                dateDisplay = "Prier Aujourd'hui • Adoration du Dimanche Soir à la Bougie (Taizé)",
                scriptureReference = "Saint Luc 24, 28-35",
                mottoFocus = "Science & Sagesse (Scientiam)",
                hymnOrSongTitle = "«Jésus, Pain de Vie» • Prière contemplative",
                steps = listOf(
                    RezandoVoyStep(
                        title = "1. Entrée dans la Nuit",
                        subtitle = "À 21h00 • En communion avec les maisons de la congrégation",
                        iconName = "nightlight",
                        body = "Selon la tradition des communautés basiliennes, le dimanche soir nous nous recueillons devant le Saint-Sacrement. La douce clarté d'une bougie et le silence nous invitent à déposer notre semaine : les joies, les fatigues et les espérances partagées.",
                        audioDurationSeconds = 100
                    ),
                    RezandoVoyStep(
                        title = "2. La Sainte Parole de Dieu",
                        subtitle = "Sainte Lecture : Saint Luc (24, 28-32)",
                        iconName = "auto_stories",
                        body = "Quand ils approchèrent du village où ils se rendaient, Jésus fit semblant d'aller plus loin. Mais ils s'efforcèrent de le retenir : « Reste avec nous, car le soir tombe et le jour baisse ». Il entra donc pour rester avec eux. Quand il fut à table avec eux, il prit le pain, dit la bénédiction, le rompit et le leur donna. Alors leurs yeux s'ouvrirent, et ils le reconnurent, mais il disparut de leur regard. Ils se dirent l'un à l'autre : « Notre cœur n'était-il pas brûlant en nous, tandis qu'il nous parlait sur la route et nous ouvrait les Écritures ? ». Parole du Seigneur.",
                        audioDurationSeconds = 115
                    ),
                    RezandoVoyStep(
                        title = "3. Méditation Contemplative",
                        subtitle = "« Reste avec nous, Seigneur »",
                        iconName = "lightbulb",
                        body = "• Relisez la semaine qui s'achève : où votre cœur a-t-il été touché par la grâce ?\n\n• Confiez au Seigneur les malades, les personnes isolées et les jeunes en quête d'espérance.\n\n• Reposez-vous dans le secret de son amour.",
                        audioDurationSeconds = 130
                    ),
                    RezandoVoyStep(
                        title = "4. Cantique de Siméon (Nunc Dimittis)",
                        subtitle = "Liturgie de la nuit dans l'Église",
                        iconName = "volunteer_activism",
                        body = "Maintenant, ô Maître souverain, tu peux laisser ton serviteur s'en aller en paix, selon ta parole.\nCar mes yeux ont vu le salut que tu préparais à la face des peuples :\nlumière qui se révèle aux nations et gloire d'Israël ton peuple.\n\nGloire au Père, et au Fils, et au Saint-Esprit. Amen.",
                        audioDurationSeconds = 85
                    ),
                    RezandoVoyStep(
                        title = "5. Salve Regina & Nuit Paisible",
                        subtitle = "Sous la protection maternelle de la Vierge Marie",
                        iconName = "send",
                        body = "Salve, Regina, mater misericordiae ; vita, dulcedo et spes nostra, salve. Ad te clamamus, exsules filii Hevae. Ad te suspiramus, gementes et flentes in hac lacrimarum valle. Eia ergo, advocata nostra, illos tuos misericordes oculos ad nos converte. Et Iesum, benedictum fructum ventris tui, nobis post hoc exsilium ostende. O clemens, o pia, o dulcis Virgo Maria. Priez pour nous, Sainte Mère de Dieu, afin que nous devenions dignes des promesses du Christ. Que le Seigneur nous accorde une nuit paisible et une sainte fin. Amen.",
                        audioDurationSeconds = 90
                    )
                )
            )
        )
        AppLanguage.ES -> defaultThematicSessions
    }

    val defaultThematicSessions: List<RezandoVoySession> by lazy {
        listOf(
            RezandoVoySession(
                id = "martes_camino",
                dayOfWeek = "Martes",
                dateDisplay = "Rezando Hoy • El Llamado de Cristo y la Vida en Común",
                scriptureReference = "San Juan 15: 12-17",
                mottoFocus = "Bondad (Bonitatem)",
                hymnOrSongTitle = "«Ubi Caritas et Amor, Deus ibi est» (Canto de Taizé)",
                steps = listOf(
                    RezandoVoyStep(
                        title = "1. Nos preparamos en silencio",
                        subtitle = "Silencia el ruido del día y ponte en presencia del Padre",
                        iconName = "spa",
                        body = "Busca una postura cómoda. Respira lenta y profundamente tres veces. Deja a un lado las prisas, las tareas pendientes y las preocupaciones. Trae a tu corazón la certeza de que Dios te ama entrañablemente. Escucha en tu interior el lema basiliano: «Enséñame, Señor, bondad, disciplina y conocimiento». Aquí y ahora, Jesús te sale al encuentro como a sus amigos.",
                        audioDurationSeconds = 90
                    ),
                    RezandoVoyStep(
                        title = "2. La Palabra de Dios",
                        subtitle = "Lectura Sagrada: San Juan (15, 12-17)",
                        iconName = "auto_stories",
                        body = "En aquel tiempo dijo Jesús a sus discípulos: «Este es mi mandamiento: que se amen los unos a los otros como yo los he amado. Nadie tiene mayor amor que el que da la vida por sus amigos. Ustedes son mis amigos si hacen lo que yo les mando. Ya no los llamo siervos, porque el siervo no sabe lo que hace su señor; los llamo amigos, porque les he dado a conocer todo lo que he oído de mi Padre. No fueron ustedes quienes me eligieron a mí; fui yo quien los elegí a ustedes y los destiné para que vayan y den fruto, y un fruto que permanezca; de modo que todo lo que pidan al Padre en mi nombre, Él se lo conceda. Lo que les mando es esto: que se amen unos a otros». Palabra del Señor.",
                        audioDurationSeconds = 110
                    ),
                    RezandoVoyStep(
                        title = "3. Pistas de Reflexión",
                        subtitle = "¿A qué te invita Jesús a través de su Palabra?",
                        iconName = "lightbulb",
                        body = "• «No me eligieron ustedes a mí, soy yo quien los elegí»: Recuerda con gratitud cómo llegó Dios a tu vida. No estamos solos; fuimos soñados y convocados por un amor incondicional.\n\n• «Que se amen como yo los he amado»: Como nos recuerdan las Constituciones Basilianas (FVB 34), el corazón de la vida cristiana es la casa fraterna, donde juntos adoramos a Dios, juntos comemos y nos recreamos con sinceridad y acogida.\n\n• ¿Con quién te cuesta hoy vivir la bondad o la paciencia? Pide a Jesús su misma mansedumbre para acoger al hermano sin juzgar ni condenar.",
                        audioDurationSeconds = 130
                    ),
                    RezandoVoyStep(
                        title = "4. Oración Comunitaria Basiliana",
                        subtitle = "Oración por la Vida en Común (Tradición Colombia 1987)",
                        iconName = "volunteer_activism",
                        body = "Señor, concédenos que en el amor seamos sinceros;\nque aborrezcamos el mal y cuidemos todo lo bueno;\nque en el amor entre hermanos nos demostremos cariño unos a otros;\nque en el respeto estimemos a los otros como más dignos;\nque en el cumplimiento del deber no seamos flojos;\nque en el espíritu seamos fervorosos;\nque tengamos esperanza y estemos alegres;\nque en las pruebas seamos pacientes;\nque con los necesitados compartamos con ellos;\nque con los que estén de paso seamos solícitos para recibirlos en casa.\n\nSeñor, que hagamos todo lo posible en cuanto a nosotros dependa, para vivir en paz con todos. Amén.",
                        audioDurationSeconds = 95
                    ),
                    RezandoVoyStep(
                        title = "5. Coloquio y Envío",
                        subtitle = "Habla con Jesús como un amigo habla con otro amigo",
                        iconName = "send",
                        body = "Conversa espontáneamente con el Señor Jesús. Dale gracias por este instante de encuentro. Pídele la gracia de llevar bondad a tu hogar, a tu estudio o a tu trabajo durante el día de hoy.\n\nGloria al Padre, y al Hijo, y al Espíritu Santo. Como era en el principio, ahora y siempre, por los siglos de los siglos. Amén.\n\nSan Basilio Magno, ruega por nosotros.",
                        audioDurationSeconds = 75
                    )
                )
            ),
            RezandoVoySession(
                id = "domingo_taize",
                dayOfWeek = "Domingo",
                dateDisplay = "Rezando Hoy • Adoración del Domingo en la Noche (Taizé)",
                scriptureReference = "San Lucas 24: 28-35",
                mottoFocus = "Ciencia y Sabiduría (Scientiam)",
                hymnOrSongTitle = "«Jesús, Pan de Vida» • A la luz de las velas",
                steps = listOf(
                    RezandoVoyStep(
                        title = "1. Entrada en la Noche",
                        subtitle = "A las 9:00 p.m. • En comunión con la tradición de las casas basilianas",
                        iconName = "nightlight",
                        body = "Como es costumbre en las casas basilianas de Colombia (Costumbres 2.3), al caer la noche del domingo nos reunimos a los pies de Jesús en el Santísimo Sacramento. La luz tenue de una vela y el silencio nos invitan a recapitular todo lo vivido en la semana: las alegrías, los cansancios, las luchas y las esperanzas compartidas.",
                        audioDurationSeconds = 100
                    ),
                    RezandoVoyStep(
                        title = "2. La Palabra de Dios",
                        subtitle = "Lectura Sagrada: San Lucas (24, 28-32)",
                        iconName = "auto_stories",
                        body = "Al acercarse al pueblo adonde iban, Jesús hizo ademán de seguir adelante. Pero ellos le rogaron con insistencia: «Quédate con nosotros, porque ya es tarde y el día se acaba». Entró, pues, para quedarse con ellos. Estando a la mesa, tomó el pan, pronunció la bendición, lo partió y se lo dio. Entonces se les abrieron los ojos y lo reconocieron, pero Él desapareció de su vista. Y se decían el uno al otro: «¿No ardía acaso nuestro corazón en el camino cuando nos hablaba y nos explicaba las Escrituras?». Palabra del Señor.",
                        audioDurationSeconds = 115
                    ),
                    RezandoVoyStep(
                        title = "3. Meditación Contemplativa",
                        subtitle = "«Quédate con nosotros, Señor»",
                        iconName = "lightbulb",
                        body = "• Repasa la semana que concluye: ¿dónde ardió tu corazón? ¿dónde sentiste la presencia discreta del Resucitado?\n\n• Presenta a Jesús Sacramentado a los enfermos, a los que sufren violencia o desánimo, y a los jóvenes que buscan sentido para sus vidas.\n\n• Descansa en el Sagrario de su amor.",
                        audioDurationSeconds = 130
                    ),
                    RezandoVoyStep(
                        title = "4. Cántico de Simeón (Nunc Dimittis)",
                        subtitle = "Oración de la noche en la liturgia eclesial",
                        iconName = "volunteer_activism",
                        body = "Ahora, Señor, según tu promesa,\npuedes dejar a tu siervo irse en paz;\nporque mis ojos han visto a tu Salvador,\na quien has presentado ante todos los pueblos:\nluz para alumbrar a las naciones\ny gloria de tu pueblo Israel.\n\nGloria al Padre, y al Hijo, y al Espíritu Santo. Amén.",
                        audioDurationSeconds = 85
                    ),
                    RezandoVoyStep(
                        title = "5. Salve Regina y Descanso",
                        subtitle = "Bajo el amparo maternal de la Virgen María",
                        iconName = "send",
                        body = "Salve, Regina, mater misericordiae; vita, dulcedo et spes nostra, salve. Ad te clamamus, exsules filii Hevae. Ad te suspiramus, gementes et flentes in hac lacrimarum valle. Eia ergo, advocata nostra, illos tuos misericordes oculos ad nos converte. Et Iesum, benedictum fructum ventris tui, nobis post hoc exsilium ostende. O clemens, o pia, o dulcis Virgo Maria. Ruega por nosotros, Santa Madre de Dios, para que seamos dignos de alcanzar las promesas de Cristo. Que el Señor nos conceda una noche tranquila y una santa muerte. Amén.",
                        audioDurationSeconds = 90
                    )
                )
            )
        )
    }

    val sessions: List<RezandoVoySession> get() = defaultThematicSessions

    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
