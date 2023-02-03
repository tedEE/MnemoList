package ru.jeinmentalist.mail.mnemolist.background.reminder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.note.noteUseCase.GetNoteByIdUseCase
import ru.jeinmentalist.mail.mnemolist.UI.utilits.sendNotification
import java.time.Duration

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted val workerParameters: WorkerParameters,
    val mGetNoteById: GetNoteByIdUseCase
)
    : CoroutineWorker(context, workerParameters){

    override suspend fun doWork(): Result {
        try {
            val noteId = workerParameters.inputData.getInt(ID, 0)
            mGetNoteById(GetNoteByIdUseCase.Params(noteId)) {
                it.either({}, { note ->
                    val imageBitmap = getImage(note)
                    createNotification(imageBitmap, note)
                })
            }
        } catch (ex: Exception) {
            return Result.failure(); //или Result.retry()
        }
        return Result.success()
    }

    fun getImage(note: Note): Bitmap {
        val inputStream = applicationContext.contentResolver.openInputStream(Uri.parse(note.pathImage))
        return BitmapFactory.decodeStream(inputStream)
    }

    fun createNotification(image: Bitmap, note: Note){
        sendNotification(note.location, note.description, note.noteId, applicationContext, image)
    }

    companion object {
        const val ID = "id_note_ReminderWorker"
        const val WORK_NAME = "ReminderWorker"
        const val NOTE_TIMESTAMP = "note_timestamp"

        fun create(context: Context, executableTimestamp: Long, id: Int, tag: String) {
            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                makeRequest(id, executableTimestamp, tag)
            )
        }

        fun cancell(context: Context, tag: String){
            WorkManager.getInstance(context).cancelAllWorkByTag(tag)
        }

        private fun makeRequest(id: Int, timestamp: Long, tag: String): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<ReminderWorker>()
                .addTag(tag)
                .setInputData(workDataOf(ID to id, NOTE_TIMESTAMP to timestamp))
                .setInitialDelay(Duration.ofMillis(timestamp))
                .build()
        }
    }
}