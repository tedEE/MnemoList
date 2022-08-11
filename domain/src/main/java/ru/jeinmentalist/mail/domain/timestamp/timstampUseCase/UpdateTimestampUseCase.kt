package ru.jeinmentalist.mail.domain.timestamp.timstampUseCase

import ru.jeinmentalist.mail.domain.timestamp.ITimestampRepository
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class UpdateTimestampUseCase(val repository: ITimestampRepository) : UseCase<ParamsTimestamp, None>() {

    override suspend fun run(params: ParamsTimestamp): Either<Failure, None> {
        return repository.update(params.timestamp)
    }

}