package ru.jeinmentalist.mail.mnemolist.screens.delegat

import ru.jeinmentalist.mail.domain.profile.Profile

interface IGetProfile {
    fun getProfile(profileId: String, callback: (Profile)->Unit)
}