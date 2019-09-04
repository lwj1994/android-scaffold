package com.lwjlol.scaffold.core.util

import android.util.Log
import com.lwjlol.scaffold.BuildConfig

object LogUtil {
    private const val TAG = "LogUtil"
    fun debug(msg: String, tag: String = TAG) {
        if (!BuildConfig.DEBUG) return
        Log.d(tag, msg)
    }


    fun error(msg: String, tag: String = TAG) {
        if (!BuildConfig.DEBUG) return
        Log.e(tag, msg)
    }
}