package ru.jeinmentalist.mail.mnemolist.screens.createProfile

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.jeinmentalist.mail.domain.profile.profileUseCase.AddProfileUseCase
import ru.jeinmentalist.mail.domain.timestamp.Timestamp
import ru.jeinmentalist.mail.domain.timestamp.timstampUseCase.AddTimestampsListUseCase
import ru.jeinmentalist.mail.domain.timestamp.timstampUseCase.TimestampsUseCases
import ru.jeinmentalist.mail.mnemolist.base.BaseViewModel
import ru.jeinmentalist.mail.mnemolist.utils.convertKeyMap
import ru.jeinmentalist.mail.mnemolist.utils.timeInterval
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel@Inject constructor(
//    private val profileUseCases: ProfileUseCases,
    private val addProfileUseCase: AddProfileUseCase,
    private val timestampUseCases: TimestampsUseCases,
    application: Application
) : BaseViewModel(application) {


    fun createProfile(profileName: String, profileId: String,  list: List<Timestamp>){
        // как то надо реализовать установку типа профиля
        addProfileUseCase(AddProfileUseCase.Params(profileName, profileId=profileId, profileType = 0)){
            it.either(::handleFailure
            ) { addListTimestamp(list) }// надо подумать как передать эту функцию в виде сслыки
        }
    }

    private fun addListTimestamp(list: List<Timestamp>){
        timestampUseCases.addTimestampsList(AddTimestampsListUseCase.Params(list)){
            it.either(
                {},
                {}
            )
        }
    }
    //----------------------------------- методы работы с мапой, возможно вынесу их в файл хелпер
//    val timeInterval = mapOf(
////        R.string.minute to createPair(60000, (10..60 step 10)),
//        R.string.minute to createPair(60000, 1..60),
//        R.string.hour to createPair(3600000, 1..24),
//        R.string.day to createPair(86400000, 1..7),
//        R.string.week to createPair(604800000, 1..4),
//        R.string.month to createPair(2592000000, 1..6)
//    )
    val mapKeyToString = timeInterval.keys.map { convertKeyMap(it, getApplication()) }
    val mapKeyToInt = timeInterval.keys.toList()
//
//    fun selectElementFromTimeInterval(key: Int): Pair<Long, IntProgression>{
//        return timeInterval[key] ?: createPair(1, 0..1)
//    }
//
//    private fun convertKeyMap(key: Int): String{
//        return getApplication<Application>().resources.getString(key)
//    }
//
//    private fun createPair(f: Long, s: IntProgression): Pair<Long, IntProgression>{
//        return f to s
//    }
}