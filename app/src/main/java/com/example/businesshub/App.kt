package com.example.businesshub

import android.app.Application
import com.parse.Parse
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    val abc = 10

    override fun onCreate() {
        super.onCreate()
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        )
    }
}