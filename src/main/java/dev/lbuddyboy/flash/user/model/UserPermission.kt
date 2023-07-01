package dev.lbuddyboy.flash.user.model

import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.util.TimeUtils
import lombok.Data
import lombok.RequiredArgsConstructor
import java.text.SimpleDateFormat
import java.util.*

@Data
@RequiredArgsConstructor
class UserPermission {
    private val node: String? = null
    private val duration: Long = 0
    private val sentAt: Long = 0
    private val sentBy: UUID? = null
    private val sentFor: String? = null
    private val removedBy: UUID? = null
    private val removedAt: Long = 0
    private val removedFor: String? = null
    private val removed = false
    fun getExpireString(): String? {
        return if (isPermanent()) {
            "Never"
        } else TimeUtils.formatLongIntoDetailedString((getExpiry() - System.currentTimeMillis()) / 1000)
    }

    fun getExpiry(): Long {
        return sentAt + duration
    }

    fun isExpired(): Boolean {
        return if (isPermanent()) false else getExpiry() < System.currentTimeMillis()
    }

    fun isPermanent(): Boolean {
        return duration == Long.MAX_VALUE
    }

    fun getAddedAtDate(): String {
        val sdf = SimpleDateFormat()
        sdf.timeZone = TimeZone.getTimeZone(FlashLanguage.TIMEZONE.string)
        return sdf.format(sentAt)
    }

    fun getRemovedAtDate(): String {
        val sdf = SimpleDateFormat()
        sdf.timeZone = TimeZone.getTimeZone(FlashLanguage.TIMEZONE.string)
        return sdf.format(removedAt)
    }
}