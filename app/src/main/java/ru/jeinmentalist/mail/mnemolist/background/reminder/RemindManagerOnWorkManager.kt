package ru.jeinmentalist.mail.mnemolist.background.reminder

import android.content.Context
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.note.noteUseCase.GetNoteByIdUseCase
import ru.jeinmentalist.mail.domain.note.noteUseCase.UpdateNoteNextTimestampUseCase
import ru.jeinmentalist.mail.domain.note.noteUseCase.UpdateNoteStateUseCase
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.domain.profile.profileUseCase.GetProfileByIdUseCase
import ru.jeinmentalist.mail.domain.profile.profileUseCase.UpdateProfileUseCase
import ru.jeinmentalist.mail.mnemolist.UI.utilits.sendLastNotification
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showToast
import java.util.*

class RemindManagerOnWorkManager(
    val getNote: GetNoteByIdUseCase,
    val updateNote: UpdateNoteNextTimestampUseCase,
    val updateNoteState: UpdateNoteStateUseCase
) : IReminderManager {

    override fun startReminder(context: Context, noteId: Int, timeReminder: Long, reminderId: Int) {
        getNote(GetNoteByIdUseCase.Params(noteId)){
            it.either({}){ note: Note ->
                ReminderWorker.create(context, note.nextRunningTimestamp, note.noteId)
            }
        }
    }

    override fun repeatReminder(context: Context, noteId: Int) {
        getNote(GetNoteByIdUseCase.Params(noteId)){
            it.either({}){ note: Note ->

                if(note.nextRunningTimestamp == note.getSortedTimestampList().last()){
                    showLog("надо изменить состояние")
                    note.changeState(Note.Done())
                    showLog("состояние ${note.state}")
                }
                note.changeСurrentExecutableTimestamp()
                note.changeNextExecutableTimestamp()
                updateNote(UpdateNoteNextTimestampUseCase.Params(note.noteId, note.nextRunningTimestamp, note.state)){
                    it.either({},{
                        if (checkTime(note) && note.checkRunningNote()){
                            showLog("Уже поздно")
                            ReminderWorker.create(context, 1000, note.noteId)
                        }else{
                            if (note.checkDoneNote()) {
                                sendLastNotification(context)
                            }else{
                                showToast(context, "Заметка обновленна")
                                showLog("состояние ${note.state}")
                                val executableTimestamp = note.timeOfCreation.toLong() + note.nextRunningTimestamp - System.currentTimeMillis()
                                ReminderWorker.create(context, executableTimestamp, note.noteId)
                            }
                        }
                    })

                }
            }
        }

    }

    override fun cancelReminder(noteId: Int, callback: ()->Unit) {
        updateNoteState(UpdateNoteStateUseCase.Param(noteId, Note.Canceled().state)){
            it.either({}, {
                callback.invoke()
            })
        }
    }

    private fun checkTime(note: Note): Boolean {
        val newDate = Date()
        val noteDate = Date(note.timeOfCreation.toLong() + note.nextRunningTimestamp)
        return noteDate.time < newDate.time
    }

    override fun stopReminder(context: Context, reminderId: Int) {
        ReminderWorker.cancell(context, reminderId.toString())
    }
}