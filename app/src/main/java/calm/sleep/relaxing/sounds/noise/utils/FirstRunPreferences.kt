package calm.sleep.relaxing.sounds.noise.utils

import android.content.Context
import androidx.preference.PreferenceManager

object FirstRunPreferences {
    private const val PREF_IS_FIRST_RUN = "is_first_run"
    fun isFirstRun(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(PREF_IS_FIRST_RUN, true)
    }

    fun setIsNotFirstRun(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(PREF_IS_FIRST_RUN, false)
            .apply()
    }
}