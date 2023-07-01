package dev.lbuddyboy.flash.user.listener

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.model.PunishmentType
import dev.lbuddyboy.flash.user.packet.ServerChangePacket
import dev.lbuddyboy.flash.user.packet.StaffMessagePacket
import dev.lbuddyboy.flash.util.HashUtil
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.UserUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.*
import java.util.*

class UserListener : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onAsyncPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
        if (FlashLanguage.USER_BLACKLIST.stringList!!.contains(event.name)) {
            event.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_BANNED
            val message = CC.translate("&cYou're banned stop trying.")
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message)
            return
        }
        val user: User = Flash.instance.userHandler.createUser(event.uniqueId, event.name)
        user.setIp(HashUtil.Companion.encryptUsingKey(event.address.hostAddress))
        if (!user.getKnownIps().contains(user.getIp())) {
            user.getKnownIps().add(user.getIp())
        }
        if (user.hasActivePunishment(PunishmentType.BAN)) {
            val punishment = user.getActivePunishment(PunishmentType.BAN)
            val message = CC.translate(
                punishment.type.kickMessage,
                "%REASON%",
                punishment.sentFor,
                "%REASON%",
                punishment.sentFor,
                "%TEMP-FORMAT%",
                FlashLanguage.PUNISHMENT_TEMPORARY_FORMAT.string.replace(
                    "%TIME%".toRegex(),
                    punishment!!.expireString!!
                )
            )
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message)
            return
        }
        if (user.hasActivePunishment(PunishmentType.IP_BAN)) {
            val punishment = user.getActivePunishment(PunishmentType.IP_BAN)
            val message = CC.translate(
                punishment.type.kickMessage,
                "%REASON%",
                punishment.sentFor,
                "%REASON%",
                punishment.sentFor,
                "%TEMP-FORMAT%",
                FlashLanguage.PUNISHMENT_TEMPORARY_FORMAT.string.replace(
                    "%TIME%".toRegex(),
                    punishment!!.expireString!!
                )
            )
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message)
            return
        }
        if (user.hasActivePunishment(PunishmentType.BLACKLIST)) {
            val punishment = user.getActivePunishment(PunishmentType.BLACKLIST)
            val message = CC.translate(
                punishment.type.kickMessage,
                "%REASON%",
                punishment.sentFor,
                "%REASON%",
                punishment.sentFor,
                "%TEMP-FORMAT%",
                FlashLanguage.PUNISHMENT_TEMPORARY_FORMAT.string.replace(
                    "%TIME%".toRegex(),
                    punishment!!.expireString!!
                )
            )
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message)
            return
        }
        val alts = Flash.instance.userHandler.relativeAlts(user.getIp())
        for (alt in alts) {
            val altUser: User = Flash.instance.userHandler.tryUser(alt, true)
            if (altUser.hasActivePunishment(PunishmentType.BLACKLIST)) {
                event.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_BANNED, CC.translate(
                        FlashLanguage.PUNISHMENT_BANNED_IP_RELATIVE.string, "%OWNER%", UserUtils.formattedName(
                            alts[0]
                        )!!
                    )
                )
                return
            }
            if (altUser.hasActivePunishment(PunishmentType.BAN)) {
                StaffMessagePacket(user.coloredName + " &fmight be ban evading... &7(" + altUser.coloredName + ")").send()
                break
            }
        }
        Flash.instance.userHandler.getUsers().put(event.uniqueId, user)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val user = Flash.instance.userHandler.tryUser(event.player.uniqueId, false)
        if (user == null) {
            player.sendMessage(CC.translate("&cFailed to load your profile. Please re-log."))
            player.kickPlayer(CC.translate("&cFailed to load your profile. Please re-log."))
            return
        }


        /*            if (user.getPlayerInfo().isOfflineInventoryEdited()) {
                player.getInventory().setArmorContents(user.getServerInfo().getArmor());
                player.getInventory().setContents(user.getServerInfo().getContents());
                player.updateInventory();
                user.getPlayerInfo().setOfflineInventoryEdited(false);
            }*/user.setName(Flash.instance.cacheHandler.getUserCache().getName(user.getUuid()))
        user.updatePerms()
        user.updateGrants()
        user.buildPlayer()
        if (player.hasPermission("flash.staff")) {
            ServerChangePacket(true, player.displayName, FlashLanguage.SERVER_NAME.string).send()
        }
        val notifications = Flash.instance.serverHandler.getUnReadNotifications(user)
        if (notifications.size > 0) {
            player.sendMessage(CC.translate("&aYou currently have " + notifications.size + " unread notifications. &7(( /notifications for more info ))"))
        }
        //
//        if (user.getActiveRank().isStaff() || player.isOp() || player.hasPermission("*") || player.hasPermission("flash.staff")) {
//            if (user.isDiscordSynced()) return;
//            if (user.getDiscordRank() != null) return;
//
//            FreezeCommand.freeze(Bukkit.getConsoleSender(), player);
//            player.sendMessage(CC.translate("&eSync your discord by doing /sync to verify your staff identity."));
//        }
    }

    @EventHandler
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val args = event.message.lowercase(Locale.getDefault()).split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        for (s in FlashLanguage.BLOCKED_COMMANDS.stringList!!) {
            if (args[0].equals(s, ignoreCase = true)) event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        val user: User = Flash.instance.userHandler.getUsers().remove(player.uniqueId)
            ?: return

//        user.addServerInfo();
//        user.getServerInfo().setArmor(player.getInventory().getArmorContents());
//        user.getServerInfo().setContents(player.getInventory().getContents());
        user.updatePerms()
        user.updateGrants()
        user.save(true)
        if (player.hasPermission("flash.staff")) {
            ServerChangePacket(false, player.displayName, FlashLanguage.SERVER_NAME.string).send()
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChatFormat(event: AsyncPlayerChatEvent) {
        if (!FlashLanguage.FORMAT_CHAT.boolean) return
        val user: User = Flash.instance.userHandler.tryUser(event.player.uniqueId, false)
        event.format = CC.translate(user.displayName + "&7: &f") + event.message
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChat(event: AsyncPlayerChatEvent) {
        if (!event.player.hasPermission("flash.staff")) return
        val staff: User = Flash.instance.userHandler.tryUser(event.player.uniqueId, false)
        if (!staff.getStaffInfo().isStaffChat) return
        event.isCancelled = true
        StaffMessagePacket("&9[Staff Chat] " + staff.displayName + "&7: &f" + event.message).send()
    }
}