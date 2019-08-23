package com.lwjlol.scaffold.core.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.lwjlol.scaffold.R

class LoadingDialog(context: Context) : Dialog(context, R.style.ScaffoldDialogTheme) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            FrameLayout(context).apply {
                addView(ProgressBar(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER
                    }
                })
            }
        )
    }
}