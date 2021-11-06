package com.example.tipjar.ui.viewtip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.tipjar.R
import com.example.tipjar.database.entity.Tip
import com.example.tipjar.databinding.TipItemBinding
import com.example.tipjar.util.toDate
import java.io.File

/**
 * ViewHolder for binding tip item
 */
class TipViewHolder(
  private val view: View,
  onClick: (tip: Tip) -> Unit,
  private val glide: RequestManager
) :
  RecyclerView.ViewHolder(view) {

  var tip: Tip? = null
  private val binding = TipItemBinding.bind(itemView)
  private val dateTextView = binding.dateTextView
  private val totalTextView = binding.totalTextView
  private val tipTextView = binding.tipTextView
  private val photoThumbImageView = binding.photoThumbImageView

  init {
    view.setOnClickListener {
      onClick.invoke(tip!!)
    }
  }

  fun bind(tip: Tip?) {
    tip?.apply {
      this@TipViewHolder.tip = this
      this@TipViewHolder.dateTextView.text = this.timestamp.toDate()
      this@TipViewHolder.totalTextView.text =
        view.context.getString(R.string.tip_item_total, tip.total)
      this@TipViewHolder.tipTextView.text =
        view.context.getString(R.string.tip_item_tip_value, tip.tipValue)

      if (this.path != null) {
        glide.load(File(this.path))
          .centerInside()
          .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
          .placeholder(R.drawable.ic_no_image)
          .thumbnail(0.2f)
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .into(photoThumbImageView)
      } else {
        photoThumbImageView.setImageDrawable(
          ContextCompat.getDrawable(
            view.context,
            R.drawable.ic_no_image
          )
        )
      }
    }
  }

  companion object {
    fun create(
      parent: ViewGroup,
      onClick: (tip: Tip) -> Unit,
      glide: RequestManager
    ): TipViewHolder {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.tip_item, parent, false)
      return TipViewHolder(view, onClick, glide)
    }
  }
}
