package dev.lbuddyboy.flash.user.listener

import dev.lbuddyboy.flash.user.packet.StaffMessagePacket
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent

class FreezeListener : Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val from = event.from
        val to = event.to
        if (isFrozen(player)) {
            if (from.blockX != to.blockX || from.blockZ != to.blockZ) {
                event.to = from
            }
        }
    }

    @EventHandler
    fun onPlayerCommandPreprocess(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        if (isFrozen(player)) {
            if (!event.message.startsWith("/helpop") && !event.message.startsWith("/faction chat")
                && !event.message.startsWith("/fac chat") && !event.message.startsWith("/f chat")
                && !event.message.startsWith("/faction c") && !event.message.startsWith("/fac c")
                && !event.message.startsWith("/f c") && !event.message.startsWith("/helpop")
                && !event.message.startsWith("/request") && !event.message.startsWith("/msg")
                && !event.message.startsWith("/r") && !event.message.startsWith("/reply")
                && !event.message.startsWith("/ss") && !event.message.startsWith("/freeze")
                && !event.message.startsWith("/panic") && !event.message.startsWith("/tpm")
                && !event.message.startsWith("/sync")
                && !event.message.startsWith("/message") && !event.message.startsWith("/reply")
            ) {
                event.isCancelled = true
                player.sendMessage(CC.translate("&cYou can not use commands while you are frozen."))
            }
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        if (isFrozen(player)) {
            event.isCancelled = true
            player.sendMessage(CC.translate("&cYou can not place blocks while you are frozen."))
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        if (isFrozen(player)) {
            event.isCancelled = true
            player.sendMessage(CC.translate("&cYou can not break blocks while you are frozen."))
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        if (isFrozen(player)) {
            StaffMessagePacket("&4&l[Freeze] " + player.displayName + " &fhas just left the server whilst frozen.").send()
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (isFrozen(player)) {
            StaffMessagePacket("&4&l[Freeze] " + player.displayName + " &fhas just joined the server whilst frozen.").send()
        }
    }

    fun getDamager(entity: Entity?): Player? {
        if (entity is Player) {
            return entity
        }
        if (entity is Projectile) {
            val projectile = entity
            if (projectile.shooter != null && projectile.shooter is Player) {
                return projectile.shooter as Player
            }
        }
        return null
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val damager = getDamager(event.damager)
        val damaged = getDamager(event.entity)
        if (damager != null && damaged != null && damaged !== damager) {
            if (isFrozen(damager)) {
                damager.sendMessage(CC.translate("&cYou can not attack players while frozen."))
                event.isCancelled = true
            }
            if (isFrozen(damaged)) {
                damager.sendMessage(CC.translate("&c" + damaged.name + "&c is currently frozen, you may not attack."))
                event.isCancelled = true
            }
        }
    }

    fun isFrozen(player: Player): Boolean {
        return player.hasMetadata("frozen")
    }
}