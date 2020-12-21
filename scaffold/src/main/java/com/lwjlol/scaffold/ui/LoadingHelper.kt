package com.lwjlol.scaffold.ui

import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.lwjlol.ktx.dp
import com.lwjlol.ktx.lazyUnsafe
import com.lwjlol.ktx.setColorFilterCompact

/**
 * @author luwenjie on 2020/4/4 09:41:17
 *@param rootView 根部局，最好是[FrameLayout] 让 loading 能居中显示
 */
class LoadingHelper(private val rootView: ViewGroup) {
    var show: Boolean = false
        set(value) {
            field = value
            if (value) {
                attach()
            } else {
                detach()
            }
        }
    private var isAttach = false
    private val loadingView by lazyUnsafe {
        FrameLayout(rootView.context).apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setOnClickListener {
                //
            }
            tag = TAG_LOADING_VIEW
            addView(ProgressBar(rootView.context).apply {
                indeterminateDrawable.setColorFilterCompact()
                layoutParams = FrameLayout.LayoutParams(SIZE.dp, SIZE.dp)
                    .apply {
                        gravity = Gravity.CENTER
                    }
            })
        }
    }

    private fun attach() {
        if (isAttach) return
        rootView.addView(loadingView)
        isAttach = true
    }

    private fun detach() {
        if (!isAttach) return
        rootView.removeView(loadingView)
        isAttach = false
    }

    companion object {
        private const val TAG = "LoadingViewHelper"
        private const val TAG_LOADING_VIEW = "tag_loading_view"
        const val SIZE = 50
    }
}
