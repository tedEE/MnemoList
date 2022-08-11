package ru.jeinmentalist.mail.data.db.dao

import androidx.room.*
import ru.jeinmentalist.mail.data.db.model.TimestampEntity

@Dao
interface TimestampDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTimestampList(timestamp: List<TimestampEntity>) // возвращает id добавленых записей

    @Query("SELECT * FROM ${TimestampEntity.TABLE_NAME_TIMESTAMP}")
    fun loadTimestampList(): List<TimestampEntity>

    @Query("SELECT * FROM ${TimestampEntity.TABLE_NAME_TIMESTAMP} WHERE profile_id = :profileId")
    fun loadTimestampListByProfileId(profileId: String): List<TimestampEntity>

    @Insert
    fun addTimestamp(timestamp: TimestampEntity)

    @Update
    fun updateTimestamp(timestamp: TimestampEntity)

    @Delete
    fun deleteTimestamp(timestamp: TimestampEntity)
}