package com.example.tipjar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tipjar.databinding.FragmentAddTipBinding

class AddTipFragment: Fragment() {

  private var _binding: FragmentAddTipBinding? = null
  private val binding
    get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentAddTipBinding.inflate(inflater, container, false)

    return binding.root
  }
}
