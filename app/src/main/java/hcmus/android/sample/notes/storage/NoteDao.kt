package hcmus.android.sample.notes.storage

import androidx.room.*

// https://developer.android.com/training/data-storage/room#dao
@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    suspend fun getAll(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getOne(id: Long): NoteEntity

    @Update
    suspend fun setOne(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE title LIKE :query OR tag LIKE :query OR content LIKE :query")
    suspend fun search(query: String): List<NoteEntity>

    @Insert
    suspend fun insert(note: NoteEntity): Long

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("DELETE FROM notes WHERE title = '' AND tag = '' AND content = ''")
    suspend fun deleteEmptyNotes()
}
