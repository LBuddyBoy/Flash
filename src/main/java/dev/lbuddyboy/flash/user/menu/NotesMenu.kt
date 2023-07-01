package dev.lbuddyboy.flash.user.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashMenuLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.listener.GrantListener;
import dev.lbuddyboy.flash.user.listener.NoteListener;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.Note;
import dev.lbuddyboy.flash.user.model.UserPermission;
import dev.lbuddyboy.flash.user.packet.NoteAddPacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import dev.lbuddyboy.flash.util.bukkit.UserUtils;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotesMenu extends PagedMenu<Note> {

    public UUID uuid;

    public NotesMenu(UUID uuid) {
        this.uuid = uuid;
        this.objects = Flash.getInstance().getUserHandler().tryUser(uuid, true).getNotes();
    }

    @Override
    public String getPageTitle(Player player) {
        return CC.translate(FlashMenuLanguage.NOTES_MENU_TITLE.getString(),
                "%PLAYER%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid));
    }

    @Override
    public List<Button> getPageButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        int i = 1;
        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);
        for (Note note : user.getSortedNotes()) {
            buttons.add(new NoteButton(uuid, note, i++));
        }

        return buttons;
    }

    @Override
    public boolean autoUpdate() {
        return true;
    }

    @Override
    public boolean autoFill() {
        return true;
    }

    @Override
    public List<Button> getGlobalButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        buttons.add(new AddNoteButton(uuid));

        return buttons;
    }

    @AllArgsConstructor
    private static class AddNoteButton extends Button {

        public UUID target;

        @Override
        public int getSlot() {
            return 5;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(CompatibleMaterial.getMaterial("STAINED_CLAY"))
                    .setDurability(DyeColor.GREEN.getWoolData())
                    .setName("&aAdd Note &7(Click)")
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            player.closeInventory();
            NoteListener.noteTargetAddMap.put(player.getName(), target);

            player.sendMessage(CC.translate("&aType the title of the note you would like to add."));
        }
    }

    @AllArgsConstructor
    private static class NoteButton extends Button {

        public UUID target;
        public Note note;
        private int slot;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            if (note.isRemoved()) {
                return new ItemBuilder(FlashMenuLanguage.NOTES_MENU_REMOVED_NOTE_BUTTON_MATERIAL.getMaterial())
                        .setDurability(FlashMenuLanguage.NOTES_MENU_REMOVED_NOTE_BUTTON_DATA.getInt())
                        .setName(FlashMenuLanguage.NOTES_MENU_REMOVED_NOTE_BUTTON_NAME.getString(),
                                "%ADDEDAT%", note.getDateSentAt(),
                                "%ADDEDBY%", UserUtils.formattedName(note.getSender()),
                                "%ADDEDFOR%", note.getMessage(),
                                "%ADDEDTITLE%", note.getTitle(),
                                "%REMOVEDAT%", note.getRemovedAtDate(),
                                "%REMOVEDBY%", UserUtils.formattedName(note.getRemovedBy()),
                                "%REMOVEDFOR%", note.getRemovedFor())
                        .setLore(FlashMenuLanguage.NOTES_MENU_REMOVED_NOTE_BUTTON_LORE.getStringList(),
                                "%ADDEDAT%", note.getDateSentAt(),
                                "%ADDEDBY%", UserUtils.formattedName(note.getSender()),
                                "%ADDEDFOR%", note.getMessage(),
                                "%ADDEDTITLE%", note.getTitle(),
                                "%REMOVEDAT%", note.getRemovedAtDate(),
                                "%REMOVEDBY%", UserUtils.formattedName(note.getRemovedBy()),
                                "%REMOVEDFOR%", note.getRemovedFor())
                        .create();
            }
            return new ItemBuilder(FlashMenuLanguage.NOTES_MENU_DEFAULT_NOTE_BUTTON_MATERIAL.getMaterial())
                    .setDurability(FlashMenuLanguage.NOTES_MENU_DEFAULT_NOTE_BUTTON_DATA.getInt())
                    .setName(FlashMenuLanguage.NOTES_MENU_DEFAULT_NOTE_BUTTON_NAME.getString(),
                            "%ADDEDAT%", note.getDateSentAt(),
                            "%ADDEDBY%", UserUtils.formattedName(note.getSender()),
                            "%ADDEDFOR%", note.getMessage(),
                            "%ADDEDTITLE%", note.getTitle())
                    .setLore(FlashMenuLanguage.NOTES_MENU_DEFAULT_NOTE_BUTTON_LORE.getStringList(),
                            "%ADDEDAT%", note.getDateSentAt(),
                            "%ADDEDBY%", UserUtils.formattedName(note.getSender()),
                            "%ADDEDFOR%", note.getMessage(),
                            "%ADDEDTITLE%", note.getTitle()
                    )
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;

            Player player = (Player) event.getWhoClicked();

            if (note.isRemoved()) {
                return;
            }

            NoteListener.noteRemoveMap.put(player.getName(), note);
            NoteListener.noteTargetRemoveMap.put(player.getName(), target);
            player.closeInventory();
            player.sendMessage(CC.translate("&aType the reason for removing this note. Type 'cancel' to stop this process."));
        }
    }

}
