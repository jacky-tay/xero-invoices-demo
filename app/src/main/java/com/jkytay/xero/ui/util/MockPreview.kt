package com.jkytay.xero.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jkytay.xero.ui.formatter.Formatter
import java.time.LocalDateTime

@Composable
internal fun mockFormatter(): Formatter = remember {
    object : Formatter {
        override fun formatCurrency(value: Number): String {
            return "$%.2f".format(value)
        }

        override fun formatDate(value: LocalDateTime): String {
            return value.toString()
        }
    }
}