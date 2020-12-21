package com.lwjlol.scaffold.core.mvrx

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksViewModel
import com.lwjlol.liveeventbus.EventLiveData
import com.lwjlol.scaffold.core.thread.CorDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * 有状态的 viewModel
 */
abstract class StatefulViewModel<S : PageState>(
    initialState: S
) : MavericksViewModel<S>(initialState) {
    companion object {
        private const val TAG = "StatefulViewModel"
    }

    /**
     * 分发 loading 状态，在 setState 中使用
     */
    fun postLoadingState(
        request: Async<*>,
        loadMore: Boolean
    ) {
        ListHelper.postLoadingState(loadingLiveData, request, loadMore)
    }

    fun observeLoadingState(
        life: LifecycleOwner,
        observer: Observer<Pair<Int, String>>
    ) {
        loadingLiveData.observe(life, observer)
    }

    private val loadingLiveData = EventLiveData<Pair<Int, String>>(true)

    /**
     * 启动一个协程
     */
    protected fun launch(
        context: CoroutineContext = CorDispatchers.main,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(context, start, block)

    /**
     * 异步协程
     */
    protected fun <T> async(
        context: CoroutineContext = CorDispatchers.io,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T
    ) = viewModelScope.async(context, start, block)
}