package com.mksolution.depressionreducer

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mksolution.depressionreducer.Model.Note
import com.mksolution.depressionreducer.databinding.ActivityAddBinding
import java.util.*

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseHelper(this)

        // Ensure only one checkbox is checked at a time
        setupSingleCheckBoxSelection()

        // Adding the click listener for adding a note
        binding.btnAddNote.setOnClickListener {
            validateData()
        }
    }

    // Setup only one checkbox to be checked at a time
    private fun setupSingleCheckBoxSelection() {
        val checkBoxes = listOf(binding.checkBoxEmotions, binding.checkBoxAccom, binding.checkBoxNegative)

        for (checkBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    // Uncheck all other checkboxes when one is selected
                    for (otherCheckBox in checkBoxes) {
                        if (otherCheckBox != buttonView) {
                            otherCheckBox.isChecked = false
                        }
                    }
                }
            }
        }
    }

    // Validate input data
    private fun validateData() {
        if (binding.title.text.toString().isEmpty()) {
            binding.title.error = "Enter title"
        } else if (binding.notes.text.toString().isEmpty()) {
            binding.notes.error = "Enter note"
        } else if (!binding.checkBoxEmotions.isChecked &&
            !binding.checkBoxAccom.isChecked &&
            !binding.checkBoxNegative.isChecked) {
            Toast.makeText(this, "Select a category", Toast.LENGTH_SHORT).show()
        } else {
            insertData()
        }
    }

    // Insert data into the database
    private fun insertData() {
        val title = binding.title.text.toString()
        val notedes = binding.notes.text.toString()

        // Determine the selected category
        val category = when {
            binding.checkBoxEmotions.isChecked -> "Emotions"
            binding.checkBoxAccom.isChecked -> "Accomplishments"
            binding.checkBoxNegative.isChecked -> "Negative People List"
            else -> ""
        }

        // Get the current date
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())

        // Create the Note object with title, description, category, and date
        val note = Note(0, title, notedes, category, currentDate)

        // Insert the note into the database
        db.insertN(note)

        // Finish the activity and show a success message
        finish()
        Toast.makeText(this, "Note Added", Toast.LENGTH_SHORT).show()
    }
}