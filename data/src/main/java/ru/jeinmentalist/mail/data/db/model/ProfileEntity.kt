package ru.jeinmentalist.mail.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.jeinmentalist.mail.domain.profile.Profile

@Entity(tableName = ProfileEntity.TABLE_NAME_PROFILE)
data class ProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "profile_id")
    val profileId: String = "0",
    @ColumnInfo(name = "profile_name")
    val profileName: String,
    @ColumnInfo(name = "profile_type")
    val profileType: Int,
    @ColumnInfo(name = "completed_entries") // выполненые записи
    val completedEntries: Int,
    @ColumnInfo(name = "running_entries")// выполняемые записи
    val running_entries: Int,
){
    companion object{
        const val TABLE_NAME_PROFILE = "profile_table"
    }
}

fun ProfileEntity.map() =
    Profile(
        profileId = profileId,
        profileName = profileName,
        profileType = profileType,
        runningEntries = running_entries,
        completedEntries = completedEntries
    )
