package com.jkytay.xero.usecases

import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.modal.InvoiceDisplayRow
import com.jkytay.xero.ui.modal.InvoiceLineItem
import javax.inject.Inject

interface CollapseInvoiceUseCase {
    operator fun invoke(
        invoiceId: String,
        displayItems: List<InvoiceDisplayRow>
    ): List<InvoiceDisplayRow>
}

internal class CollapseInvoiceUseCaseImpl @Inject constructor() : CollapseInvoiceUseCase {
    override operator fun invoke(
        invoiceId: String,
        displayItems: List<InvoiceDisplayRow>
    ): List<InvoiceDisplayRow> {
        return displayItems.asSequence().mapNotNull {
            val item = it
            if (item is Invoice && item.id == invoiceId) {
                item.copy(isExpand = false)
            } else if (item is InvoiceLineItem && item.invoiceId == invoiceId) {
                null
            } else {
                item
            }
        }.toList()
    }
}
