package white.noise.sounds.baby.sleep.data

import android.util.Log
import white.noise.sounds.baby.sleep.BuildConfig
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
    private val mixProvider: MixesProvider

) {
    suspend fun getSounds(): List<Sound> {
//        soundsProvider.getSounds().forEach { showLog(it.toString()) }
        return soundsProvider.getSounds()
    }

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

    suspend fun getSounds(category: SoundCategory): List<Sound> {
        return soundsProvider.getSounds().filter { it.category == category }
    }

    fun getMixes(): List<Mix> {
        return mixProvider.getMixes()
    }

    fun getMixes(category: MixCategory): List<Mix> {
        return mixProvider.getMixes().filter { it.category == category }
    }

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}