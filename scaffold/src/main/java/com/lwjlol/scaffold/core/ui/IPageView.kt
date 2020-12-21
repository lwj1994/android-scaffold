package com.lwjlol.scaffold.core.ui

import android.content.Context
import com.lwjlol.scaffold.core.util.ToastUtil

/**
 * 表示 fragment/Avtivity 页面
 */
interface IPageView {
    val pageContext: Context

    fun showLoading(show: Boolean)

    fun showSnackBar(text: String, action: (() -> Unit)?)

    fun showToast(text: String, long: Boolean = false) {
        if (long) {
            ToastUtil.show(text)
        } else {
            ToastUtil.showLong(text)
        }
    }
}