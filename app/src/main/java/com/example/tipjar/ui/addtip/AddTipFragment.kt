package com.example.tipjar.ui.addtip

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.tipjar.R
import com.example.tipjar.custom.RepeatListener
import com.example.tipjar.data.Result
import com.example.tipjar.databinding.FragmentAddTipBinding
import com.example.tipjar.ui.MainSharedViewModel
import com.example.tipjar.util.debounce
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


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
  private val viewModel: AddTipViewModel by viewModel()
  private val sharedViewModel by sharedViewModel<MainSharedViewModel>()

  var currentPhotoPath: String? = null

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
    savedInstanceState?.run {
      amountValueInstance = getString(AMOUNT_VALUE)
      peopleValueInstance = getString(PEOPLE_VALUE)
      tipValueInstance = getString(TIP_VALUE)
      photoCheckBoxInstance = getBoolean(TAKE_PHOTO, false)
    }
  }

  @InternalCoroutinesApi
  @SuppressLint("ClickableViewAccessibility")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Set previous value after config change
    binding.run {
      amountEditText.setText(amountValueInstance.orEmpty())
      peopleCountTextView.text = peopleValueInstance ?: DEFAULT_PEOPLE_COUNT.toString()
      percentTipEditText.setText(tipValueInstance.orEmpty())
      takePhotoCheckBox.isChecked = photoCheckBoxInstance
    }

    // compute tip and per person on default values or from values fetched from savedInstanceState
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
        computeTip(people = peopleCount.dec())
      }
    })

    binding.amountEditText.doOnTextChanged { text, _, _, _ ->
      computeTip(amount = text.toString().getFloat())
    }

    binding.percentTipEditText.doOnTextChanged { text, _, _, _ ->
      computeTip(tip = text.toString().getFloat(DEFAULT_PERCENTAGE.toFloat()))
    }

    binding.savePaymentButton.debounce {
      savePayment()
    }

    // Observe result of saving payment
    lifecycleScope.launch {
      lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.addTipFlow.collect {
          when (it.status) {
            Result.Status.SUCCESS -> {
              resetViews()
              showToast(strId = R.string.payment_saved_msg)
              sharedViewModel.triggerPaymentSuccess()
              currentPhotoPath = null
            }
            Result.Status.ERROR -> {
              showToast(strMsg = it.message.toString())
              showToast()
            }
            Result.Status.NONE -> {
              Log.d("test", "none")
            }
          }
        }
      }
    }
  }

  private fun resetViews() {
    binding.run {
      amountEditText.text = null
      peopleCountTextView.text = DEFAULT_PEOPLE_COUNT.toString()
      percentTipEditText.text = null
      takePhotoCheckBox.isChecked = false
    }
  }

  private fun computeTip(
    amount: Float = binding.amountEditText.text.toString().getFloat(),
    people: Int = binding.peopleCountTextView.text.toString().toInt(),
    tip: Float = binding.percentTipEditText.text.toString().getFloat(DEFAULT_PERCENTAGE.toFloat())
  ) {
    val totalTip = (tip / 100f) * amount
    val perPerson = totalTip / people
    binding.totalTipValueTextView.text = getString(R.string.total_tip_value, totalTip)
    binding.perPersonValueTextView.text = getString(R.string.per_person_value, perPerson)
  }

  @InternalCoroutinesApi
  private fun savePayment() {
    if (binding.takePhotoCheckBox.isChecked) {
      if (hasNeededPermissions()) {
        getCameraImage.launch(getUri(createImageFile()))
      } else {
        requestMultiplePermissionsLauncher.launch(
          arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
          )
        )
      }
    } else {
      viewModel.saveTip(
        amount = binding.amountEditText.text.toString().getFloat(),
        tipPercentage = binding.percentTipEditText.text.toString()
          .getFloat(DEFAULT_PERCENTAGE.toFloat()),
        photoPath = currentPhotoPath
      )
    }
  }

  private fun hasNeededPermissions() = REQUIRED_PERMISSIONS.all {
    ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
  }

  @InternalCoroutinesApi
  private val requestMultiplePermissionsLauncher =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultsMap ->
      if (resultsMap.all { entry -> entry.value == true }) {
        savePayment()
      } else {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) && shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
          Snackbar.make(
            binding.root,
            R.string.permission_rationale_msg,
            Snackbar.LENGTH_LONG
          ).show()
        } else {
          // Make user update permission in settings
          showToast(strId = R.string.permission_rationale_msg)

          val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
          val uri = Uri.fromParts("package", requireContext().packageName, null)
          intent.data = uri
          startActivity(intent)
        }
      }
    }

  private fun createImageFile(): File {
    val timeStamp = System.currentTimeMillis()
    val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    requireContext().cacheDir
    return File.createTempFile(
      "JPEG_${timeStamp}",
      ".jpg",
      storageDir
    ).apply {
      currentPhotoPath = absolutePath
    }
  }

  private fun getUri(file: File) =
    FileProvider.getUriForFile(requireContext(), "com.example.tipjar", file)

  @InternalCoroutinesApi
  private val getCameraImage =
    registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
      if (success) {
        viewModel.saveTip(
          amount = binding.amountEditText.text.toString().getFloat(),
          tipPercentage = binding.percentTipEditText.text.toString()
            .getFloat(DEFAULT_PERCENTAGE.toFloat()),
          photoPath = currentPhotoPath
        )
      }
    }

  private fun showToast(@StringRes strId: Int? = null, strMsg: String? = null) {
    strId?.let {
      Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    } ?: Toast.makeText(requireContext(), strMsg!!, Toast.LENGTH_SHORT).show()
  }

  private fun String.getFloat(default: Float = DEFAULT_AMOUNT): Float {
    return if (this.isBlank()) {
      default
    } else {
      this.toFloat()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.run {
      putString(AMOUNT_VALUE, binding.amountEditText.text.toString())
      putString(PEOPLE_VALUE, binding.peopleCountTextView.text.toString())
      putString(TIP_VALUE, binding.percentTipEditText.text.toString())
      putBoolean(TIP_VALUE, binding.takePhotoCheckBox.isChecked)
    }
  }

  companion object {
    private const val DEFAULT_AMOUNT = 100.00f
    private const val DEFAULT_PERCENTAGE = 10
    private const val DEFAULT_PEOPLE_COUNT = 1
    private const val AMOUNT_VALUE = "com.example.tipjar.amountValue"
    private const val PEOPLE_VALUE = "com.example.tipjar.peopleValue"
    private const val TIP_VALUE = "com.example.tipjar.tipValue"
    private const val TAKE_PHOTO = "com.example.tipjar.takePhoto"

    private val REQUIRED_PERMISSIONS =
      arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
  }
}
