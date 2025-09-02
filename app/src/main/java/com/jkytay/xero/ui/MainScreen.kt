package com.jkytay.xero.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jkytay.xero.R
import com.jkytay.xero.ui.formatter.Formatter
import com.jkytay.xero.ui.modal.InvoiceState
import com.jkytay.xero.ui.theme.ThemePreviews
import com.jkytay.xero.ui.theme.XeroInvoicesTheme
import com.jkytay.xero.ui.util.mockFormatter
import com.jkytay.xero.ui.util.mockInvoice
import com.jkytay.xero.ui.util.mockInvoiceLineItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: InvoicesViewModel, formatter: Formatter) {
    val state by viewModel.sharedState.collectAsState()
    XeroInvoicesTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.app_name))
                    }
                )
            }
        ) { innerPadding ->
            InvoiceScreen(
                invoiceState = state,
                innerPadding = innerPadding,
                invoiceFetchHandler = viewModel,
                sectionHandler = viewModel,
                formatter = formatter,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun MainScreenPreview(
    @PreviewParameter(InvoicePreviewProvider::class) state: InvoiceState
) {
    val viewModel = rememberMockInvoicesViewModel(state)
    val formatter = mockFormatter()
    MainScreen(viewModel = viewModel, formatter = formatter)
}

private class InvoicePreviewProvider : PreviewParameterProvider<InvoiceState> {
    override val values: Sequence<InvoiceState>
        get() = sequenceOf(
            InvoiceState.Loading,
            InvoiceState.Error,
            InvoiceState.ContentReady(displayItems = emptyList()),
            InvoiceState.ContentReady(
                displayItems = listOf(
                    mockInvoice
                )
            ),
            InvoiceState.ContentReady(
                displayItems = listOf(
                    mockInvoice.copy(isExpand = true)
                )
            ),
        )
}

@Composable
private fun rememberMockInvoicesViewModel(state: InvoiceState): InvoicesViewModel {
    return remember {
        object : InvoicesViewModel {
            override val sharedState: StateFlow<InvoiceState> =
                MutableStateFlow(state).asStateFlow()

            override fun onRetry() = Unit

            override fun onReload() = Unit

            override fun onInvoiceHeaderClick(id: String) = Unit
        }
    }
}
