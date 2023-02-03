package ru.jeinmentalist.mail.mnemolist.screens.delegat

import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.domain.profile.profileUseCase.GetProfileByIdUseCase

class GatProfileImp (
    val getProfileUC: GetProfileByIdUseCase,
) : IGetProfile{

    override fun getProfile(profileId: String, callback: (Profile) -> Unit) {
        getProfileUC(GetProfileByIdUseCase.Params(profileId)){
            it.either({}){
                callback.invoke(it)
            }
        }
    }
}