package calm.sleep.relaxing.sounds.noise.di

import org.koin.dsl.module
import calm.sleep.relaxing.sounds.noise.service.PlayerService

val serviceModule = module {
//    single<Repository> { Repository(soundsProvider = get(), mixProvider = get()) }
//    factory<SoundsProvider> { SoundsProvider(assetManager = get()) }
//    factory<MixesProvider> { MixesProvider(assetManager = get(), soundsProvider = get()) }
//    factory<AssetManager> { AssetManager(context = androidContext()) }
    single<PlayerService> { PlayerService() }
}