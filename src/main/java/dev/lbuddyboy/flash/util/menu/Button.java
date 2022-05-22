package dev.lbuddyboy.flash.util.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Button {

    public abstract int getSlot();
    public abstract ItemStack getItem();
    public void action(InventoryClickEvent event) {

    }

    public boolean cancels() {
        return true;
    }

    public static Button fromButton(int slot, Button old) {
        return new Button() {
            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public ItemStack getItem() {
                return old.getItem();
            }

            @Override
            public void action(InventoryClickEvent event) {
                old.action(event);
            }
        };
    }

}
