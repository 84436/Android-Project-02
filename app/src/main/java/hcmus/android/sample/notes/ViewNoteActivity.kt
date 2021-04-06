package hcmus.android.sample.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.doOnTextChanged
import hcmus.android.sample.notes.databinding.ActivityViewNoteBinding
import hcmus.android.sample.notes.storage.NoteDao
import hcmus.android.sample.notes.storage.NoteDatabase
import hcmus.android.sample.notes.storage.NoteEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewNoteActivity : AppCompatActivity() {
    private lateinit var dao: NoteDao
    private lateinit var binding: ActivityViewNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Layout
        supportActionBar?.hide()
        binding = ActivityViewNoteBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        // Note
        dao = NoteDatabase.getInstance(this).noteDao
        getNote()

        // Workaround: save on every key press ._.
        binding.viewNoteTitle.doOnTextChanged { _, _, _, _ -> updateNote() }
        binding.viewNoteTag.doOnTextChanged { _, _, _, _ -> updateNote() }
        binding.viewNoteContent.doOnTextChanged { _, _, _, _ -> updateNote() }
    }

    override fun onPause() { updateNote(); super.onPause(); }

    private fun getNote() {
        GlobalScope.launch {
            val existingId = intent.getLongExtra("note_id", -1)
            if (existingId == -1L) {
                val newId = dao.insert(NoteEntity())
                binding.thisNote = dao.getOne(newId)
            }
            else {
                binding.thisNote = dao.getOne(existingId)
            }
        }
    }

    private fun updateNote() {
        binding.thisNote?.apply {
            title = binding.viewNoteTitle.text.toString()
            tag = binding.viewNoteTag.text.toString()
            content = binding.viewNoteContent.text.toString()
            dateModified = System.currentTimeMillis()
        }
        GlobalScope.launch {
            binding.thisNote?.let { dao.setOne(it) }
        }
    }

    fun btnCloseNoteHandler(view: View) {
        updateNote()
        finish()
    }

    fun btnInsertTabHandler(view: View) {
        val start = binding.viewNoteContent.selectionStart
        val insertedText = "\t"
        val finalText = "${binding.thisNote?.content?.substring(0, start)}${insertedText}${binding.thisNote?.content?.substring(start)}"
        binding.viewNoteContent.setText(finalText)
        binding.viewNoteContent.setSelection(start + insertedText.length)
        updateNote()
    }

    /* fun btnFormatHandler(view: View) {
        val content = binding.viewNoteContent
        val selectionStart = content.selectionStart
        val selectionEnd = content.selectionEnd
        if (selectionStart == selectionEnd || selectionStart == -1 || selectionEnd == -1)  return

        val format_type = when (view.id) {
            R.id.btn_format_bold -> HTMLToEditTextHelpers.BOLD
            R.id.btn_format_italic -> HTMLToEditTextHelpers.ITALIC
            R.id.btn_format_underline -> HTMLToEditTextHelpers.UNDERLINE
            R.id.btn_format_strike -> HTMLToEditTextHelpers.STRIKE
            else -> 0
        }
        val formatted_text = HTMLToEditTextHelpers.format(content.text.toString(), selectionStart, selectionEnd, format_type)
    } */
}
