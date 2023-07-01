package dev.lbuddyboy.flash.rank

import dev.lbuddyboy.flash.Flashimport

dev.lbuddyboy.flash.rank.packet.RanksUpdatePacketimport dev.lbuddyboy.flash.user.Userimport lombok.*import org.bukkit.*
import java.util.*

@Data
abstract class Rank {
    var uuid: UUID? = null
    var name: String? = null
    var displayName = name
    var color = ChatColor.WHITE
    var weight = 1
    var prefix = ""
    var suffix = ""
    var permissions: List<String> = ArrayList()
    var inheritance: List<String> = ArrayList()
    var defaultRank = false
    var staff = false
    abstract fun load()
    abstract fun delete()
    abstract fun save(async: Boolean)
    val usersWithRank: List<User>
        get() {
            val peopleWithThisRank: MutableList<User> = ArrayList()
            for (uuid in Flash.instance.cacheHandler.getUserCache().allUUIDs()) {
                val user = Flash.instance.userHandler.tryUserRank(uuid, true) ?: continue
                if (user.activeRank != null && user.activeRank.getUuid().toString()
                        .equals(this.uuid.toString(), ignoreCase = true)
                ) peopleWithThisRank.add(user)
            }
            return peopleWithThisRank
        }
    val coloredName: String
        get() = color.toString() + name

    fun getDisplayName(): String? {
        return if (displayName == null) name else displayName
    }

    val inheritedPermissions: List<String?>
        get() {
            val perms: MutableList<String?> = ArrayList()
            for (key in inheritance) {
                val rank = Flash.instance.rankHandler.getRank(key) ?: continue
                perms.addAll(rank.getPermissions())
            }
            return perms
        }

    fun setDefaultRank() {
        for (rank in Flash.instance.rankHandler.getRanks().values()) {
            if (rank.isDefaultRank()) {
                rank.setDefaultRank(false)
            }
        }
        defaultRank = !defaultRank
        Flash.instance.rankHandler.setDefaultRank(this)
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }
}