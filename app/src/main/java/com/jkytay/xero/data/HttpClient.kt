package com.jkytay.xero.data

import android.content.Context
import com.jkytay.xero.R
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

internal interface HttpClient {
    suspend fun fetchInvoice(): String
}

internal class HttpClientImpl @Inject constructor(
    private val context: Context,
) : HttpClient {
    private var rawResourceIndex = 0
    private val rawResources = listOf(
        R.raw.invoices_empty,
        R.raw.invoices_malformed,
        R.raw.invoices
    )

    override suspend fun fetchInvoice(): String {
        // mimic a network request
        delay(2.seconds)

        val index = if(rawResourceIndex >= rawResources.size) {
            // reset it to fetch empty invoice if it excess list size
            0
        } else {
            rawResourceIndex
        }
        // fetch raw invoice data,
        val resource = context.resources.openRawResource(rawResources[index])

        // then increment index by 1
        rawResourceIndex = index + 1

        var inputStream: InputStreamReader? = null
        var reader: BufferedReader? = null
        var result: String?
        try {
            inputStream = InputStreamReader(resource)
            reader = BufferedReader(inputStream)
            result = reader.readText()
        } finally {
            // close buffered reader
            reader?.close()
            inputStream?.close()
        }
        return result
    }
}
