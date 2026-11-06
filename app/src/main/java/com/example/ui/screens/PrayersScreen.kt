package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.DailyPrayerStatus
import com.example.data.model.PrayerItem
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.BasilianViewModel

@Composable
fun PrayersScreen(
    viewModel: BasilianViewModel,
    appLanguage: AppLanguage = AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val strings = remember(appLanguage) { AppStrings.get(appLanguage) }
    val prayers = viewModel.prayersList
    val prayerStatus by viewModel.todayPrayerStatus.collectAsState()
    val isSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val selectedPrayer by viewModel.selectedPrayer.collectAsState()

    var filterTime by remember { mutableStateOf("TODAS") } // "TODAS", "HORAS", "DEVOCIONARIO", "EXAMEN"
    var showExamenDialog by remember { mutableStateOf(false) }

    val filteredPrayers = remember(filterTime) {
        when (filterTime) {
            "HORAS" -> prayers.filter { it.id in listOf("laudes", "angelus", "visperas", "completas") }
            "DEVOCIONARIO" -> prayers.filter { it.id !in listOf("laudes", "angelus", "visperas", "completas") }
            else -> prayers
        }
    }

    val examenTitle = when (appLanguage) {
        AppLanguage.EN -> "Daily Examination of Conscience"
        AppLanguage.FR -> "Examen de Conscience Quotidien"
        AppLanguage.ES -> "Examen de Conciencia Diario"
    }
    val examenSubtitle = when (appLanguage) {
        AppLanguage.EN -> "5 contemplative steps from the Basilian formation tradition"
        AppLanguage.FR -> "5 étapes contemplatives de la tradition de formation basilienne"
        AppLanguage.ES -> "5 pasos contemplativos de la tradición de formación basiliana"
    }
    val startBtnLabel = when (appLanguage) {
        AppLanguage.EN -> "Begin"
        AppLanguage.FR -> "Commencer"
        AppLanguage.ES -> "Iniciar"
    }
    val allChipLabel = when (appLanguage) {
        AppLanguage.EN -> "All (${prayers.size})"
        AppLanguage.FR -> "Toutes (${prayers.size})"
        AppLanguage.ES -> "Todas (${prayers.size})"
    }
    val hoursChipLabel = when (appLanguage) {
        AppLanguage.EN -> "Liturgy of the Hours"
        AppLanguage.FR -> "Liturgie des Heures"
        AppLanguage.ES -> "Liturgia de las Horas"
    }
    val devotionalChipLabel = when (appLanguage) {
        AppLanguage.EN -> "CSB Devotional"
        AppLanguage.FR -> "Dévotionnaire CSB"
        AppLanguage.ES -> "Devocionario CSB"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Examen de Conciencia Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clickable { showExamenDialog = true }
                .testTag("examen_conciencia_banner"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BasilianNavy)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = BasilianGold,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Bedtime,
                            contentDescription = null,
                            tint = BasilianNavy,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = examenTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = examenSubtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = BasilianGoldLight,
                        fontSize = 11.sp
                    )
                }
                FilledTonalButton(
                    onClick = { showExamenDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = BasilianGold,
                        contentColor = BasilianNavy
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(startBtnLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = filterTime == "TODAS",
                onClick = { filterTime = "TODAS" },
                label = { Text(allChipLabel, fontSize = 11.sp) }
            )
            FilterChip(
                selected = filterTime == "HORAS",
                onClick = { filterTime = "HORAS" },
                label = { Text(hoursChipLabel, fontSize = 11.sp) }
            )
            FilterChip(
                selected = filterTime == "DEVOCIONARIO",
                onClick = { filterTime = "DEVOCIONARIO" },
                label = { Text(devotionalChipLabel, fontSize = 11.sp) }
            )
        }

        // List of Prayers
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredPrayers, key = { it.id }) { rawPrayer ->
                val prayer = com.example.data.repository.BasilianContentLocalization.getLocalizedPrayer(rawPrayer, appLanguage)
                val isDone = when (prayer.id) {
                    "laudes" -> prayerStatus?.laudesDone == true
                    "angelus" -> prayerStatus?.angelusDone == true
                    "visperas" -> prayerStatus?.visperasDone == true
                    "completas" -> prayerStatus?.completasDone == true
                    else -> false
                }

                PrayerItemCard(
                    prayer = prayer,
                    isDone = isDone,
                    isSpeaking = isSpeaking,
                    appLanguage = appLanguage,
                    onToggleDone = { viewModel.togglePrayer(prayer.id) },
                    onSpeak = {
                        val fullText = "${prayer.title}. ${prayer.subtitle}. ${prayer.prayerText}"
                        viewModel.speakText(fullText)
                    },
                    onExpand = { viewModel.selectPrayer(prayer) }
                )
            }
        }
    }

    // Detail Dialog for prayer
    selectedPrayer?.let { rawPrayer ->
        val prayer = com.example.data.repository.BasilianContentLocalization.getLocalizedPrayer(rawPrayer, appLanguage)
        val closeText = strings.common.close
        val listenText = if (isSpeaking) {
            when (appLanguage) {
                AppLanguage.EN -> "Stop"
                AppLanguage.FR -> "Arrêter"
                AppLanguage.ES -> "Detener"
            }
        } else {
            when (appLanguage) {
                AppLanguage.EN -> "Listen"
                AppLanguage.FR -> "Écouter"
                AppLanguage.ES -> "Escuchar"
            }
        }
        val sourcePrefix = when (appLanguage) {
            AppLanguage.EN -> "Source: "
            AppLanguage.FR -> "Source : "
            AppLanguage.ES -> "Fuente: "
        }
        val reflectionLabel = when (appLanguage) {
            AppLanguage.EN -> "Spiritual Reflection:"
            AppLanguage.FR -> "Réflexion spirituelle :"
            AppLanguage.ES -> "Reflexión espiritual:"
        }
        AlertDialog(
            onDismissRequest = { viewModel.selectPrayer(null) },
            confirmButton = {
                Button(
                    onClick = { viewModel.selectPrayer(null) },
                    colors = ButtonDefaults.buttonColors(containerColor = BasilianBlue)
                ) {
                    Text(closeText)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        val fullText = "${prayer.title}. ${prayer.subtitle}. ${prayer.prayerText}"
                        viewModel.speakText(fullText)
                    }
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(listenText)
                }
            },
            title = {
                Column {
                    Text(
                        text = prayer.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BasilianNavy
                    )
                    prayer.latinTitle?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            color = BasilianGoldDark,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "$sourcePrefix${prayer.sourceDocument}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = prayer.prayerText,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = reflectionLabel,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = BasilianCrimson
                    )
                    Text(
                        text = prayer.reflection,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }

    // Examen de Conciencia Step-by-Step Dialog
    if (showExamenDialog) {
        ExamenConcienciaDialog(
            viewModel = viewModel,
            appLanguage = appLanguage,
            onDismiss = {
                viewModel.resetExamen()
                showExamenDialog = false
            }
        )
    }
}

@Composable
private fun PrayerItemCard(
    prayer: PrayerItem,
    isDone: Boolean,
    isSpeaking: Boolean,
    appLanguage: AppLanguage = AppLanguage.ES,
    onToggleDone: () -> Unit,
    onSpeak: () -> Unit,
    onExpand: () -> Unit
) {
    val markDoneDesc = when (appLanguage) {
        AppLanguage.EN -> "Mark as prayed today"
        AppLanguage.FR -> "Marquer comme priée aujourd'hui"
        AppLanguage.ES -> "Marcar como rezada hoy"
    }
    val readFullText = when (appLanguage) {
        AppLanguage.EN -> "Read full"
        AppLanguage.FR -> "Lire au complet"
        AppLanguage.ES -> "Leer completa"
    }
    val listenTooltip = when (appLanguage) {
        AppLanguage.EN -> "Listen to prayer"
        AppLanguage.FR -> "Écouter la prière"
        AppLanguage.ES -> "Escuchar oración"
    }
    val timeLabel = when (prayer.timeOfDay) {
        "Mañana" -> when (appLanguage) {
            AppLanguage.EN -> "Morning"
            AppLanguage.FR -> "Matin"
            AppLanguage.ES -> "Mañana"
        }
        "Mediodía" -> when (appLanguage) {
            AppLanguage.EN -> "Midday"
            AppLanguage.FR -> "Midi"
            AppLanguage.ES -> "Mediodía"
        }
        "Tarde" -> when (appLanguage) {
            AppLanguage.EN -> "Afternoon"
            AppLanguage.FR -> "Après-midi"
            AppLanguage.ES -> "Tarde"
        }
        "Noche" -> when (appLanguage) {
            AppLanguage.EN -> "Night"
            AppLanguage.FR -> "Nuit"
            AppLanguage.ES -> "Noche"
        }
        else -> prayer.timeOfDay
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onExpand)
            .testTag("prayer_card_${prayer.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDone) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
            else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            if (isDone) BasilianGold else ParchmentBorder
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = when (prayer.timeOfDay) {
                            "Mañana" -> BasilianGoldLight.copy(alpha = 0.3f)
                            "Mediodía" -> BasilianGold.copy(alpha = 0.3f)
                            "Tarde" -> BasilianCrimsonLight.copy(alpha = 0.2f)
                            "Noche" -> BasilianNavy.copy(alpha = 0.2f)
                            else -> BasilianBlue.copy(alpha = 0.2f)
                        },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = when (prayer.timeOfDay) {
                                    "Mañana" -> Icons.Default.WbSunny
                                    "Mediodía" -> Icons.Default.Brightness7
                                    "Tarde" -> Icons.Default.WbTwilight
                                    "Noche" -> Icons.Default.NightsStay
                                    else -> Icons.Default.Church
                                },
                                contentDescription = null,
                                tint = BasilianBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = prayer.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$timeLabel • ${prayer.sourceDocument}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = onToggleDone,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = markDoneDesc,
                        tint = if (isDone) BasilianGoldDark else MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = prayer.prayerText.take(180) + "...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onExpand,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(readFullText, fontSize = 12.sp, color = BasilianBlue)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = BasilianBlue
                    )
                }

                IconButton(
                    onClick = onSpeak,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = listenTooltip,
                        tint = BasilianBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExamenConcienciaDialog(
    viewModel: BasilianViewModel,
    appLanguage: AppLanguage = AppLanguage.ES,
    onDismiss: () -> Unit
) {
    val step by viewModel.examenStep.collectAsState()

    val stepsContent = when (appLanguage) {
        AppLanguage.EN -> listOf(
            Pair(
                "1. Presence and Thanksgiving",
                "Place yourself in the presence of God. Recall the words of Saint Basil: «We cannot spend a single moment without recognizing divine goodness». Thank the Lord for 3 concrete gifts or people with whom He blessed you today."
            ),
            Pair(
                "2. Request for Light from the Holy Spirit",
                "Invoke the Holy Spirit (Prayer J.25 of the Customs): «Holy Spirit, our protector, enlighten our minds with wisdom». Ask for the grace to look back on your day with evangelical sincerity, without selfish justifications."
            ),
            Pair(
                "3. Review of the Day (Goodness, Discipline, and Knowledge)",
                "Review the hours of the day from the morning:\n• Did I live with Goodness toward my neighbor, family, or colleagues?\n• Did I maintain the Discipline of my responsibilities without sloth?\n• Did I put Knowledge and intellect at the service of truth and those who suffer?"
            ),
            Pair(
                "4. Request for Forgiveness and Reconciliation",
                "Ask forgiveness for moments of harshness, impatience, judgment of a brother, or neglect of the needy. Jesus welcomes you with infinite mercy: «We hold this treasure in earthen vessels»."
            ),
            Pair(
                "5. Resolution and Trusting Surrender",
                "Surrender your rest into the hands of the Father: «Into your hands, Lord, I commend my spirit». Make a simple resolution for tomorrow: a kind word, a duty done on time, an act of selfless love."
            )
        )
        AppLanguage.FR -> listOf(
            Pair(
                "1. Présence et Action de Grâce",
                "Placez-vous en présence de Dieu. Rappelez-vous les paroles de saint Basile : « Nous ne pouvons passer un seul instant sans reconnaître la bonté divine ». Rendez grâce au Seigneur pour 3 cadeaux ou personnes concrètes dont Il vous a béni aujourd'hui."
            ),
            Pair(
                "2. Demande de Lumière à l'Esprit Saint",
                "Invoquez l'Esprit Saint (Prière J.25 des Coutumes) : « Esprit Saint, notre protecteur, illumine notre esprit de sagesse ». Demandez la grâce de regarder votre journée avec sincérité évangélique."
            ),
            Pair(
                "3. Revue de la Journée (Bonté, Discipline et Savoir)",
                "Parcourez les heures de la journée depuis le matin :\n• Ai-je vécu avec Bonté envers mon prochain, ma famille ou mes confrères ?\n• Ai-je gardé la Discipline de mes devoirs sans paresse ?\n• Ai-je mis le Savoir au service de la vérité et des souffrants ?"
            ),
            Pair(
                "4. Demande de Pardon et Réconciliation",
                "Demandez pardon pour les moments de rudesse, d'impatience ou de jugement. Jésus vous accueille avec miséricorde infinie : « Nous portons ce trésor dans des vases d'argile »."
            ),
            Pair(
                "5. Résolution et Abandon Confiant",
                "Remettez votre repos entre les mains du Père : « En tes mains, Seigneur, je remets mon esprit ». Prenez une résolution simple pour demain : une parole bienveillante, un devoir accompli avec amour."
            )
        )
        AppLanguage.ES -> listOf(
            Pair(
                "1. Presencia y Acción de Gracias",
                "Ponte en la presencia de Dios. Recuerda las palabras de San Basilio: «No podemos pasar un solo momento sin reconocer la bondad divina». Da gracias al Señor por 3 regalos o personas concretas con las que te bendijo hoy."
            ),
            Pair(
                "2. Petición de Luz al Espíritu Santo",
                "Invoca al Espíritu Santo (Oración J.25 de las Costumbres): «Espíritu Santo, protector nuestro, ilumina nuestra mente con la sabiduría». Pide la gracia de mirar tu jornada con sinceridad evangélica, sin justificaciones egoístas."
            ),
            Pair(
                "3. Revisión del Día (Bondad, Disciplina y Ciencia)",
                "Recorre las horas del día desde la mañana:\n• ¿Viví con Bondad hacia mi prójimo, familia o compañeros?\n• ¿Mantuve la Disciplina de mis responsabilidades sin pereza?\n• ¿Puse la Ciencia y mi entendimiento al servicio de la verdad y de los que sufren?"
            ),
            Pair(
                "4. Petición de Perdón y Reconciliación",
                "Pide perdón por los momentos de aspereza, impaciencia, juicio al hermano o descuido de los necesitados. Jesús te acoge con misericordia infinita: «Cargamos este tesoro en vasijas de barro»."
            ),
            Pair(
                "5. Propósito y Abandono Confiado",
                "Entrega tu descanso en las manos del Padre: «En tus manos, Señor, encomiendo mi espíritu». Haz un propósito simple para mañana: una palabra amable, un deber cumplido a tiempo, un gesto de amor desinteresado."
            )
        )
    }

    val currentContent = stepsContent[step]
    val dialogTitle = when (appLanguage) {
        AppLanguage.EN -> "Night Examination of Conscience"
        AppLanguage.FR -> "Examen de Conscience Nocturne"
        AppLanguage.ES -> "Examen de Conciencia Nocturno"
    }
    val stepLabel = when (appLanguage) {
        AppLanguage.EN -> "Step ${step + 1} of 5"
        AppLanguage.FR -> "Étape ${step + 1} sur 5"
        AppLanguage.ES -> "Paso ${step + 1} de 5"
    }
    val nextBtnLabel = when (appLanguage) {
        AppLanguage.EN -> if (step < 4) "Next step" else "Complete and rest"
        AppLanguage.FR -> if (step < 4) "Étape suivante" else "Terminer et se reposer"
        AppLanguage.ES -> if (step < 4) "Siguiente paso" else "Finalizar y descansar"
    }
    val prevBtnLabel = when (appLanguage) {
        AppLanguage.EN -> "Previous"
        AppLanguage.FR -> "Précédent"
        AppLanguage.ES -> "Anterior"
    }
    val closeBtnLabel = when (appLanguage) {
        AppLanguage.EN -> "Close"
        AppLanguage.FR -> "Fermer"
        AppLanguage.ES -> "Cerrar"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = dialogTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BasilianNavy
                )
                Text(
                    text = stepLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = BasilianGoldDark,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                // Step Progress Bar
                LinearProgressIndicator(
                    progress = { (step + 1) / 5f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = BasilianGold,
                    trackColor = ParchmentBorder
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = currentContent.first,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = BasilianCrimson
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentContent.second,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (step < 4) {
                        viewModel.nextExamenStep()
                    } else {
                        viewModel.togglePrayer("examen")
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BasilianBlue)
            ) {
                Text(nextBtnLabel)
            }
        },
        dismissButton = {
            if (step > 0) {
                OutlinedButton(onClick = { viewModel.prevExamenStep() }) {
                    Text(prevBtnLabel)
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text(closeBtnLabel)
                }
            }
        }
    )
}
