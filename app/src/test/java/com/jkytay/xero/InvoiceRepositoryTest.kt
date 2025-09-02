package com.jkytay.xero

import com.jkytay.xero.data.AnalyticTracker
import com.jkytay.xero.data.HttpClient
import com.jkytay.xero.data.InvoiceRepositoryImpl
import com.jkytay.xero.data.modal.InvoiceLineItemResponse
import com.jkytay.xero.data.modal.InvoiceResponse
import com.jkytay.xero.data.modal.InvoiceResponseTransformerImpl
import com.jkytay.xero.data.modal.InvoicesResponse
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.collections.emptyList
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class InvoiceRepositoryTest {
    private lateinit var httpClient: HttpClient
    private lateinit var repo: InvoiceRepositoryImpl
    private lateinit var analyticTracker: AnalyticTracker

    @Before
    fun setup() {
        httpClient = mockk(relaxed = true)
        analyticTracker = mockk(relaxed = true)
        repo = InvoiceRepositoryImpl(
            httpClient = httpClient,
            transformer = InvoiceResponseTransformerImpl(),
            analyticTracker = analyticTracker,
        )
    }

    @Test
    fun `should parsed and fetch invoices when httpClient return valid json`() = runTest {
        coEvery { httpClient.fetchInvoice() } returns validJson
        val expected = InvoicesResponse(
            items = listOf(
                InvoiceResponse(
                    id = "f143404a-3e6c-4a61-98d0-5e9c3fe81d80",
                    date = "2022-10-01T10:22:32",
                    description = null,
                    items = listOf(
                        InvoiceLineItemResponse(
                            id = "c100f400-0da3-4161-8f16-dba7d0b4356a",
                            name = "Service #1",
                            quantity = 1,
                            priceInCents = 100,
                        ),
                        InvoiceLineItemResponse(
                            id = "8149d691-c303-48e2-a8b3-8976d7e8489a",
                            name = "Service #2",
                            quantity = 2,
                            priceInCents = 750,
                        ),
                        InvoiceLineItemResponse(
                            id = "202d128a-9bde-4d5f-9a30-2e296091c0a5",
                            name = "Service #3",
                            quantity = 1,
                            priceInCents = 500,
                        )
                    )
                )
            )
        )
        val actual = repo.fetchInvoices()
        assertEquals(expected = expected, actual = actual)
        // verify analytic logger is tracking network duration
        verify { analyticTracker.trackNetwork(event = "fetch invoice", duration = any()) }
    }


    @Test
    fun `should throw Exception when httpClient is malformedJson string`() = runTest {
        coEvery { httpClient.fetchInvoice() } returns malformedJson
        assertFailsWith(Exception::class) {
            repo.fetchInvoices()
        }

        // verify analytic logger is tracking network duration
        verify { analyticTracker.trackNetwork(event = "fetch invoice", duration = any()) }
    }

    @Test
    fun `should return empty list when httpClient is empty list`() = runTest {
        coEvery { httpClient.fetchInvoice() } returns emptyInvoices

        val expected = InvoicesResponse(items = emptyList())
        val actual = repo.fetchInvoices()
        assertEquals(expected = expected, actual = actual)

        // verify analytic logger is tracking network duration
        verify { analyticTracker.trackNetwork(event = "fetch invoice", duration = any()) }
    }

    @Test
    fun `should throw Exception when httpClient is empty string`() = runTest {
        coEvery { httpClient.fetchInvoice() } returns ""
        assertFailsWith(Exception::class) {
            repo.fetchInvoices()
        }

        // verify analytic logger is tracking network duration
        verify { analyticTracker.trackNetwork(event = "fetch invoice", duration = any()) }
    }
}

private val emptyInvoices = """
    {
      "items": []
    }
""".trimIndent()

private val malformedJson = """
    {malformed
        "items": [
            {
                "id": 1,
                "date": "2022-10-01T10:22:32",
            }
""".trimIndent()

private val validJson = """
    {
        "items": [
            {
                "id": "f143404a-3e6c-4a61-98d0-5e9c3fe81d80",
                "date": "2022-10-01T10:22:32",
                "items": [
                    {
                        "id": "c100f400-0da3-4161-8f16-dba7d0b4356a",
                        "name": "Service #1",
                        "quantity": 1,
                        "priceinCents": 100
                    },
                    {
                        "id": "8149d691-c303-48e2-a8b3-8976d7e8489a",
                        "name": "Service #2",
                        "quantity": 2,
                        "priceinCents": 750
                    },
                    {
                        "id": "202d128a-9bde-4d5f-9a30-2e296091c0a5",
                        "name": "Service #3",
                        "quantity": 1,
                        "priceinCents": 500
                    }
                ]
            }
        ]
    }
""".trimIndent()
