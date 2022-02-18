package white.noise.sounds.baby.sleep.model

import android.graphics.Bitmap

data class Mix(
    val title: String,
    val sounds: MutableList<Sound> = mutableListOf(),
    var picturePath: String,
    val category: MixCategory?
)
enum class MixCategory {
    AllSounds, Sleep,Rain, Relax, Others
}
