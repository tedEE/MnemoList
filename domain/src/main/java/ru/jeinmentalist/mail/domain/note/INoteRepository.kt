package ru.jeinmentalist.mail.domain.note

import kotlinx.coroutines.flow.Flow
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure

interface INoteRepository {
    fun add(location: String, description: String, profId: String, timeOfCreation: String, executableTimestamp: Long, state: Int, pathImage: String): Either<Failure, Int>
    fun getById(id: Int): Either<Failure, Note>
    fun getList(): Either<Failure, List<Note>>
    fun getListByProfileId(profId: String): Either<Failure, List<Note>>
    fun getNotesFlow(profId: String): Either<Failure, Flow<List<Note>>>
    fun updateExecutableTimestamp(id: Int, executableTimestamp: Long): Either<Failure, None>
    fun updateState(id: Int, state: Int): Either<Failure, None>
    fun getListIdPerformedNote(): Either<Failure, List<Int>>
    fun deleteNote(note: Note): Either<Failure, None>
    fun checkNoteForExistence(noteId: Int): Either<Failure, Boolean>
}