package hcmus.android.sample.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import hcmus.android.sample.notes.storage.NoteDao
import hcmus.android.sample.notes.storage.NoteDatabase
import hcmus.android.sample.notes.storage.NoteEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var dao: NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get database
        // This must be done before inflating layout due to note list dependence on database
        dao = NoteDatabase.getInstance(this).noteDao
        checkInsertWelcomeNote()

        // Layout
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
    }

    private fun checkInsertWelcomeNote() {
        val prefs = getPreferences(MODE_PRIVATE)
        if (prefs.getBoolean("is_first_run", true)) {
            // Insert the note...
            GlobalScope.launch {
                val sampleNotes: List<NoteEntity> = listOf(
                        NoteEntity(
                            title = resources.getString(R.string.sample_note_01_title),
                            tag = resources.getString(R.string.sample_note_01_tag),
                            content = resources.getString(R.string.sample_note_01_content)
                        ),
                        NoteEntity(
                            title = resources.getString(R.string.sample_note_02_title),
                            tag = resources.getString(R.string.sample_note_02_tag),
                            content = resources.getString(R.string.sample_note_02_content)
                        ),
                        NoteEntity(
                            title = resources.getString(R.string.sample_note_03_title),
                            tag = resources.getString(R.string.sample_note_03_tag),
                            content = resources.getString(R.string.sample_note_03_content)
                        )
                )
                for (each in sampleNotes) {
                    dao.insert(each)
                }
            }

            // ...then mark isFirstRun false
            prefs.edit(commit = true) { putBoolean("is_first_run", false) }
        }
    }

    fun btnCreateNoteHandler(view: View) {
        val intent = Intent(this, ViewNoteActivity::class.java)
        startActivity(intent)
    }
}
