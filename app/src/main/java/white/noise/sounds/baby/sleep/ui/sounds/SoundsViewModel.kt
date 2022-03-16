package white.noise.sounds.baby.sleep.ui.sounds

import android.util.Log
import androidx.lifecycle.*
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.data.Repository
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.model.SoundCategory
import white.noise.sounds.baby.sleep.service.PlayerService
import white.noise.sounds.baby.sleep.utils.Constants

private const val TAG = "SoundsViewModel"

class SoundsViewModel(private val repository: Repository) : ViewModel() {
    private val _sections = MutableLiveData<List<Section>>()
    val sections: LiveData<List<Section>> = _sections

    private val _selectedSounds = MutableLiveData<List<Sound>>(listOf())
    val selectedSounds: LiveData<List<Sound>> = _selectedSounds

    private val selectedSoundsObserver: Observer<Set<Sound>?> = Observer {
        if (PlayerService.launcher == Constants.SOUNDS_LAUNCHER) {
            it?.let { _selectedSounds.postValue(it.toList()) }
            it?.forEach { showLog(it.title) }
        }
    }
    init {
        PlayerService.currentSoundsLD.observeForever(selectedSoundsObserver)
    }

    fun updateSections() {
        val sounds = repository.getSounds()
        val mapSoundCategoryToSection: MutableMap<SoundCategory, Section> = mutableMapOf()
        enumValues<SoundCategory>().forEach {
            mapSoundCategoryToSection[it] = Section(it)
        }
        sounds.forEach {
            if (PlayerService.launcher == Constants.SOUNDS_LAUNCHER
                    && PlayerService.currentSounds.containsKey(it.id)
            ) {
                mapSoundCategoryToSection[it.category]?.items?.add(PlayerService.currentSounds[it.id]!!)
            } else {
                mapSoundCategoryToSection[it.category]?.items?.add(it)
            }
        }
        _sections.postValue(mapSoundCategoryToSection.values.toList())
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
            is SoundsEvent.OnSeekBarChanged -> {}
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

    override fun onCleared() {
        showLog("onCleared")
        PlayerService.currentSoundsLD.removeObserver(selectedSoundsObserver)
        super.onCleared()
    }

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}