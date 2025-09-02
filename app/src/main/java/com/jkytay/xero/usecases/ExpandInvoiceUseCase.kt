package com.jkytay.xero.usecases

import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.modal.InvoiceDisplayRow
import com.jkytay.xero.ui.modal.InvoiceLineItem
import javax.inject.Inject

interface ExpandInvoiceUseCase {
    operator fun invoke(
        invoiceId: String,
        displayItems: List<InvoiceDisplayRow>
    ): List<InvoiceDisplayRow>
}

internal class ExpandInvoiceUseCaseImpl @Inject constructor() : ExpandInvoiceUseCase {
    override operator fun invoke(
        invoiceId: String,
        displayItems: List<InvoiceDisplayRow>
    ): List<InvoiceDisplayRow> {
        val list = displayItems.toMutableList()
        if (list.any { it is InvoiceLineItem && it.invoiceId == invoiceId }) {
            // error, line item should not be at this state
            TODO("Log as error")
        }
        val index = list.indexOfFirst { it is Invoice && it.id == invoiceId }
        if (index != -1) {
            val invoice = list[index] as? Invoice ?: return displayItems // something is wrong, return same list back
            list[index] = invoice.copy(isExpand = true)
            val invoiceLineItems = invoice.items
            // add invoice line items after invoice row
            list.addAll(index + 1, invoiceLineItems)
        }
        return list.toList()
    }
}
