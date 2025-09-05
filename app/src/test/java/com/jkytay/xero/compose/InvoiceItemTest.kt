package com.jkytay.xero.compose

import androidx.compose.ui.test.junit4.createComposeRule
import com.jkytay.xero.compose.robot.InvoiceItemRobot
import com.jkytay.xero.compose.utils.MockFormatter
import com.jkytay.xero.ui.InvoiceItem
import com.jkytay.xero.ui.modal.InvoiceLineItem
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class InvoiceItemTest {
    @get:Rule
    internal val composeTestRule = createComposeRule()
    private val robot: InvoiceItemRobot by lazy { InvoiceItemRobot(composeTestRule) }

    @Test
    fun `verify invoice item with duration 1 hour`() {
        setupContent(
            invoiceLineItem = InvoiceLineItem(
                invoiceId = "invoice_0",
                id = "invoice_line_item_0",
                name = "My service",
                timeSpentInHour = 1,
                hourlyRate = 12.50
            )
        )
        robot.invoiceLineItemNode(
            hourRate = "$12.50",
            name = "My service",
            duration = 1,
            total = "$12.50"
        ).assertExists()
    }

    @Test
    fun `verify invoice item with duration 2 hours`() {
        setupContent(
            invoiceLineItem = InvoiceLineItem(
                invoiceId = "invoice_0",
                id = "invoice_line_item_0",
                name = "My service",
                timeSpentInHour = 2,
                hourlyRate = 12.50
            )
        )
        robot.invoiceLineItemNode(
            hourRate = "$12.50",
            name = "My service",
            duration = 2,
            total = "$25.00"
        ).assertExists()
    }

    private fun setupContent(invoiceLineItem: InvoiceLineItem) {
        composeTestRule.setContent {
            InvoiceItem(
                invoiceItem = invoiceLineItem,
                formatter = MockFormatter,
            )
        }
    }
}
