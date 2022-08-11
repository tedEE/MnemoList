package ru.jeinmentalist.mail.mnemolist.background

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showToast

class StopNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("nification_id", 0)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}