package hcmus.android.sample.notes.helpers

import java.text.SimpleDateFormat
import java.util.*

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("E dd/MM hh:mmaa", Locale.ENGLISH)
    return sdf.format(timestamp)
}
