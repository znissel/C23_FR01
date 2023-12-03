package id.fishku.consumer.fishinformation

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.fishku.consumer.core.databinding.ItemFishNutritionBinding
import id.fishku.consumer.model.FishNutrition

class FishNutritionAdapter(private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<FishNutritionAdapter.NutritionViewHolder>() {

    private var fishListNutrition: List<FishNutrition> = emptyList()

    inner class NutritionViewHolder(private val binding: ItemFishNutritionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemName: FishNutrition) {
            binding.fishNutritionName.text = itemName.fishName
            Glide.with(binding.root)
                .load(itemName.photoFishUrl)
                .into(binding.fishNutritionImage)
            binding.root.setOnClickListener {
                onItemClick.invoke(itemName.id)
                Toast.makeText(
                    binding.root.context,
                    "Kamu Memilih " + itemName.fishName,
                    Toast.LENGTH_SHORT
                ).show()

                val intentDetail = Intent(binding.root.context, FishInformationActivity::class.java)
                intentDetail.putExtra(FishInformationActivity.ID, itemName.id)
                intentDetail.putExtra(FishInformationActivity.NAME, itemName.fishName)
                intentDetail.putExtra(FishInformationActivity.PICTURE, itemName.photoFishUrl)
                intentDetail.putExtra(FishInformationActivity.PROTEIN, itemName.fishProtein)
                intentDetail.putExtra(FishInformationActivity.KALIUM, itemName.fishKalium)
                intentDetail.putExtra(FishInformationActivity.VITB12, itemName.fishVitB12)
                intentDetail.putExtra(FishInformationActivity.VITB6, itemName.fishVitB6)
                intentDetail.putExtra(FishInformationActivity.ZATBESI, itemName.fishZatBesi)
                intentDetail.putExtra(FishInformationActivity.MAGNESIUM, itemName.fishMagnesium)
                intentDetail.putExtra(FishInformationActivity.FOSFOR, itemName.fishFosfor)
                intentDetail.putExtra(FishInformationActivity.KARBOHIDRAT, itemName.fishKarbohidrat)
                intentDetail.putExtra(FishInformationActivity.LEMAK, itemName.fishLemak)
                intentDetail.putExtra(FishInformationActivity.NATRIUM, itemName.fishNatrium)
                intentDetail.putExtra(FishInformationActivity.KALSIUM, itemName.fishKalsium)
                binding.root.context.startActivity(intentDetail)
            }
        }
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
        val binding = ItemFishNutritionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NutritionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NutritionViewHolder, position: Int) {
        val fishNutritionList = fishListNutrition[position]
        holder.bind(fishNutritionList)
    }

    override fun getItemCount(): Int {
        return fishListNutrition.size
    }
}
