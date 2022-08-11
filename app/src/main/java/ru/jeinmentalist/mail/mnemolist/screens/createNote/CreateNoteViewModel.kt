package ru.jeinmentalist.mail.mnemolist.screens.createNote

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.note.noteUseCase.AddNoteUseCase
import ru.jeinmentalist.mail.domain.note.noteUseCase.NoteUseCases
import ru.jeinmentalist.mail.domain.profile.profileUseCase.CounterEntriesParams
import ru.jeinmentalist.mail.domain.profile.profileUseCase.ProfileUseCases
import ru.jeinmentalist.mail.domain.timestamp.Timestamp
import ru.jeinmentalist.mail.domain.timestamp.timstampUseCase.LoadTimestampListUseCase
import ru.jeinmentalist.mail.domain.timestamp.timstampUseCase.TimestampsUseCases
import ru.jeinmentalist.mail.mnemolist.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val noteUseCase: NoteUseCases,
    private val timestampsUseCases: TimestampsUseCases,
    private val profileUseCases: ProfileUseCases,
    application: Application
) : BaseViewModel(application) {

    private val _noteIdLiveData = MutableLiveData<Int>()
    val mNoteIdLiveData: LiveData<Int>
        get() = _noteIdLiveData

    fun addNote(location: String, descriptor: String, profileId: String) {
        timestampsUseCases.loadTimestampList(LoadTimestampListUseCase.Params(profileId)){
            it.either({}, {
                noteUseCase.addNote(
                    AddNoteUseCase.Param(
                        location,
                        descriptor,
                        profileId,
                        System.currentTimeMillis().toString(),
                        getMinimalTimestamp(mapTimestampList(it)),
                        Note.PERFORMED
                    )
                ) {
                    it.either(::handleFailure,::updateNoteIdLiveData)
                }
               // изменение активных записей пока напишу здесь возможно прийдеться перенести
                profileUseCases.changeRunningEntries(CounterEntriesParams(profileId))
            })
        }

    }

    private fun getMinimalTimestamp(list: List<Long>): Long{
        return list.minOrNull() ?: 0
    }

    private fun mapTimestampList(list: List<Timestamp>): List<Long>{
        return list.map { it.executionTime }
    }


    private fun updateNoteIdLiveData(id: Int){
        _noteIdLiveData.postValue(id)
    }

}