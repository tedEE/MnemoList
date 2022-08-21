package ru.jeinmentalist.mail.mnemolist.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.mnemolist.background.reminder.IReminderManager

object ReminderManager {
    const val REMINDER_NOTIFICATION_REQUEST_CODE = 123

     fun startReminder(
        context: Context,
        reminderTime: Long,
        note: Note,
        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val requestCode = note.timeOfCreation.toLong()

        val intent =
            Intent(context.applicationContext, AlarmReceiver::class.java).let { intent ->
                intent.putExtra(AlarmReceiver.TITLE, note.location)
                intent.putExtra(AlarmReceiver.MESSAGE, note.description)
                intent.putExtra(AlarmReceiver.NOTE_ID, note.noteId)
                PendingIntent.getBroadcast(
                    context.applicationContext,
                    requestCode.toInt(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

//         срочное срабатывание
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(reminderTime, intent),
            intent
        )

//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, reminderTime, intent)
    }

    fun stopReminder(
        context: Context,
        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                reminderId,
                intent,
                0
            )
        }
        alarmManager.cancel(intent)
    }
}