package dev.lbuddyboy.flash.user.listener;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.command.user.UserCommand;
import dev.lbuddyboy.flash.user.menu.GrantMenu;
import dev.lbuddyboy.flash.user.menu.GrantsMenu;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.GrantBuild;
import dev.lbuddyboy.flash.user.model.PermissionBuild;
import dev.lbuddyboy.flash.user.model.UserPermission;
import dev.lbuddyboy.flash.user.packet.GrantRemovePacket;
import dev.lbuddyboy.flash.user.packet.PermissionRemovePacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import dev.lbuddyboy.flash.util.bukkit.UserUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GrantListener implements Listener {

    public static Map<String, Grant> grantRemoveMap = new HashMap<>();
    public static Map<String, UserPermission> grantRemovePermMap = new HashMap<>();
    public static Map<String, UUID> grantTargetRemoveMap = new HashMap<>();

    public static Map<String, GrantBuild> grantTargetMap = new HashMap<>();
    public static Map<String, PermissionBuild> grantPermissionTargetMap = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        if (!grantRemoveMap.containsKey(event.getPlayer().getName())) return;

        event.setCancelled(true);

        Grant grant = grantRemoveMap.remove(event.getPlayer().getName());
        UUID uuid = grantTargetRemoveMap.remove(event.getPlayer().getName());

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            Tasks.run(() -> {
                GrantsMenu grantsMenu = new GrantsMenu(uuid);
                grantsMenu.setView("ranks");
                grantsMenu.openMenu(event.getPlayer());
            });
            return;
        }

        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        grant.setRemovedFor(event.getMessage());
        grant.setRemovedAt(System.currentTimeMillis());
        grant.setRemovedBy(event.getPlayer().getUniqueId());

        if (Bukkit.getPlayer(uuid) == null) {
            new GrantRemovePacket(uuid, grant).send();
        } else {
            user.save(true);
            user.updateGrants();
            user.buildPlayer();
        }

        event.getPlayer().sendMessage(CC.translate("&aRemoved the grant from " + user.getColoredName() +"&a."));

        Tasks.run(() -> new GrantsMenu(uuid).openMenu(event.getPlayer()));

    }

    @EventHandler
    public void onChatGrant(AsyncPlayerChatEvent event) {

        if (!grantTargetMap.containsKey(event.getPlayer().getName())) return;

        event.setCancelled(true);

        GrantBuild grantBuild = grantTargetMap.get(event.getPlayer().getName());
        UUID target = grantBuild.getTarget();

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            Tasks.run(() -> new GrantMenu(target).openMenu(event.getPlayer()));
            return;
        }

        if (grantBuild.getReason() == null) {

            grantBuild.setReason(event.getMessage());
            event.getPlayer().sendMessage(CC.translate("&aNow, type the duration for granting the " + grantBuild.getRank().getColoredName() + " &arank to " + UserUtils.formattedName(target) + "&a."));

            return;
        }

        if (grantBuild.getTime() == null) {

            grantBuild.setTime(event.getMessage());
            event.getPlayer().sendMessage(CC.translate("&aNow, type the scopes for granting the " + grantBuild.getRank().getColoredName() + " &arank to " + UserUtils.formattedName(target) + "&a."));
            event.getPlayer().sendMessage(CC.translate("&aSeparate the servers with a command. (Ex: Global,Hub)"));

            return;
        }

        if (grantBuild.getScopes() == null) {

            grantBuild.setScopes(event.getMessage().split(","));

            UserCommand.rankAdd(event.getPlayer(), target, grantBuild.getRank(), grantBuild.getTime(), grantBuild.getScopes(), grantBuild.getReason());

            Tasks.run(() -> new GrantMenu(target).openMenu(event.getPlayer()));

            grantTargetMap.remove(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onChatGrantPermission(AsyncPlayerChatEvent event) {

        if (!grantPermissionTargetMap.containsKey(event.getPlayer().getName())) return;

        event.setCancelled(true);

        PermissionBuild permissionBuild = grantPermissionTargetMap.get(event.getPlayer().getName());
        UUID target = permissionBuild.getTarget();

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            Tasks.run(() -> new GrantMenu(target).openMenu(event.getPlayer()));
            return;
        }

        if (permissionBuild.getNode() == null) {

            permissionBuild.setNode(event.getMessage());
            event.getPlayer().sendMessage(CC.translate("&aNow, type the reason for granting the " + permissionBuild.getNode() + " &apermission to " + UserUtils.formattedName(target) + "&a."));

            return;
        }

        if (permissionBuild.getReason() == null) {

            permissionBuild.setReason(event.getMessage());
            event.getPlayer().sendMessage(CC.translate("&aNow, type the duration for granting the " + permissionBuild.getNode() + " &apermission to " + UserUtils.formattedName(target) + "&a."));

            return;
        }

        if (permissionBuild.getTime() == null) {

            permissionBuild.setTime(event.getMessage());
            UserCommand.permissionAdd(event.getPlayer(), target, permissionBuild.getNode(), permissionBuild.getTime(), permissionBuild.getReason());

            Tasks.run(() -> new GrantMenu(target).openMenu(event.getPlayer()));

            grantPermissionTargetMap.remove(event.getPlayer().getName());
        }

    }

    @EventHandler
    public void onChatRemovePerm(AsyncPlayerChatEvent event) {

        if (!grantRemovePermMap.containsKey(event.getPlayer().getName())) return;

        event.setCancelled(true);

        UserPermission permission = grantRemovePermMap.remove(event.getPlayer().getName());
        UUID uuid = grantTargetRemoveMap.remove(event.getPlayer().getName());

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            Tasks.run(() -> {
                GrantsMenu grantsMenu = new GrantsMenu(uuid);
                grantsMenu.setView("permissions");
                grantsMenu.openMenu(event.getPlayer());
            });
            return;
        }

        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        permission.setRemovedFor(event.getMessage());
        permission.setRemovedAt(System.currentTimeMillis());
        permission.setRemovedBy(event.getPlayer().getUniqueId());
        permission.setRemoved(true);

        if (Bukkit.getPlayer(uuid) == null) {
            new PermissionRemovePacket(uuid, permission).send();
        } else {
            user.updatePerms();
            user.save(true);
        }

        event.getPlayer().sendMessage(CC.translate("&a"));

        Tasks.run(() -> {
            GrantsMenu grantsMenu = new GrantsMenu(uuid);
            grantsMenu.setView("permissions");
            grantsMenu.openMenu(event.getPlayer());
        });

    }

}
