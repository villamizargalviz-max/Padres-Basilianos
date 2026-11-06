package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BasilianReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i("BasilianReceiver", "Dispositivo reiniciado. Los recordatorios se resincronizarán al abrir la app.")
            return
        }

        val eventId = intent.getStringExtra(NotificationHelper.EXTRA_EVENT_ID) ?: "basilian_reminder"
        val title = intent.getStringExtra(NotificationHelper.EXTRA_TITLE) ?: "Oración y Vida Basiliana"
        val message = intent.getStringExtra(NotificationHelper.EXTRA_MESSAGE) ?: "El Señor nos convoca a la oración y fraternidad en este día."
        val channelId = intent.getStringExtra(NotificationHelper.EXTRA_CHANNEL_ID) ?: NotificationHelper.CHANNEL_GENERAL
        val reminderType = intent.getStringExtra(NotificationHelper.EXTRA_TYPE) ?: "GENERAL"
        val targetTab = intent.getStringExtra(NotificationHelper.EXTRA_TARGET_TAB)

        Log.i("BasilianReceiver", "Alarma recibida: $eventId ($reminderType) - $title")

        NotificationHelper.showPushNotification(
            context = context,
            notificationId = eventId.hashCode(),
            title = title,
            message = message,
            channelId = channelId,
            targetTab = targetTab
        )

        // Re-programación automática para el siguiente ciclo
        when (reminderType) {
            "DAILY_PRAYER" -> {
                val hour = intent.getIntExtra(NotificationHelper.EXTRA_HOUR, 8)
                val minute = intent.getIntExtra(NotificationHelper.EXTRA_MINUTE, 0)
                val prayerType = eventId.removePrefix("daily_prayer_")
                NotificationHelper.scheduleDailyPrayer(
                    context = context,
                    prayerType = prayerType,
                    hour = hour,
                    minute = minute,
                    title = title,
                    message = message
                )
            }
            "SOLEMNITY" -> {
                val month = intent.getIntExtra(NotificationHelper.EXTRA_MONTH, 1)
                val day = intent.getIntExtra(NotificationHelper.EXTRA_DAY, 1)
                val hour = intent.getIntExtra(NotificationHelper.EXTRA_HOUR, 8)
                val minute = intent.getIntExtra(NotificationHelper.EXTRA_MINUTE, 0)
                NotificationHelper.scheduleSolemnityReminder(
                    context = context,
                    solemnityId = eventId,
                    month = month,
                    day = day,
                    hour = hour,
                    minute = minute,
                    title = title,
                    message = message
                )
            }
            "BIRTHDAY" -> {
                val month = intent.getIntExtra(NotificationHelper.EXTRA_MONTH, 1)
                val day = intent.getIntExtra(NotificationHelper.EXTRA_DAY, 1)
                val hour = intent.getIntExtra(NotificationHelper.EXTRA_HOUR, 8)
                val minute = intent.getIntExtra(NotificationHelper.EXTRA_MINUTE, 30)
                val bdayId = eventId.removePrefix("bday_")
                NotificationHelper.scheduleBirthdayReminder(
                    context = context,
                    birthdayId = bdayId,
                    month = month,
                    day = day,
                    hour = hour,
                    minute = minute,
                    personName = title.removePrefix("🎂 ¡Hoy es el Cumpleaños de ").removeSuffix("!"),
                    blessingNote = message
                )
            }
        }
    }
}
