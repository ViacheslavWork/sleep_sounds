package white.noise.sounds.baby.sleep.ui.sounds

import white.noise.sounds.baby.sleep.model.Sound

sealed class SoundsEvent {
    class OnSoundClick(val sound: Sound) : SoundsEvent()

    sealed class AdditionalSoundsEvent : SoundsEvent() {
        class OnRemoveClick(val sound: Sound) : AdditionalSoundsEvent()
        class OnSeekBarChanged(val progress: Int) : AdditionalSoundsEvent()
    }
}
