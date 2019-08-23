package com.lwjlol.scaffold.suzy.core.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.lwjlol.scaffold.R
import com.lwjlol.scaffold.core.ktx.dp
import com.lwjlol.scaffold.core.ktx.setSelectableBackground

/**
 * @author luwenjie on 2019-05-30 17:40:25
 *
 */
class ChunchunToolbar @JvmOverloads constructor(
    context: Context, attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attr, defStyleAttr) {
    val backView = ImageView(context).apply {
        setImageResource(R.drawable.ic_arrow_back_black_24dp)
        layoutParams = FrameLayout.LayoutParams(
            50.dp,
            ViewGroup.LayoutParams.MATCH_PARENT
        ).apply { gravity = Gravity.START }
        scaleType = ImageView.ScaleType.CENTER
        setSelectableBackground()
    }
    val titleView = TextView(context).apply {
        textSize = 18F
        setTextColor(Color.BLACK)
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ).apply { gravity = Gravity.CENTER }
        typeface = Typeface.DEFAULT_BOLD
        gravity = Gravity.CENTER
    }
    val menuView = LinearLayout(context).apply {
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ).apply { gravity = Gravity.END }
        orientation = LinearLayout.HORIZONTAL
    }

    fun addMenuView(@DrawableRes res: Int) {
        menuView.addView(
            ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                ).apply {
                    gravity = Gravity.CENTER
                    setMargins(15.dp, 0, 15.dp, 0)
                }
                scaleType = ImageView.ScaleType.CENTER
                setImageResource(res)
                setSelectableBackground(android.R.attr.selectableItemBackgroundBorderless)
            })
    }

    init {
        addViewInLayout(backView, -1, backView.layoutParams, true)
        addViewInLayout(titleView, -1, titleView.layoutParams, true)
        addViewInLayout(menuView, -1, menuView.layoutParams, true)
        requestLayout()
    }

    fun setOnClickBackListener(listener: () -> Unit) {
        backView.setOnClickListener {
            listener()
        }
    }

    fun setTitle(title: String) {
        titleView.text = title
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        backView.setOnClickListener(null)
    }

    companion object {
        private const val TAG = "LcToolbar"
    }
}