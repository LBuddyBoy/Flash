package dev.lbuddyboy.flash.command.user

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.util.NameMCUtils
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@CommandAlias("namemc|claimrank|claimfreerank|freerank")
object NameMCCommand : BaseCommand() {
    @Default
    fun namemc(sender: Player) {
        val user: User = Flash.instance.userHandler.tryUser(sender.uniqueId, false)
        if (user.getPlayerInfo().isClaimedNameMC) {
            sender.sendMessage(CC.translate("&cYou've already claimed a free rank."))
            return
        }
        if (!NameMCUtils.hasLiked(sender)) {
            sender.sendMessage(CC.translate("&cYou need to like " + FlashLanguage.SERVER_IP.string + " on NameMC"))
            sender.sendMessage(CC.translate("&c - https://namemc.com/server/" + FlashLanguage.SERVER_IP.string + "/"))
            return
        }
        for (s in CC.applyTarget(FlashLanguage.NAMEMC_RANK_CLAIM_COMMANDS.stringList, user.getUuid())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s)
        }
        user.getPlayerInfo().isClaimedNameMC = true
        user.save(true)
    }
}