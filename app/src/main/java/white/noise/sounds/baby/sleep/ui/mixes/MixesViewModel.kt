package white.noise.sounds.baby.sleep.ui.mixes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import white.noise.sounds.baby.sleep.data.Repository
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.MixCategory

class MixesViewModel(private val repository: Repository) : ViewModel() {
    private val _mixes = MutableLiveData<List<Mix>>()
    val mixes: LiveData<List<Mix>> = _mixes

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
        _mixes.postValue(repository.getMixes())
    }

    private fun loadMixes(category: MixCategory) {
        _mixes.postValue(repository.getMixes(category = category))
    }

    fun handleEvent(event: MixesEvent) {
        when (event) {
            is MixesEvent.OnCategoryClick -> {

/*
                _currentCategory.value = categories.value?.get(event.position)?.category
//                markSelectedCategory(category = event.mixCategory.category)
                if (event.mixCategory.category != MixCategory.AllSounds) {
//                    loadMixes(category = event.mixCategory.category)
                    loadMixes(category = categories.value?.get(event.position)?.category!!)
                } else loadAllMixes()
//                loadMixes(category = categories.value?.get(event.position)?.category!!)
*/
            }

            is MixesEvent.OnMixClick -> _currentMix.value = event.mix

            is MixesEvent.OnMixSave -> repository.saveMix(event.mix)
        }
    }

   /* private fun markSelectedCategory(category: MixCategory) {
        _categories.value = _categories.value?.onEach {
            it.isSelected = it.category == category
        }
    }*/

}