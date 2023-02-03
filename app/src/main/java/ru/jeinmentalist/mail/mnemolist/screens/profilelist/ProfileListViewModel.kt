package ru.jeinmentalist.mail.mnemolist.screens.profilelist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.domain.profile.profileUseCase.GetProfileListFlowUseCase
import ru.jeinmentalist.mail.domain.profile.profileUseCase.RemoveProfileUseCase
import ru.jeinmentalist.mail.domain.timestamp.Timestamp
import ru.jeinmentalist.mail.domain.timestamp.timstampUseCase.LoadTimestampListUseCase
import ru.jeinmentalist.mail.domain.timestamp.timstampUseCase.TimestampsUseCases
import ru.jeinmentalist.mail.domain.type.ITransmitted
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.mnemolist.base.BaseViewModel
import ru.jeinmentalist.mail.mnemolist.utils.convertTransmittedFromProfile
import javax.inject.Inject

@HiltViewModel
class ProfileListViewModel @Inject constructor(
//    private val profileUseCases: ProfileUseCases,
    private val getProfileListFlow: GetProfileListFlowUseCase,
    private val removeProfile: RemoveProfileUseCase,
    private val timestampsUseCases: TimestampsUseCases,
    application: Application
) : BaseViewModel(application) {

    private val _profileListLiveData = MutableLiveData<List<Profile>>()
    val profileListLiveData: LiveData<List<Profile>>
        get() = _profileListLiveData

    private val _timestampListLiveData = MutableLiveData<List<Timestamp>>()
    val timestampListLiveData: LiveData<List<Timestamp>>
        get() = _timestampListLiveData

    init {
        getProfileList()
    }

    private fun getProfileList() {
        getProfileListFlow(None()) {
            it.either(::handleFailure, ::handleProfileList)
        }
    }

    fun deleteProfile(profile: Profile) {
        removeProfile(RemoveProfileUseCase.Params(profile)) {
            it.either(
                {},
                {}
            )
        }
    }

    fun getTimestampList(profileId: String, success: (List<Timestamp>) -> Unit) {
        timestampsUseCases.loadTimestampList(LoadTimestampListUseCase.Params(profileId)) {
            it.either(::handleFailure) {
                success(it)
            }
        }
    }

    private fun handleProfileList(flow: Flow<List<Profile>>) {
        viewModelScope.launch {
            liveDataFromFlow(flow)
        }
    }

    private fun handleTimestampList(list: List<Timestamp>) {
        _timestampListLiveData.postValue(list)
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
}