package com.lwjlol.scaffold.core.ui

import android.content.Context

interface BaseView {
  val viewContext: Context

  fun showLoading(show: Boolean)

}