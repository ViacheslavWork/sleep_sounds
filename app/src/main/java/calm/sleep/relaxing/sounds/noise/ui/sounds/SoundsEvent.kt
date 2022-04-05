package calm.sleep.relaxing.sounds.noise.ui.sounds

import calm.sleep.relaxing.sounds.noise.model.Sound

sealed class SoundsEvent(val sound: Sound) {
    class OnSoundClick(sound: Sound, val soundsHolder: SoundsHolder) : SoundsEvent(sound)
    class OnAdditionalSoundClick(sound: Sound) : SoundsEvent(sound)
    class OnRemoveClick(sound: Sound) : SoundsEvent(sound)
    class OnSeekBarChanged(sound: Sound) : SoundsEvent(sound)

    /*sealed class AdditionalSoundsEvent : SoundsEvent(sound) {
        class OnRemoveClick(sound: Sound, val position: Int) : AdditionalSoundsEvent()
    }*/
}
