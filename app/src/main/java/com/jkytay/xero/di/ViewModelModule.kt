package com.jkytay.xero.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jkytay.xero.MainActivity
import com.jkytay.xero.ui.InvoicesViewModelImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
internal object ViewModelModule {
    @Provides
    internal fun provideViewModel(activity: MainActivity): ViewModel {
        return ViewModelProvider(activity)[InvoicesViewModelImpl::class]
    }

    @Provides
    internal fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}
