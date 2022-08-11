package ru.jeinmentalist.mail.domain.note.noteUseCase

import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class GetPerformedNotesUseCase(private val repository: INoteRepository) : UseCase<None, List<Int>>() {
    override suspend fun run(params: None): Either<Failure, List<Int>> {
        return repository.getListIdPerformedNote()
    }
}