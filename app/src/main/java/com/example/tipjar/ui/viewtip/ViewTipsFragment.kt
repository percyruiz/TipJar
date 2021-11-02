package com.example.tipjar.ui.viewtip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tipjar.databinding.FragmentAddTipBinding
import com.example.tipjar.databinding.FragmentViewTipsBinding

class ViewTipsFragment: Fragment() {

  private var _binding: FragmentViewTipsBinding? = null
  private val binding
    get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentViewTipsBinding.inflate(inflater, container, false)

    return binding.root
  }
}
