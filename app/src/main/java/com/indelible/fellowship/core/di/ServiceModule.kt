package com.indelible.fellowship.core.di

import com.indelible.fellowship.core.service.AccountService
import com.indelible.fellowship.core.service.MessageService
import com.indelible.fellowship.core.service.StorageService
import com.indelible.fellowship.core.service.impl.AccountServiceImpl
import com.indelible.fellowship.core.service.impl.MessageServiceImpl
import com.indelible.fellowship.core.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule{
    @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService
    @Binds abstract fun provideMessageService(impl: MessageServiceImpl): MessageService
    @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService
}