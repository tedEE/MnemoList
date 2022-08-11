package ru.jeinmentalist.mail.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.jeinmentalist.mail.data.db.model.NoteEntity.Companion.TABLE_NAME_NOTE
import ru.jeinmentalist.mail.domain.note.Note

@Entity(tableName = TABLE_NAME_NOTE,
    foreignKeys = [ForeignKey(
        entity = ProfileEntity::class,
        parentColumns = ["profile_id"],
        childColumns = ["prof_id"],
        onDelete = ForeignKey.CASCADE
    )],
)
data class NoteEntity(
    // autoGenerate нужно будет убрать при добавлении работы с сервером
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    val noteId: Int = 0,
    val location: String,
    val description: String,
    @ColumnInfo(name = "prof_id")
    val profId: String,
    @ColumnInfo(name = "time_of_creation")
    val timeOfCreation: String,
    @ColumnInfo(name = "executable_timestamp")
    val executableTimestamp: Long = 0,
    val state: Int
) {
    companion object{
        const val TABLE_NAME_NOTE = "notes_table"
    }
}

fun NoteEntity.map() =
    Note(
        noteId = this.noteId,
        location = this.location,
        description = this.description,
        profId = this.profId,
        timeOfCreation = this.timeOfCreation,
        executableTimestamp = this.executableTimestamp,
        state = state
    )