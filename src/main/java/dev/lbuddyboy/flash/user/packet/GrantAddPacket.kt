package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Demotion;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.Promotion;
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
        if (user.hasGrant(grant.getUuid())) return;

        if (user.getActiveRank().isStaff() && user.getActiveRank().getWeight() < grant.getRank().getWeight()) {
            Promotion promotion = new Promotion(user.getActiveRank().getColoredName(), grant.getRank().getColoredName(), System.currentTimeMillis());
            user.getPromotions().add(promotion);
        }
        if (grant.getRank().isStaff() && !user.getActiveRank().isStaff()) {
            user.getStaffInfo().setJoinedStaffTeam(System.currentTimeMillis());
        }
        user.getGrants().add(grant);
        user.save(true);
        user.updateGrants();

    }

}
