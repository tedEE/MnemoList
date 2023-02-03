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
import ru.jeinmentalist.mail.domain.note.noteUseCase.GetNotesFlowUseCase
import ru.jeinmentalist.mail.domain.note.noteUseCase.NoteUseCases
import ru.jeinmentalist.mail.domain.type.ITransmitted
import ru.jeinmentalist.mail.mnemolist.base.BaseViewModel
import ru.jeinmentalist.mail.mnemolist.screens.delegat.IGetProfile
import ru.jeinmentalist.mail.mnemolist.utils.convertTransmittedFromNote
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    val useCase: NoteUseCases,
//    val getProfileUC: GetProfileByIdUseCase,
//    val updateProfile: UpdateProfileUseCase,
    delegate: IGetProfile,
    application: Application
) : BaseViewModel(application), IGetProfile by delegate{

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
//                changeProfile(note)

//                if (note.state == Note.Done().state){
//                    getProfile(note.profId){
//
//                    }
//                }else if (note.state == Note.Running().state){
////                    decrementRunngEntries(CounterEntriesParams(note.profId))
//                }

            }
        }
    }

    private fun handleNoteList(flow: Flow<List<Note>>){
        viewModelScope.launch {
            liveDataFromFlow(flow)
        }
    }

    private fun changeProfile(note: Note){
        when(note.state){
            Note.Done().state -> {
                getProfile(note.profId){
//                    it.completedEntries --
//                    saveProfile(it)
                }
            }
            Note.Running().state -> {
                getProfile(note.profId){
//                    it.runningEntries --
//                    saveProfile(it)
                }
            }
            Note.Canceled().state -> {
                getProfile(note.profId){
//                    it.canceledEntries --
//                    saveProfile(it)
                }
            }
        }
    }

//    private fun getProfile(profileId: String, callback: (Profile)->Unit){
//        getProfileUC(GetProfileByIdUseCase.Params(profileId)){
//            it.either({}){
//                callback.invoke(it)
//            }
//        }
//    }
//
//    private fun saveProfile(pr: Profile){
//        updateProfile(UpdateProfileUseCase.Params(pr))
//    }

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