package ru.jeinmentalist.mail.mnemolist.contract

interface HasCustomAction {
    fun getCustomAction(): CustomAction
}

class CustomAction(
    val iconRes: Int,
    val textRes: Int,
    val onCustomAction: Runnable
)