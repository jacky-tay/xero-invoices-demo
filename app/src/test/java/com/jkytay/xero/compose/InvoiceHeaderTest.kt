package com.jkytay.xero.compose

import androidx.compose.ui.test.junit4.createComposeRule
import com.jkytay.xero.compose.robot.InvoiceHeaderRobot
import com.jkytay.xero.compose.utils.MockFormatter
import com.jkytay.xero.ui.InvoiceHeader
import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.modal.InvoiceLineItem
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class InvoiceHeaderTest {
    internal interface InvoiceHeaderOnClick {
        fun onClick(invoiceId: String)
    }

    @get:Rule
    internal val composeTestRule = createComposeRule()

    private val robot: InvoiceHeaderRobot by lazy { InvoiceHeaderRobot(composeTestRule) }

    private lateinit var mockOnClick: InvoiceHeaderOnClick

    @Before
    fun setup() {
        mockOnClick = mockk(relaxed = true)
    }

    // region Collapse mode
    @Test
    fun `verify invoice header with single item in collapse mode`() {
        setupContent(
            Invoice(
                id = "invoice_0",
                date = LocalDateTime.of(
                    /* year = */ 2025,
                    /* month = */ 9,
                    /* dayOfMonth = */ 4,
                    /* hour = */ 19,
                    /* minute = */ 15,
                    /* second = */ 0
                ),
                description = null,
                items = listOf(
                    InvoiceLineItem(
                        invoiceId = "invoice_0",
                        id = "invoice_line_item_0",
                        name = "My service",
                        timeSpentInHour = 2,
                        hourlyRate = 32.0
                    )
                ),
                isExpand = false,
            )
        )

        robot.invoiceHeaderNode(
            id = "invoice_0",
            total = "$64.00",
            date = "Sep 4, 2025 7:15 PM",
            description = null,
            isExpand = false
        ).assertExists()
    }

    @Test
    fun `verify invoice header with single item and description in collapse mode`() {
        setupContent(
            Invoice(
                id = "invoice_0",
                date = LocalDateTime.of(
                    /* year = */ 2025,
                    /* month = */ 9,
                    /* dayOfMonth = */ 4,
                    /* hour = */ 19,
                    /* minute = */ 15,
                    /* second = */ 0
                ),
                description = "This is a single invoice",
                items = listOf(
                    InvoiceLineItem(
                        invoiceId = "invoice_0",
                        id = "invoice_line_item_0",
                        name = "My service",
                        timeSpentInHour = 2,
                        hourlyRate = 32.0
                    )
                ),
                isExpand = false,
            )
        )

        robot.invoiceHeaderNode(
            id = "invoice_0",
            total = "$64.00",
            date = "Sep 4, 2025 7:15 PM",
            description = "This is a single invoice",
            isExpand = false
        ).assertExists()
    }

    @Test
    fun `verify invoice header with 2 items in collapse mode`() {
        setupContent(
            Invoice(
                id = "invoice_0",
                date = LocalDateTime.of(
                    /* year = */ 2025,
                    /* month = */ 9,
                    /* dayOfMonth = */ 4,
                    /* hour = */ 19,
                    /* minute = */ 15,
                    /* second = */ 0
                ),
                description = null,
                items = listOf(
                    InvoiceLineItem(
                        invoiceId = "invoice_0",
                        id = "invoice_line_item_0",
                        name = "My service",
                        timeSpentInHour = 2,
                        hourlyRate = 32.0
                    ),
                    InvoiceLineItem(
                        invoiceId = "invoice_0",
                        id = "invoice_line_item_1",
                        name = "My service #2",
                        timeSpentInHour = 1,
                        hourlyRate = 80.0
                    )
                ),
                isExpand = false,
            )
        )

        robot.invoiceHeaderNode(
            id = "invoice_0",
            total = "$144.00",
            date = "Sep 4, 2025 7:15 PM",
            description = null,
            isExpand = false
        ).assertExists()
    }
    // endregion

    // region Expand mode
    @Test
    fun `verify invoice header with single item in expand mode`() {
        setupContent(
            Invoice(
                id = "invoice_0",
                date = LocalDateTime.of(
                    /* year = */ 2025,
                    /* month = */ 9,
                    /* dayOfMonth = */ 4,
                    /* hour = */ 19,
                    /* minute = */ 15,
                    /* second = */ 0
                ),
                description = null,
                items = listOf(
                    InvoiceLineItem(
                        invoiceId = "invoice_0",
                        id = "invoice_line_item_0",
                        name = "My service",
                        timeSpentInHour = 2,
                        hourlyRate = 32.0
                    )
                ),
                isExpand = true,
            )
        )

        robot.invoiceHeaderNode(
            id = "invoice_0",
            total = "$64.00",
            date = "Sep 4, 2025 7:15 PM",
            description = null,
            isExpand = true
        ).assertExists()
    }

    @Test
    fun `verify invoice header with 2 items in expand mode`() {
        setupContent(
            Invoice(
                id = "invoice_0",
                date = LocalDateTime.of(
                    /* year = */ 2025,
                    /* month = */ 9,
                    /* dayOfMonth = */ 4,
                    /* hour = */ 19,
                    /* minute = */ 15,
                    /* second = */ 0
                ),
                description = null,
                items = listOf(
                    InvoiceLineItem(
                        invoiceId = "invoice_0",
                        id = "invoice_line_item_0",
                        name = "My service",
                        timeSpentInHour = 2,
                        hourlyRate = 32.0
                    ),
                    InvoiceLineItem(
                        invoiceId = "invoice_0",
                        id = "invoice_line_item_1",
                        name = "My service #2",
                        timeSpentInHour = 1,
                        hourlyRate = 80.0
                    )
                ),
                isExpand = true,
            )
        )

        robot.invoiceHeaderNode(
            id = "invoice_0",
            total = "$144.00",
            date = "Sep 4, 2025 7:15 PM",
            description = null,
            isExpand = true
        ).assertExists()
    }
    // endregion

    private fun setupContent(invoice: Invoice) {
        composeTestRule.setContent {
            InvoiceHeader(
                invoice = invoice,
                formatter = MockFormatter,
                onExpandIconClick = mockOnClick::onClick,
            )
        }
    }
}