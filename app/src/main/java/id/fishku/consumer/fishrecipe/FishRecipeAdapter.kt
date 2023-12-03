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
        val recipe = recipeList[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}

//class FishRecipeAdapter(private val onItemClick: (String) -> Unit) :
//    RecyclerView.Adapter<FishRecipeAdapter.RecipeViewHolder>() {
//
//    private var recipeList: List<FishRecipe> = emptyList()
//
//    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val recipeImageView: ImageView = itemView.findViewById(R.id.recipe_image)
//        val recipeNameTextView: TextView = itemView.findViewById(R.id.fish_recipe_name)
//    }
//
//    fun setRecipes(recipes: List<FishRecipe>) {
//        recipeList = recipes
//        notifyDataSetChanged()
//    }
//
//    fun getRecipeById(recipeId: String): FishRecipe? {
//        return recipeList.find { it.id == recipeId }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
//        return RecipeViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
//        val recipe = recipeList[position]
//
//        Glide.with(holder.recipeImageView.context)
//            .load(recipe.photoRecipeUrl)
//            .into(holder.recipeImageView)
//
//        holder.recipeNameTextView.text = recipe.recipeName
//
//        holder.itemView.setOnClickListener {
//            onItemClick.invoke(recipe.id)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return recipeList.size
//    }
//}