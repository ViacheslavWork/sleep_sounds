package calm.sleep.relaxing.sounds.noise.ui.onboarding.onboarding_7

sealed class OnBoarding7Event {
    class OnClick(val item: OnBoarding7Item) : OnBoarding7Event()
}
