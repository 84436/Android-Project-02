package hcmus.android.sample.notes.helpers

object HTMLToEditTextHelpers {
    // A simple helper that selected* HTML tags to a string with a selection

    // Supported tag list (2010? that's debatable)
    // https://commonsware.com/blog/Android/2010/05/26/html-tags-supported-by-textview.html

    // A rudimentary enum
    const val BOLD        : Short = 0x1
    const val ITALIC      : Short = 0x2
    const val UNDERLINE   : Short = 0x3
    const val STRIKE      : Short = 0x4

    fun format(text: String, selectionStart: Int, selectionEnd: Int, type: Short): String {
        val (startTag, endTag) = when(type) {
            BOLD        -> "<b>" to "</b>"
            ITALIC      -> "<i>" to "</i>"
            UNDERLINE   -> "<u>" to "</u>"
            STRIKE      -> "<strike>" to "</strike>"
            else        -> "" to ""
        }
        return listOf(
            text.substring(0, selectionStart),
            startTag,
            text.substring(selectionStart, selectionEnd),
            endTag,
            text.substring(selectionEnd)
        ).joinToString("")
    }
}
