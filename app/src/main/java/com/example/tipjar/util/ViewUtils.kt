package com.example.tipjar.util

import android.os.SystemClock
import android.view.View

fun View.debounce(debounceTime: Long = 999L, action: () -> Unit) {
  this.setOnClickListener(object : View.OnClickListener {
    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
      if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
      else action()

      lastClickTime = SystemClock.elapsedRealtime()
    }
  })
}
