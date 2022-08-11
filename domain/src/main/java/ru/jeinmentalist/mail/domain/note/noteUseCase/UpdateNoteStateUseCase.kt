package ru.jeinmentalist.mail.domain.note.noteUseCase

import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class UpdateNoteStateUseCase(private val repository: INoteRepository) : UseCase<UpdateNoteStateUseCase.Param, None>() {
    override suspend fun run(params: Param): Either<Failure, None> {
        return repository.updateState(params.noteId, params.state)
    }

    data class Param(val noteId: Int, val state: Int)
}