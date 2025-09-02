package com.jkytay.xero.ui.modal

import com.jkytay.xero.data.DateFormatter
import com.jkytay.xero.data.modal.InvoiceLineItemResponse
import com.jkytay.xero.data.modal.InvoiceResponse
import com.jkytay.xero.data.modal.InvoicesResponse
import javax.inject.Inject

internal interface InvoiceTransformer {
    fun parse(input: InvoicesResponse): List<Invoice>
}

internal class InvoiceTransformerImpl @Inject constructor(
    private val dateFormat: DateFormatter,
) : InvoiceTransformer {
    override fun parse(input: InvoicesResponse): List<Invoice> {
        return input.items.map { parse(it) }
    }

    private fun parse(input: InvoiceResponse): Invoice {
        return Invoice(
            id = input.id,
            date = dateFormat.parse(input.date),
            description = input.description,
            items = input.items.map {
                parse(
                    invoiceId = input.id,
                    input = it
                )
            }
        )
    }

    private fun parse(
        invoiceId: String,
        input: InvoiceLineItemResponse
    ): InvoiceLineItem {
        return InvoiceLineItem(
            invoiceId = invoiceId,
            id = input.id,
            name = input.name,
            timeSpentInHour = input.quantity,
            hourlyRate = input.priceInCents / 100.0 // convert cent to dollar
        )
    }
}
