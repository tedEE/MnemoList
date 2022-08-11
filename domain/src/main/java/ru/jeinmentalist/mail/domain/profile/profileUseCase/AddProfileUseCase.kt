package ru.jeinmentalist.mail.domain.profile.profileUseCase

import ru.jeinmentalist.mail.domain.profile.IProfileRepository
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class AddProfileUseCase(private val repository: IProfileRepository) : UseCase<AddProfileUseCase.Params, None>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return repository.create(params.profileName, params.profileId, params.profileType)
    }

    data class Params( val profileName: String, val profileId: String, val profileType: Int)
}