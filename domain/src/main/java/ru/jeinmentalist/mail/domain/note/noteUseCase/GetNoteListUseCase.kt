package ru.jeinmentalist.mail.domain.note.noteUseCase

import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class GetNoteListUseCase (private val repository: INoteRepository)
    : UseCase<Note, List<Note>>() {
    override suspend fun run(params: Note): Either<Failure, List<Note>> {
        return repository.getList()
    }
}