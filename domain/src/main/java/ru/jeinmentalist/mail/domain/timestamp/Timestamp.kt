package ru.jeinmentalist.mail.domain.timestamp

data class Timestamp(
    val profileId: String,
    val executionStatus: Int, // статус исполнения
    val executionTime: Long,// время исполнения
){
    fun translationFromMilliseconds(): Pair<String, Long> {

        var str = ""
        var ml = executionTime
        var pair: Pair<String, Long> = str to ml

        val month = 2419200000 // 6
        val week = 604800000   // 4
        val day = 86400000     // 6
        val hour = 3600000     // 23
        val min = 60000        //59

        if ((executionTime / month) > 0) {
            return "month" to executionTime / month
        }
        if ((executionTime / week) > 0) {
            return "week" to executionTime / week
        }
        if ((executionTime / day) > 0) {
            return "day" to executionTime / day
        }
        if ((executionTime / hour) > 0) {
            return "hour" to executionTime / hour
        }
        if ((executionTime / min) > 0) {
            return "min" to executionTime / min
        }
        return pair
    }
}