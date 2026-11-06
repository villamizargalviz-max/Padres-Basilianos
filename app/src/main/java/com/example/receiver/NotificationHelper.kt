package com.example.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import java.util.Calendar

object NotificationHelper {

    private const val TAG = "NotificationHelper"

    const val CHANNEL_DAILY_PRAYER = "basilian_daily_prayer_channel"
    const val CHANNEL_SOLEMNITIES = "basilian_solemnities_channel"
    const val CHANNEL_BIRTHDAYS = "basilian_birthdays_channel"
    const val CHANNEL_GENERAL = "basilian_reminders_channel"
    const val CHANNEL_SECURITY_ALERTS = "basilian_security_alerts_channel"

    const val EXTRA_EVENT_ID = "EVENT_ID"
    const val EXTRA_TITLE = "TITLE"
    const val EXTRA_MESSAGE = "MESSAGE"
    const val EXTRA_CHANNEL_ID = "CHANNEL_ID"
    const val EXTRA_TYPE = "REMINDER_TYPE" // "DAILY_PRAYER", "SOLEMNITY", "BIRTHDAY", "CUSTOM"
    const val EXTRA_TARGET_TAB = "TARGET_TAB"
    const val EXTRA_HOUR = "HOUR"
    const val EXTRA_MINUTE = "MINUTE"
    const val EXTRA_MONTH = "MONTH"
    const val EXTRA_DAY = "DAY"

    fun createAllNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val prayerChannel = NotificationChannel(
                CHANNEL_DAILY_PRAYER,
                "Oración Diaria (Laudes, Ángelus, Vísperas)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Recordatorios diarios para la oración de la mañana, mediodía y noche con la meditación"
                enableLights(true)
                enableVibration(true)
            }

            val solemnityChannel = NotificationChannel(
                CHANNEL_SOLEMNITIES,
                "Solemnidades y Fiestas Litúrgicas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de fiestas patronales, santoral católico y aniversarios basilianos"
                enableLights(true)
                enableVibration(true)
            }

            val birthdayChannel = NotificationChannel(
                CHANNEL_BIRTHDAYS,
                "Cumpleaños y Aniversarios Fraternos",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Avisos de cumpleaños de sacerdotes, cohermanos basilianos, santos fundadores y amigos"
                enableLights(true)
                enableVibration(true)
            }

            val generalChannel = NotificationChannel(
                CHANNEL_GENERAL,
                "Avisos Generales y Curia Basiliana",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones generales de la Congregación de San Basilio"
            }

            val securityChannel = NotificationChannel(
                CHANNEL_SECURITY_ALERTS,
                "Alertas de Seguridad Anti-Hackeo (Solo para Mí)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Avisos inmediatos si alguien intenta ingresar sin autorización al sistema o adivinar contraseñas"
                enableLights(true)
                enableVibration(true)
            }

            notificationManager.createNotificationChannels(
                listOf(prayerChannel, solemnityChannel, birthdayChannel, generalChannel, securityChannel)
            )
        }
    }

    /**
     * Envía una alerta inmediata ÚNICAMENTE al Líder Creador de la aplicación
     * (villamizargalviz@gmail.com o dispositivo autorizado del Líder) cuando alguien
     * intenta ingresar sin autorización en cualquier copia compartida de la app.
     * Los demás hermanos basilianos y usuarios NUNCA reciben esta notificación.
     */
    fun sendIntrusionAlertNotification(
        context: Context,
        targetArea: String,
        actorInfo: String,
        failedAttempts: Int,
        forceForLeaderSync: Boolean = false
    ) {
        // ESTRICTAMENTE SOLO PARA EL LÍDER ÚNICO: villamizargalviz@gmail.com
        if (!com.example.data.security.BasilianSecurityManager.isAppCreatorLeader(context)) {
            // Silenciar notificación en cualquier teléfono que NO tenga sesión iniciada con villamizargalviz@gmail.com
            return
        }
        val alertId = 99000 + (System.currentTimeMillis() % 900).toInt()
        val title = "🚨 Alerta Exclusiva para villamizargalviz@gmail.com • Intento Detectado"
        val message = "Alguien intentó ingresar sin autorización en «$targetArea» ($actorInfo • Intento #$failedAttempts). El sistema bloqueó el acceso y te notificó únicamente a ti (villamizargalviz@gmail.com)."
        showPushNotification(
            context = context,
            notificationId = alertId,
            title = title,
            message = message,
            channelId = CHANNEL_SECURITY_ALERTS,
            targetTab = "PORTAL_CURIA"
        )
    }

    /**
     * Envía una notificación fraterna a todas las aplicaciones de la Congregación en sus
     * diferentes países (Colombia, Estados Unidos, Canadá, México, Francia) cuando alguien
     * publica un mensaje de «¡Feliz Día!» en una fecha especial de nuestros Patronos.
     */
    fun sendPatronGreetingNotification(
        context: Context,
        patronOrFeastTitle: String,
        authorName: String,
        authorContext: String,
        greetingMessage: String
    ) {
        val notifId = 88000 + (System.currentTimeMillis() % 900).toInt()
        val title = "🎉 ¡Feliz Día! • $patronOrFeastTitle"
        val message = "«$greetingMessage» — Enviado por $authorName desde $authorContext para toda la familia basiliana."
        showPushNotification(
            context = context,
            notificationId = notifId,
            title = title,
            message = message,
            channelId = CHANNEL_SOLEMNITIES,
            targetTab = "RECURSOS"
        )
    }

    /**
     * Muestra una notificación push instantánea o disparada por alarma.
     */
    fun showPushNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String,
        channelId: String = CHANNEL_GENERAL,
        targetTab: String? = null
    ) {
        createAllNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            if (targetTab != null) {
                putExtra("NAVIGATE_TO_TAB", targetTab)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        // Acción adicional para ingresar directamente
        val actionText = when (channelId) {
            CHANNEL_DAILY_PRAYER -> "Rezar Ahora"
            CHANNEL_BIRTHDAYS -> "Felicitar & Orar"
            CHANNEL_SOLEMNITIES -> "Ver Fiesta"
            CHANNEL_SECURITY_ALERTS -> "Reforzar Seguridad"
            else -> "Abrir App"
        }
        builder.addAction(android.R.drawable.ic_menu_agenda, actionText, pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            notificationManager.notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            Log.w(TAG, "Permiso POST_NOTIFICATIONS no concedido: ${e.message}")
        }
    }

    /**
     * Programa un recordatorio diario a una hora y minuto específicos (Laudes, Ángelus, Vísperas).
     */
    fun scheduleDailyPrayer(
        context: Context,
        prayerType: String, // "LAUDES", "ANGELUS", "VESPERAS"
        hour: Int,
        minute: Int,
        title: String,
        message: String
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BasilianReminderReceiver::class.java).apply {
            action = "com.example.ACTION_BASILIAN_REMINDER"
            putExtra(EXTRA_EVENT_ID, "daily_prayer_$prayerType")
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_MESSAGE, message)
            putExtra(EXTRA_CHANNEL_ID, CHANNEL_DAILY_PRAYER)
            putExtra(EXTRA_TYPE, "DAILY_PRAYER")
            putExtra(EXTRA_TARGET_TAB, "ORACION_DIARIA")
            putExtra(EXTRA_HOUR, hour)
            putExtra(EXTRA_MINUTE, minute)
        }

        val requestCode = ("prayer_$prayerType").hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.i(TAG, "Oración diaria programada: $prayerType a las $hour:$minute (Millis: ${calendar.timeInMillis})")
        } catch (e: SecurityException) {
            Log.w(TAG, "Permiso exacto no disponible, usando alarma estándar: ${e.message}")
            try {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            } catch (ex: Exception) {
                Log.e(TAG, "Error final programando alarma: ${ex.message}")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error programando alarma diaria $prayerType: ${e.message}")
        }
    }

    /**
     * Cancela la alarma de una oración diaria.
     */
    fun cancelDailyPrayer(context: Context, prayerType: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BasilianReminderReceiver::class.java).apply {
            action = "com.example.ACTION_BASILIAN_REMINDER"
        }
        val requestCode = ("prayer_$prayerType").hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Programa un recordatorio anual para una Solemnidad Litúrgica.
     */
    fun scheduleSolemnityReminder(
        context: Context,
        solemnityId: String,
        month: Int, // 1..12
        day: Int,   // 1..31
        hour: Int = 8,
        minute: Int = 0,
        title: String,
        message: String
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BasilianReminderReceiver::class.java).apply {
            action = "com.example.ACTION_BASILIAN_REMINDER"
            putExtra(EXTRA_EVENT_ID, solemnityId)
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_MESSAGE, message)
            putExtra(EXTRA_CHANNEL_ID, CHANNEL_SOLEMNITIES)
            putExtra(EXTRA_TYPE, "SOLEMNITY")
            putExtra(EXTRA_TARGET_TAB, "CALENDARIO")
            putExtra(EXTRA_MONTH, month)
            putExtra(EXTRA_DAY, day)
            putExtra(EXTRA_HOUR, hour)
            putExtra(EXTRA_MINUTE, minute)
        }

        val requestCode = solemnityId.hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.i(TAG, "Solemnidad programada: $title para el $day/$month a las $hour:$minute")
        } catch (e: Exception) {
            Log.w(TAG, "Error programando solemnidad $solemnityId: ${e.message}")
        }
    }

    /**
     * Programa un recordatorio anual para un CUMPLEAÑOS (cohermano, santo fundador o personalizado).
     */
    fun scheduleBirthdayReminder(
        context: Context,
        birthdayId: String,
        month: Int, // 1..12
        day: Int,   // 1..31
        hour: Int = 8,
        minute: Int = 30,
        personName: String,
        blessingNote: String
    ) {
        val title = "🎂 ¡Hoy es el Cumpleaños de $personName!"
        val message = blessingNote.ifBlank {
            "Hoy damos gracias a Dios por la vida de $personName. Oremos por su fidelidad, vocación y bendición fraterna."
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BasilianReminderReceiver::class.java).apply {
            action = "com.example.ACTION_BASILIAN_REMINDER"
            putExtra(EXTRA_EVENT_ID, birthdayId)
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_MESSAGE, message)
            putExtra(EXTRA_CHANNEL_ID, CHANNEL_BIRTHDAYS)
            putExtra(EXTRA_TYPE, "BIRTHDAY")
            putExtra(EXTRA_TARGET_TAB, "DIRECTORIO")
            putExtra(EXTRA_MONTH, month)
            putExtra(EXTRA_DAY, day)
            putExtra(EXTRA_HOUR, hour)
            putExtra(EXTRA_MINUTE, minute)
        }

        val requestCode = ("bday_$birthdayId").hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.i(TAG, "Cumpleaños programado: $personName ($day/$month a las $hour:$minute)")
        } catch (e: Exception) {
            Log.w(TAG, "Error programando cumpleaños $birthdayId: ${e.message}")
        }
    }

    /**
     * Cancela cualquier recordatorio por ID.
     */
    fun cancelReminder(context: Context, eventId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BasilianReminderReceiver::class.java).apply {
            action = "com.example.ACTION_BASILIAN_REMINDER"
        }
        val requestCode = eventId.hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    // Métodos de compatibilidad previa
    fun createNotificationChannel(context: Context) {
        createAllNotificationChannels(context)
    }

    fun showReminderNotification(context: Context, notificationId: Int, title: String, message: String) {
        showPushNotification(context, notificationId, title, message, CHANNEL_GENERAL)
    }

    fun scheduleDateReminder(
        context: Context,
        eventId: String,
        month: Int,
        day: Int,
        title: String,
        description: String
    ) {
        scheduleSolemnityReminder(
            context = context,
            solemnityId = eventId,
            month = month,
            day = day,
            title = title,
            message = description
        )
    }

    fun cancelDateReminder(context: Context, eventId: String) {
        cancelReminder(context, eventId)
    }
}
