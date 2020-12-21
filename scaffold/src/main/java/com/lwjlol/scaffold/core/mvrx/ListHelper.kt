package com.lwjlol.scaffold.core.mvrx

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.lwjlol.scaffold.core.ui.widget.BaseRecyclerView

/**
 * @author luwenjie on 2020/5/21 17:27:53
 */
abstract class ListHelper(
    private val recyclerView: BaseRecyclerView,
    private val swipeRefreshLayout: SwipeRefreshLayout? = null,
    var emptyText: String = "",
    var onClickEmptyView: () -> Unit = {}
) {
    fun initLoadingState(
        owner: LifecycleOwner,
        loadingState: LiveData<Pair<Int, String>>,
        onStateChange: (state: Int, msg: String) -> Unit
    ) {
        loadingState.observe(owner, Observer { (state, msg) ->
            showRefreshing(state == LOADING)
            onStateChange.invoke(state, msg)
        })
    }

    fun checkStatus(
        epoxyController: EpoxyController,
        list: List<*>,
        request: Async<*>
    ): Boolean {
        if (list.isEmpty()) {
            epoxyController.run {
                when (request) {
                    is Loading -> {
                    }
                    is Success -> {
                        emptyView(this, emptyText)
                    }
                    is Fail -> {
                        failView(this, request.error.message ?: "", onClickEmptyView)
                    }
                    else -> {

                    }
                }
            }
            return false
        }
        return true
    }

    fun showRefreshing(b: Boolean) {
        swipeRefreshLayout ?: return
        if (!swipeRefreshLayout.isEnabled) {
            swipeRefreshLayout.post {
                swipeRefreshLayout.isRefreshing = false
            }
            return
        }
        swipeRefreshLayout.post {
            if (b == swipeRefreshLayout.isRefreshing) return@post
            swipeRefreshLayout.isRefreshing = b
        }
    }

    fun clear() {
        for (i in 0 until recyclerView.itemDecorationCount) {
            recyclerView.removeItemDecorationAt(0)
        }
        recyclerView.clearOnScrollListeners()
        recyclerView.clear()
        onClickEmptyView = {}
    }

    companion object {
        private const val TAG = "ListFragmentHelper"
        const val LOADING = 0
        const val LOAD_SUCCESS = 1
        const val LOAD_FAIL = 2
        const val LOADING_MORE = 3
        const val LOAD_MORE_FAIL = 4
        const val LOAD_MORE_SUCCESS = 5

        /**
         * 分发列表异步加载数据时的加载状态
         * @param request 异步请求
         * @param loadMore 是否是加载更多
         */
        @JvmStatic
        fun postLoadingState(
            loadingLiveData: MutableLiveData<Pair<Int, String>>,
            request: Async<*>,
            loadMore: Boolean
        ) {
            loadingLiveData.postValue(
                when {
                    (request is Loading) && loadMore -> LOADING_MORE to "loading more"
                    (request is Success) && loadMore -> LOAD_MORE_SUCCESS to "LOAD_MORE_SUCCESS"
                    (request is Fail) && loadMore -> LOAD_MORE_FAIL to " LOAD_MORE_FAIL error:${request.error}"
                    (request is Loading) && !loadMore -> LOADING to "loading"
                    (request is Success) && !loadMore -> LOAD_SUCCESS to "LOAD_SUCCESS"
                    (request is Fail) && !loadMore -> LOAD_FAIL to " LOAD_FAIL error:${request.error}"
                    else -> -1 to ""
                }
            )
        }
    }

    fun addLoadingView(
        epoxyController: EpoxyController,
        size: Int,
        canLoadMore: Boolean,
        tag: String,
        loadMore: () -> Unit
    ) {
        epoxyController.run {
            verticalLoadingView(this)
        }
    }

    abstract fun emptyView(controller: EpoxyController, emptyText: String)

    abstract fun failView(controller: EpoxyController, errorMsg: String, onClickEmptyView: () -> Unit)

    abstract fun verticalLoadingView(controller: EpoxyController)
}
