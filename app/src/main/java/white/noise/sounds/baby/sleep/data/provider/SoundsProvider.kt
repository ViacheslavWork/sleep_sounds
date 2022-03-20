package white.noise.sounds.baby.sleep.data.provider

import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.utils.AssetManager

class SoundsProvider(val assetManager: AssetManager) {
    fun getSounds(): List<Sound> {
        val sounds = mutableListOf<Sound>()
        sounds.addAll(SoundsEnum.values().map { it.getSound() })
        return sounds
    }
}

