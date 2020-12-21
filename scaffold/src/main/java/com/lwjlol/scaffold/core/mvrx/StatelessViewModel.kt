package com.lwjlol.scaffold.core.mvrx

import androidx.lifecycle.ViewModel

abstract class StatelessViewModel : ViewModel() {
    companion object {
        private const val TAG = "StatelessViewModel"
    }
}
