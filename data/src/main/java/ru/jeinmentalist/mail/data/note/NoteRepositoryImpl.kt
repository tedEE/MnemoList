package ru.jeinmentalist.mail.data.note

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.jeinmentalist.mail.data.db.dao.NoteDao
import ru.jeinmentalist.mail.data.db.model.NoteEntity
import ru.jeinmentalist.mail.data.db.model.map
import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure

class NoteRepositoryImpl (private val dao: NoteDao) : INoteRepository {

    override fun add(
        location: String,
        description: String,
        profId: String,
        timeOfCreation: String,
        executableTimestamp: Long,
        state: Int
    ): Either<Failure, Int> {
        val id = dao.addNote(
            NoteEntity(
                location = location,
                description = description,
                profId = profId,
                timeOfCreation = timeOfCreation,
                executableTimestamp = executableTimestamp,
                state = state
            )
        )
        return Either.Right(id.toInt())
    }

    override fun getById(id: Int): Either<Failure, Note> {
        val noteEntity = dao.getNoteById(id)
        return Either.Right(noteEntity.map())
    }

    override fun getList(): Either<Failure, List<Note>> {
        TODO("Not yet implemented")
    }

    override fun getListByProfileId(profId: String): Either<Failure, List<Note>> {
        val listNote = dao.loadNoteListByIdProfile(profId)
        return Either.Right(listNote.map { it.map() })
    }

    override fun getNotesFlow(profId: String): Either<Failure, Flow<List<Note>>> {
        val flow = dao.loadNoteFlowByIdProfile(profId)
            .map { list->
                list.map {
                    it.map()
                }
            }
        return Either.Right(flow)
    }

    override fun updateExecutableTimestamp(id: Int, executableTimestamp: Long): Either<Failure, None> {
        dao.updateNoteExecutableTimestamp(id, executableTimestamp)
        return Either.Right(None())
    }

    override fun updateState(id: Int, state: Int): Either<Failure, None> {
        dao.updateNoteState(id, state)
        return Either.Right(None())
    }

    override fun getListIdPerformedNote(): Either<Failure, List<Int>> {
        val list = dao.loadPerformedNoteList()
        return Either.Right(list)
    }

    override fun deleteNote(note: Note): Either<Failure, None> {
        dao.deleteNote(
            NoteEntity(
            noteId = note.noteId,
            location = note.location,
            description = note.description,
            profId = note.profId,
            timeOfCreation = note.timeOfCreation,
            executableTimestamp = note.executableTimestamp,
            state = note.state
            )
        )
        return Either.Right(None())
    }

    override fun checkNoteForExistence(noteId: Int): Either<Failure, Boolean> {
        val bool = dao.checkForRecord(noteId)
        return Either.Right(bool)
    }


}