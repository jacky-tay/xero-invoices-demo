package com.jkytay.xero.ui.modal

import androidx.compose.runtime.Immutable
import java.time.LocalDateTime

@Immutable
data class Invoice(
    val id: String,
    val date: LocalDateTime,
    val description: String?,
    val items: List<InvoiceLineItem>,
    val isExpand: Boolean = false,
) : InvoiceDisplayRow
