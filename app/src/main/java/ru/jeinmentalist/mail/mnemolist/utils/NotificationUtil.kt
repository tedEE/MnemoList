package ru.jeinmentalist.mail.mnemolist.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.app.NotificationCompat
import ru.jeinmentalist.mail.mentalist.R
import ru.jeinmentalist.mail.mnemolist.background.RepetitionNotificationReceiver
import ru.jeinmentalist.mail.mnemolist.background.StopNotificationReceiver

class NotificationUtil(val context: Context) {
    private val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun sendNotification(
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

    private fun createBuilder(
        messageTitle: String,
        messageBody: String
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