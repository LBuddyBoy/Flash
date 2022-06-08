package dev.lbuddyboy.flash.user;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.comparator.*;
import dev.lbuddyboy.flash.user.model.*;
import dev.lbuddyboy.flash.util.bukkit.CC;
import lombok.Data;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    public List<UUID> blocked = new ArrayList<>();
    public List<Grant> grants = new ArrayList<>();
    public List<Note> notes = new ArrayList<>();
    public List<Punishment> punishments = new ArrayList<>();
    public Grant activeGrant = null;
    public Prefix activePrefix = null;

    public PlayerInfo playerInfo = new PlayerInfo(true, false, null, -1, -1);
    public StaffInfo staffInfo = new StaffInfo();

    public abstract void load();

    public abstract void save(boolean async);

    public String getDisplayName() {
        if (activePrefix != null) return CC.translate(getActivePrefix().getDisplay() + getActiveRank().getPrefix() + name + getActiveRank().getSuffix());
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

    public Note getNote(String title) {
        return this.notes.stream().filter(note -> note.getTitle().equals(title)).collect(Collectors.toList()).get(0);
    }

    public Prefix getActivePrefix() {
        if (activePrefix == null) return null;
        return Flash.getInstance().getUserHandler().getPrefix(activePrefix.getId());
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
        if (active == null)
            return this.grants.stream().filter(grant -> grant.getRank() != null && grant.getRank().isDefaultRank()).collect(Collectors.toList()).get(0);

        return active;
    }

    public List<Grant> getActiveGrants() {
        return this.grants.stream().filter(grant -> !grant.isExpired() && !grant.isRemoved()).collect(Collectors.toList());
    }

    public List<Grant> getSortedGrants() {
        return this.grants.stream().sorted(new GrantWeightComparator().reversed().thenComparing(new GrantDateComparator().reversed())).collect(Collectors.toList());
    }

    public List<Note> getSortedNotes() {
        return this.notes.stream().sorted(new NoteDateComparator()).collect(Collectors.toList());
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
            if (grant.getRank() == null) continue;
            if (!Arrays.stream(grant.getScopes()).map(String::toLowerCase).collect(Collectors.toList()).contains("global") && !Arrays.asList(grant.getScopes()).contains(FlashLanguage.SERVER_NAME.getString()))
                continue;

            activeGrant = grant;
            buildPlayer();
            break;
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
            if (attachmentInfo.getAttachment() == null) continue;

            attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> attachmentInfo.getAttachment().unsetPermission(permission));
        }

        PermissionAttachment attachment = player.addAttachment(Flash.getInstance());

        if (player.isOp()) attachment.setPermission("op", true);
        getActiveRank().getPermissions().forEach(permission -> attachment.setPermission(permission, true));
        getActiveRank().getInheritedPermissions().forEach(permission -> attachment.setPermission(permission, true));
        getActivePermissions().stream().map(UserPermission::getNode).forEach(permission -> attachment.setPermission(permission, true));

        player.recalculatePermissions();
    }

    public void addPerm(UserPermission permission) {
        if (!permission.getNode().startsWith("group.")) {
            this.permissions.add(permission);
            return;
        }
        String striped = permission.getNode().replaceAll("group", "").replaceAll("\\.", "");
        String key = WordUtils.capitalize(striped);

        Rank rank = Flash.getInstance().getRankHandler().getRank(key);
        if (rank == null) return;
        Grant grant = new Grant(UUID.randomUUID(), rank.getUuid(), key, null, "Imported from LuckPerms", System.currentTimeMillis(), permission.getDuration(), new String[]{"GLOBAL"});

        grants.add(grant);
    }

    public String colorAlt() {
        ChatColor color = ChatColor.GRAY;

        if (Bukkit.getPlayer(uuid) != null) color = ChatColor.GREEN;
        if (hasActivePunishment(PunishmentType.MUTE)) color = getActivePunishment(PunishmentType.MUTE).getType().getColor();
        if (hasActivePunishment(PunishmentType.BAN)) color = getActivePunishment(PunishmentType.BAN).getType().getColor();
        if (hasActivePunishment(PunishmentType.IP_BAN)) color = getActivePunishment(PunishmentType.IP_BAN).getType().getColor();
        if (hasActivePunishment(PunishmentType.BLACKLIST)) color = getActivePunishment(PunishmentType.BLACKLIST).getType().getColor();

        return color + name;
    }

}
