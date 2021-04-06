package hcmus.android.sample.notes.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// https://developer.android.com/training/data-storage/room#database
@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        @Volatile
        var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context, NoteDatabase::class.java, "notes").build()
                }
            }
            return INSTANCE as NoteDatabase
        }
    }
}
