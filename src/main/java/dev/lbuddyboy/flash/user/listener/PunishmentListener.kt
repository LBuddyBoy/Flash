package dev.lbuddyboy.flash.user.listener

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.menu.PunishmentHistoryMenu
import dev.lbuddyboy.flash.user.model.Punishment
import dev.lbuddyboy.flash.user.model.PunishmentType
import dev.lbuddyboy.flash.user.packet.PunishmentRemovePacket
import dev.lbuddyboy.flash.user.packet.PunishmentSendPacket
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.Tasks
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class PunishmentListener : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: AsyncPlayerChatEvent) {
        if (punishmentRemovePermMap.containsKey(event.player.name)) {
            val punishment = punishmentRemovePermMap.remove(event.player.name)
            punishment.setRemoved(true)
            punishment.setRemovedAt(System.currentTimeMillis())
            punishment.setRemovedFor(event.message)
            punishment.setRemovedBy(event.player.uniqueId)
            punishment.setRemovedSilent(true)
            val user: User = Flash.instance.userHandler.tryUser(punishment.getTarget(), true)
            PunishmentRemovePacket(punishment.getTarget(), punishment).send()
            user.save(true)
            PunishmentSendPacket(punishment).send()
            Tasks.run { PunishmentHistoryMenu(punishment.getTarget(), punishment.getType()).openMenu(event.player) }
            return
        }
        val user = Flash.instance.userHandler.tryUser(event.player.uniqueId, false)
        if (user == null) {
            event.isCancelled = true
            return
        }
        if (user.hasActivePunishment(PunishmentType.MUTE)) {
            val punishment = user.getActivePunishment(PunishmentType.MUTE)
            event.isCancelled = true
            for (s in FlashLanguage.PUNISHMENT_MUTED_FORMAT.stringList!!) {
                event.player.sendMessage(
                    CC.translate(
                        s,
                        "%REASON%",
                        punishment.sentFor,
                        "%TIME-LEFT%",
                        punishment!!.expireString!!
                    )
                )
            }
        }
    }

    companion object {
        var punishmentRemovePermMap: MutableMap<String, Punishment?> = HashMap()
    }
}