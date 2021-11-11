package com.example.tipjar.di

import android.content.Context
import com.bumptech.glide.Glide
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object GlideModule {

  @Provides
  @Singleton
  fun provideGlide(@ApplicationContext context: Context) = Glide.with(context)
}
