package com.smerkis.weamther

import android.app.Application
import com.smerkis.weamther.di.NetworkModule
import com.smerkis.weamther.di.repositoryModule
import com.smerkis.weamther.di.viewModelModule
import io.paperdb.Paper
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@FlowPreview
class MyApp : Application() {

    val appName: String
        get() = resources.getString(R.string.app_name)


    override fun onCreate() {
        super.onCreate()
        instance = this
        Paper.init(this)

        startKoin {
            printLogger()
            androidContext(this@MyApp)
            modules(viewModelModule, NetworkModule.networkModule, repositoryModule)
        }
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}