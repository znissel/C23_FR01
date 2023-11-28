package id.fishku.consumer.fishinformation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.fishku.consumer.core.R
import id.fishku.consumer.model.FishNutrition

class FishNutritionAdapter(private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<FishNutritionAdapter.NutritionViewHolder>() {

    private var fishListNutrition: List<FishNutrition> = emptyList()

    class NutritionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fishImageNutrition: ImageView = itemView.findViewById(R.id.fish_nutrition_image)
        val fishNameNutrition: TextView = itemView.findViewById(R.id.fish_nutrition_name)
    }

    fun setFishNutrition(recipes: List<FishNutrition>) {
        fishListNutrition = recipes
        notifyDataSetChanged()
    }

    fun getRecipeById(recipeId: String): FishNutrition? {
        return fishListNutrition.find { it.id == recipeId }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NutritionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_fish_nutrition, parent, false)
        return NutritionViewHolder(view)
    }

    override fun onBindViewHolder(holder: NutritionViewHolder, position: Int) {
        val fishNutritionList = fishListNutrition[position]

        Glide.with(holder.fishImageNutrition)
            .load(fishNutritionList.photoFishUrl)
            .into(holder.fishImageNutrition)

        holder.fishNameNutrition.text = fishNutritionList.fishName


        holder.itemView.setOnClickListener {
            onItemClick.invoke(fishNutritionList.id)
        }
    }

    override fun getItemCount(): Int {
        return fishListNutrition.size
    }


}