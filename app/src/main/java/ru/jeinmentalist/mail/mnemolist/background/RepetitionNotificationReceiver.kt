package ru.jeinmentalist.mail.mnemolist.background

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RepetitionNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val noteId = intent.getIntExtra("note_id", 0)
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(noteId)

        MakeAlarmWorker.create(context, noteId)
    }
}