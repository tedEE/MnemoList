package ru.jeinmentalist.mail.domain.profile.profileUseCase

import ru.jeinmentalist.mail.domain.profile.IProfileRepository
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class RemoveProfileUseCase(private val repository: IProfileRepository) :
    UseCase<RemoveProfileUseCase.Params, None>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return repository.remove(params.profile.profileId)
    }

        data class Params(val profile: Profile)
}