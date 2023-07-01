package dev.lbuddyboy.flash.util

import java.text.SimpleDateFormat
import java.util.regex.Pattern

object TimeUtils {
    private val mmssBuilder = ThreadLocal.withInitial { StringBuilder() }
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm")
    fun formatIntoHHMMSS(secs: Int): String {
        return formatIntoMMSS(secs)
    }

    fun formatLongIntoHHMMSS(secs: Long): String {
        val unconvertedSeconds = secs.toInt()
        return formatIntoMMSS(unconvertedSeconds)
    }

    fun formatIntoMMSS(secs: Int): String {
        var secs = secs
        val seconds = secs % 60
        secs -= seconds
        var minutesCount = (secs / 60).toLong()
        val minutes = minutesCount % 60L
        minutesCount -= minutes
        val hours = minutesCount / 60L
        val result = mmssBuilder.get() as StringBuilder
        result.setLength(0)
        if (hours > 0L) {
            if (hours < 10L) {
                result.append("0")
            }
            result.append(hours)
            result.append(":")
        }
        if (minutes < 10L) {
            result.append("0")
        }
        result.append(minutes)
        result.append(":")
        if (seconds < 10) {
            result.append("0")
        }
        result.append(seconds)
        return result.toString()
    }

    fun formatLongIntoMMSS(secs: Long): String {
        val unconvertedSeconds = secs.toInt()
        return formatIntoMMSS(unconvertedSeconds)
    }

    fun formatIntoDetailedString(secs: Int): String {
        return if (secs == 0) {
            "0 seconds"
        } else {
            val remainder = secs % 86400
            val days = secs / 86400
            val hours = remainder / 3600
            val minutes = remainder / 60 - hours * 60
            val seconds = remainder % 3600 - minutes * 60
            val fDays = if (days > 0) " " + days + " day" + (if (days > 1) "s" else "") else ""
            val fHours = if (hours > 0) " " + hours + " hour" + (if (hours > 1) "s" else "") else ""
            val fMinutes = if (minutes > 0) " " + minutes + " minute" + (if (minutes > 1) "s" else "") else ""
            val fSeconds = if (seconds > 0) " " + seconds + " second" + (if (seconds > 1) "s" else "") else ""
            (fDays + fHours + fMinutes + fSeconds).trim { it <= ' ' }
        }
    }

    fun formatLongIntoDetailedString(secs: Long): String {
        val unconvertedSeconds = secs.toInt()
        return formatIntoDetailedString(unconvertedSeconds)
    }

    fun parseTime(time: String): Int {
        return if (time != "0" && time != "") {
            val lifeMatch = arrayOf("w", "d", "h", "m", "s")
            val lifeInterval = intArrayOf(604800, 86400, 3600, 60, 1)
            var seconds = -1
            for (i in lifeMatch.indices) {
                val matcher = Pattern.compile("([0-9]+)" + lifeMatch[i]).matcher(time)
                while (matcher.find()) {
                    if (seconds == -1) {
                        seconds = 0
                    }
                    seconds += matcher.group(1).toInt() * lifeInterval[i]
                }
            }
            if (seconds == -1) {
                throw IllegalArgumentException("Invalid time provided.")
            } else {
                seconds
            }
        } else {
            0
        }
    }

    fun parseTimeToLong(time: String): Long {
        val unconvertedSeconds = parseTime(time)
        return unconvertedSeconds.toLong()
    }
}