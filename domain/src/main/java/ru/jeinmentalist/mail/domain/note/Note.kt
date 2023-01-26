package ru.jeinmentalist.mail.domain.note

import ru.jeinmentalist.mail.domain.timestamp.Timestamp
import ru.jeinmentalist.mail.domain.type.ITransmitted

data class Note(
    val noteId: Int,
    val location: String,
    val description: String,
    val profId: String,
    val timeOfCreation: String,
    var currentRunningTimestamp: Long,
    var nextRunningTimestamp: Long,
    var state: Int,
    val pathImage: String,
    val timestampList: List<Timestamp> = listOf()
):ITransmitted {

    fun getSortedTimestampList(): List<Long>{
        val list = timestampList.map { ts: Timestamp -> ts.executionTime }
        return list.sorted()
    }

    fun changeNextExecutableTimestamp(){
        for (i in getSortedTimestampList()){
            if (i > nextRunningTimestamp){
                nextRunningTimestamp = i
                break
            }
        }
    }

    fun checkDoneNote(): Boolean{
        return state == DONE
    }

    fun checkRunningNote(): Boolean{
        return state == RUNNING
    }

    fun changeСurrentExecutableTimestamp(){
    }

    fun changeState(state: State){
        when(state){
            State.RUNNING -> this.state = 1
            State.CANSELED -> this.state = 2
            State.DONE -> this.state = 0
        }
    }

    enum class State{
        RUNNING, CANSELED, DONE
    }

    companion object{
        const val RUNNING = 1
        const val CANSELED = 2
        const val DONE = 0
    }
}