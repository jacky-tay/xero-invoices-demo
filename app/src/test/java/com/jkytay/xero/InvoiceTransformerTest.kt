package com.jkytay.xero

import com.jkytay.xero.data.modal.InvoiceLineItemResponse
import com.jkytay.xero.data.modal.InvoiceResponse
import com.jkytay.xero.data.modal.InvoicesResponse
import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.modal.InvoiceLineItem
import com.jkytay.xero.ui.modal.InvoiceTransformerImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.Before
import java.time.LocalDateTime
import kotlin.test.assertEquals

class InvoiceTransformerTest {

    private lateinit var impl: InvoiceTransformerImpl
    private lateinit var mockDate: LocalDateTime

    @Before
    fun setup() {
        mockDate = LocalDateTime.now()
        impl = InvoiceTransformerImpl(
            dateFormat = mockk(relaxed = true) {
                every { parse(any()) } returns mockDate
            }
        )
    }

    @Test
    fun `assert invoice response model can be converted to model`() {
        val input = InvoicesResponse(
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
                InvoiceResponse(
                    id = "invoice_1",
                    date = "2022-10-05T13:00:10",
                    description = "There is no invoice line items",
                    items = emptyList()
                )
            )
        )
        val actual = impl.parse(input)
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
            Invoice(
                id = "invoice_1",
                date = mockDate,
                description = "There is no invoice line items",
                items = emptyList(),
                isExpand = false,
            ),
        )
        assertEquals(expected = expected, actual = actual)
    }
}