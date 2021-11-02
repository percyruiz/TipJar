package com.example.tipjar

import android.app.Application
import com.example.tipjar.di.appModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@HiltAndroidApp
class ApplicationClass : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@ApplicationClass)
      modules(appModule)
    }
  }
}
