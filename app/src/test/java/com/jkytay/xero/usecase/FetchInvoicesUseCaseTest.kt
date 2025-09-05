package com.jkytay.xero.usecase

import com.jkytay.xero.data.InvoiceRepository
import com.jkytay.xero.data.modal.InvoiceLineItemResponse
import com.jkytay.xero.data.modal.InvoiceResponse
import com.jkytay.xero.data.modal.InvoicesResponse
import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.modal.InvoiceLineItem
import com.jkytay.xero.ui.modal.InvoiceTransformerImpl
import com.jkytay.xero.usecases.FetchInvoicesUseCaseImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class FetchInvoicesUseCaseTest {

    private lateinit var mockDate: LocalDateTime
    private lateinit var invoiceRepository: InvoiceRepository
    private lateinit var useCase: FetchInvoicesUseCaseImpl

    @Before
    fun setup() {
        mockDate = LocalDateTime.now()
        invoiceRepository = mockk(relaxed = true)
        useCase = FetchInvoicesUseCaseImpl(
            repository = invoiceRepository,
            transformer = InvoiceTransformerImpl(
                dateFormat = mockk(relaxed = true) {
                    every { parse(any()) } returns mockDate
                }
            )
        )
    }

    @Test
    fun `invoice response model should be fetched and converted to invoice display model`() = runTest {
        val invoicesResponse = InvoicesResponse(
            items = listOf(
                InvoiceResponse(
                    id = "invoice_0",
                    date = "2022-10-01T10:22:32",
                    description = null,
                    items = listOf(
                        InvoiceLineItemResponse(
                            id = "invoice_item_0",
                            name = "My service",
                            quantity = 2,
                            priceInCents = 2350
                        )
                    )
                ),
            )
        )
        coEvery { invoiceRepository.fetchInvoices() } returns invoicesResponse
        val actual = useCase.invoke()
        val expected = listOf(
            Invoice(
                id = "invoice_0",
                date = mockDate,
                description = null,
                items = listOf(
                    InvoiceLineItem(
                        invoiceId = "invoice_0",
                        id = "invoice_item_0",
                        name = "My service",
                        timeSpentInHour = 2,
                        hourlyRate = 23.5
                    )
                ),
                isExpand = false,
            ),
        )
        assertEquals(expected = expected, actual = actual)
    }
}