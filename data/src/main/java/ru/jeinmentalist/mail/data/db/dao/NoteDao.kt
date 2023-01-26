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
    @Query("SELECT * FROM ${NoteEntity.TABLE_NAME_NOTE} AS n JOIN ${TimestampEntity.TABLE_NAME_TIMESTAMP} AS t ON n.prof_id = t.profile_id WHERE n.note_id = :id" )
    fun getNoteAndTimestampList(id: Int): Map<NoteEntity, List<TimestampEntity>>

    @Query("SELECT * FROM ${NoteEntity.TABLE_NAME_NOTE} WHERE note_id = :id" )
    fun getNoteById(id: Int): NoteEntity

    @Query("UPDATE ${NoteEntity.TABLE_NAME_NOTE} SET next_running_timestamp = :nextTimestamp, state = :state WHERE note_id = :id")
    fun updateNoteNextTimestamp(id: Int, nextTimestamp: Long, state: Int)

    @Query("UPDATE ${NoteEntity.TABLE_NAME_NOTE} SET state = :state WHERE note_id = :id")
    fun updateNoteState(id: Int, state: Int)

    @Query("SELECT EXISTS(SELECT note_id FROM ${NoteEntity.TABLE_NAME_NOTE} WHERE note_id = :noteId)")
    fun checkForRecord(noteId: Int): Boolean
}