package ru.jeinmentalist.mail.domain.timestamp

import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.None
import ru.jeinmentalist.mail.domain.type.exception.Failure

interface ITimestampRepository {
    fun addList(timestampList: List<Timestamp>): Either<Failure, None>
    fun getList(): Either<Failure, List<Timestamp>>
    fun getListByProfileId(id: String): Either<Failure, List<Timestamp>>
    fun add(timestamp: Timestamp): Either<Failure, None>
    fun update(timestamp: Timestamp): Either<Failure, None>
    fun delete(timestamp: Timestamp ): Either<Failure, None>
}