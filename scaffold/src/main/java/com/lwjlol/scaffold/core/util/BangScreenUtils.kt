package com.lwjlol.scaffold.core.util

import android.os.Build
import android.view.Window

/**
 * 异形屏工具类
 */
object BangScreenUtils {
    fun getHeight(window: Window): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.decorView.rootWindowInsets?.displayCutout?.safeInsetTop ?: 0
        } else {
            0
        }
    }
}