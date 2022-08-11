package ru.jeinmentalist.mail.mnemolist.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.jeinmentalist.mail.mnemolist.UI.utilits.sendNotification

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra(TITLE) ?: ""
        val message = intent?.getStringExtra(MESSAGE) ?: ""
        val noteId = intent?.getIntExtra(NOTE_ID, 0) ?: 0
        sendNotification(title, message, noteId, context!!)

    }
    companion object{
        const val TITLE = "title"
        const val MESSAGE = "message"
        const val NOTE_ID = "note_id"
    }
}