package ru.jeinmentalist.mail.domain.note.noteUseCase

import kotlinx.coroutines.flow.Flow
import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class GetNotesFlowUseCase(private val repository: INoteRepository) : UseCase<GetNotesFlowUseCase.Params, Flow<List<Note>>>() {

    override suspend fun run(params: Params): Either<Failure, Flow<List<Note>>> {
        return repository.getNotesFlow(params.profileId)
    }

    data class Params(val profileId: String)
}