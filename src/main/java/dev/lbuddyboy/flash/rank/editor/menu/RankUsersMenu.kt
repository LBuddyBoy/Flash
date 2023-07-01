package dev.lbuddyboy.flash.rank.editor.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.rank.editor.listener.RankEditorListener;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.menu.GrantsMenu;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.ColorUtil;
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.Menu;
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
import java.util.UUID;
import java.util.stream.Collectors;

public class RankUsersMenu extends PagedMenu<User> {

    public Rank rank;

    public RankUsersMenu(Rank rank) {
        this.rank = rank;
        this.objects = rank.getUsersWithRank();
    }

    @Override
    public String getPageTitle(Player player) {
        return "Inheritance Editor";
    }

    @Override
    public List<Button> getPageButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        int i = 0;
        for (User user : objects) {
            if (!user.getActiveRank().getUuid().toString().equals(this.rank.getUuid().toString())) continue;

            buttons.add(new UserButton(i++, user, this));
        }

        return buttons;
    }

    @Override
    public List<Button> getGlobalButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        buttons.add(new BackButton(5, new RankEditorMenu(rank)));

        return buttons;
    }

    @AllArgsConstructor
    private static class UserButton extends Button {

        public int slot;
        public User user;
        public Menu menu;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(CompatibleMaterial.getMaterial("SKULL_ITEM"))
                    .setName(user.getColoredName())
                    .setLore(Arrays.asList(
                            CC.MENU_BAR,
                            "&fClick to view the grants of " + user.getColoredName() + "&f.",
                            CC.MENU_BAR
                    ))
                    .setDurability(3)
                    .setOwner(user.getName())
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            new GrantsMenu(user.getUuid(), menu).openMenu(player);
        }
    }

}
