package com.smerkis.weamther.activities

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.smerkis.weamther.R
import com.smerkis.weamther.worker.WeatherWorker
import org.koin.core.component.KoinApiExtension
import java.util.concurrent.TimeUnit

@KoinApiExtension
class MainActivity : AppCompatActivity() {

    private val weatherWorker = PeriodicWorkRequest.Builder(
        WeatherWorker::class.java,
        PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
        TimeUnit.MINUTES
    ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.black);
        }
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("weatherWork", ExistingPeriodicWorkPolicy.REPLACE, weatherWorker)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.clear()
        super.onSaveInstanceState(outState)
    }
}