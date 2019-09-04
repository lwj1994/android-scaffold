package com.lwjlol.scaffold.core.ktx

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MvRx

/**
 * 给一个 [Fragment] 匹配参数
 */
fun Fragment.applyArgs(args: Parcelable): Fragment {
  return apply { arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, args) } }
}
/**
 * 为下一个跳转创建一个 Intent
 * @param args intent 的参数
 * @param T 要跳转的目标
 */
inline fun <reified T : Activity> Fragment.createIntent(args: Parcelable? = null): Intent {
  return if (args == null) {
    Intent(requireContext(), T::class.java)
  } else {
    Intent(requireContext(), T::class.java).argument(args)
  }
}
