package dev.lbuddyboy.flash.server.listener;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.command.server.NotificationsCommand;
import dev.lbuddyboy.flash.command.user.note.NotesCommand;
import dev.lbuddyboy.flash.server.menu.NotificationsEditorMenu;
import dev.lbuddyboy.flash.server.model.Notification;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.menu.NotesMenu;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class NotificationListener implements Listener {

    public static List<String> notificationsAdd = new ArrayList<>();
    public static Map<String, String> notificationTargetAddTitleMap = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatAdd(AsyncPlayerChatEvent event) {

        if (!notificationsAdd.contains(event.getPlayer().getName())) return;

        event.setCancelled(true);

        if (!notificationTargetAddTitleMap.containsKey(event.getPlayer().getName())) {

            if (event.getMessage().equalsIgnoreCase("cancel")) {
                Tasks.run(() -> new NotificationsEditorMenu(event.getPlayer().getUniqueId()).openMenu(event.getPlayer()));
                return;
            }

            notificationTargetAddTitleMap.put(event.getPlayer().getName(), event.getMessage());
            event.getPlayer().sendMessage(CC.translate("&aNow, please type the message of the notification you would like to create."));
            return;
        }

        String title = notificationTargetAddTitleMap.remove(event.getPlayer().getName());
        notificationsAdd.remove(event.getPlayer().getName());

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            Tasks.run(() -> new NotificationsEditorMenu(event.getPlayer().getUniqueId()).openMenu(event.getPlayer()));
            return;
        }

        NotificationsCommand.create(event.getPlayer(), title, event.getMessage());

        event.getPlayer().sendMessage(CC.translate("&aCreated the " + title + " notification with the message " + event.getMessage() + "&a."));

        Tasks.run(() -> new NotificationsEditorMenu(event.getPlayer().getUniqueId()).openMenu(event.getPlayer()));
    }

}
