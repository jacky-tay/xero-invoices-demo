package com.jkytay.xero.di

import com.jkytay.xero.usecases.CollapseInvoiceUseCase
import com.jkytay.xero.usecases.CollapseInvoiceUseCaseImpl
import com.jkytay.xero.usecases.ExpandInvoiceUseCase
import com.jkytay.xero.usecases.ExpandInvoiceUseCaseImpl
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

    @Binds
    internal abstract fun bindCollapseInvoiceUseCase(
        impl: CollapseInvoiceUseCaseImpl,
    ): CollapseInvoiceUseCase

    @Binds
    internal abstract fun bindExpandInvoiceUseCase(
        impl: ExpandInvoiceUseCaseImpl,
    ): ExpandInvoiceUseCase
}
