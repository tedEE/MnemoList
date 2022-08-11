package ru.jeinmentalist.mail.domain.profile

import kotlinx.coroutines.flow.Flow
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure

interface IProfileRepository {
    fun create(name: String, id: String, type: Int): Either<Failure, None>
    fun getById(id: String): Either<Failure, Profile>
    fun remove(id: String): Either<Failure, None>
    fun getList(): Either<Failure, List<Profile>>
    fun getListFlow(): Either<Failure, Flow<List<Profile>>>
    fun changeCompletedEntries(profileId: String): Either<Failure, None>
    fun deleteCompletedEntries(profileId: String): Either<Failure, None>
    fun changeRunningEntries(profileId: String): Either<Failure, None>
    fun deleteRunningEntries(profileId: String): Either<Failure, None>
}