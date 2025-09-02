package com.jkytay.xero.data

import java.time.LocalDateTime
import javax.inject.Inject

internal interface DateFormatter {
    fun parse(date: String): LocalDateTime
}

internal class DateFormatterImpl @Inject constructor() : DateFormatter {
    override fun parse(date: String): LocalDateTime {
        return LocalDateTime.parse(date)
    }
}
