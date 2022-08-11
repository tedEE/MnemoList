package ru.jeinmentalist.mail.domain.profile.profileUseCase

import ru.jeinmentalist.mail.domain.profile.IProfileRepository
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import ru.jeinmentalist.mail.domain.usecase.UseCase

class GetProfileListUseCase(private val repository: IProfileRepository)
    : UseCase<None, List<Profile>>() {
    override suspend fun run(params: None): Either<Failure, List<Profile>> {
        return repository.getList()
    }
}