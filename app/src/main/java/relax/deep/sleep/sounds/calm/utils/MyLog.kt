package relax.deep.sleep.sounds.calm.utils

import android.util.Log
import relax.deep.sleep.sounds.calm.BuildConfig

object MyLog {
    fun showLog(message: String, TAG: String = "") {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}