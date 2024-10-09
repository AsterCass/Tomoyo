package biz

import cn.hutool.core.date.ChineseDate
import cn.hutool.core.date.DatePattern
import cn.hutool.core.date.DateTime
import constant.BaseResText
import constant.enums.UserChineseZodiac
import constant.enums.UserZodiac
import kotlinx.datetime.LocalDate
import java.time.format.DateTimeFormatter


expect fun copyToClipboard(text: String)

fun formatSeconds(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return if (0 == hours) String.format("%02d:%02d", minutes, remainingSeconds)
    else String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}

fun getChineseZodiac(date: LocalDate): UserChineseZodiac {
    val chineseYear = ChineseDate(
        java.time.LocalDate.of(
            date.year, date.month, date.dayOfMonth
        )
    ).chineseYear
    return UserChineseZodiac.entries[(chineseYear - 1900) % UserChineseZodiac.entries.size]
}


fun getZodiac(month: Int, day: Int): UserZodiac {
    val actualMonth = month - 1
    return if (day < UserZodiac.entries[actualMonth].sepDay) UserZodiac.entries[actualMonth]
    else UserZodiac.entries[actualMonth + 1]
}

fun getLastTime(time: String?): String {
    if (time.isNullOrBlank()) return ""
    val dateTime = DateTime(time, DatePattern.NORM_DATETIME_FORMAT).toLocalDateTime()
    val now = DateTime.now().toLocalDateTime()
    if (null == now || null == dateTime) return ""
    if (now.year != dateTime.year) {
        return "${dateTime.year}/${dateTime.monthValue}/${dateTime.dayOfMonth}"
    }
    if (now.isAfter(dateTime.plusWeeks(1))) {
        return "${dateTime.monthValue}/${dateTime.dayOfMonth}"
    }
    if (now.dayOfMonth != dateTime.dayOfMonth) {
        return BaseResText.weekDayList[dateTime.dayOfWeek.value]
    }

    return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun getLastTimeInChatting(time: String?): String {
    if (time.isNullOrBlank()) return ""
    val dateTime = DateTime(time, DatePattern.NORM_DATETIME_FORMAT).toLocalDateTime()
    val now = DateTime.now().toLocalDateTime()
    if (null == now || null == dateTime) return ""
    if (now.year != dateTime.year) {
        return dateTime.format(
            DateTimeFormatter.ofPattern(
                DatePattern.NORM_DATETIME_MINUTE_PATTERN
            )
        )
    }
    if (now.isAfter(dateTime.plusWeeks(1))) {
        return dateTime.format(
            DateTimeFormatter.ofPattern(
                "MM-dd HH:mm"
            )
        )
    }
    if (now.dayOfMonth != dateTime.dayOfMonth) {
        return BaseResText.weekDayList[dateTime.dayOfWeek.value]
    }

    return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
}