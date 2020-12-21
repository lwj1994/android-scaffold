package com.lwjlol.scaffold.core.thread

import com.lwjlol.imagehosting.utils.ThreadPoolUtil
import com.lwjlol.ktx.lazyUnsafe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher

/**
 * 协程的调度器
 */
object CorDispatchers {
    private val ioCoroutineDispatcher by lazyUnsafe {
        ThreadPoolUtil.THREAD_POOL_EXECUTOR.asCoroutineDispatcher()
    }
    val main: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
    val io: CoroutineDispatcher
        get() = ioCoroutineDispatcher
}