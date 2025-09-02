package com.jkytay.xero.ui.modal

import java.time.LocalDateTime

data class Invoice(
    val id: String,
    val date: LocalDateTime,
    val description: String?,
    val items: List<InvoiceLineItem>,
)
