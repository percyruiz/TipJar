package com.example.tipjar.di

import com.example.tipjar.repository.TipJarRepository
import com.example.tipjar.repository.TipJarRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(ViewModelComponent::class)
@Module
abstract class TipModule {

  @Binds
  @ViewModelScoped
  abstract fun bindTipJarRepository(impl: TipJarRepositoryImpl): TipJarRepository
}

@InstallIn(ViewModelComponent::class)
@Module
object DispatcherModule {

  @Provides
  @ViewModelScoped
  fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
}