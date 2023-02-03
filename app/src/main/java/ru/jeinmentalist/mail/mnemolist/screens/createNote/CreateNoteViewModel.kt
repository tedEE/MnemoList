package ru.jeinmentalist.mail.mnemolist.screens.createNote

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
import ru.jeinmentalist.mail.domain.note.noteUseCase.AddNoteUseCase
import ru.jeinmentalist.mail.domain.note.noteUseCase.NoteUseCases
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.domain.profile.profileUseCase.GetProfileListFlowUseCase
import ru.jeinmentalist.mail.domain.timestamp.Timestamp
import ru.jeinmentalist.mail.domain.type.ITransmitted
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.mnemolist.base.BaseViewModel
import ru.jeinmentalist.mail.mnemolist.screens.delegat.IGetProfile
import ru.jeinmentalist.mail.mnemolist.utils.convertTransmittedFromProfile
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val noteUseCase: NoteUseCases,
//    private val timestampsUseCases: TimestampsUseCases,
//    private val profileUseCases: ProfileUseCases,
//    private val getProfileByIdUC: GetProfileByIdUseCase,
//    private val updateProfile: UpdateProfileUseCase
    private val getProfileListFlow: GetProfileListFlowUseCase,
    delegate: IGetProfile,
    application: Application
) : BaseViewModel(application), IGetProfile by delegate {

    private val _noteIdLiveData = MutableLiveData<Int>()

    private val _profileListLiveData = MutableLiveData<List<Profile>>()
    val profileListLiveData: LiveData<List<Profile>>
        get() = _profileListLiveData

    val mNoteIdLiveData: LiveData<Int>
        get() = _noteIdLiveData

    init {
        getProfileList()
    }

    fun addNote(location: String, descriptor: String, profileId: String, pathImage: String) {
        getProfile(profileId) { profile: Profile ->
            val timestamps = profile.timestampList
            noteUseCase.addNote(
                AddNoteUseCase.Param(
                    location,
                    descriptor,
                    profileId,
                    System.currentTimeMillis().toString(),
                    nextRunningTimestamp = getMinimalTimestamp(mapTimestampList(timestamps)),
                    currentRunningTimestamp = System.currentTimeMillis(),
                    state = Note.Running().state,
                    pathImage = pathImage
                )
            ) {
                it.either(::handleFailure, ::updateNoteIdLiveData)
            }
        }
    }

//    fun addNote(location: String, descriptor: String, profileId: String, pathImage: String) {
//        timestampsUseCases.loadTimestampList(LoadTimestampListUseCase.Params(profileId)){
//            it.either({}, {
//                noteUseCase.addNote(
//                    AddNoteUseCase.Param(
//                        location,
//                        descriptor,
//                        profileId,
//                        System.currentTimeMillis().toString(),
//                        nextRunningTimestamp = getMinimalTimestamp(mapTimestampList(it)),
//                        currentRunningTimestamp = System.currentTimeMillis(),
//                        state = Note.Running().state,
//                        pathImage = pathImage
//                    )
//                ) {
//                    it.either(::handleFailure,::updateNoteIdLiveData)
//                }
//               // изменение активных записей пока напишу здесь возможно прийдеться перенести
////                profileUseCases.changeRunningEntries(CounterEntriesParams(profileId))
//            })
//        }
//
//    }

    fun getProfileList() {
        getProfileListFlow(None()) {
            it.either(::handleFailure, ::handleProfileList)
        }
    }

    private fun handleProfileList(flow: Flow<List<Profile>>) {
        viewModelScope.launch {
            liveDataFromFlow(flow)
        }
    }

    private suspend fun liveDataFromFlow(flow: Flow<List<ITransmitted>>) {
        flow
            .map {
                it.map { convertTransmittedFromProfile(it) }
            }
            .collect {
                _profileListLiveData.postValue(it)
            }
    }

    private fun getMinimalTimestamp(list: List<Long>): Long {
        return list.minOrNull() ?: 0
    }

    private fun mapTimestampList(list: List<Timestamp>): List<Long> {
        return list.map { it.executionTime }
    }


    private fun updateNoteIdLiveData(id: Int) {
        _noteIdLiveData.postValue(id)
    }

}