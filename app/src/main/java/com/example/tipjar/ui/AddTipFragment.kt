package com.example.tipjar.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.tipjar.R
import com.example.tipjar.custom.RepeatListener
import com.example.tipjar.databinding.FragmentAddTipBinding

/**
 * View for adding tips
 */
class AddTipFragment : Fragment() {

  private var _binding: FragmentAddTipBinding? = null
  private val binding
    get() = _binding!!

  private var amountValueInstance: String? = null
  private var peopleValueInstance: String? = null
  private var tipValueInstance: String? = null
  private var photoCheckBoxInstance: Boolean = false

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentAddTipBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if(savedInstanceState != null) {
      amountValueInstance = savedInstanceState.getString(AMOUNT_VALUE)
      peopleValueInstance = savedInstanceState.getString(PEOPLE_VALUE)
      tipValueInstance = savedInstanceState.getString(TIP_VALUE)
      photoCheckBoxInstance = savedInstanceState.getBoolean(TAKE_PHOTO, false)
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Set previous value after config change
    binding.amountEditText.setText(amountValueInstance.orEmpty())
    binding.peopleCountTextView.text = peopleValueInstance ?: "1"
    binding.percentTipEditText.setText(tipValueInstance ?: "10")
    binding.takePhotoCheckBox.isChecked = photoCheckBoxInstance

    computeTip()

    binding.addCounterView.setOnTouchListener(RepeatListener(400, 100) {
      val peopleCount = binding.peopleCountTextView.text.toString().toInt()
      binding.peopleCountTextView.text = peopleCount.inc().toString()
      computeTip(people = peopleCount.inc())
    })

    binding.minusCounterView.setOnTouchListener(RepeatListener(400, 100) {
      val peopleCount = binding.peopleCountTextView.text.toString().toInt()
      if (peopleCount != 1) {
        binding.peopleCountTextView.text = peopleCount.dec().toString()
        computeTip(people = peopleCount.inc())
      }
    })

    binding.amountEditText.doOnTextChanged { text, start, before, count ->
      computeTip(amount = text.toString().getFloat())
    }

    binding.percentTipEditText.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        val tip = binding.percentTipEditText.text.toString()
        if (tip.isBlank()) {
          Toast.makeText(
            requireContext(),
            "Must have a tip value, defaulting to 10%",
            Toast.LENGTH_LONG
          ).show()
          binding.percentTipEditText.setText("10")
        } else {
          computeTip()
        }
        true
      }
      false
    }

  }

  private fun String.getFloat(default: Float = DEFAULT_AMOUNT): Float {
    return if (this.isBlank()) {
      default
    } else {
      this.toFloat()
    }
  }

  private fun computeTip(
    amount: Float = binding.amountEditText.text.toString().getFloat(),
    people: Int = binding.peopleCountTextView.text.toString().toInt(),
    tip: Float = binding.percentTipEditText.text.toString().toFloat()
  ) {
    val totalTip = (tip / 100f) * amount
    val perPerson = totalTip / people
    binding.totalTipValueTextView.text = getString(R.string.total_tip_value, totalTip)
    binding.perPersonValueTextView.text = getString(R.string.per_person_value, perPerson)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString(AMOUNT_VALUE, binding.amountEditText.text.toString())
    outState.putString(PEOPLE_VALUE, binding.peopleCountTextView.text.toString())
    outState.putString(TIP_VALUE, binding.percentTipEditText.text.toString())
    outState.putBoolean(TIP_VALUE, binding.takePhotoCheckBox.isChecked)
  }

  companion object {
    private const val DEFAULT_AMOUNT = 100.00f
    private const val AMOUNT_VALUE = "com.example.tipjar.amountValue"
    private const val PEOPLE_VALUE = "com.example.tipjar.peopleValue"
    private const val TIP_VALUE = "com.example.tipjar.tipValue"
    private const val TAKE_PHOTO = "com.example.tipjar.takePhoto"
  }
}
