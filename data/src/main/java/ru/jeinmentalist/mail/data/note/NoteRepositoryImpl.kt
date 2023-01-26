package ru.jeinmentalist.mail.data.note

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.jeinmentalist.mail.data.db.dao.NoteDao
import ru.jeinmentalist.mail.data.db.model.NoteEntity
import ru.jeinmentalist.mail.data.db.model.TimestampEntity
import ru.jeinmentalist.mail.data.db.model.map
import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure

class NoteRepositoryImpl(private val dao: NoteDao) : INoteRepository {

    override fun add(
        location: String,
        description: String,
        profId: String,
        timeOfCreation: String,
        currentRunningTimestamp: Long,
        nextRunningTimestamp: Long,
        state: Int,
        pathImage: String
    ): Either<Failure, Int> {
        val id = dao.addNote(
            NoteEntity(
                location = location,
                description = description,
                profId = profId,
                timeOfCreation = timeOfCreation,
                currentRunningTimestamp = currentRunningTimestamp,
                nextRunningTimestamp = nextRunningTimestamp,
                state = state,
                pathImage = pathImage
            )
        )
        return Either.Right(id.toInt())
    }

    override fun getById(id: Int): Either<Failure, Note> {
//        val noteEntity = dao.getNoteById(id)
//        return Either.Right(noteEntity.map())
        val noteMap = dao.getNoteAndTimestampList(id)
        val note: NoteEntity = noteMap.keys.toList()[0]
        val list: List<TimestampEntity> = noteMap[note] ?: listOf()
        return Either.Right(note.map(list))
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
            .map { list ->
                list.map {
                    it.map()
                }
            }
        return Either.Right(flow)
    }

    override fun updateNextTimestamp(
        id: Int,
        nextTimestamp: Long,
        state: Int
    ): Either<Failure, None> {
        dao.updateNoteNextTimestamp(id, nextTimestamp, state)
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
                currentRunningTimestamp = note.currentRunningTimestamp,
                nextRunningTimestamp = note.nextRunningTimestamp,
                state = note.state,
                pathImage = note.pathImage
                )
        )
        return Either.Right(None())
    }

    override fun checkNoteForExistence(noteId: Int): Either<Failure, Boolean> {
        val bool = dao.checkForRecord(noteId)
        return Either.Right(bool)
    }


}