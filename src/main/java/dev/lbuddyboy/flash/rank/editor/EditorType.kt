package dev.lbuddyboy.flash.rank.editor

import dev.lbuddyboy.flash.command.rank.RankCommandimport

dev.lbuddyboy.flash.rank.Rankimport dev.lbuddyboy.flash.rank.editor.listener.RankEditorListenerimport dev.lbuddyboy.flash.rank.editor.menu.InheritanceEditorMenuimport dev.lbuddyboy.flash.rank.editor.menu.PermissionEditorMenuimport dev.lbuddyboy.flash.util.*import dev.lbuddyboy.flash.util.Callback.TripleCallbackimport

dev.lbuddyboy.flash.util.bukkit.CCimport dev.lbuddyboy.flash.util.bukkit.CompatibleMaterialimport dev.lbuddyboy.flash.util.bukkit.ItemBuilderimport lombok.*import org.bukkit.*
import org.bukkit.entity.Playerimport

org.bukkit.inventory.ItemStack
@AllArgsConstructor
@Getter
enum class EditorType {
    RENAME(
        32,
        ItemBuilder(CompatibleMaterial.getMaterial("SIGN")).setName("&dRename").create(),
        Callback { player: Player, rank: Rank? ->
            player.sendMessage(CC.translate("&aType what you would like the new name of this rank to be. Type 'cancel' to stop this process."))
            player.closeInventory()
        },
        TripleCallback<Player, Rank, String> { obj: T?, sender: V?, rank: O? -> RankCommand.setName(sender, rank) }),
    DISPLAY_NAME(
        17,
        ItemBuilder(CompatibleMaterial.getMaterial("NAME_TAG")).setName("&bSet Display Name").create(),
        Callback { player: Player, rank: Rank? ->
            player.sendMessage(CC.translate("&aType what you would like the new display name of this rank to be. Type 'cancel' to stop this process."))
            player.closeInventory()
        },
        TripleCallback { sender: Player?, rank: Rank?, message: String? -> }),
    RECOLOR(
        29,
        ItemBuilder(CompatibleMaterial.getMaterial("INK_SACK")).setName("&eSet Color").create(),
        Callback { player: Player, rank: Rank? ->
            player.sendMessage(CC.translate("&aType what you would like the new color of this rank to be. Type 'cancel' to stop this process."))
            player.closeInventory()
        },
        TripleCallback { sender: Player, rank: Rank, message: String? ->
            RankCommand.setColor(
                sender, rank, ChatColor.valueOf(
                    message!!
                )
            )
        }),
    PREFIX(
        39,
        ItemBuilder(CompatibleMaterial.getMaterial("PAPER")).setName("&eSet Prefix").create(),
        Callback { player: Player, rank: Rank? ->
            player.sendMessage(CC.translate("&aType what you would like the new prefix of this rank to be. Type 'cancel' to stop this process."))
            player.closeInventory()
        },
        TripleCallback<Player, Rank, String> { obj: T?, sender: V?, rank: O? -> RankCommand.setPrefix(sender, rank) }),
    SUFFIX(
        43,
        ItemBuilder(CompatibleMaterial.getMaterial("PAPER")).setName("&eSet Suffix").create(),
        Callback { player: Player, rank: Rank? ->
            player.sendMessage(CC.translate("&aType what you would like the new suffix of this rank to be. Type 'cancel' to stop this process."))
            player.closeInventory()
        },
        TripleCallback<Player, Rank, String> { obj: T?, sender: V?, rank: O? -> RankCommand.setSuffix(sender, rank) }),
    PERMISSIONS(
        20,
        ItemBuilder(CompatibleMaterial.getMaterial("BOOK")).setName("&3Permission Editor").create(),
        Callback { player: Player?, rank: Rank ->
            PermissionEditorMenu(rank).openMenu(
                player!!
            )
        },
        TripleCallback { sender: Player, rank: Rank, message: String? ->
            RankCommand.setColor(
                sender, rank, ChatColor.valueOf(
                    message!!
                )
            )
        }),
    INHERITANCE(
        26,
        ItemBuilder(CompatibleMaterial.getMaterial("EGG")).setName("&4Inheritance Editor").create(),
        Callback { player: Player?, rank: Rank ->
            InheritanceEditorMenu(rank).openMenu(
                player!!
            )
        },
        TripleCallback { sender: Player, rank: Rank, message: String? ->
            RankCommand.setColor(
                sender, rank, ChatColor.valueOf(
                    message!!
                )
            )
        }),
    DEFAULT(
        35,
        ItemBuilder(CompatibleMaterial.getMaterial("LEATHER")).setName("&aToggle Default").create(),
        Callback { player: Player, rank: Rank ->
            RankCommand.toggleDefault(player, rank)
            RankEditorListener.Companion.rankEditorMap.remove(player)
        },
        TripleCallback { sender: Player, rank: Rank, message: String? -> RankCommand.toggleDefault(sender, rank) }),
    SET_WEIGHT(
        11,
        ItemBuilder(CompatibleMaterial.getMaterial("ANVIL")).setName("&eSet Weight").create(),
        Callback { player: Player, rank: Rank? ->
            player.sendMessage(CC.translate("&aType what you would like the new weight of this rank to be. Type 'cancel' to stop this process."))
            player.closeInventory()
        },
        TripleCallback { sender: Player, rank: Rank, message: String ->
            RankCommand.setWeight(
                sender,
                rank,
                message.toInt()
            )
        });

    private val slot = 0
    private val stack: ItemStack? = null
    private val button: Callback<Player, Rank>? = null
    private val edit: TripleCallback<Player, Rank, String>? = null
}