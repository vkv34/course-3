package ru.online.education.app.core.util.time

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.format

fun LocalDateTime.customFormat(): String {
    val day = this.dayOfMonth.toString().padStart(2, '0')
    val month = when (this.month) {
        Month.JANUARY -> "янв."
        Month.FEBRUARY -> "фев."
        Month.MARCH -> "мар."
        Month.APRIL -> "апр."
        Month.MAY -> "май"
        Month.JUNE -> "июн."
        Month.JULY -> "июл."
        Month.AUGUST -> "авг."
        Month.SEPTEMBER -> "сен."
        Month.OCTOBER -> "окт."
        Month.NOVEMBER -> "ноя."
        Month.DECEMBER -> "дек."
        else -> ""
    }
    val year = this.year
    val hour = this.hour.toString().padStart(2, '0')
    val minute = this.minute.toString().padStart(2, '0')

    return "$day $month $year $hour:$minute"
}