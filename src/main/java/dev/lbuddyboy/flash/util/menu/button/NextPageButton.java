package dev.lbuddyboy.flash.util.menu.button;

import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class NextPageButton<T> extends Button {

    public PagedMenu<T> menu;
    public int slot;

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public ItemStack getItem() {

        Material material = menu.page < menu.getMaxPages() ? Material.REDSTONE_TORCH_ON : Material.LEVER;
        String name = menu.page < menu.getMaxPages() ? "&c&lNext Page" : "&c&lNo Next Page";

        return new ItemBuilder(material).setName(name).create();
    }

    @Override
    public void action(InventoryClickEvent event) {
        if (event.getClick().isLeftClick() && menu.page < menu.getMaxPages()) {
            menu.page += 1;
            menu.openMenu((Player) event.getWhoClicked());
        }
    }
}
