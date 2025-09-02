package com.jkytay.xero.usecases

import com.jkytay.xero.data.InvoiceRepository
import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.modal.InvoiceTransformer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface FetchInvoicesUseCase {
    suspend operator fun invoke(): List<Invoice>
}

internal class FetchInvoicesUseCaseImpl @Inject constructor(
    private val repository: InvoiceRepository,
    private val transformer: InvoiceTransformer,
) : FetchInvoicesUseCase {
    override suspend operator fun invoke(): List<Invoice> {
        // set fetch in IO dispatch context
        val result = withContext(Dispatchers.IO) {
            repository.fetchInvoices()
        }
        return transformer.parse(result)
    }
}
