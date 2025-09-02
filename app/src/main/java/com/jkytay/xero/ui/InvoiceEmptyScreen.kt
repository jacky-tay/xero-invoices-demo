package com.jkytay.xero.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ShoppingCart
import androidx.compose.material3.Icon
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
fun InvoiceEmptyScreen(
    modifier: Modifier = Modifier,
    onReload: () -> Unit
) {
    Template(
        iconContent = {
            Icon(
                imageVector = Icons.TwoTone.ShoppingCart,
                contentDescription = stringResource(R.string.invoice_empty_icon_content_description),
                modifier = Modifier.size(64.dp),
            )
        },
        headerContent = {
            Text(text = stringResource(R.string.invoice_empty_message))
        },
        secondaryContent = {
            TextButton(onClick = onReload) {
                Text(stringResource(R.string.invoice_empty_reload))
            }
        },
        modifier = modifier
    )
}

@ThemePreviews
@Composable
private fun InvoiceEmptyScreenPreview() {
    XeroInvoicesTheme {
        InvoiceEmptyScreen(onReload = {})
    }
}
