package white.noise.sounds.baby.sleep.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import kotlinx.parcelize.Parcelize
import white.noise.sounds.baby.sleep.model.SoundCategory
@Parcelize
data class SoundFromResources (
    var id: Long = 0,
    val title: String,
    @RawRes
    val file: Int,
    @DrawableRes
    val icon: Int,
    var volume: Int,
    var isPlaying: Boolean = false,
    val category: SoundCategory
) : Parcelable