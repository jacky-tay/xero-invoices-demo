package com.jkytay.xero.ui.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jkytay.xero.ui.theme.ThemePreviews
import com.jkytay.xero.ui.theme.XeroInvoicesTheme

@Composable
fun Template(
    iconContent: @Composable () -> Unit,
    headerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    secondaryContent: (@Composable () -> Unit)? = null,
) {
    Surface {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                iconContent()
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.headlineMedium.copy(
                        textAlign = TextAlign.Center
                    )
                ) {
                    headerContent()
                }
                secondaryContent?.invoke()
            }
        }
    }
}

@ThemePreviews
@Composable
private fun TemplatePreview() {
    XeroInvoicesTheme {
        Template(
            iconContent = {
                Icon(
                    imageVector = Icons.TwoTone.AccountBox,
                    contentDescription = "Account box",
                    modifier = Modifier.size(64.dp),
                )
            },
            headerContent = {
                Text(text = "This is an Account box")
            }
        )
    }
}
