package ru.jeinmentalist.mail.domain.timestamp.timstampUseCase

import ru.jeinmentalist.mail.domain.timestamp.ITimestampRepository
import ru.jeinmentalist.mail.domain.timestamp.Timestamp
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class LoadTimestampListUseCase(val repository: ITimestampRepository) : UseCase <LoadTimestampListUseCase.Params, List<Timestamp>>() {

    override suspend fun run(params: Params): Either<Failure, List<Timestamp>> {
        return repository.getListByProfileId(params.profileId)
    }

    data class Params(val profileId: String)
}