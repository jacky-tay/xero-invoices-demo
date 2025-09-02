package com.jkytay.xero.data

import com.jkytay.xero.data.modal.InvoiceResponseTransformer
import com.jkytay.xero.data.modal.InvoicesResponse
import javax.inject.Inject
import kotlin.time.measureTime

internal interface InvoiceRepository {
    suspend fun fetchInvoices(): InvoicesResponse
}

internal class InvoiceRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val transformer: InvoiceResponseTransformer,
    private val analyticTracker: AnalyticTracker,
) : InvoiceRepository {
    override suspend fun fetchInvoices(): InvoicesResponse {
        var result: String
        val duration = measureTime {
            result = httpClient.fetchInvoice()
        }
        analyticTracker.trackNetwork(event = "fetch invoice", duration = duration)
        return transformer.parse(result)
    }
}
