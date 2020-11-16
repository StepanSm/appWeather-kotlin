package com.smerkis.weamther.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.smerkis.weamther.activities.MainNavigator

abstract class BaseFragment<VM : ViewModel, B : ViewBinding>(layoutId: Int) :
    Fragment(layoutId) {

    protected lateinit var navigator: MainNavigator
    protected abstract val binding: B

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as MainNavigator
        initDi()
    }

    abstract fun initDi()
}