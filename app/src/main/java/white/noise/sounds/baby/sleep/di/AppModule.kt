package white.noise.sounds.baby.sleep.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import white.noise.sounds.baby.sleep.ui.mixes.MixesViewModel
import white.noise.sounds.baby.sleep.ui.sounds.SoundsViewModel

val appModule = module {
    viewModel { SoundsViewModel(repository = get()) }
    viewModel { MixesViewModel(repository = get()) }
//    viewModel { GoPremiumViewModel() }
}