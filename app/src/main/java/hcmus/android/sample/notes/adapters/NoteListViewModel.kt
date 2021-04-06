package hcmus.android.sample.notes.adapters

import androidx.lifecycle.*
import hcmus.android.sample.notes.storage.NoteDao
import hcmus.android.sample.notes.storage.NoteEntity
import kotlinx.coroutines.launch
import java.lang.Exception

class NoteListViewModel(private val dao: NoteDao) : ViewModel() {
    private var notesPrivate = MutableLiveData<List<NoteEntity>>()
    val notes: LiveData<List<NoteEntity>> get() = notesPrivate

    val isListEmpty = Transformations.map(notesPrivate) { it.isEmpty() }

    fun loadNotes() = viewModelScope.launch {
        dao.deleteEmptyNotes()
        notesPrivate.value = dao.getAll()
    }

    fun removeNote(noteEntity: NoteEntity) {
        viewModelScope.launch {
            dao.delete(noteEntity)
            loadNotes()
        }
    }

    fun filterNote(query: String) {
        when {
            // Remember the %'s
            query.isNotEmpty() -> viewModelScope.launch { notesPrivate.value = dao.search("%$query%") }
            else -> loadNotes()
        }
    }

    init { loadNotes() }
}

@Suppress("UNCHECKED_CAST")
class NoteListViewModelFactory(private val dao: NoteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteListViewModel::class.java)) {
            return NoteListViewModel(dao) as T
        }
        throw Exception("Cannot create NoteListViewModel")
    }
}
