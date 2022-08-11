package ru.jeinmentalist.mail.mnemolist.background

import android.content.Context
import androidx.hilt.work.HiltWorker

import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class AlarmWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val duration = workerParameters.inputData.getLong(DURATION, 0)
        try {
//            ReminderManager.startReminder(applicationContext, duration)
        } catch (ex: Exception) {
            return Result.failure(); //или Result.retry()
        }

        return Result.success()

    }

    companion object{
        const val DURATION = "executableTimestamp"
        const val WORK_NAME = "AlarmWorker"

        fun makeRequest(duration: Long): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<AlarmWorker>()
                .setInputData(workDataOf(DURATION to duration))
                .setInitialDelay(duration, TimeUnit.MILLISECONDS)
                .build()
        }
    }
}