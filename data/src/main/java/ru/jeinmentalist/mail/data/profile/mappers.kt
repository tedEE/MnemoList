package ru.jeinmentalist.mail.data.profile

import ru.jeinmentalist.mail.data.db.model.ProfileEntity
import ru.jeinmentalist.mail.domain.profile.Profile

fun mapProfileEntityToProfile(pe: ProfileEntity): Profile{
    return Profile(
        profileId = pe.profileId,
        profileName = pe.profileName,
        profileType = pe.profileType,
        runningEntries = pe.runningEntries,
        completedEntries = pe.completedEntries,
        canceledEntries = pe.canceledEntries
    )
}

fun mapProfileToProfileEntity(p: Profile): ProfileEntity = ProfileEntity(
        profileId = p.profileId,
        profileName = p.profileName,
        profileType = p.profileType,
        completedEntries = p.completedEntries,
        runningEntries = p.runningEntries,
        canceledEntries = p.canceledEntries
    )
