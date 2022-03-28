package relax.deep.sleep.sounds.calm.data.provider

import relax.deep.sleep.sounds.calm.model.Sound

class SoundsProvider() {
    fun getSounds(): List<Sound> {
        val sounds = mutableListOf<Sound>()
        sounds.addAll(SoundsEnum.values().map { it.getSound() })
        return sounds
    }
}

