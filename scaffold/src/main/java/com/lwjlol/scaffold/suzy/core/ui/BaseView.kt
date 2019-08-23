package com.lwjlol.scaffold.suzy.core.ui

import android.content.Context

interface BaseView {
  val viewContext: Context

  fun showLoading(show: Boolean)

}