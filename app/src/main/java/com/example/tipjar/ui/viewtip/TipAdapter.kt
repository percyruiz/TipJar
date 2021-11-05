package com.example.tipjar.ui.viewtip

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.example.tipjar.database.entity.Tip

/**
 * Extends PagingDataAdapter to make us of Paging3
 */
class TipAdapter(
  private val glide: RequestManager,
  private val onClick: (tip: Tip) -> Unit
) : PagingDataAdapter<Tip, TipViewHolder>(ITEM_COMP) {

  override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    TipViewHolder.create(parent, onClick, glide)

  companion object {
    private val ITEM_COMP = object : DiffUtil.ItemCallback<Tip>() {
      override fun areItemsTheSame(oldItem: Tip, newItem: Tip) = oldItem.id == newItem.id

      /**
       * Note that in kotlin, == checking on data classes compares all contents, but in Java,
       * typically you'll implement Object#equals, and use it to compare object contents.
       */
      override fun areContentsTheSame(oldItem: Tip, newItem: Tip) = oldItem == newItem
    }
  }
}
