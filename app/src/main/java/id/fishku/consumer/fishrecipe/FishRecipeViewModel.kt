package id.fishku.consumer.fishrecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.fishku.consumer.model.FishRecipe
import id.fishku.consumer.model.FishRecipeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FishRecipeViewModel : ViewModel() {

    private val _fishRecipes = MutableStateFlow<List<FishRecipe>>(emptyList())
    val fishRecipes: StateFlow<List<FishRecipe>> get() = _fishRecipes

    private val allFishRecipes: List<FishRecipe> = FishRecipeData.recipe

    init {
        loadFishRecipes()
    }

    private fun loadFishRecipes() {
        viewModelScope.launch {
            _fishRecipes.value = allFishRecipes
        }
    }

    fun searchFishRecipes(query: String) {
        viewModelScope.launch {
            val result = allFishRecipes.filter { it.recipeName.contains(query, true) }
            _fishRecipes.value = result
        }
    }
}