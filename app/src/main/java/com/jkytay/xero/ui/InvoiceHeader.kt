package com.jkytay.xero.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jkytay.xero.R
import com.jkytay.xero.ui.formatter.Formatter
import com.jkytay.xero.ui.modal.Invoice
import com.jkytay.xero.ui.modal.InvoiceLineItem
import com.jkytay.xero.ui.theme.ThemePreviews
import com.jkytay.xero.ui.theme.XeroInvoicesTheme
import com.jkytay.xero.ui.util.mockFormatter
import com.jkytay.xero.ui.util.mockInvoice
import java.time.LocalDateTime

@Composable
fun InvoiceHeader(
    invoice: Invoice,
    formatter: Formatter,
    onExpandIconClick: (invoiceId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val total = remember(formatter, invoice.items) {
        val overallCost = invoice.items.sumOf { it.timeSpentInHour * it.hourlyRate }
        formatter.formatCurrency(overallCost)
    }
    val date = remember(formatter, invoice.date) {
        formatter.formatDate(invoice.date)
    }

    ListItem(
        headlineContent = {
            Text(text = date)
        },
        overlineContent = {
            Text(
                text = stringResource(R.string.invoice_header_total, total),
                color = MaterialTheme.colorScheme.primary
            )
        },
        supportingContent = if (invoice.description != null) {
            {
                Text(
                    text = invoice.description
                )
            }
        } else {
            null
        },
        trailingContent = {
            IconButton(onClick = { onExpandIconClick(invoice.id) }) {
                Icon(
                    imageVector = if (invoice.isExpand) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = stringResource(
                        if (invoice.isExpand) {
                            R.string.invoice_header_expanded
                        } else {
                            R.string.invoice_header_collapsed
                        },
                        invoice.id
                    )
                )
            }
        },
        modifier = modifier
    )
}

@ThemePreviews
@Composable
private fun InvoiceHeaderPreview() {
    val formatter = mockFormatter()
    XeroInvoicesTheme {
        InvoiceHeader(
            invoice = mockInvoice,
            formatter = formatter,
            onExpandIconClick = { /* NO OP */ }
        )
    }
}
