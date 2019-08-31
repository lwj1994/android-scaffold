package com.lwjlol.scaffold.core.util

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import com.blankj.utilcode.util.Utils

/**
 * Android 系统相关的一些工具
 *
 * 具体的各种工具类使用 [https://github.com/Blankj/AndroidUtilCode] 的 [Utils]
 *
 * [https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/README-CN.md]
 *
 *
 */
object AndroidUtil {
    private const val TAG = "AndroidUtil"
    private val mainHandler = Handler(Looper.getMainLooper())
    private val ioHandlerThread = HandlerThread(TAG).apply {
        start()
    }
    private val ioHandler = Handler(ioHandlerThread.looper)
    val application: Application
        get() = Utils.getApp()

    /**
     * post 到主线程执行一些事情
     */
    fun postMainThread(delay: Long = 0L, runnable: () -> Unit) {
        mainHandler.postDelayed({ runnable() }, delay)
    }

    /**
     * post 到 io 线程执行一些事情, 按队列执行
     */
    fun postIoThread(delay: Long = 0L, runnable: () -> Unit) {
        ioHandler.postDelayed({ runnable() }, delay)
    }

    fun init(context: Context) {
        Utils.init(context)
    }

}