package com.lwjlol.scaffold.core.epoxy

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import com.airbnb.epoxy.EpoxyHolder

abstract class BaseEpoxyHolder : EpoxyHolder() {
    protected open lateinit var context: Context
    lateinit var itemView: View

    @CallSuper
    override fun bindView(itemView: View) {
        this.itemView = itemView
        context = itemView.context
    }
}
