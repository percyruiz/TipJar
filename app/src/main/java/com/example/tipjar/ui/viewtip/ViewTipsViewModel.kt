package com.example.tipjar.ui.viewtip

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.tipjar.data.Search
import com.example.tipjar.database.entity.Tip
import com.example.tipjar.repository.TipJarRepository
import kotlinx.coroutines.flow.flatMapLatest

/**
 * Contains logic for viewing and searching tips
 *
 * @property repository data source for [Tip] related data
 * @property handle data source having a persistent variable for search range
 */
class ViewTipsViewModel(
  private val repository: TipJarRepository,
  private val handle: SavedStateHandle
) : ViewModel() {

  init {
    if (!handle.contains(SEARCH)) {
      handle.set(SEARCH, Search())
    }
  }

  val tips = handle.getLiveData<Search>(SEARCH)
    .asFlow()
    .flatMapLatest {
      if(it.start == null && it.end == null) {
        repository.getAllTips()
      } else {
        repository.getTipsWithRange(it.start!!, it.end!!)
      }
    }
    .cachedIn(viewModelScope)

  fun search(start: Long? = null, end: Long? = null) {
    handle.set(SEARCH, Search(start, end))
  }

  companion object {
    const val SEARCH = "com.example.tipjar.SEARCH"
  }
}
