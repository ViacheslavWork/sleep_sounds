package calm.sleep.relaxing.sounds.noise.utils

import android.content.Context
import androidx.databinding.library.BuildConfig
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