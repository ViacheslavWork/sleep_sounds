package white.noise.sounds.baby.sleep.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mix(
    var id: Long = 0,
    val title: String,
    val sounds: MutableList<Sound> = mutableListOf(),
    var picturePath: String,
    val category: MixCategory?
):Parcelable

enum class MixCategory(val title: String) {
    AllSounds("All Sounds"),
    Sleep("Sleep"),
    Rain("Rain"),
    Relax("Relax"),
    Others("Others")
}

