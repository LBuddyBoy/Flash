package dev.lbuddyboy.flash.user.model

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.rank.Rank
import dev.lbuddyboy.flash.util.TimeUtils
import lombok.*
import java.text.SimpleDateFormat
import java.util.*

@RequiredArgsConstructor
@Data
class Grant {
    private val uuid: UUID? = null
    private val rank: UUID? = null
    private val rankName: String? = null
    private val addedBy: UUID? = null
    private val addedReason: String? = null
    private val addedAt: Long = 0
    private val duration: Long = 0
    private val scopes: Array<String>
    private val removedBy: UUID? = null
    private val removedAt: Long = 0
    private val removedFor: String? = null
    fun getRank(): Rank? {
        return Flash.instance.rankHandler.getRanks().get(rank)
            ?: return Flash.instance.rankHandler.getDefaultRank()
    }

    fun getRankName(): String? {
        return if (getRank() == null) rankName else getRank().getName()
    }

    fun isRemoved(): Boolean {
        return removedBy != null || removedAt > 0 || removedFor != null
    }

    fun isExpired(): Boolean {
        return getExpiresAt() <= 0
    }

    fun getExpiresAt(): Long {
        return addedAt + duration - System.currentTimeMillis()
    }

    fun getExpireString(): String? {
        return if (duration == Long.MAX_VALUE) "Never" else TimeUtils.formatLongIntoDetailedString(getExpiresAt() / 1000)
    }

    fun getAddedAtDate(): String {
        val sdf = SimpleDateFormat()
        sdf.timeZone = TimeZone.getTimeZone(FlashLanguage.TIMEZONE.string)
        return sdf.format(addedAt)
    }

    fun getRemovedAtDate(): String {
        val sdf = SimpleDateFormat()
        sdf.timeZone = TimeZone.getTimeZone(FlashLanguage.TIMEZONE.string)
        return sdf.format(removedAt)
    }

    companion object {
        fun defaultGrant(): Grant {
            val actual: Rank = Flash.instance.rankHandler.getDefaultRank()
            return Grant(
                UUID.randomUUID(),
                actual.getUuid(),
                actual.getName(),
                null,
                "Default Grant",
                System.currentTimeMillis(),
                Long.MAX_VALUE,
                arrayOf("GLOBAL")
            )
        }
    }
}