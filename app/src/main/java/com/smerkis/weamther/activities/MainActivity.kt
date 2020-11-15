package com.smerkis.weamther.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.smerkis.weamther.R
import com.smerkis.weamther.worker.WeatherWorker
import java.util.concurrent.TimeUnit

enum class Layout(val id: Int) {
    SPLASH(R.id.splashFragment), MAIN(R.id.mainFragment)
}

class MainActivity : FragmentActivity(), MainNavigator {

    private lateinit var nav: NavController
    private val weatherWorker = PeriodicWorkRequest.Builder(
        WeatherWorker::class.java,
        PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
        TimeUnit.MINUTES
    ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav = Navigation.findNavController(this, R.id.main_container)

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("weatherWork", ExistingPeriodicWorkPolicy.REPLACE, weatherWorker)
    }

    override fun navigateTo(layout: Layout) {
        Log.d("MainActivity", "onNextFragment id = $layout")
        nav.navigate(layout.id)
    }

    override fun closeApp() {
        finish()
    }

    override fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}