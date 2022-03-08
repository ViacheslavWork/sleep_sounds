package white.noise.sounds.baby.sleep.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import white.noise.sounds.baby.sleep.ui.mix_sounds.MixesSoundsViewModel
import white.noise.sounds.baby.sleep.ui.mixes.MixesViewModel
import white.noise.sounds.baby.sleep.ui.player.PlayerViewModel
import white.noise.sounds.baby.sleep.ui.sounds.SoundsViewModel
import white.noise.sounds.baby.sleep.ui.timer.TimerViewModel

val appModule = module {
    viewModel { SoundsViewModel(repository = get()) }
    viewModel { MixesViewModel(repository = get()) }
    viewModel { MixesSoundsViewModel(repository = get()) }
    viewModel { PlayerViewModel(repository = get()) }
    viewModel { TimerViewModel() }
//    viewModel { GoPremiumViewModel() }
}