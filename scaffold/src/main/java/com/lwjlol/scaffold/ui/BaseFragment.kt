package com.lwjlol.scaffold.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.lifecycleScope
import com.lwjlol.ktx.colorInt
import com.lwjlol.ktx.lazyUnsafe
import com.lwjlol.ktx.match_match
import com.lwjlol.scaffold.R
import com.lwjlol.scaffold.dark.DarkMode
import com.lwjlol.scaffold.core.thread.CorDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * 统一在根布局套一层 [CoordinatorLayout], 方便覆盖 loading view 或者 snackbar
 */
abstract class BaseFragment(@LayoutRes layoutRes: Int = 0) : Fragment(layoutRes), IPageView {
    protected val coordinatorLayout by lazyUnsafe {
        CoordinatorLayout(requireContext()).apply {
            match_match()
        }
    }
    protected lateinit var loadingHelper: LoadingHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            onDarkModeChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val onCreateView = super.onCreateView(inflater, container, savedInstanceState)
        if (coordinatorLayout.isEmpty() && onCreateView != null) {
            coordinatorLayout.addView(onCreateView)
        }
        loadingHelper = LoadingHelper(coordinatorLayout)
        return coordinatorLayout
    }

    override val pageContext: Context
        get() = requireContext()

    override fun showLoading(show: Boolean) {
        loadingHelper.show = show
    }

    override fun showSnackBar(text: String, action: (() -> Unit)?) {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        onDarkModeChanged(DarkMode.isDarkMode(newConfig))
    }


    /**
     * 深色模式发生变化
     * 必须在 activity 节点注册  android:configChanges="uiMode"
     * @param isDark 是否是深色模式
     */
    protected open fun onDarkModeChanged(isDark: Boolean = DarkMode.isDark) {
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            return
        }
        // 深色模式下做出统一的 ui 变换
        if (!isDark) {
            view?.setBackgroundColor(R.color.rootViewBg.colorInt)
        } else {
            view?.setBackgroundColor(R.color.rootViewBg_dark.colorInt)
        }
    }

    fun launch(
        context: CoroutineContext = CorDispatchers.main,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = lifecycleScope.launch(context, start, block)

    fun <T> async(
        context: CoroutineContext = CorDispatchers.io,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T
    ) = lifecycleScope.async(context, start, block)
}
