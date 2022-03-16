package white.noise.sounds.baby.sleep.data.provider

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.model.SoundCategory
import white.noise.sounds.baby.sleep.utils.AssetManager
import java.util.*

class SoundsProvider(val assetManager: AssetManager) {
    fun getSounds(): List<Sound> {
        val sounds = mutableListOf<Sound>()
        sounds.addAll(SoundsEnum.values().map { it.getSound() })
        return sounds
    }
}

