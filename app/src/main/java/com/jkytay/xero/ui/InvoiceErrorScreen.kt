package com.jkytay.xero.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jkytay.xero.R
import com.jkytay.xero.ui.theme.ThemePreviews
import com.jkytay.xero.ui.theme.XeroInvoicesTheme
import com.jkytay.xero.ui.util.Template

@Composable
fun InvoiceErrorScreen(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Template(
        iconContent = {
            Icon(
                imageVector = Icons.TwoTone.Warning,
                contentDescription = stringResource(R.string.invoice_error_icon_content_description),
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error,
            )
        },
        headerContent = {
            Text(text = stringResource(R.string.invoice_error_message))
        },
        secondaryContent = {
            TextButton(onClick = onRetry) {
                Text(text = stringResource(R.string.invoice_error_retry))
            }
        },
        modifier = modifier
    )
}

@ThemePreviews
@Composable
private fun InvoiceErrorScreenPreview() {
    XeroInvoicesTheme {
        InvoiceErrorScreen(onRetry = {})
    }
}
