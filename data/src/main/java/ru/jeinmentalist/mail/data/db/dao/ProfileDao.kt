package ru.jeinmentalist.mail.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.jeinmentalist.mail.data.db.model.ProfileEntity
import ru.jeinmentalist.mail.data.db.model.ProfileWithTimestamps

@Dao
interface ProfileDao {

    @Insert(entity = ProfileEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addProfile(entity: ProfileEntity)

    @Query("SELECT * FROM ${ProfileEntity.TABLE_NAME_PROFILE}")
    fun loadProfileList(): List<ProfileEntity>

    @Query("SELECT * FROM ${ProfileEntity.TABLE_NAME_PROFILE}")
    fun loadProfileListFlow(): Flow<List<ProfileWithTimestamps>>

//    @Query("SELECT * FROM ${ProfileEntity.TABLE_NAME_PROFILE}")
//    fun loadProfileListFlow(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM ${ProfileEntity.TABLE_NAME_PROFILE} WHERE profile_id = :id")
    fun getProfileById(id: String): ProfileEntity

    @Query("DELETE FROM ${ProfileEntity.TABLE_NAME_PROFILE} WHERE profile_id = :id")
    fun deleteProfile(id: String)

    @Update(entity = ProfileEntity::class)
    fun updateProfile(profile: ProfileEntity)


//    @Query("UPDATE ${ProfileEntity.TABLE_NAME_PROFILE}" +
//            " SET completed_entries = completed_entries + 1, running_entries = running_entries - 1 WHERE profile_id = :profileId")
//    fun changeCompletedEntries(profileId: String)
//
//    @Query("UPDATE ${ProfileEntity.TABLE_NAME_PROFILE} SET completed_entries = completed_entries - 1 WHERE profile_id = :profileId")
//    fun deleteCompletedEntries(profileId: String)
//
//    @Query("UPDATE ${ProfileEntity.TABLE_NAME_PROFILE} SET running_entries = running_entries + 1 WHERE profile_id = :profileId")
//    fun changeRunningEntries(profileId: String)
//
//    @Query("UPDATE ${ProfileEntity.TABLE_NAME_PROFILE} SET running_entries = running_entries - 1 WHERE profile_id = :profileId")
//    fun deleteRunningEntries(profileId: String)
}