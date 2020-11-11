package com.smerkis.weamther

import android.app.Application
import com.smerkis.weamther.di.AppComponent
import com.smerkis.weamther.di.DaggerAppComponent
import com.smerkis.weamther.di.modules.AppModule
import io.paperdb.Paper

class MyApp : Application() {

    val appName: String
        get() = resources.getString(R.string.app_name)
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        Paper.init(this)
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}