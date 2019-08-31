package com.lwjlol.scaffold.core.util

import android.os.AsyncTask
import com.lwjlol.scaffold.core.util.ThreadPoolManager.Companion.SERIAL_IO_DISPATCHER
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher

/**
 * @author luwenjie on 2019-05-17 11:10:28
 *
 * 统一的线程池管理类, 直接沿用系统的 [AsyncTask] 中的 [AsyncTask.SERIAL_EXECUTOR] 和 [AsyncTask.THREAD_POOL_EXECUTOR]
 */
class ThreadPoolManager private constructor() {
    private object Loader {
        val INSTANCE = ThreadPoolManager()
    }

    fun executeTask(runnable: Runnable) {
        SERIAL_EXECUTOR.execute(runnable)
    }

    companion object {
        private const val TAG = "ThreadPoolManager"

        @JvmStatic
        fun get() = Loader.INSTANCE

        private val SERIAL_EXECUTOR = AsyncTask.SERIAL_EXECUTOR
        private val THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR
        //  并发协程调度器，在io线程池中并发执行任务
        val IO_DISPATCHER = THREAD_POOL_EXECUTOR.asCoroutineDispatcher()
        // 顺序协程调度器，在io线程池中顺序执行任务，不用担心并发的问题
        val SERIAL_IO_DISPATCHER = SERIAL_EXECUTOR.asCoroutineDispatcher()


    }
}

// 协程的 io 线程调度器
val DISPATCHER_IO: CoroutineDispatcher
    get() = SERIAL_IO_DISPATCHER
// 协程的主线程调度器
val DISPATCHER_MAIN: CoroutineDispatcher
    get() = Dispatchers.Main

// RxJava 的调度器
val ioScheduler: Scheduler
    get() = Schedulers.io()
val mainScheduler: Scheduler
    get() = AndroidSchedulers.mainThread()


