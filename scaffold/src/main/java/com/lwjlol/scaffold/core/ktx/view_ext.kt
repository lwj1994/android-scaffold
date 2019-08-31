package com.lwjlol.scaffold.core.ktx

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 设置 View 系统自带的的点击水波纹效果.
 * @param res [android.R.attr.selectableItemBackground]/[android.R.attr.selectableItemBackgroundBorderless]
 */
fun View.setSelectableBackground(res: Int = android.R.attr.selectableItemBackground) {
  val typeValue = TypedValue()
  context.theme
      .resolveAttribute(res, typeValue, true)
  val attribute = intArrayOf(res)
  val typedArray = context.theme.obtainStyledAttributes(typeValue.resourceId, attribute);
  background = typedArray.getDrawable(0)

}

fun View.clickOnce(time: Long = 300, runnable: () -> Unit) {
  setOnClickListener {
    isEnabled = false
    runnable()
    postDelayed({ isEnabled = true }, time)
  }
}


//editext

fun EditText.updateText(text: String, selectPosition: Int = -1) {
  setText(text)

  setSelection(if (selectPosition == -1) {
    text.length
  } else selectPosition)
}

fun EditText.toggleKeyboard(show: Boolean) {
  if (show) {
    showKeyboard()
  } else {
    hideKeyboard()
  }
}

fun EditText.showKeyboard() {

  try {
    val inputManager = context.getSystemService(
        Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
  } catch (e: Exception) {
  }
}

fun EditText.hideKeyboard() {
  try {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (!imm.isActive) {
      return
    }
    imm.hideSoftInputFromWindow(windowToken, 0)
  } catch (e: Exception) {
  }
}


