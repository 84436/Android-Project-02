package hcmus.android.sample.notes.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import hcmus.android.sample.notes.databinding.FragmentNoteEntityBinding
import hcmus.android.sample.notes.helpers.formatTimestamp
import hcmus.android.sample.notes.storage.NoteEntity

class NoteListRecyclerViewAdapter(private val clickEventHandler: ClickEventHandler) : RecyclerView.Adapter<NoteListRecyclerViewAdapter.ViewHolder>() {
    // An internal copy of notes in the RecyclerView
    private var values = listOf<NoteEntity>()
    fun submitList(note: List<NoteEntity>) { values = note }

    class ViewHolder(val binding: FragmentNoteEntityBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = FragmentNoteEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Data
        val item = values[position]
        holder.binding.apply {
            noteId = item.id
            noteTitle.text = item.title
            noteDateModified.text = formatTimestamp(item.dateModified)
            noteTag.text = item.tag
            noteContent.text = item.content
        }

        // Events
        holder.itemView.setOnClickListener { clickEventHandler.onClick(item) }
        holder.itemView.setOnLongClickListener { clickEventHandler.onLongClick(item); true }
    }

    override fun getItemCount(): Int = values.size
}

interface ClickEventHandler {
    fun onLongClick(note: NoteEntity)
    fun onClick(note: NoteEntity)
}
