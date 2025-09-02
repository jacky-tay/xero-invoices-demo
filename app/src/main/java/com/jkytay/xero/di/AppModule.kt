package com.jkytay.xero.di

import android.content.Context
import com.jkytay.xero.data.DateFormatter
import com.jkytay.xero.data.DateFormatterImpl
import com.jkytay.xero.data.HttpClient
import com.jkytay.xero.data.HttpClientImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
        TransformerModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    internal abstract fun provideContext(@ApplicationContext context: Context): Context

    // make sure singleton throughout the entire application lifecycle
    @Singleton
    @Binds
    internal abstract fun bindHttpClient(impl: HttpClientImpl): HttpClient

    // make sure singleton throughout the entire application lifecycle
    @Singleton
    @Binds
    internal abstract fun bindDateFormatter(impl: DateFormatterImpl): DateFormatter
}
