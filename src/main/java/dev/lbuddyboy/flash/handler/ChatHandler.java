package dev.lbuddyboy.flash.handler;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.TimeUtils;
import dev.lbuddyboy.flash.util.bukkit.CC;
import lombok.Data;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@Data
public class ChatHandler implements Listener {

    private boolean chatMuted;
    private int slowChat;

    public ChatHandler() {
        this.chatMuted = FlashLanguage.CHAT_MUTED.getBoolean();
        this.slowChat = FlashLanguage.CHAT_SLOW.getInt();

        Flash.getInstance().getServer().getPluginManager().registerEvents(this, Flash.getInstance());

    }

    public boolean bypassesSlowChat(Player player) {
        return player.hasPermission("slowchat.bypass");
    }

    public boolean bypassesMuteChat(Player player) {
        return player.hasPermission("mutechat.bypass");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User user = Flash.getInstance().getUserHandler().tryUser(player.getUniqueId(), false);

        if (isChatMuted() && !bypassesMuteChat(player)) {
            event.setCancelled(true);
            return;
        }

        long lastSent = user.getPlayerInfo().getLastMessageSent();
        if (getSlowChat() > 0 && !bypassesSlowChat(player) && lastSent + (getSlowChat() * 1000L) < System.currentTimeMillis() && lastSent == -1) {
            player.sendMessage(CC.translate("&cChat is currently slowed. You may talk in " + TimeUtils.formatLongIntoMMSS(((lastSent + (getSlowChat() * 1000L)) / 1000))));
            event.setCancelled(true);
            return;
        }

        user.getPlayerInfo().setLastMessageSent(System.currentTimeMillis());

    }

}
