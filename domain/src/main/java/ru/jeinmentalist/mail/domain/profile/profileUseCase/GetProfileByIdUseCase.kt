package ru.jeinmentalist.mail.domain.profile.profileUseCase

import ru.jeinmentalist.mail.domain.profile.IProfileRepository
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class GetProfileByIdUseCase(private val repository: IProfileRepository)
    : UseCase<GetProfileByIdUseCase.Params, Profile>() {
    override suspend fun run(params: GetProfileByIdUseCase.Params): Either<Failure, Profile> {
        return repository.getById(params.profileId)
    }

    data class Params( val profileId: String)
}