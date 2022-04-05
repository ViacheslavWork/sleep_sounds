package calm.sleep.relaxing.sounds.noise.ui.mixes

import calm.sleep.relaxing.sounds.noise.model.Mix
import calm.sleep.relaxing.sounds.noise.model.MixCategory

sealed class MixesEvent {
    class OnMixClick(val mix: Mix): MixesEvent()
    class OnDeleteMixClick(val mix: Mix): MixesEvent()
    class OnPremiumMixClick(val mix: Mix): MixesEvent()
    class OnMixSave(val mix: Mix): MixesEvent()
    class OnCategoryClick(val mixCategory: MixCategory, val position: Int): MixesEvent()
}