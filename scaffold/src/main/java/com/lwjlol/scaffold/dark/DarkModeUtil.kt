package com.lwjlol.scaffold.dark

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.lwjlol.scaffold.R
import com.lwjlol.scaffold.ScaffoldApplication

object DarkModeUtil {
    private const val TAG = "DarkModelUtil"

    sealed class UiMode {
        object Dark : UiMode()
        object Light : UiMode()
        object FollowSystem : UiMode()
    }

    var mode: UiMode = UiMode.FollowSystem
        set(value) {
            when (value) {
                UiMode.Dark -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                UiMode.Light -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                UiMode.FollowSystem -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
            field = value
        }
        get() = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> UiMode.Dark
            AppCompatDelegate.MODE_NIGHT_NO -> UiMode.Light
            else -> UiMode.FollowSystem
        }

    val isDarkMode
        get() = DarkModeUtil.mode is UiMode.Dark || (DarkModeUtil.mode is UiMode.FollowSystem && (ScaffoldApplication.application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES))

    fun getDarkMode(configuration: Configuration): Boolean =
        DarkModeUtil.mode is UiMode.Dark || (DarkModeUtil.mode is UiMode.FollowSystem && (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES))

    val modeList = ScaffoldApplication.application.resources.getStringArray(R.array.darkmode_list)

    fun getModeString() = when (mode) {
        is UiMode.Dark -> modeList[0]
        is UiMode.Light -> modeList[1]
        is UiMode.FollowSystem -> modeList[2]
    }

    fun setMode(index: Int) {
        mode = when (index) {
            0 -> UiMode.Dark
            1 -> UiMode.Light
            2 -> UiMode.FollowSystem
            else -> UiMode.Dark
        }
    }

}

