package ru.jeinmentalist.mail.mnemolist.base

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import ru.jeinmentalist.mail.domain.type.exception.Failure

abstract class BaseWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        try {
            resultSuccess()
        } catch (ex: Exception) {
            return Result.failure(); //или Result.retry()
        }
        return Result.success()
    }

    abstract fun resultSuccess()

    // этот метод только для обработки ошибки запроса в бд
    protected fun handleFailure(failure: Failure) {
    }
}