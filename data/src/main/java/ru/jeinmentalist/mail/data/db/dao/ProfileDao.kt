package ru.jeinmentalist.mail.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.jeinmentalist.mail.data.db.model.ProfileEntity

@Dao
interface ProfileDao {
    @Query("SELECT * FROM ${ProfileEntity.TABLE_NAME_PROFILE}")
    fun loadProfileList(): List<ProfileEntity>

    @Query("SELECT * FROM ${ProfileEntity.TABLE_NAME_PROFILE}")
    fun loadProfileListFlow(): Flow<List<ProfileEntity>>

    @Insert(entity = ProfileEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addProfile(entity: ProfileEntity)

    @Query("DELETE FROM ${ProfileEntity.TABLE_NAME_PROFILE} WHERE profile_id = :id")
    fun deleteProfile(id: String)

    @Query("SELECT * FROM ${ProfileEntity.TABLE_NAME_PROFILE} WHERE profile_id = :id")
    fun getProfileById(id: String): ProfileEntity

    @Query("UPDATE ${ProfileEntity.TABLE_NAME_PROFILE}" +
            " SET completed_entries = completed_entries + 1, running_entries = running_entries - 1 WHERE profile_id = :profileId")
    fun changeCompletedEntries(profileId: String)

    @Query("UPDATE ${ProfileEntity.TABLE_NAME_PROFILE} SET completed_entries = completed_entries - 1 WHERE profile_id = :profileId")
    fun deleteCompletedEntries(profileId: String)

    @Query("UPDATE ${ProfileEntity.TABLE_NAME_PROFILE} SET running_entries = running_entries + 1 WHERE profile_id = :profileId")
    fun changeRunningEntries(profileId: String)

    @Query("UPDATE ${ProfileEntity.TABLE_NAME_PROFILE} SET running_entries = running_entries - 1 WHERE profile_id = :profileId")
    fun deleteRunningEntries(profileId: String)
}