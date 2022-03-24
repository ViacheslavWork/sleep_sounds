package relax.deep.sleep.sounds.calm.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import relax.deep.sleep.sounds.calm.R
import relax.deep.sleep.sounds.calm.advertising.MyInterstitialAd
import relax.deep.sleep.sounds.calm.ui.mix_sounds.MixesSoundsViewModel
import relax.deep.sleep.sounds.calm.ui.mixes.MixesViewModel
import relax.deep.sleep.sounds.calm.ui.player.PlayerViewModel
import relax.deep.sleep.sounds.calm.ui.settings.SettingsViewModel
import relax.deep.sleep.sounds.calm.ui.sounds.SoundsViewModel
import relax.deep.sleep.sounds.calm.ui.timer.TimerViewModel

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
//    viewModel { GoPremiumViewModel() }
}