package com.example.tipjar.util

import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

fun Long.toDate(): String? {
  return try {
    val date = LocalDateTime.ofInstant(
      Instant.ofEpochMilli(this),
      DateTimeUtils.toZoneId(TimeZone.getDefault())
    )
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/d")
    date.format(formatter)
  } catch (e: Exception) {
    e.toString()
  }
}
