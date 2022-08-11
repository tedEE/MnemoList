package ru.jeinmentalist.mail.mnemolist.screens.noteList

import androidx.recyclerview.widget.DiffUtil
import ru.jeinmentalist.mail.domain.note.Note

class NoteItemDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.noteId == newItem.noteId
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}