package com.smerkis.weamther.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.smerkis.weamther.MyApp
import com.smerkis.weamther.repository.weather.WeatherRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class WeatherWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams), KoinComponent {

    private val repo: WeatherRepo by inject ()

    @FlowPreview
    override suspend fun doWork(): Result {
        withContext(Dispatchers.Main) {
            WeatherBus.instance.register(this)
        }
        Log.d("WeatherWorker", " doWork")

        return repo.loadCity().flatMapConcat { city ->
            repo.downloadWeather(city).map {
                withContext(Dispatchers.Main) {
                    WeatherBus.instance.post(it)
                    WeatherBus.instance.unregister(this@WeatherWorker)
                }
                Result.success()
            }
        }.catch {
            withContext(Dispatchers.Main) {
                WeatherBus.instance.post(it)
                WeatherBus.instance.unregister(this@WeatherWorker)
            }
            Result.failure()
        }.single()

    }
}
