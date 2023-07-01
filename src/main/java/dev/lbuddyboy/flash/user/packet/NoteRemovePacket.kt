package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.user.model.Note
import lombok.AllArgsConstructor
import java.util.*

@AllArgsConstructor
class NoteRemovePacket : JedisPacket {
    private val uuid: UUID? = null
    private val note: Note? = null
    override fun onReceive() {
        val user = Flash.instance.userHandler.tryUser(uuid, false) ?: return
        for (userNote in user.getNotes()) {
            if (userNote.id.toString() != note.getId().toString()) continue
            userNote.isRemoved = true
            userNote.removedBy = note.getRemovedBy()
            userNote.removedFor = note.getRemovedFor()
            userNote.removedAt = note.getRemovedAt()
        }
        user.save(true)
    }
}