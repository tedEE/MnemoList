package ru.jeinmentalist.mail.mnemolist.UI.utilits

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import ru.jeinmentalist.mail.mentalist.R
import ru.jeinmentalist.mail.mnemolist.App
import ru.jeinmentalist.mail.mnemolist.MainActivity
import ru.jeinmentalist.mail.mnemolist.background.RepetitionNotificationReceiver
import ru.jeinmentalist.mail.mnemolist.background.StopNotificationReceiver

private val NOTIFICATION_ID = 0
    var count = 0

fun createNotification(context: Context, intent: Intent, pendingIntent: PendingIntent){
    val notification = NotificationCompat.Builder(context, App.CHANNEL_ID)
        .setContentTitle("Foreground service")
        .setContentText(intent?.getStringExtra("note_location"))
        .setSmallIcon(R.drawable.ic_alert)
        .setContentIntent(pendingIntent)
//        .addAction(R.drawable.ic_delete, "Остановить сервис", stopPendingIntent)
//        .addAction(R.drawable.ic__10, "Продлить на 10с", delayTenPendingIntent)
//        .addAction(R.drawable.ic_20, "Продлить на 20с", delayTwentyPendingIntent)
        .build()
}

fun sendNotification(messageTitle: String, messageBody: String, noteId: Int, applicationContext: Context) {

    val idNotification = noteId
    count++

    val remember = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_remember)

    val proceedIntent = Intent(applicationContext, RepetitionNotificationReceiver::class.java).let { intent ->
        intent.putExtra("note_id", noteId)
        PendingIntent.getBroadcast(applicationContext,
            noteId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    val stopIntent = Intent(applicationContext, StopNotificationReceiver::class.java).let { intent ->
        intent.putExtra("nification_id", idNotification)
        PendingIntent.getBroadcast(applicationContext,
            noteId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    val contentIntent = Intent(applicationContext, MainActivity::class.java).let{ intent ->
        PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    val builder = NotificationCompat.Builder(
        applicationContext,
        "exampleServiceChannel"
    )
        .setSmallIcon(R.drawable.ic_remember__1_)
        .setLargeIcon(remember)
//        .setContentTitle(count.toString()) наверно было нужно отслеживать count
        .setContentTitle(messageTitle)
//        .setTicker("Всплывающий текст")
        .setWhen(System.currentTimeMillis())
        .setContentText(messageBody)
        .setContentIntent(contentIntent)
        .addAction(R.drawable.ic_remember, "Продолжить", proceedIntent)
        .addAction(R.drawable.ic_remember__1_, "Сбросить", stopIntent)
        //уведомление закрывается, когда оно переводит их в приложение.
        .setAutoCancel(true)
        .setContentInfo("setContentInfo")
        // запрет на смахивание
        .setOngoing(true)
            //приоритет
        .setPriority(NotificationCompat.PRIORITY_MAX)
        // создание группы
//        .setGroup("GROUP_KEY_WORK_EMAIL")

    // механизм создания группы сообщений
    // требуеться доработка
//    if(count == 3){
//        builder.setStyle(NotificationCompat.InboxStyle()
//            .addLine("addLine")
////            .setSummaryText("setSummaryText")
////            .setBigContentTitle("setBigContentTitle")
//        )
//            .setGroupSummary(true)
//    }

    val nm  = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    nm.notify(idNotification, builder.build())
}

fun sendLastNotification(applicationContext: Context) {

    val contentIntent = Intent(applicationContext, MainActivity::class.java).let{ intent ->
        PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    val remember = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_remember)

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(null)
        .bigLargeIcon(remember)

    val builder = NotificationCompat.Builder(
        applicationContext,
        "exampleServiceChannel"
    )
        .setSmallIcon(R.drawable.ic_remember__1_)
        .setStyle(bigPicStyle)
        .setLargeIcon(remember)
        .setContentTitle(applicationContext.getString(R.string.last_notification_title))
        .setWhen(System.currentTimeMillis())
        .setContentIntent(contentIntent)
        .setAutoCancel(true)
        .setContentInfo("setContentInfo")

    val nm  = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    nm.notify(System.currentTimeMillis().toInt(), builder.build())
}

fun createNotificationChannel(context: Context, channelId: String, channelName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        // включение светодиода
        notificationChannel.enableLights(true)
        // цвет светодиода
        notificationChannel.lightColor = Color.RED
        // включить вибрацию
        notificationChannel.enableVibration(true)
        notificationChannel.description = "Mnemo list"
        val manager = context.getSystemService(
            NotificationManager::class.java
        )
        manager.createNotificationChannel(notificationChannel)
    }
}

fun showLog(log: String){
    Log.d("MnemolistLog", log)
}

fun showToast(context: Context, messege: String){
    Toast.makeText(context, messege, Toast.LENGTH_SHORT).show()
}