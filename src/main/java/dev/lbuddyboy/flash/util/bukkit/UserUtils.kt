package dev.lbuddyboy.flash.util.bukkit

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.model.Punishment
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object UserUtils {
    fun formattedName(uuid: UUID?): String? {
        val user = Flash.instance.userHandler.tryUser(uuid, true) ?: return "&4Console"
        return user.coloredName
    }

    fun formattedName(sender: CommandSender?): String? {
        return if (sender !is Player) {
            "&4Console"
        } else formattedName(sender.uniqueId)
    }

    fun addPunishment(uuid: UUID?, punishment: Punishment?) {
        val user: User = Flash.instance.userHandler.tryUser(uuid, true)
        user.getStaffInfo().knownPunishments.add(punishment)
    }
}