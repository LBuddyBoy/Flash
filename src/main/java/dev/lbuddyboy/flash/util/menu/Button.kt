package dev.lbuddyboy.flash.util.menu;

import dev.lbuddyboy.flash.util.Callable;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Button {

    public abstract int getSlot();
    public abstract ItemStack getItem();
    public void action(InventoryClickEvent event) {

    }
    public boolean clickUpdate() {
        return false;
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

    public static Button fromItem(ItemStack stack, int slot) {
        return new Button() {
            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public ItemStack getItem() {
                return stack;
            }

        };
    }

    public static Button fromItem(ItemStack stack, int slot, Callable callable) {
        return new Button() {
            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public ItemStack getItem() {
                return stack;
            }

            @Override
            public void action(InventoryClickEvent event) {
                callable.call();
            }
        };
    }

}
