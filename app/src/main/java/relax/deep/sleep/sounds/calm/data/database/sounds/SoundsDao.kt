package relax.deep.sleep.sounds.calm.data.database.sounds

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import relax.deep.sleep.sounds.calm.data.database.entity.SoundEntity
import relax.deep.sleep.sounds.calm.model.SoundCategory

@Dao
interface SoundsDao {
    @Query("SELECT * FROM sounds")
    fun getAll(): List<SoundEntity>

    @Query("SELECT * FROM sounds")
    fun getAllLD(): LiveData<List<SoundEntity>>

    @Query("SELECT * FROM sounds WHERE category LIKE :soundCategory")
    fun getByCategory(soundCategory: SoundCategory): LiveData<List<SoundEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sounds: List<SoundEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(soundEntity: SoundEntity)

    @Query("DELETE FROM sounds")
    fun deleteAll()
}