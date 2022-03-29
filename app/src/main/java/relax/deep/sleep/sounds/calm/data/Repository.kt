package relax.deep.sleep.sounds.calm.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.*
import relax.deep.sleep.sounds.calm.data.database.entity.MixEntity
import relax.deep.sleep.sounds.calm.data.database.entity.SoundEntity
import relax.deep.sleep.sounds.calm.data.database.entity.toMix
import relax.deep.sleep.sounds.calm.data.database.entity.toSound
import relax.deep.sleep.sounds.calm.data.database.mixes.MixesDao
import relax.deep.sleep.sounds.calm.data.database.sounds.SoundsDao
import relax.deep.sleep.sounds.calm.data.provider.MixesProvider
import relax.deep.sleep.sounds.calm.data.provider.SoundsProvider
import relax.deep.sleep.sounds.calm.model.Mix
import relax.deep.sleep.sounds.calm.model.MixCategory
import relax.deep.sleep.sounds.calm.model.Sound
import relax.deep.sleep.sounds.calm.model.SoundCategory

private const val TAG = "Repository"

class Repository(
    private val soundsProvider: SoundsProvider,
    private val mixProvider: MixesProvider,
    private val soundsDao: SoundsDao,
    private val mixesDao: MixesDao
) {
    private val ioScope = CoroutineScope(Dispatchers.IO)

    suspend fun getSounds(): List<Sound> = withContext(Dispatchers.IO) {
        if (soundsDao.getAll().isEmpty()) {
            soundsDao.insertAll(soundsProvider.getSounds().map { SoundEntity.fromSound(it) })
        }
        return@withContext soundsDao.getAll().map { it.toSound() }
    }

    suspend fun saveSound(sound: Sound) = withContext(Dispatchers.IO) {
        soundsDao.insert(SoundEntity.fromSound(sound))
    }

    suspend fun getMixes(): List<Mix> = withContext(Dispatchers.IO) {
        GlobalScope.launch(Dispatchers.IO) {
            if (mixesDao.getAll().isEmpty()) {
                mixesDao.insertAll(mixProvider.getMixes().map { mix -> MixEntity.fromMix(mix) })
            }
        }
        return@withContext mixesDao.getAll().map { it.toMix() }
    }

    suspend fun saveMix(mix: Mix) = withContext(Dispatchers.IO) {
        mixesDao.insert(mixEntity = MixEntity.fromMix(mix))
    }

    suspend fun getMix(id: Long): Mix = withContext(Dispatchers.IO) {
        return@withContext mixesDao.getMix(id).toMix()
    }

    suspend fun deleteMix(id: Long) = withContext(Dispatchers.IO) {
        mixesDao.delete(id)
    }

    fun getMixesLD(): LiveData<List<Mix>> {
        GlobalScope.launch(Dispatchers.IO) {
//            mixesDao.deleteAll()
            if (mixesDao.getAll().isEmpty()) {
                mixesDao.insertAll(mixProvider.getMixes().map { mix -> MixEntity.fromMix(mix) })
            }
        }
        return Transformations.map(mixesDao.getAllLD()) {
            return@map it.map { mixEntity -> mixEntity.toMix() }
        }
    }

    fun getMixLD(id: Long): LiveData<Mix?> {
        return Transformations.map(mixesDao.getMixLD(id)) {
            it?.toMix()
        }
    }


    //unused
    fun getMixes(category: MixCategory): List<Mix> {
        return mixProvider.getMixes().filter { it.category == category }
    }

    fun getSound(soundId: Long): Sound? {
        soundsProvider.getSounds().forEach { if (it.id == soundId) return it }
        return null
    }

    fun getSounds(category: SoundCategory): LiveData<List<Sound>> {
        return Transformations.map(soundsDao.getByCategory(category)) {
            return@map it.map { soundEntity -> soundEntity.toSound() }
        }
    }

    suspend fun insertSound(sound: Sound) = withContext(Dispatchers.IO) {
        soundsDao.insert(soundEntity = SoundEntity.fromSound(sound))
    }

    suspend fun insertSounds(sounds: List<Sound>) = withContext(Dispatchers.IO) {
        soundsDao.insertAll(sounds = sounds.map { SoundEntity.fromSound(it) })
    }
}