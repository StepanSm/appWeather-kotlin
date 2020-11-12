package com.smerkis.weamther.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.smerkis.weamther.viewModels.AbstractViewModel

@SuppressLint("Registered")
class BaseActivity<B : ViewDataBinding, VM : AbstractViewModel>(private val layoutId: Int) :
    AbstractActivity() {

    protected var binding: B? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)

        onCreateComplete(savedInstanceState)
    }

    open fun onCreateComplete(savedInstanceState: Bundle?) {}

    inline fun <reified VM : AbstractViewModel> initViewModel(): VM {
        return ViewModelProvider(this).get(VM::class.java)
    }
}