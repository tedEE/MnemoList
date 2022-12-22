package ru.jeinmentalist.mail.mnemolist.background.reminder

import android.content.Context
import androidx.hilt.work.HiltWorker

import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.mnemolist.UI.utilits.sendNotification
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
import java.util.concurrent.TimeUnit

//@HiltWorker
//class AlarmWorker @AssistedInject constructor(
//    @Assisted context: Context,
//    @Assisted val workerParameters: WorkerParameters
//) : Worker(context, workerParameters) {
//
//    override fun doWork(): Result {
//        val title = workerParameters.inputData.getString(TITLE) ?: ""
//        val body = workerParameters.inputData.getString(BODY) ?: ""
//        val imagePath = workerParameters.inputData.getString(IMAGE_PATH) ?: ""
//        val id = workerParameters.inputData.getInt(ID, 0)
//        try {
//            sendNotification(title, body, id, applicationContext, imagePath)
//        } catch (ex: Exception) {
//            return Result.failure(); //или Result.retry()
//        }

//        return Result.success()
//
//    }

//    companion object{
//        const val TITLE = "notification_title"
//        const val BODY = "notification_body"
//        const val ID = "notification_id"
//        const val IMAGE_PATH = "image_path"
//
//        fun makeRequest( note: Note): OneTimeWorkRequest {
//
//            return OneTimeWorkRequestBuilder<AlarmWorker>()
//                .setInputData(workDataOf(
//                    TITLE to note.location,
//                    BODY to note.description,
//                    ID to note.noteId,
//                    IMAGE_PATH to note.pathImage
//                ))
//                .setInitialDelay(delayСalculation(note), TimeUnit.MILLISECONDS)
//                .build()
//        }
//
//        private fun delayСalculation(note: Note): Long{
//            val n = note.timeOfCreation.toLong() + note.executableTimestamp - System.currentTimeMillis()
////            return note.executableTimestamp - (System.currentTimeMillis() - note.timeOfCreation.toLong())
//            return n
//        }
//    }
//}