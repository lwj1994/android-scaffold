package com.lwjlol.scaffold.core.ui

import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import com.lwjlol.ktx.click
import com.lwjlol.ktx.colorInt
import com.lwjlol.ktx.dp
import com.lwjlol.ktx.dpF
import com.lwjlol.ktx.lazyUnsafe
import com.lwjlol.ktx.setRoundedBackground
import com.lwjlol.scaffold.R
import com.lwjlol.scaffold.core.ScaffoldApplication
import com.lwjlol.scaffold.core.dark.DarkMode
import com.lwjlol.scaffold.core.dark.IDarkMode

/**
 * @author luwenjie on 2020/4/4 09:41:17
 * @param rootView 根部局，最好是[FrameLayout]
 */
class SnackBarHelper(private val rootView: ViewGroup) : IDarkMode {
    private val dismissCallback by lazyUnsafe {
        Runnable {
            dismiss()
        }
    }

    override fun onDarkModeChanged() {
        snackBarView.run {
            if (DarkMode.isLight) {
                setRoundedBackground(R.color.snackBarBg.colorInt, 30.dpF)
            } else {
                setRoundedBackground(R.color.snackBarBg_dark.colorInt, 30.dpF)
            }
        }
        messageView.run {
            if (DarkMode.isLight) {
                setTextColor(R.color.snackBarMsgText.colorInt)
            } else {
                setTextColor(R.color.snackBarMsgText_dark.colorInt)
            }
        }
        actionView.run {
            if (DarkMode.isLight) {
                setRoundedBackground(R.color.snackBarActionBg.colorInt, 20.dpF)
                setTextColor(R.color.snackBarActionText.colorInt)
            } else {
                setRoundedBackground(R.color.snackBarActionBg_dark.colorInt, 20.dpF)
                setTextColor(R.color.snackBarActionText_dark.colorInt)
            }
        }
    }

    fun show(
        text: String,
        actionText: String = ScaffoldApplication.application.resources.getString(R.string.snackbar_ok),
        duration: Long = DURATION_SHORT,
        action: (() -> Unit)? = null
    ) {
        attach()
        if (text.isEmpty()) return
        snackBarView.animate()
            .cancel()
        snackBarView.isVisible = true
        messageView.text = text
        actionView.text = actionText
        actionView.click {
            action?.invoke()
            dismiss()
        }
        if (snackBarView.alpha == 1F) {
            snackBarView.removeCallbacks(dismissCallback)
            snackBarView.postDelayed(dismissCallback, duration)
        } else {
            snackBarView.animate()
                .alpha(1F)
                .setDuration(400)
                .withEndAction {
                    snackBarView.removeCallbacks(dismissCallback)
                    snackBarView.postDelayed(dismissCallback, duration)
                }
                .start()
        }
    }

    fun dismiss() {
        snackBarView.removeCallbacks(dismissCallback)
        snackBarView.animate()
            .cancel()
        if (!snackBarView.isVisible) return
        snackBarView.alpha = 1F
        snackBarView.animate()
            .alpha(0F)
            .withEndAction {
                snackBarView.isVisible = false
                detach()
            }
            .start()
    }

    private var isAttach = false
    val messageView by lazyUnsafe {
        AppCompatTextView(rootView.context).apply {
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT)
                .apply {
                    weight = 1F
                    setMargins(24.dp, 0, 12.dp, 0)
                }
            textSize = 14F
            setLineSpacing(0F, 1.3F)
            gravity = Gravity.CENTER_VERTICAL
            typeface = Typeface.DEFAULT_BOLD
            if (DarkMode.isLight) {
                setTextColor(R.color.snackBarMsgText.colorInt)
            } else {
                setTextColor(R.color.snackBarMsgText_dark.colorInt)
            }
        }
    }
    val actionView by lazyUnsafe {
        AppCompatTextView(rootView.context).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                .apply {
                    setMargins(12.dp, 0, 24.dp, 0)
                }
            textSize = 14F
            typeface = Typeface.DEFAULT_BOLD
            setPadding(10.dp, 5.dp, 10.dp, 5.dp)
            if (DarkMode.isLight) {
                setRoundedBackground(R.color.snackBarActionBg.colorInt, 20.dpF)
                setTextColor(R.color.snackBarActionText.colorInt)
            } else {
                setRoundedBackground(R.color.snackBarActionBg_dark.colorInt, 20.dpF)
                setTextColor(R.color.snackBarActionText_dark.colorInt)
            }
        }
    }
    val snackBarView by lazyUnsafe {
        LinearLayout(rootView.context).apply {
            alpha = 0F
            if (DarkMode.isLight) {
                setRoundedBackground(R.color.snackBarBg.colorInt, 30.dpF)
            } else {
                setRoundedBackground(R.color.snackBarBg_dark.colorInt, 30.dpF)
            }
            setPadding(0, 10.dp, 0, 10.dp)
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                .apply {
                    setMargins(24.dp, 0, 24.dp, 12.dp)
                    gravity = Gravity.BOTTOM
                }
            gravity = Gravity.CENTER_VERTICAL
            addView(messageView)
            addView(actionView)
            click {
            }
        }
    }

    private fun attach() {
        if (isAttach) return
        rootView.addView(snackBarView)
        isAttach = true
    }

    private fun detach() {
        if (!isAttach) return
        isAttach = false
    }

    companion object {
        private const val TAG = "LoadingViewHelper"
        const val DURATION_SHORT = 3000L
        const val DURATION_LONG = 200000L
    }
}
