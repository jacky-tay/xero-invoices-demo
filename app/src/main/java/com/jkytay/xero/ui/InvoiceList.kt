package com.jkytay.xero.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jkytay.xero.ui.formatter.Formatter
import com.jkytay.xero.ui.modal.Divider
import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.modal.InvoiceDisplayRow
import com.jkytay.xero.ui.modal.InvoiceLineItem
import com.jkytay.xero.ui.theme.ThemePreviews
import com.jkytay.xero.ui.theme.XeroInvoicesTheme
import com.jkytay.xero.ui.util.mockFormatter
import com.jkytay.xero.ui.util.mockInvoice
import com.jkytay.xero.ui.util.mockInvoiceLineItem

@Composable
fun InvoiceList(
    list: List<InvoiceDisplayRow>,
    formatter: Formatter,
    onInvoiceHeaderClick: (invoiceId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        list.forEach { row ->
            when (row) {
                // render invoice
                is Invoice -> stickyHeader(
                    key = row.id,
                    contentType = InvoiceListItemType.Header
                ) {
                    InvoiceHeader(
                        invoice = row,
                        formatter = formatter,
                        onExpandIconClick = onInvoiceHeaderClick,
                        modifier = Modifier.animateItem(),
                    )
                }

                // render invoice item
                is InvoiceLineItem -> item(
                    key = row.id,
                    contentType = InvoiceListItemType.Item
                ) {
                    InvoiceItem(
                        invoiceItem = row,
                        formatter = formatter,
                        modifier = Modifier.animateItem(),
                    )
                }

                // render divider
                is Divider -> item(
                    key = "divider_${row.invoiceId}",
                    contentType = InvoiceListItemType.Divider
                ) {
                    HorizontalDivider(
                        modifier = Modifier.animateItem(),
                    )
                }
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
    val list: MutableState<List<InvoiceDisplayRow>> = remember {
        mutableStateOf(
            listOf(
                mockInvoice,
                Divider(invoiceId = "header_id_0")
            )
        )
    }
    val formatter = mockFormatter()
    XeroInvoicesTheme {
        InvoiceList(
            list = list.value,
            formatter = formatter,
            onInvoiceHeaderClick = {
                val mutableList = list.value.toMutableList()
                if (mutableList.contains(mockInvoiceLineItem)) {
                    mutableList.remove(mockInvoiceLineItem)
                } else {
                    mutableList.add(1, mockInvoiceLineItem)
                }
                list.value = mutableList
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}
