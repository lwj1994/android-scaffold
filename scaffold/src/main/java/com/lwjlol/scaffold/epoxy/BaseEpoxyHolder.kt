package com.lwjlol.scaffold.epoxy

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import com.airbnb.epoxy.EpoxyHolder

abstract class BaseEpoxyHolder : EpoxyHolder() {
    lateinit var context: Context
    lateinit var itemView: View

    @CallSuper
    override fun bindView(itemView: View) {
        this.context = itemView.context
        this.itemView = itemView
    }
}

