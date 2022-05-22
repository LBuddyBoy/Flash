package dev.lbuddyboy.flash.user.listener;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.grant.menu.GrantsMenu;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.UserPermission;
import dev.lbuddyboy.flash.user.packet.ServerChangePacket;
import dev.lbuddyboy.flash.user.packet.UserUpdatePacket;
import dev.lbuddyboy.flash.util.CC;
import dev.lbuddyboy.flash.util.EncryptionHandler;
import dev.lbuddyboy.flash.util.Tasks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GrantListener implements Listener {

    public static Map<String, Grant> grantRemoveMap = new HashMap<>();
    public static Map<String, UserPermission> grantRemovePermMap = new HashMap<>();
    public static Map<String, UUID> grantTargetRemoveMap = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        if (!grantRemoveMap.containsKey(event.getPlayer().getName())) return;

        event.setCancelled(true);

        Grant grant = grantRemoveMap.remove(event.getPlayer().getName());
        UUID uuid = grantTargetRemoveMap.remove(event.getPlayer().getName());

        User user = Flash.getInstance().getUserHandler().getUser(uuid, true);

        grant.setRemovedFor(event.getMessage());
        grant.setRemovedAt(System.currentTimeMillis());
        grant.setRemovedBy(event.getPlayer().getUniqueId());

        user.updateGrants();
        user.save(true);

        event.getPlayer().sendMessage(CC.translate("&a"));

        new UserUpdatePacket(uuid, user).send();
        Tasks.run(() -> {
            new GrantsMenu(uuid).openMenu(event.getPlayer());
        });

    }

    @EventHandler
    public void onChatRemovePerm(AsyncPlayerChatEvent event) {

        if (!grantRemovePermMap.containsKey(event.getPlayer().getName())) return;

        event.setCancelled(true);

        UserPermission permission = grantRemovePermMap.remove(event.getPlayer().getName());
        UUID uuid = grantTargetRemoveMap.remove(event.getPlayer().getName());

        User user = Flash.getInstance().getUserHandler().getUser(uuid, true);

        permission.setRemovedFor(event.getMessage());
        permission.setRemovedAt(System.currentTimeMillis());
        permission.setRemovedBy(event.getPlayer().getUniqueId());
        permission.setRemoved(true);

        user.updatePerms();
        user.save(true);

        event.getPlayer().sendMessage(CC.translate("&a"));

        new UserUpdatePacket(uuid, user).send();
        Tasks.run(() -> {
            GrantsMenu grantsMenu = new GrantsMenu(uuid);
            grantsMenu.setView("permissions");
            grantsMenu.openMenu(event.getPlayer());
        });

    }

}
