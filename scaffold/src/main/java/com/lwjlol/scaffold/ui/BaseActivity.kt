package com.lwjlol.scaffold.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.lwjlol.ktx.safeTopHeight
import com.lwjlol.scaffold.dark.DarkMode
import com.lwjlol.scaffold.safeInsetTop

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeInsetTop = window.safeTopHeight
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            onDarkModeChanged(DarkMode.isDarkMode(newConfig))
        }
    }


    /**
     * 深色模式发生变化
     * 必须在 activity 节点注册  android:configChanges="uiMode"
     * @param isDark 是否是深色模式
     */
    protected open fun onDarkModeChanged(isDark: Boolean = DarkMode.isDark) {

    }
}
