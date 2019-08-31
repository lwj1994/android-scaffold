package com.lwjlol.scaffold.core.ktx

import android.content.Context
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.smoothScrollToAtOnce(position: Int) {
    layoutManager?.startSmoothScroll(
        OneTimeSmoothScroller(context).apply {
            targetPosition = position
        })
}












class OneTimeSmoothScroller(context: Context) : LinearSmoothScroller(context) {
    private var isScrolled: Boolean = false
    override fun updateActionForInterimTarget(action: Action) {
        if (isScrolled) {
            action.jumpTo(targetPosition)
        } else {
            super.updateActionForInterimTarget(action)
            action.interpolator = AccelerateInterpolator(1.5F)
            isScrolled = true
        }
    }

    override fun getVerticalSnapPreference(): Int {
        return LinearSmoothScroller.SNAP_TO_START
    }

    companion object {
        private const val TAG = "OneTimeSmoothScroller"
    }
}