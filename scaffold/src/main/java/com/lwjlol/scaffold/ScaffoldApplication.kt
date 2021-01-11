package com.lwjlol.scaffold

import android.app.Application
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.core.FlipperClient
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import com.lwjlol.ktx.Initializer

/**
 * @author luwenjie on 2019-08-23 14:12:55
 */
abstract class ScaffoldApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        application = this

        Initializer.initUtilsKtx(this)
        Utils.init(this)
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

    private fun initFlipper() {
        networkFlipperPlugin = NetworkFlipperPlugin()
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            SoLoader.init(this, false)
            val client: FlipperClient = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.addPlugin(networkFlipperPlugin)
            client.start()
        }
    }

    companion object {
        @JvmStatic
        lateinit var application: ScaffoldApplication

        @JvmStatic
        lateinit var networkFlipperPlugin: NetworkFlipperPlugin
        private const val TAG = "ScaffoldApplication"
    }
}
