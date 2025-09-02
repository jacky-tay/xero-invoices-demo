package com.jkytay.xero.usecases

import com.jkytay.xero.data.AnalyticTracker
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

internal class ExpandInvoiceUseCaseImpl @Inject constructor(
    private val analyticTracker: AnalyticTracker,
) : ExpandInvoiceUseCase {
    override operator fun invoke(
        invoiceId: String,
        displayItems: List<InvoiceDisplayRow>
    ): List<InvoiceDisplayRow> {
        val list = displayItems.toMutableList()
        if (list.any { it is InvoiceLineItem && it.invoiceId == invoiceId }) {
            // error, line item should not be at this state
            analyticTracker.trackError(message = "Invoice line items are already rendered in collapsed mode")
        }
        val index = list.indexOfFirst { it is Invoice && it.id == invoiceId }
        if (index != -1) {
            val invoice = list[index] as? Invoice ?: return displayItems.also {
                // something is wrong and it shouldn't, return same list back
                analyticTracker.trackError(message = "Invoice is not found in display list")
            }

            list[index] = invoice.copy(isExpand = true)
            val invoiceLineItems = invoice.items
            // add invoice line items after invoice row
            list.addAll(index + 1, invoiceLineItems)
        } else {
            analyticTracker.trackError(message = "Invoice id is not found in display list")
        }
        return list.toList()
    }
}
