package relax.deep.sleep.sounds.calm.ui.sounds

import relax.deep.sleep.sounds.calm.model.Sound
import relax.deep.sleep.sounds.calm.model.SoundCategory

data class Section(val soundCategory: SoundCategory, var items: MutableList<Sound> = mutableListOf())