package dev.lbuddyboy.flash.user.grant.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashMenuLanguage;
import dev.lbuddyboy.flash.util.CC;
import dev.lbuddyboy.flash.util.ItemBuilder;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class GrantMenu extends Menu {

    public UUID uuid;

    @Override
    public String getTitle(Player player) {
        return CC.translate(FlashMenuLanguage.GRANT_MENU_TITLE.getString(), "%PLAYER%", Flash.getInstance().getCacheHandler().getUserCache().getName(this.uuid));
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        buttons.add(new GrantRankButton(uuid));
        buttons.add(new GrantPermissionButton(uuid));

        return buttons;
    }

    @Override
    public int getSize(Player player) {
        return FlashMenuLanguage.GRANT_MENU_SIZE.getInt();
    }

    @AllArgsConstructor
    private static class GrantRankButton extends Button {

        public UUID uuid;

        @Override
        public int getSlot() {
            return FlashMenuLanguage.GRANT_MENU_RANK_BUTTON_SLOT.getInt();
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(FlashMenuLanguage.GRANT_MENU_RANK_BUTTON_MATERIAL.getMaterial())
                    .setDurability(FlashMenuLanguage.GRANT_MENU_RANK_BUTTON_DATA.getInt())
                    .setName(FlashMenuLanguage.GRANT_MENU_RANK_BUTTON_NAME.getString())
                    .setLore(FlashMenuLanguage.GRANT_MENU_RANK_BUTTON_LORE.getStringList(), "%PLAYER%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid))
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            event.setCancelled(true);
        }
    }

    @AllArgsConstructor
    private static class GrantPermissionButton extends Button {

        public UUID uuid;

        @Override
        public int getSlot() {
            return FlashMenuLanguage.GRANT_MENU_PERMISSION_BUTTON_SLOT.getInt();
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(FlashMenuLanguage.GRANT_MENU_PERMISSION_BUTTON_MATERIAL.getMaterial())
                    .setDurability(FlashMenuLanguage.GRANT_MENU_PERMISSION_BUTTON_DATA.getInt())
                    .setName(FlashMenuLanguage.GRANT_MENU_PERMISSION_BUTTON_NAME.getString())
                    .setLore(FlashMenuLanguage.GRANT_MENU_PERMISSION_BUTTON_LORE.getStringList(), "%PLAYER%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid))
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            event.setCancelled(true);
        }
    }

}
