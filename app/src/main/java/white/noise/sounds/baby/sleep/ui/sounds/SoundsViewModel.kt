package white.noise.sounds.baby.sleep.ui.sounds

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

private const val TAG = "SoundsViewModel"

class SoundsViewModel(private val repository: Repository) : ViewModel() {
    private val _sounds = MutableLiveData<List<Section>>()
    val sounds: LiveData<List<Section>> = _sounds

    private val _selectedSounds = MutableLiveData<List<Sound>>(listOf())
    val selectedSounds: LiveData<List<Sound>> = _selectedSounds

    init {
        loadAllSounds()
    }


    private fun loadAllSounds() {
        viewModelScope.launch(Dispatchers.IO) { _sounds.postValue(repository.getSoundsInSections()) }
    }

    fun handleEvent(event: SoundsEvent) {
        when (event) {
            is SoundsEvent.OnSoundClick -> {
                if (_selectedSounds.value?.contains(event.sound) == true) {
                    removeFromSelected(event.sound)
//                    markIsPlaying(event.sound, isPlaying = false)
                } else {
                    addToSelected(event.sound)
//                    markIsPlaying(event.sound, isPlaying = true)
                }
//                markIsPlaying(event.sound, isPlaying = true)
                sounds.value?.forEach {
                    showLog(it.toString())
                }
            }

            is SoundsEvent.AdditionalSoundsEvent.OnSeekBarChanged -> {}
        }
    }

    private fun addToSelected(sound: Sound) {
        _selectedSounds.value =
            selectedSounds.value
                ?.toMutableList()
                ?.apply { add(sound) }
                ?.toSet()
                ?.toList()
                ?.take(8)
    }

    private fun removeFromSelected(sound: Sound) {
        _selectedSounds.value =
            selectedSounds.value
                ?.toMutableList()
                ?.apply { remove(sound) }
                ?.toList()
    }

    private fun markIsPlaying(sound: Sound, isPlaying: Boolean) {
        /* _sounds.value = sounds.value?.toMutableList()?.onEach {
             it.items.forEachIndexed {index, soundInCategory ->
                 if (soundInCategory == sound) {
                     it.items[index] = soundInCategory.copy(isPlaying = isPlaying)
                 }
             }
         }?.toList()*/
        var newList = sounds.value?.toMutableList()
        newList = newList?.map { it.copy() }?.toMutableList()
        newList?.forEach {
            val listSoundsInSection = it.items.map { sound -> sound.copy() }
            listSoundsInSection.forEach { soundInCategory ->
                if (soundInCategory == sound) {
//                   soundInCategory.isPlaying = isPlaying
                }
            }
            it.items = listSoundsInSection.toMutableList()
        }
        _sounds.postValue(newList!!)
    }

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}