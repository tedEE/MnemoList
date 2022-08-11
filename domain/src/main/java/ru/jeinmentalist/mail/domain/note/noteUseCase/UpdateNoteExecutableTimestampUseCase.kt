package ru.jeinmentalist.mail.domain.note.noteUseCase

import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class UpdateNoteExecutableTimestampUseCase(private val repository: INoteRepository) : UseCase<UpdateNoteExecutableTimestampUseCase.Params, None>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return repository.updateExecutableTimestamp(params.noteId, params.executableTimestamp)
    }

    data class Params(val noteId: Int, val executableTimestamp: Long)
}