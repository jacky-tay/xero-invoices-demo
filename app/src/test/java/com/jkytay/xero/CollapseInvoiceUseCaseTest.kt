package com.jkytay.xero

import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.modal.InvoiceLineItem
import com.jkytay.xero.usecases.CollapseInvoiceUseCaseImpl
import org.junit.Before
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class CollapseInvoiceUseCaseTest {
    private lateinit var useCase: CollapseInvoiceUseCaseImpl

    private lateinit var mockDate: LocalDateTime

    @Before
    fun setup() {
        mockDate = LocalDateTime.now()
        useCase = CollapseInvoiceUseCaseImpl()
    }

    @Test
    fun `expanded invoice should collapse when invoked`() {
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
                isExpand = false,
            ),
        )
        assertEquals(expected = expected, actual = actual)
    }
}
