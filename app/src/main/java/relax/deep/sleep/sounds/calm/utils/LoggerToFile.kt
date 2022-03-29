package relax.deep.sleep.sounds.calm.utils

import android.content.Context
import relax.deep.sleep.sounds.calm.BuildConfig
import java.io.File

object LoggerToFile {
    fun logToFile(message: String, context: Context) {
        if (BuildConfig.DEBUG) {
            var file = File(context.getExternalFilesDir(null), Constants.LOG_EXTERNAL_DIRECTORY)
            file.mkdirs()
            // Create a file to save the image
            file = File(file, "log.txt")
            file.appendText(message)
        }
    }
}