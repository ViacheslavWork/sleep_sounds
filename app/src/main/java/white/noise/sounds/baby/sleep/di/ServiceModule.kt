package white.noise.sounds.baby.sleep.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import white.noise.sounds.baby.sleep.service.RescheduleAlarmsService

val serviceModule = module {
//    single<Repository> { Repository(soundsProvider = get(), mixProvider = get()) }
//    factory<SoundsProvider> { SoundsProvider(assetManager = get()) }
//    factory<MixesProvider> { MixesProvider(assetManager = get(), soundsProvider = get()) }
//    factory<AssetManager> { AssetManager(context = androidContext()) }
    single<RescheduleAlarmsService> {
        RescheduleAlarmsService(
            alarmRepository = get(),
            context = androidContext()
        )
    }
}