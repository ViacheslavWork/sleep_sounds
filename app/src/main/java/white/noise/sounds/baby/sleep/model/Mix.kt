package white.noise.sounds.baby.sleep.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mix(
    var id: Long,
    val title: String,
    val sounds: MutableList<Sound> = mutableListOf(),
    var picturePath: Uri,
    val category: MixCategory?,
    val isPremium: Boolean = false
) : Parcelable

enum class MixCategory(val title: String) {
    AllSounds("All Sounds"),
    Sleep("Sleep"),
    Rain("Rain"),
    Relax("Relax"),
    Others("Others")
}

