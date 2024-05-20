package com.ti2a.noteapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ti2a.noteapp.Adapter.NotesAdapter
import com.ti2a.noteapp.Models.Note
import com.ti2a.noteapp.Models.NoteViewModel
import com.ti2a.noteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NotesAdapter.NotesClicklistener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NotesAdapter
    private var selectedNote: Note? = null

    private val UpdateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val note = result.data?.getSerializableExtra("note") as? Note
            note?.let {
                viewModel.update(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi UI
        initUi()

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)

        // Observasi perubahan data
        viewModel.allNotes.observe(this) { list ->
            list?.let {
                adapter.UpdateList(it)
            }
        }
    }

    private fun initUi() {
        binding.recyleView.setHasFixedSize(true)
        binding.recyleView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NotesAdapter(this, this)
        binding.recyleView.adapter = adapter

        // Menangani hasil aktivitas
        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val note = result.data?.getSerializableExtra("note") as? Note
                note?.let {
                    viewModel.insert(it)
                }
            }
        }

        // Menangani klik tombol tambah catatan
        binding.fbAddNote.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }

        // Menangani pencarian
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchNotes(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchNotes(it)
                }
                return false
            }
        })
    }

    // Fungsi untuk mencari catatan berdasarkan query
    private fun searchNotes(query: String) {
        val filteredList = viewModel.allNotes.value?.filter {
            it.title.contains(query, true) || it.note.contains(query, true)
        }
        filteredList?.let {
            adapter.UpdateList(it)
        }
    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)
        UpdateNote.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.inflate(R.menu.pop_up_menu)
        popup.setOnMenuItemClickListener(this)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.delete_note -> {
                selectedNote?.let { viewModel.delete(it) }
                true
            }
            else -> false
        }
    }
}


