package dev.lbuddyboy.flash.user.punishment.menu;

import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.util.ColorUtil;
import dev.lbuddyboy.flash.util.ItemBuilder;
import dev.lbuddyboy.flash.util.UUIDUtils;
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
public class PunishmentHistorySelectionMenu extends Menu {

    public UUID target;

    @Override
    public String getTitle(Player player) {
        return "Punishment Selection";
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
                    .setName(type.getDisplay())
                    .setLore("&7Click to view the " + type.getPlural() + "&7 of " + UUIDUtils.formattedName(target) + "&7.")
                    .setDurability(ColorUtil.COLOR_MAP.get(type.getColor()).getWoolData())
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            new PunishmentHistoryMenu(target, type).openMenu(player);
        }
    }

}
