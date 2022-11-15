package ru.jeinmentalist.mail.mnemolist.background

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.note.noteUseCase.GetNoteByIdUseCase
import ru.jeinmentalist.mail.mnemolist.UI.utilits.sendBigPicNotification
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
import ru.jeinmentalist.mail.mnemolist.base.BaseWorker
// Не используеться
@HiltWorker
class ShowBigPicWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted val workerParameters: WorkerParameters,
    val mGetNoteById: GetNoteByIdUseCase,
) : BaseWorker(context, workerParameters) {


    override fun resultSuccess() {
        val id = workerParameters.inputData.getInt(NOTE_ID, 0)
        showLog("ShowBigPicWorker $id")
        mGetNoteById(GetNoteByIdUseCase.Params(id)) {
            it.either(::handleFailure, ::handleNote)
        }
    }

    private fun handleNote(note: Note){
        sendBigPicNotification(note.location, note.description, note.noteId, applicationContext, note.pathImage)
    }

    companion object{
        const val NOTE_ID = "notification_id"
        const val WORK_NAME = "ShowBigPicWorker"

        fun create(context: Context, ids: Int) {
            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                makeRequest(ids)
            )
        }

        private fun makeRequest( noteId: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<ShowBigPicWorker>()
                .setInputData(workDataOf(
                    NOTE_ID to noteId,
                ))
                .build()
        }


    }
}