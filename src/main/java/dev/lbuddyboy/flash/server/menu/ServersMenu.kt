package dev.lbuddyboy.flash.server.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.server.Server;
import dev.lbuddyboy.flash.server.listener.ServerListener;
import dev.lbuddyboy.flash.server.packet.ServerStopPacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServersMenu extends PagedMenu<Server> {

    public ServersMenu() {
        this.objects = new ArrayList<>(Flash.getInstance().getServerHandler().getServers().values());
    }

    @Override
    public String getPageTitle(Player player) {
        return "Server List";
    }

    @Override
    public List<Button> getPageButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        int i = 0;
        for (Server server : this.objects) {
            buttons.add(new ServerButton(i++, server));
        }

        return buttons;
    }

    @Override
    public boolean autoUpdate() {
        return true;
    }

    @AllArgsConstructor
    private static class ServerButton extends Button {

        public int slot;
        public Server server;

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
            return new ItemBuilder(server.isOffline() ? CompatibleMaterial.getMaterial("BARRIER") : CompatibleMaterial.getMaterial("BEACON"))
                    .setName("&g&l" + server.getName())
                    .setLore(Arrays.asList(
                            CC.MENU_BAR,
                            "&hStatus&7: &r" + server.status(),
                            "&hOnline&7: &f" + server.getOnline() + "/" + server.getMaxPlayers(),
                            "&hMOTD&7: &f" + server.getMotd(),
                            "",
                            "&7Left Click to shut down the server",
                            "&7Right Click to run a server specific command.",
                            CC.MENU_BAR
                    ))
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            if (event.getClick() == ClickType.LEFT) {
                new ServerStopPacket(this.server.getName()).send();
                return;
            }

            player.sendMessage(CC.translate("&aType the command you would like to run on that server."));
            player.closeInventory();
            ServerListener.serverCommandMap.put(player.getName(), server);
        }
    }

}
