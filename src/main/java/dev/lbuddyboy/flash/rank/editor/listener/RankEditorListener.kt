package dev.lbuddyboy.flash.rank.editor.listener;

import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.rank.editor.EditorType;
import dev.lbuddyboy.flash.rank.editor.RankEdit;
import dev.lbuddyboy.flash.rank.editor.menu.InheritanceEditorMenu;
import dev.lbuddyboy.flash.rank.editor.menu.PermissionEditorMenu;
import dev.lbuddyboy.flash.rank.editor.menu.RankEditorMenu;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class RankEditorListener implements Listener {

    public static Map<Player, RankEdit> rankEditorMap = new HashMap<>();

    public static Map<Player, Rank> rankPermissionEditMap = new HashMap<>();
    public static Map<Player, Rank> rankInheritanceEditMap = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!rankEditorMap.containsKey(player)) return;

        event.setCancelled(true);
        RankEdit edit = rankEditorMap.remove(player);
        EditorType type = edit.getType();
        Rank rank = edit.getRank();

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            player.sendMessage(CC.translate("&cEdit process cancelled."));
            Tasks.run(() -> new RankEditorMenu(rank).openMenu(player));
            return;
        }

        type.getEdit().call(player, rank, event.getMessage());
        Tasks.run(() -> new RankEditorMenu(rank).openMenu(player));

    }

    @EventHandler
    public void onPermChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!rankPermissionEditMap.containsKey(player)) return;

        event.setCancelled(true);
        Rank rank = rankPermissionEditMap.remove(player);

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            player.sendMessage(CC.translate("&cEdit process cancelled."));
            Tasks.run(() -> new PermissionEditorMenu(rank).openMenu(player));
            return;
        }

        rank.getPermissions().add(event.getMessage());
        rank.save(true);

        Tasks.run(() -> new PermissionEditorMenu(rank).openMenu(player));

    }

    @EventHandler
    public void onInheritChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!rankInheritanceEditMap.containsKey(player)) return;

        event.setCancelled(true);
        Rank rank = rankInheritanceEditMap.remove(player);

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            player.sendMessage(CC.translate("&cEdit process cancelled."));
            Tasks.run(() -> new InheritanceEditorMenu(rank).openMenu(player));
            return;
        }

        rank.getInheritance().add(event.getMessage());
        rank.save(true);

        Tasks.run(() -> new InheritanceEditorMenu(rank).openMenu(player));

    }

}
