package relax.deep.sleep.sounds.calm.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import relax.deep.sleep.sounds.calm.data.AlarmRepository
import relax.deep.sleep.sounds.calm.data.Repository
import relax.deep.sleep.sounds.calm.data.provider.MixesProvider
import relax.deep.sleep.sounds.calm.data.provider.SoundsProvider
import relax.deep.sleep.sounds.calm.utils.AssetManager

val dataModule = module {
    single<Repository> {
        Repository(
                soundsProvider = get(),
                mixProvider = get(),
                soundsDao = get(),
                mixesDao = get()
        )
    }
    single<AlarmRepository> { AlarmRepository(alarmsDao = get()) }
    factory<SoundsProvider> { SoundsProvider(assetManager = get()) }
    factory<MixesProvider> { MixesProvider(context = androidContext()) }
    factory<AssetManager> { AssetManager(context = androidContext()) }
}