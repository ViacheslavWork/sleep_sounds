package relax.deep.sleep.sounds.calm.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mix(
    var id: Long,
    val title: String,
    val sounds: MutableList<Sound> = mutableListOf(),
    var picturePath: Uri?,
    val category: MixCategory?,
    var isPremium: Boolean = true,
    val isCustom: Boolean = false
) : Parcelable

enum class MixCategory(val title: String) {
    AllSounds("All Sounds"),
    Sleep("Sleep"),
    Rain("Rain"),
    Relax("Relax"),
    Others("Others")
}

