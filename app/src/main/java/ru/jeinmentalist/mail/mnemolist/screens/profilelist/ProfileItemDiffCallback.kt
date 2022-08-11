package ru.jeinmentalist.mail.mnemolist.screens.profilelist

import androidx.recyclerview.widget.DiffUtil
import ru.jeinmentalist.mail.domain.profile.Profile

class ProfileItemDiffCallback : DiffUtil.ItemCallback<Profile>() {
    override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem.profileId == newItem.profileId
    }

    override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem == newItem
    }
}