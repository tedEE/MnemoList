package ru.jeinmentalist.mail.domain

fun translationFromMilliseconds(milliseconds: Long): Pair<String, Long> {

    var str = ""
    var ml = milliseconds
    var pair: Pair<String, Long> = str to ml

    val month = 2419200000 // 6
    val week = 604800000   // 4
    val day = 86400000     // 6
    val hour = 3600000     // 23
    val min = 60000        //59

    if ((milliseconds / month) > 0) {
        return "month" to milliseconds / month
    }
    if ((milliseconds / week) > 0) {
        return "week" to milliseconds / week
    }
    if ((milliseconds / day) > 0) {
        return "day" to milliseconds / day
    }
    if ((milliseconds / hour) > 0) {
        return "hour" to milliseconds / hour
    }
    if ((milliseconds / min) > 0) {
        return "min" to milliseconds / min
    }
    return pair
}
