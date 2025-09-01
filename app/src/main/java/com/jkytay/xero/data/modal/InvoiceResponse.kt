package com.jkytay.xero.data.modal

data class InvoiceResponse(
    /**
     * Unique Id for invoice
     */
    val id: String,
    /**
     * Date in ISO8601
     */
    val date: String,
    /**
     * Description for Invoice
     */
    val description: String?,
    /**
     * Line items for Invoice
     */
    val items: List<InvoiceLineItemResponse>,
)
