package com.lwjlol.scaffold.core.util

import android.widget.Toast
import com.lwjlol.scaffold.core.ScaffoldApplication

object ToastUtil {
    private val context = ScaffoldApplication.application
    private val toast = Toast.makeText(context, null, Toast.LENGTH_SHORT)
    fun show(s: String) {
        if (s.isEmpty()) return
        toast.duration = Toast.LENGTH_SHORT
        toast.setText(s)
        toast.show()
    }

    fun showLong(s: String) {
        if (s.isEmpty()) return
        toast.duration = Toast.LENGTH_LONG
        toast.setText(s)
        toast.show()
    }
}


