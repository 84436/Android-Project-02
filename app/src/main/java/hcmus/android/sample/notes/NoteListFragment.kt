package hcmus.android.sample.notes

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import hcmus.android.sample.notes.adapters.NoteListRecyclerViewAdapter
import hcmus.android.sample.notes.adapters.NoteListViewModel
import hcmus.android.sample.notes.adapters.NoteListViewModelFactory
import hcmus.android.sample.notes.adapters.ClickEventHandler
import hcmus.android.sample.notes.databinding.FragmentNoteListBinding
import hcmus.android.sample.notes.storage.NoteDao
import hcmus.android.sample.notes.storage.NoteDatabase
import hcmus.android.sample.notes.storage.NoteEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NoteListFragment : Fragment() {
    private lateinit var dao: NoteDao
    private lateinit var binding: FragmentNoteListBinding

    private val noteListViewModel by lazy {
        NoteListViewModelFactory(NoteDatabase.getInstance(requireContext()).noteDao).create(NoteListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dao = NoteDatabase.getInstance(requireContext()).noteDao
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentNoteListBinding.inflate(inflater, container, false)
        binding = view

        binding.lifecycleOwner = this
        binding.noteListViewModel = noteListViewModel

        val listAdapter = NoteListRecyclerViewAdapter(clickEventHandler)

        GlobalScope.launch {
            view.list.apply {
                adapter = listAdapter
            }
        }

        noteListViewModel.notes.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
            listAdapter.notifyDataSetChanged()
        }

        binding.mainSearchBox.doOnTextChanged { _, _, _, _ -> run {
            val query = binding.mainSearchBox.text.toString()
            noteListViewModel.filterNote(query)
        } }

        return view.root
    }

    // Event handler: Click/Long-click on notes
    private val clickEventHandler = object: ClickEventHandler {
        // Click to show notes
        override fun onClick(note: NoteEntity) {
            val intent = Intent(context, ViewNoteActivity::class.java)
            intent.putExtra("note_id", note.id)
            startActivity(intent)
        }

        // Long-click to delete
        override fun onLongClick(note: NoteEntity) {
            AlertDialog.Builder(requireContext())
                .setTitle("Delete this note?")
                .setNegativeButton("No") { _, _ -> }
                .setPositiveButton("Yes") { _, _ -> run {
                    noteListViewModel.removeNote(note)
                    Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show()
                } }
                .show()
        }
    }

    // Fragment lifecycle: Refresh note list once brought back to foreground
    override fun onResume() {
        super.onResume()
        noteListViewModel.loadNotes()
    }
}
