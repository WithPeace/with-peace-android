package com.withpeace.withpeace.core.ui

import android.content.Context
import com.withpeace.withpeace.core.domain.model.date.Date
import com.withpeace.withpeace.core.domain.model.date.DurationFromNow
import java.time.format.DateTimeFormatter

fun Date.toRelativeString(context: Context): String {
    return when (durationFromNow) {
        is DurationFromNow.LessThanOneMinute -> {
            context.getString(
                R.string.second_format,
                durationFromNow.seconds,
            )
        }

        is DurationFromNow.OneHourToOneDay -> context.getString(
            R.string.hour_format,
            durationFromNow.minutes,
        )

        is DurationFromNow.OneDayToSevenDay -> context.getString(
            R.string.second_format,
            durationFromNow.days,
        )

        is DurationFromNow.OneMinuteToOneHour -> context.getString(
            R.string.day_format,
            durationFromNow.days,
        )

        is DurationFromNow.SevenDayToOneYear -> date.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
        is DurationFromNow.OverOneYear -> context.getString(
            R.string.years_format,
            durationFromNow.years,
        )
    }
}

private const val DATE_FORMAT = "MM월 DD일"
