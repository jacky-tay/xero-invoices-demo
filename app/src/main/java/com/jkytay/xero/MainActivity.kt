package com.jkytay.xero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jkytay.xero.ui.InvoicesViewModel
import com.jkytay.xero.ui.MainScreen
import com.jkytay.xero.ui.formatter.Formatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    internal lateinit var viewModel: InvoicesViewModel

    @Inject
    internal lateinit var formatter: Formatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen(
                viewModel = viewModel,
                formatter = formatter
            )
        }
    }
}
