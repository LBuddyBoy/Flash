package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Grant;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class GrantAddPacket implements JedisPacket {

    private UUID uuid;
    private Grant grant;

    @Override
    public void onReceive() {
        User user = Flash.getInstance().getUserHandler().tryUser(this.uuid, false);
        if (user == null) return;

        user.getGrants().add(grant);
        user.save(true);
        user.updateGrants();

    }

}
