package com.withpeace.withpeace.core.domain.model.date

import java.time.Duration
import java.time.LocalDateTime

sealed class DurationFromNow(
    val value: Duration,
) {
    data class LessThanOneMinute(val duration: Duration) : DurationFromNow(duration)
    data class OneMinuteToOneHour(val duration: Duration) : DurationFromNow(duration)
    data class OneHourToOneDay(val duration: Duration) : DurationFromNow(duration)
    data class OneDayToSevenDay(val duration: Duration) : DurationFromNow(duration)
    data class SevenDayToOneYear(val duration: Duration) : DurationFromNow(duration)
    data class OverOneYear(val duration: Duration) : DurationFromNow(duration)

    val seconds = value.seconds
    val minutes = value.toMinutes()
    val hours = value.toHours()
    val days = value.toDays()
    val years = value.toDays() / DAYS_FOR_YEAR

    companion object {
        fun from(date: LocalDateTime): DurationFromNow {
            val duration = Duration.between(date, LocalDateTime.now())
            return when {
                duration.isLessThanOneMinute() -> LessThanOneMinute(duration)
                duration.isLessThanOneHour() -> OneMinuteToOneHour(duration)
                duration.isLessThanOneDay() -> OneHourToOneDay(duration)
                duration.isLessThanWeekDays() -> OneDayToSevenDay(duration)
                duration.isLessOneYear() -> SevenDayToOneYear(duration)
                else -> OverOneYear(duration)
            }
        }

        private fun Duration.isLessThanOneMinute() = toMinutes() < 1
        private fun Duration.isLessThanOneHour() = toHours() < 1
        private fun Duration.isLessThanOneDay() = toDays() < 1
        private fun Duration.isLessThanWeekDays() = toDays() < DAYS_FOR_WEEK
        private fun Duration.isLessOneYear() = toDays() < DAYS_FOR_YEAR

        private const val DAYS_FOR_YEAR = 365
        private const val DAYS_FOR_WEEK = 7
    }
}
