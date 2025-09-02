package com.jkytay.xero.ui

import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.jkytay.xero.R
import com.jkytay.xero.ui.formatter.Formatter
import com.jkytay.xero.ui.modal.InvoiceLineItem
import com.jkytay.xero.ui.theme.ThemePreviews
import com.jkytay.xero.ui.theme.XeroInvoicesTheme
import com.jkytay.xero.ui.util.mockFormatter
import com.jkytay.xero.ui.util.mockInvoiceLineItem

@Composable
fun InvoiceItem(
    invoiceItem: InvoiceLineItem,
    formatter: Formatter,
    modifier: Modifier = Modifier
) {
    val formattedRate = remember(formatter, invoiceItem.hourlyRate) {
        formatter.formatCurrency(invoiceItem.hourlyRate)
    }
    val formattedTotal = remember(formatter, invoiceItem.hourlyRate, invoiceItem.timeSpentInHour) {
        formatter.formatCurrency(invoiceItem.hourlyRate * invoiceItem.timeSpentInHour)
    }

    ListItem(
        headlineContent = {
            Text(text = invoiceItem.name)
        },
        overlineContent = {
            Text(text = stringResource(R.string.invoice_item_hourly_rate, formattedRate))
        },
        supportingContent = {
            Text(
                text = pluralStringResource(
                    R.plurals.invoice_item_hour_quantity,
                    invoiceItem.timeSpentInHour,
                    invoiceItem.timeSpentInHour
                )
            )
        },
        trailingContent = {
            Text(
                text = formattedTotal,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
        },
        modifier = modifier
    )
}

@ThemePreviews
@Composable
private fun InvoiceItemPreview() {
    val formatter = mockFormatter()
    XeroInvoicesTheme {
        InvoiceItem(
            invoiceItem = mockInvoiceLineItem,
            formatter = formatter,
        )
    }
}
