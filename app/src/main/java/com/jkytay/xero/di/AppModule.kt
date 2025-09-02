package com.jkytay.xero.di

import com.jkytay.xero.data.DateFormatter
import com.jkytay.xero.data.DateFormatterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
        TransformerModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    // make sure singleton throughout the entire application lifecycle
    @Singleton
    @Binds
    internal abstract fun bindDateFormatter(impl: DateFormatterImpl): DateFormatter
}
