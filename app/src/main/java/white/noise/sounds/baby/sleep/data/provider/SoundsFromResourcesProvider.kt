package white.noise.sounds.baby.sleep.data.provider

import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.model.SoundCategory

class SoundsFromResourcesProvider {
    fun getSounds(): List<Sound> {
        val sounds = mutableListOf<Sound>()
        /**rain*/
        sounds.add(Sound(0,"Heavy rain", R.raw.heavy_rain,R.drawable.icon_heavy_rain,50,false,SoundCategory.Rain))
        sounds.add(Sound(1,"Light rain", R.raw.light_rain,R.drawable.icon_light_rain,50,false,SoundCategory.Rain))
        //TODO change icon
        sounds.add(Sound(2,"Rain in city", R.raw.rain_in_city,R.drawable.icon_light_rain,50,false,SoundCategory.Rain))
        //TODO change icon
        sounds.add(Sound(3,"Rain on car roof", R.raw.rain_on_car_roof,R.drawable.icon_light_rain,50,false,SoundCategory.Rain))
        //TODO change icon
        sounds.add(Sound(4,"Rain on city", R.raw.rain_on_city,R.drawable.icon_light_rain,50,false,SoundCategory.Rain))
        sounds.add(Sound(5,"Rain on roof", R.raw.rain_on_roof,R.drawable.icon_rain_on_roof,50,false,SoundCategory.Rain))
        sounds.add(Sound(6,"Rain on tent", R.raw.rain_on_tent,R.drawable.icon_rain__on_tent,50,false,SoundCategory.Rain))
        sounds.add(Sound(7,"Rain on window", R.raw.rain_on_window,R.drawable.icon_rain_on_window,50,false,SoundCategory.Rain))
        sounds.add(Sound(8,"Snow", R.raw.snow,R.drawable.icon_snow,50,false,SoundCategory.Rain))
        sounds.add(Sound(9,"Thunder", R.raw.thunder,R.drawable.icon_thunder,50,false,SoundCategory.Rain))
        /**nature*/
        sounds.add(Sound(10,"Fire", R.raw.fire,R.drawable.icon_fire,50,false,SoundCategory.Nature))
        sounds.add(Sound(11,"Forest", R.raw.forest,R.drawable.icon_forest,50,false,SoundCategory.Nature))
        sounds.add(Sound(12,"Underwater", R.raw.underwater,R.drawable.icon_underwater,50,false,SoundCategory.Nature))
        sounds.add(Sound(13,"Wind", R.raw.wind,R.drawable.icon_wind,50,false,SoundCategory.Nature))
        sounds.add(Sound(14,"Wind leaves", R.raw.wind_leaves,R.drawable.icon_wind_leaves,50,false,SoundCategory.Nature))
        /**animal*/
        sounds.add(Sound(15,"Bird", R.raw.bird,R.drawable.icon_bird,50,false,SoundCategory.Animal))
        sounds.add(Sound(16,"Cat purring", R.raw.cat_purring,R.drawable.icon_cat_purring,50,false,SoundCategory.Animal))
        sounds.add(Sound(17,"Cricket", R.raw.cricket,R.drawable.icon_cricket,50,false,SoundCategory.Animal))
        sounds.add(Sound(18,"Frog", R.raw.frog,R.drawable.icon_frog,50,false,SoundCategory.Animal))
        sounds.add(Sound(19,"Owl", R.raw.owl,R.drawable.icon_owl,50,false,SoundCategory.Animal))
        sounds.add(Sound(20,"Parrot", R.raw.parrot,R.drawable.icon_parrot,50,false,SoundCategory.Animal))
        sounds.add(Sound(21,"Whale", R.raw.whale,R.drawable.icon_whale,50,false,SoundCategory.Animal))
        sounds.add(Sound(22,"Wolf", R.raw.wolf,R.drawable.icon_wolf,50,false,SoundCategory.Animal))
        /**transport*/
        sounds.add(Sound(23,"Airplane", R.raw.airplane,R.drawable.icon_airplane,50,false,SoundCategory.Transport))
        sounds.add(Sound(24,"Car", R.raw.car,R.drawable.icon_car,50,false,SoundCategory.Transport))
        sounds.add(Sound(25,"Train", R.raw.train,R.drawable.icon_train,50,false,SoundCategory.Transport))
        /**city and instrument*/
        sounds.add(Sound(26,"Cafe", R.raw.cafe,R.drawable.icon_cafe,50,false,SoundCategory.CityAndInstrument))
        sounds.add(Sound(27,"Crowd", R.raw.crowd,R.drawable.icon_crowd,50,false,SoundCategory.CityAndInstrument))
        sounds.add(Sound(28,"Flute", R.raw.flute,R.drawable.icon_flute,50,false,SoundCategory.CityAndInstrument))
        sounds.add(Sound(29,"Harp", R.raw.harp,R.drawable.icon_harp,50,false,SoundCategory.CityAndInstrument))
        sounds.add(Sound(30,"Jazz", R.raw.jazz,R.drawable.icon_jazz,50,false,SoundCategory.CityAndInstrument))
        sounds.add(Sound(31,"Lullaby", R.raw.lullaby,R.drawable.icon_lullaby,50,false,SoundCategory.CityAndInstrument))
        sounds.add(Sound(32,"Piano", R.raw.piano,R.drawable.icon_piano,50,false,SoundCategory.CityAndInstrument))
        sounds.add(Sound(33,"Piano 2", R.raw.piano2,R.drawable.icon_piano,50,false,SoundCategory.CityAndInstrument))
        /**white noise*/
        sounds.add(Sound(34,"Brown noise", R.raw.brownnoise,R.drawable.icon_brown_noise,50,false,SoundCategory.WhiteNoise))
        sounds.add(Sound(35,"Pink noise", R.raw.pinknoise,R.drawable.icon_pink_noise,50,false,SoundCategory.CityAndInstrument))
        sounds.add(Sound(36,"White noise", R.raw.white_noise,R.drawable.icon_white_noise,50,false,SoundCategory.CityAndInstrument))
        /**white noise*/
        sounds.add(Sound(38,"Concentration", R.raw.concentration,R.drawable.icon_concentration,50,false,SoundCategory.Meditation))
        sounds.add(Sound(39,"Dream", R.raw.dream,R.drawable.icon_dream,50,false,SoundCategory.Meditation))
        sounds.add(Sound(37,"Meditation", R.raw.meditation,R.drawable.icon_meditation,50,false,SoundCategory.Meditation))
        sounds.add(Sound(40,"Universe", R.raw.universe,R.drawable.icon_universe,50,false,SoundCategory.Meditation))
        sounds.add(Sound(41,"Zen", R.raw.zen,R.drawable.icon_zen,50,false,SoundCategory.Meditation))
        
        return sounds
    }

}