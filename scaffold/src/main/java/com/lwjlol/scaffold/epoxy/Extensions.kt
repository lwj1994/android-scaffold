package com.lwjlol.scaffold.epoxy

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.airbnb.epoxy.EpoxyControllerAdapter
import com.airbnb.epoxy.EpoxyModel
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * 给使用了 Epoxy 的[RecyclerView] 便捷的设置 padding
 * @param block:(position: 位置, model: EpoxyModel<*> 当前的item 的model, outRect: Rect 绘制的范围, size: 布局内总的个数)
 */
inline fun RecyclerView.addItemPaddingForEpoxy(
    crossinline block: (position: Int, model: EpoxyModel<*>, outRect: Rect, size: Int) -> Unit
) {
    val myAdapter = (adapter as? EpoxyControllerAdapter) ?: return
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            if (position == -1) return

            val models = myAdapter.copyOfModels
            if (models.isEmpty()) return
            val model = models[position]
            val count = parent.layoutManager?.itemCount ?: 0
            if (count == 0) return
            block(position, model, outRect, count)
        }
    })
}


/**
 * 为 [GridLayoutManager] 创建 Lookup
 */
inline fun RecyclerView.createGridLookupForEpoxy(crossinline map: (model: EpoxyModel<*>) -> Int): GridLayoutManager.SpanSizeLookup {
    val myAdapter = (adapter as? EpoxyControllerAdapter)
        ?: throw IllegalAccessException("adapter is not EpoxyControllerAdapter")
    val manager = (layoutManager as? GridLayoutManager)
        ?: throw IllegalAccessException("layoutManager is not GridLayoutManager")
    return object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val models = myAdapter.copyOfModels
            if (models.isEmpty()) return manager.spanCount
            val model = models[position]
            return map(model)
        }
    }
}


/**
 * 使用 viewBinding 获取 holder 的控件
 */
inline fun <reified T : ViewBinding> BaseEpoxyHolder.viewBinding() =
    EpoxyHolderBindingDelegate(T::class.java)

class EpoxyHolderBindingDelegate<T : ViewBinding>(
    bindingClass: Class<T>
) : ReadOnlyProperty<BaseEpoxyHolder, T> {
    private var binding: T? = null
    private val bindMethod = bindingClass.getMethod("bind", View::class.java)

    override fun getValue(
        thisRef: BaseEpoxyHolder,
        property: KProperty<*>
    ): T {
        binding?.let { return it }
        @Suppress("UNCHECKED_CAST")
        binding = bindMethod.invoke(null, thisRef.itemView) as T
        return binding!!
    }
}
