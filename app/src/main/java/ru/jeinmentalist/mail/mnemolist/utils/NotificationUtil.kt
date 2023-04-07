package ru.jeinmentalist.mail.mnemolist.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import ru.jeinmentalist.mail.mentalist.R
import ru.jeinmentalist.mail.mnemolist.background.RepetitionNotificationReceiver
import ru.jeinmentalist.mail.mnemolist.background.StopNotificationReceiver

class NotificationUtil(val context: Context) {
    private val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val notificationDescription = "Mnemo list"

    // этот метод вызываеться ReminderWorker
    fun sendNotificationWithImage(
        messageTitle: String,
        messageBody: String,
        noteId: Int,
        image: Bitmap
    ) {

        val idNotification = noteId

        val proceedIntent =
            Intent(context, RepetitionNotificationReceiver::class.java).let { intent ->
                intent.putExtra(RepetitionNotificationReceiver.NOTE_ID, noteId)
                PendingIntent.getBroadcast(
                    context,
                    noteId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }


        val stopIntent =
            Intent(context, StopNotificationReceiver::class.java).let { intent ->
                intent.putExtra("nification_id", idNotification)
                PendingIntent.getBroadcast(
                    context,
                    noteId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

        val build = createBuilder(messageTitle, messageBody)
            .addAction(R.drawable.ic_remember, "Продолжить", proceedIntent)
            .addAction(R.drawable.ic_remember__1_, "Сбросить", stopIntent)
            .setAutoCancel(true)
            .setOngoing(true)
            .setStyle(createBigPicStyle(image))


        nm.notify(idNotification, build.build())
    }

    // переписать надо или заменить sendNotification
    fun sendLastNotification(applicationContext: Context) {
        val remember =
            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_remember)

        val builder = createBuilder(applicationContext.getString(R.string.last_notification_title))
            .setLargeIcon(remember)
            .setContentTitle(applicationContext.getString(R.string.last_notification_title))
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setContentInfo("setContentInfo")

        nm.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName, // название которое увидит пользователь
                NotificationManager.IMPORTANCE_DEFAULT // настройка важности уведомления
            )

            // включение светодиода
            notificationChannel.enableLights(true)
            // цвет светодиода
            notificationChannel.lightColor = Color.RED
            // включить вибрацию
            notificationChannel.enableVibration(true)
            notificationChannel.description =
                notificationDescription // описание канала которое увидит пользователь
            val manager = context.getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(notificationChannel)
        }
    }

    // этот метоб надо вызывать в BootReceiver, StopNotificationReceiver, TrackTimeChangeReceiver
    fun sendNotification(title: String, content: String = "запуск после перезагрузки"){
        val remember =
            BitmapFactory.decodeResource(context.resources, R.drawable.ic_remember)
        val builder = createBuilder(title, content)
            .setSmallIcon(R.drawable.ic_remember__1_)
            .setLargeIcon(remember)
            .setWhen(System.currentTimeMillis())
            .setContentInfo("setContentInfo")
            .setPriority(NotificationCompat.PRIORITY_MAX)

        nm.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    private fun createBuilder(
        messageTitle: String,
        messageBody: String = ""
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, "exampleServiceChannel")
            .setSmallIcon(R.drawable.ic_remember__1_)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_MAX)
    }

    private fun createBigPicStyle(image: Bitmap): NotificationCompat.BigPictureStyle {
        return NotificationCompat.BigPictureStyle()
            .bigPicture(image)
            .bigLargeIcon(null)
    }
}