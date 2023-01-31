package ru.jeinmentalist.mail.mnemolist.background

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import ru.jeinmentalist.mail.mnemolist.UI.utilits.createRebootNotification
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showToast
import ru.jeinmentalist.mail.mnemolist.background.reminder.IReminderManager
import javax.inject.Inject

@AndroidEntryPoint
class StopNotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var rm: IReminderManager
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("nification_id", 0)
        rm.cancelReminder(notificationId){
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)
            createRebootNotification(context, "Отмена", "заметка была отменена")
        }
    }
}