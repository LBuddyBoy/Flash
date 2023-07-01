package dev.lbuddyboy.flash.rank;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.rank.packet.RanksUpdatePacket;
import dev.lbuddyboy.flash.user.User;
import lombok.Data;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public abstract class Rank {

    public UUID uuid;
    public String name;
    public String displayName = name;
    public ChatColor color = ChatColor.WHITE;
    public int weight = 1;
    public String prefix = "";
    public String suffix = "";
    public List<String> permissions = new ArrayList<>();
    public List<String> inheritance = new ArrayList<>();
    public boolean defaultRank, staff;

    public abstract void load();
    public abstract void delete();
    public abstract void save(boolean async);

    public List<User> getUsersWithRank() {
        List<User> peopleWithThisRank = new ArrayList<>();

        for (UUID uuid : Flash.getInstance().getCacheHandler().getUserCache().allUUIDs()) {
            User user = Flash.getInstance().getUserHandler().tryUserRank(uuid, true);

            if (user == null) continue;

            if (user.getActiveRank() != null && user.getActiveRank().getUuid().toString().equalsIgnoreCase(this.uuid.toString())) peopleWithThisRank.add(user);
        }

        return peopleWithThisRank;
    }

    public String getColoredName() {
        return this.color + this.name;
    }

    public String getDisplayName() {
        if (displayName == null) return this.name;

        return this.displayName;
    }

    public List<String> getInheritedPermissions() {
        List<String> perms = new ArrayList<>();

        for (String key : this.inheritance) {
            Rank rank = Flash.getInstance().getRankHandler().getRank(key);
            if (rank == null) continue;
            perms.addAll(rank.getPermissions());
        }

        return perms;
    }

    public void setDefaultRank() {
        for (Rank rank : Flash.getInstance().getRankHandler().getRanks().values()) {
            if (rank.isDefaultRank()) {
                rank.setDefaultRank(false);
            }
        }

        this.defaultRank = !defaultRank;
        Flash.getInstance().getRankHandler().setDefaultRank(this);

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();
    }

}
