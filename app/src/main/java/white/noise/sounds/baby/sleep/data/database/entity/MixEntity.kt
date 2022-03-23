package white.noise.sounds.baby.sleep.data.database.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.MixCategory

@Entity(tableName = "mixes")
data class MixEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    @TypeConverters(SoundsEntitiesListConverter::class)
    val soundsEntities: List<SoundEntity>,
    @TypeConverters(UriConverters::class)
    var picturePath: Uri,
    val category: MixCategory?,
    val isPremium: Boolean = false,
    val isCustom: Boolean = false
) {
    companion object {
        fun fromMix(mix: Mix): MixEntity {
            return MixEntity(
                id = mix.id,
                title = mix.title,
                soundsEntities = mix.sounds.map { SoundEntity.fromSound(it) },
                picturePath = mix.picturePath,
                category = mix.category,
                isPremium = mix.isPremium,
                isCustom = mix.isCustom
            )
        }
    }
}

fun MixEntity.toMix(): Mix {
    return Mix(
        id = id,
        title = title,
        sounds = soundsEntities.map { it.toSound() }.toMutableList(),
        picturePath = picturePath,
        category = category,
        isPremium = isPremium,
        isCustom = isCustom
    )
}

class SoundsEntitiesListConverter {
    @TypeConverter
    fun listToJson(value: List<SoundEntity>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<SoundEntity>::class.java).toList()
}

class UriConverters {
    @TypeConverter
    fun fromString(value: String?): Uri? {
        return if (value == null) null else Uri.parse(value)
    }

    @TypeConverter
    fun toString(uri: Uri?): String? {
        return uri?.toString()
    }
}
