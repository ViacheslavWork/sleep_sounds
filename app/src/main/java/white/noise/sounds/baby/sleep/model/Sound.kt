package white.noise.sounds.baby.sleep.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sound(
    var id: Long = 0,
    val title: String,
    val file: String,
    val icon: String,
    var volume: Int,
    var isPlaying: Boolean = false,
    val category: SoundCategory
) : Parcelable

enum class SoundCategory(val title: String) {
    Rain("Rain"),
    Nature("Nature"),
    Animal("Animal"),
    Transport("Transport"),
    CityAndInstrument("City And Instrument"),
    WhiteNoise("White Noise"),
    Meditation("Meditation"),
    Other("Other")
}
