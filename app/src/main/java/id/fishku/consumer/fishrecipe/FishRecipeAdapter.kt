package id.fishku.consumer.fishrecipe

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.fishku.consumer.core.databinding.ItemRecipeBinding
import id.fishku.consumer.model.FishRecipe

class FishRecipeAdapter(private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<FishRecipeAdapter.RecipeViewHolder>() {

    private var recipeList: List<FishRecipe> = emptyList()
    private var filteredRecipeList: List<FishRecipe> = emptyList()

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: FishRecipe) {
            binding.apply {
                Glide.with(recipeImage.context)
                    .load(recipe.photoRecipeUrl)
                    .into(recipeImage)

                fishRecipeName.text = recipe.recipeName
            }
            binding.root.setOnClickListener {
                onItemClick.invoke(recipe.id)
                Toast.makeText(
                    binding.root.context,
                    "Kamu Memilih Resep " + recipe.recipeName,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun setRecipes(recipes: List<FishRecipe>) {
        recipeList = recipes
        filteredRecipeList = recipes
        notifyDataSetChanged()
    }

    fun filterRecipes(query: String) {
        filteredRecipeList = recipeList.filter { it.recipeName.contains(query, true) }
        notifyDataSetChanged()
    }

    fun getRecipeById(recipeId: String): FishRecipe? {
        return recipeList.find { it.id == recipeId }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = filteredRecipeList[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int {
        return filteredRecipeList.size
    }
}