package white.noise.sounds.baby.sleep.model

data class Sound(
    val title: String,
    val file: String,
    val icon: String,
    var volume: Int,
    var isPlaying: Boolean = false,
    val category: SoundCategory
)

enum class SoundCategory {
    Rain, Nature, Animal, Transport, CityAndInstrument, WhiteNoise, Meditation, Other
}
