package calm.sleep.relaxing.sounds.noise.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import calm.sleep.relaxing.sounds.noise.data.AlarmRepository
import calm.sleep.relaxing.sounds.noise.data.Repository
import calm.sleep.relaxing.sounds.noise.data.provider.MixesProvider
import calm.sleep.relaxing.sounds.noise.data.provider.SoundsProvider
import calm.sleep.relaxing.sounds.noise.utils.AssetManager

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
    factory<SoundsProvider> { SoundsProvider() }
    factory<MixesProvider> { MixesProvider(context = androidContext()) }
    factory<AssetManager> { AssetManager(context = androidContext()) }
}