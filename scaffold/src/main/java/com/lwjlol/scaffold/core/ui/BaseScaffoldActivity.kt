package com.lwjlol.scaffold.core.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.airbnb.mvrx.BaseMvRxActivity
import com.airbnb.mvrx.BaseMvRxFragment
import com.lwjlol.scaffold.R
import com.lwjlol.scaffold.core.ktx.dp
import com.lwjlol.scaffold.core.ktx.immersionMode
import com.lwjlol.scaffold.core.ui.BaseListFragment.OnKeyBoardVisibleListener
import com.lwjlol.scaffold.core.ui.widget.ChunchunToolbar
import com.lwjlol.scaffold.core.util.AndroidUtil
import com.lwjlol.scaffold.core.util.DISPATCHER_MAIN
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import kotlinx.coroutines.*
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author luwenjie on 2019-07-03 23:17:51
 */
abstract class BaseScaffoldActivity : BaseMvRxActivity(), BaseView, OnKeyBoardVisibleListener {
    protected val lifeScope = CoroutineScope(SupervisorJob() + DISPATCHER_MAIN)

    companion object {
        private const val TAG = "BaseScaffoldActivity"
        const val ACTIVITY_ARG_KEY = "scaffold_activity_arg_key"
        private const val KEYBOARD_HEIGHT_EST = 200
    }

    protected open val activityRootView: View by lazy(NONE) {
        val view = findViewById<View>(R.id.scaffold_activity_base_rootView) ?: window.decorView
        view
    }

    override val viewContext: Context
        get() = this
    protected open val layoutRes: Int = R.layout.scaffold_activity_empty
    private val loadingDialog by lazy(NONE) {
        LoadingDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.immersionMode = false
        initView()
        initData(savedInstanceState)
    }

    abstract fun initData(savedInstanceState: Bundle?)


    @CallSuper
    protected open fun initView() {
        setContentView(layoutRes)
    }

    private val keyboardObserver = ViewTreeObserver.OnGlobalLayoutListener {
        val heightDiff = activityRootView.rootView.height - activityRootView.height
        val isVisible = heightDiff > KEYBOARD_HEIGHT_EST.dp
        onKeyBoardVisibleStateChanged(isVisible, if (isVisible) heightDiff else 0)
    }

    override fun onKeyBoardVisibleStateChanged(isVisible: Boolean, keyboardHeight: Int) {

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener(keyboardObserver)
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            loadingDialog.show()
        } else {
            loadingDialog.dismiss()
        }
    }

    override fun onDestroy() {
        activityRootView.viewTreeObserver.removeOnGlobalLayoutListener(keyboardObserver)
        loadingDialog.cancel()
        lifeScope.coroutineContext.cancel()
        super.onDestroy()
    }
}


abstract class BaseFragment : BaseMvRxFragment(), BaseView {
    protected var toolbar: ChunchunToolbar? = null
    override val viewContext: Context by lazy(NONE) {
        requireActivity()
    }

    protected abstract val layoutRes: Int

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val loadingDialog by lazy {
        LoadingDialog(requireContext())
    }
    protected val lifeScope by lazy(LazyThreadSafetyMode.NONE) {
        AndroidLifecycleScopeProvider.from(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false).apply {
            toolbar = findViewById(R.id.scaffold_toolbar)

            initView(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(savedInstanceState)
    }

    protected abstract fun initView(view: View)
    protected abstract fun initData(savedInstanceState: Bundle?)


    override fun showLoading(show: Boolean) {
        AndroidUtil.postMainThread {
            if (show) loadingDialog.show() else loadingDialog.dismiss()
        }
    }

    override fun invalidate() {

    }

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = coroutineScope.launch(context, start, block)

    protected fun async(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = coroutineScope.async(context, start, block)


    override fun onDestroy() {
        coroutineScope.coroutineContext.cancel()
        loadingDialog.cancel()
        super.onDestroy()
    }


    /**
     * Activity Intent argument delegate that makes it possible to set Activity argument without
     * creating a key for each one.
     *
     * To create argument, define a property in your Activity like:
     *     `private val listingId by actArgs<MyArgs>()`
     *
     * Each Activity can only have a single argument with the key [LC_BASEACTIVITY_ARGS]
     */
    fun <V : Any> actArgs() = object : ReadOnlyProperty<Activity, V> {
        var value: V? = null

        override fun getValue(thisRef: Activity, property: KProperty<*>): V {
            if (value == null) {
                val args = thisRef.intent
                    ?: throw IllegalArgumentException("There are no Activity intent!")
                val argUntyped =
                    args.getParcelableExtra<Parcelable>(BaseScaffoldActivity.ACTIVITY_ARG_KEY)
                argUntyped ?: throw IllegalArgumentException(
                    "Activity argument not found at key ACTIVITY_ARG_KEY!"
                )
                @Suppress("UNCHECKED_CAST")
                value = argUntyped as V
            }
            return value ?: throw IllegalArgumentException("")
        }
    }


}


abstract class BaseListFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.scaffold_fragment_base_refreshlist
    protected var listView: BaseListView? = null
    protected var swipeRefreshLayout: SwipeRefreshLayout? = null
    private val visibilityTracker by lazy(NONE) { EpoxyVisibilityTracker() }
    protected val viewController by lazy(NONE) { createViewController() }


    @CallSuper
    override fun initView(view: View) {
        listView = view.findViewById(R.id.scaffold_fragment_base_list)
        swipeRefreshLayout = view.findViewById(R.id.scaffold_fragment_base_swipe)

        listView?.run {
            layoutManager = LinearLayoutManager(viewContext)
            setController(viewController)
            itemAnimator = null
            visibilityTracker.attach(this)
        }

        swipeRefreshLayout?.run {
            setOnRefreshListener {
                showRefreshing(true)
                onRefresh()
            }
        }


    }

    protected abstract fun onRefresh()


    override fun invalidate() {
        listView?.requestModelBuild()
    }

    protected fun showRefreshing(show: Boolean) {
        swipeRefreshLayout?.run {
            if (show == isRefreshing) return
            post { isRefreshing = show }
        }
    }

    override fun onDestroyView() {
        viewController.cancelPendingModelBuild()
        listView?.let {
            visibilityTracker.detach(it)
            it.clearOnScrollListeners()
        }

        swipeRefreshLayout?.setOnRefreshListener(null)
        super.onDestroyView()
    }

    /**
     * Provide the EpoxyController to use when building models for this Fragment.
     * Basic usages can simply use [simpleController]
     */
    abstract fun createViewController(): EpoxyController


    interface OnKeyBoardVisibleListener {
        fun onKeyBoardVisibleStateChanged(isVisible: Boolean, keyboardHeight: Int = 0)
    }
}