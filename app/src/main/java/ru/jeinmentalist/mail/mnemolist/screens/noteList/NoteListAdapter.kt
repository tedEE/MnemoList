package ru.jeinmentalist.mail.mnemolist.screens.noteList

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.mentalist.R

class NoteListAdapter : ListAdapter<Note, NoteListAdapter.NoteViewHolder>(NoteItemDiffCallback()) {

    var deleteButtonClickListener: ((Note)->Unit)? = null
    var onItemNoteListClickListener: ((Note)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note_list, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)
        holder.iconDelete.setOnClickListener{
            deleteButtonClickListener?.invoke(item)
        }
        holder.view.setOnClickListener{
            onItemNoteListClickListener?.invoke(item)
        }
        holder.binding(item)
    }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        private val location = view.findViewById<TextView>(R.id.location)
        val iconDelete = view.findViewById<ImageView>(R.id.icon_delete_note)

        fun binding(item: Note){
            location.text = item.location
            // измение цвета иконки удаление
            val porterDuffColorFilter = PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)
            if (item.nextRunningTimestamp> 0L){
                iconDelete.colorFilter = porterDuffColorFilter
            }
        }
    }
}