package white.noise.sounds.baby.sleep.ui.mixes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import white.noise.sounds.baby.sleep.data.Repository
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.MixCategory

private const val TAG = "MixesViewModel"

class MixesViewModel(private val repository: Repository) : ViewModel() {
    private val _mixes = MutableLiveData<List<Mix>>()
    lateinit var mixes: LiveData<List<Mix>>

    private val _categories = MutableLiveData<List<MixCategory>>()
    val categories: LiveData<List<MixCategory>> = _categories

    private val _currentCategory = MutableLiveData<MixCategory>(MixCategory.AllSounds)
    val currentCategory: LiveData<MixCategory> = _currentCategory

    private val _currentMix = MutableLiveData<Mix>()
    val currentMix: LiveData<Mix> = _currentMix

    init {
        loadAllMixes()
        loadAllCategories()
    }

    private fun loadAllCategories() {
        _categories.postValue(MixCategory.values().toList())
    }

    private fun loadAllMixes() {
        mixes = repository.getMixes()
    }

    suspend fun getMix(mixId: Long): Mix {
        return repository.getMix(mixId)
    }

    fun getMixLD(mixId: Long): LiveData<Mix> {
        return repository.getMixLD(mixId)
    }

    private fun loadMixes(category: MixCategory) {
        _mixes.postValue(repository.getMixes(category = category))
    }

    fun handleEvent(event: MixesEvent) {
        when (event) {
            is MixesEvent.OnCategoryClick -> {}

            is MixesEvent.OnMixClick -> _currentMix.value = event.mix

            is MixesEvent.OnDeleteMixClick -> viewModelScope.launch { repository.deleteMix(event.mix.id) }

            is MixesEvent.OnMixSave -> {
                Log.i(TAG, "handleEvent: ${event.mix}")
                viewModelScope.launch { repository.saveMix(event.mix) }
            }
        }
    }

}