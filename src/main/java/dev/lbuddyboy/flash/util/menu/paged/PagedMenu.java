package dev.lbuddyboy.flash.util.menu.paged;

import dev.lbuddyboy.flash.util.CC;
import dev.lbuddyboy.flash.util.ItemBuilder;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.Menu;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class PagedMenu<T> extends Menu {

    public abstract String getPageTitle(Player player);
    public abstract List<Button> getPageButtons(Player player);
    public List<Button> getGlobalButtons(Player player) {
        return new ArrayList<>();
    }

    public List<T> objects;

    private static final int[] ITEM_SLOTS = {
            12, 13, 14, 15, 16,
            21, 22, 23, 24, 25,
            30, 31, 32, 33, 34
    };

    private int page = 1;

    @Override
    public String getTitle(Player player) {

        String title = getPageTitle(player) + " (" + page + "/" + getMaxPages(objects) + ")";

        return title.substring(0, 15);
    }

    @Override
    public List<Button> getButtons(Player player) {

        List<Button> buttons = new ArrayList<>();

        IntRange range;
        if (page == 1) {
            range = new IntRange(1, ITEM_SLOTS.length);
        } else {
            range = new IntRange(((page - 1) * 15) + 1, page * ITEM_SLOTS.length);
        }

        int skipped = 1;
        int slotIndex = 0;

        for (Button button : getPageButtons(player)) {
            if (skipped < range.getMinimumInteger()) {
                skipped++;
                continue;
            }

            buttons.add(Button.fromButton(ITEM_SLOTS[slotIndex], button));
            if (slotIndex >= 14) {
                break;
            } else {
                slotIndex++;
            }
        }

        buttons.addAll(getGlobalButtons(player));

        buttons.add(new PreviousPageButton(20, objects));
        buttons.add(new NextPageButton(26, objects));

        return buttons;
    }

    @Override
    public int getSize(Player player) {
        return 45;
    }

    @AllArgsConstructor
    private class PreviousPageButton extends Button {

        public int slot;
        public List<T> objects;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {

            Material material = page > 1 ? Material.REDSTONE_TORCH_ON : Material.LEVER;
            String name = page > 1 ? "&c&lPrevious Page" : "&c&lNo Previous Page";

            return new ItemBuilder(material).setName(name).create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (event.getClick().isLeftClick() && page > 1) {
                page -= 1;
                openMenu((Player) event.getWhoClicked());
            }
        }
    }

    @AllArgsConstructor
    private class NextPageButton extends Button {

        public int slot;
        public List<T> objects;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {

            Material material = page < getMaxPages(objects) ? Material.REDSTONE_TORCH_ON : Material.LEVER;
            String name = page < getMaxPages(objects) ? "&c&lNext Page" : "&c&lNo Next Page";

            return new ItemBuilder(material).setName(name).create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (event.getClick().isLeftClick() && page < getMaxPages(objects)) {
                page += 1;
                openMenu((Player) event.getWhoClicked());
            }
        }
    }

    private int getMaxPages(List<T> objects) {
        if (objects.size() == 0) {
            return 1;
        } else {
            return (int) Math.ceil(objects.size() / (double) (ITEM_SLOTS.length));
        }
    }

}
