package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.UserPermission;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class PermissionRemovePacket implements JedisPacket {

    private UUID uuid;
    private UserPermission permission;

    @Override
    public void onReceive() {
        User user = Flash.getInstance().getUserHandler().tryUser(this.uuid, false);
        if (user == null) return;

        for (UserPermission userPermission : user.getPermissions()) {
            if (!userPermission.getNode().equals(permission.getNode())) continue;

            userPermission.setRemovedBy(permission.getRemovedBy());
            userPermission.setRemovedFor(permission.getRemovedFor());
            userPermission.setRemovedAt(permission.getRemovedAt());
            userPermission.setRemoved(permission.isRemoved());
        }

        user.save(true);
        user.updateGrants();

    }

}
