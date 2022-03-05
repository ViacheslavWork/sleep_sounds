package white.noise.sounds.baby.sleep.data.provider

import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.model.SoundCategory
import white.noise.sounds.baby.sleep.utils.AssetManager
import java.util.*

class SoundsProvider(val assetManager: AssetManager) {
    fun getSounds(): List<Sound> {
        val sounds = mutableListOf<Sound>()
        val fileNames = assetManager.getFilenamesWithFolder("sounds").toMutableMap()

        getFolders().forEach { fileNames.remove(it) }//remove folders from files

        fileNames.forEach { (key, value) ->
            val sound = Sound(
                title = getSoundName(fileNameToSimpleName(key)),
                file = key,
                icon = getIcon(fileNameToSimpleName(key)) ?: "",
                volume = 50,
                category = getCategory(value)
            )
            sounds.add(sound)
        }
        return sounds.toList()
    }

    private fun fileNameToSimpleName(fileName: String): String {
        return fileName.removeSuffix(".ogg").replace("_", " ")
    }

    private fun getSoundName(simpleName: String): String {
        return simpleName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    private fun getFolders(): List<String> {
        return listOf(
            "rain",
            "animal",
            "city_and_instrument",
            "meditation",
            "nature",
            "transport",
            "white_noise"
        )
    }

    private fun getCategory(folder: String): SoundCategory {
        val folders = folder.split("/")
        val lastFolder = folders[folders.size - 1]
        when (lastFolder) {
            "rain" -> return SoundCategory.Rain
            "animal" -> return SoundCategory.Animal
            "city_and_instrument" -> return SoundCategory.CityAndInstrument
            "meditation" -> return SoundCategory.Meditation
            "nature" -> return SoundCategory.Nature
            "transport" -> return SoundCategory.Transport
            "white_noise" -> return SoundCategory.WhiteNoise
        }
        return SoundCategory.Other
    }

    private fun getIcon(simpleName: String): String? {
        assetManager.getFilenames("icons").forEach {
            val iconName = it.removePrefix("icon-")
                .removeSuffix(".svg")
                .replace("-", " ")
            if (iconName == simpleName) return it
        }
        return null
    }


}