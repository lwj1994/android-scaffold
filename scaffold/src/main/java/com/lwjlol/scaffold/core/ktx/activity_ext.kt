package com.lwjlol.scaffold.core.ktx

import android.app.Activity
import android.content.Intent
import android.os.Parcelable

/**
 * 快速启动一个 [Activity]
 */
inline fun <reified T : Activity> Activity.startActivityWithArgs(args: Parcelable? = null) {
  if (args == null) {
    startActivity(Intent(this, T::class.java))
  } else {
    startActivity(Intent(this, T::class.java).argument(args))
  }
}

/**
 * 为下一个跳转创建一个 Intent
 * @param args intent 的参数
 * @param T 要跳转的目标
 */
inline fun <reified T : Activity> Activity.createIntent(args: Parcelable? = null): Intent {
  return if (args == null) {
    Intent(this, T::class.java)
  } else {
    Intent(this, T::class.java).argument(args)
  }
}


