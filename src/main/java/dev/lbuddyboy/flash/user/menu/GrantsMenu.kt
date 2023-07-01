package dev.lbuddyboy.flash.user.menu

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashMenuLanguage
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.listener.GrantListener
import dev.lbuddyboy.flash.user.model.Grant
import dev.lbuddyboy.flash.user.model.UserPermission
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder
import dev.lbuddyboy.flash.util.bukkit.UserUtils
import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.Menu
import dev.lbuddyboy.flash.util.menu.button.BackButton
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu
import lombok.*
import org.apache.commons.lang.StringUtils
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class GrantsMenu : PagedMenu<Grant?> {
    var uuid: UUID

    @Setter
    @Getter
    var view = "ranks"
    var previousMenu: Menu? = null

    constructor(uuid: UUID) {
        this.uuid = uuid
        objects = Flash.instance.userHandler.tryUser(uuid, true).getGrants()
    }

    constructor(uuid: UUID, previousMenu: Menu?) {
        this.uuid = uuid
        this.previousMenu = previousMenu
        objects = Flash.instance.userHandler.tryUser(uuid, true).getGrants()
    }

    override fun getPageTitle(player: Player?): String? {
        return translate(
            FlashMenuLanguage.GRANTS_MENU_TITLE.string,
            "%PLAYER%", Flash.instance.cacheHandler.getUserCache().getName(uuid)
        )
    }

    override fun getPageButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        var i = 1
        val user: User = Flash.instance.userHandler.tryUser(uuid, true)
        if (view == "permissions") {
            for (permission in user.sortedPermissions) {
                buttons.add(PermissionButton(uuid, permission, i++))
            }
        } else {
            for (grant in user.sortedGrants) {
                buttons.add(GrantButton(uuid, grant, i++))
            }
        }
        return buttons
    }

    override fun autoUpdate(): Boolean {
        return true
    }

    override fun autoFill(): Boolean {
        return true
    }

    override fun getGlobalButtons(player: Player?): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(ToggleViewButton(this))
        if (previousMenu != null) buttons.add(BackButton(6, previousMenu))
        return buttons
    }

    @AllArgsConstructor
    private class ToggleViewButton : Button() {
        var menu: GrantsMenu? = null
        override fun getSlot(): Int {
            if (menu!!.previousMenu != null) return 4
            return if (menu.getView() == "permissions") FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_RANKS_SLOT.int else FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_SLOT.int
        }

        override fun getItem(): ItemStack? {
            val name =
                if (menu.getView() == "permissions") FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_RANKS_NAME.string else FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_NAME.string
            val lore =
                if (menu.getView() == "permissions") FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_RANKS_LORE.stringList else FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_LORE.stringList
            val material =
                if (menu.getView() == "permissions") FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_RANKS_MATERIAL.material else FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_MATERIAL.material
            val data =
                if (menu.getView() == "permissions") FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_RANKS_DATA.int else FlashMenuLanguage.GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_DATA.int
            return ItemBuilder(material).setName(name).setLore(lore).setDurability(data).create()
        }

        override fun action(event: InventoryClickEvent) {
            menu.setView(if (menu.getView() == "permissions") "ranks" else "permissions")
            menu!!.update((event.whoClicked as Player))
        }
    }

    @AllArgsConstructor
    private class GrantButton : Button() {
        var target: UUID? = null
        var grant: Grant? = null
        private override val slot = 0
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return if (grant!!.isRemoved) {
                ItemBuilder(FlashMenuLanguage.GRANTS_MENU_REMOVED_GRANT_BUTTON_MATERIAL.material)
                    .setDurability(FlashMenuLanguage.GRANTS_MENU_REMOVED_GRANT_BUTTON_DATA.int)
                    .setName(
                        FlashMenuLanguage.GRANTS_MENU_REMOVED_GRANT_BUTTON_NAME.string,
                        "%ADDEDAT%", grant!!.addedAtDate,
                        "%ADDEDBY%", UserUtils.formattedName(grant.getAddedBy()),
                        "%ADDEDFOR%", grant.getAddedReason(),
                        "%TIMELEFT%", grant!!.expireString,
                        "%SCOPES%", StringUtils.join(grant.getScopes(), ", "),
                        "%REMOVEDAT%", grant!!.removedAtDate,
                        "%REMOVEDBY%", UserUtils.formattedName(grant.getRemovedBy()),
                        "%REMOVEDFOR%", grant.getRemovedFor(),
                        "%RANK%", grant!!.rank!!.getDisplayName()
                    )
                    .setLore(
                        FlashMenuLanguage.GRANTS_MENU_REMOVED_GRANT_BUTTON_LORE.stringList,
                        "%ADDEDAT%", grant!!.addedAtDate,
                        "%ADDEDBY%", UserUtils.formattedName(grant.getAddedBy()),
                        "%ADDEDFOR%", grant.getAddedReason(),
                        "%SCOPES%", StringUtils.join(grant.getScopes(), ", "),
                        "%TIMELEFT%", grant!!.expireString,
                        "%REMOVEDAT%", grant!!.removedAtDate,
                        "%REMOVEDBY%", UserUtils.formattedName(grant.getRemovedBy()),
                        "%REMOVEDFOR%", grant.getRemovedFor(),
                        "%RANK%", grant!!.rank!!.getDisplayName()
                    )
                    .create()
            } else ItemBuilder(FlashMenuLanguage.GRANTS_MENU_DEFAULT_GRANT_BUTTON_MATERIAL.material)
                .setDurability(FlashMenuLanguage.GRANTS_MENU_DEFAULT_GRANT_BUTTON_DATA.int)
                .setName(
                    FlashMenuLanguage.GRANTS_MENU_DEFAULT_GRANT_BUTTON_NAME.string,
                    "%ADDEDAT%", grant!!.addedAtDate,
                    "%ADDEDBY%", UserUtils.formattedName(grant.getAddedBy()),
                    "%ADDEDFOR%", grant.getAddedReason(),
                    "%TIMELEFT%", grant!!.expireString,
                    "%SCOPES%", StringUtils.join(grant.getScopes(), ", "),
                    "%RANK%", grant!!.rank!!.getDisplayName()
                )
                .setLore(
                    FlashMenuLanguage.GRANTS_MENU_DEFAULT_GRANT_BUTTON_LORE.stringList,
                    "%ADDEDAT%", grant!!.addedAtDate,
                    "%ADDEDBY%", UserUtils.formattedName(grant.getAddedBy()),
                    "%ADDEDFOR%", grant.getAddedReason(),
                    "%SCOPES%", StringUtils.join(grant.getScopes(), ", "),
                    "%TIMELEFT%", grant!!.expireString,
                    "%RANK%", grant!!.rank!!.getDisplayName()
                )
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            if (grant!!.isRemoved) {
                return
            }
            GrantListener.Companion.grantRemoveMap.put(player.name, grant)
            GrantListener.Companion.grantTargetRemoveMap.put(player.name, target)
            player.closeInventory()
            player.sendMessage(CC.translate("&aType the reason for removing this grant. Type 'cancel' to stop this process."))
        }
    }

    @AllArgsConstructor
    private class PermissionButton : Button() {
        var target: UUID? = null
        var permission: UserPermission? = null
        private override val slot = 0
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return if (permission.isRemoved()) {
                ItemBuilder(FlashMenuLanguage.GRANTS_MENU_REMOVED_PERMISSION_BUTTON_MATERIAL.material)
                    .setDurability(FlashMenuLanguage.GRANTS_MENU_REMOVED_PERMISSION_BUTTON_DATA.int)
                    .setName(
                        FlashMenuLanguage.GRANTS_MENU_REMOVED_PERMISSION_BUTTON_NAME.string,
                        "%ADDEDAT%", permission!!.addedAtDate,
                        "%ADDEDBY%", UserUtils.formattedName(permission.getSentBy()),
                        "%ADDEDFOR%", permission.getSentFor(),
                        "%REMOVEDAT%", permission!!.removedAtDate,
                        "%REMOVEDBY%", UserUtils.formattedName(permission.getRemovedBy()),
                        "%REMOVEDFOR%", permission.getRemovedFor(),
                        "%PERMISSION%", permission.getNode()
                    )
                    .setLore(
                        FlashMenuLanguage.GRANTS_MENU_REMOVED_PERMISSION_BUTTON_LORE.stringList,
                        "%ADDEDAT%", permission!!.addedAtDate,
                        "%ADDEDBY%", UserUtils.formattedName(permission.getSentBy()),
                        "%ADDEDFOR%", permission.getSentFor(),
                        "%REMOVEDAT%", permission!!.removedAtDate,
                        "%REMOVEDBY%", UserUtils.formattedName(permission.getRemovedBy()),
                        "%REMOVEDFOR%", permission.getRemovedFor(),
                        "%PERMISSION%", permission.getNode()
                    )
                    .create()
            } else ItemBuilder(FlashMenuLanguage.GRANTS_MENU_DEFAULT_PERMISSION_BUTTON_MATERIAL.material)
                .setDurability(FlashMenuLanguage.GRANTS_MENU_DEFAULT_PERMISSION_BUTTON_DATA.int)
                .setName(
                    FlashMenuLanguage.GRANTS_MENU_DEFAULT_PERMISSION_BUTTON_NAME.string,
                    "%ADDEDAT%", permission!!.addedAtDate,
                    "%ADDEDBY%", UserUtils.formattedName(permission.getSentBy()),
                    "%ADDEDFOR%", permission.getSentFor(),
                    "%TIMELEFT%", permission!!.expireString,
                    "%PERMISSION%", permission.getNode()
                )
                .setLore(
                    FlashMenuLanguage.GRANTS_MENU_DEFAULT_PERMISSION_BUTTON_LORE.stringList,
                    "%ADDEDAT%", permission!!.addedAtDate,
                    "%ADDEDBY%", UserUtils.formattedName(permission.getSentBy()),
                    "%TIMELEFT%", permission!!.expireString,
                    "%ADDEDFOR%", permission.getSentFor(),
                    "%PERMISSION%", permission.getNode()
                )
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            if (permission.isRemoved()) {
                return
            }
            GrantListener.Companion.grantRemovePermMap.put(player.name, permission)
            GrantListener.Companion.grantTargetRemoveMap.put(player.name, target)
            player.closeInventory()
            player.sendMessage(CC.translate("&aType the reason for removing this grant. Type 'cancel' to stop this process."))
        }
    }
}