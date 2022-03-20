package white.noise.sounds.baby.sleep.data.provider

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.model.SoundCategory

enum class SoundsEnum(
        var id: Long = 0,
        val title: String,
        @RawRes
        val file: Int,
        @DrawableRes
        val icon: Int,
        var volume: Int,
        var isPlaying: Boolean = false,
        val category: SoundCategory,
        val isPremium: Boolean = false
) {
    /**rain*/
    HeavyRain(0, "Heavy rain", R.raw.heavy_rain, R.drawable.icon_heavy_rain, 50, false, SoundCategory.Rain, true),
    LightRain(1, "Light rain", R.raw.light_rain, R.drawable.icon_light_rain, 50, false, SoundCategory.Rain, false),
    //TODO change icon
    RainInCity(2, "Rain in city", R.raw.rain_in_city, R.drawable.icon_light_rain, 50, false, SoundCategory.Rain, false),
    //TODO change icon
    RainOnCarRoof(3, "Rain on car roof", R.raw.rain_on_car_roof, R.drawable.icon_light_rain, 50, false, SoundCategory.Rain),
    //TODO change icon
    RainOnCity(4, "Rain on city", R.raw.rain_on_city, R.drawable.icon_light_rain, 50, false, SoundCategory.Rain),
    RainOnRoof(5, "Rain on roof", R.raw.rain_on_roof, R.drawable.icon_rain_on_roof, 50, false, SoundCategory.Rain),
    RainOnTent(6, "Rain on tent", R.raw.rain_on_tent, R.drawable.icon_rain__on_tent, 50, false, SoundCategory.Rain),
    RainOnWindow(7, "Rain on window", R.raw.rain_on_window, R.drawable.icon_rain_on_window, 50, false, SoundCategory.Rain),
    Snow(8, "Snow", R.raw.snow, R.drawable.icon_snow, 50, false, SoundCategory.Rain),
    Thunder(9, "Thunder", R.raw.thunder, R.drawable.icon_thunder, 50, false, SoundCategory.Rain),

    /**nature*/
    Fire(10, "Fire", R.raw.fire, R.drawable.icon_fire, 50, false, SoundCategory.Nature, false),
    Forest(11, "Forest", R.raw.forest, R.drawable.icon_forest, 50, false, SoundCategory.Nature, false),
    Underwater(12, "Underwater", R.raw.underwater, R.drawable.icon_underwater, 50, false, SoundCategory.Nature),
    Wind(13, "Wind", R.raw.wind, R.drawable.icon_wind, 50, false, SoundCategory.Nature),
    WindLeaves(14, "Wind leaves", R.raw.wind_leaves, R.drawable.icon_wind_leaves, 50, false, SoundCategory.Nature),

    /**animal*/
    Bird(15, "Bird", R.raw.bird, R.drawable.icon_bird, 50, false, SoundCategory.Animal, false),
    CatPurring(16, "Cat purring", R.raw.cat_purring, R.drawable.icon_cat_purring, 50, false, SoundCategory.Animal, false),
    Cricket(17, "Cricket", R.raw.cricket, R.drawable.icon_cricket, 50, false, SoundCategory.Animal, false),
    Frog(18, "Frog", R.raw.frog, R.drawable.icon_frog, 50, false, SoundCategory.Animal),
    Owl(19, "Owl", R.raw.owl, R.drawable.icon_owl, 50, false, SoundCategory.Animal),
    Parrot(20, "Parrot", R.raw.parrot, R.drawable.icon_parrot, 50, false, SoundCategory.Animal),
    Whale(21, "Whale", R.raw.whale, R.drawable.icon_whale, 50, false, SoundCategory.Animal),
    Wolf(22, "Wolf", R.raw.wolf, R.drawable.icon_wolf, 50, false, SoundCategory.Animal),

    /**transport*/
    Airplane(23, "Airplane", R.raw.airplane, R.drawable.icon_airplane, 50, false, SoundCategory.Transport, false),
    Car(24, "Car", R.raw.car, R.drawable.icon_car, 50, false, SoundCategory.Transport),
    Train(25, "Train", R.raw.train, R.drawable.icon_train, 50, false, SoundCategory.Transport),

    /**city and instrument*/
    Cafe(26, "Cafe", R.raw.cafe, R.drawable.icon_cafe, 50, false, SoundCategory.CityAndInstrument, false),
    Crowd(27, "Crowd", R.raw.crowd, R.drawable.icon_crowd, 50, false, SoundCategory.CityAndInstrument, false),
    Flute(28, "Flute", R.raw.flute, R.drawable.icon_flute, 50, false, SoundCategory.CityAndInstrument, false),
    Harp(29, "Harp", R.raw.harp, R.drawable.icon_harp, 50, false, SoundCategory.CityAndInstrument),
    Jazz(30, "Jazz", R.raw.jazz, R.drawable.icon_jazz, 50, false, SoundCategory.CityAndInstrument),
    Lullaby(31, "Lullaby", R.raw.lullaby, R.drawable.icon_lullaby, 50, false, SoundCategory.CityAndInstrument),
    Piano(32, "Piano", R.raw.piano, R.drawable.icon_piano, 50, false, SoundCategory.CityAndInstrument),
    Piano2(33, "Piano 2", R.raw.piano2, R.drawable.icon_piano, 50, false, SoundCategory.CityAndInstrument),

    /**white noise*/
    BrownNoise(34, "Brown noise", R.raw.brownnoise, R.drawable.icon_brown_noise, 50, false, SoundCategory.WhiteNoise, false),
    PinkNoise(35, "Pink noise", R.raw.pinknoise, R.drawable.icon_pink_noise, 50, false, SoundCategory.WhiteNoise),
    WhiteNoise(36, "White noise", R.raw.white_noise, R.drawable.icon_white_noise, 50, false, SoundCategory.WhiteNoise),

    /**meditation*/
    Concentration(38, "Concentration", R.raw.concentration, R.drawable.icon_concentration, 50, false, SoundCategory.Meditation, false),
    Dream(39, "Dream", R.raw.dream, R.drawable.icon_dream, 50, false, SoundCategory.Meditation, false),
    Meditation(37, "Meditation", R.raw.meditation, R.drawable.icon_meditation, 50, false, SoundCategory.Meditation),
    Universe(40, "Universe", R.raw.universe, R.drawable.icon_universe, 50, false, SoundCategory.Meditation),
    Zen(41, "Zen", R.raw.zen, R.drawable.icon_zen, 50, false, SoundCategory.Meditation),

    BeachNight(42, "Beach Night", R.raw.beach_night, R.drawable.night_beach, 50, false, SoundCategory.Nature),
    Cave(43, "Cave", R.raw.cave, R.drawable.cave_svgrepo_com, 50, false, SoundCategory.Nature),
    Dryer(44, "Dryer", R.raw.dryer, R.drawable.drier, 50, false, SoundCategory.CityAndInstrument),
    Tide(45, "High Tide", R.raw.high_tide, R.drawable.tide, 50, false, SoundCategory.Nature),
    Melody(46, "Melody", R.raw.melody, R.drawable.melody, 50, false, SoundCategory.CityAndInstrument),
    MusicBox(47, "Music Box", R.raw.music_box, R.drawable.music_box, 50, false, SoundCategory.CityAndInstrument),
    Night(48, "Night", R.raw.night, R.drawable.moon, 50, false, SoundCategory.Nature),
    //    River(45, "River", R.raw.river, R.drawable.ri, 50, false, SoundCategory.Nature),
    Shower(49, "Shower", R.raw.shower, R.drawable.shower_1, 50, false, SoundCategory.CityAndInstrument),
    Shower2(50, "Shower 2", R.raw.shower2, R.drawable.shower_2, 50, false, SoundCategory.CityAndInstrument),
    Waves(51, "Waves", R.raw.waves, R.drawable.waves, 50, false, SoundCategory.Nature),
    Yoga(52, "Yoga", R.raw.yoga, R.drawable.yoga, 50, false, SoundCategory.Meditation)
    ;

    fun getSound(): Sound {
        return Sound(id, title, file, icon, volume, isPlaying, category, isPremium)
    }
}