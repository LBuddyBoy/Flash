package dev.lbuddyboy.flash.user;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.UserPermission;
import dev.lbuddyboy.flash.util.CC;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Data
public abstract class User {

    public UUID uuid = null;
    public String name = null;
    public String ip = null;
    public String lastServer = null;
    public List<UserPermission> permissions = new ArrayList<>();
    public List<String> knownIps = new ArrayList<>();
    public List<Grant> grants = new ArrayList<>();
    public Grant activeGrant = null;

    public abstract void load();
    public abstract void save(boolean async);

    public String getDisplayName() {
        return CC.translate(getActiveRank().getPrefix() + name + getActiveRank().getSuffix());
    }

    public String getColoredName() {
        return getActiveRank().getColor() + name;
    }

    public Rank getActiveRank() {
        Grant active = activeGrant;
        if (active == null) return Flash.getInstance().getRankHandler().getDefaultRank();

        return active.getRank();
    }

    public void setActiveGrant(Grant grant) {
        activeGrant = grant;
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.setDisplayName(getDisplayName());
            setupPermissionsAttachment(player);
        }

    }

    public void activateNextGrant() {
        List<Grant> grants = new ArrayList<>(this.grants);
        grants.sort(Comparator.comparingInt(grant -> grant.getRank().getWeight()));

        for (Grant grant : grants) {
            if (grant.getRank() == null) continue;
            if (!grant.isRemoved() && !grant.isExpired()) {
                setActiveGrant(grant);
            }
        }

    }

    public void updateGrants() {
        for (Grant grant : grants) {
            if (grant.isRemoved() || grant.isExpired()) continue;

            grant.setRemovedAt(System.currentTimeMillis());
            grant.setRemovedFor("Expired");
            grant.setRemovedBy(null);
            if (activeGrant != null && activeGrant.equals(grant)) {
                activeGrant = null;
            }
        }

        if (activeGrant == null) {
            activateNextGrant();
            if (activeGrant != null) {
                return;
            }

            Grant grant = Grant.defaultGrant();
            grants.add(grant);
            setActiveGrant(grant);
        }
    }

    public void updatePerms() {
        for (UserPermission permission : permissions) {
            if (!permission.isExpired()) continue;
            if (permission.isRemoved()) continue;

            permission.setRemovedAt(System.currentTimeMillis());
            permission.setRemovedBy(null);
            permission.setRemovedFor("Expired");
            permission.setRemoved(true);
        }
    }

    public void buildPlayer(Player player) {
        setupPermissionsAttachment(player);

        player.setDisplayName(getDisplayName());
        player.setPlayerListName(getColoredName());
    }

    public void setupPermissionsAttachment(Player player) {
        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getAttachment() == null) {
                continue;
            }
            attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> attachmentInfo.getAttachment().unsetPermission(permission));
        }

        PermissionAttachment attachment = player.addAttachment(Flash.getInstance());

        for (String perm : activeGrant.getRank().getPermissions()) { // Rank perms
            attachment.setPermission(perm, true);
        }

        for (String perm : activeGrant.getRank().getInheritedPermissions()) { // Rank inherited perms
            attachment.setPermission(perm, true);
        }

        for (UserPermission userPermission : permissions) { // User specific perms
            if (userPermission.isRemoved()) continue;
            attachment.setPermission(userPermission.getNode(), true);
        }

        player.recalculatePermissions();
    }
}
