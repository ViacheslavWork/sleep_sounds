package relax.deep.sleep.sounds.calm.data.provider

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import relax.deep.sleep.sounds.calm.R
import relax.deep.sleep.sounds.calm.model.Mix
import relax.deep.sleep.sounds.calm.model.MixCategory
import relax.deep.sleep.sounds.calm.model.Sound

private const val TAG = "MixesProvider"

class MixesProvider(val context: Context) {

    fun getMixes(): List<Mix> {
        val mixes = mutableListOf<Mix>()
        /**rain*/
        mixes.add(
            Mix(
                id = 36,
                title = "Rain and piano",
                picturePath = resourceUri(R.drawable.rain_and_piano),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.LightRain.getSound().apply { isPlaying = true })
                    add(SoundsEnum.Piano.getSound().apply { isPlaying = true })
                },
                category = MixCategory.Rain,
            )
        )
        mixes.add(
            Mix(
                id = 1,
                title = "Summer rain",
                picturePath = resourceUri(R.drawable.summer_rain),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Frog.getSound().apply { isPlaying = true }.apply { volume = 10 })
                    add(SoundsEnum.LightRain.getSound().apply { isPlaying = true }
                        .apply { volume = 70 })
                },
                category = MixCategory.Rain,
                isPremium = false
            )
        )
        mixes.add(
            Mix(id = 2, title = "Spring rain",
                picturePath = resourceUri(R.drawable.spring_rain),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.HeavyRain.getSound().apply { isPlaying = true })
                    add(SoundsEnum.Thunder.getSound().apply { isPlaying = true })
                }, category = MixCategory.Rain, isPremium = false
            )
        )
        mixes.add(
            Mix(id = 3, title = "Rain",
                picturePath = resourceUri(R.drawable.rain),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.LightRain.getSound().apply { isPlaying = true })
                }, category = MixCategory.Rain, isPremium = false
            )
        )
        mixes.add(
            Mix(id = 4, title = "Rain in the Forest",
                picturePath = resourceUri(R.drawable.rain_in_the_forest),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Thunder.getSound().apply { isPlaying = true })
                    add(SoundsEnum.HeavyRain.getSound().apply { isPlaying = true })
                }, category = MixCategory.Rain
            )
        )
        mixes.add(
            Mix(id = 5, title = "Rain in the tent",
                picturePath = resourceUri(R.drawable.rain_on_the_tent),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.RainOnTent.getSound().apply { isPlaying = true })
                }, category = MixCategory.Rain
            )
        )
        mixes.add(
            Mix(id = 6, title = "Rain on car Windows",
                picturePath = resourceUri(R.drawable.rain_on_car_windows),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Car.getSound().apply { isPlaying = true })
                    add(SoundsEnum.RainOnCarRoof.getSound().apply { isPlaying = true })
                }, category = MixCategory.Rain
            )
        )
        mixes.add(
            Mix(id = 7, title = "Thunderstorm",
                picturePath = resourceUri(R.drawable.thunderstorm),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Thunder.getSound().apply { isPlaying = true })
                    add(SoundsEnum.HeavyRain.getSound().apply { isPlaying = true })
                }, category = MixCategory.Rain
            )
        )
        mixes.add(
            Mix(id = 8, title = "Sunday Rain",
                picturePath = resourceUri(R.drawable.sunday_rain),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.LightRain.getSound().apply { isPlaying = true })
                    add(SoundsEnum.RainOnRoof.getSound().apply { isPlaying = true })
                }, category = MixCategory.Rain
            )
        )
        mixes.add(
            Mix(id = 9, title = "Rainy Day",
                picturePath = resourceUri(R.drawable.spring_rain),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.LightRain.getSound().apply { isPlaying = true })
                    add(SoundsEnum.Bird.getSound().apply { isPlaying = true }.apply { volume = 10 })
                    add(SoundsEnum.Thunder.getSound().apply { isPlaying = true })
                }, category = MixCategory.Rain
            )
        )
        mixes.add(
            Mix(id = 10, title = "Rain on the Leaves",
                picturePath = resourceUri(R.drawable.rain_on_the_leaves),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.LightRain.getSound().apply { isPlaying = true })
                }, category = MixCategory.Rain
            )
        )
        /**sleep*/
        mixes.add(
            Mix(id = 11, title = "Beach in the Evening",
                picturePath = resourceUri(R.drawable.beach_in_the_evening),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Zen.getSound().apply { isPlaying = true })
                    add(SoundsEnum.BeachNight.getSound().apply { isPlaying = true })
                }, category = MixCategory.Sleep, isPremium = false
            )
        )
        mixes.add(
            Mix(id = 12, title = "Fire",
                picturePath = resourceUri(R.drawable.fire),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Fire.getSound().apply { isPlaying = true })
                }, category = MixCategory.Sleep, isPremium = false
            )
        )
        mixes.add(
            Mix(id = 13, title = "Train Journey",
                picturePath = resourceUri(R.drawable.train_jorney),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Train.getSound().apply { isPlaying = true })
                    Log.i(TAG, "getMixes: Train: ${SoundsEnum.Train.getSound().file}")
                    add(SoundsEnum.RainOnRoof.getSound().apply { isPlaying = true }
                        .apply { volume = 5 })
                }, category = MixCategory.Sleep, isPremium = false
            )
        )
        mixes.add(
            Mix(id = 14, title = "Outside the City",
                picturePath = resourceUri(R.drawable.outside_the_city),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Fire.getSound().apply { isPlaying = true })
                    add(SoundsEnum.Night.getSound().apply { isPlaying = true })
                }, category = MixCategory.Sleep
            )
        )
        mixes.add(
            Mix(id = 15, title = "In the Airplane",
                picturePath = resourceUri(R.drawable.in_the_airplane),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Airplane.getSound().apply { isPlaying = true })
                }, category = MixCategory.Sleep
            )
        )
        mixes.add(
            Mix(id = 16, title = "City Life",
                picturePath = resourceUri(R.drawable.city_life),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Car.getSound().apply { isPlaying = true })
                    add(SoundsEnum.RainOnWindow.getSound().apply { isPlaying = true }
                        .apply { volume = 10 })
                }, category = MixCategory.Sleep
            )
        )
        mixes.add(
            Mix(id = 17, title = "Cold Winter",
                picturePath = resourceUri(R.drawable.cold_winter),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Snow.getSound().apply { isPlaying = true })
                    add(SoundsEnum.Wind.getSound().apply { isPlaying = true })
                    add(SoundsEnum.Fire.getSound().apply { isPlaying = true })
                }, category = MixCategory.Sleep
            )
        )
        mixes.add(
            Mix(id = 18, title = "Cave",
                picturePath = resourceUri(R.drawable.cave),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Fire.getSound().apply { isPlaying = true })
                    add(SoundsEnum.Cave.getSound().apply { isPlaying = true })
                    add(SoundsEnum.HeavyRain.getSound().apply { isPlaying = true })
                    add(SoundsEnum.RainOnWindow.getSound().apply { isPlaying = true })
                }, category = MixCategory.Sleep
            )
        )
        mixes.add(
            Mix(id = 19, title = "Dryer",
                picturePath = resourceUri(R.drawable.dryer),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Dryer.getSound().apply { isPlaying = true })
                }, category = MixCategory.Sleep
            )
        )
        mixes.add(
            Mix(id = 20, title = "Music Box - Lullaby",
                picturePath = resourceUri(R.drawable.music_box_lullaby),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Lullaby.getSound().apply { isPlaying = true })
                }, category = MixCategory.Sleep
            )
        )

        /**relax*/
        mixes.add(
            Mix(id = 21, title = "Sounds of nature",
                picturePath = resourceUri(R.drawable.sounds_of_nature),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Forest.getSound().apply { isPlaying = true })
                    add(SoundsEnum.Melody.getSound().apply { isPlaying = true })
                }, category = MixCategory.Relax, isPremium = false
            )
        )
        mixes.add(
            Mix(id = 22, title = "Quiet night",
                picturePath = resourceUri(R.drawable.quiet_night),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Night.getSound().apply { isPlaying = true })
                    add(SoundsEnum.WindLeaves.getSound().apply { isPlaying = true })
                }, category = MixCategory.Relax, isPremium = false
            )
        )
        mixes.add(
            Mix(id = 23, title = "Time to Relax",
                picturePath = resourceUri(R.drawable.time_to_relax),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.LightRain.getSound().apply { isPlaying = true })
                    add(SoundsEnum.Harp.getSound().apply { isPlaying = true })
                }, category = MixCategory.Relax
            )
        )
        mixes.add(
            Mix(id = 24, title = "Memories",
                picturePath = resourceUri(R.drawable.memories),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Piano.getSound().apply { isPlaying = true })
                    add(SoundsEnum.LightRain.getSound().apply { isPlaying = true })
                }, category = MixCategory.Relax
            )
        )
        mixes.add(
            Mix(
                id = 25,
                title = "Deep Relaxation",
                picturePath = resourceUri(R.drawable.quiet_night),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Piano2.getSound().apply { isPlaying = true })
                },
                category = MixCategory.Relax
            )
        )
        mixes.add(
            Mix(id = 26, title = "Music for Yoga",
                picturePath = resourceUri(R.drawable.music_for_yoga),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Yoga.getSound().apply { isPlaying = true })
                }, category = MixCategory.Relax
            )
        )
        /**others*/
        mixes.add(
            Mix(id = 27, title = "Meditation",
                picturePath = resourceUri(R.drawable.meditation),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Meditation.getSound().apply { isPlaying = true })
                }, category = MixCategory.Others, isPremium = false
            )
        )
        mixes.add(
            Mix(id = 28, title = "Meditation in the Forest",
                picturePath = resourceUri(R.drawable.meditation_in_the_forest),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Flute.getSound().apply { isPlaying = true })
                    add(SoundsEnum.River.getSound().apply { isPlaying = true })
                }, category = MixCategory.Others, isPremium = false
            )
        )
        mixes.add(
            Mix(id = 29, title = "Library",
                picturePath = resourceUri(R.drawable.library),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Concentration.getSound().apply { isPlaying = true })
                }, category = MixCategory.Others, isPremium = false
            )
        )
        mixes.add(
            Mix(id = 30, title = "Universe",
                picturePath = resourceUri(R.drawable.universe),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Universe.getSound().apply { isPlaying = true })
                }, category = MixCategory.Others
            )
        )
        mixes.add(
            Mix(id = 31, title = "Dryer",
                picturePath = resourceUri(R.drawable.dryer),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Dryer.getSound().apply { isPlaying = true }
                        .apply { isPlaying = true })
                }, category = MixCategory.Others
            )
        )
        mixes.add(
            Mix(id = 32, title = "Concentration",
                picturePath = resourceUri(R.drawable.concentration),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Concentration.getSound().apply { isPlaying = true })
                }, category = MixCategory.Others
            )
        )
        mixes.add(
            Mix(id = 33, title = "Autumn in the Jungle",
                picturePath = resourceUri(R.drawable.autumn_in_the_jungle),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.WindLeaves.getSound().apply { isPlaying = true })
                    add(SoundsEnum.Bird.getSound().apply { isPlaying = true })
                }, category = MixCategory.Others
            )
        )
        mixes.add(
            Mix(id = 34, title = "Cafe",
                picturePath = resourceUri(R.drawable.cafe),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Cafe.getSound().apply { isPlaying = true }.apply { volume = 24 })
                    add(SoundsEnum.Crowd.getSound().apply { isPlaying = true }
                        .apply { volume = 86 })
                }, category = MixCategory.Others
            )
        )
        mixes.add(
            Mix(id = 35, title = "Zen",
                picturePath = resourceUri(R.drawable.universe),
                sounds = mutableListOf<Sound>().apply {
                    add(SoundsEnum.Zen.getSound().apply { isPlaying = true })
                }, category = MixCategory.Others
            )
        )
        return mixes
    }

    private fun resourceUri(resourceId: Int): Uri = with(context.resources) {
        Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(getResourcePackageName(resourceId))
            .appendPath(getResourceTypeName(resourceId))
            .appendPath(getResourceEntryName(resourceId))
            .build()
    }

}