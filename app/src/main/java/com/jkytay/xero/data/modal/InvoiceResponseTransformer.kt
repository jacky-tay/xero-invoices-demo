package com.jkytay.xero.data.modal

import kotlinx.serialization.json.Json
import javax.inject.Inject

internal interface InvoiceResponseTransformer {
    fun parse(json: String): InvoicesResponse
}

internal class InvoiceResponseTransformerImpl @Inject constructor() : InvoiceResponseTransformer {
    private val jsonParser = Json {
        explicitNulls = false
    }

    override fun parse(json: String): InvoicesResponse {
        return jsonParser.decodeFromString<InvoicesResponse>(json)
    }
}
