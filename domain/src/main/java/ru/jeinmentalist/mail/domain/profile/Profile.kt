package ru.jeinmentalist.mail.domain.profile

import ru.jeinmentalist.mail.domain.timestamp.Timestamp
import ru.jeinmentalist.mail.domain.type.ITransmitted

data class Profile(
    val profileId: String,
    val profileName: String,
    val profileType: Int,
    val completedEntries: Int,
    val runningEntries: Int,
    val canceledEntries: Int,
    val timestampList: List<Timestamp> = listOf()
): ITransmitted{

//    fun addRunningEntries(){
//        runningEntries ++
//    }
//
//    fun addCanceledEntries(){
//        runningEntries --
//        canceledEntries ++
//    }
//
//    fun addCompletedEntries(){
//        runningEntries --
//        completedEntries ++
//    }
}