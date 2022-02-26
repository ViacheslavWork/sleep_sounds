package white.noise.sounds.baby.sleep.ui.sounds

import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.model.SoundCategory

data class Section(val soundCategory: SoundCategory, var items: MutableList<Sound> = mutableListOf())