package ru.jeinmentalist.mail.data.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.jeinmentalist.mail.data.db.dao.ProfileDao
import ru.jeinmentalist.mail.data.db.model.ProfileEntity
import ru.jeinmentalist.mail.data.db.model.map
import ru.jeinmentalist.mail.domain.profile.IProfileRepository
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import javax.inject.Inject

class ProfileRepositoryImpl (private val dao: ProfileDao) :
    IProfileRepository {

    override fun create(name: String, id: String, type: Int): Either<Failure, None> {
        dao.addProfile(ProfileEntity(profileId = id, profileName = name, profileType = type, completedEntries = 0, runningEntries = 0, canceledEntries = 0))
        return Either.Right(None())
    }

    override fun getById(id: String): Either<Failure,Profile> {
        val profile = dao.getProfileById(id)
//        return Either.Right(profile.map())
        return Either.Right(mapProfileEntityToProfile(profile))
    }

    override fun remove(id: String): Either<Failure, None> {
        dao.deleteProfile(id)
        return Either.Right(None())
    }

    override fun getList(): Either<Failure, List<Profile>> {
        val list = dao.loadProfileList()
//        val resultList: List<Profile> = list.map { it.map() }
        val resultList: List<Profile> = list.map { mapProfileEntityToProfile(it) }
        return Either.Right(resultList)
    }

    override fun getListFlow(): Either<Failure, Flow<List<Profile>>> {
        val flow = dao.loadProfileListFlow()
            .map { listProfile: List<ProfileEntity> ->
                listProfile.map{ profileEntity: ProfileEntity ->
//                    profileEntity.map()
                    mapProfileEntityToProfile(profileEntity)
                }
            }
        return Either.Right(flow)
    }

    override fun update(profile: Profile): Either<Failure, None> {
        dao.updateProfile(mapProfileToProfileEntity(profile))
        return Either.Right(None())
    }
//
//    override fun changeCompletedEntries(profileId: String): Either<Failure, None> {
//        dao.changeCompletedEntries(profileId)
//        return Either.Right(None())
//    }
//
//    override fun deleteCompletedEntries(profileId: String): Either<Failure, None> {
//        dao.deleteCompletedEntries(profileId)
//        return Either.Right(None())
//    }
//
//    override fun changeRunningEntries(profileId: String): Either<Failure, None> {
//        dao.changeRunningEntries(profileId)
//        return Either.Right(None())
//    }
//
//    override fun deleteRunningEntries(profileId: String): Either<Failure, None> {
//        dao.deleteRunningEntries(profileId)
//        return Either.Right(None())
//    }

}

