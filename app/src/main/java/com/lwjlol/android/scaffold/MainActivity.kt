package com.lwjlol.android.scaffold

import android.os.Bundle
import com.lwjlol.scaffold.core.ktx.bindView
import com.lwjlol.scaffold.core.ui.BaseScaffoldActivity
import wenchieh.lu.bottombar.BottomBar

class MainActivity : BaseScaffoldActivity(R.layout.activity_main) {
    private val bottomBar: BottomBar by bindView(R.id.activity_main_BottomBar)


    override fun initData(savedInstanceState: Bundle?) {
    }
}
