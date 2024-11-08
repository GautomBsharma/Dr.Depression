package com.mksolution.depressionreducer

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.mksolution.depressionreducer.Adapters.NoteAdapter
import com.mksolution.depressionreducer.Model.Note
import com.mksolution.depressionreducer.databinding.ActivityEmossionBinding
import java.util.*

class EmossionActivity : AppCompatActivity() {
private lateinit var binding: ActivityEmossionBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var noteAdapter: NoteAdapter
    private var noteList = mutableListOf<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmossionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DatabaseHelper(this)
        noteList = dbHelper.getAllNotes().toMutableList()
        if (noteList.isEmpty()){
            binding.textView3.visibility = View.VISIBLE
        }
        noteAdapter = NoteAdapter(this, noteList,
            onEditClick = { note -> editNote(note) },
            onDeleteClick = { note -> deleteNote(note) }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.recyclerView.adapter = noteAdapter

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this,AddActivity::class.java))
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun editNote(note: Note) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_note, null)
        val editTitle = dialogView.findViewById<EditText>(R.id.editTitle)
        val editNote = dialogView.findViewById<EditText>(R.id.editNote)
        val editCategory = dialogView.findViewById<EditText>(R.id.editCategory)

        editTitle.setText(note.title)
        editNote.setText(note.description)
        editCategory.setText(note.category)

        AlertDialog.Builder(this)
            .setTitle("Edit Note")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedTitle = editTitle.text.toString()
                val updatedDescription = editNote.text.toString()
                val updatedCategory = editCategory.text.toString()

                val updatedNote = note.copy(
                    title = updatedTitle,
                    description = updatedDescription,
                    category = updatedCategory,
                    date = getCurrentDate() // Update with current date
                )

                dbHelper.updateNote(updatedNote)
                refreshNotesList()
            }
            .setNegativeButton("Cancel", null)
            .create().show()
    }

    private fun deleteNote(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { _, _ ->
                dbHelper.deleteNote(note.id)
                refreshNotesList()
            }
            .setNegativeButton("No", null)
            .create().show()
    }

    private fun refreshNotesList() {
        noteList = dbHelper.getAllNotes().toMutableList()
        noteAdapter.updateData(noteList)
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}