package com.jkytay.xero.data.modal

data class InvoiceLineItemResponse(
    /**
     * Unique Id for invoice line item
     */
    val id: String,
    /**
     * Name of service
     */
    val name: String,
    /**
     * Quantity of hours
     */
    val quantity: Int,
    /**
     * Price in cents per hour
     */
    val priceInCents: Int,
)
