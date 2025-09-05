package com.jkytay.xero.usecase

import com.jkytay.xero.data.AnalyticTracker
import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.modal.InvoiceDisplayRow
import com.jkytay.xero.ui.modal.InvoiceLineItem
import com.jkytay.xero.usecases.ExpandInvoiceUseCaseImpl
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpandInvoiceUseCaseTest {
    private lateinit var useCase: ExpandInvoiceUseCaseImpl
    private lateinit var mockDate: LocalDateTime
    private lateinit var analyticTracker: AnalyticTracker

    @Before
    fun setup() {
        analyticTracker = mockk(relaxed = true)
        mockDate = LocalDateTime.now()
        useCase = ExpandInvoiceUseCaseImpl(analyticTracker = analyticTracker)
    }

    @Test
    fun `collapsed invoice should expand when invoked`() {
        val input = listOf(
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
        val actual = useCase.invoke(invoiceId = "invoice_0", displayItems = input)
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
                isExpand = true,
            ),
            InvoiceLineItem(
                invoiceId = "invoice_0",
                id = "invoice_item_0",
                name = "My service",
                timeSpentInHour = 2,
                hourlyRate = 23.5
            ),
        )
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun `analytic should be tracked when invoice line items are already rendered in collapse mode`() {
        val input = listOf(
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
                isExpand = true,
            ),
            InvoiceLineItem(
                invoiceId = "invoice_0",
                id = "invoice_item_0",
                name = "My service",
                timeSpentInHour = 2,
                hourlyRate = 23.5
            ),
        )
        useCase.invoke(invoiceId = "invoice_0", displayItems = input)
        verify { analyticTracker.trackError(message = "Invoice line items are already rendered in collapsed mode") }
    }

    @Test
    fun `analytic should be tracked when no invoice is found`() {
        val input = emptyList<InvoiceDisplayRow>()
        val actual = useCase.invoke(invoiceId = "invoice_0", displayItems = input)
        verify { analyticTracker.trackError(message = "Invoice id is not found in display list") }
        // same list should be returned
        assertEquals(expected = input, actual = actual)
    }
}
