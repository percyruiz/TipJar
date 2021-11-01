package com.example.tipjar.custom

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import com.example.tipjar.R
import android.text.Spanned

import android.text.InputFilter
import java.util.regex.Matcher
import java.util.regex.Pattern
import com.example.tipjar.custom.AmountEditText.DecimalDigitsInputFilter


class AmountEditText @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

  private val defaultValue = "100.00"

  init {
    background = AppCompatResources.getDrawable(context, R.drawable.rectangle)
    gravity = Gravity.CENTER
    hint = defaultValue

    val attr = intArrayOf(R.attr.textAppearanceHeadline3)
    val ta = context.obtainStyledAttributes(attr)
    val resId = ta.getResourceId(0, 0)
    ta.recycle()
    setTextAppearance(context, resId)

    maxLines = 1

    setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dollar, 0, 0, 0)
    val scale = resources.displayMetrics.density
    val dpAsPixels = (24 * scale + 0.5f).toInt()
    setPadding(dpAsPixels, 0, dpAsPixels, 0)

    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
    filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 3))
  }

  internal class DecimalDigitsInputFilter(
    digitsBeforeZero: Int,
    digitsAfterZero: Int
  ) : InputFilter {
    private val mPattern: Pattern =
      Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")

    override fun filter(
      source: CharSequence,
      start: Int,
      end: Int,
      dest: Spanned,
      dstart: Int,
      dend: Int
    ): CharSequence? {
      val matcher: Matcher = mPattern.matcher(dest)
      return if (!matcher.matches()) "" else null
    }

  }
}
