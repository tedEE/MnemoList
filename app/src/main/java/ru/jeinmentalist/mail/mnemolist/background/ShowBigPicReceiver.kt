package ru.jeinmentalist.mail.mnemolist.background

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
// Не используеться
class ShowBigPicReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        showLog("ShowBigPicReceiver")
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val noteId = intent?.getIntExtra(NOTE_ID, 0) ?: 0
//        notificationManager.cancel(noteId)
        ShowBigPicWorker.create(context, noteId)
    }

    companion object{
        const val NOTE_ID = "note_id"
    }
}