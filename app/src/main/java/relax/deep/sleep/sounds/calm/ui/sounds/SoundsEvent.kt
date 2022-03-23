package relax.deep.sleep.sounds.calm.ui.sounds

import relax.deep.sleep.sounds.calm.model.Sound

sealed class SoundsEvent {
    class OnSoundClick(val sound: Sound, val soundsHolder: SoundsHolder) : SoundsEvent()
    class OnSeekBarChanged(val sound: Sound) : AdditionalSoundsEvent()

    sealed class AdditionalSoundsEvent : SoundsEvent() {
        class OnRemoveClick(val sound: Sound, val position: Int) : AdditionalSoundsEvent()
    }
}
