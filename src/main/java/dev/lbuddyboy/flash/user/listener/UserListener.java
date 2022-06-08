package dev.lbuddyboy.flash.user.listener;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.user.packet.ServerChangePacket;
import dev.lbuddyboy.flash.user.packet.StaffMessagePacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.HashUtil;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import dev.lbuddyboy.flash.util.bukkit.UserUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserListener implements Listener {

    @EventHandler
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

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = Flash.getInstance().getUserHandler().tryUser(event.getPlayer().getUniqueId(), false);

        if (user == null) {
            player.sendMessage(CC.translate("&cFailed to load your profile. Please re-log."));
            player.kickPlayer(CC.translate("&cFailed to load your profile. Please re-log."));
            return;
        }

        Tasks.runAsync(() -> {

            user.updatePerms();
            user.updateGrants();
            user.buildPlayer();

            if (player.hasPermission("flash.staff")) {
                new ServerChangePacket(true, player.getDisplayName(), FlashLanguage.SERVER_NAME.getString()).send();
            }
        });

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        User user = Flash.getInstance().getUserHandler().getUsers().remove(player.getUniqueId());

        user.updatePerms();
        user.updateGrants();

        user.save(true);

        if (player.hasPermission("flash.staff")) {
            new ServerChangePacket(false, player.getDisplayName(), FlashLanguage.SERVER_NAME.getString()).send();
        }
    }

    @EventHandler
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

}
