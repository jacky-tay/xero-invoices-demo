package com.jkytay.xero.di

import com.jkytay.xero.usecases.FetchInvoicesUseCase
import com.jkytay.xero.usecases.FetchInvoicesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCasesModule() {
    @Binds
    internal abstract fun bindFetchInvoiceUseCase(
        impl: FetchInvoicesUseCaseImpl,
    ): FetchInvoicesUseCase
}
