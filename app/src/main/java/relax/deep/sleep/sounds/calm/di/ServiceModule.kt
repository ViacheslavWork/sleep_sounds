package relax.deep.sleep.sounds.calm.di

import org.koin.dsl.module
import relax.deep.sleep.sounds.calm.service.PlayerService

val serviceModule = module {
//    single<Repository> { Repository(soundsProvider = get(), mixProvider = get()) }
//    factory<SoundsProvider> { SoundsProvider(assetManager = get()) }
//    factory<MixesProvider> { MixesProvider(assetManager = get(), soundsProvider = get()) }
//    factory<AssetManager> { AssetManager(context = androidContext()) }
    single<PlayerService> { PlayerService() }
}