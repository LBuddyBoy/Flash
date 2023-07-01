package dev.lbuddyboy.flash.user.model

import dev.lbuddyboy.flash.FlashLanguage
import lombok.AllArgsConstructor
import lombok.Getter
import java.text.SimpleDateFormat
import java.util.*

@AllArgsConstructor
@Getter
class Promotion {
    private val promotedFrom: String? = null
    private val promotedTo: String? = null
    private val promotedAt: Long = 0
    fun getPromotedAtDate(): String {
        val sdf = SimpleDateFormat()
        sdf.timeZone = TimeZone.getTimeZone(FlashLanguage.TIMEZONE.string)
        return sdf.format(promotedAt)
    }
}