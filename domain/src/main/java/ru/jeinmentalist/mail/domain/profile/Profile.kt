package ru.jeinmentalist.mail.domain.profile

import ru.jeinmentalist.mail.domain.type.ITransmitted

data class Profile(
    val profileId: String,
    val profileName: String,
    val profileType: Int,
    val completedEntries: Int ,
    val runningEntries: Int
): ITransmitted