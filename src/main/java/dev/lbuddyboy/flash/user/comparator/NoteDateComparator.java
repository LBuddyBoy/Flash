package dev.lbuddyboy.flash.user.comparator;

import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.Note;

import java.util.Comparator;

public class NoteDateComparator implements Comparator<Note> {

    @Override
    public int compare(Note note, Note otherNote) {
        return Long.compare(note.getSentAt(), otherNote.getSentAt());
    }
}