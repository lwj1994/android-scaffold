package com.lwjlol.scaffold.ui

import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import com.lwjlol.scaffold.R

abstract class BaseNavHostActivity : BaseActivity() {
    protected abstract val graphRes: Int

    protected val navController by lazy(LazyThreadSafetyMode.NONE) {
        findNavController(this, R.id.activity_base_nav_host)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scaffold_activity_base_nav)
        startNavigate()
    }

    protected open fun startNavigate() {
        navController.setGraph(graphRes, getPrimeFragmentArgs())
    }

    /**
     * primeFragment 的 bundle 数据
     */
    protected open fun getPrimeFragmentArgs() = Bundle()

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    companion object {
        private const val TAG = "BaseNavHostActivity"
    }
}
