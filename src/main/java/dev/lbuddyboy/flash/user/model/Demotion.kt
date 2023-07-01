package dev.lbuddyboy.flash.user.model

import dev.lbuddyboy.flash.FlashLanguage
import lombok.AllArgsConstructor
import lombok.Getter
import java.text.SimpleDateFormat
import java.util.*

@AllArgsConstructor
@Getter
class Demotion {
    private val demotedFrom: String? = null
    private val demotedAt: Long = 0
    fun getDemotedAtDate(): String {
        val sdf = SimpleDateFormat()
        sdf.timeZone = TimeZone.getTimeZone(FlashLanguage.TIMEZONE.string)
        return sdf.format(demotedAt)
    }
}