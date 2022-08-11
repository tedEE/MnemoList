package ru.jeinmentalist.mail.mnemolist.screens.createNote

import android.view.View
import android.widget.AdapterView

class OnItemSelectedListener(private val onSuccess: ()->Unit) : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onSuccess()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}