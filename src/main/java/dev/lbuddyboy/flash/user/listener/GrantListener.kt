package dev.lbuddyboy.flash.user.listener

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.command.user.UserCommand
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.menu.GrantMenu
import dev.lbuddyboy.flash.user.menu.GrantsMenu
import dev.lbuddyboy.flash.user.model.Grant
import dev.lbuddyboy.flash.user.model.GrantBuild
import dev.lbuddyboy.flash.user.model.PermissionBuild
import dev.lbuddyboy.flash.user.model.UserPermission
import dev.lbuddyboy.flash.user.packet.GrantRemovePacket
import dev.lbuddyboy.flash.user.packet.PermissionRemovePacket
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.Tasks
import dev.lbuddyboy.flash.util.bukkit.UserUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.*

class GrantListener : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: AsyncPlayerChatEvent) {
        if (!grantRemoveMap.containsKey(event.player.name)) return
        event.isCancelled = true
        val grant = grantRemoveMap.remove(event.player.name)
        val uuid = grantTargetRemoveMap.remove(event.player.name)
        if (event.message.equals("cancel", ignoreCase = true)) {
            Tasks.run {
                val grantsMenu = GrantsMenu(uuid)
                grantsMenu.setView("ranks")
                grantsMenu.openMenu(event.player)
            }
            return
        }
        val user: User = Flash.instance.userHandler.tryUser(uuid, true)
        grant.setRemovedFor(event.message)
        grant.setRemovedAt(System.currentTimeMillis())
        grant.setRemovedBy(event.player.uniqueId)
        for (userGrant in user.getGrants()) {
            if (userGrant.uuid.toString() != grant.getUuid().toString()) continue
            userGrant.removedBy = grant.getRemovedBy()
            userGrant.removedFor = grant.getRemovedFor()
            userGrant.removedAt = grant.getRemovedAt()
        }
        GrantRemovePacket(uuid, grant).send()
        user.save(true)
        user.updateGrants()
        user.buildPlayer()
        event.player.sendMessage(CC.translate("&aRemoved the grant from " + user.coloredName + "&a."))
        Tasks.run { GrantsMenu(uuid).openMenu(event.player) }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChatGrant(event: AsyncPlayerChatEvent) {
        if (!grantTargetMap.containsKey(event.player.name)) return
        event.isCancelled = true
        val grantBuild = grantTargetMap[event.player.name]
        val target = grantBuild.getTarget()
        if (event.message.equals("cancel", ignoreCase = true)) {
            Tasks.run { GrantMenu(target).openMenu(event.player) }
            return
        }
        if (grantBuild.getReason() == null) {
            grantBuild.setReason(event.message)
            event.player.sendMessage(
                CC.translate(
                    "&aNow, type the duration for granting the " + grantBuild!!.rank.coloredName + " &arank to " + UserUtils.formattedName(
                        target
                    ) + "&a."
                )
            )
            return
        }
        if (grantBuild.getTime() == null) {
            grantBuild.setTime(event.message)
            event.player.sendMessage(
                CC.translate(
                    "&aNow, type the scopes for granting the " + grantBuild!!.rank.coloredName + " &arank to " + UserUtils.formattedName(
                        target
                    ) + "&a."
                )
            )
            event.player.sendMessage(CC.translate("&aSeparate the servers with a command. (Ex: Global,Hub)"))
            return
        }
        if (grantBuild.getScopes() == null) {
            grantBuild.setScopes(event.message.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            UserCommand.rankAdd(
                event.player,
                target,
                grantBuild!!.rank,
                grantBuild.time,
                grantBuild.scopes,
                grantBuild.reason
            )
            Tasks.run { GrantsMenu(target).openMenu(event.player) }
            grantTargetMap.remove(event.player.name)
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChatGrantPermission(event: AsyncPlayerChatEvent) {
        if (!grantPermissionTargetMap.containsKey(event.player.name)) return
        event.isCancelled = true
        val permissionBuild = grantPermissionTargetMap[event.player.name]
        val target = permissionBuild.getTarget()
        if (event.message.equals("cancel", ignoreCase = true)) {
            Tasks.run { GrantMenu(target).openMenu(event.player) }
            return
        }
        if (permissionBuild.getNode() == null) {
            permissionBuild.setNode(event.message)
            event.player.sendMessage(
                CC.translate(
                    "&aNow, type the reason for granting the " + permissionBuild.getNode() + " &apermission to " + UserUtils.formattedName(
                        target
                    ) + "&a."
                )
            )
            return
        }
        if (permissionBuild.getReason() == null) {
            permissionBuild.setReason(event.message)
            event.player.sendMessage(
                CC.translate(
                    "&aNow, type the duration for granting the " + permissionBuild.getNode() + " &apermission to " + UserUtils.formattedName(
                        target
                    ) + "&a."
                )
            )
            return
        }
        if (permissionBuild.getTime() == null) {
            permissionBuild.setTime(event.message)
            UserCommand.permissionAdd(
                event.player,
                target,
                permissionBuild.getNode().split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray(),
                permissionBuild.getTime(),
                permissionBuild.getReason())
            Tasks.run { GrantMenu(target).openMenu(event.player) }
            grantPermissionTargetMap.remove(event.player.name)
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChatRemovePerm(event: AsyncPlayerChatEvent) {
        if (!grantRemovePermMap.containsKey(event.player.name)) return
        event.isCancelled = true
        val permission = grantRemovePermMap.remove(event.player.name)
        val uuid = grantTargetRemoveMap.remove(event.player.name)
        if (event.message.equals("cancel", ignoreCase = true)) {
            Tasks.run {
                val grantsMenu = GrantsMenu(uuid)
                grantsMenu.setView("permissions")
                grantsMenu.openMenu(event.player)
            }
            return
        }
        val user: User = Flash.instance.userHandler.tryUser(uuid, true)
        permission.setRemovedFor(event.message)
        permission.setRemovedAt(System.currentTimeMillis())
        permission.setRemovedBy(event.player.uniqueId)
        permission.setRemoved(true)
        for (userPermission in user.getPermissions()) {
            if (userPermission.node != permission.getNode()) continue
            userPermission.removedBy = permission.getRemovedBy()
            userPermission.removedFor = permission.getRemovedFor()
            userPermission.removedAt = permission.getRemovedAt()
            userPermission.isRemoved = permission.isRemoved()
        }
        PermissionRemovePacket(uuid, permission).send()
        user.updatePerms()
        user.save(true)
        event.player.sendMessage(CC.translate("&a"))
        Tasks.run {
            val grantsMenu = GrantsMenu(uuid)
            grantsMenu.setView("permissions")
            grantsMenu.openMenu(event.player)
        }
    }

    companion object {
        var grantRemoveMap: MutableMap<String, Grant?> = HashMap()
        var grantRemovePermMap: MutableMap<String, UserPermission?> = HashMap()
        var grantTargetRemoveMap: MutableMap<String, UUID?> = HashMap()
        var grantTargetMap: MutableMap<String, GrantBuild> = HashMap()
        var grantPermissionTargetMap: MutableMap<String, PermissionBuild> = HashMap()
    }
}