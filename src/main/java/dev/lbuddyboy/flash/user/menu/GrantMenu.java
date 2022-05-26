package dev.lbuddyboy.flash.user.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashMenuLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.listener.GrantListener;
import dev.lbuddyboy.flash.user.model.GrantBuild;
import dev.lbuddyboy.flash.user.model.PermissionBuild;
import dev.lbuddyboy.flash.util.CC;
import dev.lbuddyboy.flash.util.ColorUtil;
import dev.lbuddyboy.flash.util.ItemBuilder;
import dev.lbuddyboy.flash.util.UUIDUtils;
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
    public boolean autoFill() {
        return FlashMenuLanguage.GRANT_MENU_FILL.getBoolean();
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
            new RanksMenu(uuid).openMenu((Player) event.getWhoClicked());
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
            if (!(event.getWhoClicked() instanceof Player)) return;

            Player player = (Player) event.getWhoClicked();

            player.closeInventory();
            GrantListener.grantPermissionTargetMap.put(player.getName(), new PermissionBuild(uuid, null, null, null));
            player.sendMessage(CC.translate("&aNow, type the permission node you would like to grant to " + UUIDUtils.formattedName(uuid) + "&a."));
        }

    }

    private static class RanksMenu extends PagedMenu<Rank> {

        public UUID uuid;

        public RanksMenu(UUID uuid) {
            this.uuid = uuid;
            this.objects = Flash.getInstance().getRankHandler().getSortedRanks();
        }

        @Override
        public String getPageTitle(Player player) {
            return CC.translate(FlashMenuLanguage.GRANT_MENU_RANK_MENU_TITLE.getString());
        }

        @Override
        public List<Button> getPageButtons(Player player) {
            List<Button> buttons = new ArrayList<>();

            int i = 1;
            for (Rank rank : this.objects) {
                buttons.add(new RankButton(rank, i++, uuid));
            }

            return buttons;
        }

        @Override
        public List<Button> getGlobalButtons(Player player) {
            List<Button> buttons = new ArrayList<>();

            buttons.add(new BackButton(5, new GrantMenu(uuid)));

            return buttons;
        }

        @Override
        public boolean autoFill() {
            return true;
        }
    }

    @AllArgsConstructor
    private static class RankButton extends Button {

        public Rank rank;
        public int slot;
        public UUID target;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.WOOL)
                    .setDurability(ColorUtil.COLOR_MAP.get(rank.getColor()).getWoolData())
                    .setName(CC.applyRank(CC.applyPlayer(FlashMenuLanguage.GRANT_MENU_RANK_MENU_RANK_BUTTON_NAME.getString(), target), rank))
                    .setLore(CC.applyRank(CC.applyPlayer(FlashMenuLanguage.GRANT_MENU_RANK_MENU_RANK_BUTTON_LORE.getStringList(), target), rank))
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;

            Player player = (Player) event.getWhoClicked();

            player.closeInventory();
            GrantListener.grantTargetMap.put(player.getName(), new GrantBuild(target, rank.getUuid(), null, null, null));
            player.sendMessage(CC.translate("&aNow, type the reason for granting the " + rank.getColoredName() + " &arank to " + UUIDUtils.formattedName(target) + "&a."));
        }
    }

}
