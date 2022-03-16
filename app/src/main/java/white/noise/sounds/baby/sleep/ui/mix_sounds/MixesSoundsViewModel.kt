package white.noise.sounds.baby.sleep.ui.mix_sounds

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.data.Repository
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.model.SoundCategory
import white.noise.sounds.baby.sleep.ui.sounds.Section
import white.noise.sounds.baby.sleep.ui.sounds.SoundsEvent

private const val TAG = "MixesSoundsViewModel"

class MixesSoundsViewModel(private val repository: Repository) : ViewModel() {
    private val _sections = MutableLiveData<List<Section>>()
    val sections: LiveData<List<Section>> = _sections

    private var _selectedSounds = MutableLiveData<List<Sound>>(listOf())
    val selectedSounds: LiveData<List<Sound>> = _selectedSounds

    /*  init {
          loadAllSounds()
      }

      private fun loadAllSounds() {
          viewModelScope.launch(Dispatchers.IO) { _sounds.postValue(repository.getSoundsInSections()) }
      }*/

    fun loadMixSounds(mixId: Long) {
        viewModelScope.launch {
            val mix = repository.getMix(mixId)
            mix.sounds.forEach { it.isPlaying = true }
            _selectedSounds.postValue(mix.sounds)

            val sounds = repository.getSounds()
            val mapSoundCategoryToSection: MutableMap<SoundCategory, Section> = mutableMapOf()
            enumValues<SoundCategory>().forEach {
                mapSoundCategoryToSection[it] = Section(it)
            }
            sounds.forEach {
                if (mix.sounds.map { sound -> sound.id }.contains(it.id)) {
                    val soundFromMix = mix.sounds.filter { mixSound -> mixSound.id == it.id }.take(1)[0]
                    mapSoundCategoryToSection[it.category]?.items?.add(soundFromMix)
                } else {
                    mapSoundCategoryToSection[it.category]?.items?.add(it)
                }
            }
            _sections.postValue(mapSoundCategoryToSection.values.toList())
        }
    }

    fun saveChangesInMix(mixId: Long) {
        viewModelScope.launch {
            val mix = repository.getMix(mixId)
            mix.sounds.clear()
            selectedSounds.value?.let { mix.sounds.addAll(selectedSounds.value!!) }
            repository.saveMix(mix)
        }
    }

    fun handleEvent(event: SoundsEvent) {
        when (event) {
            is SoundsEvent.OnSoundClick -> {
                if (event.sound.isPlaying) {
                    addToSelected(sound = event.sound)
                } else {
                    removeFromSelected(sound = event.sound)
                }
            }
            is SoundsEvent.AdditionalSoundsEvent.OnRemoveClick -> {
                removeFromSelected(event.sound)
            }
            is SoundsEvent.OnSeekBarChanged -> showLog(event.sound.volume.toString())
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


    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}