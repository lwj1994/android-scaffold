package com.lwjlol.scaffold.core.mvrx

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.BuildConfig
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.withState

/**
 * For use with [BaseListFragment.epoxyController].
 *
 * This builds Epoxy models in a background thread.
 * @param lookup GridLayoutManager SpanSizeLookup
 * @param buildModelsCallback  应运行在单线程中
 */
open class BaseEpoxyController(
    private var lookup: SpanSizeLookup = GridLayoutManager.DefaultSpanSizeLookup(),
    val buildModelsCallback: EpoxyController.() -> Unit = {}
) : AsyncEpoxyController() {
    override fun onExceptionSwallowed(exception: RuntimeException) {
        super.onExceptionSwallowed(exception)
    }

    init {
        isDebugLoggingEnabled = BuildConfig.DEBUG
    }

    fun setLookup(lookup: SpanSizeLookup) {
        this.lookup = lookup
    }

    override fun buildModels() {
        buildModelsCallback()
    }

    override fun getSpanSizeLookup(): SpanSizeLookup {
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
fun <S : PageState, A : MavericksViewModel<S>> Fragment.simpleController(
    viewModel: A,
    lookup: SpanSizeLookup = GridLayoutManager.DefaultSpanSizeLookup(),
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
fun <A : MavericksViewModel<B>, B : PageState, C : MavericksViewModel<D>, D : PageState> Fragment.simpleController(
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
fun <A : MavericksViewModel<B>, B : PageState, C : MavericksViewModel<D>, D : PageState, E : MavericksViewModel<F>, F : PageState> Fragment.simpleController(
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
