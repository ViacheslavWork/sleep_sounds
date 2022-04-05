package calm.sleep.relaxing.sounds.noise.utils

import android.util.Log
import calm.sleep.relaxing.sounds.noise.BuildConfig

object MyLog {
    fun showLog(message: String, TAG: String = "") {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}