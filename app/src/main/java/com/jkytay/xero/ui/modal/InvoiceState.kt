package com.jkytay.xero.ui.modal

sealed class InvoiceState {
    /**
     * State when invoice is loading
     */
    data object Loading : InvoiceState()

    /**
     * State when error has occurred during or after invoice fetch
     * could be network error, or parsing error
     */
    data object Error : InvoiceState()

    /**
     * State when invoice is ready
     */
    data class ContentReady(val displayItems: List<InvoiceDisplayRow>) : InvoiceState()
}
