package ru.jeinmentalist.mail.domain.profile.profileUseCase

import ru.jeinmentalist.mail.domain.profile.IProfileRepository
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class UpdateProfileUseCase(private val repository: IProfileRepository)
    : UseCase<UpdateProfileUseCase.Params, None>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return repository.update(params.profile)
    }

    data class Params(val profile: Profile)
}