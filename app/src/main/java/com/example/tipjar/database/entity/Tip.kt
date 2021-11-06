package com.example.tipjar.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * TipJar entity
 */
@Entity(tableName = "tip")
@Parcelize
data class Tip(
  @PrimaryKey(autoGenerate = true) val id: Long? = null,
  val total: Float,
  @ColumnInfo(name = "tip_value") val tipValue: Float,
  val path: String?,
  val timestamp: Long
): Parcelable
