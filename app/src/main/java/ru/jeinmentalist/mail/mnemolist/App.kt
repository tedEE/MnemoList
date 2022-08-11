package ru.jeinmentalist.mail.mnemolist

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp

import ru.jeinmentalist.mail.mnemolist.UI.utilits.createNotificationChannel
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    companion object {
        const val CHANNEL_ID = "exampleServiceChannel"
        const val CHANNEL_NAME = "Name Channel"
        private var _appVisible: Boolean? = null

        fun isAppVisible(): Boolean {
            return _appVisible ?: false
        }

        fun appResumed() {
            _appVisible = true
        }

        fun appPaused() {
            _appVisible = false
        }
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(applicationContext, CHANNEL_ID, CHANNEL_NAME)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }


}