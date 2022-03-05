package white.noise.sounds.baby.sleep.ui.sounds

import white.noise.sounds.baby.sleep.model.Sound

sealed class SoundsEvent {
    class OnSoundClick(val sound: Sound) : SoundsEvent()
    class OnSeekBarChanged(val sound: Sound) : AdditionalSoundsEvent()

    sealed class AdditionalSoundsEvent : SoundsEvent() {
        class OnRemoveClick(val sound: Sound, val position: Int) : AdditionalSoundsEvent()
    }
    sealed class PlayerEvent : SoundsEvent() {
        object OnButtonClick : AdditionalSoundsEvent()
        object OnTimerClick : AdditionalSoundsEvent()
    }
}
