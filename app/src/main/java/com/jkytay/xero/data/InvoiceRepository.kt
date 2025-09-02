package com.jkytay.xero.data

import com.jkytay.xero.data.modal.InvoiceResponseTransformer
import com.jkytay.xero.data.modal.InvoicesResponse
import javax.inject.Inject

internal interface InvoiceRepository {
    suspend fun fetchInvoices(): InvoicesResponse
}

internal class InvoiceRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val transformer: InvoiceResponseTransformer,
) : InvoiceRepository {
    override suspend fun fetchInvoices(): InvoicesResponse {
        val result = httpClient.fetchInvoice()
        return transformer.parse(result)
    }
}
