package calm.sleep.relaxing.sounds.noise.ui.sounds

import calm.sleep.relaxing.sounds.noise.model.Sound
import calm.sleep.relaxing.sounds.noise.model.SoundCategory

data class Section(
    val soundCategory: SoundCategory,
    var items: MutableList<Sound> = mutableListOf()
)