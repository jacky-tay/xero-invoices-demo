package com.jkytay.xero.data.modal

import kotlinx.serialization.Serializable

@Serializable
internal data class InvoicesResponse(
    /**
     * The list of invoices
     */
    val items: List<InvoiceResponse>
)
