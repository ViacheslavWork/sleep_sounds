package white.noise.sounds.baby.sleep.model

data class Mix(
    val title: String,
    val sounds: MutableList<Sound> = mutableListOf(),
    var picturePath: String,
    val category: MixCategory?
)

enum class MixCategory(val title: String) {
    AllSounds("All Sounds"),
    Sleep("Sleep"),
    Rain("Rain"),
    Relax("Relax"),
    Others("Others")
}

