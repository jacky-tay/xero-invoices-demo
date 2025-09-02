package com.jkytay.xero.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jkytay.xero.ui.formatter.Formatter
import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.theme.ThemePreviews
import com.jkytay.xero.ui.theme.XeroInvoicesTheme
import com.jkytay.xero.ui.util.mockFormatter
import com.jkytay.xero.ui.util.mockInvoice

@Composable
fun InvoiceList(
    list: List<Invoice>,
    formatter: Formatter,
    onInvoiceHeaderClick: (invoiceId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        list.forEach { invoice ->
            stickyHeader(
                key = invoice.id,
                contentType = InvoiceListItemType.Header
            ) {
                InvoiceHeader(
                    invoice = invoice,
                    formatter = formatter,
                    onExpandIconClick = onInvoiceHeaderClick,
                    modifier = Modifier.animateItem(),
                )
            }
            if (invoice.isExpand) {
                items(
                    items = invoice.items,
                    key = { it.id },
                    contentType = { InvoiceListItemType.Item }) {
                    InvoiceItem(
                        invoiceItem = it,
                        formatter = formatter,
                        modifier = Modifier.animateItem(),
                    )
                }
            }
            item(
                key = "divider_${invoice.id}",
                contentType = InvoiceListItemType.Divider
            ) {
                HorizontalDivider(
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

private enum class InvoiceListItemType {
    Header, Item, Divider
}

/**
 * Mock invoice list preview,
 * invoice header starts in collapse mode
 * when expand icon is clicked, it insert item to list
 */
@ThemePreviews
@Composable
private fun InvoiceListPreview() {
    val list = remember {
        mutableStateListOf(mockInvoice)
    }
    val formatter = mockFormatter()
    XeroInvoicesTheme {
        InvoiceList(
            list = list,
            formatter = formatter,
            onInvoiceHeaderClick = {
                list[0] = list[0].copy(isExpand = list[0].isExpand.not())
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}
