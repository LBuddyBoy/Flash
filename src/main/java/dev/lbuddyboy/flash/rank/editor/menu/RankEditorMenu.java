package dev.lbuddyboy.flash.rank.editor.menu;

import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.rank.editor.EditorType;
import dev.lbuddyboy.flash.rank.editor.RankEdit;
import dev.lbuddyboy.flash.rank.editor.listener.RankEditorListener;
import dev.lbuddyboy.flash.rank.menu.RankListMenu;
import dev.lbuddyboy.flash.util.*;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.ColorUtil;
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.Menu;
import dev.lbuddyboy.flash.util.menu.button.BackButton;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class RankEditorMenu extends Menu {

    public Rank rank;

    @Override
    public String getTitle(Player player) {
        return "Editing: " + rank.getName();
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        buttons.add(new BackButton(1, new RankListMenu((p, r) -> {
            p.closeInventory();
            new RankEditorMenu(r).openMenu(p);
        })));

        buttons.add(new RankDisplayButton(rank));
        buttons.add(new UsersButton(rank));

        for (EditorType type : EditorType.values()) {
            buttons.add(new RankActionButton(type.getSlot(), rank, type.getButton(), type, type.getStack()));
        }

        return buttons;
    }

    @Override
    public int getSize(Player player) {
        return 54;
    }

    @Override
    public boolean autoFill() {
        return true;
    }

    @AllArgsConstructor
    private static class RankActionButton extends Button {

        public int slot;
        public Rank rank;
        public Callback<Player, Rank> callback;
        public EditorType type;
        public ItemStack stack;

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
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            callback.call(player, rank);
            if (type == EditorType.PERMISSIONS || type == EditorType.INHERITANCE) return;
            RankEditorListener.rankEditorMap.put(player, new RankEdit(rank, type));
        }
    }

    @AllArgsConstructor
    private static class RankDisplayButton extends Button {

        public Rank rank;

        @Override
        public int getSlot() {
            return 13;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.WOOL)
                    .setName(rank.getColoredName())
                    .setDurability(ColorUtil.COLOR_MAP.get(rank.getColor()).getWoolData())
                    .setLore(Arrays.asList(
                            CC.MENU_BAR,
                            "&cDisplay Name&7: &f" + rank.getDisplayName(),
                            "&cWeight&7: &f" + rank.getWeight(),
                            "&cColor&7: &f" + rank.getColor().name(),
                            "&cPrefix&7: &f" + rank.getPrefix(),
                            "&cSuffix&7: &f" + rank.getSuffix(),
                            "&cDefault Rank&7: &f" + (rank.isDefaultRank() ? "&a&l✓" : "&c&l✕"),
                            CC.MENU_BAR
                    ))
                    .create();
        }
    }

    @AllArgsConstructor
    private static class UsersButton extends Button {

        public Rank rank;

        @Override
        public int getSlot() {
            return 15;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(CompatibleMaterial.getMaterial("SKULL_ITEM"))
                    .setName("&eUsers")
                    .setLore(Arrays.asList(
                            CC.MENU_BAR,
                            "&7Click to view all users with this rank.",
                            CC.MENU_BAR
                    ))
                    .setDurability(3)
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            new RankUsersMenu(rank).openMenu(player);
        }
    }

}
