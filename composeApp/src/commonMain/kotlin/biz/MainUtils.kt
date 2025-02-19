package biz

import constant.BaseResText
import constant.LUNAR_CODE
import constant.enums.UserChineseZodiac
import constant.enums.UserZodiac
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


expect fun copyToClipboard(text: String)

expect fun logInfo(text: String)

fun formatSeconds(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return if (0 == hours) "${minutes.toString().padStart(2, '0')}:" +
            remainingSeconds.toString().padStart(2, '0')
    else "${hours.toString().padStart(2, '0')}:" +
            "${minutes.toString().padStart(2, '0')}:" +
            remainingSeconds.toString().padStart(2, '0')
}

fun getChineseZodiac(date: LocalDate): UserChineseZodiac {
    val chineseYear = getChineseYear(date)
    return UserChineseZodiac.entries[(chineseYear - 1900) % UserChineseZodiac.entries.size]
}


fun getChineseYear(localDate: LocalDate): Int {
    // 求出和1900年1月31日相差的天数
    var offset: Int = localDate.toEpochDays() -
            LocalDate(1900, 1, 31).toEpochDays()
    // 计算农历年份
    // 用offset减去每农历年的天数，计算当天是农历第几天，offset是当年的第几天
    var daysOfYear: Int
    var iYear = 1900
    while (iYear <= 2099) {
        daysOfYear = yearDays(iYear)
        if (offset < daysOfYear) {
            break
        }
        offset -= daysOfYear
        iYear++
    }
    return iYear
}

private fun yearDays(y: Int): Int {
    var i: Int
    var sum = 348
    i = 0x8000
    while (i > 0x8) {
        if ((getCode(y) and i.toLong()) != 0L) {
            sum += 1
        }
        i = i shr 1
    }
    return (sum + leapDays(y))
}

private fun leapDays(y: Int): Int {
    if (leapMonth(y) != 0) {
        return if ((getCode(y) and 0x10000L) != 0L) 30 else 29
    }
    return 0
}

private fun leapMonth(y: Int): Int {
    return (getCode(y) and 0xfL).toInt()
}

private fun getCode(year: Int): Long {
    return LUNAR_CODE[year - 1900]
}


fun getZodiac(month: Int, day: Int): UserZodiac {
    val actualMonth = month - 1
    return if (day < UserZodiac.entries[actualMonth].sepDay) UserZodiac.entries[actualMonth]
    else UserZodiac.entries[actualMonth + 1]
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun getLastTime(time: String?): String {
    if (time.isNullOrBlank()) return ""
    val dateTime = LocalDateTime.Format {
        byUnicodePattern("yyyy-MM-dd HH:mm:ss")
    }.parse(time)

    val nowCo = Clock.System.now()
    val systemTZ = TimeZone.currentSystemDefault()
    val now = nowCo.toLocalDateTime(systemTZ)
    if (now.year != dateTime.year) {
        return dateTime.format(LocalDateTime.Format
        { byUnicodePattern("yyyy/MM/dd") })
    }
    if (nowCo.epochSeconds >
        dateTime.toInstant(systemTZ)
            .plus(3, DateTimeUnit.DAY, systemTZ).epochSeconds
    ) {
        return dateTime.format(LocalDateTime.Format
        { byUnicodePattern("MM/dd") })
    }
    if (now.dayOfWeek != dateTime.dayOfWeek) {
        return BaseResText.weekDayList[dateTime.dayOfWeek.isoDayNumber]
    }
    return dateTime.format(LocalDateTime.Format { byUnicodePattern("HH:mm") })
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun getLastTimeInChatting(time: String?): String {
    if (time.isNullOrBlank()) return ""
    val dateTime = LocalDateTime.Format {
        byUnicodePattern("yyyy-MM-dd HH:mm:ss")
    }.parse(time)
    val nowCo = Clock.System.now()
    val systemTZ = TimeZone.currentSystemDefault()
    val now = nowCo.toLocalDateTime(systemTZ)

    if (now.year != dateTime.year) {
        return dateTime.format(LocalDateTime.Format
        { byUnicodePattern("yyyy-MM-dd HH:mm") })
    }
    if (nowCo.epochSeconds >
        dateTime.toInstant(systemTZ)
            .plus(3, DateTimeUnit.DAY, systemTZ).epochSeconds
    ) {
        return dateTime.format(LocalDateTime.Format
        { byUnicodePattern("MM-dd HH:mm") })
    }
    if (now.dayOfWeek != dateTime.dayOfWeek) {
        return BaseResText.weekDayList[dateTime.dayOfWeek.isoDayNumber] +
                dateTime.format(LocalDateTime.Format
                { byUnicodePattern(" HH:mm") })
    }
    return dateTime.format(LocalDateTime.Format { byUnicodePattern("HH:mm") })
}