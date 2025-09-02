package com.jkytay.xero.ui.modal

data class InvoiceLineItem(
    val id: String,
    val name: String,
    val timeSpentInHour: Int,
    val hourlyRate: Double,
)
