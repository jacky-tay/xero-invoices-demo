package com.jkytay.xero.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jkytay.xero.ui.formatter.Formatter
import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.modal.InvoiceLineItem
import java.time.LocalDateTime

internal val mockInvoiceLineItem = InvoiceLineItem(
    invoiceId = "header_id_0",
    id = "id",
    name = "My service",
    timeSpentInHour = 2,
    hourlyRate = 23.5
)

internal val mockInvoice = Invoice(
    id = "header_id_0",
    date = LocalDateTime.now(),
    description = "This is an optional description",
    items = listOf(mockInvoiceLineItem)
)

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