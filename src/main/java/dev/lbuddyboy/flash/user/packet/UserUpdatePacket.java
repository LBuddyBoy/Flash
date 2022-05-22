package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.Tasks;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class UserUpdatePacket implements JedisPacket {

    private UUID uuid;
    private User newUser;

    @Override
    public void onReceive() {
        Tasks.runAsync(() -> {
            User user = Flash.getInstance().getUserHandler().getUser(this.uuid, false);

            if (user == null) {
                // return here because we only want to update them if they are in the cache
                return;
            }

            user.setLastServer(newUser.getLastServer());
            user.setIp(newUser.getIp());
            user.setName(newUser.getName());
            user.setGrants(newUser.getGrants());
            user.setActiveGrant(newUser.getActiveGrant());
            user.setPermissions(newUser.getPermissions());
            user.setKnownIps(newUser.getKnownIps());

            user.updatePerms();
            user.updateGrants();

        });
    }

}
