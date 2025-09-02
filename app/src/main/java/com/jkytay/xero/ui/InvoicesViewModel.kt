package com.jkytay.xero.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkytay.xero.ui.modal.Divider
import com.jkytay.xero.ui.modal.InvoiceState
import com.jkytay.xero.usecases.FetchInvoicesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface InvoicesViewModel {
    val sharedState: StateFlow<InvoiceState>
}

internal class InvoicesViewModelImpl @Inject constructor(
    private val fetchInvoicesUseCase: FetchInvoicesUseCase,
) : ViewModel(), InvoicesViewModel {

    private val _sharedState: MutableStateFlow<InvoiceState> =
        MutableStateFlow(InvoiceState.Loading)
    override val sharedState: StateFlow<InvoiceState> = _sharedState.asStateFlow()

    private var fetchInvoicesJob: Job? = null

    init {
        fetchInvoices()
    }

    private fun fetchInvoices() {
        // cancel current fetch invoice job, as a new fetch request has been made
        fetchInvoicesJob?.cancel()
        fetchInvoicesJob = viewModelScope.launch {
            try {
                val result = fetchInvoicesUseCase()
                // add a divider at the end of each invoice
                val zipList = result.flatMap { invoice ->
                    listOf(
                        invoice,
                        Divider(invoice.id)
                    )
                }
                _sharedState.update {
                    InvoiceState.ContentReady(zipList)
                }
            } catch (e: Exception) {
                _sharedState.update { InvoiceState.Error }
            }
        }
    }
}
