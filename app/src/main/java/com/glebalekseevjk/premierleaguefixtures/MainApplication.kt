package com.glebalekseevjk.premierleaguefixtures

import android.app.Application
import android.content.Context
import com.glebalekseevjk.premierleaguefixtures.di.AppComponent
import com.glebalekseevjk.premierleaguefixtures.di.DaggerAppComponent

class MainApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.injectMainApplication(this)
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApplication -> {
            appComponent
        }
        else -> {
            this.applicationContext.appComponent
        }
    }