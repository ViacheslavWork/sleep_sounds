package white.noise.sounds.baby.sleep.ui.sounds

import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.model.SoundCategory

data class Section(val sectionName: SoundCategory, val items: MutableList<Sound> = mutableListOf())