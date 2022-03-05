package white.noise.sounds.baby.sleep.ui.mix_sounds

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.data.Repository
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.ui.sounds.Section
import white.noise.sounds.baby.sleep.ui.sounds.SoundsEvent

private const val TAG = "SoundsViewModel"

class AdditionalSoundsViewModel(private val repository: Repository) : ViewModel() {
    private val _sounds = MutableLiveData<List<Section>>()
    val sounds: LiveData<List<Section>> = _sounds

    private val _selectedSounds = MutableLiveData<List<Sound>>(listOf())
    val selectedSounds: LiveData<List<Sound>> = _selectedSounds

    private val _playerSounds = MutableLiveData<List<Sound>>(listOf())
    val playerSounds: LiveData<List<Sound>> = _playerSounds

    init {
        loadAllSounds()
    }

    private fun loadAllSounds() {
        viewModelScope.launch(Dispatchers.IO) { _sounds.postValue(repository.getSoundsInSections()) }
    }

    fun handleEvent(event: SoundsEvent) {
        when (event) {
            is SoundsEvent.OnSoundClick -> {
                _selectedSounds.value =
                    selectedSounds.value
                        ?.toMutableList()
                        ?.apply { add(event.sound) }
                        ?.toSet()
                        ?.toList()
                        ?.take(8)
                _playerSounds.postValue(selectedSounds.value?.take(5))
            }
            is SoundsEvent.AdditionalSoundsEvent.OnRemoveClick -> {
                _selectedSounds.value =
                    selectedSounds.value
                        ?.toMutableList()
                        ?.apply { remove(event.sound) }
                        ?.toList()
                _playerSounds.postValue(selectedSounds.value?.take(5))
            }
            is SoundsEvent.OnSeekBarChanged -> showLog(event.sound.volume.toString())
        }
    }

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}