package dev.lbuddyboy.flash.command.user.note;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.menu.NotesMenu;
import dev.lbuddyboy.flash.user.model.Note;
import dev.lbuddyboy.flash.user.packet.GrantAddPacket;
import dev.lbuddyboy.flash.user.packet.NoteAddPacket;
import dev.lbuddyboy.flash.user.packet.NoteRemovePacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("note|notes")
@CommandPermission("flash.command.note")
public class NotesCommand extends BaseCommand {

    @Subcommand("add|new|addnote|create")
    @CommandCompletion("@target")
    public static void add(CommandSender sender, @Name("target") UUID target, @Name("title") @Single String title, @Name("note") String note) {
        Note noteObj = new Note(UUID.randomUUID(), title, note, sender instanceof Player ? ((Player) sender).getUniqueId() : null, System.currentTimeMillis());

        User user = Flash.getInstance().getUserHandler().tryUser(target, true);

        if (Bukkit.getPlayer(target) != null) {
            user.getNotes().add(noteObj);
            user.save(true);
        } else {
            new NoteAddPacket(target, noteObj).send();
        }

        sender.sendMessage(CC.translate("&aAdded the '" + note + "' note to " + user.getColoredName() + "&a."));
    }

    @Subcommand("remove|delnote|delete")
    @CommandCompletion("@target")
    public static void remove(CommandSender sender, @Name("target") UUID target, @Name("title") String title) {

        User user = Flash.getInstance().getUserHandler().tryUser(target, true);
        Note noteObj = user.getNote(title);

        if (noteObj == null) {
            sender.sendMessage(CC.translate("&cCould not find a note with that message"));
            return;
        }

        noteObj.setRemoved(true);
        noteObj.setRemovedBy(sender instanceof Player ? ((Player) sender).getUniqueId() : null);
        noteObj.setRemovedFor("No reason specified");
        noteObj.setRemovedAt(System.currentTimeMillis());

        if (Bukkit.getPlayer(target) != null) {
            user.save(true);
        } else {
            new NoteRemovePacket(target, noteObj).send();
        }

        sender.sendMessage(CC.translate("&aRemoved the " + title + " note from " + user.getColoredName() + "&a."));
    }

    @Subcommand("list|info")
    @CommandCompletion("@target")
    public static void list(CommandSender sender, @Name("target") UUID target) {

        User user = Flash.getInstance().getUserHandler().tryUser(target, true);
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate(user.getColoredName() + "'s &7Notes"));
            for (Note note : user.getSortedNotes()) {
                sender.sendMessage(CC.translate(" &7&o(" + note.getDateSentAt() + ") &e" + note.getTitle() + "&7: &f" + note.getMessage()));
            }
            return;
        }

        new NotesMenu(target).openMenu((Player) sender);
    }

}
