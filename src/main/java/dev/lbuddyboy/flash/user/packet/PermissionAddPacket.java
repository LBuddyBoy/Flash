package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.UserPermission;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class PermissionAddPacket implements JedisPacket {

    private UUID uuid;
    private UserPermission permission;

    @Override
    public void onReceive() {
        User user = Flash.getInstance().getUserHandler().tryUser(this.uuid, false);
        if (user == null) return;

        user.getPermissions().add(permission);
        user.save(true);
        user.updatePerms();

    }

}
