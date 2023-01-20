package ru.jeinmentalist.mail.domain.note

import ru.jeinmentalist.mail.domain.timestamp.Timestamp
import ru.jeinmentalist.mail.domain.type.ITransmitted

data class Note(
    val noteId: Int,
    val location: String,
    val description: String,
    val profId: String,
    val timeOfCreation: String,
    val executableTimestamp: Long, // выполня           емая временная метка
    val state: Int,
    val pathImage: String,
    val timestampList: List<Timestamp> = listOf()
):ITransmitted {

    private fun getSortedTimestampList(): List<Long>{
        val list = timestampList.map { ts: Timestamp -> ts.executionTime }
        return list.sorted()
    }

    fun getNextExecutableTimestamp(): Long{
        var nextExecutableTimestamp: Long = 0
        for (i in getSortedTimestampList()) {
            if (i > executableTimestamp) {
                nextExecutableTimestamp = i
                break
            }
        }
        return nextExecutableTimestamp
    }

    companion object{
        const val PERFORMED = 1
        const val DONE = 0
    }
}