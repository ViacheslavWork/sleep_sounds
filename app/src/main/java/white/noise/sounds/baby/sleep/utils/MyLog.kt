package white.noise.sounds.baby.sleep.utils

import android.util.Log
import white.noise.sounds.baby.sleep.BuildConfig

object MyLog {
    fun showLog( message: String, TAG: String = "") {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}