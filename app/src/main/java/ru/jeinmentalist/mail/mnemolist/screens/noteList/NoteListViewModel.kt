package ru.jeinmentalist.mail.mnemolist.screens.noteList

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.note.noteUseCase.DeleteNoteUseCase
import ru.jeinmentalist.mail.domain.note.noteUseCase.GetNoteByProfileIdUseCase
import ru.jeinmentalist.mail.domain.note.noteUseCase.GetNotesFlowUseCase
import ru.jeinmentalist.mail.domain.note.noteUseCase.NoteUseCases
import ru.jeinmentalist.mail.domain.profile.profileUseCase.CounterEntriesParams
import ru.jeinmentalist.mail.domain.profile.profileUseCase.DecrementCounterCompletedEntries
import ru.jeinmentalist.mail.domain.profile.profileUseCase.DecrementCounterRunningEntries
import ru.jeinmentalist.mail.domain.type.ITransmitted
import ru.jeinmentalist.mail.mnemolist.base.BaseViewModel
import ru.jeinmentalist.mail.mnemolist.utils.convertTransmittedFromNote
import ru.jeinmentalist.mail.mnemolist.utils.convertTransmittedFromProfile
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    val useCase: NoteUseCases,
    val decrementCompleteEntries: DecrementCounterCompletedEntries,
    val decrementRunngEntries: DecrementCounterRunningEntries,
    application: Application
) : BaseViewModel(application) {

    private val _noteListLiveData = MutableLiveData<List<Note>>()
    val notListLiveData: LiveData<List<Note>>
        get() = _noteListLiveData


    fun getListNote(id: String) {
        useCase.getNotesFlow(GetNotesFlowUseCase.Params(id)) {
            it.either(::handleFailure, ::handleNoteList)
        }
    }

    fun deleteNote(note: Note){
        useCase.deleteNote(DeleteNoteUseCase.Params(note)){
            it.either(::handleFailure){
                if (note.state == Note.DONE){
                    decrementCompleteEntries(CounterEntriesParams(note.profId))
                }else if (note.state == Note.PERFORMED){
                    decrementRunngEntries(CounterEntriesParams(note.profId))
                }

            }
        }
    }

    private fun handleNoteList(flow: Flow<List<Note>>){
        viewModelScope.launch {
            liveDataFromFlow(flow)
        }
    }

    private suspend fun liveDataFromFlow(flow: Flow<List<ITransmitted>>) {
        flow
            .map{
                it.map { convertTransmittedFromNote(it) }
            }
            .collect {
                _noteListLiveData.postValue(it)
            }

    }
}