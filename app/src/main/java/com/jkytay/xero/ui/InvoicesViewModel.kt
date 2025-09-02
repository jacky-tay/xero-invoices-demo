package com.jkytay.xero.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkytay.xero.data.Action
import com.jkytay.xero.data.AnalyticTracker
import com.jkytay.xero.ui.modal.Divider
import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.modal.InvoiceState
import com.jkytay.xero.usecases.CollapseInvoiceUseCase
import com.jkytay.xero.usecases.ExpandInvoiceUseCase
import com.jkytay.xero.usecases.FetchInvoicesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface InvoiceSectionHandler {
    fun isInvoiceHeaderExpand(id: String): Boolean

    fun onInvoiceHeaderClick(id: String)
}

interface InvoiceFetchHandler {
    fun onRetry()

    fun onReload()
}

interface InvoicesViewModel : InvoiceFetchHandler, InvoiceSectionHandler {
    val sharedState: StateFlow<InvoiceState>
}

internal class InvoicesViewModelImpl @Inject constructor(
    private val fetchInvoicesUseCase: FetchInvoicesUseCase,
    private val expandInvoiceUseCase: ExpandInvoiceUseCase,
    private val collapseInvoiceUseCase: CollapseInvoiceUseCase,
    private val dispatcher: CoroutineDispatcher,
    private val analyticTracker: AnalyticTracker,
) : ViewModel(), InvoicesViewModel {

    private val _sharedState: MutableStateFlow<InvoiceState> =
        MutableStateFlow(InvoiceState.Loading)
    override val sharedState: StateFlow<InvoiceState> = _sharedState.asStateFlow()

    private var fetchInvoicesJob: Job? = null

    init {
        fetchInvoices()
    }

    // region invoice section
    override fun onRetry() {
        analyticTracker.trackUI("onRetry", Action.ButtonClick)
        // change state to Loading from Error
        _sharedState.update { InvoiceState.Loading }
        fetchInvoices()
    }

    override fun onReload() {
        analyticTracker.trackUI("onReload", Action.ButtonClick)
        // change state to Loading from ContentReady(Empty)
        _sharedState.update { InvoiceState.Loading }
        fetchInvoices()
    }
    // endregion

    // region invoice section
    override fun isInvoiceHeaderExpand(id: String): Boolean {
        return (_sharedState.value as? InvoiceState.ContentReady)
            ?.displayItems
            ?.any { it is Invoice && it.id == id && it.isExpand }
            ?: false
    }

    override fun onInvoiceHeaderClick(id: String) {
        val list = (_sharedState.value as? InvoiceState.ContentReady)?.displayItems ?: return
        val newList = if (isInvoiceHeaderExpand(id)) {
            analyticTracker.trackUI("Invoice Section", Action.Collapse)
            collapseInvoiceUseCase(invoiceId = id, displayItems = list)
        } else {
            analyticTracker.trackUI("Invoice Section", Action.Expand)
            expandInvoiceUseCase(invoiceId = id, displayItems = list)
        }
        _sharedState.update { InvoiceState.ContentReady(newList) }
    }
    // endregion

    private fun fetchInvoices() {
        // cancel current fetch invoice job, as a new fetch request has been made
        fetchInvoicesJob?.cancel()
        fetchInvoicesJob = viewModelScope.launch(dispatcher) {
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
                analyticTracker.trackError(exception = e, message = "Fetch invoices")
                _sharedState.update { InvoiceState.Error }
            }
        }
    }
}
