package ru.jeinmentalist.mail.mnemolist.screens.profilelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.jeinmentalist.mail.mentalist.R

class ProfileItemAdapter : RecyclerView.Adapter<ProfileItemAdapter.ProfileItemViewHolder>() {

    private var data = listOf<Pair<String, Long>>()

    fun setData(list: List<Pair<String, Long>>){
        data = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileItemViewHolder {
        return ProfileItemViewHolder(view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile_cart, parent, false))
    }

    override fun onBindViewHolder(holder: ProfileItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ProfileItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val itemText = view.findViewById<TextView>(R.id.item_timestamp)
        fun bind(model: Pair<String, Long>){
            itemText.text = "${model.first} ${model.second}"
        }
    }
}