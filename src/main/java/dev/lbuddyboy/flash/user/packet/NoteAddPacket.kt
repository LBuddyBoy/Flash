package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.user.model.Note
import lombok.AllArgsConstructor
import java.util.*

@AllArgsConstructor
class NoteAddPacket : JedisPacket {
    private val uuid: UUID? = null
    private val note: Note? = null
    override fun onReceive() {
        val user = Flash.instance.userHandler.tryUser(uuid, false) ?: return
        if (user.hasNote(note.getId())) return
        user.getNotes().add(note)
        user.save(true)
    }
}