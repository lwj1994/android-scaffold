package com.lwjlol.scaffold.core.ktx

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

fun @receiver:ColorInt Int.toColor(context: Context): Int = ContextCompat.getColor(context, this)

