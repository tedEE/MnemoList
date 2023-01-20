package ru.jeinmentalist.mail.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.jeinmentalist.mail.data.db.model.NoteEntity
import ru.jeinmentalist.mail.data.db.model.TimestampEntity

@Dao
interface NoteDao {

    @Query("SELECT * FROM ${NoteEntity.TABLE_NAME_NOTE}")
    fun loadNoteList(): LiveData<NoteEntity>

    @Query("SELECT note_id FROM ${NoteEntity.TABLE_NAME_NOTE} WHERE state = 1")
    fun loadPerformedNoteList(): List<Int>

    @Query("SELECT * FROM ${NoteEntity.TABLE_NAME_NOTE} WHERE prof_id = :idProfile")
    fun loadNoteListByIdProfile(idProfile: String): List<NoteEntity>

    @Query("SELECT * FROM ${NoteEntity.TABLE_NAME_NOTE} WHERE prof_id = :idProfile")
    fun loadNoteFlowByIdProfile(idProfile: String): Flow<List<NoteEntity>>

    @Insert(entity = NoteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addNote(note: NoteEntity): Long

    @Delete(entity = NoteEntity::class)
    fun deleteNote(entity: NoteEntity)

//    @Query("SELECT * FROM ${NoteEntity.TABLE_NAME_NOTE} WHERE note_id = :id" )
//    fun getNoteById(id: Int): NoteEntity

    @Query("SELECT * FROM ${NoteEntity.TABLE_NAME_NOTE} WHERE note_id = :id" )
    fun getNoteById(id: Int): NoteEntity

    @Query("UPDATE ${NoteEntity.TABLE_NAME_NOTE} SET executable_timestamp = :executableTimestamp WHERE note_id = :id")
    fun updateNoteExecutableTimestamp(id: Int, executableTimestamp: Long)

    @Query("UPDATE ${NoteEntity.TABLE_NAME_NOTE} SET state = :state WHERE note_id = :id")
    fun updateNoteState(id: Int, state: Int)

    @Query("SELECT EXISTS(SELECT note_id FROM ${NoteEntity.TABLE_NAME_NOTE} WHERE note_id = :noteId)")
    fun checkForRecord(noteId: Int): Boolean
}