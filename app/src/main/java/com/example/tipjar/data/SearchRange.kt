package com.example.tipjar.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Search(val start: Long? = null, val end: Long? = null) : Parcelable