package dev.lbuddyboy.flash.user.listener;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.command.essentials.InvseeCommand;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.server.model.Notification;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.user.packet.ServerChangePacket;
import dev.lbuddyboy.flash.user.packet.StaffMessagePacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.HashUtil;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import dev.lbuddyboy.flash.util.bukkit.UserUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {

        User user = Flash.getInstance().getUserHandler().createUser(event.getUniqueId(), event.getName());

        user.setIp(HashUtil.encryptUsingKey(event.getAddress().getHostAddress()));
        if (!user.getKnownIps().contains(user.getIp())) {
            user.getKnownIps().add(user.getIp());
        }

        if (user.hasActivePunishment(PunishmentType.BAN)) {
            Punishment punishment = user.getActivePunishment(PunishmentType.BAN);

            String message = CC.translate(punishment.getType().getKickMessage(),
                    "%REASON%", punishment.getSentFor(),
                    "%REASON%", punishment.getSentFor(),
                    "%TEMP-FORMAT%", FlashLanguage.PUNISHMENT_TEMPORARY_FORMAT.getString().replaceAll("%TIME%", punishment.getExpireString())
            );
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message);
            return;
        }

        if (user.hasActivePunishment(PunishmentType.IP_BAN)) {
            Punishment punishment = user.getActivePunishment(PunishmentType.IP_BAN);

            String message = CC.translate(punishment.getType().getKickMessage(),
                    "%REASON%", punishment.getSentFor(),
                    "%REASON%", punishment.getSentFor(),
                    "%TEMP-FORMAT%", FlashLanguage.PUNISHMENT_TEMPORARY_FORMAT.getString().replaceAll("%TIME%", punishment.getExpireString())
            );
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message);
            return;
        }

        if (user.hasActivePunishment(PunishmentType.BLACKLIST)) {
            Punishment punishment = user.getActivePunishment(PunishmentType.BLACKLIST);

            String message = CC.translate(punishment.getType().getKickMessage(),
                    "%REASON%", punishment.getSentFor(),
                    "%REASON%", punishment.getSentFor(),
                    "%TEMP-FORMAT%", FlashLanguage.PUNISHMENT_TEMPORARY_FORMAT.getString().replaceAll("%TIME%", punishment.getExpireString())
            );
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message);
            return;
        }

        List<UUID> alts = new ArrayList<>();
        Flash.getInstance().getUserHandler().relativeAlts(user.getIp(), alts);

        if (alts.contains(event.getUniqueId())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, CC.translate(FlashLanguage.PUNISHMENT_BANNED_IP_RELATIVE.getString(), "%OWNER%", UserUtils.formattedName(alts.get(0))));
            return;
        }

        Flash.getInstance().getUserHandler().getUsers().put(event.getUniqueId(), user);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = Flash.getInstance().getUserHandler().tryUser(event.getPlayer().getUniqueId(), false);

        if (user == null) {
            player.sendMessage(CC.translate("&cFailed to load your profile. Please re-log."));
            player.kickPlayer(CC.translate("&cFailed to load your profile. Please re-log."));
            return;
        }


/*            if (user.getPlayerInfo().isOfflineInventoryEdited()) {
                player.getInventory().setArmorContents(user.getServerInfo().getArmor());
                player.getInventory().setContents(user.getServerInfo().getContents());
                player.updateInventory();
                user.getPlayerInfo().setOfflineInventoryEdited(false);
            }*/

        user.updatePerms();
        user.updateGrants();
        user.buildPlayer();

        if (player.hasPermission("flash.staff")) {
            new ServerChangePacket(true, player.getDisplayName(), FlashLanguage.SERVER_NAME.getString()).send();
        }

        List<Notification> notifications = Flash.getInstance().getServerHandler().getUnReadNotifications(user);
        if (notifications.size() > 0) {
            player.sendMessage(CC.translate("&aYou currently have " + notifications.size() + " unread notifications. &7(( /notifications for more info ))"));
        }

    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (FlashLanguage.BLOCKED_COMMANDS.getStringList().contains(event.getMessage().toLowerCase())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        User user = Flash.getInstance().getUserHandler().getUsers().remove(player.getUniqueId());

//        user.addServerInfo();
//        user.getServerInfo().setArmor(player.getInventory().getArmorContents());
//        user.getServerInfo().setContents(player.getInventory().getContents());
        user.updatePerms();
        user.updateGrants();

        user.save(true);

        if (player.hasPermission("flash.staff")) {
            new ServerChangePacket(false, player.getDisplayName(), FlashLanguage.SERVER_NAME.getString()).send();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatFormat(AsyncPlayerChatEvent event) {
        if (!FlashLanguage.FORMAT_CHAT.getBoolean()) return;
        User user = Flash.getInstance().getUserHandler().tryUser(event.getPlayer().getUniqueId(), false);

        event.setFormat(CC.translate(user.getDisplayName() + "&7: &f") + event.getMessage());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("flash.staff")) return;

        User staff = Flash.getInstance().getUserHandler().tryUser(event.getPlayer().getUniqueId(), false);
        if (!staff.getStaffInfo().isStaffChat()) return;

        event.setCancelled(true);

        new StaffMessagePacket("&9[Staff Chat] " + staff.getDisplayName() + "&7: &f" + event.getMessage()).send();

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;

        Player player = (Player) event.getPlayer();
        if (!InvseeCommand.offlineEditMap.containsKey(player)) return;

        UUID target = InvseeCommand.offlineEditMap.remove(player);
        User user = Flash.getInstance().getUserHandler().tryUser(target, true);
        Inventory inventory = event.getInventory();
        ItemStack[] armor = new ItemStack[4];
        ItemStack[] contents = new ItemStack[36];

        for (int i = 0; i < 36; i++) {
            contents[i] = inventory.getItem(i);
        }

        armor[0] = inventory.getItem(44);
        armor[1] = inventory.getItem(43);
        armor[2] = inventory.getItem(42);
        armor[3] = inventory.getItem(41);

        user.getServerInfo().setContents(contents);
        user.getServerInfo().setArmor(armor);
        user.getPlayerInfo().setOfflineInventoryEdited(true);

        user.save(true);
    }

}
