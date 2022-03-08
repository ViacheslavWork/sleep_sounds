package white.noise.sounds.baby.sleep.data.database.mixes

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import white.noise.sounds.baby.sleep.data.database.entity.MixEntity
import white.noise.sounds.baby.sleep.model.MixCategory

@Dao
interface MixesDao {
    @Query("SELECT * FROM mixes")
    fun getAll(): List<MixEntity>

    @Query("SELECT * FROM mixes")
    fun getAllLD(): LiveData<List<MixEntity>>

    @Query("SELECT * FROM mixes WHERE id=:id")
    fun getMix(id: Long): MixEntity

    @Query("SELECT * FROM mixes WHERE id=:id")
    fun getMixLD(id: Long): LiveData<MixEntity>

    @Query("SELECT * FROM mixes WHERE category LIKE :mixCategory")
    fun getByCategory(mixCategory: MixCategory): LiveData<List<MixEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(mixes: List<MixEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mixEntity: MixEntity)

    @Query("DELETE FROM mixes")
    fun deleteAll()
}