package white.noise.sounds.baby.sleep.data.provider

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.MixCategory
import white.noise.sounds.baby.sleep.model.Sound

private const val TAG = "MixesProvider"

class MixesProvider(val context: Context) {

    fun getMixes(): List<Mix> {
        val mixes = mutableListOf<Mix>()
        /**rain*/
        mixes.add(
                Mix(
                    id = 0,
                    title = "Rain and piano",
                    picturePath = resourceUri(R.drawable.rain_and_piano),
                    sounds = mutableListOf<Sound>().apply {
                        add(SoundsEnum.LightRain.getSound())
                        add(SoundsEnum.Piano.getSound())
                    },
                    category = MixCategory.Rain,
                    isPremium = true
                )
        )
        mixes.add(
                Mix(
                    id = 1,
                    title = "Summer rain",
                    picturePath = resourceUri(R.drawable.summer_rain),
                    sounds = mutableListOf<Sound>().apply {
                        add(SoundsEnum.Frog.getSound().apply { volume = 10 })
                        add(SoundsEnum.LightRain.getSound().apply { volume = 70 })
                    },
                    category = MixCategory.Rain,
                    isPremium = true
                )
        )
        mixes.add(
                Mix(id = 2, title = "Spring rain",
                        picturePath = resourceUri(R.drawable.spring_rain),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.HeavyRain.getSound())
                            add(SoundsEnum.Thunder.getSound())
                        }, category = MixCategory.Rain, isPremium = false)
        )
        mixes.add(
                Mix(id = 3, title = "Rain",
                        picturePath = resourceUri(R.drawable.rain),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.LightRain.getSound())
                        }, category = MixCategory.Rain)
        )
        mixes.add(
                Mix(id = 4, title = "Rain in the Forest",
                        picturePath = resourceUri(R.drawable.rain_in_the_forest),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Thunder.getSound())
                            add(SoundsEnum.HeavyRain.getSound())
                        }, category = MixCategory.Rain)
        )
        mixes.add(
                Mix(id = 5, title = "Rain in the tent",
                        picturePath = resourceUri(R.drawable.rain_on_the_tent),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.RainOnTent.getSound())
                        }, category = MixCategory.Rain)
        )
        mixes.add(
                Mix(id = 6, title = "Rain on car Windows",
                        picturePath = resourceUri(R.drawable.rain_on_car_windows),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Car.getSound())
                            add(SoundsEnum.RainOnCarRoof.getSound())
                        }, category = MixCategory.Rain)
        )
        mixes.add(
                Mix(id = 7, title = "Thunderstorm",
                        picturePath = resourceUri(R.drawable.thunderstorm),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Thunder.getSound())
                            add(SoundsEnum.HeavyRain.getSound())
                        }, category = MixCategory.Rain)
        )
        mixes.add(
                Mix(id = 8, title = "Sunday Rain",
                        picturePath = resourceUri(R.drawable.sunday_rain),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.LightRain.getSound())
                            add(SoundsEnum.RainOnRoof.getSound())
                        }, category = MixCategory.Rain)
        )
        mixes.add(
                Mix(id = 9, title = "Rainy Day",
                        picturePath = resourceUri(R.drawable.spring_rain),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.LightRain.getSound())
                            add(SoundsEnum.Bird.getSound().apply { volume = 10 })
                            add(SoundsEnum.Thunder.getSound())
                        }, category = MixCategory.Rain)
        )
        mixes.add(
                Mix(id = 10, title = "Rain on the Leaves",
                        picturePath = resourceUri(R.drawable.rain_on_the_leaves),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.LightRain.getSound())
                        }, category = MixCategory.Rain)
        )
        /**sleep*/
        mixes.add(
                Mix(id = 11, title = "Beach in the Evening",
                        picturePath = resourceUri(R.drawable.beach_in_the_evening),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Zen.getSound())
                            //TODO add beach
                        }, category = MixCategory.Sleep, isPremium = false)
        )
        mixes.add(
                Mix(id = 12, title = "Fire",
                        picturePath = resourceUri(R.drawable.fire),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Fire.getSound())
                        }, category = MixCategory.Sleep, isPremium = false)
        )
        mixes.add(
                Mix(id = 13, title = "Train Journey",
                        picturePath = resourceUri(R.drawable.train_jorney),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Train.getSound())
                            add(SoundsEnum.RainOnRoof.getSound().apply { volume = 5 })
                        }, category = MixCategory.Sleep, isPremium = false)
        )
        mixes.add(
                Mix(id = 14, title = "Outside the City",
                        picturePath = resourceUri(R.drawable.outside_the_city),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Fire.getSound())
                            //TODO add night
                        }, category = MixCategory.Sleep)
        )
        mixes.add(
                Mix(id = 15, title = "In the Airplane",
                        picturePath = resourceUri(R.drawable.in_the_airplane),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Airplane.getSound())
                        }, category = MixCategory.Sleep)
        )
        mixes.add(
                Mix(id = 16, title = "City Life",
                        picturePath = resourceUri(R.drawable.city_life),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Car.getSound())
                            add(SoundsEnum.RainOnWindow.getSound().apply { volume = 10 })
                        }, category = MixCategory.Sleep)
        )
        mixes.add(
                Mix(id = 17, title = "Cold Winter",
                        picturePath = resourceUri(R.drawable.cold_winter),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Snow.getSound())
                            add(SoundsEnum.Wind.getSound())
                            add(SoundsEnum.Fire.getSound())
                        }, category = MixCategory.Sleep)
        )
        mixes.add(
                Mix(id = 18, title = "Cave",
                        picturePath = resourceUri(R.drawable.cave),
                        sounds = mutableListOf<Sound>().apply {
                            //TODO add cave
                            add(SoundsEnum.Fire.getSound())
                            add(SoundsEnum.HeavyRain.getSound())
                            add(SoundsEnum.RainOnWindow.getSound())
                        }, category = MixCategory.Sleep)
        )
        mixes.add(
                Mix(id = 19, title = "Dryer",
                        picturePath = resourceUri(R.drawable.dryer),
                        sounds = mutableListOf<Sound>().apply {
                            //TODO add dryer
                        }, category = MixCategory.Sleep)
        )
        mixes.add(
                Mix(id = 20, title = "Music Box - Lullaby",
                        picturePath = resourceUri(R.drawable.music_box_lullaby),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Lullaby.getSound())
                        }, category = MixCategory.Sleep)
        )

        /**relax*/
        mixes.add(
                Mix(id = 21, title = "Sounds of nature",
                        picturePath = resourceUri(R.drawable.sounds_of_nature),
                        sounds = mutableListOf<Sound>().apply {
                            //TODO add melody
                            add(SoundsEnum.Forest.getSound())
                        }, category = MixCategory.Relax, isPremium = false)
        )
        mixes.add(
                Mix(id = 22, title = "Quiet night",
                        picturePath = resourceUri(R.drawable.quiet_night),
                        sounds = mutableListOf<Sound>().apply {
                            //TODO add night
                            add(SoundsEnum.WindLeaves.getSound())
                        }, category = MixCategory.Relax, isPremium = false)
        )
        mixes.add(
                Mix(id = 23, title = "Time to Relax",
                        picturePath = resourceUri(R.drawable.time_to_relax),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.LightRain.getSound())
                            add(SoundsEnum.Harp.getSound())
                        }, category = MixCategory.Relax)
        )
        mixes.add(
                Mix(id = 24, title = "Memories",
                        picturePath = resourceUri(R.drawable.memories),
                        sounds = mutableListOf<Sound>().apply {
                            //TODO add rain on leaves
                            add(SoundsEnum.Piano.getSound())
                        }, category = MixCategory.Relax)
        )
        mixes.add(Mix(id = 25, title = "Deep Relaxation", picturePath = resourceUri(R.drawable.quiet_night), sounds = mutableListOf<Sound>().apply {
            add(SoundsEnum.Piano2.getSound())
        }, category = MixCategory.Relax))
        mixes.add(
                Mix(id = 26, title = "Music for Yoga",
                        picturePath = resourceUri(R.drawable.music_for_yoga),
                        sounds = mutableListOf<Sound>().apply {
                            //add(SoundsEnum..getSound())
                            //TODO add yoga sound
                        }, category = MixCategory.Relax)
        )
        /**others*/
        mixes.add(
                Mix(id = 27, title = "Meditation",
                        picturePath = resourceUri(R.drawable.meditation),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Meditation.getSound())
                        }, category = MixCategory.Others, isPremium = false)
        )
        mixes.add(
                Mix(id = 28, title = "Meditation in the Forest",
                        picturePath = resourceUri(R.drawable.meditation_in_the_forest),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Flute.getSound())
                            //TODO add river
                        }, category = MixCategory.Others, isPremium = false)
        )
        mixes.add(
                Mix(id = 29, title = "Library",
                        picturePath = resourceUri(R.drawable.library),
                        sounds = mutableListOf<Sound>().apply {
                            //add(SoundsEnum.Astral.getSound())
                            //TODO add astral
                        }, category = MixCategory.Others, isPremium = false)
        )
        mixes.add(
                Mix(id = 30, title = "Universe",
                        picturePath = resourceUri(R.drawable.universe),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Universe.getSound())
                        }, category = MixCategory.Others)
        )
        mixes.add(
                Mix(id = 31, title = "Dryer",
                        picturePath = resourceUri(R.drawable.dryer),
                        sounds = mutableListOf<Sound>().apply {
                            //TODO add dryer
                        }, category = MixCategory.Others)
        )
        mixes.add(
                Mix(id = 32, title = "Concentration",
                        picturePath = resourceUri(R.drawable.concentration),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Concentration.getSound())
                        }, category = MixCategory.Others)
        )
        mixes.add(
                Mix(id = 33, title = "Autumn in the Jungle",
                        picturePath = resourceUri(R.drawable.autumn_in_the_jungle),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.WindLeaves.getSound())
                            add(SoundsEnum.Bird.getSound())
                        }, category = MixCategory.Others)
        )
        mixes.add(
                Mix(id = 34, title = "Cafe",
                        picturePath = resourceUri(R.drawable.cafe),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Cafe.getSound().apply { volume = 24 })
                            add(SoundsEnum.Crowd.getSound().apply { volume = 86 })
                        }, category = MixCategory.Others)
        )
        mixes.add(
                Mix(id = 35, title = "Zen",
                        picturePath = resourceUri(R.drawable.universe),
                        sounds = mutableListOf<Sound>().apply {
                            add(SoundsEnum.Zen.getSound())
                        }, category = MixCategory.Others)
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