package com.example.tipjar.di

import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.tipjar.database.TipDatabase
import com.example.tipjar.repository.TipJarRepository
import com.example.tipjar.repository.TipJarRepositoryImpl
import com.example.tipjar.ui.MainSharedViewModel
import com.example.tipjar.ui.addtip.AddTipViewModel
import com.example.tipjar.ui.viewtip.ViewTipsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Handles dependency injection using Koin
 */
val appModule = module {

  single {
    Room.databaseBuilder(
      androidApplication(),
      TipDatabase::class.java, "tipjarapp"
    ).build()
  }

  single<TipJarRepository> { TipJarRepositoryImpl(db = get()) }

  viewModel { AddTipViewModel(repository = get()) }

  viewModel { MainSharedViewModel() }

  viewModel { ViewTipsViewModel(repository = get(), handle = get()) }

  single { Glide.with(androidContext()) }
}
