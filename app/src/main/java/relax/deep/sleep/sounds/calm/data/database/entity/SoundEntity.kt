package relax.deep.sleep.sounds.calm.data.database.entity

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import relax.deep.sleep.sounds.calm.model.Sound
import relax.deep.sleep.sounds.calm.model.SoundCategory

@Entity(tableName = "sounds")
class SoundEntity(
        @PrimaryKey
        var id: Long = 0,
        val title: String,
        @RawRes
        val file: Int,
        @DrawableRes
        val icon: Int,
        var volume: Int,
        var isPlaying: Boolean = false,
        val category: SoundCategory,
        val isPremium: Boolean
) {
    companion object {
        fun fromSound(sound: Sound): SoundEntity {
            return SoundEntity(
                    id = sound.id,
                    title = sound.title,
                    file = sound.file,
                    icon = sound.icon,
                    volume = sound.volume,
                    isPlaying = sound.isPlaying,
                    category = sound.category,
                    isPremium = sound.isPremium
            )
        }
    }

}

fun SoundEntity.toSound(): Sound {
    return Sound(
            id = id,
            title = title,
            file = file,
            icon = icon,
            volume = volume,
            isPlaying = isPlaying,
            category = category,
            isPremium = isPremium
    )
}