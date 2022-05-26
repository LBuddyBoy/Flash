package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Grant;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class GrantRemovePacket implements JedisPacket {

    private UUID uuid;
    private Grant grant;

    @Override
    public void onReceive() {
        User user = Flash.getInstance().getUserHandler().tryUser(this.uuid, false);
        if (user == null) return;

        for (Grant userGrant : user.getGrants()) {
            if (!userGrant.getUuid().toString().equals(grant.getUuid().toString())) continue;

            userGrant.setRemovedBy(grant.getRemovedBy());
            userGrant.setRemovedFor(grant.getRemovedFor());
            userGrant.setRemovedAt(grant.getRemovedAt());
        }

        user.save(true);
        user.updateGrants();

    }

}
