package com.lwjlol.scaffold.core

import android.app.Application
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils

/**
 * @author luwenjie on 2019-08-23 14:12:55
 */
abstract class ScaffoldApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)


        // 设置日志
        LogUtils.getConfig()
            // 日志的总开关
            .setConsoleSwitch(BuildConfig.DEBUG)
            // 全局的 tag
            .setGlobalTag("lwjlol")
            .setSingleTagSwitch(true)
            // 文件开关
            .setLog2FileSwitch(false)
    }
    companion object {
        private const val TAG = "ScaffoldApplication"
    }
}