package white.noise.sounds.baby.sleep.ui.sounds

import white.noise.sounds.baby.sleep.model.Sound

sealed class SoundsEvent {
    class OnSoundClick(val sound: Sound, val soundsHolder: SoundsHolder) : SoundsEvent()
    class OnSeekBarChanged(val sound: Sound) : AdditionalSoundsEvent()

    sealed class AdditionalSoundsEvent : SoundsEvent() {
        class OnRemoveClick(val sound: Sound, val position: Int) : AdditionalSoundsEvent()
    }
}
