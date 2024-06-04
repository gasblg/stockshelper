package com.github.gasblg.stockshelper.preferences.di

import com.github.gasblg.stockshelper.preferences.UserDataRepository
import com.github.gasblg.stockshelper.preferences.UserDataRepositoryImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UserDataModule {


    @Binds
    fun bindsUserDataRepository(
        userDataRepository: UserDataRepository
    ): UserDataRepositoryImpl

}
