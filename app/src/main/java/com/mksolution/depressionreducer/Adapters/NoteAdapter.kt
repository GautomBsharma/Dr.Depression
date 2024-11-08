package com.mksolution.depressionreducer.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mksolution.depressionreducer.Model.Note
import com.mksolution.depressionreducer.R
import de.hdodenhof.circleimageview.CircleImageView

class NoteAdapter(
    private val context: Context,
    private var noteList: List<Note>,
    private val onEditClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.tvtitle)
        val noteDescription = view.findViewById<TextView>(R.id.tvnote)
        val category = view.findViewById<TextView>(R.id.tvEvent)
        val date = view.findViewById<TextView>(R.id.tvDate)
        val editBtn = view.findViewById<CircleImageView>(R.id.editbtn)
        val deleteBtn = view.findViewById<CircleImageView>(R.id.btndela)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.title.text = note.title
        holder.noteDescription.text = note.description
        holder.category.text = note.category
        holder.date.text = note.date

        holder.editBtn.setOnClickListener {
            onEditClick(note)
        }

        holder.deleteBtn.setOnClickListener {
            onDeleteClick(note)
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun updateData(newNotes: List<Note>) {
        noteList = newNotes
        notifyDataSetChanged()
    }
}
