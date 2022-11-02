package ru.jeinmentalist.mail.domain.note

import ru.jeinmentalist.mail.domain.type.ITransmitted

data class Note(
    val noteId: Int,
    val location: String,
    val description: String,
    val profId: String,
    val timeOfCreation: String,
    val executableTimestamp: Long, // выполняемая временная метка
    val state: Int,
    val pathImage: String
):ITransmitted {
    companion object{
        const val PERFORMED = 1
        const val DONE = 0
    }
}