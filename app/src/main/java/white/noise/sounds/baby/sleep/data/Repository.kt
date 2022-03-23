package white.noise.sounds.baby.sleep.data

import android.util.Log
import androidx.databinding.library.BuildConfig
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import white.noise.sounds.baby.sleep.data.database.entity.MixEntity
import white.noise.sounds.baby.sleep.data.database.entity.SoundEntity
import white.noise.sounds.baby.sleep.data.database.entity.toMix
import white.noise.sounds.baby.sleep.data.database.entity.toSound
import white.noise.sounds.baby.sleep.data.database.mixes.MixesDao
import white.noise.sounds.baby.sleep.data.database.sounds.SoundsDao
import white.noise.sounds.baby.sleep.data.provider.MixesProvider
import white.noise.sounds.baby.sleep.data.provider.SoundsProvider
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.MixCategory
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.model.SoundCategory
import white.noise.sounds.baby.sleep.ui.sounds.Section

private const val TAG = "Repository"

class Repository(
        private val soundsProvider: SoundsProvider,
        private val mixProvider: MixesProvider,
        private val soundsDao: SoundsDao,
        private val mixesDao: MixesDao
) {

    fun getSounds(): List<Sound> {
        return soundsProvider.getSounds()
    }

    //TODO: remove it later
    fun getSoundsInSections(): List<Section> {
        val categoryMap: MutableMap<SoundCategory, Section> = mutableMapOf()
        enumValues<SoundCategory>().forEach {
            categoryMap[it] = Section(it)
        }
        soundsProvider.getSounds().forEach {
            categoryMap[it.category]?.items?.add(it)
        }
        return categoryMap.values.toList()
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


    fun getMixes(): LiveData<List<Mix>> {
        GlobalScope.launch(Dispatchers.IO) {
//            mixesDao.deleteAll()
            if (mixesDao.getAll().isEmpty()) {
                Log.i(TAG, "getMixes: ")
                mixesDao.insertAll(mixProvider.getMixes().map { mix -> MixEntity.fromMix(mix) })
            }
        }
        return Transformations.map(mixesDao.getAllLD()) {
            return@map it.map { mixEntity -> mixEntity.toMix() }
        }
    }

    fun getMixes(category: MixCategory): List<Mix> {
        return mixProvider.getMixes().filter { it.category == category }
    }

    suspend fun saveMix(mix: Mix) = withContext(Dispatchers.IO) {
        mixesDao.insert(mixEntity = MixEntity.fromMix(mix))
    }

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }

    suspend fun getMix(id: Long): Mix = withContext(Dispatchers.IO) {
        return@withContext mixesDao.getMix(id).toMix()
    }

    fun getMixLD(id: Long): LiveData<Mix?> {
        return Transformations.map(mixesDao.getMixLD(id)) {
            it?.toMix()
        }
    }
    suspend fun deleteMix(id: Long) = withContext(Dispatchers.IO) {
        mixesDao.delete(id)
    }

    fun getSound(soundId: Long): Sound? {
        soundsProvider.getSounds().forEach { if (it.id == soundId) return it }
        return null
    }
}