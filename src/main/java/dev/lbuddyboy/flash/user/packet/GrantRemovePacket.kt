package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Demotion;
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

        if (grant.getRank().isStaff()) {
            Demotion demotion = new Demotion(user.getActiveRank().getColoredName(), System.currentTimeMillis());
            user.getDemotions().add(demotion);
        }

        for (Grant userGrant : user.getGrants()) {
            if (!userGrant.getUuid().toString().equals(grant.getUuid().toString())) continue;

            userGrant.setRemovedBy(grant.getRemovedBy());
            userGrant.setRemovedFor(grant.getRemovedFor());
            userGrant.setRemovedAt(grant.getRemovedAt());
        }

        user.getGrants().removeIf(g -> g.getUuid().toString().equals(grant.getUuid().toString()));
        user.getGrants().add(grant);
        user.save(true);
        user.updateGrants();

    }

}
