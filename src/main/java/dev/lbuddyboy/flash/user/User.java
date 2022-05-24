package dev.lbuddyboy.flash.user;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.comparator.*;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.user.model.UserPermission;
import dev.lbuddyboy.flash.util.CC;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;
import java.util.stream.Collectors;

@Data
public abstract class User {

    public UUID uuid = null;
    public String name = null;
    public String ip = null;
    public List<UserPermission> permissions = new ArrayList<>();
    public List<String> knownIps = new ArrayList<>();
    public List<Grant> grants = new ArrayList<>();
    public List<Punishment> punishments = new ArrayList<>();
    public Grant activeGrant = null;

    public String currentServer = null;
    public String lastServer = null;

    public abstract void load();
    public abstract void save(boolean async);

    public String getDisplayName() {
        return CC.translate(getActiveRank().getPrefix() + name + getActiveRank().getSuffix());
    }

    public String getColoredName() {
        return getActiveRank().getColor() + name;
    }

    public Punishment getActivePunishment(PunishmentType type) {
        for (Punishment punishment : this.punishments) {
            if (punishment.getType() != type) continue;
            if (punishment.isExpired() || punishment.isRemoved()) continue;

            return punishment;
        }
        return null;
    }

    public boolean hasActivePunishment(PunishmentType type) {
        return getActivePunishment(type) != null;
    }

    public List<Punishment> getSortedPunishments() {
        return this.punishments.stream().sorted(new PunishmentDateComparator().reversed().thenComparing(new PunishmentRemovedComparator().reversed())).collect(Collectors.toList());
    }

    public List<Punishment> getSortedPunishmentsByType(PunishmentType type) {
        return this.punishments.stream().filter(punishment -> punishment.getType() == type).sorted(new PunishmentDateComparator().reversed().thenComparing(new PunishmentRemovedComparator().reversed())).collect(Collectors.toList());
    }

    public Rank getActiveRank() {
        Grant active = activeGrant;
        if (active == null) return Flash.getInstance().getRankHandler().getDefaultRank();

        return active.getRank();
    }

    public Grant getActiveGrant() {
        Grant active = activeGrant;
        if (active == null) return this.grants.stream().filter(grant -> grant.getRank() != null && grant.getRank().isDefaultRank()).collect(Collectors.toList()).get(0);

        return active;
    }

    public List<Grant> getActiveGrants() {
        return this.grants.stream().filter(grant -> !grant.isExpired() && !grant.isRemoved()).collect(Collectors.toList());
    }

    public List<Grant> getSortedGrants() {
        return this.grants.stream().sorted(new GrantWeightComparator().reversed().thenComparing(new GrantDateComparator().reversed())).collect(Collectors.toList());
    }

    public List<UserPermission> getActivePermissions() {
        return this.permissions.stream().filter(permission -> !permission.isExpired() && !permission.isRemoved()).collect(Collectors.toList());
    }

    public List<UserPermission> getSortedPermissions() {
        return this.permissions.stream().sorted(new UserPermissionDateComparator().reversed().thenComparing(new UserPermissionDateComparator().reversed())).collect(Collectors.toList());
    }

    public void updateGrants() {
        for (Grant grant : grants) {
            if (grant.isRemoved()) continue;
            if (!grant.isExpired()) continue;

            grant.setRemovedAt(System.currentTimeMillis());
            grant.setRemovedFor("Expired");
            grant.setRemovedBy(null);
            if (activeGrant != null && activeGrant.equals(grant)) {
                activeGrant = null;
            }
        }

        List<Grant> grants = this.getActiveGrants().stream().sorted(new GrantWeightComparator().reversed().thenComparing(new GrantDateComparator().reversed())).collect(Collectors.toList());

        for (Grant grant : grants) {
            if (Arrays.stream(grant.getScopes()).map(String::toLowerCase).collect(Collectors.toList()).contains("global") || Arrays.asList(grant.getScopes()).contains(FlashLanguage.SERVER_NAME.getString())) {
                activeGrant = grant;
                buildPlayer();
                break;
            }
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

    public void buildPlayer() {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

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

        for (String perm : getActiveRank().getPermissions()) { // Rank perms
            attachment.setPermission(perm, true);
        }

        for (String perm : getActiveRank().getInheritedPermissions()) { // Rank inherited perms
            attachment.setPermission(perm, true);
        }

        for (UserPermission userPermission : getActivePermissions()) { // User specific perms
            attachment.setPermission(userPermission.getNode(), true);
        }

        player.recalculatePermissions();
    }
}
