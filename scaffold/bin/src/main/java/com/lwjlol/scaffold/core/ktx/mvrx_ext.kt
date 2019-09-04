package com.lwjlol.scaffold.core.ktx

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.withState
import com.lwjlol.scaffold.BuildConfig

abstract class WidgetState : MvRxState


/**
 * For use with [BaseListFragment.createViewController].
 *
 * This builds Epoxy models in a background thread.
 */
open class BaseEpoxyController(
    private val lookup: GridLayoutManager.SpanSizeLookup = GridLayoutManager.DefaultSpanSizeLookup(),
    val buildModelsCallback: EpoxyController.() -> Unit = {}
) : AsyncEpoxyController() {
  init {
    isDebugLoggingEnabled = BuildConfig.DEBUG
  }

  override fun buildModels() {
    buildModelsCallback()
  }

  override fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
    return lookup
  }
}

/**
 * Create a [BaseEpoxyController] that builds models with the given callback.
 */
fun Fragment.simpleController(
    buildModels: EpoxyController.() -> Unit
) = BaseEpoxyController {
  // Models are built asynchronously, so it is possible that this is called after the fragment
  // is detached under certain race conditions.
  if (view == null || isRemoving) return@BaseEpoxyController
  buildModels()
}

/**
 * Create a [BaseEpoxyController] that builds models with the given callback.
 * When models are built the current state of the viewmodel will be provided.
 */
fun <S : MvRxState, A : StatefulViewModel<S>> Fragment.simpleController(
    viewModel: A,
    lookup: GridLayoutManager.SpanSizeLookup = GridLayoutManager.DefaultSpanSizeLookup(),
    buildModels: EpoxyController.(state: S) -> Unit
) = BaseEpoxyController(lookup) {
  if (view == null || isRemoving) return@BaseEpoxyController
  withState(viewModel) { state ->
    buildModels(state)
  }
}

/**
 * Create a [BaseEpoxyController] that builds models with the given callback.
 * When models are built the current state of the viewmodels will be provided.
 */
fun <A : BaseMvRxViewModel<B>, B : MvRxState, C : BaseMvRxViewModel<D>, D : MvRxState> Fragment.simpleController(
    viewModel1: A,
    viewModel2: C,
    buildModels: EpoxyController.(state1: B, state2: D) -> Unit
) = BaseEpoxyController {
  if (view == null || isRemoving) return@BaseEpoxyController
  withState(viewModel1, viewModel2) { state1, state2 ->
    buildModels(state1, state2)
  }
}

/**
 * Create a [BaseEpoxyController] that builds models with the given callback.
 * When models are built the current state of the viewmodels will be provided.
 */
fun <A : BaseMvRxViewModel<B>, B : MvRxState, C : BaseMvRxViewModel<D>, D : MvRxState, E : BaseMvRxViewModel<F>, F : MvRxState> Fragment.simpleController(
    viewModel1: A,
    viewModel2: C,
    viewModel3: E,
    buildModels: EpoxyController.(state1: B, state2: D, state3: F) -> Unit
) = BaseEpoxyController {
  if (view == null || isRemoving) return@BaseEpoxyController
  withState(viewModel1, viewModel2, viewModel3) { state1, state2, state3 ->
    buildModels(state1, state2, state3)
  }
}


abstract class BaseModelWithHolder<T : EpoxyHolder> : EpoxyModelWithHolder<T>() {

}

abstract class BaseHolder : EpoxyHolder() {
  protected open lateinit var context: Context
  lateinit var itemView: View
  @CallSuper
  override fun bindView(itemView: View) {
    this.itemView = itemView
    context = itemView.context
  }
}


/**
 * 有状态的 viewModel
 */
abstract class StatefulViewModel<S : WidgetState>(
    initialState: S,
    debugMode: Boolean = BuildConfig.DEBUG
) : BaseMvRxViewModel<S>(initialState, debugMode) {

  companion object {
    private const val TAG = "StatefulViewModel"
  }
}


abstract class StatelessViewModel : ViewModel() {


  companion object {
    private const val TAG = "StatelessViewModel"
  }
}
