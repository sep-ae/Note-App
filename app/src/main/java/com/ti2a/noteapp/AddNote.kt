package com.ti2a.noteapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ti2a.noteapp.Models.Note
import com.ti2a.noteapp.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.Date

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    private lateinit var note: Note
    private lateinit var oldNote: Note
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldNote = intent.getSerializableExtra("current_note") as Note
            binding.editTitle.setText(oldNote.title)
            binding.editNote.setText(oldNote.note)
            isUpdate = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val noteText = binding.editNote.text.toString()
            if (title.isNotEmpty() || noteText.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm")
                val currentDate = formatter.format(Date())

                note = if (isUpdate) {
                    Note(
                        oldNote.id, title, noteText, currentDate
                    )
                } else {
                    Note(
                        title = title, note = noteText, date = currentDate
                    )
                }

                val intent = Intent().apply {
                    putExtra("note", note)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Masukkan Data", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
        }
    }
}
