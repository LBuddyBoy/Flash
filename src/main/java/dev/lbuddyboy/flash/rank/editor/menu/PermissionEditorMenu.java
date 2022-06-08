package dev.lbuddyboy.flash.rank.editor.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.rank.editor.listener.RankEditorListener;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.button.BackButton;
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PermissionEditorMenu extends PagedMenu<String> {

    public Rank rank;

    public PermissionEditorMenu(Rank rank) {
        this.rank = rank;
        this.objects = rank.getPermissions();
    }

    @Override
    public String getPageTitle(Player player) {
        return "Permission Editor";
    }

    @Override
    public List<Button> getPageButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        int i = 0;
        for (String permission : objects) {
            buttons.add(new PermissionButton(i++, rank, permission));
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
        buttons.add(new AddPermissionButton(6, rank));

        return buttons;
    }

    @AllArgsConstructor
    private static class PermissionButton extends Button {

        public int slot;
        public Rank rank;
        public String permission;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.PAPER)
                    .setName("&c" + permission)
                    .setLore(Arrays.asList(
                            CC.MENU_BAR,
                            "&fClick to remove this permission from the " + rank.getColoredName() + "&f rank.",
                            CC.MENU_BAR
                    ))
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            rank.getPermissions().remove(permission);
            rank.save(true);

            player.sendMessage(CC.translate("&cRemoved the " + permission + " permission from the " + rank.getColoredName() + "&c rank."));
        }
    }

    private static class PermissionAddMenu extends PagedMenu<String> {

        public Rank rank;

        public PermissionAddMenu(Rank rank) {
            this.rank = rank;
            this.objects = Flash.getInstance().getCommandHandler().getKnownPermissionsMap().keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        }

        @Override
        public String getPageTitle(Player player) {
            return "Add Permission";
        }

        @Override
        public int[] getButtonSlots() {
            return new int[] {
                    11,12,13,14,15,16,17,
                    20,21,22,23,24,25,26,
                    29,30,31,32,33,34,35,
                    38,39,40,41,42,43,44

            };
        }

        @Override
        public int getSize(Player player) {
            return 54;
        }

        @Override
        public boolean autoFill() {
            return true;
        }

        @Override
        public boolean autoUpdate() {
            return true;
        }

        @Override
        public int getMaxPageButtons(Player player) {
            return getButtonSlots().length;
        }

        @Override
        public int getPreviousButtonSlot() {
            return 1;
        }

        @Override
        public int getNextPageButtonSlot() {
            return 9;
        }

        @Override
        public List<Button> getPageButtons(Player player) {
            List<Button> buttons = new ArrayList<>();

            int i = 0;
            for (String permission : this.objects) {
                if (rank.getPermissions().contains(permission)) continue;

                buttons.add(new PermissionAddButton(i++, permission, rank));
            }

            return buttons;
        }
    }

    @AllArgsConstructor
    private static class PermissionAddButton extends Button {

        public int slot;
        public String permission;
        public Rank rank;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.PAPER)
                    .setName("&c" + permission)
                    .setLore("&fClick to add the &a" + permission + "&f to the " + rank.getColoredName() + "&f rank.")
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            rank.getPermissions().add(permission);
            rank.save(true);
        }
    }

    @AllArgsConstructor
    private static class AddPermissionButton extends Button {

        public int slot;
        public Rank rank;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.WOOL)
                    .setName("&aAdd a Permission")
                    .setLore(Arrays.asList(
                            CC.MENU_BAR,
                            "&fLeft click to add a custom permission to the " + rank.getColoredName() + "&f rank.",
                            "&fRight click to view all the permissions editor.",
                            CC.MENU_BAR
                    ))
                    .setDurability(5)
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            if (event.getClick() == ClickType.RIGHT) {
                player.closeInventory();
                new PermissionAddMenu(rank).openMenu(player);
                return;
            }

            player.closeInventory();
            RankEditorListener.rankPermissionEditMap.put(player, rank);

            player.sendMessage(CC.translate("&aType the permission you would like to add to the " + rank.getColoredName() + "&a rank."));
        }
    }

}
