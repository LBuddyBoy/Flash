package dev.lbuddyboy.flash.rank.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.util.Callback;
import dev.lbuddyboy.flash.util.bukkit.ColorUtil;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RankListMenu extends PagedMenu<Rank> {

    public Callback<Player, Rank> callback;

    public RankListMenu(Callback<Player, Rank> callback) {
        this.callback = callback;
        this.objects = Flash.getInstance().getRankHandler().getSortedRanks();
    }

    @Override
    public String getPageTitle(Player player) {
        return "Select a rank...";
    }

    @Override
    public List<Button> getPageButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        int slot = 0;
        for (Rank rank : this.objects) {
            buttons.add(new RankButton(slot, callback, rank));
        }

        return buttons;
    }

    @AllArgsConstructor
    private static class RankButton extends Button {

        public int slot;
        public Callback<Player, Rank> callback;
        public Rank rank;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.WOOL)
                    .setName(rank.getDisplayName())
                    .setDurability(ColorUtil.COLOR_MAP.get(rank.getColor()).getWoolData())
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            callback.call(player, rank);
        }
    }

}
