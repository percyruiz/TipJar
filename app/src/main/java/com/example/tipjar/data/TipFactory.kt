package com.example.tipjar.data

import com.example.tipjar.database.entity.Tip

class TipFactory {
  companion object {
    fun createTip(id: Long, timestamp: Long) = Tip(
      id = id,
      total = 1.0f,
      tipValue = 1.0f,
      path = "path",
      timestamp = timestamp
    )
  }
}
