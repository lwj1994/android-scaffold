package com.lwjlol.scaffold.suzy.core.ktx

import android.util.TypedValue
import com.lwjlol.scaffold.core.util.AndroidUtil
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP

val Number.dp: Int
  get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toFloat(),
      AndroidUtil.application.resources.displayMetrics).toInt()
val Number.sp: Float
  get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, toFloat(),
      AndroidUtil.application.resources.displayMetrics)
val Number.dpF: Float
  get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toFloat(),
      AndroidUtil.application.resources.displayMetrics)

fun Number.toK(allow0: Boolean = false): String {
  val oneK = 1000
  if (this.toInt() == 0) {
    return if (allow0) "0" else ""
  }
  return if (this.toInt() > oneK) {
    "${BigDecimal(this.toString()).divide(BigDecimal(oneK), 1, HALF_UP)}k"
  } else {
    this.toString()
  }
}