package dev.lbuddyboy.flash.rank.editor;

import dev.lbuddyboy.flash.command.rank.RankCommand;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.rank.editor.listener.RankEditorListener;
import dev.lbuddyboy.flash.rank.editor.menu.InheritanceEditorMenu;
import dev.lbuddyboy.flash.rank.editor.menu.PermissionEditorMenu;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.Callback;
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public enum EditorType {

    RENAME(32, new ItemBuilder(CompatibleMaterial.getMaterial("SIGN")).setName("&dRename").create(), (player, rank) -> {
        player.sendMessage(CC.translate("&aType what you would like the new name of this rank to be. Type 'cancel' to stop this process."));
        player.closeInventory();
    }, RankCommand::setName),
    DISPLAY_NAME(17, new ItemBuilder(CompatibleMaterial.getMaterial("NAME_TAG")).setName("&bSet Display Name").create(), (player, rank) -> {
        player.sendMessage(CC.translate("&aType what you would like the new display name of this rank to be. Type 'cancel' to stop this process."));
        player.closeInventory();
    }, (sender, rank, message) -> {

    }),
    RECOLOR(29, new ItemBuilder(CompatibleMaterial.getMaterial("INK_SACK")).setName("&eSet Color").create(), (player, rank) -> {
        player.sendMessage(CC.translate("&aType what you would like the new color of this rank to be. Type 'cancel' to stop this process."));
        player.closeInventory();
    }, (sender, rank, message) -> RankCommand.setColor(sender, rank, ChatColor.valueOf(message))),
    PREFIX(39, new ItemBuilder(CompatibleMaterial.getMaterial("PAPER")).setName("&eSet Prefix").create(), (player, rank) -> {
        player.sendMessage(CC.translate("&aType what you would like the new prefix of this rank to be. Type 'cancel' to stop this process."));
        player.closeInventory();
    }, RankCommand::setPrefix),
    SUFFIX(43, new ItemBuilder(CompatibleMaterial.getMaterial("PAPER")).setName("&eSet Suffix").create(), (player, rank) -> {
        player.sendMessage(CC.translate("&aType what you would like the new suffix of this rank to be. Type 'cancel' to stop this process."));
        player.closeInventory();
    }, RankCommand::setSuffix),
    PERMISSIONS(20, new ItemBuilder(CompatibleMaterial.getMaterial("BOOK")).setName("&3Permission Editor").create(), (player, rank) -> {
        new PermissionEditorMenu(rank).openMenu(player);
    }, (sender, rank, message) -> RankCommand.setColor(sender, rank, ChatColor.valueOf(message))),
    INHERITANCE(26, new ItemBuilder(CompatibleMaterial.getMaterial("EGG")).setName("&4Inheritance Editor").create(), (player, rank) -> {
        new InheritanceEditorMenu(rank).openMenu(player);
    }, (sender, rank, message) -> RankCommand.setColor(sender, rank, ChatColor.valueOf(message))),
    DEFAULT(35, new ItemBuilder(CompatibleMaterial.getMaterial("LEATHER")).setName("&aToggle Default").create(), (player, rank) -> {
        RankCommand.toggleDefault(player, rank);
        RankEditorListener.rankEditorMap.remove(player);
    }, (sender, rank, message) -> RankCommand.toggleDefault(sender, rank)),
    SET_WEIGHT(11, new ItemBuilder(CompatibleMaterial.getMaterial("ANVIL")).setName("&eSet Weight").create(), (player, rank) -> {
        player.sendMessage(CC.translate("&aType what you would like the new weight of this rank to be. Type 'cancel' to stop this process."));
        player.closeInventory();
    }, (sender, rank, message) -> RankCommand.setWeight(sender, rank, Integer.parseInt(message)));

    private final int slot;
    private final ItemStack stack;
    private final Callback<Player, Rank> button;
    private final Callback.TripleCallback<Player, Rank, String> edit;

}
