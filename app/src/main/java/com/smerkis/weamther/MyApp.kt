package com.smerkis.weamther

import android.app.Application
import com.smerkis.weamther.di.AppComponent
import com.smerkis.weamther.di.DaggerAppComponent
import com.smerkis.weamther.di.ViewModelSubComponent
import io.paperdb.Paper

class MyApp : Application() {

    val appName: String
        get() = resources.getString(R.string.app_name)

    private lateinit var appComponent: AppComponent
    private lateinit var viewModelSubComponent: ViewModelSubComponent

    fun getAppComponent(): AppComponent {
        return appComponent
    }

    fun getViewModelSubComponent(): ViewModelSubComponent {
        return viewModelSubComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Paper.init(this)
        appComponent = DaggerAppComponent.builder().application(this).build()

        viewModelSubComponent = appComponent.viewModelSubComponentBuilder().build()
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}