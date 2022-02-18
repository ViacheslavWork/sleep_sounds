package white.noise.sounds.baby.sleep.utils

import android.content.Context
import java.io.IOException

class AssetManager(private val context: Context) {
    fun getFilenames(path: String): List<String> {
        val names = mutableListOf<String>()
        listAssetFiles(path = path, names = names)
        return names.toList()
    }
    private fun listAssetFiles(path: String, names: MutableList<String>): Boolean {
        val list: Array<String>
        try {
            list = context.assets.list(path) as Array<String>
            if (list.isNotEmpty()) {
                // This is a folder
                for (file in list) {
                    if (!listAssetFiles("$path/$file", names)) return false else {
                        // This is a file
                        names.add(file)
                    }
                }
            }
        } catch (e: IOException) {
            return false
        }
        return true
    }
    fun getFilenamesWithFolder(path: String): Map<String,String> {
        val namesAndFolders = mutableMapOf<String, String>()
        listAssetFilesAndFolders(path = path, foldersAndNames = namesAndFolders)
        return namesAndFolders.toMap()
    }
    private fun listAssetFilesAndFolders(path: String, foldersAndNames: MutableMap<String,String>): Boolean {
        val list: Array<String>
        var currentFolder = ""
        try {
            list = context.assets.list(path) as Array<String>
            if (list.isNotEmpty()) {
                // This is a folder
                    currentFolder = path
                for (file in list) {
                    if (!listAssetFilesAndFolders("$path/$file", foldersAndNames)) return false else {
                        // This is a file
                        foldersAndNames[file] = currentFolder
                    }
                }
            }
        } catch (e: IOException) {
            return false
        }
        return true
    }
}