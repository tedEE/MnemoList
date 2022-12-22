package ru.jeinmentalist.mail.mnemolist.background.reminder

import android.content.Context
import androidx.work.*
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
import java.util.concurrent.TimeUnit

//class AlarmHelper: IReminderManager {

//    override fun startReminder(context: Context, note: Note, reminderId: Int) {
//        WorkManager.getInstance(context).enqueue(AlarmWorker.makeRequest( note))
//    }
//
//    override fun stopReminder(context: Context, reminderId: Int) {
//
//    }

//}