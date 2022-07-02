package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.Note;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class NoteAddPacket implements JedisPacket {

    private UUID uuid;
    private Note note;

    @Override
    public void onReceive() {
        User user = Flash.getInstance().getUserHandler().tryUser(this.uuid, false);
        if (user == null) return;
        if (user.hasNote(note.getId())) return;

        user.getNotes().add(note);
        user.save(true);

    }

}
