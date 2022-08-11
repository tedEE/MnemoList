package ru.jeinmentalist.mail.mnemolist.utils

import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.domain.type.ITransmitted

fun convertTransmittedFromProfile(item: ITransmitted): Profile {
    return item as Profile
}

fun convertTransmittedFromNote(item: ITransmitted): Note {
    return item as Note
}