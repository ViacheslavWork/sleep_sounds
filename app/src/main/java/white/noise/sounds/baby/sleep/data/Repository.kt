package white.noise.sounds.baby.sleep.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.data.database.entity.SoundEntity
import white.noise.sounds.baby.sleep.data.database.entity.toSound
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
    private val soundsDao: SoundsDao
) {
    lateinit var sounds: List<Sound>

    fun getSounds(): LiveData<List<Sound>> {
        GlobalScope.launch {
            soundsDao.deleteAll()
            soundsDao.insertAll(
                soundsProvider.getSounds().map { sound -> SoundEntity.fromSound(sound) })
        }

        return Transformations.map(soundsDao.getAllLD()) {
            return@map it.map { soundEntity -> soundEntity.toSound() }
        }
    }

    //TODO: remove it later
    suspend fun getSoundsInSections(): List<Section> {
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


    fun getMixes(): List<Mix> {
        return mixProvider.getMixes()
    }

    fun getMixes(category: MixCategory): List<Mix> {
        return mixProvider.getMixes().filter { it.category == category }
    }

    fun saveMix(mix: Mix) {
        TODO()
    }

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}