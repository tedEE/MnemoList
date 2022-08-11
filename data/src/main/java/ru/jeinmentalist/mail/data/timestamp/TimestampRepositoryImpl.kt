package ru.jeinmentalist.mail.data.timestamp

import ru.jeinmentalist.mail.data.db.dao.TimestampDao
import ru.jeinmentalist.mail.data.db.model.TimestampEntity
import ru.jeinmentalist.mail.data.db.model.map
import ru.jeinmentalist.mail.domain.timestamp.ITimestampRepository
import ru.jeinmentalist.mail.domain.timestamp.Timestamp
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure
import javax.inject.Inject

class TimestampRepositoryImpl(private val dao: TimestampDao) : ITimestampRepository {

    override fun addList(timestampList: List<Timestamp>): Either<Failure, None> {
        val list = timestampList.map {
            map(it)
        }
        dao.addTimestampList(list)
        return Either.Right(None())
    }

    override fun getList(): Either<Failure, List<Timestamp>> {
        val list = dao.loadTimestampList().map { it.map() }
        return Either.Right(list)
    }

    override fun getListByProfileId(id: String): Either<Failure, List<Timestamp>> {
        val list = dao.loadTimestampListByProfileId(id).map { it.map() }
        return Either.Right(list)
    }

    override fun add(timestamp: Timestamp): Either.Right<None> {
        dao.addTimestamp(map(timestamp))
        return Either.Right(None())
    }

    override fun update(timestamp: Timestamp): Either<Failure, None> {
        dao.updateTimestamp(map(timestamp))
        return Either.Right(None())
    }

    override fun delete(timestamp: Timestamp): Either<Failure, None> {
        dao.deleteTimestamp(map(timestamp))
        return Either.Right(None())
    }

    private fun map(timestamp: Timestamp): TimestampEntity {
        return TimestampEntity(
            profileId = timestamp.profileId,
            executionStatus = timestamp.executionStatus,
            executionTime = timestamp.executionTime
        )
    }
}