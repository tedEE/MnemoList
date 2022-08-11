package ru.jeinmentalist.mail.domain.profile.profileUseCase

import ru.jeinmentalist.mail.domain.profile.IProfileRepository
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class DecrementCounterRunningEntries (private val repository: IProfileRepository) : UseCase<CounterEntriesParams, None>() {

    override suspend fun run(params: CounterEntriesParams): Either<Failure, None> {
        return repository.deleteRunningEntries(params.profileId)
    }
}