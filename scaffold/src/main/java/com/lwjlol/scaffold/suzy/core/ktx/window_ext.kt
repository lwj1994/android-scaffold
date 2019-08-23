package com.lwjlol.scaffold.suzy.core.ktx

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.lwjlol.scaffold.core.isSDK_M


/**
 * 状态栏沉浸模式的开关
 */
var Window.immersionMode: Boolean
  set(value) {
    if (value) {
      addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE   // 防止状态栏、底部导航栏隐藏时，内容区域大小发生变化
          or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)  // Activity会全屏显示，但状态栏不会被隐藏，状态栏依然可见，Activity 顶端布局部分会被状态栏盖住
      statusBarColor = Color.TRANSPARENT
    } else {
      clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
      statusBarColor = Color.WHITE
    }
  }
  get() {
    return (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS and attributes.flags == WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
  }

var Window.darkStatusBarContent: Boolean
  @TargetApi(VERSION_CODES.M)
  set(value) {
    if (!isSDK_M) return
    decorView.systemUiVisibility = if (value) {
      decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
      decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
  }
  @TargetApi(VERSION_CODES.M)
  get() {
    return (isSDK_M && View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR and attributes.flags == View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
  }