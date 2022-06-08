package dev.lbuddyboy.flash.user.listener;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.command.user.UserCommand;
import dev.lbuddyboy.flash.command.user.note.NotesCommand;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.menu.GrantMenu;
import dev.lbuddyboy.flash.user.menu.GrantsMenu;
import dev.lbuddyboy.flash.user.menu.NotesMenu;
import dev.lbuddyboy.flash.user.model.*;
import dev.lbuddyboy.flash.user.packet.GrantRemovePacket;
import dev.lbuddyboy.flash.user.packet.NoteRemovePacket;
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

public class NoteListener implements Listener {

    public static Map<String, Note> noteRemoveMap = new HashMap<>();
    public static Map<String, UUID> noteTargetRemoveMap = new HashMap<>();

    public static Map<String, UUID> noteTargetAddMap = new HashMap<>();
    public static Map<String, String> noteTargetAddTitleMap = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        if (!noteRemoveMap.containsKey(event.getPlayer().getName())) return;

        event.setCancelled(true);

        Note note = noteRemoveMap.remove(event.getPlayer().getName());
        UUID uuid = noteTargetRemoveMap.remove(event.getPlayer().getName());

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            Tasks.run(() -> new NotesMenu(uuid).openMenu(event.getPlayer()));
            return;
        }

        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        note.setRemoved(true);
        note.setRemovedFor(event.getMessage());
        note.setRemovedAt(System.currentTimeMillis());
        note.setRemovedBy(event.getPlayer().getUniqueId());

        if (Bukkit.getPlayer(uuid) == null) {
            new NoteRemovePacket(uuid, note).send();
        } else {
            user.save(true);
        }

        event.getPlayer().sendMessage(CC.translate("&aRemoved the " + note.getTitle() + " note from " + user.getColoredName() + "&a."));

        Tasks.run(() -> new NotesMenu(uuid).openMenu(event.getPlayer()));
    }

    @EventHandler
    public void onChatAdd(AsyncPlayerChatEvent event) {

        if (!noteTargetAddMap.containsKey(event.getPlayer().getName())) return;

        event.setCancelled(true);

        UUID uuid = noteTargetAddMap.get(event.getPlayer().getName());
        if (!noteTargetAddTitleMap.containsKey(event.getPlayer().getName())) {

            if (event.getMessage().equalsIgnoreCase("cancel")) {
                UUID finalUuid = uuid;
                Tasks.run(() -> new NotesMenu(finalUuid));
                return;
            }

            noteTargetAddTitleMap.put(event.getPlayer().getName(), event.getMessage());
            event.getPlayer().sendMessage(CC.translate("&aNow, please type the note you would like to add."));
            return;
        }

        uuid = noteTargetAddMap.remove(event.getPlayer().getName());
        String title = noteTargetAddTitleMap.remove(event.getPlayer().getName());

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            UUID finalUuid1 = uuid;
            Tasks.run(() -> new NotesMenu(finalUuid1).openMenu(event.getPlayer()));
            return;
        }

        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        NotesCommand.add(event.getPlayer(), uuid, title, event.getMessage());

        event.getPlayer().sendMessage(CC.translate("&aAdded the " + title + " note from " + user.getColoredName() + "&a."));

        UUID finalUuid2 = uuid;
        Tasks.run(() -> new NotesMenu(finalUuid2).openMenu(event.getPlayer()));
    }

}
