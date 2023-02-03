package ru.jeinmentalist.mail.mnemolist.screens.profilelist

import android.animation.LayoutTransition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.mentalist.R
import java.lang.RuntimeException

class ProfileListAdapter : ListAdapter<Profile, ProfileListAdapter.ProfileViewHolder>(ProfileItemDiffCallback()) {
    // так как обработчика два можно заменить лямбды на интерфейс
    var onProfileItemClickListener: ((Profile)->Unit)? = null
    var onProfileItemLongClickListener: ((Profile, View)->Unit)? = null
    var onProfileButtonDeleteClickListener: ((Profile)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val layout = when(viewType){
            TypeProfile.LIST.idType -> R.layout.profile_list_item_list
            TypeProfile.CART.idType -> R.layout.profile_list_item_cart
            else -> throw RuntimeException("тип $viewType не найден")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.view.setOnClickListener{
            onProfileItemClickListener?.invoke(item)
        }
        holder.view.setOnLongClickListener {
            onProfileItemLongClickListener?.invoke(item, it)
            true
        }

        holder.buttonDelete.setOnClickListener{
            onProfileButtonDeleteClickListener?.invoke(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return  when(item.profileType){
            TypeProfile.LIST.idType -> TypeProfile.LIST.idType
            TypeProfile.CART.idType -> TypeProfile.CART.idType
            else -> TypeProfile.HAS_NO_TYPE.idType
        }
    }

    class ProfileViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val profileName = view.findViewById<TextView>(R.id.nameProfile)
        private val runningEntries = view.findViewById<TextView>(R.id.running_entries)
        private val completedEntries = view.findViewById<TextView>(R.id.completed_entries)
        private val canceledEntries = view.findViewById<TextView>(R.id.canceled_entries)
//        private val desc = view.findViewById<ConstraintLayout>(R.id.profile_description)
        private val profile_item = view.findViewById<ConstraintLayout>(R.id.profile_item)
        val buttonDelete = view.findViewById<Button>(R.id.button_delete)

        fun bind(item: Profile){
            profileName.text = item.profileName
            runningEntries.text = item.runningEntries.toString()
            completedEntries.text = item.completedEntries.toString()
            canceledEntries.text = item.canceledEntries.toString()
            // необходимо для анимации открытия cardview
//            profile_item.layoutTransition.enableTransitionType(LayoutTransition.APPEARING)
        }
    }

    enum class TypeProfile(val idType: Int){
        LIST(0),
        CART(1),
        HAS_NO_TYPE(99)
    }

    companion object{
        const val MAX_POOL_SIZE = 5
    }
}