package calm.sleep.relaxing.sounds.noise.data.provider

import calm.sleep.relaxing.sounds.noise.model.Sound

class SoundsProvider() {
    fun getSounds(): List<Sound> {
        val sounds = mutableListOf<Sound>()
        sounds.addAll(SoundsEnum.values().map { it.getSound() })
        return sounds
    }
}

