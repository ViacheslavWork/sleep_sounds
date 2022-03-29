package relax.deep.sleep.sounds.calm.ui.mix_sounds

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import relax.deep.sleep.sounds.calm.BuildConfig
import relax.deep.sleep.sounds.calm.data.Repository
import relax.deep.sleep.sounds.calm.model.Sound
import relax.deep.sleep.sounds.calm.model.SoundCategory
import relax.deep.sleep.sounds.calm.service.PlayerService
import relax.deep.sleep.sounds.calm.ui.sounds.Section
import relax.deep.sleep.sounds.calm.ui.sounds.SoundsEvent
import relax.deep.sleep.sounds.calm.utils.Constants
import kotlin.collections.set

private const val TAG = "MixesSoundsViewModel"

class MixesSoundsViewModel(private val repository: Repository) : ViewModel() {
    private val _sections = MutableLiveData<List<Section>>()
    val sections: LiveData<List<Section>> = _sections

    private var _selectedSounds = MutableLiveData<List<Sound>>(listOf())
    val selectedSounds: LiveData<List<Sound>> = _selectedSounds

    private val selectedSoundsObserver: Observer<Set<Sound>?> = Observer {
        if (PlayerService.launcher == Constants.MIX_LAUNCHER) {
            it?.let { _selectedSounds.postValue(it.toList()) }
            it?.forEach { showLog(it.toString()) }
        }
    }

    init {
        PlayerService.currentSoundsLD.observeForever(selectedSoundsObserver)
    }

    fun loadSounds() {
        viewModelScope.launch {
            val sounds = repository.getSounds().toMutableList()
            val soundsInService = PlayerService.currentSounds
            sounds.map { it.id }.forEachIndexed { index, soundId ->
                if (soundsInService.keys.contains(soundId)) {
                    sounds[index] = soundsInService[soundId]!!
                }
            }
            val mapSoundCategoryToSection: MutableMap<SoundCategory, Section> = mutableMapOf()
            enumValues<SoundCategory>().forEach {
                mapSoundCategoryToSection[it] = Section(it)
            }
            sounds.forEach {
                mapSoundCategoryToSection[it.category]?.items?.add(it)
            }
            _sections.postValue(mapSoundCategoryToSection.values.toList())
        }
    }

    fun saveChangesInMix(mixId: Long) {
        viewModelScope.launch {
            val mix = repository.getMix(mixId)
            mix.sounds.clear()
            mix.sounds.addAll(PlayerService.currentSounds.values)
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
            is SoundsEvent.OnRemoveClick -> {
                removeFromSelected(event.sound)
            }
            is SoundsEvent.OnSeekBarChanged -> showLog(event.sound.volume.toString())
        }
    }

    /* fun updateSections() {
         val sounds = repository.getSounds()
         val mapSoundCategoryToSection: MutableMap<SoundCategory, Section> = mutableMapOf()
         enumValues<SoundCategory>().forEach {
             mapSoundCategoryToSection[it] = Section(it)
         }
         sounds.forEach {
             if (PlayerService.currentSounds.containsKey(it.id)) {
                 mapSoundCategoryToSection[it.category]?.items?.add(PlayerService.currentSounds[it.id]!!)
             } else {
                 mapSoundCategoryToSection[it.category]?.items?.add(it)
             }
         }
         _sections.postValue(mapSoundCategoryToSection.values.toList())
     }*/

    private fun addToSelected(sound: Sound) {
        _selectedSounds.value =
            selectedSounds.value
                ?.toMutableList()
                ?.apply { add(sound) }
                ?.toSet()
                ?.toList()
//                ?.take(Constants.MAX_SELECTABLE_SOUNDS)
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