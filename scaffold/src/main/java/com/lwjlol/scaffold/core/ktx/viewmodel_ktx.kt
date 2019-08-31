package com.lwjlol.scaffold.core.ktx

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.airbnb.mvrx.lifecycleAwareLazy

/**
 * 得到一个无状态的 [ViewModel]
 */
inline fun <T, reified VM : StatelessViewModel> T.simpleViewModel()
    where T : FragmentActivity = lifecycleAwareLazy(this) {
  ViewModelProviders.of(this)[VM::class.java]
}

/**
 * 得到一个无状态的 [ViewModel]
 */
inline fun <T, reified VM : StatelessViewModel> T.simpleViewModel()
    where T : Fragment = lifecycleAwareLazy(this) {
  ViewModelProviders.of(requireActivity())[VM::class.java]
}

/**
 * 得到一个无状态的 [ViewModel]
 */
inline fun <T, reified VM : StatelessViewModel> T.simpleViewModel()
    where T : View = lifecycleAwareLazy(context as FragmentActivity) {
  ViewModelProviders.of(context as FragmentActivity)[VM::class.java]
}