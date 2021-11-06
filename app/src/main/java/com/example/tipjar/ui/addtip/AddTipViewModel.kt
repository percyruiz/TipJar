package com.example.tipjar.ui.addtip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipjar.data.Result
import com.example.tipjar.repository.TipJarRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Contains logic for adding tips
 *
 * @property repository data source for [Tip] related data
 */
class AddTipViewModel(
  private val repository: TipJarRepository,
  private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

  private val _addTipFlow = Channel<Result<Boolean>>(Channel.BUFFERED)
  val addTipFlow = _addTipFlow.receiveAsFlow()

  @InternalCoroutinesApi
  fun saveTip(amount: Float, tipPercentage: Float, photoPath: String?) {
    val tip = (tipPercentage / 100) * amount
    val total = amount + tip
    viewModelScope.launch(dispatcher) {
      repository.addTip(total, tip, photoPath)
        .catch { e ->
          _addTipFlow.send(Result.error(e.toString()))
        }
        .collect {
          _addTipFlow.send(it)
        }
    }
  }
}
