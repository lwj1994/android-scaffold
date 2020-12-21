package com.lwjlol.scaffold.core.image

import android.app.Activity
import android.content.ComponentCallbacks2.TRIM_MEMORY_COMPLETE
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.WorkerThread
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.SCALE_TYPE_START
import com.lwjlol.imagehosting.utils.ThreadPoolUtil
import com.lwjlol.ktx.buildRoundedCornerDrawable
import com.lwjlol.ktx.colorInt
import com.lwjlol.ktx.screenHeight
import com.lwjlol.scaffold.ScaffoldApplication
import java.io.File

/**
 * @author Wenchieh.Lu  2018/8/15
 */
class ImageLoader private constructor() {
    private val context = ScaffoldApplication.application

    init {
        // 内存缓存策略
        glide.apply {
            setMemoryCategory(MemoryCategory.NORMAL)
            // 触发 LRU 缓存清理的阈值，最高 80%
            trimMemory(TRIM_MEMORY_COMPLETE)
        }
        if (isInit) {
            throw RuntimeException("ImageLoader instance has inited!")
        }
        isInit = true
    }

    fun getBitmap(
        w: Int,
        h: Int,
        config: Bitmap.Config
    ) = glide.bitmapPool.get(w, h, config)

    fun putBitmap(bitmap: Bitmap) {
        glide.bitmapPool.put(bitmap)
    }

    private val glide: Glide
        get() = Glide.get(context)

    fun clearDiskCache() {
        glide.clearDiskCache()
    }

    fun clearMemory() {
        glide.clearMemory()
    }

    fun clearAllCache() {
        clearDiskCache()
        clearMemory()
    }

    companion object {
        private const val TAG = "ImageLoader"

        @JvmStatic
        private var isInit = false

        @JvmStatic
        fun get() = Loader.INSTANCE
    }

    private object Loader {
        val INSTANCE = ImageLoader()
    }

    fun load(
        fragment: Fragment,
        url: String,
        view: ImageView
    ) {
        getRequestManager(fragment).load(url)
            .into(view)
    }

    fun load(
        context: Context,
        url: String,
        view: ImageView
    ) {
        getRequestManager(context).load(url)
            .into(view)
    }

    private fun getRequestManager(context: Context = this@ImageLoader.context) =
        Glide.with(context)
            .apply {
                setPauseAllRequestsOnTrimMemoryModerate(true)
            }

    private fun getRequestManager(fragment: Fragment) = Glide.with(fragment)
        .apply {
            setPauseAllRequestsOnTrimMemoryModerate(true)
        }

    @WorkerThread
    fun download(
        context: Context = this@ImageLoader.context,
        url: String
    ) = getRequestManager(context).downloadOnly()
        .load(url)
        .submit()

    @WorkerThread
    fun download(
        context: Context = this@ImageLoader.context,
        uri: Uri
    ) = getRequestManager(context).downloadOnly()
        .load(uri)
        .submit()

    @WorkerThread
    fun preload(
        context: Context = this@ImageLoader.context,
        uri: Uri
    ) = getRequestManager(context).downloadOnly()
        .load(uri)
        .submit()

    fun load(
        url: String,
        target: ImageView
    ) {
        if (!target.checkActivity) return
        Glide.with(target)
            .load(url)
            .apply(RequestOptions().apply {
                setPlaceholder()
            })
            .into(target)
    }

    fun loadWithoutCache(
        url: String,
        target: ImageView
    ) {
        Glide.with(target)
            .load(url)
            .apply(RequestOptions().apply {
                setPlaceholder()
                skipMemoryCache(true)
                diskCacheStrategy(DiskCacheStrategy.NONE)
            })
            .into(target)
    }

    fun load(
        uri: Uri,
        target: ImageView
    ) {
        Glide.with(target)
            .load(uri)
            .apply(
                RequestOptions().apply {
                    setPlaceholder()
                }
            )
            .into(target)
    }

    fun load(
        bitmap: Bitmap,
        target: ImageView
    ) {
        Glide.with(target)
            .load(bitmap)
            .apply(
                RequestOptions().apply {
                    setPlaceholder()
                }
            )
            .into(target)
    }

    fun load(
        @DrawableRes res: Int,
        target: ImageView
    ) {
        Glide.with(target)
            .load(res)
            .into(target)
    }

    fun load(
        @DrawableRes res: Int,
        view: ImageView,
        requestOptions: RequestOptions
    ) {
        if (view.checkActivity)
            Glide.with(view)
                .load(res)
                .apply(requestOptions)
                .into(view)
    }

    fun load(
        url: String,
        target: ImageView,
        requestOptions: RequestOptions
    ) {
        if (target.checkActivity)
            Glide.with(target)
                .load(url)
                .apply(requestOptions)
                .into(target)
    }

    fun load(
        url: String,
        target: ImageView,
        width: Int,
        height: Int
    ) {
        if (target.checkActivity)
            Glide.with(target)
                .load(url)
                .apply(
                    RequestOptions().apply { override(width, height) })
                .into(
                    target
                )
    }

    fun RequestOptions.setPlaceholder() =
        placeholder(buildRoundedCornerDrawable("#F7F7F7".colorInt))

    private val View.checkActivity: Boolean
        get() {
            return context.checkActivity
        }
    private val Context?.checkActivity: Boolean
        get() {
            val context = this ?: return false
            if (context is Activity) {
                if (context.isDestroyed || context.isFinishing) return false
            }
            return true
        }

    fun load(
        url: Uri,
        target: ImageView,
        width: Int,
        height: Int
    ) {
        Glide.with(target)
            .load(url)
            .apply(
                RequestOptions().apply { override(width, height) })
            .into(
                target
            )
    }

    fun loadFile(
        url: String,
        target: ImageView
    ) {
        Glide.with(target)
            .load(File(url))
            .into(target)
    }

    fun loadToSimpleTargetView(
        view: View,
        path: String,
        options: RequestOptions = RequestOptions(),
        callback: (Bitmap) -> Unit
    ) {
        Glide.with(view)
            .asBitmap()
            .apply(options)
            .load(
                path
            )
            .into(object : CustomViewTarget<View, Bitmap>(view) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                }

                override fun onResourceCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    callback(resource)
                }
            })
    }

    fun loadForCallback(
        context: Context,
        path: String,
        options: RequestOptions = RequestOptions(),
        fail: (String) -> Unit = {},
        success: (Bitmap) -> Unit = {}
    ) {
        getRequestManager(context).asBitmap()
            .apply(options)
            .load(
                path
            )
            .into(SimpleTarget(success, {
                fail(path)
            }))
    }

    fun loadForCallback(
        view: ImageView,
        path: String,
        options: RequestOptions = RequestOptions(),
        fail: (String) -> Unit = {},
        success: (Bitmap) -> Unit = {}
    ) {
        Glide.with(view)
            .asBitmap()
            .apply(options)
            .load(path)
            .into(
                SimpleTarget(
                    success,
                    {
                        fail(path)
                    })
            )
    }

    fun loadForCallback(
        fragment: Fragment,
        path: String,
        options: RequestOptions = RequestOptions(),
        fail: (String) -> Unit = {},
        success: (Bitmap) -> Unit = {}
    ) {
        if (fragment.context.checkActivity)
            Glide.with(fragment)
                .asBitmap()
                .apply(options)
                .load(path)
                .into(
                    SimpleTarget(
                        success,
                        {
                            fail(path)
                        })
                )
    }

    fun pause(fragment: Fragment) {
        Glide.with(fragment)
            .pauseRequests()
    }

    fun pause(activity: Activity) {
        Glide.with(activity)
            .pauseRequests()
    }

    fun resume(fragment: Fragment) {
        Glide.with(fragment)
            .resumeRequests()
    }

    fun resume(activity: Activity) {
        Glide.with(activity)
            .resumeRequests()
    }

    fun loadBitmap(
        view: ImageView,
        bitmap: Bitmap,
        requestOptions: RequestOptions = RequestOptions()
    ) {
        Glide.with(view)
            .load(bitmap)
            .apply(requestOptions)
            .into(view)
    }

    fun pause(context: Context) {
        getRequestManager(context).pauseRequests()
    }

    fun resume(context: Context) {
        getRequestManager(context).resumeRequests()
    }

    class SimpleTarget(
        private val success: (Bitmap) -> Unit,
        private val fail: (String) -> Unit
    ) : Target<Bitmap> {
        override fun onLoadStarted(placeholder: Drawable?) {
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            fail("")
        }

        override fun getSize(cb: SizeReadyCallback) {
        }

        override fun getRequest(): Request? = null

        override fun onStop() {
        }

        override fun setRequest(request: Request?) {
        }

        override fun removeCallback(cb: SizeReadyCallback) {
        }

        override fun onLoadCleared(placeholder: Drawable?) {
        }

        override fun onResourceReady(
            resource: Bitmap,
            transition: Transition<in Bitmap>?
        ) {
            success(resource)
        }

        override fun onStart() {
        }

        override fun onDestroy() {
        }
    }
}

fun ImageView.load(url: String) {
    ImageLoader.get()
        .load(url, this)
}

fun ImageView.load(
    url: String,
    fragment: Fragment
) {
    ImageLoader.get()
        .load(fragment, url, this)
}

fun ImageView.load(bitmap: Bitmap) {
    ImageLoader.get()
        .load(bitmap, this)
}

fun ImageView.load(@DrawableRes res: Int) {
    ImageLoader.get()
        .load(res, this)
}

fun ImageView.load(uri: Uri) {
    ImageLoader.get()
        .load(uri, this)
}

fun SubsamplingScaleImageView.load(
    url: String,
    options: RequestOptions = RequestOptions()
) {
    ImageLoader.get()
        .loadToSimpleTargetView(this, url, options) {
            if (it.width * 3 < it.height) {
                val scale = it.height * 1F / screenHeight
                setEagerLoadingEnabled(true)
                setExecutor(ThreadPoolUtil.THREAD_POOL_EXECUTOR)
                setMinimumScaleType(SCALE_TYPE_START)
            }
            setImage(ImageSource.cachedBitmap(it))
        }
}