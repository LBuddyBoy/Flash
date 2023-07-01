package dev.lbuddyboy.flash.user.menu;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashMenuLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.listener.GrantListener;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.UserPermission;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import dev.lbuddyboy.flash.util.bukkit.UserUtils;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.Menu;
import dev.lbuddyboy.flash.util.menu.button.BackButton;
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GrantsMenu extends PagedMenu<Grant> {

    public UUID uuid;
    @Setter @Getter
    public String view = "ranks";
    public Menu previousMenu;

    public GrantsMenu(UUID uuid) {
        this.uuid = uuid;
        this.objects = Flash.getInstance().getUserHandler().tryUser(uuid, true).getGrants();
    }

    public GrantsMenu(UUID uuid, Menu previousMenu) {
        this.uuid = uuid;
        this.previousMenu = previousMenu;
        this.objects = Flash.getInstance().getUserHandler().tryUser(uuid, true).getGrants();
    }

    @Override
    public String getPageTitle(Player player) {
        return CC.translate(FlashMenuLanguage.GRANTS_MENU_TITLE.getString(),
                "%PLAYER%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid));
    }

    @Override
    public List<Button> getPageButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        int i = 1;
        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);
        if (view.equals("permissions")) {
            for (UserPermission permission : user.getSortedPermissions()) {
                buttons.add(new PermissionButton(uuid, permission, i++));
            }
        } else {
            for (Grant grant : user.getSortedGrants()) {
                buttons.add(new GrantButton(uuid, grant, i++));
            }
        }

        return buttons;
    }

    @Override
    public boolean autoUpdate() {
        return true;
    }

    @Override
    public boolean autoFill() {
        return true;
    }

    @Override
    public List<Button> getGlobalButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        buttons.add(new ToggleViewButton(this));
        if (this.previousMenu != null) buttons.add(new BackButton(6, this.previousMenu));

        return buttons;
    }

    @AllArgsConstructor
    private static class ToggleViewButton extends Button {

        public GrantsMenu menu;

        @Override
        public int getSlot() {
            if (menu.previousMenu != null) return 4;
            return menu.getView().equals("permissions") ? FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_RANKS_SLOT.getInt() : FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_SLOT.getInt();
        }

        @Override
        public ItemStack getItem() {
            String name = menu.getView().equals("permissions") ? FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_RANKS_NAME.getString() : FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_NAME.getString();
            List<String> lore = menu.getView().equals("permissions") ? FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_RANKS_LORE.getStringList() : FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_LORE.getStringList();
            Material material = menu.getView().equals("permissions") ? FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_RANKS_MATERIAL.getMaterial() : FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_MATERIAL.getMaterial();
            int data = menu.getView().equals("permissions") ? FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_RANKS_DATA.getInt() : FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_DATA.getInt();

            return new ItemBuilder(material).setName(name).setLore(lore).setDurability(data).create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            menu.setView(menu.getView().equals("permissions") ? "ranks" : "permissions");
            menu.update((Player) event.getWhoClicked());
        }
    }

    @AllArgsConstructor
    private static class GrantButton extends Button {

        public UUID target;
        public Grant grant;
        private int slot;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            if (grant.isRemoved()) {
                return new ItemBuilder(FlashMenuLanguage.GRANTS_MENU_REMOVED_GRANT_BUTTON_MATERIAL.getMaterial())
                        .setDurability(FlashMenuLanguage.GRANTS_MENU_REMOVED_GRANT_BUTTON_DATA.getInt())
                        .setName(FlashMenuLanguage.GRANTS_MENU_REMOVED_GRANT_BUTTON_NAME.getString(),
                                "%ADDEDAT%", grant.getAddedAtDate(),
                                "%ADDEDBY%", UserUtils.formattedName(grant.getAddedBy()),
                                "%ADDEDFOR%", grant.getAddedReason(),
                                "%TIMELEFT%", grant.getExpireString(),
                                "%SCOPES%", StringUtils.join(grant.getScopes(), ", "),
                                "%REMOVEDAT%", grant.getRemovedAtDate(),
                                "%REMOVEDBY%", UserUtils.formattedName(grant.getRemovedBy()),
                                "%REMOVEDFOR%", grant.getRemovedFor(),
                                "%RANK%", grant.getRank().getDisplayName())
                        .setLore(FlashMenuLanguage.GRANTS_MENU_REMOVED_GRANT_BUTTON_LORE.getStringList(),
                                "%ADDEDAT%", grant.getAddedAtDate(),
                                "%ADDEDBY%", UserUtils.formattedName(grant.getAddedBy()),
                                "%ADDEDFOR%", grant.getAddedReason(),
                                "%SCOPES%", StringUtils.join(grant.getScopes(), ", "),
                                "%TIMELEFT%", grant.getExpireString(),
                                "%REMOVEDAT%", grant.getRemovedAtDate(),
                                "%REMOVEDBY%", UserUtils.formattedName(grant.getRemovedBy()),
                                "%REMOVEDFOR%", grant.getRemovedFor(),
                                "%RANK%", grant.getRank().getDisplayName())
                        .create();
            }
            return new ItemBuilder(FlashMenuLanguage.GRANTS_MENU_DEFAULT_GRANT_BUTTON_MATERIAL.getMaterial())
                    .setDurability(FlashMenuLanguage.GRANTS_MENU_DEFAULT_GRANT_BUTTON_DATA.getInt())
                    .setName(FlashMenuLanguage.GRANTS_MENU_DEFAULT_GRANT_BUTTON_NAME.getString(),
                            "%ADDEDAT%", grant.getAddedAtDate(),
                            "%ADDEDBY%", UserUtils.formattedName(grant.getAddedBy()),
                            "%ADDEDFOR%", grant.getAddedReason(),
                            "%TIMELEFT%", grant.getExpireString(),
                            "%SCOPES%", StringUtils.join(grant.getScopes(), ", "),
                            "%RANK%", grant.getRank().getDisplayName())
                    .setLore(FlashMenuLanguage.GRANTS_MENU_DEFAULT_GRANT_BUTTON_LORE.getStringList(),
                            "%ADDEDAT%", grant.getAddedAtDate(),
                            "%ADDEDBY%", UserUtils.formattedName(grant.getAddedBy()),
                            "%ADDEDFOR%", grant.getAddedReason(),
                            "%SCOPES%", StringUtils.join(grant.getScopes(), ", "),
                            "%TIMELEFT%", grant.getExpireString(),
                            "%RANK%", grant.getRank().getDisplayName()
                    )
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;

            Player player = (Player) event.getWhoClicked();

            if (grant.isRemoved()) {
                return;
            }

            GrantListener.grantRemoveMap.put(player.getName(), grant);
            GrantListener.grantTargetRemoveMap.put(player.getName(), target);
            player.closeInventory();
            player.sendMessage(CC.translate("&aType the reason for removing this grant. Type 'cancel' to stop this process."));
        }
    }

    @AllArgsConstructor
    private static class PermissionButton extends Button {

        public UUID target;
        public UserPermission permission;
        private int slot;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            if (permission.isRemoved()) {
                return new ItemBuilder(FlashMenuLanguage.GRANTS_MENU_REMOVED_PERMISSION_BUTTON_MATERIAL.getMaterial())
                        .setDurability(FlashMenuLanguage.GRANTS_MENU_REMOVED_PERMISSION_BUTTON_DATA.getInt())
                        .setName(FlashMenuLanguage.GRANTS_MENU_REMOVED_PERMISSION_BUTTON_NAME.getString(),
                                "%ADDEDAT%", permission.getAddedAtDate(),
                                "%ADDEDBY%", UserUtils.formattedName(permission.getSentBy()),
                                "%ADDEDFOR%", permission.getSentFor(),
                                "%REMOVEDAT%", permission.getRemovedAtDate(),
                                "%REMOVEDBY%", UserUtils.formattedName(permission.getRemovedBy()),
                                "%REMOVEDFOR%", permission.getRemovedFor(),
                                "%PERMISSION%", permission.getNode())
                        .setLore(FlashMenuLanguage.GRANTS_MENU_REMOVED_PERMISSION_BUTTON_LORE.getStringList(),
                                "%ADDEDAT%", permission.getAddedAtDate(),
                                "%ADDEDBY%", UserUtils.formattedName(permission.getSentBy()),
                                "%ADDEDFOR%", permission.getSentFor(),
                                "%REMOVEDAT%", permission.getRemovedAtDate(),
                                "%REMOVEDBY%", UserUtils.formattedName(permission.getRemovedBy()),
                                "%REMOVEDFOR%", permission.getRemovedFor(),
                                "%PERMISSION%", permission.getNode())
                        .create();
            }
            return new ItemBuilder(FlashMenuLanguage.GRANTS_MENU_DEFAULT_PERMISSION_BUTTON_MATERIAL.getMaterial())
                    .setDurability(FlashMenuLanguage.GRANTS_MENU_DEFAULT_PERMISSION_BUTTON_DATA.getInt())
                    .setName(FlashMenuLanguage.GRANTS_MENU_DEFAULT_PERMISSION_BUTTON_NAME.getString(),
                            "%ADDEDAT%", permission.getAddedAtDate(),
                            "%ADDEDBY%", UserUtils.formattedName(permission.getSentBy()),
                            "%ADDEDFOR%", permission.getSentFor(),
                            "%TIMELEFT%", permission.getExpireString(),
                            "%PERMISSION%", permission.getNode())
                    .setLore(FlashMenuLanguage.GRANTS_MENU_DEFAULT_PERMISSION_BUTTON_LORE.getStringList(),
                            "%ADDEDAT%", permission.getAddedAtDate(),
                            "%ADDEDBY%", UserUtils.formattedName(permission.getSentBy()),
                            "%TIMELEFT%", permission.getExpireString(),
                            "%ADDEDFOR%", permission.getSentFor(),
                            "%PERMISSION%", permission.getNode()
                    )
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;

            Player player = (Player) event.getWhoClicked();

            if (permission.isRemoved()) {
                return;
            }

            GrantListener.grantRemovePermMap.put(player.getName(), permission);
            GrantListener.grantTargetRemoveMap.put(player.getName(), target);
            player.closeInventory();
            player.sendMessage(CC.translate("&aType the reason for removing this grant. Type 'cancel' to stop this process."));
        }
    }
}
