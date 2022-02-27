package white.noise.sounds.baby.sleep.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import white.noise.sounds.baby.sleep.data.Repository
import white.noise.sounds.baby.sleep.data.provider.MixesProvider
import white.noise.sounds.baby.sleep.data.provider.SoundsProvider
import white.noise.sounds.baby.sleep.utils.AssetManager

val serviceModule = module {
//    single<Repository> { Repository(soundsProvider = get(), mixProvider = get()) }
//    factory<SoundsProvider> { SoundsProvider(assetManager = get()) }
//    factory<MixesProvider> { MixesProvider(assetManager = get(), soundsProvider = get()) }
//    factory<AssetManager> { AssetManager(context = androidContext()) }
}