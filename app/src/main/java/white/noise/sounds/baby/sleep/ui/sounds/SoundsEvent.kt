package white.noise.sounds.baby.sleep.ui.sounds

import white.noise.sounds.baby.sleep.model.Sound

sealed class SoundsEvent {
    class OnSoundClick(sound: Sound): SoundsEvent()
}