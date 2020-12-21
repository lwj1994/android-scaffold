package com.lwjlol.scaffold.ui

import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel @JvmOverloads constructor(private val saveState: SavedStateHandle? = null) :
    ViewModel(),
    LifecycleOwner {
    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    init {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onCleared() {
        super.onCleared()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    @ExperimentalCoroutinesApi
    fun launch(
        context: CoroutineContext = Dispatchers.Main.immediate,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(context, start, block)

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    abstract class Factory(owner: SavedStateRegistryOwner, defaultArgs: Bundle) :
        AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    }

    companion object {
        private const val TAG = "StatelessViewModel"


    }
}
