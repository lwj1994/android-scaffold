package com.lwjlol.scaffold.core

import android.content.res.Resources
import android.os.Build

/**
 * 记录 Android 系统的一些变量
 */


val isSDK_M = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
val statusBarHeight = Resources.getSystem().getDimensionPixelSize(
    Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"))