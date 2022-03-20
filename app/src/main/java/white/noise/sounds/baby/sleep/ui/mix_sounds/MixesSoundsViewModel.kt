package white.noise.sounds.baby.sleep.ui.mix_sounds

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.data.Repository
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.model.SoundCategory
import white.noise.sounds.baby.sleep.service.PlayerService
import white.noise.sounds.baby.sleep.ui.sounds.Section
import white.noise.sounds.baby.sleep.ui.sounds.SoundsEvent
import white.noise.sounds.baby.sleep.utils.Constants
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
//        updateSections()
    }

    /*
        fun loadMixSounds(mixId: Long) {
            Log.i(TAG, "loadMixSounds: ")
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
                        val soundFromMix =
                            mix.sounds.filter { mixSound -> mixSound.id == it.id }.take(1)[0]
                        mapSoundCategoryToSection[it.category]?.items?.add(soundFromMix)
                    } else {
                        mapSoundCategoryToSection[it.category]?.items?.add(it)
                    }
                }
                _sections.postValue(mapSoundCategoryToSection.values.toList())
            }
        }
    */

    fun loadSounds(mixId: Long): LiveData<List<Sound>> {
        return Transformations.map(repository.getMixLD(mixId)) {
            return@map it.sounds.toList()
        }
    }

    fun loadMixSounds(mixId: Long) {


        viewModelScope.launch {
            val sounds = repository.getSounds().toMutableList()
            val soundsInService = PlayerService.currentSounds
            
            sounds.map { it.id }.forEachIndexed{ index, soundId->
                if (soundsInService.keys.contains(soundId)) {
                    sounds[index] = soundsInService[soundId]!!
                }
            }
            sounds.forEach { Log.i(TAG, "loadMixSounds: $it") }
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
//                updateSections()
            }
            is SoundsEvent.OnSeekBarChanged -> showLog(event.sound.volume.toString())
        }
    }

    fun updateSections() {
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

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}