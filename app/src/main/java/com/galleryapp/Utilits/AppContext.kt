package com.galleryapp.Utilits

import android.app.Application
import com.galleryapp.Utilits.di.appModule
import com.galleryapp.Utilits.di.repoModule
import com.galleryapp.Utilits.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppContext : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AppContext)
            modules(listOf(
                appModule,
                viewModelModule,
                repoModule
            ))
        }
    }
}