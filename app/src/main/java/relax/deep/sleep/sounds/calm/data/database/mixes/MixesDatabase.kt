package relax.deep.sleep.sounds.calm.data.database.mixes

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import relax.deep.sleep.sounds.calm.data.database.entity.MixEntity
import relax.deep.sleep.sounds.calm.data.database.entity.SoundsEntitiesListConverter
import relax.deep.sleep.sounds.calm.data.database.entity.UriConverters

@Database(
    entities = [MixEntity::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(SoundsEntitiesListConverter::class, UriConverters::class)
abstract class MixesDatabase : RoomDatabase() {
    abstract val mixesDao: MixesDao
}