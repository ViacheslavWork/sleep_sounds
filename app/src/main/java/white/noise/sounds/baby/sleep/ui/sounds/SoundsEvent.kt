package white.noise.sounds.baby.sleep.ui.sounds

import white.noise.sounds.baby.sleep.model.Sound

sealed class SoundsEvent {
    class OnSoundClick(val sound: Sound) : SoundsEvent()

    sealed class AdditionalSoundsEvent : SoundsEvent() {
        class OnRemoveClick(val sound: Sound, val position: Int) : AdditionalSoundsEvent()
        class OnSeekBarChanged(val progress: Int) : AdditionalSoundsEvent()
    }
    sealed class PlayerEvent : SoundsEvent() {
        object OnButtonClick : AdditionalSoundsEvent()
        object OnTimerClick : AdditionalSoundsEvent()
    }
}
