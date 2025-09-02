package com.jkytay.xero.ui.formatter

import android.icu.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

interface Formatter {
    fun formatCurrency(value: Number): String
    fun formatDate(value: LocalDateTime): String
}

internal class FormatterImpl @Inject constructor() : Formatter {
    private val currencyFormatter = NumberFormat.getCurrencyInstance()
    private val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

    override fun formatCurrency(value: Number): String {
        return currencyFormatter?.format(value) ?: "?"
    }

    override fun formatDate(value: LocalDateTime): String {
        return dateTimeFormatter?.format(value) ?: "?"
    }
}
