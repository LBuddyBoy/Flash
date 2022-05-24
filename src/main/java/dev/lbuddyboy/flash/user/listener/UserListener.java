package dev.lbuddyboy.flash.user.listener;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.user.packet.ServerChangePacket;
import dev.lbuddyboy.flash.util.CC;
import dev.lbuddyboy.flash.util.EncryptionHandler;
import dev.lbuddyboy.flash.util.Tasks;
import dev.lbuddyboy.flash.util.UUIDUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

        user.setIp(EncryptionHandler.encryptUsingKey(event.getAddress().getHostAddress()));
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
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, CC.translate(FlashLanguage.PUNISHMENT_BANNED_IP_RELATIVE.getString(), "%OWNER%", UUIDUtils.formattedName(alts.get(0))));
            return;
        }

        Flash.getInstance().getUserHandler().getUsers().put(event.getUniqueId(), user);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Tasks.runAsync(() -> {
            User user = Flash.getInstance().getUserHandler().tryUser(event.getPlayer().getUniqueId(), false);

            user.setCurrentServer(FlashLanguage.SERVER_NAME.getString());

            user.updatePerms();
            user.updateGrants();
            user.buildPlayer();

            if (player.hasPermission("flash.staff")) {
                Tasks.runAsyncLater(() -> {
                    User staff = Flash.getInstance().getUserHandler().getUser(player.getUniqueId(), true);

                    new ServerChangePacket(true, player.getDisplayName(), FlashLanguage.SERVER_NAME.getString(), staff.getLastServer()).send();
                }, 40);
            }

        });

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        User user = Flash.getInstance().getUserHandler().getUsers().remove(player.getUniqueId());

        user.setCurrentServer(null);
        user.setLastServer(FlashLanguage.SERVER_NAME.getString());
        user.updateGrants();

        user.save(true);

        if (player.hasPermission("flash.staff")) {
            staffLeave(player);
        }

    }


    /*

      NOTES

      We pull from the db the profile and saved variables. If their current server is null, which
      it can only be null if they do not rejoin. It will send a network leave packet and provide the
      server they left from. When they join it sets their current server. We delay this task, so we
      can give them a chance to update these variables.

    */
    private void staffLeave(Player player) {
        Tasks.runAsyncLater(() -> {
            User staff = Flash.getInstance().getUserHandler().getUser(player.getUniqueId(), true);

            if (staff.getCurrentServer() == null) {
                new ServerChangePacket(false, player.getDisplayName(), null, FlashLanguage.SERVER_NAME.getString()).send();
            }
        }, 40);
    }

}
