package relax.deep.sleep.sounds.calm.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import relax.deep.sleep.sounds.calm.data.Repository
import relax.deep.sleep.sounds.calm.model.Mix
import relax.deep.sleep.sounds.calm.model.Sound

private const val TAG = "PlayerViewModel"

class PlayerViewModel(private val repository: Repository) : ViewModel() {
    fun loadSounds(mixId: Long): LiveData<List<Sound>> {
        return Transformations.map(repository.getMixLD(mixId)) {
            return@map it?.sounds?.toList()
        }
    }

    fun getMix(mixId: Long): LiveData<Mix?> {
        return repository.getMixLD(mixId)
    }

  /*  private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }*/
}