package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerChangePacket implements JedisPacket {

    private boolean join;
    private String player;
    private String server;

    @Override
    public void onReceive() {
        if (!join) {
            CC.broadCastStaff(FlashLanguage.SERVER_CHANGE_LEAVE.getString(), "%PLAYER%", this.player, "%SERVER%", this.server);
            return;
        }
        CC.broadCastStaff(FlashLanguage.SERVER_CHANGE_JOIN.getString(), "%PLAYER%", this.player, "%SERVER%", this.server);
    }

}
