package ru.jeinmentalist.mail.mnemolist.background.reminder

import android.content.Context
import ru.jeinmentalist.mail.domain.note.Note

class RemindManagerOnWorkManager : IReminderManager {
    override fun startReminder(context: Context, note: Note, timeReminder: Long, reminderId: Int) {
        ReminderWorker.create(context, timeReminder, note.noteId)
    }

    override fun stopReminder(context: Context, reminderId: Int) {
        ReminderWorker.cencell(context, reminderId.toString())
    }
}