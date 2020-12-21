package com.lwjlol.scaffold.util

import android.content.Context
import android.widget.Toast
import com.lwjlol.scaffold.ScaffoldApplication

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
