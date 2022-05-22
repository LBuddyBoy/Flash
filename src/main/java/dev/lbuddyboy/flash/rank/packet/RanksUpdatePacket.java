package dev.lbuddyboy.flash.rank.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.util.CC;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class RanksUpdatePacket implements JedisPacket {

    private Map<UUID, Rank> newRanks;

    @Override
    public void onReceive() {
        Flash.getInstance().getRankHandler().setRanks(this.newRanks);
        CC.broadCastStaff("&4[Rank] &aAll ranks have been updated & refreshed!");
        for (Rank rank : newRanks.values()) {
            if (rank.isDefaultRank()) {
                Flash.getInstance().getRankHandler().setDefaultRank(rank);
                break;
            }
        }
    }

}
