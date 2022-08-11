package ru.jeinmentalist.mail.domain.note.noteUseCase


import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class GetNoteByIdUseCase(private val repository: INoteRepository) : UseCase<GetNoteByIdUseCase.Params, Note>() {

    override suspend fun run(params: Params): Either<Failure, Note> {
        return repository.getById(params.id)
    }

    data class Params(val id: Int)
}