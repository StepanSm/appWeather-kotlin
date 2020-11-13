package com.smerkis.weamther.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.smerkis.weamther.R
import timber.log.Timber

enum class Layout(val id: Int) {
    SPLASH(R.id.splashFragment), MAIN(R.id.mainFragment)
}

class MainActivity : FragmentActivity(), MainNavigator {

    private lateinit var nav: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav = Navigation.findNavController(this, R.id.main_container)
    }

    override fun navigateTo(layout: Layout) {
        Timber.d("onNextFragment id = $layout")
        nav.navigate(layout.id)
    }

    @SuppressLint("LogNotTimber")
    override fun navigateTo(directions: NavDirections) {
        Timber.d("onNextFragment id = ${directions.actionId}")
        nav.navigate(directions)
    }

    override fun closeApp() {
        finish()
    }
}