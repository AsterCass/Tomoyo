package biz

import cn.hutool.core.date.ChineseDate
import constant.enums.UserChineseZodiac
import constant.enums.UserZodiac
import kotlinx.datetime.LocalDate


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