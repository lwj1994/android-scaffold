package com.lwjlol.scaffold.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.airbnb.mvrx.MavericksView
import com.lwjlol.ktx.lazyUnsafe
import com.lwjlol.scaffold.R
import com.lwjlol.scaffold.mvrx.MvrxListHelper
import com.lwjlol.scaffold.ui.widget.WenRecyclerView
import com.lwjlol.scaffold.ui.widget.Toolbar

abstract class BaseListFragment : BaseFragment(R.layout.scaffold_fragment_base_refreshlist), MavericksView {
    protected lateinit var toolbar: Toolbar
    protected lateinit var swipeRefreshLayout: SwipeRefreshLayout
    protected lateinit var recyclerView: WenRecyclerView
    protected open val hasToolbar = false
    protected val listHelper by lazyUnsafe { listHelper() }
    protected val epoxyController by lazyUnsafe { epoxyController() }
    protected val visibilityTracker by lazyUnsafe { EpoxyVisibilityTracker() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            toolbar = findViewById(R.id.scaffold_toolbar)
            swipeRefreshLayout = findViewById(R.id.scaffold_fragment_base_swipe)
            recyclerView = findViewById(R.id.scaffold_fragment_base_recyclerview)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.isVisible = hasToolbar
        initRecyclerView()
    }

    protected open fun initRecyclerView() {
        recyclerView.setController(epoxyController)
        swipeRefreshLayout.setOnRefreshListener {
            listHelper.showRefreshing(true)
            onRefresh()
        }
        visibilityTracker.attach(recyclerView)
    }

    protected open fun initLoadingState(loadingState: MutableLiveData<Pair<Int, String>>) {
        listHelper.initLoadingState(this, loadingState) { state, msg ->
            when (state) {
                MvrxListHelper.LOADING -> {
                }
                MvrxListHelper.LOADING_MORE -> {
                }
                MvrxListHelper.LOAD_SUCCESS -> {
                    listHelper.showRefreshing(false)
                }
                MvrxListHelper.LOAD_FAIL -> {
                    if (isResumed) {
                        showSnackBar(msg, null)
                    }
                }
                MvrxListHelper.LOAD_MORE_SUCCESS -> {
                }
                MvrxListHelper.LOAD_MORE_FAIL -> {
                    // 加载更多失败. 刷新加载更多的信息
                    epoxyController.requestModelBuild()
                }
            }
        }
    }

    override fun invalidate() {
        if (recyclerView.adapter == null) {
            recyclerView.setController(epoxyController)
        }
        recyclerView.requestModelBuild()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        epoxyController.cancelPendingModelBuild()
        visibilityTracker.detach(recyclerView)
        listHelper.clear()
        super.onDestroy()
    }

    abstract fun onRefresh()

    abstract fun listHelper(): MvrxListHelper

    abstract fun epoxyController(): EpoxyController

    protected open fun onLoadMore() {
    }
}
