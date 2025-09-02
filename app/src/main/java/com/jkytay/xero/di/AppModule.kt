package com.jkytay.xero.di

import android.content.Context
import com.jkytay.xero.data.DateFormatter
import com.jkytay.xero.data.DateFormatterImpl
import com.jkytay.xero.data.HttpClient
import com.jkytay.xero.data.HttpClientImpl
import com.jkytay.xero.data.InvoiceRepository
import com.jkytay.xero.data.InvoiceRepositoryImpl
import com.jkytay.xero.ui.InvoicesViewModel
import com.jkytay.xero.ui.InvoicesViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
        ViewModelModule::class,
        TransformerModule::class,
        UseCasesModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    internal abstract fun provideContext(@ApplicationContext context: Context): Context

    @Binds
    internal abstract fun bindInvoicesViewModel(impl: InvoicesViewModelImpl): InvoicesViewModel

    // make sure singleton throughout the entire application lifecycle
    @Singleton
    @Binds
    internal abstract fun bindHttpClient(impl: HttpClientImpl): HttpClient

    // make sure singleton throughout the entire application lifecycle
    @Singleton
    @Binds
    internal abstract fun bindRepository(impl: InvoiceRepositoryImpl): InvoiceRepository

    // make sure singleton throughout the entire application lifecycle
    @Singleton
    @Binds
    internal abstract fun bindDateFormatter(impl: DateFormatterImpl): DateFormatter
}
