package white.noise.sounds.baby.sleep.ui.player

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.data.Repository
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.Sound

private const val TAG = "PlayerViewModel"

class PlayerViewModel(private val repository: Repository) : ViewModel() {
    fun loadSounds(mixId: Long): LiveData<List<Sound>> {
        return Transformations.map(repository.getMixLD(mixId)) {
            return@map it.sounds.toList()
        }
    }

    fun getMix(mixId: Long): LiveData<Mix> {
        return repository.getMixLD(mixId)
    }

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}