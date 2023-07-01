package dev.lbuddyboy.flash.rank.packet

import dev.lbuddyboy.flash.Flashimport

dev.lbuddyboy.flash.rank.Rankimport dev.lbuddyboy.flash.redis.JedisPacketimport dev.lbuddyboy.flash.util.bukkit.CCimport lombok.AllArgsConstructorimport java.util.*
@AllArgsConstructor
class RanksUpdatePacket : JedisPacket {
    private val newRanks: Map<UUID, Rank>? = null
    override fun onReceive() {
        Flash.instance.rankHandler.setRanks(newRanks)
        CC.broadCastStaff("&4[Rank] &aAll ranks have been updated & refreshed!")
        for (rank in newRanks!!.values) {
            if (rank.isDefaultRank) {
                Flash.instance.rankHandler.setDefaultRank(rank)
                break
            }
        }
    }
}