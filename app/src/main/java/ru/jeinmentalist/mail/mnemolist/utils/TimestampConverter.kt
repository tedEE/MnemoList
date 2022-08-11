package ru.jeinmentalist.mail.mnemolist.utils

import android.app.Application
import ru.jeinmentalist.mail.mentalist.R

val timeInterval = mapOf(
//        R.string.minute to createPair(60000, (10..60 step 10)),
    R.string.minute to createPair(60000, 1..60),
    R.string.hour to createPair(3600000, 1..24),
    R.string.day to createPair(86400000, 1..7),
    R.string.week to createPair(604800000, 1..4),
    R.string.month to createPair(2592000000, 1..6)
)
//val mapKeyToString = timeInterval.keys.map { convertKeyMap(it) }
//val mapKeyToInt = timeInterval.keys.toList()

fun selectElementFromTimeInterval(key: Int): Pair<Long, IntProgression>{
    return timeInterval[key] ?: createPair(1, 0..1)
}

fun convertKeyMap(key: Int, application: Application): String{
    return application.resources.getString(key)
}

fun createPair(f: Long, s: IntProgression): Pair<Long, IntProgression>{
    return f to s
}