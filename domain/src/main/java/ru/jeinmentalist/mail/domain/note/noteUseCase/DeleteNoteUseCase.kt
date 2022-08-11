package ru.jeinmentalist.mail.domain.note.noteUseCase

import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class DeleteNoteUseCase (private val repository: INoteRepository) : UseCase<DeleteNoteUseCase.Params, None>(){
    override suspend fun run(params: Params): Either<Failure, None> {
        return repository.deleteNote(params.note)
    }
    data class Params(val note: Note)
}