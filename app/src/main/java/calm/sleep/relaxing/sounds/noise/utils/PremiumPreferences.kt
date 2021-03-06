package calm.sleep.relaxing.sounds.noise.utils

import android.content.Context
import androidx.preference.PreferenceManager

object PremiumPreferences {
    private const val PREF_IS_PREMIUM = "is_premium"
    fun userHasPremiumStatus(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(PREF_IS_PREMIUM, false)
    }

    fun setStoredPremiumStatus(context: Context, isPremium: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(PREF_IS_PREMIUM, isPremium)
            .apply()
    }
}