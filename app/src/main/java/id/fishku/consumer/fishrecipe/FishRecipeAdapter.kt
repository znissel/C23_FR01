package id.fishku.consumer.fishrecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.fishku.consumer.core.R
import id.fishku.consumer.model.FishRecipe

class FishRecipeAdapter(private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<FishRecipeAdapter.RecipeViewHolder>() {

    private var recipeList: List<FishRecipe> = emptyList()

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImageView: ImageView = itemView.findViewById(R.id.recipe_image)
        val recipeNameTextView: TextView = itemView.findViewById(R.id.fish_recipe_name)
    }

    fun setRecipes(recipes: List<FishRecipe>) {
        recipeList = recipes
        notifyDataSetChanged()
    }

    fun getRecipeById(recipeId: String): FishRecipe? {
        return recipeList.find { it.id == recipeId }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]

        Glide.with(holder.recipeImageView.context)
            .load(recipe.photoRecipeUrl)
            .into(holder.recipeImageView)

        holder.recipeNameTextView.text = recipe.recipeName

        holder.itemView.setOnClickListener {
            onItemClick.invoke(recipe.id)
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}