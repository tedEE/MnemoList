package ru.jeinmentalist.mail.mnemolist.background

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import ru.jeinmentalist.mail.mnemolist.background.reminder.IReminderManager
import javax.inject.Inject

@AndroidEntryPoint
class RepetitionNotificationReceiver : BroadcastReceiver() {

    @Inject lateinit var remMen: IReminderManager

    override fun onReceive(context: Context, intent: Intent) {
        val noteId = intent.getIntExtra(NOTE_ID, 0)
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(noteId)
        remMen.repeatReminder(context, noteId)
//        MakeAlarmWorker.create(context, intArrayOf(noteId), MakeAlarmWorker.LAUNCH_REPETITION)
    }

    companion object{
        const val NOTE_ID = "note_id"
    }
}