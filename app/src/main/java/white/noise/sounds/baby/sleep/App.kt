package white.noise.sounds.baby.sleep

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import white.noise.sounds.baby.sleep.di.appModule
import white.noise.sounds.baby.sleep.di.dataModule
import white.noise.sounds.baby.sleep.di.roomModule
import white.noise.sounds.baby.sleep.di.serviceModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(listOf(appModule, dataModule, serviceModule, roomModule))
        }
    }

}