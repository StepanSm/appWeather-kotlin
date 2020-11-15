package com.smerkis.weamther.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.smerkis.weamther.MyApp
import com.smerkis.weamther.repository.weather.WeatherRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class WeatherWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var repo: WeatherRepo

    init {
        MyApp.instance.getWorkerComponent().inject(this)
    }


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
            WeatherBus.instance.post(it)
            WeatherBus.instance.unregister(this@WeatherWorker)
            Result.failure()
        }.single()
    }
}
