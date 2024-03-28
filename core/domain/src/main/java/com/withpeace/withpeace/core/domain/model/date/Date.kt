package com.withpeace.withpeace.core.domain.model.date;

import java.time.LocalDateTime

@JvmInline
value class Date(
    val date: LocalDateTime,
) {
    val durationFromNow: DurationFromNow
        get() = DurationFromNow.from(date)
}
