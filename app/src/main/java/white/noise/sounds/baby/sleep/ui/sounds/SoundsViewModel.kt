package white.noise.sounds.baby.sleep.ui.sounds

import android.util.Log
import androidx.lifecycle.*
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.data.Repository
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.model.SoundCategory
import white.noise.sounds.baby.sleep.service.PlayerService

private const val TAG = "SoundsViewModel"

class SoundsViewModel(private val repository: Repository) : ViewModel() {
    var sounds: MutableLiveData<List<Sound>> =
        repository.getSounds() as MutableLiveData<List<Sound>>
    var sections: LiveData<List<Section>> = fillSections()

    private val _selectedSounds = MutableLiveData<List<Sound>>(listOf())
    val selectedSounds: LiveData<List<Sound>> = _selectedSounds

    private val selectedSoundsObserver: Observer<Set<Sound>?> = Observer {
        it?.forEach { showLog(it.toString()) }
        it?.let { _selectedSounds.postValue(it.toList()) }
    }

    init {
        PlayerService.currentSoundsLD.observeForever(selectedSoundsObserver)
    }

    private fun fillSections(): LiveData<List<Section>> {
        return Transformations.map(sounds) {
            val categoryMap: MutableMap<SoundCategory, Section> = mutableMapOf()
            enumValues<SoundCategory>().forEach {
                categoryMap[it] = Section(it)
            }
            sounds.value?.forEach {
                if (PlayerService.currentSounds.containsKey(it.id)) {
                    categoryMap[it.category]?.items?.add(PlayerService.currentSounds[it.id]!!)
                } else {
                    categoryMap[it.category]?.items?.add(it)
                }
            }
            return@map categoryMap.values.toList()
        }
    }

    fun handleEvent(event: SoundsEvent) {
        when (event) {
            is SoundsEvent.OnSoundClick -> {}
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