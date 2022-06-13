package dev.lbuddyboy.flash.server.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.server.listener.NotificationListener;
import dev.lbuddyboy.flash.server.model.Notification;
import dev.lbuddyboy.flash.server.packet.NotificationsUpdatePacket;
import dev.lbuddyboy.flash.user.listener.NoteListener;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NotificationsEditorMenu extends PagedMenu<Notification> {

    public UUID target;

    public NotificationsEditorMenu(UUID target) {
        this.target = target;

        this.objects = Flash.getInstance().getServerHandler().getNotifications();
    }

    @Override
    public String getPageTitle(Player player) {
        return "Notifications Editor";
    }

    @Override
    public boolean autoUpdate() {
        return true;
    }

    @Override
    public List<Button> getPageButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        int i = 0;
        for (Notification notification : this.objects) {
            buttons.add(new NotificationButton(i++, notification));
        }

        return buttons;
    }

    @Override
    public List<Button> getGlobalButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        if (this.objects.isEmpty()) {
            buttons.add(new EmptyButton(23));
        }

        buttons.add(new AddNotificationButton());

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
            return new ItemBuilder(Material.BOOK_AND_QUILL)
                    .setName(CC.DARK_RED + notification.getTitle())
                    .setLore(Arrays.asList(
                            CC.MENU_BAR,
                            "&4Message&7: &f" + notification.getMessage(),
                            "&4Sent At&7: &f" + notification.getSentAtDate(),
                            "",
                            "&7Click to &cdelete&7 this notification.",
                            CC.MENU_BAR
                    ))
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            notification.delete();
            new NotificationsUpdatePacket(Flash.getInstance().getServerHandler().getNotifications()).send();
            player.sendMessage(CC.translate("&aYou have marked that notification as read."));
        }
    }

    @AllArgsConstructor
    private static class AddNotificationButton extends Button {

        @Override
        public int getSlot() {
            return 5;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(CompatibleMaterial.getMaterial("STAINED_CLAY"))
                    .setDurability(DyeColor.GREEN.getWoolData())
                    .setName("&aCreate Notification &7(Click)")
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            player.closeInventory();
            NotificationListener.notificationsAdd.add(player.getName());

            player.sendMessage(CC.translate("&aType the title of the notification you would like to create."));
        }
    }

}
