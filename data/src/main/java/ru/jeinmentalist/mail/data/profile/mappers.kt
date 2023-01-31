package ru.jeinmentalist.mail.data.profile

import ru.jeinmentalist.mail.data.db.model.ProfileEntity
import ru.jeinmentalist.mail.data.db.model.TimestampEntity
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.domain.timestamp.Timestamp

fun mapProfileEntityToProfile(
    pe: ProfileEntity,
    timestamps: List<TimestampEntity> = listOf()
): Profile {
    return Profile(
        profileId = pe.profileId,
        profileName = pe.profileName,
        profileType = pe.profileType,
        runningEntries = pe.runningEntries,
        completedEntries = pe.completedEntries,
        canceledEntries = pe.canceledEntries,
        timestampList = timestamps.map { tse: TimestampEntity -> mapTimestampsEntityToTimestamp(tse) }
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

fun mapTimestampsEntityToTimestamp(tse: TimestampEntity): Timestamp = Timestamp(
    profileId = tse.profileId,
    executionStatus = tse.executionStatus,
    executionTime = tse.executionTime
)
