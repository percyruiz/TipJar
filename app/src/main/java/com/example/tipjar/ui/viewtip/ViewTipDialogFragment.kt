package com.example.tipjar.ui.viewtip

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.tipjar.R
import com.example.tipjar.database.entity.Tip
import com.example.tipjar.databinding.FragmentViewTipDialogBinding
import com.example.tipjar.util.toDate
import org.koin.android.ext.android.inject
import java.io.File

/**
 * DialogFragment for showing viewing the tip details upon clicking on the list item
 */
class ViewTipDialogFragment private constructor(): DialogFragment() {

  private var _binding: FragmentViewTipDialogBinding? = null
  private val binding
    get() = _binding!!

  private var tip: Tip? = null
  private val glide: RequestManager by inject()

  override fun onAttach(context: Context) {
    super.onAttach(context)
    arguments?.getParcelable<Tip>(ARGS_TIP)?.let {
      tip = it
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    _binding = FragmentViewTipDialogBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.dateTextView.text = tip?.timestamp?.toDate()
    binding.totalTextView.text =
      view.context.getString(R.string.tip_item_total, tip?.total)
    binding.tipTextView.text =
      view.context.getString(R.string.tip_item_tip_value, tip?.tipValue)

    tip?.path?.let {
      glide.load(File(it))
        .centerInside()
        .placeholder(R.drawable.ic_no_image)
        .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(binding.photoImageView)
    }
  }

  override fun onStart() {
    super.onStart()
    val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
    dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
  }

  companion object {
    fun newInstance(tip: Tip) = ViewTipDialogFragment().apply {
      arguments = Bundle().apply {
        putParcelable(ARGS_TIP, tip)
      }
    }

    private const val ARGS_TIP = "com.example.tipjar.ARGS_TIP"
  }
}
