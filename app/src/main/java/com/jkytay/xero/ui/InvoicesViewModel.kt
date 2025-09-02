package com.jkytay.xero.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkytay.xero.usecases.FetchInvoicesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface InvoicesViewModel

internal class InvoicesViewModelImpl @Inject constructor(
    private val fetchInvoicesUseCase: FetchInvoicesUseCase,
) : ViewModel(), InvoicesViewModel {
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
            } catch (e: Exception) {
            }
        }
    }
}
