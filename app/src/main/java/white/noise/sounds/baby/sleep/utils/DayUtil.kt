package white.noise.sounds.baby.sleep.utils

import android.util.Log
import white.noise.sounds.baby.sleep.BuildConfig
import java.util.*

object DayUtil {
    @Throws(Exception::class)
    fun toDay(day: Int): String {
        when (day) {
            Calendar.SUNDAY -> return "Sunday"
            Calendar.MONDAY -> return "Monday"
            Calendar.TUESDAY -> return "Tuesday"
            Calendar.WEDNESDAY -> return "Wednesday"
            Calendar.THURSDAY -> return "Thursday"
            Calendar.FRIDAY -> return "Friday"
            Calendar.SATURDAY -> return "Saturday"
        }
        throw Exception("Could not locate day")
    }
}