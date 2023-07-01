package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.user.model.Prefix;
import dev.lbuddyboy.flash.util.bukkit.CC;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class PrefixesUpdatePacket implements JedisPacket {

    private List<Prefix> newPrefixes;

    @Override
    public void onReceive() {
        Flash.getInstance().getUserHandler().setPrefixes(this.newPrefixes);
        CC.broadCastStaff("&4[Prefixes] &aAll ranks have been updated & refreshed!");
    }

}
