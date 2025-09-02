package com.jkytay.xero.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jkytay.xero.ui.theme.ThemePreviews
import com.jkytay.xero.ui.theme.XeroInvoicesTheme
import com.jkytay.xero.ui.util.shimmerLoading

@Composable
fun InvoiceLoadingScreen() {
    Column {
        repeat(3) {
            InvoiceHeaderLoading()
        }
    }
}

@Composable
private fun InvoiceHeaderLoading() {
    ListItem(
        headlineContent = {
            Column {
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .shimmerLoading()
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                )
            }
        },
        overlineContent = {
            Box(
                modifier = Modifier
                    .shimmerLoading()
                    .fillMaxWidth(0.3f)
                    .height(12.dp)
            )
        }
    )
}

@ThemePreviews
@Composable
private fun InvoiceLoadingScreenPreview() {
    XeroInvoicesTheme {
        InvoiceLoadingScreen()
    }
}
