package dev.lbuddyboy.flash.user.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashMenuLanguage;
import dev.lbuddyboy.flash.user.listener.PunishmentListener;
import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.util.CC;
import dev.lbuddyboy.flash.util.ItemBuilder;
import dev.lbuddyboy.flash.util.UUIDUtils;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.button.BackButton;
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PunishmentHistoryMenu extends PagedMenu<Punishment> {

    public UUID target;
    public PunishmentType type;

    public PunishmentHistoryMenu(UUID target, PunishmentType type) {
        this.target = target;
        this.type = type;
        this.objects = Flash.getInstance().getUserHandler().tryUser(this.target, true).getSortedPunishmentsByType(type);
    }

    @Override
    public String getPageTitle(Player player) {
        return CC.translate(CC.applyTarget(FlashMenuLanguage.PUNISHMENTS_MENU_TITLE.getString(), target), "%PUNISHMENT_PLURAL%", type.getPlural());
    }

    @Override
    public List<Button> getPageButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        int i = 0;
        for (Punishment punishment : this.objects) {
            if (punishment.getType() != type) continue;
            buttons.add(new PunishmentButton(i++, punishment));
        }

        return buttons;
    }

    @Override
    public boolean autoFill() {
        return true;
    }

    @Override
    public List<Button> getGlobalButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        buttons.add(new BackButton(5, new PunishmentHistorySelectionMenu(target)));

        return buttons;
    }

    @Override
    public boolean autoUpdate() {
        return true;
    }

    @AllArgsConstructor
    private static class PunishmentButton extends Button {

        public int slot;
        public Punishment punishment;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            if (punishment.isRemoved()) {
                return new ItemBuilder(FlashMenuLanguage.PUNISHMENTS_MENU_REMOVED_PUNISHMENT_BUTTON_MATERIAL.getMaterial())
                        .setLore(FlashMenuLanguage.PUNISHMENTS_MENU_REMOVED_PUNISHMENT_BUTTON_LORE.getStringList(),
                                "%ADDEDBY%", UUIDUtils.formattedName(punishment.getSentBy()),
                                "%ADDEDFOR%", punishment.getSentFor(),
                                "%ADDEDAT%", punishment.getAddedAtDate(),
                                "%TIMELEFT%", punishment.getExpireString(),
                                "%SERVER%", punishment.getServer(),
                                "%REMOVEDBY%", UUIDUtils.formattedName(punishment.getRemovedBy()),
                                "%REMOVEDFOR%", punishment.getRemovedFor(),
                                "%REMOVEDAT%", punishment.getRemovedAtDate()
                        )
                        .setName(FlashMenuLanguage.PUNISHMENTS_MENU_REMOVED_PUNISHMENT_BUTTON_NAME.getString(),
                                "%ADDEDBY%", UUIDUtils.formattedName(punishment.getSentBy()),
                                "%ADDEDFOR%", punishment.getSentFor(),
                                "%ADDEDAT%", punishment.getAddedAtDate(),
                                "%TIMELEFT%", punishment.getExpireString(),
                                "%SERVER%", punishment.getServer(),
                                "%REMOVEDBY%", UUIDUtils.formattedName(punishment.getRemovedBy()),
                                "%REMOVEDFOR%", punishment.getRemovedFor(),
                                "%REMOVEDAT%", punishment.getRemovedAtDate())
                        .setDurability(FlashMenuLanguage.PUNISHMENTS_MENU_REMOVED_PUNISHMENT_BUTTON_DATA.getInt())
                        .create();
            }
            return new ItemBuilder(FlashMenuLanguage.PUNISHMENTS_MENU_PUNISHMENT_BUTTON_MATERIAL.getMaterial())
                    .setName(FlashMenuLanguage.PUNISHMENTS_MENU_PUNISHMENT_BUTTON_NAME.getString(),
                            "%ADDEDBY%", UUIDUtils.formattedName(punishment.getSentBy()),
                            "%SERVER%", punishment.getServer(),
                            "%ADDEDFOR%", punishment.getSentFor(),
                            "%ADDEDAT%", punishment.getAddedAtDate(),
                            "%TIMELEFT%", punishment.getExpireString())
                    .setLore(FlashMenuLanguage.PUNISHMENTS_MENU_PUNISHMENT_BUTTON_LORE.getStringList(),
                            "%ADDEDBY%", UUIDUtils.formattedName(punishment.getSentBy()),
                            "%SERVER%", punishment.getServer(),
                            "%ADDEDFOR%", punishment.getSentFor(),
                            "%ADDEDAT%", punishment.getAddedAtDate(),
                            "%TIMELEFT%", punishment.getExpireString()
                    )
                    .setDurability(FlashMenuLanguage.PUNISHMENTS_MENU_PUNISHMENT_BUTTON_DATA.getInt())
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            player.closeInventory();
            PunishmentListener.punishmentRemovePermMap.put(player.getName(), punishment);
            player.sendMessage(CC.translate("&aType the reason for resolving this punishment."));
        }
    }

}
