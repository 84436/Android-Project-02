package hcmus.android.sample.notes.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// https://developer.android.com/training/data-storage/room#data-entity
@Entity(tableName = "notes")
data class NoteEntity (
    @PrimaryKey(autoGenerate = true)    var id: Long = 0,
    @ColumnInfo(name = "date_modified") var dateModified: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "title")         var title: String = "",
    @ColumnInfo(name = "tag")           var tag: String = "",
    @ColumnInfo(name = "content")       var content: String = ""
)
