package relax.deep.sleep.sounds.calm.data.provider

import relax.deep.sleep.sounds.calm.model.Sound
import relax.deep.sleep.sounds.calm.utils.AssetManager

class SoundsProvider(val assetManager: AssetManager) {
    fun getSounds(): List<Sound> {
        val sounds = mutableListOf<Sound>()
        sounds.addAll(SoundsEnum.values().map { it.getSound() })
        return sounds
    }
}

