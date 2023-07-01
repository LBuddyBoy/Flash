package dev.lbuddyboy.flash.server.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.server.model.Notification;
import dev.lbuddyboy.flash.server.packet.NotificationsUpdatePacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NotificationsMenu extends PagedMenu<Notification> {

    public String filter;
    public User target;

    public NotificationsMenu(String filter, User target) {
        this.filter = filter;
        this.target = target;

        objects = filter.equals("all") ? Flash.getInstance().getServerHandler().getNotifications() : filter.equals("read") ? Flash.getInstance().getServerHandler().getReadNotifications(target) : Flash.getInstance().getServerHandler().getUnReadNotifications(target);
    }

    @Override
    public String getPageTitle(Player player) {
        return "Notifications";
    }

    @Override
    public List<Button> getPageButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        int i = 0;
        for (Notification notification : this.objects) {
            buttons.add(new NotificationButton(i++, notification, player, this));
        }

        return buttons;
    }

    @Override
    public void update(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();

        for (int slot : getButtonSlots()) {
            inventory.setItem(slot - 1, null);
        }

        super.update(player);
    }

    @Override
    public List<Button> getGlobalButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        if (this.objects.isEmpty()) {
            buttons.add(new EmptyButton(23));
        }

        buttons.add(new Button() {
            @Override
            public int getSlot() {
                return 5;
            }

            @Override
            public boolean clickUpdate() {
                return true;
            }

            @Override
            public ItemStack getItem() {
                return new ItemBuilder(CompatibleMaterial.getMaterial("SIGN"))
                        .setName("&eChange Filter")
                        .setLore(Arrays.asList(
                                CC.MENU_BAR,
                                (filter.equals("all") ? "&a&l✓" : "&c&l✕") + " &7All Notifications",
                                (filter.equals("read") ? "&a&l✓" : "&c&l✕") + " &7All Read Notifications",
                                (filter.equals("unread") ? "&a&l✓" : "&c&l✕") + " &7All Unread Notifications",
                                "",
                                "&7Click to scroll thru the filters",
                                CC.MENU_BAR
                        ))
                        .create();
            }

            @Override
            public void action(InventoryClickEvent event) {
                if (!(event.getWhoClicked() instanceof Player)) return;
                Player player = (Player) event.getWhoClicked();
                User user = Flash.getInstance().getUserHandler().tryUser(player.getUniqueId(), false);

                filter = filter.equals("all") ? "read" : filter.equals("read") ? "unread" : "all";
                objects = filter.equals("all") ? Flash.getInstance().getServerHandler().getNotifications() : filter.equals("read") ? Flash.getInstance().getServerHandler().getReadNotifications(user) : Flash.getInstance().getServerHandler().getUnReadNotifications(user);

                update(player);
            }
        });

        return buttons;
    }

    @AllArgsConstructor
    private static class EmptyButton extends Button {

        public int slot;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(CompatibleMaterial.getMaterial("BARRIER")).setName("&cCouldn't find any notifications...").create();
        }

    }

    @AllArgsConstructor
    private static class NotificationButton extends Button {

        public int slot;
        public Notification notification;
        public Player player;
        public NotificationsMenu menu;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public boolean clickUpdate() {
            return true;
        }

        @Override
        public ItemStack getItem() {
            User user = Flash.getInstance().getUserHandler().tryUser(player.getUniqueId(), false);

            if (user.getPlayerInfo().getReadNotifications().contains(notification.getId())) {
                return new ItemBuilder(Material.BOOK_AND_QUILL)
                        .setName(CC.DARK_RED + notification.getTitle())
                        .setLore(Arrays.asList(
                                CC.MENU_BAR,
                                "&gMessage&7: &f" + notification.getMessage(),
                                "&gSent At&7: &f" + notification.getSentAtDate(),
                                CC.MENU_BAR
                        ))
                        .create();
            }
            return new ItemBuilder(Material.BOOK_AND_QUILL)
                    .setName(CC.DARK_RED + notification.getTitle())
                    .setLore(Arrays.asList(
                            CC.MENU_BAR,
                            "&gMessage&7: &f" + notification.getMessage(),
                            "&gSent At&7: &f" + notification.getSentAtDate(),
                            "",
                            "&7Click to mark this notification as read.",
                            CC.MENU_BAR
                    ))
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();
            User user = Flash.getInstance().getUserHandler().tryUser(player.getUniqueId(), false);

            if (user.getPlayerInfo().getReadNotifications().contains(notification.getId())) {
                player.sendMessage(CC.translate("&aYou already have that notification marked as read."));
                return;
            }

            user.getPlayerInfo().getReadNotifications().add(notification.getId());
            user.save(true);
            new NotificationsUpdatePacket(Flash.getInstance().getServerHandler().getNotifications()).send();
            player.sendMessage(CC.translate("&aYou have marked that notification as read."));

            menu.update(player);
            menu.objects = menu.filter.equals("all") ? Flash.getInstance().getServerHandler().getNotifications() : menu.filter.equals("read") ? Flash.getInstance().getServerHandler().getReadNotifications(user) : Flash.getInstance().getServerHandler().getUnReadNotifications(user);

        }
    }

}
