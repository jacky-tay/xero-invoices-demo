package com.jkytay.xero.di

import com.jkytay.xero.data.modal.InvoiceResponseTransformer
import com.jkytay.xero.data.modal.InvoiceResponseTransformerImpl
import com.jkytay.xero.ui.modal.InvoiceTransformer
import com.jkytay.xero.ui.modal.InvoiceTransformerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TransformerModule() {
    @Binds
    internal abstract fun bindInvoiceResponseTransformer(
        impl: InvoiceResponseTransformerImpl
    ): InvoiceResponseTransformer

    @Binds
    internal abstract fun bindInvoiceTransformer(impl: InvoiceTransformerImpl): InvoiceTransformer
}
