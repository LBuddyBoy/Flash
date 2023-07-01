package dev.lbuddyboy.flash.user.comparator

import dev.lbuddyboy.flash.user.model.Note

class NoteDateComparator : Comparator<Note> {
    override fun compare(note: Note, otherNote: Note): Int {
        return java.lang.Long.compare(note.sentAt, otherNote.sentAt)
    }
}