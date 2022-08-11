package ru.jeinmentalist.mail.domain.note.noteUseCase

import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class GetNoteByProfileIdUseCase(private val repository: INoteRepository)
    : UseCase<GetNoteByProfileIdUseCase.Params, List<Note>>() {

    override suspend fun run(params: Params): Either<Failure, List<Note>> {
        return repository.getListByProfileId(params.profileId)
    }

    data class Params(val profileId: String)
}