package ru.jeinmentalist.mail.domain.note.noteUseCase

import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class UpdateNoteNextTimestampUseCase(private val repository: INoteRepository) : UseCase<UpdateNoteNextTimestampUseCase.Params, None>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return repository.updateNextTimestamp(params.noteId, params.nextTimestamp, params.state)
    }

    data class Params(val noteId: Int, val nextTimestamp: Long, val state: Int)
}