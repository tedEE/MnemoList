package ru.jeinmentalist.mail.domain.timestamp.timstampUseCase

import ru.jeinmentalist.mail.domain.timestamp.ITimestampRepository
import ru.jeinmentalist.mail.domain.timestamp.Timestamp
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class AddTimestampsListUseCase(private val repository: ITimestampRepository) : UseCase<AddTimestampsListUseCase.Params, None>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return repository.addList(params.timestampList)
    }

    data class Params( val timestampList: List<Timestamp>)
}