package com.example.tipjar.ui.viewtip

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.tipjar.R
import com.example.tipjar.databinding.FragmentViewTipsBinding
import com.example.tipjar.util.debounce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class ViewTipsFragment : Fragment() {

  private var _binding: FragmentViewTipsBinding? = null
  private val binding
    get() = _binding!!

  private val viewModel by viewModels<ViewTipsViewModel>()
  @Inject lateinit var glide: RequestManager

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentViewTipsBinding.inflate(inflater, container, false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val adapter = TipAdapter(glide) {
      ViewTipDialogFragment.newInstance(it).show(childFragmentManager, "ViewTipDialogFragment")
    }
    binding.list.adapter = adapter

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.tips.collectLatest { adapter.submitData(it) }
    }

    binding.startDateTextInputEditText.setOnClickListener {
      val date = LocalDate.now()
      DatePickerDialog(
        requireContext(),
        { view, year, month, dayOfMonth ->
          binding.startDateTextInputEditText.setText(
            resources.getString(
              R.string.date_format, year, month + 1, dayOfMonth
            )
          )
        },
        date.year,
        date.monthValue - 1,
        date.dayOfMonth
      ).show()
    }

    binding.endDateTextInputEditText.setOnClickListener {
      val date = LocalDate.now()
      DatePickerDialog(
        requireContext(),
        { view, year, month, dayOfMonth ->
          binding.endDateTextInputEditText.setText(
            resources.getString(
              R.string.date_format, year, month + 1, dayOfMonth
            )
          )
        },
        date.year,
        date.monthValue - 1,
        date.dayOfMonth + 1
      ).show()
    }

    binding.searchImageView.debounce {

      val startDateInput = binding.startDateTextInputEditText.text.toString()
      val endDateInput = binding.endDateTextInputEditText.text.toString()

      if (startDateInput.isNullOrBlank() || endDateInput.isNullOrBlank()) {
        viewModel.search()
      } else {
        val dtf = DateTimeFormatter.ofPattern("yyyy/MM/d")
        val ldStart = LocalDate.parse(startDateInput, dtf)
        val ldEnd = LocalDate.parse(endDateInput, dtf)

        val startMillis = ldStart.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMillis =
          ldEnd.atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusSeconds(1).toInstant()
            .toEpochMilli()

        viewModel.search(startMillis, endMillis)
      }
    }

    viewModel.search()

    initSwipeToDelete()
  }

  private fun initSwipeToDelete() {
    ItemTouchHelper(object : ItemTouchHelper.Callback() {

      override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
      ): Int {
        val tipViewHolder = viewHolder as TipViewHolder
        return if (tipViewHolder.tip != null) {
          makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        } else {
          makeMovementFlags(0, 0)
        }
      }

      override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder): Boolean = false

      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        (viewHolder as TipViewHolder).tip?.let {
          viewModel.remove(it)
        }
      }
    }).attachToRecyclerView(binding.list)
  }
}
