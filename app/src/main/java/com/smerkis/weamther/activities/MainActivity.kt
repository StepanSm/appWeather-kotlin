package com.smerkis.weamther.activities

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.smerkis.weamther.R
import com.smerkis.weamther.worker.WeatherWorker
import java.util.concurrent.TimeUnit

class MainActivity : FragmentActivity() {


    private val weatherWorker = PeriodicWorkRequest.Builder(
        WeatherWorker::class.java,
        PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
        TimeUnit.MINUTES
    ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("weatherWork", ExistingPeriodicWorkPolicy.REPLACE, weatherWorker)
    }
}