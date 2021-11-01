package com.example.tipjar.custom

import android.os.Handler
import android.view.MotionEvent
import android.view.View

class RepeatListener(
  private val initInterval: Long,
  private val normalInterval: Long,
  private val onClick: () -> Unit
) : View.OnTouchListener {

  private val handler = Handler()
  private val runnable: Runnable = object : Runnable {
    override fun run() {
      if (view?.isEnabled == true) {
        handler.postDelayed(this, normalInterval)
        onClick()
      } else {
        handler.removeCallbacks(this)
        view?.isPressed = false
        view = null
      }
    }
  }
  private var view: View? = null

  override fun onTouch(v: View?, event: MotionEvent?): Boolean {
    return when (event?.action) {
      MotionEvent.ACTION_DOWN -> {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, initInterval)
        view = v
        v?.isPressed = true
        onClick()
        true
      }
      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
        handler.removeCallbacks(runnable)
        view?.isPressed = false
        view = null
        false
      }
      else -> false
    }
  }
}
