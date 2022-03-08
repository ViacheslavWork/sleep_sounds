package white.noise.sounds.baby.sleep.ui.mixes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import white.noise.sounds.baby.sleep.data.Repository
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.MixCategory

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

    private fun loadMixes(category: MixCategory) {
        _mixes.postValue(repository.getMixes(category = category))
    }

    fun handleEvent(event: MixesEvent) {
        when (event) {
            is MixesEvent.OnCategoryClick -> {}

            is MixesEvent.OnMixClick -> _currentMix.value = event.mix

//            is MixesEvent.OnMixSave -> repository.saveMix(event.mix)
        }
    }

}