package dev.lbuddyboy.flash.util

import com.google.common.base.CharMatcher
import com.google.common.base.Joiner
import com.google.common.base.Preconditions
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

object JavaUtils {
    private val CHAR_MATCHER_ASCII: CharMatcher? = null
    private val UUID_PATTERN: Pattern? = null
    private const val DEFAULT_NUMBER_FORMAT_DECIMAL_PLACES = 5
    fun tryParseInt(string: String): Int? {
        return try {
            string.toInt()
        } catch (ex: IllegalArgumentException) {
            null
        }
    }

    fun tryParseDouble(string: String): Double? {
        return try {
            string.toDouble()
        } catch (ex: IllegalArgumentException) {
            null
        }
    }

    @JvmOverloads
    fun format(number: Number, decimalPlaces: Int = 5, roundingMode: RoundingMode? = RoundingMode.HALF_DOWN): String {
        Preconditions.checkNotNull(number as Any, "The number cannot be null" as Any)
        return BigDecimal(number.toString()).setScale(decimalPlaces, roundingMode).stripTrailingZeros().toPlainString()
    }

    @JvmOverloads
    fun andJoin(collection: Collection<String?>?, delimiterBeforeAnd: Boolean, delimiter: String? = ", "): String {
        if (collection == null || collection.isEmpty()) {
            return ""
        }
        val contents: List<String?> = ArrayList(collection)
        val last: String = contents.removeAt(contents.size - 1)
        val builder = StringBuilder(Joiner.on(delimiter).join(contents as Iterable<*>))
        if (delimiterBeforeAnd) {
            builder.append(delimiter)
        }
        return builder.append(" and ").append(last).toString()
    }

    fun parse(input: String?): Long {
        if (input == null || input.isEmpty()) {
            return -1L
        }
        var result = 0L
        var number = StringBuilder()
        for (i in 0 until input.length) {
            val c = input[i]
            if (Character.isDigit(c)) {
                number.append(c)
            } else {
                var str: String
                if (Character.isLetter(c) && !number.toString().also { str = it }.isEmpty()) {
                    result += convert(str.toInt(), c)
                    number = StringBuilder()
                }
            }
        }
        return result
    }

    private fun convert(value: Int, unit: Char): Long {
        return when (unit) {
            'y' -> {
                value * TimeUnit.DAYS.toMillis(365L)
            }

            'M' -> {
                value * TimeUnit.DAYS.toMillis(30L)
            }

            'd' -> {
                value * TimeUnit.DAYS.toMillis(1L)
            }

            'h' -> {
                value * TimeUnit.HOURS.toMillis(1L)
            }

            'm' -> {
                value * TimeUnit.MINUTES.toMillis(1L)
            }

            's' -> {
                value * TimeUnit.SECONDS.toMillis(1L)
            }

            else -> {
                -1L
            }
        }
    }
}