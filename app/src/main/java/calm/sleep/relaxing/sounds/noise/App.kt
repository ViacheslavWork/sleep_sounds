package calm.sleep.relaxing.sounds.noise

import android.app.Application
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import calm.sleep.relaxing.sounds.noise.di.appModule
import calm.sleep.relaxing.sounds.noise.di.dataModule
import calm.sleep.relaxing.sounds.noise.di.roomModule
import calm.sleep.relaxing.sounds.noise.di.serviceModule
import calm.sleep.relaxing.sounds.noise.utils.EveryDayAlarmManager
import calm.sleep.relaxing.sounds.noise.utils.FirstRunPreferences
import calm.sleep.relaxing.sounds.noise.utils.MyLog.showLog
import com.onesignal.OneSignal

class App : Application() {
    private val everyDayAlarmManager: EveryDayAlarmManager by inject()
    override fun onCreate() {
        super.onCreate()
        activateAppMetrica()
        initOneSignal()
//        showFirebaseToken()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(listOf(appModule, dataModule, serviceModule, roomModule))
        }
        everyDayAlarmManager.startStopEveryDayAlarmIfNeeded()
    }
    private fun initOneSignal() {
        // TODO Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(getString(R.string.one_signal_app_id))
    }

    private fun showFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                showLog("Fetching FCM registration token failed, task.exception)")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = token.toString()
            showLog("token: $msg")
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

    private fun activateAppMetrica() {
        val appMetricaConfig: YandexMetricaConfig =
            YandexMetricaConfig.newConfigBuilder(getString(R.string.ya_app_metrick_API_key))
                .handleFirstActivationAsUpdate(isFirstActivationAsUpdate())
                .withLocationTracking(true)
                .withStatisticsSending(true)
                .build()
        YandexMetrica.activate(applicationContext, appMetricaConfig)
    }

    private fun isFirstActivationAsUpdate(): Boolean {
        // Implement logic to detect whether the app is opening for the first time.
        // For example, you can check for files (settings, databases, and so on),
        // which the app creates on its first launch.
        return FirstRunPreferences.isFirstRun(applicationContext)
    }
}