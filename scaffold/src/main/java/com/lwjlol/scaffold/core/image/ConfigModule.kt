package com.lwjlol.scaffold.core.image

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class ConfigModule : AppGlideModule() {
    override fun applyOptions(
        context: Context,
        builder: GlideBuilder
    ) {
        val calculator = MemorySizeCalculator.Builder(context)
            .build()
        val maxCacheSize = calculator.memoryCacheSize.toLong()
        val cache = LruResourceCache(maxCacheSize)
        builder.setMemoryCache(cache)
        val bitmapPoolSize = calculator.bitmapPoolSize.toLong()
        builder.setBitmapPool(LruBitmapPool(bitmapPoolSize))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, IMAGE_MAX_DISK_SIZE))
    }

    companion object {
        private const val TAG = "ConfigModule"
        private const val IMAGE_MAX_DISK_SIZE = 300 * 1024 * 1024L
    }
}