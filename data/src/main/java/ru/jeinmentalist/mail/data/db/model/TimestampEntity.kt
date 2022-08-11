package ru.jeinmentalist.mail.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.jeinmentalist.mail.domain.timestamp.Timestamp

@Entity(
    tableName = TimestampEntity.TABLE_NAME_TIMESTAMP,
    foreignKeys = [ForeignKey(
        entity = ProfileEntity::class,
        parentColumns = ["profile_id"],
        childColumns = ["profile_id"],
        onDelete = ForeignKey.CASCADE
    )],
    primaryKeys = ["profile_id", "execution_time"]
)
data class TimestampEntity(
    @ColumnInfo(name = "profile_id")
    val profileId: String,
    @ColumnInfo(name = "execution_status") // статус исполнения
    val executionStatus: Int,
    @ColumnInfo(name = "execution_time") // время исполнения
    val executionTime: Long,
) {
    companion object {
        const val TABLE_NAME_TIMESTAMP = "timestamp_table"
    }
}

fun TimestampEntity.map() =
    Timestamp(
        profileId = this.profileId,
        executionStatus = this.executionStatus,
        executionTime = this.executionTime
    )
