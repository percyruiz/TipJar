package com.example.tipjar.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Contains logic for MainActivity view triggers
 */
@HiltViewModel
class MainSharedViewModel @Inject constructor() : ViewModel() {

  private val _paymentSuccessLiveData = MutableLiveData<Unit>()
  val paymentSuccessLiveData: LiveData<Unit> = _paymentSuccessLiveData

  fun triggerPaymentSuccess() {
    _paymentSuccessLiveData.value = Unit
  }
}
