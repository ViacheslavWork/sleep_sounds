package white.noise.sounds.baby.sleep.data.provider

import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.MixCategory
import white.noise.sounds.baby.sleep.utils.AssetManager
import java.util.*

private const val TAG = "MixesProvider"

class MixesProvider(val assetManager: AssetManager, val soundsProvider: SoundsProvider) {
    fun getMixes(): List<Mix> {
        val mixes = mutableListOf<Mix>()
        val fileNames = assetManager.getFilenamesWithFolder("mixes").toMutableMap()

        getFolders().forEach { fileNames.remove(it) }//remove folders from files

        fileNames.forEach { (key, value) ->
            val mix = Mix(
                title = getTitle(key),
                picturePath = key,
                category = getCategory(value)
            )
            mixes.add(mix)
        }
        addSoundsToMixes(mixes)
        return mixes.toList()
    }

    private fun addSoundsToMixes(mixes: List<Mix>) {
        //TODO
        mixes.forEach {
//            it.sounds.add(soundsProvider.getSounds()[0])
//            it.sounds.add(soundsProvider.getSounds()[1])
        }
    }

    private fun getFolders(): List<String> {
        return listOf("rain", "relax", "sleep", "others")
    }

    private fun getCategory(folder: String): MixCategory? {
        val folders = folder.split("/")
        val lastFolder = folders[folders.size - 1]
        when (lastFolder) {
            "rain" -> return MixCategory.Rain
            "relax" -> return MixCategory.Relax
            "sleep" -> return MixCategory.Sleep
            "others" -> return MixCategory.Others
        }
        return null
    }

    private fun getTitle(fileName: String): String {
        return fileName.removeSuffix(".png")
            .replace("-", " ")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}