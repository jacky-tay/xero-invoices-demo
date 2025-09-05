package com.jkytay.xero.compose.utils

import com.jkytay.xero.ui.formatter.Formatter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MockFormatter : Formatter {
    // Format example: Sept 4, 2025 7:30 PM
    private val dateTimeFormat = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a")
    override fun formatCurrency(value: Number): String {
        return "$%.2f".format(value)
    }

    override fun formatDate(value: LocalDateTime): String {
        return value.format(dateTimeFormat)
    }
}
