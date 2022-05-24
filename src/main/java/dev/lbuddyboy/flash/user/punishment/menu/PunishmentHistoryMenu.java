package dev.lbuddyboy.flash.user.punishment.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.listener.PunishmentListener;
import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.user.packet.PunishmentSendPacket;
import dev.lbuddyboy.flash.user.packet.UserUpdatePacket;
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
        return UUIDUtils.formattedName(this.target) + "'s " + type.getPlural();
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
                return new ItemBuilder(Material.WOOL)
                        .setLore(Arrays.asList(
                                        CC.MENU_BAR,
                                        "&4&lAdded Information",
                                        CC.MENU_BAR,
                                        "&4Added By&7:&f %ADDEDBY%",
                                        "&4Added For&7:&f %ADDEDFOR%",
                                        "&4Added On&7:&f %SERVER%",
                                        CC.MENU_BAR,
                                        "&4&lRemoved Information",
                                        CC.MENU_BAR,
                                        "&4Removed By&7:&f %REMOVEDBY%",
                                        "&4Removed For&7:&f %REMOVEDFOR%",
                                        "&4Removed At&7: &f%REMOVEDAT%",
                                        CC.MENU_BAR
                                ),
                                "%ADDEDBY%", UUIDUtils.formattedName(punishment.getSentBy()),
                                "%ADDEDFOR%", punishment.getSentFor(),
                                "%ADDEDAT%", punishment.getAddedAtDate(),
                                "%TIMELEFT%", punishment.getExpireString(),
                                "%SERVER%", punishment.getServer(),
                                "%REMOVEDBY%", UUIDUtils.formattedName(punishment.getRemovedBy()),
                                "%REMOVEDFOR%", punishment.getRemovedFor(),
                                "%REMOVEDAT%", punishment.getRemovedAt()
                        )
                        .setName("&4" + punishment.getAddedAtDate())
                        .setDurability(punishment.isRemoved() ? 14 : 5)
                        .create();
            }
            return new ItemBuilder(Material.WOOL)
                    .setName("&4" + punishment.getAddedAtDate())
                    .setLore(Arrays.asList(
                                    CC.MENU_BAR,
                                    "&4Added By&7:&f %ADDEDBY%",
                                    "&4Added For&7:&f %ADDEDFOR%",
                                    "&4Added On&7:&f %SERVER%",
                                    "&4Time Left&7: &f%TIMELEFT%",
                                    "",
                                    "&fClick to remove this punishment.",
                                    CC.MENU_BAR
                            ),
                            "%ADDEDBY%", UUIDUtils.formattedName(punishment.getSentBy()),
                            "%SERVER%", punishment.getServer(),
                            "%ADDEDFOR%", punishment.getSentFor(),
                            "%ADDEDAT%", punishment.getAddedAtDate(),
                            "%TIMELEFT%", punishment.getExpireString()
                    )
                    .setDurability(punishment.isRemoved() ? 14 : 5)
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
