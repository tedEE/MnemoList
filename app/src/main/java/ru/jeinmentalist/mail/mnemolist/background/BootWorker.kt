package ru.jeinmentalist.mail.mnemolist.background

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.jeinmentalist.mail.data.db.MnemoListDatabase
import ru.jeinmentalist.mail.data.note.NoteRepositoryImpl
import ru.jeinmentalist.mail.domain.note.noteUseCase.GetPerformedNotesUseCase
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.mnemolist.UI.utilits.sendNotification
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
import ru.jeinmentalist.mail.mnemolist.base.BaseWorker

@HiltWorker
class BootWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParameters: WorkerParameters,
    val getPerformedNotes: GetPerformedNotesUseCase
) : BaseWorker(context, workerParameters) {

    override fun resultSuccess() {
        showLog("BootWorker")
        getPerformedNotes(None()){
            it.either(::handleFailure, ::makeAlarm)
        }
    }

    fun makeAlarm(list: List<Int>){
        MakeAlarmWorker.create(applicationContext, list.toIntArray(), MakeAlarmWorker.LAUNCH_REBOOT)
    }
    companion object {
        const val WORK_NAME = "BootWorker"

        fun create(context: Context){
            val workManager = WorkManager.getInstance(context!!)
            workManager.enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                makeRequest())
        }

        private fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<BootWorker>()
                .build()
        }
    }

}