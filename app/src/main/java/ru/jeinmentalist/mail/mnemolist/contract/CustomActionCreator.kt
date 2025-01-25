package ru.jeinmentalist.mail.mnemolist.contract

interface CustomActionCreator {

    fun createCustomToolbarAction(action: CustomAction)

    fun clearToolbarAction()
}