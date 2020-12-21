package com.lwjlol.android.scaffold

import android.os.Bundle
import androidx.savedstate.SavedStateRegistry
import com.lwjlol.scaffold.ui.BaseActivity
import wenchieh.lu.bottombar.BottomBar

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
