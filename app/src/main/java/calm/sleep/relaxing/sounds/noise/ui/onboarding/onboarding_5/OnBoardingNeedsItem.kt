package calm.sleep.relaxing.sounds.noise.ui.onboarding.onboarding_5

import androidx.annotation.DrawableRes

data class OnBoardingNeedsItem(
    val title: String,
    @DrawableRes val image: Int,
    var isChecked: Boolean = false
)
