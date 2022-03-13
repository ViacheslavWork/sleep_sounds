package white.noise.sounds.baby.sleep.data.database.entity

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.model.SoundCategory

@Entity(tableName = "sounds")
class SoundEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val title: String,
    @RawRes
    val file: Int,
    @DrawableRes
    val icon: Int,
    var volume: Int,
    var isPlaying: Boolean = false,
    val category: SoundCategory
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
                category = sound.category
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
        category = category
    )
}