package dev.lbuddyboy.flash.util.menu.paged;

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
        return getPageTitle(player) + " (" + page + "/" + getMaxPages(objects) + ")";
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        IntRange range = new IntRange(((page - 1) * getMaxPageButtons(player) + 1) + 1, page * getButtonSlots().length);

        if (page == 1) range = new IntRange(1, getButtonSlots().length);

        int skipped = 1;
        int slotIndex = 0;

        for (Button button : getPageButtons(player)) {
            if (skipped < range.getMinimumInteger()) {
                skipped++;
                continue;
            }

            buttons.add(Button.fromButton(getButtonSlots()[slotIndex], button));
            if (slotIndex >= getMaxPageButtons(player)) {
                break;
            }
            slotIndex++;
        }

        buttons.addAll(getGlobalButtons(player));

        buttons.add(new PreviousPageButton<T>(this, 20));
        buttons.add(new NextPageButton<T>(this, 26));

        return buttons;
    }

    @Override
    public int getSize(Player player) {
        return 45;
    }

    public int[] getButtonSlots() {
        return DEFAULT_ITEM_SLOTS;
    }

    public int getMaxPageButtons(Player player) {
        return 14;
    }

    public int getMaxPages(List<T> objects) {
        if (objects.size() == 0) {
            return 1;
        } else {
            return (int) Math.ceil(objects.size() / (double) (getButtonSlots().length));
        }
    }

}
