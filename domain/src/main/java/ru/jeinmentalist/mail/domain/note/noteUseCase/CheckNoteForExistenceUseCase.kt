package ru.jeinmentalist.mail.domain.note.noteUseCase

import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class CheckNoteForExistenceUseCase(private val repository: INoteRepository) : UseCase<CheckNoteForExistenceUseCase.Params, Boolean>() {

    override suspend fun run(params: Params): Either<Failure, Boolean> {
        return repository.checkNoteForExistence(params.noteId)
    }

    data class Params(val noteId: Int)
}