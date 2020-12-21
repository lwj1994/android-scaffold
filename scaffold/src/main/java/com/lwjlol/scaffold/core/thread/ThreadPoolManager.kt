package com.lwjlol.imagehosting.utils

import android.util.Log
import java.util.ArrayDeque
import java.util.LinkedList
import java.util.concurrent.Callable
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.FutureTask
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit.SECONDS
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author luwenjie on 2020/6/27 22:54:22
 */
object ThreadPoolUtil {
    private const val CORE_POOL_SIZE = 1
    private const val BACKUP_POOL_SIZE = 5
    private const val MAXIMUM_POOL_SIZE = 20
    private const val KEEP_ALIVE_SECONDS = 3L
    private const val TAG = "ThreadPoolUtil"
    private val futureTaskQueue = LinkedList<FutureTask<*>>()

    private val sBackupExecutorQueue: LinkedBlockingQueue<Runnable> by lazy {
        LinkedBlockingQueue<Runnable>()
    }

    private val sBackupExecutor by lazy {
        val sBackupExecutor = ThreadPoolExecutor(
            BACKUP_POOL_SIZE, BACKUP_POOL_SIZE, KEEP_ALIVE_SECONDS, SECONDS, sBackupExecutorQueue,
            sThreadFactory
        )
        sBackupExecutor.allowCoreThreadTimeOut(true)
        sBackupExecutor
    }

    private val sRunOnSerialPolicy: RejectedExecutionHandler = RejectedExecutionHandler { r, e ->
        Log.w(TAG, "Exceeded ThreadPoolExecutor pool size")
        // Create this executor lazily, hopefully almost never.
        sBackupExecutor.execute(r)
    }

    private val sThreadFactory: ThreadFactory = object : ThreadFactory {
        private val mCount = AtomicInteger(1)
        override fun newThread(r: Runnable): Thread {
            return Thread(r, "$TAG #" + mCount.getAndIncrement())
        }
    }

    val THREAD_POOL_EXECUTOR by lazy {
        val threadPoolExecutor = ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, SECONDS,
            SynchronousQueue<Runnable>(),
            sThreadFactory
        )
        threadPoolExecutor.rejectedExecutionHandler = sRunOnSerialPolicy
        threadPoolExecutor
    }

    val SERIAL_EXECUTOR: Executor by lazy { SerialExecutor() }

    private class SerialExecutor : Executor {
        private val mTasks = ArrayDeque<Runnable>()
        private var mActive: Runnable? = null

        @Synchronized
        override fun execute(r: Runnable) {
            mTasks.offer(Runnable {
                try {
                    r.run()
                } finally {
                    scheduleNext()
                }
            })
            if (mActive == null) {
                scheduleNext()
                return
            }
        }

        @Synchronized
        private fun scheduleNext() {
            if (mTasks.poll()
                    .also { mActive = it } != null
            ) {
                THREAD_POOL_EXECUTOR.execute(mActive ?: return)
            }
        }
    }

    fun <T> execute(
        isSerial: Boolean = false,
        id: String = "",
        block: () -> T
    ) {
        execute(isSerial, object : WorkerRunnable<T>(id) {
            override fun call(): T {
                return block()
            }
        })
    }

    fun <R> execute(
        isSerial: Boolean = false,
        workerRunnable: WorkerRunnable<R>
    ) {
        val id = workerRunnable.id
        val task = object : FutureTask<R>(workerRunnable) {
            override fun done() {
                try {
                    postResult(id, get())
                } catch (e: InterruptedException) {
                    Log.w(TAG, e)
                } catch (e: ExecutionException) {
                    throw RuntimeException(
                        "An error occurred while executing doInBackground()", e.cause
                    )
                } catch (e: CancellationException) {
                    postResult(id, null)
                }
            }
        }.also {
            futureTaskQueue.offer(it)
        }
        (if (isSerial) SERIAL_EXECUTOR else THREAD_POOL_EXECUTOR).execute(task)
    }

    private fun <R> postResult(
        id: String,
        result: R
    ) {
        Log.d(TAG, "id:$id end")
    }

    fun stopAll() {
        futureTaskQueue.clear()
        THREAD_POOL_EXECUTOR.shutdownNow()
    }

    abstract class WorkerRunnable<Result>(val id: String) : Callable<Result>

}
