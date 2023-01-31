package ru.jeinmentalist.mail.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

data class ProfileWithTimestamps(
    @Embedded val profile: ProfileEntity,
    @Relation(
        parentColumn = "profile_id",
        entityColumn = "profile_id"
    )
    val timestamps: List<TimestampEntity>
)