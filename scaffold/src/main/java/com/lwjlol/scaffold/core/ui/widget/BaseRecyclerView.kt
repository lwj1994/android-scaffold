package com.lwjlol.scaffold.core.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.airbnb.epoxy.EpoxyRecyclerView

/**
 * @author luwenjie on 2019-07-28 15:56:57
 */
class BaseRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
    def: Int = 0) : EpoxyRecyclerView(context, attrs, def) {
  var isUserInputEnabled = true
  var shouldShareViewPool = true


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