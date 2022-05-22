package dev.lbuddyboy.flash.user.listener;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.packet.ServerChangePacket;
import dev.lbuddyboy.flash.util.EncryptionHandler;
import dev.lbuddyboy.flash.util.Tasks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {

        User user = Flash.getInstance().getUserHandler().createUser(event.getUniqueId(), event.getName());

        user.load();

        user.setIp(EncryptionHandler.encryptUsingKey(event.getAddress().getHostAddress()));
        if (!user.getKnownIps().contains(user.getIp())) {
            user.getKnownIps().add(user.getIp());
        }

        Flash.getInstance().getUserHandler().getUsers().put(event.getUniqueId(), user);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Tasks.runAsync(() -> {
            User user = Flash.getInstance().getUserHandler().getUser(event.getPlayer().getUniqueId(), false);

            if (player.hasPermission("flash.staff")) {
                Tasks.runAsyncLater(() -> {
                    if (user.getLastServer() == null) {
                        new ServerChangePacket(true, player.getDisplayName(), null, user.getLastServer()).send();
                    } else {
                        new ServerChangePacket(true, player.getDisplayName(), FlashLanguage.SERVER_NAME.getString(), user.getLastServer()).send();
                    }
                }, 10);
            }

            user.updatePerms();
            user.updateGrants();
            user.buildPlayer(event.getPlayer());
            user.setLastServer(FlashLanguage.SERVER_NAME.getString());
        });

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        User user = Flash.getInstance().getUserHandler().getUsers().remove(player.getUniqueId());

        user.setLastServer(null);
        user.updateGrants();

        user.save(true);

        if (player.hasPermission("flash.staff")) {
            Tasks.runAsyncLater(() -> {
                User staff = Flash.getInstance().getUserHandler().getUser(player.getUniqueId(), true);

                if (staff.getLastServer() == null) {
                    new ServerChangePacket(false, player.getDisplayName(), null, FlashLanguage.SERVER_NAME.getString()).send();
                    return;
                }

                new ServerChangePacket(false, player.getDisplayName(), user.getLastServer(), FlashLanguage.SERVER_NAME.getString()).send();
            }, 10);
        }

    }

}
