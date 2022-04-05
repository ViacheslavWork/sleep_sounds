package calm.sleep.relaxing.sounds.noise.data.database.mixes

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import calm.sleep.relaxing.sounds.noise.data.database.entity.MixEntity
import calm.sleep.relaxing.sounds.noise.data.database.entity.SoundsEntitiesListConverter
import calm.sleep.relaxing.sounds.noise.data.database.entity.UriConverters

@Database(
    entities = [MixEntity::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(SoundsEntitiesListConverter::class, UriConverters::class)
abstract class MixesDatabase : RoomDatabase() {
    abstract val mixesDao: MixesDao
}