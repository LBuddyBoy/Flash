package dev.lbuddyboy.flash.command.user

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.menu.SearchUsersMenu
import dev.lbuddyboy.flash.user.model.UserPermission
import org.bukkit.entity.Player
import java.util.stream.Collectors

@CommandAlias("lookup|paramlookup")
@CommandPermission("flash.command.lookup")
class LookUpCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun lookup(sender: Player?, @Name("params {rank:<rank>|permission:<perm>}") param: String) {
        val users: MutableList<User?> = ArrayList()
        if (param.startsWith("permission:")) {
            val args = param.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (target in Flash.instance.cacheHandler.getUserCache().allUUIDs()) {
                val user = Flash.instance.userHandler.tryUser(target, true) ?: continue
                if (user.activePermissions.stream().map { obj: UserPermission? -> obj.getNode() }
                        .collect(Collectors.toList()).contains(
                        args[1]
                    )
                ) {
                    users.add(user)
                }
            }
        } else if (param.startsWith("rank:")) {
            val args = param.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (target in Flash.instance.cacheHandler.getUserCache().allUUIDs()) {
                val user = Flash.instance.userHandler.tryUserRank(target, true) ?: continue
                if (user.activeRank.getName().equals(args[1], ignoreCase = true)) {
                    users.add(user)
                }
            }
        }
        SearchUsersMenu(users).openMenu(sender!!)
    }
}