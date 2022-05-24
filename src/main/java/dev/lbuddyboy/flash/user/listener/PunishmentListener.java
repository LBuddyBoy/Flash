package dev.lbuddyboy.flash.user.listener;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.user.model.UserPermission;
import dev.lbuddyboy.flash.user.packet.PunishmentSendPacket;
import dev.lbuddyboy.flash.user.packet.UserUpdatePacket;
import dev.lbuddyboy.flash.user.punishment.menu.PunishmentHistoryMenu;
import dev.lbuddyboy.flash.util.CC;
import dev.lbuddyboy.flash.util.Tasks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class PunishmentListener implements Listener {

    public static Map<String, Punishment> punishmentRemovePermMap = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (punishmentRemovePermMap.containsKey(event.getPlayer().getName())) {
            Punishment punishment = punishmentRemovePermMap.remove(event.getPlayer().getName());

            punishment.setRemoved(true);
            punishment.setRemovedAt(System.currentTimeMillis());
            punishment.setRemovedFor(event.getMessage());
            punishment.setRemovedBy(event.getPlayer().getUniqueId());
            punishment.setRemovedSilent(true);

            User user = Flash.getInstance().getUserHandler().tryUser(punishment.getTarget(), true);

            user.save(true);

            new PunishmentSendPacket(punishment).send();
            new UserUpdatePacket(user.getUuid(), user).send();

            Tasks.run(() -> new PunishmentHistoryMenu(punishment.getTarget(), punishment.getType()).openMenu(event.getPlayer()));

            return;
        }

        User user = Flash.getInstance().getUserHandler().tryUser(event.getPlayer().getUniqueId(), false);

        if (user == null) {
            event.setCancelled(true);
            return;
        }

        if (user.hasActivePunishment(PunishmentType.MUTE)) {
            Punishment punishment = user.getActivePunishment(PunishmentType.MUTE);

            event.setCancelled(true);
            for (String s : FlashLanguage.PUNISHMENT_MUTED_FORMAT.getStringList()) {
                event.getPlayer().sendMessage(CC.translate(s, "%REASON%", punishment.getSentFor(), "%TIME-LEFT%", punishment.getExpireString()));
            }
        }

    }

}
