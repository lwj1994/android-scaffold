package com.lwjlol.scaffold

import android.content.res.Resources
import android.os.Build

/**
 * 记录 Android 系统的一些变量
 */


val isSDK_23 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
val isSDK_29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
val isSDK_30 = Build.VERSION.SDK_INT >= 30

// 异形屏的安全高度
var safeInsetTop = 0

val statusBarHeight
    get() = Resources.getSystem().getDimensionPixelSize(
        Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")
    ).coerceAtLeast(
        safeInsetTop
    )

var debugMode = false
