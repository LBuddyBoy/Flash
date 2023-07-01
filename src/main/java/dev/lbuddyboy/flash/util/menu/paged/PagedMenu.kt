package dev.lbuddyboy.flash.util.menu.paged;

import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.Menu;
import dev.lbuddyboy.flash.util.menu.button.NextPageButton;
import dev.lbuddyboy.flash.util.menu.button.PreviousPageButton;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class PagedMenu<T> extends Menu {

    private final int[] DEFAULT_ITEM_SLOTS = {
            12, 13, 14, 15, 16,
            21, 22, 23, 24, 25,
            30, 31, 32, 33, 34
    };

    public List<T> objects;
    public int page = 1;

    public abstract String getPageTitle(Player player);
    public abstract List<Button> getPageButtons(Player player);
    public List<Button> getGlobalButtons(Player player) {
        return new ArrayList<>();
    }

    @Override
    public String getTitle(Player player) {
        return getPageTitle(player) + " (" + page + "/" + getMaxPages() + ")";
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        int index = 0;
        for (int i = (page * getMaxPageButtons()) - getMaxPageButtons(); i < (page * getMaxPageButtons()); i++) {
            try {
                if (objects.size() <= i) continue;

                buttons.add(Button.fromButton(getButtonSlots()[index++], getPageButtons(player).get(i)));
            } catch (Exception ignored) {

            }
        }

        buttons.addAll(getGlobalButtons(player));

        buttons.add(new PreviousPageButton<T>(this, getPreviousButtonSlot()));
        buttons.add(new NextPageButton<T>(this, getNextPageButtonSlot()));

        return buttons;
    }

    public int getPreviousButtonSlot() {
        return 20;
    }

    public int getNextPageButtonSlot() {
        return 26;
    }

    @Override
    public int getSize(Player player) {
        return 45;
    }

    public int[] getButtonSlots() {
        return DEFAULT_ITEM_SLOTS;
    }

    public int getMaxPageButtons() {
        return DEFAULT_ITEM_SLOTS.length;
    }

    public int getMaxPages() {
        return (objects.size() / getMaxPageButtons()) + 1;
    }

}
