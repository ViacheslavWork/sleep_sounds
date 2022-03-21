package white.noise.sounds.baby.sleep.ui.mixes

import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.MixCategory

sealed class MixesEvent {
    class OnMixClick(val mix: Mix): MixesEvent()
    class OnDeleteMixClick(val mix: Mix): MixesEvent()
    class OnPremiumMixClick(val mix: Mix): MixesEvent()
    class OnMixSave(val mix: Mix): MixesEvent()
    class OnCategoryClick(val mixCategory: MixCategory, val position: Int): MixesEvent()
}