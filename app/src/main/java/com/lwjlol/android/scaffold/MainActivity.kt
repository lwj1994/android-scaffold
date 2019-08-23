package com.lwjlol.android.scaffold

import android.os.Bundle
import com.lwjlol.scaffold.core.ui.BaseScaffoldActivity

class MainActivity : BaseScaffoldActivity() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {

    }
}
