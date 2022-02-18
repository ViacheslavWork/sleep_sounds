package white.noise.sounds.baby.sleep.ui.sounds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import white.noise.sounds.baby.sleep.data.Repository

class SoundsViewModel(private val repository: Repository) : ViewModel() {
    private val _sounds = MutableLiveData<List<Section>>()
    val sounds: LiveData<List<Section>> = _sounds

    fun loadAllSounds() {
        viewModelScope.launch(Dispatchers.IO) { _sounds.postValue(repository.getSoundsInSections()) }
    }
}