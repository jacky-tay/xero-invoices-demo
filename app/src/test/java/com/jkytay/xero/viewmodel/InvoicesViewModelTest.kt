package com.jkytay.xero.viewmodel

import app.cash.turbine.test
import com.jkytay.xero.data.Action
import com.jkytay.xero.data.AnalyticTracker
import com.jkytay.xero.ui.InvoicesViewModelImpl
import com.jkytay.xero.ui.modal.Divider
import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.modal.InvoiceLineItem
import com.jkytay.xero.ui.modal.InvoiceState
import com.jkytay.xero.usecases.CollapseInvoiceUseCase
import com.jkytay.xero.usecases.ExpandInvoiceUseCase
import com.jkytay.xero.usecases.FetchInvoicesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class InvoicesViewModelTest {
    val testDispatcher = StandardTestDispatcher()

    private lateinit var mockDate: LocalDateTime
    private lateinit var fetchInvoicesUseCase: FetchInvoicesUseCase
    private lateinit var expandInvoiceUseCase: ExpandInvoiceUseCase
    private lateinit var collapseInvoiceUseCase: CollapseInvoiceUseCase
    private lateinit var analyticTracker: AnalyticTracker
    private lateinit var viewModel: InvoicesViewModelImpl

    @Before
    fun setup() {
        mockDate = LocalDateTime.now()

        fetchInvoicesUseCase = mockk(relaxed = true)
        expandInvoiceUseCase = mockk(relaxed = true)
        collapseInvoiceUseCase = mockk(relaxed = true)
        analyticTracker = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `fetch invoice useCase should be invoked when view model is initiated`() =
        runTest(testDispatcher) {
            setupViewModel()
            viewModel.sharedState.test {
                // initially return Loading state
                assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
                // fetchInvoicesUseCase would return empty list
                assertEquals(
                    expected = InvoiceState.ContentReady(displayItems = emptyList()),
                    actual = awaitItem()
                )
                coVerify { fetchInvoicesUseCase.invoke() }
                expectNoEvents()
            }
        }

    // region Invoice State
    @Test
    fun `invoice state should be initiated as Loading`() = runTest(testDispatcher) {
        setupViewModel()
        viewModel.sharedState.test {
            // initially return Loading state
            assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `when fetch invoice return invoices it should set state as ContentReady`() =
        runTest(testDispatcher) {
            val list = listOf(
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
            coEvery { fetchInvoicesUseCase.invoke() } returns list

            setupViewModel()

            // it should also include a divider after invoice
            val expected = InvoiceState.ContentReady(list + listOf(Divider("invoice_0")))
            viewModel.sharedState.test {
                // initially return Loading state
                assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
                assertEquals(expected = expected, actual = awaitItem())
                expectNoEvents()
            }
        }

    @Test
    fun `when fetch invoice return empty invoices it should set state as ContentReady`() =
        runTest(testDispatcher) {
            coEvery { fetchInvoicesUseCase.invoke() } returns emptyList()

            setupViewModel()

            val expected = InvoiceState.ContentReady(emptyList())
            viewModel.sharedState.test {
                // initially return Loading state
                assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
                assertEquals(expected = expected, actual = awaitItem())
                expectNoEvents()
            }
        }

    @Test
    fun `when fetch invoice throws error, it should set state as Error`() =
        runTest(testDispatcher) {
            val exception = Exception("Some error")
            coEvery { fetchInvoicesUseCase.invoke() } throws exception

            setupViewModel()

            val expected = InvoiceState.Error
            viewModel.sharedState.test {
                // initially return Loading state
                assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
                assertEquals(expected = expected, actual = awaitItem())
                expectNoEvents()
            }

            verify {
                analyticTracker.trackError(
                    exception = exception,
                    message = "Fetch invoices"
                )
            }
        }
    // endregion

    // region InvoiceFetchHandler
    @Test
    fun `when state is Error, retry should change state to Loading`() = runTest(testDispatcher) {
        coEvery { fetchInvoicesUseCase.invoke() } throws Exception("Some error")

        setupViewModel()

        viewModel.sharedState.test {
            // initially return Loading state
            assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
            // expecting Exception is received
            assertEquals(expected = InvoiceState.Error, actual = awaitItem())
        }

        viewModel.onRetry()

        verify { analyticTracker.trackUI(event = "onRetry", action = Action.ButtonClick) }


        viewModel.sharedState.test {
            // state change to Loading
            assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
            // fetch should still be error
            assertEquals(expected = InvoiceState.Error, actual = awaitItem())
            expectNoEvents()
        }

        // 2 attempt, first is called when viewModel is init, second call is via onRetry()
        coVerify(exactly = 2) { fetchInvoicesUseCase.invoke() }
    }

    @Test
    fun `when state is Empty, reload should change state to Loading`() = runTest(testDispatcher) {
        coEvery { fetchInvoicesUseCase.invoke() } returns emptyList()

        setupViewModel()

        viewModel.sharedState.test {
            // state change to loading
            assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
            // expecting empty list is received
            assertEquals(expected = InvoiceState.ContentReady(emptyList()), actual = awaitItem())
        }

        viewModel.onReload()

        verify { analyticTracker.trackUI(event = "onReload", action = Action.ButtonClick) }

        viewModel.sharedState.test {
            // state change to Loading
            assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
            // fetch should still be empty list
            assertEquals(expected = InvoiceState.ContentReady(emptyList()), actual = awaitItem())
            expectNoEvents()
        }

        // 2 attempt, first is called when viewModel is init, second call is via onReload()
        coVerify(exactly = 2) { fetchInvoicesUseCase.invoke() }

    }
    // endregion


    // region InvoiceSectionHandler
    @Test
    fun `isInvoiceHeaderExpand return false when invoice section is not expanded`() =
        runTest(testDispatcher)
        {
            val invoice = Invoice(
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
            )
            coEvery { fetchInvoicesUseCase.invoke() } returns listOf(invoice)

            setupViewModel()
            viewModel.sharedState.test {
                // state change to Loading
                assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
                // invoice fetch return ContentReady with Divider
                assertEquals(
                    expected = InvoiceState.ContentReady(
                        displayItems = listOf(
                            invoice,
                            Divider("invoice_0")
                        )
                    ),
                    actual = awaitItem()
                )
                expectNoEvents()
            }

            val actual = viewModel.isInvoiceHeaderExpand("invoice_0")
            assertEquals(expected = false, actual = actual)
        }

    // This is cheating vm, as invoice should always start with collapse mode
    @Test
    fun `isInvoiceHeaderExpand return true when invoice section is expanded`() =
        runTest(testDispatcher) {
            val invoice = Invoice(
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
            )
            coEvery { fetchInvoicesUseCase.invoke() } returns listOf(invoice)

            setupViewModel()
            viewModel.sharedState.test {
                // state change to Loading
                assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
                // invoice fetch return ContentReady with Divider
                assertEquals(
                    expected = InvoiceState.ContentReady(
                        displayItems = listOf(
                            invoice,
                            Divider("invoice_0")
                        )
                    ),
                    actual = awaitItem()
                )
                expectNoEvents()
            }
            val actual = viewModel.isInvoiceHeaderExpand("invoice_0")
            assertEquals(expected = true, actual = actual)
        }

    @Test
    fun `isInvoiceHeaderExpand return false when invoice section is not found`() =
        runTest(testDispatcher) {
            val invoice = Invoice(
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
            )
            coEvery { fetchInvoicesUseCase.invoke() } returns listOf(invoice)

            setupViewModel()
            viewModel.sharedState.test {
                // state change to Loading
                assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
                // invoice fetch return ContentReady with Divider
                assertEquals(
                    expected = InvoiceState.ContentReady(
                        displayItems = listOf(
                            invoice,
                            Divider("invoice_0")
                        )
                    ),
                    actual = awaitItem()
                )
                expectNoEvents()
            }
            val actual = viewModel.isInvoiceHeaderExpand("invoice_1")
            assertEquals(expected = false, actual = actual)
        }

    @Test
    fun `when invoice is collapsed, tapping it should invoke expandInvoiceUseCase`() =
        runTest(testDispatcher) {
            val invoice = Invoice(
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
            )
            coEvery { fetchInvoicesUseCase.invoke() } returns listOf(invoice)

            setupViewModel()
            viewModel.sharedState.test {
                // state change to Loading
                assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
                // invoice fetch return ContentReady with Divider
                assertEquals(
                    expected = InvoiceState.ContentReady(
                        displayItems = listOf(
                            invoice,
                            Divider("invoice_0")
                        )
                    ),
                    actual = awaitItem()
                )
                expectNoEvents()
            }
            viewModel.onInvoiceHeaderClick("invoice_0")

            verify { analyticTracker.trackUI(event = "Invoice Section", action = Action.Expand) }

            viewModel.sharedState.test {
                awaitItem() // return from expandInvoiceUseCase
                expectNoEvents()
            }

            verify {
                expandInvoiceUseCase(
                    invoiceId = "invoice_0",
                    displayItems = listOf(
                        invoice,
                        Divider("invoice_0")
                    )
                )
            }
        }

    // This is cheating vm, as invoice should always start with collapse mode
    @Test
    fun `when invoice is expanded, tapping it should invoke collapseInvoiceUseCase`() =
        runTest(testDispatcher) {
            val invoice = Invoice(
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
            )
            coEvery { fetchInvoicesUseCase.invoke() } returns listOf(invoice)

            setupViewModel()
            viewModel.sharedState.test {
                // state change to Loading
                assertEquals(expected = InvoiceState.Loading, actual = awaitItem())
                // invoice fetch return ContentReady with Divider
                assertEquals(
                    expected = InvoiceState.ContentReady(
                        displayItems = listOf(
                            invoice,
                            Divider("invoice_0")
                        )
                    ),
                    actual = awaitItem()
                )
                expectNoEvents()
            }
            viewModel.onInvoiceHeaderClick("invoice_0")

            verify { analyticTracker.trackUI(event = "Invoice Section", action = Action.Collapse) }

            viewModel.sharedState.test {
                awaitItem() // return from expandInvoiceUseCase
                expectNoEvents()
            }

            verify {
                collapseInvoiceUseCase(
                    invoiceId = "invoice_0",
                    displayItems = listOf(invoice, Divider("invoice_0"))
                )
            }
        }
    // endregion

    private fun setupViewModel() {
        viewModel = InvoicesViewModelImpl(
            fetchInvoicesUseCase = fetchInvoicesUseCase,
            expandInvoiceUseCase = expandInvoiceUseCase,
            collapseInvoiceUseCase = collapseInvoiceUseCase,
            dispatcher = testDispatcher,
            analyticTracker = analyticTracker,
        )
    }
}