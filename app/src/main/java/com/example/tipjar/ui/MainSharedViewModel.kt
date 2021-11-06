package com.example.tipjar.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Contains logic for MainActivity view triggers
 */
class MainSharedViewModel : ViewModel() {

  private val _paymentSuccessLiveData = MutableLiveData<Unit>()
  val paymentSuccessLiveData: LiveData<Unit> = _paymentSuccessLiveData

  fun triggerPaymentSuccess() {
    _paymentSuccessLiveData.value = Unit
  }
}
