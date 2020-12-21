package com.lwjlol.scaffold.core.network

import android.util.Log
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.lwjlol.imagehosting.utils.ThreadPoolUtil.THREAD_POOL_EXECUTOR
import com.lwjlol.scaffold.ScaffoldApplication
import com.lwjlol.scaffold.ScaffoldApplication.Companion.application
import com.lwjlol.scaffold.debugMode
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

val glideClient: OkHttpClient by lazy {
    val builder = OkHttpClient.Builder()
        .connectionPool(ConnectionPool())
        .dispatcher(Dispatcher(THREAD_POOL_EXECUTOR))
        .cache(Cache(File(application.cacheDir.toString() + "/glide"), 1024))

    if (debugMode) {
        builder.addInterceptor(HttpLoggingInterceptor { message ->
            Log.d("IH_OkHttp",
                String(message.toByteArray()))
        }.apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        builder.addNetworkInterceptor(FlipperOkhttpInterceptor(ScaffoldApplication.networkFlipperPlugin))
    }

    builder.build()
}