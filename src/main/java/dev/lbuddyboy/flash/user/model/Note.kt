package dev.lbuddyboy.flash.user.model

import dev.lbuddyboy.flash.FlashLanguage
import lombok.Data
import lombok.RequiredArgsConstructor
import java.text.SimpleDateFormat
import java.util.*

@RequiredArgsConstructor
@Data
class Note {
    private val id: UUID? = null
    private val title: String? = null
    private val message: String? = null
    private val sender: UUID? = null
    private val sentAt: Long = 0
    private val removed = false
    private val removedBy: UUID? = null
    private val removedAt: Long = 0
    private val removedFor: String? = null
    fun getRemovedAtDate(): String {
        val sdf = SimpleDateFormat()
        sdf.timeZone = TimeZone.getTimeZone(FlashLanguage.TIMEZONE.string)
        return sdf.format(removedAt)
    }

    fun getDateSentAt(): String {
        val sdf = SimpleDateFormat()
        sdf.timeZone = TimeZone.getTimeZone(FlashLanguage.TIMEZONE.string)
        return sdf.format(Date(sentAt))
    }
}