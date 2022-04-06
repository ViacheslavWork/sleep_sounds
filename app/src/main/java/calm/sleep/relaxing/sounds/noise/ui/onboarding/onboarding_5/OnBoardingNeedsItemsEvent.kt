package calm.sleep.relaxing.sounds.noise.ui.onboarding.onboarding_5

sealed class OnBoardingNeedsItemsEvent {
    class OnClick(val item: OnBoardingNeedsItem): OnBoardingNeedsItemsEvent()
}
