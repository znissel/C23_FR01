package id.fishku.consumer.fishprice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.fishku.consumer.core.databinding.ItemFishPriceBinding
import id.fishku.consumer.model.FishPricePrediction

class FishPriceAdapter(private val fishPrices: List<FishPricePrediction>) :
    RecyclerView.Adapter<FishPriceAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemFishPriceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fishPrice: FishPricePrediction) {
            binding.apply {
                tvProvence.text = fishPrice.provence
                tvFish1.text = fishPrice.fishType1
                tvFish2.text = fishPrice.fishType2
                tvFish3.text = fishPrice.fishType3
                tvFish4.text = fishPrice.fishType4
                tvFish5.text = fishPrice.fishType5
                tvFish6.text = fishPrice.fishType6
                tvPrice1.text = fishPrice.fishPrice1
                tvPrice2.text = fishPrice.fishPrice2
                tvPrice3.text = fishPrice.fishPrice3
                tvPrice4.text = fishPrice.fishPrice4
                tvPrice5.text = fishPrice.fishPrice5
                tvPrice6.text = fishPrice.fishPrice6
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFishPriceBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fishPrice = fishPrices[position]
        holder.bind(fishPrice)
    }

    override fun getItemCount(): Int = fishPrices.size


}