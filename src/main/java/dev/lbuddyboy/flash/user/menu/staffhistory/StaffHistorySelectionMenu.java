package dev.lbuddyboy.flash.user.menu.staffhistory;

import dev.lbuddyboy.flash.FlashMenuLanguage;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.ColorUtil;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import dev.lbuddyboy.flash.util.bukkit.UserUtils;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class StaffHistorySelectionMenu extends Menu {

    public UUID target;

    @Override
    public String getTitle(Player player) {
        return "Staff History: " + UserUtils.formattedName(target);
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        buttons.add(new PunishmentSelectButton(1, PunishmentType.KICK, target));
        buttons.add(new PunishmentSelectButton(4, PunishmentType.MUTE, target));
        buttons.add(new PunishmentSelectButton(5, PunishmentType.BAN, target));
        buttons.add(new PunishmentSelectButton(6, PunishmentType.IP_BAN, target));
        buttons.add(new PunishmentSelectButton(9, PunishmentType.BLACKLIST, target));

        return buttons;
    }

    @Override
    public boolean autoFill() {
        return true;
    }

    @AllArgsConstructor
    private static class PunishmentSelectButton extends Button {

        public int slot;
        public PunishmentType type;
        public UUID target;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.WOOL)
                    .setName(CC.applyTarget(FlashMenuLanguage.PUNISHMENTS_SELECTION_SELECT_BUTTON_NAME.getString(), target), "%PUNISHMENT_TYPE%", type.getDisplay())
                    .setLore(CC.applyTarget(FlashMenuLanguage.PUNISHMENTS_SELECTION_SELECT_BUTTON_LORE.getStringList(), target), "%PUNISHMENT_PLURAL%", type.getPlural())
                    .setDurability(ColorUtil.COLOR_MAP.get(type.getColor()).getWoolData())
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            new StaffHistoryMenu(target, type).openMenu(player);
        }
    }

}
