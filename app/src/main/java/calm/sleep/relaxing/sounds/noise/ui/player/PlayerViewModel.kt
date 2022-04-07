package calm.sleep.relaxing.sounds.noise.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import calm.sleep.relaxing.sounds.noise.data.Repository
import calm.sleep.relaxing.sounds.noise.model.Mix
import calm.sleep.relaxing.sounds.noise.model.Sound

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
}