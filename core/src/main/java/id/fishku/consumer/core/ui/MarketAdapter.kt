package id.fishku.consumer.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.fishku.consumer.core.databinding.ItemFishMarketGridBinding
import id.fishku.consumer.core.domain.model.Fish
import id.fishku.consumer.core.domain.model.Market
import id.fishku.consumer.core.utils.loadFishImage
import kotlin.reflect.KFunction1

class MarketAdapter(private val onItemClick: KFunction1<Fish, Unit>) :
    ListAdapter<Fish, MarketAdapter.ListViewHolder>(DIFF_CALLBACK) {

    inner class ListViewHolder(private val binding: ItemFishMarketGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(market: Fish) { /*TODO Fish*/
            binding.apply {
                itemTvName.text = market.name
                //itemImgPhoto.loadFishImage(fish.photoUrl.addPhotoUrl())
                itemImgPhoto.loadFishImage(market.photoUrl) /*TODO loadFishImage*/
                itemTvPlace.text = market.location
                root.setOnClickListener {
                    onItemClick(market)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemFishMarketGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val market = getItem(position)
        holder.bind(market)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Fish>() {
            override fun areItemsTheSame(oldItem: Fish, newItem: Fish): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: Fish, newItem: Fish): Boolean =
                oldItem.fishID == newItem.fishID
        }
    }
}