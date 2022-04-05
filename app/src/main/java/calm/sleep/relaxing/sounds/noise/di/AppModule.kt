package calm.sleep.relaxing.sounds.noise.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.advertising.MyInterstitialAd
import calm.sleep.relaxing.sounds.noise.ui.mix_sounds.MixesSoundsViewModel
import calm.sleep.relaxing.sounds.noise.ui.mixes.MixesViewModel
import calm.sleep.relaxing.sounds.noise.ui.player.PlayerViewModel
import calm.sleep.relaxing.sounds.noise.ui.settings.SettingsViewModel
import calm.sleep.relaxing.sounds.noise.ui.sounds.SoundsViewModel
import calm.sleep.relaxing.sounds.noise.ui.timer.TimerViewModel
import calm.sleep.relaxing.sounds.noise.utils.EveryDayAlarmManager

val appModule = module {
    viewModel { SoundsViewModel(repository = get()) }
    viewModel { MixesViewModel(repository = get()) }
    viewModel { MixesSoundsViewModel(repository = get()) }
    viewModel { PlayerViewModel(repository = get()) }
    viewModel { SettingsViewModel(alarmRepository = get()) }
    viewModel { TimerViewModel() }
    factory {
        MyInterstitialAd(
            context = androidContext(),
            androidContext().resources.getString(R.string.rewarded_id)
        )
    }
    single { EveryDayAlarmManager(context = get(), alarmRepository = get()) }
//    viewModel { GoPremiumViewModel() }
}