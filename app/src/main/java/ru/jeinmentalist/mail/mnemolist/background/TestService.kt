package ru.jeinmentalist.mail.mnemolist.background

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import ru.jeinmentalist.mail.mentalist.R
import ru.jeinmentalist.mail.mnemolist.App
import ru.jeinmentalist.mail.mnemolist.App.Companion.CHANNEL_ID
import ru.jeinmentalist.mail.mnemolist.MainActivity

class TestService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("ksdjhfsjdhdhj", "Сервис создаеться")
        Log.d("ksdjhfsjdhdhj", App.isAppVisible().toString())

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

//        if (intent?.action == "stop"){
//            stopSelf()
//        }

        when(intent?.action){
            "stop" -> stopSelf()
            "10" -> {

                stopSelf()
            }
            "20" -> {

                stopSelf()
            }
            else -> Log.d("sldfkjk", "Что то пошло не так")
        }

        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, 0)

        val stopIntent = Intent(this, TestService::class.java).setAction("stop")
        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val delayTenIntent = Intent(this, TestService::class.java).setAction("10")
        val delayTenPendingIntent = PendingIntent.getService(this, 0, delayTenIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val delayTwentyIntent = Intent(this, TestService::class.java).setAction("20")
        val delayTwentyPendingIntent = PendingIntent.getService(this, 0, delayTwentyIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Foreground service")
            .setContentText(intent?.getStringExtra("note_location"))
            .setSmallIcon(R.drawable.ic_alert)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_delete, "Остановить сервис", stopPendingIntent)
            .addAction(R.drawable.ic__10, "Продлить на 10с", delayTenPendingIntent)
            .addAction(R.drawable.ic_20, "Продлить на 20с", delayTwentyPendingIntent)
            .build()

        startForeground(1, notification);


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ksdjhfsjdhdhj", "Сервис остановлен")
    }
}