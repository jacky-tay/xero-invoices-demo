package com.jkytay.xero.ui.modal

import androidx.compose.runtime.Immutable

@Immutable
data class InvoiceLineItem(
    val invoiceId: String,
    val id: String,
    val name: String,
    val timeSpentInHour: Int,
    val hourlyRate: Double,
) : InvoiceDisplayRow
