package white.noise.sounds.baby.sleep.ui.timer

import android.os.Parcelable
import org.threeten.bp.LocalTime
import java.io.Serializable


sealed class Times(val title: String, val time: LocalTime?): Serializable {
    class Custom(time: LocalTime?) : Times("Custom", time)
    object off : Times("Off", null)
    object _10minutes : Times("10 minutes", LocalTime.of(0, 10))
    object _15minutes : Times("15 minutes", LocalTime.of(0, 15))
    object _20minutes : Times("20 minutes", LocalTime.of(0, 20))
    object _30minutes : Times("30 minutes", LocalTime.of(0, 30))
    object _40minutes : Times("40 minutes", LocalTime.of(0, 40))
    object _50minutes : Times("50 minutes", LocalTime.of(0, 50))
    object _1hour : Times("1 hour", LocalTime.of(1, 0))
    object _2hours : Times("2 hours", LocalTime.of(2, 0))
    object _3hours : Times("3 hours", LocalTime.of(3, 0))
    object _4hours : Times("4 hours", LocalTime.of(4, 0))
    object _8hours : Times("8 hours", LocalTime.of(8, 0))
}