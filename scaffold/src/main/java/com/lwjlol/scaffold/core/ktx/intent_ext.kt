package com.lwjlol.scaffold.core.ktx

import android.content.Intent
import android.os.Parcelable
import com.lwjlol.scaffold.core.ui.BaseScaffoldActivity

/**
 * 设置 key 为 [BaseScaffoldActivity.ACTIVITY_ARG_KEY] 的 [Parcelable]
 */
fun Intent.argument(parcelable: Parcelable): Intent {
  putExtra(BaseScaffoldActivity.ACTIVITY_ARG_KEY, parcelable)
  return this
}