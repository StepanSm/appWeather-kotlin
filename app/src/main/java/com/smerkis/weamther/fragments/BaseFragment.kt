package com.smerkis.weamther.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.smerkis.weamther.R
import com.smerkis.weamther.activities.MainNavigator

abstract class BaseFragment<VM : ViewModel, B : ViewDataBinding>(private val layoutId: Int) :
    Fragment() {

    protected lateinit var navigator: MainNavigator
    protected lateinit var binding: B

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as MainNavigator
    }

    abstract fun initDi()

    @SuppressLint("LongNotTimber")
    fun logD(msg: String) {
        Log.d(this.javaClass.simpleName + "TAG", msg)
    }

    @SuppressLint("LongNotTimber")
    fun logE(msg: String) {
        Log.e(this.javaClass.simpleName + "TAG", msg)
    }

    @SuppressLint("LongNotTimber")
    fun logI(msg: String) {
        Log.i(this.javaClass.simpleName + "TAG", msg)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initDi()
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    fun showInfoDialog(title: String, message: String?, call: (() -> Unit)? = null) {
        AlertDialog.Builder(navigator as Context)
            .setTitle(title)
            .setMessage(message ?: getString(R.string.dialog_missing_message))
            .setCancelable(false)
            .setPositiveButton("Ok") { _, _ ->
                call?.invoke()
            }
            .create()
            .show()
    }
}