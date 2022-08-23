package ru.jeinmentalist.mail.mnemolist.background

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.note.noteUseCase.GetNoteByIdUseCase
import ru.jeinmentalist.mail.domain.note.noteUseCase.UpdateNoteExecutableTimestampUseCase
import ru.jeinmentalist.mail.domain.note.noteUseCase.UpdateNoteStateUseCase
import ru.jeinmentalist.mail.domain.profile.profileUseCase.ChangeCompletedEntriesUseCase
import ru.jeinmentalist.mail.domain.profile.profileUseCase.CounterEntriesParams
import ru.jeinmentalist.mail.domain.timestamp.timstampUseCase.LoadTimestampListUseCase
import ru.jeinmentalist.mail.mnemolist.UI.utilits.sendLastNotification
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
import ru.jeinmentalist.mail.mnemolist.base.BaseWorker
import java.util.*

@HiltWorker
class MakeAlarmWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted val workerParameters: WorkerParameters,
    val mGetNoteById: GetNoteByIdUseCase,
    val mUpdateNote: UpdateNoteExecutableTimestampUseCase,
    val mUpdateNoteState: UpdateNoteStateUseCase,
    val mLoadTimestampList: LoadTimestampListUseCase,
    val mChangeCompletedEntries: ChangeCompletedEntriesUseCase
) : BaseWorker(context, workerParameters) {

    private var lch: Int = 0

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
        if (note.state != Note.DONE) {
//            createNotification(note)
            getListTimestamp(note) { listTimestamp: List<Long> ->
                val sortList = listTimestamp.sorted()
                // сдесь переодически падает изза пустого листа
                when (lch) {
                    LAUNCH_CREATION -> {
                        notificationHandler(note, sortList)
//                        createNotification(note)
                        changeExecutableTimestamp(note, sortList)
                    }
                    LAUNCH_REPETITION -> {
                        notificationHandler(note, sortList)
//                        createNotification(note)
                        if (note.executableTimestamp == sortList.last()) {
                            changeNoteState(note)
                        } else {
                            changeExecutableTimestamp(note, sortList)
                        }
                    }
                    LAUNCH_REBOOT -> {
                        notificationHandler(note, sortList)
//                        createNotification(note)
                    }
                }

//                if (note.executableTimestamp == sortList.last()) {
//                    changeNoteState(note)
//                } else {
//                    if (lch == LAUNCH_REPETITION)
//                        changeExecutableTimestamp(note, sortList)
//                }
            }
        } else {
            changeCompletedEntries(note)
            sendLastNotification(applicationContext)
            mUpdateNote(UpdateNoteExecutableTimestampUseCase.Params(note.noteId, -1))
        }
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
            if (i > note.executableTimestamp) {
                mUpdateNote(UpdateNoteExecutableTimestampUseCase.Params(note.noteId, i))
                break
            }
        }
    }

    private fun changeNoteState(note: Note) {
        mUpdateNoteState(UpdateNoteStateUseCase.Param(note.noteId, Note.DONE))
    }

    private fun changeCompletedEntries(note: Note) {
        mChangeCompletedEntries(CounterEntriesParams(note.profId))
    }

    private fun notificationHandler(note: Note, timestampList: List<Long>) {
        if (lch == LAUNCH_REBOOT) {
            val rebootTimestamp: Long =
                (timestampList[timestampList.indexOf(note.executableTimestamp) - 1]) + note.timeOfCreation.toLong()
            showLog("rebootTimestamp : $rebootTimestamp")
            createNotification(rebootTimestamp, note)
        } else {
            createNotification(note.timeOfCreation.toLong() + note.executableTimestamp, note)
            val date = Date(note.timeOfCreation.toLong() + note.executableTimestamp)
            val calendar = Calendar.getInstance()
            calendar.time = date
            showLog("часов : ${calendar.get(Calendar.HOUR_OF_DAY)}")
        }
    }

    private fun createNotification(timestamp: Long, note: Note) {
        ReminderManager.startReminder(
            applicationContext,
            timestamp,
            note
        )
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
