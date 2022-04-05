package calm.sleep.relaxing.sounds.noise.ui.sounds

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import calm.sleep.relaxing.sounds.noise.data.Repository
import calm.sleep.relaxing.sounds.noise.model.Sound
import calm.sleep.relaxing.sounds.noise.model.SoundCategory
import calm.sleep.relaxing.sounds.noise.service.PlayerService
import calm.sleep.relaxing.sounds.noise.utils.Constants
import calm.sleep.relaxing.sounds.noise.utils.MyLog.showLog
import kotlin.collections.set

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
        showLog("updateSections: ", TAG)
        viewModelScope.launch {
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
            is SoundsEvent.OnSeekBarChanged -> {
                var indexSoundInList = 0
                selectedSounds.value?.forEachIndexed { index, sound ->
                    if (sound.id == event.sound.id) indexSoundInList = index
                }
                val selSounds = selectedSounds.value?.toMutableList()
                selSounds?.set(indexSoundInList, event.sound)
                _selectedSounds.postValue(selSounds!!)
            }
        }
    }

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
}