package ru.jeinmentalist.mail.mnemolist.background

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.note.noteUseCase.GetNoteByIdUseCase
import ru.jeinmentalist.mail.domain.note.noteUseCase.UpdateNoteNextTimestampUseCase
import ru.jeinmentalist.mail.domain.note.noteUseCase.UpdateNoteStateUseCase
import ru.jeinmentalist.mail.domain.profile.profileUseCase.ChangeCompletedEntriesUseCase
import ru.jeinmentalist.mail.domain.profile.profileUseCase.CounterEntriesParams
import ru.jeinmentalist.mail.domain.timestamp.timstampUseCase.LoadTimestampListUseCase
import ru.jeinmentalist.mail.mnemolist.UI.utilits.sendLastNotification
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
import ru.jeinmentalist.mail.mnemolist.background.reminder.IReminderManager
import ru.jeinmentalist.mail.mnemolist.background.reminder.RemindManagerOnWorkManager
import ru.jeinmentalist.mail.mnemolist.base.BaseWorker
import java.util.*
import javax.inject.Inject

@HiltWorker
class MakeAlarmWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted val workerParameters: WorkerParameters,
    val mGetNoteById: GetNoteByIdUseCase,
    val mUpdateNote: UpdateNoteNextTimestampUseCase,
    val mUpdateNoteState: UpdateNoteStateUseCase,
    val mLoadTimestampList: LoadTimestampListUseCase,
    val mChangeCompletedEntries: ChangeCompletedEntriesUseCase,
) : BaseWorker(context, workerParameters) {

    private var lch: Int = 0

    @Inject lateinit var remindManager: IReminderManager

    override fun resultSuccess() {
        val ids = workerParameters.inputData.getIntArray(IDS)
        lch = workerParameters.inputData.getInt(LAUNCH, 0)
        ids?.map { id: Int ->
            mGetNoteById(GetNoteByIdUseCase.Params(id)) {
                it.either(::handleFailure, ::handleNote)
            }
        }
    }

    private fun handleNote(note: Note) {
//        if (note.state != Note.DONE) {
//            showLog("время выполнения ${note.nextRunningTimestamp}")
//            note.changeNextExecutableTimestamp()
//            showLog("время выполнения ${note.nextRunningTimestamp}")
//            note.changeNextExecutableTimestamp()
//            showLog("время выполнения ${note.nextRunningTimestamp}")
//            note.changeNextExecutableTimestamp()
//            showLog("время выполнения ${note.nextRunningTimestamp}")

//            getListTimestamp(note) { listTimestamp: List<Long> ->
//                val sortList = listTimestamp.sorted()
//                // сдесь переодически падает изза пустого листа
//                when (lch) {
//                    LAUNCH_CREATION -> {
//                        notificationHandler(note, sortList)
////                        createNotification(note)
//                        changeExecutableTimestamp(note, sortList)
//                    }
//                    LAUNCH_REPETITION -> {
//                        notificationHandler(note, sortList)
//                        if (note.executableTimestamp == sortList.last()) {
//                            changeNoteState(note)
//                        } else {
//                            changeExecutableTimestamp(note, sortList)
//                        }
//                    }
//                    LAUNCH_REBOOT -> {
//                        notificationHandler(note, sortList)
//                    }
//                }
//            }
//        } else {
//            changeCompletedEntries(note)
//            sendLastNotification(applicationContext)
//            mUpdateNote(UpdateNoteNextTimestampUseCase.Params(note.noteId, -1))
//        }
    }

    private fun getListTimestamp(note: Note, successful: (List<Long>) -> Unit) {
        mLoadTimestampList(LoadTimestampListUseCase.Params(note.profId)) {
            it.either(::handleFailure) { listTimestamp ->
                successful(listTimestamp.map { it.executionTime })
            }
        }
    }

    private fun changeExecutableTimestamp(note: Note, list: List<Long>) {
        for (i in list) {
            if (i > note.nextRunningTimestamp) {
//                mUpdateNote(UpdateNoteNextTimestampUseCase.Params(note.noteId, i))
                break
            }
        }
    }

    private fun changeNoteState(note: Note) {
//        mUpdateNoteState(UpdateNoteStateUseCase.Param(note.noteId, Note.DONE))
    }

    private fun changeCompletedEntries(note: Note) {
        mChangeCompletedEntries(CounterEntriesParams(note.profId))
    }

    private fun notificationHandler(note: Note, timestampList: List<Long>) {
        if (lch == LAUNCH_REBOOT) {
            val rebootTimestamp: Long =
                (timestampList[timestampList.indexOf(note.nextRunningTimestamp) - 1]) + note.timeOfCreation.toLong()
            createNotification(rebootTimestamp, note)
        } else {
            val date = Date((note.timeOfCreation.toLong() + note.nextRunningTimestamp))
            showLog("timestamp : $date ")
            createNotification((note.timeOfCreation.toLong() + note.nextRunningTimestamp), note)
        }
    }

    /**
     * @param timestamp пока оставлю в случае использования аларм менеджер при ребуте
     */
    private fun createNotification(timestamp: Long, note: Note) {
        val newDate = Date()
        val noteDate = Date(note.timeOfCreation.toLong() + note.nextRunningTimestamp)
        // этот код пригодиться для смещения времени в случае создания уведомления ночью
        val calendar = Calendar.getInstance()
        calendar.time = noteDate
        val hourNotification = calendar.get(Calendar.HOUR_OF_DAY)
        showLog("час следующего уведомления $hourNotification")
        ///////////////////////////////////////////////////////////////////////////////////
//        val remindManager: IReminderManager = RemindManagerOnWorkManager()
        if (noteDate.time < newDate.time){
            showLog("Уже поздно")
//            remindManager.startReminder(applicationContext, note, 1000)
//            ReminderWorker.create(applicationContext, 1000, note.noteId)
        }else{
            // сдесь должен быть вызов метода интерфейса
//            remindManager.startReminder(applicationContext, note, note.nextRunningTimestamp )
//            ReminderWorker.create(applicationContext, note.executableTimestamp , note.noteId)
        }
    }

    companion object {
        const val IDS = "id_note_MakeFirstNotificationWorker"
        const val WORK_NAME = "MakeFirstNotificationWorker"

        const val LAUNCH_REBOOT = 1
        const val LAUNCH_CREATION = 2
        const val LAUNCH_REPETITION = 3
        const val LAUNCH = "launch"

        fun create(context: Context, ids: IntArray, launch: Int) {
            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                makeRequest(ids, launch)
            )
        }

        private fun makeRequest(ids: IntArray, launch: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MakeAlarmWorker>()
                .setInputData(workDataOf(IDS to ids, LAUNCH to launch))
//                .setConstraints(makeConstrains()) задать ограничения
                .build()
        }

        // создание объекта ограничений
        private fun makeConstrains(): Constraints {
            return Constraints.Builder()
                ////
                .build()
        }
    }
}
