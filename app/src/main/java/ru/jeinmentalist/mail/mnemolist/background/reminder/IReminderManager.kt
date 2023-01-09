package ru.jeinmentalist.mail.mnemolist.background.reminder

import android.content.Context
import ru.jeinmentalist.mail.domain.note.Note

interface IReminderManager {
    fun startReminder(
        context: Context,
        note: Note,
        timeReminder: Long,
        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
    )

    fun stopReminder(
        context: Context,
        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
    )

    companion object {
        const val REMINDER_NOTIFICATION_REQUEST_CODE = 123
    }
}