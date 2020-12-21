package com.lwjlol.scaffold.core.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lwjlol.scaffold.core.safeInsetTop
import com.lwjlol.scaffold.core.util.BangScreenUtils

class BaseActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeInsetTop = BangScreenUtils.getHeight(window)
    }
}