package dev.lbuddyboy.flash.server.model

import dev.lbuddyboy.flash.FlashLanguage
import lombok.Getter
import java.text.SimpleDateFormat
import java.util.*

@Getter
abstract class Notification {
    var id: UUID? = null
    var title: String? = null
    var message: String? = null
    var sentAt: Long = 0
    fun getSentAtDate(): String {
        val sdf = SimpleDateFormat()
        sdf.timeZone = TimeZone.getTimeZone(FlashLanguage.TIMEZONE.string)
        return sdf.format(sentAt)
    }

    abstract fun load()
    abstract fun delete()
    abstract fun save()
}