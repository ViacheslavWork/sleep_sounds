package relax.deep.sleep.sounds.calm.ui.onboarding.onboarding_5

sealed class OnBoardingNeedsItemsEvent {
    class OnClick(val item: OnBoardingNeedsItem): OnBoardingNeedsItemsEvent()
}
