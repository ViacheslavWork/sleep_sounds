package relax.deep.sleep.sounds.calm

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import relax.deep.sleep.sounds.calm.di.appModule
import relax.deep.sleep.sounds.calm.di.dataModule
import relax.deep.sleep.sounds.calm.di.roomModule
import relax.deep.sleep.sounds.calm.di.serviceModule
import relax.deep.sleep.sounds.calm.utils.EveryDayAlarmManager

class App : Application() {
    private val everyDayAlarmManager: EveryDayAlarmManager by inject()
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(listOf(appModule, dataModule, serviceModule, roomModule))
            everyDayAlarmManager.startStopEveryDayAlarmIfNeeded()
        }
    }


}