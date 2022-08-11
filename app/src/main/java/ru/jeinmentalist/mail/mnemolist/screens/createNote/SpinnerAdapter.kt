package ru.jeinmentalist.mail.mnemolist.screens.createNote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.mentalist.R

class SpinnerAdapter(private val profiles: List<Profile>) : BaseAdapter() {
    override fun getCount(): Int {
        return profiles.size
    }

    override fun getItem(position: Int): Profile{
        return profiles[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.spinner_profile_layout,parent,false)
        val textView = view.findViewById<TextView>(R.id.text)
        textView.text = profiles[position].profileName
        return view
    }
}