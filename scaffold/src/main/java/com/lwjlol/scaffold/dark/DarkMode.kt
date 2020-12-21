package com.lwjlol.scaffold.dark

import android.content.res.Configuration
import com.lwjlol.scaffold.ScaffoldApplication
import com.lwjlol.scaffold.util.mmkv

/**
 * 深色模式
 */
object DarkMode {
    const val Light = 0
    const val DARK = 1
    const val AUTO = -1

    // 深色模式强制的开关
    var modeSwitch: Int by mmkv("dark_mode", AUTO, "com.lwjlol.scaffold")
    val isDark
        get() = isDarkMode(ScaffoldApplication.application.resources.configuration)
    val isLight
        get() = !isDark

    /**
     * @param configuration
     */
    fun isDarkMode(configuration: Configuration) =
        ((configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) && modeSwitch == AUTO) || (modeSwitch == DARK)
}
