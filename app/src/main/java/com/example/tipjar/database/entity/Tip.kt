package com.example.tipjar.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TipJar entity
 */
@Entity(tableName = "tip")
data class Tip(
  @PrimaryKey(autoGenerate = true) val id: Long? = null,
  val total: String,
  @ColumnInfo(name = "tip_value") val tipValue: String,
  val path: String?,
  val timestamp: Long
)
