package com.example.tipjar

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass : Application() {
  override fun onCreate() {
    super.onCreate()

//    startKoin {
//      androidContext(this@ApplicationClass)
//      modules(appModule)
//    }

    AndroidThreeTen.init(this)
  }
}
