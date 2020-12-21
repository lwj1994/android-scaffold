package com.lwjlol.scaffold.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView

/**
 * @author luwenjie on 2019-07-28 15:56:57
 */
class WenRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    def: Int = 0
) : EpoxyRecyclerView(context, attrs, def) {
    var isUserInputEnabled = true
    var shouldShareViewPool = true


    init {
        // fix 瀑布流上滑到顶部出现空白
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val layoutManager =
                    recyclerView.layoutManager as? StaggeredGridLayoutManager ?: return
                val first = IntArray(2)
                layoutManager.findFirstCompletelyVisibleItemPositions(first)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 0 || first[0] == 1 || first[1] == 2)) {
                    layoutManager.invalidateSpanAssignments()
                }
            }
        })
    }

    override fun shouldShareViewPoolAcrossContext(): Boolean {
        return shouldShareViewPool
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return isUserInputEnabled && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (!isUserInputEnabled) return false
        return super.onInterceptTouchEvent(event)
    }


    companion object {
        private const val TAG = "BaseListView"
    }
}
