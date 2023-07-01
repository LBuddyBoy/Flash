package dev.lbuddyboy.flash.handler

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.util.TimeUtils
import dev.lbuddyboy.flash.util.bukkit.CC
import lombok.Data
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

@Data
class ChatHandler : Listener {
    private val chatMuted: Boolean
    private val slowChat: Int

    init {
        chatMuted = FlashLanguage.CHAT_MUTED.boolean
        slowChat = FlashLanguage.CHAT_SLOW.int
        Flash.instance.server.pluginManager.registerEvents(this, Flash.instance)
    }

    fun bypassesSlowChat(player: Player): Boolean {
        return player.hasPermission("slowchat.bypass")
    }

    fun bypassesMuteChat(player: Player): Boolean {
        return player.hasPermission("mutechat.bypass")
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val user = Flash.instance.userHandler.tryUser(player.uniqueId, false)
        if (isChatMuted() && !bypassesMuteChat(player)) {
            event.isCancelled = true
            return
        }
        val lastSent = user.getPlayerInfo().lastMessageSent
        if (getSlowChat() > 0 && !bypassesSlowChat(player) && lastSent + getSlowChat() * 1000L - System.currentTimeMillis() > 0 && lastSent != -1L) {
            player.sendMessage(
                CC.translate(
                    "&cChat is currently slowed. You may talk in " + TimeUtils.formatLongIntoMMSS(
                        (lastSent + getSlowChat() * 1000L - System.currentTimeMillis()) / 1000
                    )
                )
            )
            event.isCancelled = true
            return
        }
        user.getPlayerInfo().lastMessageSent = System.currentTimeMillis()
    }
}