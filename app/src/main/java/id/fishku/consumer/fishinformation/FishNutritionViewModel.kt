package id.fishku.consumer.fishinformation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.fishku.consumer.model.FishNutrition
import id.fishku.consumer.model.FishNutritionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FishNutritionViewModel : ViewModel() {

    private val _fishNutrition = MutableStateFlow<List<FishNutrition>>(emptyList())
    val fishNutrition: StateFlow<List<FishNutrition>> get() = _fishNutrition

    init {
        loadFishRecipes()
    }

    private fun loadFishRecipes() {
        viewModelScope.launch {
            _fishNutrition.value = FishNutritionData.fishNutrition
        }
    }
}