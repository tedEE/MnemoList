package ru.jeinmentalist.mail.domain.note.noteUseCase


import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class AddNoteUseCase(private val repository: INoteRepository) : UseCase<AddNoteUseCase.Param, Int>(){
    override suspend fun run(params: Param): Either<Failure, Int> {
        return repository.add(params.location, params.description, params.profId, params.timeOfCreation, params.executableTimestamp, params.state, params.pathImage)
    }

    data class Param(
        val location: String,
        val description: String,
        val profId: String,
        val timeOfCreation: String,
        val executableTimestamp: Long,
        val state: Int,
        val pathImage: String
    )
}