package com.jkytay.xero.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jkytay.xero.ui.formatter.Formatter
import com.jkytay.xero.ui.modal.InvoiceState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceScreen(
    invoiceState: InvoiceState,
    innerPadding: PaddingValues,
    invoiceFetchHandler: InvoiceFetchHandler,
    sectionHandler: InvoiceSectionHandler,
    formatter: Formatter,
) {
    Crossfade(
        targetState = invoiceState,
        modifier = Modifier.padding(innerPadding)
    ) {
        when (val value = it) {
            is InvoiceState.Loading -> InvoiceLoadingScreen()
            is InvoiceState.Error -> InvoiceErrorScreen(onRetry = invoiceFetchHandler::onRetry)
            is InvoiceState.ContentReady -> {
                if (value.displayItems.isEmpty()) {
                    InvoiceEmptyScreen(onReload = invoiceFetchHandler::onReload)
                } else {
                    InvoiceList(
                        list = value.displayItems,
                        formatter = formatter,
                        onInvoiceHeaderClick = sectionHandler::onInvoiceHeaderClick
                    )
                }
            }
        }
    }
}
