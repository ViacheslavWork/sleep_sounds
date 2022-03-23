package relax.deep.sleep.sounds.calm.ui.mixes

import relax.deep.sleep.sounds.calm.model.Mix
import relax.deep.sleep.sounds.calm.model.MixCategory

sealed class MixesEvent {
    class OnMixClick(val mix: Mix): MixesEvent()
    class OnDeleteMixClick(val mix: Mix): MixesEvent()
    class OnPremiumMixClick(val mix: Mix): MixesEvent()
    class OnMixSave(val mix: Mix): MixesEvent()
    class OnCategoryClick(val mixCategory: MixCategory, val position: Int): MixesEvent()
}