package dev.lbuddyboy.flash.user.listener;

import dev.lbuddyboy.flash.user.packet.StaffMessagePacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FreezeListener implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		Location from = event.getFrom();
		Location to = event.getTo();
		if (isFrozen(player)) {
			if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
				event.setTo(from);
			}
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (isFrozen(player)) {
			if (!event.getMessage().startsWith("/helpop") && !event.getMessage().startsWith("/faction chat")
					&& !event.getMessage().startsWith("/fac chat") && !event.getMessage().startsWith("/f chat")
					&& !event.getMessage().startsWith("/faction c") && !event.getMessage().startsWith("/fac c")
					&& !event.getMessage().startsWith("/f c") && !event.getMessage().startsWith("/helpop")
					&& !event.getMessage().startsWith("/request") && !event.getMessage().startsWith("/msg")
					&& !event.getMessage().startsWith("/r") && !event.getMessage().startsWith("/reply")
					&& !event.getMessage().startsWith("/ss") && !event.getMessage().startsWith("/freeze")
					&& !event.getMessage().startsWith("/panic") && !event.getMessage().startsWith("/tpm")
					&& !event.getMessage().startsWith("/message") && !event.getMessage().startsWith("/reply")) {
				event.setCancelled(true);
				player.sendMessage(CC.translate("&cYou can not use commands while you are frozen."));
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (isFrozen(player)) {
			event.setCancelled(true);
			player.sendMessage(CC.translate("&cYou can not place blocks while you are frozen."));
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (isFrozen(player)) {
			event.setCancelled(true);
			player.sendMessage(CC.translate("&cYou can not break blocks while you are frozen."));
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (isFrozen(player)) {
			new StaffMessagePacket("&4&l[Freeze] " + player.getDisplayName() + " &fhas just left the server whilst frozen.").send();
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (isFrozen(player)) {
			new StaffMessagePacket("&4&l[Freeze] " + player.getDisplayName() + " &fhas just joined the server whilst frozen.").send();
		}
	}

	public Player getDamager(Entity entity) {
		if (entity instanceof Player) {
			return (Player) entity;
		}
		if (entity instanceof Projectile) {
			Projectile projectile = (Projectile) entity;
			if (projectile.getShooter() != null && projectile.getShooter() instanceof Player) {
				return (Player) projectile.getShooter();
			}
		}
		return null;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Player damager = getDamager(event.getDamager());
		Player damaged = getDamager(event.getEntity());
		if (damager != null && damaged != null && damaged != damager) {
			if (isFrozen(damager)) {
				damager.sendMessage(CC.translate("&cYou can not attack players while frozen."));
				event.setCancelled(true);
			}

			if (isFrozen(damaged)) {
				damager.sendMessage(CC
						.translate("&c" + damaged.getName() + "&c is currently frozen, you may not attack."));
				event.setCancelled(true);
			}
		}

	}

	public boolean isFrozen(Player player) {
		return player.hasMetadata("frozen");
	}
}
