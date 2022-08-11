package ru.jeinmentalist.mail.mnemolist.background

import android.content.Context
import androidx.work.*
import ru.jeinmentalist.mail.domain.note.Note
import java.util.concurrent.TimeUnit

class AlarmHelper(private val context: Context) {

    private val workManager = WorkManager.getInstance(context)

    fun startAlarm(workerName: String, ){
//        workManager.enqueueUniqueWork(
//            workerName,
//            ExistingWorkPolicy.APPEND_OR_REPLACE,
//            makeRequest()
//        )
    }

}