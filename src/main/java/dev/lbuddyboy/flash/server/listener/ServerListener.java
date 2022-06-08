package dev.lbuddyboy.flash.server.listener;

import dev.lbuddyboy.flash.server.Server;
import dev.lbuddyboy.flash.server.menu.ServersMenu;
import dev.lbuddyboy.flash.server.packet.ServerCommandPacket;
import dev.lbuddyboy.flash.user.menu.GrantsMenu;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerListener implements Listener {

    public static Map<String, Server> serverCommandMap = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        if (!serverCommandMap.containsKey(event.getPlayer().getName())) return;

        event.setCancelled(true);

        Server server = serverCommandMap.remove(event.getPlayer().getName());

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            Tasks.run(() -> new ServersMenu().openMenu(event.getPlayer()));
            return;
        }

        event.getPlayer().sendMessage(CC.translate("&aSuccessfully sent /" + event.getMessage() + " to the " + server.getName() + " server."));
        new ServerCommandPacket(server.getName(), event.getMessage()).send();

        Tasks.run(() -> new ServersMenu().openMenu(event.getPlayer()));

    }

}
