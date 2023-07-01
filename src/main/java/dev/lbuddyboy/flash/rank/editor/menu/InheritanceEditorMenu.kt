package dev.lbuddyboy.flash.rank.editor.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.rank.editor.listener.RankEditorListener;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.ColorUtil;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
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

public class InheritanceEditorMenu extends PagedMenu<String> {

    public Rank rank;

    public InheritanceEditorMenu(Rank rank) {
        this.rank = rank;
        this.objects = rank.getInheritance();
    }

    @Override
    public String getPageTitle(Player player) {
        return "Inheritance Editor";
    }

    @Override
    public List<Button> getPageButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        int i = 0;
        for (String rankName : objects) {
            Rank inheritance = Flash.getInstance().getRankHandler().getRank(rankName);
            if (inheritance == null) continue;
            buttons.add(new InheritanceButton(i++, rank, inheritance));
        }

        return buttons;
    }

    @Override
    public boolean autoUpdate() {
        return true;
    }

    @Override
    public List<Button> getGlobalButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        buttons.add(new BackButton(4, new RankEditorMenu(rank)));
        buttons.add(new AddInheritanceButton(6, rank));

        return buttons;
    }

    @AllArgsConstructor
    private static class InheritanceButton extends Button {

        public int slot;
        public Rank rank, inherited;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.WOOL)
                    .setName(inherited.getColoredName())
                    .setLore(Arrays.asList(
                            CC.MENU_BAR,
                            "&fClick to remove this inheritance from the " + rank.getColoredName() + "&f rank.",
                            CC.MENU_BAR
                    ))
                    .setDurability(ColorUtil.COLOR_MAP.get(inherited.getColor()).getWoolData())
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            rank.getInheritance().remove(inherited.getName());
            rank.save(true);

            player.sendMessage(CC.translate("&cRemoved the " + inherited.getColoredName() + "&c inheritance from the " + rank.getColoredName() + "&c rank."));
        }
    }

    @AllArgsConstructor
    private static class AddInheritanceButton extends Button {

        public int slot;
        public Rank rank;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.WOOL)
                    .setName("&aAdd an Inheritance")
                    .setLore(Arrays.asList(
                            CC.MENU_BAR,
                            "&fClick to add a new inheritance to the " + rank.getColoredName() + "&f rank.",
                            CC.MENU_BAR
                    ))
                    .setDurability(5)
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            player.closeInventory();
            RankEditorListener.rankInheritanceEditMap.put(player, rank);

            player.sendMessage(CC.translate("&aType the rank you would like the " + rank.getColoredName() + "&a rank to inherit."));
        }
    }

}
