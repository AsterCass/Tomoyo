package biz

fun formatSeconds(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return if (0 == hours) String.format("%02d:%02d", minutes, remainingSeconds)
    else String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}