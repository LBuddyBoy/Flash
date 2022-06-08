package dev.lbuddyboy.flash.user.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Prefix;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrefixesMenu extends PagedMenu<Prefix> {

    public PrefixesMenu() {
        this.objects = new ArrayList<>(Flash.getInstance().getUserHandler().getPrefixes());
    }

    @Override
    public String getPageTitle(Player player) {
        return "All Prefixes";
    }

    @Override
    public List<Button> getPageButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        int i = 0;
        for (Prefix prefix : this.objects) {
            buttons.add(new PrefixButton(i++, prefix, player));
        }

        return buttons;
    }

    @Override
    public List<Button> getGlobalButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        buttons.add(new ResetPrefixButton(5));

        return buttons;
    }

    @Override
    public boolean autoFill() {
        return true;
    }

    @AllArgsConstructor
    private static class ResetPrefixButton extends Button {

        public int slot;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(CompatibleMaterial.getMaterial("BARRIER")).setName("&cReset Prefix &7(Click)").create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            User user = Flash.getInstance().getUserHandler().tryUser(player.getUniqueId(), false);
            if (user.getActivePrefix() == null) return;

            user.setActivePrefix(null);
            user.save(true);

            player.sendMessage(CC.translate("&aSuccessfully reset your prefix."));
        }
    }

    @AllArgsConstructor
    private static class PrefixButton extends Button {

        public int slot;
        public Prefix prefix;
        public Player player;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            User user = Flash.getInstance().getUserHandler().tryUser(player.getUniqueId(), false);

            String userPrefix = user.getActiveRank().getPrefix();
            String userSuffix = user.getActiveRank().getSuffix();

            return new ItemBuilder(Material.PAPER).setName("&6" + prefix.getId() + " &ePrefix")
                    .setLore(Arrays.asList(
                            CC.MENU_BAR,
                            "&eExample&7: &f" + userPrefix + prefix.getDisplay() + user.getColoredName() + userSuffix,
                            " ",
                            "&7Click to &aactivate the " + prefix.getId() + "&e Prefix&7.",
                            CC.MENU_BAR
                    ))
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!player.hasPermission("flash.prefix." + prefix.getId())) {
                player.sendMessage(CC.translate("&cNo permission."));
                return;
            }
            User user = Flash.getInstance().getUserHandler().tryUser(player.getUniqueId(), false);
            if (user.getActivePrefix() == prefix) return;

            user.setActivePrefix(prefix);
            user.save(true);

            player.sendMessage(CC.translate("&aSuccessfully applied the " + prefix.getId() + " prefix."));
        }
    }

}
